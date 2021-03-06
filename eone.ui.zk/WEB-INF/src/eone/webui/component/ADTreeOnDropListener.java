package eone.webui.component;

import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import eone.base.model.MRole;
import eone.base.model.MTree;
import eone.base.model.MTreeNode;
import eone.webui.util.TreeUtils;
import eone.webui.window.FDialog;

/**
 * 
 * QUYNHNV: 24/09/2020 
 * keo tha menu theo role.
 * luu parent tai cac bang truc tiep. Bo cac bang AD_TreeNodeXXX
 *
 */
public class ADTreeOnDropListener implements EventListener<Event> {
	
	private SimpleTreeModel treeModel;
	private MTree mTree;
	private int windowNo;
	private Tree tree;
	
	private static final CLogger log = CLogger.getCLogger(ADTreeOnDropListener.class);

	/**
	 * 
	 * @param tree
	 * @param model
	 * @param mTree
	 * @param windowNo
	 */
	public ADTreeOnDropListener(Tree tree, SimpleTreeModel model, MTree mTree, int windowNo) {
		this.tree = tree;
		this.treeModel = model;
		this.mTree = mTree;
		this.windowNo = windowNo;
		//Neu khong phan quyen thi khong cho keo tha
		boolean isDragDrop = true;
		if (!MRole.isDragDrop()) {
			isDragDrop = false;
		}
		treeModel.setItemDraggable(isDragDrop);
		
	}

	/**
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event instanceof DropEvent) {
			DropEvent de = (DropEvent) event;
			
			if (log.isLoggable(Level.FINE)) log.fine("Source=" + de.getDragged() + " Target=" + de.getTarget());
			if (de.getDragged() != de.getTarget()) {
				Treeitem src = (Treeitem) ((Treerow) de.getDragged()).getParent();
				Treeitem target = (Treeitem) ((Treerow) de.getTarget()).getParent();
				moveNode((DefaultTreeNode<Object>)src.getValue(), (DefaultTreeNode<Object>)target.getValue());
			}
		} 
	}
	
	/**
	 *	Move TreeNode
	 *	@param	movingNode	The node to be moved
	 *	@param	toNode		The target node
	 */
	private void moveNode(DefaultTreeNode<Object> movingNode, DefaultTreeNode<Object> toNode)
	{
		log.info(movingNode.toString() + " to " + toNode.toString());

		
		if (movingNode == toNode)
			return;
		
		MTreeNode toMNode = (MTreeNode) toNode.getData();
				
		if (!toMNode.isSummary())	//	drop on a child node
		{
			moveNode(movingNode, toNode, false);
		}
		else						//	drop on a summary node
		{
			//prompt user to select insert after or drop into the summary node
			int path[] = treeModel.getPath(toNode);
			Treeitem toItem = tree.renderItemByPath(path);
			
			//tree.setSelectedItem(toItem);
			//Events.sendEvent(tree, new Event(Events.ON_SELECT, tree));

			MenuListener listener = new MenuListener(movingNode, toNode);

			Menupopup popup = new Menupopup();
			Menuitem menuItem = new Menuitem(Msg.getMsg(Env.getCtx(), "InsertAfter"));
			menuItem.setValue("InsertAfter");
			menuItem.setParent(popup);
			menuItem.addEventListener(Events.ON_CLICK, listener);

			menuItem = new Menuitem(Msg.getMsg(Env.getCtx(), "MoveInto"));
			menuItem.setValue("MoveInto");
			menuItem.setParent(popup);
			menuItem.addEventListener(Events.ON_CLICK, listener);

			popup.setPage(tree.getPage());
			popup.open(toItem.getTreerow());
		}
		
	}	//	moveNode
	
	private void moveNode(DefaultTreeNode<Object> movingNode, DefaultTreeNode<Object> toNode, boolean moveInto)
	{
		DefaultTreeNode<Object> newParent;
		int index;		
		
		//  remove
		DefaultTreeNode<Object> oldParent = treeModel.getParent(movingNode);
		treeModel.removeNode(movingNode);
		
		//get new index
		if (!moveInto)
		{
			newParent = treeModel.getParent(toNode);
			index = newParent.getChildren().indexOf(toNode) + 1;	//	the next node
		}
		else									//	drop on a summary node
		{
			newParent = toNode;
			index = 0;                   			//	the first node
		}
		
		//  insert
		newParent = treeModel.addNode(newParent, movingNode, index);
		
		int path[] = treeModel.getPath(movingNode);
		if (TreeUtils.isOnInitRenderPosted(tree) || tree.getTreechildren() == null)
		{
			tree.onInitRender();
		}
		@SuppressWarnings("unused")
		Treeitem movingItem = tree.renderItemByPath(path);		
		//tree.setSelectedItem(movingItem);
		//Events.sendEvent(tree, new Event(Events.ON_SELECT, tree));

		//	***	Save changes to disk
		Trx trx = Trx.get (Trx.createTrxName("ADTree"), true);
		trx.setDisplayName(getClass().getName()+"_moveNode");
		try
		{
			@SuppressWarnings("unused")
			int no = 0;
			MTreeNode oldMParent = (MTreeNode) oldParent.getData();
			for (int i = 0; i < oldParent.getChildCount(); i++)
			{
				DefaultTreeNode<?> nd = (DefaultTreeNode<?>)oldParent.getChildAt(i);
				MTreeNode md = (MTreeNode) nd.getData();
				StringBuilder sql = new StringBuilder("UPDATE ");
				sql.append(mTree.getNodeTableName())
					.append(" SET Parent_ID=").append(oldMParent.getNode_ID())
					.append(", SeqNo=").append(i)
					.append(", Updated=getDate()")
					.append(" WHERE AD_Tree_ID=").append(mTree.getAD_Tree_ID())
					.append(" AND Node_ID=").append(md.getNode_ID());
				if (log.isLoggable(Level.FINE)) log.fine(sql.toString());
				if (mTree.getTreeType().equalsIgnoreCase(MTree.TREETYPE_CustomTable)) {
					String tableName = MTree.getSourceTableName(mTree.getAD_Table_ID());
					sql = new StringBuilder();
					sql.append(" UPDATE ").append(tableName)
					.append(" SET Parent_ID=").append(oldMParent.getNode_ID()).append(", Updated=getDate()")
					.append(" WHERE ").append(tableName).append("_ID=").append(md.getNode_ID());
				}
				no = DB.executeUpdate(sql.toString(),trx.getTrxName());
			}
			if (oldParent != newParent) 
			{
				MTreeNode newMParent = (MTreeNode) newParent.getData();
				for (int i = 0; i < newParent.getChildCount(); i++)
				{
					DefaultTreeNode<?> nd = (DefaultTreeNode<?>)newParent.getChildAt(i);
					MTreeNode md = (MTreeNode) nd.getData();
					StringBuilder sql = new StringBuilder("UPDATE ");
					sql.append(mTree.getNodeTableName())
						.append(" SET Parent_ID=").append(newMParent.getNode_ID())
						.append(", SeqNo=").append(i)
						.append(", Updated=getDate()")
						.append(" WHERE AD_Tree_ID=").append(mTree.getAD_Tree_ID())
						.append(" AND Node_ID=").append(md.getNode_ID());
					if (log.isLoggable(Level.FINE)) log.fine(sql.toString());
					if (mTree.getTreeType().equalsIgnoreCase(MTree.TREETYPE_CustomTable)) {
						String tableName = MTree.getSourceTableName(mTree.getAD_Table_ID());
						sql = new StringBuilder();
						sql.append(" UPDATE ").append(tableName)
						.append(" SET Parent_ID=").append(newMParent.getNode_ID()).append(", Updated=getDate()")
						.append(" WHERE ").append(tableName).append("_ID=").append(md.getNode_ID());
					}
					DB.executeUpdateEx(sql.toString(),trx.getTrxName());
				}
			}
			//	COMMIT          *********************
			trx.commit(true);
		}
        catch (Exception e)
		{
			trx.rollback();
			FDialog.error(windowNo, tree, "TreeUpdateError", e.getLocalizedMessage());
		}
		finally
		{
			trx.close();
			trx = null;
		}
	}
	
	class MenuListener implements EventListener<Event> {
		private DefaultTreeNode<Object> movingNode;
		private DefaultTreeNode<Object> toNode;
		MenuListener(DefaultTreeNode<Object> movingNode, DefaultTreeNode<Object> toNode) {
			this.movingNode = movingNode;
			this.toNode = toNode;
		}
		public void onEvent(Event event) throws Exception {
			if (Events.ON_CLICK.equals(event.getName()) && event.getTarget() instanceof Menuitem) {
				Menuitem menuItem = (Menuitem) event.getTarget();
				if ("InsertAfter".equals(menuItem.getValue())) {
					moveNode(movingNode, toNode, false);
				} else if ("MoveInto".equals(menuItem.getValue())) {
					moveNode(movingNode, toNode, true);
				}
			}
		}
		
	}
}
