/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package eone.base.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

/**
 *	Inventory Movement Model
 *	
 *  @author Jorg Janke
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 * 			<li>FR [ 1948157  ]  Is necessary the reference for document reverse
 *  		@see http://sourceforge.net/tracker/?func=detail&atid=879335&aid=1948157&group_id=176962
 * 			<li> FR [ 2520591 ] Support multiples calendar for Org 
 *			@see http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962 
 *  @author Armen Rizal, Goodwill Consulting
 * 			<li>BF [ 1745154 ] Cost in Reversing Material Related Docs  
 *  @author Teo Sarca, www.arhipac.ro
 *  		<li>FR [ 2214883 ] Remove SQL code and Replace for Query
 *  @version $Id: MMovement.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MMovement extends X_M_Movement implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3201199540429467933L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Movement_ID id
	 *	@param trxName transaction
	 */
	public MMovement (Properties ctx, int M_Movement_ID, String trxName)
	{
		super (ctx, M_Movement_ID, trxName);
		if (M_Movement_ID == 0)
		{
		//	setC_DocType_ID (0);
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setIsApproved (false);
			setIsInTransit (false);
			setMovementDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setPosted (false);
			super.setProcessed (false);
		}	
	}	//	MMovement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MMovement (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MMovement

	/**	Lines						*/
	protected MMovementLine[]		m_lines = null;
	
	/** Reversal Indicator			*/
	public static String	REVERSE_INDICATOR = "^";
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public MMovementLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		//
		final String whereClause = "M_Movement_ID=?";
		List<MMovementLine> list = new Query(getCtx(), I_M_MovementLine.Table_Name, whereClause, get_TrxName())
		 										.setParameters(getM_Movement_ID())
		 										.setOrderBy(MMovementLine.COLUMNNAME_Line)
		 										.list();
		m_lines = new MMovementLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	
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
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getC_DocType_ID() == 0)
		{
			MDocType types[] = MDocType.getOfDocBaseType(getCtx(), MDocType.DOCBASETYPE_Debit_151);
			if (types.length > 0)	//	get first
				setC_DocType_ID(types[0].getC_DocType_ID());
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @C_DocType_ID@"));
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		final String sql = "UPDATE M_MovementLine SET Processed=? WHERE M_Movement_ID=?";
		int noLine = DB.executeUpdateEx(sql, new Object[]{processed, get_ID()}, get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine("Processed=" + processed + " - Lines=" + noLine);
	}	//	setProcessed
	
	
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
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	
	public String completeIt()
	{
		
		StringBuilder errors = new StringBuilder();
		
		
		if (errors.toString().length() > 0)
		{
			m_processMsg = errors.toString();
			return DocAction.STATUS_Drafted;
		}
		
		m_processMsg = null;
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	

	/**
	 * 	Check Material Policy
	 * 	Sets line ASI
	 */
	protected void checkMaterialPolicy(MMovementLine line,BigDecimal qtyToDeliver)
	{
		
		
		if(Env.ZERO.compareTo(qtyToDeliver)==0)
			return;
		
		boolean needSave = false;

		
		
		if (needSave)
		{
			line.saveEx();
		}
	}	//	checkMaterialPolicy

	
	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
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
		sb.append(": ")
			.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
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
		if (m_processMsg != null) {
			setProcessed(false);
			
		}
		return m_processMsg;
	}	//	getProcessMsg
	
	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID
	
	/** Reversal Flag		*/
	protected boolean m_reversal = false;
	
	/**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
	protected void setReversal(boolean reversal)
	{
		m_reversal = reversal;
	}	//	setReversal
	/**
	 * 	Is Reversal
	 *	@return reversal
	 */
	protected boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal

	/**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds) 
			|| DOCSTATUS_Closed.equals(ds)
			|| DOCSTATUS_Reversed.equals(ds);
	}	//	isComplete

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}	//	MMovement

