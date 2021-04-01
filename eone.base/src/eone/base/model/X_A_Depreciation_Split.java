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

/** Generated Model for A_Depreciation_Split
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_A_Depreciation_Split extends PO implements I_A_Depreciation_Split, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200830L;

    /** Standard Constructor */
    public X_A_Depreciation_Split (Properties ctx, int A_Depreciation_Split_ID, String trxName)
    {
      super (ctx, A_Depreciation_Split_ID, trxName);
      /** if (A_Depreciation_Split_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_A_Depreciation_Split (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_A_Depreciation_Split[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_A_Asset getA_Asset() throws RuntimeException
    {
		return (eone.base.model.I_A_Asset)MTable.get(getCtx(), eone.base.model.I_A_Asset.Table_Name)
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

	/** Set Depreciation Split.
		@param A_Depreciation_Split_ID Depreciation Split	  */
	public void setA_Depreciation_Split_ID (int A_Depreciation_Split_ID)
	{
		if (A_Depreciation_Split_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Depreciation_Split_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Depreciation_Split_ID, Integer.valueOf(A_Depreciation_Split_ID));
	}

	/** Get Depreciation Split.
		@return Depreciation Split	  */
	public int getA_Depreciation_Split_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Depreciation_Split_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_C_ElementValue getAccount() throws RuntimeException
    {
		return (eone.base.model.I_C_ElementValue)MTable.get(getCtx(), eone.base.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_ID(), get_TrxName());	}

	/** Set Account.
		@param Account_ID 
		Account used
	  */
	public void setAccount_ID (int Account_ID)
	{
		if (Account_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Account_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Account_ID, Integer.valueOf(Account_ID));
	}

	/** Get Account.
		@return Account used
	  */
	public int getAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_ID);
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

	/** Set Percent.
		@param Percent 
		Percentage
	  */
	public void setPercent (int Percent)
	{
		set_Value (COLUMNNAME_Percent, Integer.valueOf(Percent));
	}

	/** Get Percent.
		@return Percentage
	  */
	public int getPercent () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Percent);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}