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

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.Msg;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;


public class MAssembly extends X_M_Assembly implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7632011237765946083L;


	
	public MAssembly (Properties ctx, int C_Cash_ID, String trxName)
	{
		super (ctx, C_Cash_ID, trxName);
		if (C_Cash_ID == 0)
		{
		
			setDocAction(DOCACTION_Complete);
			setDocStatus(DOCSTATUS_Drafted);
			setProcessed (false);
		}
	}	//	MCash

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MAssembly (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCash
	
	/*Khong dung LINE nua
	
	
	protected MCashLine[]		m_lines = null;
	
	
	public MCashLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		
		final String whereClause =MCashLine.COLUMNNAME_C_Cash_ID+"=?"; 
		List<MCashLine> list = new Query(getCtx(),I_C_CashLine.Table_Name,  whereClause, get_TrxName())
								.setParameters(getC_Cash_ID())
								.setOrderBy(I_C_CashLine.COLUMNNAME_Line)
								.setOnlyActiveRecords(true)
								.list();
		
		m_lines =  list.toArray(new MCashLine[list.size()]);
		return m_lines;
	}	//	getLines

	*/
	public String getDocumentNo()
	{
		return getName();
	}	//	getDocumentNo

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		StringBuilder msgreturn = new StringBuilder().append(Msg.getElement(getCtx(), "C_Cash_ID")).append(" ").append(getDocumentNo());
		return msgreturn.toString();
	}	//	getDocumentInfo

	

	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
		return true;
	}	//	beforeSave
	
	
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

	
	
	public String completeIt()
	{
		m_processMsg = "";
		
		
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	
	
	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		
	}	//	addDescription

	
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		if (m_processMsg != null)
			return false;
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		
				
		return true;
	}	//	reActivateIt
	
	/**
	 * 	Set Processed
	 *	@param processed processed
	 */
	/*Khong dung LINE
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		StringBuilder sql = new StringBuilder("UPDATE C_CashLine SET Processed='")
			.append((processed ? "Y" : "N"))
			.append("' WHERE C_Cash_ID=").append(getC_Cash_ID());
		int noLine = DB.executeUpdate (sql.toString(), get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed
	*/
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MCash[");
		sb.append (get_ID ())
			.append ("-").append (getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		
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

	
	
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds) 
			|| DOCSTATUS_Closed.equals(ds);
	}	//	isComplete

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}


	
}	//	MCash
