

package eone.webui.acct;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.compiere.report.RModel;
import org.compiere.report.RModelExcelExporter;
import org.compiere.tools.FileUtil;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.North;
import org.zkoss.zul.Paging;
import org.zkoss.zul.South;

import eone.base.model.MColumn;
import eone.base.model.MLookup;
import eone.base.model.MLookupFactory;
import eone.webui.ClientInfo;
import eone.webui.apps.AEnv;
import eone.webui.component.Button;
import eone.webui.component.Column;
import eone.webui.component.Columns;
import eone.webui.component.Datebox;
import eone.webui.component.Grid;
import eone.webui.component.Label;
import eone.webui.component.ListModelTable;
import eone.webui.component.Listbox;
import eone.webui.component.Row;
import eone.webui.component.Rows;
import eone.webui.component.Textbox;
import eone.webui.component.VerticalBox;
import eone.webui.component.WListItemRenderer2;
import eone.webui.component.Window;
import eone.webui.editor.WSearchEditor;
import eone.webui.editor.WTableDirEditor;
import eone.webui.event.DialogEvents;
import eone.webui.panel.ADForm;
import eone.webui.panel.CustomForm;
import eone.webui.panel.IFormController;
import eone.webui.panel.InfoPanel;
import eone.webui.session.SessionManager;
import eone.webui.theme.ThemeManager;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.FDialog;

/**
 * 14/08/2020
 * @author Admin
 *
 */

public class WAcctViewer extends Window implements IFormController, EventListener<Event>
{
	
	private static final long serialVersionUID = 3440375640756094077L;

	private static final String TITLE = "Posting";

	private static final int PAGE_SIZE = 50;

	/** State Info          */
	private WAcctViewerData	m_data = null;
	
	private Label lDate = new Label();
	private Datebox selDateFrom = new Datebox();
	private Datebox selDateTo = new Datebox();
	
	private Label lAcct = new Label();
	private Textbox selAcct = new Textbox();

	private Label lDocNo = new Label();
	private Textbox selDocNo = new Textbox();
	
	private Label lOrg = new Label();
	private WTableDirEditor selOrg;
	private Label lDept = new Label();
	private WTableDirEditor selDept;
	
	
	private Label lBPartner = new Label();
	private WSearchEditor selBPartner;
	
	private Label lProduct = new Label();
	private WSearchEditor selProduct;
	private Label lWarehouse = new Label();
	private WTableDirEditor selWarehouse;
	private Label lAsset = new Label();
	private WTableDirEditor selAsset;	
	
	private Label lCost = new Label();
	private WTableDirEditor selCost;	
	private Label lRevenue = new Label();
	private WTableDirEditor selRevenue;	
	
	
	private Button bQuery = new Button();
	private Button bExport = new Button();
	
	private Label statusLine = new Label();
	
	private Borderlayout boderLayout = new Borderlayout();

	private Listbox table = new Listbox();
	private Paging paging = new Paging();

	private VerticalBox selectionPanel = new VerticalBox();
	private int m_windowNo;

	private ArrayList<ArrayList<Object>> m_queryData;

	private South pagingPanel;

	private Borderlayout resultPanel;

	private RModel m_rmodel;
	
	private static final CLogger log = CLogger.getCLogger(WAcctViewer.class);


	public WAcctViewer()
	{
		this (0, 0, 0);
	} 

	

	public WAcctViewer(int AD_Client_ID, int AD_Table_ID, int Record_ID)
	{
		
		super ();
		log.info("AD_Table_ID=" + AD_Table_ID + ", Record_ID=" + Record_ID);

		m_windowNo = SessionManager.getAppDesktop().registerWindow(this);
		m_data = new WAcctViewerData (Env.getCtx(), m_windowNo, AD_Client_ID, AD_Table_ID);

		try
		{
			init(AD_Table_ID, Record_ID, "");
			dynInit (AD_Table_ID, Record_ID, "");
			setAttribute(MODE_KEY, MODE_EMBEDDED);
			setAttribute(Window.INSERT_POSITION_KEY, Window.INSERT_NEXT);
			if (AD_Table_ID > 0 && Record_ID > 0) {
				AEnv.showWindow(this);
			}
			
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}
	


	public WAcctViewer(String sqlZoomLogic)
	{
		
		super ();
		
		m_windowNo = SessionManager.getAppDesktop().registerWindow(this);
		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_windowNo, "AD_Client_ID");
		m_data = new WAcctViewerData (Env.getCtx(), m_windowNo, AD_Client_ID, 0, sqlZoomLogic);

		try
		{
			init(0, 0, sqlZoomLogic);
			dynInit (0, 0, sqlZoomLogic);
			setAttribute(MODE_KEY, MODE_EMBEDDED);
			setAttribute(Window.INSERT_POSITION_KEY, Window.INSERT_NEXT);
			AEnv.showWindow(this);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}

	private North north = null;
	private void init(int AD_Table_ID, int Record_ID, String zoomLogic) throws Exception
	{
		form = new CustomForm();
		ZKUpdateUtil.setHflex(selectionPanel, "1");
		
		m_gColHistory = new ArrayList<String>();
		m_filterHistory = new ArrayList<String>();
		Grid grid = new Grid();
		ZKUpdateUtil.setHflex(grid, "1");
		grid.setSclass("grid-layout");
		
		selectionPanel.appendChild(grid);
		
		Columns columns = new Columns();
		grid.appendChild(columns);
		
		Rows rows = grid.newRows();
		Row row = rows.newRow();
		
		//Group 1
		Column column = new Column();
		ZKUpdateUtil.setWidth(column, "10%");
		columns.appendChild(column);
		column = new Column();
		ZKUpdateUtil.setWidth(column, "23%");
		columns.appendChild(column);
		
		//Gruop 2
		column = new Column();
		ZKUpdateUtil.setWidth(column, "10%");
		columns.appendChild(column);		
		column = new Column();
		ZKUpdateUtil.setWidth(column, "23%");
		columns.appendChild(column);
		
		//Group 3
		column = new Column();
		ZKUpdateUtil.setWidth(column, "10%");
		columns.appendChild(column);		
		column = new Column();
		ZKUpdateUtil.setWidth(column, "23%");
		columns.appendChild(column);
		
		//Row 1
		row = rows.newRow();

		//Col 1.1: DocNo
		lDocNo.setValue(Msg.translate(Env.getCtx(), "DocumentNo"));
		row.appendChild(lDocNo);
		selDocNo.setWidth("90%");
		row.appendChild(selDocNo);
		
		//Col 1.2: Date
		Hlayout hlayout = new Hlayout();
		lDate.setValue(Msg.translate(Env.getCtx(), "DateAcct"));
		row.appendChild(lDate);
		hlayout = new Hlayout();
		ZKUpdateUtil.setWidth(hlayout, "100%");
		hlayout.appendChild(selDateFrom);		
		hlayout.appendChild(new Label(" - "));
		hlayout.appendChild(selDateTo);
		row.appendChild(hlayout);
		
		//Col 1.3: Account
		lAcct.setValue(Msg.translate(Env.getCtx(), "Account_ID"));
		row.appendChild(lAcct);
		selAcct.setWidth("90%");
		row.appendChild(selAcct);
		
		
		//row 2
		row = rows.newRow();
		//Col 2.1: BPartner
		lBPartner.setValue(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		row.appendChild(lBPartner);
		MLookup mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("C_BPartner", "C_BPartner_ID"), DisplayType.Search);
		selBPartner = new WSearchEditor("C_BPartner_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selBPartner.getComponent(), "90%");
		row.appendChild(selBPartner.getComponent());

		//Col 2.2 Organization
		row.appendChild(lOrg);
		lOrg.setValue(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("AD_Org", "AD_Org_ID"), DisplayType.Table);
		selOrg = new WTableDirEditor("AD_Org_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selOrg.getComponent(), "90%");
		row.appendChild(selOrg.getComponent());
		
		//Col 2.3 Department
		row.appendChild(lDept);
		lDept.setValue(Msg.translate(Env.getCtx(), "AD_Department_ID"));
		mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("AD_Department", "AD_Department_ID"), DisplayType.Table);
		selDept = new WTableDirEditor("AD_Department_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selDept.getComponent(), "90%");
		row.appendChild(selDept.getComponent());
		
		//row 3
		row = rows.newRow();
		//Col 3.1: Product
		lProduct.setValue(Msg.translate(Env.getCtx(), "M_Product_ID"));
		row.appendChild(lProduct);
		mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("M_Product", "M_Product_ID"), DisplayType.Search);
		selProduct = new WSearchEditor("M_Product_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selProduct.getComponent(), "90%");
		row.appendChild(selProduct.getComponent());

		//Col 3.2 Warehouse
		row.appendChild(lWarehouse);
		lWarehouse.setValue(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
		mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("M_Warehouse", "M_Warehouse_ID"), DisplayType.Table);
		selWarehouse = new WTableDirEditor("M_Warehouse_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selWarehouse.getComponent(), "90%");
		row.appendChild(selWarehouse.getComponent());
		
		//Col 3.3 Asset
		row.appendChild(lAsset);
		lAsset.setValue(Msg.translate(Env.getCtx(), "A_Asset_ID"));
		mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("A_Asset", "A_Asset_ID"), DisplayType.Table);
		selAsset = new WTableDirEditor("A_Asset_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selAsset.getComponent(), "90%");
		row.appendChild(selAsset.getComponent());
		
		//row 4
		row = rows.newRow();
		//Col 4.1: TypeCost
		lCost.setValue(Msg.translate(Env.getCtx(), "C_TypeCost_ID"));
		row.appendChild(lCost);
		mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("C_TypeCost", "C_TypeCost_ID"), DisplayType.Table);
		selCost = new WTableDirEditor("C_TypeCost_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selCost.getComponent(), "90%");
		row.appendChild(selCost.getComponent());

		//Col 4.2 TypeRevenue
		row.appendChild(lRevenue);
		lRevenue.setValue(Msg.translate(Env.getCtx(), "C_TypeRevenue_ID"));
		mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("C_TypeRevenue", "C_TypeRevenue_ID"), DisplayType.Table);
		selRevenue = new WTableDirEditor("C_TypeRevenue_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selRevenue.getComponent(), "90%");
		row.appendChild(selRevenue.getComponent());
				
		grid = new Grid();
		grid.setSclass("grid-layout");
		ZKUpdateUtil.setHflex(grid, "1");
		columns = new Columns();
		grid.appendChild(columns);
		column = new Column();
		ZKUpdateUtil.setWidth(column, "70%");
		columns.appendChild(column);
		column = new Column();
		ZKUpdateUtil.setWidth(column, "30%");
		columns.appendChild(column);
	
		north = new North();
		north.appendChild(selectionPanel);
		
		bQuery.setImage(ThemeManager.getThemeResource("images/Refresh16.png"));
		bQuery.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")));
		bQuery.addEventListener(Events.ON_CLICK, this);

		bExport.setImage(ThemeManager.getThemeResource("images/Export16.png"));
		bExport.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Export")));
		bExport.addEventListener(Events.ON_CLICK, this);
		bExport.setVisible(false);

		Hbox hbox = new Hbox();
		ZKUpdateUtil.setWidth(hbox, "100%");
		Cell cellStatus = new Cell();
		cellStatus.appendChild(statusLine);
		cellStatus.setStyle("border:none");
		Cell cellLabel = new Cell();
		lbTotal = new Label();
		cellLabel.appendChild(lbTotal);
		cellLabel.setStyle("border:none");
		Cell cellButton = new Cell();
		cellButton.appendChild(bExport);
		cellButton.appendChild(bQuery);
		cellButton.setAlign("right");
		cellButton.setStyle("border:none");
		ZKUpdateUtil.setWidth(cellStatus, "40%");
		ZKUpdateUtil.setWidth(cellLabel, "20%");
		ZKUpdateUtil.setWidth(cellButton, "40%");
		hbox.appendChild(cellStatus);
		hbox.appendChild(cellLabel);
		hbox.appendChild(cellButton);
		

		resultPanel = new Borderlayout();
		resultPanel.setStyle("position: absolute");
		ZKUpdateUtil.setWidth(resultPanel, "99%");
		ZKUpdateUtil.setHeight(resultPanel, "99%");
		
		createGrid();
		
		ZKUpdateUtil.setHflex(resultPanel, "1");
		Center center = new Center();
		center.appendChild(resultPanel);
		
		South south = new South();
		south.setStyle("background-color: transparent");
		ZKUpdateUtil.setHeight(south, "36px");
		south.appendChild(hbox);
		
		if (AD_Table_ID == 0 && Record_ID == 0) {
			boderLayout.appendChild(north);
			north.setCollapsible(true);
			north.setSplittable(true);		
			
		}
		if (zoomLogic != "") {
			north.setOpen(false);
		}
		north.setCtrlKeys(ctrlKeys);
		north.addEventListener(Events.ON_CTRL_KEY, this);
		north.addEventListener(Events.ON_CLICK, this);
		
		//boderLayout.appendChild(north);
		
		boderLayout.appendChild(center);
		boderLayout.appendChild(south);
		
		ZKUpdateUtil.setHflex(boderLayout, "1");
		ZKUpdateUtil.setVflex(boderLayout, "1");
		
		//boderLayout.setParent(this);
		form.appendChild(boderLayout);
		ZKUpdateUtil.setHeight(boderLayout, "100%");
		ZKUpdateUtil.setWidth(boderLayout, "100%");
		boderLayout.setStyle("background-color: transparent; margin: 0; position: absolute; padding: 0;");
		this.appendChild(form);
		this.setTitle(Msg.getMsg(Env.getCtx(), TITLE));
		this.setClosable(true);
		this.setStyle("position: absolute; width: 100%; height: 100%;");
		this.setSizable(true);
		this.setMaximizable(true);
	}
	
	String ctrlKeys = "#f6#f7#f12"                	// Fn
            + "^g^l^r"                                // Ctrl+ 
            +	"^2^4^6^8"								// Ctrl+ 
            + "#left#right#up#down";
	private void createGrid() {
		Center resultCenter = new Center();
		resultPanel.appendChild(resultCenter);
		ZKUpdateUtil.setHflex(table, "1");
		ZKUpdateUtil.setVflex(table, true);
		resultCenter.appendChild(table);
		ZKUpdateUtil.setHflex(table, "1");
		table.addEventListener(Events.ON_DOUBLE_CLICK, this);
		table.addEventListener(Events.ON_CLICK, this);
		table.addEventListener(Events.ON_SELECT, this);
		table.setCtrlKeys(ctrlKeys);
		table.addEventListener(Events.ON_CTRL_KEY, this);
		if (ClientInfo.isMobile())
			table.setSizedByContent(true);

		pagingPanel = new South();
		resultPanel.appendChild(pagingPanel);
		pagingPanel.appendChild(paging);

		ZKUpdateUtil.setHflex(resultPanel, "1");
		resultPanel.setStyle("position: relative");

		paging.addEventListener("onPaging", this);
		paging.setAutohide(true);
		paging.setDetailed(true);

	}
	
	private void dynInit (int AD_Table_ID, int Record_ID, String zoomLogic)
	{

		statusLine.setValue(" " + Msg.getMsg(Env.getCtx(), "ViewerOptions"));

		boolean haveDoc = (AD_Table_ID != 0 && Record_ID != 0);
		if (haveDoc)
		{
			m_data.Record_ID = Record_ID;
			actionQuery();
		}
		if (zoomLogic != "") {
			m_data.sqlZoomLogic = zoomLogic;
			actionQuery();
		}

	}

	
	public void dispose()
	{
		m_data.dispose();
		m_data = null;
		this.detach();
	} // dispose;

	

	public void stateChanged()
	{
		bExport.setVisible(true);
	}  

	

	public void onEvent(Event e) throws Exception
	{
		Object source = e.getTarget();
		
		if (source == bQuery)
		{
			if (selOrg == null || selOrg.getValue() == null) {
				m_data.AD_Org_ID = 0;
			}
			if (selDept == null || selDept.getValue() == null) {
				m_data.AD_Department_ID = 0;
			}
			if (selBPartner == null || selBPartner.getValue() == null) {
				m_data.C_BPartner_ID = 0;
			}
			if (selProduct == null || selProduct.getValue() == null) {
				m_data.M_Product_ID = 0;
			}
			if (selWarehouse == null || selWarehouse.getValue() == null) {
				m_data.M_Warehouse_ID = 0;
			}
			if (selAsset == null || selAsset.getValue() == null) {
				m_data.A_Asset_ID = 0;
			}
			if (selCost == null || selCost.getValue() == null) {
				m_data.C_TypeCost_ID = 0;
			}
			if (selRevenue == null || selRevenue.getValue() == null) {
				m_data.C_TypeRevenue_ID = 0;
			}
			actionQuery();
			stateChanged();
		}
		else if  (source == bExport)
			actionExport();
		
		else if (source instanceof Button)
			actionButton((Button)source);
		else if (source == paging)
		{
			int pgno = paging.getActivePage();
			int start = pgno * PAGE_SIZE;
			int end = start + PAGE_SIZE;
			if ( end > paging.getTotalSize())
				end = paging.getTotalSize();
			List<ArrayList<Object>> list = m_queryData.subList(start, end);
			ListModelTable model = new ListModelTable(list);
			table.setModel(model);
		}
		else if (Events.ON_CLICK.equals(e.getName()) && source instanceof Listbox && source == table) {
			//Display Sum
			Object objSum = table.getAttribute("total");
			if (objSum != null) {
				BigDecimal total = (BigDecimal)objSum;
				DecimalFormat formatter = new DecimalFormat("###,##0.0000");
				lbTotal.setText("Sum: " + formatter.format(total.doubleValue()));
			}else {
				lbTotal.setText("");
			}
		}
		else if (Events.ON_DOUBLE_CLICK.equals(e.getName()) && source instanceof Listbox && source == table) {
			if (currentQuery == 0)
				actionZoom();
			else 
				doFilter();//Mo ban ghi khi Ctrl + G
		}else if (Events.ON_CTRL_KEY.equals(e.getName()) && source instanceof Listbox && source == table) {
			if (e instanceof KeyEvent) {
				doOnKey(e);
			} 
		}
	} // onEvent
	
	private void actionQuery()
	{

		StringBuilder para = new StringBuilder();
		
		if (m_data.AD_Table_ID != 0 && m_data.Record_ID != 0)
		{
			para.append(", AD_Table_ID=").append(m_data.AD_Table_ID)
			.append(", Record_ID=").append(m_data.Record_ID);
		}
		else
		{
			//Xem tu tim kiem
			m_data.AD_Org_ID = 0;
	
			m_data.DateFrom = selDateFrom.getValue() != null
					? new Timestamp(selDateFrom.getValue().getTime()) : null;
			para.append(", DateFrom=").append(m_data.DateFrom);
			m_data.DateTo = selDateTo.getValue() != null
				? new Timestamp(selDateTo.getValue().getTime()) : null;
			para.append(", DateTo=").append(m_data.DateTo);
	
			
			if (selOrg != null && selOrg.getValue() != null) {
				m_data.AD_Org_ID = Integer.parseInt(selOrg.getValue().toString());
				para.append(", AD_Org_ID=").append(m_data.AD_Org_ID);
			}
			
			if (selDept != null && selDept.getValue() != null) {
				m_data.AD_Department_ID = Integer.parseInt(selDept.getValue().toString());
			}
			
			if (selBPartner != null && selBPartner.getValue() != null) {
				m_data.C_BPartner_ID = Integer.parseInt(selBPartner.getValue().toString());
			}
			
			if (selAcct != null && selAcct.getValue() != null) {
				m_data.Account = selAcct.getValue();
			}
			
			if (selDocNo != null && selDocNo.getValue() != null) {
				m_data.DocNo = selDocNo.getValue();
			}
			
			if (selProduct != null && selProduct.getValue() != null) {
				m_data.M_Product_ID = Integer.parseInt(selProduct.getValue().toString());
			}
			
			if (selWarehouse != null && selWarehouse.getValue() != null) {
				m_data.M_Warehouse_ID = Integer.parseInt(selWarehouse.getValue().toString());
			}
			
			if (selAsset != null && selAsset.getValue() != null) {
				m_data.A_Asset_ID = Integer.parseInt(selAsset.getValue().toString());
			}
			
			//
			Iterator<String> it = m_data.whereInfo.values().iterator();
			while (it.hasNext())
				para.append(", ").append(it.next());
		}
		bQuery.setEnabled(false);
		statusLine.setValue(" " + Msg.getMsg(Env.getCtx(), "Processing"));

		if (log.isLoggable(Level.CONFIG)) log.config(para.toString());

		stateChanged();

		//  Set TableModel with Query
		// Add into history
		m_filterHistory = new ArrayList<String>();
		m_gColHistory = new ArrayList<String>();
		m_filterHistory.add("");
		m_gColHistory.add("");
		currentQuery = 0;
		
		m_rmodel = m_data.query();
		m_queryData = m_rmodel.getRows();
		
		finishActionQuery();
	}   //  actionQuery
	
	private int totalPage = 0;
	
	private void finishActionQuery() {
		List<ArrayList<Object>> list = null;
		paging.setPageSize(PAGE_SIZE);
		if (m_queryData.size() > PAGE_SIZE)
		{
			list = m_queryData.subList(0, PAGE_SIZE);
			pagingPanel.setVisible(true);
		}
		else
		{
			list = m_queryData;
			pagingPanel.setVisible(false);
		}
		
		paging.setTotalSize(m_queryData.size());
		paging.setActivePage(0);
		totalPage = paging.getTotalSize()/PAGE_SIZE;
		if (paging.getTotalSize() % PAGE_SIZE != 0) {
			totalPage ++;
		}
		
		int nPostingType = -1;
		int nAutoPosted = -1;
		ListModelTable listmodeltable = new ListModelTable(list);

		if (table.getListhead() == null)
		{
			Listhead listhead = new Listhead();
			listhead.setSizable(true);

			for (int i = 0; i < m_rmodel.getColumnCount(); i++)
			{
				Listheader listheader = new Listheader(Msg.getMsg(Env.getCtx(), m_rmodel.getColumnName(i)));
				listheader.setTooltiptext(m_rmodel.getColumnName(i));
				if ("AD_Table_ID".equals(m_rmodel.getRColumn(i).getColumnName())) 
				{
					listheader.setVisible(false);
				}
				else if ("Record_ID".equals(m_rmodel.getRColumn(i).getColumnName()))
				{
					listheader.setVisible(false);
				}
				else if ("Fact_Acct_ID".equals(m_rmodel.getRColumn(i).getColumnName()))
				{
					listheader.setVisible(false);
				}
				
				listhead.appendChild(listheader);
			}

			table.appendChild(listhead);
		}
		// Elaine 2008/07/28
		else
		{
			Listhead listhead = table.getListhead();

			// remove existing column header
			listhead.getChildren().clear();

			// add in new column header
			for (int i = 0; i < m_rmodel.getColumnCount(); i++)
			{
				Listheader listheader = new Listheader(Msg.getMsg(Env.getCtx(), m_rmodel.getColumnName(i)));
				listheader.setTooltiptext(m_rmodel.getColumnName(i));
				if ("AD_Table_ID".equals(m_rmodel.getRColumn(i).getColumnName())) 
				{
					listheader.setVisible(false);
				}
				else if ("Record_ID".equals(m_rmodel.getRColumn(i).getColumnName()))
				{
					listheader.setVisible(false);
				}
				else if ("Fact_Acct_ID".equals(m_rmodel.getRColumn(i).getColumnName()))
				{
					listheader.setVisible(false);
				}
				listhead.appendChild(listheader);
			}
		}
		//

		table.getItems().clear();

		table.setItemRenderer(new WListItemRenderer2(nPostingType, nAutoPosted));
		table.setModel(listmodeltable);
		table.setSizedByContent(true);
		
		//Cai nay se hien thi scrollbar
		resultPanel.invalidate();
		boderLayout.invalidate();
		
		bQuery.setEnabled(true);
		statusLine.setValue(" " + Msg.getMsg(Env.getCtx(), "ViewerOptions"));
	}
	/*
	 * Add Key Event
	 */
	//private int 					   	m_cursor = 0;
	private int 						currentQuery = 0;
	private List<String>	   			m_filterHistory = null;
	private List<String>	   			m_gColHistory = null;
	
	private Label						lbTotal;
	
	
	private KeyEvent   prevKeyEvent;
	private long prevKeyEventTime = 0;
	
	private void doOnKey(Event event) throws Exception {

		KeyEvent keyEvent = (KeyEvent) event;
		long time = System.currentTimeMillis();
		if (prevKeyEvent != null && prevKeyEventTime > 0
				&& prevKeyEvent.getKeyCode() == keyEvent.getKeyCode()
				&& prevKeyEvent.getTarget() == keyEvent.getTarget()
				&& prevKeyEvent.isAltKey() == keyEvent.isAltKey()
				&& prevKeyEvent.isCtrlKey() == keyEvent.isCtrlKey()
				&& prevKeyEvent.isShiftKey() == keyEvent.isShiftKey()) {
			if ((time - prevKeyEventTime) <= 300) {
				return;
			}
		}
		prevKeyEvent = keyEvent;
		prevKeyEventTime = System.currentTimeMillis();

		int keyCode = keyEvent.getKeyCode();
		switch (keyCode) {
		case KeyEvent.F12:
			currentQuery ++;
			doFilter();
			break;
		case 71:						// Ctrl + G
			currentQuery ++;
			actionQueryGroup();
			break;
		case KeyEvent.F6:				// Back Action
			currentQuery --;
			if (currentQuery < 0) {
				currentQuery = 0;
			}
			actionQueryHistory(currentQuery);
			break;
		case KeyEvent.F7:				// Foward Action
			currentQuery ++;
			actionQueryHistory(currentQuery);
			break;
		
		case 82:// Ctrl + R				// Reload
			currentQuery = 0;
			actionQuery();
			break;
		case KeyEvent.LEFT:
			actionMoveOnGrid(keyCode);
			break;
		case KeyEvent.RIGHT:
			actionMoveOnGrid(keyCode);
			break;
		case KeyEvent.DOWN:
			actionMoveOnGrid(keyCode);
			break;
		case KeyEvent.UP:
			actionMoveOnGrid(keyCode);
			break;
		case 50://Ctrl + 2: An/Hien form tim kiem
			displayFormSearch();
			break;
		case 76:		//Ctrl + L: Last Page
			paging.setActivePage(totalPage - 1);
			break;
		case 75:		//Ctrl + F: First Page
			paging.setActivePage(0);
			break;
		default:
			break;
		}
	}
	
	private void actionMoveOnGrid(int keyCode) {
		if (keyCode == KeyEvent.UP) {
			FDialog.info(0, this, "UP");
		}
		if (keyCode == KeyEvent.DOWN) {
			FDialog.info(0, this, "DOWN");
		}
		if (keyCode == KeyEvent.LEFT) {
			FDialog.info(0, this, "LEFT");
		}
		if (keyCode == KeyEvent.RIGHT) {
			FDialog.info(0, this, "RIGHT");
		}
	}
	
	private void doFilter()
	{
		Listcell cell = (Listcell)table.getAttribute("selectedcell");
		if (cell == null)
			return ;
		int col = cell.getColumnIndex();
		Listhead head = table.getListhead();
		Listheader header = (Listheader)head.getChildren().get(col);
		String columnName = header.getTooltiptext();
		
		if (columnName == null ) 
			return ;
		
		String filterClause = null;
		
		Integer row = (Integer)table.getAttribute("rowcell");
		ArrayList<ArrayList<Object>> queryData = m_rmodel.getRows();
		ArrayList<Object> items = queryData.get(row.intValue());
		Object value = items.get(col);

		if (value instanceof String) {
			filterClause = columnName + " = '" + value + "'";
		} else if(value instanceof Integer || value instanceof Number)
		{
			filterClause = columnName + " = " + value;
		}else if(value instanceof KeyNamePair) {
			KeyNamePair val = (KeyNamePair)value;
			filterClause = columnName + " = " + val.getKey();
		}
		//History
		//xoa cac phan tu sau CurrentQuey
		for(int i = m_gColHistory.size() - 1; i >= currentQuery; i--) {
			m_gColHistory.remove(i);
			m_filterHistory.remove(i);
		}
		String gCol = m_gColHistory.get(currentQuery-1);
		//If user is groupping then they don't filter !
		if (gCol != null && gCol.length() >0) {
			return ;
		}
		String oldFilterClause = m_filterHistory.get(currentQuery-1);
		if (oldFilterClause != null && oldFilterClause.length() >0) {
			filterClause = oldFilterClause + " AND " + filterClause; 
		}
		m_gColHistory.add("");
		m_filterHistory.add(filterClause);
		actionQueryHistory(currentQuery);
	}
	
	private void actionQueryGroup()
	{
		if (currentQuery < 0) {
			return;
		}
		Listcell cell = (Listcell)table.getAttribute("selectedcell");
		if (cell == null)
			return ;
		int col = cell.getColumnIndex();
		
		String columnName = m_data.sortColumns.get(col);
		if (columnName == null ) 
			return ;
		//xoa cac phan tu sau CurrentQuey
		for(int i = m_gColHistory.size() - 1; i >= currentQuery; i--) {
			m_gColHistory.remove(i);
			m_filterHistory.remove(i);
		}
		// Add to history
		String gCol = m_gColHistory.get(currentQuery-1);
		//If user is groupping then they don't group !
		if (gCol != null && gCol.length() >0) {
			return ;
		}
		String filterClause = m_filterHistory.get(currentQuery-1);
		if (filterClause != null && filterClause.length() > 0) {
			m_filterHistory.add(filterClause);
		} else {
			m_filterHistory.add("");
		}	
		m_gColHistory.add(columnName);
		
		actionQueryHistory(currentQuery);
	
	}
	
	private void actionQueryHistory(int action)
	{
		if (currentQuery < 0) {
			return;
		}
		// Get filterClause & Group Column in history
		String filterClause = m_filterHistory.get(action);
		String gCol			= m_gColHistory.get(action);
		
		m_data.setFilterClause(filterClause);
		
		if (gCol != null && gCol.length() > 0) {
			if (gCol.equals("C_Currency_ID") 
			 || gCol.equals("CurrencyRate") 
			 || gCol.equals("PriceActual") 
			 || gCol.equals("Qty") 
			 || gCol.equals("AmountConvert")) {
				return;
			}
			m_rmodel = m_data.queryGroup(gCol);
		} else {
			gCol = "";
			m_rmodel = m_data.query();
		}
		
		m_queryData = m_rmodel.getRows();
		
		finishActionQuery();
		
	}
	
	public void displayFormSearch() {
		if (north.isOpen()) 
			north.setOpen(false);
		else
			north.setOpen(true);
	}
	
	/*
	 * End add key Event
	 */
	
	
	private void actionExport() {
		if (m_rmodel != null && m_rmodel.getRowCount() > 0) {
			RModelExcelExporter exporter = new RModelExcelExporter(m_rmodel);
			File file;
			try {
				file = new File(FileUtil.getTempMailName(Msg.getMsg(Env.getCtx(), TITLE), ".xls"));
				exporter.export(file, Env.getLanguage(Env.getCtx()));
				Filedownload.save(file, "application/vnd.ms-excel");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}			
		}
		
	}



	

	
	private void actionButton(final Button button) throws Exception
	{
		final String keyColumn = button.getName();
		log.info(keyColumn);
		String whereClause = "(IsSummary='N' OR IsSummary IS NULL)";
		String lookupColumn = keyColumn;


		final String tableName = lookupColumn.substring(0, lookupColumn.length()-3);

		final InfoPanel info = InfoPanel.create(m_data.WindowNo, tableName, lookupColumn, "", false, whereClause);

		if (!info.loadedOK())
		{
			button.setLabel("");
			m_data.whereInfo.put(keyColumn, "");
			return;
		}

		info.setVisible(true);
		final String lookupColumnRef = lookupColumn;
		info.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				String selectSQL = info.getSelectedSQL();       //  C_Project_ID=100 or ""
				Integer key = (Integer)info.getSelectedKey();

				if (selectSQL == null || selectSQL.length() == 0 || key == null)
				{
					button.setLabel("");
					m_data.whereInfo.put(keyColumn, "");    //  no query
					return;
				}

				//  Save for query

				if (log.isLoggable(Level.CONFIG)) log.config(keyColumn + " - " + key);
				
				m_data.whereInfo.put(keyColumn, keyColumn + "=" + key.intValue());
				//  Display Selection and resize
				button.setLabel(m_data.getButtonText(tableName, lookupColumnRef, selectSQL));
				//pack();
				
			}
		});
		AEnv.showWindow(info);
		
	} // actionButton

	
	private void actionZoom()
	{
		int selected = table.getSelectedIndex();
		if(selected == -1) return;

		int tableWindow = m_rmodel.getColumnIndex("AD_Window_ID");
		int tableIdColumn = m_rmodel.getColumnIndex("AD_Table_ID");
		int recordIdColumn = m_rmodel.getColumnIndex("Record_ID");
		ListModelTable model = (ListModelTable) table.getListModel();
		KeyNamePair tabknp = (KeyNamePair) model.getDataAt(selected, tableIdColumn);
		KeyNamePair wdknp = (KeyNamePair) model.getDataAt(selected, tableWindow);
		Integer recint = (Integer) model.getDataAt(selected, recordIdColumn);
		if (tabknp != null && recint != null && wdknp != null) {
			int AD_Table_ID = tabknp.getKey();
			int Record_ID = recint.intValue();
			int AD_Window_ID = wdknp.getKey();

			AEnv.zoom(AD_Table_ID, Record_ID, AD_Window_ID);
		}
	}

	private CustomForm form;
	@Override
	public ADForm getForm() {
		return form;
	}
}
