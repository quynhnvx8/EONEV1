package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;


public class MTransferPeriodLine extends X_C_TransferPeriodLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static MTransferPeriodLine get (Properties ctx, int C_TransferPeriodLine_ID, String trxName)
	{
		return (MTransferPeriodLine)MTable.get(ctx, MTransferPeriodLine.Table_Name).getPO(C_TransferPeriodLine_ID, trxName);
	}	//	get
	
	private MTransferPeriod			m_parent = null;
	
	public MTransferPeriod getParent()
	{
		if (m_parent == null)
			m_parent = new MTransferPeriod (getCtx(), getC_TransferPeriod_ID(), get_TrxName());
		return m_parent;
	}
	
	
	/** Create constructor */
	public MTransferPeriodLine (Properties ctx, int C_TransferPeriodLine_ID, String trxName)
	{
		super (ctx, C_TransferPeriodLine_ID,trxName);
		
	}	//	MAsset

	
	public MTransferPeriodLine (Properties ctx, ResultSet rs, String trxName)
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
		
		final String sql = "UPDATE C_TransferPeriod o"
				+ " SET (Amount, AmountConvert) ="
				+ "(SELECT Sum(Amount), Sum(AmountConvert)"
				+ " FROM C_TransferPeriodLine ol WHERE ol.C_TransferPeriod_ID=o.C_TransferPeriod_ID) "
				+ "WHERE C_TransferPeriod_ID=?";
		int no = DB.executeUpdateEx(sql, new Object[] {getC_TransferPeriod_ID()}, get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Lines -> #" + no);
		
		return true;
	}	//	afterSave
	
	
	protected boolean beforeDelete()
	{
		
		
		return true;
	}       //      beforeDelete
	
	
	

}
