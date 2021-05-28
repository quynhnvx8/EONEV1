/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for M_Promotion
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_M_Promotion extends PO implements I_M_Promotion, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210524L;

    /** Standard Constructor */
    public X_M_Promotion (Properties ctx, int M_Promotion_ID, String trxName)
    {
      super (ctx, M_Promotion_ID, trxName);
      /** if (M_Promotion_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_M_Promotion (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_Promotion[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Approved.
		@param Approved Approved	  */
	public void setApproved (String Approved)
	{
		set_Value (COLUMNNAME_Approved, Approved);
	}

	/** Get Approved.
		@return Approved	  */
	public String getApproved () 
	{
		return (String)get_Value(COLUMNNAME_Approved);
	}

	/** Set Canceled.
		@param Canceled Canceled	  */
	public void setCanceled (String Canceled)
	{
		set_Value (COLUMNNAME_Canceled, Canceled);
	}

	/** Get Canceled.
		@return Canceled	  */
	public String getCanceled () 
	{
		return (String)get_Value(COLUMNNAME_Canceled);
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

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Reject = RE */
	public static final String DOCSTATUS_Reject = "RE";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set IsMultiple.
		@param IsMultiple IsMultiple	  */
	public void setIsMultiple (boolean IsMultiple)
	{
		set_Value (COLUMNNAME_IsMultiple, Boolean.valueOf(IsMultiple));
	}

	/** Get IsMultiple.
		@return IsMultiple	  */
	public boolean isMultiple () 
	{
		Object oo = get_Value(COLUMNNAME_IsMultiple);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Pending.
		@param IsPending Pending	  */
	public void setIsPending (boolean IsPending)
	{
		set_Value (COLUMNNAME_IsPending, Boolean.valueOf(IsPending));
	}

	/** Get Pending.
		@return Pending	  */
	public boolean isPending () 
	{
		Object oo = get_Value(COLUMNNAME_IsPending);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsRecursive.
		@param IsRecursive IsRecursive	  */
	public void setIsRecursive (boolean IsRecursive)
	{
		set_Value (COLUMNNAME_IsRecursive, Boolean.valueOf(IsRecursive));
	}

	/** Get IsRecursive.
		@return IsRecursive	  */
	public boolean isRecursive () 
	{
		Object oo = get_Value(COLUMNNAME_IsRecursive);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

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

	public I_M_PromotionType getM_PromotionType() throws RuntimeException
    {
		return (I_M_PromotionType)MTable.get(getCtx(), I_M_PromotionType.Table_Name)
			.getPO(getM_PromotionType_ID(), get_TrxName());	}

	/** Set Promtion Type.
		@param M_PromotionType_ID Promtion Type	  */
	public void setM_PromotionType_ID (int M_PromotionType_ID)
	{
		if (M_PromotionType_ID < 1) 
			set_Value (COLUMNNAME_M_PromotionType_ID, null);
		else 
			set_Value (COLUMNNAME_M_PromotionType_ID, Integer.valueOf(M_PromotionType_ID));
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

	/** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Valid to.
		@param ValidTo 
		Valid to including this date (last day)
	  */
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
	}
}