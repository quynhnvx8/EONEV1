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

/** Generated Model for A_Asset_Tools
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_A_Asset_Tools extends PO implements I_A_Asset_Tools, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200725L;

    /** Standard Constructor */
    public X_A_Asset_Tools (Properties ctx, int A_Asset_Tools_ID, String trxName)
    {
      super (ctx, A_Asset_Tools_ID, trxName);
      /** if (A_Asset_Tools_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_A_Asset_Tools (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_A_Asset_Tools[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Asset Tools.
		@param A_Asset_Tools_ID Asset Tools	  */
	public void setA_Asset_Tools_ID (int A_Asset_Tools_ID)
	{
		if (A_Asset_Tools_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_Tools_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_Tools_ID, Integer.valueOf(A_Asset_Tools_ID));
	}

	/** Get Asset Tools.
		@return Asset Tools	  */
	public int getA_Asset_Tools_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_Tools_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_ValueNoCheck (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Up Asset = UPA */
	public static final String TOOLSTYPE_UpAsset = "UPA";
	/** Down Asset = DWA */
	public static final String TOOLSTYPE_DownAsset = "DWA";
	/** New Asset = NEA */
	public static final String TOOLSTYPE_NewAsset = "NEA";
	/** Set ToolsType.
		@param ToolsType ToolsType	  */
	public void setToolsType (String ToolsType)
	{

		set_Value (COLUMNNAME_ToolsType, ToolsType);
	}

	/** Get ToolsType.
		@return ToolsType	  */
	public String getToolsType () 
	{
		return (String)get_Value(COLUMNNAME_ToolsType);
	}
}