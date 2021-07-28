package eone.base.model;

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
		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
			
		return true;
	}


	@Override
	public String getProcessMsg() {
		if (m_processMsg != null) {
			setProcessed(false);
			
		}
		return m_processMsg;
	}

	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
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
