/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for HR_ItemLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_ItemLine extends PO implements I_HR_ItemLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200804L;

    /** Standard Constructor */
    public X_HR_ItemLine (Properties ctx, int HR_ItemLine_ID, String trxName)
    {
      super (ctx, HR_ItemLine_ID, trxName);
      /** if (HR_ItemLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_ItemLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_ItemLine[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public I_HR_Item getHR_Item() throws RuntimeException
    {
		return (I_HR_Item)MTable.get(getCtx(), I_HR_Item.Table_Name)
			.getPO(getHR_Item_ID(), get_TrxName());	}

	/** Set Item.
		@param HR_Item_ID Item	  */
	public void setHR_Item_ID (int HR_Item_ID)
	{
		if (HR_Item_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Item_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Item_ID, Integer.valueOf(HR_Item_ID));
	}

	/** Get Item.
		@return Item	  */
	public int getHR_Item_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Item_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Item Line.
		@param HR_ItemLine_ID Item Line	  */
	public void setHR_ItemLine_ID (int HR_ItemLine_ID)
	{
		if (HR_ItemLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_ItemLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_ItemLine_ID, Integer.valueOf(HR_ItemLine_ID));
	}

	/** Get Item Line.
		@return Item Line	  */
	public int getHR_ItemLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_ID);
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