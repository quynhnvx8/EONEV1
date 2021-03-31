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

/** Generated Model for HR_Working
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_Working extends PO implements I_HR_Working, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201225L;

    /** Standard Constructor */
    public X_HR_Working (Properties ctx, int HR_Working_ID, String trxName)
    {
      super (ctx, HR_Working_ID, trxName);
      /** if (HR_Working_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_Working (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_Working[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_Department getAD_Department() throws RuntimeException
    {
		return (I_AD_Department)MTable.get(getCtx(), I_AD_Department.Table_Name)
			.getPO(getAD_Department_ID(), get_TrxName());	}

	/** Set Department.
		@param AD_Department_ID Department	  */
	public void setAD_Department_ID (int AD_Department_ID)
	{
		if (AD_Department_ID < 1) 
			set_Value (COLUMNNAME_AD_Department_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Department_ID, Integer.valueOf(AD_Department_ID));
	}

	/** Get Department.
		@return Department	  */
	public int getAD_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate)
	{
		set_Value (COLUMNNAME_EndDate, EndDate);
	}

	/** Get End Date.
		@return Last effective date (inclusive)
	  */
	public Timestamp getEndDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDate);
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

	public I_HR_ItemLine getHR_ItemLine_05() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_05_ID(), get_TrxName());	}

	/** Set Jobs.
		@param HR_ItemLine_05_ID Jobs	  */
	public void setHR_ItemLine_05_ID (int HR_ItemLine_05_ID)
	{
		if (HR_ItemLine_05_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_05_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_05_ID, Integer.valueOf(HR_ItemLine_05_ID));
	}

	/** Get Jobs.
		@return Jobs	  */
	public int getHR_ItemLine_05_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_05_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_06() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_06_ID(), get_TrxName());	}

	/** Set Position.
		@param HR_ItemLine_06_ID Position	  */
	public void setHR_ItemLine_06_ID (int HR_ItemLine_06_ID)
	{
		if (HR_ItemLine_06_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_06_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_06_ID, Integer.valueOf(HR_ItemLine_06_ID));
	}

	/** Get Position.
		@return Position	  */
	public int getHR_ItemLine_06_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_06_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Working.
		@param HR_Working_ID Working	  */
	public void setHR_Working_ID (int HR_Working_ID)
	{
		if (HR_Working_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Working_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Working_ID, Integer.valueOf(HR_Working_ID));
	}

	/** Get Working.
		@return Working	  */
	public int getHR_Working_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Working_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Selected.
		@param IsSelected Selected	  */
	public void setIsSelected (boolean IsSelected)
	{
		set_Value (COLUMNNAME_IsSelected, Boolean.valueOf(IsSelected));
	}

	/** Get Selected.
		@return Selected	  */
	public boolean isSelected () 
	{
		Object oo = get_Value(COLUMNNAME_IsSelected);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
	}
}