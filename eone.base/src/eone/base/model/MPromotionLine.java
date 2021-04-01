package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MPromotionLine extends X_M_PromotionLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPromotionLine (Properties ctx, int M_PromotionLine_ID, String trxName)
	{
		super (ctx, M_PromotionLine_ID, trxName);
		
	}	//	MAssetUse

	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}


	public MPromotionLine (Properties ctx, ResultSet rs, String trxName)
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
