
package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MInvoice extends X_C_Invoice implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3191227310812025813L;


	public static MInvoice[] getOfBPartnerDr (Properties ctx, int C_BPartner_ID, String trxName)
	{
		List<MInvoice> list = new Query(ctx, Table_Name, COLUMNNAME_C_BPartner_Dr_ID+"=?", trxName)
									.setParameters(C_BPartner_ID)
									.list();
		return list.toArray(new MInvoice[list.size()]);
	}	

	
	public static MInvoice get (Properties ctx, int C_Invoice_ID)
	{
		Integer key = Integer.valueOf(C_Invoice_ID);
		MInvoice retValue = (MInvoice) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MInvoice (ctx, C_Invoice_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MInvoice>	s_cache	= new CCache<Integer,MInvoice>(Table_Name, 20, 2);	//	2 minutes


	/**************************************************************************
	 * 	Invoice Constructor
	 * 	@param ctx context
	 * 	@param C_Invoice_ID invoice or 0 for new
	 * 	@param trxName trx name
	 */
	public MInvoice (Properties ctx, int C_Invoice_ID, String trxName)
	{
		super (ctx, C_Invoice_ID, trxName);
		if (C_Invoice_ID == 0)
		{
			setDocStatus (DOCSTATUS_Drafted);		//	Draft
			//

			setDateInvoiced (new Timestamp (System.currentTimeMillis ()));
			setDateAcct (new Timestamp (System.currentTimeMillis ()));
			//
			//
			setPosted(false);
			super.setProcessed (false);
		}
	}	//	MInvoice

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trxName transaction
	 */
	public MInvoice (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoice

	


	protected boolean beforeSave (boolean newRecord)
	{
		log.fine("");
		
		
		return true;
	}	//	beforeSave

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		
		return true;
	}	//	beforeDelete
	
	/**
	 * After Delete
	 * @param success success
	 * @return deleted
	 */
	protected boolean afterDelete(boolean success) {
		// If delete invoice failed then do nothing
		if (!success)
			return success;
		
				
		return true;
	} //afterDelete

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MInvoice[")
			.append(get_ID()).append("-").append(getDocumentNo());
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		StringBuilder msgreturn = new StringBuilder().append(dt.getName()).append(" ").append(getDocumentNo());
		return msgreturn.toString();
	}	//	getDocumentInfo


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;

		
		
		return true;
	}	//	afterSave


	
	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}

	/**	Process Message 			*/
	private String		m_processMsg = null;
	

	
	public String completeIt()
	{
		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}	//	completeIt

	
	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		

		if(!super.reActivate())
			return false;
		
		setProcessed(false);

		return true;
	}	//	reActivateIt


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		if (m_processMsg != null) {
			setProcessed(false);
			
		}
		return m_processMsg;
	}	//	getProcessMsg

	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}
	
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds);
	}	//	isComplete

	

	/** Returns C_DocType_ID (or C_DocTypeTarget_ID if C_DocType_ID is not set) */
	public int getDocTypeID()
	{
		return getC_DocType_ID();
	}


	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}	//	MInvoice
