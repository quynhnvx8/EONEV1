
package eone.base.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MAssetChange extends X_A_Asset_Change implements DocAction
{
	
	private static final long serialVersionUID = 4083373951793617528L;

	private static CLogger s_log = CLogger.getCLogger(MAssetChange.class);
	

	public MAssetChange (Properties ctx, int A_Asset_Change_ID, String trxName)
	{
		super (ctx, A_Asset_Change_ID, trxName);
	}
	
	public MAssetChange (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	protected MAssetChangeLine[]	m_lines = null;
	
	public MAssetChangeLine[] getLines (boolean requery)
	{
		if (m_lines != null) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		List<MDepreciationExp> list = new Query(getCtx(), I_A_Asset_ChangeLine.Table_Name, "A_Asset_Change_ID=? ", get_TrxName())
		.setParameters(getA_Asset_Change_ID())
		.list();
		//
		m_lines = new MAssetChangeLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}

	
	protected boolean beforeSave (boolean newRecord)
	{
		//Neu da phan bo khau hao thi khong cho thay doi gia tri, YC Canceled ban ghi khau hao.
		return true;
	}	//	beforeSave

	public static MAssetChange get (Properties ctx, int A_Asset_ID, String changeType,  String trxName)
	{
		if (A_Asset_ID <= 0 || changeType == null)
		{
			return null;
		}
		
		MAssetChange ac = new Query(ctx, MAssetChange.Table_Name, "", trxName)
				.setParameters(new Object[]{A_Asset_ID, changeType})
				.firstOnly();
		return ac;
	}
	
	List<MAssetChangeLine> listAsset = null;
	
	private void updateAllAsset(boolean insert) {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (listAsset == null)
			listAsset = getListLine();
		
		MAsset asset = null;
		for(int i = 0; i < listAsset.size(); i++) {
			asset = MAsset.get(getCtx(), listAsset.get(i).getA_Asset_ID(), get_TrxName());
			updateAsset(insert, dt, asset, listAsset.get(i));
			updateAssetHistory(insert, dt, asset);
			updateWorkfile(insert, dt, asset, listAsset.get(i));
		}
	}
	
	private void updateAsset(boolean insert, MDocType dt, MAsset asset,  MAssetChangeLine line) {
		if (X_C_DocType.DOCBASETYPE_211Upgrade.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			if (line.getAmount().compareTo(Env.ZERO) > 0) {
				if (insert) {
					asset.setBaseAmtCurrent(asset.getBaseAmtCurrent().add(line.getAmount()));
					asset.setRemainAmt(asset.getRemainAmt().add(line.getAmount()));
				} else {
					asset.setBaseAmtCurrent(asset.getBaseAmtCurrent().subtract(line.getAmount()));
					asset.setRemainAmt(asset.getRemainAmt().subtract(line.getAmount()));
				}
			}
			if (line.getUseLifes().compareTo(Env.ZERO) > 0) {
				if (insert) {
					asset.setUseLifes(asset.getUseLifes().add(line.getUseLifes()));
				} else {
					asset.setUseLifes(asset.getUseLifes().subtract(line.getUseLifes()));
				}
			}
		}
		
		else if (X_C_DocType.DOCBASETYPE_211Down.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			if (line.getAmount().compareTo(Env.ZERO) > 0) {
				if (insert) {
					asset.setBaseAmtCurrent(asset.getBaseAmtCurrent().subtract(line.getAmount()));
					asset.setRemainAmt(asset.getRemainAmt().subtract(line.getAmount()));
				} else {
					asset.setBaseAmtCurrent(asset.getBaseAmtCurrent().add(line.getAmount()));
					asset.setRemainAmt(asset.getRemainAmt().add(line.getAmount()));
				}
			}
			if (line.getUseLifes().compareTo(Env.ZERO) > 0) {
				if (insert) {
					asset.setUseLifes(asset.getUseLifes().subtract(line.getUseLifes()));
				} else {
					asset.setUseLifes(asset.getUseLifes().add(line.getUseLifes()));
				}
			}
		}else if (X_C_DocType.DOCBASETYPE_211Transfer.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			asset.setIsDisposed(insert ? true : false);
		}
		
		asset.save();
	}
	
	//Lay ban ghi truoc do de xac dinh ban ghi ke tiep cua truong OrderNo
	private MDepreciationWorkfile getBeforeRow(Timestamp dateAcct, int A_Asset_ID) {
		String sqlWhere = "OrderNo = (select max(OrderNo) From A_Depreciation_Workfile Where A_Asset_ID = ? And StartDate < ?)"
				+ " And A_Asset_ID = ?";
		
		MDepreciationWorkfile relValue = new Query(getCtx(), X_A_Depreciation_Workfile.Table_Name, sqlWhere, get_TrxName())
				.setParameters(A_Asset_ID, dateAcct, A_Asset_ID)
				.first();
		return relValue;
		
	}
	
	private String updateWorkfile (boolean insert, MDocType dt, MAsset asset,  MAssetChangeLine line) {
		MDepreciationWorkfile workfile = null;
		if (X_C_DocType.DOCBASETYPE_211Upgrade.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_211Down.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			MDepreciationWorkfile wf = getBeforeRow(getDateAcct(), line.getA_Asset_ID());
			if (insert) {
				workfile = new MDepreciationWorkfile(getCtx(), 0, get_TrxName());
				workfile.setDateAcct(getDateAcct());
				workfile.setStartDate(getDateAcct());
				workfile.setEndDate(null);
				workfile.setA_Asset_ID(asset.getA_Asset_ID());				
				workfile.setOrderNo(wf.getOrderNo() + 10);//Thu tu nay rat quan trong, dung de xac dinh cac ky tinh toan
				BigDecimal AmountNew = asset.getBaseAmtCurrent();
				BigDecimal userLifesNew = asset.getUseLifes();
				
				workfile.setAmount(AmountNew.divide(userLifesNew.multiply(new BigDecimal("30")), Env.rountQty, RoundingMode.HALF_UP));
				workfile.save();
			} else {
				String sql = "Delete A_Depreciation_Workfile Where DateAcct = ? And A_Asset_ID = ?";
				Object [] params = {getDateAcct(), line.getA_Asset_ID()};
				DB.executeUpdate(sql, params, true, get_TrxName());
			}
			//Xu ly ban ghi truoc do
			MDepreciationWorkfile wfbefore = MDepreciationWorkfile.get(getCtx(), wf.getA_Depreciation_Workfile_ID(), get_TrxName());
			if (insert) {
				wfbefore.setEndDate(TimeUtil.addDays(getDateAcct(), -1));
			} else {
				wfbefore.setEndDate(null);
			}
			wfbefore.save();
		}
		
		return "";
	}
	
	private void updateAssetHistory (boolean insert, MDocType dt, MAsset asset) {
		if (insert) {
			MAssetHistory his = MAssetHistory.get(getCtx(), asset.getA_Asset_ID(), getDateAcct());
			if (his != null) {
				return;
			}
			his = new MAssetHistory(getCtx(), 0, get_TrxName());
			his.setA_Asset_ID(asset.getA_Asset_ID());
			his.setChangeDate(getDateAcct());
			his.setDescription(getSummary());
			his.save();
		} else {
			String sql = "Delete A_Asset_History Where ChangeDate = ? And A_Asset_ID = ?";
			Object [] params = {getDateAcct(), asset.getA_Asset_ID()};
			DB.executeUpdate(sql, params, true, get_TrxName());
		}
	}
	
	private List<MAssetChangeLine> getListLine() {
		String whereClause = "A_Asset_Change_ID = ? And A_Asset_ID is not null";
		List<MAssetChangeLine> ls = new Query(getCtx(), X_A_Asset_ChangeLine.Table_Name, whereClause, get_TrxName())
				.setParameters(getA_Asset_Change_ID())
				.list();
		return ls;
	}
	
	//Implements DocAction
	protected void updateProcessed(boolean status) {
		String sql = "Update A_Asset_ChangeLine set Processed = ? Where A_Asset_Change_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getA_Asset_Change_ID()}, true, get_TrxName());
	}
	protected String		m_processMsg = null;
	
	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}


	@Override
	public String completeIt() {
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Drafted;
		}
		
		
		String errorDis = checkAssetDistribution();
		if (errorDis != null) {
			m_processMsg = Msg.getMsg(Env.getCtx(), "AssetDistributed") + ": "+ errorDis;
			return DocAction.STATUS_Drafted;
		}
		
		setProcessed(true);
		updateProcessed(true);
		updateAllAsset(true);
		return DocAction.STATUS_Completed;
	}


	
	public String checkAssetDistribution() {
		String strAssetName = "";
		String sql = " select a.Name "+
				" From A_Asset a Inner Join A_Depreciation_Exp e On a.A_Asset_ID = e.A_Asset_ID "+
				" Where ? Between StartDate And EndDate  "+
				"	And exists (Select 1 From A_Asset_ChangeLine l Where a.A_Asset_ID = l.A_Asset_ID And l.A_Asset_Change_ID = ?)";
		
		PreparedStatement pt = DB.prepareCall(sql);
		ResultSet rs = null;
		try {
			pt.setTimestamp(1, getDateAcct());
			pt.setInt(2, getA_Asset_Change_ID());
			rs = pt.executeQuery();
			while (rs.next()) {
				if (strAssetName == "")
					strAssetName = rs.getString("Name");
				else
					strAssetName = strAssetName + ", " + rs.getString("Name"); 
			}
		} catch (SQLException e) {
			s_log.info(e.getStackTrace().toString());
		}
		if (strAssetName != "") {
			return strAssetName;
		}
		return null;
		
	}

	@Override
	public boolean reActivateIt() {
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}
		
		String errorDis = checkAssetDistribution();
		if (errorDis != null) {
			m_processMsg = Msg.getMsg(Env.getCtx(), "AssetDistributed") + ": "+ errorDis;
			return false;
		}
		
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		updateProcessed(false);
		updateAllAsset(false);
			
		return true;
	}




	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}	//	MAssetChange