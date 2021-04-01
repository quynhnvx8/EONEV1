package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;

public class MAssetTools extends X_A_Asset_Tools
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1371478760221357780L;


	
	private static CCache<String,MAssetTools> s_cache = new CCache<String,MAssetTools>(Table_Name, 10, 0);
	
	
	public static MAssetTools get (Properties ctx, int A_Asset_Tools_ID)
	{
		if (A_Asset_Tools_ID <= 0)
			return null;
		MAssetTools o = s_cache.get(String.valueOf(A_Asset_Tools_ID));
		if (o != null)
			return o;
		o = new MAssetTools(ctx, A_Asset_Tools_ID, null);
		if (o.get_ID() > 0) {
			s_cache.put(String.valueOf(A_Asset_Tools_ID), o);
			return o;
		}
		return null;
	}
	
	public static MAssetTools get (Properties ctx, int A_Asset_ID, int M_Product_ID, Timestamp date)
	{
		if (A_Asset_ID <= 0 || M_Product_ID <= 0)
			return null;
		MAssetTools o = s_cache.get(A_Asset_ID + "_" + M_Product_ID);
		if (o != null)
			return o;
		String whereClause = " A_Asset_ID = ? And M_Product_ID = ? And DateTrx = ? ";
		o = new Query(ctx, Table_Name, whereClause, null)
				.setParameters(A_Asset_ID, M_Product_ID, date)
				.firstOnly();
		if (o != null && o.get_ID() > 0) {
			s_cache.put(A_Asset_ID + "_" + M_Product_ID, o);
			return o;
		}
		return null;
	}

	public static MAssetTools get (Properties ctx, Object id)
	{
		if (id == null)
			return null;
		return get(ctx, ((Number)id).intValue());
	}
	
	/** Standard Constructor */
	public MAssetTools (Properties ctx, int A_Asset_Tools_ID, String trxName)
	{
		super (ctx, A_Asset_Tools_ID, trxName);
	}
	
	/** Load Constructor */
	public MAssetTools (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	


}	// class MAssetType
