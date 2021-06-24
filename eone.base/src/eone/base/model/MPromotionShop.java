package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

public class MPromotionShop extends X_M_PromotionShop
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPromotionShop (Properties ctx, int M_PromotionShop_ID, String trxName)
	{
		super (ctx, M_PromotionShop_ID, trxName);
		
	}	//	MAssetUse

	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (newRecord || is_ValueChanged(COLUMNNAME_AD_Department_ID)) {
			List<MPromotionShop> relValue = new Query(getCtx(), Table_Name, "C_Promotion_ID = ? And AD_Department_ID != ? And IsActive = 'Y' And AD_Department_ID > 0", get_TrxName())
					.setParameters(getM_Promotion_ID(), getAD_Department_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", "Shop exists");
				return false;
			}
		}
		return true;
	}


	public MPromotionShop (Properties ctx, ResultSet rs, String trxName)
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
