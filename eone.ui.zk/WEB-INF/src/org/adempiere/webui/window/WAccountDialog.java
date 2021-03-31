/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.adempiere.webui.window;

import static org.compiere.model.SystemIDs.WINDOW_ACCOUNTCOMBINATION;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.webui.ClientInfo;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.adwindow.ADTabpanel;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.ToolBar;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.panel.StatusBarPanel;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ITheme;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.DataStatusEvent;
import org.compiere.model.DataStatusListener;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.model.GridWindowVO;
import org.compiere.model.MAccount;
import org.compiere.model.MAccountLookup;
import org.compiere.model.MElementValue;
import org.compiere.model.MQuery;
import org.compiere.util.CLogger;
import org.compiere.util.Callback;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Vbox;

/**
 *	Dialog to enter Account Info
 *
 * 	@author Low Heng Sin
 */
public final class WAccountDialog extends Window
	implements EventListener<Event>, DataStatusListener, ValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3041802296879719489L;

	private Callback<Integer> m_callback;
	
	/**
	 * 	Constructor
	 *  @param title title
	 *  @param mAccount account info
	 *  @param C_AcctSchema_ID as
	 */
	public WAccountDialog (String title,
		MAccountLookup mAccount, int C_AcctSchema_ID, Callback<Integer> callback)
	{
		super ();
		this.setTitle(title);
		if (!ThemeManager.isUseCSSForWindowSize())
		{
			ZKUpdateUtil.setWindowHeightX(this, 500);
			ZKUpdateUtil.setWindowWidthX(this, 750);
		}
		else
		{
			addCallback(AFTER_PAGE_ATTACHED, t-> {
				ZKUpdateUtil.setCSSHeight(this);
				ZKUpdateUtil.setCSSWidth(this);
				this.invalidate();
			});
		}

		if (log.isLoggable(Level.CONFIG)) log.config("C_AcctSchema_ID=" + C_AcctSchema_ID
			+ ", C_Account_ID=" + mAccount.C_Account_ID);
		m_mAccount = mAccount;
		m_C_AcctSchema_ID = C_AcctSchema_ID;
		m_callback = callback;
		m_WindowNo = SessionManager.getAppDesktop().registerWindow(this);
		try
		{
			init();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, ex.toString());
		}
		if (initAccount())
			AEnv.showCenterScreen(this);
		else
			dispose();
	}	//	WAccountDialog

	/** Window No					*/
	private int					m_WindowNo;
	/**	Journal Entry				*
	private boolean				m_onlyNonDocControlled = false;
	/** Selection changed			*/
	protected boolean			m_changed = false;

	
	/** MWindow for AccountCombination  */
	private GridWindow             m_mWindow = null;
	/** MTab for AccountCombination     */
	private GridTab                m_mTab = null;
	/** GridController                  */
	private ADTabpanel      m_adTabPanel = null;

	/** Account used                */
	private MAccountLookup		m_mAccount = null;
	/** Result                      */
	private int 				m_C_Account_ID;
	/** Acct Schema					*/
	private int					m_C_AcctSchema_ID = 0;
	/** Client                      */
	private int                 m_AD_Client_ID;
	/** Where clause for combination search */
	private MQuery				m_query;
	/** Current combination */
	private int IDvalue = 0;
	/**	Logger			*/
	private static final CLogger log = CLogger.getCLogger(WAccountDialog.class);

	//  Editors for Query
	private WEditor 			f_AD_Org_ID, f_Account_ID;
	//
	private Label f_Description = new Label ("");

	//private int					m_line = 0;
	//private boolean				m_newRow = true;
	//
	@SuppressWarnings("unused")
	private Vbox panel = new Vbox();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private StatusBarPanel statusBar = new StatusBarPanel();
	private Hbox northPanel = new Hbox();
	private Groupbox parameterPanel = new Groupbox();
	private Grid parameterLayout = new Grid();
	private ToolBar toolBar = new ToolBar();
	private ToolBarButton bRefresh = new ToolBarButton();
	private ToolBarButton bSave = new ToolBarButton();
	private ToolBarButton bIgnore = new ToolBarButton();
	//private Row m_row;
	private Rows m_rows;

	private boolean m_smallWidth;



	/**
	 *	Static component init.
	 *  <pre>
	 *  - north
	 *    - parameterPanel
	 *    - toolBar
	 *  - center
	 *    - adtabpanel
	 *  - south
	 *    - confirmPanel
	 *    - statusBar
	 *  </pre>
	 *  @throws Exception
	 */
	void init() throws Exception
	{
		//
		Caption caption = new Caption(Msg.getMsg(Env.getCtx(),"Parameter"));
		parameterPanel.appendChild(caption);
		ZKUpdateUtil.setHflex(parameterPanel, "min");
		parameterPanel.setStyle("background-color: transparent;");
		toolBar.setOrient("vertical");
		toolBar.setStyle("border: none; padding: 5px");
		ZKUpdateUtil.setHflex(toolBar, "min");

		if (ThemeManager.isUseFontIconForImage())
			bSave.setIconSclass("z-icon-Save");
		else
			bSave.setImage(ThemeManager.getThemeResource("images/Save24.png"));
		bSave.setTooltiptext(Msg.getMsg(Env.getCtx(),"AccountNewUpdate"));
		bSave.addEventListener(Events.ON_CLICK, this);
		bSave.setSclass("img-btn");
		if (ThemeManager.isUseFontIconForImage())
			bRefresh.setIconSclass("z-icon-Refresh");
		else
			bRefresh.setImage(ITheme.ICON_REFRESH);
		bRefresh.setTooltiptext(Msg.getMsg(Env.getCtx(),"Refresh"));
		bRefresh.setSclass("img-btn");
		bRefresh.addEventListener(Events.ON_CLICK, this);
		if (ThemeManager.isUseFontIconForImage())
			bIgnore.setIconSclass("z-icon-Ignore");
		else
			bIgnore.setImage(ThemeManager.getThemeResource("images/Ignore24.png"));
		bIgnore.setTooltiptext(Msg.getMsg(Env.getCtx(),"Ignore"));
		bIgnore.setSclass("img-btn");
		bIgnore.addEventListener(Events.ON_CLICK, this);
		if (ThemeManager.isUseFontIconForImage())
		{
			LayoutUtils.addSclass("medium-toolbarbutton", bSave);
			LayoutUtils.addSclass("medium-toolbarbutton", bRefresh);
			LayoutUtils.addSclass("medium-toolbarbutton", bIgnore);
		}
		//
		toolBar.appendChild(bRefresh);
		toolBar.appendChild(bIgnore);
		toolBar.appendChild(bSave);
		//

		northPanel.appendChild(parameterPanel);
		northPanel.appendChild(toolBar);
		ZKUpdateUtil.setWidth(northPanel, "100%");

		m_adTabPanel = new ADTabpanel();

		Borderlayout layout = new Borderlayout();
		layout.setParent(this);
		ZKUpdateUtil.setHeight(layout, "100%");
		ZKUpdateUtil.setWidth(layout, "100%");
		layout.setStyle("background-color: transparent; position: relative;");

		North nRegion = new North();
		nRegion.setParent(layout);
		ZKUpdateUtil.setHflex(northPanel, "false");
		ZKUpdateUtil.setVflex(northPanel, "min");
		ZKUpdateUtil.setVflex(parameterPanel, "min");
		nRegion.appendChild(northPanel);
		nRegion.setStyle("background-color: transparent; border: none");
		northPanel.setStyle("background-color: transparent;");
		nRegion.setCollapsible(true);
		nRegion.setSplittable(true);
		nRegion.setAutoscroll(true);

		Center cRegion = new Center();
		cRegion.setParent(layout);
		ZKUpdateUtil.setHflex(m_adTabPanel, "true");
		ZKUpdateUtil.setVflex(m_adTabPanel, "true");
		cRegion.appendChild(m_adTabPanel);
		ZKUpdateUtil.setVflex(cRegion, "min");

		South sRegion = new South();
		sRegion.setParent(layout);
		Div div = new Div();
		div.appendChild(confirmPanel);
		confirmPanel.setStyle("margin-top: 5px; margin-bottom: 5px");
		div.appendChild(statusBar);
		sRegion.appendChild(div);
		sRegion.setStyle("background-color: transparent; border: none");
		ZKUpdateUtil.setVflex(sRegion, "min");
		ZKUpdateUtil.setVflex(div, "min");
		ZKUpdateUtil.setVflex(confirmPanel, "min");
		ZKUpdateUtil.setVflex(statusBar, "min");

		confirmPanel.addActionListener(Events.ON_CLICK, this);

		this.setBorder("normal");
		this.setClosable(false);

		this.setSizable(true);
		this.setMaximizable(true);
		this.setSclass("account-dialog");
		
		if (ClientInfo.isMobile()) {
			ClientInfo.onClientInfo(this, this::onClientInfo);
		}
	}	//	jbInit

	/**
	 *	Dyanmic Init.
	 *  When a row is selected, the editor values are set
	 *  (editors do not change grid)
	 *  @return true if initialized
	 */
	private boolean initAccount()
	{
		m_AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
	
		Env.setContext(Env.getCtx(), m_WindowNo, "C_AcctSchema_ID", m_C_AcctSchema_ID);

		//  Model
		int AD_Window_ID = WINDOW_ACCOUNTCOMBINATION;		//	Maintain Account Combinations
		GridWindowVO wVO = AEnv.getMWindowVO (m_WindowNo, AD_Window_ID, 0);
		if (wVO == null)
			return false;
		// Force window/tab to be read-only
		wVO.WindowType = GridWindowVO.WINDOWTYPE_QUERY;
		wVO.Tabs.get(0).IsReadOnly = true;
		m_mWindow = new GridWindow (wVO);
		m_mTab = m_mWindow.getTab(0);
		// Make sure is the tab is loaded - teo_sarca [ 1659124 ]
		if (!m_mTab.isLoadComplete())
			m_mWindow.initTab(0);

		m_mTab.getField("AD_Client_ID").setDisplayed(false);
		m_mTab.getField("IsActive").setDisplayed(false);
		for (int i = 0; i < m_mTab.getFieldCount(); i++)
		{
			GridField field = m_mTab.getField(i);
			if (!field.isDisplayed (true))      //  check context
				field.setDisplayed (false);
		}

		//  GridController
		m_adTabPanel.init(null, m_WindowNo, m_mTab, null);

		//  Prepare Parameter
		parameterLayout.makeNoStrip();
		parameterLayout.setOddRowSclass("even");
		parameterLayout.setParent(parameterPanel);
		parameterLayout.setStyle("background-color: transparent; margin:none; border:none; padding:none;");

		layoutParameters();

		//	Finish
		m_query = new MQuery();
		//action_save doesn't filter by IsFullyQualified
//		m_query.addRestriction("IsFullyQualified", MQuery.EQUAL, "Y");
		if (m_mAccount.C_Account_ID == 0)
			m_mTab.setQuery(MQuery.getEqualQuery("1", "2"));
		else
		{
			MQuery query = new MQuery();
			query.addRestriction("C_Account_ID", MQuery.EQUAL, m_mAccount.C_Account_ID);
			m_mTab.setQuery(query);
		}
		m_mTab.query(false);
		m_adTabPanel.getGridTab().addDataStatusListener(this);
		m_adTabPanel.activate(true);
		if (!m_adTabPanel.isGridView())
			m_adTabPanel.switchRowPresentation();

		statusBar.setStatusLine("");
		statusBar.setStatusDB("");

		//	Initial value
		if (m_mAccount.C_Account_ID != 0) {
			m_mTab.navigate(0);
			if (f_Account_ID.getValue() instanceof Integer) {
				Env.setContext(Env.getCtx(), m_WindowNo, "Account_ID", (Integer)f_Account_ID.getValue());
				Env.setContext(Env.getCtx(), m_WindowNo, 0, "Account_ID", (Integer)f_Account_ID.getValue());
				
			}
		}

		log.config("fini");
		return true;
	}	//	initAccount

	protected void layoutParameters() {
		m_smallWidth = ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1);
		
		m_rows = new Rows();
		m_rows.setParent(parameterLayout);

		
		//m_newRow = true;

		

		//	Add description
		//m_newRow = true;
		Row row = new Row();
		f_Description.setStyle("font-decoration: italic;");
		Cell cell = new Cell();
		cell.setColspan(4);
		cell.appendChild(f_Description);
		row.appendChild(cell);
		row.setStyle("background-color: transparent; padding: 10px");
		m_rows.appendChild(row);
	}

	/**
	 *	Add Editor to parameterPanel alernative right/left depending on m_newRow.
	 *  Field Value changes update Editors
	 *  @param field field
	 *  @param editor editor
	 *  @param mandatory mandatory
	 */
	/*
	private void addLine (GridField field, WEditor editor, boolean mandatory)
	{
		if (log.isLoggable(Level.FINE)) log.fine("Field=" + field);
		Label label = editor.getLabel();
		editor.setReadWrite(true);
		editor.setMandatory(mandatory);
		//  MField => VEditor
		field.addPropertyChangeListener(editor);

		//	label
		if (m_newRow)
		{
			m_row = new Row();
			m_row.setStyle("background-color: transparent");
			m_rows.appendChild(m_row);
		}
//		else
//			m_gbc.gridx = 2;
		if (ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1))
		{
			Vlayout vlayout = new Vlayout();
			vlayout.setHflex("1");
			vlayout.setSpacing("0px");
			vlayout.appendChild(label);
			vlayout.appendChild(editor.getComponent());
			m_row.appendCellChild(vlayout, 2);
		}
		else
		{
			Div div = new Div();
			div.setStyle("text-align: right");
			div.appendChild(label);
			m_row.appendChild(div);
	
			m_row.appendChild(editor.getComponent());
		}
		editor.fillHorizontal();
		editor.dynamicDisplay();		
		
		//
		m_newRow = !m_newRow;
	}	//	addLine
*/
	/**
	 *	Load Information
	 *  @param C_ValidCombination_ID valid combination
	 *  @param C_AcctSchema_ID acct schema
	 */
	private void loadInfo (int C_Account_ID)
	{
		if (log.isLoggable(Level.FINE)) log.fine("C_Account_ID=" + C_Account_ID);
		String sql = "SELECT * FROM C_Account_ID WHERE C_Account_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Account_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				
				loadInfoOf (rs, f_AD_Org_ID, "AD_Org_ID");
				loadInfoOf (rs, f_Account_ID, "Account_ID");
				
				//
				f_Description.setValue (rs.getString("Description"));
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
	}	//	loadInfo

	/**
	 *	Set Value of Editor
	 *  @param rs result set
	 *  @param editor editor
	 *  @param name name
	 *  @throws SQLException
	 */
	private void loadInfoOf (ResultSet rs, WEditor editor, String name) throws SQLException
	{
		if (editor == null)
			return;
		int intValue = rs.getInt(name);
		if (rs.wasNull())
			editor.setValue(null);
		else
			editor.setValue(Integer.valueOf(intValue));
	}	//	loadInfoOf


	/**
	 *	dispose
	 */
	public void dispose()
	{
		saveSelection();
		//  GridController
		if (m_adTabPanel != null)
			m_adTabPanel.detach();
		m_adTabPanel = null;
		//  Model
		m_mTab = null;
		if (m_mWindow != null)
			m_mWindow.dispose();
		m_mWindow = null;

		Env.clearWinContext(m_WindowNo);
		this.onClose();
	}	//	dispose

	
	/* (non-Javadoc)
	 * @see org.adempiere.webui.component.Window#onPageDetached(org.zkoss.zk.ui.Page)
	 */
	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		if (m_callback != null) {
			m_callback.onCallback(getValue());
		}
	}

	/**
	 *	Save Selection
	 */
	private void saveSelection()
	{
		if (m_changed && m_adTabPanel != null)
		{
			int row = m_adTabPanel.getGridTab().getCurrentRow();
			if (row >= 0)
				m_C_Account_ID = ((Integer)m_mTab.getValue(row, "C_Account_ID")).intValue();
			if (log.isLoggable(Level.CONFIG)) log.config("(" + row + ") - " + m_C_Account_ID);
		}
	}	//	saveSelection

	public void onEvent(Event event) throws Exception {
		if (event.getTarget().getId().equals("Ok"))
		{
			// Compare all data to propose creation/update of combination
			MAccount combiOrg = new MAccount(Env.getCtx(), IDvalue > 0 ? IDvalue : m_mAccount.C_Account_ID, null);
			boolean needconfirm = false;
			if (needConfirm(f_AD_Org_ID, combiOrg))
				needconfirm = true;
			else if (needConfirm(f_Account_ID, combiOrg))
				needconfirm = true;
			

			if (needconfirm) {
				FDialog.ask(m_WindowNo, this, "CreateNewAccountCombination?", new Callback<Boolean>() {
					public void onCallback(Boolean result) {
						if (result) {
							if (action_Save()) {
								m_changed = true;
								dispose();
							}
						}
					}
				});
			} else {
				m_changed = true;
				dispose();
			}
			
		}
		else if (event.getTarget().getId().equals("Cancel"))
		{
			m_changed = false;
			dispose();
		}
		//
		else if (event.getTarget() == bSave)
			action_Save();
		else if (event.getTarget() == bIgnore)
			action_Ignore();
		//	all other
		else
			action_Find (true);
	}

	boolean needConfirm(WEditor editor, MAccount combiOrg)
	{
		if (editor != null ) {
			String columnName = editor.getColumnName();
			String oldValue = combiOrg.get_ValueAsString(columnName);
			String newValue = "";
			if (editor.getValue() != null)
				newValue = editor.getValue().toString();
			if (log.isLoggable(Level.FINE)) log.fine("columnName : " + columnName + " : " + oldValue + " - " + newValue);

			return ! oldValue.equals(newValue);
		}

		return false;
	}

	/**
	 *	Status Change Listener
	 *  @param e event
	 */
	public void dataStatusChanged (DataStatusEvent e)
	{
		if (log.isLoggable(Level.CONFIG)) log.config(e.toString());
		String info = (String)m_mTab.getValue("Description");
		if (Executions.getCurrent() != null)
			f_Description.setValue (info);
	}	//	statusChanged


	/**
	 *	Action Find.
	 *	- create where clause
	 *	- query database
	 *  @param includeAliasCombination include alias combination
	 */
	private void action_Find (boolean includeAliasCombination)
	{
		log.info("");

		//	Create where Clause
		MQuery query = null;
		if (m_query != null)
			query = m_query.deepCopy();
		else
			query = new MQuery();
		
		m_mTab.setQuery(query);
		m_mTab.query(false);
		statusBar.setStatusDB(String.valueOf(m_mTab.getRowCount()));
	}	//	action_Find


	/**
	 *	Create/Save Account
	 */
	private boolean action_Save()
	{
		log.info("");
		/**
		 *	Check completeness (mandatory fields) ... and for duplicates
		 */
		StringBuilder sb = new StringBuilder();
		StringBuilder sql = new StringBuilder ("SELECT C_Account_ID, Alias FROM C_Account WHERE ");
		//Object value = null;
		
		

		if (sb.length() != 0)
		{
			FDialog.error(m_WindowNo, this, "FillMandatory", sb.substring(0, sb.length()-2));
			return false;
		}
		if (f_AD_Org_ID == null || f_AD_Org_ID.getValue() == null)
		{
			FDialog.error(m_WindowNo, this, "FillMandatory", Msg.getElement(Env.getCtx(), "AD_Org_ID"));
			return false;
		}
		if (f_Account_ID == null || f_Account_ID.getValue() == null)
		{
			FDialog.error(m_WindowNo, this, "FillMandatory", Msg.getElement(Env.getCtx(), "Account_ID"));
			return false;
		}


		/**
		 *	Check if already exists
		 */
		sql.append("AD_Client_ID=?");
		if (log.isLoggable(Level.FINE)) log.fine("Check = " + sql.toString());
		String Alias = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				IDvalue = rs.getInt(1);
				Alias = rs.getString(2);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			IDvalue = 0;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (log.isLoggable(Level.FINE)) log.fine("ID=" + IDvalue + ", Alias=" + Alias);

		if (Alias == null)
			Alias = "";

		

		//	load and display
		if (IDvalue != 0)
		{
			loadInfo (IDvalue);
			action_Find (false);
			return true;
		}

		log.config("New");
		

		MElementValue acct = MElementValue.get (Env.getCtx(), ((Integer)f_Account_ID.getValue()).intValue());
		if (acct != null && acct.get_ID() == 0)
			acct.saveEx();

		//  Show Info
		if (acct == null || acct.get_ID() == 0)
			loadInfo (0);
		else
		{
			acct.saveEx();
			loadInfo (acct.get_ID());
		}
		IDvalue = acct.get_ID();
		action_Find (false);
		return true;
	}	//	action_Save

	
	private void action_Ignore()
	{
		
		f_Description.setValue("");
		//
		//	Org (mandatory)
		f_AD_Org_ID.setValue(null);
		//	Account (mandatory)
		f_Account_ID.setValue(null);
		
	}	//	action_Ignore

	/**
	 *	Get selected account
	 *  @return account
	 */
	public Integer getValue()
	{
		if (log.isLoggable(Level.CONFIG)) log.config("C_Account_ID=" + m_C_Account_ID + ", Changed=" + m_changed);
		if (!m_changed || m_C_Account_ID == 0)
			return null;
		return Integer.valueOf(m_C_Account_ID);
	}

	/**
	 * 	valueChange - Account Changed
	 *	@param evt event
	 */
	public void valueChange(ValueChangeEvent evt) {
		Object newValue = evt.getNewValue();
		if (newValue instanceof Integer) {
			Env.setContext(Env.getCtx(), m_WindowNo, "Account_ID", ((Integer)newValue).intValue());
			Env.setContext(Env.getCtx(), m_WindowNo, 0, "Account_ID", ((Integer)newValue).intValue());
			
		}
	}
		
	protected void onClientInfo() {
		if (parameterLayout != null && parameterLayout.getRows() != null) {
			boolean smallWidth = ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1);
			if (smallWidth != m_smallWidth) {
				parameterLayout.getRows().detach();
				layoutParameters();
				if (ThemeManager.isUseCSSForWindowSize()) {
					ZKUpdateUtil.setCSSHeight(this);
					ZKUpdateUtil.setCSSWidth(this);
				}
				this.invalidate();
			}
		}
	}
}	//	WAccountDialog