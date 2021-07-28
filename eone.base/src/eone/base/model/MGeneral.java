package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MGeneral extends X_C_General implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static MGeneral get (Properties ctx, int A_Asset_Build_ID, String trxName)
	{
		return (MGeneral)MTable.get(ctx, MGeneral.Table_Name).getPO(A_Asset_Build_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MGeneral (Properties ctx, int A_Asset_Build_ID, String trxName)
	{
		super (ctx, A_Asset_Build_ID,trxName);
		
	}	//	MAsset

	
	public MGeneral (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAsset
	
	
	protected MGeneral (MProject project)
	{
		this(project.getCtx(), 0, project.get_TrxName());
		setDescription(project.getDescription());
	}
	
	public MGeneral(MInOut mInOut, MInOutLine sLine, int deliveryCount) {
		this(mInOut.getCtx(), 0, mInOut.get_TrxName());
		setDescription(sLine.getDescription());
		
	}

	
	
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
	
	
	
	protected String		m_processMsg = null;

	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}


	@Override
	public String completeIt() {
		
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Drafted;
		}
		
		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}
		
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
