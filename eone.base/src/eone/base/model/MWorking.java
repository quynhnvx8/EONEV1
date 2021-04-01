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

public class MWorking extends X_HR_Working
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MWorking (Properties ctx, int HR_Working_ID, String trxName)
	{
		super (ctx, HR_Working_ID, trxName);
		
	}	//	MAssetUse

	
	public static MWorking get (Properties ctx, int HR_Employee_ID, int C_Period_ID, String trxName)
	{
		final String whereClause = "HR_Employee_ID=? And C_Period_ID=? AND AD_Client_ID=?";
		MWorking retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(HR_Employee_ID, C_Period_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		String sql = "Select max(StartDate) From HR_Working Where HR_Employee_ID = ? And HR_Working_ID != ? "+
					" And StartDate < (Select Max(StartDate) From HR_Working Where HR_Working_ID = ?)";
		Object [] params = {getHR_Employee_ID(), getHR_Working_ID(), getHR_Working_ID()};
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_IsSelected, isSelected());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_Working_ID, getHR_Working_ID());
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + COLUMNNAME_IsSelected);
			return false;
		}
		
		if (newRecord) {
			sql = "Select max(StartDate) From HR_Working Where HR_Employee_ID = ?";
			params = new Object [] {getHR_Employee_ID()};
		}
		Timestamp startDateOld = DB.getSQLValueTS(get_TrxName(), sql, params);
		if (startDateOld != null) {
			if (startDateOld.compareTo(getStartDate()) > 0 && isSelected()) {
				log.saveError("Error", "StartDate must be great than max StartDate current !");
				return false;
			} 
			
			//Cap nhat EndDate cua ban ghi truoc do
			String sqlUpdate = "Update HR_Working set EndDate = ? Where StartDate = ?";
			params = new Object [] {TimeUtil.getPreviousDay(getStartDate()), startDateOld};
			DB.executeUpdate(sqlUpdate, params, true, get_TrxName());
		}
		
		if (isSelected()) {
			MEmployee em = MEmployee.get(getCtx(), getHR_Employee_ID(), get_TrxName());
			em.setHR_ItemLine_05_ID(getHR_ItemLine_05_ID());
			em.setHR_ItemLine_06_ID(getHR_ItemLine_06_ID());
			em.setAD_Department_ID(getAD_Department_ID());
			em.save();
		}
		
		return true;
	}


	public MWorking (Properties ctx, ResultSet rs, String trxName)
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
