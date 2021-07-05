
package eone.base.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;

import eone.base.model.CalloutEngine;
import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.base.model.MDocType;
import eone.base.model.MOrder;
import eone.base.model.MProduct;
import eone.base.model.MTax;
import eone.base.model.Query;
import eone.base.model.X_C_DocType;
import eone.base.model.X_C_Order;


public class CalloutOrder extends CalloutEngine
{
	
	public String product (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		int C_Order_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_Order_ID");

		MOrder order = new Query(ctx, X_C_Order.Table_Name, "C_Order_ID = ?", null)
				.setParameters(C_Order_ID)
				.first();
		Map<String, BigDecimal> data = MProduct.getPrice_SOPO(order.getDateOrdered(), M_Product_ID);
		BigDecimal pricePO = Env.ZERO;
		BigDecimal priceSO = Env.ZERO;
		BigDecimal price = Env.ZERO;
		if (data != null) {
			pricePO = data.get("PricePO");
			priceSO = data.get("PriceSO");
			MDocType dt = MDocType.get(ctx, order.getC_DocType_ID());
			if(X_C_DocType.DOCTYPE_Output.equals(dt.getDocType())) {
				price = pricePO;
			} else {
				price = priceSO;
			}
		}
		mTab.setValue("Price", price);

		return "";
	}	//	product

	

	public String qty (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";

		BigDecimal QtyOrdered = (BigDecimal)value;
		Object objPrice = Env.getContext(ctx,  WindowNo, mTab.getTabNo(), "Price");
		if (objPrice == null) {
			return "";
		}
		int C_Order_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_Order_ID");

		MOrder order = new Query(ctx, X_C_Order.Table_Name, "C_Order_ID = ?", null)
				.setParameters(C_Order_ID)
				.first();
		
		BigDecimal priceActual = new BigDecimal(objPrice.toString());
		boolean IsTaxIncluded = order.isTaxIncluded();
		
		int C_Tax_ID = order.getC_Tax_ID();
		if (C_Tax_ID != 0) {
			MTax tax = MTax.get(ctx, C_Tax_ID);
			BigDecimal rate = tax.getRate();
			if (rate.compareTo(Env.ZERO) > 0) {
				if (IsTaxIncluded) {
					mTab.setValue("TaxBaseAmt", QtyOrdered.multiply(priceActual));
					mTab.setValue("Amount", QtyOrdered.multiply(priceActual).divide(Env.ONE.add(rate), 0, RoundingMode.HALF_UP));
					mTab.setValue("TaxAmt", QtyOrdered.multiply(priceActual).subtract(QtyOrdered.multiply(priceActual).divide(Env.ONE.add(rate), 0, RoundingMode.HALF_UP)));
				} else {
					mTab.setValue("Amount", QtyOrdered.multiply(priceActual));
					mTab.setValue("TaxBaseAmt", QtyOrdered.multiply(priceActual).multiply(Env.ONE.add(rate)));
					mTab.setValue("TaxAmt", QtyOrdered.multiply(priceActual).multiply(Env.ONE.add(rate)).subtract(QtyOrdered.multiply(priceActual)));
				}
			}
			
		} else {
			mTab.setValue("TaxBaseAmt", Env.ZERO);
			mTab.setValue("Amount", QtyOrdered.multiply(priceActual));
			mTab.setValue("TaxAmt", Env.ZERO);
		}
		
		return "";
		
	}	//	qty
	
}	//	CalloutOrder

