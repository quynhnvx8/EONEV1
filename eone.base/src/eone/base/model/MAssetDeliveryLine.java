package eone.base.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;

public class MAssetDeliveryLine extends X_A_Asset_DeliveryLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MAssetDeliveryLine (Properties ctx, int A_Asset_DeliveryLine_ID, String trxName)
	{
		super (ctx, A_Asset_DeliveryLine_ID, trxName);
		
	}	//	MAssetUse

	public static Map<Integer, MAssetDeliveryLine> get(Properties ctx, int A_Asset_Delivery_ID) {
		
		List<MAssetDeliveryLine> list = new Query(ctx, I_A_Asset_DeliveryLine.Table_Name, "A_Asset_Delivery_ID = ? And AD_Client_ID = ?", null)
				.setParameters(A_Asset_Delivery_ID, Env.getAD_Client_ID(ctx))
				.setOnlyActiveRecords(true)
				.list();
				
		MAssetDeliveryLine [] retValue = new MAssetDeliveryLine[list.size ()];
		Map<Integer, MAssetDeliveryLine> map = new HashMap<Integer, MAssetDeliveryLine>();
		for (int i = 0; i <= retValue.length; i++) {
			map.put(retValue[i].getA_Asset_Delivery_ID(), retValue[i]);
		}
		return map;
	}
	
	public static MAssetDeliveryLine get (Properties ctx, int A_Asset_Delivery_ID, String trxName)
	{
		final String whereClause = "A_Asset_Delivery_ID=?  AND AD_Client_ID=?";
		MAssetDeliveryLine retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(A_Asset_Delivery_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}


	public MAssetDeliveryLine (Properties ctx, ResultSet rs, String trxName)
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
