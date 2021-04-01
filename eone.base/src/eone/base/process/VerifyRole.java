package eone.base.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.model.MPermission;
import eone.base.model.PO;
import eone.base.model.X_AD_Permission;

//org.compiere.process.VerifyRole
/*
 * Muc dich:
 * Xoa rac phan phan quyen khi trien khai
 * Gan phan quyen cho nhung Role co vai tro o Cua Hang (SALEMT)
 */
public class VerifyRole extends SvrProcess
{
	protected void prepare()
	{
	
	}	//	prepare
	
	private int BATCH_SIZE = Env.getBatchSize(getCtx());
	
	protected String doIt() throws java.lang.Exception
	{
		int AD_Role_ID = getRecord_ID();
		ClearOrgAccess(AD_Role_ID);
		ClearUserAccess(AD_Role_ID);
		ClearFormAccess_SaleMT(AD_Role_ID);
		return "";
	}	//	doIt
	
	private void ClearOrgAccess(int AD_Role_ID) {
		String sql = "Delete From AD_Role_OrgAccess "+
				" Where (AD_Role_ID not in (Select AD_Role_ID From AD_Role Where IsActive = 'Y') Or AD_Org_ID not in (Select AD_Org_ID From AD_Org Where IsActive = 'Y'))"+
				"		And AD_Client_ID = ?"	;
		DB.executeUpdate(sql, getAD_Client_ID(), get_TrxName());
	}
	
	private void ClearUserAccess(int AD_Role_ID) {
		String sql = "Delete From AD_User_Roles "+
				" Where (AD_Role_ID not in (Select AD_Role_ID From AD_Role Where IsActive = 'Y') Or AD_User_ID not in (Select AD_User_ID From AD_User Where IsActive = 'Y'))"+
				"		And AD_Client_ID = ?"	;
		DB.executeUpdate(sql, getAD_Client_ID(), get_TrxName());
	}
	
	private void ClearFormAccess_SaleMT(int AD_Role_ID) throws java.lang.Exception {
		String sql = "Delete From AD_Permission "+
				" Where (AD_Role_ID not in (Select AD_Role_ID From AD_Role Where IsActive = 'Y') Or AD_Form_Sale_ID not in (Select AD_Form_Sale_ID From AD_Form_Sale Where IsActive = 'Y'))"+
				"		And AD_Client_ID = ?"	;
		DB.executeUpdate(sql, getAD_Client_ID(), get_TrxName());
		
		//Insert cac form chua khai bao
		sql = "Select AD_Form_Sale_ID From AD_Form_Sale Where AD_Form_Sale_ID not in (Select AD_Form_Sale_ID From AD_Permission Where AD_Role_ID = ?)";
		ResultSet rs = null;
		PreparedStatement ps = DB.prepareCall(sql);
		ps.setInt(1, AD_Role_ID);
		rs = ps.executeQuery();
		
		MPermission per = null;
		List<List<Object>> ListItem = new ArrayList<List<Object>>();
		String sqlInsert = PO.getSqlInsert(X_AD_Permission.Table_ID, null);
		
		while (rs.next()) {
			int sequence = DB.getNextID(getCtx(), X_AD_Permission.Table_Name, null);
			per = new MPermission(getCtx(), 0, null);
			per.setAD_Role_ID(AD_Role_ID);
			per.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
			per.setAD_Client_ID(getAD_Client_ID());
			per.setAD_Form_Sale_ID(rs.getInt("AD_Form_Sale_ID"));
			per.setIsPrinted(true);
			per.setIsDeleteable(true);
			per.setIsInsertRecord(true);
			per.setIsUpdateable(true);
			List<Object> item = PO.getBatchValueList(per, X_AD_Permission.Table_ID, get_TrxName(), sequence);
			ListItem.add(item);
			
			if (ListItem.size() >= BATCH_SIZE) {
				DB.excuteBatch(sqlInsert, ListItem, get_TrxName());
				ListItem.clear();
			}
		}
		
		if (ListItem.size() > 0) {
			DB.excuteBatch(sqlInsert, ListItem, get_TrxName());
			ListItem.clear();
		}
		
	}
}	