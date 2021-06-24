/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for M_PromotionType
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_M_PromotionType extends PO implements I_M_PromotionType, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210601L;

    /** Standard Constructor */
    public X_M_PromotionType (Properties ctx, int M_PromotionType_ID, String trxName)
    {
      super (ctx, M_PromotionType_ID, trxName);
      /** if (M_PromotionType_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_M_PromotionType (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_PromotionType[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Promtion Type.
		@param M_PromotionType_ID Promtion Type	  */
	public void setM_PromotionType_ID (int M_PromotionType_ID)
	{
		if (M_PromotionType_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_PromotionType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_PromotionType_ID, Integer.valueOf(M_PromotionType_ID));
	}

	/** Get Promtion Type.
		@return Promtion Type	  */
	public int getM_PromotionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PromotionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** LINE = LINE */
	public static final String PROMOTIONTYPE_LINE = "LINE";
	/** GROUP = GROUP */
	public static final String PROMOTIONTYPE_GROUP = "GROUP";
	/** BUNDLE = BUNDLE */
	public static final String PROMOTIONTYPE_BUNDLE = "BUNDLE";
	/** DOCMT = DOCMT */
	public static final String PROMOTIONTYPE_DOCMT = "DOCMT";
	/** Set Promotion Type.
		@param PromotionType Promotion Type	  */
	public void setPromotionType (String PromotionType)
	{

		set_Value (COLUMNNAME_PromotionType, PromotionType);
	}

	/** Get Promotion Type.
		@return Promotion Type	  */
	public String getPromotionType () 
	{
		return (String)get_Value(COLUMNNAME_PromotionType);
	}

	/** Set Requied Amount.
		@param Requied_Amt Requied Amount	  */
	public void setRequied_Amt (boolean Requied_Amt)
	{
		set_Value (COLUMNNAME_Requied_Amt, Boolean.valueOf(Requied_Amt));
	}

	/** Get Requied Amount.
		@return Requied Amount	  */
	public boolean isRequied_Amt () 
	{
		Object oo = get_Value(COLUMNNAME_Requied_Amt);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Requied Discount.
		@param Requied_Dis Requied Discount	  */
	public void setRequied_Dis (boolean Requied_Dis)
	{
		set_Value (COLUMNNAME_Requied_Dis, Boolean.valueOf(Requied_Dis));
	}

	/** Get Requied Discount.
		@return Requied Discount	  */
	public boolean isRequied_Dis () 
	{
		Object oo = get_Value(COLUMNNAME_Requied_Dis);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Requied Gif.
		@param Requied_Gif Requied Gif	  */
	public void setRequied_Gif (boolean Requied_Gif)
	{
		set_Value (COLUMNNAME_Requied_Gif, Boolean.valueOf(Requied_Gif));
	}

	/** Get Requied Gif.
		@return Requied Gif	  */
	public boolean isRequied_Gif () 
	{
		Object oo = get_Value(COLUMNNAME_Requied_Gif);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Requied Group Product.
		@param Requied_Group Requied Group Product	  */
	public void setRequied_Group (boolean Requied_Group)
	{
		set_Value (COLUMNNAME_Requied_Group, Boolean.valueOf(Requied_Group));
	}

	/** Get Requied Group Product.
		@return Requied Group Product	  */
	public boolean isRequied_Group () 
	{
		Object oo = get_Value(COLUMNNAME_Requied_Group);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Requied Percent.
		@param Requied_Per Requied Percent	  */
	public void setRequied_Per (boolean Requied_Per)
	{
		set_Value (COLUMNNAME_Requied_Per, Boolean.valueOf(Requied_Per));
	}

	/** Get Requied Percent.
		@return Requied Percent	  */
	public boolean isRequied_Per () 
	{
		Object oo = get_Value(COLUMNNAME_Requied_Per);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Requied Qty.
		@param Requied_Qty Requied Qty	  */
	public void setRequied_Qty (boolean Requied_Qty)
	{
		set_Value (COLUMNNAME_Requied_Qty, Boolean.valueOf(Requied_Qty));
	}

	/** Get Requied Qty.
		@return Requied Qty	  */
	public boolean isRequied_Qty () 
	{
		Object oo = get_Value(COLUMNNAME_Requied_Qty);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Code.
		@param Value 
		Code
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Code.
		@return Code
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}