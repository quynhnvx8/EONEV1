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

/** Generated Model for C_TransferPeriodLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_TransferPeriodLine extends PO implements I_C_TransferPeriodLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200721L;

    /** Standard Constructor */
    public X_C_TransferPeriodLine (Properties ctx, int C_TransferPeriodLine_ID, String trxName)
    {
      super (ctx, C_TransferPeriodLine_ID, trxName);
      /** if (C_TransferPeriodLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_TransferPeriodLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_TransferPeriodLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_ElementValue getAccount_Cr() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_Cr_ID(), get_TrxName());	}

	/** Set Account Cr.
		@param Account_Cr_ID 
		Account Cr
	  */
	public void setAccount_Cr_ID (int Account_Cr_ID)
	{
		if (Account_Cr_ID < 1) 
			set_Value (COLUMNNAME_Account_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_Account_Cr_ID, Integer.valueOf(Account_Cr_ID));
	}

	/** Get Account Cr.
		@return Account Cr
	  */
	public int getAccount_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getAccount_Dr() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_Dr_ID(), get_TrxName());	}

	/** Set Account Dr.
		@param Account_Dr_ID 
		Account Dr
	  */
	public void setAccount_Dr_ID (int Account_Dr_ID)
	{
		if (Account_Dr_ID < 1) 
			set_Value (COLUMNNAME_Account_Dr_ID, null);
		else 
			set_Value (COLUMNNAME_Account_Dr_ID, Integer.valueOf(Account_Dr_ID));
	}

	/** Get Account Dr.
		@return Account Dr
	  */
	public int getAccount_Dr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_Dr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Amount.
		@param Amount 
		Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmountConvert.
		@param AmountConvert AmountConvert	  */
	public void setAmountConvert (BigDecimal AmountConvert)
	{
		set_Value (COLUMNNAME_AmountConvert, AmountConvert);
	}

	/** Get AmountConvert.
		@return AmountConvert	  */
	public BigDecimal getAmountConvert () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountConvert);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_TransferPeriod getC_TransferPeriod() throws RuntimeException
    {
		return (I_C_TransferPeriod)MTable.get(getCtx(), I_C_TransferPeriod.Table_Name)
			.getPO(getC_TransferPeriod_ID(), get_TrxName());	}

	/** Set Transfer Period.
		@param C_TransferPeriod_ID Transfer Period	  */
	public void setC_TransferPeriod_ID (int C_TransferPeriod_ID)
	{
		if (C_TransferPeriod_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_TransferPeriod_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TransferPeriod_ID, Integer.valueOf(C_TransferPeriod_ID));
	}

	/** Get Transfer Period.
		@return Transfer Period	  */
	public int getC_TransferPeriod_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TransferPeriod_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Transfer Period Line.
		@param C_TransferPeriodLine_ID Transfer Period Line	  */
	public void setC_TransferPeriodLine_ID (int C_TransferPeriodLine_ID)
	{
		if (C_TransferPeriodLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_TransferPeriodLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TransferPeriodLine_ID, Integer.valueOf(C_TransferPeriodLine_ID));
	}

	/** Get Transfer Period Line.
		@return Transfer Period Line	  */
	public int getC_TransferPeriodLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TransferPeriodLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_TypeCost getC_TypeCost() throws RuntimeException
    {
		return (I_C_TypeCost)MTable.get(getCtx(), I_C_TypeCost.Table_Name)
			.getPO(getC_TypeCost_ID(), get_TrxName());	}

	/** Set TypeCost.
		@param C_TypeCost_ID TypeCost	  */
	public void setC_TypeCost_ID (int C_TypeCost_ID)
	{
		if (C_TypeCost_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_TypeCost_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TypeCost_ID, Integer.valueOf(C_TypeCost_ID));
	}

	/** Get TypeCost.
		@return TypeCost	  */
	public int getC_TypeCost_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TypeCost_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_TypeRevenue getC_TypeRevenue() throws RuntimeException
    {
		return (I_C_TypeRevenue)MTable.get(getCtx(), I_C_TypeRevenue.Table_Name)
			.getPO(getC_TypeRevenue_ID(), get_TrxName());	}

	/** Set Type Revenue.
		@param C_TypeRevenue_ID Type Revenue	  */
	public void setC_TypeRevenue_ID (int C_TypeRevenue_ID)
	{
		if (C_TypeRevenue_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_TypeRevenue_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_TypeRevenue_ID, Integer.valueOf(C_TypeRevenue_ID));
	}

	/** Get Type Revenue.
		@return Type Revenue	  */
	public int getC_TypeRevenue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TypeRevenue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Product getM_Product_Cr() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_Cr_ID(), get_TrxName());	}

	/** Set Product Cr.
		@param M_Product_Cr_ID Product Cr	  */
	public void setM_Product_Cr_ID (int M_Product_Cr_ID)
	{
		if (M_Product_Cr_ID < 1) 
			set_Value (COLUMNNAME_M_Product_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_Cr_ID, Integer.valueOf(M_Product_Cr_ID));
	}

	/** Get Product Cr.
		@return Product Cr	  */
	public int getM_Product_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Cr_ID);
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
}