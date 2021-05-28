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

public class MPromotion extends X_M_Promotion implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPromotion (Properties ctx, int M_Promotion_ID, String trxName)
	{
		super (ctx, M_Promotion_ID, trxName);
		setContextPromotionType ();
		
	}	//	MAssetUse

	
	public static MPromotion get (Properties ctx, int M_Promotion_ID)
	{
		return get(ctx,M_Promotion_ID,null);
	}
	
	protected void setContextPromotionType () {
		MPromotionType promotionType = MPromotionType.getById(getCtx(), getM_PromotionType_ID(), null);
		Env.setContext(getCtx(), "#REQUIED_AMT", promotionType.isRequied_Amt());
		Env.setContext(getCtx(), "#REQUIED_QTY", promotionType.isRequied_Qty());
		Env.setContext(getCtx(), "#REQUIED_GIF", promotionType.isRequied_Gif());
		Env.setContext(getCtx(), "#REQUIED_PER", promotionType.isRequied_Per());
		Env.setContext(getCtx(), "#REQUIED_DIS", promotionType.isRequied_Dis());
		Env.setContext(getCtx(), "#REQUIED_GROUP", promotionType.isRequied_Group());
	}
	

	public static MPromotion get (Properties ctx, int M_Promotion_ID, String trxName)
	{
		final String whereClause = "M_Promotion_ID=? AND AD_Client_ID=?";
		MPromotion retValue = new Query(ctx,I_M_Production.Table_Name,whereClause,trxName)
		.setParameters(M_Promotion_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}
	
	
	
	public Map<Integer,MPromotionLine> getAllLine (Properties ctx, String trxName)
	{
		final String whereClause = "M_Promotion_ID=? AND AD_Client_ID=?";
		List<MPromotionLine> retValue = new Query(ctx,I_M_PromotionLine.Table_Name,whereClause,trxName)
				.setParameters(getM_Promotion_ID(), getAD_Client_ID())
				.list();
		Map<Integer, MPromotionLine> listItems = new HashMap<Integer, MPromotionLine>();
		for(int i = 0; i < retValue.size(); i++) {
			listItems.put(retValue.get(i).getM_PromotionLine_ID(), retValue.get(i));
		}
		return listItems;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if (newRecord || is_ValueChanged(COLUMNNAME_ValidFrom) ) {
			Map<String, Object> dataColumn = new HashMap<String, Object>();
			dataColumn.put(COLUMNNAME_ValidFrom, getValidFrom());
			boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_M_Promotion_ID, getM_Promotion_ID());
			if (!check) {
				log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + " ");
				return false;
			}
			
		}
		
		if (is_ValueChanged(COLUMNNAME_M_PromotionType_ID)) {
			setContextPromotionType ();
			resetForPromotionType();
		}
		
		return true;
	}
	
	protected void resetForPromotionType() {
		MPromotionType proType = MPromotionType.getById(getCtx(), getM_PromotionType_ID(), null);
		boolean requiredAmt = false;
		boolean requiredQty = false;
		boolean requiredGif = false;
		boolean requiredPer = false;
		boolean requiredDis = false;
		boolean requiredGro = false;
		int i = 0;
		if (proType.isRequied_Amt()) {
			requiredAmt = true;
			i++;
		}
		if (proType.isRequied_Qty()) {
			requiredQty = true;
			i++;
		}
		if (proType.isRequied_Gif()) {
			requiredGif = true;
			i++;
		}
		if (proType.isRequied_Per()) {
			requiredPer = true;
			i++;
		}
		if (proType.isRequied_Dis()) {
			requiredDis = true;
			i++;
		}
		if (proType.isRequied_Group()) {
			requiredGro = true;
			i++;
		}
		String sql = "";
		if (i > 0) {
			sql = "Update M_PromotionLine set M_Product_ID = M_Product_ID";
			if (!requiredAmt) {
				sql = sql + ", Amount = 0";
			}
			if (!requiredQty) {
				sql = sql + ", Qty = 0";
			}
			if (!requiredGif) {
				sql = sql + ", M_Product_Free_ID = null, QtyFree = 0, C_UOM_Free_ID = null";
			}
			if (!requiredPer) {
				sql = sql + ", DiscountPercent = 0";
			}
			if (!requiredDis) {
				sql = sql + ", DiscountAmt = 0";
			}
			if (!requiredGro) {
				sql = sql + ", IsRequiredProduct = 'N'";
			}
			sql = sql + " Where M_Promotion_ID = ?";
			DB.executeUpdate(sql, getM_Promotion_ID(), get_TrxName());
		}
	}

	protected void updateProcessed(boolean status) {
		String sql = "Update M_PromotionLine set Processed = ? Where M_Promotion_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getM_Promotion_ID()}, true, get_TrxName());
		sql = "Update M_PromotionShop set Processed = ? Where M_Promotion_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getM_Promotion_ID()}, true, get_TrxName());
		sql = "Update M_PromotionCus set Processed = ? Where M_Promotion_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getM_Promotion_ID()}, true, get_TrxName());
	}

	public MPromotion (Properties ctx, ResultSet rs, String trxName)
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
