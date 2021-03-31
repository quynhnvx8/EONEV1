package org.compiere.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MConfig extends X_HR_Config
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MConfig (Properties ctx, int HR_Config_ID, String trxName)
	{
		super (ctx, HR_Config_ID, trxName);
		
	}	//	MAssetUse

	
	public static MConfig get (Properties ctx, int HR_Config_ID, String trxName)
	{
		return (MConfig) MTable.get(ctx, Table_ID).getPO(HR_Config_ID, trxName);
	}
	
	public static Map<String, MConfig> getAllItem (Properties ctx, String trxName, int AD_Client_ID)
	{
		final String whereClause = "AD_Client_ID=? And IsActive = 'Y'";
		List<MConfig> retValue = new Query(ctx,I_HR_Config.Table_Name,whereClause,trxName)
		.setParameters(AD_Client_ID)
		.list();
		Map<String, MConfig> listItems = new HashMap<String, MConfig>();
		for(int i = 0; i < retValue.size(); i++) {
			listItems.put(retValue.get(i).getName(), retValue.get(i));
		}
		return listItems;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_Name, getName());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_Config_ID, getHR_Config_ID());
		dataColumn = null;
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + COLUMNNAME_HR_Config_ID);
			return false;
		}
		
		return true;
	}


	public MConfig (Properties ctx, ResultSet rs, String trxName)
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
