
package eone.base.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.Env;

import eone.base.model.CalloutEngine;
import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.base.model.MDocType;
import eone.base.model.MInOut;
import eone.base.model.MInOutLine;
import eone.base.model.MStorage;
import eone.base.model.MTax;
import eone.base.model.X_M_InOut;

//eone.base.model.CalloutInOut.fillAmount_QtyPrice
//eone.base.model.CalloutInOut.fillAmount_Discount

public class CalloutInOut extends CalloutEngine
{

	

	public String fillAmount_QtyPrice (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())	
			return "";

		Object objQty = mTab.getValue(MInOutLine.COLUMNNAME_Qty);
		Object objPrice = mTab.getValue(MInOutLine.COLUMNNAME_Price);
		Object objAmount = mTab.getValue("Amount");
		Object objTax = mTab.getValue("C_Tax_ID");
		Object objInOut = mTab.getValue("M_InOut_ID");
		int p_M_InOut_ID = 0;
		if (objInOut != null) {
			p_M_InOut_ID = Integer.parseInt(objInOut.toString());
		}
		MInOut inout = MInOut.get(ctx, p_M_InOut_ID);
		MDocType mDocType = MDocType.get(ctx, inout.getC_DocType_ID());
		
		BigDecimal qty = Env.ZERO;
		BigDecimal price = Env.ZERO;
		BigDecimal p_Amount = Env.ZERO;
		BigDecimal p_TaxAmt = Env.ZERO;
		BigDecimal p_TaxBase = Env.ZERO;
		BigDecimal taxRate = Env.ZERO;
		
		if (objQty == null) {
			objQty = "0";
		}
		if (objPrice == null) {
			objPrice = "0";
		}
		
		if (objTax == null) {
			objTax = 0;
		}
		
		//Cot so luong xoa ve 0
		if (MInOutLine.COLUMNNAME_Qty.equalsIgnoreCase(mField.getColumnName())) {
			objQty = mTab.getValue(MInOutLine.COLUMNNAME_Qty);
			if (objQty == null)
				return "";				
		}
		//Cot gia xoa ve 0
		if (MInOutLine.COLUMNNAME_Price.equalsIgnoreCase(mField.getColumnName())) {
			objPrice = mTab.getValue(MInOutLine.COLUMNNAME_Price);
			if (objPrice == null)
				return "";				
		}
		
		//Nhap don gia hoac so luong
		
		if (MDocType.DOCTYPE_Output.equals(mDocType.getDocType())) {
			//Neu la type xuat thi tu dong lay don gia theo AD_Client.MMPolicy
			//Chi cho nhap so luong va tu dong tinh don gia theo phuong phap tinh gia vat tu duoc cau hinh AD_Client.MMPolicy
			
			//Lay kho xuat (Kho co)
			int M_Warehouse_ID = inout.getM_Warehouse_Cr_ID();
			Timestamp dateAcct = inout.getDateAcct();
			//Lay Product
			Object objProduct_ID = mTab.getValue(MInOutLine.COLUMNNAME_M_Product_ID);
			if (objProduct_ID == null)
				return "";
			int M_Product_ID = Integer.parseInt(objProduct_ID.toString());
			
			//Lay so luong can xuat
			if (MInOutLine.COLUMNNAME_Qty.equalsIgnoreCase(mField.getColumnName())) 
			{
				objQty = mTab.getValue(MInOutLine.COLUMNNAME_Qty);
				if (objQty == null)
					return "";
				qty = new BigDecimal(objQty.toString());
			}
			
			
			Object [] data = MStorage.getQtyPrice(M_Product_ID, M_Warehouse_ID, dateAcct);
			
			BigDecimal qtyRemain = Env.ZERO;
			BigDecimal amtRemain = Env.ZERO;
			if (data != null && data.length > 0) {
				qtyRemain = new BigDecimal(data[0].toString());
				amtRemain = new BigDecimal(data[2].toString());
				price = amtRemain.divide(qtyRemain, Env.getScalePrice(), RoundingMode.HALF_UP);
				p_Amount = qty.multiply(price);
				
				p_Amount = p_Amount.setScale(Env.getScaleFinal(), RoundingMode.HALF_UP);
			
				if (qtyRemain.compareTo(qty) < 0)
				{
					//Neu so luong nhap nho hon so luong con lai thi lay so luong con lai.
					mTab.setValue(MInOutLine.COLUMNNAME_Qty, qtyRemain);
					mTab.setValue(MInOutLine.COLUMNNAME_Price, price);
					mTab.setValue(MInOutLine.COLUMNNAME_PricePO, price);
					mTab.setValue(MInOutLine.COLUMNNAME_Amount, amtRemain);
				} 
				else
				{
					//Nguoc lai thi lay tho so luong nhap tren form
					mTab.setValue(MInOutLine.COLUMNNAME_Price, price);
					mTab.setValue(MInOutLine.COLUMNNAME_PricePO, price);
					mTab.setValue(MInOutLine.COLUMNNAME_Amount, p_Amount);
				}
			} 
			else
			{//trong hop khong co data tu m_storage
				objQty = mTab.getValue(MInOutLine.COLUMNNAME_Qty);
				objPrice = mTab.getValue(MInOutLine.COLUMNNAME_Price);
				if (objQty == null) {
					objQty = "0";
				}
				if (objPrice == null) {
					objPrice = "0";
				}
				qty = new BigDecimal(objQty.toString());
				price = new BigDecimal(objPrice.toString());
				mTab.setValue(MInOutLine.COLUMNNAME_Amount, qty.multiply(price));
				
			}//end truong hop ko co data
			
		} else {
			//Cac type nhap khac
			if (MInOutLine.COLUMNNAME_Qty.equalsIgnoreCase(mField.getColumnName()) 
					|| MInOutLine.COLUMNNAME_Price.equalsIgnoreCase(mField.getColumnName())
					|| "C_Tax_ID".equalsIgnoreCase(mField.getColumnName())
					) {
				
	 			objQty = mTab.getValue(MInOutLine.COLUMNNAME_Qty);
				objPrice = mTab.getValue(MInOutLine.COLUMNNAME_Price);
				if (objQty == null) {
					objQty = "0";
				}
				if (objPrice == null) {
					objPrice = "0";
				}
				qty = new BigDecimal(objQty.toString());
				price = new BigDecimal(objPrice.toString());
											
			}
		}//End Type nhap khac
		
		//Nhap thanh tien. Chi tinh lai so luong va tien chuyen doi
		if ("Amount".equalsIgnoreCase(mField.getColumnName())) {
			objAmount = mTab.getValue("Amount");
			if (objAmount == null) {
				objAmount = "0";
			}
			p_Amount = new BigDecimal(objAmount.toString());
			qty = new BigDecimal(objQty.toString());
			price = new BigDecimal(objPrice.toString());
			if (price.compareTo(Env.ZERO) > 0)
				mTab.setValue(MInOutLine.COLUMNNAME_Qty, p_Amount.divide(price, Env.getScaleCalculating(), RoundingMode.HALF_UP));		
		}
		
		
		if(!X_M_InOut.INCLUDETAXTAB_NONE.equalsIgnoreCase(inout.getIncludeTaxTab())) {
			MTax tax = MTax.get(Env.getCtx(), inout.getC_Tax_ID());
			if (tax != null) 
				taxRate = tax.getRate();
		} else {
			taxRate = Env.ZERO;
		}
		
		p_Amount = qty.multiply(price);
		//Tinh lai Amount va AmountConvert neu co thue
		if (taxRate.compareTo(Env.ZERO) != 0) {
			p_TaxBase = p_Amount.multiply(Env.ONE.add(taxRate));
			p_TaxAmt = p_TaxBase.subtract(p_Amount);
			mTab.setValue("TaxAmt", p_TaxAmt);
			mTab.setValue("TaxBaseAmt", p_TaxBase);
		}
		mTab.setValue("Amount", p_Amount);
		return "";
	}

	public String fillAmount_Discount (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())	
			return "";

		Object objDiscountType = mTab.getValue(MInOutLine.COLUMNNAME_DiscountType);
		Object objAmount = mTab.getValue(MInOutLine.COLUMNNAME_Amount);
		Object objBaseAmt = mTab.getValue(MInOutLine.COLUMNNAME_TaxBaseAmt);
		
		BigDecimal p_Amount = Env.ZERO;
		
		if (objAmount != null) {
			
		}
		p_Amount = new BigDecimal(objBaseAmt.toString());
		
		if (objDiscountType != null) {
			if (!MInOutLine.DISCOUNTTYPE_NONE.equals(objDiscountType.toString())) {
				mTab.setValue("DiscountAmt", p_Amount);
			} else {
				mTab.setValue("DiscountAmt", Env.ZERO);
			}
		}
		
		return "";
	}
	
}	//	CalloutInOut