
package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MContract extends X_C_Contract implements DocAction
{
	
	private static final long serialVersionUID = 8631795136761641303L;

	
	public MContract (Properties ctx, int C_Contract_ID, String trxName)
	{
		super (ctx, C_Contract_ID, trxName);
		
	}	

	public MContract (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	



	public String toString()
	{
		StringBuilder sb = new StringBuilder ("MContract[").append(get_ID())
			.append("]");
		return sb.toString();
	}	//	toString

	
	public MContractLine[] getLines()
	{
		final String whereClause = "C_Contract_ID=?";
		List <MContractLine> list = new Query(getCtx(), I_C_ContractLine.Table_Name, whereClause, get_TrxName())
			.setParameters(getC_Contract_ID())
			.setOrderBy("Line")
			.list();
		//
		MContractLine[] retValue = new MContractLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("Value") || isActive()) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "C_Contract_ID != ? And Value = ? And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getC_Contract_ID(), getValue(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", "Value exists");
				return false;
			}

		}
		
		return true;
	}	//	beforeSave
	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
				
		return success;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		return success;
	}	//	afterDelete
	
	
	//Implements DocAction
	protected void updateProcessed(boolean status) {
		String sql = "Update C_ContractLine set Processed = ? Where C_Contract_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getC_Contract_ID()}, true, get_TrxName());
		
		sql = "Update C_ContractAnnex set Processed = ? Where C_Contract_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getC_Contract_ID()}, true, get_TrxName());
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
		m_processMsg = null;
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		setProcessed(true);
		updateProcessed(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		updateProcessed(false);
		m_processMsg = null;
		if (m_processMsg != null)
			return false;		
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

	@Override
	public void setDocStatus(String newStatus) {
		//this.setDocStatus(newStatus);		
	}

	@Override
	public String getDocStatus() {
		return getDocStatus();
	}

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}	//	MProject
