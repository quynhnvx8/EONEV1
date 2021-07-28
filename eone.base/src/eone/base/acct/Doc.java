
package eone.base.acct;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import eone.base.model.MPeriod;
import eone.base.model.PO;


public abstract class Doc
{
	
	
	//  Posting Status - AD_Reference_ID=234     //
	/**	Document Status         */
	public static final String 	STATUS_NotPosted        = "N";
	/**	Document Status         */
	public static final String 	STATUS_NotBalanced      = "b";
	/**	Document Status         */
	public static final String 	STATUS_NotConvertible   = "c";
	/**	Document Status         */
	public static final String 	STATUS_PeriodClosed     = "p";
	/**	Document Status         */
	public static final String 	STATUS_InvalidAccount   = "i";
	/**	Document Status         */
	public static final String 	STATUS_PostPrepared     = "y";
	/**	Document Status         */
	public static final String 	STATUS_Posted           = "Y";
	/**	Document Status         */
	public static final String 	STATUS_Error            = "E";
	/** Document Status			*/
	public static final String	STATUS_Deferred			= "d";

	/**	Static Log						*/
	protected static final CLogger	s_log = CLogger.getCLogger(Doc.class);
	/**	Log	per Document				*/
	protected transient CLogger			log = CLogger.getCLogger(getClass());

	private boolean m_manageLocalTrx;


	public Doc (Class<?> clazz, ResultSet rs, String trxName)
	{
		p_Status = STATUS_Error;
		m_ctx = new Properties(Env.getCtx());
		m_ctx.setProperty("#AD_Client_ID", String.valueOf(Env.getAD_Client_ID(getCtx())));

		String className = clazz.getName();
		className = className.substring(className.lastIndexOf('.')+1);
		try
		{
			Constructor<?> constructor = clazz.getConstructor(new Class[]{Properties.class, ResultSet.class, String.class});
			p_po = (PO)constructor.newInstance(new Object[]{m_ctx, rs, trxName});
		}
		catch (Exception e)
		{
			String msg = className + ": " + e.getLocalizedMessage();
			log.severe(msg);
			throw new IllegalArgumentException(msg);
		}
		p_po.load(p_po.get_TrxName());

		
		m_trxName = trxName;
		m_manageLocalTrx = false;
		if (m_trxName == null)
		{
			m_trxName = "Post" + m_DocumentType + p_po.get_ID();
			m_manageLocalTrx = true;
		}
		p_po.set_TrxName(m_trxName);

		
	}   //  Doc

	/** Properties					*/
	private Properties			m_ctx = null;
	/** Transaction Name			*/
	private String				m_trxName = null;
	/** The Document				*/
	protected PO				p_po = null;
	/** Document Type      			*/
	private String				m_DocumentType = null;
	/** Document No      			*/
	private String				m_DocumentNo = null;
	/** Description      			*/
	private String				m_Description = null;
	/** GL Period					*/
	private MPeriod 			m_period = null;
	/** Period ID					*/
	private int					m_C_Period_ID = 0;
	/** Accounting Date				*/
	private Timestamp			m_DateAcct = null;
	
	/** Currency					*/
	private int					m_C_Currency_ID = -1;

	/**	Contained Doc Lines			*/
	protected DocLine[]			p_lines;
	
	protected DocLine[]			p_linesTax;
	
	protected Doc[]				headLines;

	/** Facts                       */
	private ArrayList<Fact>    	m_fact = null;

	/** No Currency in Document Indicator (-1)	*/
	protected static final int  NO_CURRENCY = -2;

	/**	Actual Document Status  */
	protected String			p_Status = null;
	public String getPostStatus() {
		return p_Status;
	}

	/** Error Message			*/
	protected String			p_Error = null;


	/**
	 * 	Get Context
	 *	@return context
	 */
	public Properties getCtx()
	{
		return m_ctx;
	}	//	getCtx

	/**
	 * 	Get Table Name
	 *	@return table name
	 */
	public String get_TableName()
	{
		return p_po.get_TableName();
	}	//	get_TableName

	/**
	 * 	Get Table ID
	 *	@return table id
	 */
	public int get_Table_ID()
	{
		return p_po.get_Table_ID();
		
	}	//	get_Table_ID

	private int AD_Window_ID;
	public int getAD_Window_ID() {
		return AD_Window_ID;
	}
	
	public void setAD_Window_ID(int AD_Window_ID) {
		this.AD_Window_ID = AD_Window_ID;
	}
	/**
	 * 	Get Record_ID
	 *	@return record id
	 */
	public int get_ID()
	{
		return p_po.get_ID();
	}	//	get_ID

	/**
	 * 	Get Persistent Object
	 *	@return po
	 */
	public PO getPO()
	{
		return p_po;
	}	//	getPO

	
	public final String post (boolean force, boolean repost)
	{
		
		p_Error = loadDocumentDetails();
		if (p_Error != null)
			return p_Error;
			
		p_Status = STATUS_NotPosted;
	
		m_fact = new ArrayList<Fact>();

		getPO().setDoc(this);
		try
		{
			p_Status = postLogic ();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			p_Status = STATUS_Error;
			p_Error = e.toString();
		}

		
		p_Status = postCommit (p_Status);

		
		if (!p_Status.equals(STATUS_Posted) && !p_Status.equals(STATUS_Deferred))
		{
			SimpleDateFormat dateFormat = DisplayType.getDateFormat(DisplayType.Date);
			String AD_MessageValue = "PostingError-" + p_Status;
			
			StringBuilder Text = new StringBuilder (Msg.getMsg(Env.getCtx(), AD_MessageValue));
			if (p_Error != null)
				Text.append(p_Error);
			Text.append(" - " + Msg.getMsg(Env.getCtx(),"DocumentNo") + "=").append(getDocumentNo())
			.append(" - " + Msg.getMsg(Env.getCtx(),"DateAcct") + "=").append(dateFormat.format(getDateAcct()))
			;
			
			p_Error = Text.toString();
		}

		//  dispose facts
		for (int i = 0; i < m_fact.size(); i++)
		{
			Fact fact = m_fact.get(i);
			if (fact != null)
				fact.dispose();
		}
		p_lines = null;

		if (p_Status.equals(STATUS_Posted))
			return null;
		return p_Error;
	}   //  post

	
	private final String postLogic ()
	{
		
		//if (!isPeriodOpen())
			//return STATUS_PeriodClosed;

		//  createFacts
		ArrayList<Fact> facts = createFacts ();
		if (facts == null)
			return STATUS_Error;

		
		for (int f = 0; f < facts.size(); f++)
		{
			Fact fact = facts.get(f);
		
			if (fact == null)
				return STATUS_Error;
			m_fact.add(fact);
			//
			p_Status = STATUS_PostPrepared;

			//	check accounts
			
			String error = fact.checkAccounts();
			if (!error.isEmpty()) {
				p_Error = error;
				return STATUS_InvalidAccount;
			}
				
	
		}	//	for all facts

		return STATUS_Posted;
	}   //  postLogic

	
	
	private final String postCommit (String status)
	{
	
		p_Status = status;

		Trx trx = Trx.get(getTrxName(), true);
		try
		{
		//  *** Transaction Start       ***
			//  Commit Facts
			if (status.equals(STATUS_Posted))
			{
				for (int i = 0; i < m_fact.size(); i++)
				{
					Fact fact = m_fact.get(i);
					if (fact == null)
						;
					else if (fact.save(getTrxName()))
						;
					else
					{
						log.log(Level.SEVERE, "(fact not saved) ... rolling back");
						if (m_manageLocalTrx) {
							trx.rollback();
							trx.close();
						}
						return STATUS_Error;
					}
				}
			}


			//	Success
			if (m_manageLocalTrx) {
				trx.commit(true);
				trx.close();
				trx = null;
			}
		//  *** Transaction End         ***
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "... rolling back", e);
			status = STATUS_Error;
			if (m_manageLocalTrx) {
				try {
					if (trx != null)
						trx.rollback();
				} catch (Exception e2) {}
				try {
					if (trx != null)
						trx.close();
					trx = null;
				} catch (Exception e3) {}
			}
		}
		p_Status = status;
		return status;
	}   //  postCommit

	/**
	 * 	Get Trx Name and create Transaction
	 *	@return Trx Name
	 */
	public String getTrxName()
	{
		return m_trxName;
	}	//	getTrxName

	
	public void setPeriod()
	{
		if (m_period != null)
			return;

		//	Period defined in GL Journal (e.g. adjustment period)
		int index = p_po.get_ColumnIndex("C_Period_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				m_period = MPeriod.get(getCtx(), ii.intValue());
		}
		if (m_period == null)
			m_period = MPeriod.get(getCtx(), getDateAcct(), getAD_Org_ID(), m_trxName);
		
	}   //  setC_Period_ID

	/**
	 * 	Get C_Period_ID
	 *	@return period
	 */
	public int getC_Period_ID()
	{
		if (m_period == null)
			setPeriod();
		return m_C_Period_ID;
	}	//	getC_Period_ID
	
	public int getC_Tax_ID()
	{
		int index = p_po.get_ColumnIndex("C_Tax_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}

	/**
	 *	Is Period Open
	 *  @return true if period is open
	 */
	public boolean isPeriodOpen()
	{
		setPeriod();
		boolean open = m_C_Period_ID > 0;
		if (open) {
			if (log.isLoggable(Level.FINE)) log.fine("Yes - " + toString());
		} else {
			log.warning("NO - " + toString());
		}
		return open;
	}	//	isPeriodOpen

	

	
	public DocLine getDocLine (int Record_ID)
	{
		if (p_lines == null || p_lines.length == 0 || Record_ID == 0)
			return null;

		for (int i = 0; i < p_lines.length; i++)
		{
			if (p_lines[i].get_ID() == Record_ID)
				return p_lines[i];
		}
		return null;
	}   //  getDocLine

	/**
	 *  String Representation
	 *  @return String
	 */
	public String toString()
	{
		return p_po.toString();
	}   //  toString


	/**
	 * 	Get AD_Client_ID
	 *	@return client
	 */
	public int getAD_Client_ID()
	{
		return p_po.getAD_Client_ID();
	}	//	getAD_Client_ID

	/**
	 * 	Get AD_Org_ID
	 *	@return org
	 */
	public int getAD_Org_ID()
	{
		return p_po.getAD_Org_ID();
	}	//	getAD_Org_ID

	
	public int getAD_Department_ID()
	{
		return p_po.getAD_Department_ID();
	}
	
	/**
	 * 	Get Document No
	 *	@return document No
	 */
	public String getDocumentNo()
	{
		if (m_DocumentNo != null)
			return m_DocumentNo;
		int index = p_po.get_ColumnIndex("DocumentNo");
		if (index == -1)
			index = p_po.get_ColumnIndex("Name");
		if (index == -1)
			throw new UnsupportedOperationException("No DocumentNo");
		m_DocumentNo = (String)p_po.get_Value(index);
		return m_DocumentNo;
	}	//	getDocumentNo

	/**
	 * 	Get Description
	 *	@return Description
	 */
	public String getDescription()
	{
		if (m_Description == null)
		{
			int index = p_po.get_ColumnIndex("Description");
			if (index != -1)
				m_Description = (String)p_po.get_Value(index);
			else
				m_Description = "";
		}
		return m_Description;
	}	//	getDescription

	/**
	 * 	Get C_Currency_ID
	 *	@return currency
	 */
	public int getC_Currency_ID()
	{
		if (m_C_Currency_ID == -1)
		{
			int index = p_po.get_ColumnIndex("C_Currency_ID");
			if (index != -1)
			{
				Integer ii = (Integer)p_po.get_Value(index);
				if (ii != null)
					m_C_Currency_ID = ii.intValue();
			}
			if (m_C_Currency_ID == -1)
				m_C_Currency_ID = NO_CURRENCY;
		}
		return m_C_Currency_ID;
	}	//	getC_Currency_ID

	/**
	 * 	Set C_Currency_ID
	 *	@param C_Currency_ID id
	 */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		m_C_Currency_ID = C_Currency_ID;
	}	//	setC_Currency_ID

	
	
	public Timestamp getDateAcct()
	{
		if (m_DateAcct != null)
			return m_DateAcct;
		int index = p_po.get_ColumnIndex("DateAcct");
		if (index != -1)
		{
			m_DateAcct = (Timestamp)p_po.get_Value(index);
			if (m_DateAcct != null)
				return m_DateAcct;
		}
		throw new IllegalStateException("No DateAcct");
	}	//	getDateAcct

	/**
	 * 	Set Date Acct
	 *	@param da accounting date
	 */
	public void setDateAcct (Timestamp da)
	{
		m_DateAcct = da;
	}	//	setDateAcct
	

	public int getC_DocType_ID()
	{
		int index = p_po.get_ColumnIndex("C_DocType_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			
			if (ii != null)
				return ii.intValue();
		}
		
		return 0;
	}	//	getC_DocType_ID

	public int getC_DocTypeSub_ID()
	{
		int index = p_po.get_ColumnIndex("C_DocTypeSub_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			
			if (ii != null)
				return ii.intValue();
		}
		
		return 0;
	}

	
	protected abstract String loadDocumentDetails ();

	
	public abstract ArrayList<Fact> createFacts ();

}   //  Doc
