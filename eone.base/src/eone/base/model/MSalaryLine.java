package eone.base.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;

public class MSalaryLine extends X_HR_SalaryLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSalaryLine (Properties ctx, int HR_SalaryLine_ID, String trxName)
	{
		super (ctx, HR_SalaryLine_ID, trxName);
		
	}	//	MAssetUse

	public static Map<Integer, MSalaryLine> get(Properties ctx, int C_Period_ID, int AD_Client_ID) {
		
		List<MSalaryLine> list = new Query(ctx, I_HR_SalaryLine.Table_Name, "C_Period_ID = ? And AD_Client_ID = ?", null)
				.setParameters(C_Period_ID, AD_Client_ID)
				.setOnlyActiveRecords(true)
				.list();
				
		MSalaryLine [] retValue = new MSalaryLine[list.size ()];
		list.toArray(retValue);
		Map<Integer, MSalaryLine> map = new HashMap<Integer, MSalaryLine>();
		for (int i = 0; i < retValue.length; i++) {
			map.put(retValue[i].getHR_Employee_ID(), retValue[i]);
		}
		return map;
	}
	
	public static MSalaryLine get (Properties ctx, int HR_Employee_ID, int C_Period_ID, String trxName)
	{
		final String whereClause = "HR_Employee_ID=? And C_Period_ID=? AND AD_Client_ID=?";
		MSalaryLine retValue = new Query(ctx,I_HR_SalaryLine.Table_Name,whereClause,trxName)
		.setParameters(HR_Employee_ID, C_Period_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}


	public MSalaryLine (Properties ctx, ResultSet rs, String trxName)
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
