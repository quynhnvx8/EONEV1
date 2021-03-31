package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;


public class MTransferConfigLine extends X_C_TransferConfigLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static MTransferConfigLine get (Properties ctx, int C_TransferConfigLine_ID, String trxName)
	{
		return (MTransferConfigLine)MTable.get(ctx, MTransferConfigLine.Table_Name).getPO(C_TransferConfigLine_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MTransferConfigLine (Properties ctx, int C_TransferConfigLine_ID, String trxName)
	{
		super (ctx, C_TransferConfigLine_ID,trxName);
		
	}	//	MAsset

	
	public MTransferConfigLine (Properties ctx, ResultSet rs, String trxName)
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
	
	
}
