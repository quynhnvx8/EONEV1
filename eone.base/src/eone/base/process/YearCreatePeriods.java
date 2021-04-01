
package eone.base.process;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.util.AdempiereUserError;

import eone.base.model.MYear;


public class YearCreatePeriods extends SvrProcess
{
	private int	p_C_Year_ID = 0;
	private Timestamp p_StartDate;
	private String p_DateFormat;
	

	protected void prepare ()
	{
		p_DateFormat = "MM-YYYY";
		p_C_Year_ID = getRecord_ID();
	}	

	
	protected String doIt ()
		throws Exception
	{
		MYear year = new MYear (getCtx(), p_C_Year_ID, get_TrxName());
		if (p_C_Year_ID == 0 || year.get_ID() != p_C_Year_ID)
			throw new AdempiereUserError ("@NotFound@: @C_Year_ID@ - " + p_C_Year_ID);
		if (log.isLoggable(Level.INFO)) log.info(year.toString());
		//
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, Integer.parseInt(year.getFiscalYear()));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		p_StartDate = new Timestamp(cal.getTimeInMillis());
		
		if (year.createStdPeriods(null, p_StartDate, p_DateFormat))
			return "@OK@";
		return "@Error@";
	}	
	
}	
