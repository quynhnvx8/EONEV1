package org.compiere.print;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.pdf.WritePDF;
import org.apache.ecs.XhtmlDocument;
import org.apache.ecs.xhtml.a;
import org.apache.ecs.xhtml.script;
import org.apache.ecs.xhtml.table;
import org.apache.ecs.xhtml.tbody;
import org.apache.ecs.xhtml.td;
import org.apache.ecs.xhtml.th;
import org.apache.ecs.xhtml.tr;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Util;

import eone.base.impexp.PrintDataXLSXExporter;
import eone.base.model.I_AD_PrintFormat;
import eone.base.model.MProcess;
import eone.base.model.PrintInfo;
import eone.base.process.ProcessInfo;

public class ReportEngine implements PrintServiceAttributeListener
{
	
	public ReportEngine (Properties ctx, MPrintFormat pf, PrintInfo info, HashMap<String, Object> params)
	{
		this(ctx, pf, info, null, params);
	}	//	ReportEngine
	

	public ReportEngine (Properties ctx, MPrintFormat pf, PrintInfo info, boolean isSummary, HashMap<String, Object> params)
	{
		this(ctx, pf, info, isSummary, null, params);
	}	//	ReportEngine
	

	public ReportEngine (Properties ctx, MPrintFormat pf, PrintInfo info, String trxName, HashMap<String, Object> params){
		this(ctx, pf, info, false, trxName, params);
	}
	

	public ReportEngine (Properties ctx, MPrintFormat pf, PrintInfo info, boolean isSummary, String trxName, HashMap<String, Object> m_params)
	{
		m_summary = isSummary;
		if (pf == null)
			throw new IllegalArgumentException("ReportEngine - no PrintFormat");
		if (log.isLoggable(Level.INFO)) log.info(pf + " -- " + m_params);
		m_ctx = ctx;
		//
		m_printFormat = pf;
		m_info = info;
		//m_trxName = trxName;
		setParams(m_params);
		
	}	//	ReportEngine


	/**	Static Logger	*/
	private static CLogger	log	= CLogger.getCLogger (ReportEngine.class);

	/**	Context					*/
	private Properties		m_ctx;

	/**	Print Format			*/
	public MPrintFormat	m_printFormat;
	/** Print Info				*/
	private PrintInfo		m_info;
	/**	Query					*/
	//private MQuery			m_query;
	/**	Query Data				*/
	//private PrintData		m_printData;
	/** Layout					*/
	//private LayoutEngine 	m_layout = null;
	/**	Printer					*/
	private String			m_printerName = Ini.getProperty(Ini.P_PRINTER);	
	/** Transaction Name 		*/
	//private String 			m_trxName = null;
	/** Where filter */
	private String 			m_whereExtended = null;
	/** Window */
	private int m_windowNo = 0;
	
	private int m_language_id = 0;
	
	private boolean m_summary = false;
	
	private HashMap<String, Object> m_params;
	
	public HashMap<String, Object> getParams() {
		return m_params;
	}

	private Map<CSSInfo, List<ColumnInfo>> mapCssInfo = new HashMap<CSSInfo, List<ColumnInfo>>();
	
	private List<IReportEngineEventListener> eventListeners = new ArrayList<IReportEngineEventListener>();

	public void addEventListener(IReportEngineEventListener listener)
	{
		eventListeners.add(listener);
	}
	
	public boolean removeEventListener(IReportEngineEventListener listener)
	{
		return eventListeners.remove(listener);
	}
	
	/**
	 * 	Set PrintFormat.
	 *  If Layout was created, re-create layout
	 * 	@param pf print format
	 */
	public void setPrintFormat (MPrintFormat pf)
	{
		m_printFormat = pf;
		pf.reloadItems();
		/*
		if (m_layout != null)
		{
			setPrintData();
			m_layout.setPrintFormat(pf, false);
			m_layout.setPrintData(m_printData, m_params, true);	//	format changes data
		}
		*/
		IReportEngineEventListener[] listeners = eventListeners.toArray(new IReportEngineEventListener[0]);
		for(IReportEngineEventListener listener : listeners)
		{
			listener.onPrintFormatChanged(new ReportEngineEvent(this));
		}
	}	//	setPrintFormat
	
	
	public void setParams (HashMap<String, Object> params)
	{
		this.m_params = params;
		//setPrintData();
		/*
		if (m_layout != null)
			m_layout.setPrintData(m_printData, m_params, true);
		*/
		IReportEngineEventListener[] listeners = eventListeners.toArray(new IReportEngineEventListener[0]);
		for(IReportEngineEventListener listener : listeners)
		{
			listener.onQueryChanged(new ReportEngineEvent(this));
		}
	}	//	setQuery

	/*
	private void setPrintData()
	{
		DataEngine de = new DataEngine(m_printFormat.getLanguage(),m_trxName);
		setPrintData(de.getPrintData (m_ctx, m_printFormat, m_summary, m_params));
	}	//	setPrintData
	*/

	

	/**
	 * 	Get PrintFormat (Report) Name
	 * 	@return name
	 */
	public String getName()
	{
		return m_printFormat.get_Translation("Name");
	}	//	getName

	/**
	 * 	Get PrintFormat
	 * 	@return print format
	 */
	public MPrintFormat getPrintFormat()
	{
		return m_printFormat;
	}	//	getPrintFormat

	/**
	 * 	Get Print Info
	 *	@return info
	 */
	public PrintInfo getPrintInfo()
	{
		return m_info;
	}	//	getPrintInfo
	
	/**
	 * 	Get PrintLayout (Report) Context
	 * 	@return context
	 */
	public Properties getCtx()
	{
		return m_ctx;
	}	//	getCtx
	
	/**
	 * 	Get Row Count
	 * 	@return row count
	 */
	/*
	public int getRowCount()
	{
		return m_printData.getRowCount();
	}	//	getRowCount
	*/
	/**
	 * 	Get Column Count
	 * 	@return column count
	 */
	public int getColumnCount()
	{
		//if (m_layout != null)
		//	return m_layout.getColumnCount();
		return 0;
	}	//	getColumnCount

	
	/**************************************************************************
	 * 	Print Report
	 */
	public void print ()
	{
		/*
		if (log.isLoggable(Level.INFO)) log.info(m_info.toString());
		if (m_layout == null)
			layout();
		
		//	Paper Attributes: 	media-printable-area, orientation-requested, media
		PrintRequestAttributeSet prats = m_layout.getPaper().getPrintRequestAttributeSet();
		//	add:				copies, job-name, priority
		if (m_info.isDocumentCopy() || m_info.getCopies() < 1)
			prats.add (new Copies(1));
		else
			prats.add (new Copies(m_info.getCopies()));
		Locale locale = Language.getLoginLanguage().getLocale();
		prats.add(new JobName(m_printFormat.getName(), locale));
		prats.add(PrintUtil.getJobPriority(m_layout.getNumberOfPages(), m_info.getCopies(), true));

		try
		{
			//	PrinterJob
			PrinterJob job = getPrinterJob(m_info.getPrinterName());
		//	job.getPrintService().addPrintServiceAttributeListener(this);
			job.setPageable(m_layout.getPageable(false));	//	no copy
		//	Dialog
			try
			{
				if (m_info.isWithDialog() && !job.printDialog(prats))
					return;
			}
			catch (Exception e)
			{
				log.log(Level.WARNING, "Operating System Print Issue, check & try again", e);
				return;
			}

		//	submit
			boolean printCopy = m_info.isDocumentCopy() && m_info.getCopies() > 1;
			ArchiveEngine.get().archive(m_layout, m_info);
			PrintUtil.print(job, prats, false, printCopy);

			//	Document: Print Copies
			if (printCopy)
			{
				if (log.isLoggable(Level.INFO)) log.info("Copy " + (m_info.getCopies()-1));
				prats.add(new Copies(m_info.getCopies()-1));
				job = getPrinterJob(m_info.getPrinterName());
			//	job.getPrintService().addPrintServiceAttributeListener(this);
				job.setPageable (m_layout.getPageable(true));		//	Copy
				PrintUtil.print(job, prats, false, false);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		*/
	}	//	print

	/**
	 * 	Print Service Attribute Listener.
	 * 	@param psae event
	 */
	public void attributeUpdate(PrintServiceAttributeEvent psae)
	{
		
		if (log.isLoggable(Level.FINE)) log.fine("attributeUpdate - " + psae);
	//	PrintUtil.dump (psae.getAttributes());
	}	//	attributeUpdate


	/**
	 * 	Get PrinterJob based on PrinterName
	 * 	@param printerName optional Printer Name
	 * 	@return PrinterJob
	 */
	/*
	private PrinterJob getPrinterJob (String printerName)
	{
		if (printerName != null && printerName.length() > 0)
			return PrintUtil.getPrinterJob(printerName);
		return PrintUtil.getPrinterJob(m_printerName);
	}	//	getPrinterJob
	*/

	/**
	 * 	Show Dialog and Set Paper
	 *  Optionally re-calculate layout
	 */
	public void pageSetupDialog ()
	{
		/*
		if (m_layout == null)
			layout();
		m_layout.pageSetupDialog(getPrinterJob(m_printerName));
		
		IReportEngineEventListener[] listeners = eventListeners.toArray(new IReportEngineEventListener[0]);
		for(IReportEngineEventListener listener : listeners)
		{
			listener.onPageSetupChanged(new ReportEngineEvent(this));
		}
		*/
	}	//	pageSetupDialog

	/**
	 * 	Set Printer (name)
	 * 	@param printerName valid printer name
	 */
	public void setPrinterName(String printerName)
	{
		if (printerName == null)
			m_printerName = Ini.getProperty(Ini.P_PRINTER);
		else
			m_printerName = printerName;
	}	//	setPrinterName


	public String getPrinterName()
	{
		return m_printerName;
	}	//	getPrinterName


	public boolean createHTML (File file, boolean onlyTable)
	{
		return createHTML(file, onlyTable, null);
	}
	

	public boolean createHTML (File file, boolean onlyTable, IHTMLExtension extension)
	{
		try
		{
			Writer fw = new OutputStreamWriter(new FileOutputStream(file, false), Ini.getCharset()); // teo_sarca: save using adempiere charset [ 1658127 ]
			return createHTML (new BufferedWriter(fw), onlyTable, extension);
		}
		catch (FileNotFoundException fnfe)
		{
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(f)", e);
			throw new AdempiereException(e);
		}
		return false;
	}	//	createHTML


	public boolean createHTML (Writer writer, boolean onlyTable)
	{
		return createHTML(writer, onlyTable, null);
	}

	public boolean createHTML (Writer writer, boolean onlyTable, IHTMLExtension extension){
		return createHTML (writer, onlyTable, extension, false);
	}
	

	public boolean createHTML (Writer writer, boolean onlyTable, IHTMLExtension extension, boolean isExport)
	{
		try
		{
			String cssPrefix = extension != null ? extension.getClassPrefix() : null;
			if (cssPrefix != null && cssPrefix.trim().length() == 0)
				cssPrefix = null;
			
			
			table table = new table();
			if (cssPrefix != null)
				table.setClass(cssPrefix + "-table");
			//
			//
			table.setNeedClosingTag(false);
			PrintWriter w = new PrintWriter(writer);
			XhtmlDocument doc = null;
					
			if (onlyTable)
				table.output(w);
			else
			{
				doc = new XhtmlDocument();
				doc.getHtml().setNeedClosingTag(false);
				doc.getBody().setNeedClosingTag(false);
				doc.appendHead("<meta charset=\"UTF-8\" />");
				
				if (extension != null && extension.getStyleURL() != null)
				{
					// maybe cache style content with key is path
					String pathStyleFile = extension.getFullPathStyle(); // creates a temp file - delete below
					Path path = Paths.get(pathStyleFile);
				    List<String> styleLines = Files.readAllLines(path, Ini.getCharset());
				    Files.delete(path); // delete temp file
				    StringBuilder styleBuild = new StringBuilder();
				    for (String styleLine : styleLines){
				    	styleBuild.append(styleLine); //.append("\n");
				    }
				    appendInlineCss (doc, styleBuild);
				}
				if (extension != null && extension.getScriptURL() != null && !isExport)
				{
					script jslink = new script();
					jslink.setLanguage("javascript");
					jslink.setSrc(extension.getScriptURL());
					doc.appendHead(jslink);
				}
				
				if (extension != null && !isExport){
					//extension.setWebAttribute(doc.getBody());
				}				
			}
			
			if (doc != null)
			{
				//IDEMPIERE-4113
				mapCssInfo.clear();
				MPrintFormatItem item = null;
				int printColIndex = -1;
				for(int col = 0; col < m_printFormat.getItemCount(); col++)
				{
					item = m_printFormat.getItem(col);
					if(item.isPrinted())
					{
						printColIndex++;
						addCssInfo(item, printColIndex);
					}
				}//IDEMPIERE-4113
				appendInlineCss(doc);
				
				StringBuilder styleBuild = new StringBuilder();
									
				MPrintFont printFont = MPrintFont.get(m_printFormat.getAD_PrintFont_ID());
				Font base = printFont.getFont();
				Font newFont = new Font(base.getName(), Font.PLAIN, base.getSize()-1);
				CSSInfo cssInfo = new CSSInfo(newFont, null);
				styleBuild.append(".tr-level-1 td").append(cssInfo.getCssRule());
				
				newFont = new Font(base.getName(), Font.PLAIN, base.getSize()-2);
				cssInfo = new CSSInfo(newFont, null);
				styleBuild.append(".tr-level-2 td").append(cssInfo.getCssRule());
				
				styleBuild = new StringBuilder(styleBuild.toString().replaceAll(";", "!important;"));
				appendInlineCss (doc, styleBuild);
							
				doc.output(w);
				
				table.output(w);
			}
			
			tbody tbody = new tbody();
			tbody.setNeedClosingTag(false);
			
			ProcessInfo pi = (ProcessInfo)m_params.get("ProcessInfo");
			int countRow = pi.getRowCountQuery();
			ArrayList<ArrayList<PrintDataItem>> dataQuery = pi.getDataQuery();
			int maxRow = pi.getMaxRow();
			//Add Header
			tr tr = new tr();
			MPrintFormatItem [] items = m_printFormat.getItemContent();
            
			for(int r = 1; r <= maxRow; r++) {
	    		for(int c = 0; c < items.length; c++) {
	            	MPrintFormatItem item = items[c];
	            	int rowOrder = Integer.parseInt(item.getOrderRowHeader());
	            	if (rowOrder == r) {
	            		th th = new th();
	    				tr.addElement(th);
	    				th.addElement(Util.maskHTML(item.getName(m_printFormat.getLanguage(), 0)));
	            		th.setColSpan(item.getColumnSpan());
	            		th.setRowSpan(item.getNumLines());		
	            		th.setWidth(item.getMaxWidth());
	            	}
	            }  
	    		tr.output(w);
	    		tr = new tr();
	    	}
				            
            tr.output(w);
			
           				
			for (int row = 0; row < countRow; row ++) {

				tr = new tr();
				ArrayList<PrintDataItem> arrItem = dataQuery.get(row);
            	for(int c = 0; c < arrItem.size(); c++) {  
            		PrintDataItem item = arrItem.get(c);

					td td = new td();
					tr.addElement(td);
					Object value = item.getValueDisplay(Env.getLanguage(Env.getCtx()));
					if (value == null){
						td.addElement("&nbsp;");
					}else if(item.isZoomLogic()) {            			
						a href = new a("javascript:void(0)");									
						href.setID(item.getColumnName() + "_" + row + "_a");									
						td.addElement(href);
						href.addElement(Util.maskHTML(value.toString()));
						if (cssPrefix != null)
							href.setClass(cssPrefix + "-href");
						extension.extendIDColumn(row, td, href, item);
						//new WAcctViewer(item.getZoomLogic());
					
					}else {
						td.addElement(Util.maskHTML(value.toString()));
					}
				}	//	for all columns
				
				tr.output(w);					
			
			}
			
			w.println();
			w.println("</tbody>");
			w.println("</table>");
			if (!onlyTable)
			{
				w.println("</body>");
				w.println("</html>");
			}
			w.flush();
			w.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(w)", e);
			throw new AdempiereException(e);
		}
		return true;
	}


	private String getCSSFontFamily(String fontFamily) {
		if ("Dialog".equals(fontFamily) || "DialogInput".equals(fontFamily) || 	"Monospaced".equals(fontFamily))
		{
			return "monospace";
		} else if ("SansSerif".equals(fontFamily))
		{
			return "sans-serif";
		} else if ("Serif".equals(fontFamily))
		{
			return "serif";
		}
		return null;
	}

	
	
	public File getPDF (File file)
	{
		try
		{
			if (file == null)
				file = File.createTempFile (makePrefix(getName()), ".pdf");
		}
		catch (IOException e)
		{
			log.log(Level.SEVERE, "", e);
		}
		if (createPDF (file))
			return file;
		return null;
	}	//	getPDF

	
	public File getHTML()
	{
		File file = null;
		try
		{
			file = File.createTempFile (makePrefix(getName()), ".html");
		}
		catch (IOException e)
		{
			log.log(Level.SEVERE, "", e);
		}
		if (createHTML(file, false))
			return file;
		return null;
	}	//	getHTML
	
	
	
	public File getXLSX()
	{
		File file = null;
		try
		{
			file = File.createTempFile (makePrefix(getName()), ".xlsx");
		}
		catch (IOException e)
		{
			log.log(Level.SEVERE, "", e);
		}
		try 
		{
			createXLSX(file);
			return file;
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			return null;
		}
	}	//	getXLSX
	
	/**
	 * 	Create PDF File
	 * 	@param file file
	 * 	@return true if success
	 */
	public boolean createPDF (File file)
	{
		String fileName = null;
		URI uri = null;

		try
		{
			if (file == null)
				file = File.createTempFile ("ReportEngine", ".pdf");
			fileName = file.getAbsolutePath();
			uri = file.toURI();
			if (file.exists())
				file.delete();

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "file", e);
			return false;
		}
			
		if (log.isLoggable(Level.FINE)) log.fine(uri.toString());

		try
		{
			if (m_printFormat != null) {
				//if (m_layout == null)
				//	layout ();
				new WritePDF().getPDFAsFile(fileName, m_printFormat, m_params);//, m_layout.getPageable(false)
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "PDF", e);
			throw new AdempiereException(e);
		}

		File file2 = new File(fileName);
		if (log.isLoggable(Level.INFO)) log.info(file2.getAbsolutePath() + " - " + file2.length());
		return file2.exists();
	}	//	createPDF

	private String makePrefix(String name) {
		StringBuilder prefix = new StringBuilder();
		char[] nameArray = name.toCharArray();
		for (char ch : nameArray) {
			if (Character.isLetterOrDigit(ch)) {
				prefix.append(ch);
			} else {
				prefix.append("_");
			}
		}
		return prefix.toString();
	}
	
	/**
	 * 	Create PDF as Data array
	 *	@return pdf data
	 */
	public byte[] createPDFData ()
	{
		try
		{
			//if (m_layout == null)
			//	layout ();
			return WritePDF.getPDFAsArray(m_printFormat, m_params);//m_layout.getPageable(false),
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "PDF", e);
		}
		return null;
	}	//	createPDFData
	
	
	public void createXLSX(File outFile)
	throws Exception
	{
		PrintDataXLSXExporter exp = new PrintDataXLSXExporter(m_params, getPrintFormat());
		exp.export(outFile, true);
	}
	
	/**************************************************************************
	 * 	Get Report Engine for process info 
	 *	@param ctx context
	 *	@param pi process info with AD_PInstance_ID
	 *	@return report engine or null
	 */
	static public ReportEngine get (Properties ctx, ProcessInfo pi, HashMap<String, Object> m_params)
	{
		
		MProcess process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());
		MPrintFormat format = MPrintFormat.get(Env.getCtx(), process.getAD_PrintFormat_ID(), false);
		
		//format.setTranslationLanguage(format.getLanguage());
		//
		PrintInfo info = new PrintInfo (pi);
		
		return new ReportEngine(ctx, format, info, pi.isSummary(), pi.getTransactionName(), m_params);
	}	//	get
	
	
	public static ReportEngine get (Properties ctx, int type, int Record_ID, HashMap<String, Object> params)
	{
		return get(ctx, type, Record_ID, params);
	}
	
	

	public void setWhereExtended(String whereExtended) {
		m_whereExtended = whereExtended;
	}

	public String getWhereExtended() {
		return m_whereExtended;
	}

	/* Save windowNo of the report to parse the context */
	public void setWindowNo(int windowNo) {
		m_windowNo = windowNo;
	}
	
	public int getWindowNo() {
		return m_windowNo;
	}

	public void setSummary(boolean summary)
	{
		m_summary = summary;
	}

	public boolean isSummary()
	{
		return m_summary;
	}
	
	public void setLanguageID(int languageID)
	{
		m_language_id = languageID;
	}

	public int getLanguageID()
	{
		return m_language_id;
	}
	
	private String reportType;
	
	public void setReportType(String type)
	{
		reportType = type;
	}
	
	public String getReportType()
	{
		return reportType;
	}
	
	/**
	 * build css for table from mapCssInfo
	 * @param doc
	 */
	public void appendInlineCss (XhtmlDocument doc){
		StringBuilder buildCssInline = new StringBuilder();
		
		// each entry is a css class
		for (Entry<CSSInfo, List<ColumnInfo>> cssClassInfo : mapCssInfo.entrySet()){
			// each column is a css name.
			for (int i = 0; i < cssClassInfo.getValue().size(); i++){
				if (i > 0)
					buildCssInline.append (",");
				
				buildCssInline.append(cssClassInfo.getValue().get(i).getCssSelector());
			}
			
			buildCssInline.append(cssClassInfo.getKey().getCssRule());
			buildCssInline.append("\n");
		}
		
		appendInlineCss (doc, buildCssInline);
	}
	
	public void appendInlineCss (XhtmlDocument doc, StringBuilder buildCssInline){
		if (buildCssInline.length() > 0){
			buildCssInline.insert(0, "<style>");
			buildCssInline.append("</style>");
			doc.appendHead(buildCssInline.toString());
		}
	}
	
	/**
	 * create css info from formatItem, add all column has same formatItem in a list
	 * @param formatItem
	 * @param index
	 */
	public void addCssInfo (MPrintFormatItem formatItem, int index){
		CSSInfo cadidateCss = new CSSInfo(formatItem);
		if (mapCssInfo.containsKey(cadidateCss)){
			mapCssInfo.get(cadidateCss).add(new ColumnInfo(index, formatItem));
		}else{
			List<ColumnInfo> newColumnList = new ArrayList<ColumnInfo>();
			newColumnList.add(new ColumnInfo(index, formatItem));
			mapCssInfo.put(cadidateCss, newColumnList);
		}
	}
	

	public class CSSInfo {
		private Font font;		
		private Color color;
		private String cssStr;
		public CSSInfo (MPrintFormatItem item){
			MPrintFont mPrintFont = null;
			I_AD_PrintFormat m_printFormat = item.getAD_PrintFormat();
			
			if (item.getAD_PrintFont_ID() > 0) 
			{
				mPrintFont = MPrintFont.get(item.getAD_PrintFont_ID());
			}			
			else if (m_printFormat.getAD_PrintFont_ID() > 0)
			{
				mPrintFont = MPrintFont.get(m_printFormat.getAD_PrintFont_ID());
			}
			if (mPrintFont != null && mPrintFont.getAD_PrintFont_ID() > 0)
			{
				font = mPrintFont.getFont();				
			}
			
			MPrintColor mPrintColor = null;
			if (item.getAD_PrintColor_ID() > 0) 
			{
				mPrintColor = MPrintColor.get(m_ctx, item.getAD_PrintColor_ID());
			}
			else if (m_printFormat.getAD_PrintColor_ID() > 0)
			{
				mPrintColor = MPrintColor.get(m_ctx, m_printFormat.getAD_PrintColor_ID());
			}
			if (mPrintColor != null && mPrintColor.getAD_PrintColor_ID() > 0)
			{
				color = mPrintColor.getColor();
				
			}
		}
		
		public CSSInfo (Font font, Color color) {
			this.font = font;
			this.color = color;
		}
		
		/**
		 * sum hashCode of partial
		 */
		@Override
		public int hashCode() {
			return (color == null ? 0 : color.hashCode()) + (font == null ? 0 : font.hashCode());
		}
		
		/**
		 * equal only when same color and font
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof CSSInfo) || obj.hashCode() != this.hashCode())
				return false;
			
			CSSInfo compareObj = (CSSInfo)obj;
			
			return compareObj (compareObj.color, color) && compareObj (compareObj.font, font);			
		}
		
		/**
		 * compare two object equal when both is null or result of equal
		 * @param obj1
		 * @param obj2
		 * @return
		 */
		protected boolean compareObj(Object obj1, Object obj2) {
			if (obj1 == null && obj2 != null)
				return false;
			
			if (obj1 == null && obj2 == null){
				return true;
			}
			
			return obj1.equals(obj2);
		}
		
		/**
		 * append a css rule to css class
		 * @param cssBuild
		 * @param ruleName
		 * @param ruleValue
		 */
		protected void addCssRule(StringBuilder cssBuild, String ruleName, String ruleValue) {
			cssBuild.append (ruleName);
			cssBuild.append (":");
			cssBuild.append (ruleValue);
			cssBuild.append (";");
		}
		
		/**
		 * build css rule
		 * @return
		 */
		public String getCssRule (){
			if (cssStr != null)
				return cssStr;
			
			StringBuilder cssBuild = new StringBuilder();
			cssBuild.append ("{");
			
			if (font != null){
				
				String fontFamily = font.getFamily();
				fontFamily = getCSSFontFamily(fontFamily);
				if (fontFamily != null){
					addCssRule(cssBuild, "font-family", fontFamily);
				}
				
				if (font.isBold())
				{
					addCssRule(cssBuild, "font-weight", "bold");					
				}
				
				if (font.isItalic())
				{
					addCssRule(cssBuild, "font-style", "italic");
				}									
				
				int size = font.getSize();
				addCssRule(cssBuild, "font-size", size + "pt");
			}
			
			if (color != null)
			{
				cssBuild.append("color:rgb(");
				cssBuild.append(color.getRed()); 
				cssBuild.append(",");
				cssBuild.append(color.getGreen());
				cssBuild.append(",");
				cssBuild.append(color.getBlue());
				cssBuild.append(");");
			}
			cssBuild.append ("}");
			cssStr = cssBuild.toString();
			
			return cssStr;
		}
	}
	
	
	public static class ColumnInfo {
		protected static String CSS_SELECTOR_TEMPLATE = "table > tbody > tr > td:nth-child(%1$s)";
		int index = -1;
		public ColumnInfo (int index, MPrintFormatItem formatItem){
			this.index = index;
			
		}
		
		public String getCssSelector(){
			return String.format(CSS_SELECTOR_TEMPLATE, index + 1);
		}
	}
	
	
}	//	ReportEngine
