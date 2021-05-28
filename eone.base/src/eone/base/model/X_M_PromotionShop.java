/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for M_PromotionShop
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_M_PromotionShop extends PO implements I_M_PromotionShop, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210524L;

    /** Standard Constructor */
    public X_M_PromotionShop (Properties ctx, int M_PromotionShop_ID, String trxName)
    {
      super (ctx, M_PromotionShop_ID, trxName);
      /** if (M_PromotionShop_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_M_PromotionShop (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_PromotionShop[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_Department getAD_Department() throws RuntimeException
    {
		return (I_AD_Department)MTable.get(getCtx(), I_AD_Department.Table_Name)
			.getPO(getAD_Department_ID(), get_TrxName());	}

	/** Set Department.
		@param AD_Department_ID Department	  */
	public void setAD_Department_ID (int AD_Department_ID)
	{
		if (AD_Department_ID < 1) 
			set_Value (COLUMNNAME_AD_Department_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Department_ID, Integer.valueOf(AD_Department_ID));
	}

	/** Get Department.
		@return Department	  */
	public int getAD_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Promotion for Shop.
		@param M_PromotionShop_ID Promotion for Shop	  */
	public void setM_PromotionShop_ID (int M_PromotionShop_ID)
	{
		if (M_PromotionShop_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_PromotionShop_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_PromotionShop_ID, Integer.valueOf(M_PromotionShop_ID));
	}

	/** Get Promotion for Shop.
		@return Promotion for Shop	  */
	public int getM_PromotionShop_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PromotionShop_ID);
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

	/** Set Quantity Max.
		@param Qty_Max Quantity Max	  */
	public void setQty_Max (int Qty_Max)
	{
		set_Value (COLUMNNAME_Qty_Max, Integer.valueOf(Qty_Max));
	}

	/** Get Quantity Max.
		@return Quantity Max	  */
	public int getQty_Max () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Qty_Max);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Quantity Received.
		@param Qty_Received Quantity Received	  */
	public void setQty_Received (int Qty_Received)
	{
		set_Value (COLUMNNAME_Qty_Received, Integer.valueOf(Qty_Received));
	}

	/** Get Quantity Received.
		@return Quantity Received	  */
	public int getQty_Received () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Qty_Received);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}