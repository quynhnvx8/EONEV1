package eone.webui.panel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;
import org.zkoss.zul.Vbox;

import eone.base.model.MColumn;
import eone.base.model.MLookupFactory;
import eone.base.model.MTable;
import eone.webui.AdempiereWebUI;
import eone.webui.ClientInfo;
import eone.webui.component.Grid;
import eone.webui.component.GridFactory;
import eone.webui.component.Label;
import eone.webui.component.Row;
import eone.webui.component.Rows;
import eone.webui.component.Textbox;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.FDialog;

public class InfoGeneralPanel extends InfoPanel implements EventListener<Event>
{
	private static final long serialVersionUID = 3328089102224160413L;

	private Textbox txt1;
	
	private Label lbl1;
	
	private ColumnInfo[] m_generalLayout;
	
	private ArrayList<String> m_queryColumns = new ArrayList<String>();

	private ArrayList<String> m_queryColumnsSql = new ArrayList<String>();
	private Borderlayout layout;
	private Vbox southBody;

	private int noOfParameterColumn;

	public InfoGeneralPanel(String queryValue, int windowNo,String tableName,String keyColumn, boolean isSOTrx, String whereClause)
	{
		this(queryValue, windowNo, tableName, keyColumn, isSOTrx, whereClause, true);
	}

	public InfoGeneralPanel(String queryValue, int windowNo,String tableName,String keyColumn, boolean isSOTrx, String whereClause, boolean lookup)
	{
		super(windowNo, tableName, keyColumn, false,whereClause, lookup);

		setTitle(Msg.getMsg(Env.getCtx(), "Info"));

		try
		{
			init();
			initComponents();

			p_loadedOK = initInfo ();
			
			if (queryValue != null && queryValue.length() > 0)
			{				
				Textbox[] txts = new Textbox[] {txt1};
				for(Textbox t : txts) 
				{
					if (t != null && t.isVisible())
					{
						t.setValue(queryValue);
						testCount();
						if (m_count <= 0)
							t.setValue(null);
						else
							break;
					}
				}
				if (m_count <= 0)
				{
					txt1.setValue(queryValue);
				}
			}
		}
		catch (Exception e)
		{
			return;
		}

		// Elaine 2008/12/15
		int no = contentPanel.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		//

		if (queryValue != null && queryValue.length() > 0)
        {
			executeQuery();
            renderItems();
        }
		
		if (ClientInfo.isMobile()) {
			ClientInfo.onClientInfo(this, this::onClientInfo);
		}
	}

	private void initComponents()
	{
		Grid grid = GridFactory.newGridLayout();
		ZKUpdateUtil.setWidth(grid, "100%");
		ZKUpdateUtil.setVflex(grid, "min");

		layoutParameterGrid(grid);

		layout = new Borderlayout();
		ZKUpdateUtil.setWidth(layout, "100%");
		ZKUpdateUtil.setHeight(layout, "100%");
        layout.setStyle("position: relative");
        this.appendChild(layout);

        North north = new North();
        layout.appendChild(north);
		north.appendChild(grid);
		ZKUpdateUtil.setVflex(north, "min");

        Center center = new Center();
		layout.appendChild(center);
		Div div = new Div();
		div.appendChild(contentPanel);
		ZKUpdateUtil.setWidth(contentPanel, "100%");
        ZKUpdateUtil.setVflex(contentPanel, true);
        contentPanel.setSizedByContent(true);
		center.appendChild(div);
		ZKUpdateUtil.setVflex(div, "1");
		ZKUpdateUtil.setHflex(div, "1");
		ZKUpdateUtil.setVflex(center, "1");

		South south = new South();
		layout.appendChild(south);
		southBody = new Vbox();
		ZKUpdateUtil.setWidth(southBody, "100%");
		south.appendChild(southBody);
		southBody.appendChild(new Separator());
		confirmPanel.addComponentsCenter(statusBar);
		southBody.appendChild(confirmPanel);		
		//southBody.appendChild(statusBar);
		ZKUpdateUtil.setVflex(south, "min");
	}

	protected void layoutParameterGrid(Grid grid) {
		noOfParameterColumn = getNoOfParameterColumn();
		Rows rows = new Rows();
		grid.appendChild(rows);

		Row row = new Row();
		rows.appendChild(row);
		//row.appendChild(lbl1);
		row.appendChild(txt1);
		ZKUpdateUtil.setHflex(txt1, "1");
		
	}

	private int getNoOfParameterColumn() {
		return 2;
	}

	private void init()
	{
		txt1 = new Textbox();
		
		txt1.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "textbox1");
		
		lbl1 = new Label();
		
	}

	private boolean initInfo ()
	{
		if (!initInfoTable())
			return false;

		//  Prepare table

		StringBuilder where = new StringBuilder(p_tableName).append(".").append("IsActive='Y'");

		if (p_whereClause.length() > 0)
			where.append(" AND (").append(p_whereClause).append(")");
		prepareTable(m_generalLayout, p_tableName, where.toString(), "2");

		//	Set & enable Fields

		lbl1.setValue(Util.cleanAmp(Msg.translate(Env.getCtx(), "SearchInput")));

		
		return true;
	}

	
	private boolean initInfoTable ()
	{
		//	Get Query Columns

		String sql = 
				" SELECT c.ColumnName, t.AD_Table_ID, t.TableName, c.ColumnSql "+
				" FROM AD_Table t" +
				" 	INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"+
				" WHERE c.AD_Reference_ID IN (10,14) AND t.TableName=?"+	//	#1
				//Neu khong cau hinh IsInfoPanel thi lay cac truong tren giao dien.
				" 	AND "+
				"	("+
				"	(EXISTS "+
				"		("+
				"			SELECT 1 FROM AD_Field f "+
				"			WHERE f.AD_Column_ID=c.AD_Column_ID AND f.IsDisplayed='Y' AND f.IsEncrypted='N' AND f.ObscureType IS NULL"+
				"		) "+
				" 		AND NOT EXISTS "+
				"		("+
				"			SELECT 1 FROM AD_COLUMN cc "+
				"			WHERE t.AD_Table_ID=cc.AD_Table_ID And cc.IsInfoPanel = 'Y'"+
				"		) "+
				"	)"+
				"	OR"+
				" 	(EXISTS "+
				"		("+
				"			SELECT 1 FROM AD_COLUMN cc "+
				"			WHERE t.AD_Table_ID=cc.AD_Table_ID And cc.IsInfoPanel = 'Y'"+
				"		) "+
				"		AND (c.IsInfoPanel = 'Y')"+
				"	)"+
				"	)"+
				" ORDER BY c.IsIdentifier DESC, c.IsSelectionColumn Desc, c.AD_Reference_ID, c.SeqNoSelection, c.SeqNo";

		int AD_Table_ID = 0;
		String tableName = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, p_tableName);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				m_queryColumns.add(rs.getString(1));
				String columnSql = rs.getString(4);
				if (columnSql != null && columnSql.length() > 0 && columnSql.contains("@"))
					columnSql = "NULL";
				if (columnSql != null && columnSql.contains("@"))
					columnSql = Env.parseContext(Env.getCtx(), -1, columnSql, false, true);
				String qualified = p_tableName+"."+rs.getString(1);
				if (columnSql != null && columnSql.length() > 0)
					m_queryColumnsSql.add(columnSql);
				else
					m_queryColumnsSql.add(qualified);

				if (AD_Table_ID == 0)
				{
					AD_Table_ID = rs.getInt(2);
					tableName = rs.getString(3);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		//	Miminum check
		if (m_queryColumns.size() == 0)
		{
			FDialog.error(p_WindowNo, this, "Error", Msg.getMsg(Env.getCtx(),"NoQueryColumnsFound"));
			log.log(Level.SEVERE, "No query columns found");
			return false;
		}

		if (log.isLoggable(Level.FINEST)) log.finest("Table " + tableName + ", ID=" + AD_Table_ID
			+ ", QueryColumns #" + m_queryColumns.size());

		//	Only 4 Query Columns
		while (m_queryColumns.size() > 4)
		{
			m_queryColumns.remove(m_queryColumns.size()-1);
			m_queryColumnsSql.remove(m_queryColumnsSql.size()-1);
		}

		//  Set Title
		String title = Msg.translate(Env.getCtx(), tableName + "_ID");  //  best bet

		if (title.endsWith("_ID"))
			title = Msg.translate(Env.getCtx(), tableName);             //  second best bet

		setTitle(getTitle() + " " + title);

		//	Get Display Columns

		ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
		sql = "SELECT c.ColumnName, c.AD_Reference_ID, c.IsKey, f.IsDisplayed, c.AD_Reference_Value_ID, c.ColumnSql, c.AD_Column_ID, ft.Name "
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Tab tab ON (t.AD_Window_ID=tab.AD_Window_ID)"
			+ " INNER JOIN AD_Field f ON (tab.AD_Tab_ID=f.AD_Tab_ID AND f.AD_Column_ID=c.AD_Column_ID) "
			+ " INNER JOIN AD_Field_Trl ft ON (f.AD_Field_ID = ft.AD_Field_ID) "
			+ "WHERE t.AD_Table_ID=? "
			+ " AND "
			+ "("
			+ "		((c.IsKey='Y' OR (f.IsEncrypted='N' AND f.ObscureType IS NULL)) And not exists (Select 1 From AD_Column cc Where c.AD_Table_ID = cc.AD_Table_ID And cc.IsInfoPanel = 'Y') )"
			+ "		OR (Exists(Select 1 From AD_Column cc Where c.AD_Table_ID = cc.AD_Table_ID And cc.IsInfoPanel = 'Y') AND c.IsInfoPanel = 'Y')"
			+ ")"
			+ "	AND ft.AD_Language='"+ Env.getAD_Language(Env.getCtx())+"'"
			+ "ORDER BY c.IsKey DESC, f.SeqNo";

		if (Env.isBaseLanguage(Env.getCtx(), "AD_Field")) {
			sql = "SELECT c.ColumnName, c.AD_Reference_ID, c.IsKey, f.IsDisplayed, c.AD_Reference_Value_ID, c.ColumnSql, c.AD_Column_ID, f.Name "
					+ "FROM AD_Column c"
					+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID)"
					+ " INNER JOIN AD_Tab tab ON (t.AD_Window_ID=tab.AD_Window_ID)"
					+ " INNER JOIN AD_Field f ON (tab.AD_Tab_ID=f.AD_Tab_ID AND f.AD_Column_ID=c.AD_Column_ID) "
					+ "WHERE t.AD_Table_ID=? "
					+ " AND "
					+ "("
					+ "		((c.IsKey='Y' OR (f.IsEncrypted='N' AND f.ObscureType IS NULL)) And not exists (Select 1 From AD_Column cc Where c.AD_Table_ID = cc.AD_Table_ID And cc.IsInfoPanel = 'Y') )"
					+ "		OR (Exists(Select 1 From AD_Column cc Where c.AD_Table_ID = cc.AD_Table_ID And cc.IsInfoPanel = 'Y') AND c.IsInfoPanel = 'Y')"
					+ ")"
					+ "ORDER BY c.IsKey DESC, f.SeqNo";
		}
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String columnName = rs.getString(1);
				String columnNameTranslate = rs.getString("Name");
				int displayType = rs.getInt(2);
				boolean isKey = rs.getString(3).equals("Y");
				boolean isDisplayed = rs.getString(4).equals("Y");
				int AD_Reference_Value_ID = rs.getInt(5);
				String columnSql = rs.getString(6);
				if (columnSql != null && columnSql.length() > 0 && columnSql.contains("@"))
					columnSql = "NULL";
				if (columnSql != null && columnSql.contains("@"))
					columnSql = Env.parseContext(Env.getCtx(), -1, columnSql, false, true);
				if (columnSql == null || columnSql.length() == 0)
					columnSql = tableName+"."+columnName;
				int AD_Column_ID = rs.getInt(7);

				//  Default
				StringBuffer colSql = new StringBuffer(columnSql);
				Class<?> colClass = null;

				if (isKey)
					colClass = IDColumn.class;
				else if (!isDisplayed)
					;
				else if (displayType == DisplayType.YesNo)
					colClass = Boolean.class;
				else if (displayType == DisplayType.Amount)
					colClass = BigDecimal.class;
				else if (displayType == DisplayType.Number || displayType == DisplayType.Quantity)
					colClass = Double.class;
				else if (displayType == DisplayType.Integer)
					colClass = Integer.class;
				else if (displayType == DisplayType.String || displayType == DisplayType.Text || displayType == DisplayType.Memo)
					colClass = String.class;
				else if (DisplayType.isDate(displayType))
					colClass = Timestamp.class;
				//  ignore Binary, Button, ID, RowID
				else if (displayType == DisplayType.List)
				{
					if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
						colSql = new StringBuffer("(SELECT l.Name FROM AD_Ref_List l WHERE l.AD_Reference_ID=")
							.append(AD_Reference_Value_ID).append(" AND l.Value=").append(columnSql)
							.append(") AS ").append(columnName);
					else
						colSql = new StringBuffer("(SELECT t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
							+ "WHERE l.AD_Ref_List_ID=t.AD_Ref_List_ID AND l.AD_Reference_ID=")
							.append(AD_Reference_Value_ID).append(" AND l.Value=").append(columnSql)
							.append(" AND t.AD_Language='").append(Env.getAD_Language(Env.getCtx()))
							.append("') AS ").append(columnName);
					colClass = String.class;
				}

				if (colClass != null)
				{
					list.add(new ColumnInfo(columnNameTranslate, colSql.toString(), colClass));
					if (log.isLoggable(Level.FINEST)) log.finest("Added Column=" + columnName);
				}
				else if (isDisplayed && DisplayType.isLookup(displayType))
				{
					ColumnInfo colInfo = createLookupColumnInfo(columnNameTranslate, columnName, displayType, AD_Reference_Value_ID, AD_Column_ID, colSql.toString());
					if (colInfo != null)
					{
						list.add(colInfo);
					}
					else
					{
						if (log.isLoggable(Level.FINEST)) log.finest("Not Added Column=" + columnName);
					}
					
				}
				else
					if (log.isLoggable(Level.FINEST)) log.finest("Not Added Column=" + columnName);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		if (list.size() == 0)
		{
			FDialog.error(p_WindowNo, this, "Error", "No Info Columns");
			log.log(Level.SEVERE, "No Info for AD_Table_ID=" + AD_Table_ID + " - " + sql);
			return false;
		}

		if (log.isLoggable(Level.FINEST)) log.finest("InfoColumns #" + list.size());

		//  Convert ArrayList to Array
		m_generalLayout = new ColumnInfo[list.size()];
		list.toArray(m_generalLayout);
		return true;
	}

	@Override
	public String getSQLWhere()
	{
		StringBuffer sql = new StringBuffer();
		addSQLWhere (sql, 0, txt1.getText().toUpperCase());
		return sql.toString();
	}

	private void addSQLWhere(StringBuffer sql, int index, String value)
	{
		if (m_queryColumns.size() > 0 && !"".equals(value)) {
			sql.append("AND (");
			for(int i = 0; i < m_queryColumns.size(); i++) {
				if(i == 0) {
					sql.append(" UPPER(").append(m_queryColumns.get(i)).append(")").append(" LIKE ?");
				}else {
					sql.append(" OR ").append(" UPPER(").append(m_queryColumns.get(i)).append(")").append(" LIKE ?");
				}
				
			}
			sql.append(")");
		}		
	}

	private String getSQLText (Textbox f)
	{
		String s = f.getText().toUpperCase();
		if (!s.endsWith("%"))
			s += "%";
		if (log.isLoggable(Level.FINE)) log.fine( "String=" + s);
		return s;
	}   //  getSQLText

	
	protected void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		if (txt1.getText().length() > 0 && m_queryColumns.size() > 0) {
			for(int i = 0; i < m_queryColumns.size(); i++) {
				pstmt.setString(i+1, getSQLText(txt1));
			}
		}
			
	}   //  setParameters

	
    @Override
	protected void insertPagingComponent()
    {
		southBody.insertBefore(paging, southBody.getFirstChild());
		layout.invalidate();
    }
    
	protected void onClientInfo() {
		if (layout != null && layout.getNorth() != null && layout.getNorth().getFirstChild() instanceof Grid) {
			int t = getNoOfParameterColumn();
			if (t > 0 && noOfParameterColumn > 0 && t != noOfParameterColumn) {
				Grid grid = (Grid) layout.getNorth().getFirstChild();
				grid.getRows().detach();
				layoutParameterGrid(grid);
				this.invalidate();
			}
		}
	}
	
	protected ColumnInfo createLookupColumnInfo(String name, String columnName, int AD_Reference_ID, int AD_Reference_Value_ID, int AD_Column_ID, String columnSql) {
		
		MTable table = MTable.get(Env.getCtx(), p_tableName);
		MColumn column = table.getColumn(columnName);
		String baseColumn = column.isVirtualColumn() ? columnSql : columnName;

		String embedded = AD_Reference_Value_ID > 0 ? MLookupFactory.getLookup_TableEmbed(Env.getLanguage(Env.getCtx()), columnName, p_tableName, AD_Reference_Value_ID)
				: MLookupFactory.getLookup_TableDirEmbed(Env.getLanguage(Env.getCtx()), columnName, p_tableName, baseColumn);
		embedded = "(" + embedded + ")";
	
		if (embedded.contains("@"))
			embedded = "NULL";
		
		ColumnInfo columnInfo = null;
		if (columnName.endsWith("_ID")  && !column.isVirtualColumn())
			columnInfo = new ColumnInfo(name, embedded, KeyNamePair.class, p_tableName+"."+columnName);
		else
			columnInfo = new ColumnInfo(name, embedded, String.class, null);
		return columnInfo;
	}
}
