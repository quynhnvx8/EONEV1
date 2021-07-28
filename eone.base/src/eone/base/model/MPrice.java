package eone.base.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MPrice extends X_M_Price implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPrice (Properties ctx, int M_Price_ID, String trxName)
	{
		super (ctx, M_Price_ID, trxName);
		
	}	//	MAssetUse

	
	public static MPrice get (Properties ctx, int M_Price_ID)
	{
		return get(ctx,M_Price_ID,null);
	}
	
	

	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (is_ValueChanged(X_M_Price.COLUMNNAME_ValidFrom) || is_ValueChanged(X_M_Price.COLUMNNAME_ValidTo)) {
			String sql = "Select count(1) From M_Price Where ((? between ValidFrom And ValidTo) Or (? between ValidFrom And ValidTo)) And M_Price_ID > 0 And M_Price_ID != ? And M_Product_ID = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(getValidFrom());
			params.add(getValidTo());
			params.add(getM_Price_ID());
			params.add(getM_Product_ID());
			int no = DB.getSQLValue(null, sql, params);
			if (no > 0) {
				log.saveError("Save Error!", "Thời gian không hợp lệ");
				return false;
			}
		}
		return true;
	}


	public static MPrice get (Properties ctx, int M_Price_ID, String trxName)
	{
		final String whereClause = "M_Price_ID=? AND AD_Client_ID=?";
		MPrice retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(M_Price_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}
	
	
	

	public MPrice (Properties ctx, ResultSet rs, String trxName)
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
	

	//Implements DocAction
	protected String		m_processMsg = null;
	
	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}

	@Override
	public String completeIt() {
		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		
		return true;
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}

	@Override
	public String getSummary() {
		
		return "";
	}


	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
