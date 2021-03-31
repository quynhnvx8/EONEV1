
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MInOut extends X_M_InOut implements DocAction
{

	private static final long serialVersionUID = 1226522383231204912L;

	public static MInOut createFrom (MOrder order, Timestamp movementDate,
			boolean forceDelivery, boolean allAttributeInstances, Timestamp minGuaranteeDate,
			boolean complete, String trxName)
	{		
		if (order == null)
			throw new IllegalArgumentException("No Order");
		
		//	Create Header
		MInOut retValue = new MInOut (order, 0, movementDate);
		
		//	Check if we can create the lines
		MOrderLine[] oLines = order.getLines(true, "M_Product_ID");
		for (int i = 0; i < oLines.length; i++)
		{
			// Calculate how much is left to deliver (ordered - delivered)
			BigDecimal qty = oLines[i].getQtyOrdered().subtract(oLines[i].getQtyDelivered());
			//	Nothing to deliver
			if (qty.signum() == 0)
				continue;
			
		}	//	for all order lines

		//	No Lines saved
		if (retValue.get_ID() == 0)
			return null;

		return retValue;
		
	}


	public static MInOut copyFrom (MInOut from, Timestamp dateDoc, Timestamp dateAcct,
		int C_DocType_ID, boolean counter, String trxName, boolean setOrder)
	{
		MInOut to = new MInOut (from.getCtx(), 0, null);
		to.set_TrxName(trxName);
		copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("M_InOut_ID", I_ZERO);
		to.set_ValueNoCheck ("DocumentNo", null);
		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		//
		to.setC_DocType_ID (C_DocType_ID);
		
		//
		to.setDateOrdered (dateDoc);
		to.setDateAcct (dateAcct);
		to.setDateReceived(null);
	
		to.setPosted (false);
		to.setProcessed (false);
		
		

		return to;
	}	//	copyFrom


	public static MInOut copyFrom (MInOut from, Timestamp dateDoc,
		int C_DocType_ID, boolean isSOTrx, boolean counter, String trxName, boolean setOrder)
	{
		MInOut to = copyFrom ( from, dateDoc, dateDoc,
				C_DocType_ID, counter,
				trxName, setOrder);
		return to;

	}

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOut_ID
	 *	@param trxName rx name
	 */
	public MInOut (Properties ctx, int M_InOut_ID, String trxName)
	{
		super (ctx, M_InOut_ID, trxName);
		if (M_InOut_ID == 0)
		{
			setDocStatus (DOCSTATUS_Drafted);
			//
			super.setProcessed (false);
			setPosted(false);
		}
	}	//	MInOut

	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trxName transaction
	 */
	public MInOut (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInOut

	/**
	 * 	Order Constructor - create header only
	 *	@param order order
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MOrder order, int C_DocTypeShipment_ID, Timestamp movementDate)
	{
		this (order.getCtx(), 0, order.get_TrxName());
		
	}	//	MInOut

	/**
	 * 	Copy Constructor - create header only
	 *	@param original original
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MInOut original, int C_DocTypeShipment_ID, Timestamp movementDate)
	{
		this (original.getCtx(), 0, original.get_TrxName());
		setClientOrg(original);
		
		if (C_DocTypeShipment_ID == 0)
			setC_DocType_ID(original.getC_DocType_ID());
		else
			setC_DocType_ID (C_DocTypeShipment_ID);

		

		setDateOrdered(original.getDateOrdered());
		setDescription(original.getDescription());


	}	//	MInOut


	/**	Lines					*/
	protected MInOutLine[]	m_lines = null;
	
	/** BPartner				*/
	protected MBPartner		m_partner = null;


	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else{
			StringBuilder msgd = new StringBuilder(desc).append(" | ").append(description);
			setDescription(msgd.toString());
		}	
	}	//	addDescription

	/**
	 *	String representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MInOut[")
			.append (get_ID()).append("-").append(getDocumentNo())
			.append(",DocStatus=").append(getDocStatus())
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
		StringBuilder msgreturn = new StringBuilder().append(dt.getName()).append(" ").append(getDocumentNo());
		return msgreturn.toString();
	}	//	getDocumentInfo

	
	protected MInvoiceTax[]	m_linesTax = null;
	public MInvoiceTax[] getLinesTax (boolean requery)
	{
		String sqlWhere = " 1 = 1";
		if (!X_M_InOut.INCLUDETAXTAB_TAXS.equalsIgnoreCase(getIncludeTaxTab())) {
			sqlWhere = " 1=2 ";
		}
		if (m_linesTax != null && !requery) {
			set_TrxName(m_linesTax, get_TrxName());
			return m_linesTax;
		}
		List<MInvoiceTax> list = new Query(getCtx(), I_C_InvoiceTax.Table_Name, sqlWhere + "And M_InOut_ID=?", get_TrxName())
		.setParameters(getM_InOut_ID())
		.list();
		//
		m_linesTax = new MInvoiceTax[list.size()];
		list.toArray(m_linesTax);
		return m_linesTax;
	}
	
	public MInOutLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		List<MInOutLine> list = new Query(getCtx(), I_M_InOutLine.Table_Name, "M_InOut_ID=?", get_TrxName())
		.setParameters(getM_InOut_ID())
		.setOrderBy(MInOutLine.COLUMNNAME_Line)
		.list();
		//
		m_lines = new MInOutLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}	//	getMInOutLines

	/**
	 * 	Get Lines of Shipment
	 * 	@return lines
	 */
	public MInOutLine[] getLines()
	{
		return getLines(false);
	}	//	getLines


	

	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		StringBuilder sql = new StringBuilder("UPDATE M_InOutLine SET Processed='")
			.append((processed ? "Y" : "N"))
			.append("' WHERE M_InOut_ID=").append(getM_InOut_ID());
		int noLine = DB.executeUpdate(sql.toString(), get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed

	/**
	 * 	Get BPartner
	 *	@return partner
	 */
	public MBPartner getBPartner()
	{
		if (m_partner == null)
			m_partner = new MBPartner (getCtx(), getC_BPartner_Dr_ID(), get_TrxName());
		return m_partner;
	}	//	getPartner

	/**
	 * 	Set Document Type
	 * 	@param DocBaseType doc type MDocType.DOCBASETYPE_
	 */
	public void setC_DocType_ID (String DocBaseType)
	{
		
	}	//	setC_DocType_ID

	/**
	 * 	Set Default C_DocType_ID.
	 * 	Based on SO flag
	 */
	public void setC_DocType_ID()
	{
		
	}	//	setC_DocType_ID

	/**
	 * 	Set Business Partner Defaults & Details
	 * 	@param bp business partner
	 */
	public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

	
	}	//	setBPartner

	

	protected boolean beforeSave (boolean newRecord)
	{
		

		return true;
	}	//	beforeSave

	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;

		if (is_ValueChanged("AD_Org_ID"))
		{
			final String sql = "UPDATE M_InOutLine ol"
					+ " SET AD_Org_ID ="
					+ "(SELECT AD_Org_ID"
					+ " FROM M_InOut o WHERE ol.M_InOut_ID=o.M_InOut_ID) "
					+ "WHERE M_InOut_ID=?";
			int no = DB.executeUpdateEx(sql, new Object[] {getM_InOut_ID()}, get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Lines -> #" + no);
		}
		return true;
	}	


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

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		return true;
	}	//	unlockIt

	

	public String completeIt()
	{
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Drafted;
		}
		
		MInOutLine[] lines = getLines(true);
		if (lines == null || lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Drafted;
		}
		

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		
		//Cap nhat Storage
		updateStorage(true);
		
		setProcessed(true);
		return DocAction.STATUS_Completed;
	}	//	completeIt

	
	private void updateStorage(boolean isComplete) {
		List<Object> lstColumn = new ArrayList<Object>();
		List<List<Object>> lstRows = new ArrayList<List<Object>>();
		String sqlInsert = MStorage.sqlInsert;
		int BATCH_SIZE = Env.getBatchSize(getCtx());
		String sql = "";
		MDocType mDoctype = MDocType.get(getCtx(), getC_DocType_ID());
		String doctype = mDoctype.getDocType();
		
		//Type nhap so du dau ky
		if (MDocType.DOCTYPE_Balance.equals(doctype)) {
			sql = " select 'OP' DocType, il.M_InOutLine_ID, i.M_Warehouse_Dr_ID M_Warehouse_ID, i.DateAcct, il.M_Product_ID, il.Qty, il.Price, il.Amount "+
					" From M_InOut i Inner Join M_InOutLine il On i.M_InOut_ID = il.M_InOut_ID "+
					" Where i.M_InOut_ID = ? ";
		}
		
		//Type nhap
		if (MDocType.DOCTYPE_Input.equals(doctype)) {
			if (getIncludeTaxTab().equals(MInOut.INCLUDETAXTAB_TAXS) 
					&& getCalculateTax().equals(MInOut.CALCULATETAX_GROSS))
				//Neu co tinh thue va Thanh tien da bao gom thue thi lay truong Amt khac
				sql = " select 'IN' DocType, il.M_InOutLine_ID, i.M_Warehouse_Dr_ID M_Warehouse_ID, i.DateAcct, il.M_Product_ID, il.Qty, il.Price, il.TaxBaseAmt Amount "+
						" From M_InOut i Inner Join M_InOutLine il On i.M_InOut_ID = il.M_InOut_ID "+
						" Where i.M_InOut_ID = ? ";
			else
				sql = " select 'IN' DocType, il.M_InOutLine_ID, i.M_Warehouse_Dr_ID M_Warehouse_ID, i.DateAcct, il.M_Product_ID, il.Qty, il.Price, il.Amount "+
						" From M_InOut i Inner Join M_InOutLine il On i.M_InOut_ID = il.M_InOut_ID "+
						" Where i.M_InOut_ID = ? ";
		}
		
		//Type Xuat
		if (MDocType.DOCTYPE_Output.equals(doctype)) {
			sql = " select 'OU' DocType, il.M_InOutLine_ID, i.M_Warehouse_Cr_ID M_Warehouse_ID, i.DateAcct, il.M_Product_ID, il.Qty, il.Price, il.Amount "+
					" From M_InOut i Inner Join M_InOutLine il On i.M_InOut_ID = il.M_InOut_ID "+
					" Where i.M_InOut_ID = ? ";
		}
		if (isComplete) {//CO
			ResultSet rs = null;
			PreparedStatement ps = DB.prepareCall(sql);
			try {
				ps.setInt(1, getM_InOut_ID());
				rs = ps.executeQuery();
				MStorage storage = null;
				while (rs.next()) {
					lstColumn = new ArrayList<Object>();
					String docType = rs.getString("DocType");
					storage = new MStorage(getCtx(), 0, null);
					storage.setRecord_ID(rs.getInt("M_InOutLine_ID"));
					storage.setM_Product_ID(rs.getInt("M_Product_ID"));
					storage.setM_Warehouse_ID(rs.getInt("M_Warehouse_ID"));
					storage.setQty(rs.getBigDecimal("Qty"));
					storage.setPrice(rs.getBigDecimal("Price"));
					storage.setAmount(rs.getBigDecimal("Amount"));
					storage.setTypeInOut(docType);
					storage.setDateTrx(rs.getTimestamp("DateAcct"));
					int ID = DB.getNextID(getAD_Client_ID(), MStorage.Table_Name, get_TrxName());
					lstColumn = PO.getBatchValueList(storage, MStorage.Table_ID, get_TrxName(), ID);
					lstRows.add(lstColumn);
					if (lstRows.size() >= BATCH_SIZE) {
						DB.excuteBatch(sqlInsert, lstRows, get_TrxName());
						lstRows.clear();
					}
				}
				if (lstRows.size() > 0) {
					DB.excuteBatch(sqlInsert, lstRows, get_TrxName());
					lstRows.clear();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			
		} else { //RA
			sql = "Delete M_Storage Where Record_ID in (Select M_InOutLine_ID From M_InOutLine Where M_InOut_ID = ?)";
			DB.executeUpdate(sql, getM_InOut_ID(), get_TrxName());
		}
	}
	
	/* Save array of documents to process AFTER completing this one */
	ArrayList<PO> docsPostProcess = new ArrayList<PO>();

	protected void addDocsPostProcess(PO doc) {
		docsPostProcess.add(doc);
	}

	public ArrayList<PO> getDocsPostProcess() {
		return docsPostProcess;
	}

	
	/**
	 * 	Check Material Policy
	 * 	Sets line ASI
	 */
	protected void checkMaterialPolicy(MInOutLine line,BigDecimal qty)
	{
			
		
		if(Env.ZERO.compareTo(qty)==0)
			return;
		
		//	Incoming Trx
		boolean needSave = false;

		MProduct product = line.getProduct();

		//	Need to have Location
		if (product != null )
		{
			
			needSave = true;
		}

		//	Attribute Set Instance
		

		if (needSave)
		{
			line.saveEx();
		}
	}	//	checkMaterialPolicy

	protected BigDecimal autoBalanceNegative(MInOutLine line, MProduct product,BigDecimal qtyToReceive) {
		
		return qtyToReceive;
	}


	
	

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt()
	{
		
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}
		
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
		
		//Cap nhat Storage
		updateStorage(false);
		
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
		//	: Total Lines = 123.00 (#1)
		sb.append(":")
		//	.append(Msg.translate(getCtx(),"TotalLines")).append("=").append(getTotalLines())
			.append(" (#").append(getLines(false).length).append(")");
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

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return Env.ZERO;
	}	//	getApprovalAmt

	
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds);
	}	//	isComplete


	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	

}	//	MInOut
