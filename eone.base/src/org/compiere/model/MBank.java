
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;
 
public class MBank extends X_C_Bank implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4286511528899179483L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatement_ID id
	 *	@param trxName transaction
	 */	
	public MBank (Properties ctx, int C_BankStatement_ID, String trxName)
	{
		super (ctx, C_BankStatement_ID, trxName);
		if (C_BankStatement_ID == 0)
		{ 
		
			setPosted (true);	// N
			super.setProcessed (false);
		}
	}	//	MBankStatement

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 *	@param trxName transaction
	 */
	public MBank(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatement

 	
	/**	Lines							*/
	protected MBankLine[] m_lines = null;
	
 	/**
 	 * 	Get Bank Statement Lines
 	 * 	@param requery requery
 	 *	@return line array
 	 */
	/*
 	public MBankLine[] getLines (boolean requery)
 	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		//
		final String whereClause = I_C_BankLine.COLUMNNAME_C_Bank_ID+"=?";
		List<MBankLine> list = new Query(getCtx(),I_C_BankLine.Table_Name,whereClause,get_TrxName())
		.setParameters(getC_Bank_ID())
		.setOrderBy("Line")
		.list();
		MBankLine[] retValue = new MBankLine[list.size()];
		list.toArray(retValue);
		return retValue;
 	}	//	getLines
 	
 	*/

 	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else{
			StringBuilder msgd = new StringBuilder(desc).append(" | ").append(description);
			setDescription(msgd.toString());
		}	
	}	//	addDescription

	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	/*
	public void setProcessedLine (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		StringBuilder sql = new StringBuilder("UPDATE C_BankLine SET Processed='")
			.append((processed ? "Y" : "N"))
			.append("' WHERE C_Bank_ID=").append(getC_Bank_ID());
		int noLine = DB.executeUpdate(sql.toString(), get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine("setProcessed - " + processed + " - Lines=" + noLine);
	}	//	setProcessed

	*/
	
	
	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		StringBuilder msgreturn = new StringBuilder().append(" ").append(getDocumentNo());
		return msgreturn.toString();
	}	//	getDocumentInfo

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
		return true;
	}	//	beforeSave
	
	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}
	
	/**	Process Message 			*/
	protected String m_processMsg = null;
	/**	Just Prepared Flag			*/
	protected boolean m_justPrepared = false;

	
	
	public String completeIt()
	{
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Drafted;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		
		
		if (log.isLoggable(Level.INFO)) log.info("completeIt - " + toString());
		
		
		setProcessed(true);
		//setProcessedLine(true);
		//setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	
	
	
	/** 
	 * 	Re-activate
	 * 	@return false 
	 */
	public boolean reActivateIt()
	{
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}
		
		if (log.isLoggable(Level.INFO)) log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;		
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;		
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
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getUpdatedBy();
	}	//	getDoc_User_ID

	
	
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds);
	}	//	isComplete

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	


}	//	MBankStatement
