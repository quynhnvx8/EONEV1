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
import java.util.Properties;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_SafetyStockLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_M_SafetyStockLine extends PO implements I_M_SafetyStockLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201224L;

    /** Standard Constructor */
    public X_M_SafetyStockLine (Properties ctx, int M_SafetyStockLine_ID, String trxName)
    {
      super (ctx, M_SafetyStockLine_ID, trxName);
      /** if (M_SafetyStockLine_ID == 0)
        {
			setC_Period_ID (0);
			setM_Product_ID (0);
			setM_SafetyStockLine_ID (0);
			setQty (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_M_SafetyStockLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_M_SafetyStockLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Period_ID()));
    }

	/** CAT A = A */
	public static final String CATEGORY_CATA = "A";
	/** CAT B = B */
	public static final String CATEGORY_CATB = "B";
	/** CAT C = C */
	public static final String CATEGORY_CATC = "C";
	/** CAT D = D */
	public static final String CATEGORY_CATD = "D";
	/** Set Category.
		@param Category Category	  */
	public void setCategory (String Category)
	{

		set_Value (COLUMNNAME_Category, Category);
	}

	/** Get Category.
		@return Category	  */
	public String getCategory () 
	{
		return (String)get_Value(COLUMNNAME_Category);
	}

	/** Set Delivery Days.
		@param DeliveryDays 
		Number of Days (planned) until Delivery
	  */
	public void setDeliveryDays (int DeliveryDays)
	{
		set_Value (COLUMNNAME_DeliveryDays, Integer.valueOf(DeliveryDays));
	}

	/** Get Delivery Days.
		@return Number of Days (planned) until Delivery
	  */
	public int getDeliveryDays () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DeliveryDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_M_SafetyStock getM_SafetyStock() throws RuntimeException
    {
		return (I_M_SafetyStock)MTable.get(getCtx(), I_M_SafetyStock.Table_Name)
			.getPO(getM_SafetyStock_ID(), get_TrxName());	}

	/** Set SafetyStock.
		@param M_SafetyStock_ID SafetyStock	  */
	public void setM_SafetyStock_ID (int M_SafetyStock_ID)
	{
		if (M_SafetyStock_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_SafetyStock_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_SafetyStock_ID, Integer.valueOf(M_SafetyStock_ID));
	}

	/** Get SafetyStock.
		@return SafetyStock	  */
	public int getM_SafetyStock_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_SafetyStock_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line.
		@param M_SafetyStockLine_ID Line	  */
	public void setM_SafetyStockLine_ID (int M_SafetyStockLine_ID)
	{
		if (M_SafetyStockLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_SafetyStockLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_SafetyStockLine_ID, Integer.valueOf(M_SafetyStockLine_ID));
	}

	/** Get Line.
		@return Line	  */
	public int getM_SafetyStockLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_SafetyStockLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Minimum Quantity.
		@param MinQty 
		Minimum quantity for the business partner
	  */
	public void setMinQty (BigDecimal MinQty)
	{
		set_Value (COLUMNNAME_MinQty, MinQty);
	}

	/** Get Minimum Quantity.
		@return Minimum quantity for the business partner
	  */
	public BigDecimal getMinQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set NextDelivery.
		@param NextDelivery NextDelivery	  */
	public void setNextDelivery (int NextDelivery)
	{
		set_Value (COLUMNNAME_NextDelivery, Integer.valueOf(NextDelivery));
	}

	/** Get NextDelivery.
		@return NextDelivery	  */
	public int getNextDelivery () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NextDelivery);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Percent.
		@param Percent 
		Percentage
	  */
	public void setPercent (BigDecimal Percent)
	{
		set_Value (COLUMNNAME_Percent, Percent);
	}

	/** Get Percent.
		@return Percentage
	  */
	public BigDecimal getPercent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Quantity.
		@param Qty 
		Quantity
	  */
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}