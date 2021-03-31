package org.compiere.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MTimekeeper extends X_HR_Timekeeper implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MTimekeeper (Properties ctx, int HR_Timekeeper_ID, String trxName)
	{
		super (ctx, HR_Timekeeper_ID, trxName);
		
	}	//	MAssetUse

	
	public static MTimekeeper get (Properties ctx, int HR_Timekeeper_ID)
	{
		return get(ctx,HR_Timekeeper_ID,null);
	}
	
	

	public static MTimekeeper get (Properties ctx, int HR_Timekeeper_ID, String trxName)
	{
		return (MTimekeeper)MTable.get(ctx, Table_ID).getPO(HR_Timekeeper_ID, trxName);
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if (newRecord || is_ValueChanged("AD_Department_ID") || is_ValueChanged("C_Period_ID")) {
			String sql = "Select count(1) From HR_Timekeeper Where AD_Department_ID = ? And C_Period_ID = ? And HR_Timekeeper_ID != ?";
			List<Object> params = new ArrayList<Object>();
			params.add(getAD_Department_ID());
			params.add(getC_Period_ID());
			params.add(getHR_Timekeeper_ID());
			int no = DB.getSQLValue(get_TrxName(), sql, params);
			if (no > 0) {
				log.saveError("Error", "Department exists this month");
				return false;
			}
		}
		
		return true;
	}

	protected void updateProcessed(boolean status) {
		String sql = "Update HR_TimekeeperLine set Processed = ? Where HR_Timekeeper_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getHR_Timekeeper_ID()}, true, get_TrxName());
	}

	public MTimekeeper (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAssetUse


	protected boolean afterSave (boolean newRecord,boolean success)
	{
		log.info ("afterSave");
		if (!success)
			return success;
		if (is_ValueChanged("C_Period_ID")) {
			String sql = "Update HR_TimekeeperLine set C_Period_ID = ? Where HR_Timekeeper_ID = ?";
			DB.executeUpdate(sql, new Object [] {getC_Period_ID(), getHR_Timekeeper_ID()}, true, get_TrxName());
		}
		
		return success;
		 
		
	}	//	afterSave
	

	//Implements DocAction
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
	public int getAD_Window_ID() {
		
		return 0;
	}
	
	private static CCache<Integer,MTimekeeperLine []> s_cache = new CCache<Integer,MTimekeeperLine []>(Table_Name, 10);
	public MTimekeeperLine [] getLine(int HR_Timekeeper_ID) {
		if (s_cache.containsKey(HR_Timekeeper_ID))
			return s_cache.get(HR_Timekeeper_ID);
		String whereClause = "HR_Timekeeper_ID = ?  And Processed = 'N'";
		List<MTimekeeperLine> list = new Query(getCtx(), X_HR_TimekeeperLine.Table_Name, whereClause, get_TrxName())
				.setParameters(HR_Timekeeper_ID)
				.list();
		MTimekeeperLine [] retValue = new MTimekeeperLine [list.size()];
		list.toArray(retValue);
		s_cache.put(HR_Timekeeper_ID, retValue);
		return retValue;
	}
	
}
