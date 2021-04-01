
package eone.base.acct;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import eone.base.model.MRefList;
import eone.base.model.ModelValidationEngine;
import eone.base.model.ModelValidator;
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

	/* If the transaction must be managed locally (false if it's managed externally by the caller) */
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

		//	DocStatus
		int index = p_po.get_ColumnIndex("DocStatus");
		if (index != -1)
			m_DocStatus = (String)p_po.get_Value(index);

		
		m_trxName = trxName;
		m_manageLocalTrx = false;
		if (m_trxName == null)
		{
			m_trxName = "Post" + m_DocumentType + p_po.get_ID();
			m_manageLocalTrx = true;
		}
		p_po.set_TrxName(m_trxName);

		//	Amounts
		for(int i = 0; i < m_Amounts.length; i++)
		{
			m_Amounts[i] = Env.ZERO;
		}
	}   //  Doc

	/** Properties					*/
	private Properties			m_ctx = null;
	/** Transaction Name			*/
	private String				m_trxName = null;
	/** The Document				*/
	protected PO				p_po = null;
	/** Document Type      			*/
	private String				m_DocumentType = null;
	/** Document Status      			*/
	private String				m_DocStatus = null;
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
	
	/** Tax Included				*/
	private boolean				m_TaxIncluded = false;
	
	private boolean				m_MultiCurrency = false;
	/** B Partner	    			*/
	private int					m_C_BPartner_ID = -1;

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
		
		if (p_po.getAD_Client_ID() != Env.getAD_Client_ID(getCtx()))
		{
			StringBuilder error = new StringBuilder("AD_Client_ID Conflict - Document=").append(p_po.getAD_Client_ID())
				.append(", Env=").append(Env.getAD_Client_ID(getCtx()));
			log.severe(error.toString());
			return error.toString();
		}

		
		p_Error = loadDocumentDetails();
		if (p_Error != null)
			return p_Error;
			
		p_Status = STATUS_NotPosted;
	
		//  Create Fact per AcctSchema
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

		String validatorMsg = null;
		// Call validator on before post
		if (p_Status.equals(STATUS_Posted)) {
			validatorMsg = ModelValidationEngine.get().fireDocValidate(getPO(), ModelValidator.TIMING_BEFORE_POST);
			if (validatorMsg != null) {
				p_Status = STATUS_Error;
				p_Error = validatorMsg;
			}
		}

		//  commitFact
		p_Status = postCommit (p_Status);

		if (p_Status.equals(STATUS_Posted)) {
			validatorMsg = ModelValidationEngine.get().fireDocValidate(getPO(), ModelValidator.TIMING_AFTER_POST);
			if (validatorMsg != null) {
				p_Status = STATUS_Error;
				p_Error = validatorMsg;
			}
		}

		//  Create Note
		if (!p_Status.equals(STATUS_Posted) && !p_Status.equals(STATUS_Deferred))
		{
			//  Insert Note
			SimpleDateFormat dateFormat = DisplayType.getDateFormat(DisplayType.Date);
			DecimalFormat numberFormat = DisplayType.getNumberFormat(DisplayType.Amount);
			String AD_MessageValue = "PostingError-" + p_Status;
			
			StringBuilder Text = new StringBuilder (Msg.getMsg(Env.getCtx(), AD_MessageValue));
			if (p_Error != null)
				Text.append(" (").append(p_Error).append(")");
			String cn = getClass().getName();
			Text.append(" - ").append(cn.substring(cn.lastIndexOf('.')))
			.append(" - " + Msg.getElement(Env.getCtx(),"DocumentNo") + "=").append(getDocumentNo())
			.append(" - " + Msg.getElement(Env.getCtx(),"DateAcct") + "=").append(dateFormat.format(getDateAcct()))
			.append(" - " + Msg.getMsg(Env.getCtx(),"Amount") + "=").append(numberFormat.format(getAmount()))
			.append(" - " + Msg.getElement(Env.getCtx(),"DocStatus") + "=").append(MRefList.getListName(getCtx(), 131, m_DocStatus))
			.append(" - " + Msg.getMsg(Env.getCtx(),"PeriodOpen") + "=").append(Msg.getMsg(Env.getCtx(), String.valueOf(isPeriodOpen())))
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

		// call modelValidator
		String validatorMsg = ModelValidationEngine.get().fireFactsValidate(facts, getPO());
		if (validatorMsg != null) {
			p_Error = validatorMsg;
			return STATUS_Error;
		}

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
			if (!error.isEmpty())
				return STATUS_InvalidAccount;
	
		}	//	for all facts

		return STATUS_Posted;
	}   //  postLogic

	/**
	 *  Post Commit.
	 *  Save Facts & Document
	 *  @param status status
	 *  @return Posting Status
	 */
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

	/*************************************************************************/

	/**	Amount Type - Invoice - Gross   */
	public static final int 	AMTTYPE_Gross   = 0;
	/**	Amount Type - Invoice - Net   */
	public static final int 	AMTTYPE_Net     = 1;
	/**	Amount Type - Invoice - Charge   */
	public static final int 	AMTTYPE_Charge  = 2;

	/** Source Amounts (may not all be used)	*/
	private BigDecimal[]		m_Amounts = new BigDecimal[4];
	/** Quantity								*/
	private BigDecimal			m_qty = null;

	/**
	 *	Get the Amount
	 *  (loaded in loadDocumentDetails)
	 *
	 *  @param AmtType see AMTTYPE_*
	 *  @return Amount
	 */
	public BigDecimal getAmount(int AmtType)
	{
		if (AmtType < 0 || AmtType >= m_Amounts.length)
			return null;
		return m_Amounts[AmtType];
	}	//	getAmount

	/**
	 *	Set the Amount
	 *  @param AmtType see AMTTYPE_*
	 *  @param amt Amount
	 */
	public void setAmount(int AmtType, BigDecimal amt)
	{
		if (AmtType < 0 || AmtType >= m_Amounts.length)
			return;
		if (amt == null)
			m_Amounts[AmtType] = Env.ZERO;
		else
			m_Amounts[AmtType] = amt;
	}	//	setAmount

	/**
	 *  Get Amount with index 0
	 *  @return Amount (primary document amount)
	 */
	public BigDecimal getAmount()
	{
		return m_Amounts[0];
	}   //  getAmount

	
	public BigDecimal getQty()
	{
		if (m_qty == null)
		{
			int index = p_po.get_ColumnIndex("Qty");
			if (index != -1)
				m_qty = (BigDecimal)p_po.get_Value(index);
			else
				m_qty = Env.ZERO;
		}
		return m_qty;
	}   //  getQty

	
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

	/**
	 * 	Is Multi Currency
	 *	@return mc
	 */
	public boolean isMultiCurrency()
	{
		return m_MultiCurrency;
	}	//	isMultiCurrency

	/**
	 * 	Set Multi Currency
	 *	@param mc multi currency
	 */
	public void setIsMultiCurrency (boolean mc)
	{
		m_MultiCurrency = mc;
	}	//	setIsMultiCurrency

	/**
	 * 	Is Tax Included
	 *	@return tax incl
	 */
	public boolean isTaxIncluded()
	{
		return m_TaxIncluded;
	}	//	isTaxIncluded

	/**
	 * 	Set Tax Included
	 *	@param ti Tax Included
	 */
	public void setIsTaxIncluded (boolean ti)
	{
		m_TaxIncluded = ti;
	}	//	setIsTaxIncluded

	
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
	
	public void setDocumentNo(String value)
    {
		m_DocumentNo = value;
    }
	
	
	
	/**
	 * 	Is Document Posted
	 *	@return true if posted
	 */
	public boolean isPosted()
	{
		int index = p_po.get_ColumnIndex("Posted");
		if (index != -1)
		{
			Object posted = p_po.get_Value(index);
			if (posted instanceof Boolean)
				return ((Boolean)posted).booleanValue();
			if (posted instanceof String)
				return "Y".equals(posted);
		}
		throw new IllegalStateException("No Posted");
	}	//	isPosted

	
	/**
	 * 	Get C_DocType_ID
	 *	@return DocType
	 */
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

	/**
	 * 	Get M_Warehouse_ID
	 *	@return Warehouse
	 */
	public int getM_Warehouse_ID()
	{
		int index = p_po.get_ColumnIndex("M_Warehouse_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}	//	getM_Warehouse_ID


	/**
	 * 	Get C_BPartner_ID
	 *	@return BPartner
	 */
	public int getC_BPartner_ID()
	{
		if (m_C_BPartner_ID == -1)
		{
			int index = p_po.get_ColumnIndex("C_BPartner_ID");
			if (index != -1)
			{
				Integer ii = (Integer)p_po.get_Value(index);
				if (ii != null)
					m_C_BPartner_ID = ii.intValue();
			}
			if (m_C_BPartner_ID == -1)
				m_C_BPartner_ID = 0;
		}
		return m_C_BPartner_ID;
	}	//	getC_BPartner_ID

	/**
	 * 	Set C_BPartner_ID
	 *	@param C_BPartner_ID bp
	 */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		m_C_BPartner_ID = C_BPartner_ID;
	}	//	setC_BPartner_ID

	

	/**
	 * 	Get C_Project_ID
	 *	@return Project
	 */
	public int getC_Project_ID()
	{
		int index = p_po.get_ColumnIndex("C_Project_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}	//	getC_Project_ID

	/**
	 * 	Get C_ProjectPhase_ID
	 *	@return Project Phase
	 */
	public int getC_ProjectPhase_ID()
	{
		int index = p_po.get_ColumnIndex("C_ProjectPhase_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}	//	getC_ProjectPhase_ID

	

	/**
	 * 	Get M_Product_ID
	 *	@return Product
	 */
	public int getM_Product_ID()
	{
		int index = p_po.get_ColumnIndex("M_Product_ID");
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}	//	getM_Product_ID

	

        	/**
	 * 	Get User Defined value
	 *	@return User defined
	 */
	public int getValue (String ColumnName)
	{
		int index = p_po.get_ColumnIndex(ColumnName);
		if (index != -1)
		{
			Integer ii = (Integer)p_po.get_Value(index);
			if (ii != null)
				return ii.intValue();
		}
		return 0;
	}	//	getValue

	protected abstract String loadDocumentDetails ();

	
	public abstract ArrayList<Fact> createFacts ();

}   //  Doc
