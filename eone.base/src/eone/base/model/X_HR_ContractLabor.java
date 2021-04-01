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

/** Generated Model for HR_ContractLabor
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_ContractLabor extends PO implements I_HR_ContractLabor, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200804L;

    /** Standard Constructor */
    public X_HR_ContractLabor (Properties ctx, int HR_ContractLabor_ID, String trxName)
    {
      super (ctx, HR_ContractLabor_ID, trxName);
      /** if (HR_ContractLabor_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_ContractLabor (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_ContractLabor[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Allowance.
		@param Allowance Allowance	  */
	public void setAllowance (BigDecimal Allowance)
	{
		set_Value (COLUMNNAME_Allowance, Allowance);
	}

	/** Get Allowance.
		@return Allowance	  */
	public BigDecimal getAllowance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Allowance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Content.
		@param ContentText Content	  */
	public void setContentText (String ContentText)
	{
		set_Value (COLUMNNAME_ContentText, ContentText);
	}

	/** Get Content.
		@return Content	  */
	public String getContentText () 
	{
		return (String)get_Value(COLUMNNAME_ContentText);
	}

	/** Set DateSign.
		@param DateSign DateSign	  */
	public void setDateSign (Timestamp DateSign)
	{
		set_Value (COLUMNNAME_DateSign, DateSign);
	}

	/** Get DateSign.
		@return DateSign	  */
	public Timestamp getDateSign () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateSign);
	}

	/** Set Date Start.
		@param DateStart 
		Date Start for this Order
	  */
	public void setDateStart (Timestamp DateStart)
	{
		set_Value (COLUMNNAME_DateStart, DateStart);
	}

	/** Get Date Start.
		@return Date Start for this Order
	  */
	public Timestamp getDateStart () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateStart);
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

	/** Set End Time.
		@param EndTime 
		End of the time span
	  */
	public void setEndTime (Timestamp EndTime)
	{
		set_Value (COLUMNNAME_EndTime, EndTime);
	}

	/** Get End Time.
		@return End of the time span
	  */
	public Timestamp getEndTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndTime);
	}

	/** Set ContractLabor.
		@param HR_ContractLabor_ID ContractLabor	  */
	public void setHR_ContractLabor_ID (int HR_ContractLabor_ID)
	{
		if (HR_ContractLabor_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_ContractLabor_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_ContractLabor_ID, Integer.valueOf(HR_ContractLabor_ID));
	}

	/** Get ContractLabor.
		@return ContractLabor	  */
	public int getHR_ContractLabor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ContractLabor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Employee getHR_Employee() throws RuntimeException
    {
		return (I_HR_Employee)MTable.get(getCtx(), I_HR_Employee.Table_Name)
			.getPO(getHR_Employee_ID(), get_TrxName());	}

	/** Set Employee.
		@param HR_Employee_ID Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1) 
			set_Value (COLUMNNAME_HR_Employee_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
	}

	/** Get Employee.
		@return Employee	  */
	public int getHR_Employee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_20() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_20_ID(), get_TrxName());	}

	/** Set Item Line 20.
		@param HR_ItemLine_20_ID Item Line 20	  */
	public void setHR_ItemLine_20_ID (int HR_ItemLine_20_ID)
	{
		if (HR_ItemLine_20_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_20_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_20_ID, Integer.valueOf(HR_ItemLine_20_ID));
	}

	/** Get Item Line 20.
		@return Item Line 20	  */
	public int getHR_ItemLine_20_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_20_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_28() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_28_ID(), get_TrxName());	}

	/** Set Item Line 28.
		@param HR_ItemLine_28_ID Item Line 28	  */
	public void setHR_ItemLine_28_ID (int HR_ItemLine_28_ID)
	{
		if (HR_ItemLine_28_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_28_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_28_ID, Integer.valueOf(HR_ItemLine_28_ID));
	}

	/** Get Item Line 28.
		@return Item Line 28	  */
	public int getHR_ItemLine_28_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_28_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set NumTimes.
		@param NumTimes NumTimes	  */
	public void setNumTimes (BigDecimal NumTimes)
	{
		set_Value (COLUMNNAME_NumTimes, NumTimes);
	}

	/** Get NumTimes.
		@return NumTimes	  */
	public BigDecimal getNumTimes () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_NumTimes);
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

	/** Set SalaryPercent.
		@param SalaryPercent SalaryPercent	  */
	public void setSalaryPercent (BigDecimal SalaryPercent)
	{
		set_Value (COLUMNNAME_SalaryPercent, SalaryPercent);
	}

	/** Get SalaryPercent.
		@return SalaryPercent	  */
	public BigDecimal getSalaryPercent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SalaryPercent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SalaryRate.
		@param SalaryRate SalaryRate	  */
	public void setSalaryRate (BigDecimal SalaryRate)
	{
		set_Value (COLUMNNAME_SalaryRate, SalaryRate);
	}

	/** Get SalaryRate.
		@return SalaryRate	  */
	public BigDecimal getSalaryRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SalaryRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Start Time.
		@param StartTime 
		Time started
	  */
	public void setStartTime (Timestamp StartTime)
	{
		set_Value (COLUMNNAME_StartTime, StartTime);
	}

	/** Get Start Time.
		@return Time started
	  */
	public Timestamp getStartTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime);
	}

	/** Set Code.
		@param Value 
		Code
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Code.
		@return Code
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}