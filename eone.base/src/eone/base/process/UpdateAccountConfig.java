package eone.base.process;

import java.util.List;

import org.compiere.util.DB;

import eone.base.model.MAccount;
import eone.base.model.MColumn;
import eone.base.model.MDocType;
import eone.base.model.MTable;
import eone.base.model.X_C_Account;

//eone.base.process.UpdateAccountConfig
public class UpdateAccountConfig extends SvrProcess
{
	int p_ID;
	protected void prepare()
	{
		p_ID = getRecord_ID();
	}	//	prepare
	

	protected String doIt() throws java.lang.Exception
	{
		MDocType dt = MDocType.get(getCtx(), p_ID);
		
		
		List<Object> subDocType = dt.getListDocTypeSub();
		if (subDocType != null) {
			for(int i = 0; i < subDocType.size(); i++) {
				
			}
		}
		
		if (dt.getAD_Table_ID() > 0) {
			MTable tb = MTable.get(getCtx(), dt.getAD_Table_ID());
			
			boolean colSubDocType = false;
			boolean colAcctDebit = false;
			boolean colAcctCredit = false;
			boolean colAcctTax = false;
			boolean colAcctCOGS = false;
			boolean colAcctREV = false;
			
			MColumn [] cols = tb.getColumns(true);
			for(int c = 0; c < cols.length; c++) {
				if ("C_DocTypeSub_ID".equalsIgnoreCase(cols[c].getColumnName())) {
					colSubDocType = true;
				}
				
				if ("Account_COGS_ID".equalsIgnoreCase(cols[c].getColumnName())) {
					colAcctCOGS = true;
				}
				
				if ("Account_Cr_ID".equalsIgnoreCase(cols[c].getColumnName())) {
					colAcctCredit = true;
				}
				
				if ("Account_Dr_ID".equalsIgnoreCase(cols[c].getColumnName())) {
					colAcctDebit = true;
				}
				
				if ("Account_Tax_ID".equalsIgnoreCase(cols[c].getColumnName())) {
					colAcctTax = true;
				}
				
				if ("Account_REV_ID".equalsIgnoreCase(cols[c].getColumnName())) {
					colAcctREV = true;
				}
			}
			
			
			//Lay cac cot cua bang co hach toan
			String sql = "Select ColumnName From AD_Column Where AD_Table_ID = ? And ColumnName like 'Account_%' "
					+ "ORDER BY ColumnName DESC";
			List<Object> data = DB.getSQLObjectsEx(get_TrxName(), sql, dt.getAD_Table_ID());
			
			String fieldSelect = "";
			String fieldWhere = "";
			
			for(int i = 0; i < data.size(); i++) {
				String columnName = data.get(i).toString();
				
				if ("".equals(fieldSelect)) {
					fieldSelect = columnName;
					fieldWhere = "Coalesce("+ columnName + ",0) = 0";
				}
				else
				{
					fieldSelect = fieldSelect + ", " + columnName;
					fieldWhere = fieldWhere +   " OR Coalesce("+ columnName + ",0) = 0";
				}
			}
			
			String sqlUpdate = "";
			
			List<MAccount> re = null;
			
			if (colSubDocType) {
				String sqlTable = "Select C_DocTypeSub_ID From " + tb.getTableName() + " Where DocStatus = 'DR' And C_DocType_ID = ? " + " AND (" + fieldWhere + ")";
				List<Object> listSub = DB.getSQLObjectsEx(get_TrxName(), sqlTable, new Object [] {p_ID});
				if (listSub != null) {
					for(int c = 0; c < listSub.size(); c ++) {
						re = MAccount.getAccountDocType(Integer.parseInt(listSub.get(c).toString()));
						for(int a = 0; a < re.size(); a++) {
							//Tk nợ
							if(X_C_Account.TYPEACCOUNT_DebitAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctDebit) {
								sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_Dr_ID = " + re.get(a).getAccount_ID() 
										+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_Dr_ID,0) = 0"
										+ " AND DocStatus = 'DR'";
								DB.executeUpdate(sqlUpdate);
							}
							
							//Tk có
							if(X_C_Account.TYPEACCOUNT_CreditAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctCredit) {
								sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_Cr_ID = " + re.get(a).getAccount_ID()
										+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_Cr_ID,0) = 0"
										+ " AND DocStatus = 'DR'";
								DB.executeUpdate(sqlUpdate);
							}
							
							//Tk thuế
							if(colAcctTax 
									&& (X_C_Account.TYPEACCOUNT_TaxInputAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) 
											|| X_C_Account.TYPEACCOUNT_TaxInputAccount.equalsIgnoreCase(re.get(a).getTypeAccount())) ) {
								sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_Tax_ID = " + re.get(a).getAccount_ID()
										+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_Tax_ID,0) = 0"
										+ " AND DocStatus = 'DR'";
								DB.executeUpdate(sqlUpdate);
							}
							
							//Tk giá vốn
							if(X_C_Account.TYPEACCOUNT_COGSAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctCOGS) {
								sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_COGS_ID = " + re.get(a).getAccount_ID()
										+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_COGS_ID,0) = 0"
										+ " AND DocStatus = 'DR'";
								DB.executeUpdate(sqlUpdate);
							}
							
							//Tk doanh thu
							if(X_C_Account.TYPEACCOUNT_RevenueAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctREV) {
								sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_REV_ID = " + re.get(a).getAccount_ID()
										+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_REV_ID,0) = 0"
										+ " AND DocStatus = 'DR'";
								DB.executeUpdate(sqlUpdate);
							}
						}
					}
				}
			}
			
			//Update lại trong trường hợp sub ko update được hoặc không có sub
			re = MAccount.getAccountDocType(p_ID);
			for(int a = 0; a < re.size(); a++) {
				//Tk nợ
				if(X_C_Account.TYPEACCOUNT_DebitAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctDebit) {
					sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_Dr_ID = " + re.get(a).getAccount_ID() 
							+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_Dr_ID,0) = 0"
							+ " AND DocStatus = 'DR'";
					DB.executeUpdate(sqlUpdate);
				}
				
				//Tk có
				if(X_C_Account.TYPEACCOUNT_CreditAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctCredit) {
					sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_Cr_ID = " + re.get(a).getAccount_ID()
							+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_Cr_ID,0) = 0"
							+ " AND DocStatus = 'DR'";
					DB.executeUpdate(sqlUpdate);
				}
				
				//Tk thuế
				if(colAcctTax 
						&& (X_C_Account.TYPEACCOUNT_TaxInputAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) 
								|| X_C_Account.TYPEACCOUNT_TaxInputAccount.equalsIgnoreCase(re.get(a).getTypeAccount())) ) {
					sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_Tax_ID = " + re.get(a).getAccount_ID()
							+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_Tax_ID,0) = 0"
							+ " AND DocStatus = 'DR'";
					DB.executeUpdate(sqlUpdate);
				}
				
				//Tk giá vốn
				if(X_C_Account.TYPEACCOUNT_COGSAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctCOGS) {
					sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_COGS_ID = " + re.get(a).getAccount_ID()
							+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_COGS_ID,0) = 0"
							+ " AND DocStatus = 'DR'";
					DB.executeUpdate(sqlUpdate);
				}
				
				//Tk doanh thu
				if(X_C_Account.TYPEACCOUNT_RevenueAccount.equalsIgnoreCase(re.get(a).getTypeAccount()) && colAcctREV) {
					sqlUpdate = "UPDATE " + tb.getTableName() + " SET Account_REV_ID = " + re.get(a).getAccount_ID()
							+ " WHERE C_DocTypeSub_ID = " + re.get(a).getC_DocTypeSub_ID() + " AND Coalesce(Account_REV_ID,0) = 0"
							+ " AND DocStatus = 'DR'";
					DB.executeUpdate(sqlUpdate);
				}
			}
		}
		return "Success!";
	}	//	doIt
	
	
}	
