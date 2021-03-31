package eone.base.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.model.MTransferConfig;
import org.compiere.model.MTransferConfigLine;
import org.compiere.model.MTransferPeriod;
import org.compiere.model.MTransferPeriodLine;
import org.compiere.util.DB;
import org.compiere.util.Env;

//TODO: Chua xu ly triet de.
public class TransferPeriod extends SvrProcess
{
	private int	p_Report_ID = 0;
	
	protected void prepare ()
	{
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}			
		p_Report_ID = getRecord_ID();
	}	//	prepare

	
	protected String doIt ()
		throws Exception
	{
		MTransferPeriod period = MTransferPeriod.get(getCtx(), p_Report_ID, get_TrxName());
		MTransferConfig config = MTransferConfig.get(getCtx(), period.getC_TransferConfig_ID(), get_TrxName());
		MTransferConfigLine [] lines = config.getLines(true);
		
		String sqlDel = "Delete From C_TransferPeriodLine Where C_TransferPeriod_ID = ?";
		DB.executeUpdate(sqlDel, p_Report_ID, get_TrxName());
		
		Map<String, String> arrs = new HashMap<String, String>();
		String key = "";
		String strData = "";
		
		//Quy chuan lai
		for(int i = 0; i < lines.length; i ++) {
			key = lines[i].getAccount_Dr_ID() +  "_" + lines[i].getOrderCalculate() + "_" + lines[i].getMethodTransfer();
			if (arrs.containsKey(key)) {
				strData = arrs.get(key);
			}
			if (strData != "") {
				strData = strData + "," + lines[i].getAccount_Cr_ID();
			} else {
				strData = lines[i].getAccount_Cr_ID();
			}
			arrs.put(key, strData);
			key = "";
			strData = "";
		}
		
		//Xet thu tu tinh toan
		int orderCurr = 0;
		int orderMin = 0;
		String calMethod = "";
		int account_target_id = 0;
		Map<String, HashMap<Integer, BigDecimal>> data = null;
		
		for (String k : arrs.keySet()) {
			//Xu ly lay thu tu tinh nho nhat de tinh toan truoc.
			data = new HashMap<String, HashMap<Integer, BigDecimal>>();
			key = k;
			String [] keys = key.split("_");
			orderCurr = Integer.parseInt(keys[1].toString());
			calMethod = keys[2].toString();
			account_target_id = Integer.parseInt(keys[0].toString());
			
			if (orderCurr < orderMin) {
				orderMin = orderCurr;
				
			}
			HashMap<Integer, BigDecimal> valueline =  getAmt(arrs.get(key), calMethod, period);
			data.put(account_target_id + "_" + calMethod, valueline);
			String error = saveData(account_target_id, data);
			if (error != "") {
				
				return error;
			}
		}
		
		return "OK";
	}	//	doIt
	
	//Lay du no hoac du co theo tai khoan
	protected HashMap<Integer, BigDecimal> getAmt(String keyAccount, String keyMethod, MTransferPeriod period) throws Exception{
		HashMap<Integer, BigDecimal> arr = new HashMap<Integer, BigDecimal>();
		
		String sql = " Select Account_ID, Sum(Amt) Amt "+
				" From "+
				" ( "+
				" 	select Account_Dr_ID Account_ID, Case when '"+ keyMethod +"' = "+ 
				"'"		+MTransferConfigLine.METHODTRANSFER_BalanceDebit + "'"+
				" 		Then Sum(AmountConvert) Else -Sum(AmountConvert) End Amt "+
				" 	From Fact_Acct  "+
				" 	Where DateAcct Between ? And ? And Account_Dr_ID in ("+ keyAccount +")  "+
				" 	Group by Account_Dr_ID "+
				" 	Union All "+
				" 	select Account_Cr_ID Account_ID, Case when '"+ keyMethod +"' = "+ 
				"'"		+MTransferConfigLine.METHODTRANSFER_BalanceDebit + "'"+
				" 		Then -Sum(AmountConvert) Else Sum(AmountConvert) End Amt "+
				" 	From Fact_Acct  "+
				" 	Where DateAcct Between ? And ? And Account_Cr_ID in ("+ keyAccount +") "+
				" 	Group by Account_Cr_ID "+
				" )b Group by Account_ID ";
		PreparedStatement ps = DB.prepareCall(sql);
		ResultSet rs = null;
		
		ps.setTimestamp(1, period.getStartDate());
		ps.setTimestamp(2, period.getEndDate());
		ps.setTimestamp(3, period.getStartDate());
		ps.setTimestamp(4, period.getEndDate());
		rs = ps.executeQuery();
		while (rs.next()) {
			arr.put(rs.getInt("Account_ID"), rs.getBigDecimal("Amt"));
		}
		return arr;
	}
	
	protected String saveData(int account_target_id, Map<String, HashMap<Integer, BigDecimal>> data) {
		int dr_id = 0;
		int cr_id = 0;
		BigDecimal Amt = Env.ZERO;
		String error = "";
		for(String key : data.keySet()) {
			HashMap<Integer, BigDecimal> value = data.get(key);
			String [] ar = key.split("_");
			String method = ar[1];
			for(Integer acct_id : value.keySet()) {
				Amt = value.get(acct_id);
				if (method.equalsIgnoreCase(MTransferConfigLine.METHODTRANSFER_BalanceDebit)) {
					dr_id = account_target_id;
					cr_id = acct_id;					
				} else {
					cr_id = account_target_id;
					dr_id = acct_id;
				}
				MTransferPeriodLine line = MTransferPeriodLine.get(getCtx(), 0, get_TrxName());
				line.setC_TransferPeriod_ID(p_Report_ID);
				line.setAccount_Dr_ID(dr_id);
				line.setAccount_Cr_ID(cr_id);
				line.setAmount(Amt);
				line.setAmountConvert(Amt);
				if (!line.save()) {
					if (error == "")
						error = "Error: " + Amt;
					else
						error = error + "Error: " + Amt;
				}
			}
		}
		return error;
	}
}	