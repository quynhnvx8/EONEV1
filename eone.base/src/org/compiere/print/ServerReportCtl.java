package org.compiere.print;

import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.PrintInfo;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import eone.base.process.ProcessInfo;



public class ServerReportCtl {

	/**
	 * Constants used to pass process parameters to Jasper Process
	 */
	public static final String PARAM_PRINTER_NAME = "PRINTER_NAME";
	public static final String PARAM_PRINT_FORMAT = "PRINT_FORMAT";
	public static final String PARAM_PRINT_INFO = "PRINT_INFO";
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (ServerReportCtl.class);
	
	
	public static boolean startDocumentPrint (int type, MPrintFormat customPrintFormat, int Record_ID, String printerName)
	{
		return startDocumentPrint(type, customPrintFormat, Record_ID, printerName, null);
	}
	
	
	public static boolean startDocumentPrint (int type, MPrintFormat customPrintFormat, int Record_ID, String printerName, ProcessInfo pi)
	{
		ReportEngine re = ReportEngine.get (Env.getCtx(), type, Record_ID, null);
		if (re == null)
		{
			CLogger log = CLogger.getCLogger(ServerReportCtl.class);
			log.warning("NoDocPrintFormat");
			return false;
		}
		if (customPrintFormat!=null) {
			// Use custom print format if available
			re.setPrintFormat(customPrintFormat);
		}
		
		if (re.getPrintFormat()!=null)
		{
			

			if (pi != null && pi.isBatch() && pi.isPrintPreview())
			{
				if ("HTML".equals(pi.getReportType())) 
				{
					pi.setExport(true);
					pi.setExportFileExtension("html");
					pi.setExportFile(re.getHTML());
				}
				else if ("XLSX".equals(pi.getReportType()))
				{
					pi.setExport(true);
					pi.setExportFileExtension("xlsx");
					pi.setExportFile(re.getXLSX());					
				}
				else
				{
					pi.setPDFReport(re.getPDF(null));
				}
			}
			else
			{
				createOutput(re, printerName);
			}
		
		}
		return true;
	}	//	StartDocumentPrint
	
	
	private static void createOutput(ReportEngine re, String printerName)
	{
		if (printerName!=null) {
			re.getPrintInfo().setPrinterName(printerName);
		}
		re.print();
	}
	
	
	
	static public boolean start (ProcessInfo pi)
	{
		return startStandardReport (pi);
	}	//	create

	
	static public boolean startStandardReport (ProcessInfo pi, boolean IsDirectPrint)
	{
		pi.setPrintPreview(!IsDirectPrint);
		return startStandardReport(pi);
	}
	
	
	static public boolean startStandardReport (ProcessInfo pi)
	{
		ReportEngine re = null;
		//
		// Create Report Engine by using attached MPrintFormat (if any)
		Object o = pi.getTransientObject();
		if (o == null)
			o = pi.getSerializableObject();
		if (o != null && o instanceof MPrintFormat) {
			Properties ctx = Env.getCtx();
			MPrintFormat format = (MPrintFormat)o;
			
			PrintInfo info = new PrintInfo(pi);
			re = new ReportEngine(ctx, format, info, pi.isSummary(), pi.getTransactionName(), null);
			if (pi.isPrintPreview() && pi.isBatch())
			{
				if ("HTML".equals(pi.getReportType())) 
				{
					pi.setExport(true);
					pi.setExportFileExtension("html");
					pi.setExportFile(re.getHTML());
				}
				else if ("XLSX".equals(pi.getReportType()))
				{
					pi.setExport(true);
					pi.setExportFileExtension("xlsx");
					pi.setExportFile(re.getXLSX());					
				}
				else
				{
					pi.setPDFReport(re.getPDF(null));
				}
			}
			else
			{
				createOutput(re, null);
			}
			return true;
		}
		//
		// Create Report Engine normally
		else {
			re = ReportEngine.get(Env.getCtx(), pi, null);
			if (re == null)
			{
				pi.setSummary("No ReportEngine");
				return false;
			}
		}
		
		if (pi.isPrintPreview() && pi.isBatch())
		{
			if ("HTML".equals(pi.getReportType())) 
			{
				pi.setExport(true);
				pi.setExportFileExtension("html");
				pi.setExportFile(re.getHTML());
			}
			else if ("XLSX".equals(pi.getReportType()))
			{
				pi.setExport(true);
				pi.setExportFileExtension("xlsx");
				pi.setExportFile(re.getXLSX());					
			}
			else
			{
				pi.setPDFReport(re.getPDF(null));
			}
		}
		else
		{
			createOutput(re, null);
		}
		return true;
	}	//	startStandardReport

	/**
	 *	Start Financial Report.
	 *  @param pi Process Info
	 *  @return true if OK
	 */
	static public boolean startFinReport (ProcessInfo pi)
	{
		@SuppressWarnings("unused")
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());

		//	Get PrintFormat
		MPrintFormat format = (MPrintFormat)pi.getTransientObject();
		if (format == null)
			format = (MPrintFormat)pi.getSerializableObject();
		if (format == null)
		{
			s_log.log(Level.SEVERE, "startFinReport - No PrintFormat");
			return false;
		}
		PrintInfo info = new PrintInfo(pi);

		ReportEngine re = new ReportEngine(Env.getCtx(), format, info, null);
		if (pi.isPrintPreview() && pi.isBatch())
		{
			if ("HTML".equals(pi.getReportType())) 
			{
				pi.setExport(true);
				pi.setExportFileExtension("html");
				pi.setExportFile(re.getHTML());
			}
			else if ("XLSX".equals(pi.getReportType()))
			{
				pi.setExport(true);
				pi.setExportFileExtension("xlsx");
				pi.setExportFile(re.getXLSX());					
			}
			else
			{
				pi.setPDFReport(re.getPDF(null));
			}
		}
		else
		{
			createOutput(re, null);
		}
		return true;
	}	//	startFinReport
	
	
}
