/***********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Contributors:                                                       *
 * - Carlos Ruiz - globalqss                                           *
 * Sponsored by FH                                                     *
 **********************************************************************/

package eone.base.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.model.I_HR_SalaryLine;
import eone.base.model.MConfig;
import eone.base.model.MFamilyTies;
import eone.base.model.MPayroll;
import eone.base.model.MSalary;
import eone.base.model.MSalaryExtra;
import eone.base.model.MSalaryLine;
import eone.base.model.MTaxPersonal;
import eone.base.model.PO;
import eone.base.model.X_HR_Config;

public class CalculateSalary extends SvrProcess {

	/**
	 * Quynhnv.x8: Phe duyet cong den ngay nao day
	 */
	private int BATCH_SIZE = Env.getBatchSize(getCtx());
	@Override
	protected void prepare() {
		//
		/*
		for (ProcessInfoParameter para : getParameter()) {
			String name = para.getParameterName();
			if ("Date".equals(name)) {
				p_Date  = para.getParameterAsTimestamp();
			} else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
		*/
	}

	@Override
	protected String doIt() throws Exception {
		String sql = "Select count(1) From HR_SalaryLine Where HR_Salary_ID = ?";
		int no = DB.getSQLValue(get_TrxName(), sql, getRecord_ID());
		if (no <= 0)
			return "No record detail!";
		
		MSalary salary = MSalary.get(getCtx(), getRecord_ID());
		
		Map<Integer,MSalaryLine> listEmployees = salary.getAllEmployee(getCtx(), get_TrxName());
		//Lay danh sach thong tin luong duoc cau hinh.
		Map<Integer, MPayroll> listItems = MPayroll.getAllItem(getCtx(), get_TrxName(), salary.getAD_Client_ID());
		
		//Tinh phu cap
		List<MSalaryExtra> listExtra = null;
		BigDecimal totalExtra;
		MSalaryExtra extra;
		
		MSalaryLine line;
		MPayroll payroll;
		
		BigDecimal totalSalaryPart1;			//Tong luong ky 1: = Luong di lam + luong phu cap ... + luong lam them.
		BigDecimal totalSalaryWorkDay;			//Tong luong di lam = Luong di lam + Luong nghi phep
		BigDecimal totalSalaryWorkExtra;		//Tong luong lam them = Lam them ngay thuong + lam them ngay le
		BigDecimal totalSalaryExtra;			//Tong luong phu cap: chuc vu, ...
		BigDecimal totalWorkSalary;				//Tong ngay lam viec
		BigDecimal salaryOneDay;				//Luong cua 1 ngay
		BigDecimal totalTaxableIncom;			//Tong thu nhap chịu thu = Gross - cac khoan giam tru.
		double taxIncom;					//Thue thu nhap ca nhan
		String sqlUpdate = PO.getSqlUpdate(I_HR_SalaryLine.Table_ID);
		
		List<List<Object>> values = new ArrayList<List<Object>>();
		List<Object> params;
		
		//Lay cac thong tin cau hinh
		Map<String, MConfig> hrConfig = MConfig.getAllItem(getCtx(), get_TrxName(), Env.getAD_Client_ID(getCtx()));
		BigDecimal healthInsurance = 		hrConfig.get(X_HR_Config.NAME_HealthInsurance).getValueNumber();
		BigDecimal socialInsurance = 		hrConfig.get(X_HR_Config.NAME_SocialInsurance).getValueNumber();
		BigDecimal unemploymentInsurance = 	hrConfig.get(X_HR_Config.NAME_UnemploymentInsurance).getValueNumber();
		BigDecimal personalDeduction = 		hrConfig.get(X_HR_Config.NAME_PersonalDeduction).getValueNumber();
		BigDecimal dependentDeduction = 	hrConfig.get(X_HR_Config.NAME_DependentDeduction).getValueNumber();
		BigDecimal baseSalary = 			hrConfig.get(X_HR_Config.NAME_BaseSalaryPay).getValueNumber();
		
		//So nguoi phu thuoc
		Map<Integer, BigDecimal> listDependent = MFamilyTies.getAllItem(getCtx(), salary.getAD_Client_ID(), salary.getC_Period_ID());
		BigDecimal numberDependent;
		
		for(Map.Entry<Integer, MSalaryLine> entry : listEmployees.entrySet()) {
			totalSalaryPart1 = Env.ZERO;
			totalSalaryWorkDay = Env.ZERO;
			totalSalaryWorkExtra = Env.ZERO;
			totalSalaryExtra = Env.ZERO;
			totalWorkSalary = Env.ZERO;
			totalTaxableIncom = Env.ZERO;
			numberDependent = Env.ZERO;
			totalExtra = Env.ZERO;
			taxIncom = 0;
			
			line = entry.getValue();
			
			if (listItems.containsKey(line.getHR_Employee_ID())) {
				params = new ArrayList<Object>();
				
				//Thong tin luong
				payroll = listItems.get(line.getHR_Employee_ID());
				
				
				//Luong 1 ngay cong di lam
				salaryOneDay = payroll.getSalaryBase()
						.multiply(payroll.getSalaryRate())
						.divide(new BigDecimal(line.getWorkdaySTD()), 2, RoundingMode.HALF_UP);
				
				line.setSalaryBase(payroll.getSalaryBase());
				
				//Tong ngay cong tinh luong: di lam + nghi phep + nghi le
				totalWorkSalary = line.getTotalWorkDay().add(line.getTotalDayOff());
				
				//Neu tong cong di lam vuot cong chuan thi lay bang cong chuan.
				if (totalWorkSalary.compareTo(new BigDecimal(line.getWorkdaySTD())) > 0) {
					totalWorkSalary = new BigDecimal(line.getWorkdaySTD());
				}
				
				//Tong luong di lam + nghi phep
				totalSalaryWorkDay = salaryOneDay.multiply(totalWorkSalary);
				
				//Nhan them voi ty le huong luong
				totalSalaryWorkDay = totalSalaryWorkDay.multiply(payroll.getSalaryPercent()).divide(Env.ONEHUNDRED);
				
				//Tinh luong lam them: (Ngay thuong + Ngay le ) * Luong 1 ngay. (Trong nay da tinh ty le huong roi)
				totalSalaryWorkExtra = (line.getTotalWorkExtra().add(line.getTotalWorkExtraHoliday())).multiply(salaryOneDay);
				
				//Tinh tong cac loai luong:
				totalSalaryPart1 = totalSalaryWorkDay.add(totalSalaryExtra).add(totalSalaryWorkExtra);
				line.setSalaryPart1(totalSalaryPart1.setScale(Env.getScaleFinal(), RoundingMode.HALF_UP));
				
				line.setSalaryPosition(Env.ZERO);
				line.setSalaryProduction(Env.ZERO);
				
				//Tinh luong phu cap
				listExtra = MSalaryExtra.getAllItem(getCtx(), get_TrxName(), salary.getAD_Client_ID(), line.getHR_Employee_ID());
				if (listExtra != null) {
					for(int i = 0; i < listExtra.size(); i++) {
						extra = listExtra.get(i);
						String str = extra.getFormulaSetup();
						if (extra.getFormulaSetup().contains("@BaseSalary@")) {
							str = baseSalary.toString();
						}
						double value = 0;
						if (!str.isEmpty() && str != null)
							value = Env.getValueByFormula(str);
						totalExtra = totalExtra.add(extra.getPercent().multiply(new BigDecimal(value)));
					}
					line.setSalaryExtra(totalExtra);
				}
				
				line.setSalaryGross(line.getSalaryPart1()
						.add(line.getSalaryProduction())
						.add(line.getSalaryExtra()));
				
				line.setInsua_Health(line.getSalaryGross().multiply(healthInsurance).divide(Env.ONEHUNDRED, 0, RoundingMode.HALF_UP));
				line.setInsua_Social(line.getSalaryGross().multiply(socialInsurance).divide(Env.ONEHUNDRED, 0, RoundingMode.HALF_UP));
				line.setInsua_Unemployee(line.getSalaryGross().multiply(unemploymentInsurance).divide(Env.ONEHUNDRED, 0, RoundingMode.HALF_UP));
				
				//Tinh so nguoi phu thuoc
				if (listDependent.containsKey(line.getHR_Employee_ID()))
					numberDependent = listDependent.get(line.getHR_Employee_ID());
				if (numberDependent == null) 
					numberDependent = Env.ZERO;
				
				//Tinh tong thu nhap chiu thue
				totalTaxableIncom = line.getSalaryGross()
						.subtract(line.getInsua_Health())
						.subtract(line.getInsua_Social())
						.subtract(line.getInsua_Unemployee())
						.subtract(personalDeduction)
						.subtract(dependentDeduction.multiply(numberDependent));
				
				taxIncom = MTaxPersonal.getAmountTaxPersonal(totalTaxableIncom, getCtx(), get_TrxName(), Env.getAD_Client_ID(getCtx())); 
				line.setTaxAmt(new BigDecimal(taxIncom).setScale(Env.getScaleFinal(), RoundingMode.HALF_UP));
				
				line.setSalaryNet(line.getSalaryGross()
						.subtract(line.getInsua_Health())
						.subtract(line.getInsua_Social())
						.subtract(line.getInsua_Unemployee())
						.subtract(line.getTaxAmt())						
						);
				
				line.save();
				//Add du lieu vao list de update thoe batch
				params = PO.getBatchValueList(line, I_HR_SalaryLine.Table_ID, get_TrxName(), line.getHR_SalaryLine_ID());
				values.add(params);
				
				
				if (values.size() >= BATCH_SIZE) {
					String err = DB.excuteBatch(sqlUpdate, values, get_TrxName());
					if(err != null) {
						try {
							DB.rollback(false, null);
						} catch(Exception e) {
						}
					}
					values.clear();
				}
			}
		}
		/*
		if (values.size() > 0) {
			String err = DB.excuteBatch(sqlUpdate, values, get_TrxName());
			if(err != null) {
				try {
					DB.rollback(false, null);
				} catch(Exception e) {
				}
			}
			values.clear();
		}
		*/
		listEmployees.clear();
		hrConfig.clear();
		listItems.clear();
		listDependent.clear();
		return "Success";
	}
	
	
}
