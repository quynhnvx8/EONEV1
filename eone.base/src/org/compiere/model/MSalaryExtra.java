package org.compiere.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MSalaryExtra extends X_HR_SalaryExtra
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSalaryExtra (Properties ctx, int HR_SalaryExtra_ID, String trxName)
	{
		super (ctx, HR_SalaryExtra_ID, trxName);
		
	}	//	MAssetUse

	
	public static MSalaryExtra get (Properties ctx, int HR_SalaryExtra_ID, String trxName)
	{
		return (MSalaryExtra) MTable.get(ctx, Table_ID).getPO(HR_SalaryExtra_ID, trxName);
	}
	
	private static CCache<Integer, List<MSalaryExtra>> s_cache = new CCache<Integer, List<MSalaryExtra>>(Table_Name, 5);
	
	public static List<MSalaryExtra> getAllItem (Properties ctx, String trxName, int AD_Client_ID, int Hr_Employee_ID)
	{
		if (s_cache.containsKey(Hr_Employee_ID))
			return s_cache.get(Hr_Employee_ID);
		final String whereClause = "AD_Client_ID=?";
		List<MSalaryExtra> retValue = new Query(ctx,I_HR_SalaryExtra.Table_Name,whereClause,trxName)
		.setParameters(AD_Client_ID)
		.setOrderBy(COLUMNNAME_HR_Employee_ID)
		.list();
		
		List<MSalaryExtra> item = new ArrayList<MSalaryExtra>();
		int employee_id = 0;
		int employeenext_id = 0;
		for(int i = 0; i < retValue.size(); i++) {
			employee_id = retValue.get(i).getHR_Employee_ID();
			if (employeenext_id == 0) {
				item.clear();				
			} else if (employee_id != employeenext_id) {
				item.clear();
				s_cache.put(employeenext_id, item);
			}
			item.add(retValue.get(i));
			employeenext_id = employee_id;
		}
		
		if (employeenext_id > 0 && item.size() > 0)
			s_cache.put(employeenext_id, item);
		
		return s_cache.get(Hr_Employee_ID);
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_HR_SalaryTable_ID, getHR_SalaryTable_ID());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_SalaryExtra_ID, getHR_SalaryExtra_ID());
		
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + COLUMNNAME_HR_SalaryTable_ID);
			return false;
		}
		
		dataColumn.clear();
		dataColumn.put(COLUMNNAME_HR_SalaryTableLine_ID, getHR_SalaryTableLine_ID());
		check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_SalaryExtra_ID, getHR_SalaryExtra_ID());
		dataColumn = null;
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + COLUMNNAME_HR_SalaryTableLine_ID);
			return false;
		}
		
		return true;
	}


	public MSalaryExtra (Properties ctx, ResultSet rs, String trxName)
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
