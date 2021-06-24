package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

public class MPromotionCus extends X_M_PromotionCus
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPromotionCus (Properties ctx, int M_PromotionCus_ID, String trxName)
	{
		super (ctx, M_PromotionCus_ID, trxName);
		
	}	//	MAssetUse

	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if (newRecord || is_ValueChanged(COLUMNNAME_C_BP_Group_ID)) {
			List<MPromotionCus> relValue = new Query(getCtx(), Table_Name, "C_Promotion_ID = ? And C_BP_Group_ID != ? And IsActive = 'Y' And C_BP_Group_ID > 0", get_TrxName())
					.setParameters(getM_Promotion_ID(), getC_BP_Group_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", "Group exists");
				return false;
			}
		}
		return true;
	}


	public MPromotionCus (Properties ctx, ResultSet rs, String trxName)
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
