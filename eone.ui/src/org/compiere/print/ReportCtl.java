package org.compiere.print;

import java.util.HashMap;

import org.adempiere.base.Service;
import org.compiere.util.Env;
import org.compiere.util.IProcessUI;

import eone.base.process.ProcessInfo;


public class ReportCtl
{
	
	public static final String PARAM_PRINTER_NAME = ServerReportCtl.PARAM_PRINTER_NAME;
	
	public static final String PARAM_PRINT_FORMAT = ServerReportCtl.PARAM_PRINT_FORMAT;
	
	public static final String PARAM_PRINT_INFO = ServerReportCtl.PARAM_PRINT_INFO;

	
	private ReportCtl()
	{
	}	//	ReportCtrl


	//private volatile static ProcessInfo m_pi;

	
	static public boolean start (ProcessInfo pi, HashMap<String, Object> params)
	{
		return start(null, -1, pi, params);
	}

	static public boolean start (IProcessUI parent, int WindowNo, ProcessInfo pi, HashMap<String, Object> params)
	{
		pi.setPrintPreview(true);
		return startStandardReport (pi, WindowNo, params);
	}	//	create

	
	static public boolean startStandardReport (ProcessInfo pi, int WindowNo, HashMap<String, Object> params)
	{
		ReportEngine re = null;
		
		re = ReportEngine.get(Env.getCtx(), pi, params);
		if (re == null)
		{
			pi.setSummary("No ReportEngine");
			return false;
		}

		if (pi.getReportType() != null) {
			re.setReportType(pi.getReportType());
		}
		re.setLanguageID(pi.getLanguageID());
		re.setWindowNo(WindowNo);
		createOutput(re, pi.isPrintPreview(), null);
		return true;
	}	//	startStandardReport

	
	private static void createOutput(ReportEngine re, boolean printPreview, String printerName)
	{
		//if (m_pi != null)
		//	m_pi.setRowCount(re.getPrintData().getRowCount());
		if (printPreview)
			preview(re);
		else {
			if (printerName!=null) {
				re.getPrintInfo().setPrinterName(printerName);
			}
			re.print();
		}
	}

	
	public static void preview(ReportEngine re)
	{
		ReportViewerProvider viewer = Service.locator().locate(ReportViewerProvider.class).getService();
		viewer.openViewer(re);
	}

}	
