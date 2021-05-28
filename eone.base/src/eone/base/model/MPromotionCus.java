package eone.base.model;

import java.sql.ResultSet;
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
