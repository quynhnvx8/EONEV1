
package eone.webui.info;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.EmbedWinInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.compiere.util.ValueNamePair;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;
import org.zkoss.zul.Vbox;

import eone.base.impexp.AbstractExcelExporter;
import eone.base.model.AccessSqlParser;
import eone.base.model.AccessSqlParser.TableInfo;
import eone.exceptions.EONEException;
import eone.base.model.GridField;
import eone.base.model.GridFieldVO;
import eone.base.model.GridWindow;
import eone.base.model.Lookup;
import eone.base.model.MInfoColumn;
import eone.base.model.MInfoWindow;
import eone.base.model.MLookupFactory;
import eone.base.model.MLookupInfo;
import eone.base.model.MRole;
import eone.base.model.MTable;
import eone.base.model.X_AD_InfoColumn;
import eone.webui.AdempiereWebUI;
import eone.webui.ClientInfo;
import eone.webui.ISupportMask;
import eone.webui.LayoutUtils;
import eone.webui.apps.AEnv;
import eone.webui.component.Borderlayout;
import eone.webui.component.Button;
import eone.webui.component.Column;
import eone.webui.component.Columns;
import eone.webui.component.ConfirmPanel;
import eone.webui.component.EditorBox;
import eone.webui.component.Grid;
import eone.webui.component.GridFactory;
import eone.webui.component.Label;
import eone.webui.component.ListModelTable;
import eone.webui.component.Menupopup;
import eone.webui.component.Row;
import eone.webui.component.Rows;
import eone.webui.component.Tabbox;
import eone.webui.component.WInfoWindowListItemRenderer;
import eone.webui.component.WListbox;
import eone.webui.editor.WEditor;
import eone.webui.editor.WSearchEditor;
import eone.webui.editor.WTableDirEditor;
import eone.webui.editor.WebEditorFactory;
import eone.webui.event.DialogEvents;
import eone.webui.event.ValueChangeEvent;
import eone.webui.event.ValueChangeListener;
import eone.webui.event.WTableModelEvent;
import eone.webui.factory.ButtonFactory;
import eone.webui.grid.WQuickEntry;
import eone.webui.panel.InfoPanel;
import eone.webui.session.SessionManager;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.FDialog;


public class InfoWindow extends InfoPanel implements ValueChangeListener, EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5041961608373943362L;

	protected Grid parameterGrid;
	private Borderlayout layout;
	private Vbox southBody;
	/** List of WEditors            */
    protected List<WEditor> editors;
    protected List<WEditor> identifiers;
    protected Properties infoContext;

    /** embedded Panel **/
    Tabbox embeddedPane = new Tabbox();
    ArrayList <EmbedWinInfo> embeddedWinList = new ArrayList <EmbedWinInfo>();
    Map<Integer, RelatedInfoWindow> relatedMap = new HashMap<>();

	/** Max Length of Fields */
    public static final int FIELDLENGTH = 20;
    
    protected ColumnInfo[] columnInfos;
	protected TableInfo[] tableInfos;
	protected MInfoColumn[] infoColumns;	
	protected String queryValue;
	protected WQuickEntry vqe;
	
	private List<GridField> gridFields;
		
	private boolean hasEditable = false;
	private Map<Integer, List<Object>> cacheOriginalValues = new HashMap<>();
	private Map<Integer, List<Object>> temporarySelectedData = new HashMap<>(); 	
	private WInfoWindowListItemRenderer infoWindowListItemRenderer = null;
	
	
	private Button exportButton = null;
	
	/**
	 * Menu contail process menu item
	 */
	protected Menupopup ipMenu;
	private int noOfParameterColumn;
	/**
	 * @param WindowNo
	 * @param tableName
	 * @param keyColumn
	 * @param multipleSelection
	 * @param whereClause
	 */
	public InfoWindow(int WindowNo, String tableName, String keyColumn, String queryValue, 
			boolean multipleSelection, String whereClause, int AD_InfoWindow_ID) {
		this(WindowNo, tableName, keyColumn, queryValue, multipleSelection, whereClause, AD_InfoWindow_ID, true);
	}

	/**
	 * @param WindowNo
	 * @param tableName
	 * @param keyColumn
	 * @param multipleSelection
	 * @param whereClause
	 * @param lookup
	 */
	public InfoWindow(int WindowNo, String tableName, String keyColumn, String queryValue, 
			boolean multipleSelection, String whereClause, int AD_InfoWindow_ID, boolean lookup) {
		this(WindowNo, tableName, keyColumn, queryValue, multipleSelection, whereClause, AD_InfoWindow_ID, lookup, null);		
	}

	/**
	 * @param WindowNo
	 * @param tableName
	 * @param keyColumn
	 * @param multipleSelection
	 * @param whereClause
	 * @param lookup
	 * @param gridfield
	 */
	public InfoWindow(int WindowNo, String tableName, String keyColumn, String queryValue, 
			boolean multipleSelection, String whereClause, int AD_InfoWindow_ID, boolean lookup, GridField field) {
		super(WindowNo, tableName, keyColumn, multipleSelection, whereClause,
				lookup, AD_InfoWindow_ID);
		this.m_gridfield = field;
		this.queryValue = queryValue;

   		contentPanel.addActionListener(new EventListener<Event>() {
   			public void onEvent(Event event) throws Exception {
   				
   				int row = -1;
   				
   				if(event instanceof SelectEvent<?, ?>)
   				{
   					@SuppressWarnings("unchecked")
						SelectEvent<Listitem, List<Object>> selEvent = (SelectEvent<Listitem, List<Object>>)event;
   					
   					if(selEvent.getReference() != null)
   					{
   						row = selEvent.getReference().getIndex();
   					}
   				}
   				   				
   				updateSubcontent(row);
   			}
   		}); //xolali --end-

		infoContext = new Properties(Env.getCtx());
		p_loadedOK = loadInfoDefinition(); 
		
				
		if (loadedOK()) {
			if (isLookup()) {
				Env.clearTabContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO);
			}
			
			renderWindow();
			
			if (queryValue != null && queryValue.trim().length() > 0)
			{
				prepareTable();
			}			
		}
		
		if (ClientInfo.isMobile()) {
			ClientInfo.onClientInfo(this, this::onClientInfo);
		}
		
		// F3P: add export button
		
		initExport();
	}
	
	/** 
	 * 
	* {@inheritDoc}
	*/
	@Override
	protected void updateSubcontent (int row){ // F3P: For multi-selection info, using selected row blocks the dislay to the first selected
		
		if(row < 0)
			row = contentPanel.getSelectedRow();

		if (row >= 0) {
			for (EmbedWinInfo embed : embeddedWinList) {
				// default link column is key column
				int indexData = 0;
				if (columnDataIndex.containsKey(embed.getParentLinkColumnID())){
					// get index of link column
					indexData = p_layout.length + columnDataIndex.get(embed.getParentLinkColumnID());
				}
				RelatedInfoWindow relatedInfoWindow = relatedMap.get(embed.getInfowin().getAD_InfoWindow_ID());
				relatedInfoWindow.refresh(contentPanel.getValueAt(row,indexData));
			}// refresh for all
		}else{
			for (EmbedWinInfo embed : embeddedWinList) {
				reset(embed);
			}
		}
	}

	
	@Override
	protected void loadInfoWindowData (){
		if (m_infoWindowID > 0) {
			infoWindow = new MInfoWindow(Env.getCtx(), m_infoWindowID, null);
		}else {
			infoWindow = MInfoWindow.get(p_tableName, (String)null);			
		}
		
		if (infoWindow == null)
			return;
		if (!infoWindow.isValid()) {
			infoWindow = null;
		} else {
			String tableName = MTable.getTableName(Env.getCtx(), infoWindow.getAD_Table_ID());
			if (!tableName.equalsIgnoreCase(p_tableName)) {
				throw new IllegalArgumentException("AD_InfoWindow.TableName <> TableName argument. ("+tableName + " <> " + p_tableName+")");
			}
		}
	}
	
	protected boolean loadInfoDefinition() {
		if (infoWindow != null) {
			String tableName = null;
				tableName = MTable.getTableName(Env.getCtx(), infoWindow.getAD_Table_ID());
			
			AccessSqlParser sqlParser = new AccessSqlParser("SELECT * FROM " + infoWindow.getFromClause());
			tableInfos = sqlParser.getTableInfo(0);
			if (tableInfos[0].getSynonym() != null && tableInfos[0].getSynonym().trim().length() > 0) {
				p_tableName = tableInfos[0].getSynonym().trim();
				if (p_whereClause != null && p_whereClause.trim().length() > 0) {
					p_whereClause = p_whereClause.replace(tableName+".", p_tableName+".");
				}					
			}
			
			infoColumns = infoWindow.getInfoColumns(tableInfos);
		
			gridFields = new ArrayList<GridField>();
			
			for(MInfoColumn infoColumn : infoColumns) {
				if (infoColumn.isKey())
					keyColumnOfView = infoColumn;
				String columnName = infoColumn.getColumnName();
				/*!m_lookup && infoColumn.isMandatory():apply Mandatory only case open as window and only for criteria field*/
				boolean isMandatory = !m_lookup && infoColumn.isMandatory() && infoColumn.isQueryCriteria();
				GridFieldVO vo = GridFieldVO.createParameter(infoContext, p_WindowNo, AEnv.getADWindowID(p_WindowNo), infoWindow.getAD_InfoWindow_ID(), 0,
						columnName, infoColumn.get_Translation("Name"), infoColumn.getAD_Reference_ID(), 
						infoColumn.getAD_Reference_Value_ID(), isMandatory, false, infoColumn.get_Translation("Placeholder"));
				if (infoColumn.getAD_Val_Rule_ID() > 0) {
					vo.ValidationCode = infoColumn.getAD_Val_Rule().getCode();
					if (vo.lookupInfo != null) {
						vo.lookupInfo.ValidationCode = vo.ValidationCode;
						vo.lookupInfo.IsValidated = false;
					}
				}
				if (infoColumn.getDisplayLogic() != null)					
					vo.DisplayLogic =  infoColumn.getDisplayLogic();
				if (infoColumn.isQueryCriteria() && infoColumn.getDefaultValue() != null)
					vo.DefaultValue = infoColumn.getDefaultValue();
				String desc = infoColumn.get_Translation("Description");
				vo.Description = desc != null ? desc : "";
				String help = infoColumn.get_Translation("Help");
				vo.Help = help != null ? help : "";
				GridField gridField = new GridField(vo);
				gridFields.add(gridField);
			}
			
			// If we have a process and at least one process and an editable field, change to the info window rendered
			
			infoWindowListItemRenderer = new WInfoWindowListItemRenderer(this);
			contentPanel.setItemRenderer(infoWindowListItemRenderer);
			if(hasEditable)
			{
					contentPanel.setAllowIDColumnForReadWrite(true);
					infoWindowListItemRenderer.addTableValueChangeListener(contentPanel); // Replicated from WListbox constructor
				}					
			
			StringBuilder builder = new StringBuilder(p_whereClause != null ? p_whereClause.trim() : "");
			String infoWhereClause = infoWindow.getWhereClause();
			if (infoWhereClause != null && infoWhereClause.indexOf("@") >= 0) {
				infoWhereClause = Env.parseContext(Env.getCtx(), p_WindowNo, infoWhereClause, true, false);
				if (infoWhereClause.length() == 0)
					log.log(Level.SEVERE, "Cannot parse context= " + infoWindow.getWhereClause());
			}
			if (infoWhereClause != null && infoWhereClause.trim().length() > 0) {								
				if (builder.length() > 0) {
					builder.append(" AND ");
				}
				builder.append(infoWhereClause);
				p_whereClause = builder.toString();
			}
			
			return true;
		} else {
			return false;
		}
	}


	protected void prepareTable() {		
		List<ColumnInfo> list = new ArrayList<ColumnInfo>();
		String keyTableAlias = tableInfos[0].getSynonym() != null && tableInfos[0].getSynonym().trim().length() > 0 
				? tableInfos[0].getSynonym()
				: tableInfos[0].getTableName();
					
		String keySelectClause = keyTableAlias+"."+p_keyColumn;
		list.add(new ColumnInfo(" ", keySelectClause, IDColumn.class, true, false, null, p_keyColumn));
		
		List<MInfoColumn> gridDisplayedIC = new ArrayList<>();				
		gridDisplayedIC.add(null); // First column does not have any matching info column		
		
				
		int i = 0;
		for(MInfoColumn infoColumn : infoColumns) 
		{						
			if (infoColumn.isDisplayed(infoContext, p_WindowNo)) 
			{
				ColumnInfo columnInfo = null;
				String colSQL = infoColumn.getSelectClause();
				if (! colSQL.toUpperCase().contains(" AS "))
					colSQL += " AS " + infoColumn.getColumnName();
				if (infoColumn.getAD_Reference_ID() == DisplayType.ID) 
				{
					if (infoColumn.getSelectClause().equalsIgnoreCase(keySelectClause))
						continue;
					
					columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), colSQL, DisplayType.getClass(infoColumn.getAD_Reference_ID(), true), infoColumn.isReadOnly());
				}
				else if (DisplayType.isLookup(infoColumn.getAD_Reference_ID()))
				{
					if (infoColumn.getAD_Reference_ID() == DisplayType.List)
					{
						WEditor editor = null;
						editor = WebEditorFactory.getEditor(gridFields.get(i), true);
				        editor.setMandatory(false);
				        editor.setReadWrite(false);
				        editorMap.put(colSQL, editor);
						columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), colSQL, ValueNamePair.class, (String)null, infoColumn.isReadOnly());
					}
					else
					{
						columnInfo = createLookupColumnInfo(tableInfos, gridFields.get(i), infoColumn);
					}					
				}
				else  
				{
					columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), colSQL, DisplayType.getClass(infoColumn.getAD_Reference_ID(), true), infoColumn.isReadOnly() );
				}
				columnInfo.setColDescription(infoColumn.get_Translation("Description"));
				columnInfo.setAD_Reference_ID(infoColumn.getAD_Reference_ID());
				columnInfo.setGridField(gridFields.get(i));
				columnInfo.setColumnName(infoColumn.getColumnName());
				list.add(columnInfo);
				gridDisplayedIC.add(infoColumn);
				
				if (keyColumnOfView == infoColumn){
					if (columnInfo.getColClass().equals(IDColumn.class)) 
						isIDColumnKeyOfView = true;
					indexKeyOfView = list.size() - 1;
				}
			}		
			i++;
		}
		
		if (keyColumnOfView == null){
			isIDColumnKeyOfView = true;// because use main key
		}
		
		columnInfos = list.toArray(new ColumnInfo[0]);
		MInfoColumn gridDisplayedInfoColumns[] = gridDisplayedIC.toArray(new MInfoColumn[gridDisplayedIC.size()]);
		
		if(infoWindowListItemRenderer != null)
			infoWindowListItemRenderer.setGridDisplaydInfoColumns(gridDisplayedInfoColumns,columnInfos);
		
		prepareTable(columnInfos, infoWindow.getFromClause(), p_whereClause, infoWindow.getOrderByClause());		
	}

	protected ColumnInfo createLookupColumnInfo(TableInfo[] tableInfos,
			GridField gridField, MInfoColumn infoColumn) {
		String columnName = gridField.getColumnName();
		String validationCode = "";
		MLookupInfo lookupInfo = MLookupFactory.getLookupInfo(Env.getCtx(), p_WindowNo, 0, infoColumn.getAD_Reference_ID(), Env.getLanguage(Env.getCtx()), columnName, infoColumn.getAD_Reference_Value_ID(), false, validationCode);
		String displayColumn = lookupInfo.DisplayColumn;
		
				
		int index = infoColumn.getSelectClause().indexOf(".");
		if (index == infoColumn.getSelectClause().lastIndexOf("."))
		{
			String synonym = infoColumn.getSelectClause().substring(0, index);
			for(TableInfo tableInfo : tableInfos)
			{
				if (tableInfo.getSynonym() != null && tableInfo.getSynonym().equals(synonym)) 
				{
					if (tableInfo.getTableName().equalsIgnoreCase(lookupInfo.TableName))
					{
						displayColumn = displayColumn.replace(lookupInfo.TableName+".", tableInfo.getSynonym()+".");
						ColumnInfo columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), displayColumn, KeyNamePair.class, infoColumn.getSelectClause(), infoColumn.isReadOnly());
						return columnInfo;
					}
					break;
				}
			}
		}
		
		WEditor editor = null;
        editor = WebEditorFactory.getEditor(gridField, true);
        editor.setMandatory(false);
        editor.setReadWrite(false);

		String colSQL = infoColumn.getSelectClause();
		if (! colSQL.toUpperCase().contains(" AS "))
			colSQL += " AS " + infoColumn.getColumnName();
        editorMap.put(colSQL, editor);
        Class<?> colClass = columnName.endsWith("_ID") ? KeyNamePair.class : String.class;
		ColumnInfo columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), colSQL, colClass, (String)null, infoColumn.isReadOnly());
		return columnInfo;
	}

	/* (non-Javadoc)
	 * @see eone.webui.panel.InfoPanel#getSQLWhere()
	 */
	@Override
	protected String getSQLWhere() {
		if (!isQueryByUser && prevWhereClause != null){
			return prevWhereClause;
		}
		
		StringBuilder builder = new StringBuilder();
		MTable table = MTable.get(Env.getCtx(), infoWindow.getAD_Table_ID());
		if (!hasIsActiveEditor() && table.get_ColumnIndex("IsActive") >=0 ) {
			if (p_whereClause != null && p_whereClause.trim().length() > 0) {
				builder.append(" AND ");
			}
			builder.append(tableInfos[0].getSynonym()).append(".IsActive='Y'");
		}
		int count = 0;
		for(WEditor editor : editors) {
			if (!editor.isVisible())
				continue;
			
			if (editor instanceof IWhereClauseEditor) {
				String whereClause = ((IWhereClauseEditor) editor).getWhereClause();
				if (whereClause != null && whereClause.trim().length() > 0) {
					count++;
					builder.append(whereClause);					
				}
			} else if (editor.getGridField() != null && editor.getValue() != null && editor.getValue().toString().trim().length() > 0) {
				MInfoColumn mInfoColumn = findInfoColumn(editor.getGridField());
				if (mInfoColumn == null || mInfoColumn.getSelectClause().equals("0")) {
					continue;
				}
				String columnName = mInfoColumn.getSelectClause();
				int asIndex = columnName.toUpperCase().lastIndexOf(" AS ");
				if (asIndex > 0) {
					columnName = columnName.substring(0, asIndex);
				}
				
								
				if (mInfoColumn.getAD_Reference_ID() == DisplayType.ChosenMultipleSelectionList)
				{
					String pString = editor.getValue().toString();
					String column = columnName;
					if (column.indexOf(".") > 0)
						column = column.substring(column.indexOf(".")+1);
					int cnt = DB.getSQLValueEx(null, "SELECT Count(*) From AD_Column WHERE IsActive='Y' AND AD_Client_ID=0 AND Upper(ColumnName)=? AND AD_Reference_ID=?", column.toUpperCase(), DisplayType.ChosenMultipleSelectionList);
					if (cnt > 0)
						builder.append(DB.intersectClauseForCSV(columnName, pString));
					else
						builder.append(DB.inClauseForCSV(columnName, pString));
				} 
				else if (mInfoColumn.getAD_Reference_ID() == DisplayType.ChosenMultipleSelectionTable || mInfoColumn.getAD_Reference_ID() == DisplayType.ChosenMultipleSelectionSearch)
				{
					String pString = editor.getValue().toString();
					if (columnName.endsWith("_ID"))
					{						
						builder.append(DB.inClauseForCSV(columnName, pString));
					}
					else
					{
						builder.append(DB.intersectClauseForCSV(columnName, pString));
					}
				}
				else
				{
					String columnClause = null;
					if (mInfoColumn.getQueryFunction() != null && mInfoColumn.getQueryFunction().trim().length() > 0) {
						String function = mInfoColumn.getQueryFunction();
						if (function.indexOf("@") >= 0) {
							String s = Env.parseContext(infoContext, p_WindowNo, function, true, false);
							if (s.length() == 0) {
								log.log(Level.SEVERE, "Failed to parse query function. " + function);
							} else {
								function = s;
							}
						}
						if (function.indexOf("?") >= 0) {
							columnClause = function.replaceFirst("[?]", columnName);
						} else {
							columnClause = function+"("+columnName+")";
						}
					} else {
						columnClause = columnName;
					}
					builder.append(columnClause)
						   .append(" ")
						   .append(mInfoColumn.getQueryOperator());
					if (columnClause.toUpperCase().startsWith("UPPER(")) {
						builder.append(" UPPER(?)");
					} else {
						builder.append(" ?");
					}
				}
			}
		}	
		if (count > 0) {
			builder.append(" ) ");
		}
		String sql = builder.toString();
		if (sql.indexOf("@") >= 0) {
			sql = Env.parseContext(infoContext, p_WindowNo, sql, true, true);
		}
		
		prevWhereClause = sql;
		
		return sql;
	}

	protected MInfoColumn findInfoColumn(GridField gridField) {
		for(int i = 0; i < gridFields.size(); i++) {
			if (gridFields.get(i) == gridField) {
				return infoColumns[i];
			}
		}
		return null;
	}

	/**
	 * Check has new parameter is change or new input
	 * in case first time search, return true
	 * @return
	 */
	protected boolean isParameteChangeValue (){
		if (prevParameterValues == null){
			return true;
		}
		
		// compare old and new value of parameter input at prev time
		for (int parameterIndex = 0; parameterIndex < prevParameterValues.size(); parameterIndex++){
			Object newValue = prevRefParmeterEditor.get(parameterIndex).getValue();
			if (!prevParameterValues.get(parameterIndex).equals(newValue)){
				return true;
			}
		}

		// in case new field is entered value
		for(WEditor editor : editors) {
			if (!editor.isVisible() || prevRefParmeterEditor.contains(editor))
				continue;
			
			if (editor.getGridField() != null && editor.getValue() != null && editor.getValue().toString().trim().length() > 0) {
				MInfoColumn mInfoColumn = findInfoColumn(editor.getGridField());
				if (mInfoColumn == null || mInfoColumn.getSelectClause().equals("0")) {
					continue;
				}
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	protected void setParameters(PreparedStatement pstmt, boolean forCount)
			throws SQLException {
		// when query not by click requery button, reuse parameter value
		if (!isQueryByUser && prevParameterValues != null){
			for (int parameterIndex = 0; parameterIndex < prevParameterValues.size(); parameterIndex++){
				setParameter (pstmt, parameterIndex + 1, prevParameterValues.get(parameterIndex), prevQueryOperators.get(parameterIndex));
			}
			return;
		}
		
		// init collection to save current parameter value 
		if (prevParameterValues == null){
			prevParameterValues = new ArrayList<Object> ();
			prevQueryOperators = new ArrayList<String> ();
			prevRefParmeterEditor = new ArrayList<WEditor>(); 
		}else{
			prevParameterValues.clear();
			prevQueryOperators.clear();
			prevRefParmeterEditor.clear();
		}

		int parameterIndex = 0;
		for(WEditor editor : editors) {
			if (!editor.isVisible())
				continue;
			
			if (editor.getGridField() != null && editor.getValue() != null && editor.getValue().toString().trim().length() > 0) {
				MInfoColumn mInfoColumn = findInfoColumn(editor.getGridField());
				if (mInfoColumn == null || mInfoColumn.getSelectClause().equals("0")) {
					continue;
				}
				if (mInfoColumn.getAD_Reference_ID()==DisplayType.ChosenMultipleSelectionList || mInfoColumn.getAD_Reference_ID()==DisplayType.ChosenMultipleSelectionSearch
					|| mInfoColumn.getAD_Reference_ID()==DisplayType.ChosenMultipleSelectionTable) {
					continue;
				}
				Object value = editor.getValue();
				parameterIndex++;
				prevParameterValues.add(value);
				prevQueryOperators.add(mInfoColumn.getQueryOperator());
				prevRefParmeterEditor.add(editor);
				setParameter (pstmt, parameterIndex, value, mInfoColumn.getQueryOperator());
			}
		}

	}
	
	
	protected void setParameter (PreparedStatement pstmt, int parameterIndex, Object value, String queryOperator) throws SQLException{
		if (value instanceof Boolean) {					
			pstmt.setString(parameterIndex, ((Boolean) value).booleanValue() ? "Y" : "N");
		} else if (value instanceof String) {
			StringBuilder valueStr = new StringBuilder(value.toString());
			if (queryOperator.equals(X_AD_InfoColumn.QUERYOPERATOR_Like)) {
				if (!valueStr.toString().endsWith("%"))
					valueStr.append("%");
			} else if (queryOperator.equals(X_AD_InfoColumn.QUERYOPERATOR_FullLike)) {
				if (!valueStr.toString().startsWith("%"))
					valueStr.insert(0, "%");
				if (!valueStr.toString().endsWith("%"))
					valueStr.append("%");
			}
			pstmt.setString(parameterIndex, valueStr.toString());
		} else {
			pstmt.setObject(parameterIndex, value);
		}
	}

	@Override
	protected void prepareTable(ColumnInfo[] layout, String from, String where,
			String orderBy) {
		super.prepareTable(layout, from, where, orderBy);
		if (m_sqlMain.indexOf("@") >= 0) {
			String sql = Env.parseContext(infoContext, p_WindowNo, m_sqlMain, true);
			if (sql == null || sql.length() == 0) {
				log.severe("Failed to parsed sql. sql=" + m_sqlMain);
			} else {
				m_sqlMain = sql;
			}
		}
		
		addViewIDToQuery();
		addKeyViewToQuery();
		
		if (m_sqlMain.length() > 0 &&  infoWindow.isDistinct()) {
			m_sqlMain = m_sqlMain.substring("SELECT ".length());
			m_sqlMain = "SELECT DISTINCT " + m_sqlMain;			
		}	
		
		if (m_sqlOrder != null && m_sqlOrder.indexOf("@") >= 0) {
			String sql = Env.parseContext(infoContext, p_WindowNo, m_sqlOrder, true, false);
			if (sql == null || sql.length() == 0) {
				log.severe("Failed to parsed sql. sql=" + m_sqlOrder);
			} else {
				m_sqlOrder = sql;
			}
		}
	}

	
	protected void addViewIDToQuery () {
		m_sqlMain = addMoreColumnToQuery (m_sqlMain);
	}
	
	
	protected void addKeyViewToQuery () {
		if (isNeedAppendKeyViewData()){
			m_sqlMain = addMoreColumnToQuery (m_sqlMain);
		}
	}
	
	@Override
	public boolean isNeedAppendKeyViewData() {
		return (keyColumnOfView != null && !keyColumnOfView.isDisplayed(infoContext, p_WindowNo));
	}
	
	
	protected String addMoreColumnToQuery (String sqlMain) {
		if (sqlMain == null || sqlMain.length() == 0){
			return sqlMain;
		}
				
		int fromIndex = sqlMain.indexOf("FROM");
		// split Select and from clause
		String selectClause = sqlMain.substring(0, fromIndex);
		String fromClause = sqlMain.substring(fromIndex);
		
		// get alias of main table
		StringBuilder sqlBuilder = new StringBuilder(selectClause);
		
		sqlBuilder.append(fromClause);
		// update main sql 
		return sqlBuilder.toString();
		
	}
	
	protected void renderWindow()
	{		
		setTitle(infoWindow.get_Translation("Name"));
		layout = new Borderlayout();
		ZKUpdateUtil.setWidth(layout, "100%");
		ZKUpdateUtil.setHeight(layout, "100%");
        this.appendChild(layout);
		
        if (isLookup())
        	ZKUpdateUtil.setWidth(contentPanel, "99%");
        else
        	contentPanel.setStyle("width: 99%; margin: 0px auto;");
        ZKUpdateUtil.setVflex(contentPanel, true);
        contentPanel.setSizedByContent(true);
        contentPanel.setSpan(true);
        
        North north = new North();
        north.setCollapsible(true);
        north.setSplittable(true);
        north.setAutoscroll(true);                
        LayoutUtils.addSlideSclass(north);
        ZKUpdateUtil.setVflex(north, "min");
        layout.appendChild(north);
        renderParameterPane(north);
        

        Center center = new Center();
		layout.appendChild(center);
        renderContentPane(center);		

        South south = new South();
		layout.appendChild(south);
		renderFooter(south);		
		if (confirmPanel.getButton(ConfirmPanel.A_ZOOM) != null) {
			confirmPanel.getButton(ConfirmPanel.A_ZOOM).setVisible(hasZoom());
			confirmPanel.getButton(ConfirmPanel.A_ZOOM).setDisabled(true);
		}
		

		
		// IDEMPIERE-1334 end
	}

	protected void renderFooter(South south) {		
		southBody = new Vbox();
		ZKUpdateUtil.setHflex(southBody, "1");
		south.appendChild(southBody);
		southBody.appendChild(new Separator());
		southBody.appendChild(confirmPanel);
		southBody.appendChild(statusBar);
	}

	protected void insertPagingComponent() {
		southBody.insertBefore(paging, southBody.getFirstChild());
		layout.invalidate();
	}
	
	protected void renderContentPane(Center center) {				
		Div div = new Div();
		div.setStyle("width :100%; height: 100%");
		ZKUpdateUtil.setVflex(div, "1");
		ZKUpdateUtil.setHflex(div, "1");
		div.appendChild(contentPanel);

		Borderlayout inner = new Borderlayout();
		ZKUpdateUtil.setWidth(inner, "100%");
		ZKUpdateUtil.setHeight(inner, "100%");
		int height = SessionManager.getAppDesktop().getClientInfo().desktopHeight * 90 / 100;
		inner.setStyle("border: none; position: relative; ");
		inner.appendCenter(div);
		//true will conflict with listbox scrolling
		inner.getCenter().setAutoscroll(false);

		if (embeddedWinList.size() > 0) {
			South south = new South();
			int detailHeight = (height * 25 / 100);
			ZKUpdateUtil.setHeight(south, detailHeight + "px");
			south.setAutoscroll(true);
			south.setCollapsible(true);
			south.setSplittable(true);
			south.setTitle(Msg.translate(Env.getCtx(), "Related Information"));
			south.setTooltiptext(Msg.translate(Env.getCtx(), "Related Information"));

			
			south.setSclass("south-collapsible-with-title");
			south.setAutoscroll(true);
			//south.sets
			inner.appendChild(south);
			embeddedPane.setSclass("info-product-tabbedpane");
			ZKUpdateUtil.setVflex(embeddedPane, "1");
			ZKUpdateUtil.setHflex(embeddedPane, "1");

			south.appendChild(embeddedPane);

		}// render embedded

		center.appendChild(inner);
	}

	protected void renderParameterPane(North north) {
		createParameterPanel();        
		north.appendChild(parameterGrid);
	}

	protected void createParameterPanel() {
		layoutParameterGrid(false);
	}
	
	protected void layoutParameterGrid(boolean update) {
		if (!update) {
			parameterGrid = GridFactory.newGridLayout();
			parameterGrid.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "infoParameterPanel");
			parameterGrid.setStyle("width: 95%; margin: auto !important;");
		}
		if (parameterGrid.getColumns() != null)
			parameterGrid.getColumns().detach();
		Columns columns = new Columns();
		parameterGrid.appendChild(columns);
		noOfParameterColumn = getNoOfParameterColumns();
		for(int i = 0; i < noOfParameterColumn; i++)
			columns.appendChild(new Column());
		
		Column column = new Column();
		ZKUpdateUtil.setWidth(column, "100px");
		column.setAlign("right");
		columns.appendChild(column);
		
		if (parameterGrid.getRows() != null)
			parameterGrid.getRows().detach();
		Rows rows = new Rows();
		parameterGrid.appendChild(rows);
		
		if (!update) {
			editors = new ArrayList<WEditor>();
			identifiers = new ArrayList<WEditor>();
		}
		TreeMap<Integer, List<Object[]>> tree = new TreeMap<Integer, List<Object[]>>();
		for (int i = 0; i < infoColumns.length; i++)
		{
			if (infoColumns[i].isQueryCriteria()) {
				List<Object[]> list = tree.get(infoColumns[i].getSeqNoSelection());
				if (list == null) {
					list = new ArrayList<Object[]>();
					tree.put(infoColumns[i].getSeqNoSelection(), list);
				}
				list.add(new Object[]{infoColumns[i], gridFields.get(i)});				
			}
		}
		
		for (Integer i : tree.keySet()) {
			List<Object[]> list = tree.get(i);
			for(Object[] value : list) {
				if (update) {
					for (WEditor editor : editors) {
						if (editor.getGridField() == value[1]) {
							addSearchParameter(editor.getLabel(), editor.getComponent());
							break;
						}
					}
				} else {
					addSelectionColumn((MInfoColumn)value[0], (GridField)value[1]);
				}
			}
		}
		
		evalDisplayLogic();
		if (!update)
			initParameters();
		dynamicDisplay(null);
	}
	
	protected void evalDisplayLogic() {
		for(WEditor editor : editors) {
        	if (editor.getGridField() != null && !editor.getGridField().isDisplayed(true)) {        		
        		editor.getComponent().setVisible(false);
        		if (editor.getLabel() != null)
        			editor.getLabel().setVisible(false);
        	}
        	else if (!editor.getComponent().isVisible()) {
        		editor.getComponent().setVisible(true);
        		if (editor.getLabel() != null)
        			editor.getLabel().setVisible(true);
        	}
        }
		
	}
	
	/**
     *  Add Selection Column to first Tab
	 * @param infoColumn 
     *  @param mField field
    **/
    protected void addSelectionColumn(MInfoColumn infoColumn, GridField mField)
    {
        int displayLength = mField.getDisplayLength();
        if (displayLength <= 0 || displayLength > FIELDLENGTH)
            mField.setDisplayLength(FIELDLENGTH);
        else
            displayLength = 0;

        //  Editor
        WEditor editor = null;
        if (mField.getDisplayType() == DisplayType.PAttribute) 
        {
        	editor = new WInfoPAttributeEditor(infoContext, p_WindowNo, mField);
	        editor.setReadWrite(true);
        }
        else 
        {
	        editor = WebEditorFactory.getEditor(mField, false);
	        editor.setReadWrite(true);
	        editor.dynamicDisplay();
	        editor.addValueChangeListener(this);
	        editor.fillHorizontal();
        }
        Label label = editor.getLabel();
        Component fieldEditor = editor.getComponent();

        //
        if (displayLength > 0)      //  set it back
            mField.setDisplayLength(displayLength);
        //
        if (label != null) {
        	if (infoColumn.getQueryOperator().equals(X_AD_InfoColumn.QUERYOPERATOR_Gt) ||
        		infoColumn.getQueryOperator().equals(X_AD_InfoColumn.QUERYOPERATOR_GtEq) ||
        		infoColumn.getQueryOperator().equals(X_AD_InfoColumn.QUERYOPERATOR_Le) ||
        		infoColumn.getQueryOperator().equals(X_AD_InfoColumn.QUERYOPERATOR_LeEq) ||
        		infoColumn.getQueryOperator().equals(X_AD_InfoColumn.QUERYOPERATOR_NotEq )) {
        		label.setValue(label.getValue() + " " + infoColumn.getQueryOperator());
        	}
        }

        addSearchParameter(label, fieldEditor);
        
        editors.add(editor);
        
        editor.showMenu();
        
        if (infoColumn.isIdentifier()) {
        	identifiers.add(editor);
        }

        fieldEditor.addEventListener(Events.ON_OK, this);		

        mField.addPropertyChangeListener(editor);
                
        mField.setValue(mField.getDefaultForPanel(), true);

    }   // addSelectionColumn

	protected void addSearchParameter(Label label, Component fieldEditor) {
		Row panel = null;
        if (parameterGrid.getRows().getChildren().isEmpty())
        {
        	panel = new Row();
        	parameterGrid.getRows().appendChild(panel);
        }
        else
        {
        	panel = (Row) parameterGrid.getRows().getLastChild();
        	if (panel.getChildren().size() == getNoOfParameterColumns())
        	{
        		panel.appendChild(new Space());
        		panel = new Row();
            	parameterGrid.getRows().appendChild(panel); 
        	}
        }
        if (!(fieldEditor instanceof Checkbox))
        {
        	Div div = new Div();
        	div.setStyle("text-align: right;");
        	div.appendChild(label);
        	if (label.getDecorator() != null){
        		div.appendChild (label.getDecorator());
        	}
        	panel.appendChild(div);
        } else {
        	panel.appendChild(new Space());
        }
        
        // add out parent to add menu for this field, without outerDiv, a new cell will auto make for menu.
        Div outerParent = new Div();
        outerParent.appendChild(fieldEditor);
        panel.appendChild(outerParent);
	}

	protected int getNoOfParameterColumns() {
		return 2;
		
	}

	
	
	protected int findColumnIndex(String columnName) {
		for(int i = 0; i < columnInfos.length; i++) {
			GridField field = columnInfos[i].getGridField();
			if (field != null && field.getColumnName().equalsIgnoreCase(columnName)) {
				return i;
			}
		}
		return -1;
	}
    
    @Override
    protected String buildDataSQL(int start, int end) {
		String dataSql;
		String dynWhere = getSQLWhere();
        StringBuilder sql = new StringBuilder (m_sqlMain);
        if (dynWhere.length() > 0)
            sql.append(dynWhere);   //  includes first AND
        
        if (sql.toString().trim().endsWith("WHERE")) {
        	int index = sql.lastIndexOf(" WHERE");
        	sql.delete(index, sql.length());
        }
        
        dataSql = Msg.parseTranslation(Env.getCtx(), sql.toString());    //  Variables
        dataSql = MRole.getDefault().addAccessSQL(dataSql, getTableName(),
            MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
        
        String otherClause = getOtherClauseParsed();
        if (otherClause.length() > 0) {
        	dataSql = dataSql + " " + otherClause;
        }
        
        dataSql = dataSql + getUserOrderClause();
        
        if (end > start && isUseDatabasePaging() && DB.getDatabase().isPagingSupported())
        {
        	dataSql = DB.getDatabase().addPagingSQL(dataSql, getCacheStart(), getCacheEnd());
        }
		return dataSql;
	}

    private String getOtherClauseParsed() {
    	String otherClause = "";
        if (infoWindow != null && infoWindow.getOtherClause() != null && infoWindow.getOtherClause().trim().length() > 0) {
        	otherClause = infoWindow.getOtherClause();
        	if (otherClause.indexOf("@") >= 0) {
        		String s = Env.parseContext(infoContext, p_WindowNo, otherClause, true, false);
        		if (s.length() == 0) {
        			log.severe("Failed to parse other clause. " + otherClause);
        		} else {
        			otherClause = s;
        		}
        	}
        }
    	return otherClause;
	}

	@Override
    protected void executeQuery() {
    	if (!isRequeryByRunSuccessProcess)
    		prepareTable();
    	super.executeQuery();
    	cacheOriginalValues.clear(); // F3P: Clear original values
    	if (ClientInfo.maxHeight(ClientInfo.SMALL_HEIGHT-1) ||
    		ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1)) {
    		layout.getNorth().setOpen(false);
    		LayoutUtils.addSclass("slide", layout.getNorth());
    	}
    }
    
	@Override
	protected boolean hasZoom() {
		return !isLookup() && infoWindow != null && !MTable.get(Env.getCtx(), infoWindow.getAD_Table_ID()).isView();
	}

	@Override
	public void valueChange(ValueChangeEvent evt) {
		if (evt != null && evt.getSource() instanceof WEditor)
        {
            WEditor editor = (WEditor)evt.getSource();            
            if (evt.getNewValue() == null) {
            	Env.setContext(infoContext, p_WindowNo, editor.getColumnName(), "");
            	Env.setContext(infoContext, p_WindowNo, Env.TAB_INFO, editor.getColumnName(), "");
            } else if (evt.getNewValue() instanceof Boolean) {
            	Env.setContext(infoContext, p_WindowNo, editor.getColumnName(), (Boolean)evt.getNewValue());
            	Env.setContext(infoContext, p_WindowNo, Env.TAB_INFO, editor.getColumnName(), (Boolean)evt.getNewValue());
            } else if (evt.getNewValue() instanceof Timestamp) {
            	Env.setContext(infoContext, p_WindowNo, editor.getColumnName(), (Timestamp)evt.getNewValue());
            	Env.setContext(infoContext, p_WindowNo, Env.TAB_INFO+"|"+editor.getColumnName(), (Timestamp)evt.getNewValue());
            } else {
            	Env.setContext(infoContext, p_WindowNo, editor.getColumnName(), evt.getNewValue().toString());
            	Env.setContext(infoContext, p_WindowNo, Env.TAB_INFO, editor.getColumnName(), evt.getNewValue().toString());
            }
            dynamicDisplay(editor);
        }
		
	}

	protected void dynamicDisplay(WEditor editor) {
		validateField(editor);
		// if attribute set changed (from any value to any value) clear the attribute set instance m_pAttributeWhere
		boolean asiChanged = false;
		if (editor != null && editor instanceof WTableDirEditor && editor.getColumnName().equals("M_AttributeSet_ID"))
			asiChanged = true;
		
		for(WEditor otherEditor : editors)
		{
			if (otherEditor == editor) 
				continue;
			
			// reset value of WInfoPAttributeEditor to null when change M_AttributeSet_ID
			if (asiChanged && otherEditor instanceof WInfoPAttributeEditor)
				((WInfoPAttributeEditor)otherEditor).clearWhereClause();
			
			otherEditor.dynamicDisplay();
		}
		
		evalDisplayLogic();
	}
	
	public void onEvent(Event event)
    {
		if (event.getName().equals(Events.ON_FOCUS)) {
    		for (WEditor editor : editors)
    		{
    			if (editor.isComponentOfEditor(event.getTarget()))
    			{
        			SessionManager.getAppDesktop().updateHelpTooltip(editor.getGridField());
        			return;
    			}
    		}
    	}else if (event.getName().equals(Events.ON_OK) && event.getTarget() != null){ // event when push enter at parameter
    		Component tagetComponent = event.getTarget();
    		boolean isCacheEvent = false;// when event from parameter component, catch it and process at there
    		for(WEditor editor : editors) {
    			Component editorComponent = editor.getComponent();
    			if (editorComponent instanceof EditorBox){
    				editorComponent = ((EditorBox)editorComponent).getTextbox();
    			}
    			if (editorComponent.equals(tagetComponent)){
    				// IDEMPIERE-2136
        			if (editor instanceof WSearchEditor){
        				if (((WSearchEditor)editor).isShowingDialog()){
    						return;
    					}
        			}
    				isCacheEvent = true;
    				break;
    			}
    		}
    		
    		if (isCacheEvent){
    			boolean isParameterChange = isParameteChangeValue();
        		// when change parameter, also requery
        		if (isParameterChange){
        			if (!isQueryByUser)
        				onUserQuery();
        		}else if (m_lookup && contentPanel.getSelectedIndex() >= 0){
        			// do nothing when parameter not change and at window mode, or at dialog mode but select non record    			
        			onOk();
        		}else {
        			// parameter not change. do nothing.
        		}
    		}else{
    			super.onEvent(event);
    		}
    		
    	}
    	else
    	{
    		super.onEvent(event);
    	}
    }
	
	/**
	 * {@inheritDoc-}
	 */
	@Override
	protected void resetParameters() {
		// reset value of parameter to null, just reset display parameter
		for (WEditor editor : editors) {
			GridField gField = editor.getGridField();
			if (gField == null || !gField.isDisplayed()) {
				continue;
			}

			// just reset to default Field set explicit DefaultValue
			Object resetValue = null;
			if (! Util.isEmpty(gField.getVO().DefaultValue, true)) {
				resetValue = gField.getDefaultForPanel();
			}
			Object oldValue = gField.getValue();
			gField.setValue(resetValue, true);
			
			// call valueChange to update env
			ValueChangeEvent changeEvent = new ValueChangeEvent (editor, "", oldValue, resetValue);
			valueChange (changeEvent);
		}

		// init again parameter
		initParameters();

		// filter dynamic value
		dynamicDisplay(null);
		
		onQueryCallback (null);
		
		if (paging != null)
			paging.setParent(null);
		
		layout.invalidate();
		
		contentPanel.getListHead().detach();
	}
	
	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		if (newpage != null) {
			for (WEditor editor : editors)
			{
				editor.getComponent().addEventListener(Events.ON_FOCUS, this);
			}
		}
	}
	
	protected void onClientInfo() {
		int t = getNoOfParameterColumns();
		if (t > 0 && noOfParameterColumn > 0 && t != noOfParameterColumn) {
			layoutParameterGrid(true);
			this.invalidate();
		}
	}

	/**
	 * 	Test Row Count
	 *	@return true if display
	 */
	protected boolean testCount()
	{
		return testCount(true);
	}
	
	/**
	 * 	Test Row Count
	 *	@return true if display
	 */
	protected boolean testCount(boolean promptError)
	{
		long start = System.currentTimeMillis();
		String dynWhere = getSQLWhere();
		StringBuilder sql = new StringBuilder (m_sqlMain);

		if (dynWhere.length() > 0)
			sql.append(dynWhere);   //  includes first AND
		
		String countSql = Msg.parseTranslation(Env.getCtx(), sql.toString());	//	Variables
		if (countSql.trim().endsWith("WHERE")) {
			countSql = countSql.trim();
			countSql = countSql.substring(0, countSql.length() - 5);
		}
		countSql = MRole.getDefault().addAccessSQL	(countSql, getTableName(),
													MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		String otherClause = getOtherClauseParsed();
        if (otherClause.length() > 0) {
    		countSql = countSql + " " + otherClause;
        }
		
		countSql = "SELECT COUNT(*) FROM ( " + countSql + " ) a";			
		
		if (log.isLoggable(Level.FINER))
			log.finer(countSql);
		m_count = -1;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(countSql, null);
			setParameters (pstmt, true);
			rs = pstmt.executeQuery();

			if (rs.next())
				m_count = rs.getInt(1);

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, countSql, e);
			m_count = -2;
		}
		finally
		{
			DB.close(rs, pstmt);
		}

		if (log.isLoggable(Level.FINE))
			log.fine("#" + m_count + " - " + (System.currentTimeMillis()-start) + "ms");
		
		if (infoWindow.getMaxQueryRecords() > 0 && m_count > infoWindow.getMaxQueryRecords())
		{
			if (promptError)
			{
				FDialog.error(getWindowNo(), this, "InfoFindOverMax",
		                m_count + " > " + infoWindow.getMaxQueryRecords());
			}
	        m_count = 0;
		}

		return true;
	}	//	testCount

	/** Return true if there is an 'IsActive' criteria */
	boolean hasIsActiveEditor() {
		for (WEditor editor : editors) {
			if (editor.getGridField() != null && "IsActive".equals(editor.getGridField().getColumnName())) {
				return true;
			}
		}
		return false;
	}

	
	public ArrayList<ColumnInfo> getInfoColumnslayout(MInfoWindow info){

		AccessSqlParser sqlParser = new AccessSqlParser("SELECT * FROM " + info.getFromClause());
		TableInfo[] tableInfos = sqlParser.getTableInfo(0);

		MInfoColumn[] infoColumns = info.getInfoColumns(tableInfos);
		ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
		String keyTableAlias = tableInfos[0].getSynonym() != null && tableInfos[0].getSynonym().trim().length() > 0
				? tableInfos[0].getSynonym()
						: tableInfos[0].getTableName();

				String keySelectClause = keyTableAlias + "." + p_keyColumn;

				for (MInfoColumn infoColumn : infoColumns)
				{
					if (infoColumn.isDisplayed(infoContext, p_WindowNo))
					{
						ColumnInfo columnInfo = null;
						String colSQL = infoColumn.getSelectClause();
						if (! colSQL.toUpperCase().contains(" AS "))
							colSQL += " AS " + infoColumn.getColumnName();
						if (infoColumn.getAD_Reference_ID() == DisplayType.ID)
						{
							if (infoColumn.getSelectClause().equalsIgnoreCase(keySelectClause))
								continue;

							columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), colSQL, DisplayType.getClass(infoColumn.getAD_Reference_ID(), true));
						}
						else if (DisplayType.isLookup(infoColumn.getAD_Reference_ID()))
						{
							if (infoColumn.getAD_Reference_ID() == DisplayType.List)
							{
								WEditor editor = null;							
						        editor = WebEditorFactory.getEditor(getGridField(infoColumn), true);
						        editor.setMandatory(false);
						        editor.setReadWrite(false);
						        editorMap.put(colSQL, editor);
								columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), colSQL, ValueNamePair.class, (String)null);
							}
							else
							{
								GridField field = getGridField(infoColumn);
								columnInfo = createLookupColumnInfo(tableInfos, field, infoColumn);
							}
						}
						else
						{
							columnInfo = new ColumnInfo(infoColumn.get_Translation("Name"), colSQL, DisplayType.getClass(infoColumn.getAD_Reference_ID(), true));
						}
						columnInfo.setColDescription(infoColumn.get_Translation("Description"));
						columnInfo.setGridField(getGridField(infoColumn));
						list.add(columnInfo);
					}

				}

				return   list;
	}

	/**
	 * reset to empty
	 * @param relatedInfo
	 */
	protected void reset(EmbedWinInfo relatedInfo){
		if (relatedInfo.getInfoTbl() != null){
			if (((WListbox)relatedInfo.getInfoTbl()).getModel() != null)
				((WListbox)relatedInfo.getInfoTbl()).getModel().clear();
			else
				((WListbox)relatedInfo.getInfoTbl()).clear();
		}
	}

	protected GridField getGridField(MInfoColumn infoColumn){
		String columnName = infoColumn.getColumnName();
		GridFieldVO vo = GridFieldVO.createParameter(infoContext, p_WindowNo, AEnv.getADWindowID(p_WindowNo), m_infoWindowID, 0,
				columnName, infoColumn.get_Translation("Name"), infoColumn.getAD_Reference_ID(),
				infoColumn.getAD_Reference_Value_ID(), false, false, infoColumn.get_Translation("Placeholder"));
		if (infoColumn.getAD_Val_Rule_ID() > 0) {
			vo.ValidationCode = infoColumn.getAD_Val_Rule().getCode();
			if (vo.lookupInfo != null) {
				vo.lookupInfo.ValidationCode = vo.ValidationCode;
				vo.lookupInfo.IsValidated = false;
			}
		}
		vo.DisplayLogic = infoColumn.getDisplayLogic() != null ? infoColumn.getDisplayLogic() : "";
		String desc = infoColumn.get_Translation("Description");
		vo.Description = desc != null ? desc : "";
		String help = infoColumn.get_Translation("Help");
		vo.Help = help != null ? help : "";
		GridField gridField = new GridField(vo);

		return gridField;
	}

	
	/**
	 * {@inheritDoc}
	 * eval input value of mandatory field, if null show field in red color
	 */
	@Override
	public boolean validateParameters() {
		boolean isValid = true;
		
		for (int i = 0; i < editors.size(); i++){
			WEditor wEditor = (WEditor) editors.get(i);
			// cancel editor not display
			if (wEditor == null || !wEditor.isVisible() || wEditor.getGridField() == null){
				continue;
			}
			
			isValid = isValid & validateField (wEditor);
		}
		
		return isValid;
	}
	
	/**
	 * valid mandatory of a not null, display field
	 * display red color when a mandatory field is not input
	 * @param wEditor
	 * @return
	 */
	protected boolean validateField (WEditor wEditor){
		if (wEditor == null || !wEditor.isVisible() || wEditor.getGridField() == null){
			return true;
		}
		
		GridField validateGrid = wEditor.getGridField();
		// eval only mandatory field
		if (validateGrid.isMandatory(true)){
			// update color of field
			wEditor.updateStyle();
			Object data = wEditor.getValue();
			if (data == null || data.toString().length() == 0) {				
				return false;
			}
		}
		
		return true;		
	}
	
	@Override
	protected boolean hasNew() {
		boolean hasNew = getADWindowID () > 0;
		if (hasNew && vqe == null && hasRightQuickEntry){
			GridWindow gridwindow = GridWindow.get(Env.getCtx(), 0, getADWindowID());
			hasRightQuickEntry = gridwindow != null;
			if (hasRightQuickEntry)
				vqe = new WQuickEntry (0, getADWindowID());
		}
			
		return hasNew && vqe != null && vqe.isAvailableQuickEdit();
	}
	
	
	protected int getADWindowID() {
		if(infoWindow == null)
			return 0;
		String isSOTrx = Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx");
		if (!isLookup() && Util.isEmpty(isSOTrx)) {
			isSOTrx = "Y";
		}
		
		return super.getAD_Window_ID(MTable.getTableName(Env.getCtx(), infoWindow.getAD_Table_ID()), isSOTrx.equalsIgnoreCase("Y"));
	}
	
	@Override
	protected void newRecordAction() {
		WQuickEntry vqe = new WQuickEntry (0, getADWindowID());
		
		vqe.loadRecord (0);								
		
		final ISupportMask parent = LayoutUtils.showWindowWithMask(vqe, this, LayoutUtils.OVERLAP_SELF);
		
		vqe.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {								
			@Override							
			public void onEvent(Event event) throws Exception {							
				Clients.response(new AuEcho(InfoWindow.this, "onQueryCallback", null));
				if (parent != null)
					parent.hideMask();
			}
		});								
										
		vqe.setVisible(true);								
					
	}

	/** Allow to show or hide the sub panel (detail) programmatically */
	protected void setSouthVisible(boolean visible) {
		Component comp = layout.getCenter();
		for (Component c : comp.getChildren()) {
			if (c instanceof Borderlayout) {
				for (Component c1 : c.getChildren()) {
					if (c1 instanceof South) {
						c1.setVisible(visible);
						break;
					}
				}
			}
		}
	}
	
	// Edit Callback method and original values management
	
	public Properties getRowaAsCtx(int row, int editingColumn, Object editingValue)
	{
		ListModelTable model = contentPanel.getModel();
		Properties ctx = new Properties(Env.getCtx()); // Allow session values
		
		// Parameter dditors
		
		for(WEditor e:editors)
		{
			Object val = e.getValue();
			String column = e.getColumnName();
			
			if(val != null)
			{
				if(val instanceof Integer)
					Env.setContext(ctx, 0, column, (Integer)val);
				else if(val instanceof Timestamp)
					Env.setContext(ctx, 0, column, (Timestamp)val);
				else if(val instanceof Boolean)
					Env.setContext(ctx, 0, column, (Boolean)val);
				else
					Env.setContext(ctx, 0, column, val.toString());
			}
		}
		
		for(int i=0; i < p_layout.length; i++)			
		{			
			String column = p_layout[i].getColumnName();
					
			Object val = null;
			
			if(i != editingColumn)
				val = model.getValueAt(row, i);
			else
				val = editingValue;
			
			// Get id from 'complex' types
			
			if(val != null)
			{				
				if(val instanceof IDColumn)
				{
					IDColumn idc = (IDColumn)val;
					val = idc.getRecord_ID();
				}
				else if(val instanceof KeyNamePair)
				{
					KeyNamePair knp = (KeyNamePair)val;
					val = knp.getKey();
				}
								
				if(val instanceof Integer)
					Env.setContext(ctx, 0, column, (Integer)val);
				else if(val instanceof Timestamp)
					Env.setContext(ctx, 0, column, (Timestamp)val);
				else if(val instanceof Boolean)
					Env.setContext(ctx, 0, column, (Boolean)val);
				else
					Env.setContext(ctx, 0, column, val.toString());
			}				
		}
		
		return ctx;
	}
	
	public void onCellEditCallback(ValueChangeEvent event, int rowIndex, int colIndex, WEditor editor, GridField field)
	{		
		Object val = event.getNewValue();
	
		if(val != null && columnInfos[colIndex].getColClass().equals(KeyNamePair.class))
		{
			Integer iVal = (Integer)val;
			String 	display = editor.getDisplay();
			
			KeyNamePair kdc = new KeyNamePair(iVal, display);
			val = kdc;
		}
		
		MInfoColumn infoColumn = infoColumns[colIndex - 1];
		boolean changeIsValid = true;
		String validationSQL = null;
		
		if(!Util.isEmpty(infoColumn.getInputFieldValidation(), true)) // Run validation
		{
			changeIsValid = false;
			
			Properties ctx = getRowaAsCtx(rowIndex, colIndex, val);
			
			String rawSQL = infoColumn.getInputFieldValidation(); 
			validationSQL = Env.parseContext(ctx, 0, rawSQL, false);
			
			try
			{
				List<List<Object>> errors = DB.getSQLArrayObjectsEx(null, validationSQL);
			
				if(errors != null && errors.size() > 0)
				{
					StringBuilder sbError = new StringBuilder();

					for(List<Object> line:errors)
					{
						if(line.size() > 0)
						{
							if(sbError.length() > 0)
								sbError.append('\n');
							
							sbError.append(line.get(0));
						}
					}
					
					String msg = Msg.translate(ctx, sbError.toString());
					FDialog.error(0, this, "ValidationError", msg); // TODO messaggio
				}
				else	
					changeIsValid = true;
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE, "Error executing validation SQL: " + validationSQL, e);
				
				FDialog.error(0, this, "Error", validationSQL); // TODO messaggio
				changeIsValid = false;
			}
		}
		
		if(changeIsValid)
		{		
			ListModelTable model = contentPanel.getModel();
			Object row = model.get(rowIndex);
			
			model.removeFromSelection(row);
			contentPanel.setValueAt(val, rowIndex, colIndex);		
			model.addToSelection(row);
			
			Clients.resize(contentPanel);
		}
		else
		{
			editor.setValue(event.getOldValue());
		}
	}
	
	protected void restoreOriginalValues(int rowIndex)
	{
		Integer viewIdKey = getColumnValue(rowIndex);
		
		if(cacheOriginalValues.containsKey(viewIdKey)) // Only cache if not cached to avoid caching subsequent modifications
		{
			int 				 colCount = contentPanel.getColumnCount();
			List<Object> row = cacheOriginalValues.get(viewIdKey);
			
			for(int i=1; i < colCount; i++) // Skip first row (selection)
			{
				Object val = row.get(i-1);
				contentPanel.setValueAt(val, rowIndex, i);
			}
		}
	}
	
	protected void cacheOriginalValues(int rowIndex)
	{
		Integer viewIdKey = getColumnValue(rowIndex);
		
		if(cacheOriginalValues.containsKey(viewIdKey) == false) // Only cache if not cached to avoid caching subsequent modifications
		{
			int colCount = contentPanel.getColumnCount();
			List<Object> row = new ArrayList<>();
			
			for(int i=1; i < colCount; i++) // Skip first row (selection)
			{
				Object val = contentPanel.getValueAt(rowIndex, i);
				row.add(val);
			}
			
			cacheOriginalValues.put(viewIdKey, row);
		}
	}
		
	@Override
	public void tableChanged(WTableModelEvent event)
	{
		// Manage cache of values
		
		if(hasEditable && event.getColumn() == 0)
		{
			for(int row=event.getFirstRow(); row <= event.getLastRow(); row++)
			{
				Object col0 = contentPanel.getValueAt(row, 0);
				
				if(col0 instanceof IDColumn)
				{
					IDColumn idc = (IDColumn)col0;
					
					if(idc.isSelected())
					{
						cacheOriginalValues(row);
					}
					else
					{
						restoreOriginalValues(row);
					}
				}
			}
			
			Clients.resize(contentPanel);
		}

		super.tableChanged(event);
	}
	
	@Override
	public void onQueryCallback(Event event)
	{
		super.onQueryCallback(event);
		
		enableExportButton();
	}
		
	
	@Override
	protected void updateListSelected()
	{
		if(hasEditable)
		{
			temporarySelectedData = new HashMap<>();
			
			ListModelTable model = contentPanel.getModel();
			
			for(int rowIndex:contentPanel.getSelectedIndices())
			{
				Integer keyViewValue = getColumnValue(rowIndex);
				@SuppressWarnings("unchecked")
				List<Object> row = (List<Object>)model.get(rowIndex);
				
				ArrayList<Object> clonedRow = new ArrayList<>(row);				
				temporarySelectedData.put(keyViewValue, clonedRow);
			}
			
			for(Entry<Integer, List<Object>> entry: recordSelectedData.entrySet())
			{
				ArrayList<Object> clonedRow = new ArrayList<>(entry.getValue());				
				temporarySelectedData.put(entry.getKey(), clonedRow);
			}						
		}		

		super.updateListSelected();
	}

	@Override
	protected void restoreSelectedInPage()
	{
		super.restoreSelectedInPage();
		
		if(temporarySelectedData != null)
		{
			temporarySelectedData.clear();
			temporarySelectedData = null;
		}		
	}


	@Override
	public boolean onRestoreSelectedItemIndexInPage(Integer keyViewValue, int rowIndex, Object oRow)
	{
		if(hasEditable && temporarySelectedData != null)
		{
			
			cacheOriginalValues(rowIndex);
			
			int gridFieldsOffset = 1; // First column is the id, and its not on the infoColumns
			
			@SuppressWarnings("unchecked")
			List<Object> row = (List<Object>)oRow;
			List<Object> originalSelectedRow = temporarySelectedData.get(keyViewValue);
			ListModelTable model = contentPanel.getModel();
			
			// While restoring values we dong want to trigger listeners
			model.removeTableModelListener(this);
						
			for(int i=0; i < infoColumns.length; i++)
			{
				if(infoColumns[i].isReadOnly() == false) // Only replace editable column, in case some other data changed on db
				{
					int colIndex = i + gridFieldsOffset;
					Object obj = originalSelectedRow.get(colIndex);
					model.setValueAt( obj, rowIndex, colIndex);
				}				
			}
			
			// Restore isSelected status on IDColumn
			Object id = (IDColumn)row.get(0);
			
			if(id instanceof IDColumn)
			{
				IDColumn idc = (IDColumn)id;
				idc.setSelected(true);
			}
			
			// Restore listners
			model.addTableModelListener(this);
		}

		return super.onRestoreSelectedItemIndexInPage(keyViewValue, rowIndex, oRow);
	}
	
	// F3P: Export function
	
	protected void initExport()
	{
	    exportButton = ButtonFactory.createNamedButton("Export", false, true);        
	    exportButton.setId("Export");
	    exportButton.setEnabled(false);       
	    exportButton.addEventListener(Events.ON_CLICK, new XlsExportAction());
	
	    confirmPanel.addComponentsLeft(exportButton);
	}

	protected void enableExportButton()
	{
		if(exportButton == null)
			return;
		
		exportButton.setEnabled(contentPanel.getRowCount() > 0);		
	}
	
	private class XlsExportAction implements EventListener<Event>
	{		
		@Override
		public void onEvent(Event evt) throws Exception
		{
			if(evt.getTarget() == exportButton)
			{
				XlsExporter exporter = new XlsExporter();
				
				exporter.doExport();
			}
		}
	}
	
	private class XlsExporter extends AbstractExcelExporter
	{
		private ResultSet m_rs = null;
		private int rowCount = -1;
		private int currentRow = -1;
		
		public void doExport() throws Exception
		{
			int originalCount = m_count;
			
			String dataSql = buildDataSQL(0, 0);
			
			File file = File.createTempFile("Export", ".xls");
			
			testCount();
			
			rowCount = m_count;
			m_count = originalCount;				
			
			if(rowCount > 0)
			{
				PreparedStatement pstmt = null;
				Trx trx = null;
				
				try
				{
					String trxName = Trx.createTrxName("InfoPanelLoad:");
					trx  = Trx.get(trxName, true);
					trx.setDisplayName(getClass().getName()+"_exportXls");
					pstmt = DB.prepareStatement(dataSql, trxName);
					setParameters (pstmt, false);	//	no count

					pstmt.setFetchSize(100);
					m_rs = pstmt.executeQuery();

					export(file, null);
				}
				catch(SQLException e)
				{
					log.log(Level.SEVERE, dataSql, e);
				}
				finally
				{
					DB.close(m_rs, pstmt);
					trx.close();
					
					m_rs = null;
					currentRow = -1;
				}
				
				AMedia media = null;
				media = new AMedia(file.getName(), null, "application/vnd.ms-excel", file, true);
				Filedownload.save(media);
			}			
		}

		@Override
		public boolean isFunctionRow()
		{
			return false;
		}

		@Override
		public int getColumnCount()
		{			
			return columnInfos.length;
		}

		@Override
		public int getRowCount()
		{
			return rowCount;
		}

		@Override
		protected void setCurrentRow(int row)
		{
			if(row > currentRow)
			{
				try
				{
					m_rs.next();
					currentRow = row;
				}
				catch(SQLException e)
				{
					throw new EONEException(e);
				}
			}
		}

		@Override
		protected int getCurrentRow()
		{
			return currentRow;
		}

		@Override
		public boolean isColumnPrinted(int col)
		{
			return (columnInfos[col].getGridField() != null);
		}

		@Override
		public String getHeaderName(int col)
		{			
			return columnInfos[col].getColHeader();
		}

		@Override
		public int getDisplayType(int row, int col)
		{
			int displayType = -1;
			GridField gridField = columnInfos[col].getGridField();
			displayType = gridField.getDisplayType();
						
			return displayType;
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			Object val = null;

			int columnIndex = 1;
			int colFound = 0;
			for (int idx = 0; idx < getColumnCount(); idx++) {
				if (isColumnPrinted(idx)) {
					columnIndex++;
					colFound++;
					if (colFound >= col) {
						break;
					}
					if (columnInfos[idx].isKeyPairCol()) {
						columnIndex++;
					}
				}
			}

			try
			{
				val = m_rs.getObject(columnIndex);
				if (columnInfos[col].isKeyPairCol()) {
					m_rs.getObject(columnIndex+1);
					if (m_rs.wasNull()) {
						val = null;
					}
				}
			}
			catch(SQLException e)
			{
				throw new EONEException(e);
			}
			
			if(val != null && !columnInfos[col].isKeyPairCol() 
					&& columnInfos[col].getGridField().getLookup() != null)
			{
				Lookup lookup = columnInfos[col].getGridField().getLookup();
				if (lookup != null)
				{
					val = lookup.getDisplay(val);
				}
			} 
			
			return val; 
		}

		@Override
		public boolean isPageBreak(int row, int col)
		{
			return false;
		}

		@Override
		public boolean isDisplayed(int row, int col)
		{
			return true;
		}	
	}	
}
