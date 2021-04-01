
package eone.base.process;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import eone.base.model.MDayOff;
import eone.base.model.MDayOffLine;
import eone.base.model.MHoliday;
import eone.base.model.MTimekeeperMap;
import eone.base.model.MWorkDay;
import eone.base.model.PO;
import eone.base.model.X_HR_DayOffLine;
import eone.base.model.X_HR_SalaryLine;

public class CreateDayOffLine extends SvrProcess {

	/**
	 * Quynhnv.x8: Tong hop cong nhan vien
	 */
	private int BATCH_SIZE = Env.getBatchSize(getCtx());
	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		String sqlDel = "Delete from HR_DayOffLine Where HR_DayOff_ID = ?";
		DB.executeUpdate(sqlDel, getRecord_ID(), get_TrxName());
		
		double dayOffStandard = 12;//TODO: Tinh toan them cho cau hinh ngay nghi phep de lay gia tri nay
		
		Map<String, MTimekeeperMap> listItems = MTimekeeperMap.getAllItems(getCtx(), get_TrxName());
		List<List<Object>> values = new ArrayList<List<Object>>();
		MDayOff dayoff = MDayOff.get(getCtx(), getRecord_ID(), get_TrxName());
		int daybetween = TimeUtil.getDaysBetween(dayoff.getStartTime(), dayoff.getEndTime());
		Timestamp dayInc = dayoff.getStartTime();
		MDayOffLine line = null;
		int sequence = -1;
		
		//Lay list danh sach ngay lam viec va ngay nghi
		Map<Timestamp, Timestamp> listHoliday = MHoliday.getListHoliday(getCtx(), get_TrxName(), dayoff.getStartTime(), dayoff.getEndTime());
		Map<Timestamp, Timestamp> listWorkDay = MWorkDay.getListWorkDay(getCtx(), get_TrxName(), dayoff.getStartTime(), dayoff.getEndTime());
		
		while (dayInc.compareTo(dayoff.getEndTime()) <= 0) {
			
			//Khong phai ngay lam viec thi bo qua
			if (!listWorkDay.containsKey(dayInc)) {
				dayInc = TimeUtil.getNextDay(dayInc);
				continue;
			}
			
			line = new MDayOffLine(getCtx(), 0, get_TrxName());
			
			//Neu la ngay le thi cham cong la nghi le, nguoc lai thi moi xet co phai la ngay phep khong
			if (listHoliday.containsKey(dayInc)) {
				line.setDayOffType(X_HR_DayOffLine.DAYOFFTYPE_NL);
			} else {
				if (listItems.containsKey(dayoff.getDayOffType()) 
						&& listItems.get(dayoff.getDayOffType()).isPaidDayoff()
						&& dayOffStandard > 0) 
				{
					line.setDayOffType(dayoff.getDayOffType());
					dayOffStandard -= Double.parseDouble(listItems.get(dayoff.getDayOffType()).getValueNumber().toString());					
				} else {
					line.setDayOffType(X_HR_DayOffLine.DAYOFFTYPE_KLCP);
				}
			}//End ngay le
			
			line.setHR_DayOff_ID(getRecord_ID());
			
			line.setStartTime(dayInc);
			line.setEndTime(dayInc);
			line.setAddress(dayoff.getAddress());
			if (daybetween > 5) {
				sequence = DB.getNextID(Env.getAD_Client_ID(Env.getCtx()), X_HR_DayOffLine.Table_Name, null);
				List<Object> lstParam = PO.getBatchValueList(line, X_HR_DayOffLine.Table_ID, get_TrxName(), sequence);
				values.add(lstParam);
				if(values.size() >= BATCH_SIZE) {
					String sqlInsert = PO.getSqlInsert(X_HR_SalaryLine.Table_ID, null);
					if(values.size() > 0) {
						String err = DB.excuteBatch(sqlInsert, values, null);
						if(err != null) {
							try {
								DB.rollback(false, null);
							} catch(Exception e) {
							}
						}
					}
					values.clear();
				}
			} else {
				line.save();
			}
			
			dayInc = TimeUtil.getNextDay(dayInc);
		}
		String sqlInsert = PO.getSqlInsert(X_HR_DayOffLine.Table_ID, null);
		if(values.size() > 0) {
			String err = DB.excuteBatch(sqlInsert, values, null);
			if(err != null) {
				try {
					DB.rollback(false, null);
				} catch(Exception e) {
				}
			}
		}
		
		//Clear Memory Leak
		listItems.clear();
		listHoliday.clear();
		listWorkDay.clear();
		return "Success";
	}
}
