/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Copyright (C) 2003-2010 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): victor.perez@e-evolution.com, www.e-evolution.com          *
 *****************************************************************************/
package eone.webui.apps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.form.TreeBOM;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Center;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.West;

import eone.base.model.MColumn;
import eone.base.model.MLookup;
import eone.base.model.MLookupFactory;
import eone.base.model.MProduct;
import eone.base.model.MProductBOM;
import eone.base.model.MUOM;
import eone.base.model.Query;
import eone.webui.ClientInfo;
import eone.webui.component.Borderlayout;
import eone.webui.component.Checkbox;
import eone.webui.component.ConfirmPanel;
import eone.webui.component.Label;
import eone.webui.component.ListModelTable;
import eone.webui.component.ListboxFactory;
import eone.webui.component.Panel;
import eone.webui.component.SimpleTreeModel;
import eone.webui.component.WListbox;
import eone.webui.editor.WSearchEditor;
import eone.webui.event.ValueChangeEvent;
import eone.webui.panel.ADForm;
import eone.webui.panel.CustomForm;
import eone.webui.panel.IFormController;
import eone.webui.session.SessionManager;
import eone.webui.util.TreeUtils;
import eone.webui.util.ZKUpdateUtil;

public class WTreeBOM extends TreeBOM implements IFormController, EventListener<Event> {
	
	private int         	m_WindowNo = 0;
	private CustomForm		m_frame = new CustomForm();
	private Tree			m_tree = new Tree();
	private Borderlayout 	mainLayout = new Borderlayout();
	private Panel			northPanel = new Panel();
	private Panel			southPanel = new Panel();
	private Label			labelProduct = new Label();
	private WSearchEditor   fieldProduct;
	private West 			west = new West();	
	private Checkbox		implosion	= new Checkbox ();
	private Label			treeInfo	= new Label ();
	
	private Panel dataPane = new Panel();
	private Panel treePane = new Panel();

	private mySimpleTreeNode   m_selectedNode;	//	the selected model node
	private int   m_selected_id = 0;
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private WListbox tableBOM = ListboxFactory.newDataTable();
	private Vector<Vector<Object>> dataBOM = new Vector<Vector<Object>>();
	private Hlayout northLayout = new Hlayout();
	private Hlayout southLayout = new Hlayout();
	private mySimpleTreeNode  	m_root = null;
	private boolean reload = false;
	private Checkbox treeExpand = new Checkbox();
	
	public WTreeBOM(){
		try{
			m_WindowNo = m_frame.getWindowNo();
			preInit();
			jbInit ();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "VTreeBOM.init", e);
		}
	}
	
	private void loadTableBOM()
	{

	//  Header Info
		
		Vector<String> columnNames = new Vector<String>();
		
		columnNames.add(Msg.translate(Env.getCtx(), "IsActive"));        // 0
		columnNames.add(Msg.getElement(Env.getCtx(), "Line"));           // 1
		columnNames.add(Msg.getElement(Env.getCtx(), "M_Product_ID"));   // 2
		columnNames.add(Msg.getElement(Env.getCtx(), "C_UOM_ID"));       // 3
		columnNames.add(Msg.getElement(Env.getCtx(), "QtyBOM"));   	   	 // 4
		
		tableBOM.clear();

		//  Set Model
		ListModelTable model = new ListModelTable(dataBOM);
		tableBOM.setData(model, columnNames);

		tableBOM.setColumnClass( 0, Boolean.class, true);     //  0 IsActive
		tableBOM.setColumnClass( 1, String.class,true);       //  1 Line
		tableBOM.setColumnClass( 2, KeyNamePair.class,true);  //  2 M_Product_ID
		tableBOM.setColumnClass( 3, KeyNamePair.class,true);  //  3 C_UOM_ID
		tableBOM.setColumnClass( 4, BigDecimal.class,true);   //  4 QtyBOM
		
	}   //  dynInit
	
	private void preInit() throws Exception
	{
		Properties ctx = Env.getCtx();
		Language language = Language.getLoginLanguage(); // Base Language
		MLookup m_fieldProduct = MLookupFactory.get(ctx, m_WindowNo,
				MColumn.getColumn_ID(MProduct.Table_Name, "M_Product_ID"),
				DisplayType.Search, language, MProduct.COLUMNNAME_M_Product_ID, 0, false,
				" M_Product.IsSummary = 'N'");
		
		fieldProduct = new WSearchEditor("M_Product_ID", true, false, true, m_fieldProduct)
		{
			public void setValue(Object value) {
				super.setValue(value);
				this.fireValueChange(new ValueChangeEvent(this, this.getColumnName(), getValue(), value));
				confirmPanel.getOKButton().setFocus(true);
			}
		};
		
		implosion.addActionListener(this);
		treeExpand.addActionListener(this);
		
	}
	
	private void jbInit()
	{
	
		ZKUpdateUtil.setWidth(m_frame, "99%");
		ZKUpdateUtil.setHeight(m_frame, "100%");
		m_frame.setStyle("position: absolute; padding: 0; margin: 0");
		m_frame.appendChild (mainLayout);
		ZKUpdateUtil.setHflex(mainLayout, "1");
		ZKUpdateUtil.setHeight(mainLayout, "100%");
		northPanel.appendChild(northLayout);
		southPanel.appendChild(southLayout);
		ZKUpdateUtil.setVflex(southPanel, "min");
		
		
		labelProduct.setText (Msg.getElement(Env.getCtx(), "M_Product_ID"));
		implosion.setText (Msg.getElement(Env.getCtx(), "Implosion"));
		treeInfo.setText (Msg.getElement(Env.getCtx(), "Sel_Product_ID")+": ");
		
		North north = new North();
		north.appendChild(northPanel);
		ZKUpdateUtil.setVflex(north, "min");
		ZKUpdateUtil.setWidth(northPanel, "100%");
		mainLayout.appendChild(north);

		northLayout.setValign("middle");
		northLayout.setStyle("padding: 4px;");
		northLayout.appendChild(labelProduct.rightAlign());
		if (ClientInfo.maxWidth(ClientInfo.EXTRA_SMALL_WIDTH-1))
			ZKUpdateUtil.setWidth(fieldProduct.getComponent(), "150px");
		else if (ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1))
			ZKUpdateUtil.setWidth(fieldProduct.getComponent(), "200px");
		else if (ClientInfo.minWidth(ClientInfo.MEDIUM_WIDTH))
			ZKUpdateUtil.setWidth(fieldProduct.getComponent(), "400px");
		else
			ZKUpdateUtil.setWidth(fieldProduct.getComponent(), "300px");
		northLayout.appendChild(fieldProduct.getComponent());
		northLayout.appendChild(new Space());
		northLayout.appendChild(implosion);
		northLayout.appendChild(new Space());
		northLayout.appendChild(treeInfo);
		if (ClientInfo.maxWidth(ClientInfo.SMALL_WIDTH-1))
			treeInfo.setVisible(false);
		
		treeExpand.setText(Msg.getMsg(Env.getCtx(), "ExpandTree"));

		South south = new South();
		south.appendChild(southPanel);
		ZKUpdateUtil.setVflex(south, "min");
		ZKUpdateUtil.setWidth(southPanel, "100%");
		mainLayout.appendChild(south);
		
		southLayout.setValign("middle");
		southLayout.setStyle("padding: 4px");
		ZKUpdateUtil.setHflex(southLayout, "1");
		ZKUpdateUtil.setVflex(southLayout, "min");
		southLayout.appendChild(treeExpand);
		ZKUpdateUtil.setHflex(treeExpand, "1");
		treeExpand.setStyle("float: left;");
		southLayout.appendChild(confirmPanel);
		ZKUpdateUtil.setHflex(confirmPanel, "1");
		confirmPanel.setStyle("float: right;");
		confirmPanel.addActionListener(this);
		
		mainLayout.appendChild(west);
		west.setSplittable(true);
		west.appendChild(treePane);
		treePane.appendChild(m_tree);
		m_tree.setStyle("border: none;");
		ZKUpdateUtil.setWidth(west, "33%");
		west.setAutoscroll(true);
		m_tree.addEventListener(Events.ON_SELECT, this);
		
		Center center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(dataPane);
		dataPane.appendChild(tableBOM);
		ZKUpdateUtil.setHflex(dataPane, "1");
		ZKUpdateUtil.setVflex(dataPane, "1");
		center.setAutoscroll(true);
	}
	
	public void dispose()
	{
		SessionManager.getAppDesktop().closeActiveWindow();
	}	//	dispose
	
	@Override
	public void onEvent(Event event) throws Exception {
		
		if (event.getTarget().getId().equals(ConfirmPanel.A_OK))
		{
			if(m_selected_id > 0 || getM_Product_ID() > 0) action_loadBOM();
		}
		if (event.getTarget().getId().equals(ConfirmPanel.A_CANCEL)) 
		{
			dispose();
		}
		if (event.getTarget().equals(treeExpand)) 
		{
			expandOrCollapse();
		}
		//  *** Tree ***
		if (event.getTarget() instanceof Tree )	
		{
			Treeitem ti = m_tree.getSelectedItem(); 
			if (ti == null) {
				log.log(Level.WARNING, "WTreeBOM.onEvent treeItem=null");
			}
			else
			{
				mySimpleTreeNode tn = (mySimpleTreeNode)ti.getValue();
				setSelectedNode(tn);
			}
		}

	}

	private void expandOrCollapse() {
		if (treeExpand.isChecked())
		{
			if (m_tree.getTreechildren() != null)
				TreeUtils.expandAll(m_tree);
		}
		else
		{
			if (m_tree.getTreechildren() != null)
				TreeUtils.collapseAll(m_tree);				
		}
	}
	
	/**
	 *  Set the selected node & initiate all listeners
	 *  @param nd node
	 * @throws Exception 
	 */
	private void setSelectedNode (mySimpleTreeNode nd) throws Exception
	{
		if (log.isLoggable(Level.CONFIG)) log.config("Node = " + nd);
		m_selectedNode = nd;
		if(m_selectedNode == null)
			return;

		Vector <?> nodeInfo = (Vector <?>)(m_selectedNode.getData());
        m_selected_id =  ((KeyNamePair)nodeInfo.elementAt(2)).getKey() ;

        if(m_selected_id > 0)
        	action_reloadBOM();
        
	}   //  setSelectedNode
	
	private void action_loadBOM() throws Exception
	{
		reload = false;

		int M_Product_ID = getM_Product_ID(); 
		if (M_Product_ID == 0)
			return;
		MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);
		treeInfo.setText (Msg.getElement(Env.getCtx(), "Sel_Product_ID")+": "+product.getValue());

		Vector<Object> line = new Vector<Object>(10);
		line.add( Boolean.valueOf(product.isActive()));   //  0 IsActive
		line.add( Integer.valueOf(0).toString()); // 1 Line
		KeyNamePair pp = new KeyNamePair(product.getM_Product_ID(),product.getValue().concat("_").concat(product.getName()));
		line.add(pp); //  2 M_Product_ID
		MUOM u = new MUOM(product.getCtx(), product.getC_UOM_ID(), product.get_TrxName());
		KeyNamePair uom = new KeyNamePair(u.get_ID(),u.getUOMSymbol());
		line.add(uom); //  3 C_UOM_ID
		line.add((BigDecimal) (Env.ONE).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros());  //  4 QtyBOM

		// dummy root node, as first node is not displayed in tree  
		mySimpleTreeNode parent = new mySimpleTreeNode("Root",new ArrayList<TreeNode<Object>>());
		//m_root = parent;
		m_root = new mySimpleTreeNode((Vector<Object>)line,new ArrayList<TreeNode<Object>>());
		parent.getChildren().add(m_root);

		dataBOM.clear();

		if (isImplosion())
		{
			try{
				m_tree.setModel(null);
			}catch(Exception e)
			{}
			
			if (m_tree.getTreecols() != null)
				m_tree.getTreecols().detach();
			if (m_tree.getTreefoot() != null)
				m_tree.getTreefoot().detach();
			if (m_tree.getTreechildren() != null)
				m_tree.getTreechildren().detach();
			
			for (MProductBOM bomline : getParentBOMs(M_Product_ID))
			{
				addParent(bomline, m_root);
			}     
			
			Treecols treeCols = new Treecols();
			m_tree.appendChild(treeCols);
			Treecol treeCol = new Treecol();
			treeCols.appendChild(treeCol);
			
			SimpleTreeModel model = new SimpleTreeModel(parent);
			m_tree.setPageSize(-1);
			m_tree.setItemRenderer(model);
			m_tree.setModel(model);
		}
		else
		{
			try{
				m_tree.setModel(null);
			}catch(Exception e)
			{}
			
			if (m_tree.getTreecols() != null)
				m_tree.getTreecols().detach();
			if (m_tree.getTreefoot() != null)
				m_tree.getTreefoot().detach();
			if (m_tree.getTreechildren() != null)
				m_tree.getTreechildren().detach();
			for (MProductBOM bom : getChildBOMs(M_Product_ID, true))
			{
				addChild(bom, m_root);                    
			}      
			
			Treecols treeCols = new Treecols();
			m_tree.appendChild(treeCols);
			Treecol treeCol = new Treecol();
			treeCols.appendChild(treeCol);
			
			SimpleTreeModel model = new SimpleTreeModel(parent);
			
			m_tree.setPageSize(-1);
			m_tree.setItemRenderer(model);
			m_tree.setModel(model);
		}
		
		loadTableBOM();

		treeExpand.setChecked(false);
	}

	private void action_reloadBOM() throws Exception
	{
		reload = true;
		int M_Product_ID = m_selected_id;

		if (M_Product_ID == 0)
			return;
		MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);
		treeInfo.setText (Msg.getElement(Env.getCtx(), "Sel_Product_ID")+": "+product.getValue());
		
		dataBOM.clear();

		if (isImplosion())
		{
			
			for (MProductBOM bomline : getParentBOMs(M_Product_ID))
			{
				addParent(bomline, m_selectedNode);
			}     
			
		}
		else
		{

			for (MProductBOM bom : getChildBOMs(M_Product_ID, true))
			{
				addChild(bom, m_selectedNode);                    
			}      
			
		}

		loadTableBOM();
	}

	
	public void addChild(MProductBOM bomline, mySimpleTreeNode parent) throws Exception 
	{

		MProduct M_Product = MProduct.get(getCtx(), bomline.getM_ProductBOM_ID());

		Vector<Object> line = new Vector<Object>(10);
		line.add( Boolean.valueOf(bomline.isActive()));   //  0 IsActive
		line.add( Integer.valueOf(bomline.getLine()).toString()); // 1 Line
		KeyNamePair pp = new KeyNamePair(M_Product.getM_Product_ID(),M_Product.getValue().concat("_").concat(M_Product.getName()));
		line.add(pp); //  2 M_Product_ID
		MUOM u = new MUOM(M_Product.getCtx(), M_Product.getC_UOM_ID(), M_Product.get_TrxName());
		KeyNamePair uom = new KeyNamePair(u.get_ID(),u.getUOMSymbol());
		line.add(uom); //  3 C_UOM_ID
		line.add((BigDecimal) ((bomline.getBOMQty()!=null) ? bomline.getBOMQty() : Env.ZERO).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros());  //  4 QtyBOM

		mySimpleTreeNode child = new mySimpleTreeNode(line,new ArrayList<TreeNode<Object>>());
		if (!reload)
			parent.getChildren().add(child);
		
		if (m_selected_id == bomline.getM_Product_ID() || getM_Product_ID() == bomline.getM_Product_ID())		
			dataBOM.add(line);
		
		if (reload) return;

		for (MProductBOM bom : getChildBOMs(bomline.getM_ProductBOM_ID(), false))
		{
			addChild(bom, child);
		}
	}

	public void addParent(MProductBOM bom, mySimpleTreeNode parent) throws Exception 
	{
		MProduct M_Product = MProduct.get(getCtx(), bom.getM_Product_ID());

		Vector<Object> line = new Vector<Object>(10);
		line.add( Boolean.valueOf(M_Product.isActive()));   //  0 IsActive
		line.add( Integer.valueOf(bom.getLine()).toString()); // 1 Line
		KeyNamePair pp = new KeyNamePair(M_Product.getM_Product_ID(),M_Product.getValue().concat("_").concat(M_Product.getName()));
		line.add(pp); //  2 M_Product_ID
		MUOM u = new MUOM(M_Product.getCtx(), M_Product.getC_UOM_ID(), M_Product.get_TrxName());
		KeyNamePair uom = new KeyNamePair(u.get_ID(),u.getUOMSymbol());
		line.add(uom); //  3 C_UOM_ID
		line.add((BigDecimal) ((bom.getBOMQty()!=null) ? bom.getBOMQty() : Env.ZERO).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros());  //  4 QtyBOM

		if(m_selected_id == bom.getM_ProductBOM_ID() || getM_Product_ID() == bom.getM_ProductBOM_ID())		
			dataBOM.add(line);

		mySimpleTreeNode child = new mySimpleTreeNode(line,new ArrayList<TreeNode<Object>>());
		if (!reload)
			parent.getChildren().add(child);

		if (reload) return;
		
		for (MProductBOM bomline : getParentBOMs(bom.getM_Product_ID()))
		{
			addParent(bomline, child);
		}

	}
	
	private int getM_Product_ID() {
		Integer Product = (Integer)fieldProduct.getValue();
		if (Product == null)
			return 0;
		return Product.intValue(); 
	}
	
	private List<MProductBOM> getChildBOMs(int M_Product_ID, boolean onlyActiveRecords)
	{
		String filter = MProductBOM.COLUMNNAME_M_Product_ID+"=?"
						+(onlyActiveRecords ? " AND IsActive='Y'" : "");
		return new Query(getCtx(), MProductBOM.Table_Name, filter, null)
					.setParameters(new Object[]{M_Product_ID})
					.setOrderBy(MProductBOM.COLUMNNAME_Line)
					.list();
	}
	
	private List<MProductBOM> getParentBOMs(int M_Product_ID) 
	{
		String filter = MProductBOM.COLUMNNAME_M_ProductBOM_ID+"=?";
		return new Query(getCtx(), MProductBOM.Table_Name, filter, null)
						.setParameters(new Object[]{M_Product_ID})
						.setOrderBy(MProductBOM.COLUMNNAME_M_Product_ID+","+MProductBOM.COLUMNNAME_Line)
						.list()
						;
	}

	private boolean isImplosion() {
		return implosion.isSelected();
	}
	
	@Override
	public ADForm getForm() {
		return m_frame;
	}

}

/**************************************************************************
 * 	mySimpleTreeNode
 *  - Override toString method for display
 *  
 */
class mySimpleTreeNode extends DefaultTreeNode<Object>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7430786399068849936L;

	public mySimpleTreeNode(Object data, List<TreeNode<Object>> children) {
		
		super(data, children);
		
	}
	
	@Override 
	public String toString(){
		
		Vector <?> userObject = (Vector <?>)getData();
		// Product
		StringBuilder sb = new StringBuilder(((KeyNamePair)userObject.elementAt(2)).getName());
		// UOM
		sb.append(" ["+((KeyNamePair) userObject.elementAt(3)).getName().trim()+"]");
		// BOMQty
		BigDecimal BOMQty = (BigDecimal)(userObject.elementAt(4));
		sb.append("x"+BOMQty.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
		
		return sb.toString();
	}

}