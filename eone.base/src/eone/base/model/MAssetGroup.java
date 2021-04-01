package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 * Asset Group Model
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 */
public class MAssetGroup extends X_A_Asset_Group
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -173157506404569463L;
	/** Cache: ID -> MAssetGroup */
	private static CCache<Integer, MAssetGroup> s_cache = new CCache<Integer, MAssetGroup>(Table_Name, 10, 0);
	
	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param A_Asset_Group_ID
	 */
	public MAssetGroup (Properties ctx, int A_Asset_Group_ID, String trxName)
	{
		super (ctx,A_Asset_Group_ID, trxName);
	}	//	MAssetGroup
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAssetGroup (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs,  trxName);
	}	//	MAssetGroup
	
	/**
	 * Get Asset Group [CACHE]
	 * @param ctx context
	 * @param A_Asset_Group_ID	asset group id
	 * @return asset group or null
	 */
	public static MAssetGroup get(Properties ctx, int A_Asset_Group_ID)
	{
		if (A_Asset_Group_ID <= 0)
			return null;
		// Try cache
		MAssetGroup ag = s_cache.get(A_Asset_Group_ID);
		if (ag != null)
			return ag;
		// Load
		ag = new MAssetGroup(ctx, A_Asset_Group_ID, null);
		if (ag != null && ag.get_ID() != A_Asset_Group_ID)
			ag = null;
		else
			s_cache.put(A_Asset_Group_ID, ag);
		//
		return ag;
	}
	
	/**
	 * Get default asset group ID for given model.
	 * WARNING: trxName = null.
	 * @param m	reference model (used to get AD_Client_ID)
	 * @return default asset group ID or 0 if not found
	 */
	public static int getDefault_ID(SetGetModel m)
	{
		int AD_Client_ID = SetGetUtil.get_AttrValueAsInt(m, "AD_Client_ID");
		
		final String sql = "SELECT "+COLUMNNAME_A_Asset_Group_ID
				+ " FROM "+Table_Name
				+ " WHERE AD_Client_ID=?"
				+ " ORDER BY Value DESC" // default first, older first
		;
		int id = DB.getSQLValueEx(null, sql, AD_Client_ID);
		
		return id;
	}
	
	/**
	 * Update Asset
	 * - updates asset M_AssetGroup_ID if is null
	 */
	public static void updateAsset(SetGetModel m, int A_Asset_Group_ID)
	{
		
		if (A_Asset_Group_ID < 0) {
			A_Asset_Group_ID = MAssetGroup.getDefault_ID(SetGetUtil.wrap(m));
			m.set_AttrValue(MAsset.COLUMNNAME_A_Asset_Group_ID, A_Asset_Group_ID);
		}
		
		//end modify by @win
	}

	
	protected boolean beforeSave (boolean newRecord)
	{

		if (newRecord || is_ValueChanged("Value") || isActive() || is_ValueChanged("Name")) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "A_Asset_Group_ID != ? And (Value = ? Or Name = ?) And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getA_Asset_Group_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", Msg.getMsg(getCtx(), "ValueOrNameExists"));//ValueExists, NameExists
				return false;
			}

		}
		return true;
	}
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(!success)
		{
			return false;
		}
		
		return true;
	}
}	//	MAssetGroup

