package eone.base.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.model.MBPGroup;
import eone.base.model.MBPartner;
import eone.base.model.MBPartnerInfo;
import eone.base.model.MCurrency;
import eone.base.model.MInOut;
import eone.base.model.MInOutLine;
import eone.base.model.MProduct;
import eone.base.model.MWarehouse;
import eone.base.model.PO;
import eone.base.model.X_C_Account;
import eone.base.model.X_C_BP_Group;
import eone.base.model.X_C_BPartner;
import eone.base.model.X_C_BPartnerInfo;
import eone.base.model.X_M_InOut;
import eone.base.model.X_M_InOutLine;

//org.compiere.process.GetDataSaleMT
public class GetDataSaleMT extends SvrProcess
{
	private int BATCH_SIZE = Env.getBatchSize(getCtx());
	protected void prepare()
	{
	
	}	//	prepare
	
	private List<Object> listColHeader = null;
	private List<List<Object>> listRowHear = null;
	private List<Object> listColLine = null;
	private List<List<Object>> listRowLine = null;
	
	private String sqlInsertHeader = "";
	private String sqlInsertLine = "";
	
	protected String doIt() throws java.lang.Exception
	{
		getCustomer();
		getExportData();
		getReturnData();
		return "Success!";
	}	//	doIt
	
	/*
	 * CUSTOMERS
	 * 
	 * customerCode, shopCode, firstName,  lastName,  dateOfBirth,
		placeOfBirth,  occupationCode, 
		 occupationOther,  maritalStatus, 
		 sexCode,  bloodCode,  raceCode,  religionCode, 
		customerGroupCode, idNo, idNoIssuedDate, idNoIssuedPlace, 
		passportNo, passportNoIssuedDate, 
		passportNoExpiryDate, passportNoIssuedPlace,
		 tel, fax, mobilePhone, emailAddress, countryCode, 
		provinceCityCode, provinceCityOther, 
		districtCode, districtCode, communesWards, houseStreet, [address], 
		workingOffice, officeAddress, numberMark, 
		actionLog, isActive, picturePath, remark, createDate, createBy, 
		GETDATE() updateDate, lastUpdateBy, customerTypeCode,
		[money], yearOfBirth, taxCode, oldCode, barCode, 
		colSearch, eventId,  isPrivate,3 synAction,storeCode
	 */
	private void getCustomer() throws java.lang.Exception{
		String sql = "{Call GetXML_Customers_Read}";
		CallableStatement cs = DB.prepareCallExtend(sql, get_TrxName());
		ResultSet rs = cs.executeQuery();
		sqlInsertHeader = PO.getSqlInsert(X_C_BPartner.Table_ID, get_TrxName());
		sqlInsertLine = PO.getSqlInsert(X_C_BPartnerInfo.Table_ID, get_TrxName());
		listRowHear = new ArrayList<List<Object>>();
		
		listRowLine = new ArrayList<List<Object>>();
		
		int C_BP_Group_ID = MBPGroup.getC_BP_Group_ID(X_C_BP_Group.GROUPTYPE_Customer);
		
		MBPartner item = null;
		MBPartnerInfo itemInfo = null;
		int actionSyn = 0;
		String key = "";
		while (rs.next()) {
			actionSyn = rs.getInt("synAction");
			
			//Inserted
			if (actionSyn == 1)
			{
				item = new MBPartner(getCtx());
				itemInfo = new MBPartnerInfo(getCtx(), 0, get_TrxName());
				int id = DB.getNextID(getCtx(), X_C_BPartner.Table_Name, get_TrxName());
				item.setValue(rs.getString("customerCode"));
				item.setKey_Original(item.getValue());//Luu key nay khong thay doi thuan tien cho viec doi chieu
				item.setIsAutoCreate(false);
				item.setOriginal(X_C_BPartner.ORIGINAL_SALEMT);
				
				item.setName(rs.getString("lastName") + " " + rs.getString("firstName"));
				item.setName2(item.getName());
				item.setIsActive(true);
				item.setDescription("SALE MT");
				item.setIsCustomer(true);
				item.setIsEmployee(false);
				item.setIsVendor(false);
				item.setIsBankAccount(false);
				item.setAD_Client_ID(getAD_Client_ID());
				item.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
				item.setC_BP_Group_ID(C_BP_Group_ID);
				Timestamp create = rs.getTimestamp("createDate");
				if(create == null)
					create = new Timestamp(new Date().getTime());
				item.set_ValueNoCheck("Created", create);
				item.set_ValueNoCheck("Updated", rs.getTimestamp("updateDate"));
				item.set_ValueNoCheck("CreatedBy", 100);
				item.set_ValueNoCheck("UpdatedBy", 100);
				item.setSendEMail(false);
				listColHeader = PO.getBatchValueList(item, X_C_BPartner.Table_ID, get_TrxName(), id);
				listRowHear.add(listColHeader);
				
				//Bpartner info:
				itemInfo.setC_BPartner_ID(id);
				itemInfo.setAddress(rs.getString("Address"));
				itemInfo.setPhone(rs.getString("mobilePhone"));
				itemInfo.setBirthday(rs.getTimestamp("dateOfBirth"));
				itemInfo.setBirthPlace(rs.getString("placeOfBirth"));
				String gender = rs.getString("sexCode");
				if(gender != null  && gender.contains("1"))
					itemInfo.setGender(X_C_BPartnerInfo.GENDER_Male);
				if(gender != null  &&  gender.contains("2"))
					itemInfo.setGender(X_C_BPartnerInfo.GENDER_Female);
				itemInfo.setCardID(rs.getString("IdNo"));
				itemInfo.setDateIssue(rs.getTimestamp("IdNoIssuedDate"));
				itemInfo.setPlaceIssue(rs.getString("IdNoIssuedPlace"));
				itemInfo.set_ValueNoCheck("Created", create);
				itemInfo.set_ValueNoCheck("Updated", rs.getTimestamp("updateDate"));
				itemInfo.set_ValueNoCheck("CreatedBy", 100);
				itemInfo.set_ValueNoCheck("UpdatedBy", 100);
				itemInfo.setAD_Client_ID(getAD_Client_ID());
				itemInfo.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
				itemInfo.setIsActive(true);
				
				int idInfo = DB.getNextID(getCtx(), X_C_BPartnerInfo.Table_Name, get_TrxName());
				
				listColLine = PO.getBatchValueList(itemInfo, X_C_BPartnerInfo.Table_ID, get_TrxName(), idInfo);
				listRowLine.add(listColLine);
				
				if (listRowHear.size() >= BATCH_SIZE) {
					DB.excuteBatch(sqlInsertHeader, listRowHear, get_TrxName());
					listRowHear.clear();
					
					DB.excuteBatch(sqlInsertLine, listRowLine, get_TrxName());
					listRowLine.clear();
					
				}
			}//End Inserted
				
			//Deleted
			if (actionSyn == 3) {
				if ("".equals(key))
					key =  "'" + rs.getString("customerCode") + "'";
				else
					key = key + ",'" + rs.getString("customerCode") + "'";
			}//End Deleted
			
		}
		
		if (listRowHear.size() > 0) {
			DB.excuteBatch(sqlInsertHeader, listRowHear, get_TrxName());
			listRowHear.clear();
			
			DB.excuteBatch(sqlInsertLine, listRowLine, get_TrxName());
			listRowLine.clear();
		}
		
		DB.close(rs, cs);
		//Xoa ban ghi co trang thai xoa tu goc
		if (!"".equals(key)) {
			String sqlDel = "Delete From C_BPartnerInfo Where C_BPartner_ID in (Select C_BPartner_ID From C_Bpartner Where Original in (" + key + "))";
			DB.executeUpdate(sqlDel);
			
			sqlDel = "Delete From C_Bpartner Where Original in (" + key + ")";
			DB.executeUpdate(sqlDel);
		}
	}
	
	/*
	 * EXPORT_DETAIL
	 * 
	 * EXPORT_CODE,  EXPORT_DATE,  CUSTOMER_ID,  STORE_CODE,  CURRENCY_ID,   TOTAL_AMOUNT, 
		  TOTAL_DISCOUNT,  TOTAL_PAYMENTS,  TOTAL_PROMOTION, 3 headSyncAction, 
		  TYPE_EXPORT,  PRODUCT_ID,  QUANTITY,  AMOUNT,  PRICE_SALES,  DISCOUNT,   PROMOTION
	 */
	private int C_DocType_ID = 200027;
	private int account_dr_id = 0;
	private int account_cr_id = 0;
	private int account_tax_id = 0;
	private int account_cogs_id = 0;
	private int account_revenue_id = 0;
	private void getExportData() throws java.lang.Exception{
		String sql = "{Call GetXML_SalesData_Read}";
		CallableStatement cs = DB.prepareCallExtend(sql, get_TrxName());
		ResultSet rs = cs.executeQuery();
		sqlInsertHeader = PO.getSqlInsert(X_M_InOut.Table_ID, get_TrxName());
		sqlInsertLine = PO.getSqlInsert(X_M_InOutLine.Table_ID, get_TrxName());
		listRowHear = new ArrayList<List<Object>>();
		List<Integer> arrID = new ArrayList<Integer>();
		listRowLine = new ArrayList<List<Object>>();
		String exportCodeOld = "";
		getAccount(C_DocType_ID);
		
		MInOut item = null;
		MInOutLine line = null;
		int headerSynAction = 0;
		String key = "";
		int i = 0;
		while (rs.next()) {
			headerSynAction = rs.getInt("headSyncAction");
			
			MCurrency curr = MCurrency.get(getCtx(), rs.getString("CURRENCY_ID"));
			
			//Inserted
			if (headerSynAction == 1)
			{
				int idheader = 0;
				if (!exportCodeOld.equals(rs.getString("EXPORT_CODE")))
				{
					item = new MInOut(getCtx(), 0, get_TrxName());
					item.setDocStatus(X_M_InOut.DOCSTATUS_Drafted);
					item.setProcessed(false);
					item.setPosted(true);
					item.setApproved("Y");
					item.setCanceled("Y");
					item.setDocumentNo(rs.getString("EXPORT_CODE"));
					item.setDateAcct(rs.getTimestamp("EXPORT_DATE"));
					item.setDescription("Sync From SaleMT");
					//
					item.setC_DocType_ID(C_DocType_ID);
					item.setC_Currency_ID(curr.getC_Currency_ID());
					item.setCurrencyRate(Env.ONE);
					item.setAccount_COGS_ID(account_cogs_id);
					item.setAccount_Cr_ID(account_cr_id);
					item.setAccount_Dr_ID(account_dr_id);
					item.setAccount_REV_ID(account_revenue_id);
					item.setAccount_Tax_ID(account_tax_id);
					//
					String [] wh = rs.getString("STORE_CODE").split("@");
					int wh_id = Integer.parseInt(wh[0].toString());
					item.setM_Warehouse_Cr_ID(wh_id);
					item.setC_BPartner_Dr_ID(wh_id); //TODO: Do doi tuong va kho duoc tao tu dong tu phong ban nen cung ID		
					item.setC_Tax_ID(104);
					
					item.setIncludeTaxTab(X_M_InOut.INCLUDETAXTAB_TAXS);
					item.set_ValueNoCheck("Created", rs.getTimestamp("EXPORT_DATE"));
					item.set_ValueNoCheck("Updated", rs.getTimestamp("EXPORT_DATE"));
					item.set_ValueNoCheck("CreatedBy", 100);
					item.set_ValueNoCheck("UpdatedBy", 100);
					item.setAD_Client_ID(getAD_Client_ID());
					item.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
					item.setOriginal(X_M_InOut.ORIGINAL_SALEMT);
					idheader = DB.getNextID(getCtx(), X_M_InOut.Table_Name, get_TrxName());
					listColHeader = PO.getBatchValueList(item, X_M_InOut.Table_ID, get_TrxName(), idheader);
					listRowHear.add(listColHeader);
					arrID.add(idheader);
				}//Insert header
				
				//Line
				line = new MInOutLine(getCtx(), 0, get_TrxName());
				line.setM_InOut_ID(idheader);
				Object [] data = MProduct.get(getCtx(), rs.getString("PRODUCT_ID"));
				if (data != null) {
					line.setM_Product_ID(Integer.parseInt(data[0].toString()));
					line.setC_UOM_ID(Integer.parseInt(data[1].toString()));
				}
				line.setQty(rs.getBigDecimal("QUANTITY"));
				line.setPrice(rs.getBigDecimal("PRICE_SALES"));
				String typeExport = rs.getString("TYPE_EXPORT");
				BigDecimal discount = rs.getBigDecimal("DISCOUNT");
				String discountType = "";
				if ("SALES".equalsIgnoreCase(typeExport))
					discountType = "NONE";
				else
					discountType = "PROM";
				if (discount.compareTo(Env.ZERO) > 0) {
					discountType = "DISC";
					line.setDiscountAmt(discount);
				}
				line.setTaxBaseAmt(rs.getBigDecimal("AMOUNT"));
				line.setAmount(rs.getBigDecimal("AMOUNT").divide(new BigDecimal("1.1"), Env.roundAmount, RoundingMode.HALF_UP));
				line.setTaxAmt(line.getTaxBaseAmt().subtract(line.getAmount()));
				line.setLine(i++);
				line.setDiscountType(discountType);
				line.set_ValueNoCheck("Created", rs.getTimestamp("EXPORT_DATE"));
				line.set_ValueNoCheck("Updated", rs.getTimestamp("EXPORT_DATE"));
				line.set_ValueNoCheck("CreatedBy", 100);
				line.set_ValueNoCheck("UpdatedBy", 100);
				line.setAD_Client_ID(getAD_Client_ID());
				line.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
				line.setIsActive(true);
				line.setOriginal(X_M_InOutLine.ORIGINAL_SALEMT);
				int idInfo = DB.getNextID(getCtx(), X_M_InOutLine.Table_Name, get_TrxName());
				
				listColLine = PO.getBatchValueList(line, X_M_InOutLine.Table_ID, get_TrxName(), idInfo);
				listRowLine.add(listColLine);
				
				if (listRowLine.size() >= BATCH_SIZE) {
					DB.excuteBatch(sqlInsertHeader, listRowHear, get_TrxName());
					listRowHear.clear();
					
					DB.excuteBatch(sqlInsertLine, listRowLine, get_TrxName());
					listRowLine.clear();
					
				}
			}//End Inserted
				
			//Deleted
			if (headerSynAction == 3) {
				if ("".equals(key))
					key =  "'" + rs.getString("EXPORT_CODE") + "'";
				else
					key = key + ",'" + rs.getString("EXPORT_CODE") + "'";
			}//End Deleted
			exportCodeOld = rs.getString("EXPORT_CODE");
		}
		
		if (listRowLine.size() > 0) {
			DB.excuteBatch(sqlInsertHeader, listRowHear, get_TrxName());
			listRowHear.clear();
			
			DB.excuteBatch(sqlInsertLine, listRowLine, get_TrxName());
			listRowLine.clear();
		}
		
		//Xoa ban ghi co trang thai xoa tu goc
		if (!"".equals(key)) {
			String sqlDel = "Delete From M_InOutLine Where M_InOut_ID in (Select M_InOut_ID From M_InOut Where DocumentNo in (" + key + "))";
			DB.executeUpdate(sqlDel);
			
			sqlDel = "Delete From M_InOut Where DocumentNo in (" + key + ")";
			DB.executeUpdate(sqlDel);
			
			sqlDel = "Delete From Fact_Acct Where DocumentNo in (" + key + ")";
			DB.executeUpdate(sqlDel);
		}
		DB.close(rs, cs);
		
		for(int n = 0; n < arrID.size(); n++) {
			item = new MInOut(getCtx(), arrID.get(n), get_TrxName());
			item.processIt(DocAction.ACTION_Complete, 0);
		}
	}
	
	private void getAccount(int C_DocType_ID) throws Exception {
		String sql = "Select Account_ID, TypeAccount From C_Account Where C_DocType_ID = ? And IsDefault = 'Y'";
		PreparedStatement ps = DB.prepareCall(sql);
		ps.setInt(1, C_DocType_ID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			if (X_C_Account.TYPEACCOUNT_DebitAccount.equals(rs.getString("TypeAccount")))
				account_dr_id = rs.getInt("Account_ID");
			if (X_C_Account.TYPEACCOUNT_CreditAccount.equals(rs.getString("TypeAccount")))
				account_cr_id = rs.getInt("Account_ID");
			if (X_C_Account.TYPEACCOUNT_RevenueAccount.equals(rs.getString("TypeAccount")))
				account_revenue_id = rs.getInt("Account_ID");
			if (X_C_Account.TYPEACCOUNT_TaxOutputAccount.equals(rs.getString("TypeAccount")))
				account_tax_id = rs.getInt("Account_ID");
			if (X_C_Account.TYPEACCOUNT_COGSAccount.equals(rs.getString("TypeAccount")))
				account_cogs_id = rs.getInt("Account_ID");
			
		}
		DB.close(rs, ps);
	}
	
	//Return data
	/*
	 * h.RETURN_CODE, RETURN_DATE, STORE_CODE,	PRODUCT_ID ,QUANTITY, PRICE_PRODUCT, REASON, 1 headSyncAction
	 */
	
	private void getReturnData() throws java.lang.Exception{
		C_DocType_ID = 200028;
		String sql = "{Call GetXML_ItemReturnData_Read}";
		CallableStatement cs = DB.prepareCallExtend(sql, get_TrxName());
		ResultSet rs = cs.executeQuery();
		sqlInsertHeader = PO.getSqlInsert(X_M_InOut.Table_ID, get_TrxName());
		sqlInsertLine = PO.getSqlInsert(X_M_InOutLine.Table_ID, get_TrxName());
		listRowHear = new ArrayList<List<Object>>();
		List<Integer> arrID = new ArrayList<Integer>();
		listRowLine = new ArrayList<List<Object>>();
		String exportCodeOld = "";
		getAccount(C_DocType_ID);
		
		MInOut item = null;
		MInOutLine line = null;
		int headerSynAction = 0;
		String key = "";
		int i = 0;
		while (rs.next()) {
			headerSynAction = rs.getInt("headSyncAction");
			
			MCurrency curr = MCurrency.get(getCtx(), "VND");
			
			//Inserted
			if (headerSynAction == 1)
			{
				int idheader = 0;
				if (!exportCodeOld.equals(rs.getString("RETURN_CODE")))
				{
					item = new MInOut(getCtx(), 0, get_TrxName());
					item.setDocStatus(X_M_InOut.DOCSTATUS_Drafted);
					item.setProcessed(false);
					item.setPosted(true);
					item.setApproved("Y");
					item.setCanceled("Y");
					item.setDocumentNo(rs.getString("RETURN_CODE"));
					item.setDateAcct(rs.getTimestamp("RETURN_DATE"));
					item.setDescription(rs.getString("STORE_CODE") + " _ " + rs.getString("REASON"));
					//
					item.setC_DocType_ID(C_DocType_ID);
					item.setC_Currency_ID(curr.getC_Currency_ID());
					item.setCurrencyRate(Env.ONE);
					item.setAccount_Cr_ID(account_cr_id);
					item.setAccount_Dr_ID(account_dr_id);
					//
					String [] wh = rs.getString("STORE_CODE").split("@");
					int wh_id = Integer.parseInt(wh[0].toString());
					item.setM_Warehouse_Dr_ID(MWarehouse.getDefault(getCtx(), null).getM_Warehouse_ID());
					item.setM_Warehouse_Cr_ID(wh_id);
					
					item.set_ValueNoCheck("Created", rs.getTimestamp("RETURN_DATE"));
					item.set_ValueNoCheck("Updated", rs.getTimestamp("RETURN_DATE"));
					item.set_ValueNoCheck("CreatedBy", 100);
					item.set_ValueNoCheck("UpdatedBy", 100);
					item.setAD_Client_ID(getAD_Client_ID());
					item.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
					item.setOriginal(X_M_InOut.ORIGINAL_SALEMT);
					idheader = DB.getNextID(getCtx(), X_M_InOut.Table_Name, get_TrxName());
					listColHeader = PO.getBatchValueList(item, X_M_InOut.Table_ID, get_TrxName(), idheader);
					listRowHear.add(listColHeader);
					arrID.add(idheader);
				}//Insert header
				
				//Line
				line = new MInOutLine(getCtx(), 0, get_TrxName());
				line.setM_InOut_ID(idheader);
				Object [] data = MProduct.get(getCtx(), rs.getString("PRODUCT_ID"));
				if (data != null) {
					line.setM_Product_ID(Integer.parseInt(data[0].toString()));
					line.setC_UOM_ID(Integer.parseInt(data[1].toString()));
				}
				line.setQty(rs.getBigDecimal("QUANTITY"));
				line.setPrice(rs.getBigDecimal("PRICE_PRODUCT"));
				line.setAmount(line.getQty().multiply(line.getPrice()));
				line.setLine(i++);
				line.set_ValueNoCheck("Created", rs.getTimestamp("RETURN_DATE"));
				line.set_ValueNoCheck("Updated", rs.getTimestamp("RETURN_DATE"));
				line.set_ValueNoCheck("CreatedBy", 100);
				line.set_ValueNoCheck("UpdatedBy", 100);
				line.setAD_Client_ID(getAD_Client_ID());
				line.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
				line.setIsActive(true);
				line.setOriginal(X_M_InOutLine.ORIGINAL_SALEMT);
				int idInfo = DB.getNextID(getCtx(), X_M_InOutLine.Table_Name, get_TrxName());
				
				listColLine = PO.getBatchValueList(line, X_M_InOutLine.Table_ID, get_TrxName(), idInfo);
				listRowLine.add(listColLine);
				
				if (listRowLine.size() >= BATCH_SIZE) {
					DB.excuteBatch(sqlInsertHeader, listRowHear, get_TrxName());
					listRowHear.clear();
					
					DB.excuteBatch(sqlInsertLine, listRowLine, get_TrxName());
					listRowLine.clear();
					
				}
			}//End Inserted
				
			//Deleted
			if (headerSynAction == 3) {
				if ("".equals(key))
					key =  "'" + rs.getString("RETURN_CODE") + "'";
				else
					key = key + ",'" + rs.getString("RETURN_CODE") + "'";
			}//End Deleted
			exportCodeOld = rs.getString("RETURN_CODE");
		}
		
		if (listRowLine.size() > 0) {
			DB.excuteBatch(sqlInsertHeader, listRowHear, get_TrxName());
			listRowHear.clear();
			
			DB.excuteBatch(sqlInsertLine, listRowLine, get_TrxName());
			listRowLine.clear();
		}
		
		//Xoa ban ghi co trang thai xoa tu goc
		if (!"".equals(key)) {
			String sqlDel = "Delete From M_InOutLine Where M_InOut_ID in (Select M_InOut_ID From M_InOut Where DocumentNo in (" + key + "))";
			DB.executeUpdate(sqlDel);
			
			sqlDel = "Delete From M_InOut Where DocumentNo in (" + key + ")";
			DB.executeUpdate(sqlDel);
			
			sqlDel = "Delete From Fact_Acct Where DocumentNo in (" + key + ")";
			DB.executeUpdate(sqlDel);
		}
		DB.close(rs, cs);
		
		for(int n = 0; n < arrID.size(); n++) {
			item = new MInOut(getCtx(), arrID.get(n), get_TrxName());
			item.processIt(DocAction.ACTION_Complete, 0);
		}
	}
}	
