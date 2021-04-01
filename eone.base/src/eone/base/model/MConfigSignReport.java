package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;


public class MConfigSignReport extends X_C_ConfigSignReport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static MConfigSignReport get (Properties ctx, int C_ConfigSignReport_ID, String trxName)
	{
		return (MConfigSignReport)MTable.get(ctx, MConfigSignReport.Table_Name).getPO(C_ConfigSignReport_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MConfigSignReport (Properties ctx, int C_ConfigSignReport_ID, String trxName)
	{
		super (ctx, C_ConfigSignReport_ID,trxName);
		
	}	//	MAsset

	
	public MConfigSignReport (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAsset
	
	
	

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		
		
		return true;
	}	//	beforeSave
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(!success)
		{
			return success;
		}
		
		
		
		return true;
	}	//	afterSave
	
	
	protected boolean beforeDelete()
	{
		
		
		return true;
	}       //      beforeDelete
	
	private static CCache<Integer,MConfigSignReport[]> s_roles = new CCache<Integer,MConfigSignReport[]>(Table_Name, 5);
	
	protected static MConfigSignReport[]	m_lines = null;
	
	public static MConfigSignReport[] getLines (Properties ctx, int AD_Process_ID)
	{
		m_lines = (MConfigSignReport[]) s_roles.get(AD_Process_ID);
		if (m_lines != null) {
			return m_lines;
		}
		List<MConfigSignReport> list = new Query(ctx, MConfigSignReport.Table_Name, "AD_Process_ID=? And AD_Client_ID = ?", null)
		.setParameters(AD_Process_ID, Env.getAD_Client_ID(ctx))
		.setOrderBy(MConfigSignReport.COLUMNNAME_SeqNo + " ASC ")
		.list();
		//
		m_lines = new MConfigSignReport[list.size()];
		list.toArray(m_lines);
		s_roles.put(AD_Process_ID, m_lines);
		return m_lines;
	}
}
