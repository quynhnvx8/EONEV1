
package eone.base.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.compiere.model.MSalary;
import org.compiere.model.MSalaryLine;
import org.compiere.model.MTimekeeperMap;
import org.compiere.model.X_HR_SalaryLine;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class CreateEmployeeSalary extends SvrProcess {

	/**
	 * Quynhnv.x8: Tong hop cong nhan vien
	 */

	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		MSalary salary = MSalary.get(getCtx(), getRecord_ID());
		
		String sql = "Select l.HR_Employee_ID, h.WorkdaySTD "+
				" From HR_TimekeeperLine l inner join HR_Timekeeper h On l.HR_Timekeeper_ID = h.HR_Timekeeper_ID "+
				" Where h.C_Period_ID = ? And h.Processed = 'Y' And h.AD_Client_ID = ? Group by l.HR_Employee_ID, h.WorkdaySTD ";
		PreparedStatement ps = DB.prepareCall(sql);
		ResultSet rs = null;
		ps.setInt(1, salary.getC_Period_ID());
		ps.setInt(2, salary.getAD_Client_ID());
		rs = ps.executeQuery();
		MSalaryLine line = null;
		Map<Integer, MSalaryLine> employeeExists = MSalaryLine.get(getCtx(), salary.getC_Period_ID(), salary.getAD_Client_ID());
		
		//Lay tong cac loai ngay cong cua tung nhan vien
		Map<Integer, Map<MTimekeeperMap, Double>> listTimekeeper = getTimekeeper(salary);
		Map<MTimekeeperMap, Double> lineObj = null;
		MTimekeeperMap item = null;
		int HR_Employee_ID;
		
		//khai bao bien cac ngay cong:
		BigDecimal totalStandard;					//Tong cong di lam
		BigDecimal totalDayoffPermission;			//Tong ngay nghi phep duoc huong luong.
		BigDecimal totalNotPaidOffPermistion;		//Tong ngay nghi phep khong duoc huong luong
		BigDecimal totalNotPaidOffUnPermisstion;	//Tong ngay nghi khong phep va khong duoc huong luong
		BigDecimal totalWorkExtra;					//Tong ngay ngay thuong di lam them
		BigDecimal totalWorkExtraHoliday;			//Tong ngay le di lam them
		BigDecimal totalDayMartenity;				//Tong ngay nghi thai san
		while (rs.next()) {
			totalStandard = Env.ZERO;
			totalDayoffPermission = Env.ZERO;
			totalNotPaidOffPermistion = Env.ZERO;
			totalNotPaidOffUnPermisstion = Env.ZERO;
			totalWorkExtra = Env.ZERO;
			totalWorkExtraHoliday = Env.ZERO;
			totalDayMartenity = Env.ZERO;
			
			HR_Employee_ID = rs.getInt("HR_Employee_ID");
			if (listTimekeeper.containsKey(HR_Employee_ID))
			{
				lineObj = listTimekeeper.get(HR_Employee_ID);
				for(Map.Entry<MTimekeeperMap, Double> entry : lineObj.entrySet()) {
					item = entry.getKey();
					if (item.isWorkDay() && entry.getValue() != 0)
						totalStandard = totalStandard.add(new BigDecimal(entry.getValue().toString()));
					
					if (item.isDayOffPermistion() && entry.getValue() != 0)
						totalDayoffPermission = totalDayoffPermission.add(new BigDecimal(entry.getValue().toString()));
					
					if (item.isNotPaidDayoffPemission() && entry.getValue() != 0)
						totalNotPaidOffPermistion = totalNotPaidOffPermistion.add(new BigDecimal(entry.getValue().toString()));
					
					if (item.isNotPaidDayoffUnPermistion() && entry.getValue() != 0)
						totalNotPaidOffUnPermisstion = totalNotPaidOffUnPermisstion.add(new BigDecimal(entry.getValue().toString()));
					
					if (item.isWorkingNormal() && entry.getValue() != 0)
						totalWorkExtra = totalWorkExtra.add(new BigDecimal(entry.getValue().toString()));
					
					if (item.isWorkingHoliday() && entry.getValue() != 0)
						totalWorkExtraHoliday = totalWorkExtraHoliday.add(new BigDecimal(entry.getValue().toString()));
					
					if (item.isMartenityDayoff() && entry.getValue() != 0)
						totalDayMartenity = totalDayMartenity.add(new BigDecimal(entry.getValue().toString()));
					
				}
				if (employeeExists.containsKey(HR_Employee_ID)) {
					line = MSalaryLine.get(getCtx(), HR_Employee_ID, salary.getC_Period_ID(), get_TrxName());			
				} else {
					int HR_SalaryLine_ID = DB.getNextID(getAD_Client_ID(), X_HR_SalaryLine.Table_Name, get_TrxName());
					line = new MSalaryLine(getCtx(), 0, get_TrxName());
					line.setHR_Employee_ID(HR_Employee_ID);
					line.setHR_Salary_ID(salary.getHR_Salary_ID());
					line.setC_Period_ID(salary.getC_Period_ID());
					line.setHR_SalaryLine_ID(HR_SalaryLine_ID);
				}			
				line.setWorkdaySTD(rs.getInt("WorkdaySTD"));
				line.setTotalWorkDay(totalStandard);						//Ngay di lam thuc te	
				line.setTotalDayOff(totalDayoffPermission); 				//Nghi phep => ngay phep duoc tru di.
				line.setTotalDayOffNone(totalNotPaidOffPermistion);			//Nghi khong luong co phep => Ngay phep giu nguyen
				line.setTotalDayOffUnNotice(totalNotPaidOffUnPermisstion);	//Nghi khong luong khong phep
				line.setTotalWorkExtra(totalWorkExtra);						//Lam them ngay thuong
				line.setTotalWorkExtraHoliday(totalWorkExtraHoliday);		//Lam them ngay le
				line.setTotalDayMartenity(totalDayMartenity);				//Ngay nghi thai san
				if (!line.save())
					return "Create false";
			}
			
			
		}
		//clear memory leaks
		listTimekeeper.clear();
		employeeExists.clear();
		return "Success";
	}

	
	private Map<Integer, Map<MTimekeeperMap, Double>> getTimekeeper(MSalary salary) throws SQLException {
		Map<String, MTimekeeperMap> listItems = MTimekeeperMap.getAllItems(getCtx(), get_TrxName());
		
		String sql = 
				"Select hr_employee_id, \r\n" + 
				"	concat_ws(',',day01,day02,day03,day04,day05,day06,day07,day08,day09,day10,\r\n" + 
				"			  day11,day12,day13,day14,day15,day16,day17,day18,day19,day20,\r\n" + 
				"			  day21,day22,day23,day23,day24,day25,day26,day27,day28,day29,day30,day31) as keeper \r\n" + 
				"From hr_timekeeperline l \r\n" + 
				"	Inner Join hr_timekeeper h On l.hr_timekeeper_id = h.hr_timekeeper_id\r\n" + 
				"where h.c_period_id = ? and h.ad_client_id = ?";
		PreparedStatement ps = DB.prepareCall(sql);
		ps.setInt(1, salary.getC_Period_ID());
		ps.setInt(2, salary.getAD_Client_ID());
		ResultSet rs = ps.executeQuery();
		MTimekeeperMap value = null;
		
		//Map nay luu du lieu la: Loai cong va so luong cong. 
		//VD: CongDiLam = NgayLamThucTe + NghiLe (W4, W8, NL)
		//NghiPhep = P4, P8 ...
		Map<MTimekeeperMap, Double> line;
		BigDecimal valueNumber = Env.ZERO;
		
		Map<Integer, Map<MTimekeeperMap, Double>> list = new HashMap<Integer, Map<MTimekeeperMap,Double>>();
		while (rs.next()) {
			String str = rs.getString("keeper");
			line = new HashMap<MTimekeeperMap, Double>();
			
			for(Map.Entry<String, MTimekeeperMap> entry : listItems.entrySet()) {
				
				value = entry.getValue();
				String strFind = entry.getKey();
		        int fromIndex = 0;
		        double count = 0;
		        
		        while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
		        	valueNumber = value.getValueNumber().multiply(value.getPercentage().divide(Env.ONEHUNDRED));
		        	count += Double.parseDouble(valueNumber.toString());
		            fromIndex++;		            
		        }
		        line.put(value, count);
			}
			list.put(rs.getInt("hr_employee_id"), line);
		}
		DB.close(rs, ps);
		//Clear memory leaks
		listItems.clear();
		
		return list;
	}
	
}
