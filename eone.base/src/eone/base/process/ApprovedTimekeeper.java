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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import eone.base.model.MPeriod;
import eone.base.model.MTimekeeper;
import eone.base.model.MTimekeeperLine;

public class ApprovedTimekeeper extends SvrProcess {

	/**
	 * Quynhnv.x8: Phe duyet cong den ngay nao day
	 */
	private Timestamp p_Date;
	
	@Override
	protected void prepare() {
		//
		
		for (ProcessInfoParameter para : getParameter()) {
			String name = para.getParameterName();
			if ("Date".equals(name)) {
				p_Date  = para.getParameterAsTimestamp();
			} else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		String sql = "Select count(1) From HR_TimekeeperLine Where HR_Timekeeper_ID = ?";
		int no = DB.getSQLValue(get_TrxName(), sql, getRecord_ID());
		if (no <= 0)
			return "No record detail!";
		sql = "Update HR_TimekeeperLine set DateApproved = ? Where HR_Timekeeper_ID = ?";
		no = DB.executeUpdate(sql, new Object [] {p_Date, getRecord_ID()}, true, get_TrxName());
		if (no < 0)
			return "Update false";
		
		//Lay cac ngay cong dang ky nghi duoc phe duyet
		MTimekeeper timekeeper = MTimekeeper.get(getCtx(), getRecord_ID());
		MTimekeeperLine [] lines = timekeeper.getLine(getRecord_ID());
		List<InfoDayOff> listItem = getDataDayOff(getCtx(), timekeeper.getC_Period_ID(), timekeeper.getAD_Department_ID());
		List<Object> listParam;
		String sqlUpdate;
		String sqlWhere = " Where HR_TimekeeperLine_ID = ?";
		String day = "";
		for(int i = 0; i < lines.length; i++) {
			listParam = new ArrayList<Object>();
			sqlUpdate = "";
			for(int n = 0; n < listItem.size(); n++) {
				if (lines[i].getHR_Employee_ID() == listItem.get(n).getHR_Employee_ID()) {
					day = Env.numToChar(TimeUtil.getDaySel(listItem.get(n).getDateOff()), 2);
					if (n == 0) {
						sqlUpdate = "Update HR_TimekeeperLine set Day" + day + "=? ";
						listParam.add(listItem.get(n).getDayOff());
					}
					else {
						sqlUpdate = sqlUpdate + ", Day" + day + "=? ";
						listParam.add(listItem.get(n).getDayOff());
					}
				}
				
			}
			//Xu ly cuoi cung cau lenh SQl
			if (sqlUpdate != "") 
			{
				//Tam thoi chua dung BATCH. Moi nhan vien se update 1 lan.
				sqlUpdate = sqlUpdate + sqlWhere;
				listParam.add(lines[i].getHR_TimekeeperLine_ID());
				Object [] params = listParam.toArray();
				//listParam.toArray(params);
				DB.executeUpdate(sqlUpdate, params, true, get_TrxName());
			}
			
		}
		
		return "Success";
	}
	
	public List<InfoDayOff> getDataDayOff(Properties ctx, int C_Period_ID, int AD_Department_ID) throws SQLException {
		MPeriod period = MPeriod.get(ctx, C_Period_ID);
		String sql = "Select HR_Employee_ID, l.DayOffType, l.StartTime " +
				" From HR_DayOff h Inner Join HR_DayOffLine l On h.HR_DayOff_ID = l.HR_DayOff_ID " +
				" Where l.StartTime between ? And ? And h.Processed = 'Y' And h.AD_Department_ID = ?";
		PreparedStatement ps = DB.prepareCall(sql);
		ps.setTimestamp(1, period.getStartDate());
		ps.setTimestamp(2, period.getEndDate());
		ps.setInt(3, AD_Department_ID);
		ResultSet rs = ps.executeQuery();
		List<InfoDayOff> listItem = new ArrayList<InfoDayOff>();
		while (rs.next()) {
			listItem.add(new InfoDayOff(rs.getInt("HR_Employee_ID"), rs.getString("DayOffType"), rs.getTimestamp("StartTime")));
		}
		DB.close(rs, ps);
		rs = null;
		ps = null;
		return listItem;
	}
	
	class InfoDayOff {
		private int HR_Employee_ID;
		private String dayOff;
		private Timestamp dateOff;
		public InfoDayOff(int HR_Employee_ID, String dayOff, Timestamp dateOff) {
			this.HR_Employee_ID = HR_Employee_ID;
			this.dayOff = dayOff;
			this.dateOff = dateOff;
		}
		public int getHR_Employee_ID() {
			return HR_Employee_ID;
		}
		public void setHR_Employee_ID(int hR_Employee_ID) {
			HR_Employee_ID = hR_Employee_ID;
		}
		public String getDayOff() {
			return dayOff;
		}
		public void setDayOff(String dayOff) {
			this.dayOff = dayOff;
		}
		public Timestamp getDateOff() {
			return dateOff;
		}
		public void setDateOff(Timestamp dateOff) {
			this.dateOff = dateOff;
		}
		
		
	}

	

}
