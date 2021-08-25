
package eone.webui.apps;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.compiere.minigrid.IDColumn;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.MSort;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.West;

import eone.base.model.I_C_BPartner;
import eone.base.model.I_C_ElementValue;
import eone.base.model.MColumn;
import eone.base.model.MFactAcct;
import eone.base.model.MLookup;
import eone.base.model.MLookupFactory;
import eone.webui.component.Button;
import eone.webui.component.Column;
import eone.webui.component.Columns;
import eone.webui.component.ComboItem;
import eone.webui.component.ConfirmPanel;
import eone.webui.component.Datebox;
import eone.webui.component.Grid;
import eone.webui.component.GridFactory;
import eone.webui.component.Label;
import eone.webui.component.ListHead;
import eone.webui.component.ListHeader;
import eone.webui.component.ListboxFactory;
import eone.webui.component.Row;
import eone.webui.component.Rows;
import eone.webui.component.Textbox;
import eone.webui.component.WListbox;
import eone.webui.component.Window;
import eone.webui.editor.WSearchEditor;
import eone.webui.event.WTableModelEvent;
import eone.webui.event.WTableModelListener;
import eone.webui.panel.ADForm;
import eone.webui.panel.CustomForm;
import eone.webui.panel.IFormController;
import eone.webui.panel.StatusBarPanel;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.FDialog;


public class WupdateLongterm extends Window implements IFormController, EventListener<Event>, WTableModelListener
{

	private static final long serialVersionUID = -3464399582503864350L;
	
	private boolean				p_initOK = false;
	
	private WListbox 			selectedTable = ListboxFactory.newDataTable();
	
	
	private StatusBarPanel 		selectionStatusBar = new StatusBarPanel();
	private Button				btnSearch;	
	
	private Label				lbDate;
	private Datebox				fieldFromDate;
	private Datebox				fieldToDate;
	
	private Label lAcct = new Label();
	private Textbox selAcct = new Textbox();
	
	private Label lBPartner = new Label();
	private WSearchEditor selBPartner;
	
	private Combobox			cbListDuration;
	private Label				lbListDuration;
	
	private ConfirmPanel 		confirmPanel;
	
	private StringBuilder		m_query = new StringBuilder();
	private WInfo_Column[] 		m_selectedLayout = null;	
	
	private Hashtable<Integer, List<Object>> 			m_selected = new Hashtable<Integer, List<Object>>(); 
	private Hashtable<Integer, List<Object>> 			m_existSelected = new Hashtable<Integer, List<Object>>(); 
	
	
	public WupdateLongterm()
	{
		super();
		
		try
		{
			
			initComponent();
			jbInit ();
			prepareTable();
			p_initOK = dynInit();
		}
		catch (Exception ex)
		{
			p_initOK = false;
		}
	}	//	init
	
	private void prepareTable()
	{
		
		String sqlFrom = 
				" Fact_Acct "
				+ " LEFT JOIN C_ElementValue adr ON Fact_Acct.Account_Dr_ID = adr.C_ElementValue_ID "
				+ " LEFT JOIN C_ElementValue acr ON Fact_Acct.Account_Cr_ID = acr.C_ElementValue_ID "
				+ " LEFT JOIN C_BPartner pdr ON Fact_Acct.C_BPartner_Dr_ID = pdr.C_BPartner_ID "
				+ " LEFT JOIN C_BPartner pcr ON Fact_Acct.C_BPartner_Cr_ID = pcr.C_BPartner_ID ";
		
		StringBuilder m_queryFrom = new StringBuilder(sqlFrom);
		String m_queryAlias = "SELECT ";
		
		
		int m_selectionColumns = 11;
		int m_infoColumn = 0;		
			
		m_infoColumn = 0;
		m_selectedLayout = new WInfo_Column[m_selectionColumns];
		m_selectedLayout[m_infoColumn++] = new WInfo_Column("", MFactAcct.Table_Name + "." + MFactAcct.COLUMNNAME_Fact_Acct_ID, IDColumn.class);		
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_DocumentNo), MFactAcct.Table_Name + "." + MFactAcct.COLUMNNAME_DocumentNo, String.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_DateAcct), MFactAcct.Table_Name + "." + MFactAcct.COLUMNNAME_DateAcct, Date.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_Amount), MFactAcct.Table_Name + "." + MFactAcct.COLUMNNAME_Amount, BigDecimal.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_AmountConvert), MFactAcct.Table_Name + "." + MFactAcct.COLUMNNAME_AmountConvert, BigDecimal.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_ListDuration), MFactAcct.Table_Name +  "." + MFactAcct.COLUMNNAME_ListDuration, String.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_Description), MFactAcct.Table_Name +  "." + MFactAcct.COLUMNNAME_Description, String.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_Account_Dr_ID), "adr" + "." + I_C_ElementValue.COLUMNNAME_Value, String.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_Account_Cr_ID), "acr" + "." + I_C_ElementValue.COLUMNNAME_Value, String.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_C_BPartner_Dr_ID), "pdr" + "." + I_C_BPartner.COLUMNNAME_Name, String.class);
		m_selectedLayout[m_infoColumn++] = new WInfo_Column(Msg.translate(Env.getCtx(), MFactAcct.COLUMNNAME_C_BPartner_Cr_ID), "pcr" + "." + I_C_BPartner.COLUMNNAME_Name, String.class);
		
		WListbox[] tables = new WListbox[] {this.selectedTable};
		Object[] layouts = new Object[] {m_selectedLayout};
		
		for (int tableIndex=0; tableIndex<tables.length; tableIndex++)
		{
			WListbox table = this.selectedTable;
			WInfo_Column[] layout = (WInfo_Column[])layouts[tableIndex];
			ListHead listhead = new ListHead();
			table.appendChild(listhead);
			
			// remove existing column header
			listhead.getChildren().clear();
			listhead.setSizable(true);
			for (int i = 0; i < layout.length; i++)
			{
				if (layout[i] != null)
				{
					if (tableIndex == 0)
					{
						if (i > 0)
							m_query.append(", ");
						m_query.append(layout[i].getColSQL());
						//  adding ID column
						if (layout[i].isIDcol())
							m_query.append(",").append(layout[i].getIDcolSQL());
					}
					
					// add Header
					ListHeader listheader = new ListHeader(layout[i].getColHeader());
					int min = 4; //0;
					int colWith = layout[i].getColHeader().trim().length();
					if (colWith > 0 && colWith < min)
						colWith = min;
					if(i == 0) {
						listheader.setWidth("30px");
					}
					else
					{
						listhead.setSizable(true);
					}
					listheader.setTooltiptext(layout[i].getColHeader());
					Comparator<Object> ascComparator =  getColumnComparator(true, i);
			        Comparator<Object> dscComparator =  getColumnComparator(false, i);
 			        listheader.setSortAscending(ascComparator);
			        listheader.setSortDescending(dscComparator);

					listhead.appendChild(listheader);
					
					//  add to model
					table.addColumn(layout[i].getColHeader());					
					if (layout[i].isColorColumn())
						table.setColorColumn(i);					
				}
			}
			table.autoSize();
			
			for (int i = 0; i < layout.length; i++)
			{
				if (layout[i] != null)
				{
					table.setColumnClass(i, layout[i].getColClass(), 
							layout[i].isReadOnly(), 
							layout[i].getColHeader());
				}
			}
		}
		m_query.append(", Fact_Acct_ID ");
		m_query = new StringBuilder(m_queryAlias).append(m_query);
		m_query = m_query.append(" FROM ").append(m_queryFrom);
		
		this.selectedTable.setMultiple(true);
		this.selectedTable.setCheckmark(true);
		this.selectedTable.setMultiSelection(true);
		
	}
	
	private boolean dynInit()
	{
		setTitle(Msg.translate(Env.getCtx(), "Select Fact Account"));
		return true;
	}
	
	public boolean isInitOK()
	{
		return p_initOK;
	}	//	isInitOK
	
	private void initComponent()
	{
		lbDate = new Label(Msg.translate(Env.getCtx(), "DateAcct"));
		fieldFromDate = new Datebox();			
		fieldToDate = new Datebox();	
		
		lAcct = new Label();
		selAcct = new Textbox();
		
		lBPartner.setValue(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		MLookup mLookup = MLookupFactory.get(Env.getCtx(), 0, 0, MColumn.getColumn_ID("C_BPartner", "C_BPartner_ID"), DisplayType.Search);
		selBPartner = new WSearchEditor("C_BPartner_ID", false, false, true, mLookup);
		ZKUpdateUtil.setWidth(selBPartner.getComponent(), "90%");
		
		lbListDuration = new Label(Msg.translate(Env.getCtx(), "ListDuration"));
		
		ComboItem item = null;
		cbListDuration = new Combobox("---- Select ----");
		item =new ComboItem(Msg.translate(Env.getCtx(), "---- Select ----"), "00");
		cbListDuration.appendChild(item);
		item =new ComboItem(Msg.translate(Env.getCtx(), "ShortTeam"), "01");
		cbListDuration.appendChild(item);
		item =new ComboItem(Msg.translate(Env.getCtx(), "LongTeam"), "02");
		cbListDuration.appendChild(item);
		
		
		btnSearch = new Button(Msg.translate(Env.getCtx(), "Search"));
		btnSearch.addActionListener(this);
		confirmPanel = new ConfirmPanel(true, false, false, false, false, false);
		confirmPanel.addActionListener(Events.ON_CLICK, this);
		
		selectedTable.getModel().addTableModelListener(this);
		return;
	}
	
	
	private void jbInit () throws Exception
	{        
		form = new CustomForm();
        this.setBorder("normal");
		
		West west = new West();
		
		west.setWidth("100%");
		Borderlayout westlayout = new Borderlayout();
		westlayout.setWidth("100%");
		westlayout.setHeight("100%");
		west.appendChild(westlayout);
		//west North
		North westNorth = new North();
		westlayout.appendChild(westNorth);
		Grid grid = GridFactory.newGridLayout();
		
		Columns columns = new Columns();
		grid.appendChild(columns);
		//Group 1
		Column column = new Column();
		ZKUpdateUtil.setWidth(column, "15%");
		columns.appendChild(column);
		column = new Column();
		ZKUpdateUtil.setWidth(column, "30%");
		columns.appendChild(column);
		
		//Gruop 2
		column = new Column();
		ZKUpdateUtil.setWidth(column, "15%");
		columns.appendChild(column);		
		column = new Column();
		ZKUpdateUtil.setWidth(column, "30%");
		columns.appendChild(column);
		
		//Group 3
		column = new Column();
		ZKUpdateUtil.setWidth(column, "10%");
		columns.appendChild(column);		
		
		
		Rows rows = new Rows();
		Row row = rows.newRow();		
		Hlayout hlayout = new Hlayout();
		lbDate.setValue(Msg.translate(Env.getCtx(), "DateAcct"));
		row.appendChild(lbDate);
		hlayout = new Hlayout();
		ZKUpdateUtil.setWidth(hlayout, "100%");
		hlayout.appendChild(fieldFromDate);		
		fieldFromDate.setValue(Env.getContextAsDate(Env.getCtx(), "#FYDate"));
		hlayout.appendChild(new Label(" - "));
		hlayout.appendChild(fieldToDate);
		fieldToDate.setValue(Env.getContextAsDate(Env.getCtx(), "#LDate"));
		row.appendChild(hlayout);
		
		row.appendChild(lbListDuration);
		row.appendChild(cbListDuration);
		
		row = rows.newRow();
		
		lAcct.setValue(Msg.translate(Env.getCtx(), "Account_ID"));
		row.appendChild(lAcct);
		selAcct.setWidth("90%");
		row.appendChild(selAcct);
		
		
		row.appendChild(lBPartner);
		row.appendChild(selBPartner.getComponent());
		
		
		
		row.appendChild(btnSearch);
		rows.appendChild(row);
		
		grid.appendChild(rows);
		westNorth.appendChild(grid);
		
		// Center 
		Center wCenter = new Center();
		Borderlayout wCenterLayout = new Borderlayout();
		wCenterLayout.setWidth("100%");
		wCenterLayout.setHeight("100%");
		wCenter.appendChild(wCenterLayout);
		westlayout.appendChild(wCenter);
		
		//west south
		North wCSouth = new North();
		wCSouth.appendChild(selectedTable);
		selectedTable.addActionListener(this);
		selectedTable.setAutopaging(true);
		
		wCSouth.setHeight("100%");
		wCenterLayout.appendChild(wCSouth);
		//~West
		
		// south
		South westSouth = new South();
		//westSouth.setStyle("border: solid 1px gray");
		westSouth.appendChild(confirmPanel);
		westlayout.appendChild(westSouth);
		//~south
		
		Borderlayout mainPanel = new Borderlayout();
        mainPanel.setWidth("100%");
        mainPanel.setHeight("100%");
        //mainPanel.setStyle("border-width: 5px; position: relative");
        
		mainPanel.appendChild(west);
		form.appendChild(mainPanel);
        this.appendChild(form);
	}	//	jbInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		this.detach();
	}	//	dispose
				
	
	private boolean saveChange()
	{
		this.m_selected = new Hashtable<Integer, List<Object>>();
		
		int [] listID = selectedTable.getSelectedIndices();
		int index = 0;
		if (listID != null) {
			for( int i = 0; i < listID.length; i++) {
				List<Object> objs = new ArrayList<Object>();
				index = listID[i];
				Listitem item = selectedTable.getItemAtIndex(index);
				Listcell cell = (Listcell)item.getChildren().get(0);
				Integer value = cell.getValue();
				objs.add(value); 
				this.m_selected.put(value, objs);
			}
		}
		
		return updateFact(m_selected);
	}
	
	private boolean updateFact(Hashtable<Integer, List<Object>> objs) {
		String listID = "";
		int i = 0;
		for(Integer key : m_selected.keySet()) {
			if (i == 0)
				listID = "" + key;
			else
				listID = listID + "," + key;
			i ++;
		}
		
		String listDuration = "00";
		
		if ("01".equals(cbListDuration.getSelectedItem().getValue())) {
			listDuration = "12";
		}
		
		if ("02".equals(cbListDuration.getSelectedItem().getValue())) {
			listDuration = "13";
		}
		
		if (!listID.isEmpty()) {
			String sql = "UPDATE Fact_Acct Set ListDuration = '"+ listDuration +"' Where Fact_Acct_ID in (" + listID + ")";
			DB.executeUpdate(sql);
		}
		
		return true;
	}
	
	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void onEvent (Event event)
	{
		if (event != null)
		{
			if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_CANCEL)))
			{
				dispose();
			}
			if (event.getTarget().equals(confirmPanel.getButton(ConfirmPanel.A_OK)))
			{
				saveChange();
				dispose();
			}
			else if (event.getTarget().equals(btnSearch))
			{
				if (fieldFromDate == null || fieldFromDate.getValue() == null) {
					FDialog.warn(0, "From date is null");
					return ;
				}
				if (fieldToDate == null || fieldToDate.getValue() == null) {
					FDialog.warn(0, "To date is null");
					return ;
				}
				Timestamp fromDate = new Timestamp(fieldFromDate.getValue().getTime());
				Timestamp toDate = new Timestamp(fieldToDate.getValue().getTime());
				
				if (TimeUtil.getYearSel(fromDate) != TimeUtil.getYearSel(toDate)) {
					FDialog.warn(0, "From Date and To date not same year !");
					return ;
				}
				
				run();
			}
		}
	}	//	actionPerformed
	
	private String getSQLWhere()
	{
		
		StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
		
		whereClause.append(" AND  exists(select * from c_elementvalue where c_elementvalue_id in (Fact_Acct.account_dr_id, Fact_Acct.account_cr_id)")
		.append("AND IsDistribute = 'Y')");
		
		if (selAcct != null && selAcct.getValue() != null && !selAcct.getValue().isEmpty()) {
			whereClause.append(" AND ");
			whereClause.append(" exists(select * from c_elementvalue where c_elementvalue_id in (Fact_Acct.account_dr_id, Fact_Acct.account_cr_id)")
			.append(" AND value like '").append(selAcct.getValue()).append("%')");
		}
		
		if (selBPartner != null && selBPartner.getValue() != null) {
			whereClause.append(" AND ")
			.append(Integer.parseInt(selBPartner.getValue().toString()))
			.append(" IN (Fact_Acct.C_BPartner_Dr_ID, Fact_Acct.C_BPartner_Cr_ID)");
		}
		
		if (fieldFromDate != null && fieldFromDate.getValue() != null) {
			Timestamp fromDate = new Timestamp(fieldFromDate.getValue().getTime());
			whereClause.append(" AND Fact_Acct.DateAcct >= ").append(DB.TO_DATE(fromDate));
		}
		
		if (fieldToDate != null && fieldToDate.getValue() != null) {
			Timestamp toDate = new Timestamp(fieldToDate.getValue().getTime());
			whereClause.append(" AND Fact_Acct.DateAcct <= ").append(DB.TO_DATE(toDate));
		}
		
		if ("01".equals(cbListDuration.getSelectedItem().getValue())) {
			whereClause.append(" AND ").append(" TO_NUMBER(Fact_Acct.ListDuration, '99G999D9S')  <= 12");
		}
		
		if ("02".equals(cbListDuration.getSelectedItem().getValue())) {
			whereClause.append(" AND ").append(" TO_NUMBER(Fact_Acct.ListDuration, '99G999D9S') > 12");
		}
		
		return whereClause.toString();
	}
	
	private boolean run()
	{
		
		if (this.selectedTable == null)
			return true;
		
		//  Clear Table
		this.selectedTable.setRowCount(0);
		
		
		StringBuilder sql = new StringBuilder(m_query.toString());
		sql.append(getSQLWhere());
		
		
		
		PreparedStatement m_pstmt = null;
		ResultSet m_rs = null;
		try
		{	
			
			m_pstmt = DB.prepareStatement(sql.toString(), null);
			
			int[] lstSelectedArr = {};
			m_rs = m_pstmt.executeQuery();
			while (m_rs.next())
			{
				int row = this.selectedTable.getRowCount();
				this.selectedTable.setRowCount(row+1);
				int colOffset = 1;  //  columns start with 1
				List<Object> list = new ArrayList<Object>();
				Integer key_ID  = 0;
				for (int col = 0; col < m_selectedLayout.length; col++)
				{
					if (m_selectedLayout[col] != null)
					{						
						Object data = null;
						Class<?> c = m_selectedLayout[col].getColClass();
						int colIndex = col + colOffset;
						if (c == IDColumn.class)
						{
							data = new IDColumn(m_rs.getInt(colIndex));
							//((IDColumn)data).setSelected(false);
							key_ID = m_rs.getInt(colIndex);	
							if (m_rs.getInt("Fact_Acct_ID") > 0)
							{
								
								lstSelectedArr = Arrays.copyOf(lstSelectedArr, lstSelectedArr.length + 1);
								lstSelectedArr[lstSelectedArr.length - 1] = row;
							}
						}
						else if (c == Boolean.class)
							data = Boolean.valueOf("Y".equals(m_rs.getString(colIndex)));
						else if (c == Timestamp.class)
							data = m_rs.getTimestamp(colIndex);
						else if (c == BigDecimal.class)
							data = m_rs.getBigDecimal(colIndex);
						else if (c == Double.class)
							data = Double.valueOf(m_rs.getDouble(colIndex));
						else if (c == Integer.class)
							data = Integer.valueOf(m_rs.getInt(colIndex));
						else if (c == KeyNamePair.class)
						{
							String display = m_rs.getString(colIndex);
							int key = m_rs.getInt(colIndex+1);
							data = new KeyNamePair(key, display);
							colOffset++;
						}
						else
							data = m_rs.getString(colIndex);
						
						//  store
						this.selectedTable.setValueAt(data, row, col);
						list.add(data);
					}
				}
				
				if(!m_selected.containsKey(key_ID))
				{
					m_selected.put(key_ID, list);
					m_existSelected.put(key_ID, list);
				}
			}
			if(lstSelectedArr.length > 0)
				;//selectedTable.setSelectedIndices(lstSelectedArr);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.close(m_rs, m_pstmt);
			m_rs = null;
			m_pstmt = null;
		}
		
		return true;
	}
	
	//Lấy dữ liệu từ lệnh S đổ vào m_rows
	List<ArrayList<Object>> m_rows = new ArrayList<ArrayList<Object>>();
	public void query ()
	{
		
		StringBuilder sql = new StringBuilder(m_query.toString());
		sql.append(getSQLWhere());
		
		int index = 0;      //  rowset index
		m_rows.clear();
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			int size = m_selectedLayout.length;
			stmt = DB.createStatement();
			rs = stmt.executeQuery(sql.toString());
			while (rs.next())
			{
				ArrayList<Object> row = new ArrayList<Object>(size);
				index = 1;
				//  Columns
				for (int i = 0; i < size; i++)
				{
					Class<?> c = m_selectedLayout[i].getColClass();
					//  Get ID
					if (c == IDColumn.class)
						row.add(rs.getString(index++));
					//  Null check
					else if (rs.getObject(index) == null)
					{
						index++;
						row.add(null);
					}
					else if (c == String.class)
						row.add(rs.getString(index++));
					else if (c == BigDecimal.class)
						row.add(rs.getBigDecimal(index++));
					else if (c == Double.class)
						row.add(Double.valueOf(rs.getDouble(index++)));
					else if (c == Integer.class)
						row.add(Integer.valueOf(rs.getInt(index++)));
					else if (c == Timestamp.class)
						row.add(rs.getTimestamp(index++));
					else if (c == Boolean.class)
						row.add(Boolean.valueOf("Y".equals(rs.getString(index++))));
					else    //  should not happen
					{
						row.add(rs.getString(index++));
					}
				}
				m_rows.add(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.close(rs, stmt);
			rs = null;
			stmt = null;
		}
	}
	
	public void setSearchStatusLine (int no, String text, boolean error)
	{
		selectionStatusBar.setStatusLine(text, error);
	}	//	setStatusLine

	
	protected Comparator<Object> getColumnComparator(boolean ascending, final int columnIndex)
    {
    	return new ColumnComparator(ascending, columnIndex);
    }
	
	public static class ColumnComparator implements Comparator<Object>
    {

    	private int columnIndex;
		private MSort sort;

		public ColumnComparator(boolean ascending, int columnIndex)
    	{
    		this.columnIndex = columnIndex;
    		sort = new MSort(0, null);
        	sort.setSortAsc(ascending);
    	}

        public int compare(Object o1, Object o2)
        {
                Object item1 = ((List<?>)o1).get(columnIndex);
                Object item2 = ((List<?>)o2).get(columnIndex);
                return sort.compare(item1, item2);
        }

		public int getColumnIndex()
		{
			return columnIndex;
		}
    }

	private CustomForm form;
	@Override
	public ADForm getForm() {
		return form;
	}

	@Override
	public void tableChanged(WTableModelEvent event) {
		// TODO Auto-generated method stub
		
	}


}
	