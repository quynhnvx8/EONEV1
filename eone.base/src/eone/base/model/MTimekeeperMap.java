package eone.base.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MTimekeeperMap extends X_HR_TimekeeperMap
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MTimekeeperMap (Properties ctx, int HR_TimekeeperMap_ID, String trxName)
	{
		super (ctx, HR_TimekeeperMap_ID, trxName);
		
	}	//	MAssetUse

	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if(getPercentage().compareTo(Env.ZERO) < 0) {
			log.saveError("Error", "Percentage must be great than zero!");
			return false;
		}
		
		if (getValueNumber().compareTo(Env.ZERO) < 0 || getValueNumber().compareTo(Env.ONE) > 0) {
			log.saveError("Error", "ValueNumber must be between 0 and 1 !");
			return false;
		}
		
		Map<String, Object> dataColumn = new HashMap<String, Object>();
		dataColumn.put(COLUMNNAME_Name, getName());
		boolean check = isCheckDoubleValue(Table_Name, dataColumn, COLUMNNAME_HR_TimekeeperMap_ID, getHR_TimekeeperMap_ID());
		dataColumn = null;
		if (!check) {
			log.saveError("Error", Msg.getMsg(Env.getLanguage(getCtx()), "ValueExists") + ": " + getName());
			return false;
		}
		
		return true;
	}


	public MTimekeeperMap (Properties ctx, ResultSet rs, String trxName)
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

	private static CCache<String,MTimekeeperMap> s_cache = new CCache<String,MTimekeeperMap>(Table_Name, 5);

	public static Map<String, MTimekeeperMap> getAllItems (Properties ctx, String trxName)
	{
		Map<String, MTimekeeperMap> list = new HashMap<String, MTimekeeperMap>();
		final String whereClause = "IsActive = 'Y' And AD_Client_ID = ?";
		List<MTimekeeperMap> retValue = new Query(ctx, Table_Name,whereClause,trxName)
				.setParameters(Env.getAD_Client_ID(ctx))
				.list();
		for(int i = 0; i < retValue.size(); i++) {
			list.put(retValue.get(i).getName(), retValue.get(i));
		}
		return list;
	}
	
	public static MTimekeeperMap get (Properties ctx, String name, String trxName)
	{
		if(s_cache.containsKey(name))
			return s_cache.get(name);
		
		final String whereClause = "Name=? And AD_Client_ID = ? And IsActive = 'Y'";
		List<MTimekeeperMap> retValue = new Query(ctx, Table_Name,whereClause,trxName)
		.setParameters(name, Env.getAD_Client_ID(ctx))
		.list();
		for(int i = 0; i < retValue.size(); i++) {
			s_cache.put(retValue.get(i).getName(), retValue.get(i));
		}
		return s_cache.get(name);
	}
	
	/*
	 * Ham kiem tra nghi phep duoc huong luong
	 */
	public boolean isPaidDayoff() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiPhepCoLuong))
			return true;
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiPhepCoLuong))
			return true;
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiLe))
			return true;
		return false;
	}
	
	public boolean isWorkDay() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_LamMacDinh))
			return true;
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiLe))
			return true;
		return false;
	}
	
	public boolean isDayOffPermistion() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiPhepCoLuong))
			return true;
		return false;
	}
	
	/*
	 * Ham kiem tra nghi phep khong duoc huong luong
	 */
	public boolean isNotPaidDayoffPemission() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiKhongLuongCoPhep))
			return true;
		return false;
	}
	
	public boolean isNotPaidDayoffUnPermistion() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiKhongLuongKhongPhep))
			return true;
		return false;
	}
	
	/*
	 * Ham kiem tra nghi thai san
	 */
	public boolean isMartenityDayoff() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_NghiThaiSan))
			return true;
		return false;
	}
	
	/*
	 * Ham kiem tra di lam them ngay thuong
	 */
	public boolean isWorkingNormal() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_LamThemThuong))
			return true;
		return false;
	}
	
	/*
	 * Ham kiem tra di lam them ngay le
	 */
	public boolean isWorkingHoliday() {
		if (getTypeTimeKeeper().equalsIgnoreCase(TYPETIMEKEEPER_LamThemLe))
			return true;
		return false;
	}
}
