package org.adempiere.pdf;

import java.awt.print.PageFormat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.Adempiere;
import org.compiere.model.MConfigSignReport;
import org.compiere.print.CPaper;
import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormatItem;
import org.compiere.print.MPrintPaper;
import org.compiere.print.PrintDataItem;
import org.compiere.util.Env;
import org.compiere.util.Language;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import eone.base.process.ProcessInfo;
import eone.base.process.ProcessInfoParameter;


/**
 * @author Quynhnv.x8
 * Date: 03/10/2020
 */
public class WritePDF {

	static {
		FontFactory.registerDirectories();
	}
	
	public WritePDF () {
		
	}
	
	public static final String FONT = Adempiere.getAdempiereHome() + File.separator + "arial.ttf";

	
	//Create Header
	private static void createHeader(PdfPTable table, HashMap<String, Object> m_params, MPrintFormat format, BaseFont courier, int columnCount) {
		//MClientInfo ci = MClientInfo.get(Env.getCtx());
		PdfPCell cell = null;
		Font smallfont = new Font(courier,10, Font.BOLD);
		//Fill Company And Address.
		String company = m_params.get("COMPANY_NAME").toString();
		String address = m_params.get("ADDRESS").toString();
		String tempApply = "";
		if (m_params.get("TEMPAPPLY") != null) {
			tempApply = m_params.get("TEMPAPPLY").toString();
		}
		//Neu khong cau hinh header
		//Header la Title bao cao tro len tren. Cac tham so khong dua vao cau hinh. Cac tham so se duoc lay tu giao dien chon bao cao
		if (header.length == 0) {
			//row1
			cell = new PdfPCell(new Phrase(company, smallfont));
	        cell.setNoWrap(false);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setColspan((int)columnCount/2);
	        cell.setPadding(4);
	        table.addCell(cell);
	        
	        smallfont = new Font(courier,10, Font.NORMAL);
	        cell = new PdfPCell(new Phrase(tempApply, smallfont));
	        cell.setNoWrap(false);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setColspan(columnCount - (int)columnCount/2);
	        cell.setRowspan(2);
	        cell.setPadding(4);
	        table.addCell(cell);
	        
			//row 2
	        
	        cell = new PdfPCell(new Phrase(address, smallfont));
	        cell.setNoWrap(false);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setColspan((int)columnCount/2);
	        cell.setPadding(4);
	        table.addCell(cell);
	        
			//Fill Templete Reference (if any)
			
			//Fill Title Report
	        smallfont = new Font(courier,10, Font.BOLD);
			String titleReport = m_params.get("ReportName").toString().toUpperCase();
			cell = new PdfPCell(new Phrase(titleReport, smallfont));
	        cell.setNoWrap(false);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setColspan(columnCount);
	        cell.setBorderWidth(0.5f);
	        cell.setPaddingBottom(5);
	        table.addCell(cell);
		}//End khong cau hinh heade, 
		else {//Co cau hinh header
			for( int i = 0; i < header.length; i++) {
	        	MPrintFormatItem item = header[i];
	        	String name = item.getName(language, windowNo);
	        	if (name.contains("@")) {
	        		name = Env.parseContext(Env.getCtx(), windowNo, name, false);
	        	}
	        	cell = new PdfPCell(new Phrase((String) name, smallfont));
	        	cell.setNoWrap(false);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setHorizontalAlignment(item.getAlignment());
	            cell.setBorder(Rectangle.NO_BORDER);
	            cell.setRowspan(item.getNumLines());
	            cell.setColspan(item.getColumnSpan());
	            cell.setPadding(4);
	            table.addCell(cell); 
	        }
		}//End cau hinh header
		
        
        //Fill Param:
        ProcessInfo processInfo = (ProcessInfo) m_params.get("ProcessInfo");
        ProcessInfoParameter[] para = processInfo.getProcessPara();
        //Fill From Date And ToDate
        String date = "";
        ArrayList<String> arrOtherParam = new ArrayList<String>();
    	for (int i = 0; i < para.length; i ++) {
    		ProcessInfoParameter item = para[i];
    		String other = "";
    		String name = para[i].getParameterName();
            String info = para[i].getInfo();
    		if ("FromDate".equalsIgnoreCase(name)) {
    			date += item.getHeader()  + ": " + info + " - ";
    		}
    		else if ("ToDate".equalsIgnoreCase(name)) {
    			date += item.getHeader()  + ": " + info;
    		} else if (info != null && !info.isEmpty()) {
    			other = item.getHeader()  + ": " + info;
    		}
    		arrOtherParam.add(other);
    	}
    	smallfont = new Font(courier,10, Font.NORMAL);
 		cell = new PdfPCell(new Phrase(date, smallfont));
        cell.setNoWrap(false);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(columnCount);
        cell.setBorderWidth(0.5f);
        cell.setPaddingBottom(10);
        table.addCell(cell);
        //Fill Param Other.
        smallfont = new Font(courier,10, Font.NORMAL);
        for(int i = 0; i < arrOtherParam.size(); i++) {
        	cell = new PdfPCell(new Phrase("", smallfont));
            cell.setNoWrap(false);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(1);
            cell.setPadding(4);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(arrOtherParam.get(i), smallfont));
            cell.setNoWrap(false);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(columnCount - 1);
            cell.setPadding(4);
            table.addCell(cell);
        }
        //Fill Currency.
        
	}//End Create Header
	
	//Create Footer
	private static PdfPTable createFooter(HashMap<String, Object> m_params, MPrintFormat format, BaseFont courier, MPrintFormatItem [] items) {
		PdfPTable table;
		MConfigSignReport [] listSign = (MConfigSignReport []) m_params.get("SIGNREPORT");
		int columnSign = listSign.length;
		if (columnSign == 0) {
			columnSign = 1;
		}
		PdfPCell cell = null;
		Font smallfont = new Font(courier,10, Font.ITALIC);
		
		float total = (float)widthBase;
		
		float [] arrWidthNew = new float [columnSign];
		
		int col = (int) (total / columnSign);
		int acc = 0;
		for(int i = 0; i < columnSign; i++) {
			if (i == columnSign - 1) {
				col = (int) (total - acc);
			}
			arrWidthNew[i] = col;
			acc += col;
		}
		
		table = new PdfPTable(columnSign);
		try {
			table.setTotalWidth(arrWidthNew);
		} catch (DocumentException e) {
		}
        table.setLockedWidth(true);
        
		if (listSign.length > 0) {
        	for(int i = 0; i < columnSign; i++) {
            	MConfigSignReport item = listSign[i]; 
            	smallfont = new Font(courier,10, Font.BOLD);
            	cell = new PdfPCell(new Phrase(item.getPosition(), smallfont));
                cell.setNoWrap(false);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.setPadding(4);
                table.addCell(cell);
    		}
            for(int i = 0; i < columnSign; i++) {
            	MConfigSignReport item = listSign[i]; 
                smallfont = new Font(courier,10, Font.ITALIC);
            	cell = new PdfPCell(new Phrase(item.getName2(), smallfont));
                cell.setNoWrap(false);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.setPaddingBottom(40);;
                table.addCell(cell);
    		}
            for(int i = 0; i < columnSign; i++) {
            	MConfigSignReport item = listSign[i]; 
                smallfont = new Font(courier,10, Font.BOLD);
            	cell = new PdfPCell(new Phrase(item.getName(), smallfont));
                cell.setNoWrap(false);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                table.addCell(cell);
    		}
        }
        
        return table;
	}// End Create Footer
	
	
	public  File getPDFAsFile(String filename, MPrintFormat m_printFormat, HashMap<String, Object> m_params) {//Pageable pageable, 
        final File result = new File(filename);
        
        try {
        	writePDF(new FileOutputStream(result), m_printFormat, m_params);//pageable, 
        } catch (Exception e) {
            throw new AdempiereException(e);
        }
        
        return result;
    }
    
    public static byte[] getPDFAsArray(MPrintFormat m_printFormat, HashMap<String, Object> m_params) {//Pageable pageable, 
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream(10240);
            //writePDF(output, m_printFormat, m_params);//pageable, 
            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return null;
    }
    
    private static float [] widthTable = null;
    private static int countColumn = 0;
    private static int countRow = 0;
    private static int windowNo = 0;
    private static ArrayList<ArrayList<PrintDataItem>> dataQuery = null;
    private static MPrintFormatItem [] items = null;
    private static MPrintFormatItem [] header = null;
    private static MPrintFormatItem [] footer = null;
    private static Font smallfont = null;
    private static Language language = null;
    //private static int rowRepeat = 0;
    private static double widthBase = 0;
    private static int maxRow = 0;
    
    private static void createHeaderTable(PdfPTable table) {
    	PdfPCell cell = null;
    	
    	for(int r = 1; r <= maxRow; r++) {
    		for(int c = 0; c < items.length; c++) {
            	MPrintFormatItem item = items[c];
            	int rowOrder = Integer.parseInt(item.getOrderRowHeader());
            	if (rowOrder == r && item.isPrinted()) {
            		cell = new PdfPCell(new Phrase(item.getName(language, windowNo), smallfont));
                    cell.setNoWrap(false);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    //cell.setBorder(Rectangle.BOX);
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setColspan(item.getColumnSpan());
                    cell.setRotation(item.getRotationText());
                    cell.setRowspan(item.getNumLines());
                    cell.setPadding(4);
                    //cell.setBorderWidth(0.5f);
                    table.addCell(cell);
            	}
            }            
    	}
        
    }
    
    
    private  void createContent(PdfPTable table) {
    	PdfPCell cell = null;
    	//Map<Integer, BigDecimal> lsTotal = new HashMap<Integer, BigDecimal>();
    	//Map<Integer, BigDecimal> lsMax = new HashMap<Integer, BigDecimal>();
    	//Map<Integer, BigDecimal> lsMin = new HashMap<Integer, BigDecimal>();
    	//Map<Integer, BigDecimal> lsAvg = new HashMap<Integer, BigDecimal>();
    	//Map<Integer, BigDecimal> lsBalance = new HashMap<Integer, BigDecimal>();
        for(int row = 0; row < countRow; row ++) {
        	ArrayList<PrintDataItem> arrItem = dataQuery.get(row);
        	
        	//
        	for(int c = 0; c < arrItem.size(); c++) {  
        		PrintDataItem item = arrItem.get(c);
        		Object value = item.getValueDisplay(Env.getLanguage(Env.getCtx()));
            	cell = new PdfPCell(new Phrase((String) value, smallfont));
                cell.setNoWrap(false);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(item.getAlignment());
                cell.setPadding(4);
                table.addCell(cell);
                
            }
        }
        
        //Add 1 dong trang cach footer
        cell = new PdfPCell(new Phrase(" ", smallfont));
    	cell.setNoWrap(false);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setRowspan(2);
        cell.setColspan(countColumn);
        table.addCell(cell);
        //add footer
        for( int i = 0; i < footer.length; i++) {
        	MPrintFormatItem item = footer[i];
        	String name = item.getName(language, windowNo);
        	if (name.contains("@")) {
        		name = Env.parseContext(Env.getCtx(), windowNo, name, false);
        	}
        	cell = new PdfPCell(new Phrase((String) name, smallfont));
        	cell.setNoWrap(false);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(item.getAlignment());
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setRowspan(item.getNumLines());
            cell.setColspan(item.getColumnSpan());
            cell.setPadding(4);
            table.addCell(cell); 
        }
        
    }
    
    private  void writePDF(OutputStream output, MPrintFormat format, HashMap<String, Object> m_params)//Pageable pageable, 
	{
		try {
			
			ProcessInfo pi = (ProcessInfo)m_params.get("ProcessInfo");
			widthTable = pi.getWidthTable();
			countColumn = pi.getColumnCountQuery();
			countRow = pi.getRowCountQuery();
			dataQuery = pi.getDataQuery();
			maxRow = pi.getMaxRow();
			windowNo = (int)m_params.get("WindowNo");
			
			MPrintPaper paper = MPrintPaper.get(format.getAD_PrintPaper_ID());
			CPaper cPaper = paper.getCPaper();
			Background event = new Background();
            final PageFormat pf = cPaper.getPageFormat();
            widthBase = pf.getWidth();
            final Document document = new Document(new Rectangle((int) pf.getWidth(), (int) pf.getHeight()));
            
            final PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setPdfVersion(PdfWriter.VERSION_1_2);
            
            writer.setPageEvent(event);
            
            document.open();
            //document.setMargins(36, 36, 36, 36);
            //MAttachment attact = format.getAttachment();
            //String fileName = attact.getEntryName(0);
            String fontDir = getFont(FONT);
            BaseFont courier = BaseFont.createFont(fontDir,  BaseFont.IDENTITY_H , BaseFont.EMBEDDED);
            smallfont = new Font(courier,10, Font.NORMAL);
            
            PdfPTable table = new PdfPTable(countColumn);
            
            items = format.getItemContent();
            header = format.getItemHeader();
            footer = format.getItemFooter();
            language = format.getLanguage();
            
            table.setTotalWidth(widthTable);
            table.setLockedWidth(true);
            
            //Create header Report
            createHeader(table, m_params, format, courier, countColumn); 
            document.add(table);
            
            // first row: add header
            table = new PdfPTable(countColumn);
            table.setTotalWidth(widthTable);
            table.setLockedWidth(true);
            createHeaderTable(table);
            document.add(table);
            table.setHeaderRows(maxRow);
            table.setSkipFirstHeader(true);
            
            //For all row
            createContent(table);
            //End For all row
    		document.add(table);
    		
            //Create Footer
    		
    		table = createFooter(m_params, format, courier, items);
    		if (table != null)
    			document.add(table);
    		document.close();
        } catch (Exception e) {
            throw new AdempiereException(e);
        }
	}
    
    public static String getFont(String name){
    	
        return name;
    }
    
    static class Background extends PdfPageEventHelper {
        Font font;
        PdfTemplate t;
        Image total;
        
        protected PdfPTable table;
        protected float tableHeight;
        public Background() {
            
        }
     
        public float getTableHeight() {
            return tableHeight;
        }
        
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
                String fontDir = getFont(FONT);
                font =  new Font(BaseFont.createFont(fontDir, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            } catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
        
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
        	
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
        	table = new PdfPTable(3);
            try {
                int startX = 0;
                int totalw = 0;
            	for(int i = 0; i < widthTable.length; i++) {
            		totalw += (int) widthTable[i];
            	}
            	
            	startX = ((int)widthBase - totalw)/2;
            	
            	int col1 = totalw / 2;
            	int col2 = totalw - col1 - 2;
            	int col3 = 2;
            	table.setWidths(new int[]{col1, col2, col3});
    			table.setTotalWidth(totalw);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                String pattern = "HH:mm  dd-MM-yyyy ";
        		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        		Timestamp ts = new Timestamp(System.currentTimeMillis());
        		String date = simpleDateFormat.format(ts);
        		
                table.addCell(new Phrase("Printed: " + date, font));
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
                PdfPCell cell = new PdfPCell(total);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                table.writeSelectedRows(0, -1, startX, 30, canvas);
                canvas.endMarkedContentSequence();
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
        
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber()), font),
                2, 4, 0);
        }
    }
}