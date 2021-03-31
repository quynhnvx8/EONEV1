package org.adempiere.base;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.I_C_ProjectLine;

public interface IProductPricing {

	
	void setInitialValues(int M_Product_ID, int C_BPartner_ID, 
			BigDecimal qty, boolean isSOTrx, String trxName);
	
	boolean calculatePrice();
	
	BigDecimal getDiscount();
	
	int getM_Product_ID();

	int getM_PriceList_ID();
	
	void setM_PriceList_ID(int M_PriceList_ID);
	
	void setM_PriceList_Version_ID(int M_PriceList_Version_ID);
	
	void setQty(BigDecimal qty);
	
	Timestamp getPriceDate();
	
	void setPriceDate(Timestamp priceDate);

	int getC_UOM_ID();
	
	BigDecimal getPriceList();
	
	BigDecimal getPriceStd();
	
	BigDecimal getPriceLimit();
	
	int getC_Currency_ID();
	
	boolean isEnforcePriceLimit();
	
	boolean isDiscountSchema();
	
	boolean isCalculated();	
	
	void setOrderLine(I_C_OrderLine orderLine, String trxName);

	void setInvoiceLine(I_C_InvoiceLine invoiceLine, String trxName);
	
	void setProjectLine(I_C_ProjectLine projectLine, String trxName);
	
	
}
