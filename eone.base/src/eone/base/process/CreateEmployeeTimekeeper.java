
package eone.base.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MTimekeeper;
import org.compiere.model.MTimekeeperLine;
import org.compiere.model.X_HR_TimekeeperLine;
import org.compiere.util.DB;

public class CreateEmployeeTimekeeper extends SvrProcess {

	/**
	 * Quynhnv.x8: Tao danh sach nhan vien de cham cong
	 */

	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		MTimekeeper timekeeper = MTimekeeper.get(getCtx(), getRecord_ID());
		String sql = "Select HR_Employee_ID From HR_Employee Where AD_Department_ID = ? "+
				" And HR_Employee_ID not in (Select HR_Employee_ID From HR_TimekeeperLine Where HR_Timekeeper_ID = ?)";
		PreparedStatement ps = DB.prepareCall(sql);
		ResultSet rs = null;
		ps.setInt(1, timekeeper.getAD_Department_ID());
		ps.setInt(2, timekeeper.getHR_Timekeeper_ID());
		rs = ps.executeQuery();
		MTimekeeperLine line = null;
		while (rs.next()) {
			int HR_TimekeeperLine_ID = DB.getNextID(getAD_Client_ID(), X_HR_TimekeeperLine.Table_Name, get_TrxName());
			line = new MTimekeeperLine(getCtx(), 0, get_TrxName());
			line.setHR_Employee_ID(rs.getInt("HR_Employee_ID"));
			line.setHR_Timekeeper_ID(timekeeper.getHR_Timekeeper_ID());
			line.setC_Period_ID(timekeeper.getC_Period_ID());
			line.setHR_TimekeeperLine_ID(HR_TimekeeperLine_ID);
			if (!line.save())
				return "Create false";
			
		}
		
		
		return "Success";
	}

	

}
