/******************************************************************************
 * Product: EOoe ERP & CRM Smart Business Solution	                        *
 * Copyright (C) 2020, Inc. All Rights Reserved.				                *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_PrintFormat
 *  @author EOne (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_AD_PrintFormat extends PO implements I_AD_PrintFormat, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210701L;

    /** Standard Constructor */
    public X_AD_PrintFormat (Properties ctx, int AD_PrintFormat_ID, String trxName)
    {
      super (ctx, AD_PrintFormat_ID, trxName);
      /** if (AD_PrintFormat_ID == 0)
        {
			setAD_PrintColor_ID (0);
			setAD_PrintFont_ID (0);
			setAD_PrintFormat_ID (0);
// 0
			setAD_PrintPaper_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AD_PrintFormat (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
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
      StringBuilder sb = new StringBuilder ("X_AD_PrintFormat[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_AD_PrintColor getAD_PrintColor() throws RuntimeException
    {
		return (eone.base.model.I_AD_PrintColor)MTable.get(getCtx(), eone.base.model.I_AD_PrintColor.Table_Name)
			.getPO(getAD_PrintColor_ID(), get_TrxName());	}

	/** Set Print Color.
		@param AD_PrintColor_ID 
		Color used for printing and display
	  */
	public void setAD_PrintColor_ID (int AD_PrintColor_ID)
	{
		if (AD_PrintColor_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintColor_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintColor_ID, Integer.valueOf(AD_PrintColor_ID));
	}

	/** Get Print Color.
		@return Color used for printing and display
	  */
	public int getAD_PrintColor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintColor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_PrintFont getAD_PrintFont() throws RuntimeException
    {
		return (eone.base.model.I_AD_PrintFont)MTable.get(getCtx(), eone.base.model.I_AD_PrintFont.Table_Name)
			.getPO(getAD_PrintFont_ID(), get_TrxName());	}

	/** Set Print Font.
		@param AD_PrintFont_ID 
		Maintain Print Font
	  */
	public void setAD_PrintFont_ID (int AD_PrintFont_ID)
	{
		if (AD_PrintFont_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintFont_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintFont_ID, Integer.valueOf(AD_PrintFont_ID));
	}

	/** Get Print Font.
		@return Maintain Print Font
	  */
	public int getAD_PrintFont_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFont_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Print Format.
		@param AD_PrintFormat_ID 
		Data Print Format
	  */
	public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
	{
		if (AD_PrintFormat_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PrintFormat_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PrintFormat_ID, Integer.valueOf(AD_PrintFormat_ID));
	}

	/** Get Print Format.
		@return Data Print Format
	  */
	public int getAD_PrintFormat_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintFormat_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_AD_PrintPaper getAD_PrintPaper() throws RuntimeException
    {
		return (eone.base.model.I_AD_PrintPaper)MTable.get(getCtx(), eone.base.model.I_AD_PrintPaper.Table_Name)
			.getPO(getAD_PrintPaper_ID(), get_TrxName());	}

	/** Set Print Paper.
		@param AD_PrintPaper_ID 
		Printer paper definition
	  */
	public void setAD_PrintPaper_ID (int AD_PrintPaper_ID)
	{
		if (AD_PrintPaper_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintPaper_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintPaper_ID, Integer.valueOf(AD_PrintPaper_ID));
	}

	/** Get Print Paper.
		@return Printer paper definition
	  */
	public int getAD_PrintPaper_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintPaper_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Create Copy.
		@param CreateCopy Create Copy	  */
	public void setCreateCopy (String CreateCopy)
	{
		set_Value (COLUMNNAME_CreateCopy, CreateCopy);
	}

	/** Get Create Copy.
		@return Create Copy	  */
	public String getCreateCopy () 
	{
		return (String)get_Value(COLUMNNAME_CreateCopy);
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

	/** Set Footer Margin.
		@param FooterMargin 
		Margin of the Footer in 1/72 of an inch
	  */
	public void setFooterMargin (int FooterMargin)
	{
		set_Value (COLUMNNAME_FooterMargin, Integer.valueOf(FooterMargin));
	}

	/** Get Footer Margin.
		@return Margin of the Footer in 1/72 of an inch
	  */
	public int getFooterMargin () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FooterMargin);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Header Margin.
		@param HeaderMargin 
		Margin of the Header in 1/72 of an inch
	  */
	public void setHeaderMargin (int HeaderMargin)
	{
		set_Value (COLUMNNAME_HeaderMargin, Integer.valueOf(HeaderMargin));
	}

	/** Get Header Margin.
		@return Margin of the Header in 1/72 of an inch
	  */
	public int getHeaderMargin () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HeaderMargin);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }
}