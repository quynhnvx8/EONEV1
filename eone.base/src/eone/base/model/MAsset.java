package eone.base.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MAsset extends X_A_Asset implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int CHANGETYPE_setAssetGroup = Table_ID * 100 + 1;
	
	
	public static MAsset get (Properties ctx, int A_Asset_ID, String trxName)
	{
		return (MAsset)MTable.get(ctx, MAsset.Table_Name).getPO(A_Asset_ID, trxName);
	}	//	get
	
	
	
	/** Create constructor */
	public MAsset (Properties ctx, int A_Asset_ID, String trxName)
	{
		super (ctx, A_Asset_ID,trxName);
		
	}	//	MAsset

	
	public MAsset (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAsset
	
	
	protected MAsset (MProject project)
	{
		this(project.getCtx(), 0, project.get_TrxName());
		setCreateDate(new Timestamp(System.currentTimeMillis()));
		setDescription(project.getDescription());
	}
	
	public MAsset(MInOut mInOut, MInOutLine sLine, int deliveryCount) {
		this(mInOut.getCtx(), 0, mInOut.get_TrxName());
		setCreateDate(new Timestamp(System.currentTimeMillis()));
		setDescription(sLine.getDescription());
		
	}

	
	public void setAssetGroup(MAssetGroup assetGroup) {
		setA_Asset_Group_ID(assetGroup.getA_Asset_Group_ID());		
		
	}
	
	public MAssetGroup getAssetGroup() {
		return MAssetGroup.get(getCtx(), getA_Asset_Group_ID());
	}
	
	public void completeAssetBuild(MAssetBuild assetBuild) {
		
		MDocType dt = MDocType.get(getCtx(), assetBuild.getC_DocType_ID());
		String docBaseType = dt.getDocBaseType();
		String typeCalculate = getTypeCalculate();
		if (X_C_DocType.DOCBASETYPE_211AddNew.equalsIgnoreCase(docBaseType)) {
			setCreateDate(assetBuild.getDateAcct());
			setDepreciationDate(assetBuild.getDepreciationDate());
			setUseDate(assetBuild.getUseDate());
			setBaseAmtCurrent(assetBuild.getAmount());
			setBaseAmtOriginal(assetBuild.getAmount());
			setUseLifes(assetBuild.getUseLifes());
			if (X_A_Asset.TYPECALCULATE_ByDay.equalsIgnoreCase(typeCalculate)) {
				setEndDateCurrent(TimeUtil.addDays(getDepreciationDate(), assetBuild.getUseLifes().intValue()));				
			} else {
				setEndDateCurrent(TimeUtil.addMonths(getDepreciationDate(), assetBuild.getUseLifes().intValue()));
			}
			setEndDateOriginal(getEndDateCurrent());
			
		}
		
		if (X_C_DocType.DOCBASETYPE_211Upgrade.equalsIgnoreCase(docBaseType)) {
			setBaseAmtCurrent(getBaseAmtCurrent().add(assetBuild.getAmount()));
			setUseLifes(getUseLifes().add(assetBuild.getUseLifes()));
			if (X_A_Asset.TYPECALCULATE_ByDay.equalsIgnoreCase(typeCalculate)) {
				setEndDateCurrent(TimeUtil.addDays(getDepreciationDate(), assetBuild.getUseLifes().intValue()));
			} else {
				setEndDateCurrent(TimeUtil.addMonths(getDepreciationDate(), assetBuild.getUseLifes().intValue()));
			}
		}
		
		if (X_C_DocType.DOCBASETYPE_211Down.equalsIgnoreCase(docBaseType)) {
			setBaseAmtCurrent(getBaseAmtCurrent().subtract(assetBuild.getAmount()));
			setUseLifes(getUseLifes().add(assetBuild.getUseLifes()));
			if (X_A_Asset.TYPECALCULATE_ByDay.equalsIgnoreCase(typeCalculate)) {
				setEndDateCurrent(TimeUtil.addDays(getDepreciationDate(), assetBuild.getUseLifes().intValue()));
			} else {
				setEndDateCurrent(TimeUtil.addMonths(getDepreciationDate(), assetBuild.getUseLifes().intValue()));
			}
		}
	}
	
	protected boolean beforeSave (boolean newRecord)
	{
		
		if (newRecord || is_ValueChanged("Value") || isActive() || is_ValueChanged("Name")) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "A_Asset_ID != ? And (Value = ? Or Name = ?) And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getA_Asset_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", Msg.getMsg(getCtx(), "ValueOrNameExists"));//ValueExists, NameExists
				return false;
			}

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
		if (isRecordUsed()) {
			log.saveError("Error", Msg.getMsg(getCtx(), "RecordUsed"));
			return false;
		}
		
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
		m_processMsg = null;
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;

		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = null;
		if (m_processMsg != null)
			return false;	
				
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		
		return true;
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	public void setProcessMsg(String text) {
		m_processMsg = text;
	}

	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}



	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
