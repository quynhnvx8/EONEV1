package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;

public class MPromotionType extends X_M_PromotionType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPromotionType (Properties ctx, int M_PromotionType_ID, String trxName)
	{
		super (ctx, M_PromotionType_ID, trxName);
		
	}	//	MAssetUse

	static private CCache<Integer, MPromotionType> s_cache = new CCache<Integer, MPromotionType>(Table_Name, 30, 60);
	
	public static MPromotionType getById(Properties ctx, int M_PromotionType_ID, String trxName) {
		Integer key = Integer.valueOf(M_PromotionType_ID);
		MPromotionType promotionType = s_cache.get(key);
		String whereClause = "M_PromotionType_ID = ?";
		if (promotionType == null) {
			promotionType = new Query(ctx, Table_Name, whereClause, trxName)
					.setParameters(M_PromotionType_ID)
					.firstOnly();					
		}
		return promotionType;
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}


	public MPromotionType (Properties ctx, ResultSet rs, String trxName)
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
