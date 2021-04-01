package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MDayOffLine extends X_HR_DayOffLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MDayOffLine (Properties ctx, int HR_DayOffLine_ID, String trxName)
	{
		super (ctx, HR_DayOffLine_ID, trxName);
		
	}	//	MAssetUse

	
	
	public static MDayOffLine get (Properties ctx, int HR_DayOffLine_ID, String trxName)
	{
		return (MDayOffLine)MTable.get(ctx, Table_Name).getPO(HR_DayOffLine_ID, trxName);
		
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}


	public MDayOffLine (Properties ctx, ResultSet rs, String trxName)
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
