

package eone.webui.panel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.South;

import eone.base.model.MQuery;
import eone.base.model.MRole;
import eone.webui.AdempiereWebUI;
import eone.webui.apps.AEnv;
import eone.webui.component.Button;
import eone.webui.component.Grid;
import eone.webui.component.GridFactory;
import eone.webui.component.Label;
import eone.webui.component.ListItem;
import eone.webui.component.Listbox;
import eone.webui.component.ListboxFactory;
import eone.webui.component.Row;
import eone.webui.component.Rows;
import eone.webui.component.Tab;
import eone.webui.component.Tabbox;
import eone.webui.component.Tabpanel;
import eone.webui.component.Tabpanels;
import eone.webui.component.Tabs;
import eone.webui.component.Textbox;
import eone.webui.component.WListbox;
import eone.webui.event.DialogEvents;
import eone.webui.session.SessionManager;
import eone.webui.theme.ThemeManager;
import eone.webui.util.ZKUpdateUtil;


@Deprecated // replaced with InfoProductWindow IDEMPIERE-325
public class InfoProductPanel extends InfoPanel implements EventListener<Event>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3728242035878883637L;

	private Label lblValue = new Label();
	private Textbox fieldValue = new Textbox();
	private Label lblName = new Label();
	private Textbox fieldName = new Textbox();
	private Label lblWarehouse = new Label();
	private Listbox pickWarehouse = new Listbox();
	private Label lblProductCategory = new Label();
	private Listbox pickProductCategory = new Listbox();
	
	// Elaine 2008/11/25
	private Borderlayout borderlayout = new Borderlayout();
	private Textbox fieldDescription = new Textbox();
	Tabbox tabbedPane = new Tabbox();
	WListbox warehouseTbl = ListboxFactory.newDataTable();
    String m_sqlWarehouse;
    WListbox substituteTbl = ListboxFactory.newDataTable();
    String m_sqlSubstitute;
    WListbox relatedTbl = ListboxFactory.newDataTable();
    String m_sqlRelated;
    //Available to Promise Tab
	private WListbox 			m_tableAtp = ListboxFactory.newDataTable();
    int mWindowNo = 0;
    //

    //IDEMPIERE-337
    WListbox productpriceTbl = ListboxFactory.newDataTable();
    String m_sqlProductprice;
    
	/**	Search Button				*/
	private Button	m_InfoPAttributeButton = new Button();
	/** Instance Button				*/
	/** SQL From				*/
	private static final String s_productFrom =
		"M_Product p"
		+ " LEFT OUTER JOIN M_ProductPrice pr ON (p.M_Product_ID=pr.M_Product_ID AND pr.IsActive='Y')";
		//+ " LEFT OUTER JOIN M_AttributeSet pa ON (p.M_AttributeSet_ID=pa.M_AttributeSet_ID)"
		//+ " LEFT OUTER JOIN M_Product_PO ppo ON (p.M_Product_ID=ppo.M_Product_ID)"
		//+ " LEFT OUTER JOIN C_BPartner bp ON (ppo.C_BPartner_ID=bp.C_BPartner_ID)";

	/**  Array of Column Info    */
	private static ColumnInfo[] s_productLayout = null;



	private String		m_pAttributeWhere = null;
	
	/**
	 *	Standard Constructor
	 * 	@param WindowNo window no
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_PriceList_ID price list
	 * 	@param value    Query Value or Name if enclosed in @
	 * 	@param whereClause where clause
	 */
	public InfoProductPanel(int windowNo,
		int M_Warehouse_ID, int M_PriceList_ID, boolean multipleSelection,String value,
		 String whereClause)
	{
		this(windowNo, M_Warehouse_ID, M_PriceList_ID, multipleSelection, value, whereClause, true);
	}

	/**
	 *	Standard Constructor
	 * 	@param WindowNo window no
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_PriceList_ID price list
	 * 	@param value    Query Value or Name if enclosed in @
	 * 	@param whereClause where clause
	 */
	public InfoProductPanel(int windowNo,
		int M_Warehouse_ID, int M_PriceList_ID, boolean multipleSelection,String value,
		 String whereClause, boolean lookup)
	{
		super (windowNo, "p", "M_Product_ID",multipleSelection, whereClause, lookup);
		log.info(value + ", Wh=" + M_Warehouse_ID + ", PL=" + M_PriceList_ID + ", WHERE=" + whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoProduct"));
		//
		initComponents();
		init();
		initInfo (M_Warehouse_ID, M_PriceList_ID);

        int no = contentPanel.getRowCount();
        setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		//	AutoQuery
		if (value != null && value.length() > 0)
        {
	        //	Set Value
	        if (value.indexOf("_") > 0) {
	        	String values[] = value.split("_");
	        	value = values[0];
	        }
			//	Set Value or Name
			fieldValue.setText(value);
			testCount();
			if (m_count <= 0) {
				fieldValue.setText("");
				fieldName.setValue(value);
			}

			executeQuery();
            renderItems();
        }

		tabbedPane.setSelectedIndex(0);

		p_loadedOK = true;

		//Begin - fer_luck @ centuryon
		mWindowNo = windowNo; // Elaine 2008/12/16
		//End - fer_luck @ centuryon

	}	//	InfoProductPanel

	/**
	 *	initialize fields
	 */
	private void initComponents()
	{
		lblValue = new Label();
		lblValue.setValue(Util.cleanAmp(Msg.translate(Env.getCtx(), "Value")));
		lblName = new Label();
		lblName.setValue(Util.cleanAmp(Msg.translate(Env.getCtx(), "Name")));
		lblProductCategory = new Label();
		lblProductCategory.setValue(Msg.translate(Env.getCtx(), "M_Product_Category_ID"));
		lblWarehouse = new Label();
		//lblWarehouse.setValue(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Warehouse")));
		
		m_InfoPAttributeButton.setImage(ThemeManager.getThemeResource("images/PAttribute16.png"));
		m_InfoPAttributeButton.setTooltiptext(Msg.getMsg(Env.getCtx(), "PAttribute"));
		m_InfoPAttributeButton.addEventListener(Events.ON_CLICK,this);

		fieldValue = new Textbox();
		fieldValue.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "value");
		fieldName = new Textbox();
		fieldName.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "name");
		
		// Elaine 2008/11/21
		pickProductCategory = new Listbox();
		pickProductCategory.setRows(0);
		pickProductCategory.setMultiple(false);
		pickProductCategory.setMold("select");
		ZKUpdateUtil.setHflex(pickProductCategory, "1");
		pickProductCategory.addEventListener(Events.ON_SELECT, this);
		pickProductCategory.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "productCategory");
		
		pickWarehouse = new Listbox();
		pickWarehouse.setRows(0);
		pickWarehouse.setMultiple(false);
		pickWarehouse.setMold("select");
		ZKUpdateUtil.setHflex(pickWarehouse, "1");
		pickWarehouse.addEventListener(Events.ON_SELECT, this);
		pickWarehouse.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "warehouse");

		ZKUpdateUtil.setVflex(contentPanel, true);
	}	//	initComponents

	private void init()
	{
    	Grid grid = GridFactory.newGridLayout();

		Rows rows = new Rows();
		grid.appendChild(rows);

		Row row = new Row();
		rows.appendChild(row);
		row.appendChild(lblValue.rightAlign());
		row.appendChild(fieldValue);
		ZKUpdateUtil.setHflex(fieldValue, "1");
		row.appendChild(lblWarehouse.rightAlign());
		row.appendChild(pickWarehouse);
		row.appendChild(m_InfoPAttributeButton);

		row = new Row();
		row.appendCellChild(lblName.rightAlign());
		row.appendCellChild(fieldName);
		ZKUpdateUtil.setHflex(fieldName, "1");
		
		row = new Row();
		rows.appendChild(row);
		row.appendChild(lblProductCategory.rightAlign());
		row.appendChild(pickProductCategory);
		
		row = new Row();
		rows.appendChild(row);
		row.appendCellChild(statusBar, 6);
		statusBar.setEastVisibility(false);
		ZKUpdateUtil.setWidth(statusBar, "100%");

		
        // Elaine 2008/11/25
        fieldDescription.setMultiline(true);
		fieldDescription.setReadonly(true);
		ZKUpdateUtil.setHflex(fieldDescription, "1");

		//
        ColumnInfo[] s_layoutWarehouse = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"), "w.Name", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "RemainQty"), "Sum(s.Qty)", Double.class)};
        /**	From Clause							*/
        String s_sqlFrom = " M_Storage s Inner Join M_Warehouse w on s.M_Warehouse_ID = w.M_Warehouse_ID ";
        /** Where Clause						*/
        String s_sqlWhere = "s.M_Product_ID = ?";
        m_sqlWarehouse = warehouseTbl.prepareTable(s_layoutWarehouse, s_sqlFrom, s_sqlWhere, false, "s");
		m_sqlWarehouse += " GROUP BY w.Name";
		warehouseTbl.setMultiSelection(false);
		warehouseTbl.setShowTotals(true);
		warehouseTbl.autoSize();
        warehouseTbl.getModel().addTableModelListener(this);

        //Available to Promise Tab
        m_tableAtp.setMultiSelection(false);

        //IDEMPIERE-337
        ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
        list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Price"), "pp.Price", String.class));
        list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "ValidFrom"), "pp.ValidFrom", Timestamp.class));
        ColumnInfo[] s_layoutProductPrice = new ColumnInfo[list.size()];
        list.toArray(s_layoutProductPrice);
        s_sqlFrom = "M_Price pp";
        s_sqlWhere = "pp.M_Product_ID = ? AND pp.IsActive = 'Y'";
        m_sqlProductprice = productpriceTbl.prepareTable(s_layoutProductPrice, s_sqlFrom, s_sqlWhere, false, "pp") + " ORDER BY pp.ValidFrom DESC";
        productpriceTbl.setMultiSelection(false);
        productpriceTbl.autoSize();
        productpriceTbl.getModel().addTableModelListener(this);
        
        ZKUpdateUtil.setHeight(tabbedPane, "100%");
		Tabpanels tabPanels = new Tabpanels();
		tabbedPane.appendChild(tabPanels);
		Tabs tabs = new Tabs();
		tabbedPane.appendChild(tabs);

		Tab tab = new Tab(Util.cleanAmp(Msg.translate(Env.getCtx(), "Warehouse")));
		tabs.appendChild(tab);
		Tabpanel desktopTabPanel = new Tabpanel();
		ZKUpdateUtil.setHeight(desktopTabPanel, "100%");
		desktopTabPanel.appendChild(warehouseTbl);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.translate(Env.getCtx(), "Price"));
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		ZKUpdateUtil.setHeight(desktopTabPanel, "100%");
		desktopTabPanel.appendChild(productpriceTbl);
		tabPanels.appendChild(desktopTabPanel);
		//
		int height = SessionManager.getAppDesktop().getClientInfo().desktopHeight * 90 / 100;
		int width = SessionManager.getAppDesktop().getClientInfo().desktopWidth * 80 / 100;

		ZKUpdateUtil.setWidth(borderlayout, "100%");
		ZKUpdateUtil.setHeight(borderlayout, "100%");
        if (isLookup())
        	borderlayout.setStyle("border: none; position: relative; ");
        else
        	borderlayout.setStyle("border: none; position: absolute; ");
        Center center = new Center();
        //true will conflict with listbox scrolling
        center.setAutoscroll(false);
        borderlayout.appendChild(center);
		center.appendChild(contentPanel);
		ZKUpdateUtil.setVflex(contentPanel, "1");
		ZKUpdateUtil.setHflex(contentPanel, "1");
		South south = new South();
		int detailHeight = (height * 25 / 100);
		ZKUpdateUtil.setHeight(south, detailHeight + "px");
		south.setCollapsible(true);
		south.setSplittable(true);
		south.setTitle(Msg.translate(Env.getCtx(), "WarehouseStock"));
		south.setTooltiptext(Msg.translate(Env.getCtx(), "WarehouseStock"));
		borderlayout.appendChild(south);
		tabbedPane.setSclass("info-product-tabbedpane");
		south.appendChild(tabbedPane);
		ZKUpdateUtil.setVflex(tabbedPane, "1");
		ZKUpdateUtil.setHflex(tabbedPane, "1");

        Borderlayout mainPanel = new Borderlayout();
        ZKUpdateUtil.setWidth(mainPanel, "100%");
        ZKUpdateUtil.setHeight(mainPanel, "100%");
        North north = new North();
        mainPanel.appendChild(north);
        north.appendChild(grid);
        center = new Center();
        mainPanel.appendChild(center);
        center.appendChild(borderlayout);
        south = new South();
        mainPanel.appendChild(south);
        south.appendChild(confirmPanel);
        if (!isLookup())
        {
        	mainPanel.setStyle("position: absolute");
        }

		this.appendChild(mainPanel);
		if (isLookup())
		{
			ZKUpdateUtil.setWidth(this, width + "px");
			ZKUpdateUtil.setHeight(this, height + "px");
		}

		contentPanel.addActionListener(new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				int row = contentPanel.getSelectedRow();
				if (row >= 0) {
										
        			borderlayout.getSouth().setOpen(true);
				}
			}
		});
	}

	/**
	 * IDEMPIERE-337
	 * Override InfoPanel.testCount() to allow counting distinct rows
	 * 
	 */
	@Override
	protected boolean testCount() {
		long start = System.currentTimeMillis();
		String dynWhere = getSQLWhere();
		
		int M_Warehouse_ID = 0;
		ListItem listitem = pickWarehouse.getSelectedItem();
		if (listitem != null)
			M_Warehouse_ID = (Integer)listitem.getValue();
		
		StringBuffer where = new StringBuffer();
		where.append("p.IsActive='Y'");
		if (M_Warehouse_ID != 0)
			where.append(" AND p.IsSummary='N'");
		//  dynamic Where Clause
		if (p_whereClause != null && p_whereClause.length() > 0)
			where.append(" AND ")   //  replace fully qualified name with alias
				.append(Util.replace(p_whereClause, "M_Product.", "p."));	
		
		StringBuilder sqlMain = new StringBuilder("SELECT ");
		int colIndex = 0;
		ColumnInfo[] layout = getProductLayout();
		for (colIndex = 0; colIndex < layout.length; colIndex++)
		{
			if (colIndex > 0)
				sqlMain.append(", ");
			sqlMain.append(layout[colIndex].getColSQL());
			if (layout[colIndex].isKeyPairCol())
				sqlMain.append(",").append(layout[colIndex].getKeyPairColSQL());
		}
		
		sqlMain.append(" FROM ").append(s_productFrom);
		sqlMain.append(" WHERE ").append(where);
		
		if (dynWhere.length() > 0)
			sqlMain.append(dynWhere); 
		
		String countSql = Msg.parseTranslation(Env.getCtx(), sqlMain.toString());	//	Variables
		countSql = MRole.getDefault().addAccessSQL	(countSql, getTableName(),
													MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		String sqlCount = "SELECT COUNT(*) FROM (" + countSql + ") ProductInfo";
		
		log.finer(sqlCount);
		m_count = -1;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;;
		try
		{
			pstmt = DB.prepareStatement(sqlCount, null);
			setParameters (pstmt, false);
			rs = pstmt.executeQuery();
		
			if (rs.next())
				m_count = rs.getInt(1);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sqlCount, e);
			m_count = -2;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		if (log.isLoggable(Level.FINE)) log.fine("#" + m_count + " - " + (System.currentTimeMillis()-start) + "ms");

		return true;
	}
	
	@Override
	protected void insertPagingComponent() {
		North north = new North();
		north.appendChild(paging);
		borderlayout.appendChild(north);
	}

	

	/**
	 *	Dynamic Init
	 *
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 */
	private void initInfo (int M_Warehouse_ID, int M_PriceList_ID)
	{
		//	Set Warehouse
		if (M_Warehouse_ID == 0)
			M_Warehouse_ID = Env.getContextAsInt(Env.getCtx(), "#M_Warehouse_ID");
		if (M_Warehouse_ID != 0)
			setWarehouse (M_Warehouse_ID);
		
		//	Create Grid
		String orderBy = null;
		StringBuilder where = new StringBuilder();
		where.append("p.IsActive='Y'");
		if (M_Warehouse_ID != 0)
		{
			where.append(" AND p.IsSummary='N'");
			orderBy = "RemainQty DESC";
		}
		//  dynamic Where Clause
		if (p_whereClause != null && p_whereClause.length() > 0)
			where.append(" AND ")   //  replace fully qalified name with alias
				.append(Util.replace(p_whereClause, "M_Product.", "p."));
		//
		prepareTable(getProductLayout(),
			s_productFrom,
			where.toString(),
			orderBy);

	}	//	initInfo

	private void setWarehouse(int M_Warehouse_ID)
	{
		for (int i = 0; i < pickWarehouse.getItemCount(); i++)
		{
			 Integer key = (Integer) pickWarehouse.getItemAtIndex(i).getValue();
			if (key == M_Warehouse_ID)
			{
				pickWarehouse.setSelectedIndex(i);
				return;
			}
		}
	}	//	setWarehouse

	

	public String getSQLWhere()
	{
		StringBuilder where = new StringBuilder();

		if (getM_Product_Category_ID() > 0) {
			where.append(" AND p.M_Product_Category_ID=?");
		}
		//	Product Attribute Search
		if (m_pAttributeWhere != null)
		{
			where.append(m_pAttributeWhere);
			return where.toString();
		}

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(p.Value) LIKE ?");

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(p.Name) LIKE ?");

		
		return where.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query
	 *  (as defined in getSQLWhere)	 *
	 * @param pstmt pstmt
	 *  @param forCount for counting records
	 * @throws SQLException
	 */
	protected void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;

		//  => Warehouse
		int M_Warehouse_ID = 0;
		ListItem listitem = pickWarehouse.getSelectedItem();
		if (listitem != null)
			M_Warehouse_ID = (Integer)listitem.getValue();
		if (!forCount)	//	parameters in select
		{
			for (int i = 0; i < p_layout.length; i++)
			{
				if (p_layout[i].getColSQL().indexOf('?') != -1)
					pstmt.setInt(index++, M_Warehouse_ID);
			}
		}
		if (log.isLoggable(Level.FINE)) log.fine("M_Warehouse_ID=" + M_Warehouse_ID + " (" + (index-1) + "*)");

		
		// Elaine 2008/11/29
		//  => Product Category
		int M_Product_Category_ID = getM_Product_Category_ID();
		if (M_Product_Category_ID > 0) {
			pstmt.setInt(index++, M_Product_Category_ID);
			if (log.isLoggable(Level.FINE)) log.fine("M_Product_Category_ID=" + M_Product_Category_ID);
		}
		
		//	Rest of Parameter in Query for Attribute Search
		if (m_pAttributeWhere != null)
			return;

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
			if (log.isLoggable(Level.FINE)) log.fine("Value: " + value);
		}

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
			if (log.isLoggable(Level.FINE)) log.fine("Name: " + name);
		}

		
	}   //  setParameters

	/**
	 * 	Query per Product Attribute.
	 *  <code>
	 * 	Available synonyms:
	 *		M_Product p
	 *		M_ProductPrice pr
	 *		M_AttributeSet pa
	 *	</code>
	 */
	private void cmd_InfoPAttribute()
	{
		final InfoPAttributePanel ia = new InfoPAttributePanel(this);
		ia.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				m_pAttributeWhere = ia.getWhereClause();
				if (m_pAttributeWhere != null)
				{
					executeQuery();
					renderItems();
				}
			}
		});
		
	}	//	cmdInfoAttribute

	/**
	 *	Show History
	 */
	protected void showHistory()
	{
		log.info("");
		Integer M_Product_ID = getSelectedRowKey();
		if (M_Product_ID == null)
			return;
		int M_Warehouse_ID = 0;
		ListItem listitem = pickWarehouse.getSelectedItem();
		if (listitem != null)
			M_Warehouse_ID = (Integer)listitem.getValue();
		InvoiceHistory ih = new InvoiceHistory (this, 0, M_Product_ID.intValue(), M_Warehouse_ID);
		ih.setVisible(true);
		ih = null;
	}	//	showHistory

	/**
	 *	Has History
	 *
	 * @return true (has history)
	 */
	protected boolean hasHistory()
	{
		return true;
	}	//	hasHistory

	// Elaine 2008/12/16
	/**
	 *	Zoom
	 */
	public void zoom()
	{
		log.info("");
		Integer M_Product_ID = getSelectedRowKey();
		if (M_Product_ID == null)
			return;

		MQuery query = new MQuery("M_Product");
		query.addRestriction("M_Product_ID", MQuery.EQUAL, M_Product_ID);
		query.setRecordCount(1);
		int AD_WindowNo = getAD_Window_ID("M_Product", true);	//	SO
		AEnv.zoom (AD_WindowNo, query);
	}	//	zoom
	//

	/**
	 *	Has Zoom
	 *  @return (has zoom)
	 */
	protected boolean hasZoom()
	{
		return true;
	}	//	hasZoom

	/**
	 *	Customize
	 */
	protected void customize()
	{
		log.info("");
	}	//	customize

	/**
	 *	Has Customize
	 *  @return false (no customize)
	 */
	protected boolean hasCustomize()
	{
		return false;	//	for now
	}	//	hasCustomize

	/**
	 *	Save Selection Settings for PriceList
	 */
	protected void saveSelectionDetail()
	{
		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Product_ID", ID == null ? "0" : ID.toString());
		ListItem pickWH = (ListItem)pickWarehouse.getSelectedItem();
		if (pickWH != null)
        {
            Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Warehouse_ID",pickWH.getValue().toString());
        }
		
	}	//	saveSelectionDetail

	/**
	 *  Get Product Layout
	 *
	 * @return array of Column_Info
	 */
	protected ColumnInfo[] getProductLayout()
	{
		
		s_productLayout = null;
		int M_Warehouse_ID = 0;
		ListItem listitem = pickWarehouse.getSelectedItem();
		if (listitem != null)
			M_Warehouse_ID = (Integer)listitem.getValue();
				
		ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
		list.add(new ColumnInfo(" ", "DISTINCT p.M_Product_ID", IDColumn.class));
		list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Value"), "p.Value", String.class));
		list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "p.Name", String.class));
		if (M_Warehouse_ID != 0)
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "RemainQty", Double.class, true, true, null));
		s_productLayout = new ColumnInfo[list.size()];
		list.toArray(s_productLayout);

		return s_productLayout;
	}   //  getProductLayout

	

    public void onEvent(Event e)
    {
    	Component component = e.getTarget();

    	if (e.getTarget() == pickWarehouse)
		{
			boolean execute = true;
			if (fieldValue.getText().length() == 0 && fieldName.getText().length() == 0 && contentPanel.getRowCount() == 0)
				execute = false;
			// Remove existing headers
			if (contentPanel.getListhead() != null)
				contentPanel.getListhead().detach();
			//	Create Grid
			int M_Warehouse_ID = 0;
			ListItem listitem = pickWarehouse.getSelectedItem();
			if (listitem != null)
				M_Warehouse_ID = (Integer)listitem.getValue();
			StringBuilder where = new StringBuilder();
			where.append("p.IsActive='Y'");
			if (M_Warehouse_ID != 0)
				where.append(" AND p.IsSummary='N'");
			//  dynamic Where Clause
			if (p_whereClause != null && p_whereClause.length() > 0)
				where.append(" AND ")   //  replace fully qalified name with alias
					.append(Util.replace(p_whereClause, "M_Product.", "p."));
			//
			prepareTable(getProductLayout(),
				s_productFrom,
				where.toString(),
				"");
			if (!execute)
				return;
		}

    	if(component == m_InfoPAttributeButton)
    	{
    		cmd_InfoPAttribute();
    		return;
    	}

    	
		super.onEvent(e);
    }

	/**
	 *  Enable PAttribute if row selected/changed
	 */
	protected void enableButtons ()
	{
		

		super.enableButtons();
	}   //  enableButtons

    

    // Elaine 2008/11/21
    public int getM_Product_Category_ID()
    {
		int M_Product_Category_ID = 0;

		ListItem pickPC = (ListItem)pickProductCategory.getSelectedItem();
		if (pickPC!=null)
			M_Product_Category_ID = Integer.parseInt(pickPC.getValue().toString());

		return M_Product_Category_ID;
	}
    
}	//	InfoProduct
