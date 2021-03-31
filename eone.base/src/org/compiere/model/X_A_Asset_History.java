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
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for A_Asset_History
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_A_Asset_History extends PO implements I_A_Asset_History, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200905L;

    /** Standard Constructor */
    public X_A_Asset_History (Properties ctx, int A_Asset_History_ID, String trxName)
    {
      super (ctx, A_Asset_History_ID, trxName);
      /** if (A_Asset_History_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_A_Asset_History (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_A_Asset_History[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Asset History.
		@param A_Asset_History_ID Asset History	  */
	public void setA_Asset_History_ID (int A_Asset_History_ID)
	{
		if (A_Asset_History_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_History_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_History_ID, Integer.valueOf(A_Asset_History_ID));
	}

	/** Get Asset History.
		@return Asset History	  */
	public int getA_Asset_History_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_History_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_A_Asset getA_Asset() throws RuntimeException
    {
		return (org.compiere.model.I_A_Asset)MTable.get(getCtx(), org.compiere.model.I_A_Asset.Table_Name)
			.getPO(getA_Asset_ID(), get_TrxName());	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ChangeDate.
		@param ChangeDate ChangeDate	  */
	public void setChangeDate (Timestamp ChangeDate)
	{
		set_Value (COLUMNNAME_ChangeDate, ChangeDate);
	}

	/** Get ChangeDate.
		@return ChangeDate	  */
	public Timestamp getChangeDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ChangeDate);
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
}