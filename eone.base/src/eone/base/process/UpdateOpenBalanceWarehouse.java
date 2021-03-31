

package eone.base.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MStorage;
import org.compiere.model.MYear;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 * @author Admin
 * Chot so du vat tu hang hoa cap nhat vao bang M_Storage
 * N: Nam hien tai can tinh toan cap nhat lai kho.
 * Tap hop chot so du cho nam N. 
 * Tinh toan lai so phat sinh cho nam N+1.
 * VD: can chot so du cho nam 2020
 * 		1. Ton cuoi bao nhieu dua vao ngay 01/01/2021.
 * 		2. Quet ra soat cap nhat lai ps nam 2021.
 */

public class UpdateOpenBalanceWarehouse extends SvrProcess {
	

	private int p_Year_ID = 0;
	private String sqlInsert = "";
	private int BATCH_SIZE = 0;
	private List<Object> lstColumn = null;
	private List<List<Object>> lstRows = null;

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] params = getParameter();
		for (ProcessInfoParameter p : params)
		{
			if ( p.getParameterName().equals("C_Year_ID") )
				p_Year_ID = p.getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + p.getParameterName());
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		//Lay cau lenh sqlInsert theo batch
		sqlInsert = MStorage.sqlInsert;
		BATCH_SIZE = Env.getBatchSize(getCtx());
		lstColumn = new ArrayList<Object>();
		lstRows = new ArrayList<List<Object>>();
		
		MYear year = MYear.get(getCtx(), p_Year_ID, get_TrxName());
		//Lay sang ngay 01/01 cua nam sau thay vi 31/12 cua nam truoc. Vi co the lech gio, phut, giay.
		Timestamp startDate = TimeUtil.getFinalYear(year.getFiscalYear(), true);
		
		//Lay ngay 31/12/N de chot so du den ngay nay.
		Timestamp endDate = TimeUtil.getFinalYear(year.getFiscalYear(), false);
		endDate = TimeUtil.addDays(endDate, 1);//
		int AD_Client_ID = Env.getAD_Client_ID(getCtx());
		
		//Xoa ban ghi tong hop cua nam N+1 di de tinh toan lai va cap nhat lai.
		
		DB.executeUpdate("Delete M_Storage Where DateTrx >= ? And AD_Client_ID = ?", new Object [] {endDate, AD_Client_ID}, true, get_TrxName());
		
		//Tinh toan so du dau ky cua nam N de Insert vao nam N + 1
		insertOpenBalance(AD_Client_ID, startDate, endDate);
		
		//Insert so lieu phat sinh
		insertIncurred(AD_Client_ID, startDate, endDate);
		
		return "inserted ";
	}

	
	
	private void insertOpenBalance(int AD_Client_ID, Timestamp startDate, Timestamp endDate) throws Exception {
		//TypeInOut: IN: Nhap; OP: Day ky; OU: XUAT
		String sql = 
				" Select Sum(Qty) Qty, Sum(Amount) Amount, "+
				"		Case when Sum(Qty) > 0 Then Sum(Amount)/Sum(Qty) Else 0 End Price, M_Product_ID, M_Warehouse_ID "+
				" From "+
				" ( "+
				" 	select Sum(Qty) Qty, Sum(Amount) Amount, M_Product_ID, M_Warehouse_ID from M_Storage "+ 
				" 	Where (TypeInOut = 'IN' Or TypeInOut = 'OP')  And AD_Client_ID = ? And DateTrx >= ? And DateTrx < ? "+ //#1,#2,#3
				" 	Group by M_Product_ID, M_Warehouse_ID "+
				" 	Union All "+
				" 	select -Sum(Qty) Qty, -Sum(Amount) Amount, M_Product_ID, M_Warehouse_ID from M_Storage "+ 
				" 	Where TypeInOut = 'OU'  And AD_Client_ID = ? And DateTrx >= ? And DateTrx < ? "+ //#4,#5,#6
				" 	Group by M_Product_ID, M_Warehouse_ID "+
				" )B Group by M_Product_ID, M_Warehouse_ID  "+
				" Having Sum(Qty) != 0";
		ResultSet rs = null;
		PreparedStatement ps = DB.prepareCall(sql);
		ps.setInt(1, AD_Client_ID);
		ps.setTimestamp(2, startDate);
		ps.setTimestamp(3, endDate);
		
		ps.setInt(4, AD_Client_ID);
		ps.setTimestamp(5, startDate);
		ps.setTimestamp(6, endDate);
		
		rs = ps.executeQuery();
		MStorage storage = null;
		while (rs.next()) {
			lstColumn = new ArrayList<Object>();
			storage = new MStorage(getCtx(), 0, null);
			storage.setM_Product_ID(rs.getInt("M_Product_ID"));
			storage.setM_Warehouse_ID(rs.getInt("M_Warehouse_ID"));
			storage.setQty(rs.getBigDecimal("Qty"));
			storage.setPrice(rs.getBigDecimal("Price"));
			storage.setAmount(rs.getBigDecimal("Amount"));
			storage.setTypeInOut(MStorage.TYPEINOUT_Opening);
			storage.setDateTrx(endDate);
			int ID = DB.getNextID(AD_Client_ID, MStorage.Table_Name, get_TrxName());
			lstColumn = PO.getBatchValueList(storage, MStorage.Table_ID, get_TrxName(), ID);
			lstRows.add(lstColumn);
			if (lstRows.size() >= BATCH_SIZE) {
				DB.excuteBatch(sqlInsert, lstRows, get_TrxName());
				lstRows.clear();
			}
		}
		if (lstRows.size() > 0) {
			DB.excuteBatch(sqlInsert, lstRows, get_TrxName());
			lstRows.clear();
		}
	}
	
	private void insertIncurred(int AD_Client_ID, Timestamp startDate, Timestamp endDate) throws Exception {
		String sql = 
				" select 'IN' DocType, il.M_InOutLine_ID, i.M_Warehouse_Dr_ID M_Warehouse_ID, i.DateAcct, il.M_Product_ID, il.Qty, il.Price, il.Amount "+
				" From M_InOut i Inner Join M_InOutLine il On i.M_InOut_ID = il.M_InOut_ID "+
				" Where i.C_DocType_ID in (Select C_DocType_ID From C_DocType Where DocType in ('BAL','INP')) "+
				" 	 And i.AD_Client_ID = ? And i.DateAcct >= ? And i.DateAcct < ?  And i.DocStatus = 'CO'"+
				" Union All "+
				" select 'OU' DocType, il.M_InOutLine_ID, i.M_Warehouse_Cr_ID M_Warehouse_ID, i.DateAcct, il.M_Product_ID, il.Qty, il.Price, il.Amount "+
				" From M_InOut i Inner Join M_InOutLine il On i.M_InOut_ID = il.M_InOut_ID "+
				" Where i.C_DocType_ID in (Select C_DocType_ID From C_DocType Where DocType in ('OUT')) "+
				" 	 And i.AD_Client_ID = ? And i.DateAcct >= ? And i.DateAcct < ?  And i.DocStatus = 'CO' ";
		ResultSet rs = null;
		PreparedStatement ps = DB.prepareCall(sql);
		ps.setInt(1, AD_Client_ID);
		ps.setTimestamp(2, startDate);
		ps.setTimestamp(3, endDate);
		
		ps.setInt(4, AD_Client_ID);
		ps.setTimestamp(5, startDate);
		ps.setTimestamp(6, endDate);
		
		rs = ps.executeQuery();
		MStorage storage = null;
		while (rs.next()) {
			String docType = rs.getString("DocType");
			storage = new MStorage(getCtx(), 0, null);
			storage.setRecord_ID(rs.getInt("M_InOutLine_ID"));
			storage.setM_Product_ID(rs.getInt("M_Product_ID"));
			storage.setM_Warehouse_ID(rs.getInt("M_Warehouse_ID"));
			storage.setQty(rs.getBigDecimal("Qty"));
			storage.setPrice(rs.getBigDecimal("Price"));
			storage.setAmount(rs.getBigDecimal("Amount"));
			storage.setTypeInOut(docType);
			storage.setDateTrx(rs.getTimestamp("DateAcct"));
			int ID = DB.getNextID(AD_Client_ID, MStorage.Table_Name, get_TrxName());
			lstColumn = PO.getBatchValueList(storage, MStorage.Table_ID, get_TrxName(), ID);
			lstRows.add(lstColumn);
			if (lstRows.size() >= BATCH_SIZE) {
				DB.excuteBatch(sqlInsert, lstRows, get_TrxName());
				lstRows.clear();
			}
		}
		if (lstRows.size() > 0) {
			DB.excuteBatch(sqlInsert, lstRows, get_TrxName());
			lstRows.clear();
		}
	}
}
