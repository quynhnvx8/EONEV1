package eone.base.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MSalary extends X_HR_Salary implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSalary (Properties ctx, int HR_Salary_ID, String trxName)
	{
		super (ctx, HR_Salary_ID, trxName);
		
	}	//	MAssetUse

	
	public static MSalary get (Properties ctx, int HR_Salary_ID)
	{
		return get(ctx,HR_Salary_ID,null);
	}
	
	

	public static MSalary get (Properties ctx, int HR_Salary_ID, String trxName)
	{
		final String whereClause = "HR_Salary_ID=? AND AD_Client_ID=?";
		MSalary retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(HR_Salary_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}
	
	
	
	public Map<Integer,MSalaryLine> getAllEmployee (Properties ctx, String trxName)
	{
		final String whereClause = "HR_Salary_ID=? AND AD_Client_ID=?";
		List<MSalaryLine> retValue = new Query(ctx,I_HR_SalaryLine.Table_Name,whereClause,trxName)
				.setParameters(getHR_Salary_ID(), getAD_Client_ID())
				.list();
		Map<Integer, MSalaryLine> listItems = new HashMap<Integer, MSalaryLine>();
		for(int i = 0; i < retValue.size(); i++) {
			listItems.put(retValue.get(i).getHR_Employee_ID(), retValue.get(i));
		}
		return listItems;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if (newRecord || is_ValueChanged("C_Period_ID")) {
			String sql = "Select count(1) From HR_Salary Where C_Period_ID = ? And HR_Salary_ID != ?";
			List<Object> params = new ArrayList<Object>();
			params.add(getC_Period_ID());
			params.add(getHR_Salary_ID());
			int no = DB.getSQLValue(get_TrxName(), sql, params);
			if (no > 0) {
				log.saveError("Error", "Salary this month exists!");
				return false;
			}
		}
		
		return true;
	}

	protected void updateProcessed(boolean status) {
		String sql = "Update HR_SalaryLine set Processed = ? Where HR_Salary_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getHR_Salary_ID()}, true, get_TrxName());
	}

	public MSalary (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAssetUse


	protected boolean afterSave (boolean newRecord,boolean success)
	{
		log.info ("afterSave");
		if (!success)
			return success;
		
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
		
		setProcessed(true);
		updateProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		updateProcessed(false);
			
		return true;
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}
	
	@Override
	public String getSummary() {
		
		return "";
	}


	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
