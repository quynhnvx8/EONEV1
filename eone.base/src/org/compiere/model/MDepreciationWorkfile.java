package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.TimeUtil;


/**
 *  Depreciation Workfile Model
 *	@author	Teo Sarca, SC ARHIPAC SERVICE SRL
 */
public class MDepreciationWorkfile extends X_A_Depreciation_Workfile
	
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3814417671427820714L;

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_InventoryLine_ID line
	 */
	public MDepreciationWorkfile (Properties ctx, int A_Depreciation_Workfile_ID, String trxName)
	{
		super (ctx,A_Depreciation_Workfile_ID, trxName);
		
	}	//	MDepreciationWorkfile
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDepreciationWorkfile (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MDepreciationWorkfile
	
	/**	Asset (parent)			*/
	private MAsset m_asset = null;
	
	/**	Get Asset					*/
	public MAsset getAsset() {
		return getAsset(false);
	}
	
	/**	Get asset using this trxName
	 *	@param	requery	requery asset
	 *	@return parent asset
	 */
	public MAsset getAsset(boolean requery)
	{
		if (m_asset == null || requery) {
			m_asset = MAsset.get(getCtx(), getA_Asset_ID(), get_TrxName());
		}
		if (m_asset.get_ID() <= 0) {
			m_asset = null;
		}
		return m_asset;
	}
	
	/**	Set asset
	 *	@param	asset
	 */
	public void setAsset(MAsset asset)
	{
		setA_Asset_ID(asset.get_ID());
		m_asset = asset;
	}
	
	/**	Gets asset's service date (commissioning)
	 *	@return asset service date
	 */
	
	
	
	
	/**	Gets asset's class
	 *	@return asset class id
	 */
	/* commented out by @win
	public int getA_Asset_Class_ID()
	{
		MAsset asset = getAsset();
		if (asset == null) {
			return 0;
		}
		return asset.getA_Asset_Class_ID();
	}
	*/ // end comment by @win
	
	/**	After save
	 *	@param	newRecord
	 *	@return true on success
	 */
	protected boolean afterSave (boolean newRecord)
	{
		
		return true;
	}
	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (log.isLoggable(Level.INFO)) log.info ("Entering: trxName=" + get_TrxName());
		
		
		
		
		if (log.isLoggable(Level.INFO)) log.info("Leaving: trxName=" + get_TrxName() + " [RETURN TRUE]");
		return true;
	}	//	beforeSave
	
	
	
	
	
	/** Logger										*/
	private CLogger log = CLogger.getCLogger(getClass());

	public static Collection<MDepreciationWorkfile> forA_Asset_ID(Properties ctx, int asset_id, String trxName)
	{
		return new Query(ctx, Table_Name, MDepreciationWorkfile.COLUMNNAME_A_Asset_ID+"=?", trxName)
					.setParameters(new Object[]{asset_id})
					.list();
	}
	
	
	public static MDepreciationWorkfile get (Properties ctx, int A_Asset_ID, String postingType,  String trxName)
	{
		if (A_Asset_ID <= 0 || postingType == null)
		{
			return null;
		}
		
		final MultiKey key = new MultiKey(A_Asset_ID, postingType);
		if (trxName == null)
		{
			MDepreciationWorkfile wk = s_cacheAsset.get(key);
			if (wk != null)
				return wk;
		}
		
		final String whereClause = "";

		MDepreciationWorkfile wk = new Query(ctx, MDepreciationWorkfile.Table_Name, whereClause, trxName)
				.setParameters(new Object[]{A_Asset_ID, postingType})
				.firstOnly();
		
		
		if (trxName == null && wk != null)
		{
			s_cacheAsset.put(key, wk);
		}
		return wk;
	}
	
	public static MDepreciationWorkfile get (Properties ctx, int A_DepreciationWorkfile_ID, String trxName)
	{
		if (A_DepreciationWorkfile_ID <= 0)
		{
			return null;
		}
		
		final MultiKey key = new MultiKey(A_DepreciationWorkfile_ID, 0);
		if (trxName == null)
		{
			MDepreciationWorkfile wk = s_cacheAsset.get(key);
			if (wk != null)
				return wk;
		}
		
		final String whereClause = "A_Depreciation_Workfile_ID = ?";

		MDepreciationWorkfile wk = new Query(ctx, MDepreciationWorkfile.Table_Name, whereClause, trxName)
				.setParameters(A_DepreciationWorkfile_ID)
				.firstOnly();
		
		
		if (trxName == null && wk != null)
		{
			s_cacheAsset.put(key, wk);
		}
		return wk;
	}
	
	
	private static CCache<MultiKey, MDepreciationWorkfile>
	s_cacheAsset = new CCache<MultiKey, MDepreciationWorkfile>(Table_Name, Table_Name+"_Asset", 10); 
	
	/**	Returns the date of the last action
	 */
	public Timestamp getLastActionDate()
	{
		return TimeUtil.getMonthLastDay(TimeUtil.addMonths(getDateAcct(), -1));
	}
	
	/**	Check if the asset is depreciated at the specified date
	 *	@param date
	 *	@return true if you amortized until the specified date, otherwise false
	 */
	public boolean isDepreciated(Timestamp date)
	{
		Timestamp lastActionDate = getLastActionDate();
		boolean isDepr = !date.after(lastActionDate);		// date <= lastActionDate
		
		if (log.isLoggable(Level.FINE)) log.fine("LastActionDate=" + lastActionDate + ", GivenDate=" + date + " => isDepreciated=" + isDepr);
		return isDepr;
	}
	

}	//	MDepreciationWorkfile
