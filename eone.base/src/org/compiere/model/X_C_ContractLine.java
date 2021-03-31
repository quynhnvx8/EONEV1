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

/** Generated Model for C_ContractLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_ContractLine extends PO implements I_C_ContractLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200820L;

    /** Standard Constructor */
    public X_C_ContractLine (Properties ctx, int C_ContractLine_ID, String trxName)
    {
      super (ctx, C_ContractLine_ID, trxName);
      /** if (C_ContractLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_C_ContractLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_ContractLine[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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

	/** Set AmountActual.
		@param AmountActual AmountActual	  */
	public void setAmountActual (BigDecimal AmountActual)
	{
		set_Value (COLUMNNAME_AmountActual, AmountActual);
	}

	/** Get AmountActual.
		@return AmountActual	  */
	public BigDecimal getAmountActual () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountActual);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Contract Line.
		@param C_ContractLine_ID Contract Line	  */
	public void setC_ContractLine_ID (int C_ContractLine_ID)
	{
		if (C_ContractLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ContractLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ContractLine_ID, Integer.valueOf(C_ContractLine_ID));
	}

	/** Get Contract Line.
		@return Contract Line	  */
	public int getC_ContractLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ContractLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DateActual.
		@param DateActual DateActual	  */
	public void setDateActual (Timestamp DateActual)
	{
		set_Value (COLUMNNAME_DateActual, DateActual);
	}

	/** Get DateActual.
		@return DateActual	  */
	public Timestamp getDateActual () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateActual);
	}

	/** Set DatePlan.
		@param DatePlan DatePlan	  */
	public void setDatePlan (Timestamp DatePlan)
	{
		set_Value (COLUMNNAME_DatePlan, DatePlan);
	}

	/** Get DatePlan.
		@return DatePlan	  */
	public Timestamp getDatePlan () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePlan);
	}

	/** Set Duration.
		@param Duration 
		Normal Duration in Duration Unit
	  */
	public void setDuration (BigDecimal Duration)
	{
		set_ValueNoCheck (COLUMNNAME_Duration, Duration);
	}

	/** Get Duration.
		@return Normal Duration in Duration Unit
	  */
	public BigDecimal getDuration () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Duration);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Line.
		@param LineNo 
		Line No
	  */
	public void setLineNo (int LineNo)
	{
		set_Value (COLUMNNAME_LineNo, Integer.valueOf(LineNo));
	}

	/** Get Line.
		@return Line No
	  */
	public int getLineNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNo);
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
}