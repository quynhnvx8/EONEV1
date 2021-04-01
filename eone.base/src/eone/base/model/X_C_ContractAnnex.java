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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for C_ContractAnnex
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_ContractAnnex extends PO implements I_C_ContractAnnex, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200820L;

    /** Standard Constructor */
    public X_C_ContractAnnex (Properties ctx, int C_ContractAnnex_ID, String trxName)
    {
      super (ctx, C_ContractAnnex_ID, trxName);
      /** if (C_ContractAnnex_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_ContractAnnex (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_ContractAnnex[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Contract getC_Contract() throws RuntimeException
    {
		return (I_C_Contract)MTable.get(getCtx(), I_C_Contract.Table_Name)
			.getPO(getC_Contract_ID(), get_TrxName());	}

	/** Set Contract.
		@param C_Contract_ID Contract	  */
	public void setC_Contract_ID (int C_Contract_ID)
	{
		if (C_Contract_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Contract_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Contract_ID, Integer.valueOf(C_Contract_ID));
	}

	/** Get Contract.
		@return Contract	  */
	public int getC_Contract_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_ContractAnnex.
		@param C_ContractAnnex_ID C_ContractAnnex	  */
	public void setC_ContractAnnex_ID (int C_ContractAnnex_ID)
	{
		if (C_ContractAnnex_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ContractAnnex_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ContractAnnex_ID, Integer.valueOf(C_ContractAnnex_ID));
	}

	/** Get C_ContractAnnex.
		@return C_ContractAnnex	  */
	public int getC_ContractAnnex_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ContractAnnex_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date From.
		@param DateFrom 
		Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Date To.
		@param DateTo 
		End date of a date range
	  */
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	public Timestamp getDateTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
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

	/** Set Rate.
		@param Rate 
		Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate)
	{
		set_ValueNoCheck (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set RateOver.
		@param RateOver RateOver	  */
	public void setRateOver (BigDecimal RateOver)
	{
		set_ValueNoCheck (COLUMNNAME_RateOver, RateOver);
	}

	/** Get RateOver.
		@return RateOver	  */
	public BigDecimal getRateOver () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_RateOver);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Usable Life - Months.
		@param UseLifeMonths 
		Months of the usable life of the asset
	  */
	public void setUseLifeMonths (BigDecimal UseLifeMonths)
	{
		set_Value (COLUMNNAME_UseLifeMonths, UseLifeMonths);
	}

	/** Get Usable Life - Months.
		@return Months of the usable life of the asset
	  */
	public BigDecimal getUseLifeMonths () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_UseLifeMonths);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}