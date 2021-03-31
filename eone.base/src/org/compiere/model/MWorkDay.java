package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

public class MWorkDay extends X_HR_WorkDay
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MWorkDay (Properties ctx, int HR_WorkDay_ID, String trxName)
	{
		super (ctx, HR_WorkDay_ID, trxName);
		
	}	//	MAssetUse

	private static CCache<String,MWorkDay> s_cache = new CCache<String,MWorkDay>(Table_Name, 5);
	
	public static MWorkDay get (Properties ctx, String name, String trxName)
	{
		if(s_cache.containsKey(name))
			return s_cache.get(name);
		
		final String whereClause = "Name=? And IsActive = 'Y' And AD_Client_ID = ?";
		List<MWorkDay> retValue = new Query(ctx, Table_Name,whereClause,trxName)
		.setParameters(name, Env.getAD_Client_ID(ctx))
		.list();
		for(int i = 0; i < retValue.size(); i++) {
			s_cache.put(retValue.get(i).getName(), retValue.get(i));
		}
		return s_cache.get(name);
	}
	
	/**
	 * Lay danh sach ngay lam viec trong khoang thoi gian tu FromDate den ToDate => Co the dung chung o nhieu noi
	 * @param ctx
	 * @param trxName
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static Map<Timestamp, Timestamp> getListWorkDay(Properties ctx, String trxName, Timestamp fromDate, Timestamp toDate) {
		int day = 0;
		if (toDate == null && fromDate == null) {
			fromDate = new Timestamp(new Date().getTime());
			toDate = fromDate;
		} else if (fromDate != null && toDate == null) {
			toDate = fromDate;
		}
		
		Map<Timestamp, Timestamp> list = new HashMap<Timestamp, Timestamp>();
		MWorkDay [] listItem = getAllItems(ctx, trxName);
		while (fromDate.compareTo(toDate) <= 0) {
			day = TimeUtil.getDayOfWeek(fromDate);
			for(int i = 0; i < listItem.length; i++) {
				MWorkDay workday = listItem[i];
				String days = Env.numToChar(day, 2);
				if (days.equalsIgnoreCase(workday.getName())) {
					list.put(fromDate, fromDate);
				}
			}
			
			fromDate = TimeUtil.getNextDay(fromDate);
		}
		return list;
	}
	
	public static MWorkDay[] getAllItems (Properties ctx, String trxName)
	{
		
		final String whereClause = "AD_Client_ID=? And IsActive = 'Y'";
		List<MWorkDay> retValue = new Query(ctx, Table_Name,whereClause,trxName)
		.setParameters(Env.getAD_Client_ID(ctx))
		.list();
		
		MWorkDay [] list = new MWorkDay[retValue.size()];
		retValue.toArray(list);
		return list;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_Name, getName());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_WorkDay_ID, getHR_WorkDay_ID());
		dataColumn = null;
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + getName());
			return false;
		}
		
		if (getStartTime().compareTo(getEndTime()) > 0) {
			log.saveError("Error", "StartTime must be less than EndTime");
			return false;
		}
		
		if (getStartTime1().compareTo(getEndTime1()) > 0) {
			log.saveError("Error", "StartTime must be less than EndTime");
			return false;
		}
		if (getStartTime2().compareTo(getEndTime2()) > 0) {
			log.saveError("Error", "StartTime must be less than EndTime");
			return false;
		}
		if (getStartTime3().compareTo(getEndTime3()) > 0) {
			//log.saveError("Error", "StartTime must be less than EndTime");
			//return false;
		}
		return true;
	}


	public MWorkDay (Properties ctx, ResultSet rs, String trxName)
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
