package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.TimeUtil;

public class MTimekeeperLine extends X_HR_TimekeeperLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;
	
	private static CCache<String,MTimekeeperLine> s_cache = new CCache<String,MTimekeeperLine>(Table_Name, 10);

	public MTimekeeperLine (Properties ctx, int HR_TimekeeperLine_ID, String trxName)
	{
		super (ctx, HR_TimekeeperLine_ID, trxName);
		
	}	//	MAssetUse

	public static MTimekeeperLine getItem(Properties ctx, int HR_Employee_ID, int C_Period_ID, String trxName) {
		String key = HR_Employee_ID + "_" + C_Period_ID;
		if (s_cache.containsKey(key)) 
			return s_cache.get(key);
		String whereClause = "HR_Timekeeper_ID in  (Select HR_Timekeeper_ID From HR_Timekeeper Where C_Period_ID = ?)";
		List<MTimekeeperLine> query = new Query(ctx, Table_Name, whereClause, trxName)
				.setParameters(C_Period_ID)
				.list();
		MTimekeeperLine line = null;
		for(int i = 0; i < query.size(); i ++) {
			line = query.get(i);
			key = line.getHR_Employee_ID() + "_" + C_Period_ID;
			s_cache.put(key, line);
		}
		key = HR_Employee_ID + "_" + C_Period_ID;
		return s_cache.get(key);
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (isProcessed()) {
			log.saveError("Error", "Record Approved");
			return false;
		}
		if (getDateApproved() != null) {
			int day = TimeUtil.getDaySel(getDateApproved());
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day01) && day > 1) {
				log.saveError("Error", "Day 01  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day02) && day > 2) {
				log.saveError("Error", "Day 02  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day03) && day > 3) {
				log.saveError("Error", "Day 03  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day04) && day > 4) {
				log.saveError("Error", "Day 04  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day05) && day > 5) {
				log.saveError("Error", "Day 05  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day06) && day > 6) {
				log.saveError("Error", "Day 06  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day07) && day > 7) {
				log.saveError("Error", "Day 07  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day08) && day > 8) {
				log.saveError("Error", "Day 08  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day09) && day > 9) {
				log.saveError("Error", "Day 09  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day10) && day > 10) {
				log.saveError("Error", "Day 10  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day11) && day > 11) {
				log.saveError("Error", "Day 11  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day12) && day > 12) {
				log.saveError("Error", "Day 12  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day13) && day > 13) {
				log.saveError("Error", "Day 13  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day14) && day > 14) {
				log.saveError("Error", "Day 14  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day15) && day > 15) {
				log.saveError("Error", "Day 15  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day16) && day > 16) {
				log.saveError("Error", "Day 16  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day17) && day > 17) {
				log.saveError("Error", "Day 17  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day18) && day > 18) {
				log.saveError("Error", "Day 18  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day19) && day > 19) {
				log.saveError("Error", "Day 19  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day20) && day > 20) {
				log.saveError("Error", "Day 20  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day21) && day > 21) {
				log.saveError("Error", "Day 21  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day22) && day > 22) {
				log.saveError("Error", "Day 22  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day23) && day > 23) {
				log.saveError("Error", "Day 23  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day24) && day > 24) {
				log.saveError("Error", "Day 24  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day25) && day > 25) {
				log.saveError("Error", "Day 25  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day26) && day > 26) {
				log.saveError("Error", "Day 26  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day27) && day > 27) {
				log.saveError("Error", "Day 27  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day28) && day > 28) {
				log.saveError("Error", "Day 28  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day29) && day > 29) {
				log.saveError("Error", "Day 29  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day30) && day > 30) {
				log.saveError("Error", "Day 30  Approved");
				return false;
			}
			if (getDateApproved() != null && is_ValueChanged(COLUMNNAME_Day31) && day > 31) {
				log.saveError("Error", "Day 31  Approved");
				return false;
			}
		}
		
		return true;
	}


	public MTimekeeperLine (Properties ctx, ResultSet rs, String trxName)
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
	
	public boolean isApproved(Timestamp dateInput, int HR_Employee_ID) {
		if (getDateApproved() == null) 
			return false;
		if (!isProcessed())
			return false;
		if (dateInput.compareTo(getDateApproved()) > 0 && HR_Employee_ID == getHR_Employee_ID()) {
			return false;
		}
		return true;
	}

}
