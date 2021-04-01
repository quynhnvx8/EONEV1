package eone.base.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Msg;


public class MDepreciationSplit extends X_A_Depreciation_Split
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6731366890875525147L;
	
	/** Standard Constructor */
	public MDepreciationSplit(Properties ctx, int A_Depreciation_Split_ID, String trxName)
	{
		super (ctx, A_Depreciation_Split_ID, trxName);
		
	}
	
	/** Load Constructor */
	public MDepreciationSplit (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	public static MDepreciationSplit get(Properties ctx, int A_Depreciation_Split_ID) {
		if (A_Depreciation_Split_ID <= 0) {
			return null;
		}
		MDepreciationSplit depexp = new MDepreciationSplit(ctx, A_Depreciation_Split_ID, null);
		if (depexp.get_ID() != A_Depreciation_Split_ID) {
			depexp = null;
		}
		return depexp;
	}
	
	
	
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		if(getPercent() < 100) {
			log.saveWarning("Warning", Msg.getMsg(getCtx(), "PercentNotEnough"));
		}
		String sql = "Select sum(Percent) From A_Depreciation_Split Where A_Asset_ID = ? And A_Depreciation_Split != ?";
		List<Object> params = new ArrayList<Object>();
		params.add(getA_Asset_ID());
		params.add(getA_Depreciation_Split_ID());
		int total = DB.getSQLValue(get_TrxName(), sql, params);
		if (total + getPercent() > 100) {
			log.saveWarning("Warning", Msg.getMsg(getCtx(), "PercentOver"));
			return false;
		}
		return true;
	}

	protected boolean beforeDelete()
	{
		
		return true;
	}
	
	
	protected boolean afterDelete(boolean success)
	{
		if (!success)
		{
			return false;
		}
		
		return true;
	}
	
	
	
	public String toString()
	{
		return getClass().getSimpleName()+"["+get_ID()
			+",A_Asset_ID="+getA_Asset_ID()
			+"]";
	}
}
