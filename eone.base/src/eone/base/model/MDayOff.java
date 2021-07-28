package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MDayOff extends X_HR_DayOff implements DocAction
{
	/**
	 * Khi ngay nghi duoc Approved thi day cac ngay nghi vao bang cham cong.
	 * Tinh toan xac dinh cau hinh ngay nghi le va ngay lam viec trong nam de tinh cho dung
	 * thong qua MWorkDay va MHoliday
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static MDayOff get (Properties ctx, int HR_DayOff_ID, String trxName)
	{
		return (MDayOff)MTable.get(ctx, Table_Name).getPO(HR_DayOff_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MDayOff (Properties ctx, int HR_DayOff_ID, String trxName)
	{
		super (ctx, HR_DayOff_ID,trxName);
		
	}	

	
	public MDayOff (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	
	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("StartTime") || is_ValueChanged("EndTime")) {
			if(getStartTime().compareTo(getEndTime()) > 0) {
				log.saveError("Error", "StartTime must be less than EndTime");
				return false;
			}
		}
		
		String sql = "Select count(1) from HR_DayOff Where HR_Employee_ID = ? "+
				" And (? between StartTime And EndTime Or ?  between StartTime And EndTime) And AD_Client_ID = ? And HR_DayOff_ID != ?";
		Object [] params = {getHR_Employee_ID(), getStartTime(), getEndTime(), getAD_Client_ID(), getHR_DayOff_ID()};
		int no = DB.getSQLValue(get_TrxName(), sql, params);
		if (no > 0) {
			log.saveError("Error", "StartTime or EndTime exists record other!");
			return false;
		}
		return true;
	}	//	beforeSave
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(!success)
		{
			return success;
		}
		
		
		
		return true;
	}	//	afterSave
	
	
	protected boolean beforeDelete()
	{
		
		
		return true;
	}       //      beforeDelete
	
	
	
	protected String		m_processMsg = null;

	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}

	@Override
	public String completeIt() {
		m_processMsg = checkApprovedCanceled();
		
		if(m_processMsg != null) {
			return DocAction.STATUS_Drafted;
		}
		
		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		
		m_processMsg = checkApprovedCanceled();
		
		if(m_processMsg != null) {
			return false;
		}
		
		if (log.isLoggable(Level.INFO)) log.info(toString());
		return true;
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}


	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}
	
	@Override
	public String getSummary() {
		
		return "";
	}

	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		StringBuilder sql = new StringBuilder("UPDATE HR_DayOffLine SET Processed='")
			.append((processed ? "Y" : "N"))
			.append("' WHERE HR_DayOff_ID=").append(getHR_DayOff_ID());
		int noLine = DB.executeUpdate(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine(processed + " - Lines=" + noLine);
	}

	private String checkApprovedCanceled() {
		m_processMsg = "OK";
		MPeriod periodStart = MPeriod.findByCalendar(getCtx(), getStartTime(), get_TrxName());
		MTimekeeperLine lineCheck = MTimekeeperLine.getItem(getCtx(), getHR_Employee_ID(), periodStart.getC_Period_ID(), get_TrxName());
		if (lineCheck == null)
			return null;
		if(lineCheck.isApproved(getStartTime(), getHR_Employee_ID())) {
			return "StartDate in period has Approved";			
		}
		MPeriod periodSEnd = null;
		if (TimeUtil.getMonthSel(getStartTime()) != TimeUtil.getMonthSel(getEndTime())) {
			periodSEnd = MPeriod.findByCalendar(getCtx(), getEndTime(), get_TrxName());
			lineCheck = MTimekeeperLine.getItem(getCtx(), getHR_Employee_ID(), periodSEnd.getC_Period_ID(), get_TrxName());
			if(lineCheck.isApproved(getStartTime(), getHR_Employee_ID())) {
				return "EndDate in period has Approved";
			}
		}
		return null;
	}

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
