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
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for C_Locator
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_C_Locator extends PO implements I_C_Locator, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200613L;

    /** Standard Constructor */
    public X_C_Locator (Properties ctx, int C_Locator_ID, String trxName)
    {
      super (ctx, C_Locator_ID, trxName);
      /** if (C_Locator_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_Locator (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_Locator[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Locator.
		@param C_Locator_ID Locator	  */
	public void setC_Locator_ID (int C_Locator_ID)
	{
		if (C_Locator_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Locator_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Locator_ID, Integer.valueOf(C_Locator_ID));
	}

	/** Get Locator.
		@return Locator	  */
	public int getC_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Locator_ID);
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

	public I_C_Locator getParent_Locator() throws RuntimeException
    {
		return (I_C_Locator)MTable.get(getCtx(), I_C_Locator.Table_Name)
			.getPO(getParent_Locator_ID(), get_TrxName());	}

	/** Set Parent Locator.
		@param Parent_Locator_ID Parent Locator	  */
	public void setParent_Locator_ID (int Parent_Locator_ID)
	{
		if (Parent_Locator_ID < 1) 
			set_Value (COLUMNNAME_Parent_Locator_ID, null);
		else 
			set_Value (COLUMNNAME_Parent_Locator_ID, Integer.valueOf(Parent_Locator_ID));
	}

	/** Get Parent Locator.
		@return Parent Locator	  */
	public int getParent_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Parent_Locator_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}