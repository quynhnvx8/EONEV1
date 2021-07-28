
package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Msg;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MConstruction extends X_C_Construction implements DocAction
{
	
	private static final long serialVersionUID = 8631795136761641303L;

	
	public MConstruction (Properties ctx, int C_Construction_ID, String trxName)
	{
		super (ctx, C_Construction_ID, trxName);
		
	}	//	MProject

	public MConstruction (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProject



	public String toString()
	{
		return "";
	}	//	toString

	
	public MConstructionLine[] getLines()
	{
		final String whereClause = "C_Project_ID=?";
		List <MConstructionLine> list = new Query(getCtx(), I_C_ConstructionLine.Table_Name, whereClause, get_TrxName())
			.setParameters(getC_Construction_ID())
			.setOrderBy("Line")
			.list();
		//
		MConstructionLine[] retValue = new MConstructionLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("Value") || isActive()) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "C_Construction_ID != ? And (Value = ? or Name=?) And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getC_Construction_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", Msg.getMsg(getCtx(), "ValueOrNameExists"));
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
		String sql = "Update C_ConstructionLine set Processed = ? Where C_Construction_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getC_Construction_ID()}, true, get_TrxName());
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
		m_processMsg = null;
		if (m_processMsg != null)
			return false;	
				
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		updateProcessed(false);
			
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
