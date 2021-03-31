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

/** Generated Model for HR_Education
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_Education extends PO implements I_HR_Education, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200804L;

    /** Standard Constructor */
    public X_HR_Education (Properties ctx, int HR_Education_ID, String trxName)
    {
      super (ctx, HR_Education_ID, trxName);
      /** if (HR_Education_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_Education (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_Education[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Education.
		@param HR_Education_ID Education	  */
	public void setHR_Education_ID (int HR_Education_ID)
	{
		if (HR_Education_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Education_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Education_ID, Integer.valueOf(HR_Education_ID));
	}

	/** Get Education.
		@return Education	  */
	public int getHR_Education_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Education_ID);
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

	public I_HR_ItemLine getHR_ItemLine_08() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_08_ID(), get_TrxName());	}

	/** Set Specialized training.
		@param HR_ItemLine_08_ID Specialized training	  */
	public void setHR_ItemLine_08_ID (int HR_ItemLine_08_ID)
	{
		if (HR_ItemLine_08_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_08_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_08_ID, Integer.valueOf(HR_ItemLine_08_ID));
	}

	/** Get Specialized training.
		@return Specialized training	  */
	public int getHR_ItemLine_08_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_08_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_10() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_10_ID(), get_TrxName());	}

	/** Set Address Training.
		@param HR_ItemLine_10_ID Address Training	  */
	public void setHR_ItemLine_10_ID (int HR_ItemLine_10_ID)
	{
		if (HR_ItemLine_10_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_10_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_10_ID, Integer.valueOf(HR_ItemLine_10_ID));
	}

	/** Get Address Training.
		@return Address Training	  */
	public int getHR_ItemLine_10_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_10_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_12() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_12_ID(), get_TrxName());	}

	/** Set Training Form.
		@param HR_ItemLine_12_ID Training Form	  */
	public void setHR_ItemLine_12_ID (int HR_ItemLine_12_ID)
	{
		if (HR_ItemLine_12_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_12_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_12_ID, Integer.valueOf(HR_ItemLine_12_ID));
	}

	/** Get Training Form.
		@return Training Form	  */
	public int getHR_ItemLine_12_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_12_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_18() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_18_ID(), get_TrxName());	}

	/** Set Training Level.
		@param HR_ItemLine_18_ID Training Level	  */
	public void setHR_ItemLine_18_ID (int HR_ItemLine_18_ID)
	{
		if (HR_ItemLine_18_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_18_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_18_ID, Integer.valueOf(HR_ItemLine_18_ID));
	}

	/** Get Training Level.
		@return Training Level	  */
	public int getHR_ItemLine_18_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_18_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_25() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_25_ID(), get_TrxName());	}

	/** Set Training Sector.
		@param HR_ItemLine_25_ID Training Sector	  */
	public void setHR_ItemLine_25_ID (int HR_ItemLine_25_ID)
	{
		if (HR_ItemLine_25_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_25_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_25_ID, Integer.valueOf(HR_ItemLine_25_ID));
	}

	/** Get Training Sector.
		@return Training Sector	  */
	public int getHR_ItemLine_25_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_25_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_29() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_29_ID(), get_TrxName());	}

	/** Set National.
		@param HR_ItemLine_29_ID National	  */
	public void setHR_ItemLine_29_ID (int HR_ItemLine_29_ID)
	{
		if (HR_ItemLine_29_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_29_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_29_ID, Integer.valueOf(HR_ItemLine_29_ID));
	}

	/** Get National.
		@return National	  */
	public int getHR_ItemLine_29_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_29_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_ItemLine getHR_ItemLine_38() throws RuntimeException
    {
		return (I_HR_ItemLine)MTable.get(getCtx(), I_HR_ItemLine.Table_Name)
			.getPO(getHR_ItemLine_38_ID(), get_TrxName());	}

	/** Set Ranked Graduate.
		@param HR_ItemLine_38_ID Ranked Graduate	  */
	public void setHR_ItemLine_38_ID (int HR_ItemLine_38_ID)
	{
		if (HR_ItemLine_38_ID < 1) 
			set_Value (COLUMNNAME_HR_ItemLine_38_ID, null);
		else 
			set_Value (COLUMNNAME_HR_ItemLine_38_ID, Integer.valueOf(HR_ItemLine_38_ID));
	}

	/** Get Ranked Graduate.
		@return Ranked Graduate	  */
	public int getHR_ItemLine_38_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_ItemLine_38_ID);
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