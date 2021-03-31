package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;


public class MTree_Node extends X_AD_TreeNode
{

	private static final long serialVersionUID = 5473815124433234331L;


	public static MTree_Node get (MTree tree, int Node_ID)
	{
		MTree_Node retValue = null;
		String sql = "SELECT * FROM AD_TreeNode WHERE AD_Tree_ID=? AND Node_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, tree.get_TrxName());
			pstmt.setInt (1, tree.getAD_Tree_ID());
			pstmt.setInt (2, Node_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MTree_Node (tree.getCtx(), rs, tree.get_TrxName());
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		return retValue;
	}	//	get
	
	public static List<X_AD_TreeNode> getAllNode(Properties ctx, String trxName, int AD_Tree_ID, boolean root) {
		String whereClause = "AD_Tree_ID = ?";
		if (root) {
			whereClause = whereClause + " And not exists (Select 1 From AD_Menu Where AD_Menu_ID = Node_ID And RoleType = 'SY')";
		}
		List<X_AD_TreeNode> list = new Query(ctx, Table_Name, whereClause, trxName)
				.setParameters(AD_Tree_ID)
				.list();
		return list;
	}

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MTree_Node.class);


	public MTree_Node (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTree_Node


	public MTree_Node (MTree tree, int Node_ID)
	{
		super (tree.getCtx(), 0, tree.get_TrxName());
		setClientOrg(tree);
		setAD_Tree_ID (tree.getAD_Tree_ID());
		setNode_ID(Node_ID);
		//	Add to root
		setParent_ID(0);
		setSeqNo (0);
	}	//	MTree_Node

}	//	MTree_Node
