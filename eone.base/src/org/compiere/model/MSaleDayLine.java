package org.compiere.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MSaleDayLine extends X_C_SaleDayLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSaleDayLine (Properties ctx, int M_PromotionLine_ID, String trxName)
	{
		super (ctx, M_PromotionLine_ID, trxName);
		
	}	//	MAssetUse

	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (newRecord || is_ValueChanged(COLUMNNAME_C_Period_ID)) {
			Map<String, Object> dataColumn = new HashMap<String, Object>();
			dataColumn.put(COLUMNNAME_C_Period_ID, getC_Period_ID());
			boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_C_SaleDayLine_ID, getC_SaleDayLine_ID());
			if (!check) {
				log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": Month!");
				return false;
			}
			
		}
		return true;
	}


	public MSaleDayLine (Properties ctx, ResultSet rs, String trxName)
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
