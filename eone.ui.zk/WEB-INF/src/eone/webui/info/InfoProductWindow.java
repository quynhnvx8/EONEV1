package eone.webui.info;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Center;
import org.zkoss.zul.South;

import eone.webui.ClientInfo;
import eone.webui.component.Borderlayout;
import eone.webui.component.Button;
import eone.webui.component.Checkbox;
import eone.webui.component.ConfirmPanel;
import eone.webui.component.ListboxFactory;
import eone.webui.component.Tab;
import eone.webui.component.Tabbox;
import eone.webui.component.Tabpanel;
import eone.webui.component.Tabpanels;
import eone.webui.component.Tabs;
import eone.webui.component.Textbox;
import eone.webui.component.WListbox;
import eone.webui.editor.WEditor;
import eone.webui.panel.InvoiceHistory;
import eone.webui.session.SessionManager;
import eone.webui.util.ZKUpdateUtil;

public class InfoProductWindow extends InfoWindow {
	private static final long serialVersionUID = -7892916038089331016L;

	protected Tabbox tabbedPane;
	protected WListbox warehouseTbl;
	protected String m_sqlWarehouse;
	protected WListbox substituteTbl;
	protected String m_sqlSubstitute;
	protected WListbox relatedTbl;
	protected String m_sqlRelated;
    //Available to Promise Tab
	protected WListbox m_tableAtp;
	
	// Group atp by warehouse or non
	protected Checkbox chbShowDetailAtp;

	//IDEMPIERE-337
	protected WListbox productpriceTbl;
	protected String m_sqlProductprice;
    
	protected Textbox fieldDescription;
    
    

	protected Borderlayout contentBorderLayout;
	
	/** Instance Button				*/
	protected Button	m_PAttributeButton;

	protected int m_M_Warehouse_ID;
	
	/**
	 * @param WindowNo
	 * @param tableName
	 * @param keyColumn
	 * @param queryValue
	 * @param multipleSelection
	 * @param whereClause
	 * @param AD_InfoWindow_ID
	 */
	public InfoProductWindow(int WindowNo, String tableName, String keyColumn,
			String queryValue, boolean multipleSelection, String whereClause,
			int AD_InfoWindow_ID) {
		this(WindowNo, tableName, keyColumn, queryValue, multipleSelection, whereClause, AD_InfoWindow_ID, true);
	}

	/**
	 * @param WindowNo
	 * @param tableName
	 * @param keyColumn
	 * @param queryValue
	 * @param multipleSelection
	 * @param whereClause
	 * @param AD_InfoWindow_ID
	 * @param lookup
	 */
	public InfoProductWindow(int WindowNo, String tableName, String keyColumn,
			String queryValue, boolean multipleSelection, String whereClause,
			int AD_InfoWindow_ID, boolean lookup) {
		super(WindowNo, tableName, keyColumn, queryValue, multipleSelection,
				whereClause, AD_InfoWindow_ID, lookup);
	}

	@Override
	protected String getSQLWhere() {
		/**
		 * when query not by click requery button, reuse prev where clause
		 * IDEMPIERE-1979  
		 */
		if (!isQueryByUser && prevWhereClause != null){
			return prevWhereClause;
		}
		
		StringBuilder where = new StringBuilder(super.getSQLWhere());
		if (getSelectedWarehouseId() > 0) {
			if (where.length() > 0) {
				where.append(" AND ");
			}
			where.append("p.IsSummary='N' ");
		}
		// IDEMPIERE-1979
		prevWhereClause = where.toString();
		return prevWhereClause;
	}

	@Override
	protected void renderWindow() {
		super.renderWindow();
		// Product Attribute Instance
		m_PAttributeButton = confirmPanel.createButton(ConfirmPanel.A_PATTRIBUTE);
		confirmPanel.addComponentsLeft(m_PAttributeButton);
		m_PAttributeButton.setEnabled(false);
		m_PAttributeButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				onPAttributeClick();
			}
		});
		m_PAttributeButton.setVisible(true);
	}

	@Override
	protected void renderContentPane(Center center) {
		ColumnInfo[] s_layoutWarehouse = new ColumnInfo[]
			{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"), "w.Name", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "RemainQty"), "sum(s.Qty)", Double.class)
        	};
        /**	From Clause							*/
        String s_sqlFrom = " M_Storage s Inner Join M_Warehouse w On s.M_Warehouse_ID = w.M_Warehouse_ID ";
        /** Where Clause						*/
        String s_sqlWhere = "M_Product_ID = ?";
        warehouseTbl = ListboxFactory.newDataTableAutoSize();
        m_sqlWarehouse = warehouseTbl.prepareTable(s_layoutWarehouse, s_sqlFrom, s_sqlWhere, false, "s");
		m_sqlWarehouse += " GROUP BY w.Name";		
		warehouseTbl.setMultiSelection(false);
		warehouseTbl.setShowTotals(true);
		warehouseTbl.autoSize();
//        warehouseTbl.getModel().addTableModelListener(this);

        //IDEMPIERE-337
        ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
        list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Price"), "pp.Price", String.class));
        list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "ValidFrom"), "pp.ValidFrom", Timestamp.class));
        ColumnInfo[] s_layoutProductPrice = new ColumnInfo[list.size()];
        list.toArray(s_layoutProductPrice);
        s_sqlFrom = "M_Price pp ";
        s_sqlWhere = "pp.M_Product_ID = ? AND pp.IsActive = 'Y'";
        productpriceTbl = ListboxFactory.newDataTableAutoSize();
        m_sqlProductprice = productpriceTbl.prepareTable(s_layoutProductPrice, s_sqlFrom, s_sqlWhere, false, "pp") + " ORDER BY pp.ValidFrom DESC";
        productpriceTbl.setMultiSelection(false);
        productpriceTbl.autoSize();
//        productpriceTbl.getModel().addTableModelListener(this);
        
        tabbedPane = new Tabbox();
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
		
		contentBorderLayout = new Borderlayout();
		ZKUpdateUtil.setWidth(contentBorderLayout, "100%");
		ZKUpdateUtil.setHeight(contentBorderLayout, "100%");
        if (isLookup())
        	contentBorderLayout.setStyle("border: none; position: relative; ");
        else
        	contentBorderLayout.setStyle("border: none; position: absolute; ");
        contentBorderLayout.appendCenter(contentPanel);
        //true will conflict with listbox scrolling
        contentBorderLayout.getCenter().setAutoscroll(false);
        South south = new South();
		int detailHeight = (height * 25 / 100);
		ZKUpdateUtil.setHeight(south, detailHeight + "px");
		south.setCollapsible(true);
		south.setSplittable(true);
		south.setTitle(Msg.translate(Env.getCtx(), "WarehouseStock"));
		south.setTooltiptext(Msg.translate(Env.getCtx(), "WarehouseStock"));
		
		south.setSclass("south-collapsible-with-title");
		if (ClientInfo.maxHeight(ClientInfo.MEDIUM_HEIGHT-1))
		{
			south.setOpen(false);
			ZKUpdateUtil.setHeight(south, "100%");
		}
		contentBorderLayout.appendChild(south);
		tabbedPane.setSclass("info-product-tabbedpane");
		south.appendChild(tabbedPane);
		ZKUpdateUtil.setVflex(tabbedPane, "1");
		ZKUpdateUtil.setHflex(tabbedPane, "1");
		
		center.appendChild(contentBorderLayout);
		
		contentPanel.addActionListener(new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				int row = contentPanel.getSelectedRow();
				if (row >= 0) {
					int M_Warehouse_ID = getSelectedWarehouseId();

					int M_PriceList_Version_ID = getSelectedPriceListVersionId();

					for(int i = 0; i < columnInfos.length; i++) {
						if (columnInfos[i].getGridField() != null && columnInfos[i].getGridField().getColumnName().equals("Value")) {
							refresh(M_Warehouse_ID, M_PriceList_Version_ID);
							if (ClientInfo.minHeight(ClientInfo.MEDIUM_HEIGHT))
								contentBorderLayout.getSouth().setOpen(true);
		        			break;
						}
					}
					
					Object value = contentPanel.getValueAt(row, findColumnIndex("Name"));
					if (value != null && value.toString().equals("true")) {
						m_PAttributeButton.setEnabled(true);
					} else {
						m_PAttributeButton.setEnabled(false);
					}
				}
			}
		});
	}

	protected void onPAttributeClick() {
		Integer productInteger = getSelectedRowKey();
		if (productInteger == null) {
			m_PAttributeButton.setEnabled(false);
			return;
		}

		if (productInteger == null || productInteger.intValue() == 0)
			return;

		int M_Warehouse_ID = getSelectedWarehouseId();
		if (M_Warehouse_ID <= 0)
			return;

		m_M_Warehouse_ID = M_Warehouse_ID;
		
	}
	
	protected String getSelectedWarehouseLabel() {
		for(WEditor editor : editors) {
			if (editor.getGridField() != null && editor.getGridField().getColumnName().equals("M_Warehouse_ID")) {
				Number value = (Number) editor.getValue();
				if (value != null)
					return editor.getDisplay();
				
				break;
			}
		}
		return "";
	}

	protected int getSelectedPriceListVersionId() {
		for(WEditor editor : editors) {
			if (editor.getGridField() != null && editor.getGridField().getColumnName().equals("M_Price_ID")) {
				Number value = (Number) editor.getValue();
				if (value != null)
					return value.intValue();
				
				break;
			}
		}
		return 0;
	}

	protected int getSelectedWarehouseId() {
		for(WEditor editor : editors) {
			if (editor.getGridField() != null && editor.getGridField().getColumnName().equals("M_Warehouse_ID")) {
				Number value = (Number) editor.getValue();
				if (value != null)
					return value.intValue();
				
				break;
			}
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initParameters() {
		//int M_Warehouse_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "M_Warehouse_ID");
		//int M_PriceList_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "M_PriceList_ID");
		
		//int M_PriceList_Version_ID = findPLV (M_PriceList_ID);
		//	Set Warehouse
		//if (M_Warehouse_ID == 0)
		//	M_Warehouse_ID = Env.getContextAsInt(Env.getCtx(), "#M_Warehouse_ID");
		//if (M_Warehouse_ID != 0)
		//	setWarehouse (M_Warehouse_ID);
		// 	Set PriceList Version
		//if (M_PriceList_Version_ID != 0)
		//	setPriceListVersion (M_PriceList_Version_ID);
	}
	
	/**
	 *	Find Price List Version and update context
	 *
	 * @param M_PriceList_ID price list
	 * @return M_PriceList_Version_ID price list version
	 */
	protected int findPLV (int M_PriceList_ID)
	{
		Timestamp priceDate = null;
		//	Sales Order Date
		String dateStr = Env.getContext(Env.getCtx(), p_WindowNo, "DateOrdered");
		if (dateStr != null && dateStr.length() > 0)
			priceDate = Env.getContextAsDate(Env.getCtx(), p_WindowNo, "DateOrdered");
		else	//	Invoice Date
		{
			dateStr = Env.getContext(Env.getCtx(), p_WindowNo, "DateInvoiced");
			if (dateStr != null && dateStr.length() > 0)
				priceDate = Env.getContextAsDate(Env.getCtx(), p_WindowNo, "DateInvoiced");
		}
		//	Today
		if (priceDate == null)
			priceDate = new Timestamp(System.currentTimeMillis());
		//
		if (log.isLoggable(Level.CONFIG)) log.config("M_PriceList_ID=" + M_PriceList_ID + " - " + priceDate);
		int retValue = 0;
		String sql = "SELECT plv.M_PriceList_Version_ID, plv.ValidFrom "
			+ "FROM M_PriceList pl, M_PriceList_Version plv "
			+ "WHERE pl.M_PriceList_ID=plv.M_PriceList_ID"
			+ " AND plv.IsActive='Y'"
			+ " AND pl.M_PriceList_ID=? "					//	1
			+ "ORDER BY plv.ValidFrom DESC";
		//	find newest one
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_PriceList_ID);
			rs = pstmt.executeQuery();
			while (rs.next() && retValue == 0)
			{
				Timestamp plDate = rs.getTimestamp(2);
				if (!priceDate.before(plDate))
					retValue = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return retValue;
	}	//	findPLV
	
	/**
	 *	Set Warehouse
	 *
	 * 	@param M_Warehouse_ID warehouse
	 */
	protected void setWarehouse(int M_Warehouse_ID)
	{
		for(WEditor editor : editors) {
			if (editor.getGridField() != null && editor.getGridField().getColumnName().equals("M_Warehouse_ID")) {
				editor.setValue(M_Warehouse_ID);
				Env.setContext(infoContext, p_WindowNo, "M_Warehouse_ID", M_Warehouse_ID);
				Env.setContext(infoContext, p_WindowNo, Env.TAB_INFO, "M_Warehouse_ID", Integer.toString(M_Warehouse_ID));
				return;
			}
		}
	}	//	setWarehouse

	/**
	 *	Set PriceList
	 *
	 * @param M_PriceList_Version_ID price list
	 */
	protected void setPriceListVersion(int M_PriceList_Version_ID)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("M_PriceList_Version_ID=" + M_PriceList_Version_ID);
		
		for(WEditor editor : editors) {
			if (editor.getGridField() != null && editor.getGridField().getColumnName().equals("M_PriceList_Version_ID")) {
				editor.setValue(M_PriceList_Version_ID);
				Env.setContext(infoContext, p_WindowNo, "M_PriceList_Version_ID", M_PriceList_Version_ID);
				Env.setContext(infoContext, p_WindowNo, Env.TAB_INFO, "M_PriceList_Version_ID", Integer.toString(M_PriceList_Version_ID));
				return;
			}
		}
		
		if (log.isLoggable(Level.FINE))
			log.fine("NOT found");
	}	//	setPriceListVersion
	
	/**
	 * 	Refresh Query
	 */
	protected void refresh(int M_Warehouse_ID, int M_PriceList_Version_ID)
	{
		int m_M_Product_ID = getSelectedRowKey();
		String sql = m_sqlWarehouse;
		if (log.isLoggable(Level.FINEST)) log.finest(sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			rs = pstmt.executeQuery();
			warehouseTbl.loadTable(rs);
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		
		//IDEMPIERE-337
		sql = m_sqlProductprice;
		if (log.isLoggable(Level.FINEST)) log.finest(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			rs = pstmt.executeQuery();
			productpriceTbl.loadTable(rs);
		} catch (Exception e) {
			log.log(Level.WARNING, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	}	//	refresh
	
	

	public boolean isShowDetailATP() {
		return chbShowDetailAtp.isChecked();
	}

	@Override
	protected void showHistory() {
		log.info("");
		Integer M_Product_ID = getSelectedRowKey();
		if (M_Product_ID == null)
			return;
		int M_Warehouse_ID = getSelectedWarehouseId();
		InvoiceHistory ih = new InvoiceHistory (this, 0, M_Product_ID.intValue(), M_Warehouse_ID);
		ih.setVisible(true);
		ih = null;
	}

	@Override
	protected boolean hasHistory() {
		return true;
	}

	@Override
	protected void saveSelectionDetail() {
        int row = contentPanel.getSelectedRow();
        if (row == -1)
            return;

		super.saveSelectionDetail();

		Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Warehouse_ID", String.valueOf(m_M_Warehouse_ID));
        //  publish for Callout to read
        Integer ID = getSelectedRowKey();
        Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Product_ID", ID == null ? "0" : ID.toString());
	}

	@Override
	protected void prepareTable(ColumnInfo[] layout, String from, String where,
			String orderBy) {
		if (Util.isEmpty(orderBy) && getSelectedWarehouseId() > 0)
		{
			orderBy = "QtyAvailable DESC";
		}
		super.prepareTable(layout, from, where, orderBy);
	}

	@Override
	protected void executeQuery() {
		super.executeQuery();
		if (m_PAttributeButton != null)
			m_PAttributeButton.setEnabled(false);
	}
	
	@Override
	protected void updateSubcontent() {
		super.updateSubcontent();
		int row = contentPanel.getSelectedRow();
		if (row < 0){
			if (warehouseTbl != null && warehouseTbl.getModel() != null)
				warehouseTbl.getModel().clear();
			
			if (substituteTbl != null && substituteTbl.getModel() != null)
				substituteTbl.getModel().clear();
			
			if (relatedTbl != null && relatedTbl.getModel() != null)
				relatedTbl.getModel().clear();
			
			if (m_tableAtp != null && m_tableAtp.getModel() != null)
				m_tableAtp.getModel().clear();
			
			if (productpriceTbl != null && productpriceTbl.getModel() != null)
				productpriceTbl.getModel().clear();
			
			if (fieldDescription != null)
				fieldDescription.setText("");
		}
		
	}
}
