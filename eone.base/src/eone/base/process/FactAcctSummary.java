

package eone.base.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

import eone.base.model.MYear;


public class FactAcctSummary extends SvrProcess {
	

	private int p_Year_ID = 0;

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
		String sqlSelectOld = "Select max(DateAcct) From Fact_Acct_Sum Where AD_Client_ID = ?";
		int AD_Client_ID = getAD_Client_ID();
		//int AD_Org_ID = Env.getAD_Org_ID(getCtx());
		Object [] params = {AD_Client_ID};
		Timestamp dateMax = DB.getSQLValueTS(get_TrxName(), sqlSelectOld, params); 
		if (dateMax == null) {
			dateMax = TimeUtil.getMinDate();
		}
		MYear year = MYear.get(getCtx(), p_Year_ID, get_TrxName());
		//Lay sang ngay 01/01 cua nam sau thay vi 31/12 cua nam truoc. Vi co the lech gio, phut, giay.
		Timestamp dateProcess = TimeUtil.getFinalYear(year.getFiscalYear(), true);
		
		//Lay ngay 31/12 de chot so du den ngay nay.
		Timestamp dateData = TimeUtil.getFinalYear(year.getFiscalYear(), false);
		
		
		//Xoa ban ghi tong hop cu di
		DB.executeUpdate("Delete Fact_Acct_Sum Where DateAcct >= ? And AD_Client_ID = ?", new Object [] {dateData, AD_Client_ID}, true, get_TrxName());
		
		String sql = 
				" Insert Into Fact_Acct_Sum "+
					"("+
						"account_Id, c_bpartner_id, c_currency_id, ad_org_id, ad_client_Id, "+
						" amtSourceDr, amtSourceCr, amtAcctDr, amtAcctCr,"+
						" Created, CreatedBy, Updated, UpdatedBy, isActive, DateAcct"+
					")"+
						
				" Select account_id, c_bpartner_id, c_currency_id, ad_org_Id, ?, "+ //#1
				" 	sum(amt_dr) amt_dr, sum(amt_cr) amt_cr, sum(amtconvert_dr) amtconvert_dr, sum(amtconvert_cr) amtconvert_cr, "+
				" 	sysdate, 100, sysdate, 100, 'Y', ? "+ //#1
				" from "+
				" ( "+
				//Lay du lieu da chot so du cua nam truoc
				" 	Select amtSourceDr amt_dr, amtAcctDr amtconvert_dr, amtSourceCr amt_cr, amtAcctCr amtconvert_cr,"+
						" account_id, c_bpartner_id, c_currency_id, ad_org_id "+
				" 	From Fact_Acct_Sum "+
				" 	Where DateAcct = ? And AD_Client_ID = ?"+ //#2,3
				" 	union all "+
				//Lay du lieu ps cua nam cat chot
				" 	select sum(amount) amt_dr, sum(amountconvert) amtconvert_dr, 0 amt_cr, 0 amtconvert_cr, "+
				" 		account_dr_id account_id, coalesce(c_bpartner_dr_id,0) c_bpartner_id, c_currency_id, ad_org_id "+
				" 	from fact_acct "+
				" 	where ? < DateAcct And DateAcct < ? And AD_Client_ID = ? "+ //#4,5,6: dateMax, dateProcess, Ad_Client_ID
				" 	group by account_dr_id, c_bpartner_dr_id, c_currency_id, ad_org_id "+
				" 	union all "+
				" 	select 0 amt_dr, 0 amtconvert_dr, sum(amount) amt_cr, sum(amountconvert) amtconvert_cr, "+
				" 		account_cr_id account_id, coalesce(c_bpartner_cr_id,0) c_bpartner_id, c_currency_id, ad_org_id "+
				" 	from fact_acct "+
				" 	where ? < DateAcct And DateAcct < ? And AD_Client_ID = ? "+ //#7,8,9: dateMax, dateProcess, Ad_Client_ID
				" 	group by account_cr_id, c_bpartner_cr_id, c_currency_id, ad_org_id "+
				" )a "+
				" group by account_id, c_bpartner_id, c_currency_id, ad_org_id ";
		params = new Object [] {AD_Client_ID, dateData, dateMax, AD_Client_ID, dateMax, dateProcess, AD_Client_ID, dateMax, dateProcess, AD_Client_ID};
		int no = DB.executeUpdate(sql, params, true, get_TrxName());
		return "inserted " + String.valueOf(no) + " rows!";
	}




}
