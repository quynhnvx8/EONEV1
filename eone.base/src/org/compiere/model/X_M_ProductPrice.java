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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for M_ProductPrice
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_M_ProductPrice extends PO implements I_M_ProductPrice, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200622L;

    /** Standard Constructor */
    public X_M_ProductPrice (Properties ctx, int M_ProductPrice_ID, String trxName)
    {
      super (ctx, M_ProductPrice_ID, trxName);
      /** if (M_ProductPrice_ID == 0)
        {
			setM_Product_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_ProductPrice (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_ProductPrice[")
        .append(get_ID()).append("]");
      return sb.toString();
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
			set_ValueNoCheck (COLUMNNAME_M_Product_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
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

	/** Set Product Price.
		@param M_ProductPrice_ID 
		Intersection between a Product and a Price List Version
	  */
	public void setM_ProductPrice_ID (int M_ProductPrice_ID)
	{
		if (M_ProductPrice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_ProductPrice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_ProductPrice_ID, Integer.valueOf(M_ProductPrice_ID));
	}

	/** Get Product Price.
		@return Intersection between a Product and a Price List Version
	  */
	public int getM_ProductPrice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ProductPrice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set M_ProductPrice_UU.
		@param M_ProductPrice_UU M_ProductPrice_UU	  */
	public void setM_ProductPrice_UU (String M_ProductPrice_UU)
	{
		set_Value (COLUMNNAME_M_ProductPrice_UU, M_ProductPrice_UU);
	}

	/** Get M_ProductPrice_UU.
		@return M_ProductPrice_UU	  */
	public String getM_ProductPrice_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_ProductPrice_UU);
	}

	/** Set PriceBuy.
		@param PriceBuy PriceBuy	  */
	public void setPriceBuy (BigDecimal PriceBuy)
	{
		set_Value (COLUMNNAME_PriceBuy, PriceBuy);
	}

	/** Get PriceBuy.
		@return PriceBuy	  */
	public BigDecimal getPriceBuy () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceBuy);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PriceSold.
		@param PriceSold PriceSold	  */
	public void setPriceSold (BigDecimal PriceSold)
	{
		set_Value (COLUMNNAME_PriceSold, PriceSold);
	}

	/** Get PriceSold.
		@return PriceSold	  */
	public BigDecimal getPriceSold () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceSold);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
}