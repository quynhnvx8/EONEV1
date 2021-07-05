
package eone.base.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MOrder extends X_C_Order implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7784588474522162502L;

	
	public static MOrder copyFrom (MOrder from, Timestamp dateDoc, 
		int C_DocTypeTarget_ID, boolean counter, boolean copyASI, 
		String trxName)
	{
		MOrder to = new MOrder (from.getCtx(), 0, trxName);
		to.set_TrxName(trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("C_Order_ID", I_ZERO);
		to.set_ValueNoCheck ("DocumentNo", null);
		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		//
		to.setDateOrdered (dateDoc);
		to.setAmount(Env.ZERO);
		//
		to.setIsTransferred (false);
		to.setProcessed (false);
		if (counter) {
			MOrg org = MOrg.get(from.getCtx(), from.getAD_Org_ID());
			int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(trxName);
			if (counterC_BPartner_ID == 0)
				return null;
			to.setBPartner(MBPartner.get(from.getCtx(), counterC_BPartner_ID));
		} 
		//
		if (!to.save(trxName))
			throw new IllegalStateException("Could not create Order");
		
		if (to.copyLinesFrom(from, counter, copyASI) == 0)
			throw new IllegalStateException("Could not create Order Lines");
		
		
		return to;
	}	//	copyFrom
	
	
	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 *  @param trxName trx name
	 */
	public MOrder(Properties ctx, int C_Order_ID, String trxName)
	{
		super (ctx, C_Order_ID, trxName);
		//  New
		if (C_Order_ID == 0)
		{
			setDocStatus(DOCSTATUS_Drafted);
			//
			setIsTransferred(false);
			//
			super.setProcessed(false);

			setDateOrdered (new Timestamp(System.currentTimeMillis()));

			setAmount (Env.ZERO);
		}
	}	//	MOrder

	
	public MOrder (MProject project, boolean IsSOTrx, String DocSubTypeSO)
	{
		this (project.getCtx(), 0, project.get_TrxName());
		setAD_Client_ID(project.getAD_Client_ID());
		setAD_Org_ID(project.getAD_Org_ID());
		//
		setDescription(project.getName());
		Timestamp ts = project.getDateContract();
		if (ts != null)
			setDateOrdered (ts);
		//
		setC_BPartner_ID(project.getC_BPartner_ID());
		//
		
	}	//	MOrder

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MOrder (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MOrder

	/**	Order Lines					*/
	protected MOrderLine[] 	m_lines = null;
	/** Force Creation of order		*/
	protected boolean			m_forceCreation = false;
	
	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg


	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	/**
	 * 	Set Business Partner (Ship+Bill)
	 *	@param C_BPartner_ID bpartner
	 */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		super.setC_BPartner_ID (C_BPartner_ID);
	}	//	setC_BPartner_ID
	
	

	/**
	 * 	Set Ship Business Partner
	 *	@param C_BPartner_ID bpartner
	 */
	public void setShip_BPartner_ID (int C_BPartner_ID)
	{
		super.setC_BPartner_ID (C_BPartner_ID);
	}	//	setShip_BPartner_ID
	
	
	
	/**
	 * 	Set Warehouse
	 *	@param M_Warehouse_ID warehouse
	 */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		super.setM_Warehouse_ID (M_Warehouse_ID);
	}	//	setM_Warehouse_ID
	
	
	
	
	public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());
		
		
	}	//	setBPartner


	/**
	 * 	Copy Lines From other Order
	 *	@param otherOrder order
	 *	@param counter set counter info
	 *	@param copyASI copy line attributes Attribute Set Instance, Resaouce Assignment
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MOrder otherOrder, boolean counter, boolean copyASI)
	{
		if (isProcessed() || otherOrder == null)
			return 0;
		MOrderLine[] fromLines = otherOrder.getLines(false, null);
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MOrderLine line = new MOrderLine (this);
			PO.copyValues(fromLines[i], line, getAD_Client_ID(), getAD_Org_ID());
			line.setC_Order_ID(getC_Order_ID());
			//
			line.setQtyDelivered(Env.ZERO);
			//
			line.setOrder(this);
			line.set_ValueNoCheck ("C_OrderLine_ID", I_ZERO);	//	new
			
			
			line.setProcessed(false);
			if (line.save(get_TrxName()))
				count++;
			//	Cross Link
			if (counter)
			{
				fromLines[i].saveEx(get_TrxName());
			}
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

	
	/**************************************************************************
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MOrder[")
			.append(get_ID()).append("-").append(getDocumentNo())
			.append(",C_DocType_ID=").append(getC_DocType_ID())
			.append(", Amount=").append(getAmount())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	
	
	public MOrderLine[] getLines (String whereClause, String orderClause)
	{
		//red1 - using new Query class from Teo / Victor's MDDOrder.java implementation
		StringBuilder whereClauseFinal = new StringBuilder(MOrderLine.COLUMNNAME_C_Order_ID+"=? ");
		if (!Util.isEmpty(whereClause, true))
			whereClauseFinal.append(whereClause);
		if (orderClause.length() == 0)
			orderClause = MOrderLine.COLUMNNAME_Line;
		//
		List<MOrderLine> list = new Query(getCtx(), I_C_OrderLine.Table_Name, whereClauseFinal.toString(), get_TrxName())
										.setParameters(get_ID())
										.setOrderBy(orderClause)
										.list();
		for (MOrderLine ol : list) {
			ol.setHeaderInfo(this);
		}
		//
		return list.toArray(new MOrderLine[list.size()]);		
	}	//	getLines

	/**
	 * 	Get Lines of Order
	 * 	@param requery requery
	 * 	@param orderBy optional order by column
	 * 	@return lines
	 */
	public MOrderLine[] getLines (boolean requery, String orderBy)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		//
		String orderClause = "";
		if (orderBy != null && orderBy.length() > 0)
			orderClause += orderBy;
		else
			orderClause += "Line";
		m_lines = getLines(null, orderClause);
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Lines of Order.
	 * 	(used by web store)
	 * 	@return lines
	 */
	public MOrderLine[] getLines()
	{
		return getLines(false, null);
	}	//	getLines
	
	
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String set = "SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_Order_ID=" + getC_Order_ID();
		int noLine = DB.executeUpdateEx("UPDATE C_OrderLine " + set, get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine("setProcessed - " + processed + " - Lines=" + noLine );
	}	//	setProcessed
	
	
	protected boolean beforeSave (boolean newRecord)
	{
		
		if(X_C_Order.INCLUDETAX_NoneTax.equals(getIncludeTax())) {
			setC_Tax_ID(0);
			setTaxAmt(Env.ZERO);
			setTaxBaseAmt(Env.ZERO);
		}

		return true;
	}	//	beforeSave
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;
		
		BigDecimal taxRate = Env.ZERO;
		if (getC_Tax_ID() > 0) { 
			MTax tax = MTax.get(Env.getCtx(), getC_Tax_ID());
			if (tax != null) 
				taxRate = tax.getRate();
		}
		
		if (is_ValueChanged(COLUMNNAME_IncludeTax) ) {
			updateForTax(taxRate, isTaxIncluded());
		}
		if (is_ValueChanged(COLUMNNAME_IsTaxIncluded)) {
			updateForTax(taxRate, isTaxIncluded());
		}
		return true;
	}	//	afterSave
	
	
	protected void updateForTax (BigDecimal taxRate, boolean isTaxInclude ) {
		Object [] params = null;
		String sqlUpdateLine = "";
		
		if (!"NONE".equals(getIncludeTax())) {
			if (!isTaxInclude)
				//Ko bao gom thue
				sqlUpdateLine = "Update C_OrderLine r set (Amount, TaxAmt, TaxBaseAmt) = " +
					"( Select l.Qty * l.Price, l.Qty * l.Price * (1 + ?) - l.Qty * l.Price, l.Qty * l.Price * (1 + ?) From C_OrderLine l Where r.C_OrderLine_ID = l.C_OrderLine_ID) "+
					"Where  C_Order_ID = ?";
			else
				//Co bao gom thue
				sqlUpdateLine = "Update C_OrderLine r set (TaxBaseAmt, TaxAmt, Amount) = " +
						"( Select l.Qty * l.Price, l.Qty * l.Price * (1 + ?) - l.Qty * l.Price, l.Qty * l.Price * (1 + ?) From C_OrderLine l Where r.C_OrderLine_ID = l.C_OrderLine_ID) "+
						"Where  C_Order_ID = ?";
			params = new Object [] {taxRate, taxRate, getC_Order_ID()};
		} else {
			sqlUpdateLine = "Update C_OrderLine r set (Amount, TaxAmt, TaxBaseAmt) = " +
					"( Select l.Qty * l.Price, 0, 0 From C_OrderLine l Where r.C_OrderLine_ID = l.C_OrderLine_ID) "+
					"Where C_Order_ID = ?";
			params = new Object [] {getC_Order_ID()};
		}
		
		DB.executeUpdateEx(sqlUpdateLine, params, get_TrxName());
		
		//Update Header
		
		String sql = "UPDATE C_Order o"
				+ " SET (Amount, TaxAmt, TaxBaseAmt) ="
				+ "(SELECT Sum(Amount), Sum(TaxAmt), Sum(TaxBaseAmt)"
				+ " FROM C_OrderLine ol WHERE ol.C_Order_ID=o.C_Order_ID) "
				+ "WHERE C_Order_ID=?";
		
		DB.executeUpdateEx(sql, new Object[] {getC_Order_ID()}, get_TrxName());
	}
	
	protected boolean beforeDelete ()
	{
		if (isProcessed())
			return false;
		// automatic deletion of lines is driven by model cascade definition in dictionary - see IDEMPIERE-2060
		return true;
	}	//	beforeDelete
	
	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}
	
	/**	Process Message 			*/
	protected String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	protected boolean		m_justPrepared = false;

	
	public String prepareIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;

		
		
		//	Lines
		MOrderLine[] lines = getLines(true, MOrderLine.COLUMNNAME_M_Product_ID);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Drafted;
		}
				
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		
		m_justPrepared = true;
		return DocAction.STATUS_Drafted;
	}	//	prepareIt
	
	
	
	public String completeIt()
	{
		

		setProcessed(true);	
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	
	
	
	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
				
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		return true;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		//	: Grand Total = 123.00 (#1)
		sb.append(": ").
			append(Msg.translate(getCtx(),"Amount")).append("=").append(getAmount());
		if (m_lines != null)
			sb.append(" (#").append(m_lines.length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary
	
	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds);
	}	//	isComplete

	
	
	/**
	 * Set process message
	 * @param processMsg
	 */
	public void setProcessMessage(String processMsg)
	{
		m_processMsg = processMsg;
	}
	
	

	/** Returns C_DocType_ID (or C_DocTypeTarget_ID if C_DocType_ID is not set) */
	public int getDocTypeID()
	{
		return getC_DocType_ID();
	}


	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

}	//	MOrder
