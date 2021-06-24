
package eone.base.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

import org.compiere.util.Env;

import eone.base.model.CalloutEngine;
import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.base.model.MTax;

//eone.base.model.CalloutInvoice.fillAmount_TaxRate
public class CalloutInvoice extends CalloutEngine
{

	//Ham nay dang dung o bang C_Cash, A_Asset_Build
	public String fillAmount_TaxRate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())	
			return "";

		Object objTax_ID = mTab.getValue("C_Tax_ID");
		Object objAmount = mTab.getValue("Amount");
		Object objTaxBaseAmt = mTab.getValue("TaxBaseAmt");
		BigDecimal C_Tax_ID = Env.ZERO;
		BigDecimal amount = Env.ZERO;
		
		if (objTax_ID == null) {
			objTax_ID = "0";
		}
		
		if (objAmount == null) {
			objAmount = "0";
		}
		
		C_Tax_ID = new BigDecimal(objTax_ID.toString());
		amount = new BigDecimal(objAmount.toString());
		
		MTax tax = MTax.get(ctx, C_Tax_ID.intValue());
		BigDecimal rate = Env.ZERO;
		if (tax != null) {
			rate = tax.getRate();
		}
		
		BigDecimal baseAmt = Env.ZERO;
		
		//Doi thue
		if ("C_Tax_ID".equalsIgnoreCase(mField.getColumnName())) {
			C_Tax_ID = new BigDecimal(mTab.getValue("C_Tax_ID").toString());
			tax = MTax.get(ctx, C_Tax_ID.intValue());
			if (tax != null) {
				rate = tax.getRate();
			} else {
				rate = Env.ZERO;
			}
			objAmount = mTab.getValue("Amount");
			if (objAmount == null) {
				return "";
			}			
			objTaxBaseAmt = mTab.getValue("TaxBaseAmt");
			if (objTaxBaseAmt == null) {
				objTaxBaseAmt = "0";
			}
			amount = new BigDecimal(objAmount.toString());
			
			baseAmt = amount.multiply(Env.ONE.add(rate));
			mTab.setValue("TaxAmt", baseAmt.subtract(amount));
			mTab.setValue("TaxBaseAmt", baseAmt);
		}
		
		
		
		if ("Amount".equalsIgnoreCase(mField.getColumnName())) {
			objAmount = mTab.getValue("Amount");
			if (objAmount == null) {
				objAmount = "0";
			}
			amount = new BigDecimal(objAmount.toString());
			baseAmt = amount.multiply(Env.ONE.add(rate));
			mTab.setValue("TaxAmt", baseAmt.subtract(amount));
			mTab.setValue("TaxBaseAmt", baseAmt);
		}
		
		if ("TaxBaseAmt".equalsIgnoreCase(mField.getColumnName())) {
			objTaxBaseAmt = mTab.getValue("TaxBaseAmt");
			if (objTaxBaseAmt == null) {
				objTaxBaseAmt = "0";
			}
			baseAmt = new BigDecimal(objTaxBaseAmt.toString());
			
			amount = baseAmt.divide(Env.ONE.add(rate), 0, RoundingMode.HALF_UP);
			mTab.setValue("TaxAmt", baseAmt.subtract(amount));
			mTab.setValue("Amount", amount);
		}
				
		
		Object objRate = mTab.getValue("CurrencyRate");
		BigDecimal currencyRate = Env.ONE;
		if (objRate != null) {
			currencyRate = new BigDecimal(objRate.toString());
		}
		mTab.setValue("AmountConvert", amount.multiply(currencyRate));
		
		return "";
	}

}	//	CalloutInvoice
