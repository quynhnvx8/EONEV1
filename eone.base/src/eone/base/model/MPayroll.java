package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MPayroll extends X_HR_Payroll
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPayroll (Properties ctx, int HR_Working_ID, String trxName)
	{
		super (ctx, HR_Working_ID, trxName);
		
	}	//	MAssetUse

	
	public static MPayroll get (Properties ctx, int HR_Payroll_ID, String trxName)
	{
		return (MPayroll) MTable.get(ctx, Table_ID).getPO(HR_Payroll_ID, trxName);
	}
	
	public static Map<Integer, MPayroll> getAllItem (Properties ctx, String trxName, int AD_Client_ID)
	{
		final String whereClause = "AD_Client_ID=? And IsSelected = 'Y'";
		List<MPayroll> retValue = new Query(ctx,I_HR_Payroll.Table_Name,whereClause,trxName)
		.setParameters(AD_Client_ID)
		.list();
		Map<Integer, MPayroll> listItems = new HashMap<Integer, MPayroll>();
		for(int i = 0; i < retValue.size(); i++) {
			listItems.put(retValue.get(i).getHR_Employee_ID(), retValue.get(i));
		}
		return listItems;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		String sql = "Select max(DateStart) From HR_Payroll Where HR_Employee_ID = ? And HR_Payroll_ID != ? "+
				" And DateStart <= (Select Max(DateStart) From HR_Payroll Where HR_Payroll_ID = ?)";
		Object [] params = {getHR_Employee_ID(), getHR_Payroll_ID(), getHR_Payroll_ID()};
		
		
		if (newRecord) {
			sql = "Select max(DateStart) From HR_Payroll Where HR_Employee_ID = ?";
			params = new Object [] {getHR_Employee_ID()};
		}
		Timestamp startDateOld = DB.getSQLValueTS(get_TrxName(), sql, params);
		if (startDateOld != null) {
			if (startDateOld.compareTo(getDateStart()) >= 0) {
				log.saveError("Error", "StartDate must be great than max StartDate current !");
				return false;
			} 
		}
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_IsSelected, true);
		dataColumn.put(COLUMNNAME_HR_Employee_ID, getHR_Employee_ID());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_Payroll_ID, getHR_Payroll_ID());
		dataColumn = null;
		if (!check && isSelected()) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " +  COLUMNNAME_IsSelected);
			return false;
		}
		
		MSalaryTableLine line = MSalaryTableLine.get(getCtx(), getHR_SalaryTableLine_ID(), get_TrxName());
		setSalaryRate(line.getPercent());
		
		return true;
	}


	public MPayroll (Properties ctx, ResultSet rs, String trxName)
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
