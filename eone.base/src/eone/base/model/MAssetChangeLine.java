package eone.base.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;

public class MAssetChangeLine extends X_A_Asset_ChangeLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MAssetChangeLine (Properties ctx, int A_Asset_ChangeLine_ID, String trxName)
	{
		super (ctx, A_Asset_ChangeLine_ID, trxName);
		
	}	//	MAssetUse

	public static Map<Integer, MAssetChangeLine> get(Properties ctx, int C_Period_ID) {
		
		List<MAssetChangeLine> list = new Query(ctx, I_HR_SalaryLine.Table_Name, "C_Period_ID = ? And AD_Client_ID = ?", null)
				.setParameters(C_Period_ID, Env.getAD_Client_ID(ctx))
				.setOnlyActiveRecords(true)
				.list();
				
		MAssetChangeLine [] retValue = new MAssetChangeLine[list.size ()];
		Map<Integer, MAssetChangeLine> map = new HashMap<Integer, MAssetChangeLine>();
		for (int i = 0; i <= retValue.length; i++) {
			map.put(retValue[i].getA_Asset_ChangeLine_ID(), retValue[i]);
		}
		return map;
	}
	
	public static MAssetChangeLine get (Properties ctx, int HR_Employee_ID, int C_Period_ID, String trxName)
	{
		final String whereClause = "HR_Employee_ID=? And C_Period_ID=? AND AD_Client_ID=?";
		MAssetChangeLine retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(HR_Employee_ID, C_Period_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}


	public MAssetChangeLine (Properties ctx, ResultSet rs, String trxName)
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
