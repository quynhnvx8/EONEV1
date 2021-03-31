package org.compiere.apps;

import java.io.InvalidClassException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.db.CConnection;
import org.compiere.interfaces.Server;
import org.compiere.model.MClient;
import org.compiere.model.MConfigSignReport;
import org.compiere.model.MOrg;
import org.compiere.model.MProcess;
import org.compiere.model.MRule;
import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormatItem;
import org.compiere.print.PrintDataItem;
import org.compiere.print.ReportCtl;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.IProcessUI;
import org.compiere.util.Msg;
import org.compiere.util.ProcessUtil;
import org.compiere.util.Trx;

import eone.base.process.ClientProcess;
import eone.base.process.ProcessInfo;
import eone.base.process.ProcessInfoParameter;

public abstract class AbstractProcessCtl implements Runnable
{

	/**
	 * Quynhnv.x8. Mod 26/09/2020
	 * Sua lai, khong dung duoc theo idempiere
	 */
	public AbstractProcessCtl (IProcessUI aProcessUI, int WindowNo, ProcessInfo pi, Trx trx)
	{
		windowno = WindowNo;
		m_processUI = aProcessUI;
		m_pi = pi;
		m_trx = trx;	
	} 

	private int windowno;
	private IProcessUI m_processUI;
	private ProcessInfo m_pi;
	private Trx				m_trx;
	private boolean 		m_IsServerProcess = false;
	
	private static final CLogger	log	= CLogger.getCLogger (AbstractProcessCtl.class);
	
	public void start()
	{
		Thread thread = new Thread(this);
		if (m_pi != null)
			thread.setName(m_pi.getTitle()+"-"+m_pi.getAD_Session_ID());
		thread.start();
	}
	

	public void run ()
	{
		if (log.isLoggable(Level.FINE)) log.fine("AD_Session_ID=" + m_pi.getAD_Session_ID()
			+ ", Record_ID=" + m_pi.getRecord_ID());

		String 	ProcedureName = "";
		String  JasperReport = "";
		String TemplateApply = "";
		boolean IsReport = false;
		String sql = "SELECT p.Name, p.ProcedureName,p.ClassName, p.AD_Process_ID,"		
			+ " p.isReport, p.IsServerProcess, p.JasperReport, p.AD_Process_UU, p.TemplateApply "  
			+ " FROM AD_Process p"
			+ " WHERE p.IsActive='Y'"
			+ " AND p.AD_Process_ID=?";
		if (!Env.isBaseLanguage(Env.getCtx(), "AD_Process"))
			sql = "SELECT t.Name, p.ProcedureName,p.ClassName, p.AD_Process_ID,"	
				+ " p.isReport, p.IsServerProcess, p.JasperReport, p.AD_Process_UU, p.TemplateApply " 	
				+ "FROM AD_Process p"
				+ " INNER JOIN AD_Process_Trl t ON (p.AD_Process_ID=t.AD_Process_ID"
					+ " AND t.AD_Language='" + Env.getAD_Language(Env.getCtx()) + "') "
				+ " WHERE p.IsActive='Y'"
				+ " AND p.AD_Process_ID=?";
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, 
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, null);
			pstmt.setInt(1, m_pi.getAD_Process_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_pi.setTitle (rs.getString("Name"));
				//updateProgressWindowTitle(m_pi.getTitle());
				ProcedureName = rs.getString("ProcedureName");
				m_pi.setClassName (rs.getString("ClassName"));
				m_pi.setAD_Process_ID (rs.getInt("AD_Process_ID"));
				m_pi.setAD_Process_UU(rs.getString("AD_Process_UU"));
				IsReport = "Y".equalsIgnoreCase(rs.getString("isReport"));
				
				m_IsServerProcess = "Y".equals(rs.getString("IsServerProcess"));
				JasperReport = rs.getString("JasperReport");
				TemplateApply = rs.getString("TemplateApply");
			}
			else
				log.log(Level.SEVERE, "No AD_Session =" + m_pi.getAD_Session_ID());
		}
		catch (Throwable e)
		{
			m_pi.setSummary (Msg.getMsg(Env.getCtx(), "ProcessNoProcedure") + " " + e.getLocalizedMessage(), true);
			log.log(Level.SEVERE, "run", e);
			return;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		if (ProcedureName == null)
			ProcedureName = "";

		boolean isJasper = false;
		if (JasperReport != null && JasperReport.trim().length() > 0) {
			isJasper = true;
			if (ProcessUtil.JASPER_STARTER_CLASS.equals(m_pi.getClassName())) {
				m_pi.setClassName(null);
			}
		}
		
		/**********************************************************************
		 *	Start Optional Class
		 */
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		addProcessParametersNew(params, m_pi.getProcessPara());
		addProcessInfoParameters(params, m_pi.getParameter());
		if (!IsReport && m_pi.getClassName() != null)
		{
			if (isJasper)
			{
				m_pi.setReportingProcess(true);
			}
			
			//	Run Class
			if (!startProcess(params))
			{
				return;
			}

			if (!IsReport && ProcedureName.length() == 0)
			{
				return;
			}
			
		}
		
		loadInfoClient(params);
		
		params.put("TEMPAPPLY", TemplateApply);
		MConfigSignReport [] listSign = MConfigSignReport.getLines(Env.getCtx(), m_pi.getAD_Process_ID());
		MProcess process = MProcess.get(Env.getCtx(), m_pi.getAD_Process_ID());
		MPrintFormat  format = MPrintFormat.get(Env.getCtx(), process.getAD_PrintFormat_ID(), false);
		params.put("SIGNREPORT", listSign);
		//Uu tien chay Jasper, neu ko co jasper thi chay Procedure
		if (IsReport && isJasper)
		{
			params.put("ProcessInfo", m_pi);
			m_pi.setReportingProcess(true);
			m_pi.setClassName(ProcessUtil.JASPER_STARTER_CLASS);
			startProcess(params);			
			return;
		}
		
		if (IsReport && ProcedureName.length() > 0)
		{
			params.put("ProcedureName", ProcedureName);
			params.put("ReportName", m_pi.getTitle());
			params.put("WindowNo", windowno);
			params.put("MPrintFormat", format);
			getDataExcute(ProcedureName, format);
			//Luu y phai dat o day de lay duoc data
			params.put("ProcessInfo", m_pi);
			m_pi.setReportingProcess(true);
			boolean ok = ReportCtl.start(m_processUI, windowno, m_pi, params);
			m_pi.setSummary(Msg.getCleanMsg(Env.getCtx(), "Report"), !ok);
		}			
			
	}   //  run
	
	private void getDataExcute(String sql, MPrintFormat  format)
	{
        String sqlProc = Env.parseContext(Env.getCtx(), windowno, sql, false);
        PreparedStatement ps = DB.prepareCall(sqlProc);
        ArrayList<PrintDataItem> arr = null;
        ArrayList<ArrayList<PrintDataItem>> arrs = new ArrayList<ArrayList<PrintDataItem>>();
        int rowCount = 0;
		ResultSet rs = null;
		MPrintFormatItem [] items = format.getItemContent();
		
		//Gia tri tinh toan cong thuc bao cao
		Map<String, String> formula = format.getFormula();
		Map<String, Object> data = new HashMap<String, Object>();
    	Map<String, Object> valueOld = new HashMap<String, Object>();
    	Serializable balance = Env.ZERO;
    	//End
    	
		ArrayList<Object> arrWidth = new ArrayList<Object>();
		
		int maxRow = 0;
		
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				Serializable element = null;
				arr = new ArrayList<PrintDataItem>();
				for(int i = 0; i < items.length; i++) {
					MPrintFormatItem item = items[i];
					if (item.isMapColumnSelectSQL() && item.isPrinted()){
						element = (Serializable) rs.getObject(item.getName());
						
						//Nhung truong du lieu kieu so se add vao data phuc vu cho tinh toan cong thuc (neu co)
						if (item.isNumber()) {
		        			data.put(item.getColumnName(), element);
		        		}
						
						//Tinh toan doi voi cot dat cong thuc de day vao List data truoc khi view bao cao.
						if (item.isAccumulateCal() && item.getFormulaSetup() != "") {
		        			balance = getBalance(data, formula, item.getColumnName(), valueOld); 
		        			valueOld.put(item.getColumnName(), balance);
		        			if (new BigDecimal(balance.toString()).compareTo(Env.ZERO) < 0)
		        				balance = 0;
		        			element = balance;        			       			        			        				
		        		}
						
						//Add vao data
						arr.add(addNewItem(item, element));
						//Add do rong cua cac cot
						if (rowCount == 0)
							arrWidth.add((float)items[i].getMaxWidth());
					}
					if (item.getNumLines() > maxRow) {
						maxRow = item.getNumLines();
					}
				}
				arrs.add(arr);
				rowCount++;
			} 
		}
		catch (SQLException e)
		{
			log.saveError("Error", e);
		}
		finally
		{
			DB.close(rs, ps);
			rs = null; ps = null;
		}
		
		//Truong hop query khong co du lieu tra ve
		if (arrs.size() == 0) {
			Serializable element = null;
			arr = new ArrayList<PrintDataItem>();
			for(int i = 0; i < items.length; i++) {
				MPrintFormatItem item = items[i];
				if (item.isMapColumnSelectSQL() && item.isPrinted()) {
					arr.add(addNewItem(item, element));
					//Add do rong cua cac cot
					arrWidth.add((float)items[i].getMaxWidth());
						
				}
				if (item.getNumLines() > maxRow) {
					maxRow = item.getNumLines();
				}
			}
			arrs.add(arr);
		}//End khogn co du lieu tra ve
		
		float [] retValue = new float [arrWidth.size()];
		for(int i = 0; i < arrWidth.size(); i++) {
			retValue[i] = (float)arrWidth.get(i);
		}
		
		m_pi.setWidthTable(retValue);
		m_pi.setDataQuery(arrs);
		m_pi.setRowCountQuery(rowCount);
		m_pi.setColumnCountQuery(arrWidth.size());
		m_pi.setMaxRow(maxRow);
	}
	
	private PrintDataItem addNewItem(MPrintFormatItem item, Serializable element) {
		return new PrintDataItem(
				item.getName(), 			//Ten cot
				element, 					//Gia tri cua cot
				item.getAD_Reference_ID(), 	//kieu hien thi
				item.getFormatPattern(),	//Dinh dang hien thi
				item.getZoomLogic(), 		//zoom den ban ghi goc
				item.getAlignment(), 		//Can trai, phai, giua
				item.getFormulaSetup(),		//thiet lap cong thuc tinh so du
				item.isGroupBy(), 			//Co phan nhom hay khong	
				item.isSummarized(), 		//Co cong tong bao cao hay khong
				item.isMinCalc(), 			//Tinh gia tri nho nhat
				item.isMaxCalc(), 			//Tinh gia tri lon nhat
				item.isAveraged(),			//Tinh gia tri trung binh
				item.isCounted(), 			//Dem so luong ban ghi cua bao cao
				item.isCountedGroup(),		//Dem so luong ban ghi cua nhom
				item.isBalanceFinal(),		//So du cuoi ky
				item.getTableName(),		//Ten bang can de zoom theo dieu kien ZoomLogic	
				item.getRotationText()		//Huong chu tren header cua bao cao
				);
	}
	
	private Serializable getBalance(Map<String, Object> data, Map<String, String> formula, String columnName, Map<String, Object> valueOlds) {
    	String formulaStr = formula.get(columnName);
    	for(Map.Entry<String, Object> entry : data.entrySet()) {
    		String key = entry.getKey();
    		Object value = entry.getValue();
    		if (value == null) {
    			value = 0;
    		}
    		if(formulaStr.contains(key)) {
    			formulaStr = formulaStr.replaceAll(key, ""+ value);
    		}
    	}
    	Object valueOld = valueOlds.get(columnName);
    	if (valueOld == null) {
    		valueOld = Env.ZERO;
    	}
    	Serializable valueNew = Env.getValueByFormula(formulaStr);
    	
    	BigDecimal total = new BigDecimal(valueOld.toString()).add(new BigDecimal(valueNew.toString()));
    	
    	return (Serializable) (total);
    }
	
	
	
	protected int getWindowNo() 
	{
		return windowno;
	}
	
	protected ProcessInfo getProcessInfo()
	{
		return m_pi;
	}
	
	protected IProcessUI getProcessMonitor()
	{
		return m_processUI;
	}
	
	protected IProcessUI getParent()
	{
		return getProcessMonitor();
	}
	
	protected boolean isServerProcess()
	{
		return m_IsServerProcess;
	}
	
	
	private boolean startProcess (HashMap<String, Object> params)
	{
		if (log.isLoggable(Level.FINE)) log.fine(m_pi.toString());
		boolean started = false;
		
		boolean clientOnly = false;
		if (! m_pi.getClassName().toLowerCase().startsWith(MRule.SCRIPT_PREFIX)) {
			try {
				Class<?> processClass = Class.forName(m_pi.getClassName());
				if (ClientProcess.class.isAssignableFrom(processClass))
					clientOnly = true;
			} catch (Exception e) {}
		}
		
		if (m_IsServerProcess && !clientOnly)
		{
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	
					//	See ServerBean
					m_pi = server.process (Env.getRemoteCallCtx(Env.getCtx()), m_pi);
					if (log.isLoggable(Level.FINEST)) log.finest("server => " + m_pi);
					started = true;		
				}
			}
			catch (UndeclaredThrowableException ex)
			{
				Throwable cause = ex.getCause();
				if (cause != null)
				{
					if (cause instanceof InvalidClassException)
						log.log(Level.SEVERE, "Version Server <> Client: " 
							+  cause.toString() + " - " + m_pi, ex);
					else
						log.log(Level.SEVERE, "AppsServer error(1b): " 
							+ cause.toString() + " - " + m_pi, ex);
				}
				else
					log.log(Level.SEVERE, " AppsServer error(1) - " 
						+ m_pi, ex);
				started = false;
			}
			catch (Exception ex)
			{
				Throwable cause = ex.getCause();
				if (cause == null)
					cause = ex;
				log.log(Level.SEVERE, "AppsServer error - " + m_pi, cause);
				started = false;
			}
		}
		
		if (!started && (!m_IsServerProcess || clientOnly ))
		{
			if (m_pi.getClassName().toLowerCase().startsWith(MRule.SCRIPT_PREFIX)) {
				m_pi.setProcessUI(m_processUI);
				return ProcessUtil.startScriptProcess(Env.getCtx(), m_pi, m_trx);
			} else {
				return ProcessUtil.startJavaProcess(Env.getCtx(), m_pi, m_trx, true, m_processUI, params);
			}
		}
		return !m_pi.isError();
	}   //  startProcess
	
	
	private static void addProcessParametersNew(Map<String, Object> params, ProcessInfoParameter[] para)
    {
        
        if (para != null) {
        	for (int i = 0; i < para.length; i ++) {
        		String name = para[i].getParameterName();
                String pStr = para[i].getP_String();
                String pStrTo = para[i].getP_String_To();
                BigDecimal pNum = para[i].getP_Number();
                BigDecimal pNumTo = para[i].getP_Number_To();

                Timestamp pDate = para[i].getP_Date();
                Timestamp pDateTo = para[i].getP_Date_To();
                if (pStr != null) {
                    if (pStrTo!=null) {
                        params.put( name+"1", pStr);
                        params.put( name+"2", pStrTo);
                    } else {
                        params.put( name, pStr);
                    }
                } else if (pDate != null) {
                    if (pDateTo!=null) {
                        params.put( name+"1", pDate);
                        params.put( name+"2", pDateTo);
                    } else {
                        params.put( name, pDate);
                    }
                } else if (pNum != null) {
                	if (name.endsWith("_ID")) {
                		if (pNumTo!=null) {
	                        params.put( name+"1", pNum.intValue());
	                        params.put( name+"2", pNumTo.intValue());
	                    } else {
	                        params.put( name, pNum.intValue());
	                    }
                	} else {
	                    if (pNumTo!=null) {
	                        params.put( name+"1", pNum);
	                        params.put( name+"2", pNumTo);
	                    } else {
	                        params.put( name, pNum);
	                    }
                	}
                }
                //
                // Add parameter info - teo_sarca FR [ 2581145 ]
                String info = para[i].getInfo();
                String infoTo = para[i].getInfo_To();
                if (infoTo != null) {
                	params.put(name+"_View1", (info != null ? info : ""));
            		params.put(name+"_View2", (infoTo != null ? infoTo : ""));
                } else {
                	params.put(name+"_View", (info != null ? info : ""));
                }
        		
        	}
        }
    }

    private void addProcessInfoParameters(Map<String, Object> params, ProcessInfoParameter[] para)
    {
    	if (para != null) {
			for (int i = 0; i < para.length; i++) {
				if (para[i].getParameter_To() == null) {
					if (para[i].getParameterName().endsWith("_ID") && para[i].getParameter() instanceof BigDecimal) {
						params.put(para[i].getParameterName(), ((BigDecimal)para[i].getParameter()).intValue());
					} else {
						params.put(para[i].getParameterName(), para[i].getParameter());
					}
				} else {
					// range - from
					if (para[i].getParameterName().endsWith("_ID") && para[i].getParameter() != null && para[i].getParameter() instanceof BigDecimal) {
		                params.put( para[i].getParameterName()+"1", ((BigDecimal)para[i].getParameter()).intValue());
					} else {
		                params.put( para[i].getParameterName()+"1", para[i].getParameter());
					}
					// range - to
					if (para[i].getParameterName().endsWith("_ID") && para[i].getParameter_To() instanceof BigDecimal) {
		                params.put( para[i].getParameterName()+"2", ((BigDecimal)para[i].getParameter_To()).intValue());
					} else {
		                params.put( para[i].getParameterName()+"2", para[i].getParameter_To());
					}
				}
			}
    	}
	}
    
    private void loadInfoClient(HashMap<String, Object> params) {
    	params.put("AD_CLIENT_ID", Integer.valueOf( Env.getAD_Client_ID(Env.getCtx())));
    	params.put("AD_ROLE_ID", Integer.valueOf( Env.getAD_Role_ID(Env.getCtx())));
    	params.put("AD_USER_ID", Integer.valueOf( Env.getAD_User_ID(Env.getCtx())));

    	params.put("AD_CLIENT_NAME", Env.getContext(Env.getCtx(), "#AD_Client_Name").toUpperCase());
    	params.put("AD_ROLE_NAME", Env.getContext(Env.getCtx(), "#AD_Role_Name").toUpperCase());
    	params.put("AD_USER_NAME", Env.getContext(Env.getCtx(), "#AD_User_Name"));
    	params.put("AD_ORG_NAME", Env.getContext(Env.getCtx(), "#AD_Org_Name").toUpperCase());
    	params.put("C_ELEMENT_ID", Integer.valueOf(Env.getContext(Env.getCtx(), "#C_Element_ID")));
    	params.put("C_CURRENCY_ID", Integer.valueOf(Env.getContext(Env.getCtx(), "#C_CurrencyDefault_ID")));
    	
    	MClient client = MClient.get(Env.getCtx());
    	MOrg org = MOrg.get(Env.getCtx(), Env.getContextAsInt(Env.getCtx(), "#AD_Org_ID"));
    	String address = "";
    	if (client.isGroup() && org.getAddress() != null && !org.getAddress().isEmpty()) {
    		address =  org.getAddress();
    		params.put("COMPANY_NAME", Env.getContext(Env.getCtx(), "#AD_Org_Name").toUpperCase());        	
    	} else {
    		address = client.getAddress();
    		params.put("COMPANY_NAME", Env.getContext(Env.getCtx(), "#AD_Client_Name").toUpperCase());        	
    	}
    	params.put("ADDRESS", address);
    	params.put("AD_DEPARTMENT_ID", Env.getContextAsInt(Env.getCtx(), "#AD_Department_ID"));
    	params.put("AD_DEPARTMENT_NAME", Env.getContext(Env.getCtx(), "#AD_Department_Name"));
    }
}	//	ProcessCtl