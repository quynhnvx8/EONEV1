package eone.base.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import eone.base.model.MAsset;
import eone.base.model.MDepreciation;
import eone.base.model.MDepreciationExp;
import eone.base.model.MDepreciationSplit;
import eone.base.model.MDepreciationWorkfile;
import eone.base.model.X_A_Asset;
import eone.base.model.X_A_Depreciation_Split;
import eone.base.model.X_A_Depreciation_Workfile;


public class BuildDepreciation extends SvrProcess
{
	private int p_Record_ID = 0;
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null)
				;
			else if (name.equals("C_DocTypeTarget_ID"))
				;
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
	}	//	prepare


	@SuppressWarnings("deprecation")
	protected String doIt () throws Exception
	{
		MDepreciation de = MDepreciation.get(getCtx(), p_Record_ID);
		//Lay danh sach tai san tinh khau hao
		String sqlWhereAsset = "IsActive = 'Y' And IsDepreciated = 'Y' And IsDisposed != 'Y' And IsTransferred != 'Y' "+
				" And AD_Client_ID = ? And AD_Org_ID = ?  And RemainAmt > 0 "+
				" And Not Exists (Select 1 From A_Depreciation_Exp e Where A_Depreciation_ID = ? And A_Asset_ID = e.A_Asset_ID)";
		List<MAsset> assets = new eone.base.model.Query(getCtx(), X_A_Asset.Table_Name, sqlWhereAsset, get_TrxName())
				.setParameters(de.getAD_Client_ID(), de.getAD_Org_ID(), p_Record_ID)
				.list();
		
		//Lay danh sach khau hao
		String sqlWhereWf = "Exists (Select 1 From A_Asset c Where c.A_Asset_ID = A_Asset_ID And "+ sqlWhereAsset +")";
		List<MDepreciationWorkfile> wf = new eone.base.model.Query(getCtx(), X_A_Depreciation_Workfile.Table_Name, sqlWhereWf, get_TrxName())
				.setParameters(de.getAD_Client_ID(), de.getAD_Org_ID())
				.list();
		
		
		int A_Asset_ID = 0;
		String typeCalculate = "";
		BigDecimal accumulatedAmt = Env.ZERO;
		
		boolean insert = true;
		
		MDepreciationExp newExp = new MDepreciationExp(getCtx(), 0, get_TrxName());
		for(int i = 0; i < assets.size(); i++) {
			A_Asset_ID = assets.get(i).getA_Asset_ID();
			typeCalculate = assets.get(i).getTypeCalculate(); //Kieu tinh khau hao theo 30 ngay hay theo thang thuc te.
			int count = 0;//Tang bien dem de xac dinh dung gia tri thay doi gan nhat.
			int orderNo = 0;//xac dinh vi tri thay doi gia tri de lay dung thu tu
			int numberDay = 0; //xac dinh so ngay tong cong xem co vuot qua 30 thoe typeCalculate ko?
			for(int n = 0; n < wf.size(); n++) {
				int number_day = 0; //Dem khoang ngay thay doi de tinh gia tri khau hao cho khoang ngay do.
				//Duyet tat ca trong workfile cua Tai san day. Neu co thay doi nguyen gia thi lay so tien KH cua 1 ngay nhan voi so ngay thay doi.
				if (wf.get(n).getA_Asset_ID() == A_Asset_ID) {
					if (wf.get(n).getStartDate().getMonth() == de.getDateAcct().getMonth() 
							&& wf.get(n).getStartDate().getYear() == de.getDateAcct().getYear()) {
						//1.1: Neu co nhieu lan thay doi nguyen gia trong thang tinh khau hao.
						count = count + 10;
						
						if (wf.get(n).getEndDate() != null) {
							number_day = TimeUtil.getDaysBetween(wf.get(n).getStartDate(), wf.get(n).getEndDate());
							
							//Neu theo thang thi xu ly ve 30 ngay thoi
							number_day = getNumberDay(typeCalculate, numberDay, number_day);
							
						} else {
							orderNo = wf.get(n).getOrderNo(); //Lay gia tri nay de so sanh voi count de lay gia tri gan nhat 1.2: ...
							Timestamp EndDate = TimeUtil.getDayLastMonth(de.getDateAcct());
							number_day = TimeUtil.getDaysBetween(wf.get(n).getStartDate(), EndDate);
							
							//Neu theo thang thi xu ly ve 30 ngay thoi
							number_day = getNumberDay(typeCalculate, numberDay, number_day);							
						}
						accumulatedAmt = accumulatedAmt.add(wf.get(n).getAmount().multiply(new BigDecimal(number_day)));						
					} else {
						//1.2: Lay lan thay doi nguyen gia gan nhat truoc do
						if (wf.get(n).getOrderNo() == orderNo - count) {
							Timestamp firstDate = TimeUtil.getDayFirstMonth(de.getDateAcct());
							number_day = TimeUtil.getDaysBetween(firstDate, wf.get(n).getEndDate());
							
							//Neu theo thang thi xu ly ve 30 ngay thoi
							number_day = getNumberDay(typeCalculate, numberDay, number_day);
							
							accumulatedAmt = accumulatedAmt.add(wf.get(n).getAmount().multiply(new BigDecimal(number_day)));
							break;
						}
					}
				}//Ket thuc tai san cua workfile  
				numberDay = numberDay + number_day; 
			}//Het work file
			if(assets.get(i).isDepreciationSplit()) {
				//Lay danh sach khau hao phan tach
				String sqlWhereDS = "A_Asset_ID = ?";
				List<MDepreciationSplit> ds = new eone.base.model.Query(getCtx(), X_A_Depreciation_Split.Table_Name, sqlWhereDS, get_TrxName())
						.setParameters(A_Asset_ID)
						.list();
				int percent = 0;
				BigDecimal amt = Env.ZERO;
				BigDecimal amtAccu = Env.ZERO;
				for(int m = 0; m < ds.size(); m ++) {
					if (m == ds.size() - 1) {
						amt = accumulatedAmt.subtract(amtAccu);
					} else {
						percent = percent + ds.get(m).getPercent();
						amt = accumulatedAmt.multiply(new BigDecimal(ds.get(m).getPercent())).divide(new BigDecimal(10), Env.roundAmount, RoundingMode.HALF_UP);
						amtAccu = amtAccu.add(amt);
						
					}
					
					insert = insertDepreciationExp(newExp, amt, 
							ds.get(m).getC_TypeCost_ID() > 0 ? ds.get(m).getC_TypeCost_ID() :  assets.get(i).getC_TypeCost_ID(), 
							ds.get(m).getAccount_ID() > 0 ? ds.get(m).getAccount_ID() : assets.get(i).getAccount_Dr_ID(), 
							assets.get(i).getAccount_Cr_ID(), de.getDateAcct(), assets.get(i) );
					if (!insert) {
						return "Insert false!";
					}
				}
			} else {
				insert = insertDepreciationExp(newExp, accumulatedAmt, assets.get(i).getC_TypeCost_ID(), assets.get(i).getAccount_Dr_ID(), 
						assets.get(i).getAccount_Cr_ID(), de.getDateAcct(), assets.get(i) );
				if (!insert) {
					return "Insert false!";
				}
			}
		}//Het Asset.
		
		return "Success!";
	}	//	doIt
	
	private boolean insertDepreciationExp(MDepreciationExp newExp, BigDecimal accumulate, int C_TypeCost_ID, 
			int Account_Dr_ID, int Account_Cr_ID, Timestamp dateAcct, MAsset asset) {
		newExp = new MDepreciationExp(getCtx(), 0, get_TrxName());
		Timestamp startTime = TimeUtil.getDayFirstMonth(dateAcct);
		Timestamp endTime = TimeUtil.getDayLastMonth(dateAcct);
		int numberDay = TimeUtil.getDaysBetween(startTime, endTime);
		if (X_A_Asset.TYPECALCULATE_ByMonth.equals(asset.getTypeCalculate())) {
			numberDay = 30;
		}
		newExp.setOneDay(accumulate.divide(new BigDecimal(numberDay), Env.rountQty, RoundingMode.HALF_UP));
		
		accumulate = accumulate.setScale(Env.roundAmount);
		newExp.setIsSelected(true);
		newExp.setA_Asset_ID(asset.getA_Asset_ID());
		newExp.setA_Depreciation_ID(p_Record_ID);
		newExp.setC_TypeCost_ID(C_TypeCost_ID);
		newExp.setAccount_Dr_ID(Account_Dr_ID);
		newExp.setAccount_Cr_ID(Account_Cr_ID);
		newExp.setAmount(accumulate);
		newExp.setStartDate(startTime);
		newExp.setEndDate(endTime);
		newExp.setDateAcct(dateAcct);
		
		if (!newExp.save() )
			return false;
		return true;
		
	}
	
	private int getNumberDay(String typeCalculate, int numberDayFull, int number_day) {
		if (X_A_Asset.TYPECALCULATE_ByMonth.equals(typeCalculate)) {
			if (number_day > 30 && numberDayFull == 0)
				number_day = 30;
			else if (numberDayFull > 0 && numberDayFull + number_day > 30)
				number_day = 30 - numberDayFull;
		}
		return number_day;
	}
}	