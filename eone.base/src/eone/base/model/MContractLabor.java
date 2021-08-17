package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

public class MContractLabor extends X_HR_ContractLabor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MContractLabor (Properties ctx, int HR_Working_ID, String trxName)
	{
		super (ctx, HR_Working_ID, trxName);
		
	}	//	MAssetUse

	
	public static MContractLabor get (Properties ctx, int HR_Employee_ID, int C_Period_ID, String trxName)
	{
		final String whereClause = "HR_Employee_ID=? And C_Period_ID=? AND AD_Client_ID=?";
		MContractLabor retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(HR_Employee_ID, C_Period_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		String sql = "Select max(StartTime) From HR_ContractLabor Where HR_Employee_ID = ? And HR_ContractLabor_ID != ? "+
					" And StartTime <= (Select Max(StartTime) From HR_ContractLabor Where HR_ContractLabor_ID = ?)";
		Object [] params = {getHR_Employee_ID(), getHR_ContractLabor_ID(), getHR_ContractLabor_ID()};
		
		
		if (newRecord) {
			sql = "Select max(StartTime) From HR_ContractLabor Where HR_Employee_ID = ?";
			params = new Object [] {getHR_Employee_ID()};
		}
		Timestamp startDateOld = DB.getSQLValueTS(get_TrxName(), sql, params);
		if (startDateOld != null) {
			if (startDateOld.compareTo(getStartTime()) >= 0 && isSelected()) {
				log.saveError("Error", "StartTime must be great than max StartTime current !");
				return false;
			} 
			
			//Cap nhat EndDate cua ban ghi truoc do
			String sqlUpdate = "Update HR_ContractLabor set EndTime = ? Where StartTime = ?";
			params = new Object [] {TimeUtil.getPreviousDay(getStartTime()), startDateOld};
			DB.executeUpdate(sqlUpdate, params, true, get_TrxName());
		}
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_IsSelected, true);
		dataColumn.put(COLUMNNAME_HR_Employee_ID, getHR_Employee_ID());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_ContractLabor_ID, getHR_ContractLabor_ID());
		if (!check && isSelected()) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + COLUMNNAME_IsSelected);
			return false;
		}
		
		if (newRecord) {
			setEndTime(null);
		}
		
		return true;
	}


	public MContractLabor (Properties ctx, ResultSet rs, String trxName)
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



}
