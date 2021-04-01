package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

public class MHoliday extends X_HR_Holiday
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MHoliday (Properties ctx, int HR_Holiday_ID, String trxName)
	{
		super (ctx, HR_Holiday_ID, trxName);
		
	}	//	MAssetUse

	//private static CCache<Integer,MHoliday> s_cache = new CCache<Integer,MHoliday>(Table_Name, 5);
	
	public static MHoliday get (Properties ctx, int HR_Holiday_ID, String trxName)
	{
		return (MHoliday)MTable.get(ctx, Table_Name).getPO(HR_Holiday_ID, trxName);		
	}
	
	private static MHoliday[] getAllItems (Properties ctx, String trxName)
	{
		
		final String whereClause = "IsActive = 'Y'";
		List<MHoliday> retValue = new Query(ctx, Table_Name,whereClause,trxName)
		.list();
		MHoliday [] list = new MHoliday[retValue.size()];
		retValue.toArray(list);
		return list;
	}
	
	/**
	 * Lay danh sach ngay nghi le trong khoang thoi gian tu FromDate den ToDate => Co the dung chung o nhieu noi
	 * @param ctx
	 * @param trxName
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static Map<Timestamp, Timestamp> getListHoliday(Properties ctx, String trxName, Timestamp fromDate, Timestamp toDate) {
		int year = 0;
		int count = 0;
		if (toDate == null && fromDate == null) {
			year = TimeUtil.getYearSel(new Timestamp(new Date().getTime()));
			count = 1;
		} else if (fromDate != null && toDate == null) {
			year = TimeUtil.getYearSel(fromDate);
			count = 1;
		} else if (fromDate != null && toDate != null) {
			count = TimeUtil.getYearSel(toDate) - TimeUtil.getYearSel(fromDate);
			if (count == 0)
				count = 1;
		}
		Map<Timestamp, Timestamp> list = new HashMap<Timestamp, Timestamp>();
		MHoliday [] listItem = getAllItems(ctx, trxName);
		Timestamp input = null;
		for (int y = 0; y < count; y++) {
			year = year + y;
			for(int i = 0; i < listItem.length; i++) {
				MHoliday holiday = listItem[i];
				//Neu trong nam thi lay tren DB.
				if(holiday.getHolidayType().equalsIgnoreCase(HOLIDAYTYPE_ThisYear))
					year = holiday.getYearHoliday();
				
				//Neu Am lich thi chuyen sang Duong lich
				if (holiday.getCalendarType().equalsIgnoreCase(CALENDARTYPE_CalendarLunar)) 
					input = TimeUtil.getSolar(holiday.getDayOfMonth(), holiday.getMonthOfYear(), year);
				else
					input = TimeUtil.getDay(year, holiday.getMonthOfYear(), holiday.getDayOfMonth());
				list.put(input, input);
			}
		}
		
		return list;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_CalendarType, getCalendarType());
		dataColumn.put(COLUMNNAME_HolidayType, getHolidayType());
		dataColumn.put(COLUMNNAME_DayOfMonth, getDayOfMonth());
		dataColumn.put(COLUMNNAME_MonthOfYear, getMonthOfYear());
		if (getYearHoliday() != 0) {
			dataColumn.put(COLUMNNAME_YearHoliday, getYearHoliday());
		}
		boolean checkdouble = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_Holiday_ID, getHR_Holiday_ID());
		dataColumn = null;
		if (!checkdouble) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + 
					getCalendarType() + "_" + getHolidayType() + "_" + getDayOfMonth() + "_" + getMonthOfYear());
			return false;
		}
		return true;
	}


	public MHoliday (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAssetUse


	protected boolean afterSave (boolean newRecord,boolean success)
	{
		log.info ("afterSave");
		if (!success)
			return success;
		
		return success;
		 
		
	}	//	afterSave



}
