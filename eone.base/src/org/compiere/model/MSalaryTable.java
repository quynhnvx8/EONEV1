package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class MSalaryTable extends X_HR_SalaryTable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSalaryTable (Properties ctx, int HR_SalaryTable_ID, String trxName)
	{
		super (ctx, HR_SalaryTable_ID, trxName);
		
	}	//	MAssetUse

	
	public static MSalaryTable get (Properties ctx, int HR_SalaryTable_ID)
	{
		return get(ctx,HR_SalaryTable_ID,null);
	}
	
	

	public static MSalaryTable get (Properties ctx, int HR_SalaryTable_ID, String trxName)
	{
		final String whereClause = "HR_SalaryTable_ID=? AND AD_Client_ID=?";
		MSalaryTable retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(HR_SalaryTable_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if (newRecord) {
			
		}
		
		return true;
	}

	protected void updateProcessed(boolean status) {
		String sql = "Update HR_SalaryGroup set Processed = ? Where HR_SalaryTable_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getHR_SalaryTable_ID()}, true, get_TrxName());
	}

	public MSalaryTable (Properties ctx, ResultSet rs, String trxName)
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
