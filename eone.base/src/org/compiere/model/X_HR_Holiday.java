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

/** Generated Model for HR_Holiday
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_HR_Holiday extends PO implements I_HR_Holiday, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20201027L;

    /** Standard Constructor */
    public X_HR_Holiday (Properties ctx, int HR_Holiday_ID, String trxName)
    {
      super (ctx, HR_Holiday_ID, trxName);
      /** if (HR_Holiday_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HR_Holiday (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_HR_Holiday[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Calendar Lunar = Lunar */
	public static final String CALENDARTYPE_CalendarLunar = "Lunar";
	/** Calendar Solar = Solar */
	public static final String CALENDARTYPE_CalendarSolar = "Solar";
	/** Set CalendarType.
		@param CalendarType CalendarType	  */
	public void setCalendarType (String CalendarType)
	{

		set_Value (COLUMNNAME_CalendarType, CalendarType);
	}

	/** Get CalendarType.
		@return CalendarType	  */
	public String getCalendarType () 
	{
		return (String)get_Value(COLUMNNAME_CalendarType);
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

	/** EveryYear = EveryYear */
	public static final String HOLIDAYTYPE_EveryYear = "EveryYear";
	/** ThisYear = ThisYear */
	public static final String HOLIDAYTYPE_ThisYear = "ThisYear";
	/** Set HolidayType.
		@param HolidayType HolidayType	  */
	public void setHolidayType (String HolidayType)
	{

		set_Value (COLUMNNAME_HolidayType, HolidayType);
	}

	/** Get HolidayType.
		@return HolidayType	  */
	public String getHolidayType () 
	{
		return (String)get_Value(COLUMNNAME_HolidayType);
	}

	/** Set Holiday.
		@param HR_Holiday_ID Holiday	  */
	public void setHR_Holiday_ID (int HR_Holiday_ID)
	{
		if (HR_Holiday_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Holiday_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Holiday_ID, Integer.valueOf(HR_Holiday_ID));
	}

	/** Get Holiday.
		@return Holiday	  */
	public int getHR_Holiday_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Holiday_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MonthOfYear.
		@param MonthOfYear MonthOfYear	  */
	public void setMonthOfYear (int MonthOfYear)
	{
		set_Value (COLUMNNAME_MonthOfYear, Integer.valueOf(MonthOfYear));
	}

	/** Get MonthOfYear.
		@return MonthOfYear	  */
	public int getMonthOfYear () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MonthOfYear);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set YearHoliday.
		@param YearHoliday YearHoliday	  */
	public void setYearHoliday (int YearHoliday)
	{
		set_Value (COLUMNNAME_YearHoliday, Integer.valueOf(YearHoliday));
	}

	/** Get YearHoliday.
		@return YearHoliday	  */
	public int getYearHoliday () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_YearHoliday);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}