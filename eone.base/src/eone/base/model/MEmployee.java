package eone.base.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MEmployee extends X_HR_Employee implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int CHANGETYPE_setAssetGroup = Table_ID * 100 + 1;
	
	
	public static MEmployee get (Properties ctx, int HR_Employee_ID, String trxName)
	{
		return (MEmployee)MTable.get(ctx, MEmployee.Table_Name).getPO(HR_Employee_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MEmployee (Properties ctx, int HR_Employee_ID, String trxName)
	{
		super (ctx, HR_Employee_ID,trxName);
		
	}	//	MAsset

	
	public MEmployee (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAsset
	
	//Sinh ma nhan vien tu dong voi do dai la 6 chu so.
	private String createCode() {
		String sql = "Select max(Code) from hr_employee where AD_Client_ID = ? And AD_Org_ID = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(getAD_Client_ID());
		params.add(getAD_Org_ID());
		String code = "000001";
		int value = DB.getSQLValue(get_TrxName(), sql, params);
		if (value <= 0) {
			return code;
		}
		
		return Env.numToChar(++value, 6);
	}
	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord)
			setCode(createCode());
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
		m_processMsg = null;
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		setProcessed(true);
		setProcessedLine(true);
		return DocAction.STATUS_Completed;
	}
	
	

	
	public void setProcessedLine (boolean processed)
	{
		if (get_ID() == 0)
			return;
		StringBuilder sql = new StringBuilder("UPDATE HR_FamilyTies SET Processed='").append((processed ? "Y" : "N"))
			.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("UPDATE HR_ContractLabor SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("UPDATE HR_Working SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("UPDATE HR_Education SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		//
		sql = new StringBuilder("UPDATE HR_Reward SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		//
		sql = new StringBuilder("UPDATE HR_Discipline SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("UPDATE HR_JobQuit SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("UPDATE HR_Payroll SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("UPDATE HR_Literacy SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("UPDATE HR_Insurance SET Processed='").append((processed ? "Y" : "N"))
				.append("' WHERE HR_Employee_ID =").append(getHR_Employee_ID());
		DB.executeUpdate(sql.toString(), get_TrxName());
		
	}


	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		setProcessedLine(false);
		
		return true;
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		return sb.toString();
	}



	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
