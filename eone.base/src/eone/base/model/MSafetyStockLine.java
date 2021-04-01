package eone.base.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MSafetyStockLine extends X_M_SafetyStockLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSafetyStockLine (Properties ctx, int M_SafetyStockLine_ID, String trxName)
	{
		super (ctx, M_SafetyStockLine_ID, trxName);
		
	}	//	MAssetUse

	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (newRecord || is_ValueChanged(COLUMNNAME_C_Period_ID) || is_ValueChanged(COLUMNNAME_M_Product_ID) || is_ValueChanged(COLUMNNAME_Category)) {
			Map<String, Object> dataColumn = new HashMap<String, Object>();
			dataColumn.put(COLUMNNAME_C_Period_ID, getC_Period_ID());
			dataColumn.put(COLUMNNAME_M_Product_ID, getM_Product_ID());
			if (getCategory() != null)
				dataColumn.put(COLUMNNAME_Category, getCategory());
			boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_M_SafetyStockLine_ID, getM_SafetyStockLine_ID());
			if (!check) {
				log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": Month, Product, Category!");
				return false;
			}
			
		}
		return true;
	}


	public MSafetyStockLine (Properties ctx, ResultSet rs, String trxName)
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
