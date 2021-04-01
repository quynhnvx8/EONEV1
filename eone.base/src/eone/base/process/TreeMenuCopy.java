package eone.base.process;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.model.MTree;
import eone.base.model.MTree_Node;
import eone.base.model.PO;
import eone.base.model.X_AD_TreeNode;

/**
 *	Tree Copy	
 *	
 *  @author Quynhnv.x8
 *  @version 1. AddNew 28/10/2020
 */
public class TreeMenuCopy extends SvrProcess
{
	private int		m_AD_Tree_ID;
	private int m_AD_TreeSelect_ID;
	private int BATCH_SIZE = Env.getBatchSize(getCtx());
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if ("AD_Tree_ID".equalsIgnoreCase(name))
				m_AD_TreeSelect_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		m_AD_Tree_ID = getRecord_ID();		
	}	

	
	protected String doIt() throws Exception
	{
		String sqlDel = "Delete From AD_TreeNode Where AD_Tree_ID = ?";
		DB.executeUpdate(sqlDel, m_AD_Tree_ID, get_TrxName());
		MTree toTree = new MTree (getCtx(), m_AD_Tree_ID, get_TrxName());
		MTree fromTree = new MTree (getCtx(), m_AD_TreeSelect_ID, get_TrxName());
		if (toTree == null || toTree.getAD_Tree_ID() == 0)
			throw new IllegalArgumentException("No Tree -" + toTree);
		List<X_AD_TreeNode> list = MTree_Node.getAllNode(getCtx(), get_TrxName(), fromTree.getAD_Tree_ID(), fromTree.getAD_Client_ID() == 0 ? true : false);
		X_AD_TreeNode node = null;
		String sqlInsert = PO.getSqlInsert(X_AD_TreeNode.Table_ID, get_TrxName());
		List<Object> params = null;
		List<List<Object>> values = new ArrayList<List<Object>>();
		for(int i = 0; i < list.size(); i++) {
			node = list.get(i);
			node.setAD_Tree_ID(toTree.getAD_Tree_ID());
			params = PO.getBatchValueList(node, X_AD_TreeNode.Table_ID, get_TrxName(), node.getNode_ID());
			values.add(params);
			
			if(values.size() >= BATCH_SIZE) {
				String err = DB.excuteBatch(sqlInsert, values, get_TrxName());
				if (err != null) {
					DB.rollback(false, get_TrxName());
				}
				values.clear();
			}
		}
		//Excute batch final
		String err = DB.excuteBatch(sqlInsert, values, get_TrxName());
		if (err != null) {
			DB.rollback(false, get_TrxName());
		}
		values.clear();
		
		return "OK";
	}	
}
