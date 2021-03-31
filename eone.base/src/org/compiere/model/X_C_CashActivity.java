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

/** Generated Model for C_CashActivity
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_C_CashActivity extends PO implements I_C_CashActivity, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200615L;

    /** Standard Constructor */
    public X_C_CashActivity (Properties ctx, int C_CashActivity_ID, String trxName)
    {
      super (ctx, C_CashActivity_ID, trxName);
      /** if (C_CashActivity_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_CashActivity (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_CashActivity[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set C_CashActivity.
		@param C_CashActivity_ID C_CashActivity	  */
	public void setC_CashActivity_ID (int C_CashActivity_ID)
	{
		if (C_CashActivity_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_CashActivity_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_CashActivity_ID, Integer.valueOf(C_CashActivity_ID));
	}

	/** Get C_CashActivity.
		@return C_CashActivity	  */
	public int getC_CashActivity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CashActivity_ID);
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