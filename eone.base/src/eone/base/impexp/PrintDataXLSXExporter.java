package eone.base.impexp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.print.attribute.standard.MediaSizeName;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormatItem;
import org.compiere.print.MPrintPaper;
import org.compiere.print.PrintDataItem;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Util;

import eone.base.model.MSysConfig;
import eone.base.process.ProcessInfo;

public class PrintDataXLSXExporter
{
	private MPrintFormat	m_printFormat;
	int rowCount = 0;
	int columnCount = 0;
	int windowNo;
	ProcessInfo info = null;
	private static MPrintFormatItem [] items = null;
	
	private XSSFWorkbook					m_workbook;
	private XSSFFont						m_fontHeader	= null;
	private XSSFFont						m_fontDefault	= null;
	private HashMap<String, XSSFCellStyle>	m_styles		= new HashMap<String, XSSFCellStyle>();
	
	private List<ArrayList<PrintDataItem>> dataQuery = null;

	public PrintDataXLSXExporter(HashMap<String, Object> m_params, MPrintFormat printFormat)
	{
		super();
		this.m_printFormat = printFormat;
		info = (ProcessInfo) m_params.get("ProcessInfo");
		rowCount = info.getRowCountQuery();
		windowNo = (int)m_params.get("WindowNo");
		columnCount = info.getColumnCountQuery();
		dataQuery = info.getDataQueryC();
		m_workbook = new XSSFWorkbook();
		m_params.clear();
		
	}

	
	public String getHeaderName(int col)
	{
		return m_printFormat.getItemMapSQL(col).getName(getLanguage(), windowNo);
	}
	
	protected Language getLanguage()
	{
		return Env.getLanguage(Env.getCtx());
	}

	public int getRowCount()
	{
		return rowCount;
	}

	public boolean isColumnPrinted(int col)
	{
		MPrintFormatItem item = m_printFormat.getItem(col);
		return item.isPrinted();
	}

	


	protected void formatPage(XSSFSheet sheet)
	{
		MPrintPaper paper = MPrintPaper.get(this.m_printFormat.getAD_PrintPaper_ID());

		// Set paper size:
		short paperSize = -1;
		MediaSizeName mediaSizeName = paper.getMediaSize().getMediaSizeName();
		if (MediaSizeName.NA_LETTER.equals(mediaSizeName))
		{
			paperSize = HSSFPrintSetup.LETTER_PAPERSIZE;
		}
		else if (MediaSizeName.NA_LEGAL.equals(mediaSizeName))
		{
			paperSize = HSSFPrintSetup.LEGAL_PAPERSIZE;
		}
		else if (MediaSizeName.EXECUTIVE.equals(mediaSizeName))
		{
			paperSize = HSSFPrintSetup.EXECUTIVE_PAPERSIZE;
		}
		else if (MediaSizeName.ISO_A4.equals(mediaSizeName))
		{
			paperSize = HSSFPrintSetup.A4_PAPERSIZE;
		}
		else if (MediaSizeName.ISO_A5.equals(mediaSizeName))
		{
			paperSize = HSSFPrintSetup.A5_PAPERSIZE;
		}
		else if (MediaSizeName.NA_NUMBER_10_ENVELOPE.equals(mediaSizeName))
		{
			paperSize = HSSFPrintSetup.ENVELOPE_10_PAPERSIZE;
		}
		else if (MediaSizeName.MONARCH_ENVELOPE.equals(mediaSizeName))
		{
			paperSize = HSSFPrintSetup.ENVELOPE_MONARCH_PAPERSIZE;
		}
		if (paperSize != -1)
		{
			sheet.getPrintSetup().setPaperSize(paperSize);
		}

		// Set Landscape/Portrait:
		sheet.getPrintSetup().setLandscape(paper.isLandscape());

		// Set Paper Margin:
		sheet.setMargin(HSSFSheet.TopMargin, ((double) paper.getMarginTop()) / 72);
		sheet.setMargin(HSSFSheet.RightMargin, ((double) paper.getMarginRight()) / 72);
		sheet.setMargin(HSSFSheet.LeftMargin, ((double) paper.getMarginLeft()) / 72);
		sheet.setMargin(HSSFSheet.BottomMargin, ((double) paper.getMarginBottom()) / 72);
	}
	

	public boolean isDisplayed(int row, int col)
	{
		return true;
	}
	
	
	
	private XSSFFont getFont(boolean isHeader)
	{
		XSSFFont font = null;
		if (isHeader)
		{
			if (m_fontHeader == null)
			{
				m_fontHeader = m_workbook.createFont();
				m_fontHeader.setBold(true);
			}
			font = m_fontHeader;
		}
		else
		{
			if (m_fontDefault == null)
			{
				m_fontDefault = m_workbook.createFont();
			}
			font = m_fontDefault;
		}
		return font;
	}



	private XSSFCellStyle getHeaderStyle(int col)
	{
		String key = "header-" + col;
		XSSFCellStyle cs_header = m_styles.get(key);
		if (cs_header == null)
		{
			XSSFFont font_header = getFont(true);
			cs_header = m_workbook.createCellStyle();
			cs_header.setFont(font_header);
			cs_header.setBorderLeft(BorderStyle.THIN);
			cs_header.setBorderTop(BorderStyle.THIN);
			cs_header.setBorderRight(BorderStyle.THIN);
			cs_header.setBorderBottom(BorderStyle.THIN);
			cs_header.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
			cs_header.setWrapText(true);
			cs_header.setVerticalAlignment(VerticalAlignment.CENTER);
			cs_header.setAlignment(HorizontalAlignment.CENTER);
			cs_header.setDataFormat(14);
			m_styles.put(key, cs_header);
		}
		return cs_header;
	}

	

	private XSSFSheet createTableSheet()
	{
		XSSFSheet sheet = m_workbook.createSheet();
		formatPage(sheet);
		createHeaderFooter(sheet);
		createTableHeader(sheet);
		return sheet;
	}

	private void createTableHeader(XSSFSheet sheet)
	{
		int maxRow = info.getMaxRow();
		items = m_printFormat.getItemContent();
		XSSFRow row = null;
		row = sheet.createRow(0);
		for(int r = 1; r <= maxRow; r++) {
			row = sheet.createRow(r);
			int colExcel = 0;
    		for(int c = 0; c < items.length; c++) {
            	MPrintFormatItem item = items[c];
            	int rowOrder = Integer.parseInt(item.getOrderRowHeader());
            	if (rowOrder == r && item.isPrinted()) {
            		XSSFCell cell = row.createCell(colExcel);
           			// header row
    				
    				if (item.getNumLines() > 1 && item.getColumnSpan() > 1)
    					sheet.addMergedRegion(new CellRangeAddress(r, r + item.getNumLines() - 1, colExcel,colExcel + item.getColumnSpan() - 1));
    				else if (item.getColumnSpan() > 1)
    					sheet.addMergedRegion(new CellRangeAddress(r, r, colExcel, colExcel + item.getColumnSpan() - 1));
    				else if (item.getNumLines() > 1)
    					sheet.addMergedRegion(new CellRangeAddress(r, r + item.getNumLines() - 1, colExcel, colExcel));
    				String str = fixString(item.getName(Env.getLanguage(Env.getCtx()), windowNo));
    				cell.setCellValue(new XSSFRichTextString(str));
    				XSSFCellStyle style = getHeaderStyle(colExcel);
    				cell.setCellStyle(style);
    				colExcel += item.getColumnSpan();
            	} else if (r > 1 && item.getNumLines() > 1) {
            		colExcel += item.getColumnSpan();
            	}
            }            
    	}
		// for all columns
		/*
		for (int col = 0; col < columnCount; col++)
		{
			if (colnum > colnumMax)
				colnumMax = colnum;
			//
			if (isColumnPrinted(col))
			{
				XSSFCell cell = row.createCell(colnum);
				// header row
				XSSFCellStyle style = getHeaderStyle(col);
				cell.setCellStyle(style);
				String str = fixString(getHeaderName(col));
				cell.setCellValue(new XSSFRichTextString(str));
				colnum++;
			} // printed
		} // for all columns
		*/
	}
	
	private String fixString(String str)
	{
		return Util.stripDiacritics(str);
	}


	protected void createHeaderFooter(XSSFSheet sheet)
	{
		// Sheet Header
		Header header = sheet.getHeader();
		header.setRight(HSSFHeader.page() + " / " + HSSFHeader.numPages());
		// Sheet Footer
		Footer footer = sheet.getFooter();
		footer.setLeft(Env.getStandardReportFooterTrademarkText());
		String s = MSysConfig.getValue(MSysConfig.ZK_FOOTER_SERVER_MSG, "", Env.getAD_Client_ID(Env.getCtx()));
		if (Util.isEmpty(s, true))
			footer.setCenter(Env.getHeader(Env.getCtx(), 0));	
		else
			footer.setCenter(Msg.parseTranslation(Env.getCtx(), s));
		Timestamp now = new Timestamp(System.currentTimeMillis());
		s = MSysConfig.getValue(MSysConfig.ZK_FOOTER_SERVER_DATETIME_FORMAT, Env.getAD_Client_ID(Env.getCtx()));
		if (!Util.isEmpty(s, true))
			footer.setRight(new SimpleDateFormat(s).format(System.currentTimeMillis()));
		else
			footer.setRight(DisplayType.getDateFormat(DisplayType.DateTime, getLanguage()).format(now));
	}


	
	private void export(OutputStream out) throws Exception
	{
		XSSFSheet sheet = createTableSheet();
		
		//
		XSSFCellStyle style = m_workbook.createCellStyle();
		for (int r = 0; r < rowCount; r ++)
		{
			int pos = 0;
			ArrayList<PrintDataItem> arrItem = dataQuery.get(r);
			XSSFRow row = sheet.createRow(r+3);
			
			// for all columns
			for (int col = 0; col < arrItem.size(); col++)
			{
				PrintDataItem item = arrItem.get(col);
				
				XSSFCell cell = row.createCell(col + pos);
				
				if (item.getColSpan() > 1)
					pos = item.getColSpan() - 1;
				// line row
				Object obj = item.getValue();
				int displayType = item.getDisplayType();
				
        		
				if (DisplayType.isDate(displayType))
				{
					java.util.Date value = (java.util.Date) obj;
					cell.setCellValue(value);
					cell.setCellStyle(style);
				}
				else if (DisplayType.isNumeric(displayType))
				{
					double value = 0;
					if (obj instanceof Number)
					{
						value = ((Number) obj).doubleValue();
					}
					if (value != 0)
					{
						cell.setCellValue(value);
						cell.setCellStyle(style);
					}
				}
				
				else 
				{
					if (obj != null)
					{
						String value = fixString(obj.toString()); // formatted
						cell.setCellValue(new XSSFRichTextString(value));
					}
				}
				
			} // for all columns
			
		} // for all rows

		if (out != null)
		{
			m_workbook.write(out);
			out.close();
		}
		
	}

	
	public void export(File file, boolean autoOpen) throws Exception
	{
		if (file == null)
			file = File.createTempFile("Report_", ".xlsx");
		FileOutputStream out = new FileOutputStream(file);
		export(out);
		if (autoOpen && Ini.isClient())
			Env.startBrowser(file.toURI().toString());
	}

	
}
