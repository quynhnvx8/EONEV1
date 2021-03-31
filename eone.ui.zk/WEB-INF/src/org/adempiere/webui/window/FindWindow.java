
package org.adempiere.webui.window;

import static org.compiere.model.SystemIDs.REFERENCE_YESNO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Group;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListCell;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WNumberEditor;
import org.adempiere.webui.editor.WPaymentEditor;
import org.adempiere.webui.editor.WStringEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.editor.WebEditorFactory;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.factory.ButtonFactory;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.GridField;
import org.compiere.model.GridFieldVO;
import org.compiere.model.GridTab;
import org.compiere.model.Lookup;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MLookupInfo;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.au.out.AuFocus;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;


public class FindWindow extends Window implements EventListener<Event>, ValueChangeListener, DialogEvents
{
	/**
	 * MOD QUYNHNV.X8: 24/09/2020
	 */
	private static final long serialVersionUID = -3907408033854720147L;

	private static final String FIND_ROW_EDITOR = "find.row.editor";

	private static final String FIND_ROW_EDITOR_TO = "find.row.editor.to";

	
    private Window winLookupRecord;
    
    private Grid contentSimple;
    /** Target Window No            */
    private int             m_targetWindowNo;
    /** Table ID                    */
    private int             m_AD_Table_ID;
    /** Table Name                  */
    private String          m_tableName;
    /** Where                       */
    private String          m_whereExtended;
    /** Search Fields               */
    private GridField[]     m_findFields;
    /** The Tab               */
	private GridTab m_gridTab = null;
    /** Resulting query             */
    private MQuery          m_query = null;
    /** Is cancel ?                 */
    private boolean         m_isCancel = false; // teo_sarca [ 1708717 ]
    /** Logger          */
    private static final CLogger log = CLogger.getCLogger(FindWindow.class);
    /** Number of records           */
    private int             m_total;
    private PreparedStatement   m_pstmt;
    //
    /** List of WEditors            */
    private ArrayList<WEditor>          m_sEditors = new ArrayList<WEditor>();
    private ArrayList<ToolBarButton>    m_sEditorsFlag = new ArrayList<ToolBarButton>();
    private ArrayList<WEditor>          m_sEditorsTo = new ArrayList<WEditor>();
    /** For Grid Controller         */
    public static final int     TABNO = 99;
    /** Length of Fields on first tab   */
    public static final int     FIELDLENGTH = 20;

    private int m_AD_Tab_ID = 0;
	private Rows contentSimpleRows;
	private boolean m_createNew = false;
	private int m_minRecords;
	private String m_title;

	/** Index ColumnName = 0		*/
	public static final int		INDEX_COLUMNNAME = 0;
	/** Index Operator = 1			*/
	public static final int		INDEX_OPERATOR = 1;
	/** Index Value = 2				*/
	public static final int		INDEX_VALUE = 2;
	/** Index Value2 = 3			*/
	public static final int		INDEX_VALUE2 = 3;
	
	/** Index AndOr = 4		*/
	public static final int		INDEX_ANDOR = 4;
	/** Index LeftBracket = 5		*/
	public static final int		INDEX_LEFTBRACKET = 5;
	/** Index RightBracket = 6		*/
	public static final int		INDEX_RIGHTBRACKET = 6;
	/** Index History		*/
	public static final int		INDEX_HISTORY = 1;
	

	private static final String FIELD_SEPARATOR = "<^>";
	private static final String SEGMENT_SEPARATOR = "<~>";
	

	private Properties m_simpleCtx;
	private Properties m_advanceCtx;
	
	
	private static final String ON_POST_VISIBLE_ATTR = "onPostVisible.Event.Posted";

	private Label statusBar = new Label();
	
   
    public FindWindow (int targetWindowNo, String title,
            int AD_Table_ID, String tableName, String whereExtended,
            GridField[] findFields, int minRecords, int adTabId)
    {
        m_targetWindowNo = targetWindowNo;
        m_title = title;
        m_AD_Table_ID = AD_Table_ID;
        m_tableName = tableName;
        m_whereExtended = whereExtended;
        m_findFields = findFields;
        if (findFields != null && findFields.length > 0)
        	m_gridTab = findFields[0].getGridTab();
        m_AD_Tab_ID = adTabId;
        m_minRecords = minRecords;
        m_isCancel = true;
        //
        m_simpleCtx = new Properties(Env.getCtx());
        m_advanceCtx = new Properties(Env.getCtx());
        
        this.setBorder("normal");
        this.setShadow(false);
        ZKUpdateUtil.setWidth(this, "600px");
        ZKUpdateUtil.setHeight(this, "350px");
        this.setTitle(Msg.getMsg(Env.getCtx(), "Find").replaceAll("&", "") + ": " + title);
        this.setClosable(false);
        this.setSizable(true);  
        this.setMaximizable(false);
        
        this.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "findWindow");
        this.setId("findWindow_"+targetWindowNo);
        LayoutUtils.addSclass("find-window", this);
    }
    
    public boolean initialize() 
    {
    	m_query = new MQuery (m_tableName);
        m_query.addRestriction(m_whereExtended);
        //  Required for Column Validation
        Env.setContext(Env.getCtx(), m_targetWindowNo, "Find_Table_ID", m_AD_Table_ID);
        //
        initPanel();
        initFind();

        if (m_total < m_minRecords)
        {
            return false;
        }
                
        return true;
    }
    
    public boolean validate(int targetWindowNo, String title,
            int AD_Table_ID, String tableName, String whereExtended,
            GridField[] findFields, int minRecords, int adTabId)
    {
    	if (m_targetWindowNo != targetWindowNo) return false;
    	if ((title == null && m_title != null) || (title != null && m_title == null) || !(title.equals(m_title))) return false;
    	if (AD_Table_ID != m_AD_Table_ID) return false;
    	if ((tableName == null && m_tableName != null) || (tableName != null && m_tableName == null) || !(tableName.equals(m_tableName))) return false;
    	if ((whereExtended == null && m_whereExtended != null) || (whereExtended != null && m_whereExtended == null) || !(whereExtended.equals(m_whereExtended))) return false;
    	if (adTabId != m_AD_Tab_ID) return false;
    	if ((findFields == null && m_findFields != null) || (findFields != null && m_findFields == null) || (findFields.length != m_findFields.length)) return false;
    	if (findFields != null && findFields.length > 0) 
    	{
    		for(int i = 0; i < findFields.length; i++)
    		{
    			if (m_findFields[i] != null && findFields[i].getAD_Field_ID() != m_findFields[i].getAD_Field_ID()) return false;
    		}
    	}
    	
    	m_minRecords = minRecords;
    	m_total = getNoOfRecords(null, false);
    	if (m_total < m_minRecords)
        {
            return false;
        }
    	
    	m_query = new MQuery (m_tableName);
        m_query.addRestriction(m_whereExtended);
        
    	return true;
    }
    
    /**
     * initialise lookup record tab
     *
    **/
    private void initSimple()
    {
        Button btnNew = ButtonFactory.createNamedButton(ConfirmPanel.A_NEW, true, true);
        btnNew.setId("btnNew");
        btnNew.addEventListener(Events.ON_CLICK,this);

        Button btnOk = ButtonFactory.createNamedButton(ConfirmPanel.A_OK, true, true);
        btnOk.setName("btnOkSimple");
        btnOk.setId("btnOk");
        btnOk.addEventListener(Events.ON_CLICK,this);

        Button btnCancel = ButtonFactory.createNamedButton(ConfirmPanel.A_CANCEL, true, true);
        btnCancel.setId("btnCancel");
        btnCancel.addEventListener(Events.ON_CLICK,this);

        Panel pnlButtonRight = new Panel();
        pnlButtonRight.appendChild(btnNew);
        pnlButtonRight.appendChild(btnOk);
        pnlButtonRight.appendChild(btnCancel);
        pnlButtonRight.setStyle("text-align:right");
        ZKUpdateUtil.setWidth(pnlButtonRight, "100%");
        ZKUpdateUtil.setHflex(pnlButtonRight, "1");

        Panel pnlButtonLeft = new Panel();
        pnlButtonLeft.appendChild(statusBar);
        ZKUpdateUtil.setHflex(pnlButtonLeft, "1");

        Hbox hboxButton = new Hbox();
        hboxButton.appendChild(pnlButtonLeft);
        hboxButton.appendChild(pnlButtonRight);
        ZKUpdateUtil.setWidth(hboxButton, "100%");

        contentSimple = new Grid();
        contentSimple.setId("contentSimple");
        contentSimple.setStyle("width: 100%; position: relative");
        contentSimple.makeNoStrip();
        ZKUpdateUtil.setHflex(contentSimple, "1");
        
        Columns columns = new Columns();
        Column column = new Column();
        column.setAlign("right");
        ZKUpdateUtil.setWidth(column, "30%");
        columns.appendChild(column);
        
        column = new Column();
        column.setAlign("left");
        ZKUpdateUtil.setWidth(column, "50%");
        columns.appendChild(column);
        
        column = new Column();
        ZKUpdateUtil.setWidth(column, "20%");
        columns.appendChild(column);
        
        contentSimple.appendChild(columns);

        contentSimpleRows = new Rows();
        contentSimple.appendChild(contentSimpleRows);
        ZKUpdateUtil.setVflex(contentSimple, true);

        Borderlayout layout = new Borderlayout();
        ZKUpdateUtil.setHflex(layout, "1");
        ZKUpdateUtil.setVflex(layout, "1");
        winLookupRecord.appendChild(layout);

        Center center = new Center();
        layout.appendChild(center);
        center.appendChild(contentSimple);
        ZKUpdateUtil.setVflex(contentSimple, "1");
		ZKUpdateUtil.setHflex(contentSimple, "1");

        South south = new South();
        layout.appendChild(south);
        south.setStyle("background-color: #F0F0F0;");
        south.appendChild(hboxButton);

        ZKUpdateUtil.setWidth(winLookupRecord, "100%");
        ZKUpdateUtil.setHeight(winLookupRecord, "100%");
        winLookupRecord.addEventListener(Events.ON_OK, this);
        LayoutUtils.addSclass("find-window-simple", winLookupRecord);

    }   //  initSimple

    
    private void initPanel()
    {
    	winLookupRecord = new Window();
        this.appendChild(winLookupRecord);
        
        initSimple();
      
    } // initPanel
    
    
   
    private void initFind()
    {
        log.config("");
        
        ArrayList<GridField> gridFieldList = new ArrayList<GridField>();
        ArrayList<GridField> moreFieldList = new ArrayList<GridField>();
        //  Get Info from target Tab
        for (int i = 0; i < m_findFields.length; i++)
        {
            GridField mField = m_findFields[i];
            boolean isDisplayed = mField.isDisplayed();
            
            if (mField.getVO().displayType == DisplayType.YesNo || mField.isEncrypted() || mField.isEncryptedColumn()) {
				// Make Yes-No searchable as list
				GridFieldVO vo = mField.getVO();
				GridFieldVO ynvo = vo.clone(m_simpleCtx, vo.WindowNo, vo.TabNo, vo.AD_Window_ID, vo.AD_Tab_ID, vo.tabReadOnly);
				ynvo.IsDisplayed = true;
				ynvo.displayType = DisplayType.List;
				ynvo.AD_Reference_Value_ID = REFERENCE_YESNO;

				ynvo.lookupInfo = MLookupFactory.getLookupInfo (ynvo.ctx, ynvo.WindowNo, ynvo.AD_Column_ID, ynvo.displayType,
						Env.getLanguage(ynvo.ctx), ynvo.ColumnName, ynvo.AD_Reference_Value_ID,
						ynvo.IsParent, ynvo.ValidationCode);
				ynvo.lookupInfo.tabNo = TABNO;

				GridField ynfield = new GridField(ynvo);

				// replace the original field by the YN List field
				m_findFields[i] = ynfield;
				mField = ynfield;
			} else if  ( mField.getVO().displayType == DisplayType.Button ) {
				// Make Buttons searchable
				GridFieldVO vo = mField.getVO();
				if ( vo.AD_Reference_Value_ID > 0 )
				{
					GridFieldVO postedvo = vo.clone(m_simpleCtx, vo.WindowNo, vo.TabNo, vo.AD_Window_ID, vo.AD_Tab_ID, vo.tabReadOnly);
					postedvo.IsDisplayed = true;
					postedvo.displayType = DisplayType.List;

					postedvo.lookupInfo = MLookupFactory.getLookupInfo (postedvo.ctx, postedvo.WindowNo, postedvo.AD_Column_ID, postedvo.displayType,
							Env.getLanguage(postedvo.ctx), postedvo.ColumnName, postedvo.AD_Reference_Value_ID,
							postedvo.IsParent, postedvo.ValidationCode);
					postedvo.lookupInfo.tabNo = TABNO;

					GridField postedfield = new GridField(postedvo);

					// replace the original field by the Posted List field
					m_findFields[i] = postedfield;
					mField = postedfield;
				}
			} else {
				// clone the field and clean gridtab - IDEMPIERE-1105
		        GridField findField = (GridField) mField.clone(m_simpleCtx);
		        if (findField.isLookup()) {
		        	Lookup lookup = findField.getLookup();
		        	if (lookup != null && lookup instanceof MLookup) {
		        		MLookup mLookup = (MLookup) lookup;
		        		mLookup.getLookupInfo().ctx = m_simpleCtx;
		        		mLookup.getLookupInfo().tabNo = TABNO;
		        	}
		        }
		        findField.setGridTab(null);
				m_findFields[i] = findField;
				mField = findField;
			}
			
			if (mField.isSelectionColumn()) {
            	gridFieldList.add(mField); // isSelectionColumn 
            } else {
            	if ((isDisplayed || mField.isVirtualSearchColumn()) && mField.getDisplayType() != DisplayType.Button && !mField.getColumnName().equals("AD_Client_ID"))
            		moreFieldList.add(mField);
            }
        }   //  for all target tab fields

        //show well known column or the first 2 column in the form if no selection columns have been defined 
        if (gridFieldList.isEmpty() && !moreFieldList.isEmpty())
        {
        	for(GridField field:moreFieldList){
        		if (field.getColumnName().equals("Value") 
        				|| field.getColumnName().equals("Name") 
        				|| field.getColumnName().equals("DocumentNo") 
        				|| field.getColumnName().equals("Description")
        				|| field.getColumnName().equals("DateAcct"))
        		{
        			gridFieldList.add(field);        			
        		}
        	}
        	if (gridFieldList.isEmpty()) {
        		int i = 0;
        		for(GridField field:moreFieldList){
        			if(field.getColumnName().equals("AD_Client_ID") || field.getColumnName().equals("AD_Org_ID") 
        					|| field.getDisplayType() == DisplayType.ID)
        				continue;
        			gridFieldList.add(field);
        			i++;
        			if (i == 2) break;
        		}
        	}
        	for(GridField field:gridFieldList){
        		moreFieldList.remove(field);
        	}
        }
       
        // added comparator on sequence of selection column for IDEMPIERE-377
        Collections.sort(gridFieldList, new Comparator<GridField>() {
			@Override
			public int compare(GridField o1, GridField o2) {
				return o1.getSeqNoSelection()-o2.getSeqNoSelection();
			}
		});
        
        List<GridField> excludes = new ArrayList<GridField>();
        // adding sorted columns
        for(GridField field:gridFieldList){
        	if (field.isVirtualUIColumn())
        		continue;
        	if (!addSelectionColumn (field))
        		excludes.add(field);
		} 
        
        //add ... link to show the rest of the columns
        if (!moreFieldList.isEmpty() && !gridFieldList.isEmpty()) {
        	Group rowg = new Group(Msg.getMsg(Env.getCtx(), "Searchmore"));
        	contentSimpleRows.appendChild(rowg);
			Cell cell = (Cell) rowg.getFirstChild();
			cell.setSclass("z-group-findwindow");
			cell.setColspan(3);
			cell.setAlign("left");
        	for(GridField field:moreFieldList){
        		if (field.isVirtualUIColumn())
        			continue;
            	if (!addSelectionColumn (field, rowg))
            		excludes.add(field);
    		}
        	rowg.setOpen(false);
        }
        
        if (!excludes.isEmpty()) {
        	for(GridField field : excludes) {
        		for(int i = 0; i < m_findFields.length; i++) {
        			if (m_findFields[i] == field) {
        				m_findFields[i] = null;
        				break;
        			}
        		}
        	}
        }
        
        
        
        gridFieldList = null;
        m_total = getNoOfRecords(null, false);

		setStatusDB (m_total);
    }   //  initFind

   

    public boolean addSelectionColumn(GridField mField)
    {
    	return addSelectionColumn(mField, null);
    }
    
    /**
     *  Add Selection Column to first Tab
     *  @param mField field
    **/
    public boolean addSelectionColumn(GridField mField, Group group)
    {
        if (log.isLoggable(Level.CONFIG)) log.config(mField.getHeader());
        int displayLength = mField.getDisplayLength();
        if (displayLength <= 0 || displayLength > FIELDLENGTH)
            mField.setDisplayLength(FIELDLENGTH);
        else
            displayLength = 0;

        //  Editor
        WEditor editor = null;
        //false will use hflex which is render 1 pixel too width on firefox
        editor = WebEditorFactory.getEditor(mField, true);
        if (!editor.isSearchable()) {
        	return false;
        }
        editor.setMandatory(false);
        editor.setReadWrite(true);
        editor.dynamicDisplay();
        editor.updateStyle(false);
        editor.addValueChangeListener(this);
        Label label = editor.getLabel();
        Component fieldEditor = editor.getComponent();
        setLengthStringField(mField, fieldEditor);
        //Fix miss label of checkbox
        label.setValue(mField.getHeader());
        //
        if (displayLength > 0)      //  set it back
            mField.setDisplayLength(displayLength);
        //
        
        WEditor editorTo = null;
        Component fieldEditorTo = null;
        if (   DisplayType.isDate(mField.getDisplayType())
        	|| DisplayType.isNumeric(mField.getDisplayType())) {
            //  Editor To
            editorTo = WebEditorFactory.getEditor(mField, true);
            editorTo.setMandatory(false);
            editorTo.setReadWrite(true);
            editorTo.dynamicDisplay();
            editorTo.updateStyle(false);
            editorTo.addValueChangeListener(this);
            //
            if (displayLength > 0)      //  set it back
                mField.setDisplayLength(displayLength);
            fieldEditorTo = editorTo.getComponent();
            fieldEditorTo.addEventListener(Events.ON_OK,this);
        }

        Row panel = new Row();
        panel.appendChild(label);
        Div div = new Div();
        panel.appendChild(div);
        div.appendChild(fieldEditor);
        if (editorTo != null) {
        	ToolBarButton editorFlag = new ToolBarButton();
        	editorFlag.setLabel(".. " + Msg.getMsg(Env.getCtx(), "search.result.to") + " ..");
        	editorFlag.setStyle("margin-left: 5px; margin-right: 5px;");
            m_sEditorsFlag.add(editorFlag);
            editorFlag.setMode("toggle");
            div.appendChild(editorFlag);
        	div.appendChild(fieldEditorTo);       
        	fieldEditorTo.setVisible(false);
        	final Component editorRef = fieldEditorTo;
        	editorFlag.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					ToolBarButton btn = (ToolBarButton) event.getTarget();
					editorRef.setVisible(btn.isChecked());
				}
			});
            m_sEditorsTo.add(editorTo);
        } else {
            m_sEditorsFlag.add(null);
            m_sEditorsTo.add(null);
        	editor.fillHorizontal();
        	editor.updateStyle(false);
        }
        panel.appendChild(new Space());
        if (group != null)
        	panel.setGroup(group);

        contentSimpleRows.appendChild(panel);
        m_sEditors.add(editor);

        fieldEditor.addEventListener(Events.ON_OK,this);
        return true;
    }   // addSelectionColumn
    
    private void setLengthStringField(GridField field, Component fieldEditor) {
        if (DisplayType.isText(field.getVO().displayType) && fieldEditor instanceof Textbox) {
        	((Textbox) fieldEditor).setMaxlength(32767);  // a conservative max literal string - like oracle extended
        }
	}

    public void onEvent(Event event) throws Exception
    {
        	m_createNew  = false;
        if (Events.ON_CLICK.equals(event.getName()))
        {
            if(event.getTarget() instanceof Button)
            {
                Button btn = (Button)event.getTarget();

                if ("btnOkSimple".equals(btn.getName()))
                {
                    cmd_ok_Simple();
                    
                    dispose();
                }
                
                else if("btnCancel".equals(btn.getName()))
                {
                	m_isCancel = true;
                    dispose();
                }
                else if ("btnNew".equals(btn.getName()))
                {
                    m_query = MQuery.getNoRecordQuery(m_tableName, true);
                    m_total = 0;
                    m_createNew  = true;
                    m_isCancel = false;
                    dispose();
                }
                else if ("btnReset".equals(btn.getName())){
                	for (WEditor clearField : m_sEditors){
                		clearField.setValue(null);
                	}

                	for (WEditor clearField : m_sEditorsTo){
                		if (clearField != null){
                			clearField.setValue(null);
                			clearField.setVisible(false);

                			ToolBarButton moreButtor = m_sEditorsFlag.get(m_sEditorsTo.indexOf(clearField));
                			moreButtor.setChecked(false);
                		}
                	}
                }
            }
        }
        else if (Events.ON_OK.equals(event.getName()))
        {
            if (winLookupRecord.equals(event.getTarget()))
            {
                cmd_ok_Simple();
                dispose();
            }
            
            // Check simple panel fields
            for (int i = 0; i < m_sEditors.size(); i++)
            {
                WEditor editor = (WEditor)m_sEditors.get(i);
            	if (editor.getComponent() == event.getTarget())
            	{
                    cmd_ok_Simple();
                    dispose();
            	}
                WEditor editorTo = (WEditor)m_sEditorsTo.get(i);
            	if (editorTo != null && editor.getComponent() == event.getTarget())
            	{
                    cmd_ok_Simple();
                    dispose();
            	}
            }
        }

    }   //  onEvent
    
   

    private void appendCode(StringBuilder code, String columnName,
			String operator, String value1, String value2, String andOr,
			String lBrackets, String rBrackets) {
		if (code.length() > 0)
			code.append(SEGMENT_SEPARATOR);
		code.append(columnName)
			.append(FIELD_SEPARATOR)
			.append(operator)
			.append(FIELD_SEPARATOR)
			.append(value1)
			.append(FIELD_SEPARATOR)
			.append(value2)
			.append(FIELD_SEPARATOR)
			.append(andOr)
			.append(FIELD_SEPARATOR)
			.append(lBrackets)
			.append(FIELD_SEPARATOR)
			.append(rBrackets);
	}

	

	private void cmd_saveSimple(boolean saveQuery, boolean shareAllUsers)
	{
        //  Create Query String
        m_query = new MQuery(m_tableName);
        m_query.addRestriction(Env.parseContext(Env.getCtx(), m_targetWindowNo, m_whereExtended, false));
		StringBuilder code = new StringBuilder();
        //  Special Editors
        for (int i = 0; i < m_sEditors.size(); i++)
        {
            WEditor wed = (WEditor)m_sEditors.get(i);
            Object value = wed.getValue();
            String ColumnName = wed.getColumnName();
            WEditor wedTo = (WEditor)m_sEditorsTo.get(i);
            Object valueTo = null;
            if (wedTo != null && wedTo.getComponent().isVisible())
            	valueTo = wedTo.getValue();
            if (value != null && value.toString().length() > 0)
            {
            	if (valueTo != null && valueTo.toString().length() > 0) {
            		// range
                    if (log.isLoggable(Level.FINE)) {
                        StringBuilder msglog = new StringBuilder(ColumnName).append(">=").append(value).append("<=").append(valueTo);
                    	log.fine(msglog.toString());
                    }

                    GridField field = getTargetMField(ColumnName);
                    if (field.isVirtualUIColumn())
                    	continue;
                    StringBuilder ColumnSQL = new StringBuilder(field.getSearchColumnSQL());
                    m_query.addRangeRestriction(ColumnSQL.toString(), value, valueTo,
                    		ColumnName, wed.getDisplay(), wedTo.getDisplay(), true, 0);
                    appendCode(code, ColumnName, MQuery.BETWEEN, value.toString(), valueTo.toString(), "AND", "", "");
            	} else {
                    if (log.isLoggable(Level.FINE)) {
                        StringBuilder msglog = new StringBuilder(ColumnName).append("=").append(value);
                    	log.fine(msglog.toString());
                    }

                    GridField field = getTargetMField(ColumnName);
                    
                    StringBuilder ColumnSQL = new StringBuilder(field.getSearchColumnSQL());

                    // add encryption here if the field is encrypted.
                    if (field.isEncrypted()) {
                    	String Operator = MQuery.NULL;
                    	if ("Y".equals(value)){
                    		Operator = MQuery.NOT_NULL;
                    	}
                    	m_query.addRestriction(ColumnSQL.toString(), Operator, null,
                    			ColumnName, wed.getDisplay());
                    	appendCode(code, ColumnName, Operator, "", "", "AND", "", "");
                    	continue;
                    }

                    if (field.getDisplayType()==DisplayType.ChosenMultipleSelectionList||field.getDisplayType()==DisplayType.ChosenMultipleSelectionSearch||field.getDisplayType()==DisplayType.ChosenMultipleSelectionTable)
                    {
                    	String clause = DB.intersectClauseForCSV(ColumnSQL.toString(), value.toString());
                    	m_query.addRestriction(clause);
                    	continue;
                    }
                    
                    //
                    // Be more permissive for String columns
                    if (isSearchLike(field))
                    {
                    	StringBuilder valueStr = new StringBuilder(value.toString());
                        if (!valueStr.toString().endsWith("%"))
                            valueStr.append("%");
                        //
                        ColumnSQL = new StringBuilder("UPPER(").append(ColumnSQL).append(")");
                        value = valueStr.toString();
                    }
                    //
                    if (value.toString().indexOf('%') != -1) {
                        m_query.addRestriction(ColumnSQL.toString(), MQuery.LIKE, value, ColumnName, wed.getDisplay());
                        appendCode(code, ColumnName, MQuery.LIKE, value.toString(), "", "AND", "", "");
                    } else {
                    	String oper = MQuery.EQUAL;
                    	if (wedTo != null) {
                            ToolBarButton wedFlag = m_sEditorsFlag.get(i);
                            if (wedFlag.isChecked())
                            	oper = MQuery.GREATER_EQUAL;
                    	}
                        m_query.addRestriction(ColumnSQL.toString(), oper, value, ColumnName, wed.getDisplay());
                        appendCode(code, ColumnName, oper, value.toString(), "", "AND", "", "");
                    }                    
                    
            	}
            } else if (valueTo != null && valueTo.toString().length() > 0) {
            	// filled upper limit without filling lower limit
                if (log.isLoggable(Level.FINE)) {
                    StringBuilder msglog = new StringBuilder(ColumnName).append("<=").append(valueTo);
                	log.fine(msglog.toString());
                }

                GridField field = getTargetMField(ColumnName);
                StringBuilder ColumnSQL = new StringBuilder(field.getSearchColumnSQL());
                //
                m_query.addRestriction(ColumnSQL.toString(), MQuery.LESS_EQUAL, valueTo, ColumnName, wed.getDisplay());
                appendCode(code, ColumnName, MQuery.LESS_EQUAL, valueTo.toString(), "", "AND", "", "");
            }
        }   //  editors
        
	}	//	cmd_saveSimple


    private String getColumnName(ListItem row)
    {
        Listbox listColumn = (Listbox)row.getFellow("listColumn"+row.getId());
        String columnName = listColumn.getSelectedItem().getValue().toString();

        return columnName;

    }   // getColumnName

    
    public Component getEditorComponent(ListItem row, boolean to)
    {
        String columnName = getColumnName(row);
        boolean between = false;
        Listbox listOp = (Listbox) row.getFellow("listOperator"+row.getId());
        String betweenValue = listOp.getSelectedItem().getValue().toString();
        String opValue = MQuery.OPERATORS[MQuery.BETWEEN_INDEX].getValue();
        if (to &&  betweenValue != null
            && betweenValue.equals(opValue))
            between = true;

        boolean enabled = !to || (to && between);

        //  Create Editor
        GridField field = getTargetMField(columnName);        
        if(field == null) return new Label("");

        GridField findField = (GridField) field.clone(m_advanceCtx);        
        findField.setGridTab(null);
        WEditor editor = null;
        if (findField.isKey() 
        		|| (!DisplayType.isLookup(findField.getDisplayType()) && DisplayType.isID(findField.getDisplayType())))
        {
            editor = new WNumberEditor(findField);
		}
        else if (findField.getDisplayType() == DisplayType.Button)        	
        {
			if (findField.getAD_Reference_Value_ID() > 0) {		
				MLookupInfo info = MLookupFactory.getLookup_List(Env.getLanguage(Env.getCtx()), findField.getAD_Reference_Value_ID());
				info.tabNo = TABNO;
				MLookup mLookup = new MLookup(info, 0);
				editor = new WTableDirEditor(columnName, false,false, true, mLookup);
        		findField.addPropertyChangeListener(editor);

			} else {
				if (columnName.endsWith("_ID")) {
					editor = new WNumberEditor(findField);
				} else {
					editor = new WStringEditor(findField);
				}
			}
        }
        else
        {
        	//reload lookupinfo for find window
        	if (DisplayType.isLookup(findField.getDisplayType()) ) 
        	{        		
        		findField.loadLookupNoValidate(); 
        		Lookup lookup = findField.getLookup();
        		if (lookup != null && lookup instanceof MLookup)
        		{
        			MLookup mLookup = (MLookup) lookup;
        			mLookup.getLookupInfo().tabNo = TABNO;
        		}
        		editor = WebEditorFactory.getEditor(findField, true);
        		findField.addPropertyChangeListener(editor);
        	} 
        	else 
        	{
        		editor = WebEditorFactory.getEditor(findField, true);
        		findField.addPropertyChangeListener(editor);
        	}
        }
        if (editor == null)
        {
            editor = new WStringEditor(findField);
            findField.addPropertyChangeListener(editor);
        }
        setLengthStringField(findField, editor.getComponent());
        
        editor.addValueChangeListener(this);
        editor.setValue(null);
        editor.setReadWrite(enabled);
        editor.setVisible(enabled);
        editor.dynamicDisplay();
        editor.updateStyle(false);
        if (editor instanceof WPaymentEditor) {
        	((WPaymentEditor)editor).getComponent().setEnabled(true, false);
        }
        if (to)
        	row.setAttribute(FIND_ROW_EDITOR_TO, editor);
        else
        	row.setAttribute(FIND_ROW_EDITOR, editor);
        return editor.getComponent();

    }   //  getTableCellEditorComponent

    /**
     *  Get Target MField
     *  @param columnName column name
     *  @return MField
    **/
    public GridField getTargetMField (String columnName)
    {
        if (columnName == null)
            return null;
        for (int c = 0; c < m_findFields.length; c++)
        {
            GridField field = m_findFields[c];
            if (field != null && columnName.equals(field.getColumnName()))
                return field;
        }
        return null;

    }   //  getTargetMField

    /**
     *  Simple OK Button pressed
    **/
    private void cmd_ok_Simple()
    {
        m_isCancel = false; // teo_sarca [ 1708717 ]
        cmd_saveSimple(false, false);
        
        if (getNoOfRecords(m_query, true) != 0)
          dispose();

    }   //  cmd_ok_Simple
    
   

    public void dispose()
    {
        log.config("");

        //  Find SQL
        DB.close(m_pstmt);
        m_pstmt = null;
        
        //
        super.dispose();
        
    }   //  dispose

    
    private int getNoOfRecords (MQuery query, boolean alertZeroRecords)
    {
        if (log.isLoggable(Level.CONFIG)) log.config("" + query);
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        sql.append(m_tableName);
        boolean hasWhere = false;
        if (m_whereExtended != null && m_whereExtended.length() > 0)
        {
            sql.append(" WHERE ").append(m_whereExtended);
            hasWhere = true;
        }
        if (query != null && query.isActive())
        {
            if (hasWhere)
                sql.append(" AND ");
            else
                sql.append(" WHERE ");
            sql.append(query.getWhereClause());
        }
        //  Add Access
        String finalSQL = MRole.getDefault().addAccessSQL(sql.toString(),
            m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
        finalSQL = Env.parseContext(Env.getCtx(), m_targetWindowNo, finalSQL, false);
        if (log.isLoggable(Level.INFO))
        	Env.setContext(Env.getCtx(), m_targetWindowNo, TABNO, GridTab.CTX_FindSQL, finalSQL);

        //  Execute Qusery
        m_total = 999999;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = DB.createStatement();
            rs = stmt.executeQuery(finalSQL);
            if (rs.next())
                m_total = rs.getInt(1);
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, finalSQL, e);
        }
        finally
        {
        	DB.close(rs, stmt);
        	rs = null;
        	stmt = null;
        }
      
        if (m_gridTab != null && query != null && m_gridTab.isQueryMax(m_total))
        {
            FDialog.error(m_targetWindowNo, this, "FindOverMax",
                m_total + " > " + m_gridTab.getMaxQueryRecords());
            m_total = 0; 
        }
        else
            if (log.isLoggable(Level.CONFIG)) log.config("#" + m_total);
      
        return m_total;

    }   //  getNoOfRecords

   
    public MQuery getQuery()
    {
        if (m_gridTab != null && m_gridTab.isQueryMax(getTotalRecords()) && !m_isCancel)
        {
            m_query = MQuery.getNoRecordQuery (m_tableName, false);
            m_total = 0;
            log.warning("Query - over max");
        }
        else
            log.info("Query=" + m_query);
        return m_query;
    }   //  getQuery

    /**
     *  Get Total Records
     *  @return no of records
    **/
    public int getTotalRecords()
    {
        return m_total;

    }   //  getTotalRecords

    public void valueChange(ValueChangeEvent evt)
    {
        if (evt != null && evt.getSource() instanceof WEditor)
        {
            WEditor editor = (WEditor)evt.getSource();
            // Editor component
            ListCell listcell = null;
            Properties ctx = null;
            ctx = m_simpleCtx;
            
            if (evt.getNewValue() == null)
            {
            	Env.setContext(ctx, m_targetWindowNo, editor.getColumnName(), "");
            	Env.setContext(ctx, m_targetWindowNo, TABNO, editor.getColumnName(), "");
            }
            else if (evt.getNewValue() instanceof Boolean)
            {
            	Env.setContext(ctx, m_targetWindowNo, editor.getColumnName(), (Boolean)evt.getNewValue());
            	Env.setContext(ctx, m_targetWindowNo, TABNO, editor.getColumnName(), (Boolean)evt.getNewValue());
            }
            else if (evt.getNewValue() instanceof Timestamp)
            {
            	Env.setContext(ctx, m_targetWindowNo, editor.getColumnName(), (Timestamp)evt.getNewValue());
            	Env.setContext(ctx, m_targetWindowNo, TABNO + "|" + editor.getColumnName(), (Timestamp)evt.getNewValue());
            }
            else
            {
            	Env.setContext(ctx, m_targetWindowNo, editor.getColumnName(), evt.getNewValue().toString());
            	Env.setContext(ctx, m_targetWindowNo, TABNO, editor.getColumnName(), evt.getNewValue().toString());
            }
            
            dynamicDisplay(editor, listcell);
        }
    }

	private void dynamicDisplay(WEditor editor, ListCell listcell) {
		for (int i = 0; i < m_sEditors.size(); i++)
	    {
	        WEditor wed = (WEditor)m_sEditors.get(i);
	        if (wed == editor)
	        	continue;
	        if (wed.getGridField() != null && wed.getGridField().isLookup())
	        {
	        	Lookup lookup = wed.getGridField().getLookup();
	        	if (!Util.isEmpty(lookup.getValidation()))
	        	{
	        		wed.dynamicDisplay();
	        		wed = m_sEditorsTo.get(i);
	                if (wed != null && wed != editor)
	                	wed.dynamicDisplay();
	        	}
	        }
	        
	    }
	}

	public void OnPostVisible() {
		removeAttribute(ON_POST_VISIBLE_ATTR);
		if (m_sEditors.size() > 0)
			Clients.response(new AuFocus(m_sEditors.get(0).getComponent()));
	}

	
	public boolean isCancel() {
		return m_isCancel;
	}

	public boolean isCreateNew() {
		return m_createNew;
	}

	@Override
	public boolean setVisible(boolean visible) {
		boolean ret = super.setVisible(visible);
		if (visible) {
			if (getAttribute(ON_POST_VISIBLE_ATTR) == null) {
				setAttribute(ON_POST_VISIBLE_ATTR, Boolean.TRUE);
				Events.echoEvent("OnPostVisible", this, null);
			}
		} else {
			detach();
		}
		return ret;
	}
	
	private boolean isSearchLike(GridField field)
	{
		return DisplayType.isText(field.getDisplayType()) && !field.isVirtualColumn()
		&& (field.isSelectionColumn() || MColumn.isSuggestSelectionColumn(field.getColumnName(), true));
	}

	
	private void setStatusDB (int currentCount)
	{
		StringBuilder text = new StringBuilder(" ").append(Msg.getMsg(Env.getCtx(), "Records")).append(": ").append(m_total).append(" ");
		statusBar.setValue(text.toString());
	}	//	setDtatusDB
	
}
