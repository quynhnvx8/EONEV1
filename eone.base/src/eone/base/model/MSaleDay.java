package eone.base.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MSaleDay extends X_C_SaleDay implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MSaleDay (Properties ctx, int C_SaleDay_ID, String trxName)
	{
		super (ctx, C_SaleDay_ID, trxName);
		
	}	//	MAssetUse

	
	public static MSaleDay get (Properties ctx, int C_SaleDay_ID)
	{
		return get(ctx,C_SaleDay_ID,null);
	}
	
	

	public static MSaleDay get (Properties ctx, int C_SaleDay_ID, String trxName)
	{
		final String whereClause = "C_SaleDay_ID=? AND AD_Client_ID=?";
		MSaleDay retValue = new Query(ctx,I_C_SaleDay.Table_Name,whereClause,trxName)
		.setParameters(C_SaleDay_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}
	
	
	
	public Map<Integer,MSaleDayLine> getAllLine (Properties ctx, String trxName)
	{
		final String whereClause = "M_SafetyStock_ID=? AND AD_Client_ID=?";
		List<MSaleDayLine> retValue = new Query(ctx,I_C_SaleDayLine.Table_Name,whereClause,trxName)
				.setParameters(getC_SaleDay_ID(), getAD_Client_ID())
				.list();
		Map<Integer, MSaleDayLine> listItems = new HashMap<Integer, MSaleDayLine>();
		for(int i = 0; i < retValue.size(); i++) {
			listItems.put(retValue.get(i).getC_SaleDayLine_ID(), retValue.get(i));
		}
		return listItems;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (newRecord || is_ValueChanged(COLUMNNAME_AD_Department_ID) || is_ValueChanged(COLUMNNAME_C_Year_ID)) {
			Map<String, Object> dataColumn = new HashMap<String, Object>();
			dataColumn.put(COLUMNNAME_C_Year_ID, getC_Year_ID());
			dataColumn.put(COLUMNNAME_AD_Department_ID, getAD_Department_ID());
			boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_C_SaleDay_ID, getC_SaleDay_ID());
			if (!check) {
				log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": Shop, Year!");
				return false;
			}
			
		}
		
		return true;
	}

	protected void updateProcessed(boolean status) {
		String sql = "Update C_SaleDayLine set Processed = ? Where C_SaleDay_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getC_SaleDay_ID()}, true, get_TrxName());
	}

	public MSaleDay (Properties ctx, ResultSet rs, String trxName)
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
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;

		
		
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Drafted;
		}
		setProcessed(true);
		updateProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
				
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		updateProcessed(false);
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;		
		return true;
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
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
