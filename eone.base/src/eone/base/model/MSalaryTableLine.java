package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MSalaryTableLine extends X_HR_SalaryTableLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSalaryTableLine (Properties ctx, int HR_SalaryTableLine_ID, String trxName)
	{
		super (ctx, HR_SalaryTableLine_ID, trxName);
		
	}	//	MAssetUse

	
	
	public static MSalaryTableLine get (Properties ctx, int HR_SalaryTableLine_ID, String trxName)
	{
		return (MSalaryTableLine)MTable.get(ctx, Table_Name).getPO(HR_SalaryTableLine_ID, trxName);
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}


	public MSalaryTableLine (Properties ctx, ResultSet rs, String trxName)
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
