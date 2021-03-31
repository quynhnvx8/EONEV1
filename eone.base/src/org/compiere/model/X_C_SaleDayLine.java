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
import org.compiere.util.KeyNamePair;

/** Generated Model for C_SaleDayLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_C_SaleDayLine extends PO implements I_C_SaleDayLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201224L;

    /** Standard Constructor */
    public X_C_SaleDayLine (Properties ctx, int C_SaleDayLine_ID, String trxName)
    {
      super (ctx, C_SaleDayLine_ID, trxName);
      /** if (C_SaleDayLine_ID == 0)
        {
			setC_Period_ID (0);
			setC_SaleDayLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_SaleDayLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_C_SaleDayLine[")
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

	public I_C_SaleDay getC_SaleDay() throws RuntimeException
    {
		return (I_C_SaleDay)MTable.get(getCtx(), I_C_SaleDay.Table_Name)
			.getPO(getC_SaleDay_ID(), get_TrxName());	}

	/** Set Sale Days.
		@param C_SaleDay_ID Sale Days	  */
	public void setC_SaleDay_ID (int C_SaleDay_ID)
	{
		if (C_SaleDay_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_SaleDay_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_SaleDay_ID, Integer.valueOf(C_SaleDay_ID));
	}

	/** Get Sale Days.
		@return Sale Days	  */
	public int getC_SaleDay_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SaleDay_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sale Day Line.
		@param C_SaleDayLine_ID Sale Day Line	  */
	public void setC_SaleDayLine_ID (int C_SaleDayLine_ID)
	{
		if (C_SaleDayLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_SaleDayLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_SaleDayLine_ID, Integer.valueOf(C_SaleDayLine_ID));
	}

	/** Get Sale Day Line.
		@return Sale Day Line	  */
	public int getC_SaleDayLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SaleDayLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DayOfMonth.
		@param DayOfMonth DayOfMonth	  */
	public void setDayOfMonth (int DayOfMonth)
	{
		set_Value (COLUMNNAME_DayOfMonth, Integer.valueOf(DayOfMonth));
	}

	/** Get DayOfMonth.
		@return DayOfMonth	  */
	public int getDayOfMonth () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DayOfMonth);
		if (ii == null)
			 return 0;
		return ii.intValue();
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