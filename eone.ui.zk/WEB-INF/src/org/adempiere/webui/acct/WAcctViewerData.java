
package org.adempiere.webui.acct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MLookupFactory;
import org.compiere.model.X_Fact_Acct;
import org.compiere.report.core.RColumn;
import org.compiere.report.core.RModel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;


public class WAcctViewerData
{
	public int AD_Client_ID = 0;
	
	public int AD_Table_ID = 0;
	
	public int Record_ID = 0;
	
	public int WindowNo;
	
	public String DocNo = "";
	
	public int AD_Org_ID = 0;
	
	public int AD_Department_ID = 0;
	
	public int C_BPartner_ID = 0;
	
	public int M_Product_ID = 0;
	
	public int M_Warehouse_ID = 0;
	
	public int A_Asset_ID = 0;
	
	public int C_TypeCost_ID = 0;
	
	public int C_TypeRevenue_ID = 0;
	
	public Timestamp DateFrom = null;
	
	public Timestamp DateTo = null;
	
	public String Account = "";
	
	public String sqlZoomLogic = "";

	public HashMap<String,String> whereInfo = new HashMap<String,String>();
	
	public HashMap<String,Integer> tableInfo = new HashMap<String,Integer>();

	private static final CLogger log = CLogger.getCLogger(WAcctViewerData.class);
	
	private String 		filterClause	= null;
	public ArrayList<String> sortColumns = null;

	public WAcctViewerData (Properties ctx, int windowNo, int ad_Client_ID, int ad_Table_ID)
	{
		WindowNo = windowNo;
		AD_Client_ID = ad_Client_ID;
		
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "AD_Client_ID");
		
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), "AD_Client_ID");
		
		AD_Table_ID = ad_Table_ID;

		
	} 
	
	public WAcctViewerData (Properties ctx, int windowNo, int ad_Client_ID, int ad_Table_ID, String sqlZoomLogic)
	{
		WindowNo = windowNo;
		AD_Client_ID = ad_Client_ID;
		
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "AD_Client_ID");
		
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), "AD_Client_ID");
		
		AD_Table_ID = ad_Table_ID;

		this.sqlZoomLogic = sqlZoomLogic;
	} 
	
	public void dispose()
	{

		whereInfo.clear();
		whereInfo = null;

		Env.clearWinContext(WindowNo);
	} // dispose
	
	
	protected String getButtonText (String tableName, String columnName, String selectSQL)
	{
		StringBuilder sql = new StringBuilder ("SELECT (");
		Language language = Env.getLanguage(Env.getCtx());
		
		sql.append(MLookupFactory.getLookup_TableDirEmbed(language, columnName, "avd"))
			.append(") FROM ").append(tableName).append(" avd WHERE avd.").append(selectSQL);
		String retValue = "<" + selectSQL + ">";

		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = DB.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next())
				retValue = rs.getString(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, stmt);
			rs = null;
			stmt = null;
		}
		return retValue;
	} // getButtonText

	/**************************************************************************
	/**
	 *  Create Query and submit
	 *  @return Report Model
	 */
	
	protected RModel query()
	{
		//  Set Where Clause
	
		StringBuilder whereClause = new StringBuilder();
		
		//Neu xem hach toan
		if (AD_Table_ID != 0 && Record_ID != 0) {
			if (whereClause.length() > 0)
				whereClause.append(" AND ");

			whereClause.append(RModel.TABLE_ALIAS).append(".AD_Table_ID=").append(AD_Table_ID)
				.append(" AND ").append(RModel.TABLE_ALIAS).append(".Record_ID=").append(Record_ID);
			
		
		} else {
			//Neu tim kiem tu Account Infor
			Iterator<String> it = whereInfo.values().iterator();
			
			while (it.hasNext())
			{
				String where = it.next();
			
				if (where != null && where.length() > 0)    //  add only if not empty
				{
					if (whereClause.length() > 0)
						whereClause.append(" AND ");
				
					whereClause.append(RModel.TABLE_ALIAS).append(".").append(where);
				}
			}
			
			if (DateFrom != null || DateTo != null)
			{
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
			
				if (DateFrom != null && DateTo != null)
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) BETWEEN ")
						.append(DB.TO_DATE(DateFrom)).append(" AND ").append(DB.TO_DATE(DateTo));
				else if (DateFrom != null)
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) >= ")
						.append(DB.TO_DATE(DateFrom));
				else    //  DateTo != null
					whereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) <= ")
						.append(DB.TO_DATE(DateTo));
			}
			
			//  Add Organization
			
			if (AD_Org_ID != 0)
			{
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
			
				whereClause.append(RModel.TABLE_ALIAS).append(".AD_Org_ID=").append(AD_Org_ID);
			}
			
			//exists(select * from c_elementvalue where c_elementvalue_id in (account_dr_id, account_cr_id) and value like '112%')
			if (Account != "") {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause
				.append(" exists(select * from c_elementvalue where c_elementvalue_id in (")
				.append(RModel.TABLE_ALIAS).append(".account_dr_id, ")
				.append(RModel.TABLE_ALIAS).append(".account_cr_id) and value like '").append(Account).append("%')");
			}
			
			if (DocNo != "") {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".DocumentNo like '").append(DocNo).append("%'");
			}
			
			if (C_BPartner_ID > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause
				.append(" exists(select * from C_BPartner where C_BPartner_ID in (")
				.append(RModel.TABLE_ALIAS).append(".C_BPartner_Dr_ID, ")
				.append(RModel.TABLE_ALIAS).append(".C_BPartner_Cr_ID) and C_BPartner_ID = ").append(C_BPartner_ID).append(")");
			}
			
			if (M_Warehouse_ID > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause
				.append(" exists(select * from M_Warehouse where M_Warehouse_ID in (")
				.append(RModel.TABLE_ALIAS).append(".M_Warehouse_Dr_ID, ")
				.append(RModel.TABLE_ALIAS).append(".M_Warehouse_Cr_ID) and M_Warehouse_ID = ").append(M_Warehouse_ID).append(")");
			}
			
			if (M_Product_ID > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause
				.append(" exists(select * from M_Product where M_Product_ID in (")
				.append(RModel.TABLE_ALIAS).append(".M_Product_Dr_ID, ")
				.append(RModel.TABLE_ALIAS).append(".M_Product_Cr_ID) and M_Product_ID = ").append(M_Product_ID).append(")");
			}
			
			if (A_Asset_ID > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".A_Asset_ID = ").append(A_Asset_ID);
				
			}
			
			if (C_TypeCost_ID > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".C_TypeCost_ID = ").append(C_TypeCost_ID);
				
			}
			
			if (C_TypeRevenue_ID > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".C_TypeRevenue_ID = ").append(C_TypeRevenue_ID);
				
			}
			
			if (AD_Department_ID > 0) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".AD_Department_ID = ").append(AD_Department_ID);
				
			}
			
			//Dieu kien loc tren giao dien
			if (filterClause != null && !filterClause.isEmpty()) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(filterClause);
			}
			
			//Zoom theo cau hinh bao cao
			if (sqlZoomLogic != null && !sqlZoomLogic.isEmpty()) {
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(sqlZoomLogic);
			}
			
			
		}//end tim kiem
		

		//  Set Order By Clause
		
		StringBuilder orderClause = new StringBuilder();
		
		if (orderClause.length() == 0)
			orderClause.append(RModel.TABLE_ALIAS).append(".Fact_Acct_ID");

		RModel rm = getRModel();

		
		
		rm.setFunction("Amount", RModel.FUNCTION_SUM);
		rm.setFunction("AmountConvert", RModel.FUNCTION_SUM);

		rm.query (Env.getCtx(), whereClause.toString(), orderClause.toString());

		return rm;
	} // query

	
	private ArrayList<RColumn> createRColumns() 
	{
		Properties ctx = Env.getCtx();
		
		ArrayList<RColumn> m_rcolumn = new ArrayList<RColumn>();
		
		String line = "Line";
		RColumn rc  = null;
		rc = new RColumn(ctx, line,  DisplayType.Integer, "RowNum AS " + line);
		rc.setColSize(100);
		m_rcolumn.add(rc);
		//Organization
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_AD_Org_ID, DisplayType.TableDir);
		rc.setColSize(100);
		m_rcolumn.add(rc);
		/*
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_AD_Department_ID, DisplayType.Table, null, 0, "AD_Department_ID");
		rc.setColSize(100);
		m_rcolumn.add(rc);
		*/
		//Document No
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_DocumentNo, DisplayType.String);
		rc.setColSize(250);
		m_rcolumn.add(rc);
		
		//Account date
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_DateAcct, DisplayType.Date);
		rc.setColSize(100);
		m_rcolumn.add(rc);
		//Debt account
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Account_Dr_ID, DisplayType.Table, null, 0, "C_ElementValue_ID");
		rc.setColSize(300);
		m_rcolumn.add(rc);
		//Credit account
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Account_Cr_ID, DisplayType.Table, null, 0, "C_ElementValue_ID");
		rc.setColSize(300);
		m_rcolumn.add(rc);
		
		//Accounted amount
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Amount, DisplayType.Amount);
		rc.setColSize(140);
		m_rcolumn.add(rc);
		//Source amount
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_AmountConvert, DisplayType.Amount);
		rc.setColSize(140);
		m_rcolumn.add(rc);
		
		
		//Debt partner
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_BPartner_Dr_ID, DisplayType.Table, null, 0, "C_BPartner_ID");
		rc.setColSize(100);
		m_rcolumn.add(rc);
		//Credit partner
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_BPartner_Cr_ID, DisplayType.Table, null, 0, "C_BPartner_ID");
		rc.setColSize(100);
		m_rcolumn.add(rc);
		
		//Currency
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_Currency_ID, DisplayType.TableDir);
		rc.setColSize(80);
		m_rcolumn.add(rc);
		//Currency rate
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_CurrencyRate, DisplayType.Amount);
		rc.setColSize(40);
		m_rcolumn.add(rc);
		
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_BPartnerInfo_Dr_ID, DisplayType.Table, null, 0, "C_BPartnerInfo_Dr_ID");
		rc.setColSize(100);
		m_rcolumn.add(rc);
		//Credit partner
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_BPartnerInfo_Cr_ID, DisplayType.Table, null, 0, "C_BPartnerInfo_Cr_ID");
		rc.setColSize(100);
		m_rcolumn.add(rc);
		
		//Type Cost
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_TypeCost_ID, DisplayType.TableDir);
		rc.setColSize(140);
		m_rcolumn.add(rc);
		
		//Type Revenue
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_TypeRevenue_ID, DisplayType.TableDir);
		rc.setColSize(140);
		m_rcolumn.add(rc);
		
		//Line description
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Description, DisplayType.String);
		rc.setColSize(250);
		m_rcolumn.add(rc);
		
		//Asset
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_A_Asset_ID, DisplayType.TableDir);
		rc.setColSize(140);
		m_rcolumn.add(rc);
		
		//Debit Warehouse
		if (Env.DisProduct) {
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_M_Product_ID, DisplayType.TableDir);
			rc.setColSize(140);
			m_rcolumn.add(rc);
			
			//Quantity
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Qty, DisplayType.Quantity);
			rc.setColSize(50);
			m_rcolumn.add(rc);
			//Unit price
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Price, DisplayType.Amount);
			rc.setColSize(140);
			m_rcolumn.add(rc);
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_UOM_ID, DisplayType.TableDir);
			rc.setColSize(100);
			m_rcolumn.add(rc);
			
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_M_Warehouse_Dr_ID, DisplayType.Table, null, 0, "M_Warehouse_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);
			//Credit Warehouse
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_M_Warehouse_Cr_ID, DisplayType.Table, null, 0, "M_Warehouse_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);	
		}
		
		//Debit Contract
		if (Env.DisContract) {
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_Contract_ID, DisplayType.Table, null, 0, "C_Contract_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);
			
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_ContractAnnex_ID, DisplayType.Table, null, 0, "C_ContractAnnex_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);
		}
		
		//Contract line
		if (Env.DisContractLine) {
			//Credit Contract
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_ContractLine_ID, DisplayType.Table, null, 0, "C_ContractLine_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);
		}
		
		//Project
		if (Env.DisProject) {
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_Project_ID, DisplayType.Table, null, 0, "C_Project_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);		
		}
		
		//Project Line
		if (Env.DisProjectLine) {
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_ProjectLine_ID, DisplayType.Table, null, 0, "C_ProjectLine_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);
		}
		
		//Construction
		if (Env.DisConstruction) {
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_Construction_ID, DisplayType.Table, null, 0, "C_Construction_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);
		}
		
		//Construction Line
		if (Env.DisConstructionLine) {
			rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_C_ConstructionLine_ID, DisplayType.Table, null, 0, "C_ConstructionLine_ID");
			rc.setColSize(200);
			m_rcolumn.add(rc);
		}
		//Document No
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_InvoiceNo, DisplayType.String);
		rc.setColSize(250);
		m_rcolumn.add(rc);
		
		//Account date
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_DateInvoiced, DisplayType.Date);
		rc.setColSize(100);
		m_rcolumn.add(rc);
		
		//Created By
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_CreatedBy, DisplayType.Table, null, 0, "AD_User_ID");
		rc.setColSize(70);
		m_rcolumn.add(rc);
		
		//Updated By
		/*
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_UpdatedBy, DisplayType.Table, null, 0, "AD_User_ID");
		rc.setColSize(70);
		m_rcolumn.add(rc);
		*/
		//Window
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_AD_Window_ID, DisplayType.TableDir); 
		rc.setColSize(300);
		m_rcolumn.add(rc);
		//Table
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_AD_Table_ID, DisplayType.TableDir);
		m_rcolumn.add(rc);
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Record_ID, DisplayType.Integer);
		m_rcolumn.add(rc);
		//Key
		/*
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_Fact_Acct_ID,  DisplayType.Integer);
		m_rcolumn.add(rc);
		*/
		//UOM
		
		
		//PostingType
		/*
		rc = new RColumn(ctx, X_Fact_Acct.COLUMNNAME_PostingType, DisplayType.String);		
		rc.setColSize(20);
		m_rcolumn.add(rc);
		*/
		return m_rcolumn;
	}
	/**
	 *  Create Report Model (Columns)
	 *  @return Report Model
	 */
	private ArrayList<RColumn> m_rcolumn = null;
	
	private RModel getRModel()
	{
		RModel rm = new RModel("Fact_Acct");

		if (m_rcolumn == null) {
			m_rcolumn = createRColumns();
		}
		int size = 0;
		if (sortColumns == null)
			sortColumns = new ArrayList<String>();
		else
			size = sortColumns.size();
		for (int i = 0; i < m_rcolumn.size(); i++) {
			rm.addColumn(m_rcolumn.get(i));
			if (size == 0)
				sortColumns.add(m_rcolumn.get(i).getColumnName());
		}
		
		return rm;
	} // createRModel
	
	private String currentWhereClause = null;
	
	protected RModel queryGroup(String columnName)
	{
		RModel gm = getGModel(columnName);
		String whereClause = "1=1";
		StringBuilder orderClause = new StringBuilder();
	
		if (currentWhereClause != null && currentWhereClause.length() > 0) {
			whereClause = currentWhereClause;
		}
		// Set OrderBy
		orderClause.append(RModel.TABLE_ALIAS).append(".").append(columnName);
		String finalWhereClause = whereClause;
		if (filterClause != null && filterClause.length() >0) {
			finalWhereClause = finalWhereClause + " AND " + filterClause;
		}
		gm.queryGroup(Env.getCtx(), finalWhereClause, orderClause.toString());
		
		return gm;
	}
	
	private RModel getGModel(String columnName)
	{
		RModel gm = new RModel("Fact_Acct");
		
		//Create a hashmap RColumns
		HashMap<String, RColumn> mapRColumn = new HashMap<String, RColumn>();
		for (int i = 0; i < m_rcolumn.size(); i++) {
			mapRColumn.put(m_rcolumn.get(i).getColumnName().trim(), m_rcolumn.get(i));
		}
		// Add Group Column By Use
		RColumn useCol = mapRColumn.get(columnName);
		if (useCol != null) {
			useCol.setGroup();
			gm.addColumn(useCol);
		}
		// Add Currency Column
		RColumn gCol = null;
		gCol = mapRColumn.get(X_Fact_Acct.COLUMNNAME_C_Currency_ID);
		gCol.setGroup();
		gm.addColumn(gCol);
		
		// Add Quantity
		gCol = mapRColumn.get(X_Fact_Acct.COLUMNNAME_Qty);
		gCol.setFunction(RModel.FUNCTION_SUM);
		gm.addColumn(gCol);
		// Add Amount Debit
		gCol = mapRColumn.get(X_Fact_Acct.COLUMNNAME_Amount);
		gCol.setFunction(RModel.FUNCTION_SUM);
		gm.addColumn(gCol);
		
		gCol = mapRColumn.get(X_Fact_Acct.COLUMNNAME_AmountConvert);
		gCol.setFunction(RModel.FUNCTION_SUM);
		gm.addColumn(gCol);
		// Add Count(Rows) Column	
		gCol = new RColumn("RowCount", columnName, Integer.class);
		gCol.setVirual();
		gCol.setFunction(RModel.FUNCTION_COUNT);
		gm.addColumn(gCol);
		// Add function 
		gm.setFunction(X_Fact_Acct.COLUMNNAME_Qty, RModel.FUNCTION_SUM);
		gm.setFunction(X_Fact_Acct.COLUMNNAME_Amount, RModel.FUNCTION_SUM);
		gm.setFunction(X_Fact_Acct.COLUMNNAME_AmountConvert, RModel.FUNCTION_SUM);
		
		return gm;
	}
	
	public void setFilterClause(String filterClause)
	{
		this.filterClause = filterClause;
	}
}
