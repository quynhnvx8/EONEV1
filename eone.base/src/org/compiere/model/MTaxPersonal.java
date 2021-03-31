package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MTaxPersonal extends X_HR_TaxPersonal
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MTaxPersonal (Properties ctx, int HR_TaxPersonal_ID, String trxName)
	{
		super (ctx, HR_TaxPersonal_ID, trxName);
		
	}	//	MAssetUse

	
	public static MTaxPersonal get (Properties ctx, int HR_TaxPersonal_ID, String trxName)
	{
		return (MTaxPersonal) MTable.get(ctx, Table_ID).getPO(HR_TaxPersonal_ID, trxName);
	}
	
	private static CCache<Integer,MTaxPersonal> s_cache = new CCache<Integer,MTaxPersonal>(Table_Name, 5);
	
	public static Map<Integer, MTaxPersonal> getAllItem (Properties ctx, String trxName, int AD_Client_ID)
	{
		Map<Integer, MTaxPersonal> listItems = s_cache;
		if (listItems.size() > 0)
			return listItems;
		final String whereClause = "AD_Client_ID=? And IsActive = 'Y'";
		List<MTaxPersonal> retValue = new Query(ctx,I_HR_TaxPersonal.Table_Name,whereClause,trxName)
		.setParameters(AD_Client_ID)
		.list();
		
		for(int i = 0; i < retValue.size(); i++) {
			listItems.put(retValue.get(i).getHR_TaxPersonal_ID(), retValue.get(i));
			s_cache.put(retValue.get(i).getHR_TaxPersonal_ID(), retValue.get(i));
		}
		return listItems;
	}
	
	public static double getAmountTaxPersonal(BigDecimal BaseAmount, Properties ctx, String trxName, int AD_Client_ID) {
		Map<Integer, MTaxPersonal> items = s_cache;
		if (items.size() <= 0) {
			s_cache = (CCache<Integer, MTaxPersonal>) getAllItem(ctx, trxName, AD_Client_ID);
		}
		for(Map.Entry<Integer, MTaxPersonal> entry : s_cache.entrySet()) {
			if (entry.getValue().getMinValue().compareTo(BaseAmount) <= 0 
				&& entry.getValue().getMaxValue().compareTo(BaseAmount) >= 0) {
				return Env.getValueByFormula(entry.getValue().getFormulaSetup().replaceAll("X", ""+ BaseAmount));
			}
		}
		return 0;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_MinValue, getMinValue());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_TaxPersonal_ID, getHR_TaxPersonal_ID());
		dataColumn = null;
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + COLUMNNAME_MinValue);
			return false;
		}
		
		return true;
	}


	public MTaxPersonal (Properties ctx, ResultSet rs, String trxName)
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
