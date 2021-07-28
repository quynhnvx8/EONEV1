package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MAssetDelivery extends X_A_Asset_Delivery implements DocAction
{
	private static final long serialVersionUID = -1247516669047870893L;

	public MAssetDelivery (Properties ctx, int A_Asset_Delivery_ID, String trxName)
	{
		super (ctx, A_Asset_Delivery_ID, trxName);
		
	}	

	
	public static MAssetDelivery get (Properties ctx, int A_Asset_Delivery_ID)
	{
		return get(ctx,A_Asset_Delivery_ID,null);
	}
	
	

	public static MAssetDelivery get (Properties ctx, int A_Asset_Delivery_ID, String trxName)
	{
		final String whereClause = "A_Asset_Delivery_ID=? AND AD_Client_ID=?";
		MAssetDelivery retValue = new Query(ctx,I_HR_Salary.Table_Name,whereClause,trxName)
		.setParameters(A_Asset_Delivery_ID,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		
		
		return true;
	}

	protected void updateProcessed(boolean status) {
		String sql = "Update A_Asset_DeliveryLine set Processed = ? Where A_Asset_Delivery_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getA_Asset_Delivery_ID()}, true, get_TrxName());
	}

	public MAssetDelivery (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAssetUse


	protected boolean afterSave (boolean newRecord,boolean success)
	{
		log.info ("afterSave");
		if (!success)
			return success;
		
		return success;
		 
		
	}	//	afterSave
	

	//Implements DocAction
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
		updateProcessed(true);
		updateAllAsset(true);
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
		updateProcessed(false);
		updateAllAsset(false);
				
		return true;
	}

	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}

	@Override
	public String getSummary() {
		
		return "";
	}
	
	List<MAssetDeliveryLine>  listLine = null;
	
	private void updateAllAsset(boolean insert) {
		if (listLine == null) {
			listLine = getListLine();
		}
		MAsset asset = null;
		for(int i = 0; i < listLine.size(); i ++) {
			asset = MAsset.get(getCtx(), listLine.get(i).getA_Asset_ID(), get_TrxName());
			updateAsset(insert, asset);
			updateAssetUse(insert, asset, listLine.get(i));
			updateAssetHistory(insert, asset, listLine.get(i));
		}
	}
	private void updateAsset(boolean insert, MAsset asset) {
		String statusUse = asset.getStatusUse();
		
		if (X_A_Asset.STATUSUSE_Using.equals(statusUse)) {
			asset.setStatusUse(insert? X_A_Asset.STATUSUSE_UnUse : X_A_Asset.STATUSUSE_Using);
		} else {
			asset.setStatusUse(insert? X_A_Asset.STATUSUSE_Using : X_A_Asset.STATUSUSE_UnUse);
		}
		
		asset.save();
	}
	
	private void updateAssetUse(boolean insert, MAsset asset,  MAssetDeliveryLine line) {
		
		if (insert) {
			List<MAssetUse> ls = MAssetUse.get(getCtx(), asset.getA_Asset_ID(), getDateStart());
			if (ls.size() > 0) {
				return;
			}
			MAssetUse use = new MAssetUse(getCtx(), 0, get_TrxName());
			use.setAD_User_ID(getAD_User_ID());
			use.setAD_User_Receive_ID(getAD_User_Receive_ID());
			use.setA_Asset_ID(asset.getA_Asset_ID());
			//use.setStatusUse(X_A_Asset_Use.STATUSUSE_Delivery);
			use.setUseDate(getDateStart());
			use.setDescription(getDescription() + " \n " + line.getDescription() + " \n" + line.getComments());
			use.save();
		} else {
			String sql = "Delete A_Asset_Use Where UseDate = ? And A_Asset_ID = ?";
			Object [] params = {getDateStart(), asset.getA_Asset_ID()};
			DB.executeUpdate(sql, params, true, get_TrxName());
		}
	}
	
	private void updateAssetHistory (boolean insert, MAsset asset,  MAssetDeliveryLine line ) {
		if (insert) {
			MAssetHistory his = MAssetHistory.get(getCtx(), asset.getA_Asset_ID(), getDateStart());
			if (his != null) {
				return;
			}
			his = new MAssetHistory(getCtx(), 0, get_TrxName());
			his.setA_Asset_ID(asset.getA_Asset_ID());
			his.setChangeDate(getDateStart());
			his.setDescription(getDescription() + " \n " + line.getDescription() + " \n" + line.getComments());
			his.save();
		} else {
			String sql = "Delete A_Asset_History Where ChangeDate = ? And A_Asset_ID = ?";
			Object [] params = {getDateStart(), asset.getA_Asset_ID()};
			DB.executeUpdate(sql, params, true, get_TrxName());
		}
	}
	
	private List<MAssetDeliveryLine> getListLine() {
		String whereClause = "A_Asset_Delivery_ID = ? And A_Asset_ID is not null";
		List<MAssetDeliveryLine> ls = new Query(getCtx(), X_A_Asset_DeliveryLine.Table_Name, whereClause, get_TrxName())
				.setParameters(getA_Asset_Delivery_ID())
				.list();
		return ls;
	}


	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
