/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.util;

import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 *	Time Utilities
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: TimeUtil.java,v 1.3 2006/07/30 00:54:35 jjanke Exp $
 */
public class TimeUtil
{
	/**
	 * 	Get earliest time of a day (truncate)
	 *  @param time day and time
	 *  @return day with 00:00
	 */
	static public Timestamp getDay (long time)
	{
		if (time == 0)
			time = System.currentTimeMillis();
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Timestamp (cal.getTimeInMillis());
	}	//	getDay

	/**
	 * 	Get earliest time of a day (truncate)
	 *  @param dayTime day and time
	 *  @return day with 00:00
	 */
	static public Timestamp getDay (Timestamp dayTime)
	{
		if (dayTime == null)
			return getDay(System.currentTimeMillis());
		return getDay(dayTime.getTime());
	}	//	getDay

	/**
	 * 	Get earliest time of a day (truncate)
	 *	@param day day 1..31
	 *	@param month month 1..12
	 *	@param year year (if two diguts: < 50 is 2000; > 50 is 1900)
	 *	@return timestamp ** not too reliable
	 */
	static public Timestamp getDay (int year, int month, int day)
	{
		if (year < 50)
			year += 2000;
		else if (year < 100)
			year += 1900;
		if (month < 1 || month > 12)
			throw new IllegalArgumentException("Invalid Month: " + month);
		if (day < 1 || day > 31)
			throw new IllegalArgumentException("Invalid Day: " + month);
		GregorianCalendar cal = new GregorianCalendar (year, month-1, day);
		//cal.set(Calendar.HOUR, 0);
		//cal.set(Calendar.MINUTE, 0);
		//cal.set(Calendar.SECOND, 0);
		return new Timestamp (cal.getTimeInMillis());
	}	//	getDay

	/**
	 * 	Get today (truncate)
	 *  @return day with 00:00
	 */
	static public Calendar getToday ()
	{
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
	//	cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}	//	getToday

	/**
	 * 	Get earliest time of next day
	 *  @param day day
	 *  @return next day with 00:00
	 */
	static public Timestamp getNextDay (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.add(Calendar.DAY_OF_YEAR, +1);	//	next
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Timestamp (cal.getTimeInMillis());
	}	//	getNextDay
	
	/**
	 * 	Get earliest time of next day
	 *  @param day day
	 *  @return next day with 00:00
	 */
	static public Timestamp getPreviousDay (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.add(Calendar.DAY_OF_YEAR, -1);	//	previous
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Timestamp (cal.getTimeInMillis());
	}	//	getNextDay

	/**
	 * 	Get last date in month
	 *  @param day day
	 *  @return last day with 00:00
	 */
	static public Timestamp getMonthLastDay (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.add(Calendar.MONTH, 1);			//	next
		cal.set(Calendar.DAY_OF_MONTH, 1);	//	first
		cal.add(Calendar.DAY_OF_YEAR, -1);	//	previous
		return new Timestamp (cal.getTimeInMillis());
	}	//	getNextDay

	
	static public Timestamp getDayLastMonth (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.add(Calendar.MONTH, 1);			//	next
		cal.set(Calendar.DAY_OF_MONTH, 1);	//	first
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return new Timestamp (cal.getTimeInMillis());
	}
	
	static public Timestamp getDayLastYear (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.add(Calendar.MONTH, 11);			//	next
		cal.set(Calendar.DAY_OF_MONTH, 31);	//	first
		return new Timestamp (cal.getTimeInMillis());
	}
	
	//addDay: Su dung cho truong hop khong lay ngay cuoi nam ma lay ngay dau tien cua nam sau day.
	static public Timestamp getFinalYear (String year, boolean addDay)
	{
		if (year == null)
			return null;
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.set(Calendar.MONTH, 11);			//	next
		cal.set(Calendar.DAY_OF_MONTH, 31);	//	first
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		Timestamp retDate = new Timestamp (cal.getTimeInMillis());
		if (addDay) {
			retDate = addDays(retDate, 1);
		}
		return retDate;
	}
	
	static public Timestamp getMinDate ()
	{
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.set(Calendar.MONTH, 0);			//	next
		cal.set(Calendar.DAY_OF_MONTH, 1);	//	first
		cal.set(Calendar.YEAR, 2000);
		Timestamp retDate = new Timestamp (cal.getTimeInMillis());
		return retDate;
	}
	
	static public Timestamp getDayFirstMonth (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.set(Calendar.DAY_OF_MONTH, 1);	//	first
		return new Timestamp (cal.getTimeInMillis());
	}
	
	static public Timestamp getDayFirstYear (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.set(Calendar.DAY_OF_MONTH, 1);	//	first
		cal.set(Calendar.MONTH, 0);
		return new Timestamp (cal.getTimeInMillis());
	}
	/**
	 * 	Return the day and time
	 * 	@param day day part
	 * 	@param time time part
	 * 	@return day + time
	 */
	static public Timestamp getDayTime (Timestamp day, Timestamp time)
	{
		GregorianCalendar cal_1 = new GregorianCalendar();
		cal_1.setTimeInMillis(day.getTime());
		GregorianCalendar cal_2 = new GregorianCalendar();
		cal_2.setTimeInMillis(time.getTime());
		//
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.set(cal_1.get(Calendar.YEAR),
			cal_1.get(Calendar.MONTH),
			cal_1.get(Calendar.DAY_OF_MONTH),
			cal_2.get(Calendar.HOUR_OF_DAY),
			cal_2.get(Calendar.MINUTE),
			cal_2.get(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp retValue = new Timestamp(cal.getTimeInMillis());
	//	log.fine( "TimeUtil.getDayTime", "Day=" + day + ", Time=" + time + " => " + retValue);
		return retValue;
	}	//	getDayTime

	/**
	 * 	Is the _1 in the Range of _2
	 *  <pre>
	 * 		Time_1         +--x--+
	 * 		Time_2   +a+      +---b---+   +c+
	 * 	</pre>
	 *  The function returns true for b and false for a/b.
	 *  @param start_1 start (1)
	 *  @param end_1 not included end (1)
	 *  @param start_2 start (2)
	 *  @param end_2 not included (2)
	 *  @return true if in range
	 */
	static public boolean inRange (Timestamp start_1, Timestamp end_1, Timestamp start_2, Timestamp end_2)
	{
		//	validity check
		if (end_1.before(start_1))
			throw new UnsupportedOperationException ("TimeUtil.inRange End_1=" + end_1 + " before Start_1=" + start_1);
		if (end_2.before(start_2))
			throw new UnsupportedOperationException ("TimeUtil.inRange End_2=" + end_2 + " before Start_2=" + start_2);
		//	case a
		if (!end_2.after(start_1))		//	end not including
		{
	//		log.fine( "TimeUtil.InRange - No", start_1 + "->" + end_1 + " <??> " + start_2 + "->" + end_2);
			return false;
		}
		//	case c
		if (!start_2.before(end_1))		//	 end not including
		{
	//		log.fine( "TimeUtil.InRange - No", start_1 + "->" + end_1 + " <??> " + start_2 + "->" + end_2);
			return false;
		}
	//	log.fine( "TimeUtil.InRange - Yes", start_1 + "->" + end_1 + " <??> " + start_2 + "->" + end_2);
		return true;
	}	//	inRange

	/**
	 * 	Is start..end on one of the days ?
	 *  @param start start day
	 *  @param end end day (not including)
	 *  @param OnMonday true if OK
	 *  @param OnTuesday true if OK
	 *  @param OnWednesday true if OK
	 *  @param OnThursday true if OK
	 *  @param OnFriday true if OK
	 *  @param OnSaturday true if OK
	 *  @param OnSunday true if OK
	 *  @return true if on one of the days
	 */
	static public boolean inRange (Timestamp start, Timestamp end,
		boolean OnMonday, boolean OnTuesday, boolean OnWednesday,
		boolean OnThursday, boolean OnFriday, boolean OnSaturday, boolean OnSunday)
	{
		//	are there restrictions?
		if (OnSaturday && OnSunday && OnMonday && OnTuesday && OnWednesday && OnThursday && OnFriday)
			return false;

		GregorianCalendar calStart = new GregorianCalendar();
		calStart.setTimeInMillis(start.getTime());
		int dayStart = calStart.get(Calendar.DAY_OF_WEEK);
		//
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTimeInMillis(end.getTime());
		calEnd.add(Calendar.DAY_OF_YEAR, -1);	//	not including
		int dayEnd = calEnd.get(Calendar.DAY_OF_WEEK);

		//	On same day
		if (calStart.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR)
			&& calStart.get(Calendar.MONTH) == calEnd.get(Calendar.MONTH)
			&& calStart.get(Calendar.DAY_OF_MONTH) == calEnd.get(Calendar.DAY_OF_YEAR))
		{
			if ((!OnSaturday && dayStart == Calendar.SATURDAY)
				|| (!OnSunday && dayStart == Calendar.SUNDAY)
				|| (!OnMonday && dayStart == Calendar.MONDAY)
				|| (!OnTuesday && dayStart == Calendar.TUESDAY)
				|| (!OnWednesday && dayStart == Calendar.WEDNESDAY)
				|| (!OnThursday && dayStart == Calendar.THURSDAY)
				|| (!OnFriday && dayStart == Calendar.FRIDAY))
			{
		//		log.fine( "TimeUtil.InRange - SameDay - Yes", start + "->" + end + " - "
		//			+ OnMonday+"-"+OnTuesday+"-"+OnWednesday+"-"+OnThursday+"-"+OnFriday+"="+OnSaturday+"-"+OnSunday);
				return true;
			}
		//	log.fine( "TimeUtil.InRange - SameDay - No", start + "->" + end + " - "
		//		+ OnMonday+"-"+OnTuesday+"-"+OnWednesday+"-"+OnThursday+"-"+OnFriday+"="+OnSaturday+"-"+OnSunday);
			return false;
		}
		//
	//	log.fine( "TimeUtil.inRange - WeekDay Start=" + dayStart + ", Incl.End=" + dayEnd);

		//	Calendar.SUNDAY=1 ... SATURDAY=7
		BitSet days = new BitSet (8);
		//	Set covered days in BitArray
		if (dayEnd <= dayStart)
			dayEnd += 7;
		for (int i = dayStart; i < dayEnd; i++)
		{
			int index = i;
			if (index > 7)
				index -= 7;
			days.set(index);
	//		System.out.println("Set index=" + index + " i=" + i);
		}

	//	for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++)
	//		System.out.println("Result i=" + i + " - " + days.get(i));

		//	Compare days to availability
		if ((!OnSaturday && days.get(Calendar.SATURDAY))
			|| (!OnSunday && days.get(Calendar.SUNDAY))
			|| (!OnMonday && days.get(Calendar.MONDAY))
			|| (!OnTuesday && days.get(Calendar.TUESDAY))
			|| (!OnWednesday && days.get(Calendar.WEDNESDAY))
			|| (!OnThursday && days.get(Calendar.THURSDAY))
			|| (!OnFriday && days.get(Calendar.FRIDAY)))
		{
	//		log.fine( "MAssignment.InRange - Yes",	start + "->" + end + " - "
	//			+ OnMonday+"-"+OnTuesday+"-"+OnWednesday+"-"+OnThursday+"-"+OnFriday+"="+OnSaturday+"-"+OnSunday);
			return true;
		}

	//	log.fine( "MAssignment.InRange - No", start + "->" + end + " - "
	//		+ OnMonday+"-"+OnTuesday+"-"+OnWednesday+"-"+OnThursday+"-"+OnFriday+"="+OnSaturday+"-"+OnSunday);
		return false;
	}	//	isRange


	/**
	 * 	Is it the same day
	 * 	@param one day
	 * 	@param two compared day
	 * 	@return true if the same day
	 */
	static public boolean isSameDay (Timestamp one, Timestamp two)
	{
		GregorianCalendar calOne = new GregorianCalendar();
		if (one != null)
			calOne.setTimeInMillis(one.getTime());
		GregorianCalendar calTwo = new GregorianCalendar();
		if (two != null)
			calTwo.setTimeInMillis(two.getTime());
		if (calOne.get(Calendar.YEAR) == calTwo.get(Calendar.YEAR)
			&& calOne.get(Calendar.MONTH) == calTwo.get(Calendar.MONTH)
			&& calOne.get(Calendar.DAY_OF_MONTH) == calTwo.get(Calendar.DAY_OF_MONTH))
			return true;
		return false;
	}	//	isSameDay

	/**
	 * 	Is it the same hour
	 * 	@param one day/time
	 * 	@param two compared day/time
	 * 	@return true if the same day
	 */
	static public boolean isSameHour (Timestamp one, Timestamp two)
	{
		GregorianCalendar calOne = new GregorianCalendar();
		if (one != null)
			calOne.setTimeInMillis(one.getTime());
		GregorianCalendar calTwo = new GregorianCalendar();
		if (two != null)
			calTwo.setTimeInMillis(two.getTime());
		if (calOne.get(Calendar.YEAR) == calTwo.get(Calendar.YEAR)
			&& calOne.get(Calendar.MONTH) == calTwo.get(Calendar.MONTH)
			&& calOne.get(Calendar.DAY_OF_MONTH) == calTwo.get(Calendar.DAY_OF_MONTH)
			&& calOne.get(Calendar.HOUR_OF_DAY) == calTwo.get(Calendar.HOUR_OF_DAY))
			return true;
		return false;
	}	//	isSameHour

	/**
	 * 	Is all day
	 * 	@param start start date
	 * 	@param end end date
	 * 	@return true if all day (00:00-00:00 next day)
	 */
	static public boolean isAllDay (Timestamp start, Timestamp end)
	{
		GregorianCalendar calStart = new GregorianCalendar();
		calStart.setTimeInMillis(start.getTime());
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTimeInMillis(end.getTime());
		if (calStart.get(Calendar.HOUR_OF_DAY) == calEnd.get(Calendar.HOUR_OF_DAY)
			&& calStart.get(Calendar.MINUTE) == calEnd.get(Calendar.MINUTE)
			&& calStart.get(Calendar.SECOND) == calEnd.get(Calendar.SECOND)
			&& calStart.get(Calendar.MILLISECOND) == calEnd.get(Calendar.MILLISECOND)
			&& calStart.get(Calendar.HOUR_OF_DAY) == 0
			&& calStart.get(Calendar.MINUTE) == 0
			&& calStart.get(Calendar.SECOND) == 0
			&& calStart.get(Calendar.MILLISECOND) == 0
			&& start.before(end))
			return true;
		//
		return false;
	}	//	isAllDay

	/**
	 * 	Calculate the number of days between start and end.
	 * 	@param start start date
	 * 	@param end end date
	 * 	@return number of days (0 = same)
	 */
	static public int getDaysBetween (Timestamp start, Timestamp end)
	{
		boolean negative = false;
		if (end.before(start))
		{
			negative = true;
			Timestamp temp = start;
			start = end;
			end = temp;
		}
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);

	//	System.out.println("Start=" + start + ", End=" + end + ", dayStart=" + cal.get(Calendar.DAY_OF_YEAR) + ", dayEnd=" + calEnd.get(Calendar.DAY_OF_YEAR));

		//	in same year
		if (cal.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR))
		{
			if (negative)
				return (calEnd.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR)) * -1;
			return calEnd.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) + 1;
		}

		//	not very efficient, but correct
		int counter = 0;
		while (calEnd.after(cal))
		{
			cal.add (Calendar.DAY_OF_YEAR, 1);
			counter++;
		}
		if (negative)
			return counter * -1;
		return counter;
	}	//	getDaysBetween

	/**
	 * 	Return Day + offset (truncates)
	 * 	@param day Day
	 * 	@param offset day offset
	 * 	@return Day + offset at 00:00
	 */
	static public Timestamp addDays (Timestamp day, int offset)
	{
		if (offset == 0)
		{
			return day;
		}
		if (day == null)
		{
			day = new Timestamp(System.currentTimeMillis());
		}
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (offset == 0)
			return new Timestamp (cal.getTimeInMillis());
		cal.add(Calendar.DAY_OF_YEAR, offset);			//	may have a problem with negative (before 1/1)
		return new Timestamp (cal.getTimeInMillis());
	}	//	addDays

	/**
	 * 	Return DateTime + offset in minutes
	 * 	@param dateTime Date and Time
	 * 	@param offset minute offset
	 * 	@return dateTime + offset in minutes
	 */
	static public Timestamp addMinutess (Timestamp dateTime, int offset)
	{
		if (dateTime == null)
			dateTime = new Timestamp(System.currentTimeMillis());
		if (offset == 0)
			return dateTime;
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dateTime);
		cal.add(Calendar.MINUTE, offset);			//	may have a problem with negative
		return new Timestamp (cal.getTimeInMillis());
	}	//	addMinutes


	/**************************************************************************
	 * 	Format Elapsed Time
	 * 	@param start start time or null for now
	 * 	@param end end time or null for now
	 * 	@return formatted time string 1'23:59:59.999
	 */
	public static String formatElapsed (Timestamp start, Timestamp end)
	{
		long startTime = 0;
		if (start == null)
			startTime = System.currentTimeMillis();
		else
			startTime = start.getTime();
		//
		long endTime = 0;
		if (end == null)
			endTime = System.currentTimeMillis();
		else
			endTime = end.getTime();
		return formatElapsed(endTime-startTime);
	}	//	formatElapsed

	/**
	 * 	Format Elapsed Time until now
	 * 	@param start start time
	 *	@return formatted time string 1'23:59:59.999
	 */
	public static String formatElapsed (Timestamp start)
	{
		if (start == null)
			return "NoStartTime";
		long startTime = start.getTime();
		long endTime = System.currentTimeMillis();
		return formatElapsed(endTime-startTime);
	}	//	formatElapsed

	/**
	 * 	Format Elapsed Time
	 *	@param elapsedMS time in ms
	 *	@return formatted time string 1'23:59:59.999 - d'hh:mm:ss.xxx
	 */
	public static String formatElapsed (long elapsedMS)
	{
		if (elapsedMS == 0)
			return "0";
		StringBuilder sb = new StringBuilder();
		if (elapsedMS < 0)
		{
			elapsedMS = - elapsedMS;
			sb.append("-");
		}
		//
		long miliSeconds = elapsedMS%1000;
		elapsedMS = elapsedMS / 1000;
		long seconds = elapsedMS%60;
		elapsedMS = elapsedMS / 60;
		long minutes = elapsedMS%60;
		elapsedMS = elapsedMS / 60;
		long hours = elapsedMS%24;
		long days = elapsedMS / 24;
		//
		if (days != 0)
			sb.append(days).append("'");
		//	hh
		if (hours != 0)
			sb.append(get2digits(hours)).append(":");
		else if (days != 0)
			sb.append("00:");
		//	mm
		if (minutes != 0)
			sb.append(get2digits(minutes)).append(":");
		else if (hours != 0 || days != 0)
			sb.append("00:");
		//	ss
		sb.append(get2digits(seconds))
			.append(".").append(miliSeconds);
		return sb.toString();
	}	//	formatElapsed

	/**
	 * 	Get Minimum of 2 digits
	 *	@param no number
	 *	@return String
	 */
	private static String get2digits (long no)
	{
		String s = String.valueOf(no);
		if (s.length() > 1)
			return s;
		return "0" + s;
	}	//	get2digits


	/**
	 * 	Is it valid today?
	 *	@param validFrom valid from
	 *	@param validTo valid to
	 *	@return true if walid
	 */
	public static boolean isValid (Timestamp validFrom, Timestamp validTo)
	{
		return isValid (validFrom, validTo, new Timestamp (System.currentTimeMillis()));
	}	//	isValid

	/**
	 * 	Is it valid on test date
	 *	@param validFrom valid from
	 *	@param validTo valid to
	 *	@param testDate Date
	@return true if walid
	 */
	public static boolean isValid (Timestamp validFrom, Timestamp validTo, Timestamp testDate)
	{
		if (testDate == null)
			return true;
		if (validFrom == null && validTo == null)
			return true;
		//	(validFrom)	ok
		if (validFrom != null && validFrom.after(testDate))
			return false;
		//	ok	(validTo)
		if (validTo != null && validTo.before(testDate))
			return false;
		return true;
	}	//	isValid
	
	/**
	 * 	Max date
	 *	@param ts1 p1
	 *	@param ts2 p2
	 *	@return max time
	 */
	public static Timestamp max (Timestamp ts1, Timestamp ts2)
	{
		if (ts1 == null)
			return ts2;
		if (ts2 == null)
			return ts1;
		
		if (ts2.after(ts1))
			return ts2;
		return ts1;
	}	//	max

	/** Truncate Day - D			*/
	public static final String	TRUNC_DAY = "D";
	/** Truncate Week - W			*/
	public static final String	TRUNC_WEEK = "W";
	/** Truncate Month - MM			*/
	public static final String	TRUNC_MONTH = "MM";
	/** Truncate Quarter - Q		*/
	public static final String	TRUNC_QUARTER = "Q";
	/** Truncate Year - Y			*/
	public static final String	TRUNC_YEAR = "Y";
	
	/**
	 * 	Get truncated day/time
	 *  @param dayTime day
	 *  @param trunc how to truncate TRUNC_*
	 *  @return next day with 00:00
	 */
	static public Timestamp trunc (Timestamp dayTime, String trunc)
	{
		if (dayTime == null)
			dayTime = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(dayTime.getTime());
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		//	D
		cal.set(Calendar.HOUR_OF_DAY, 0);
		if (trunc == null || trunc.equals(TRUNC_DAY))
			return new Timestamp (cal.getTimeInMillis());
		//	W
		if (trunc.equals(TRUNC_WEEK))
		{
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			return new Timestamp (cal.getTimeInMillis());
		}
		// MM
		cal.set(Calendar.DAY_OF_MONTH, 1);
		if (trunc.equals(TRUNC_MONTH))
			return new Timestamp (cal.getTimeInMillis());
		//	Q
		if (trunc.equals(TRUNC_QUARTER))
		{
			int mm = cal.get(Calendar.MONTH);
			if (mm < 4)
				mm = 1;
			else if (mm < 7)
				mm = 4;
			else if (mm < 10)
				mm = 7;
			else
				mm = 10;
			cal.set(Calendar.MONTH, mm);
			return new Timestamp (cal.getTimeInMillis());
		}
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return new Timestamp (cal.getTimeInMillis());
	}	//	trunc
	
	/**
	 * Returns the day border by combining the date part from dateTime and time part form timeSlot.
	 * If timeSlot is null, then first milli of the day will be used (if end == false)
	 * or last milli of the day (if end == true).
	 * 
	 * @param dateTime
	 * @param timeSlot
	 * @param end
	 * @return
	 */
	public static Timestamp getDayBorder(Timestamp dateTime, Timestamp timeSlot, boolean end)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(dateTime.getTime());
		dateTime.setNanos(0);

		if(timeSlot != null)
		{
			timeSlot.setNanos(0);
			GregorianCalendar gcTS = new GregorianCalendar();
			gcTS.setTimeInMillis(timeSlot.getTime());

			gc.set(Calendar.HOUR_OF_DAY, gcTS.get(Calendar.HOUR_OF_DAY));
			gc.set(Calendar.MINUTE, gcTS.get(Calendar.MINUTE));
			gc.set(Calendar.SECOND, gcTS.get(Calendar.SECOND));
			gc.set(Calendar.MILLISECOND, gcTS.get(Calendar.MILLISECOND));
		} 
		else if(end)
		{
			gc.set(Calendar.HOUR_OF_DAY, 23);
			gc.set(Calendar.MINUTE, 59);
			gc.set(Calendar.SECOND, 59);
			gc.set(Calendar.MILLISECOND, 999);
		}
		else
		{
			gc.set(Calendar.MILLISECOND, 0);
			gc.set(Calendar.SECOND, 0);
			gc.set(Calendar.MINUTE, 0);
			gc.set(Calendar.HOUR_OF_DAY, 0);
		}
		return new Timestamp(gc.getTimeInMillis());
	}

	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		String str = "JavaExamplesJavaCodeJavaProgram";
        
        String strFind = "Java";
        int count = 0, fromIndex = 0;
        
        while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
 
            System.out.println("Found at index: " + fromIndex);
            count++;
            fromIndex++;
            
        }
        
        System.out.println("Total occurrences: " + count);
	}	//	main
	
// ARHIPAC: TEO: ADDITION ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * [ ARHIPAC ] Gets calendar instance of given date
	 * @param date calendar initialization date; if null, the current date is used
	 * @return calendar
	 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
	 */
	static public Calendar getCalendar(Timestamp date)
	{
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		if (date != null) {
			cal.setTimeInMillis(date.getTime());
		}
		return cal;
	}
	
	/**
	 * [ ARHIPAC ] Get first date in month
	 * @param day day; if null current time will be used
	 * @return first day of the month (time will be 00:00)
	 */
	static public Timestamp getMonthFirstDay (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		Calendar cal = getCalendar(day);
		cal.setTimeInMillis(day.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.set(Calendar.DAY_OF_MONTH, 1);	//	first
		return new Timestamp (cal.getTimeInMillis());
	}	//	getMonthFirstDay
	
	/**
	 * [ ARHIPAC ] Return Day + offset (truncates)
	 * @param day Day; if null current time will be used
	 * @param offset months offset
	 * @return Day + offset (time will be 00:00)
	 * @return Teo Sarca, SC ARHIPAC SERVICE SRL
	 */
	static public Timestamp addMonths (Timestamp day, int offset)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (offset == 0)
			return new Timestamp (cal.getTimeInMillis());
		cal.add(Calendar.MONTH, offset);
		return new Timestamp (cal.getTimeInMillis());
	}	//	addMonths
	
	public static int getMonthsBetween (Timestamp start, Timestamp end)
	{
		Calendar startCal = getCalendar(start);
		Calendar endCal = getCalendar(end);
		//
		return endCal.get(Calendar.YEAR) * 12 + endCal.get(Calendar.MONTH)
				- (startCal.get(Calendar.YEAR) * 12 + startCal.get(Calendar.MONTH));
	}

	

	/** Returns number of non business days between 2 dates */
	public static int getBusinessDaysBetween(Timestamp startDate, Timestamp endDate, int clientID, String trxName)
	{
		int retValue = 0;

		if (startDate.equals(endDate))
			return 0;

		boolean negative = false;
		if (endDate.before(startDate)) {
			negative = true;
			Timestamp temp = startDate;
			startDate = endDate;
			endDate = temp;
		}


		if (negative)
			retValue = retValue * -1;
		return retValue;
	}
	
	public static int getDayOfWeek(Timestamp time) {
		long timestamp = time.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDaySel(Timestamp time) {
		long timestamp = time.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	public static int getMonthSel(Timestamp time) {
		long timestamp = time.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.MONTH) + 1;
	}
	public static int getYearSel(Timestamp time) {
		long timestamp = time.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		return cal.get(Calendar.YEAR);
	}
	
	//Tu ngay am lay ngay duong
	public static Timestamp getSolar(int D, int M, int Y) {
		Calendar cl = Calendar.getInstance();
		int [] vl = getLunar2Solar(D, M, Y, 0);
		cl.set(Calendar.DAY_OF_MONTH, vl[0]);
		cl.set(Calendar.MONTH, vl[1]);
		cl.set(Calendar.YEAR, vl[2]);
		return new Timestamp(cl.getTimeInMillis());
	}
	
	/**
	 * Convert lich am thanh lich duong
	 * @param D
	 * @param M
	 * @param Y
	 * @param leap
	 * @return
	 */
	private static int[] getLunar2Solar(int D, int M, int Y, int leap) {
		int yy = Y;
		if (M >= 11) {
			yy = Y+1;
		}
		int[][] lunarYear = getLunarYear(yy);
		int[] lunarMonth = null;
		for (int i = 0; i < lunarYear.length; i++) {
			int[] lm = lunarYear[i];
			if (lm[3] == M && lm[4] == leap) {
				lunarMonth = lm;
				break;
			}
		}
		if (lunarMonth != null) {
			double jd = LocalToJD(lunarMonth[0], lunarMonth[1], lunarMonth[2]);
			return LocalFromJD(jd + D - 1);
		} else {
			throw new RuntimeException("Incorrect input!");
		}
	}
	
	private static int LOCAL_TIMEZONE = 7;
	public static int[] LocalFromJD(double JD) {
		return UniversalFromJD(JD + LOCAL_TIMEZONE/24.0);
	}
	
	private static double LocalToJD(int D, int M, int Y) {
		return UniversalToJD(D, M, Y) - LOCAL_TIMEZONE/24.0;
	}
	private static int INT(double d) {
		return (int)Math.floor(d);
	}

	private static int MOD(int x, int y) {
		int z = x - (int)(y * Math.floor(((double)x / y)));
		if (z == 0) {
		  z = y;
		}
		return z;
	}
	private static int[] UniversalFromJD(double JD) {
		int Z, A, alpha, B, C, D, E, dd, mm, yyyy;
		double F;
		Z = INT(JD+0.5);
		F = (JD+0.5)-Z;
		if (Z < 2299161) {
		  A = Z;
		} else {
		  alpha = INT((Z-1867216.25)/36524.25);
		  A = Z + 1 + alpha - INT(alpha/4);
		}
		B = A + 1524;
		C = INT( (B-122.1)/365.25);
		D = INT( 365.25*C );
		E = INT( (B-D)/30.6001 );
		dd = INT(B - D - INT(30.6001*E) + F);
		if (E < 14) {
		  mm = E - 1;
		} else {
		  mm = E - 13;
		}
		if (mm < 3) {
		  yyyy = C - 4715;
		} else {
		  yyyy = C - 4716;
		}
		return new int[]{dd, mm, yyyy};
	}
	
	private static double SunLongitude(double jdn) {
		double T = (jdn - 2451545.0 ) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
		double T2 = T*T;
		double dr = Math.PI/180; // degree to radian
		double M = 357.52910 + 35999.05030*T - 0.0001559*T2 - 0.00000048*T*T2; // mean anomaly, degree
		double L0 = 280.46645 + 36000.76983*T + 0.0003032*T2; // mean longitude, degree
		double DL = (1.914600 - 0.004817*T - 0.000014*T2)*Math.sin(dr*M);
		DL = DL + (0.019993 - 0.000101*T)*Math.sin(dr*2*M) + 0.000290*Math.sin(dr*3*M);
		double L = L0 + DL; // true longitude, degree
		L = L*dr;
		L = L - Math.PI*2*(INT(L/(Math.PI*2))); // Normalize to (0, 2*PI)
		return L;
	}
	
	private static int[] LunarMonth11(int Y) {
		double off = LocalToJD(31, 12, Y) - 2415021.076998695;
		int k = INT(off / 29.530588853);
		double jd = NewMoon(k);
		int[] ret = LocalFromJD(jd);
		double sunLong = SunLongitude(LocalToJD(ret[0], ret[1], ret[2])); // sun longitude at local midnight
		if (sunLong > 3*Math.PI/2) {
			jd = NewMoon(k-1);
		}
		return LocalFromJD(jd);
	}
	
	private static int[][] getLunarYear(int Y) {
		int[][] ret = null;
		int[] month11A = LunarMonth11(Y-1);
		double jdMonth11A = LocalToJD(month11A[0], month11A[1], month11A[2]);
		int k = (int)Math.floor(0.5 + (jdMonth11A - 2415021.076998695) / 29.530588853);
		int[] month11B = LunarMonth11(Y);
		double off = LocalToJD(month11B[0], month11B[1], month11B[2]) - jdMonth11A;
		boolean leap = off > 365.0;
		if (!leap) {
			ret = new int[13][5];
		} else {
			ret = new int[14][5];
		}
		ret[0] = new int[]{month11A[0], month11A[1], month11A[2], 0, 0};
		ret[ret.length - 1] = new int[]{month11B[0], month11B[1], month11B[2], 0, 0};
		for (int i = 1; i < ret.length - 1; i++) {
			double nm = NewMoon(k+i);
			int[] a = LocalFromJD(nm);
			ret[i] = new int[]{a[0], a[1], a[2], 0, 0};
		}
		for (int i = 0; i < ret.length; i++) {
			ret[i][3] = MOD(i + 11, 12);
		}
		if (leap) {
			initLeapYear(ret);
		}
		return ret;
	}
	
	private static double UniversalToJD(int D, int M, int Y) {
		double JD;
		if (Y > 1582 || (Y == 1582 && M > 10) || (Y == 1582 && M == 10 && D > 14)) {
			JD = 367*Y - INT(7*(Y+INT((M+9)/12))/4) - INT(3*(INT((Y+(M-9)/7)/100)+1)/4) + INT(275*M/9)+D+1721028.5;
		} else {
			JD = 367*Y - INT(7*(Y+5001+INT((M-9)/7))/4) + INT(275*M/9)+D+1729776.5;
		}
		return JD;
	}

	static void initLeapYear(int[][] ret) {
		double[] sunLongitudes = new double[ret.length];
		for (int i = 0; i < ret.length; i++) {
			int[] a = ret[i];
			double jdAtMonthBegin = LocalToJD(a[0], a[1], a[2]);
			sunLongitudes[i] = SunLongitude(jdAtMonthBegin);
		}
		boolean found = false;
		for (int i = 0; i < ret.length; i++) {
			if (found) {
				ret[i][3] = MOD(i+10, 12);
				continue;
			}
			double sl1 = sunLongitudes[i];
			double sl2 = sunLongitudes[i+1];
			boolean hasMajorTerm = Math.floor(sl1/Math.PI*6) != Math.floor(sl2/Math.PI*6);
			if (!hasMajorTerm) {
				found = true;
				ret[i][4] = 1;
				ret[i][3] = MOD(i+10, 12);
			}
		}		
	}
	
	private static double NewMoon(int k) {
		double T = k/1236.85; // Time in Julian centuries from 1900 January 0.5
		double T2 = T * T;
		double T3 = T2 * T;
		double dr = Math.PI/180;
		double Jd1 = 2415020.75933 + 29.53058868*k + 0.0001178*T2 - 0.000000155*T3;
		Jd1 = Jd1 + 0.00033*Math.sin((166.56 + 132.87*T - 0.009173*T2)*dr); // Mean new moon
		double M = 359.2242 + 29.10535608*k - 0.0000333*T2 - 0.00000347*T3; // Sun's mean anomaly
		double Mpr = 306.0253 + 385.81691806*k + 0.0107306*T2 + 0.00001236*T3; // Moon's mean anomaly
		double F = 21.2964 + 390.67050646*k - 0.0016528*T2 - 0.00000239*T3; // Moon's argument of latitude
		double C1=(0.1734 - 0.000393*T)*Math.sin(M*dr) + 0.0021*Math.sin(2*dr*M);
		C1 = C1 - 0.4068*Math.sin(Mpr*dr) + 0.0161*Math.sin(dr*2*Mpr);
		C1 = C1 - 0.0004*Math.sin(dr*3*Mpr);
		C1 = C1 + 0.0104*Math.sin(dr*2*F) - 0.0051*Math.sin(dr*(M+Mpr));
		C1 = C1 - 0.0074*Math.sin(dr*(M-Mpr)) + 0.0004*Math.sin(dr*(2*F+M));
		C1 = C1 - 0.0004*Math.sin(dr*(2*F-M)) - 0.0006*Math.sin(dr*(2*F+Mpr));
		C1 = C1 + 0.0010*Math.sin(dr*(2*F-Mpr)) + 0.0005*Math.sin(dr*(2*Mpr+M));
		double deltat;
		if (T < -11) {
			deltat= 0.001 + 0.000839*T + 0.0002261*T2 - 0.00000845*T3 - 0.000000081*T*T3;
		} else {
			deltat= -0.000278 + 0.000265*T + 0.000262*T2;
		};
		double JdNew = Jd1 + C1 - deltat;
		return JdNew;
	}
	
	
}	//	TimeUtil
