package eone.base.model;

import java.sql.ResultSet;
import java.util.ArrayList;
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
		if ( promotionType != null) {
			Env.setContext(getCtx(), "#REQUIED_AMT", promotionType.isRequied_Amt());
			Env.setContext(getCtx(), "#REQUIED_QTY", promotionType.isRequied_Qty());
			Env.setContext(getCtx(), "#REQUIED_GIF", promotionType.isRequied_Gif());
			Env.setContext(getCtx(), "#REQUIED_PER", promotionType.isRequied_Per());
			Env.setContext(getCtx(), "#REQUIED_DIS", promotionType.isRequied_Dis());
			Env.setContext(getCtx(), "#REQUIED_GROUP", promotionType.isRequied_Group());
		}
	}
	

	public static MPromotion get (Properties ctx, int M_Promotion_ID, String trxName)
	{
		final String whereClause = "M_Promotion_ID=? AND AD_Client_ID=?";
		MPromotion retValue = new Query(ctx,I_M_Promotion.Table_Name,whereClause,trxName)
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
		
		if (newRecord 
				|| is_ValueChanged(COLUMNNAME_ValidFrom) 
				|| is_ValueChanged(COLUMNNAME_ValidTo) 
				|| is_ValueChanged(COLUMNNAME_M_PromotionType_ID) 
		) {
			String sql = "Select count(1) From M_Promotion "+
					" Where M_PromotionType_ID = ? And (? Between ValidFrom And ValidTo Or ? Between ValidFrom And ValidTo) " +
					"		And M_Promotion_ID != ? And M_Promotion_ID > 0";
			List<Object> params = new ArrayList<Object>();
			params.add(getM_PromotionType_ID());
			params.add(getValidFrom());
			params.add(getValidTo());
			params.add(getM_Promotion_ID());
			int count = DB.getSQLValue(null, sql, params);
			if (count > 0) {
				log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + " ");
				return false;
			}
			
		}
		
		if (is_ValueChanged(COLUMNNAME_M_PromotionType_ID)) {
			setContextPromotionType ();
			//resetForPromotionType();
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
		
		String sql = "";
		if (i > 0) {
			sql = "Update M_PromotionLine set M_Product_ID = M_Product_ID";
			if (!requiredAmt) {
				sql = sql + ", Amount = null";
			}
			if (!requiredQty) {
				sql = sql + ", QtyMultiplier = null";
			}
			if (!requiredGif) {
				sql = sql + ", M_Product_Free_ID = null, QtyFree = null";
			}
			if (!requiredPer) {
				sql = sql + ", DiscountPercent = null";
			}
			if (!requiredDis) {
				sql = sql + ", DiscountAmt = null";
			}
			
			sql = sql + " Where M_Promotion_ID = ?";
			DB.executeUpdate(sql, getM_Promotion_ID(), get_TrxName());
		}
	}

	protected void updateProcessed(boolean status) {
		String sql = "Update M_PromotionLine set Processed = ? Where M_Promotion_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getM_Promotion_ID()}, true, get_TrxName());
		/*
		sql = "Update M_PromotionShop set Processed = ? Where M_Promotion_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getM_Promotion_ID()}, true, get_TrxName());
		sql = "Update M_PromotionCus set Processed = ? Where M_Promotion_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getM_Promotion_ID()}, true, get_TrxName());
		*/
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
		
		setProcessed(true);
		updateProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		updateProcessed(false);
			
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
