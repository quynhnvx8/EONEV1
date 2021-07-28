
package eone.base.process;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.adempiere.base.event.EventManager;
import org.adempiere.base.event.EventProperty;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import eone.base.acct.DocManager;
import eone.base.model.MClient;
import eone.base.model.MColumn;
import eone.base.model.MTable;
import eone.base.model.PO;
import eone.base.model.SystemIDs;


public class DocumentEngine implements DocAction
{

	
	public DocumentEngine (DocAction po, String docStatus, int AD_Window_ID)
	{
		m_document = po;
		if (docStatus != null)
			m_status = docStatus;
		this.AD_Window_ID = AD_Window_ID;
	}	//	DocActionEngine

	/** Persistent Document 	*/
	private DocAction	m_document;
	/** Document Status			*/
	private String		m_status = STATUS_Drafted;
	/**	Process Message 		*/
	private String		m_message = null;
	/** Actual Doc Action		*/
	private String		m_action = null;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocumentEngine.class);

	/**
	 * 	Get Doc Status
	 *	@return document status
	 */
	public String getDocStatus()
	{
		return m_status;
	}	//	getDocStatus

	@Override
	public void setDocStatus(String ignored)
	{
		
	}	//	setDocStatus

	
	public boolean isDrafted()
	{
		return STATUS_Drafted.equals(m_status);
	}	//	isDrafted

	
	
	/**
	 * 	Document is Completed
	 *	@return true if Completed
	 */
	public boolean isCompleted()
	{
		return STATUS_Completed.equals(m_status);
	}	//	isCompleted

	
	public boolean processIt (String processAction, String docStatus)
	{
		//ensure doc status not change by other session
		/*FIXME: Khong can doan check nay: Do ban ghi cua nguoi nao thi nguoi ay COMPLETE
		if (m_document instanceof PO) {
			PO docPO = (PO) m_document;
			if (docPO.get_ID() > 0 && docPO.get_TrxName() != null && docPO.get_ValueOld("DocStatus") != null) {
				DB.getDatabase().forUpdate(docPO, 30);
				String docStatusOriginal = (String) docPO.get_ValueOld("DocStatus");
				String statusSql = "SELECT DocStatus FROM " + docPO.get_TableName() + " WHERE " + docPO.get_KeyColumns()[0] + " = ? ";
				String currentStatus = DB.getSQLValueString((String)null, statusSql, docPO.get_ID());
				if (!docStatusOriginal.equals(currentStatus) && currentStatus != null) {
					currentStatus = DB.getSQLValueString(docPO.get_TrxName(), statusSql, docPO.get_ID());
					if (!docStatusOriginal.equals(currentStatus)) {
						throw new IllegalStateException(Msg.getMsg(docPO.getCtx(), "DocStatusChanged") + " " + docPO.toString());
					}
				}
			}
		}
		*/
		
		m_message = null;
		m_action = null;

		if (isValidAction(processAction))	//	WF Selection first
			m_action = processAction;
		//
		else if (isValidAction(docStatus))	//	User Selection second
			m_action = docStatus;
		//	Nothing to do
		
		else
		{
			throw new IllegalStateException("Status=" + getDocStatus()
				+ " - Invalid Actions: Process="  + processAction + ", Doc=" + docStatus);
		}
		if (m_document != null)
			m_document.get_Logger().info ("**** Action=" + m_action + " (Prc=" + processAction + "/Doc=" + docStatus + ") " + m_document);
		boolean success = processIt (m_action, AD_Window_ID);
		if (m_document != null)
			m_document.get_Logger().fine("**** Action=" + m_action + " - Success=" + success);
		return success;
	}	//	process

	
	public boolean processIt (String action, int AD_Window_ID)
	{
		m_message = null;
		m_action = action;
		
		if (STATUS_Completed.equals(m_action) )
		{
			String status = completeIt();
			boolean ok =   STATUS_Completed.equals(status);
			if (m_document != null && ok && MClient.isClientPostImmediate())
			{
				m_document.saveEx();
				boolean retVal = postIt();
				if (! retVal) {
					return false;
				}

			}
			return ok;
		}
		if (ACTION_ReActivate.equals(m_action))
			return reActivateIt();
		
		return false;
	}	//	processDocument

	
	
	public String completeIt()
	{
		if (!isValidAction(ACTION_Complete))
			return m_status;
		if (m_document != null)
		{
			m_status = m_document.completeIt();
			m_document.setDocStatus(m_status);
		}
		return m_status;
	}	//	completeIt

	/**
	 * 	Post Document
	 * 	Does not change status
	 * 	@return true if success
	 */
	public boolean postIt()
	{
		if (m_document == null)
			return false;

		String error = postImmediate(Env.getCtx(), Env.getAD_Client_ID(getCtx()), m_document.get_Table_ID(), m_document.get_ID(), true, m_document.get_TrxName(), AD_Window_ID);
		m_document.setProcessMsg(error);
		return (error == null);
	}	//	postIt

	
	public boolean reActivateIt()
	{
		if (!isValidAction(ACTION_ReActivate))
			return false;
		if (m_document != null)
		{
			if (m_document.reActivateIt())
			{
				return true;
			}
			return false;
		}
		m_status = STATUS_Drafted;
		return true;
	}	//	reActivateIt


	/**
	 * 	Set Document Status to new Status
	 *	@param newStatus new status
	 */
	void setStatus (String newStatus)
	{
		m_status = newStatus;
	}	//	setStatus


	/**************************************************************************
	 * 	Get Action Options based on current Status
	 *	@return array of actions
	 */
	public String[] getActionOptions()
	{
		
		if (isDrafted())
			return new String[] {ACTION_Complete};

		
		if (isCompleted())
			return new String[] {ACTION_ReActivate};

		return new String[] {};
	}	//	getActionOptions

	/**
	 * 	Is The Action Valid based on current state
	 *	@param action action
	 *	@return true if valid
	 */
	public boolean isValidAction (String action)
	{
		String[] options = getActionOptions();
		for (int i = 0; i < options.length; i++)
		{
			if (options[i].equals(action))
				return true;
		}
		return false;
	}	//	isValidAction

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg ()
	{
		return m_message;
	}	//	getProcessMsg

	/**
	 * 	Get Process Message
	 *	@param msg clear text error message
	 */
	public void setProcessMsg (String msg)
	{
		m_message = msg;
	}	//	setProcessMsg


	/**	Document Exception Message		*/
	private static String EXCEPTION_MSG = "Document Engine is no Document";

	/*************************************************************************
	 * 	Get Summary
	 *	@return throw exception
	 */
	public String getSummary()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document No
	 *	@return throw exception
	 */
	public String getDocumentNo()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Info
	 *	@return throw exception
	 */
	public String getDocumentInfo()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Owner
	 *	@return throw exception
	 */
	public int getDoc_User_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Currency
	 *	@return throw exception
	 */
	public int getC_Currency_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Approval Amount
	 *	@return throw exception
	 */
	public BigDecimal getApprovalAmt()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Client
	 *	@return throw exception
	 */
	public int getAD_Client_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Organization
	 *	@return throw exception
	 */
	public int getAD_Org_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Doc Action
	 *	@return Document Action
	 */
	public String getDocAction()
	{
		return m_action;
	}

	/**
	 * 	Save Document
	 *	@return throw exception
	 */
	public boolean save()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Save Document
	 *	@return throw exception
	 */
	public void saveEx() throws AdempiereException
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}
	
	/**
	 * 	Get Context
	 *	@return context
	 */
	public Properties getCtx()
	{
		if (m_document != null)
			return m_document.getCtx();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	getCtx

	/**
	 * 	Get ID of record
	 *	@return ID
	 */
	public int get_ID()
	{
		if (m_document != null)
			return m_document.get_ID();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	get_ID

	/**
	 * 	Get AD_Table_ID
	 *	@return AD_Table_ID
	 */
	public int get_Table_ID()
	{
		if (m_document != null)
			return m_document.get_Table_ID();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	get_Table_ID

	/**
	 * 	Get Logger
	 *	@return logger
	 */
	public CLogger get_Logger()
	{
		if (m_document != null)
			return m_document.get_Logger();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	get_Logger

	/**
	 * 	Get Transaction
	 *	@return trx name
	 */
	public String get_TrxName()
	{
		return null;
	}	//	get_TrxName

	/**
	 * 	CreatePDF
	 *	@return null
	 */
	public File createPDF ()
	{
		return null;
	}

	/**
	 * Get list of valid document action into the options array parameter.
	 * Set default document action into the docAction array parameter.
	 */
	public static int getValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID, String[] docAction, String[] options, boolean periodOpen, PO po)
	{
		if (options == null)
			throw new IllegalArgumentException("Option array parameter is null");
		if (docAction == null)
			throw new IllegalArgumentException("Doc action array parameter is null");

		int index = 0;


		if (docStatus.equals(DocumentEngine.STATUS_Drafted))
		{
			options[index++] = DocumentEngine.ACTION_Complete;
		}
		

		if (po instanceof DocOptions)
			index = ((DocOptions) po).customizeValidActions(docStatus, processing, orderType, isSOTrx,
					AD_Table_ID, docAction, options, index);

		AtomicInteger indexObj = new AtomicInteger(index);
		ArrayList<String> docActionsArray = new ArrayList<String>(Arrays.asList(docAction));
		ArrayList<String> optionsArray = new ArrayList<String>(Arrays.asList(options));
		DocActionEventData eventData = new DocActionEventData(docStatus, processing, orderType, isSOTrx, AD_Table_ID, docActionsArray, optionsArray, indexObj, po);
		Event event = EventManager.newEvent(IEventTopics.DOCACTION,
				new EventProperty(EventManager.EVENT_DATA, eventData),
				new EventProperty("tableName", po.get_TableName()));
		EventManager.getInstance().sendEvent(event);
		index = indexObj.get();
		for (int i = 0; i < optionsArray.size(); i++)
			options[i] = optionsArray.get(i);
		for (int i = 0; i < docActionsArray.size(); i++)
			docAction[i] = docActionsArray.get(i);

		return index;
	}

	/**
	 * Fill Vector with DocAction Ref_List(135) values
	 * @param v_value
	 * @param v_name
	 * @param v_description
	 */
	public static void readReferenceList(ArrayList<String> v_value, ArrayList<String> v_name,
			ArrayList<String> v_description)
	{
		if (v_value == null)
			throw new IllegalArgumentException("v_value parameter is null");
		if (v_name == null)
			throw new IllegalArgumentException("v_name parameter is null");
		if (v_description == null)
			throw new IllegalArgumentException("v_description parameter is null");

		String sql;
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			sql = "SELECT Value, Name, Description FROM AD_Ref_List "
				+ "WHERE AD_Reference_ID=? ORDER BY Name";
		else
			sql = "SELECT l.Value, t.Name, t.Description "
				+ "FROM AD_Ref_List l, AD_Ref_List_Trl t "
				+ "WHERE l.AD_Ref_List_ID=t.AD_Ref_List_ID"
				+ " AND t.AD_Language='" + Env.getAD_Language(Env.getCtx()) + "'"
				+ " AND l.AD_Reference_ID=? ORDER BY t.Name";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, DocAction.AD_REFERENCE_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String value = rs.getString(1);
				String name = rs.getString(2);
				String description = rs.getString(3);
				if (description == null)
					description = "";
				//
				v_value.add(value);
				v_name.add(name);
				v_description.add(description);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
	}

	
	public static String postImmediate (Properties ctx,
		int AD_Client_ID, int AD_Table_ID, int Record_ID, boolean force, String trxName, int AD_Window_ID)
	{
		if (MColumn.getColumn_ID(MTable.getTableName(ctx, AD_Table_ID), "Posted") <= 0)
			return null;

		String error = null;
		if (log.isLoggable(Level.INFO)) log.info ("Table=" + AD_Table_ID + ", Record=" + Record_ID);
		error = DocManager.postDocument(AD_Table_ID, Record_ID, force, true, trxName, AD_Window_ID);
		
		return error;
	}	//	postImmediate

	
	public static boolean processIt(DocAction doc, String processAction) {
		boolean success = false;
		
		DocumentEngine engine = new DocumentEngine(doc, doc.getDocStatus(), 0);
		success = engine.processIt(processAction, doc.getDocStatus());

		return success;
	}
	
	public static void readStatusReferenceList(ArrayList<String> v_value, ArrayList<String> v_name,
			ArrayList<String> v_description)
	{
		if (v_value == null)
			throw new IllegalArgumentException("v_value parameter is null");
		if (v_name == null)
			throw new IllegalArgumentException("v_name parameter is null");
		if (v_description == null)
			throw new IllegalArgumentException("v_description parameter is null");

		String sql;
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			sql = "SELECT Value, Name, Description FROM AD_Ref_List "
				+ "WHERE AD_Reference_ID=? ORDER BY Name";
		else
			sql = "SELECT l.Value, t.Name, t.Description "
				+ "FROM AD_Ref_List l, AD_Ref_List_Trl t "
				+ "WHERE l.AD_Ref_List_ID=t.AD_Ref_List_ID"
				+ " AND t.AD_Language='" + Env.getAD_Language(Env.getCtx()) + "'"
				+ " AND l.AD_Reference_ID=? ORDER BY t.Name";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, SystemIDs.REFERENCE_DOCUMENTSTATUS);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String value = rs.getString(1);
				String name = rs.getString(2);
				String description = rs.getString(3);
				if (description == null)
					description = "";
				//
				v_value.add(value);
				v_name.add(name);
				v_description.add(description);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
	}

	private int AD_Window_ID = 0;
	@Override
	public int getAD_Window_ID() {
		return AD_Window_ID;
		
	}
}	//	DocumentEnine
