package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MTransferPeriod extends X_C_TransferPeriod implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static MTransferPeriod get (Properties ctx, int C_TransferPeriod_ID, String trxName)
	{
		return (MTransferPeriod)MTable.get(ctx, MTransferPeriod.Table_Name).getPO(C_TransferPeriod_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MTransferPeriod (Properties ctx, int C_TransferPeriod_ID, String trxName)
	{
		super (ctx, C_TransferPeriod_ID,trxName);
		
	}	//	MAsset

	
	public MTransferPeriod (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAsset
	
	
	
	protected boolean beforeSave (boolean newRecord)
	{
		
		
		return true;
	}	//	beforeSave
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(!success)
		{
			return success;
		}
		
		
		
		return true;
	}	//	afterSave
	
	
	protected boolean beforeDelete()
	{
		
		
		return true;
	}       //      beforeDelete
	
	protected MTransferPeriodLine[]	m_lines = null;
	
	public MTransferPeriodLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		List<MTransferPeriodLine> list = new Query(getCtx(), MTransferPeriodLine.Table_Name, "C_TransferPeriod_ID=?", get_TrxName())
		.setParameters(getC_TransferPeriod_ID())
		.list();
		//
		m_lines = new MTransferPeriodLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}
	
	public void setProcessedLine (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		StringBuilder sql = new StringBuilder("UPDATE C_TransferPeriodLine SET Processed='")
			.append((processed ? "Y" : "N"))
			.append("' WHERE C_TransferPeriod_ID =").append(getC_TransferPeriod_ID());
		int noLine = DB.executeUpdate(sql.toString(), get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine("setProcessed - " + processed + " - Lines=" + noLine);
	}
	
	protected String		m_processMsg = null;

	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}



	@Override
	public String completeIt() {
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;

		
		
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Drafted;
		}
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
				
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;		
		return true;
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}


	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}



	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
