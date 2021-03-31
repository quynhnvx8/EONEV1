package org.compiere.model;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;

/**
 * Method of adjusting the difference between depreciation (Calculated) and registered as (booked).
 * ex. MDI, LDI, YDI ...
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 */
public class MDepreciationMethod extends X_A_Depreciation_Method
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4622027905888469713L;

	/** Standard Constructor */
	public MDepreciationMethod (Properties ctx, int A_Depreciation_Method_ID, String trxName)
	{
		super (ctx, A_Depreciation_Method_ID, trxName);
	}	//	MDepreciationMethod

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MDepreciationMethod (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MDepreciationMethod

	/**		Cache									*/
	private static CCache<Integer,MDepreciationMethod>
	s_cache = new CCache<Integer,MDepreciationMethod>(Table_Name, 5);
	/**		Cache for type							*/
	private static CCache<String,MDepreciationMethod>
	s_cache_forType = new CCache<String,MDepreciationMethod>(Table_Name, Table_Name+"_DepreciationType", 5);
	
	/**
	 *
	 */
	private static void addToCache(MDepreciationMethod depr)
	{
		if (depr == null)
		{
			return;
		}
		s_cache.put(depr.get_ID(), depr);
		s_cache_forType.put(depr.getDepreciationType(), depr);
	}

	
	public static MDepreciationMethod get(Properties ctx, int A_Depreciation_Method_ID)
	{
		MDepreciationMethod depr = s_cache.get(A_Depreciation_Method_ID);
		if (depr != null)
		{
			return depr;
		}
		depr = new MDepreciationMethod(ctx, A_Depreciation_Method_ID, null);
		if (depr.get_ID() > 0)
		{
			addToCache(depr);
		}
		else
		{
			depr = null;
		}
		return depr;
	} // get

	/**
	 *
	 */
	public static MDepreciationMethod get(Properties ctx, String depreciationType)
	{
		String key = depreciationType;
		MDepreciationMethod depr = s_cache_forType.get(key);
		if (depr != null)
		{
			return depr;
		}
		depr = new Query(ctx, Table_Name, COLUMNNAME_DepreciationType+"=?", null)
					.setParameters(new Object[]{depreciationType})
					.firstOnly();
		addToCache(depr);
		return depr;
	}

	
}
