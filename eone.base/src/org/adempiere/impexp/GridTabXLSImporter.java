package org.adempiere.impexp;

import static org.compiere.model.SystemIDs.REFERENCE_DOCUMENTACTION;
import static org.compiere.model.SystemIDs.REFERENCE_PAYMENTRULE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.adempiere.base.IGridTabImporter;
import org.adempiere.exceptions.AdempiereException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MColumn;
import org.compiere.model.MQuery;
import org.compiere.model.MRefList;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.tools.FileUtil;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.IProcessUI;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.ValueNamePair;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.prefs.CsvPreference;

public class GridTabXLSImporter implements IGridTabImporter {
	private static final String ERROR_HEADER = "_ERROR_";
	private static final String LOG_HEADER = "_LOG_";
	private boolean m_isError = false;
	private String m_import_mode = null;
	private static final String IMPORT_MODE_MERGE = "M";
	private static final String IMPORT_MODE_UPDATE = "U";
	private static final String IMPORT_MODE_INSERT = "I";
	private static Language languageDefault = Env.getLanguage(Env.getCtx());
	private int BATCH_SIZE = 200;
	private int AD_User_ID;
	private Timestamp currentDate;
	int counter = 0;
	int SCALE = 10;
	List<String> lstHeader = new ArrayList<String>();
	
	private HashMap<GridField, Object> hm_GridFieldDefaultValue = new HashMap<GridField, Object>();
	private HashMap<Integer, String> hm_ForeignTable = new HashMap<Integer, String>();
	private ConcurrentHashMap<String, Boolean> mapTranslate = new ConcurrentHashMap<String, Boolean>();
	
	private List<List<Object>> m_tmpRow = null;
	private boolean isBaseLanguage = false;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(GridTabXLSImporter.class);
	
	public File fileImport(GridTab gridTab, List<GridTab> childs, InputStream filestream, Charset charset , String importMode) {
		long start = System.currentTimeMillis();
		Map<Integer, PO> mapTabPO = new HashMap<Integer, PO>();
		Map<Integer, Integer> mapTabParent = new HashMap<Integer, Integer>();
		String AD_Language = Env.getAD_Language(Env.getCtx());
		isBaseLanguage = Language.isBaseLanguage(AD_Language);
		//doan nay tim ra quan he cha con cua cac tab
		GridTab preTab = gridTab;
		GridTab preParentTab = gridTab;
		for(GridTab detail: childs){
			if(detail.getTabLevel() > preTab.getTabLevel()) {
				mapTabParent.put(detail.getAD_Tab_ID(), preTab.getAD_Tab_ID());
				preParentTab = preTab;
			} else {
				mapTabParent.put(detail.getAD_Tab_ID(), preParentTab.getAD_Tab_ID());
			}
			preTab = detail;
		}
		
		HSSFWorkbook  mapReader = null;
		HSSFSheet	  sheetReader = null;
		AD_User_ID = Env.getAD_User_ID(Env.getCtx());
		Date date = new Date();
		currentDate = new Timestamp(date.getTime());
		List<String> lstKey = new ArrayList<String>();
		
		long starttime = 0;
		//
		File errFile = null;
		File logFile = null;
		PrintWriter errFileW = null;
		PrintWriter logFileW = null;
		//
		CsvPreference csvpref = CsvPreference.STANDARD_PREFERENCE;
		String delimiter = String.valueOf((char) csvpref.getDelimiterChar());
		String quoteChar = String.valueOf((char) csvpref.getQuoteChar());
		//
		m_import_mode = importMode;
		PO masterRecord = null;
		PO parentRecord = null;
		if(!gridTab.isInsertRecord() && isInsertMode())
        	throw new AdempiereException("Insert record disabled for Tab");
		Map<String, Object> mapDefaultValue = new HashMap<String, Object>();
		LinkedHashMap<Integer, List<List<Object>>> mapValues = new LinkedHashMap<Integer, List<List<Object>>>();
		HashMap<Integer, Integer> mapSequence = new HashMap<Integer, Integer>();
		try {
			String errFileName = FileUtil.getTempMailName("Import_" + gridTab.getTableName(), "_err.csv");
			errFile = new File(errFileName);
			errFileW = new PrintWriter(errFile, charset.name());
			mapReader = new HSSFWorkbook(filestream);
			sheetReader = mapReader.getSheetAt(0);
			mapReader.close();
			if (sheetReader == null) {
				errFileW.close();
				throw new AdempiereException("Sheet cannot be empty!");
			}
			int maxRow = sheetReader.getLastRowNum();
			boolean isUseBatch = maxRow >= BATCH_SIZE ? true : false;
			HSSFRow rowHeader = sheetReader.getRow(0);	//Line 1
			List<String> header = getValuesAt(rowHeader);
			//
			List<CellProcessor> readProcArray = new ArrayList<CellProcessor>();
			Map<GridTab,Integer> tabMapIndexes = new HashMap<GridTab,Integer>();
			int indxDetail=0;
			//List<GridField> locationFields = null;
			boolean isThereKey   = false;
			//boolean isThereDocAction = false;
			String tableName = gridTab.getTableName();
			HashMap<Integer, PO> mapPO = new HashMap<Integer, PO>();
			//Mapping header  
			for(int idx = 0; idx < header.size(); idx++) {
				String headName = header.get(idx);
				if (headName==null) {
					errFileW.close();
					throw new AdempiereException("Header column cannot be empty, Col: " + (idx + 1));
				}
				
				if (headName.equals(ERROR_HEADER) || headName.equals(LOG_HEADER)){
					header.set(idx, null);
					readProcArray.add(null);
					continue;
				}
				if (headName.indexOf(">") > 0) {
					if(idx==0){
						errFileW.close();
					   throw new AdempiereException(Msg.getMsg(Env.getCtx(),"WrongHeader", new Object[] {headName}));
				    }else
					   break;
				    
				}else{
					boolean isKeyColumn = headName.indexOf("/") > 0 || headName.contains(tableName + "_ID");
					boolean isForeing 	= headName.indexOf("[") > 0 && headName.indexOf("]")>0;
					String  columnName  = getColumnName (isKeyColumn,isForeing,false,headName);
					GridField field 	= gridTab.getField(columnName);
					
					if (field == null) {
						errFileW.close();
						throw new AdempiereException(Msg.getMsg(Env.getCtx(), "FieldNotFound" , new Object[] {columnName}) );
					}
					else if(isKeyColumn && !isThereKey)
						isThereKey =true;
					
					readProcArray.add(getProccesorFromColumn(field)); 
					indxDetail++;
			    }
			}
			
			mapPO.clear();
			//TODO: Sau doan code nay lay ve het column duoc khong?
			
			if(isUpdateOrMergeMode() && !isThereKey)
			    throw new AdempiereException(gridTab.getTableName()+": "+Msg.getMsg(Env.getCtx(), "NoKeyFound"));
			
			tabMapIndexes.put(gridTab,indxDetail-1);
			String  childTableName   = null;
			isThereKey = false;
			//locationFields = null;
			GridTab currentDetailTab = null;
			//Mapping details 
			List<Integer> lstTabAdded = new ArrayList<Integer>();
		    for(int idx = indxDetail; idx < header.size(); idx++) {	
		    	String detailName = header.get(idx);
		    	if(detailName!=null && detailName.indexOf(">") > 0){
		    	   childTableName = detailName.substring(0,detailName.indexOf(">"));  
		    	   if (currentDetailTab==null || 
		    		  (currentDetailTab!=null && !childTableName.equals(currentDetailTab.getTableName()))){
		    		   
		    		   if(currentDetailTab!=null){ 
		    			 //check out key per Tab   
		   		    	 if(isUpdateOrMergeMode() && !isThereKey){ 
		   		    		errFileW.close();
		 				    throw new AdempiereException(currentDetailTab.getTableName()+": "+Msg.getMsg(Env.getCtx(), "NoKeyFound"));
		   		    	 }else{
		   		    	    tabMapIndexes.put(currentDetailTab,idx-1);
			    			isThereKey =false; 
		   		    	 } 
		    		   }
		    		   
		    		   for(GridTab detail: childs){
						   if(detail.getTableName().equals(childTableName)){
							  if(!lstTabAdded.contains(detail.getAD_Tab_ID())) {
								  currentDetailTab = detail;
								  lstTabAdded.add(detail.getAD_Tab_ID());
								  break; 
							  }
						   }
					   } 
		    	   }
		    	   
				   if(currentDetailTab == null) {
					  errFileW.close(); 
					  throw new AdempiereException(Msg.getMsg(Env.getCtx(),"NoChildTab",new Object[] {childTableName}));
				   }
		    	   
				   String columnName = detailName;
				   boolean isKeyColumn= columnName.indexOf("/") > 0 || columnName.contains(childTableName + "_ID");
				   boolean isForeing  = columnName.indexOf("[") > 0 && columnName.indexOf("]")>0;
				   columnName = getColumnName(isKeyColumn,isForeing,true,columnName);
				   GridField field = currentDetailTab.getField(columnName);
				  
				   if(field == null) {
					  errFileW.close();
					  throw new AdempiereException(Msg.getMsg(Env.getCtx(), "FieldNotFound",new Object[] {detailName}));
				   }
				   else if(isKeyColumn && !isThereKey)
					  isThereKey =true;
				
				   readProcArray.add(getProccesorFromColumn(field));  
//				   }				   
		    	}else {
		    	   errFileW.close();
		    	   throw new AdempiereException(Msg.getMsg(Env.getCtx(),"WrongDetailName",new Object[] {" col("+idx+") ",detailName}));
		    	}		    	
		    }
		    
		    //TODO: Sau doan code nay lay ve column luon duoc khong???
		    
		    if(currentDetailTab!=null){
		    	if(isUpdateOrMergeMode() && !isThereKey)
				   throw new AdempiereException(currentDetailTab.getTableName()+": "+Msg.getMsg(Env.getCtx(), "NoKeyFound"));

			    tabMapIndexes.put(currentDetailTab,header.size()-1); 	   
		    }
	
		    TreeMap<GridTab,Integer> sortedtTabMapIndexes= null;
		    if (childs.size()>0 && !tabMapIndexes.isEmpty()){
		    	ValueComparator bvc =  new ValueComparator(tabMapIndexes);
		        sortedtTabMapIndexes = new TreeMap<GridTab,Integer>(bvc);
		        sortedtTabMapIndexes.putAll(tabMapIndexes);
		    }else{
		    	Map<GridTab,Integer> localMapIndexes = new HashMap<GridTab,Integer>();
		    	localMapIndexes.put(gridTab, header.size()-1);
		    	ValueComparator bvc =  new ValueComparator(localMapIndexes);
		        sortedtTabMapIndexes = new TreeMap<GridTab,Integer>(bvc);
		    	sortedtTabMapIndexes.putAll(localMapIndexes);
		    }
			 
			m_isError = false;
			// write the header
			String rawHeader = getUntokenizedRow(sheetReader.getRow(0));
			errFileW.write(rawHeader + delimiter + ERROR_HEADER + "\n");
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			List<String> rawData = new ArrayList<String>();
			m_tmpRow = new ArrayList<List<Object>>();
			// pre-process to check for errors
			int rownum = 1;//Line 2
			HashMap<Integer, MTable> mapTable = new HashMap<Integer, MTable>();
			starttime = System.currentTimeMillis();
			System.out.println("While true starts....");
			while (true) {
//				System.out.println(rownum);
				Map<String, Object> map = null;
				boolean isLineError = false; 
				StringBuilder errMsg = new StringBuilder();
				try {
					map = readTableDetail(sheetReader, rownum, header, readProcArray);
				} catch (SuperCsvCellProcessorException e) {
					int idx = e.getCsvContext().getColumnNumber() - 1;
					errMsg.append(header.get(idx)).append(": ").append(e.getMessage());
					isLineError = true;
				}
				String rawLine = getUntokenizedRow(sheetReader.getRow(rownum++));
				if (! isLineError) {
					if(map == null)
					   break;
					
					//Re-order information coming from map
					List<Object> tmpRow = getOrderedRowFromMap(header,map);	  		
					m_tmpRow.add(tmpRow);
					//read master and detail
					int initIndx= 0;
					for(Map.Entry<GridTab, Integer> tabIndex : sortedtTabMapIndexes.entrySet()) {
						GridTab tmpGrid = tabIndex.getKey();			
						int endindx = tabIndex.getValue();
						StringBuilder lineError = preprocessRow (tmpGrid,header,tmpRow,initIndx,endindx);
						if( lineError!= null && lineError.length() > 0 ){
							isLineError = true;
							if (errMsg.length() > 0)
								errMsg.append(" / ");
							    errMsg.append(lineError);
						}
					    initIndx = endindx + 1;
					}
				}
				if (isLineError && ! m_isError)
					m_isError = true;
				if (!m_isError) {
					data.add(map);
					rawData.add(rawLine);
				}
				// write
				rawLine = rawLine + delimiter + quoteChar + errMsg.toString().replaceAll(quoteChar, "") + quoteChar + "\n";
				errFileW.write(rawLine);
			}
			
			System.out.println("While true Ends at: " + (System.currentTimeMillis() - starttime) + " ms");
			
			if (!m_isError) {
				String logFileName = FileUtil.getTempMailName("Import_" + gridTab.getTableName(), "_log.xlsx");
				logFile = new File(logFileName);
				logFileW = new PrintWriter(logFile, charset.name());
				// write the header
				logFileW.write(rawHeader + delimiter + LOG_HEADER + "\n");
				// no errors found - process header and then details 
				boolean isMasterok = true; 
			
				boolean error=false;
				Trx trx = null;
				String trxName= null;
				List<String>  rowsTmpResult = new ArrayList<String>();
				starttime = System.currentTimeMillis();
				System.out.println("FOR ROWS STARTS...");
				for (int idx = 0; idx < data.size(); idx++) {
					lstHeader.clear();
					String rawLine = rawData.get(idx);
					String logMsg = null;
					StringBuilder rowResult = new StringBuilder();
					GridTab currentGridTab=null;
					
					boolean isDetail=false;
					int currentColumn=0;
					error=false;
					
					if (rawLine.charAt(0)==','){
				    	isDetail=true;
						//check out if master row comes empty  
						Map<String, Object> rowMap = data.get(idx);
					    for(int i=0; i < indxDetail-1; i++){	
					    	if(rowMap.get(header.get(i))!=null){
					    	   isDetail=false;
					    	   break;
					    	}
					    }
					}

					if (!isMasterok && isDetail){
						 rawLine = rawLine + delimiter + quoteChar + Msg.getMsg(Env.getCtx(),"NotProccesed") + quoteChar + "\n";
						 rowsTmpResult.add(rawLine);
						 continue;
					}
					try {

						if(!isDetail){
							if(trx!=null){ 
							   if(error){
								   trx.rollback();
								   for(String row:rowsTmpResult){						   
									   logFileW.write(row);  
								   }
								   error =false;
							   }else { 
								   trx.commit();
								   for(String row:rowsTmpResult)
								   {
									   logFileW.write(row);
								   }
							   }
							   trx.close();
							   trx=null;
							}
							trxName = "Import_" + gridTab.getTableName() + "_" + UUID.randomUUID();
							gridTab.getTableModel().setImportingMode(false,trxName);
//							gridTab.getTableModel().setImportingMode(true,trxName);	
							trx = Trx.get(trxName,true);
							masterRecord = null;
							rowsTmpResult.clear();
							isMasterok = true;
						}
						
						for(Map.Entry<GridTab, Integer> tabIndex : sortedtTabMapIndexes.entrySet()) {
							currentGridTab = tabIndex.getKey();
							if(isDetail && gridTab.equals(currentGridTab)){
							   currentColumn=indxDetail;
							   continue;			
							}
							MTable curTable = null;
							if(mapTable.containsKey(currentGridTab.getAD_Table_ID())) {
								curTable = mapTable.get(currentGridTab.getAD_Table_ID());
							} else {
								curTable = new MTable(Env.getCtx(), currentGridTab.getAD_Table_ID(), null);
							}
							PO po2 = curTable.getPO(0, trxName);
							for (GridField gField : currentGridTab.getFields()) {
								Object value = null;
								Object defaultValue = null;
								if(hm_GridFieldDefaultValue.containsKey(gField))
								{
									defaultValue = hm_GridFieldDefaultValue.get(gField);
								}
								else
								{
									defaultValue = gField.getDefault();
									hm_GridFieldDefaultValue.put(gField, defaultValue); //TODO: Fuck!!!
								}
								//if (!gField.isVirtualColumn() && gField.getDefault() != null) {
								if (!gField.isVirtualColumn() && defaultValue != null) {
									if(mapDefaultValue.containsKey(currentGridTab.getAD_Tab_ID() + gField.getColumnName())) {
										value = mapDefaultValue.get(currentGridTab.getAD_Tab_ID() + gField.getColumnName());
									} else {
										//value = gField.getDefault();
										value = defaultValue;
										mapDefaultValue.put(currentGridTab.getAD_Tab_ID() + gField.getColumnName(), value);
									}
									po2.set_ValueOfColumn(gField.getColumnName(), value);
								}
							}
							
							//Assign master trx to its children
							if(!gridTab.equals(currentGridTab)){
								currentGridTab.getTableModel().setImportingMode(true,trxName);	
								isDetail=true;
							}
							
							int j = tabIndex.getValue();	
							logMsg = areValidKeysAndColumns(currentGridTab,data.get(idx),header,currentColumn,j,masterRecord,trx);
							
							if (logMsg == null){
								if (isInsertMode()){
								  if(!currentGridTab.getTableModel().isOpen())
								      currentGridTab.getTableModel().open(0);					
								  //how to read from status since the warning is coming empty ?
								  //if(isUseBatch)
									//  currentGridTab.setIsImport(true);
//								  if (!currentGridTab.dataNew(false)){ //TODO: Lau chu yeu o ham nay
								  if(!currentGridTab.dataNew(false)) {//dataNewForImporter //TODO VT
									  logMsg = "["+currentGridTab.getName()+"]"+"- Was not able to create a new record!"; //TODO: Xem co bo duoc khong?
								  }else{
									  //currentGridTab.navigateCurrent(); //TODO: Goi cai nay lam gi?
								  } //TODO: comment lai, bo qua ham dataNew xem
								} 
								//if(isUseBatch)
								//	currentGridTab.setIsImport(true);
								
								//Set Import = true
								//currentGridTab.setIsImport(true);
								
								if(logMsg==null) {
									if(mapTabParent.containsKey(currentGridTab.getAD_Tab_ID())) {
										parentRecord = mapTabPO.get(mapTabParent.get(currentGridTab.getAD_Tab_ID()));
									}
									logMsg = proccessRow(currentGridTab,header,data.get(idx),currentColumn,j,parentRecord,trx);

								}
								copyGridTabToPO(currentGridTab, po2);
								
								currentColumn = j + 1;
								if(!(logMsg == null)){
								   m_import_mode =importMode;   
							 	   //Ignore row since there is no data 
								   if("NO_DATA_TO_IMPORT".equals(logMsg)){
									  logMsg ="";
									  if(mapPO.containsKey(currentGridTab.getTabLevel()))
										  parentRecord = mapPO.get(currentGridTab.getTabLevel());
//									  preTabLevel = currentGridTab.getTabLevel();
									  continue;
								   }else 
									  error =true;
								}
							}else {
								error =true;
								currentColumn = j + 1;
							}
							if (! error) {
								PO po;
								if(isUseBatch) {
									List<List<Object>> values = null;
									if(mapValues.containsKey(currentGridTab.getAD_Table_ID())) {
										values = mapValues.get(currentGridTab.getAD_Table_ID());
									} else {
										values = new ArrayList<List<Object>>();
									}
									int sequence = -1;
									if(mapSequence.containsKey(currentGridTab.getAD_Table_ID())) {
										sequence = mapSequence.get(currentGridTab.getAD_Table_ID());
										sequence++;
										mapSequence.remove(currentGridTab.getAD_Table_ID());
										mapSequence.put(currentGridTab.getAD_Table_ID(), sequence);
									} else {
										sequence = DB.getNextID(Env.getAD_Client_ID(Env.getCtx()), currentGridTab.getTableName(), null);
										
										mapSequence.put(currentGridTab.getAD_Table_ID(), sequence);
									}
									if(po2.get_ColumnIndex("CreatedBy") >= 0)
										po2.set_ValueNoCheck("CreatedBy", AD_User_ID);
									if(po2.get_ColumnIndex("UpdatedBy") >= 0)
										po2.set_ValueNoCheck("UpdatedBy", AD_User_ID);
									if(po2.get_ColumnIndex("Updated") >= 0)
										po2.set_ValueNoCheck("Updated", currentDate);
									if(po2.get_ColumnIndex("Created") >= 0)
										po2.set_ValueNoCheck("Created", currentDate);
									if(po2.get_ColumnIndex("Processed") >= 0)
										po2.set_ValueNoCheck("Processed", false);
									if(po2.get_ColumnIndex("Posted") >= 0)
										po2.set_ValueNoCheck("Posted", false);
									if(po2.get_ColumnIndex("docstatus") >= 0)
										po2.set_ValueNoCheck("docstatus", "DR");
									if(po2.get_ColumnIndex("docaction") >= 0)
										po2.set_ValueNoCheck("docaction", "CO");
									List<Object> lstParam = PO.getBatchValueList(po2, currentGridTab.getAD_Table_ID(), trxName, sequence);
									values.add(lstParam);
									if(!mapValues.containsKey(currentGridTab.getAD_Table_ID())) {
										mapValues.put(currentGridTab.getAD_Table_ID(), values);
									}
									if(values.size() >= BATCH_SIZE) {
										for(Entry<Integer, List<List<Object>>> entry : mapValues.entrySet()) {
											String sqlInsert = PO.getSqlInsert(entry.getKey(), null);
											if(entry.getValue().size() > 0) {
												String err = DB.excuteBatch(sqlInsert, entry.getValue(), null);
												if(err != null) {
													try {
														DB.rollback(false, null);
													} catch(Exception e) {
													}
												}
											}
										}
										mapValues.clear();
									}
									po2.set_ValueNoCheck(currentGridTab.getKeyColumnName(), sequence);
									String key = currentGridTab.getAD_Table_ID() + "_" + sequence;
									lstKey.add(key);
									//PO.putCachePO(po2, key);
									currentGridTab.setValue(currentGridTab.getKeyColumnName(), po2.get_Value(currentGridTab.getKeyColumnName())); //TODO: Code cu
									//currentGridTab.setValueForImport(currentGridTab.getKeyColumnName(), po2.get_Value(currentGridTab.getKeyColumnName()));
									po = po2;
								} else {
									po = currentGridTab.dataSavePO(false); //TODO: Ham cu cua idempiere
									//po = currentGridTab.dataSavePOForImport(false);
									po2 = po;
								}
								if (po != null && po.get_ValueAsInt(currentGridTab.getKeyColumnName()) > 0){
									if(currentGridTab.equals(gridTab)) {
										masterRecord = po;
										mapPO.clear();
									} else {
										if(mapPO.containsKey(currentGridTab.getTabLevel()))
											mapPO.remove(currentGridTab.getTabLevel());
										mapPO.put(currentGridTab.getTabLevel(), po);
									}
									parentRecord = po;	
//									preTabLevel = currentGridTab.getTabLevel();
									if(mapTabPO.containsKey(currentGridTab.getAD_Tab_ID())) {
										mapTabPO.remove(currentGridTab.getAD_Tab_ID());
										mapTabPO.put(currentGridTab.getAD_Tab_ID(), po);
									} else {
										mapTabPO.put(currentGridTab.getAD_Tab_ID(), po);
									}
									if(isInsertMode()) {
									   logMsg = Msg.getMsg(Env.getCtx(), "Inserted")+" " + po.toString();
									} else {
									   logMsg = Msg.getMsg(Env.getCtx(), "Updated")+" "+ po.toString(); 
									   if(currentGridTab.equals(gridTab) && sortedtTabMapIndexes.size()>1)
										  currentGridTab.dataRefresh(true); 
									}
									
									if (trx != null)
									   trx.commit();
								} else {
									ValueNamePair ppE = CLogger.retrieveWarning();
									if (ppE==null)   
										ppE = CLogger.retrieveError();
									
									String info = null;
									
									if (ppE != null)
										info = ppE.getName();
									if (info == null)
										info = "";
									
									logMsg = Msg.getMsg(Env.getCtx(), "Error") + " " + Msg.getMsg(Env.getCtx(), "SaveError") + " (" + info + ")";
									currentGridTab.dataIgnore();

									if(currentGridTab.equals(gridTab) && masterRecord==null){
									   isMasterok = false;
									   rowResult.append("<"+currentGridTab.getTableName()+">: ");
									   rowResult.append(logMsg);
									   rowResult.append(" / ");
									   break;
								    }
									
									if(!currentGridTab.equals(gridTab) && masterRecord!=null){
										rowResult.append("<"+currentGridTab.getTableName()+">: ");
										rowResult.append(logMsg);
									    rowResult.append(" / ");
										break;
								    }
								}
								rowResult.append("<"+currentGridTab.getTableName()+">: ");
								rowResult.append(logMsg);
							    rowResult.append(" / ");
							} else {
								currentGridTab.dataIgnore();
								
								rowResult.append("<"+currentGridTab.getTableName()+">: ");
								rowResult.append(logMsg);
							    rowResult.append(" / ");
   
								//Master Failed, thus details cannot be imported 
								if(currentGridTab.equals(gridTab) && masterRecord==null){
								   isMasterok = false;
								   break;
								}
								
								if(!currentGridTab.equals(gridTab) && masterRecord!=null){
//								   isDetailok = false;
								   break;
								}
							}	
							m_import_mode = importMode;	
							
							//Set import ve false de reset lai, khong thi khong them moi duoc bang tay
							//currentGridTab.setIsImport(false);
						}
					} catch (Exception e) {
						rowResult.append("<"+currentGridTab.getTableName()+">: ");
						rowResult.append(Msg.getMsg(Env.getCtx(), "Error") + " " + e);
					    rowResult.append(" / ");
						currentGridTab.dataIgnore();
						
						error = true;
						//Master Failed, thus details cannot be imported 
						if(currentGridTab.equals(gridTab) && masterRecord==null)
						   isMasterok = false;
						
					} finally {						
					  m_import_mode =importMode; 
					}
					// write
					rawLine = rawLine + delimiter + quoteChar + rowResult.toString().replaceAll(delimiter, "") + quoteChar + "\n";
					rowsTmpResult.add(rawLine);
				}
									
				if(trx!=null){
				   if(error){
					  trx.rollback();
					  for(String row:rowsTmpResult){
					      logFileW.write(row);
					  }   
					}else{
						
						trx.commit();
						for(String row:rowsTmpResult)
						{
							logFileW.write(row);
						}
					}   
				   
				    if(masterRecord!=null){
				       gridTab.query(false); //TODO: PhucNV3: Goi cai nay lam gi??
				       gridTab.getTableModel().setImportingMode(false,null);
					   for(GridTab detail: childs)
						   if(detail.getTableModel().isOpen()){
							  detail.query(true);
							  detail.getTableModel().setImportingMode(false,null);	
						   }
				    }
				    trx.close();
					trx=null;	
				}
			}
			
			mapTable = null;
			
			for(Entry<Integer, List<List<Object>>> entry : mapValues.entrySet()) {
				String sqlInsert = PO.getSqlInsert(entry.getKey(), null);
				if(entry.getValue().size() > 0) {
					String err = DB.excuteBatch(sqlInsert, entry.getValue(), null);
					if(err != null) {
						try {
							DB.rollback(false, null);
						} catch(Exception e) {
						}
					}
				}
			}
			mapValues.clear();
			tabMapIndexes = null;
			
		} catch (IOException e) {
	      throw new AdempiereException(e);
		}
		finally {
			try {
				if (errFileW != null) {
					errFileW.flush();
					errFileW.close();
				}
				if (logFileW != null) {
					logFileW.flush();
					logFileW.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
//		PO.clearCachePO();
		/*
		for(String key : lstKey) {
			if(PO.getCachePO(key) != null)
				PO.removeCachePO(key);
		}
		*/
		lstKey.clear();
		mapDefaultValue = null;
		mapValues = null;
		mapTabPO = null;
		mapTabParent = null;
		
//		gridTab.query(false);
		gridTab.dataRefreshAll();
		System.out.print("END FUNCTIONS IN " + (System.currentTimeMillis() - start) + " ms");
		if (logFile != null)
			return logFile;
		else
			return errFile;
	}

	
	
	private boolean isInsertMode() 
	{
		return IMPORT_MODE_INSERT.equals(m_import_mode);
	}
	
	private boolean isUpdateMode() 
	{
		return IMPORT_MODE_UPDATE.equals(m_import_mode);
	}

	private boolean isMergeMode() 
	{
		return IMPORT_MODE_MERGE.equals(m_import_mode);
	}

	private boolean isUpdateOrMergeMode() 
	{
		return isUpdateMode() || isMergeMode();
	}

	private String getColumnName(boolean isKey ,boolean isForeing ,boolean isDetail , String headName)
	{		
		
		if(isKey){
			if (headName.lastIndexOf(">") >0) {
				headName = headName.substring(headName.lastIndexOf(">") +1, headName.length());	
			}
		}
		
		if(isForeing)
		   headName = headName.substring(0, headName.indexOf("["));		
		
        if(isDetail){
           if (headName.lastIndexOf(">")>0)
        	   headName = headName.substring(headName.lastIndexOf(">")+ 1,headName.length());
        }
        return headName;
	}
	

	private List<Object> getOrderedRowFromMap (List<String> header,Map<String, Object> map)
	{
		List<Object> tmpRow= new ArrayList<Object>();  
		for (int i = 0; i < header.size(); i++)
			tmpRow.add(null);
		
		for(Map.Entry<String, Object> record : map.entrySet()) {
			String Column =record.getKey();
			Object value  = record.getValue();
			int toIndx = -1;
			if(Column.startsWith("2"))
				Column = Column.substring(1);
			toIndx = header.indexOf(Column);
		    tmpRow.set(toIndx, value);
        }	
		return tmpRow;	
	}
	
	/**
	 * Validation data 
	 * @param gridTab
	 * @param header
	 * @param tmpRow
	 * @param startindx
	 * @param endindx
	 * @return
	 */
	private StringBuilder preprocessRow (GridTab gridTab,List<String> header,List<Object> tmpRow,int startindx,int endindx)
	{
		
	    boolean isEmptyRow = true;
	    StringBuilder  mandatoryColumns = new StringBuilder();
	    for(int i = startindx;  i < endindx +1; i++){
			String columnName = header.get(i);			
			Object value = tmpRow.get(i);
			if(value!=null)
			   isEmptyRow=false;
			
			if (log.isLoggable(Level.FINE)) log.fine("Setting " + columnName + " to " + value);

			boolean isKeyColumn = columnName.indexOf("/") > 0 || columnName.contains(gridTab.getTableName() + "_ID");
			boolean isForeing 	= columnName.indexOf("[") > 0 && columnName.indexOf("]")>0;
			boolean isDetail    = columnName.indexOf(">") > 0;
			columnName = getColumnName(isKeyColumn,isForeing,isDetail,columnName);
			String foreignColumn=null; 
			if(isForeing)
			   foreignColumn = header.get(i).substring(header.get(i).indexOf("[")+1, header.get(i).indexOf("]"));
		
			GridField field=gridTab.getField(columnName);
			if (field == null) 
				return new StringBuilder(Msg.getMsg(Env.getCtx(), "NotAWindowField" , new Object[] {header.get(i)}));

			if (field.isParentValue() || field.isKey())	
				continue;
			
			
			
			int AD_Column_ID = field.getAD_Column_ID();
			MColumn column = MColumn.get(Env.getCtx(), AD_Column_ID);
			
			if (isForeing && value != null && !"(null)".equals(value)){
				//String foreignTable = column.getReferenceTableName();
				String foreignTable = null;
				if(hm_ForeignTable != null && hm_ForeignTable.containsKey(AD_Column_ID))
				{
					foreignTable = hm_ForeignTable.get(AD_Column_ID);
				}
				else
				{
					foreignTable = column.getReferenceTableName();
					hm_ForeignTable.put(AD_Column_ID, foreignTable);
				}
				String idS = null;
				int id = -1;
				if("AD_Ref_List".equals(foreignTable)) {
				   idS= resolveForeignList(column,foreignColumn,value,null);
				} else {
				   id = resolveForeign(foreignTable,foreignColumn,value,null);					
				}

				
				if(idS == null && id < 0){	
				   //it could be that record still doesn't exist if import mode is inserting or merging   	
				   if(isUpdateMode())
				     return new StringBuilder(Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{header.get(i),value}));
				}
			}
		}
	    
		if(mandatoryColumns.length()>1 && !isEmptyRow) 
		   return new StringBuilder(Msg.getMsg(Env.getCtx(), "FillMandatory")+" "+mandatoryColumns);
		else
		   return null;		
	}
	
	private String proccessRow(GridTab gridTab,List<String> header, Map<String, Object> map,int startindx,int endindx,PO masterRecord,Trx trx)
	{

		String logMsg = null;
		boolean isThereRow = false;
		boolean checkline = false;
		for (int i = startindx; i < endindx + 1; i++) {
			Object value = map.get(header.get(i));
			if (value != null) {
				checkline = true;
			}
		}
		if (!checkline) {
			return "NO_DATA_TO_IMPORT";
		}
		List<String> parentColumns = new ArrayList<String>();
		
		for(int i = startindx ; i < endindx + 1 ; i++){
			String columnName = header.get(i);
			Object value = map.get(header.get(i));
			if(lstHeader.contains(columnName)) {
				value = map.get("2" + header.get(i));
			} else
				lstHeader.add(columnName);
			boolean isDetail = false;
			if ((header.get(i).indexOf(">") >0) && (header.get(i).indexOf("[") >0)) {
				String foreignColumn = header.get(i).substring(header.get(i).indexOf("[")+1,header.get(i).indexOf("]"));
				String masterColumnName = header.get(i).substring(header.get(i).indexOf(">")+1,header.get(i).indexOf("["));
				if (masterColumnName.equalsIgnoreCase(foreignColumn) && masterRecord != null) {
					value = masterRecord.get_Value(foreignColumn);
				}
			}

			if(value == null)
			   continue;
				
			if(columnName.endsWith("_ID") && "0".equals(value))
			   continue;
				
			boolean isKeyColumn= columnName.indexOf("/") > 0 || columnName.contains(gridTab.getTableName() + "_ID");
			boolean isForeing  = columnName.indexOf("[") > 0 && columnName.indexOf("]")>0;
			isDetail   = columnName.indexOf(">") > 0;
			columnName = getColumnName(isKeyColumn,isForeing,isDetail,columnName);
			String foreignColumn = null;
			Object setValue = null;
			
			if(isForeing) 
			   foreignColumn = header.get(i).substring(header.get(i).indexOf("[")+1,header.get(i).indexOf("]"));
				if(isKeyColumn && isUpdateMode())
				   continue;
				
				GridField field = gridTab.getField(columnName);
				if (field.isParentValue() || field.isKey()){
					
					if("(null)".equals(value.toString())){
					   logMsg = Msg.getMsg(Env.getCtx(),"NoParentDelete", new Object[] {header.get(i)}); 
					   break;
					}
					
					if(isForeing && masterRecord!=null){
						Object foreignValue = masterRecord.get_Value(foreignColumn);
						
					   if (foreignValue.equals(Integer.parseInt(value.toString()))){
						   logMsg = gridTab.setValue(field,Integer.parseInt(value.toString())); //TODO:: Code cu cua no
						   //logMsg = gridTab.setValueForImporter(field,masterRecord.get_ID());
//						   po.set_ValueNoCheck(field.getColumnName(), masterRecord.get_ID());
						   if(logMsg.equals(""))
							  logMsg= null;
						   else break;
					   }else{
						   if(value!=null){					      
						      logMsg = header.get(i)+" - "+Msg.getMsg(Env.getCtx(),"DiffParentValue", new Object[] {masterRecord.get_Value(foreignColumn).toString(),value});
						      break;
						   }   
					   }
					}else if (masterRecord==null && isDetail){
						
						MColumn column = MColumn.get(Env.getCtx(),field.getAD_Column_ID());
						String foreignTable = column.getReferenceTableName();
						String idS = null;
						int id = -1;
						
						if ("AD_Ref_List".equals(foreignTable)) 
							idS= resolveForeignList(column, foreignColumn, value,trx);
						else 
							id = resolveForeign(foreignTable,foreignColumn,value,trx);
						
						if(idS == null && id < 0)	
						   return Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{header.get(i),value});
						
						if(id >= 0) {
							logMsg = gridTab.setValue(field,id); //TODO: : Code cu cua iDempiere
							//logMsg = gridTab.setValueForImporter(field,id);
//							po.set_ValueNoCheck(field.getColumnName(), id);
						}
						else if (idS != null) {
							logMsg = gridTab.setValue(field,idS); //TODO: : Code cu cua iDempiere
							//logMsg = gridTab.setValueForImporter(field,idS);
//							po.set_ValueNoCheck(field.getColumnName(), idS);
						}
						if(logMsg !=null && logMsg.equals(""))
						   logMsg = null;
						else break;
					}
					parentColumns.add(columnName);	
					continue;
				}
				
				if (field.getColumnName().equalsIgnoreCase("CreatedBy") 
				 || field.getColumnName().equalsIgnoreCase("UpdatedBy")
				 || field.getColumnName().equalsIgnoreCase("Created")
				 || field.getColumnName().equalsIgnoreCase("Updated")) {
					continue;
				}
					
				//if(!isInsertMode() && !field.isEditable(true) && value!=null){
				if(!isInsertMode() && field.isVirtualColumn()) { //TODO: Check cot ao thoi, con lai cho tat, do phai check SQL
				  
					continue;
				}		
							
	
				
				if("(null)".equals(value.toString().trim())){
				    logMsg = gridTab.setValue(field,null); //TODO: Code cu cua idempiere
					//logMsg = gridTab.setValueForImporter(field,null);
//				   po.set_ValueNoCheck(field.getColumnName(), null);
				   
				   if(logMsg.equals(""))
					  logMsg= null;
				   else break;
				}else{
					
				   MColumn column = MColumn.get(Env.getCtx(),field.getAD_Column_ID());
				   if (isForeing){
						String foreignTable = column.getReferenceTableName();
						if ("AD_Ref_List".equals(foreignTable)) {
							String idS = resolveForeignList(column, foreignColumn, value,trx);
							if(idS == null)	
							   return Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{header.get(i),value});
							
							setValue = idS;
							isThereRow =true;
						} else {
							
							int id = resolveForeign(foreignTable, foreignColumn, value,trx);
							if(id < 0)	
							   return Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{header.get(i),value});
							if (field.getLookup() instanceof org.compiere.model.MLookup) {
							   org.compiere.model.MLookup mLookup = (org.compiere.model.MLookup)field.getLookup();
							   mLookup.refresh();
							   if (mLookup.getSize() >0) {
								   Object data = mLookup.getElementAt(mLookup.getSize()-1);
								   if (data instanceof org.compiere.util.KeyNamePair) {
									  int luID = ((org.compiere.util.KeyNamePair)data).getKey();
									  if (luID == -1) {
										  return Msg.getMsg(Env.getCtx(),"ForeignIsInvalid",new Object[]{header.get(i),value});
									  }
								   }
							   }
							}
							
							setValue = id;
							if (field.isParentValue() || field.isKey()) {
								int actualId = (Integer) field.getValue();
								if (actualId != id) {
									logMsg = Msg.getMsg(Env.getCtx(), "ParentCannotChange",new Object[]{header.get(i)});
									break;
								}
							}
							isThereRow =true;
						}
				   }else{
					   if(value != null) {
						  if(value instanceof java.util.Date)
							 value = new Timestamp(((java.util.Date)value).getTime());
							
						  if(DisplayType.Payment == field.getDisplayType()){
   							 String oldValue = value.toString(); 
							 for(ValueNamePair pList: MRefList.getList(Env.getCtx(),REFERENCE_PAYMENTRULE,false)){
								 if(pList.getName().equals(oldValue.toString())){
									oldValue = pList.getValue(); 
									break;
								 }
							 }
							 if(!value.toString().equals(oldValue)) 
							     value = oldValue;
							 else
								 return Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{header.get(i),value}); 
						  }else if(DisplayType.Button == field.getDisplayType()){
							 if(column.getAD_Reference_Value_ID()== REFERENCE_DOCUMENTACTION){
								String oldValue = value.toString(); 
							    for(ValueNamePair pList: MRefList.getList(Env.getCtx(),REFERENCE_DOCUMENTACTION,false)){
								    if(pList.getName().equals(oldValue.toString())){
									   oldValue = pList.getValue(); 
									   break;
									}
								}
								if(!value.toString().equals(oldValue)) 
									value = oldValue;
							    else
								    return Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{header.get(i),value});  
							 }else{
								 return Msg.getMsg(Env.getCtx(),"Invalid") + " Column ["+column.getColumnName()+"]";   
							 } 
						  }  
						  setValue = value;
						  isThereRow =true;
					   }
				   }
					
				   if(setValue != null) {
					   logMsg = gridTab.setValue(field,setValue); //TODO: : Code cu
					   //logMsg = gridTab.setValueForImporter(field,setValue);
//					   po.set_ValueNoCheck(field.getColumnName(), setValue);
				   }
				   
				   if(logMsg!=null && logMsg.equals(""))
					  logMsg= null;	
				   else break;
//			   }
			}	
		}
	    
		boolean checkParentKey = parentColumns.size()!=gridTab.getParentColumnNames().size();
		if(isThereRow && logMsg==null && masterRecord!=null && checkParentKey){
			for(String linkColumn : gridTab.getParentColumnNames()){
				String columnName = linkColumn;
				Object setValue   = masterRecord.get_Value(linkColumn);
		        //resolve missing key 
				if(setValue==null){
			       columnName = null;
		           for(int j = startindx;j < endindx + 1;j++){
		        	   if(header.get(j).contains(linkColumn)){
		        		   columnName = header.get(j);
		        		   setValue   = map.get(columnName);
		        		   break;
		        	   }
		           }
		           if( columnName!=null ){
					   String foreignColumn = null;						
					   boolean isForeing = columnName.indexOf("[") > 0 && columnName.indexOf("]")>0;
					   if(isForeing) 
						  foreignColumn  = columnName.substring(columnName.indexOf("[")+1,columnName.indexOf("]"));   
			           
					   columnName = getColumnName(false,isForeing,true,columnName);
					   
					   MColumn column = MColumn.get(Env.getCtx(),gridTab.getField(columnName).getAD_Column_ID());
					   if (isForeing){
							String foreignTable = column.getReferenceTableName();
							if ("AD_Ref_List".equals(foreignTable)) {
								String idS = resolveForeignList(column,foreignColumn,setValue,trx);
								if(idS == null)	
								   return Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{columnName,setValue});
								
								setValue = idS;
							} else {
								int id = resolveForeign(foreignTable, foreignColumn, setValue,trx);
								if(id < 0)	
								   return Msg.getMsg(Env.getCtx(),"ForeignNotResolved",new Object[]{columnName,setValue});
								
								setValue = id;
							}
					   }	   
		           }else{ 
		    	       logMsg = "Key: "+linkColumn+" "+ Msg.getMsg(Env.getCtx(),"NotFound"); 
		    	       break; 
		           }
			    }
				logMsg = gridTab.setValue(linkColumn,setValue); //TODO: Code cu cua iDempiere
				//logMsg = gridTab.setValueForImport(linkColumn,setValue);
//				po.set_ValueNoCheck(linkColumn, setValue);
			    if(logMsg.equals(""))
			       logMsg= null;
			    else continue;
		   }
		}
		
		if(logMsg == null && !isThereRow)
		   logMsg ="NO_DATA_TO_IMPORT";
		
		return logMsg;
	}
	
	private CellProcessor getProccesorFromColumn(GridField field)
	{	
		if (DisplayType.isDate(field.getDisplayType())) {
			String format = field.getFormatPattern();
			if (format == null) {
				MColumn column = MColumn.get(Env.getCtx(), field.getAD_Column_ID());
				
				format = column.getFormatPattern();
				if (format == null || format.length() ==0) {
					format = languageDefault.getDateFormat().toPattern();
				}
			}
			return (new CellProcessor(field.getDisplayType()));
		} else if (DisplayType.Integer == field.getDisplayType()
				|| DisplayType.ID == field.getDisplayType()) {
			return (new CellProcessor(field.getDisplayType()));
		} else if (DisplayType.isNumeric(field.getDisplayType())) {
			String format = field.getFormatPattern();
			if (format == null) {
				MColumn column = MColumn.get(Env.getCtx(), field.getAD_Column_ID());
				
				format = column.getFormatPattern();
			}
			return (new CellProcessor(field.getDisplayType()));
		} else {
			String format = field.getFormatPattern();
			if (format == null) {
				MColumn column = MColumn.get(Env.getCtx(), field.getAD_Column_ID());
				
				format = column.getFormatPattern();
			}
			return (new CellProcessor(field.getDisplayType()));
		}
		
	}
	
	private String areValidKeysAndColumns(GridTab gridTab, Map<String, Object> map,List<String> header,int startindx,int endindx,PO masterRecord,Trx trx)
	{
		MQuery pquery = new MQuery(gridTab.getAD_Table_ID());
		String logMsg= null;
		Object tmpValue=null;
		String columnwithKey=null;
		Object setValue = null;
		List<String> parentColumns = new ArrayList<String>(); 
		//Process columnKeys + Foreign to add restrictions.
		for (int i = startindx ; i < endindx + 1 ; i++){					  
		    boolean isKeyColumn = header.get(i).indexOf("/") > 0 && header.get(i).endsWith("K") || header.get(i).contains(gridTab.getTableName() + "_ID");	
		    if (isKeyColumn) {
			   boolean isForeing = header.get(i).indexOf("[") > 0 && header.get(i).indexOf("]")>0;
			   boolean isDetail  = header.get(i).indexOf(">") > 0;
			   columnwithKey = getColumnName(isKeyColumn,isForeing,isDetail,header.get(i));
			   
			   if(map.get(header.get(i)) instanceof java.util.Date) {
				  tmpValue = new Timestamp(((java.util.Date)map.get(header.get(i))).getTime());
			   } else {
//				   tmpValue = map.get(header.get(i));
				   tmpValue = null;
			   }
				
			   if (tmpValue==null)
				   continue;
			   
			   GridField field = gridTab.getField(columnwithKey);
			   MColumn column  = MColumn.get(Env.getCtx(), field.getAD_Column_ID());
			   
			   if(field.isParentValue() || field.isKey()){
				  parentColumns.add(column.getColumnName());
			   }
			   String foreignColumn = null;		   
			   if(isForeing){
				  foreignColumn  = header.get(i).substring(header.get(i).indexOf("[")+1,header.get(i).indexOf("]"));
				  String foreignTable = column.getReferenceTableName();
				  if ("AD_Ref_List".equals(foreignTable)) {
					  String idS = resolveForeignList(column, foreignColumn, tmpValue,trx);
					  setValue = idS;
				  }else {
					  int id = resolveForeign(foreignTable, foreignColumn, tmpValue,trx);
					  setValue = id;
	             }
			   }else{
				   setValue = tmpValue ;
			   }
			   pquery.addRestriction(columnwithKey,MQuery.EQUAL,setValue);
		   }
		}
		
		if (pquery.getRestrictionCount() > 0){
			//check out if parent keys were completed properly 
			if (gridTab.isDetail()){
				for(String linkColumn : gridTab.getParentColumnNames()){
					if(!pquery.getWhereClause().contains(linkColumn)){
						Object value = masterRecord!=null?masterRecord.get_Value(linkColumn):null;
						//resolve key
						if(value==null){
						   String columnName = null;
				           for(int j = startindx;j<endindx + 1;j++){
				        	   if(header.get(j).contains(linkColumn)){
				        		   columnName = header.get(j);
				        		   value = map.get(header.get(j));
				        		   break;
				        	   }
				           }
				           if(columnName!=null){
				        	   boolean isForeing = columnName.indexOf("[") > 0 && columnName.indexOf("]")>0;
							   columnwithKey     = getColumnName(false,isForeing,true,columnName);
							   MColumn column    = MColumn.get(Env.getCtx(),gridTab.getField(columnwithKey).getAD_Column_ID());
							   
							   String foreignColumn = null;		   
							   if(isForeing){
								  foreignColumn       = columnName.substring(columnName.indexOf("[")+1,columnName.indexOf("]"));
								  String foreignTable = column.getReferenceTableName();
								  if ("AD_Ref_List".equals(foreignTable)) {
									  String idS = resolveForeignList(column,foreignColumn,value,trx);
									  value = idS;
								  }else {
									  int id = resolveForeign(foreignTable,foreignColumn,value,trx);
									  value = id;
					             }
							   }
				           }else{ //mandatory key not found 
				    	       return Msg.getMsg(Env.getCtx(),"FillMandatory")+" "+linkColumn;   
				           }
					    }
						if(value!=null)
						   pquery.addRestriction(linkColumn,MQuery.EQUAL,value);  	
					}
				}	
			}
			gridTab.getTableModel().dataRequery(pquery.getWhereClause(), false, 0, false);
	    	if (isInsertMode()){
				if(gridTab.getTableModel().getRowCount()>=1)
					m_import_mode = IMPORT_MODE_UPDATE;
				   //logMsg = Msg.getMsg(Env.getCtx(), "AlreadyExists")+" "+pquery;
				else  
				  return null;	
			}
			if (isUpdateMode()){
				if(gridTab.getTableModel().getRowCount()==1){
			       gridTab.navigateCurrent();
				   return null;
				}
				else if(gridTab.getTableModel().getRowCount()<=0)
				   logMsg = Msg.getMsg(Env.getCtx(), "not.found")+" "+pquery; 
				else if(gridTab.getTableModel().getRowCount()>1)
			       logMsg = Msg.getMsg(Env.getCtx(),"TooManyRows")+" "+pquery; 
			}
		    if (isMergeMode()){
			   if(gridTab.getTableModel().getRowCount()==1){
			      gridTab.navigateCurrent();
				  m_import_mode = IMPORT_MODE_UPDATE;
			   }else if(gridTab.getTableModel().getRowCount()<=0)
				  m_import_mode = IMPORT_MODE_INSERT;
			   else if(gridTab.getTableModel().getRowCount()>1)
				  logMsg = Msg.getMsg(Env.getCtx(),"TooManyRows")+" "+pquery; 	
		   }
	   }
		
	   return logMsg;
	}
	
	private String resolveForeignList(MColumn column, String foreignColumn, Object value ,Trx trx) 
	{
		String idS = null;
		String trxName = (trx!=null?trx.getTrxName():null); 
		if (value == null) 
			return null;
		StringBuilder select = new StringBuilder();
		if (isBaseLanguage) {
			select = new StringBuilder("SELECT Value FROM AD_Ref_List WHERE ")
				.append(foreignColumn).append("=? AND AD_Reference_ID=? AND IsActive='Y'");
		} else {
			select = new StringBuilder("SELECT b.Value FROM AD_Ref_List_Trl a ")
				.append(" Inner Join AD_Ref_List b on a.AD_Ref_List_ID = b.AD_Ref_List_ID ")
				.append(" WHERE a.")
				.append(foreignColumn).append("=? AND b.AD_Reference_ID=? AND a.IsActive='Y'");
		}
		idS = DB.getSQLValueStringEx(trxName, select.toString(), value, column.getAD_Reference_Value_ID());
		return idS;
	}

	private int resolveForeign(String foreignTable, String foreignColumn, Object value,Trx trx) 
	{		
		int id = -1;
		if(value == null)
			return id;
		String trxName = (trx!=null?trx.getTrxName():null);
		StringBuilder select = new StringBuilder("");	
		if (!isBaseLanguage && isTranslateColumn(foreignTable, foreignColumn)) {
			select = new StringBuilder("SELECT ")	
				.append(foreignTable).append("_ID FROM ")
				.append(foreignTable).append("_Trl").append(" WHERE ")
				.append(" UPPER(TRIM(").append(foreignColumn).append(")) = ? AND IsActive='Y' AND AD_Client_ID=?");
			id = DB.getSQLValueEx(trxName, select.toString(), value.toString().trim().toUpperCase(), Env.getAD_Client_ID(Env.getCtx()));
		} else {
			select = new StringBuilder("SELECT ")	
				.append(foreignTable).append("_ID FROM ")
				.append(foreignTable).append(" WHERE ")
				.append(" UPPER(TRIM(").append(foreignColumn).append(")) = ? AND IsActive='Y' AND AD_Client_ID=?");
			id = DB.getSQLValueEx(trxName, select.toString(), value.toString().trim().toUpperCase(), Env.getAD_Client_ID(Env.getCtx()));
		}
		
		if (id == -1 && !"AD_Client".equals(foreignTable)) {
			id = DB.getSQLValueEx(trxName, select.toString(), value.toString().trim().toUpperCase(), 0);
		} 
		return id;
	}

	private boolean isTranslateColumn(String tableName, String columnName) {
		if (mapTranslate.containsKey(tableName + "_" + columnName)) {
			return mapTranslate.get(tableName + "_" + columnName);
		} else {
			String sql = "SELECT cd.IsTranslated "
					+ " FROM AD_Table rt"
					+ " INNER JOIN AD_Column cd ON (rt.AD_Table_ID=cd.AD_Table_ID)  "
					+ "WHERE rt.TABLENAME=?"
					+ " AND rt.IsActive='Y' AND cd.Name = ?";
			boolean IsTranslated = false;

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setString(1, tableName);
				pstmt.setString(2, columnName);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					IsTranslated = rs.getString(1).equals("Y");
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
			mapTranslate.put(tableName + "_" + columnName, IsTranslated);
			return IsTranslated;
		}
	}
	
	@Override
	public String getFileExtension() 
	{
		return "xls";
	}

	@Override
	public String getFileExtensionLabel() 
	{
		return Msg.getMsg(Env.getCtx(), "IFileXLS");
	}

	@Override
	public String getContentType() 
	{
		return "application/xls";
	}

	@Override
	public String getSuggestedFileName(GridTab gridTab) 
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dt = sdf.format(cal.getTime());
		String localFile = "Import_" + gridTab.getTableName() + "_" + dt
				+ (m_isError ? "_err" : "_log")
				+ ".xls" ;//getFileExtension();
		return localFile;
	}
	
    static class ValueComparator implements Comparator<GridTab> 
    {
    	Map<GridTab,Integer> base;
		public ValueComparator(Map<GridTab,Integer> base) {
		    this.base = base;
		}
		// Note: this comparator imposes orderings that are inconsistent with equals.    
		public int compare(GridTab a, GridTab b) {
		    if(base.get(a) >= base.get(b))
		       return 1;
		    else
		       return -1;
		}
    }
     
    private List<String> getValuesAt (HSSFRow row)
    {
    	List<String> values = new ArrayList<String>();
    	Iterator<Cell> cellIterator = row.cellIterator();
    	
    	while (cellIterator.hasNext()) {
    		Cell cell = cellIterator.next();
    		values.add(cell.getStringCellValue());
    	}
    	
    	return values;
    }
    
    @SuppressWarnings("deprecation")
	private Map<String, Object> readTableDetail(HSSFSheet sheet, int rownum, List<String> header, List<CellProcessor> processors)
    {
    	if (rownum < sheet.getFirstRowNum() || rownum > sheet.getLastRowNum())
    		return null;
    	Map<String, Object> mapRow = new HashMap<String, Object>();
    	HSSFRow row = sheet.getRow(rownum);
    	int col = 0;
    	List<String> lstHeaderName = new ArrayList<String>();
    	if(row != null) {
    		for(int cn=0; cn<row.getLastCellNum(); cn++) {
     	       Cell cell = row.getCell(cn, MissingCellPolicy.CREATE_NULL_AS_BLANK);    	       
     	       String headerMap = header.get(col);
     	       if(lstHeaderName.contains(headerMap)) {
     	    	  headerMap ="2" + headerMap;
     	    	 lstHeaderName.add(headerMap);
     	       } else {
     	    	   lstHeaderName.add(headerMap);
     	       }
 	       		int displayType = processors.get(col++).getDisplayType();
 	       		if(cell.getCellType() == CellType.BLANK) {
 	       			mapRow.put(headerMap, null);
 	       		} else if (DisplayType.isDate(displayType)) {	       			
 	       			try{
 	       				String tmp = cell.getStringCellValue();
 	       				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
 	       			    Date parsedTimeStamp = dateFormat.parse(tmp);
 	       			    Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
 	       				mapRow.put(headerMap, timestamp);	       					
 	       			} catch (Exception e) {
 	       				mapRow.put(headerMap, cell.getDateCellValue());
 	       			}
 	       		} else if (DisplayType.Integer == displayType
 	       				|| DisplayType.ID == displayType
 	       				//|| DisplayType.TableDir == displayType
 	       				//|| DisplayType.Table == displayType
 	       				) {
 	       			try{
	 	       			String value = cell.getStringCellValue();
	 	       			if(value != null && value.toString().length()>0) {
	 	       				try
	 	       				{
	 	       					mapRow.put(headerMap, new Integer(value));
	 	       				}
	 	       				catch(Exception e)
	 	       				{
	 	       					log.saveError("Error", "Error while importing field " + headerMap
	 	       							+ " with value: " + value);
	 	       				}
	 	       			} else {
	 	       				mapRow.put(headerMap, null);
	 	       			}
 	       			} catch(Exception e) {
 	       				double value = cell.getNumericCellValue();
 	       				mapRow.put(headerMap, Double.valueOf(value).intValue());
 	       			}
 	       		} else if (DisplayType.isNumeric(displayType)) {
 	       				double value = cell.getNumericCellValue();
 	           			mapRow.put(headerMap, BigDecimal.valueOf(value).setScale(SCALE, BigDecimal.ROUND_HALF_UP));
 	       		} else if (DisplayType.YesNo == displayType) {
 	       			try{
 	       				String tmp = cell.getStringCellValue();
 	       				if(tmp != null && tmp.equals("Y"))
 	       					mapRow.put(headerMap, true);
 	       				else
 	       					mapRow.put(headerMap, false);
 	       			} catch(Exception e) {
 	       				mapRow.put(headerMap, false);
 	       			}
 	       		} else {
 	       			if(cell.getStringCellValue() != null && !cell.getStringCellValue().equals(""))
 	       				mapRow.put(headerMap, cell.getStringCellValue());
 	       		}
     	   }
    	} else
    		return null;
    	
    	return mapRow;
    }
    
    /**
     * 
     * @param row
     * @return
     */
    private String getUntokenizedRow (HSSFRow row)
    {
    	if (row == null)
    		return "";
    	StringBuilder untokenized = new StringBuilder();
    	for(int cn=0; cn<row.getLastCellNum(); cn++) {
    		
    		Cell cell = row.getCell(cn,  MissingCellPolicy.CREATE_NULL_AS_BLANK);
    		if (cell.getCellType() == CellType.BOOLEAN) {
    			untokenized.append(cell.getBooleanCellValue()).append(",");
    		}
    		else if (cell.getCellType() == CellType.NUMERIC) {
				untokenized.append(cell.getNumericCellValue()).append(",");		
    		}
    		else if (cell.getCellType() == CellType.STRING) {
				untokenized.append(cell.getStringCellValue()).append(",");
			} else {
				untokenized.append(",");
			}
			
    	}
    	return untokenized.toString();
    }
    
    private  class CellProcessor {
		private int m_displayType = -1;
		
		public CellProcessor(int type) {
			m_displayType = type;
		}
		
		public int getDisplayType() {
			return m_displayType;
		}
	}
    
    private void copyGridTabToPO(GridTab gridTab, PO po) {
    	if(po == null)
    		return;
    	GridField[] fields = gridTab.getFields();
    	for(GridField field : fields) {
    		if(field.getColumnName().contains("_UU")) {
				UUID uuid = UUID.randomUUID();
				po.set_ValueNoCheck(field.getColumnName(), uuid.toString());
			} else if(field.getValue() != null) {
				po.set_ValueNoCheck(field.getColumnName(), field.getValue());
    		}
    	}
    }
    

	@Override
	public File fileImport(GridTab gridTab, List<GridTab> childs, InputStream filestream, Charset charset,
			String importMode, IProcessUI processUI) {
		// TODO Auto-generated method stub
		return null;
	}
}
