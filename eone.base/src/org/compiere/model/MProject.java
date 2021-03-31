
package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Msg;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MProject extends X_C_Project implements DocAction
{
	
	private static final long serialVersionUID = 8631795136761641303L;

	
	public MProject (Properties ctx, int C_Project_ID, String trxName)
	{
		super (ctx, C_Project_ID, trxName);
		
	}	//	MProject

	public MProject (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProject



	public String toString()
	{
		StringBuilder sb = new StringBuilder ("MProject[").append(get_ID())
			.append("]");
		return sb.toString();
	}	//	toString

	
	public MProjectLine[] getLines()
	{
		final String whereClause = "C_Project_ID=?";
		List <MProjectLine> list = new Query(getCtx(), I_C_ProjectLine.Table_Name, whereClause, get_TrxName())
			.setParameters(getC_Project_ID())
			.setOrderBy("Line")
			.list();
		//
		MProjectLine[] retValue = new MProjectLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("Value") || isActive()) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "C_Project_ID != ? And (Value = ? or Name=?) And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getC_Project_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", Msg.getMsg(getCtx(), "ValueOrNameExists"));//ValueExists, NameExists
				return false;
			}

		}
		
		return true;
	}	//	beforeSave
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
				
		return success;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		return success;
	}	//	afterDelete
	
	
	//Implements DocAction
	protected void updateProcessed(boolean status) {
		String sql = "Update C_ProjectLine set Processed = ? Where C_Project_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getC_Project_ID()}, true, get_TrxName());
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
		updateProcessed(true);
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
		updateProcessed(false);
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
		
		return "";
	}

	@Override
	public void setDocStatus(String newStatus) {
		//this.setDocStatus(newStatus);		
	}

	@Override
	public String getDocStatus() {
		return getDocStatus();
	}

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}	//	MProject
