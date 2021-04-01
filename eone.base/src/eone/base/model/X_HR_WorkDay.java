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
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for HR_WorkDay
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_WorkDay extends PO implements I_HR_WorkDay, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201026L;

    /** Standard Constructor */
    public X_HR_WorkDay (Properties ctx, int HR_WorkDay_ID, String trxName)
    {
      super (ctx, HR_WorkDay_ID, trxName);
      /** if (HR_WorkDay_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_WorkDay (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_WorkDay[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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

	/** Set End Time1.
		@param EndTime1 
		End of the time span
	  */
	public void setEndTime1 (Timestamp EndTime1)
	{
		set_Value (COLUMNNAME_EndTime1, EndTime1);
	}

	/** Get End Time1.
		@return End of the time span
	  */
	public Timestamp getEndTime1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndTime1);
	}

	/** Set End Time2.
		@param EndTime2 
		End of the time span
	  */
	public void setEndTime2 (Timestamp EndTime2)
	{
		set_Value (COLUMNNAME_EndTime2, EndTime2);
	}

	/** Get End Time2.
		@return End of the time span
	  */
	public Timestamp getEndTime2 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndTime2);
	}

	/** Set End Time3.
		@param EndTime3 
		End of the time span
	  */
	public void setEndTime3 (Timestamp EndTime3)
	{
		set_Value (COLUMNNAME_EndTime3, EndTime3);
	}

	/** Get End Time3.
		@return End of the time span
	  */
	public Timestamp getEndTime3 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndTime3);
	}

	/** Set Work Day.
		@param HR_WorkDay_ID Work Day	  */
	public void setHR_WorkDay_ID (int HR_WorkDay_ID)
	{
		if (HR_WorkDay_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_WorkDay_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_WorkDay_ID, Integer.valueOf(HR_WorkDay_ID));
	}

	/** Get Work Day.
		@return Work Day	  */
	public int getHR_WorkDay_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_WorkDay_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Sunday = 01 */
	public static final String NAME_Sunday = "01";
	/** Monday = 02 */
	public static final String NAME_Monday = "02";
	/** Tuesday = 03 */
	public static final String NAME_Tuesday = "03";
	/** Wednesday = 04 */
	public static final String NAME_Wednesday = "04";
	/** Thursday = 05 */
	public static final String NAME_Thursday = "05";
	/** Friday = 06 */
	public static final String NAME_Friday = "06";
	/** Saturday = 07 */
	public static final String NAME_Saturday = "07";
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

	/** Set Start Time1.
		@param StartTime1 
		Time started
	  */
	public void setStartTime1 (Timestamp StartTime1)
	{
		set_Value (COLUMNNAME_StartTime1, StartTime1);
	}

	/** Get Start Time1.
		@return Time started
	  */
	public Timestamp getStartTime1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime1);
	}

	/** Set Start Time2.
		@param StartTime2 
		Time started
	  */
	public void setStartTime2 (Timestamp StartTime2)
	{
		set_Value (COLUMNNAME_StartTime2, StartTime2);
	}

	/** Get Start Time2.
		@return Time started
	  */
	public Timestamp getStartTime2 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime2);
	}

	/** Set Start Time3.
		@param StartTime3 
		Time started
	  */
	public void setStartTime3 (Timestamp StartTime3)
	{
		set_Value (COLUMNNAME_StartTime3, StartTime3);
	}

	/** Get Start Time3.
		@return Time started
	  */
	public Timestamp getStartTime3 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime3);
	}
}