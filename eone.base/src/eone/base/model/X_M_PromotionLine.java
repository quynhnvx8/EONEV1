/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for M_PromotionLine
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_M_PromotionLine extends PO implements I_M_PromotionLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210604L;

    /** Standard Constructor */
    public X_M_PromotionLine (Properties ctx, int M_PromotionLine_ID, String trxName)
    {
      super (ctx, M_PromotionLine_ID, trxName);
      /** if (M_PromotionLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_M_PromotionLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_M_PromotionLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Department Multiple.
		@param AD_Department_Multi_ID Department Multiple	  */
	public void setAD_Department_Multi_ID (String AD_Department_Multi_ID)
	{

		set_Value (COLUMNNAME_AD_Department_Multi_ID, AD_Department_Multi_ID);
	}

	/** Get Department Multiple.
		@return Department Multiple	  */
	public String getAD_Department_Multi_ID () 
	{
		return (String)get_Value(COLUMNNAME_AD_Department_Multi_ID);
	}

	/** Set Amount.
		@param Amount 
		Amount in a defined currency
	  */
	public void setAmount (String Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public String getAmount () 
	{
		return (String)get_Value(COLUMNNAME_Amount);
	}

	/** C_BP_Group_ID AD_Reference_ID=200045 */
	public static final int C_BP_GROUP_ID_AD_Reference_ID=200045;
	/** Set Business Partner Group.
		@param C_BP_Group_ID 
		Business Partner Group
	  */
	public void setC_BP_Group_ID (String C_BP_Group_ID)
	{

		set_Value (COLUMNNAME_C_BP_Group_ID, C_BP_Group_ID);
	}

	/** Get Business Partner Group.
		@return Business Partner Group
	  */
	public String getC_BP_Group_ID () 
	{
		return (String)get_Value(COLUMNNAME_C_BP_Group_ID);
	}

	/** Set Discount Amount.
		@param DiscountAmt 
		Calculated amount of discount
	  */
	public void setDiscountAmt (BigDecimal DiscountAmt)
	{
		set_Value (COLUMNNAME_DiscountAmt, DiscountAmt);
	}

	/** Get Discount Amount.
		@return Calculated amount of discount
	  */
	public BigDecimal getDiscountAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DiscountAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DiscountPercent.
		@param DiscountPercent DiscountPercent	  */
	public void setDiscountPercent (BigDecimal DiscountPercent)
	{
		set_Value (COLUMNNAME_DiscountPercent, DiscountPercent);
	}

	/** Get DiscountPercent.
		@return DiscountPercent	  */
	public BigDecimal getDiscountPercent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DiscountPercent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** M_Product_Free_ID AD_Reference_ID=162 */
	public static final int M_PRODUCT_FREE_ID_AD_Reference_ID=162;
	/** Set Product Free.
		@param M_Product_Free_ID Product Free	  */
	public void setM_Product_Free_ID (String M_Product_Free_ID)
	{

		set_Value (COLUMNNAME_M_Product_Free_ID, M_Product_Free_ID);
	}

	/** Get Product Free.
		@return Product Free	  */
	public String getM_Product_Free_ID () 
	{
		return (String)get_Value(COLUMNNAME_M_Product_Free_ID);
	}

	/** M_Product_ID AD_Reference_ID=162 */
	public static final int M_PRODUCT_ID_AD_Reference_ID=162;
	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (String M_Product_ID)
	{

		set_Value (COLUMNNAME_M_Product_ID, M_Product_ID);
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public String getM_Product_ID () 
	{
		return (String)get_Value(COLUMNNAME_M_Product_ID);
	}

	public I_M_Promotion getM_Promotion() throws RuntimeException
    {
		return (I_M_Promotion)MTable.get(getCtx(), I_M_Promotion.Table_Name)
			.getPO(getM_Promotion_ID(), get_TrxName());	}

	/** Set Promotion.
		@param M_Promotion_ID Promotion	  */
	public void setM_Promotion_ID (int M_Promotion_ID)
	{
		if (M_Promotion_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Promotion_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Promotion_ID, Integer.valueOf(M_Promotion_ID));
	}

	/** Get Promotion.
		@return Promotion	  */
	public int getM_Promotion_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Promotion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Promotion Line.
		@param M_PromotionLine_ID Promotion Line	  */
	public void setM_PromotionLine_ID (int M_PromotionLine_ID)
	{
		if (M_PromotionLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_PromotionLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_PromotionLine_ID, Integer.valueOf(M_PromotionLine_ID));
	}

	/** Get Promotion Line.
		@return Promotion Line	  */
	public int getM_PromotionLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PromotionLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set QtyFree.
		@param QtyFree QtyFree	  */
	public void setQtyFree (String QtyFree)
	{
		set_Value (COLUMNNAME_QtyFree, QtyFree);
	}

	/** Get QtyFree.
		@return QtyFree	  */
	public String getQtyFree () 
	{
		return (String)get_Value(COLUMNNAME_QtyFree);
	}

	/** Set Multiplier Quantity.
		@param QtyMultiplier 
		Value to multiply quantities by for generating commissions.
	  */
	public void setQtyMultiplier (String QtyMultiplier)
	{
		set_Value (COLUMNNAME_QtyMultiplier, QtyMultiplier);
	}

	/** Get Multiplier Quantity.
		@return Value to multiply quantities by for generating commissions.
	  */
	public String getQtyMultiplier () 
	{
		return (String)get_Value(COLUMNNAME_QtyMultiplier);
	}
}