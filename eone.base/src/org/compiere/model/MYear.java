
package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.adempiere.exceptions.FillMandatoryException;
import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.IProcessUI;
import org.compiere.util.Language;
import org.compiere.util.TimeUtil;

public class MYear extends X_C_Year
{
	private static final long serialVersionUID = 2110541427179611810L;

	public MYear (Properties ctx, int C_Year_ID, String trxName)
	{
		super (ctx, C_Year_ID, trxName);
		if (C_Year_ID == 0)
		{
			setProcessing (false);	// N
		}
		
	}	//	MYear
	
	private static CCache<Integer, MYear> s_cache = new CCache<Integer, MYear>(Table_Name, 0, 20);

	public MYear (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MYear
	
	public static MYear get(Properties ctx, int C_Year_ID, String trxName) {
		if (s_cache.containsKey(C_Year_ID)) {
			return s_cache.get(C_Year_ID);
		}
		String whereClause = "C_Year_ID = ?";
		MYear retValue = new Query(ctx, Table_Name, whereClause, trxName)
				.setParameters(C_Year_ID)
				.firstOnly();
		if (retValue != null) {
			s_cache.put(C_Year_ID, retValue);
		}
		return retValue;
	}
	
	public int getYearAsInt()
	{
		String year = getFiscalYear();
		try
		{
			return Integer.parseInt(year);
		}
		catch (Exception e)
		{
			StringTokenizer st = new StringTokenizer(year, "/-, \t\n\r\f");
			if (st.hasMoreTokens())
			{
				String year2 = st.nextToken();
				try
				{
					return Integer.parseInt(year2);
				}
				catch (Exception e2)
				{
					log.log(Level.WARNING, year + "->" + year2 + " - " + e2.toString());
				}
			}
			else
				log.log(Level.WARNING, year + " - " + e.toString());
		}
		return 0;
	}	//	getYearAsInt
	
	/**
	 * 	Get last two characters of year
	 *	@return 01
	 */
	public String getYY()
	{
		int yy = getYearAsInt();
		String year = String.valueOf(yy);
		if (year.length() == 4)
			return year.substring(2, 4);
		return getFiscalYear();
	}	//	getYY
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MYear[");
		sb.append(get_ID()).append("-")
			.append(getFiscalYear())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		int yy = getYearAsInt();
		if (yy == 0)
		{
			throw new FillMandatoryException(COLUMNNAME_FiscalYear);
		}
		setStartDate(TimeUtil.getDay(yy, 1, 1));
		setEndDate(TimeUtil.getDay(yy, 12, 31));
		return true;
	}	//	beforeSave
	
	
	public boolean createStdPeriods(Locale locale, Timestamp startDate, String dateFormat)
	{
		if (locale == null)
		{
			MClient client = MClient.get(getCtx());
			locale = client.getLocale();
		}
		
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(getCtx()).getLocale();
		//
		SimpleDateFormat formatter;
		if ( dateFormat == null || dateFormat.equals("") )
			dateFormat = "MMM-yy";
		formatter = new SimpleDateFormat(dateFormat, locale);
		
		//
		int year = getYearAsInt();
		GregorianCalendar cal = new GregorianCalendar(locale);
		if ( startDate != null )
		{
			cal.setTime(startDate);
			if ( cal.get(Calendar.YEAR) != year)     // specified start date takes precedence in setting year
				year = cal.get(Calendar.YEAR);
		
		}
		else 
		{
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		//
		IProcessUI processMonitor = Env.getProcessUI(getCtx());
		for (int month = 0; month < 12; month++)
		{
			
			Timestamp start = new Timestamp(cal.getTimeInMillis());
			String name = formatter.format(start);
			// get last day of same month
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			Timestamp end = new Timestamp(cal.getTimeInMillis());
			//
			MPeriod period = MPeriod.findByCalendar(getCtx(), start, get_TrxName());
			if (period == null)
			{
				period = new MPeriod (this, month+1, name, start, end);
			}
			else
			{
				period.setC_Year_ID(this.getC_Year_ID());
				period.setPeriodNo(month+1);
				period.setName(name);
				period.setStartDate(start);
				period.setEndDate(end);
			}
			if (processMonitor != null)
			{
				processMonitor.statusUpdate(period.toString());
			}
			period.saveEx(get_TrxName());	//	Creates Period Control
			// get first day of next month
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		return true;
		
	}	//	createStdPeriods
	
}	//	MYear
