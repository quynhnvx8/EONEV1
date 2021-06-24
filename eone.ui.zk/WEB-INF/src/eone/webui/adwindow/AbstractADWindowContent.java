

package eone.webui.adwindow;

import static eone.base.model.MSysConfig.ZK_GRID_AFTER_FIND;
import static eone.base.model.SystemIDs.PROCESS_AD_CHANGELOG_REDO;
import static eone.base.model.SystemIDs.PROCESS_AD_CHANGELOG_UNDO;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Callback;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ExecutionCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.RowRenderer;

import eone.base.model.DataStatusEvent;
import eone.base.model.DataStatusListener;
import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.base.model.GridTable;
import eone.base.model.GridWindow;
import eone.base.model.GridWindowVO;
import eone.base.model.I_M_Product;
import eone.base.model.MImage;
import eone.base.model.MQuery;
import eone.base.model.MRecentItem;
import eone.base.model.MRole;
import eone.base.model.MSysConfig;
import eone.base.model.MTable;
import eone.base.model.MUserPreference;
import eone.base.model.MWindow;
import eone.base.model.PO;
import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;
import eone.base.process.ProcessInfo;
import eone.base.process.ProcessInfoLog;
import eone.webui.AdempiereWebUI;
import eone.webui.ClientInfo;
import eone.webui.LayoutUtils;
import eone.webui.WRequest;
import eone.webui.WZoomAcross;
import eone.webui.apps.AEnv;
import eone.webui.apps.BusyDialogTemplate;
import eone.webui.apps.HelpWindow;
import eone.webui.apps.ProcessModalDialog;
import eone.webui.apps.WQuickForm;
import eone.webui.component.Mask;
import eone.webui.component.ProcessInfoDialog;
import eone.webui.component.Window;
import eone.webui.component.ZkCssHelper;
import eone.webui.editor.IProcessButton;
import eone.webui.editor.WButtonEditor;
import eone.webui.editor.WEditor;
import eone.webui.event.ActionEvent;
import eone.webui.event.ActionListener;
import eone.webui.event.DialogEvents;
import eone.webui.event.ToolbarListener;
import eone.webui.exception.ApplicationException;
import eone.webui.panel.ExportAction;
import eone.webui.panel.FileImportAction;
import eone.webui.panel.InfoPanel;
import eone.webui.panel.WAttachment;
import eone.webui.part.AbstractUIPart;
import eone.webui.part.ITabOnSelectHandler;
import eone.webui.session.SessionManager;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.CustomizeGridViewDialog;
import eone.webui.window.FDialog;
import eone.webui.window.FindWindow;
import eone.webui.window.WPostIt;


public abstract class AbstractADWindowContent extends AbstractUIPart implements ToolbarListener,
        EventListener<Event>, DataStatusListener, ActionListener, ITabOnSelectHandler
{
    private static final String ON_FOCUS_DEFER_EVENT = "onFocusDefer";

	private static final String ON_DEFER_SET_DETAILPANE_SELECTION_EVENT = "onDeferSetDetailpaneSelection";

	private static final CLogger logger;

    static
    {
        logger = CLogger.getCLogger(AbstractADWindowContent.class);
    }

    private Properties           ctx;

    private GridWindow           gridWindow;

    protected StatusBar     statusBar;

    protected IADTabbox          	 adTabbox;

    private int                  curWindowNo;

    private boolean              m_onlyCurrentRows = true;

    protected ADWindowToolbar     toolbar;

    protected String             title;

    private boolean 			 boolChanges = false;

	private int m_onlyCurrentDays = 0;

	private Component parent;

	private boolean m_findCancelled;

	private boolean m_findCreateNew;

	private boolean m_queryInitiating;

	protected BreadCrumb breadCrumb;

	private int adWindowId;

	private MImage image;

	/**
	 * Quick Form Status bar
	 */
	protected StatusBar statusBarQF;

	/**
	 * Maintain no of quick form tabs open
	 */
	ArrayList <Integer>			quickFormOpenTabs	= new ArrayList <Integer>();

	/**
	 * Constructor
	 * @param ctx
	 * @param windowNo
	 * @param adWindowId 
	 */
    public AbstractADWindowContent(Properties ctx, int windowNo, int adWindowId)
    {
        this.ctx = ctx;
        this.curWindowNo = windowNo;
        this.adWindowId = adWindowId;

        initComponents();
    }

    /**
     * @param parent
     * @return Component
     */
	public Component createPart(Object parent)
    {
		if (parent instanceof Component)
			this.parent = (Component) parent;

		adTabbox = createADTab();
		adTabbox.setSelectionEventListener(this);
		adTabbox.setADWindowPanel(this);

        Component comp = super.createPart(parent);
        comp.addEventListener(LayoutUtils.ON_REDRAW_EVENT, this);
        comp.addEventListener(ON_DEFER_SET_DETAILPANE_SELECTION_EVENT, this);
        comp.addEventListener(ON_FOCUS_DEFER_EVENT, this);
        comp.setAttribute(ITabOnSelectHandler.ATTRIBUTE_KEY, this);
        return comp;
    }

	public BreadCrumb getBreadCrumb()
	{
		return breadCrumb;
	}

	/**
	 * @return StatusBarPanel
	 */
	public StatusBar getStatusBar()
    {
    	return statusBar;
    }

    private void initComponents()
    {
        /** Initalise toolbar */
        toolbar = new ADWindowToolbar(this, getWindowNo());
        toolbar.setId("windowToolbar");
        toolbar.addListener(this);

        statusBar = new StatusBar();
        
        GridWindowVO gWindowVO = AEnv.getMWindowVO(curWindowNo, adWindowId, 0);
        if (gWindowVO == null)
        {
            throw new ApplicationException(Msg.getMsg(ctx,
                    "AccessTableNoView")
                    + "(No Window Model Info)");
        }
        gridWindow = new GridWindow(gWindowVO, true);
        title = gridWindow.getName();
        image = gridWindow.getMImage();
    }    

    /**
     * @return IADTab
     */
    protected abstract IADTabbox createADTab();

    protected abstract void switchEditStatus(boolean editStatus);
    
    private void focusToActivePanel() {
    	IADTabpanel adTabPanel = adTabbox.getSelectedTabpanel();
		focusToTabpanel(adTabPanel);
	}

    private void focusToTabpanel(IADTabpanel adTabPanel ) {
		if (adTabPanel != null && adTabPanel instanceof HtmlBasedComponent) {
			Events.echoEvent(ON_FOCUS_DEFER_EVENT, getComponent(), (HtmlBasedComponent)adTabPanel);
		}
	}

    /**
     * @param adWindowId
     * @param query
     * @return boolean
     */
	public boolean initPanel(MQuery query)
    {
		// This temporary validation code is added to check the reported bug
		// [ adempiere-ZK Web Client-2832968 ] User context lost?
		// https://sourceforge.net/tracker/?func=detail&atid=955896&aid=2832968&group_id=176962
		// it's harmless, if there is no bug then this must never fail
		Session currSess = Executions.getCurrent().getDesktop().getSession();
		int checkad_user_id = -1;
		if (currSess != null && currSess.getAttribute("Check_AD_User_ID") != null)
			checkad_user_id = (Integer)currSess.getAttribute("Check_AD_User_ID");
		if (checkad_user_id!=Env.getAD_User_ID(ctx))
		{
			String msg = "Timestamp=" + new Date()
					+ ", Bug 2832968 SessionUser="
					+ checkad_user_id
					+ ", ContextUser="
					+ Env.getAD_User_ID(ctx)
					+ ".  Please report conditions to your system administrator or in sf tracker 2832968";
			ApplicationException ex = new ApplicationException(msg);
			logger.log(Level.SEVERE, msg, ex);
			throw ex;
		}
		// End of temporary code for [ adempiere-ZK Web Client-2832968 ] User context lost?

		// Set AutoCommit for this Window
		Env.setAutoCommit(ctx, curWindowNo, Env.isAutoCommit(ctx));
		boolean autoNew = Env.isAutoNew(ctx);
		Env.setAutoNew(ctx, curWindowNo, autoNew);

        // WindowName variable preserved for backward compatibility
        // please consider it as DEPRECATED and use _WinInfo_WindowName instead 
        Env.setContext(ctx, curWindowNo, "WindowName", gridWindow.getName()); // deprecated
        Env.setContext(ctx, curWindowNo, "_WinInfo_WindowName", gridWindow.getName());
        Env.setContext(ctx, curWindowNo, "_WinInfo_AD_Window_ID", gridWindow.getAD_Window_ID());
        Env.setContext(ctx, curWindowNo, "_WinInfo_AD_Window_UU", gridWindow.getAD_Window_UU());

        // Set SO/AutoNew for Window
        Env.setContext(ctx, curWindowNo, "IsSOTrx", gridWindow.isSOTrx());
        if (!autoNew && gridWindow.isTransaction())
        {
            Env.setAutoNew(ctx, curWindowNo, true);
        }

        m_onlyCurrentRows =  gridWindow.isTransaction();

        MQuery detailQuery = null;
        /**
         * Window Tabs
         */
    	if (query != null && query.getZoomTableName() != null && query.getZoomColumnName() != null
				&& query.getZoomValue() instanceof Integer && (Integer)query.getZoomValue() > 0)
    	{
    		if (!query.getZoomTableName().equalsIgnoreCase(gridWindow.getTab(0).getTableName()))
    		{
    			detailQuery = query;
    			query = new MQuery();
    			query.addRestriction("1=2");
    			query.setRecordCount(0);
    		}
    	}

        int tabSize = gridWindow.getTabCount();

        GridTab gridTab = null;
        for (int tab = 0; tab < tabSize; tab++)
        {
            gridTab = initTab(query, tab);
            if (tab == 0 && gridTab == null && m_findCancelled)
            	return false;
        }

        if (gridTab != null)
        	gridTab.getTableModel().setChanged(false);

        adTabbox.setSelectedIndex(0);
        // all fields context for window is clear at AbstractADTab.prepareContext, set again IsSOTrx for window
        Env.setContext(ctx, curWindowNo, "IsSOTrx", gridWindow.isSOTrx());
        toolbar.enableTabNavigation(adTabbox.getTabCount() > 1);
        toolbar.enableFind(true);
        adTabbox.evaluate(null);

        toolbar.updateToolbarAccess(adWindowId);
        updateToolbar();
        
        if (detailQuery != null && zoomToDetailTab(detailQuery))
        {
        	return true;
        }

       // SessionManager.getAppDesktop().updateHelpContext("Tab", adTabbox.getSelectedGridTab().getAD_Tab_ID());

        return true;
    }

	private boolean zoomToDetailTab(MQuery query) {
		//zoom to detail
        if (query != null && query.getZoomTableName() != null && query.getZoomColumnName() != null)
    	{
    		GridTab gTab = gridWindow.getTab(0);
    		if (!query.getZoomTableName().equalsIgnoreCase(gTab.getTableName()))
    		{
    			int tabSize = gridWindow.getTabCount();

    	        for (int tab = 0; tab < tabSize; tab++)
    	        {
    	        	gTab = gridWindow.getTab(tab);
    	        	if (gTab.isSortTab())
    	        		continue;
    	        	if (gTab.getTableName().equalsIgnoreCase(query.getZoomTableName()))
    	        	{
    	        		if (doZoomToDetail(gTab, query, tab)) {
	        				return true;
	        			}
    	        	}
    	        }
    		}
    	}
        return false;
	}

	private boolean doZoomToDetail(GridTab gTab, MQuery query, int tabIndex) {
		GridField[] fields = gTab.getFields();
		for (GridField field : fields)
		{
			if (field.getColumnName().equalsIgnoreCase(query.getZoomColumnName()))
			{
				gridWindow.initTab(tabIndex);
				int[] parentIds = DB.getIDsEx(null, "SELECT " + gTab.getLinkColumnName() + " FROM " + gTab.getTableName() + " WHERE " + query.getWhereClause());
				if (parentIds.length > 0)
				{
					GridTab parentTab = null;
					Map<Integer, MQuery>queryMap = new TreeMap<Integer, MQuery>();

					for (int parentId : parentIds)
					{
						Map<Integer, Object[]>parentMap = new TreeMap<Integer, Object[]>();
						int index = tabIndex;
						int oldpid = parentId;
						GridTab currentTab = gTab;
						while (index > 0)
						{
							index--;
							GridTab pTab = gridWindow.getTab(index);
							if (pTab.getTabLevel() < currentTab.getTabLevel())
							{
								if (parentTab == null)
									parentTab = pTab;
								gridWindow.initTab(index);
								if (index > 0)
								{
									if (pTab.getLinkColumnName() != null && pTab.getLinkColumnName().trim().length() > 0)
									{
										int pid = DB.getSQLValue(null, "SELECT " + pTab.getLinkColumnName() + " FROM " + pTab.getTableName() + " WHERE " + currentTab.getLinkColumnName() + " = ?", oldpid);
										if (pid > 0)
										{
											parentMap.put(index, new Object[]{currentTab.getLinkColumnName(), oldpid});
											oldpid = pid;
											currentTab = pTab;
										}
										else
										{
											parentMap.clear();
											break;
										}
									}
								}
								else
								{
									parentMap.put(index, new Object[]{currentTab.getLinkColumnName(), oldpid});
								}
							}
						}
	
						for(Map.Entry<Integer, Object[]> entry : parentMap.entrySet())
						{
							GridTab pTab = gridWindow.getTab(entry.getKey());
							Object[] value = entry.getValue();
							MQuery pquery = queryMap.get(entry.getKey());
							if (pquery == null) 
							{
								pquery = new MQuery(pTab.getAD_Table_ID());
								queryMap.put(entry.getKey(), pquery);
								pquery.addRestriction((String)value[0], "=", value[1]);
							}
							else
							{
								pquery.addRestriction((String)value[0], "=", value[1], null, null, false, 0);
							}
						}
					}

					for (Map.Entry<Integer, MQuery> entry : queryMap.entrySet())
					{
						GridTab pTab = gridWindow.getTab(entry.getKey());
						IADTabpanel tp = adTabbox.findADTabpanel(pTab);
        				tp.createUI();
        				if (tp.getTabLevel() == 0)
        				{
        					pTab.setQuery(entry.getValue());
        					tp.query();
        				}
        				else 
        				{
        					tp.query();
        					pTab.setQuery(entry.getValue());
        					tp.query();
        				}
					}

					MQuery targetQuery = new MQuery(gTab.getAD_Table_ID());
					targetQuery.addRestriction(gTab.getLinkColumnName(), "=", parentTab.getRecord_ID());
					gTab.setQuery(targetQuery);
					IADTabpanel gc = null;
					gc = adTabbox.findADTabpanel(gTab);
					gc.createUI();
					gc.query(false, 0, 0);

					int zoomColumnIndex = -1;
					GridTable table = gTab.getTableModel();
					for (int i = 0; i < table.getColumnCount(); i++)
					{
						if (table.getColumnName(i).equalsIgnoreCase(query.getZoomColumnName()))
						{
							zoomColumnIndex = i;
							break;
						}
					}
    				int count = table.getRowCount();
    				for(int i = 0; i < count; i++)
    				{
    					int id = -1;
    					if (zoomColumnIndex >= 0) 
    					{
    						Object zoomValue = table.getValueAt(i, zoomColumnIndex);
    						if (zoomValue != null && zoomValue instanceof Number)
    						{
    							id = ((Number)zoomValue).intValue();
    						}
    					}
    					else
    					{
    						id = table.getKeyID(i);
    					}
    					if (id == ((Integer)query.getZoomValue()).intValue())
    					{
    						setActiveTab(gridWindow.getTabIndex(gTab), null);
    						gTab.navigate(i);
    						return true;
    					}
    				}
				}
			}
		}
		return false;
	}

	private void initQueryOnNew(MQuery result) {
		GridTab curTab = adTabbox.getSelectedGridTab();
		boolean onNew = false;
		if (curTab.isHighVolume() && m_findCreateNew)
			onNew = true;
		else if (result == null && curTab.getRowCount() == 0 && Env.isAutoNew(ctx, curWindowNo))
			onNew = true;
		else if (!curTab.isReadOnly() && curTab.isQueryNewRecord())
			onNew = true;
		if (onNew) {
			Executions.schedule(AEnv.getDesktop(), new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					onNew();
					ADTabpanel adtabpanel = (ADTabpanel) getADTab().getSelectedTabpanel();
					adtabpanel.focusToFirstEditor(false);
				}
			}, new Event("onInsert"));
		}
	}

	/**
	 * @param query
	 * @param tabIndex
	 */
	protected GridTab initTab(MQuery query, int tabIndex) {
		gridWindow.initTab(tabIndex);

		final GridTab gTab = gridWindow.getTab(tabIndex);
		Env.setContext(ctx, curWindowNo, tabIndex, GridTab.CTX_TabLevel, Integer.toString(gTab.getTabLevel()));
		
		// Query first tab
		if (tabIndex == 0)
		{
			gTab.setUpdateWindowContext(true);
			m_queryInitiating = true;
			getComponent().setVisible(false);
		    initialQuery(query, gTab, new Callback<MQuery>() {
				@Override
				public void onCallback(MQuery result) {
					m_queryInitiating = false;

					if (m_findCancelled) {
						SessionManager.getAppDesktop().closeWindow(curWindowNo);
				    	return;
					}

					if (!getComponent().isVisible())
						getComponent().setVisible(true);

					// Set initial Query on first tab
				    if (result != null)
				    {
				        m_onlyCurrentRows = false;
				        gTab.setQuery(result);
				    }

				    if (adTabbox.getSelectedTabpanel() != null)
				    {
					    initFirstTabpanel();

					    initQueryOnNew(result);
				    }
				}

			});
		    setGridTab(gTab);
		}
		else
		{
			gTab.setUpdateWindowContext(false);
		}

		if (gTab.isSortTab())
		{
			ADSortTab sortTab = new ADSortTab(curWindowNo, gTab);
			adTabbox.addTab(gTab, sortTab);
			sortTab.registerAPanel(this);
			if (tabIndex == 0) {
				sortTab.createUI();
				if (!m_queryInitiating)
				{
					initFirstTabpanel();
				}
			}
			gTab.addDataStatusListener(this);
		}
		else
		{
			ADTabpanel fTabPanel = new ADTabpanel();
			fTabPanel.addEventListener(ADTabpanel.ON_DYNAMIC_DISPLAY_EVENT, this);
	    	gTab.addDataStatusListener(this);
	    	fTabPanel.init(this, curWindowNo, gTab, gridWindow);
	    	adTabbox.addTab(gTab, fTabPanel);
		    if (tabIndex == 0) {
		    	fTabPanel.createUI();
		    	if (!m_queryInitiating)
				{
					initFirstTabpanel();
				}
		    }

		    if (!m_queryInitiating && tabIndex == 0)
		    {
		    	initQueryOnNew(query);
		    }
		}

		return gTab;
	}
	
	private void setGridTab(GridTab gridTab)
    {
        curTab = gridTab;
    }

	private void initFirstTabpanel() {
		adTabbox.getSelectedTabpanel().query(m_onlyCurrentRows, m_onlyCurrentDays, adTabbox.getSelectedGridTab().getMaxQueryRecords());
		adTabbox.getSelectedTabpanel().activate(true);
		Events.echoEvent(new Event(ADTabpanel.ON_POST_INIT_EVENT, adTabbox.getSelectedTabpanel()));
	}

    /**
     * Initial Query
     *
     * @param query
     *            initial query
     * @param mTab
     *            tab
     * @return query or null
     */
    private void initialQuery(final MQuery query, GridTab mTab, final Callback<MQuery> callback)
    {
        // We have a (Zoom) query
        if (query != null && query.isActive())
        {
    		callback.onCallback(query);
    		return;
        }

        //
		StringBuffer where = new StringBuffer(Env.parseContext(ctx, curWindowNo, mTab.getWhereExtended(), false));
        // Query automatically if high volume and no query
        boolean require = mTab.isHighVolume();
        if (!require && !m_onlyCurrentRows) // No Trx Window
        {
            if (query != null)
            {
                String wh2 = query.getWhereClause();
                if (wh2.length() > 0)
                {
                    if (where.length() > 0)
                        where.append(" AND ");
                    where.append(wh2);
                }
            }
            //
            StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ")
                    .append(mTab.getTableName());
            if (where.length() > 0)
                sql.append(" WHERE ").append(where);
            String finalSQL = MRole.getDefault().addAccessSQL(sql.toString(),
            		mTab.getTableName(), MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
            int no = DB.getSQLValue(null, finalSQL.toString());
            //
            require = mTab.isQueryRequire(no);
        }
        // Show Query
        if (require)
        {
        	m_findCancelled = false;
        	m_findCreateNew = false;
            GridField[] findFields = mTab.getFields();
            findWindow = new FindWindow(curWindowNo,
                    mTab.getName(), mTab.getAD_Table_ID(), mTab.getTableName(),
                    where.toString(), findFields, 10, mTab.getAD_Tab_ID()); // no query below 10
            setupEmbeddedFindwindow();
            if (findWindow.initialize())
            {
	        	findWindow.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (!findWindow.isCancel())
			            {
			            	m_findCreateNew = findWindow.isCreateNew();
			            	MQuery result = findWindow.getQuery();
			            	callback.onCallback(result);
			            	EventListener<? extends Event> listener = findWindow.getEventListeners(DialogEvents.ON_WINDOW_CLOSE).iterator().next();
			            	findWindow.removeEventListener(DialogEvents.ON_WINDOW_CLOSE, listener);
			            }
			            else
			            {
			            	m_findCancelled = true;
			            	callback.onCallback(null);
			            }
					}
				});	        	
	        	getComponent().addEventListener("onInitialQuery", new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						getComponent().getParent().appendChild(findWindow);
						LayoutUtils.openEmbeddedWindow(getComponent().getParent(), findWindow, "overlap");						
					}
				});
	        	Events.echoEvent("onInitialQuery", getComponent(), null);
            }
            else
            {
            	callback.onCallback(query);
            }
        }
        else
        {
        	callback.onCallback(query);
        }
    } // initialQuery

	private void setupEmbeddedFindwindow() {
		findWindow.setTitle(Msg.getMsg(Env.getCtx(), "Find").replaceAll("&", ""));
		findWindow.setBorder("width:1px;");	
		findWindow.setStyle("position: absolute;background-color: #F0F0F0; padding: 0px;");
		ZKUpdateUtil.setWidth(findWindow, "70%");
		if (ClientInfo.maxHeight(ClientInfo.MEDIUM_HEIGHT-1))
			ZKUpdateUtil.setHeight(findWindow, "100%");
		else
			ZKUpdateUtil.setHeight(findWindow, "60%");
		findWindow.setZindex(1000);
		findWindow.setSizable(false);
		findWindow.setContentStyle("background-color: #F0F0F0; width: 99%; margin: auto;");
		//AEnv.showCenterScreen(findWindow);
		//LayoutUtils.openPopupWindow(this.getComponent(), findWindow, "after_start");
	}

    public String getTitle()
    {
        return title;
    }
    
    public MImage getImage()
    {
    	return image;
    }

    /**
     * @see ToolbarListener#onDetailRecord()
     */
    public void onDetailRecord()
    {
    	adTabbox.onDetailRecord();
    }

	/**
     * @see ToolbarListener#onParentRecord()
     */
    public void onParentRecord()
    {
    	List<BreadCrumbLink> parents = breadCrumb.getParentLinks();
    	if (!parents.isEmpty()) {
    		Events.sendEvent(parents.get(parents.size()-1), new Event(Events.ON_CLICK, parents.get(parents.size()-1)));
    	}
    }

    /**
     * @see ToolbarListener#onFirst()
     */
    public void onFirst()
    {
    	Callback<Boolean> callback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					adTabbox.getSelectedGridTab().navigate(-1); // not zero because of IDEMPIERE-3736 
			        focusToActivePanel();
				}
			}
		};
		saveAndNavigate(callback);
    }

    /**
     * @see ToolbarListener#onLast()
     */
    public void onLast()
    {
        Callback<Boolean> callback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					adTabbox.getSelectedGridTab().navigate(adTabbox.getSelectedGridTab().getRowCount() - 1);
			        focusToActivePanel();
				}
			}
		};
		onSave(false, true, callback);
    }

    /**
     * @see ToolbarListener#onNext()
     */
    public void onNext()
    {
        Callback<Boolean> callback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					adTabbox.getSelectedGridTab().navigateRelative(+1);
					focusToActivePanel();
				}
			}
		};
		saveAndNavigate(callback);
    }

    /**
     * @see ToolbarListener#onPrevious()
     */
    public void onPrevious()
    {
        Callback<Boolean> callback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					adTabbox.getSelectedGridTab().navigateRelative(-1);
			        focusToActivePanel();
				}
			}
		};
		saveAndNavigate(callback);
    }

    /**
     * @see ToolbarListener#onPrevious()
     */
    public void onTreeNavigate(final GridTab gt, final int rowIndex)
    {
    	Callback<Boolean> callback = new Callback<Boolean>() {
    		@Override
    		public void onCallback(Boolean result) {
    			if (result) {
    				gt.navigate(rowIndex);
    				//focusToActivePanel();
    			}
    		}
    	};
    	saveAndNavigate(callback);
    }

   

	private FindWindow findWindow;

	private Div mask;

	protected ADWindow adwindow;

	/**
	 *	@see ToolbarListener#onLock()
	 */
	public void onLock()
	{
		
	}	//	lock
	//


    /**
     * @see ToolbarListener#onAttachment()
     */
    public void onAttachment()
    {
		int record_ID = adTabbox.getSelectedGridTab().getRecord_ID();
		if (logger.isLoggable(Level.INFO)) logger.info("Record_ID=" + record_ID);

		if (record_ID == -1)	//	No Key
		{
			//aAttachment.setEnabled(false);
			return;
		}

		EventListener<Event> listener = new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				toolbar.setPressed("Attachment",adTabbox.getSelectedGridTab().hasAttachment());
				focusToActivePanel();				
			}
		};
		//	Attachment va =
		WAttachment win = new WAttachment (	curWindowNo, adTabbox.getSelectedGridTab().getAD_AttachmentID(),
							adTabbox.getSelectedGridTab().getAD_Table_ID(), record_ID, null, listener);		
		win.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				hideBusyMask();
			}
		});
		getComponent().getParent().appendChild(win);
		showBusyMask(win);		
		LayoutUtils.openOverlappedWindow(getComponent(), win, "middle_center");
		win.focus();
	}

    public void onChat()
    {
    	int recordId = adTabbox.getSelectedGridTab().getRecord_ID();
    	if (logger.isLoggable(Level.INFO)) logger.info("Record_ID=" + recordId);

		if (recordId== -1)	//	No Key
		{
			return;
		}

		//	Find display
		String infoName = null;
		String infoDisplay = null;
		for (int i = 0; i < adTabbox.getSelectedGridTab().getFieldCount(); i++)
		{
			GridField field = adTabbox.getSelectedGridTab().getField(i);
			if (field.isKey())
				infoName = field.getHeader();
			if ((field.getColumnName().equals("Name") || field.getColumnName().equals("DocumentNo") )
				&& field.getValue() != null)
				infoDisplay = field.getValue().toString();
			if (infoName != null && infoDisplay != null)
				break;
		}
		if (infoDisplay == null) {
			infoDisplay = "";
		}

    	
    }

    public void onPostIt()
    {
    	int recordId = adTabbox.getSelectedGridTab().getRecord_ID();
    	logger.info("Record_ID=" + recordId);

    	if (recordId== -1)	//	No Key
    	{
    		return;
    	}

    	//	Find display
    	String infoName = null;
    	String infoDisplay = null;
    	for (int i = 0; i < adTabbox.getSelectedGridTab().getFieldCount(); i++)
    	{
    		GridField field = adTabbox.getSelectedGridTab().getField(i);
    		if (field.isKey())
    			infoName = field.getHeader();
    		if ((field.getColumnName().equals("Name") || field.getColumnName().equals("DocumentNo") )
    				&& field.getValue() != null)
    			infoDisplay = field.getValue().toString();
    		if (infoName != null && infoDisplay != null)
    			break;
    	}
    	String header = infoName + ": " + infoDisplay;

    	WPostIt postit = new WPostIt(header, adTabbox.getSelectedGridTab().getAD_PostIt_ID(), adTabbox.getSelectedGridTab().getAD_Table_ID(), recordId, null);
    	postit.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
    		@Override
    		public void onEvent(Event event) throws Exception {
    			hideBusyMask();
    			toolbar.setPressed("PostIt",adTabbox.getSelectedGridTab().hasPostIt());
    			focusToActivePanel();
    		}
    	});
    	getComponent().getParent().appendChild(postit);
    	showBusyMask(postit);    	    	
    	LayoutUtils.openOverlappedWindow(getComponent(), postit, "middle_center");
    	postit.showWindow();
    }

    /**
     * @see ToolbarListener#onToggle()
     */
    public void onToggle()
    {
    	adTabbox.getSelectedTabpanel().switchRowPresentation();
    	//Deepak-Enabling customize button IDEMPIERE-364
        if(!(adTabbox.getSelectedTabpanel() instanceof ADSortTab))
        	toolbar.enableCustomize(((ADTabpanel)adTabbox.getSelectedTabpanel()).isGridView());
    	focusToActivePanel();
    }

	/**
     * @param callback
     */
    public void onExit(Callback<Boolean> callback)
    {
    	if (!boolChanges)
    	{
    		callback.onCallback(Boolean.TRUE);
    	}
    	else
    	{
    		FDialog.ask(curWindowNo, null, Msg.getMsg(Env.getCtx(), "CloseUnSave"), callback);
    	}
    	
    }

	/**
	 * Invoke when quick form is click
	 */
	public void onQuickForm()
	{
		logger.log(Level.FINE, "Invoke Quick Form");
		// Prevent to open Quick Form if already opened.
		if (!this.registerQuickFormTab(getADTab().getSelectedGridTab().getAD_Tab_ID()))
		{
			logger.fine("TabID=" + getActiveGridTab().getAD_Tab_ID() + "  is already open.");
			return;
		}
		int table_ID = adTabbox.getSelectedGridTab().getAD_Table_ID();
		if (table_ID == -1)
			return;

		statusBarQF = new StatusBar();
		// Remove Key-listener of parent Quick Form
		int tabLevel = getToolbar().getQuickFormTabHrchyLevel();
		if (tabLevel > 0 && getCurrQGV() != null)
		{
			SessionManager.getSessionApplication().getKeylistener().removeEventListener(Events.ON_CTRL_KEY, getCurrQGV());
		}

		WQuickForm form = new WQuickForm(this, m_onlyCurrentRows, m_onlyCurrentDays);
		form.setTitle(this.getADTab().getSelectedGridTab().getName());
		form.setVisible(true);
		form.setSizable(true);
		form.setMaximizable(true);
		form.setMaximized(true);
		form.setPosition("center");
		ZKUpdateUtil.setWindowHeightX(form, 550);
		ZKUpdateUtil.setWindowWidthX(form, 900);
		ZkCssHelper.appendStyle(form, "z-index: 900;");

		AEnv.showWindow(form);
	} // onQuickForm

    /**
     * @param event
     * @see EventListener#onEvent(Event)
     */
    public void onEvent(Event event)
    {
    	if (CompositeADTabbox.ON_SELECTION_CHANGED_EVENT.equals(event.getName()))
    	{
    		Object eventData = event.getData();

	        if (eventData != null && eventData instanceof Object[] && ((Object[])eventData).length == 2)
	        {
	        	Object[] indexes = (Object[]) eventData;
	        	final int newTabIndex = (Integer)indexes[1];

	        	final int originalTabIndex = adTabbox.getSelectedIndex();
	        	final int originalTabRow = adTabbox.getSelectedGridTab().getCurrentRow();
	            setActiveTab(newTabIndex, new Callback<Boolean>() {

					@Override
					public void onCallback(Boolean result) {
						if (result)
						{
							if (newTabIndex < originalTabIndex)
							{
								if (adTabbox.isDetailPaneLoaded())
									adTabbox.setDetailPaneSelectedTab(originalTabIndex, originalTabRow);
								else {
									Events.echoEvent(new Event(ON_DEFER_SET_DETAILPANE_SELECTION_EVENT, getComponent(), new Integer[]{originalTabIndex, originalTabRow}));
								}
							}
			            }
			            else
			            {
			            	//reset to original
			            	adTabbox.setSelectedIndex(originalTabIndex);
			            }

					}
				});
	            
	            //SessionManager.getAppDesktop().updateHelpContext("Tab", adTabbox.getSelectedGridTab().getAD_Tab_ID());
	        }
    	}
    	else if (event.getTarget() instanceof ProcessModalDialog)
    	{
    		if (!DialogEvents.ON_WINDOW_CLOSE.equals(event.getName())){
    			return;
    		}

    		hideBusyMask();
    		ProcessModalDialog dialog = (ProcessModalDialog) event.getTarget();
    		ProcessInfo pi = dialog.getProcessInfo();

    		onModalClose(pi);

			String s = null;
			boolean b = false;
			ProcessInfoLog[] logs = null;
			if (getActiveGridTab().isQuickForm)
			{
				s = statusBarQF.getStatusLine();
				b = statusBarQF.getStatusError();
				logs = statusBarQF.getPLogs();
			}
			else
			{
				s = statusBar.getStatusLine();
				b = statusBar.getStatusError();
				logs = statusBar.getPLogs();
			}

			
			onRefresh(true, false);

			if (getActiveGridTab().isQuickForm)
			{
				statusBarQF.setStatusLine(s, b, logs);
			}
			else
			{
				statusBar.setStatusLine(s, b, logs);
			}
    	}
    	else if (ADTabpanel.ON_DYNAMIC_DISPLAY_EVENT.equals(event.getName()))
    	{
    		ADTabpanel adtab = (ADTabpanel) event.getTarget();
    		if (adtab == adTabbox.getSelectedTabpanel()) {
    			toolbar.enableProcessButton(adtab.getToolbarButtons().size() > 0 && !adTabbox.getSelectedGridTab().isNew());
    			toolbar.dynamicDisplay();
    		}
    	}
    	else if (event.getTarget() == getComponent() && event.getName().equals(LayoutUtils.ON_REDRAW_EVENT)) {
    		ExecutionCtrl ctrl = (ExecutionCtrl) Executions.getCurrent();
    		Event evt = ctrl.getNextEvent();
    		if (evt != null) {
    			Events.sendEvent(evt);
    			Events.postEvent(new Event(LayoutUtils.ON_REDRAW_EVENT, getComponent()));
    			return;
    		}
    		LayoutUtils.redraw((AbstractComponent) getComponent());
    	}
    	else if (event.getName().equals(ON_DEFER_SET_DETAILPANE_SELECTION_EVENT)) {
    		Integer[] data = (Integer[]) event.getData();
    		adTabbox.setDetailPaneSelectedTab(data[0], data[1]);
    	}
    	else if (event.getName().equals(ON_FOCUS_DEFER_EVENT)) {
    		HtmlBasedComponent comp = (HtmlBasedComponent) event.getData();
    		comp.focus();
    	}    		
    }

	private void setActiveTab(final int newTabIndex, final Callback<Boolean> callback) {

		final int oldTabIndex = adTabbox.getSelectedIndex();

		if (oldTabIndex == newTabIndex)
		{
			if (callback != null)
				callback.onCallback(true);
		}
		else
		{
			Callback<Boolean> command = new Callback<Boolean>() {

				@Override
				public void onCallback(Boolean result) {
					if (result) {
						setActiveTab0(oldTabIndex, newTabIndex, callback);
					} else if (callback != null) {
						callback.onCallback(false);
					}
				}
			};
			Object value = Executions.getCurrent().getAttribute(CompositeADTabbox.AD_TABBOX_ON_EDIT_DETAIL_ATTRIBUTE);
			if (value != null && value == adTabbox.getSelectedDetailADTabpanel()
				&& (adTabbox.getDirtyADTabpanel() == adTabbox.getSelectedDetailADTabpanel() 
				    || (adTabbox.getDirtyADTabpanel() == null 
				        && adTabbox.getSelectedDetailADTabpanel().getGridTab().isNew()))) {
				command.onCallback(true);
			} else {
				saveAndNavigate(command);
			}
		}

	}

	public void saveAndNavigate(final Callback<Boolean> callback) {
		if (adTabbox != null)
		{
			if (adTabbox.isSortTab())
			{
				onSave(false, true, callback);
			}
			else if (adTabbox.needSave(true, false))
		    {
		    	if (adTabbox.needSave(true, true))
				{
		    		onSave(false, true, callback);
				}
				else
				{
					//  new record, but nothing changed
					adTabbox.dataIgnore();
					callback.onCallback(true);
				}
			}   //  there is a change
			else {
				// just in case
				adTabbox.dataIgnore();
				callback.onCallback(true);
			}
		}
		else
			callback.onCallback(true);
	}

	private void setActiveTab0(int oldTabIndex, int newTabIndex,
			final Callback<Boolean> callback) {
		boolean back = false;
		IADTabpanel oldTabpanel = adTabbox.getSelectedTabpanel();

		if (!adTabbox.updateSelectedIndex(oldTabIndex, newTabIndex))
		{
		    FDialog.warn(curWindowNo, "TabSwitchJumpGo", title);
		    if (callback != null)
				callback.onCallback(false);
		    return;
		}

		IADTabpanel newTabpanel = adTabbox.getSelectedTabpanel();
		
		//toggle window context update
		if (newTabpanel.getGridTab() != null)
			newTabpanel.getGridTab().setUpdateWindowContext(true);
		if (oldTabIndex > newTabIndex && oldTabpanel.getGridTab() != null)
			oldTabpanel.getGridTab().setUpdateWindowContext(false);

		boolean activated = newTabpanel.isActivated();
		if (oldTabpanel != null)
			oldTabpanel.activate(false);
		if (!activated)
			newTabpanel.activate(true);

		back = (newTabIndex < oldTabIndex);
		if (back && newTabpanel.getTabLevel() > 0)
		{
			if (newTabpanel.getTabLevel() >= oldTabpanel.getTabLevel())
				back = false;
			else if ((newTabIndex - oldTabIndex) > 1)
			{
				for (int i = oldTabIndex - 1; i > newTabIndex; i--)
				{
					IADTabpanel next = adTabbox.getADTabpanel(i);
					if (next != null && next.getTabLevel() <= newTabpanel.getTabLevel())
					{
						back = false;
						break;
					}
				}
			}
		}

		if (!back)
		{
			Object value = Executions.getCurrent().removeAttribute(CompositeADTabbox.AD_TABBOX_ON_EDIT_DETAIL_ATTRIBUTE);
			if (value != newTabpanel)
			{
				newTabpanel.query();
			}
			else 
			{
				//detail pane of the new header tab might need refresh
				if (newTabpanel instanceof ADTabpanel)
				{
					ADTabpanel adtabpanel = (ADTabpanel) newTabpanel;
					Events.echoEvent(ADTabpanel.ON_POST_INIT_EVENT, adtabpanel, null);
				}
			}				
		}
		else
		{
		    newTabpanel.refresh();
		}

		if (adTabbox.getSelectedTabpanel() instanceof ADSortTab)
		{
			((ADSortTab)adTabbox.getSelectedTabpanel()).registerAPanel(this);
		}
		else
		{
			if (adTabbox.getSelectedGridTab().getRowCount() == 0 && Env.isAutoNew(ctx, getWindowNo()))
			{
				onNew();
			}
		}

		updateToolbar();

		breadCrumb.setNavigationToolbarVisibility(!adTabbox.getSelectedGridTab().isSortTab());

		if (callback != null)
			callback.onCallback(true);
	}

	private void updateToolbar()
	{
		toolbar.enableTabNavigation(breadCrumb.hasParentLink(), adTabbox.getSelectedDetailADTabpanel() != null);

		toolbar.setPressed("Attachment",adTabbox.getSelectedGridTab().hasAttachment());
		//toolbar.setPressed("PostIt",adTabbox.getSelectedGridTab().hasPostIt());
		//toolbar.setPressed("Chat",adTabbox.getSelectedGridTab().hasChat());
		/*
		if (toolbar.isPersonalLock)
		{
			toolbar.lock(adTabbox.getSelectedGridTab().isLocked());
		}
		 */
		//toolbar.enablePrint(adTabbox.getSelectedGridTab().isPrinted() && !adTabbox.getSelectedGridTab().isNew());
		//toolbar.enablePrint(curTab, curTab.isPrinted());
		
		toolbar.enableQuickForm(adTabbox.getSelectedTabpanel().isEnableQuickFormButton() && !adTabbox.getSelectedGridTab().isReadOnly());

        boolean isNewRow = adTabbox.getSelectedGridTab().getRowCount() == 0 || adTabbox.getSelectedGridTab().isNew();
        //Deepak-Enabling customize button IDEMPIERE-364
        if(adTabbox.getSelectedTabpanel() instanceof ADSortTab){//consistent with dataStatusChanged
        	toolbar.enableProcessButton (false);
        	toolbar.enableCustomize(false);
        }else{
        	ADTabpanel adtab = (ADTabpanel) adTabbox.getSelectedTabpanel();
            toolbar.enableProcessButton(!isNewRow && adtab != null && adtab.getToolbarButtons().size() > 0);
            toolbar.enableCustomize(adtab.isGridView());
        }
        
        //Quynhnv.x8: add
        validateAPCA();
        
        
		toolbar.setPressed("Find",adTabbox.getSelectedGridTab().isQueryActive() || 
				(!isNewRow && (m_onlyCurrentRows || m_onlyCurrentDays > 0)));
		
		//toolbar.refreshUserQuery(adTabbox.getSelectedGridTab().getAD_Tab_ID(), 0);
	}

	/**
	 * @param e
	 * @see DataStatusListener#dataStatusChanged(DataStatusEvent)
	 */
    public void dataStatusChanged(DataStatusEvent e)
    {
    	//ignore non-ui thread event.
    	if (Executions.getCurrent() == null)
    		return;

    	boolean detailTab = false;
    	if (e.getSource() instanceof GridTable)
    	{
    		GridTable gridTable = (GridTable) e.getSource();
    		if (adTabbox.getSelectedGridTab() != null && adTabbox.getSelectedGridTab().getTableModel() != gridTable) {
    			detailTab = true;
    		}
    	} else if (e.getSource() instanceof GridTab)
    	{
    		GridTab gridTab = (GridTab)e.getSource();
    		if (adTabbox.getSelectedGridTab() != gridTab) detailTab = true;
    	}

    	if (!detailTab)
    	{
	        String dbInfo = e.getMessage();
	        if (logger.isLoggable(Level.INFO)) logger.info(dbInfo);
	        if (adTabbox.getSelectedGridTab() != null && adTabbox.getSelectedGridTab().isQueryActive())
	            dbInfo = "[ " + dbInfo + " ]";
	        breadCrumb.setStatusDB(dbInfo, e, adTabbox.getSelectedGridTab());

	        String adInfo = e.getAD_Message();
	        if (   adInfo == null
	        	|| GridTab.DEFAULT_STATUS_MESSAGE.equals(adInfo)
	        	|| GridTable.DATA_REFRESH_MESSAGE.equals(adInfo)
	        	|| GridTable.DATA_INSERTED_MESSAGE.equals(adInfo)
	        	|| GridTable.DATA_IGNORED_MESSAGE.equals(adInfo)
	        	|| GridTable.DATA_UPDATE_COPIED_MESSAGE.equals(adInfo)
	           ) {

		        String prefix = null;
		        if (dbInfo.contains("*") || dbInfo.contains("?")) // ? used when not-autosave
		        	prefix = "*";

		        String titleLogic = null;
		        int windowID = getADTab().getSelectedGridTab().getAD_Window_ID();
		        if (windowID > 0) {
		        	titleLogic = MWindow.get(Env.getCtx(), windowID).getTitleLogic();
		        }
		        String header = null;
		        if (! Util.isEmpty(titleLogic)) {
			        StringBuilder sb = new StringBuilder();
			        if (prefix != null)
			        	sb.append(prefix);
					sb.append(Env.getContext(ctx, curWindowNo, "_WinInfo_WindowName", false)).append(": ");
					if (titleLogic.contains("<")) {
						// IDEMPIERE-1328 - enable using format or subcolumns on title
						if (   getADTab() != null
							&& getADTab().getADTabpanel(0) != null
							&& getADTab().getADTabpanel(0).getGridTab() != null
							&& getADTab().getADTabpanel(0).getGridTab().getTableModel() != null) {
							GridTab tab = getADTab().getADTabpanel(0).getGridTab();
							int row = tab.getCurrentRow();
							int cnt = tab.getRowCount();
							boolean inserting = tab.getTableModel().isInserting();
							if (row >= 0 && cnt > 0 && !inserting) {
								PO po = tab.getTableModel().getPO(row);
								titleLogic = Env.parseVariable(titleLogic, po, null, false);
							} else {
								titleLogic = Env.parseContext(Env.getCtx(), curWindowNo, titleLogic, false, true);
							}
						}
					} else {
						titleLogic = Env.parseContext(Env.getCtx(), curWindowNo, titleLogic, false, true);
					}
	        		sb.append(titleLogic);
	        		header = sb.toString().trim();
	        		if (header.endsWith(":"))
	        			header = header.substring(0, header.length()-1);
		        }
		        if (Util.isEmpty(header))
		        	header = AEnv.getDialogHeader(Env.getCtx(), curWindowNo, prefix);

		        SessionManager.getAppDesktop().setTabTitle(header, curWindowNo);
	        }
    	}
    	else if (adTabbox.getSelectedDetailADTabpanel() == null)
    	{
    		return;
    	}

        //  Set Message / Info
        if (e.getAD_Message() != null || e.getInfo() != null)
        {
        	if (GridTab.DEFAULT_STATUS_MESSAGE.equals(e.getAD_Message()))
        	{
        		if (detailTab) {
        			String msg = e.getTotalRows() + " " + Msg.getMsg(Env.getCtx(), "Records");
                	adTabbox.setDetailPaneStatusMessage(msg, false);
				} else {
					if (getActiveGridTab().isQuickForm)
					{
						statusBarQF.setStatusLine("", false);
					}
					else
					{
						statusBar.setStatusLine("", false);
					}
				}
        	}
        	else
        	{
	            StringBuilder sb = new StringBuilder();
	            String msg = e.getMessage();
	            StringBuilder adMessage = new StringBuilder();
	            String origmsg = null;
	            if (msg != null && msg.length() > 0)
	            {
	            	if (detailTab && GridTable.DATA_REFRESH_MESSAGE.equals(e.getAD_Message()))
	            	{
	            		origmsg = e.getTotalRows() + " " + Msg.getMsg(Env.getCtx(), "Records");
	            	}
	            	else
	            	{
	            		origmsg = Msg.getMsg(Env.getCtx(), e.getAD_Message());
	            	}
	            	adMessage.append(origmsg);
	            }
	            String info = e.getInfo();
	            if (info != null && info.length() > 0)
	            {
	            	Object[] arguments = info.split("[;]");
	            	int index = 0;
	            	while(index < arguments.length)
	            	{
	            		String expr = "{"+index+"}";
	            		if (adMessage.indexOf(expr) >= 0)
	            		{
	            			index++;
	            		}
	            		else
	            		{
	            			break;
	            		}
	            	}
	            	if (index < arguments.length)
	            	{
	            		if (adMessage.length() > 0 && !adMessage.toString().trim().endsWith(":"))
		                    adMessage.append(": ");
	            		StringBuilder tail = new StringBuilder();
	            		while(index < arguments.length)
	            		{
	            			if (tail.length() > 0) tail.append(", ");
	            			tail.append("{").append(index).append("}");
	            			index++;
	            		}
	            		adMessage.append(tail);
	            	}
					if (   arguments.length == 1 
						&& origmsg != null 
						&& origmsg.equals(arguments[0])) { // check dup message
		            	sb.append(origmsg);
					} else {
		            	String adMessageQuot = Util.replace(adMessage.toString(), "'", "''");
		            	sb.append(MessageFormat.format(adMessageQuot, arguments));
	            	}
	            }
	            else
	            {
	            	sb.append(adMessage);
	            }
	            if (sb.length() > 0)
	            {
	                int pos = sb.indexOf("\n");
	                if (pos != -1 && pos+1 < sb.length())  // replace CR/NL
	                {
	                    sb.replace(pos, pos+1, " - ");
	            	}
	                if (detailTab) {
	                	adTabbox.setDetailPaneStatusMessage(sb.toString (), e.isError ());
	                } else {
	                	if (getActiveGridTab().isQuickForm)
						{
	                		statusBarQF.setStatusLine(sb.toString(), e.isError());
						}
						else
						{
							statusBar.setStatusLine(sb.toString(), e.isError());
						}
	                }
	            }
        	}
        }

        IADTabpanel tabPanel = detailTab ? adTabbox.getSelectedDetailADTabpanel()
    			: getADTab().getSelectedTabpanel();

        //  Confirm Error
        if (e.isError() && !e.isConfirmed() && tabPanel instanceof ADTabpanel)
        {
        	//focus to error field
        	GridField[] fields = tabPanel.getGridTab().getFields();
        	for (GridField field : fields)
        	{
        		if (field.isError())
        		{
        			((ADTabpanel)tabPanel).setFocusToField(field.getColumnName());
        			break;
        		}
        	}
            e.setConfirmed(true);   //  show just once - if MTable.setCurrentRow is involved the status event is re-issued
        }
        //  Confirm Warning
        else if (e.isWarning() && !e.isConfirmed())
        {
        	boolean isImporting = false; 
        	if (e.getSource() instanceof GridTab) {
        		GridTab gridTab = (GridTab)e.getSource();
        		isImporting = gridTab.getTableModel().isImporting();
        	} else if (e.getSource() instanceof GridTable) {
        		GridTable gridTable = (GridTable) e.getSource();
        		isImporting = gridTable.isImporting();
        	}
        	if (!isImporting) {
        		FDialog.warn(curWindowNo, null, e.getAD_Message(), e.getInfo());
        		e.setConfirmed(true);   //  show just once - if MTable.setCurrentRow is involved the status event is re-issued
        	}
        }

        boolean changed = e.isChanged() || e.isInserting();
        boolean readOnly = adTabbox.getSelectedGridTab().isReadOnly();
        boolean processed = adTabbox.getSelectedGridTab().isProcessed();
        boolean insertRecord = !readOnly;
        boolean deleteRecord = !readOnly;
        if (!detailTab)
        {
	        //  update Change
	        boolChanges = changed;

	        if (insertRecord)
	        {
	            insertRecord = tabPanel.getGridTab().isInsertRecord();
	        }
	        toolbar.enableNew(!changed && insertRecord && !tabPanel.getGridTab().isSortTab());
	        toolbar.enableCopy(!changed && insertRecord && !tabPanel.getGridTab().isSortTab() && adTabbox.getSelectedGridTab().getRowCount()>0);
	        toolbar.enableRefresh(!changed);
	        if (deleteRecord)
	        {
	        	deleteRecord = tabPanel.getGridTab().isDeleteRecord();
	        }
	        toolbar.enableDelete(!changed && deleteRecord && !tabPanel.getGridTab().isSortTab() && !processed);
	        //toolbar.enableReport(true);
	        //
	        if (readOnly && adTabbox.getSelectedGridTab().isAlwaysUpdateField())
	        {
	            readOnly = false;
	        }
	        //Quynhnv.x8
	        validateAPCA();			
        }
        else
        {
        	adTabbox.updateDetailPaneToolbar(changed, readOnly);
        }
        boolean isEditting = adTabbox.needSave(true, false) ||
        		adTabbox.getSelectedGridTab().isNew() ||
        		(adTabbox.getSelectedDetailADTabpanel() != null && adTabbox.getSelectedDetailADTabpanel().getGridTab().isNew());
        toolbar.enableIgnore(isEditting);
        
        switchEditStatus (isEditting);
        
        if (changed && !readOnly && !toolbar.isSaveEnable() ) {
        	if (tabPanel.getGridTab().getRecord_ID() > 0) {
            	if (adTabbox.getSelectedIndex() == 0 && !detailTab) {
            		MRecentItem.addModifiedField(ctx, adTabbox.getSelectedGridTab().getAD_Table_ID(),
            				adTabbox.getSelectedGridTab().getRecord_ID(), Env.getAD_User_ID(ctx),
            				Env.getAD_Role_ID(ctx), adTabbox.getSelectedGridTab().getAD_Window_ID(),
            				adTabbox.getSelectedGridTab().getAD_Tab_ID());
            	} else {
	        		GridTab mainTab = getMainTabAbove();
	        		if (mainTab != null) {
			        	MRecentItem.addModifiedField(ctx, mainTab.getAD_Table_ID(),
			        			mainTab.getRecord_ID(), Env.getAD_User_ID(ctx),
			        			Env.getAD_Role_ID(ctx), mainTab.getAD_Window_ID(),
			        			mainTab.getAD_Tab_ID());
	        		}
            	}
        	}
        }

        toolbar.enableSave(adTabbox.needSave(true, false) ||
        		adTabbox.getSelectedGridTab().isNew() ||
        		(adTabbox.getSelectedDetailADTabpanel() != null && adTabbox.getSelectedDetailADTabpanel().getGridTab().isNew()));

        //
        //  No Rows
        if (e.getTotalRows() == 0 && insertRecord && !detailTab && !tabPanel.getGridTab().isSortTab())
        {
            toolbar.enableNew(true);
            toolbar.enableCopy(false);
            toolbar.enableDelete(false);
        }

        //  Transaction info
        if (!detailTab)
        {
        	GridTab gt = adTabbox.getSelectedGridTab();
	        String trxInfo = "";
	        //TODO: Quynhnv.x8 => Bo phan nay vi chua hop ly khi dung thuc te. can phai sua them 
	        //gt.getStatusLine();
	        //if (trxInfo == null)
	        //	trxInfo = "";
            statusBar.setInfo(trxInfo);
	        SessionManager.getAppDesktop().updateHelpQuickInfo(gt);
        }

	    //  Check Attachment
        boolean canHaveAttachment = adTabbox.getSelectedGridTab().canHaveAttachment();       //  not single _ID column
        //
        if (canHaveAttachment && e.isLoading() &&
                adTabbox.getSelectedGridTab().getCurrentRow() > e.getLoadedRows())
        {
            canHaveAttachment = false;
        }
        if (canHaveAttachment && adTabbox.getSelectedGridTab().getRecord_ID() == -1)    //   No Key
        {
            canHaveAttachment = false;
        }
        if (canHaveAttachment)
        {
            toolbar.enableAttachment(true);
            toolbar.setPressed("Attachment",adTabbox.getSelectedGridTab().hasAttachment());
        }
        else
        {
            toolbar.enableAttachment(false);
        }

        // Check Chat and PostIt
        /*
        boolean canHaveChat = true;
        if (e.isLoading() &&
                adTabbox.getSelectedGridTab().getCurrentRow() > e.getLoadedRows())
        {
            canHaveChat = false;
        }
        if (canHaveChat && adTabbox.getSelectedGridTab().getRecord_ID() == -1)    //   No Key
        {
            canHaveChat = false;
        }
        if (canHaveChat)
        {
            toolbar.enableChat(true);
            toolbar.setPressed("Chat",adTabbox.getSelectedGridTab().hasChat());
            toolbar.enablePostIt(true);
            toolbar.setPressed("PostIt",adTabbox.getSelectedGridTab().hasPostIt());
        }
        else
        {
        	toolbar.enableChat(false);
        	toolbar.enablePostIt(false);
        }
		*/
        // Elaine 2008/12/05
        //  Lock Indicator
        /*
        if (toolbar.isPersonalLock)
        {
			toolbar.lock(adTabbox.getSelectedGridTab().isLocked());
        }
        */
        //

        if (!detailTab) 
        {
        	adTabbox.evaluate(e);
        }

        boolean isNewRow = adTabbox.getSelectedGridTab().getRowCount() == 0 || adTabbox.getSelectedGridTab().isNew();
        //toolbar.enableArchive(!isNewRow);
        //toolbar.enableZoomAcross(!isNewRow);
       // toolbar.enableActiveWorkflows(!isNewRow);
        //toolbar.enableRequests(!isNewRow);
		toolbar.setPressed("Find", adTabbox.getSelectedGridTab().isQueryActive() || 
				(!isNewRow && (m_onlyCurrentRows || m_onlyCurrentDays > 0)));
		//toolbar.refreshUserQuery(adTabbox.getSelectedGridTab().getAD_Tab_ID(), 0);

        //toolbar.enablePrint(adTabbox.getSelectedGridTab().isPrinted() && !isNewRow);
        //toolbar.enablePrint(curTab, curTab.isPrinted());
        //toolbar.enableReport(!isNewRow);
        toolbar.enableExport(!isNewRow && !adTabbox.getSelectedGridTab().isSortTab());
        toolbar.enableFileImport(toolbar.isNewEnabled());
		//toolbar.enableCSVImport(toolbar.isNewEnabled() && adTabbox.getSelectedGridTab().hasTemplate());
        
        toolbar.enableTabNavigation(breadCrumb.hasParentLink(), adTabbox.getSelectedDetailADTabpanel() != null);
        
        //Deepak-Enabling customize button IDEMPIERE-364
        if(adTabbox.getSelectedTabpanel() instanceof ADSortTab){//consistent with updateToolbar
        	toolbar.enableProcessButton (false);
        	toolbar.enableCustomize(false);
        }else{
        	ADTabpanel adtab = (ADTabpanel) adTabbox.getSelectedTabpanel();
            toolbar.enableProcessButton(!isNewRow && adtab != null && adtab.getToolbarButtons().size() > 0);
            toolbar.enableCustomize(adtab.isGridView());
        }
        
    }

    
    
    /**
     * @return boolean
     */
    public boolean isFirstTab()
    {
        int selTabIndex = adTabbox.getSelectedIndex();
        return (selTabIndex == 0);
    }

    /**
     * refresh all row
     * @param fireEvent
     */
    public void onRefresh(final boolean fireEvent)
    {
    	onRefresh(fireEvent, true);
    }

    /**
     * refresh all row
     * @param fireEvent
     * @param saveCurrentRow
     */
    public void onRefresh(final boolean fireEvent, final boolean saveCurrentRow)
    {
    	if (saveCurrentRow)
    	{
	    	onSave(false, true, new Callback<Boolean>() {

				@Override
				public void onCallback(Boolean result) {
					doOnRefresh(fireEvent);
				}
			});
    	}
    	else
    	{
    		doOnRefresh(fireEvent);    		
    	}
    	//updateToolbar();
    }

	/**
	 * @param fireEvent
	 */
	protected void doOnRefresh(final boolean fireEvent) {
		IADTabpanel headerTab = adTabbox.getSelectedTabpanel();
		IADTabpanel detailTab = adTabbox.getSelectedDetailADTabpanel();
		adTabbox.getSelectedGridTab().dataRefreshAll(fireEvent, true);
		adTabbox.getSelectedGridTab().refreshParentTabs();
		headerTab.dynamicDisplay(0);
		if (detailTab != null)
		{
			detailTab.dynamicDisplay(0);
		}
		focusToActivePanel();
		// IDEMPIERE-1328 - refresh recent item after running a process, i.e. completing a doc that changes documentno
    	MRecentItem.touchUpdatedRecord(ctx, adTabbox.getSelectedGridTab().getAD_Table_ID(),
    			adTabbox.getSelectedGridTab().getRecord_ID(), Env.getAD_User_ID(ctx));
	}

    /**
     * @see ToolbarListener#onRefresh()
     */
    public void onRefresh()
    {
    	GridTab gridTab = adTabbox.getSelectedGridTab();
    	/*if (gridTab != null && gridTab.getTableModel() != null)
    	{
    		gridTab.getTableModel().resetCacheSortState();
    	}
    	Column sortColumn = findCurrentSortColumn();*/
    	onRefresh(true, false);
    	/*if (sortColumn != null)
    	{
    		sortColumn.setSortDirection("natural");
    	}*/
    	if (gridTab.isSortTab()) { // refresh is not refreshing sort tabs
    		IADTabpanel tabPanel = adTabbox.getSelectedTabpanel();
    		tabPanel.query(false, 0, 0);
    	}
    }
/*
    private Column findCurrentSortColumn() {
		IADTabpanel iadtabpanel = getADTab().getSelectedTabpanel();
		if (iadtabpanel instanceof ADTabpanel) {
			ADTabpanel adtabpanel = (ADTabpanel) iadtabpanel;
			Grid grid = adtabpanel.getGridView().getListbox();
			Columns columns = grid.getColumns();
			if (columns != null) {
				List<?> list = columns.getChildren();
				for(int i = 0; i < list.size(); i++)
				{
					Component c = (Component) list.get(i);
					if (c instanceof Column) {
						Column column = (Column) c;
						if (!"natural".equals(column.getSortDirection())) {
							return column;
						}
					}
				}
			}
		}
		return null;
	}
*/
    /**
     * @see ToolbarListener#onHelp()
     */
    public void onHelp()
    {
    	SessionManager.getAppDesktop().showWindow(new HelpWindow(gridWindow), "center");
    }

    @Override
    public void onNew()
    {
    	final Callback<Boolean> postCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.AFTER_NEW.getName());
			    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, null);
				}
			}
		};
    	Callback<Boolean> preCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					onNewCallback(postCallback);
				}
			}
		};
		
		WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.BEFORE_NEW.getName());
    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, preCallback);
    }
    
    private void onNewCallback(final Callback<Boolean> postCallback)
    {
        if (!adTabbox.getSelectedGridTab().isInsertRecord())
        {
            logger.warning("Insert Record disabled for Tab");
            if (postCallback != null)
            	postCallback.onCallback(false);
            return;
        }

        saveAndNavigate(new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result)
				{
					boolean newRecord = adTabbox.getSelectedGridTab().dataNew(false);
			        if (newRecord)
			        {
			            adTabbox.getSelectedTabpanel().dynamicDisplay(0);
			            toolbar.enableNew(false);
			            toolbar.enableCopy(false);
			            toolbar.enableDelete(false);
			            breadCrumb.enableFirstNavigation(adTabbox.getSelectedGridTab().getCurrentRow() > 0);
			            breadCrumb.enableLastNavigation(adTabbox.getSelectedGridTab().getCurrentRow() + 1 < adTabbox.getSelectedGridTab().getRowCount());
			            toolbar.enableTabNavigation(breadCrumb.hasParentLink(), adTabbox.getSelectedDetailADTabpanel() != null);
			            toolbar.enableIgnore(true);
			            if (adTabbox.getSelectedGridTab().isSingleRow()) 
			            {
			            	if (adTabbox.getSelectedTabpanel().isGridView())
			            	{
			            		adTabbox.getSelectedTabpanel().switchRowPresentation();
			            	}
			            }
			            
			            if (adTabbox.getSelectedTabpanel().isGridView())
			            {
			            	adTabbox.getSelectedTabpanel().getGridView().onEditCurrentRow();
			            }
			            if (postCallback != null)
			            	postCallback.onCallback(true);
			        }
			        else
			        {
			            logger.severe("Could not create new record");
			            if (postCallback != null)
			            	postCallback.onCallback(false);
			        }
			        focusToActivePanel();
				}
				else
				{
					if (postCallback != null)
		            	postCallback.onCallback(result);
				}
			}
		});
    }

    @Override
    public void onCopy()
    {
    	final Callback<Boolean> postCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.AFTER_COPY.getName());
			    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, null);
				}
			}
		};
    	Callback<Boolean> preCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					onCopyCallback(postCallback);
				}
			}
		};
		
		WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.BEFORE_COPY.getName());
    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, preCallback);
    }
    
	// Elaine 2008/11/19
    private void onCopyCallback(Callback<Boolean> postCallback)
    {
        if (!adTabbox.getSelectedGridTab().isInsertRecord())
        {
            logger.warning("Insert Record disabled for Tab");
            if (postCallback != null)
            	postCallback.onCallback(false);
            return;
        }

        boolean newRecord = adTabbox.getSelectedGridTab().dataNew(true);
        if (newRecord)
        {
            adTabbox.getSelectedTabpanel().dynamicDisplay(0);
            toolbar.enableNew(false);
            toolbar.enableCopy(false);
            toolbar.enableDelete(false);
            breadCrumb.enableFirstNavigation(adTabbox.getSelectedGridTab().getCurrentRow() > 0);
            breadCrumb.enableLastNavigation(adTabbox.getSelectedGridTab().getCurrentRow() + 1 < adTabbox.getSelectedGridTab().getRowCount());
            toolbar.enableTabNavigation(false);
            toolbar.enableIgnore(true);
            if (postCallback != null)
            	postCallback.onCallback(true);
            
        }
        else
        {
            logger.severe("Could not create new record");
            if (postCallback != null)
            	postCallback.onCallback(false);
        }
        focusToActivePanel();
    }
    //

    /**
     * @see ToolbarListener#onFind()
     */
    public void onFind()
    {
    	if (adTabbox.getSelectedGridTab() == null)
            return;

        clearTitleRelatedContext();
        
        // The record was not changed locally
        if (adTabbox.getDirtyADTabpanel() == null) {
        	doOnFind();
        } else {
            onSave(false, false, new Callback<Boolean>() {
    			@Override
    			public void onCallback(Boolean result) {
    				if (result) {
    					doOnFind();
    				}
    			}
    		});        	
        }
        
    }

	private void doOnFind() {
		//  Gets Fields from AD_Field_v
        GridField[] findFields = adTabbox.getSelectedGridTab().getFields();
        
        if (findWindow == null || !findWindow.validate(adTabbox.getSelectedGridTab().getWindowNo(), adTabbox.getSelectedGridTab().getName(),
            adTabbox.getSelectedGridTab().getAD_Table_ID(), adTabbox.getSelectedGridTab().getTableName(),
            adTabbox.getSelectedGridTab().getWhereExtended(), findFields, 1, adTabbox.getSelectedGridTab().getAD_Tab_ID())) {
	        findWindow = new FindWindow (adTabbox.getSelectedGridTab().getWindowNo(), adTabbox.getSelectedGridTab().getName(),
	            adTabbox.getSelectedGridTab().getAD_Table_ID(), adTabbox.getSelectedGridTab().getTableName(),
	            adTabbox.getSelectedGridTab().getWhereExtended(), findFields, 1, adTabbox.getSelectedGridTab().getAD_Tab_ID());

	        setupEmbeddedFindwindow();	        
	        if (!findWindow.initialize()) {
	        	if (findWindow.getTotalRecords() == 0) {
	        		FDialog.info(curWindowNo, getComponent(), "NoRecordsFound");
	        	}
	        	return;
	        }
        }

        if (!findWindow.getEventListeners(DialogEvents.ON_WINDOW_CLOSE).iterator().hasNext()) {
        	findWindow.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					hideBusyMask();
					if (!findWindow.isCancel())
			        {
				        MQuery query = findWindow.getQuery();

				        //  Confirmed query
				        if (query != null)
				        {
				            m_onlyCurrentRows = false;          //  search history too
				            adTabbox.getSelectedGridTab().setQuery(query);
				            adTabbox.getSelectedTabpanel().query(m_onlyCurrentRows, m_onlyCurrentDays, adTabbox.getSelectedGridTab().getMaxQueryRecords());   //  autoSize
				        }

				        if (findWindow.isCreateNew())
				        	onNew();
				        else {
				        	adTabbox.getSelectedGridTab().dataRefresh(false); // Elaine 2008/07/25

				        	if (!adTabbox.getSelectedTabpanel().isGridView()) { // See if we should force the grid view

				        		boolean forceGridView = false;
				        		String up = Env.getContext(Env.getCtx(), MUserPreference.COLUMNNAME_ViewFindResult);

				        		if (up.equals(MUserPreference.VIEWFINDRESULT_Default)) {
				        			forceGridView = MSysConfig.getBooleanValue(ZK_GRID_AFTER_FIND, false, Env.getAD_Client_ID(Env.getCtx()));
				        		}
				        		else if (up.equals(MUserPreference.VIEWFINDRESULT_AlwaysInGridView)) {
				        			forceGridView = true;
				        		}
				        		else if (up.equals(MUserPreference.VIEWFINDRESULT_AccordingToThreshold)) {
				        			forceGridView = adTabbox.getSelectedTabpanel().getGridTab().getRowCount() >= Env.getContextAsInt(Env.getCtx(), MUserPreference.COLUMNNAME_GridAfterFindThreshold);
				        		}

				        		if (forceGridView)
				        			adTabbox.getSelectedTabpanel().switchRowPresentation();
				        	}
				        }
				       // toolbar.refreshUserQuery(adTabbox.getSelectedGridTab().getAD_Tab_ID(), 0);
			        }
					else
					{
						toolbar.setPressed("Find",adTabbox.getSelectedGridTab().isQueryActive());
					}
			        focusToActivePanel();
				}
			});
        }

        getComponent().getParent().appendChild(findWindow);
        //AEnv.showWindow(findWindow);
        showBusyMask(findWindow);                
        LayoutUtils.openEmbeddedWindow(toolbar, findWindow, "after_start");
       
	}

	@Override
	public void onIgnore() 
	{
    	final Callback<Boolean> postCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.AFTER_IGNORE.getName());
			    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, null);
				}
			}
		};
    	Callback<Boolean> preCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					onIgnoreCallback(postCallback);
				}
			}
		};
		
		WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.BEFORE_IGNORE.getName());
    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, preCallback);
    }
	
    private void onIgnoreCallback(Callback<Boolean> postCallback)
    {
    	IADTabpanel dirtyTabpanel = adTabbox.getDirtyADTabpanel();
    	boolean newrecod = adTabbox.getSelectedGridTab().isNew();
    	if (dirtyTabpanel != null && dirtyTabpanel.getGridTab().isSortTab())
    	{
    		adTabbox.dataIgnore();
    		toolbar.enableIgnore(false);
    	}
    	else
    	{
    		clearTitleRelatedContext();

	        adTabbox.dataIgnore();
	        toolbar.enableIgnore(false);
	        if (newrecod) {
	        	onRefresh(true, false);
	        } else if (dirtyTabpanel != null) {
	        	dirtyTabpanel.getGridTab().dataRefresh(true);	// update statusbar & toolbar
	        	dirtyTabpanel.dynamicDisplay(0);
	        } else {
	        	onRefresh(true, false);
	        }

    	}
    	if (dirtyTabpanel != null) {
    		focusToTabpanel(dirtyTabpanel);
    		//ensure row indicator is not lost
    		if (dirtyTabpanel.getGridView() != null && 
    				dirtyTabpanel.getGridView().getListbox() != null &&
    				dirtyTabpanel.getGridView().getListbox().getRowRenderer() != null) {
        		RowRenderer<Object[]> renderer = dirtyTabpanel.getGridView().getListbox().getRowRenderer();
        		GridTabRowRenderer gtr = (GridTabRowRenderer)renderer;
        		org.zkoss.zul.Row row = gtr.getCurrentRow();
        		if (row != null)
        			gtr.setCurrentRow(row);    			
    		}
    	}
    	else
    		focusToActivePanel();

    	updateToolbar();
    	
    	if (postCallback != null)
    		postCallback.onCallback(true);
    }

    /**
     * @see ToolbarListener#onSave()
     */
    @Override
    public void onSave()
    {
    	final IADTabpanel dirtyTabpanel = adTabbox.getDirtyADTabpanel();
		onSave(true, false, new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result)
			{
				if (result)
				{
					String statusLine = null;
					if (getActiveGridTab().isQuickForm)
					{
						statusLine = statusBarQF.getStatusLine();
					}
					else
					{
						statusLine = statusBar.getStatusLine();
					}
					adTabbox.getSelectedGridTab().dataRefreshAll(true, true);
					adTabbox.getSelectedGridTab().refreshParentTabs();
					if (getActiveGridTab().isQuickForm)
					{
						statusBarQF.setStatusLine(statusLine);
					}
					else
					{
						statusBar.setStatusLine(statusLine);
					}
					if (adTabbox.getSelectedDetailADTabpanel() != null && adTabbox.getSelectedDetailADTabpanel().getGridTab() != null)
						adTabbox.getSelectedDetailADTabpanel().getGridTab().dataRefreshAll(true, true);
				}
				if (dirtyTabpanel != null) {
					if (dirtyTabpanel == adTabbox.getSelectedDetailADTabpanel())
						Clients.scrollIntoView(dirtyTabpanel);
					focusToTabpanel(dirtyTabpanel);
				} else {
					focusToActivePanel();
				}
				
				if(adTabbox.getSelectedGridTab().isQuickForm())
					onRefresh(true, true);
			}
		});
    }

    public void onSave(final boolean onSaveEvent, final boolean onNavigationEvent, final Callback<Boolean> callback) {
    	final Callback<Boolean> postCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.AFTER_SAVE.getName());
			    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, callback);
				} else {
					callback.onCallback(result);
				}
			}
		};
		
    	Callback<Boolean> preCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					onSaveCallback(onSaveEvent, onNavigationEvent, postCallback);
				} else if (callback != null) {
					callback.onCallback(result);
				}
			}
		};
		
    	WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.BEFORE_SAVE.getName());
    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, preCallback);
	}

	public void onSavePayment()
    {
    	onSave(false, false, new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean result) {
				onRefresh(true, false);
			}

		});
    }

    /**
     * @param onSaveEvent
     */
    private void onSaveCallback(final boolean onSaveEvent, final boolean onNavigationEvent, final Callback<Boolean> callback)
    {
    	final boolean wasChanged = toolbar.isSaveEnable();
    	IADTabpanel dirtyTabpanel = adTabbox.getDirtyADTabpanel();
    	final boolean newRecord = dirtyTabpanel != null ? (dirtyTabpanel.getGridTab().isNew()) : adTabbox.getSelectedGridTab().isNew();
    	if (dirtyTabpanel == null) {
			onSave0(onSaveEvent, onNavigationEvent, newRecord, wasChanged, callback);
			return;
    	}
    	if (!Util.isEmpty(dirtyTabpanel.getGridTab().getCommitWarning()) ||
			(!Env.isAutoCommit(ctx, curWindowNo) && onNavigationEvent))
		{
			FDialog.ask(curWindowNo, this.getComponent(), dirtyTabpanel.getGridTab().getCommitWarning(), Msg.getMsg(Env.getCtx(), "SaveChanges?"), new Callback<Boolean>() {

				@Override
				public void onCallback(Boolean result)
				{
					if (result)
					{
						onSave0(onSaveEvent, onNavigationEvent, newRecord, wasChanged, callback);
					}
					else
					{
						if (callback != null)
			    			callback.onCallback(false);
					}
				}
			});
		}
		else
		{
			onSave0(onSaveEvent, onNavigationEvent, newRecord, wasChanged, callback);
		}
    }

	private void onSave0(boolean onSaveEvent, boolean navigationEvent,
			boolean newRecord, boolean wasChanged, Callback<Boolean> callback) {
		IADTabpanel dirtyTabpanel = adTabbox.getDirtyADTabpanel();
		boolean retValue = adTabbox.dataSave(onSaveEvent);

		if (!retValue)
		{
			showLastError();
			if (callback != null)
				callback.onCallback(false);
			return;
		} else if (!onSaveEvent && dirtyTabpanel != null && !(dirtyTabpanel instanceof ADSortTab)) //need manual refresh
		{
			dirtyTabpanel.getGridTab().setCurrentRow(dirtyTabpanel.getGridTab().getCurrentRow());
		}

		if (!navigationEvent && dirtyTabpanel != null) {
			dirtyTabpanel.dynamicDisplay(0);
			dirtyTabpanel.afterSave(onSaveEvent);
		}

		IADTabpanel dirtyTabpanel2 = adTabbox.getDirtyADTabpanel();
		if (dirtyTabpanel2 != null && dirtyTabpanel2 != dirtyTabpanel) {
			onSave(onSaveEvent, navigationEvent, callback);
			return;
		} else if (dirtyTabpanel instanceof ADSortTab) {
			ADSortTab sortTab = (ADSortTab) dirtyTabpanel;
			if (!sortTab.isChanged()) {
				if (sortTab == adTabbox.getSelectedTabpanel()) {
					if (getActiveGridTab().isQuickForm)
					{
						statusBarQF.setStatusLine(Msg.getMsg(Env.getCtx(), "Saved"));
					}
					else
					{
						statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "Saved"));
					}
				} else {
    				adTabbox.setDetailPaneStatusMessage(Msg.getMsg(Env.getCtx(), "Saved"), false);
    			}
    		}
		}

		if (wasChanged) {
		    if (newRecord) {
		    	if (adTabbox.getSelectedGridTab().getRecord_ID() > 0) {
		        	if (adTabbox.getSelectedIndex() == 0) {
			        	MRecentItem.addModifiedField(ctx, adTabbox.getSelectedGridTab().getAD_Table_ID(),
			        			adTabbox.getSelectedGridTab().getRecord_ID(), Env.getAD_User_ID(ctx),
			        			Env.getAD_Role_ID(ctx), adTabbox.getSelectedGridTab().getAD_Window_ID(),
			        			adTabbox.getSelectedGridTab().getAD_Tab_ID());
		        	} else {
		        		GridTab mainTab = getMainTabAbove();
		        		if (mainTab != null) {
				        	MRecentItem.addModifiedField(ctx, mainTab.getAD_Table_ID(),
				        			mainTab.getRecord_ID(), Env.getAD_User_ID(ctx),
				        			Env.getAD_Role_ID(ctx), mainTab.getAD_Window_ID(),
				        			mainTab.getAD_Tab_ID());
		        		}
		        	}
		    	}
		    } else {
		    	if (adTabbox.getSelectedIndex() == 0) {
		        	MRecentItem.touchUpdatedRecord(ctx, adTabbox.getSelectedGridTab().getAD_Table_ID(),
		        			adTabbox.getSelectedGridTab().getRecord_ID(), Env.getAD_User_ID(ctx));
		    	} else {
	        		GridTab mainTab = getMainTabAbove();
		    		if (mainTab != null) {
			        	MRecentItem.touchUpdatedRecord(ctx, mainTab.getAD_Table_ID(),
			        			mainTab.getRecord_ID(), Env.getAD_User_ID(ctx));
		    		}
		    	}
		    }
		}

		if (dirtyTabpanel != null && dirtyTabpanel != adTabbox.getSelectedTabpanel()) {
			Executions.getCurrent().setAttribute("adtabpane.saved", dirtyTabpanel);
			dirtyTabpanel.getGridTab().refreshParentTabs();
		}
		
		if (callback != null)
			callback.onCallback(true);
	}

	private GridTab getMainTabAbove() {
		/* when a detail record is modified add header to recent items */
		GridTab mainTab = adTabbox.getSelectedGridTab(); // find parent tab (IDEMPIERE-2125 - tbayen)
		while (mainTab != null && mainTab.getTabLevel() > 0) {
			GridTab parentTab = mainTab.getParentTab();
			if (parentTab == mainTab)
				break;
			mainTab = parentTab;
		}
		return mainTab;
	}

	private void showLastError() {
		String msg = CLogger.retrieveErrorString(null);
		if (msg != null)
		{
			if (getActiveGridTab().isQuickForm)
			{
				statusBarQF.setStatusLine(Msg.getMsg(Env.getCtx(), msg), true);
			}
			else
			{
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), msg), true);
			}
		}
		//other error will be catch in the dataStatusChanged event
	}

	private void showLastWarning() {
		String msg = CLogger.retrieveWarningString(null);
		if (msg != null)
		{
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), msg), true);
		}
	}

	/**
	 * @see ToolbarListener#onSaveCreate()
	 */
	public void onSaveCreate()
    {
    	onSave(true, true, new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean result)
			{
				if(result)
		    	{
		    		adTabbox.getSelectedGridTab().dataRefreshAll(true, true);
		    		adTabbox.getSelectedGridTab().refreshParentTabs();
		    		IADTabpanel dirtyTabpanel = (IADTabpanel) Executions.getCurrent().removeAttribute("adtabpane.saved");
		    		if (dirtyTabpanel != null && dirtyTabpanel.getGridTab().isDetail()) {
		    			try {
							adTabbox.getSelectedTabpanel().getDetailPane().onNew();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
		    		} else {
		    			onNew();
		    		}
		    	}
			}
		});
    }

	@Override
	public void onDelete()
	{
		final Callback<Boolean> postCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.AFTER_DELETE.getName());
			    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, null);
				}
			}
		};
    	Callback<Boolean> preCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					onDeleteCallback(postCallback);
				}
			}
		};
		
		WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.BEFORE_DELETE.getName());
    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, preCallback);
	}
	
    private void onDeleteCallback(final Callback<Boolean> postCallback)
    {
        if (adTabbox.getSelectedGridTab().isReadOnly())
        {
        	if (postCallback != null)
        		postCallback.onCallback(false);
            return;
        }
        
        //delete selected if it is grid view and row selection
        final int[] indices = adTabbox.getSelectedGridTab().getSelection();
		if (indices.length > 0 && adTabbox.getSelectedTabpanel().isGridView())
		{
			onDeleteSelected(postCallback);
			return;
		}

        FDialog.ask(curWindowNo, null, "DeleteRecord?", new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean result)
			{
				if (result)
				{
		        	//error will be catch in the dataStatusChanged event
		            boolean success = adTabbox.getSelectedGridTab().dataDelete();
		            adTabbox.getSelectedGridTab().dataRefreshAll(true, true);
		    		adTabbox.getSelectedGridTab().refreshParentTabs();
		    		if (!success)
		    			showLastWarning();

		            adTabbox.getSelectedTabpanel().dynamicDisplay(0);
		            focusToActivePanel();
		            MRecentItem.publishChangedEvent(Env.getAD_User_ID(ctx));		            
				}
				if (postCallback != null)
					postCallback.onCallback(result);
	        }
		});
    }

    // Elaine 2008/12/01
    private void onDeleteSelected(final Callback<Boolean> postCallback)
	{
    	if (adTabbox.getSelectedGridTab().isReadOnly() || !adTabbox.getSelectedTabpanel().isGridView())
        {
    		if (postCallback != null)
    			postCallback.onCallback(false);
            return;
        }

		final int[] indices = adTabbox.getSelectedGridTab().getSelection();
		if(indices.length > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(Env.getContext(ctx, curWindowNo, "_WinInfo_WindowName", false)).append(" - ")
				.append(indices.length).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected"));
			FDialog.ask(sb.toString(), curWindowNo, null, Msg.getMsg(Env.getCtx(), "DeleteSelection"), new Callback<Boolean>() {
				@Override
				public void onCallback(Boolean result) {
					if(result){
						adTabbox.getSelectedGridTab().clearSelection();						
						Arrays.sort(indices);
						int offset = 0;
						int count = 0;
						for (int i = 0; i < indices.length; i++)
						{
							adTabbox.getSelectedGridTab().navigate(indices[i]-offset);
							if (adTabbox.getSelectedGridTab().dataDelete())
							{
								offset++;
								count++;
							}
						}
			            adTabbox.getSelectedGridTab().dataRefreshAll(true, true);
			    		adTabbox.getSelectedGridTab().refreshParentTabs();
						
						adTabbox.getSelectedTabpanel().dynamicDisplay(0);
						if (getActiveGridTab().isQuickForm)
						{
							statusBarQF.setStatusLine(Msg.getMsg(Env.getCtx(), "Deleted") + ": " + count, false);
						}
						else
						{
							statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "Deleted") + ": " + count, false);
						}
					}
					if (postCallback != null)
						postCallback.onCallback(result);
				}
			});
		} else {
			if (getActiveGridTab().isQuickForm)
			{
				statusBarQF.setStatusLine(Msg.getMsg(Env.getCtx(), "Selected") + ": 0", false);
			}
			else
			{
				statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "Selected") + ": 0", false);
			}
			if (postCallback != null)
				postCallback.onCallback(false);
		}
	}
	//

    @Override
    public void onPrint() {
    	final Callback<Boolean> postCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.AFTER_PRINT.getName());
			    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, null);
				}
			}
		};
    	Callback<Boolean> preCallback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					onPrintCallback(postCallback);
				}
			}
		};
		
		WindowValidatorEvent event = new WindowValidatorEvent(adwindow, WindowValidatorEventType.BEFORE_PRINT.getName());
    	WindowValidatorManager.getInstance().fireWindowValidatorEvent(event, preCallback);
    }
    
	private void onPrintCallback(final Callback<Boolean> postCallback) {
		//Get process defined for this tab
		final int AD_Process_ID = adTabbox.getSelectedGridTab().getAD_Process_ID();
		//log.info("ID=" + AD_Process_ID);

		//	No report defined
		if (AD_Process_ID == 0)
		{
			onReport();

			return;
		}

		Callback<Boolean> callback = new Callback<Boolean>() {
			@Override
			public void onCallback(Boolean result) {
				if (result) {
					int table_ID = adTabbox.getSelectedGridTab().getAD_Table_ID();
					int record_ID = adTabbox.getSelectedGridTab().getRecord_ID();

					final ProcessModalDialog dialog = new ProcessModalDialog(AbstractADWindowContent.this, getWindowNo(), AD_Process_ID,table_ID, record_ID, true);
					if (dialog.isValid()) {
						//dialog.setWidth("500px");
						dialog.setBorder("normal");						
						getComponent().getParent().appendChild(dialog);
						showBusyMask(dialog);
						LayoutUtils.openOverlappedWindow(getComponent(), dialog, "middle_center");
						if (postCallback != null) {
							dialog.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
								@Override
								public void onEvent(Event event) throws Exception {
									postCallback.onCallback(!dialog.isCancel());
								}
							});
						}
						dialog.focus();
					} else if (postCallback != null) {
						postCallback.onCallback(result);
					}
				} else if (postCallback != null) {
					postCallback.onCallback(result);
				}
			}
		};
		onSave(false, false, callback);
	}

	/**
     * @see ToolbarListener#onReport()
     */
	public void onReport() {
		

		Callback<Boolean> callback = new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean result) {
				if (result) {
					onReport0();
				}
			}
		};
		onSave(false, false, callback);
	}

	private void onReport0() {
		
	}

	/**
     * @see ToolbarListener#onZoomAcross()
     */
	public void onZoomAcross() {
		if (toolbar.getEvent() != null)
		{
			int record_ID = adTabbox.getSelectedGridTab().getRecord_ID();
			if (record_ID <= 0)
				return;

			//	Query
			MQuery query = new MQuery();
			//	Current row
			String link = adTabbox.getSelectedGridTab().getKeyColumnName();
			//	Link for detail records
			if (link.length() == 0)
				link = adTabbox.getSelectedGridTab().getLinkColumnName();
			if (link.length() != 0)
			{
				if (link.endsWith("_ID"))
					query.addRestriction(link, MQuery.EQUAL,
						Integer.valueOf(Env.getContextAsInt(ctx, curWindowNo, link)));
				else
					query.addRestriction(link, MQuery.EQUAL,
						Env.getContext(ctx, curWindowNo, link));
			}
			new WZoomAcross(toolbar.getToolbarItem("ZoomAcross"), adTabbox.getSelectedGridTab()
					.getTableName(), adTabbox.getSelectedGridTab().getAD_Window_ID(), query);
		}
	}


	// Elaine 2008/07/22
	/**
     * @see ToolbarListener#onRequests()
     */
	public void onRequests()
	{
		if (toolbar.getEvent() != null)
		{
			if (adTabbox.getSelectedGridTab().getRecord_ID() <= 0)
				return;

			int C_BPartner_ID = 0;
			Object bpartner = adTabbox.getSelectedGridTab().getValue("C_BPartner_ID");
			if(bpartner != null)
				C_BPartner_ID = Integer.valueOf(bpartner.toString());

			new WRequest(toolbar.getToolbarItem("Requests"), adTabbox.getSelectedGridTab().getAD_Table_ID(), adTabbox.getSelectedGridTab().getRecord_ID(), C_BPartner_ID);
		}
	}
	//

	// Elaine 2008/07/22
	/**
     * @see ToolbarListener#onProductInfo()
     */
	public void onProductInfo()
	{
		InfoPanel.showPanel(I_M_Product.Table_Name);
	}
	//


	//

	@Override
	public void onExport() {
		int AD_Table_ID=getActiveGridTab().getAD_Table_ID();
		final boolean isCanExport=MRole.getDefault().isCanExport(AD_Table_ID);
		if (!isCanExport) {
			FDialog.error(curWindowNo, parent, "AccessCannotExport");
			return;
		} else {
			ExportAction action = new ExportAction(this);
			action.export();
		}
	}

	@Override
	public void onFileImport() {
		FileImportAction action = new FileImportAction(this);
		action.fileImport();
	}

	@Override
	public void onCSVImport() {
		
	}
	
	@Override
	public void onSearchQuery() {
		if (adTabbox.getSelectedGridTab() == null)
            return;

        clearTitleRelatedContext();

		// The record was not changed locally
        if (adTabbox.getDirtyADTabpanel() == null) {
        	doOnQueryChange();
        } else {
            onSave(false, false, new Callback<Boolean>() {
    			@Override
    			public void onCallback(Boolean result) {
    				if (result) {
    					doOnQueryChange();
    				}
    			}
    		});        	
        }
	}
	
	/**
	 * Simulate opening the Find Window, selecting a user query and click ok
	 */
	public void doOnQueryChange() {
		//  Gets Fields from AD_Field_v
		GridField[] findFields = adTabbox.getSelectedGridTab().getFields();
		if (findWindow == null || !findWindow.validate(adTabbox.getSelectedGridTab().getWindowNo(), adTabbox.getSelectedGridTab().getName(),
				adTabbox.getSelectedGridTab().getAD_Table_ID(), adTabbox.getSelectedGridTab().getTableName(),
				adTabbox.getSelectedGridTab().getWhereExtended(), findFields, 1, adTabbox.getSelectedGridTab().getAD_Tab_ID())) {
			findWindow = new FindWindow (adTabbox.getSelectedGridTab().getWindowNo(), adTabbox.getSelectedGridTab().getName(),
					adTabbox.getSelectedGridTab().getAD_Table_ID(), adTabbox.getSelectedGridTab().getTableName(),
					adTabbox.getSelectedGridTab().getWhereExtended(), findFields, 1, adTabbox.getSelectedGridTab().getAD_Tab_ID());

			setupEmbeddedFindwindow();	        
			if (!findWindow.initialize()) {
				if (findWindow.getTotalRecords() == 0) {
					FDialog.info(curWindowNo, getComponent(), "NoRecordsFound");
				}
				return;
			}
		}

		//findWindow.setAD_UserQuery_ID(toolbar.getAD_UserQuery_ID());
		//findWindow.advancedOkClick();
		MQuery query = findWindow.getQuery();

		//  Confirmed query
		if (query != null) {
			m_onlyCurrentRows = false;
			adTabbox.getSelectedGridTab().setQuery(query);
			adTabbox.getSelectedTabpanel().query(m_onlyCurrentRows, m_onlyCurrentDays, MRole.getDefault().getMaxQueryRecords());   //  autoSize
		}

		adTabbox.getSelectedGridTab().dataRefresh(false);

		focusToActivePanel();
		findWindow.dispose();
	}

	/**************************************************************************
	 *	Start Button Process
	 *  @param vButton button
	 */
	private void actionButton (final IProcessButton wButton)
	{
		if (adTabbox.getSelectedGridTab().hasChangedCurrentTabAndParents()) {
			String msg = CLogger.retrieveErrorString("Please ReQuery Window");
			FDialog.error(curWindowNo, parent, null, msg);
			return;
		}

		if (logger.isLoggable(Level.INFO)) logger.info(wButton.toString());

		final String col = wButton.getColumnName();

		//  Zoom
		if (col.equals("Record_ID"))
		{
			int AD_Table_ID = -1;
			int Record_ID = -1;

			if (wButton instanceof WButtonEditor) {
				int curTabNo = 0;
				WButtonEditor be = (WButtonEditor)wButton;
				if (be.getGridField() != null && be.getGridField().getGridTab() != null) {
					curTabNo = ((WButtonEditor)wButton).getGridField().getGridTab().getTabNo();
					AD_Table_ID = Env.getContextAsInt (ctx, curWindowNo, curTabNo, "AD_Table_ID");
					Record_ID = Env.getContextAsInt (ctx, curWindowNo, curTabNo, "Record_ID");
				}
			}
			if (AD_Table_ID < 0)
				AD_Table_ID = Env.getContextAsInt (ctx, curWindowNo, "AD_Table_ID");
			if (Record_ID < 0)
				Record_ID = Env.getContextAsInt (ctx, curWindowNo, "Record_ID");

			AEnv.zoom(AD_Table_ID, Record_ID);
			return;
		} // Zoom

		//  save first	---------------

		if (adTabbox.needSave(true, false))
		{
			onSave(false, false, new Callback<Boolean>() {
				@Override
				public void onCallback(Boolean result) {
					if (result) {
						actionButton0(col, wButton);
					}
				}
			});
		}
		else
		{
			actionButton0(col, wButton);
		}
	}

	/**************************************************************************
	 *	Start Button Process
	 * @param col
	 * @param wButton
	 */
	private void actionButton0 (String col, final IProcessButton wButton)
	{
		//To perform button action (adtabPanel is null in QuickForm)  
		IADTabpanel adtabPanel = null;
		if (adTabbox.getSelectedGridTab().isQuickForm())
		{
			adtabPanel=this.getADTab().getSelectedTabpanel();
		}
		else
		{
			adtabPanel = findADTabpanel(wButton);
		}
		boolean startWOasking = false;
		if (adtabPanel == null) {
			return;
		}
		final int table_ID = adtabPanel.getGridTab().getAD_Table_ID();


		int record_ID = adtabPanel.getGridTab().getRecord_ID();


		if (record_ID == -1 && adtabPanel.getGridTab().getKeyColumnName().equals("AD_Language"))
			record_ID = Env.getContextAsInt (ctx, curWindowNo, "AD_Language_ID");


		if (record_ID == -1
			&& (wButton.getProcess_ID() == PROCESS_AD_CHANGELOG_UNDO || wButton.getProcess_ID() == PROCESS_AD_CHANGELOG_REDO))
		{
			Integer id = (Integer)adtabPanel.getGridTab().getValue("AD_ChangeLog_ID");
			record_ID = id.intValue();
		}

		//	Ensure it's saved

		if (record_ID == -1 && adtabPanel.getGridTab().getKeyColumnName().endsWith("_ID"))
		{
			FDialog.error(curWindowNo, parent, "SaveErrorRowNotFound");
			return;
		}

		boolean isProcessMandatory = false;
		
		executeButtonProcess(wButton, startWOasking, table_ID, record_ID, isProcessMandatory);
	} // actionButton

	private Div getMask() {
		if (mask == null) {
			mask = new Mask();
		}
		return mask;
	}
	
	public void hideBusyMask() {
		if (mask != null && mask.getParent() != null) {
			mask.detach();
			StringBuilder script = new StringBuilder("var w=zk.Widget.$('#");
			script.append(getComponent().getParent().getUuid()).append("');if(w) w.busy=false;");
			Clients.response(new AuScript(script.toString()));
		}
	}
	
	public void showBusyMask(Window window) {
		getComponent().getParent().appendChild(getMask());
		StringBuilder script = new StringBuilder("var w=zk.Widget.$('#");
		script.append(getComponent().getParent().getUuid()).append("');");
		if (window != null) {
			script.append("var d=zk.Widget.$('#").append(window.getUuid()).append("');w.busy=d;");
		} else {
			script.append("w.busy=true;");
		}
		Clients.response(new AuScript(script.toString()));
	}

	public void executeButtonProcess(final IProcessButton wButton,
			final boolean startWOasking, final int table_ID, final int record_ID,
			boolean isProcessMandatory) {
		/**
		 *  Start Process ----
		 */

		if (logger.isLoggable(Level.CONFIG)) logger.config("Process_ID=" + wButton.getProcess_ID() + ", Record_ID=" + record_ID);

		if (wButton.getProcess_ID() == 0)
		{
			if (isProcessMandatory)
			{
				FDialog.error(curWindowNo, null, null, Msg.parseTranslation(ctx, "@NotFound@ @AD_Process_ID@"));
			}
			return;
		}

		//	Save item changed

		if (adTabbox.needSave(true, false))
		{
			onSave(false, false, new Callback<Boolean>() {

				@Override
				public void onCallback(Boolean result) {
					if (result) {
						executeButtonProcess0(wButton, startWOasking, table_ID, record_ID);
					}
				}
			});
		}
		else
		{
			executeButtonProcess0(wButton, startWOasking, table_ID, record_ID);
		}
	}

	private void executeButtonProcess0(final IProcessButton wButton,
			boolean startWOasking, int table_ID, int record_ID) {

		IADTabpanel adtabPanel = null;
		if (adTabbox.getSelectedGridTab().isQuickForm())
			adtabPanel = this.getADTab().getSelectedTabpanel();
		else
			adtabPanel = findADTabpanel(wButton);

		ProcessInfo pi = new ProcessInfo("", wButton.getProcess_ID(), table_ID, record_ID);
		if (adtabPanel != null && adtabPanel.isGridView() && adtabPanel.getGridTab() != null)
		{
			int[] indices = adtabPanel.getGridTab().getSelection();
			if (indices.length > 0)
			{
				List<Integer> records = new ArrayList<Integer>();
				for (int i = 0; i < indices.length; i++)
				{
					int keyID = adtabPanel.getGridTab().getKeyID(indices[i]);
					if (keyID > 0)
						records.add(keyID);
				}

				// IDEMPIERE-3998 Set multiple selected grid records into process info
				pi.setRecord_IDs(records);
			}
		}

		ProcessModalDialog dialog = new ProcessModalDialog(this, curWindowNo, pi, startWOasking);

		if (dialog.isValid())
		{
			//dialog.setWidth("500px");
			dialog.setBorder("normal");				
			getComponent().getParent().appendChild(dialog);
			if (ClientInfo.isMobile())
			{
				dialog.doHighlighted();
			}
			else
			{
				showBusyMask(dialog);
				LayoutUtils.openOverlappedWindow(getComponent(), dialog, "middle_center");
			}
			dialog.focus();
		}
		if (adTabbox.getSelectedGridTab().isQuickForm()) {
			adTabbox.getSelectedGridTab().dataRefreshAll(false, false);
		}
		else
		{
			onRefresh(true, false);
		}
	
	}

	/**
	 * @param event
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(final ActionEvent event)
	{
		Runnable runnable = new Runnable() {
			public void run() {
				String error = processButtonCallout((IProcessButton) event.getSource());
				if (error != null && error.trim().length() > 0)
				{
					if (getActiveGridTab().isQuickForm)
					{
						statusBarQF.setStatusLine(error, true);
					}
					else
					{
						statusBar.setStatusLine(error, true);
					}
					return;
				}
				actionButton((IProcessButton) event.getSource());
			}
		};
		BusyDialogTemplate template = new BusyDialogTemplate(runnable);
		template.run();
	}

	/**************************************************************************
	 *  Process Callout(s).
	 *  <p>
	 *  The Callout is in the string of
	 *  "class.method;class.method;"
	 * If there is no class name, i.e. only a method name, the class is regarded
	 * as CalloutSystem.
	 * The class needs to comply with the Interface Callout.
	 *
	 * @param field field
	 * @return error message or ""
	 * @see eone.base.model.Callout
	 */
	private String processButtonCallout (IProcessButton button)
	{
		IADTabpanel adtab = null;
		if (adTabbox.getSelectedGridTab().isQuickForm())
		{
			adtab=this.getADTab().getSelectedTabpanel();
		}
		else
		{
			adtab = findADTabpanel(button);
		}
		if (adtab != null) {
			GridField field = adtab.getGridTab().getField(button.getColumnName());
			if (field != null)
				return adtab.getGridTab().processCallout(field);
			else
				return "";
		} else {
			return "";
		}
	}	//	processButtonCallout

	public IADTabpanel findADTabpanel(IProcessButton button) {
		IADTabpanel adtab = null;
		if (button.getADTabpanel() != null)
			return button.getADTabpanel();

		Component c = button instanceof WEditor ? ((WEditor)button).getComponent() : (Component)button;
		while (c != null) {
			if (c instanceof IADTabpanel) {
				adtab = (IADTabpanel) c;
				break;
			}
			c = c.getParent();
		}
		return adtab;
	}

	/**
	 *
	 * @return IADTab
	 */
	public IADTabbox getADTab() {
		return adTabbox;
	}

	/**
	 * @param pi
	 */
	public void executeASync(ProcessInfo pi) {
	}

	/**
	 * @param pi
	 */
	private void onModalClose(ProcessInfo pi) {
		boolean notPrint = pi != null
		&& pi.getAD_Process_ID() != adTabbox.getSelectedGridTab().getAD_Process_ID()
		&& pi.isReportingProcess() == false;
		//
		//  Process Result

		if (Executions.getCurrent() != null)
		{
			if (notPrint || pi.isError()) // show process info if it is not print or have error
			{
				updateUI(pi);
			}
		}
		else
		{
			try {
				//acquire desktop, 2 second timeout
				Executions.activate(getComponent().getDesktop(), 2000);
				try {
					if (notPrint || pi.isError()) // show process info if it is not print or have error
					{
						updateUI(pi);
					}
                } catch(Error ex){
                	throw ex;
                } finally{
                	//release full control of desktop
                	Executions.deactivate(getComponent().getDesktop());
                }
			} catch (Exception e) {
				logger.log(Level.WARNING, "Failed to update UI upon unlock.", e);
			}
		}
	}

	private void updateUI(ProcessInfo pi) {						
		//	Timeout
		if (pi.isTimeout())		//	set temporarily to R/O
			Env.setContext(ctx, curWindowNo, "Processed", "Y");
		//	Update Status Line
		String summary = pi.getSummary();
		if (summary != null && summary.indexOf('@') != -1)
			pi.setSummary(Msg.parseTranslation(Env.getCtx(), summary));

		//		Get Log Info
		ProcessInfoLog m_logs[] = pi.getLogs();
		if (getActiveGridTab().isQuickForm)
		{
			statusBarQF.setStatusLine(pi.getSummary(), pi.isError(), m_logs);
		}
		else
		{
			statusBar.setStatusLine(pi.getSummary(), pi.isError(), m_logs);
		}
		
		
		if (m_logs != null && m_logs.length > 0) {
			ProcessInfoDialog.showProcessInfo(pi, curWindowNo, getComponent(), false);
		}
		
	}

	/**
	 *
	 * @return toolbar instance
	 */
	public ADWindowToolbar getToolbar() {
		return toolbar;
	}

	/**
	 * @return active grid tab
	 */
	public GridTab getActiveGridTab() {
		return adTabbox.getSelectedGridTab();
	}

	/**
	 * @return windowNo
	 */
	public int getWindowNo() {
		return curWindowNo;
	}

	/**
	 * show dialog to customize fields (hidden, display, order of field) in grid mode 
	 * @see CustomizeGridViewDialog
     * @see ToolbarListener#onCustomize()
     */
	public void onCustomize() {
		ADTabpanel tabPanel = (ADTabpanel) getADTab().getSelectedTabpanel();
		CustomizeGridViewDialog.onCustomize(tabPanel);	}

	/**
	 * @see eone.webui.event.ToolbarListener#onProcess()
	 */
	@Override
	public void onProcess() {
		ProcessButtonPopup popup = new ProcessButtonPopup();
		popup.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "processButtonPopup");
		ADTabpanel adtab = (ADTabpanel) adTabbox.getSelectedTabpanel();
		popup.render(adtab.getToolbarButtons());
		if (popup.getChildren().size() > 0) {
			popup.setPage(this.getComponent().getPage());
			popup.open(getToolbar().getToolbarItem("Process"), "after_start");
		}
	}

	@Override
	public void onSelect() {
		if (findWindow != null && findWindow.getPage() != null && findWindow.isVisible() && m_queryInitiating) {
			LayoutUtils.openEmbeddedWindow(getComponent().getParent(), findWindow, "overlap");
		}
	}

	public boolean isPendingChanges() {
		return boolChanges;
	}

	public void setADWindow(ADWindow adwindow) {
		this.adwindow = adwindow;
	}
	
	public ADWindow getADWindow() {
		return adwindow;
	}
	
	private void clearTitleRelatedContext() {
		// IDEMPIERE-1328
		// clear the values for the tab header
        String titleLogic = null;
        int windowID = getADTab().getSelectedGridTab().getAD_Window_ID();
        if (windowID > 0) {
        	titleLogic = MWindow.get(Env.getCtx(), windowID).getTitleLogic();
        }
        if (titleLogic != null) {
    		String token;
    		String inStr = new String(titleLogic);

    		int i = inStr.indexOf('@');
    		while (i != -1)
    		{
    			inStr = inStr.substring(i+1, inStr.length());	// from first @

    			int j = inStr.indexOf('@');						// next @
    			if (j < 0)
    			{
    				logger.log(Level.SEVERE, "No second tag: " + inStr);
    				return;						//	no second tag
    			}

    			token = inStr.substring(0, j);
        		Env.setContext(ctx, curWindowNo, token, "");

    			inStr = inStr.substring(j+1, inStr.length());	// from second @
    			i = inStr.indexOf('@');
    		}
        } else {
    		Env.setContext(ctx, curWindowNo, "DocumentNo", "");
    		Env.setContext(ctx, curWindowNo, "Value", "");
    		Env.setContext(ctx, curWindowNo, "Name", "");
        }
	}
	
	/**
	 * @return Quick Form StatusBar
	 */
	public StatusBar getStatusBarQF()
	{
		return statusBarQF;
	}
	
	/**
	 * Implementation to work key listener for the current open Quick Form.
	 */
	QuickGridView currQGV = null;

	/**
	 * @return
	 */
	public QuickGridView getCurrQGV()
	{
		return currQGV;
	}

	/**
	 * @param currQGV
	 */
	public void setCurrQGV(QuickGridView currQGV)
	{
		this.currQGV = currQGV;
	}

	/**
	 * Close Quick form to remove tabID from the list
	 * 
	 * @param AD_Tab_ID
	 */
	public void closeQuickFormTab(Integer AD_Tab_ID)
	{
		quickFormOpenTabs.remove(AD_Tab_ID);
	} // closeQuickFormTab

	/**
	 * Get list of open quick form tabs
	 * 
	 * @return list of tabIDs
	 */
	public ArrayList <Integer> getOpenQuickFormTabs( )
	{
		return quickFormOpenTabs;
	} // getOpenQuickFormTabs

	/**
	 * Register Quick form against tabID
	 * 
	 * @param AD_Tab_ID
	 * @return False when already quick form opens for same tab
	 */
	public boolean registerQuickFormTab(Integer AD_Tab_ID)
	{
		if (quickFormOpenTabs.contains(AD_Tab_ID))
		{
			return false;
		}

		quickFormOpenTabs.add(AD_Tab_ID);

		return true;
	} // registerQuickFormTab
	
	//Quynhnv.x8: Add
	
	private List<PO> getListPO() {
		List<PO> arr = new ArrayList<PO>();
		final int[] indices = adTabbox.getSelectedGridTab().getRecordSelection();
		GridTab curTab = adTabbox.getSelectedGridTab();      
		MTable table = MTable.get(Env.getCtx(), curTab.getAD_Table_ID());
		PO po = null;
        if (indices.length > 0) {
        	for(int i = 0; i < indices.length; i++) {
        		po = table.getPO(indices[i], null);
        		arr.add(po);
        	}
        	    		
        } else {
    		po = table.getPO(curTab.getRecord_ID(), null);
    		arr.add(po);
        }
        return arr;
	}
	
	private GridTab             curTab;
	
	private void dataRefreshAll() {
    	curTab.dataRefreshAll();
    	focusToActivePanel();
    }
	
	private void dataRefresh()
    {
        curTab.dataRefresh(true);
        focusToActivePanel();
    }
	
	public void onComplete()
    {
    	//GridTab curTab = adTabbox.getSelectedGridTab();        
        final GridField docActionField = curTab.getField("DocStatus");
        if (docActionField == null)
            return;
        int AD_Window_ID = gridWindow.getAD_Window_ID();
        if(AD_Window_ID > 0){
        	Env.setContext(Env.getCtx(), "Period_AD_Window_ID", AD_Window_ID);        	
        }

        int record_ID = curTab.getRecord_ID();

        if (record_ID == -1 && curTab.getKeyColumnName().equals("AD_Language"))
            record_ID = Env.getContextAsInt (ctx, curWindowNo, "AD_Language_ID");

        //  Record_ID - Change Log ID

        if (record_ID == -1
            && (docActionField.getAD_Process_ID() == PROCESS_AD_CHANGELOG_UNDO
                || docActionField.getAD_Process_ID() == PROCESS_AD_CHANGELOG_REDO))
        {
            Integer id = (Integer) curTab.getValue("AD_ChangeLog_ID");
            record_ID = id.intValue();
        }

        //  Ensure it's saved
        if (record_ID == -1 && curTab.getKeyColumnName().endsWith("_ID"))
        {
            FDialog.error(curWindowNo, parent, "SaveErrorRowNotFound");
            return;
        }
        /*
        MTable table = MTable.get(Env.getCtx(), curTab.getAD_Table_ID());
		PO po = table.getPO(curTab.getRecord_ID(), null);
		if (po == null) {
			FDialog.error(curWindowNo, parent, "Cannot get po");
            return;
		}
		*/	
		boolean success = false;
		String processMsg = null;
		DocAction doc = null;
		String m_docStatus = null;
		List<PO> pos = getListPO();
		for (PO po : pos) {
			if (po == null) {
				FDialog.error(curWindowNo, parent, "Cannot get po");
	            return;
			}
			if (po instanceof DocAction)
			{
				doc = (DocAction)po;
				//
				try {
					success = doc.processIt (DocAction.ACTION_Complete, AD_Window_ID);	//	** Do the work
					processMsg = doc.getProcessMsg();
					m_docStatus = doc.getDocStatus();
					if(m_docStatus == null || !m_docStatus.equals(DocAction.STATUS_Completed)) {
						processMsg = doc.getProcessMsg();
						FDialog.error(curWindowNo, parent, processMsg);
						return;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (processMsg != null && !processMsg.equals(""))
					FDialog.error(curWindowNo, parent, processMsg);
			}
			else
				throw new IllegalStateException("Persistent Object not DocAction - "
					+ po.getClass().getName()
					+ " - AD_Table_ID=" + curTab.getAD_Table_ID() + ", Record_ID=" + curTab.getRecord_ID());
			//
			if (!po.save())
			{
				success = false;
				processMsg = "SaveError";
			}
			if (!success)
			{
				if (processMsg == null || processMsg.length() == 0)
				{
					if (doc != null)	//	problem: status will be rolled back
						processMsg += " - DocStatus=" + doc.getDocStatus();
				}
			}
		}
		
		dataRefreshAll();

    }
	
	public void onReActivate() {
        String COLUMNNAME_Processed = "Processed";
        
        boolean isProcessed = false;
        
        final GridTab curTab = adTabbox.getSelectedGridTab();
        final String m_TableName = curTab.getTableName();
        int m_Table_ID = curTab.getAD_Table_ID();
        
        if (m_TableName == null || m_Table_ID < 1) {
            FDialog.error(curWindowNo, this.getComponent(), "TableInvalid");
            return;
        }
        
        int AD_Window_ID = curTab.getAD_Window_ID();
        if(AD_Window_ID > 0){
        	Env.setContext(Env.getCtx(), "Period_AD_Window_ID", AD_Window_ID);        	
        }
        //
        
        Object m_Doc_ID = curTab.getValue(m_TableName + "_ID");
        if (m_Doc_ID == null) {
            FDialog.error(curWindowNo, this.getComponent(), "DocNotFound");
            return;
        }
        
        int Record_ID = (Integer)m_Doc_ID;
        
        if (Record_ID < 1) {
            FDialog.error(curWindowNo, this.getComponent(), "RecordNotFound");
            return;
        }
        
        MTable m_Table = MTable.get(Env.getCtx(), m_Table_ID);
        if (m_Table == null) {
            FDialog.error(curWindowNo, this.getComponent(), "TableNotFound");
            return;
        }
        
        List<PO> pos = getListPO();
        int i = 0;
        for(PO poi : pos) {
        	final PO po = poi;
            if (po == null) {
                FDialog.error(curWindowNo, this.getComponent(), "RecordNotFound");
                return;
            }
            
            int index = po.get_ColumnIndex(COLUMNNAME_Processed);
            if (index < 0) {
                FDialog.error(curWindowNo, this.getComponent(), "InvalidDoc");
            }
            
            Object value = po.get_Value(index);
            if (value != null)
            {
                 if (value instanceof Boolean) 
                     isProcessed = ((Boolean)value).booleanValue(); 
                 else
                     isProcessed = Env.YES.equals(value);
            }
            if (isProcessed || !isProcessed && po.getCreatedBy() == Env.getAD_User_ID(Env.getCtx()))
            {
                if (po instanceof DocAction)
                {
                    final DocAction doc = (DocAction)po;
                    
                    if (i == 0)
	                    FDialog.ask(curWindowNo, this.getComponent(), Msg.getMsg(Env.getCtx(), "ReActivateRecord"), new Callback<Boolean>() {
	                        
	                        @Override
	                        public void onCallback(Boolean result) {
	                            if (result) {
	                                String m_processMsg = null;
	                                boolean isComplete = false;
	                                String trxName = Trx.createTrxName("ReActivate");
	                                Trx trx = Trx.get(trxName, true);
	                                try {
	                                    po.set_TrxName(trxName);
	                                    isComplete = DocumentEngine.processIt(doc, DocumentEngine.ACTION_ReActivate);
	                                  
	                                    if (!isComplete)
	                                        m_processMsg = doc.getProcessMsg();
	                                    else
	                                        po.save(trx.getTrxName());
	                                    if (trx != null) {
	                                        try {
	                                            if (isComplete)
	                                                trx.commit();
	                                            else
	                                                trx.rollback();
	                                        } catch (Exception e) { } finally {
	                                            trx.close();
	                                        }
	                                    }
	                                } catch (Exception e) {
	                                    try {
	                                        trx.rollback();
	                                    } catch (Exception ex) { }
	                                    try {
	                                        trx.close();
	                                    } catch (Exception ex) { }
	                                    m_processMsg = e.getMessage();
	                                }
	                                if (!isComplete) {
	                                    if (m_processMsg != null)
	                                        FDialog.error(curWindowNo, null, "ProcessRunError", m_processMsg);
	                                    else
	                                        FDialog.error(curWindowNo, "CannotReActivate", "Reference Document is Processed");
	                                }
	                                
	                                dataRefresh();
	                               
	                            }
	                        }
	                    });
                    else
                    {
                    	String m_processMsg = null;
                        boolean isComplete = false;
                        String trxName = Trx.createTrxName("ReActivate");
                        Trx trx = Trx.get(trxName, true);
                        try {
                            po.set_TrxName(trxName);
                            isComplete = DocumentEngine.processIt(doc, DocumentEngine.ACTION_ReActivate);
                          
                            if (!isComplete)
                                m_processMsg = doc.getProcessMsg();
                            else
                                po.save(trx.getTrxName());
                            if (trx != null) {
                                try {
                                    if (isComplete)
                                        trx.commit();
                                    else
                                        trx.rollback();
                                } catch (Exception e) { } finally {
                                    trx.close();
                                }
                            }
                        } catch (Exception e) {
                            try {
                                trx.rollback();
                            } catch (Exception ex) { }
                            try {
                                trx.close();
                            } catch (Exception ex) { }
                            m_processMsg = e.getMessage();
                        }
                        if (!isComplete) {
                            if (m_processMsg != null)
                                FDialog.error(curWindowNo, null, "ProcessRunError", m_processMsg);
                            else
                                FDialog.error(curWindowNo, "CannotReActivate", "Reference Document is Processed");
                        }
                    }
                    // 
                }
            }
            //dataRefreshAll();
            i++;
        }
    }
	public void onPosted() {
    	int tableId = Env.getContextAsInt(ctx, curWindowNo, "AD_Table_ID", true);
        int recordId = Env.getContextAsInt(ctx, curWindowNo, "Record_ID", true);
        if ( tableId == 0 || recordId == 0 ) {
            tableId = curTab.getAD_Table_ID();
            recordId = curTab.getRecord_ID();
        }

        Object postStatus = curTab.getValue("Posted");
        if (postStatus != null && (postStatus.equals("Y") || postStatus.equals("D"))) {
            new eone.webui.acct.WAcctViewer(Env.getContextAsInt (ctx, curWindowNo, "AD_Client_ID"), tableId, recordId);
            return;
        }
    }
	
	public void validateAPCA() {
		     
		GridTab currentTab = adTabbox.getSelectedGridTab();
        GridField fieldStatus = currentTab.getField("DocStatus");
        GridField fieldApproved = currentTab.getField("Approved");
        GridField fieldCanceled = currentTab.getField("Canceled");
        
        if (fieldApproved != null && fieldApproved.getValue() != null) {
        	boolean hasDocStatus = fieldStatus != null;
        	boolean isComplete = hasDocStatus && !fieldStatus.getValue().equals(DocAction.STATUS_Drafted);
        	boolean isApproved = true;
        	
        	if (fieldApproved != null) {
            	isApproved = fieldApproved.isDisplayed(true);
            }
        	
        	toolbar.enableComplete(hasDocStatus && !isComplete && isApproved);
			toolbar.setVisible("Complete", hasDocStatus && !isComplete && isApproved);
    		
    		if (curTab.getRecord_ID() <= 0) {
    			toolbar.enableComplete(false);
    			toolbar.setVisible("Complete", false);    			
    		}
        } else {
        	toolbar.setVisible("Complete", false);
			toolbar.enableComplete(false);
        	
        }//End
        
        //Phan Quyen Cancel
        if (fieldApproved != null && fieldApproved.getValue() != null) {
        	boolean hasDocStatus = fieldStatus != null;
        	boolean isComplete = hasDocStatus && !fieldStatus.getValue().equals(DocAction.STATUS_Drafted);
        	boolean isCanceled = true;
        	
        	if (fieldCanceled != null) {
        		isCanceled = fieldCanceled.isDisplayed(true);
            }
        	
    		toolbar.enableReActivate(hasDocStatus && isComplete && isCanceled);
    		toolbar.setVisible("ReActivate", hasDocStatus && isComplete && isCanceled);    		
			
    		if (curTab.getRecord_ID() <= 0) {
    			toolbar.enableReActivate(false);
    			toolbar.setVisible("ReActivate", false);
    		}
        } else {
			toolbar.enableReActivate(false);
			toolbar.setVisible("ReActivate", false);
        	
        }
        //Co du lieu hach toan thi ai cung duoc xem, neu co quyen
        
        if (fieldApproved != null && fieldApproved.getValue() != null) {
        	boolean isComplete = fieldStatus.getValue().equals(DocAction.STATUS_Completed);
    		
    		boolean hasPosted = isComplete && MRole.getDefault().isShowAcct();
    		boolean isPosted = false;
    		if (hasPosted) {
    			GridField field = curTab.getField("Posted"); //Kiem tra xem co nut Posted hay khong
    			if(field != null)
    			{
    				isPosted = true;
    			}
    		}
    		
    		toolbar.enablePost(isPosted && isComplete);
    		toolbar.setVisible("Posted", isPosted && isComplete);
    		
    		if (curTab.getRecord_ID() <= 0) {
    			toolbar.enablePost(false);
    			toolbar.setVisible("Posted", false);
    		}
        }else {
        	toolbar.enablePost(false);
        	toolbar.setVisible("Posted", false);
        }
	}
}
