
package org.compiere.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;


public class MOrgInfo extends X_AD_OrgInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2496591466841600079L;

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *	@return Org Info
	 *  @deprecated
	 */
	public static MOrgInfo get (Properties ctx, int AD_Org_ID)
	{
		return get(ctx, AD_Org_ID, null);
	}	//	get

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *  @param trxName
	 *	@return Org Info
	 */
	public static MOrgInfo get (Properties ctx, int AD_Org_ID, String trxName)
	{
		MOrgInfo retValue = s_cache.get(AD_Org_ID);
		if (retValue != null)
		{
			return retValue;
		}
		retValue = new Query(ctx, Table_Name, "AD_Org_ID=?", trxName)
						.setParameters(AD_Org_ID)
						.firstOnly();
		if (retValue != null)
		{
			s_cache.put(AD_Org_ID, retValue);
		}
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MOrgInfo>	s_cache	= new CCache<Integer, MOrgInfo>(Table_Name, 50);

	
	/**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MOrgInfo (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MOrgInfo

	/**
	 * 	Organization constructor
	 *	@param org org
	 */
	public MOrgInfo (MOrg org)
	{
		super(org.getCtx(), 0, org.get_TrxName());
		setTaxID ("?");
	}	//	MOrgInfo

	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (newRecord) {
			Map<String, Object> dataColumn = new HashMap<String, Object>();
			dataColumn.put(COLUMNNAME_AD_Org_ID, getAD_Org_ID());
			dataColumn.put(COLUMNNAME_IsActive, isActive());
			boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_AD_OrgInfo_ID, getAD_OrgInfo_ID());
			if (!check) {
				log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": OrgInfo");
				return false;
			}
			
		}
		return true;
	}
	
	
}
