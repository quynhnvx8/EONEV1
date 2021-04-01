package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MPermission extends X_AD_Permission
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPermission (Properties ctx, int AD_Permission_ID, String trxName)
	{
		super (ctx, AD_Permission_ID, trxName);
		
	}	//	MAssetUse

	
	public static MPermission get (Properties ctx, int AD_Permission_ID, String trxName)
	{
		return (MPermission) MTable.get(ctx, Table_ID).getPO(AD_Permission_ID, trxName);
	}
	
	
	

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		
		
		return true;
	}


	public MPermission (Properties ctx, ResultSet rs, String trxName)
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
