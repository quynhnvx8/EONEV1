
package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;


public class MWarehouse extends X_M_Warehouse
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3065089372599460372L;

	
	public static MWarehouse get (Properties ctx, int M_Warehouse_ID)
	{
		return get(ctx, M_Warehouse_ID, null);
	}
	

	public static MWarehouse get (Properties ctx, int M_Warehouse_ID, String trxName)
	{
		String key = String.valueOf(M_Warehouse_ID);
		MWarehouse retValue = (MWarehouse)s_cache.get(key);
		if (retValue != null)
			return retValue;
		//
		final String whereClause = "M_Warehouse_ID=?";
		retValue = new Query(ctx,I_M_Warehouse.Table_Name,whereClause,trxName)
		.setParameters(M_Warehouse_ID)
		.firstOnly();
		s_cache.put (key, retValue);
		return retValue;
	}	//	get


	public static MWarehouse getDefault (Properties ctx, String trxName)
	{
		int AD_Org_ID = Env.getAD_Org_ID(ctx);
		String key = "Default_" + AD_Org_ID;
		
		MWarehouse retValue = (MWarehouse) s_cache.get(key);
		if (retValue != null) {
			return retValue;
		}
		String whereClause = " IsDefault = 'Y' And AD_Org_ID = ?";
		retValue = new Query(ctx, Table_Name, whereClause, trxName)
				.setParameters(AD_Org_ID)
				.firstOnly();
		s_cache.put(key, retValue);
		return retValue;
	}
	
	public static MWarehouse[] getForOrg (Properties ctx, int AD_Org_ID)
	{
		final String whereClause = "AD_Org_ID=?";
		List<MWarehouse> list = new Query(ctx, Table_Name, whereClause, null)
										.setParameters(AD_Org_ID)
										.setOnlyActiveRecords(true)
										.setOrderBy(COLUMNNAME_M_Warehouse_ID)
										.list();
		return list.toArray(new MWarehouse[list.size()]);
	}	//	get
	

	public static MWarehouse[] getDefaultForOrg (Properties ctx, int AD_Org_ID)
	{
		final String whereClause = "IsDefault=? AND AD_Org_ID=?";
		List<MWarehouse> list = new Query(ctx, Table_Name, whereClause, null)
										.setParameters("Y", AD_Org_ID)
										.setOnlyActiveRecords(true)
										.setOrderBy(COLUMNNAME_M_Warehouse_ID)
										.list();
		return list.toArray(new MWarehouse[list.size()]);
	}	//	get
	
	/**	Cache					*/
	protected static CCache<String,MWarehouse> s_cache = new CCache<String,MWarehouse>(Table_Name, 50 );	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Warehouse_ID id
	 *	@param trxName transaction
	 */
	public MWarehouse (Properties ctx, int M_Warehouse_ID, String trxName)
	{
		super(ctx, M_Warehouse_ID, trxName);
		
	}	//	MWarehouse

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MWarehouse (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWarehouse

	/**
	 * 	Organization Constructor
	 *	@param org parent
	 */
	public MWarehouse (MOrg org)
	{
		this (org.getCtx(), 0, org.get_TrxName());
		setClientOrg(org);
		setValue (org.getValue());
		setName (org.getName());
		
	}	//	MWarehouse

	
	@Override
	protected boolean beforeSave(boolean newRecord) 
	{
		
		if (isDefault()) {
			List<MWarehouse> relValue = new Query(getCtx(), Table_Name, "AD_Org_ID = ? And IsDefault = 'Y' And M_Warehouse_ID != ? and M_Warehouse_ID > 0", get_TrxName())
					.setParameters(getAD_Org_ID(), getM_Warehouse_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", "Warehouse default exists !");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		
		
		return success;
	}	//	afterSave

}	//	MWarehouse
