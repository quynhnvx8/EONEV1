package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
/** Generated Model for A_Asset_Use
 ** @version $Id: X_A_Asset.java,v 1.88 2004/08/27 21:26:37 jjanke Exp $ */
public class MAssetUse extends X_A_Asset_Use
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MAssetUse (Properties ctx, int A_Asset_Use_ID, String trxName)
	{
		super (ctx, A_Asset_Use_ID, trxName);
		if (A_Asset_Use_ID == 0)
		{
			// empty block
		}
	}	//	MAssetUse

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MAssetUse (Properties ctx, ResultSet rs, String trxName)
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


	public static List<MAssetUse> get(Properties ctx, int A_Asset_ID, Timestamp date) {
		String whereClause = " A_Asset_ID = ? And UseDate = ?";
		List<MAssetUse> retValue = new Query(ctx, Table_Name, whereClause, null)
				.setParameters(A_Asset_ID, date)
				.list();
		return retValue;
	}
}
