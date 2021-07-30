

package eone.webui.panel;

import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.ValueNamePair;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;
import org.zkoss.zul.ext.Sortable;

import eone.base.model.GridField;
import eone.base.model.MInfoColumn;
import eone.base.model.MInfoWindow;
import eone.base.model.MRole;
import eone.base.model.MSysConfig;
import eone.base.model.MTable;
import eone.exceptions.EONEException;
import eone.webui.AdempiereWebUI;
import eone.webui.ClientInfo;
import eone.webui.LayoutUtils;
import eone.webui.apps.AEnv;
import eone.webui.apps.BusyDialog;
import eone.webui.component.ConfirmPanel;
import eone.webui.component.ListModelTable;
import eone.webui.component.WListItemRenderer;
import eone.webui.component.WListbox;
import eone.webui.component.Window;
import eone.webui.editor.WEditor;
import eone.webui.event.ValueChangeEvent;
import eone.webui.event.ValueChangeListener;
import eone.webui.event.WTableModelEvent;
import eone.webui.event.WTableModelListener;
import eone.webui.factory.InfoManager;
import eone.webui.part.ITabOnSelectHandler;
import eone.webui.part.WindowContainer;
import eone.webui.session.SessionManager;
import eone.webui.util.ZKUpdateUtil;


public abstract class InfoPanel extends Window implements EventListener<Event>, WTableModelListener, Sortable<Object>, IHelpContext
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7893447773574337316L;
	private final static int DEFAULT_PAGE_SIZE = 100;
	private final static int DEFAULT_PAGE_PRELOAD = 4;
	protected Map<String, WEditor> editorMap = new HashMap<String, WEditor>();
	protected int pageSize;
	public LinkedHashMap<KeyNamePair,LinkedHashMap<String, Object>> m_values = null;
	
	protected boolean isIgnoreCacheAll = true;
	// Num of page preload, default is 2 page before current and 2 page after current 
	protected int numPagePreLoad = MSysConfig.getIntValue(MSysConfig.ZK_INFO_NUM_PAGE_PRELOAD, DEFAULT_PAGE_PRELOAD);
	// max end index is integer.max_value - 1, not integer.max_value.
	protected int extra_max_row = 1;
	
	protected MInfoColumn keyColumnOfView = null;
	
	
	protected int indexKeyOfView = -1;
	
	protected boolean isIDColumnKeyOfView = false;
	protected boolean hasRightQuickEntry = true;
	protected boolean isHasNextPage = false;
	
	protected Map<Integer, List<Object>> recordSelectedData = new HashMap<Integer, List<Object>>();
	
	
	protected boolean isRequeryByRunSuccessProcess = false;
	
	
    public static InfoPanel create (int WindowNo,
            String tableName, String keyColumn, String value,
            boolean multiSelection, String whereClause)
    {
        return InfoManager.create(WindowNo, tableName, keyColumn, value, multiSelection, whereClause, true);
    }

    public static void showPanel (String tableName)
	{
		InfoPanel info = InfoManager.create(0, tableName, tableName + "_ID", "", false, "", false);
		info.setAttribute(Window.MODE_KEY, Window.MODE_EMBEDDED);
		AEnv.showWindow(info);
	}	// showPanel

	/** Window Width                */
	static final int        INFO_WIDTH = 800;
	protected boolean m_lookup;
	protected int m_infoWindowID;
	
	
	protected InfoPanel (int WindowNo,
		String tableName, String keyColumn,boolean multipleSelection,
		 String whereClause)
	{
		this(WindowNo, tableName, keyColumn, multipleSelection, whereClause, true);
	}

	protected InfoPanel (int WindowNo,
			String tableName, String keyColumn,boolean multipleSelection,
			 String whereClause, boolean lookup){
		this(WindowNo, tableName, keyColumn, multipleSelection, whereClause,
				lookup, 0);
	}
	
	/**************************************************
     *  Detail Constructor
     * @param WindowNo  WindowNo
     * @param tableName tableName
     * @param keyColumn keyColumn
     * @param whereClause   whereClause
	 */
	protected InfoPanel (int WindowNo,
		String tableName, String keyColumn,boolean multipleSelection,
		 String whereClause, boolean lookup, int ADInfoWindowID)
	{		
		if (WindowNo <= 0) {
			p_WindowNo = SessionManager.getAppDesktop().registerWindow(this);
		} else {
			p_WindowNo = WindowNo;
		}
		if (log.isLoggable(Level.INFO))
			log.info("WinNo=" + WindowNo + " " + whereClause);
		p_tableName = tableName;
		this.m_infoWindowID = ADInfoWindowID;
		p_keyColumn = keyColumn;
		
        p_multipleSelection = multipleSelection;
        m_lookup = lookup;
        loadInfoWindowData();
		if (whereClause == null || whereClause.indexOf('@') == -1)
			p_whereClause = whereClause == null ? "" : whereClause;
		else
		{
			p_whereClause = Env.parseContext(Env.getCtx(), p_WindowNo, whereClause, false, false);
			if (p_whereClause.length() == 0)
				log.log(Level.SEVERE, "Cannot parse context= " + whereClause);
		}

		pageSize = MSysConfig.getIntValue(MSysConfig.ZK_PAGING_SIZE, DEFAULT_PAGE_SIZE, Env.getAD_Client_ID(Env.getCtx()));
		if (infoWindow != null && infoWindow.getPagingSize() > 0)
			pageSize = infoWindow.getPagingSize();

		init();

		this.setAttribute(ITabOnSelectHandler.ATTRIBUTE_KEY, new ITabOnSelectHandler() {
			public void onSelect() {
				scrollToSelectedRow();
			}
		});
		
		setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "infopanel");
		
		addEventListener(WindowContainer.ON_WINDOW_CONTAINER_SELECTION_CHANGED_EVENT, this);
		addEventListener(Events.ON_CLOSE, this);
		
	}	//	InfoPanel

	private void init()
	{
		if (isLookup())
		{
			setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
			setBorder("normal");
			setClosable(true);
			int height = ClientInfo.get().desktopHeight;
			int width = ClientInfo.get().desktopWidth;
			if (width <= ClientInfo.MEDIUM_WIDTH)
			{
				ZKUpdateUtil.setWidth(this, "100%");
				ZKUpdateUtil.setHeight(this, "100%");
			}
			else
			{
				height = height * 80 / 100;
	    		width = width * 40 / 100;
	    		ZKUpdateUtil.setWidth(this, width + "px");
	    		ZKUpdateUtil.setHeight(this, height + "px");
			}
    		this.setContentStyle("overflow: auto");
		}
		else
		{
			setAttribute(Window.MODE_KEY, Window.MODE_EMBEDDED);
			setBorder("none");
			ZKUpdateUtil.setWidth(this, "100%");
			ZKUpdateUtil.setHeight(this, "100%");
		}

		confirmPanel = new ConfirmPanel(true, true, false, false, false, false);  // Elaine 2008/12/16 
		confirmPanel.addComponentsLeft(confirmPanel.createButton(ConfirmPanel.A_NEW));
        confirmPanel.addActionListener(Events.ON_CLICK, this);
        ZKUpdateUtil.setHflex(confirmPanel, "1");
        if (ClientInfo.isMobile())
        {
        	if (ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH) || ClientInfo.maxHeight(ClientInfo.SMALL_HEIGHT))
        	{
        		confirmPanel.addButtonSclass("btn-small small-img-btn");
        		confirmPanel.useSmallButtonClassForSmallScreen();
        	}
        }

        // Elaine 2008/12/16
        if (confirmPanel.getButton(ConfirmPanel.A_CUSTOMIZE) != null)
        	confirmPanel.getButton(ConfirmPanel.A_CUSTOMIZE).setVisible(hasCustomize());
        if (confirmPanel.getButton(ConfirmPanel.A_HISTORY) != null)
        	confirmPanel.getButton(ConfirmPanel.A_HISTORY).setVisible(hasHistory());
        if (confirmPanel.getButton(ConfirmPanel.A_ZOOM) != null)
        	confirmPanel.getButton(ConfirmPanel.A_ZOOM).setVisible(hasZoom());
        if (confirmPanel.getButton(ConfirmPanel.A_NEW) != null)
        	confirmPanel.getButton(ConfirmPanel.A_NEW).setVisible(hasNew());
		//
		if (!isLookup())
		{
			confirmPanel.getButton(ConfirmPanel.A_OK).setVisible(false);
		}

        this.setSizable(true);
        this.setMaximizable(true);

        this.addEventListener(Events.ON_OK, this);
        if (isLookup())
        	addEventListener(Events.ON_CANCEL, this);
        contentPanel.setOddRowSclass(null);
        contentPanel.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "infoListbox");
        contentPanel.addEventListener("onAfterRender", this);
        contentPanel.setSclass("z-word-nowrap");
        
        this.setSclass("info-panel");
	}  //  init
	protected ConfirmPanel confirmPanel;
	protected int				p_WindowNo;
	protected String            p_tableName;
	protected String            p_keyColumn;
	protected boolean			p_multipleSelection;
	protected String			p_whereClause = "";
	protected StatusBarPanel statusBar = new StatusBarPanel();
	/**                    */
    private List<Object> line;

	private boolean			    m_ok = false;
	private boolean			    m_cancel = false;
	private ArrayList<Integer>	m_results = new ArrayList<Integer>(3);

    private ListModelTable model;
	protected ColumnInfo[]     p_layout;
	protected String              m_sqlMain;
	protected String              m_sqlCount;
	protected String              m_sqlOrder;
	private String              m_sqlUserOrder;
	protected int              	  indexOrderColumn = -1;
	protected String              sqlOrderColumn;
	protected Boolean             isColumnSortAscending = null;
    private ArrayList<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();
	protected boolean	        p_loadedOK = false;
	private int					m_SO_Window_ID = -1;
	private int					m_PO_Window_ID = -1;
	
	protected MInfoWindow infoWindow;

	/**	Logger			*/
	protected transient CLogger log = CLogger.getCLogger(getClass());

	protected WListbox contentPanel = new WListbox();
	protected Paging paging;
	protected int pageNo;
	protected int m_count;
	private int cacheStart;
	private int cacheEnd;
	private boolean m_useDatabasePaging = false;
	private BusyDialog progressWindow;
	private int m_lastSelectedIndex = -1;
	protected GridField m_gridfield;

	
	protected boolean isQueryByUser = false;
	
	protected String prevWhereClause = null;
	
	protected List<Object> prevParameterValues = null;
	protected List<String> prevQueryOperators = null;
	protected List<WEditor> prevRefParmeterEditor = null;
	private static final String[] lISTENER_EVENTS = {};

	protected Collection<KeyNamePair> m_viewIDMap = new ArrayList <KeyNamePair>();
	
	
	protected Map <Integer, Integer> columnDataIndex = new HashMap <Integer, Integer> ();
	
	protected boolean isMustUpdateColumnIndex = true;
	
	protected int indexColumnCount = 0;
	
	protected List <Integer> lsReadedColumn = new ArrayList <Integer> ();
	
	
	public boolean loadedOK()
	{
		return p_loadedOK;
	}   //  loadedOK

	/**
	 *	Set Status Line
	 *  @param text text
	 *  @param error error
	 */
	public void setStatusLine (String text, boolean error)
	{
		statusBar.setStatusLine(text, error);
	}	//	setStatusLine

	
	public void setStatusSelected ()
	{
		if (!p_multipleSelection)
			return;
		
		int selectedCount = recordSelectedData.size();
		
		for (int rowIndex = 0; rowIndex < contentPanel.getModel().getRowCount(); rowIndex++){			
			Integer keyCandidate = getColumnValue(rowIndex);
			
			@SuppressWarnings("unchecked")
			List<Object> candidateRecord = (List<Object>)contentPanel.getModel().get(rowIndex);
						
			if (contentPanel.getModel().isSelected(candidateRecord)){
				if (!recordSelectedData.containsKey(keyCandidate)){
					selectedCount++;
				}
			}else{
				if (recordSelectedData.containsKey(keyCandidate)){// unselected record
					selectedCount--;
				}
			}
		}	
		
		String msg = Msg.getMsg(Env.getCtx(), "IWStatusSelected", new Object [] {String.valueOf(selectedCount)});
		statusBar.setSelectedRowNumber(msg);
	}	//	setStatusDB
	
	protected void prepareTable (ColumnInfo[] layout,
            String from,
            String where,
            String orderBy)
	{
        String sql =contentPanel.prepareTable(layout, from,
                where,p_multipleSelection,
                getTableName(),false);
        p_layout = contentPanel.getLayout();
		m_sqlMain = sql;
		m_sqlCount = "SELECT COUNT(*) FROM " + from + " WHERE " + where;
		//
		m_sqlOrder = "";
		if (orderBy != null && orderBy.trim().length() > 0)
			m_sqlOrder = " ORDER BY " + orderBy;
	}   //  prepareTable

	protected boolean isLoadPageNumber(){
		return infoWindow == null || infoWindow.isLoadPageNum();
	}
	
	/**************************************************************************
	 *  Execute Query
	 */
	protected void executeQuery()
	{
		line = new ArrayList<Object>();
		setCacheStart(-1);
		cacheEnd = -1;
		if (isLoadPageNumber())
			testCount();
		else
			m_count = Integer.MAX_VALUE;
		
		if (m_count > 0)
		{
			m_useDatabasePaging = isIgnoreCacheAll || (m_count > 1000);
			if (m_useDatabasePaging)
			{
				return ;
			}
			else
			{
				readLine(0, -1);
			}
		}
	}

	private void readData(ResultSet rs) throws SQLException {
		int colOffset = 1;  //  columns start with 1
		List<Object> data = new ArrayList<Object>();
	
		for (int col = 0; col < p_layout.length; col++)
		{
			Object value = null;
			Class<?> c = p_layout[col].getColClass();
			int colIndex = col + colOffset;
			if (c == IDColumn.class)
			{
		        value = new IDColumn(rs.getInt(colIndex));

			}
			else if (c == Boolean.class)
		        value = Boolean.valueOf("Y".equals(rs.getString(colIndex)));
			else if (c == Timestamp.class)
		        value = rs.getTimestamp(colIndex);
			else if (c == BigDecimal.class)
		        value = rs.getBigDecimal(colIndex);
			else if (c == Double.class)
		        value = Double.valueOf(rs.getDouble(colIndex));
			else if (c == Integer.class)
		        value = Integer.valueOf(rs.getInt(colIndex));
			else if (c == KeyNamePair.class)
			{				
				if (p_layout[col].isKeyPairCol())
				{
					String display = rs.getString(colIndex);
					int key = rs.getInt(colIndex+1);
					if (! rs.wasNull()) {
		                value = new KeyNamePair(key, display);
					}
	
					colOffset++;
				}
				else
				{
					int key = rs.getInt(colIndex);
					if (! rs.wasNull()) {
						WEditor editor = editorMap.get(p_layout[col].getColSQL());
						if (editor != null)
						{
							editor.setValue(key);
							value = new KeyNamePair(key, editor.getDisplayTextForGridView(key));
						}					
						else
						{
							value = new KeyNamePair(key, Integer.toString(key));
						}
					}
				}
			}
			else if (c == ValueNamePair.class)
			{
				String key = rs.getString(colIndex);
				WEditor editor = editorMap.get(p_layout[col].getColSQL());
				if (editor != null)
				{
					value = new ValueNamePair(key, editor.getDisplayTextForGridView(key));
				}					
				else
				{
					value = new ValueNamePair(key, key);
				}
			}
			else
			{
		        value = rs.getString(colIndex);
		        if (! rs.wasNull()) {
					WEditor editor = editorMap.get(p_layout[col].getColSQL());
					if (editor != null && editor.getGridField() != null && editor.getGridField().isLookup())
					{
						editor.setValue(value);
						value = editor.getDisplay();
					}
		        }
			}
			data.add(value);
		}
		
        line.add(data);
        
        
	}
	
    protected void renderItems()
    {
        if (m_count > 0)
        {
        	if (m_count > pageSize)
        	{
        		if (paging == null)
        		{
	        		paging = new Paging();
	    			paging.setPageSize(pageSize);
	    			paging.setTotalSize(m_count);
	    			paging.setDetailed(true);
	    			paging.addEventListener(ZulEvents.ON_PAGING, this);
        		}
        		else
        		{
        			paging.setTotalSize(m_count);
        			paging.setActivePage(0);
        		}
    			List<Object> subList = readLine(0, pageSize);
    			model = new ListModelTable(subList);
    			model.setSorter(this);
	            model.addTableModelListener(this);
	            model.setMultiple(p_multipleSelection);
	            contentPanel.setData(model, null);

	            pageNo = 0;
        	}
        	else
        	{
        		if (paging != null)
        		{
        			paging.setTotalSize(m_count);
        			paging.setActivePage(0);
        			pageNo = 0;
        		}
	            model = new ListModelTable(readLine(0, -1));
	            model.setSorter(this);
	            model.addTableModelListener(this);
	            model.setMultiple(p_multipleSelection);
	            contentPanel.setData(model, null);
        	}
        }
        else
        {
        	if (paging != null)
    		{
    			paging.setTotalSize(m_count);
    			paging.setActivePage(0);
    			pageNo = 0;
    		}
        	model = new ListModelTable(new ArrayList<Object>());
        	model.setSorter(this);
            model.addTableModelListener(this);
            model.setMultiple(p_multipleSelection);
            contentPanel.setData(model, null);
        }
        restoreSelectedInPage();
        updateStatusBar (m_count);
        setStatusSelected ();
        addDoubleClickListener();
        
        if (paging != null && paging.getParent() == null)
        	insertPagingComponent();
    }

    protected void updateStatusBar (int no){
    	setStatusLine((no == Integer.MAX_VALUE?"?":Integer.toString(no)) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
        //setStatusDB(no == Integer.MAX_VALUE?"?":Integer.toString(no));
    }
    
    private List<Object> readLine(int start, int end) {
    	//cacheStart & cacheEnd - 1 based index, start & end - 0 based index
    	if (getCacheStart() >= 1 && cacheEnd > getCacheStart())
    	{
    		if (m_useDatabasePaging)
    		{
    			if (start+1 >= getCacheStart() && end+1 <= cacheEnd)
    			{
    				return end == -1 ? line : getSubList(start-getCacheStart()+1, end-getCacheStart()+1, line);
    			}
    		}
    		else
    		{
    			if (end > cacheEnd || end <= 0)
    			{
    				end = cacheEnd;
    			}
    			return getSubList (start, end, line);
    		}
    	}

    	setCacheStart(getOverIntValue((long)start + 1 - (pageSize * numPagePreLoad)));
    	if (getCacheStart() <= 0)
    		setCacheStart(1);

    	if (end == -1)
    	{
    		cacheEnd = m_count;
    	}
    	else
    	{
	    	cacheEnd = getOverIntValue(end + 1 + (pageSize * numPagePreLoad));
	    	if (cacheEnd > m_count)
	    		cacheEnd = m_count;
    	}

    	line = new ArrayList<Object>();

    	PreparedStatement m_pstmt = null;
		ResultSet m_rs = null;
		String dataSql = null;
		
		long startTime = System.currentTimeMillis();
			//

        dataSql = buildDataSQL(start, end);
        isHasNextPage = false;
        if (log.isLoggable(Level.FINER))
        	log.finer(dataSql);
        Trx trx = null;
		try
		{
			String trxName = Trx.createTrxName("InfoPanelLoad:");
			trx  = Trx.get(trxName, true);
			trx.setDisplayName(getClass().getName()+"_readLine");
			m_pstmt = DB.prepareStatement(dataSql, trxName);
			setParameters (m_pstmt, false);	//	no count
			if (log.isLoggable(Level.FINE))
				log.fine("Start query - " + (System.currentTimeMillis()-startTime) + "ms");
			m_pstmt.setFetchSize(100);
			m_rs = m_pstmt.executeQuery();
			if (log.isLoggable(Level.FINE))
				log.fine("End query - " + (System.currentTimeMillis()-startTime) + "ms");
			//skips the row that we dont need if we can't use native db paging
			if (end > start && m_useDatabasePaging && !DB.getDatabase().isPagingSupported())
			{
				for (int i = 0; i < getCacheStart() - 1; i++)
				{
					if (!m_rs.next())
						break;
				}
			}

			int rowPointer = getCacheStart()-1;
			
			while (m_rs.next())
			{
				rowPointer++;
				// reset list column readed to start new round
				lsReadedColumn.clear();
				
				readData(m_rs);
				// just set column index only one time.
				isMustUpdateColumnIndex = false;
				
				//check now of rows loaded, break if we hit the suppose end
				if (m_useDatabasePaging && rowPointer >= cacheEnd)
				{
					isHasNextPage = true;
					break;
				}
			}
		}

		catch (SQLException e)
		{
			log.log(Level.SEVERE, dataSql, e);
		}

		finally
		{
			DB.close(m_rs, m_pstmt);
			trx.close();
		}

		if (end > cacheEnd || end <= 0)
		{
			end = cacheEnd;
		}
		validateEndPage ();

		if (end == -1) 
		{
			return line;
		}
		else
		{
			int fromIndex = start-getCacheStart()+1;
			int toIndex = end-getCacheStart()+1;
			return getSubList(fromIndex, toIndex, line);
		}
	}

    /**
     * after query from database, process validate.
     * if end page include in cache, process calculate total record
     * if current page is out of page (no record is query) process query count to detect end page
     */
    protected void validateEndPage (){
    	if (paging == null || isLoadPageNumber())
    		return;
    	
    	if (!isHasNextPage){
    		int extraPage = ((line.size() % pageSize > 0)?1:0);
    		int pageInCache = line.size() / pageSize + extraPage;
    		
    		if (pageInCache == 0 || pageInCache <= numPagePreLoad){
    			// selected page is out of page
    			testCount();
    			extraPage = ((m_count  % pageSize > 0)?1:0);
        		pageInCache = m_count  / pageSize + extraPage;    			
    			// this one will set current page to end page
    			paging.setTotalSize(m_count);
    			Event pagingEvent = new PagingEvent("onPaging", paging, paging.getPageCount() - 1);
    			Events.postEvent(pagingEvent);
    		}else if (pageInCache > numPagePreLoad){
    			// current page isn't end page. but page in cache has end page.
    			int prePage = pageNo - numPagePreLoad;
    			int readTotalRecord = (prePage > 0?prePage:0) * pageSize + line.size();
    			paging.setTotalSize(readTotalRecord);
    			m_count = readTotalRecord;
    		}
    		
    		updateStatusBar (m_count);
    	}
    }
    
    
    protected List<Object> getSubList (int fromIndex, int toIndex, List<Object> line){
    	if (toIndex > line.size())
    		toIndex = line.size();
    	
    	if (fromIndex >= line.size())
    		fromIndex = line.size();
    	
    	// case line.size = 0
    	if (fromIndex < 0)
    		fromIndex = 0;
    	
    	return line.subList(fromIndex, toIndex);
    }
    
    /**
     * when calculator value at bound, sometime value is overflow by data type
     * this function calculator at high type for avoid it
     * @param overValue
     * @return
     */
    protected int getOverIntValue (long overValue){
    	return getOverIntValue (overValue, 0);
    }
    
    /**
     * see {@link #getOverIntValue(long)}. when value over max_value set it near max_value.
     * @param overValue
     * @param extra
     * @return
     */
    protected int getOverIntValue (long overValue, int extra){
    	if (overValue >= Integer.MAX_VALUE)
    		overValue = Integer.MAX_VALUE - extra;
    	
    	return (int)overValue;
    }
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
        
        sql.append(getUserOrderClause());
        
        dataSql = Msg.parseTranslation(Env.getCtx(), sql.toString());    //  Variables
        dataSql = MRole.getDefault().addAccessSQL(dataSql, getTableName(),
            MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
        if (end > start && m_useDatabasePaging && DB.getDatabase().isPagingSupported())
        {
        	dataSql = DB.getDatabase().addPagingSQL(dataSql, getCacheStart(), cacheEnd);
        }
		return dataSql;
	}

	protected void validateOrderIndex() {
		if (indexOrderColumn > 0 && (indexOrderColumn + 1 > p_layout.length || !p_layout[indexOrderColumn].getColSQL().trim().equals(sqlOrderColumn))) {
			for (int testIndex = 0; testIndex < p_layout.length; testIndex++) {
				if (p_layout[testIndex].getColSQL().trim().equals(sqlOrderColumn)) {
					indexOrderColumn = testIndex;
					break;
				}
			}
			
			if (indexOrderColumn > 0 && (indexOrderColumn + 1 > p_layout.length || !p_layout[indexOrderColumn].getColSQL().trim().equals(sqlOrderColumn))) {
				indexOrderColumn = -1;
				sqlOrderColumn = null;
				m_sqlUserOrder = null;
			}
		}
			
	}
	/**
	 * build order clause of current sort order, and save it to m_sqlUserOrder
	 * @return
	 */
	protected String getUserOrderClause() {
		validateOrderIndex();
		if (indexOrderColumn < 0) {
			return m_sqlOrder;
		}
		
		if (m_sqlUserOrder == null) {
			m_sqlUserOrder = getUserOrderClause (indexOrderColumn);
		}
		return m_sqlUserOrder;
	}
	
	
	protected String getUserOrderClause(int col) {
		String colsql = p_layout[col].getColSQL().trim();
		int lastSpaceIdx = colsql.lastIndexOf(" ");
		if (lastSpaceIdx > 0)
		{
			String tmp = colsql.substring(0, lastSpaceIdx).trim();
			char last = tmp.charAt(tmp.length() - 1);

			String alias = colsql.substring(lastSpaceIdx).trim();
			boolean hasAlias = alias.matches("^[a-zA-Z_][a-zA-Z0-9_]*$"); // valid SQL alias - starts with letter then digits, letters, underscore

			if (tmp.toLowerCase().endsWith("as") && hasAlias)
			{
				colsql = alias;
			}
			else if (!(last == '*' || last == '-' || last == '+' || last == '/' || last == '>' || last == '<' || last == '='))
			{
				if (alias.startsWith("\"") && alias.endsWith("\""))
				{
					colsql = alias;
				}
				else
				{
					if (hasAlias)
					{
						colsql = alias;
					}
				}
			}
		}
		
		return String.format(" ORDER BY %s %s ", colsql, isColumnSortAscending? "" : "DESC");
	}

    private void addDoubleClickListener() {
    	Iterator<EventListener<? extends Event>> i = contentPanel.getEventListeners(Events.ON_DOUBLE_CLICK).iterator();
		while (i.hasNext()) {
			if (i.next() == this)
				return;
		}
		contentPanel.addEventListener(Events.ON_DOUBLE_CLICK, this);
		contentPanel.addEventListener(Events.ON_SELECT, this);
	}

    protected void insertPagingComponent() {
		contentPanel.getParent().insertBefore(paging, contentPanel.getNextSibling());
	}

    public Vector<String> getColumnHeader(ColumnInfo[] p_layout)
    {
        Vector<String> columnHeader = new Vector<String>();

        for (ColumnInfo info: p_layout)
        {
             columnHeader.add(info.getColHeader());
        }
        return columnHeader;
    }
	/**
	 * 	Test Row Count
	 *	@return true if display
	 */
	protected boolean testCount()
	{
		long start = System.currentTimeMillis();
		String dynWhere = getSQLWhere();
		StringBuilder sql = new StringBuilder (m_sqlCount);

		if (dynWhere.length() > 0)
			sql.append(dynWhere);   //  includes first AND

		String countSql = Msg.parseTranslation(Env.getCtx(), sql.toString());	//	Variables
		if (countSql.trim().endsWith("WHERE")) {
			countSql = countSql.trim();
			countSql = countSql.substring(0, countSql.length() - 5);
		}
		countSql = MRole.getDefault().addAccessSQL	(countSql, getTableName(),
													MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
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
			rs = null;
			pstmt = null;
		}

		if (log.isLoggable(Level.FINE))
			log.fine("#" + m_count + " - " + (System.currentTimeMillis()-start) + "ms");

		return true;
	}	//	testCount


	protected void saveSelection ()
	{
		//	Already disposed
		if (contentPanel == null)
			return;

		if (log.isLoggable(Level.CONFIG)) log.config( "OK=" + m_ok);
		// clear prev selected result
		m_results.clear();

		if (!m_ok)      //  did not press OK
		{
			contentPanel = null;
			this.detach();
            return;
		}

		//	Multi Selection
		if (p_multipleSelection)
		{
			m_results.addAll(getSelectedRowKeys());
		}
		else    //  singleSelection
		{
			Integer data = getSelectedRowKey();
			if (data != null)
				m_results.add(data);
		}

		if (log.isLoggable(Level.CONFIG)) log.config(getSelectedSQL());


	}	//	saveSelection
	
	protected Integer getSelectedRowKey()
	{
		Integer key = contentPanel.getSelectedRowKey();

		return key;
	}   //  getSelectedRowKey

	
    protected ArrayList<Integer> getSelectedRowKeys()
    {
        ArrayList<Integer> selectedDataList = new ArrayList<Integer>();
        Collection<Integer> lsKeyValueOfSelectedRow = getSelectedRowInfo().keySet();
        if (lsKeyValueOfSelectedRow.size() == 0)
        {
            return selectedDataList;
        }

        if (p_multipleSelection)
        {        	
        	for (Integer key : lsKeyValueOfSelectedRow){        		
        		selectedDataList.add(key);
        	}
        }

        return selectedDataList;
    }   //  getSelectedRowKeys

    /**
	 *	Get selected Keys as Collection
	 *  @deprecated use getSaveKeys
	 *  @return selected keys (Integers)
	 */
	public Collection<Integer> getSelectedKeysCollection()
	{
		m_ok = true;
		if (!m_ok || m_results.size() == 0)
			return null;	
		return m_results;
	}

	
	
	protected boolean isNeedAppendKeyViewData (){
		return false;
	}
		
	
	protected boolean isIDColumn(Object keyData, boolean isCheckNull){
		if (isCheckNull && keyData == null){
			EONEException ex = getKeyNullException();
			log.severe(ex.getMessage());
			throw ex;
		}
		
		if (keyData != null && keyData instanceof IDColumn){
			return true;
		}
		
		return false;
	}
	
	
	protected boolean isIDColumn(Object keyData){
		return isIDColumn(keyData, false);
	}
	
	
	protected void updateListSelected (){
		for (int rowIndex = 0; rowIndex < contentPanel.getModel().getRowCount(); rowIndex++){			
			Integer keyCandidate = getColumnValue(rowIndex);
			
			@SuppressWarnings("unchecked")
			List<Object> candidateRecord = (List<Object>)contentPanel.getModel().get(rowIndex);
					
			if (contentPanel.getModel().isSelected(candidateRecord)){
				recordSelectedData.put(keyCandidate, candidateRecord);// add or update selected record info				
			}else{
				if (recordSelectedData.containsKey(keyCandidate)){// unselected record
					List<Object> recordSelected = recordSelectedData.get(keyCandidate);
					IDColumn idcSel = null;
					if (recordSelected.get(0) instanceof IDColumn) {
						idcSel = (IDColumn) recordSelected.get(0);
					}
					IDColumn idcCan = null;
					if (candidateRecord.get(0) instanceof IDColumn) {
						idcCan = (IDColumn) candidateRecord.get(0);
					}
					if (idcSel != null && idcCan != null && idcSel.getRecord_ID().equals(idcCan.getRecord_ID())) {
						recordSelected.set(0, candidateRecord.get(0)); // set same IDColumn for comparison
					}
					if (recordSelected.equals(candidateRecord)) {
						recordSelectedData.remove(keyCandidate);
					}
				}
			}
			
		}		    
	}
	
	/**
	 * get data index of keyView
	 * @return
	 */
	protected int getIndexKeyColumnOfView (){
		if (keyColumnOfView == null){
			return contentPanel.getKeyColumnIndex();
		}else if (isNeedAppendKeyViewData()){
			return columnDataIndex.get(keyColumnOfView.getInfoColumnID()) + p_layout.length;
		}else{
			return indexKeyOfView;
		}
	}	
	
	/**
	 * go through all data record, in case key value is in {@link #recordSelectedData}, mark it as selected record
	 */
	protected void restoreSelectedInPage (){
		if (!p_multipleSelection)
			return;
		
		Collection<Object> lsSelectionRecord = new ArrayList<Object>();
		for (int rowIndex = 0; rowIndex < contentPanel.getModel().getRowCount(); rowIndex++){
			Integer keyViewValue = getColumnValue(rowIndex);
			if (recordSelectedData.containsKey(keyViewValue)){
				Object row = contentPanel.getModel().get(rowIndex);
								
				if(onRestoreSelectedItemIndexInPage(keyViewValue, rowIndex, row)) // F3P: provide an hook for operations on restored index
					lsSelectionRecord.add(row);
			}
		}
		
		contentPanel.getModel().setSelection(lsSelectionRecord);
	}
	
	
	public boolean onRestoreSelectedItemIndexInPage(Integer keyViewValue, int rowIndex, Object row)
	{
		return true;
	}
		
	protected EONEException getKeyNullException (){
		String errorMessage = String.format("has null value at column %1$s use as key of view in info window %2$s", 
				keyColumnOfView == null ? p_keyColumn : keyColumnOfView, infoWindow.getName());
		return new EONEException(errorMessage);
	}
	
	protected Integer getColumnValue (int rowIndex){
		
		int keyIndex = getIndexKeyColumnOfView();
		Integer keyValue = null;
    	// get row data from model
		Object keyColumValue = contentPanel.getModel().getDataAt(rowIndex, keyIndex);
		// throw exception when value is null
		if (keyColumValue == null){
			EONEException ex = getKeyNullException();
			log.severe(ex.getMessage());
			throw ex;
		}
		
		// IDColumn is recreate after change page, because use value of IDColumn
		if (keyColumValue != null && keyColumValue instanceof IDColumn){
			keyColumValue = ((IDColumn)keyColumValue).getRecord_ID();
		}
		
		if (keyColumValue instanceof Integer){
			keyValue = (Integer)keyColumValue;
		}else {
			String msg = "keyView column must be integer";
			EONEException ex = new EONEException (msg);
			log.severe(msg);
			throw ex;
		}
		
		return (Integer)keyValue;
	}
	
	protected void syncSelectedAfterRequery (){
		if (isRequeryByRunSuccessProcess){
			isRequeryByRunSuccessProcess = false;
		}
	}
	
	/**
	 * update list column key value of selected record and return this list
	 * @return {@link #recordSelectedData} after update 
	 */
	public Map<Integer, List<Object>> getSelectedRowInfo (){
		updateListSelected();
		return recordSelectedData;
	}
	
	
	/**
	 *	Get selected Keys
	 *  @return selected keys (Integers)
	 */
	public Object[] getSelectedKeys()
	{
		if (!m_ok || m_results.size() == 0)
			return null;
		return m_results.toArray(new Integer[0]);
	}	//	getSelectedKeys;

	/**
	 *	Get (first) selected Key
	 *  @return selected key
	 */
	public Object getSelectedKey()
	{
		if (!m_ok || m_results.size() == 0)
			return null;
		return m_results.get(0);
	}	//	getSelectedKey

	/**
	 *	Is cancelled?
	 *	- if pressed Cancel = true
	 *	- if pressed OK or window closed = false
	 *  @return true if cancelled
	 */
	public boolean isCancelled()
	{
		return m_cancel;
	}	//	isCancelled

	/**
	 *	Get where clause for (first) selected key
	 *  @return WHERE Clause
	 */
	public String getSelectedSQL()
	{
		//	No results
		Object[] keys = getSelectedKeys();
		if (keys == null || keys.length == 0)
		{
			if (log.isLoggable(Level.CONFIG)) log.config("No Results - OK="
						+ m_ok + ", Cancel=" + m_cancel);
			return "";
		}
		//
		StringBuilder sb = new StringBuilder(getKeyColumn());
		if (keys.length > 1)
			sb.append(" IN (");
		else
			sb.append("=");

		//	Add elements
		for (int i = 0; i < keys.length; i++)
		{
			if (getKeyColumn().endsWith("_ID"))
				sb.append(keys[i].toString()).append(",");
			else
				sb.append("'").append(keys[i].toString()).append("',");
		}

		sb.replace(sb.length()-1, sb.length(), "");
		if (keys.length > 1)
			sb.append(")");
		return sb.toString();
	}	//	getSelectedSQL;

	/**
	 * query ADInfoWindow from ADInfoWindowID
	 */
	protected void loadInfoWindowData (){}

	/**
	 *  Get Table name Synonym
	 *  @return table name
	 */
	protected String getTableName()
	{
		return p_tableName;
	}   //  getTableName

	/**
	 *  Get Key Column Name
	 *  @return column name
	 */
	protected String getKeyColumn()
	{
		return p_keyColumn;
	}   //  getKeyColumn


	public String[] getEvents()
    {
        return InfoPanel.lISTENER_EVENTS;
    }

	/**
	 * enable all control button or disable all rely to selected record 
	 */
	protected void enableButtons (){
		boolean enable = (contentPanel.getSelectedCount() > 0 || getSelectedRowInfo().size() > 0);
		enableButtons(enable);
	}
	
	
	protected void enableButtons (boolean enable)
	{
		confirmPanel.getOKButton().setEnabled(enable); //red1 allow Process for 1 or more records

		if (hasHistory())
			confirmPanel.getButton(ConfirmPanel.A_HISTORY).setEnabled(enable);
		if (hasZoom())
			confirmPanel.getButton(ConfirmPanel.A_ZOOM).setEnabled(!enable?enable : (contentPanel.getSelectedCount() == 1) ); //red1 only zoom for single record
		
	}   //  enableButtons
	
	protected abstract String getSQLWhere();

	protected abstract void setParameters (PreparedStatement pstmt, boolean forCount)
		throws SQLException;
    
	protected void showHistory()					{}
	
	protected boolean hasHistory()				{return false;}
	
	protected void customize()					{}
	
	protected boolean hasCustomize()				{return false;}
	
	protected boolean hasZoom()					{return false;}
	
	protected boolean hasNew()					{return false;}
	

	protected int getAD_Window_ID (String tableName, boolean isSOTrx)
	{
		if (!isSOTrx && m_PO_Window_ID > 0)
			return m_PO_Window_ID;
		if (m_SO_Window_ID > 0)
			return m_SO_Window_ID;
		//
		String sql = "SELECT AD_Window_ID, PO_Window_ID FROM AD_Table WHERE TableName=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_SO_Window_ID = rs.getInt(1);
				m_PO_Window_ID = rs.getInt(2);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (!isSOTrx && m_PO_Window_ID > 0)
			return m_PO_Window_ID;
		return m_SO_Window_ID;
	}	//	getAD_Window_ID

    public void onEvent(Event event)
    {
        if  (event == null){
        	return;
        }
        
            if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_OK)))
            {
                onOk();
            }
            else if (event.getTarget() == contentPanel && event.getName().equals(Events.ON_SELECT))
            {
            	setStatusSelected ();
            	
            	SelectEvent<?, ?> selectEvent = (SelectEvent<?, ?>) event;
            	if (selectEvent.getReference() != null && selectEvent.getReference() instanceof Listitem)
            	{
            		Listitem m_lastOnSelectItem = (Listitem) selectEvent.getReference();
            		m_lastSelectedIndex = m_lastOnSelectItem.getIndex();
           		}

            	enableButtons();
            	
            }else if (event.getTarget() == contentPanel && event.getName().equals("onAfterRender")){           	
            	//IDEMPIERE-1334 at this event selected item from listBox and model is sync
            	enableButtons();
            }
            else if (event.getTarget() == contentPanel && event.getName().equals(Events.ON_DOUBLE_CLICK))
            {
            	if (event.getClass().equals(MouseEvent.class)){
            		return;
            	}
            	if (contentPanel.isMultiple() && m_lastSelectedIndex >= 0) {
					
            		contentPanel.setSelectedIndex(m_lastSelectedIndex);
					
            		model.clearSelection();
					List<Object> lsSelectedItem = new ArrayList<Object>();
					lsSelectedItem.add(model.getElementAt(m_lastSelectedIndex));
					model.setSelection(lsSelectedItem);
					
					int m_keyColumnIndex = contentPanel.getKeyColumnIndex();
					for (int i = 0; i < contentPanel.getRowCount(); i++) {
						// Find the IDColumn Key
						Object data = contentPanel.getModel().getValueAt(i, m_keyColumnIndex);
						if (data instanceof IDColumn) {
							IDColumn dataColumn = (IDColumn) data;
	
							if (i == m_lastSelectedIndex) {
								dataColumn.setSelected(true);
							}
							else {
								dataColumn.setSelected(false);
							}
						}
					}
            	}
            	onDoubleClick();
            	contentPanel.repaint();
            	m_lastSelectedIndex = -1;
            }
            else if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_REFRESH)))
            {
            	onUserQuery();
            }
            else if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_CANCEL)))
            {
            	m_cancel = true;
                dispose(false);
            }
            else if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_RESET))) {
            	resetParameters ();
            }
            // Elaine 2008/12/16
            else if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_HISTORY)))
            {
            	if (!contentPanel.getChildren().isEmpty() && contentPanel.getSelectedRowKey()!=null)
                {
            		showHistory();
                }
            }
    		else if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_CUSTOMIZE)))
    		{
            	if (!contentPanel.getChildren().isEmpty() && contentPanel.getSelectedRowKey()!=null)
                {
            		customize();
                }
    		}
            //
            else if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_ZOOM)))
            {
                if (!contentPanel.getChildren().isEmpty() && contentPanel.getSelectedRowKey()!=null)
                {
                    zoom();
                    if (isLookup())
                    	this.detach();
                }
            }else if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_NEW)))
            {
            	newRecordAction ();
            }
	       
	        else if (event.getTarget() == paging)
            {
            	updateListSelected();
            	int pgNo = paging.getActivePage();
            	if (pgNo == paging.getPageCount()-1  && !isLoadPageNumber()) {
            		testCount();
            		paging.setTotalSize(m_count);
            		pgNo = paging.getActivePage();
            	}

            	if (pageNo != pgNo)
            	{

            		contentPanel.clearSelection();

            		pageNo = pgNo;
            		int start = pageNo * pageSize;
            		int end = getOverIntValue ((long)start + pageSize, extra_max_row);
            		if (end >= m_count)
            			end = m_count;
            		List<Object> subList = readLine(start, end);
        			model = new ListModelTable(subList);
        			model.setSorter(this);
    	            model.addTableModelListener(this);
    	            model.setMultiple(p_multipleSelection);
    	            contentPanel.setData(model, null);
    	            restoreSelectedInPage();
    	            
    			}
            }
            else if (event.getName().equals(Events.ON_CHANGE))
            {
            }
            
            else if (event.getName().equals(Events.ON_CTRL_KEY))
            {
        		KeyEvent keyEvent = (KeyEvent) event;
        		if (LayoutUtils.isReallyVisible(this)) {
        			this.onCtrlKeyEvent(keyEvent);
        		}
        	}else if (event.getName().equals(Events.ON_OK)){// on ok when focus at non parameter component. example grid result
	        	if (m_lookup && contentPanel.getSelectedIndex() >= 0){
	    			onOk();
	    		}
	        	else if (m_infoWindowID == 0 && event.getTarget() instanceof InfoGeneralPanel) {
	        		onUserQuery();
	        	}
        	}else if (event.getName().equals(Events.ON_CANCEL) || (event.getTarget().equals(this) && event.getName().equals(Events.ON_CLOSE))){
        		m_cancel = true;
        		dispose(false);
        	}
    }  //  onEvent

    public static final int VK_ENTER          = '\r';
    public static final int VK_ESCAPE         = 0x1B;
	private void onCtrlKeyEvent(KeyEvent keyEvent) {
		if (keyEvent.isAltKey() && !keyEvent.isCtrlKey() && keyEvent.isShiftKey()) { // Shift+Alt
			if (keyEvent.getKeyCode() == KeyEvent.DOWN) { // Shift+Alt+Down
				// navigate to results
				if (contentPanel.getRowCount() > 0) {
					contentPanel.setFocus(true);
					contentPanel.setSelectedIndex(0);
				}
			}
		} else if (keyEvent.getKeyCode() == VK_ENTER) { // Enter
			// do nothing, let on_ok at infoWindo do, at this is too soon to get value from control, it's not bind
		}
	}

    /**
     * Call query when user click to query button enter in parameter field
     */
    public void onUserQuery (){   	
    	if (validateParameters()){
            showBusyDialog();
            isQueryByUser = true;
            Clients.response(new AuEcho(this, "onQueryCallback", null));
        }
    }
    
    public boolean validateParameters(){
    	return true;
    }

	protected void initParameters() {

	}
	
	protected void updateSubcontent (){ updateSubcontent(-1);};
	
	protected void updateSubcontent (int targetRow){};


	protected void resetParameters() {
	}
    
    
	protected void saveResultSelection(int infoColumnId) {
		int m_keyColumnIndex = contentPanel.getKeyColumnIndex();
		if (m_keyColumnIndex == -1) {
			return;
		}

		m_values = new LinkedHashMap<KeyNamePair,LinkedHashMap<String,Object>>();
		
		if (p_multipleSelection) {
				
			Map <Integer, List<Object>> selectedRow = getSelectedRowInfo();
			
			// for selected rows
            for (Entry<Integer, List<Object>> selectedInfo : selectedRow.entrySet())
            {
            	// get key and viewID
                Integer keyData = selectedInfo.getKey();
                KeyNamePair kp = null;
                
                if (infoColumnId > 0){
                	int dataIndex = columnDataIndex.get(infoColumnId) + p_layout.length;
                	Object viewIDValue = selectedInfo.getValue().get(dataIndex);
                	kp = new KeyNamePair(keyData, viewIDValue == null ? null : viewIDValue.toString());
                }else{
                	kp = new KeyNamePair(keyData, null);
                }
                
                // get Data
				LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();
				for(int col  = 0 ; col < p_layout.length; col ++)
				{
					// layout has same columns as selectedInfo
					if (!p_layout[col].isReadOnly())
						values.put(p_layout[col].getColumnName(), selectedInfo.getValue().get(col));
				}
				if(values.size() > 0)
					m_values.put(kp, values);
            }
		}
	} // saveResultSelection
	
	
	private void showBusyDialog() {
		progressWindow = new BusyDialog();
		progressWindow.setPage(this.getPage());
		progressWindow.doHighlighted();
	}

	private void hideBusyDialog() {		
		if (progressWindow != null) {
			progressWindow.dispose();
			progressWindow = null;
		}		
	}

	protected void correctHeaderOrderIndicator() {
		Listhead listHead = contentPanel.getListHead();
		if (listHead != null) {
			List<?> headers = listHead.getChildren();
			for(Object obj : headers)
			{
				Listheader header = (Listheader) obj;
				if (header.getColumnIndex() == indexOrderColumn)
	              header.setSortDirection(isColumnSortAscending?"ascending":"descending");
	            else
	              header.setSortDirection("natural");
			}
		}
	}
    public void onQueryCallback(Event event)
    {
    	try
    	{
    		if (event == null)
    			m_count = 0;
    		else
    			executeQuery();
    		
            renderItems();

            correctHeaderOrderIndicator();
            
        	if (isQueryByUser){
                recordSelectedData.clear();
                isRequeryByRunSuccessProcess = false;
        	}
        	if (isRequeryByRunSuccessProcess){
        		syncSelectedAfterRequery();
        		restoreSelectedInPage();
        	}
        	updateSubcontent ();
        }
    	finally
    	{
    		isQueryByUser = false;
    		hideBusyDialog();
    	}
    }

    
    protected void onOk()
    {
		if (!contentPanel.getChildren().isEmpty() && getSelectedRowInfo().size() > 0)
		{
		    dispose(true);
		}
	}

    private void onDoubleClick()
	{
		if (isLookup())
		{
			dispose(true);
		}
		else
		{
			zoom();
		}

	}

    public void tableChanged(WTableModelEvent event)
    {
    	enableButtons();
    }

    public void zoom()
    {    	
    	Integer recordId = contentPanel.getSelectedRowKey();
    	if (recordId == null)
    		return;
    	
    	if (listeners != null && listeners.size() > 0)
    	{
	        ValueChangeEvent event = new ValueChangeEvent(this,"zoom",
	                   contentPanel.getSelectedRowKey(),contentPanel.getSelectedRowKey());
	        fireValueChange(event);
    	}
    	else
    	{    		
    		int AD_Table_ID = MTable.getTable_ID(p_tableName);
    		if (AD_Table_ID <= 0)
    		{
    			if (p_keyColumn.endsWith("_ID"))
    			{
    				AD_Table_ID = MTable.getTable_ID(p_keyColumn.substring(0, p_keyColumn.length() - 3));
    			}
    		}
    		if (AD_Table_ID > 0)
    			AEnv.zoom(AD_Table_ID, recordId);
    	}
    }

    protected void newRecordAction (){}
    
    public void addValueChangeListener(ValueChangeListener listener)
    {
        if (listener == null)
        {
            return;
        }

        listeners.add(listener);
    }

    public void fireValueChange(ValueChangeEvent event)
    {
        for (ValueChangeListener listener : listeners)
        {
           listener.valueChange(event);
        }
    }
    
    public void dispose(boolean ok)
    {
    	if (log.isLoggable(Level.CONFIG)) log.config("OK=" + ok);
        m_ok = ok;

        //  End Worker
        if (isLookup())
        {
        	saveSelection();
        }
        if (Window.MODE_EMBEDDED.equals(getAttribute(Window.MODE_KEY)))
        	SessionManager.getAppDesktop().closeActiveWindow();
        else
	        this.detach();
    }   //  dispose

	public void sort(Comparator<Object> cmpr, boolean ascending) {
		updateListSelected();
		WListItemRenderer.ColumnComparator lsc = (WListItemRenderer.ColumnComparator) cmpr;
		
		// keep column order
		int col = lsc.getColumnIndex();
		indexOrderColumn = col;
		isColumnSortAscending = ascending;
		sqlOrderColumn = p_layout[col].getColSQL().trim();
		m_sqlUserOrder = null; // clear cache value
		
		if (m_useDatabasePaging)
		{
			executeQuery();
		}
		else
		{
			Collections.sort(line, lsc);
		}
		renderItems();
	}

    public boolean isLookup()
    {
    	return m_lookup;
    }

    public void scrollToSelectedRow()
    {
    	if (contentPanel != null && contentPanel.getSelectedIndex() >= 0) {
    		Listitem selected = contentPanel.getItemAtIndex(contentPanel.getSelectedIndex());
    		if (selected != null) {
    			selected.focus();
    		}
    	}
    }
    
	@Override
	public String getSortDirection(Comparator<Object> cmpr) {
		return "natural";
	}

	public int getWindowNo() {
		return p_WindowNo;
	}
	
	public int getRowCount() {
		return contentPanel.getRowCount();
	}
	
	public Integer getFirstRowKey() {
		return contentPanel.getFirstRowKey();
	}

	public Integer getRowKeyAt(int row) {
		return contentPanel.getRowKeyAt(row);
	}

	protected int getCacheStart() {
		return cacheStart;
	}

	private void setCacheStart(int cacheStart) {
		this.cacheStart = cacheStart;
	}
	
	protected int getCacheEnd() {
		return cacheEnd;
	}
	
	protected boolean isUseDatabasePaging() {
		return m_useDatabasePaging;
	}
	
	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		SessionManager.getSessionApplication().getKeylistener().addEventListener(Events.ON_CTRL_KEY, this);
	}

	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		try {
			SessionManager.getSessionApplication().getKeylistener().removeEventListener(Events.ON_CTRL_KEY, this);
		} catch (Exception e){}
	}

		public GridField getGridfield() {
		return m_gridfield;
	}

	public void setGridfield(GridField m_gridfield) {
		this.m_gridfield = m_gridfield;
	}

	public int getPageSize() {
		return pageSize;
	}
}	//	Info

