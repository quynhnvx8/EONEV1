/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for M_PromotionCus
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_M_PromotionCus extends PO implements I_M_PromotionCus, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210524L;

    /** Standard Constructor */
    public X_M_PromotionCus (Properties ctx, int M_PromotionCus_ID, String trxName)
    {
      super (ctx, M_PromotionCus_ID, trxName);
      /** if (M_PromotionCus_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_M_PromotionCus (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_PromotionCus[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_C_BP_Group getC_BP_Group() throws RuntimeException
    {
		return (eone.base.model.I_C_BP_Group)MTable.get(getCtx(), eone.base.model.I_C_BP_Group.Table_Name)
			.getPO(getC_BP_Group_ID(), get_TrxName());	}

	/** Set Business Partner Group.
		@param C_BP_Group_ID 
		Business Partner Group
	  */
	public void setC_BP_Group_ID (int C_BP_Group_ID)
	{
		if (C_BP_Group_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BP_Group_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BP_Group_ID, Integer.valueOf(C_BP_Group_ID));
	}

	/** Get Business Partner Group.
		@return Business Partner Group
	  */
	public int getC_BP_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BP_Group_ID);
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

	/** Set Promotion for Customer.
		@param M_PromotionCus_ID Promotion for Customer	  */
	public void setM_PromotionCus_ID (int M_PromotionCus_ID)
	{
		if (M_PromotionCus_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_PromotionCus_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_PromotionCus_ID, Integer.valueOf(M_PromotionCus_ID));
	}

	/** Get Promotion for Customer.
		@return Promotion for Customer	  */
	public int getM_PromotionCus_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PromotionCus_ID);
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
}