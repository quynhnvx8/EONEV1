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

/** Generated Model for HR_DayOffLine
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_DayOffLine extends PO implements I_HR_DayOffLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201029L;

    /** Standard Constructor */
    public X_HR_DayOffLine (Properties ctx, int HR_DayOffLine_ID, String trxName)
    {
      super (ctx, HR_DayOffLine_ID, trxName);
      /** if (HR_DayOffLine_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_DayOffLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_DayOffLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Address.
		@param Address Address	  */
	public void setAddress (String Address)
	{
		set_Value (COLUMNNAME_Address, Address);
	}

	/** Get Address.
		@return Address	  */
	public String getAddress () 
	{
		return (String)get_Value(COLUMNNAME_Address);
	}

	/** NP4 = P4 */
	public static final String DAYOFFTYPE_NP4 = "P4";
	/** KLCP = KL */
	public static final String DAYOFFTYPE_KLCP = "KL";
	/** CO = CO */
	public static final String DAYOFFTYPE_CO = "CO";
	/** NL = NL */
	public static final String DAYOFFTYPE_NL = "NL";
	/** NO = NO */
	public static final String DAYOFFTYPE_NO = "NO";
	/** HH = HH */
	public static final String DAYOFFTYPE_HH = "HH";
	/** TS = TS */
	public static final String DAYOFFTYPE_TS = "TS";
	/** W8 = W8 */
	public static final String DAYOFFTYPE_W8 = "W8";
	/** W4 = W4 */
	public static final String DAYOFFTYPE_W4 = "W4";
	/** CT = CT */
	public static final String DAYOFFTYPE_CT = "CT";
	/** HO = HO */
	public static final String DAYOFFTYPE_HO = "HO";
	/** KLKP = KP */
	public static final String DAYOFFTYPE_KLKP = "KP";
	/** WT4 = T4 */
	public static final String DAYOFFTYPE_WT4 = "T4";
	/** WT8 = T8 */
	public static final String DAYOFFTYPE_WT8 = "T8";
	/** WL8 = L8 */
	public static final String DAYOFFTYPE_WL8 = "L8";
	/** WL4 = L4 */
	public static final String DAYOFFTYPE_WL4 = "L4";
	/** NP8 = P8 */
	public static final String DAYOFFTYPE_NP8 = "P8";
	/** Set DayOffType.
		@param DayOffType DayOffType	  */
	public void setDayOffType (String DayOffType)
	{

		set_Value (COLUMNNAME_DayOffType, DayOffType);
	}

	/** Get DayOffType.
		@return DayOffType	  */
	public String getDayOffType () 
	{
		return (String)get_Value(COLUMNNAME_DayOffType);
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

	public I_HR_DayOff getHR_DayOff() throws RuntimeException
    {
		return (I_HR_DayOff)MTable.get(getCtx(), I_HR_DayOff.Table_Name)
			.getPO(getHR_DayOff_ID(), get_TrxName());	}

	/** Set Day Off.
		@param HR_DayOff_ID Day Off	  */
	public void setHR_DayOff_ID (int HR_DayOff_ID)
	{
		if (HR_DayOff_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_DayOff_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_DayOff_ID, Integer.valueOf(HR_DayOff_ID));
	}

	/** Get Day Off.
		@return Day Off	  */
	public int getHR_DayOff_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_DayOff_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Day Off.
		@param HR_DayOffLine_ID Day Off	  */
	public void setHR_DayOffLine_ID (int HR_DayOffLine_ID)
	{
		if (HR_DayOffLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_DayOffLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_DayOffLine_ID, Integer.valueOf(HR_DayOffLine_ID));
	}

	/** Get Day Off.
		@return Day Off	  */
	public int getHR_DayOffLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_DayOffLine_ID);
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
}