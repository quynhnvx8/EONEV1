package eone.base.model;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.RowSet;

import org.compiere.print.MPrintColor;
import org.compiere.util.CCache;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


public class MTree extends X_AD_Tree
{

	private static final long serialVersionUID = -212066085945645584L;


	public MTree (Properties ctx, int AD_Tree_ID, String trxName)
	{
		super (ctx, AD_Tree_ID, trxName);
	}   //  MTree


	public MTree (Properties ctx, int AD_Tree_ID, 
		boolean editable, boolean clientTree, String trxName)
	{
		this (ctx, AD_Tree_ID, editable, clientTree, false, trxName, null, 0, false);
	}   //  MTree

	public MTree (Properties ctx, int AD_Tree_ID, 
			boolean editable, boolean clientTree, String trxName, String linkColName, int linkID, boolean isByRole)
	{
		this (ctx, AD_Tree_ID, editable, clientTree, false, trxName, linkColName, linkID, isByRole);
	}   //  MTree

	public MTree (Properties ctx, int AD_Tree_ID, 
			boolean editable, boolean clientTree, boolean allNodes, String trxName)
	{
		this (ctx, AD_Tree_ID, editable, clientTree, allNodes, trxName, null, 0, false);
	}   //  MTree

	public MTree (Properties ctx, int AD_Tree_ID, 
			boolean editable, boolean clientTree, boolean allNodes, String trxName, String linkColName, int linkID, boolean isByRole)
	{
		this (ctx, AD_Tree_ID, trxName);
		m_editable = editable;
		int AD_User_ID;
		if (allNodes)
			AD_User_ID = -1;
		else
			AD_User_ID = Env.getContextAsInt(ctx, "AD_User_ID");
		m_clientTree = clientTree;
		if (log.isLoggable(Level.INFO)) log.info("AD_Tree_ID=" + AD_Tree_ID
				+ ", AD_User_ID=" + AD_User_ID 
				+ ", Editable=" + editable
				+ ", OnClient=" + clientTree);
		//
		m_ByRole = isByRole;
		loadNodes(AD_User_ID, linkColName, linkID);
	}   //  MTree

	/** Is Tree editable    	*/
	private boolean     		m_editable = false;
	private boolean     		m_ByRole = false;
	/** Root Node                   */
	private MTreeNode           m_root = null;
	/** Buffer while loading tree   */
	private ArrayList<MTreeNode> m_buffer = new ArrayList<MTreeNode>();
	/** Prepared Statement for Node Details */
	private RowSet			   	m_nodeRowSet;
	/** The tree is displayed on the Java Client (i.e. not web)	*/
	private boolean				m_clientTree = true;
	
	private HashMap<Integer, ArrayList<Integer>> m_nodeIdMap;

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MTree.class);
	
	/**	Cache						*/
	private static CCache<String,Integer> tree_cache	= new CCache<String,Integer>("AD_Tree_ID", 5);
	
	private static CCache<String,Integer> menu_cache	= new CCache<String,Integer>("AD_Tree_ID", 5);
	
	/**************************************************************************
	 *  Get default (oldest) complete AD_Tree_ID for KeyColumn.
	 *  Called from GridController
	 *  @param keyColumnName key column name, eg. C_Project_ID
	 *  @param AD_Client_ID client
	 *  @return AD_Tree_ID
	 */
	public static int getDefaultAD_Tree_ID (int AD_Client_ID, String keyColumnName)
	{
		String key = AD_Client_ID + "|" + keyColumnName;
		if (tree_cache.containsKey(key))
			return tree_cache.get(key);

		s_log.config(keyColumnName);
		if (keyColumnName == null || keyColumnName.length() == 0)
			return 0;

		String TreeType = null;
		if (keyColumnName.equals("AD_Menu_ID"))
			TreeType = TREETYPE_Menu; 
		else
		{
			String tableName = keyColumnName.substring(0, keyColumnName.length() - 3);
			String query = "SELECT tr.AD_Tree_ID "
					+ "FROM AD_Tree tr "
					+ "JOIN AD_Table t ON (tr.AD_Table_ID=t.AD_Table_ID) "
					+ "WHERE tr.AD_Client_ID=? AND tr.TreeType=? AND tr.IsActive='Y' AND t.TableName = ? "
					+ "ORDER BY tr.AD_Tree_ID";
			int treeID = DB.getSQLValueEx(null, query, Env.getAD_Client_ID(Env.getCtx()), TREETYPE_CustomTable, tableName);

			if (treeID != -1) {
				tree_cache.put(key, treeID);
				return treeID;
			}
			s_log.log(Level.SEVERE, "Could not map " + keyColumnName);
			tree_cache.put(key, 0);
			return 0;
		}

		int AD_Tree_ID = 0;
		String sql = "SELECT AD_Tree_ID, Name FROM AD_Tree "
			+ "WHERE AD_Client_ID=? AND TreeType=? AND IsActive='Y' AND IsAllNodes='Y' "
			+ "ORDER BY IsDefault DESC, AD_Tree_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setString(2, TreeType);
			rs = pstmt.executeQuery();
			if (rs.next())
				AD_Tree_ID = rs.getInt(1);
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}

		tree_cache.put(key, AD_Tree_ID);
		return AD_Tree_ID;
	}   //  getDefaultAD_Tree_ID


	public static String getNodeTableName(String treeType)
	{
		String	nodeTableName = "AD_TreeNode";
		return nodeTableName;
	}
	
	public String getNodeTableName()
	{
		return getNodeTableName(getTreeType());
	}

	/*************************************************************************
	 *  Load Nodes and Bar
	 * @param AD_User_ID user for tree bar
	 * @param linkColName 
	 * @param linkID 
	 */
	private void loadNodes (int AD_User_ID, String linkColName, int linkID)
	{
		//  SQL for TreeNodes
		StringBuilder sql = new StringBuilder();
		if (getTreeType().equals(TREETYPE_Menu))	// specific sql, need to load TreeBar IDEMPIERE 329 - nmicoud
		{
			sql = new StringBuilder("SELECT "
					+ "tn.Node_ID,tn.Parent_ID,tn.SeqNo,tb.IsActive "
					+ "FROM AD_TreeNode tn"
							+ " LEFT OUTER JOIN AD_TreeBar tb ON (tn.AD_Tree_ID=tb.AD_Tree_ID"
							+ " AND tn.Node_ID=tb.Node_ID AND tb.IsFavourite = 'Y'"
							+ (AD_User_ID != -1 ? " AND tb.AD_User_ID=? ": "") 	//	#1 (conditional)
							+ ") "
							+ "WHERE tn.AD_Tree_ID=?");								//	#2
			if (!m_editable)
				sql.append(" AND tn.IsActive='Y'");

			
			sql.append(" ORDER BY COALESCE(tn.Parent_ID, -1), tn.SeqNo");
		}
		else	// IDEMPIERE 329 - nmicoud
		{
			String sourceTableName = getSourceTableName(getAD_Table_ID());
			if (sourceTableName == null)
			{
				if (getAD_Table_ID() > 0)
					sourceTableName = MTable.getTableName(getCtx(), getAD_Table_ID());
			}
			sql = new StringBuilder("SELECT "
					+ sourceTableName + "_ID as" + " Node_ID,st.Parent_ID,st.SeqNo,st.IsActive "
					+ "FROM ").append(sourceTableName).append(" st ");								//	#2
			sql.append(" WHERE st.IsActive='Y'");
			
			sql.append(" ORDER BY st.Value");
			
			if (AD_User_ID != -1)
				sql = new StringBuilder(MRole.getDefault().addAccessSQL(sql.toString(), "st", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO));	// SQL_RO for Org_ID = 0
		}
		if (log.isLoggable(Level.FINEST)) log.finest(sql.toString());
		//  The Node Loop
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			// load Node details - addToTree -> getNodeDetail
			getNodeDetails(); 
			//
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			int idx = 1;
			if (AD_User_ID != -1 && getTreeType().equals(TREETYPE_Menu))	
			{
				pstmt.setInt(idx++, AD_User_ID);
				pstmt.setInt(idx++, getAD_Tree_ID());
			}
			//	Get Tree & Bar
			rs = pstmt.executeQuery();
			m_root = new MTreeNode (0, 0, getName(), getDescription(), 0, true, null, false, null);
			while (rs.next())
			{
				int node_ID = rs.getInt(1);
				int parent_ID = rs.getInt(2);
				int seqNo = rs.getInt(3);
				boolean onBar = (rs.getString(4) != null);
				//
				if (node_ID == 0 && parent_ID == 0)
					;
				else
					addToTree (node_ID, parent_ID, seqNo, onBar);	//	calls getNodeDetail
			}
			//
			//closing the rowset will also close connection for oracle rowset implementation
			//m_nodeRowSet.close();
			m_nodeRowSet = null;
			m_nodeIdMap = null;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			m_nodeRowSet = null;
			m_nodeIdMap = null;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
			
		//  Done with loading - add remainder from buffer
		if (m_buffer.size() != 0)
		{
			if (log.isLoggable(Level.FINEST)) log.finest("clearing buffer - Adding to: " + m_root);
			for (int i = 0; i < m_buffer.size(); i++)
			{
				MTreeNode node = (MTreeNode)m_buffer.get(i);
				MTreeNode parent = m_root.findNode(node.getParent_ID());
				if (parent != null && parent.getAllowsChildren())
				{
					parent.add(node);
					int sizeBeforeCheckBuffer = m_buffer.size();
					checkBuffer(node);
					if (sizeBeforeCheckBuffer == m_buffer.size())
						m_buffer.remove(i);
					i = -1;		//	start again with i=0
				}
			}
		}

		//	Nodes w/o parent
		if (m_buffer.size() != 0)
		{
			log.severe ("Nodes w/o parent - adding to root - " + m_buffer);
			for (int i = 0; i < m_buffer.size(); i++)
			{
				MTreeNode node = (MTreeNode)m_buffer.get(i);
				m_root.add(node);
				int sizeBeforeCheckBuffer = m_buffer.size();
				checkBuffer(node);
				if (sizeBeforeCheckBuffer == m_buffer.size())
					m_buffer.remove(i);
				i = -1;
			}
			if (m_buffer.size() != 0)
				log.severe ("Still nodes in Buffer - " + m_buffer);
		}	//	nodes w/o parents

		//  clean up
		if (!m_editable && m_root.getChildCount() > 0)
			trimTree();
//		diagPrintTree();
		if (CLogMgt.isLevelFinest() || m_root.getChildCount() == 0)
			if (log.isLoggable(Level.FINE)) log.fine("ChildCount=" + m_root.getChildCount());
	}   //  loadNodes

	/**
	 *  Add Node to Tree.
	 *  If not found add to buffer
	 *  @param node_ID Node_ID
	 *  @param parent_ID Parent_ID
	 *  @param seqNo SeqNo
	 *  @param onBar on bar
	 */
	private void addToTree (int node_ID, int parent_ID, int seqNo, boolean onBar)
	{
		//  Create new Node
		MTreeNode child = getNodeDetail (node_ID, parent_ID, seqNo, onBar);
		if (child == null)
			return;

		//  Add to Tree
		MTreeNode parent = null;
		if (m_root != null)
			parent = m_root.findNode (parent_ID);
		//  Parent found
		if (parent != null && parent.getAllowsChildren())
		{
			parent.add(child);
			//  see if we can add nodes from buffer
			if (m_buffer.size() > 0)
				checkBuffer(child);
		}
		else
			m_buffer.add(child);
	}   //  addToTree

	/**
	 *  Check the buffer for nodes which have newNode as Parents
	 *  @param newNode new node
	 */
	private void checkBuffer (MTreeNode newNode)
	{
		//	Ability to add nodes
		if (!newNode.isSummary() || !newNode.getAllowsChildren())
			return;
		//
		for (int i = 0; i < m_buffer.size(); i++)
		{
			MTreeNode node = (MTreeNode)m_buffer.get(i);
			if (node.getParent_ID() == newNode.getNode_ID())
			{
				try
				{
					newNode.add(node);
				}
				catch (Exception e)
				{
					log.severe("Adding " + node.getName() 
						+ " to " + newNode.getName() + ": " + e.getMessage());
				}
				m_buffer.remove(i);
				i--;
			}
		}
	}   //  checkBuffer

	
	
	/**************************************************************************
	 *  Get Node Detail.
	 * 	Loads data into RowSet m_nodeRowSet
	 *  Columns:
	 * 	- ID
	 *  - Name
	 *  - Description
	 *  - IsSummary
	 *  - ImageIndicator
	 * 	- additional for Menu
	 *  Parameter:
	 *  - Node_ID
	 *  The SQL contains security/access control
	 */
	private void getNodeDetails ()
	{
		//  SQL for Node Info
		StringBuilder sqlNode = new StringBuilder();
		String sourceTable = "t";
		String fromClause = getSourceTableName(false);	//	fully qualified
		String columnNameX = getSourceTableName(true);
		String color = getActionColorName();
		if (getTreeType().equals(TREETYPE_Menu)) {
			boolean base = Env.isBaseLanguage(p_ctx, "AD_Menu");
			sourceTable = "m";
			if (base)
				sqlNode.append("SELECT m.AD_Menu_ID, m.Name,m.Description,m.IsSummary,m.Action, "
					+ "m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Task_ID, m.AD_InfoWindow_ID "
					+ "FROM AD_Menu m");
			else
				sqlNode.append("SELECT m.AD_Menu_ID,  t.Name,t.Description,m.IsSummary,m.Action, "
					+ "m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Task_ID, m.AD_InfoWindow_ID "
					+ "FROM AD_Menu m, AD_Menu_Trl t");
			if (!base)
				sqlNode.append(" WHERE m.AD_Menu_ID=t.AD_Menu_ID AND t.AD_Language='")
					.append(Env.getAD_Language(p_ctx)).append("'");
			if (!m_editable)
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ").append("m.IsActive='Y' ");
			}
			
			String roleType = Env.getContext(getCtx(), "#RoleType");
			if (!X_AD_Role.ROLETYPE_System.equalsIgnoreCase(roleType) && Env.getAD_Client_ID(getCtx()) > 0) {
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ")
					.append("m.RoleType !='SY' ");
				
			}
			
			//	Do not show Beta
			/*
			if (!MClient.get(getCtx()).isUseBetaFunctions())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Window_ID IS NULL OR EXISTS (SELECT * FROM AD_Window w WHERE m.AD_Window_ID=w.AD_Window_ID AND w.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Process_ID IS NULL OR EXISTS (SELECT * FROM AD_Process p WHERE m.AD_Process_ID=p.AD_Process_ID AND p.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND f.IsBetaFunctionality='N'))");
			}
			*/
			//	In R/O Menu - Show only defined Forms
			if (!m_editable)
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND ");
				if (m_clientTree)
					sqlNode.append("f.Classname");
				else
					sqlNode.append("f.JSPURL");
				sqlNode.append(" IS NOT NULL))");
			}
		}else if(getAD_Table_ID() != 0)	{
			String tableName =MTable.getTableName(getCtx(), getAD_Table_ID());
			sqlNode.append("SELECT t.").append(tableName)
			.append("_ID,");
			if (isValueDisplayed())
				sqlNode.append("t.Value || '. ' || t.Name,");
			else
				sqlNode.append("t.Name,");
			
			sqlNode.append("t.Description,t.IsSummary,").append(color)
			.append(" FROM ").append(tableName).append(" t ");
			if (!m_editable)
			sqlNode.append(" WHERE t.IsActive='Y'");
		}  else if (isValueDisplayed()) {
			sqlNode.append("SELECT t.").append(columnNameX)
			.append("_ID, t.Value || '. ' || t.Name, t.Description, t.IsSummary,").append(color)
			.append(" FROM ").append(fromClause);
			if (!m_editable)
				sqlNode.append(" WHERE t.IsActive='Y'");
		}
		else {
			if (columnNameX == null)
				throw new IllegalArgumentException("Unknown TreeType=" + getTreeType());
			sqlNode.append("SELECT t.").append(columnNameX)
				.append("_ID,t.Name,t.Description,t.IsSummary,").append(color)
				.append(" FROM ").append(fromClause);
			if (!m_editable)
				sqlNode.append(" WHERE t.IsActive='Y'");
		}
		String sql = sqlNode.toString();
		
		if (!m_editable)	//	editable = menu/etc. window
			sql = MRole.getDefault(getCtx(), false).addAccessSQL(sql, 
				sourceTable, MRole.SQL_FULLYQUALIFIED, m_editable);
		
		log.fine(sql);
		m_nodeRowSet = DB.getRowSet (sql);
		m_nodeIdMap = new HashMap<Integer, ArrayList<Integer>>(50);
		try 
		{
			m_nodeRowSet.beforeFirst();
			
			int i = 0;
			while (m_nodeRowSet.next())
			{
				i++;
				int node = m_nodeRowSet.getInt(1);
				Integer nodeId = Integer.valueOf(node);
				ArrayList<Integer> list = m_nodeIdMap.get(nodeId);
				if (list == null)
				{
					list = new ArrayList<Integer>(5);
					m_nodeIdMap.put(nodeId, list);
				}
				list.add(Integer.valueOf(i));
			}
		} catch (SQLException e) 
		{
			log.log(Level.SEVERE, "", e);
		}
	}   //  getNodeDetails

	/**
	 *  Get Menu Node Details.
	 *  As SQL contains security access, not all nodes will be found
	 *  @param  node_ID     Key of the record
	 *  @param  parent_ID   Parent ID of the record
	 *  @param  seqNo       Sort index
	 *  @param  onBar       Node also on Shortcut bar
	 *  @return Node
	 */
	private MTreeNode getNodeDetail (int node_ID, int parent_ID, int seqNo, boolean onBar)
	{
		MTreeNode retValue = null;
		try
		{
			//m_nodeRowSet.beforeFirst();
			ArrayList<Integer> nodeList = m_nodeIdMap.get(Integer.valueOf(node_ID));
			int size = nodeList != null ? nodeList.size() : 0;
			int i = 0;
			//while (m_nodeRowSet.next())
			while (i < size)
			{
				Integer nodeId = nodeList.get(i);
				i++;
				m_nodeRowSet.absolute(nodeId.intValue());
				int node = m_nodeRowSet.getInt(1);				
				if (node_ID != node)	//	search for correct one
					continue;
				//	ID,Name,Description,IsSummary,Action/Color
				int index = 2;				
				String name = m_nodeRowSet.getString(index++); 
				String description = m_nodeRowSet.getString(index++);
				boolean isSummary = "Y".equals(m_nodeRowSet.getString(index++));
				String actionColor = m_nodeRowSet.getString(index++);
				//	Menu only
				if (getTreeType().equals(TREETYPE_Menu) && !isSummary)
				{
					int AD_Window_ID = m_nodeRowSet.getInt(index++);
					int AD_Process_ID = m_nodeRowSet.getInt(index++);
					int AD_Form_ID = m_nodeRowSet.getInt(index++);
					int AD_InfoWindow_ID = m_nodeRowSet.getInt(index++);
					//
					MRole role = MRole.getDefault(getCtx(), false);
					Boolean access = null;
					if (X_AD_Menu.ACTION_Window.equals(actionColor))
					{
 						access = role.getWindowAccess(AD_Window_ID);
						
					}
					else if (X_AD_Menu.ACTION_Process.equals(actionColor) 
						|| X_AD_Menu.ACTION_Report.equals(actionColor))
						access = role.getProcessAccess(AD_Process_ID);
					else if (X_AD_Menu.ACTION_Form.equals(actionColor))
						access = role.getFormAccess(AD_Form_ID);
					else if (X_AD_Menu.ACTION_Info.equals(actionColor))
						access = role.getInfoAccess(AD_InfoWindow_ID);
				//	log.fine("getNodeDetail - " + name + " - " + actionColor + " - " + access);
					//
					if (access != null		//	rw or ro for Role 
						|| m_editable)		//	Menu Window can see all
					{
						retValue = new MTreeNode (node_ID, seqNo, name, description, parent_ID, isSummary, actionColor, onBar, m_ByRole, false);	//	menu has no color
					}
				}
				else	//	always add
				{
					Color color = null;	//	action
					if (actionColor != null && !getTreeType().equals(TREETYPE_Menu))
					{
						MPrintColor printColor = MPrintColor.get(getCtx(), actionColor);
						if (printColor != null)
							color = printColor.getColor();
					}
					//
					retValue = new MTreeNode (node_ID, seqNo,
						name, description, parent_ID, isSummary,
						null, onBar, color);			//	no action
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return retValue;
	}   //  getNodeDetails

	
	/**************************************************************************
	 *  Trim tree of empty summary nodes
	 */
	public void trimTree()
	{
		boolean needsTrim = m_root != null;
		while (needsTrim)
		{
			needsTrim = false;
			Enumeration<?> en = m_root.preorderEnumeration();
			while (m_root.getChildCount() > 0 && en.hasMoreElements())
			{
				MTreeNode nd = (MTreeNode)en.nextElement();
				if (nd.isSummary() && nd.getChildCount() == 0)
				{
					nd.removeFromParent();
					needsTrim = true;
				}
			}
		}
	}   //  trimTree


	public MTreeNode getRoot()
	{
		return m_root;
	}   //  getRoot

	/**
	 * 	Is Menu Tree
	 *	@return true if menu
	 */
	public boolean isMenu()
	{
		return TREETYPE_Menu.equals(getTreeType());
	}	//	isMenu

	
	public String toString()
	{
		StringBuilder sb = new StringBuilder("MTree[");
		sb.append("AD_Tree_ID=").append(getAD_Tree_ID())
			.append(", Name=").append(getName());
		sb.append("]");
		return sb.toString();
	}
	
	public static String getSourceTableName(int AD_Table_ID)
	{
		String sourceTable = null;
		if (AD_Table_ID <= 0)
			sourceTable = "AD_Menu";
		else
		{
			sourceTable = MTable.getTableName(Env.getCtx(), AD_Table_ID);
			
		}
		return sourceTable;		
	}
	
	public String getSourceTableName (boolean tableNameOnly)
	{
		String tableName = getSourceTableName(getAD_Table_ID());
		if (tableName == null)
		{
			if (getAD_Table_ID() > 0)
				tableName = MTable.getTableName(getCtx(), getAD_Table_ID());
		}
		if (tableNameOnly)
			return tableName;
		if (tableName != null)
			tableName += " t";
		return tableName;
	}	//	getSourceTableName

	
	/**
	 * 	Get fully qualified Name of Action/Color Column
	 *	@return NULL or Action or Color
	 */
	public String getActionColorName()
	{
		String tableName = getSourceTableName(getAD_Table_ID());
		if ("AD_Menu".equals(tableName))
			return "t.Action";
		return "NULL";
	}
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (!isActive() || !isAllNodes())
			setIsDefault(false);

		if (! TREETYPE_CustomTable.equals(getTreeType())) {
			setAD_Table_ID(-1);
			setParent_Column_ID(-1);
		}
		
		String tableName = getSourceTableName(true);
		MTable table = MTable.get(getCtx(), tableName);
		if (table.getColumnIndex("IsSummary") < 0) {
			// IsSummary is mandatory column to have a tree
			log.saveError("Error", "IsSummary column required for tree tables"); 
			return false;
		}
		if (table.getColumnIndex("Value") < 0) {
			if (isTreeDrivenByValue()) {
				// Value is mandatory column to have a tree driven by Value
				setIsTreeDrivenByValue(false);
			}
			if (isValueDisplayed()) {
				// Value is mandatory column to be displayed
				setIsValueDisplayed(false);
			}
		}

		return true;
	}
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (newRecord)	//	Base Node
		{
			MTree_Node nd = new MTree_Node(this, 0);
			nd.saveEx();
		}
		
		return success;
	}
	
	public static boolean addNode (Properties ctx, String treeType, int Record_ID, String trxName)
	{
		//	Get Tree
		int AD_Tree_ID = 0;
		MClient client = MClient.get(ctx);
		MClientInfo ci = client.getInfo();
		
		if (TREETYPE_Menu.equals(treeType))
			AD_Tree_ID = ci.getAD_Tree_Menu_ID();
		
		if (AD_Tree_ID == 0)
			throw new IllegalArgumentException("No Tree found");
		MTree tree = MTree.get(ctx, AD_Tree_ID, trxName);
		if (tree.get_ID() != AD_Tree_ID)
			throw new IllegalArgumentException("Tree found AD_Tree_ID=" + AD_Tree_ID);

		//	Insert Tree in correct tree
		boolean saved = false;
		MTree_Node node = new MTree_Node (tree, Record_ID);
		saved = node.save();
		return saved;	
	}
	
	public static MTree get (Properties ctx, int AD_Tree_ID, String trxName)
	{
		Integer key = Integer.valueOf(AD_Tree_ID);
		MTree retValue = (MTree) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MTree (ctx, AD_Tree_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	
	/**	Cache						*/
	private static CCache<Integer,MTree> s_cache = new CCache<Integer,MTree>(Table_Name, 10);
	
	
	public MTree (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTree_Base

	/**
	 * 	Parent Constructor
	 *	@param client client
	 *	@param name name
	 *	@param treeType
	 */
	public MTree (MClient client, String name, String treeType)
	{
		this (client.getCtx(), 0, client.get_TrxName());
		setClientOrg (client);
		setName (name);
		setTreeType (treeType);
	}	//	MTree_Base

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Name name
	 *	@param TreeType tree type
	 *	@param trxName transaction
	 */
	public MTree (Properties ctx, String Name, String TreeType,  
		String trxName)
	{
		super(ctx, 0, trxName);
		setName (Name);
		setTreeType (TreeType);
		setIsAllNodes (true);	//	complete tree
		setIsDefault(false);
	}
	
	public static boolean isLoadAllNodesImmediately(int treeID, String trxName) {
		return false;//DB.getSQLValueStringEx(trxName, "SELECT IsLoadAllNodesImmediately FROM AD_Tree WHERE AD_Tree_ID = ?", treeID).equals("Y");
	}
}   //  MTree
