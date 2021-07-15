package eone.base.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MAssetBuild extends X_A_Asset_Build implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static MAssetBuild get (Properties ctx, int A_Asset_Build_ID, String trxName)
	{
		return (MAssetBuild)MTable.get(ctx, MAssetBuild.Table_Name).getPO(A_Asset_Build_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MAssetBuild (Properties ctx, int A_Asset_Build_ID, String trxName)
	{
		super (ctx, A_Asset_Build_ID,trxName);
		
	}	//	MAsset

	
	public MAssetBuild (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAsset
	
	
	protected MAssetBuild (MProject project)
	{
		this(project.getCtx(), 0, project.get_TrxName());
		setDescription(project.getDescription());
	}
	
	public MAssetBuild(MInOut mInOut, MInOutLine sLine, int deliveryCount) {
		this(mInOut.getCtx(), 0, mInOut.get_TrxName());
		setDescription(sLine.getDescription());
		
	}

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (is_ValueChanged("DocStatus") || is_ValueChanged("Posted")) {
			return true;
		}
		//Cap nhat so du hoac them moi chi thuc hien 1 lan doi voi 1 tai san
		MAsset asset = MAsset.get(getCtx(), getA_Asset_ID(), get_TrxName());
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (asset.getA_Asset_ID() > 0 && asset.getBaseAmtCurrent().compareTo(Env.ZERO) > 0  &&
				(
				X_C_DocType.DOCBASETYPE_211AddNew.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_211Openbalance.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_242Openbalance.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_242New.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_153Expense.equalsIgnoreCase(dt.getDocBaseType())
				)
		) {
			log.saveError("Save error", Msg.getMsg(getCtx(), "AssetProcessed"));
			return false;
		}
		
		return true;
	}	//	beforeSave
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(!success)
		{
			return success;
		}
		
		
		
		return true;
	}	//	afterSave
	
	
	protected boolean beforeDelete()
	{
		
		
		return true;
	}       //      beforeDelete
	
	
	
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
		
		updateAllAsset(true);
		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}
		
		
		updateAllAsset(false);
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		
			
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
	
	private void updateAllAsset(boolean insert) {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		MAsset asset = MAsset.get(getCtx(), getA_Asset_ID(), get_TrxName());
		updateAsset(insert, dt, asset);
		updateWorkfile(insert, dt, asset);
		updateAssetUse(insert, dt, asset);
		updateAssetHistory(insert, dt, asset);
		updateTools(insert, dt, asset);
		updateExpense(insert, dt, asset);
	}
	
	private void updateAsset(boolean insert, MDocType dt, MAsset asset) {
		asset = MAsset.get(getCtx(), getA_Asset_ID(), get_TrxName());
		
		//Mua moi thi update
		if (X_C_DocType.DOCBASETYPE_211AddNew.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_242New.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_211Openbalance.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_242Openbalance.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_153Expense.equalsIgnoreCase(dt.getDocBaseType())
				) {
			asset.setCreateDate(insert? getDateAcct() : null);
			asset.setUseDate(insert? getUseDate() : null);
			asset.setDepreciationDate(insert? getDepreciationDate() : null);
			asset.setBaseAmtCurrent(insert? getAmount() : null);
			asset.setBaseAmtOriginal(insert? getAmount() : null);
			asset.setAccumulateAmt(insert? getAccumulateAmt() : null);
			asset.setRemainAmt(insert? getAmount().subtract(getAccumulateAmt()) : null);
			asset.setUseLifes(insert? getUseLifes() : null);
			asset.setEndDateCurrent(insert ? TimeUtil.addMonths(getDepreciationDate(), getUseLifes().intValue()) : null);
			asset.setEndDateOriginal(asset.getEndDateCurrent());
			asset.setTypeCalculate(insert ? getTypeCalculate() : null);
			asset.setIsRecordUsed(insert? true : false);
			
			if (X_C_DocType.DOCBASETYPE_211AddNew.equalsIgnoreCase(dt.getDocBaseType())
					|| X_C_DocType.DOCBASETYPE_211Openbalance.equalsIgnoreCase(dt.getDocBaseType())
			) {
				asset.setAssetType(insert? X_A_Asset.ASSETTYPE_Asset : null);
			} else {
				asset.setAssetType(insert? X_A_Asset.ASSETTYPE_Expense : null);
			}
			asset.setIsDepreciated(insert ? true : false);
			asset.setEndDateOriginal(asset.getEndDateCurrent());
		}
		
		
		asset.saveEx();
		
	}
	
	private MDepreciationWorkfile getBeforeRow(int row) {
		String sqlWhere = "OrderNo = (select max(OrderNo) From A_Depreciation_Workfile Where A_Asset_ID = ?) And  A_Asset_ID = ?";
		if (row > 0) {
			sqlWhere = "OrderNo = (select max(OrderNo) From A_Depreciation_Workfile Where A_Asset_ID = ? And OrderNo < "+ row +") And  A_Asset_ID = ?";
		}
		MDepreciationWorkfile relValue = new Query(getCtx(), X_A_Depreciation_Workfile.Table_Name, sqlWhere, get_TrxName())
				.setParameters(getA_Asset_ID(), getA_Asset_ID())
				.first();
		return relValue;
		
	}
	
	private String updateWorkfile (boolean insert, MDocType dt, MAsset asset) {
		MDepreciationWorkfile workfile = null;
		if (X_C_DocType.DOCBASETYPE_211AddNew.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_211Openbalance.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_242New.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_242Openbalance.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_153Expense.equalsIgnoreCase(dt.getDocBaseType())
		) 
		{
			
			if (insert) {
				MDepreciationWorkfile wf = getBeforeRow(20);
				if (wf != null) {
					return null;
				}
				workfile = new MDepreciationWorkfile(getCtx(), 0, get_TrxName());
				workfile.setDateAcct(getDateAcct());
				workfile.setStartDate(getDepreciationDate());
				workfile.setEndDate(null);
				workfile.setA_Asset_ID(getA_Asset_ID());				
				workfile.setOrderNo(10);//Thu tu nay rat quan trong, dung de xac dinh cac ky tinh toan. Gia tri: 10,20,30...
				BigDecimal amt = getAmount();
				BigDecimal life = getUseLifes();
				if (X_C_DocType.DOCBASETYPE_211Openbalance.equalsIgnoreCase(dt.getDocBaseType()) 
						|| X_C_DocType.DOCBASETYPE_242Openbalance.equalsIgnoreCase(dt.getDocBaseType())
				) {
					amt = getAmount().subtract(getAccumulateAmt());
					life = getUseLifes().subtract(getAccumulateUseLifes());
				}
				workfile.setAmount(amt.divide(life.multiply(new BigDecimal("30")), Env.rountQty, RoundingMode.HALF_UP));
				workfile.saveEx();
			} else {
				String sql = "Delete A_Depreciation_Workfile Where DateAcct = ? And A_Asset_ID = ?";
				Object [] params = {getDateAcct(), getA_Asset_ID()};
				DB.executeUpdate(sql, params, true, get_TrxName());
			}
			
		}
		
		return "";
	}
	
	private void updateAssetUse (boolean insert, MDocType dt, MAsset asset) {
		
		if (X_C_DocType.DOCBASETYPE_211AddNew.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_211Openbalance.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			//MAssetUse assetUse = new MAssetUse(getCtx(), 0, get_TrxName());
			//assetUse.setUseDate(UseDate);
		}
		else if (X_C_DocType.DOCBASETYPE_211Upgrade.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
		else if (X_C_DocType.DOCBASETYPE_211Down.equalsIgnoreCase(dt.getDocBaseType())) 
		{
					
		}
		else if (X_C_DocType.DOCBASETYPE_211Deposal.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
		else if (X_C_DocType.DOCBASETYPE_211Transfer.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
		else if (X_C_DocType.DOCBASETYPE_211OTher.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
	}
	
	private void updateAssetHistory (boolean insert, MDocType dt, MAsset asset) {
		//Neu Co thi kiem tra chua ton tai thi insert co roi thi thoi. Truong hop RA thi xoa.
		if (insert) {
			MAssetHistory his = MAssetHistory.get(getCtx(), getA_Asset_ID(), getDateAcct());
			if (his != null) {
				return;
			}
			his = new MAssetHistory(getCtx(), 0, get_TrxName());
			his.setA_Asset_ID(getA_Asset_ID());
			his.setChangeDate(getDateAcct());
			his.setDescription(getSummary());
			his.save();
		} else {
			String sql = "Delete A_Asset_History Where ChangeDate = ? And A_Asset_ID = ?";
			Object [] params = {getDateAcct(), getA_Asset_ID()};
			DB.executeUpdate(sql, params, true, get_TrxName());
		}
	}
	
	private void updateTools (boolean insert, MDocType dt, MAsset asset) {
		//Neu Co thi kiem tra chua ton tai thi insert co roi thi thoi. Truong hop RA thi xoa.
		if (X_C_DocType.DOCBASETYPE_153ONE.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			MAssetTools tools = MAssetTools.get(getCtx(), getA_Asset_ID(), getM_Product_ID(), getUseDate());
			if (insert) {
				if (tools != null) {
					return;
				}
				tools = new MAssetTools(getCtx(), 0, get_TrxName());
				tools.setA_Asset_ID(asset.getA_Asset_ID());
				tools.setM_Product_ID(getM_Product_ID());
				tools.setDateTrx(getUseDate());
				tools.save();
			}else {
				String sql = "Delete A_Asset_Tools Where M_Product_ID = ? And A_Asset_ID = ?";
				Object [] params = {getM_Product_ID(), getA_Asset_ID()};
				DB.executeUpdate(sql, params, true, get_TrxName());
			}
		}		
	}
	
	private void updateExpense (boolean insert, MDocType dt, MAsset asset) {
		
		if (X_C_DocType.DOCBASETYPE_211AddNew.equalsIgnoreCase(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_211Openbalance.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
		else if (X_C_DocType.DOCBASETYPE_211Upgrade.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
		else if (X_C_DocType.DOCBASETYPE_211Down.equalsIgnoreCase(dt.getDocBaseType())) 
		{
					
		}
		else if (X_C_DocType.DOCBASETYPE_211Deposal.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
		else if (X_C_DocType.DOCBASETYPE_211Transfer.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
		else if (X_C_DocType.DOCBASETYPE_211OTher.equalsIgnoreCase(dt.getDocBaseType())) 
		{
			
		}
	}



	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
