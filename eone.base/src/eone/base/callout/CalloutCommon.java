package eone.base.callout;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CalloutUtil;
import org.compiere.util.Env;

import eone.base.model.CalloutEngine;
import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.base.model.MDocType;
import eone.base.model.MProduct;
import eone.base.model.MWarehouse;
import eone.base.model.X_C_DocType;

//eone.base.callout.CalloutCommon.cal_From_Curr_To_Rate
//eone.base.callout.CalloutCommon.cal_From_Amount_To_AmountConvert
//eone.base.callout.CalloutCommon.fill_UOM
//eone.base.callout.CalloutCommon.getNewDocumentNo
//eone.base.callout.CalloutCommon.getNewValue 
public class CalloutCommon extends CalloutEngine
{
	
	public String cal_From_Curr_To_Rate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		
		Integer C_Currency_ID = (Integer)value;
		Timestamp DateAcct = (Timestamp)mTab.getValue("DateAcct");
		BigDecimal rate = Env.getRateByCurrency(C_Currency_ID, DateAcct);
		
		if (C_Currency_ID == null || C_Currency_ID.intValue() == 0)
		{
			mTab.setValue("CurrencyRate", Env.ONE);
			return "";
		}
		BigDecimal amount = Env.ZERO;
		if (mTab.getValue("Amount") != null) {
			amount = new BigDecimal(mTab.getValue("Amount").toString());
		}
		
		mTab.setValue("CurrencyRate", rate);
		mTab.setValue("AmountConvert", rate.multiply(amount));
		
		return "";
	}	


	public String cal_From_Amount_To_AmountConvert (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		
		BigDecimal amount = new BigDecimal(String.valueOf(value));
		BigDecimal rate = Env.ZERO;
		Object objRate = mTab.getValue("CurrencyRate");
		if (objRate != null) {
			rate = new BigDecimal(objRate.toString());
		} else {
			objRate = mTab.getParentTab().getValue("CurrencyRate");			
		}
		if (objRate == null) {
			objRate = "0";
		}
		
		rate = new BigDecimal(objRate.toString());		
		mTab.setValue("AmountConvert", rate.multiply(amount));
		
		return "";
	}
	
	public String fill_UOM (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID != null) {
			MProduct pr = MProduct.get(ctx, M_Product_ID);
			mTab.setValue("C_UOM_ID", pr.getC_UOM_ID());
		}
		
		
		return "";
	}
	
	public String getNewDocumentNo(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value == null) {			
			return "";
		}
		
		
		String p_documentNo = CalloutUtil.getDocumentNo(ctx, null, WindowNo, mTab);
		if (p_documentNo == null || p_documentNo.length() <= 0) {
			return "";
		}
		
		mTab.setValue("DocumentNo", p_documentNo);
		
		setDefault(ctx, mTab, mField, value);
		
		return "";
	}
	

	public String getNewValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value == null) {			
			return "";
		}
		
		
		String p_Value = CalloutUtil.getValue(ctx, null, WindowNo, mTab);
		if (p_Value == null || p_Value.length() <= 0) {
			return "";
		}
		
		mTab.setValue("Value", p_Value);
		
		setDefault(ctx, mTab, mField, value);
		
		return "";
	}

	
	private void setDefault(Properties ctx, GridTab mTab, GridField mField, Object value) {
		Object objDocType = mTab.getValue("C_DocType_ID");
		MDocType dt = null;
		if (objDocType != null) {
			dt = MDocType.get(ctx, Integer.parseInt(objDocType.toString()));
		}
		
		MWarehouse wh = MWarehouse.getDefault(ctx, null);
		if (wh != null) {
			if (dt != null && X_C_DocType.DOCTYPE_Input.equals(dt.getDocType()))
			{
				mTab.setValue("M_Warehouse_Dr_ID", wh.getM_Warehouse_ID());
				//mTab.setValue("M_Warehouse_ID", wh.getM_Warehouse_ID());
			}
			if (dt != null && X_C_DocType.DOCTYPE_Output.equals(dt.getDocType()))
			{
				mTab.setValue("M_Warehouse_Cr_ID", wh.getM_Warehouse_ID());
			}
		}
	}

}	//	CalloutCashJournal
