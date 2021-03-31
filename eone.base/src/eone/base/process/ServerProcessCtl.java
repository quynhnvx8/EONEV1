package eone.base.process;

import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Trx;

public class ServerProcessCtl implements Runnable {
	
	/**	Static Logger	*/
	private static CLogger	log	= CLogger.getCLogger (ServerProcessCtl.class);
	
	/** Process Info */
	ProcessInfo m_pi;
	//private Trx				m_trx;
	//private boolean 		m_IsServerProcess = false;
	
	/**************************************************************************
	 *  Constructor
	 *  @param pi Process info
	 *  @param trx Transaction
	 */
	public ServerProcessCtl (ProcessInfo pi, Trx trx)
	{
		m_pi = pi;
		//m_trx = trx;	//	handled correctly
	}   //  ProcessCtl
	
	/**
	 *	Process Control
	 *  <code>
	 *	- Get Instance ID
	 *	- Get Parameters
	 *	- execute (lock - start process - unlock)
	 *  </code>
	 *  Creates a ProcessCtl instance, which calls
	 *  lockUI and unlockUI if parent is a ASyncProcess
	 *  <br>
	 *	Called from APanel.cmd_print, APanel.actionButton and
	 *  VPaySelect.cmd_generate
	 *
	 *  @param pi ProcessInfo process info
	 *  @param trx Transaction
	 *  @return worker started ProcessCtl instance or null for workflow
	 */
	public static ServerProcessCtl process (ProcessInfo pi, Trx trx)
	{
		if (log.isLoggable(Level.FINE)) log.fine("ServerProcess - " + pi);
		/*
		MPInstance instance = null; 
		if (pi.getAD_Session_ID() == "")
		{
			try 
			{ 
				instance = new MPInstance(Env.getCtx(), pi.getAD_Process_ID(), pi.getRecord_ID()); 
			} 
			catch (Exception e) 
			{ 
				pi.setSummary (e.getLocalizedMessage()); 
				pi.setError (true); 
				log.warning(pi.toString()); 
				return null; 
			} 
			catch (Error e) 
			{ 
				pi.setSummary (e.getLocalizedMessage()); 
				pi.setError (true); 
				log.warning(pi.toString()); 
				return null; 
			}
			if (!instance.save())
			{
				pi.setSummary (Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
				pi.setError (true);
				return null;
			}
			pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());
		}
		else
		{
			instance = new MPInstance(Env.getCtx(), pi.getAD_PInstance_ID(), null);
		}
		*/
		
		//	execute
		ServerProcessCtl worker = new ServerProcessCtl(pi, trx);
		worker.run();
		
		return worker;
	}	//	execute
	
	/**
	 * Run this process in a new thread
	 */
	public void start()
	{
		Thread thread = new Thread(this);
		// Set thread name
		if (m_pi != null)
			thread.setName(m_pi.getTitle()+"-"+m_pi.getAD_Session_ID());
		thread.start();
	}

	/**
	 *	Execute Process Instance and Lock UI.
	 *  Calls lockUI and unlockUI if parent is a ASyncProcess
	 *  <pre>
	 *		- Get Process Information
	 *      - Call Class
	 *		- Submit SQL Procedure
	 *		- Run SQL Procedure
	 *	</pre>
	 */
	public void run ()
	{
		/*
		if (log.isLoggable(Level.FINE)) log.fine("AD_PInstance_ID=" + m_pi.getAD_Session_ID()
			+ ", Record_ID=" + m_pi.getRecord_ID());

		//	Get Process Information: Name, Procedure Name, ClassName, IsReport, IsDirectPrint
		String 	ProcedureName = "";
		String  JasperReport = "";
		int     AD_ReportView_ID = 0;
		boolean IsReport = false;
		
		//
		String sql = "SELECT p.Name, p.ProcedureName,p.ClassName, p.AD_Process_ID,"		//	1..4
			+ " p.isReport,p.IsDirectPrint,p.AD_ReportView_ID, 0 as Work,"		//	5..8 
			+ " 0 as statistic,"	//	9
			+ " p.IsServerProcess, p.JasperReport, p.AD_Process_UU " 	//	10..12
			+ "FROM AD_Process p"
			+ " INNER JOIN AD_PInstance i ON (p.AD_Process_ID=i.AD_Process_ID) "
			+ "WHERE p.IsActive='Y'"
			+ " AND i.AD_PInstance_ID=?";
		if (!Env.isBaseLanguage(Env.getCtx(), "AD_Process"))
			sql = "SELECT t.Name, p.ProcedureName,p.ClassName, p.AD_Process_ID,"		//	1..4
				+ " p.isReport, p.IsDirectPrint,p.AD_ReportView_ID, 0 as Work,"	//	5..8
				+ " 0 as statistic,"	//	9
				+ " p.IsServerProcess, p.JasperReport, p.AD_Process_UU " 	//	10..12
				+ "FROM AD_Process p"
				+ " INNER JOIN AD_PInstance i ON (p.AD_Process_ID=i.AD_Process_ID) "
				+ " INNER JOIN AD_Process_Trl t ON (p.AD_Process_ID=t.AD_Process_ID"
					+ " AND t.AD_Language='" + Env.getAD_Language(Env.getCtx()) + "') "
				+ "WHERE p.IsActive='Y'"
				+ " AND i.AD_PInstance_ID=?";
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, 
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, null);
			pstmt.setInt(1, m_pi.getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_pi.setTitle (rs.getString(1));
				ProcedureName = rs.getString(2);
				m_pi.setClassName (rs.getString(3));
				m_pi.setAD_Process_ID (rs.getInt(4));
				m_pi.setAD_Process_UU (rs.getString(12));
				//	Report
				if ("Y".equals(rs.getString(5)))
				{
					IsReport = true;
				}
				AD_ReportView_ID = rs.getInt(7);
				//
				int estimate = rs.getInt(9);
				if (estimate != 0)
				{
					m_pi.setEstSeconds (estimate + 1);     //  admin overhead
				}
				m_IsServerProcess = "Y".equals(rs.getString(10));
				JasperReport = rs.getString(11);
			}
			else
				log.log(Level.SEVERE, "No AD_PInstance_ID=" + m_pi.getAD_PInstance_ID());
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

		//  No PL/SQL Procedure
		if (ProcedureName == null)
			ProcedureName = "";

		
		// Clear Jasper Report class if default - to be executed later
		boolean isJasper = false;
		if (JasperReport != null && JasperReport.trim().length() > 0) {
			isJasper = true;
			if (ProcessUtil.JASPER_STARTER_CLASS.equals(m_pi.getClassName())) {
				m_pi.setClassName(null);
			}
		}
		
		
		if (m_pi.getClassName() != null)
		{
			if (isJasper)
			{
				m_pi.setReportingProcess(true);
			}
			
			//	Run Class
			if (!startProcess())
			{
				return;
			}

			//  No Optional SQL procedure ... done
			if (!IsReport && ProcedureName.length() == 0)
			{
				return;
			}
			//  No Optional Report ... done
			if (IsReport && AD_ReportView_ID == 0 && ! isJasper)
			{
				return;
			}
		}

		
		//	Optional Pre-Report Process
		if (IsReport && ProcedureName.length() > 0)
		{
			m_pi.setReportingProcess(true);
			if (!startDBProcess(ProcedureName))
			{
				return;
			}
		}	//	Pre-Report

		if (isJasper)
		{
			m_pi.setReportingProcess(true);
			m_pi.setClassName(ProcessUtil.JASPER_STARTER_CLASS);
			startProcess();
			return;
		}
		
		if (IsReport)
		{
			m_pi.setReportingProcess(true);
			//	Start Report	-----------------------------------------------
			boolean ok = ServerReportCtl.start(m_pi);
			String summ = Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Report"));
			m_pi.setSummary(summ, !ok);
		}
		
		else
		{
			if (!startDBProcess (ProcedureName))
			{
				return;
			}
			//	Success - getResult
			ProcessInfoUtil.setSummaryFromDB(m_pi);
		}			//	*** Process submission ***
	*/
	}   //  run
	
	
	
	
}
