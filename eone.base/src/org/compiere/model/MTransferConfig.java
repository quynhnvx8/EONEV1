package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;


public class MTransferConfig extends X_C_TransferConfig
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static MTransferConfig get (Properties ctx, int C_TransferConfig_ID, String trxName)
	{
		return (MTransferConfig)MTable.get(ctx, MTransferConfig.Table_Name).getPO(C_TransferConfig_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MTransferConfig (Properties ctx, int C_TransferConfig_ID, String trxName)
	{
		super (ctx, C_TransferConfig_ID,trxName);
		
	}	//	MAsset

	
	public MTransferConfig (Properties ctx, ResultSet rs, String trxName)
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
	
	protected MTransferConfigLine[]	m_lines = null;
	
	public MTransferConfigLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		List<MTransferConfigLine> list = new Query(getCtx(), MTransferConfigLine.Table_Name, "C_TransferConfig_ID=?", get_TrxName())
		.setParameters(getC_TransferConfig_ID())
		.setOrderBy(MTransferConfigLine.COLUMNNAME_OrderCalculate + " ASC ")
		.list();
		//
		m_lines = new MTransferConfigLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}
}
