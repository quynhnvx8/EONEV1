
package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;


public class MPeriod extends X_C_Period
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 769103495098446073L;


	public static MPeriod get (Properties ctx, int C_Period_ID)
	{
		if (C_Period_ID <= 0)
			return null;
		//
		Integer key = Integer.valueOf(C_Period_ID);
		MPeriod retValue = (MPeriod) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MPeriod (ctx, C_Period_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get


	public static MPeriod get (Properties ctx, Timestamp DateAcct)
	{	
		return get(ctx, DateAcct, 0, null);
	}	//	get
	

	public static MPeriod get (Properties ctx, Timestamp DateAcct, int AD_Org_ID, String trxName)
	{
		
		if (DateAcct == null)
			return null;
		
        
        return findByCalendar(ctx, DateAcct, trxName);
	}	//	get

	@Deprecated
	public static MPeriod get (Properties ctx, Timestamp DateAcct, int AD_Org_ID)
	{
		return get(ctx, DateAcct, AD_Org_ID, null);
	}



	public static MPeriod findByCalendar(Properties ctx, Timestamp DateAcct, String trxName) {
		
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		//	Search in Cache first
		Iterator<MPeriod> it = s_cache.values().iterator();
		while (it.hasNext())
		{
			MPeriod period = (MPeriod)it.next();
			if (period.isStandardPeriod() && period.isInPeriod(DateAcct) && period.getAD_Client_ID() == AD_Client_ID) 
				return period;
		}
		
		//	Get it from DB
	    MPeriod retValue = null;
		String sql = "SELECT * "
			+ "FROM C_Period "
			+ "WHERE ? BETWEEN TRUNC(StartDate) AND TRUNC(EndDate)"
			+ " AND IsActive=? AND PeriodType=? And AD_Client_ID = ?";
        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setTimestamp (1, TimeUtil.getDay(DateAcct));
            pstmt.setString(2, "Y");
            pstmt.setString(3, "S");
            pstmt.setInt(4, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPeriod period = new MPeriod(ctx, rs, trxName);
				Integer key = Integer.valueOf(period.getC_Period_ID());
				s_cache.put (key, period);
				if (period.isStandardPeriod())
					retValue = period;
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "DateAcct=" + DateAcct, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (retValue == null)
			if (s_log.isLoggable(Level.INFO)) s_log.info("No Standard Period for " + DateAcct 
				+ " (AD_Client_ID=" + AD_Client_ID + ")");
		return retValue;
	}


	public static int getC_Period_ID (Properties ctx, Timestamp DateAcct)
	{
		MPeriod period = get (ctx, DateAcct, 0, null);
		if (period == null)
			return 0;
		return period.getC_Period_ID();
	}	//	getC_Period_ID
	

	public static int getC_Period_ID (Properties ctx, Timestamp DateAcct, int AD_Org_ID)
	{
		MPeriod period = get (ctx, DateAcct, AD_Org_ID, null);
		if (period == null)
			return 0;
		return period.getC_Period_ID();
	}	//	getC_Period_ID


	public static boolean isOpen (Properties ctx, Timestamp DateAcct, int AD_Org_ID)
	{
		if (DateAcct == null)
		{
			s_log.warning("No DateAcct");
			return false;
		}
		
		MPeriod period = MPeriod.get (ctx, DateAcct, AD_Org_ID, null);
		if (period == null)
		{
			s_log.warning("No Period for " + DateAcct + "");
			return false;
		}
		boolean open = "O".equals( period.getPeriodAction()) ? true : false;
		if (!open)
			s_log.warning(period.getName()
				+ ": Not open for  (" + DateAcct + ")");
		return open;
	}	
	

	private static CCache<Integer,MPeriod> s_cache = new CCache<Integer,MPeriod>(Table_Name, 10);
	
	/**	Logger							*/
	private static CLogger			s_log = CLogger.getCLogger (MPeriod.class); 
	
	

	public MPeriod (Properties ctx, int C_Period_ID, String trxName)
	{
		super (ctx, C_Period_ID, trxName);
		if (C_Period_ID == 0)
		{
		
			setPeriodType (PERIODTYPE_StandardCalendarPeriod);
		}
	}	//	MPeriod

	
	public MPeriod (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPeriod

	public MPeriod (MYear year, int PeriodNo, String name, 
		Timestamp startDate,Timestamp endDate)
	{
		this (year.getCtx(), 0, year.get_TrxName());
		setClientOrg(year);
		setC_Year_ID(year.getC_Year_ID());
		setPeriodNo(PeriodNo);
		setName(name);
		setStartDate(startDate);
		setEndDate(endDate);
	}	//	MPeriod
	
	
	
	public boolean isInPeriod (Timestamp date)
	{
		if (date == null)
			return false;
		Timestamp dateOnly = TimeUtil.getDay(date);
		Timestamp from = TimeUtil.getDay(getStartDate());
		if (dateOnly.before(from))
			return false;
		Timestamp to = TimeUtil.getDay(getEndDate());
		if (dateOnly.after(to))
			return false;
		return true;
	}	//	isInPeriod
	

	

	public boolean isStandardPeriod()
	{
		return PERIODTYPE_StandardCalendarPeriod.equals(getPeriodType());
	}	//	isStandardPeriod
	
	

	protected boolean beforeSave (boolean newRecord)
	{
		//	Truncate Dates
		Timestamp date = getStartDate(); 
		if (date != null)
			setStartDate(TimeUtil.getDay(date));
		else
			return false;
		//
		date = getEndDate();
		if (date != null)
			setEndDate(TimeUtil.getDay(date));
		else
			setEndDate(TimeUtil.getMonthLastDay(getStartDate()));
		
		if (getEndDate().before(getStartDate()))
		{
			SimpleDateFormat df = DisplayType.getDateFormat(DisplayType.Date);
			log.saveError("Error", df.format(getEndDate()) + " < " + df.format(getStartDate()));
			return false;
		}
		
		
		Query query = MTable.get(getCtx(), "C_Period")
		.createQuery("(? BETWEEN StartDate AND EndDate" +
				" OR ? BETWEEN StartDate AND EndDate)" +
				" AND PeriodType=?",get_TrxName());
		query.setParameters(getStartDate(), getEndDate(),	getPeriodType());
		
		List<MPeriod> periods = query.list();
		
		for ( int i=0; i < periods.size(); i++)
		{
			if ( periods.get(i).getC_Period_ID() != getC_Period_ID() )
			{
				log.saveError("Error", "Period overlaps with: "	+ periods.get(i).getName());
				return false;
			}
		}
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		return success;
	}	//	afterSave
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MPeriod[");
		sb.append (get_ID())
			.append("-").append (getName())
			.append(", ").append(getStartDate()).append("-").append(getEndDate())
			.append ("]");
		return sb.toString ();
	}	//	toString
	

	
    
}	//	MPeriod
