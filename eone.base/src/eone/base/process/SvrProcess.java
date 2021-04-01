package eone.base.process;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.event.EventManager;
import org.adempiere.base.event.EventProperty;
import org.adempiere.base.event.IEventManager;
import org.adempiere.base.event.IEventTopics;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.IProcessUI;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.osgi.service.event.Event;

import eone.base.model.PO;


public abstract class SvrProcess implements ProcessCall
{
	public static final String PROCESS_INFO_CTX_KEY = "ProcessInfo";
	public static final String PROCESS_UI_CTX_KEY = "ProcessUI";
	
	private List<ProcessInfoLog> listEntryLog;  


	public void addBufferLog(int id, Timestamp date, BigDecimal number, String msg, int tableId ,int recordId) {
		ProcessInfoLog entryLog = new ProcessInfoLog(id, date, number, msg, tableId, recordId);
		
		if (listEntryLog == null)
			listEntryLog = new ArrayList<ProcessInfoLog>();
		
		listEntryLog.add(entryLog);
	}


	public SvrProcess()
	{
	//	Env.ZERO.divide(Env.ZERO);
	}   //  SvrProcess

	private Properties  		m_ctx;
	private ProcessInfo			m_pi;

	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/**	Is the Object locked			*/
	private boolean				m_locked = false;
	/**	Loacked Object					*/
	private PO					m_lockedObject = null;
	/** Process Main transaction 		*/
	private Trx 				m_trx;
	protected IProcessUI 	processUI;

	/**	Common Error Message			*/
	protected static String 	MSG_SaveErrorRowNotFound = "@SaveErrorRowNotFound@";
	protected static String		MSG_InvalidArguments = "@InvalidArguments@";



	public final boolean startProcess (Properties ctx, ProcessInfo pi, Trx trx, HashMap<String, Object> params)
	{
		//  Preparation
		m_ctx = ctx == null ? Env.getCtx() : ctx;
		m_pi = pi;
		m_trx = trx;
		//***	Trx
		boolean localTrx = m_trx == null;
		if (localTrx)
		{
			m_trx = Trx.get(Trx.createTrxName("SvrProcess"), true);
			m_trx.setDisplayName(getClass().getName()+"_startProcess");
		}
		m_pi.setTransactionName(m_trx.getTrxName());
		m_pi.setProcessUI(processUI);
		//
		ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader processLoader = getClass().getClassLoader();
		try {
			if (processLoader != contextLoader) {
				Thread.currentThread().setContextClassLoader(processLoader);
			}
			lock();
			
			boolean success = false;
					
			try 
			{			
				m_ctx.put(PROCESS_INFO_CTX_KEY, m_pi);
				if (processUI != null)
					m_ctx.put(PROCESS_UI_CTX_KEY, processUI);
				success = process();			
			}
			finally
			{
				m_ctx.remove(PROCESS_INFO_CTX_KEY);
				m_ctx.remove(PROCESS_UI_CTX_KEY);
				if (localTrx)
				{
					if (success)
					{
						try 
						{
							m_trx.commit(true);
						} catch (Exception e)
						{
							log.log(Level.SEVERE, "Commit failed.", e);
							m_pi.addSummary("Commit Failed.");
							m_pi.setError(true);
						}
					}
					else
						m_trx.rollback();
					m_trx.close();
					m_trx = null;
					m_pi.setTransactionName(null);
				}
			
				unlock();
				
				// outside transaction processing [ teo_sarca, 1646891 ]
				postProcess(!m_pi.isError());

				@SuppressWarnings("unused")
				Event eventPP = sendProcessEvent(IEventTopics.POST_PROCESS);

				Thread.currentThread().setContextClassLoader(contextLoader);
			}
		} finally {
			if (processLoader != contextLoader) {
				Thread.currentThread().setContextClassLoader(contextLoader);
			}
		}
		
		return !m_pi.isError();
	}   //  startProcess

	
	/**************************************************************************
	 *  Process
	 *  @return true if success
	 */
	private boolean process()
	{
		String msg = null;
		boolean success = true;
		try
		{
			prepare();

			// event before process
			Event eventBP = sendProcessEvent(IEventTopics.BEFORE_PROCESS);
			@SuppressWarnings("unchecked")
			List<String> errorsBP = (List<String>) eventBP.getProperty(IEventManager.EVENT_ERROR_MESSAGES);
			if (errorsBP != null && !errorsBP.isEmpty()) {
				msg = "@Error@:" + errorsBP.get(0);
			} else {
				msg = doIt();
				if (msg != null && ! msg.startsWith("@Error@")) {
					Event eventAP = sendProcessEvent(IEventTopics.AFTER_PROCESS);
					@SuppressWarnings("unchecked")
					List<String> errorsAP = (List<String>) eventAP.getProperty(IEventManager.EVENT_ERROR_MESSAGES);
					if (errorsAP != null && !errorsAP.isEmpty()) {
						msg = "@Error@:" + errorsAP.get(0);
					}
				}
			}
		}
		catch (Throwable e)
		{
			msg = e.getLocalizedMessage();
			if (msg == null)
				msg = e.toString();
			if (e.getCause() != null)
				log.log(Level.SEVERE, msg, e.getCause());
			else 
				log.log(Level.SEVERE, msg, e);
			success = false;
		//	throw new RuntimeException(e);
		}
		
		//transaction should rollback if there are error in process
		if(msg != null && msg.startsWith("@Error@"))
			success = false;

		if (success)
			flushBufferLog();

		//	Parse Variables
		msg = Msg.parseTranslation(m_ctx, msg);
		m_pi.setSummary (msg, !success);
		
		return success;
	}   //  process

	private Event sendProcessEvent(String topic) {
		Event event = EventManager.newEvent(topic,
				new EventProperty(EventManager.EVENT_DATA, m_pi),
				new EventProperty("processUUID", m_pi.getAD_Process_UU()),
				new EventProperty("className", m_pi.getClassName()),
				new EventProperty("processClassName", this.getClass().getName()));
		EventManager.getInstance().sendEvent(event);
		return event;
	}


	abstract protected void prepare();


	abstract protected String doIt() throws Exception;


	protected void postProcess(boolean success) {
	}
	

	protected void commit()
	{
		if (m_trx != null)
			m_trx.commit();
	}	//	commit
	

	protected void commitEx() throws SQLException
	{
		if (m_trx != null)
			m_trx.commit(true);
	}
	
	/**
	 * 	Rollback
	 */
	protected void rollback()
	{
		if (m_trx != null)
			m_trx.rollback();
	}	//	rollback
	
	

	protected boolean lockObject (PO po)
	{
		//	Unlock existing
		if (m_locked || m_lockedObject != null)
			unlockObject();
		//	Nothing to lock			
		if (po == null)
			return false;
		m_lockedObject = po;
		m_locked = m_lockedObject.lock();
		return m_locked;
	}	//	lockObject

	/**
	 * 	Is an object Locked?
	 *	@return true if object locked
	 */
	protected boolean isLocked()
	{
		return m_locked;
	}	//	isLocked


	protected boolean unlockObject()
	{
		boolean success = true;
		if (m_locked || m_lockedObject != null)
		{
			success = m_lockedObject.unlock(null);
		}
		m_locked = false;
		m_lockedObject = null;
		return success;
	}	//	unlock


	/**************************************************************************
	 *  Get Process Info
	 *  @return Process Info
	 */
	public ProcessInfo getProcessInfo()
	{
		return m_pi;
	}   //  getProcessInfo

	/**
	 *  Get Properties
	 *  @return Properties
	 */
	public Properties getCtx()
	{
		return m_ctx;
	}   //  getCtx

	/**
	 *  Get Name/Title
	 *  @return Name
	 */
	protected String getName()
	{
		return m_pi.getTitle();
	}   //  getName

	/**
	 *  Get Process Instance
	 *  @return Process Instance
	 */
	protected String getAD_PInstance_ID()
	{
		return m_pi.getAD_Session_ID();
	}   //  getAD_PInstance_ID

	/**
	 *  Get Table_ID
	 *  @return AD_Table_ID
	 */
	protected int getTable_ID()
	{
		return m_pi.getTable_ID();
	}   //  getRecord_ID

	/**
	 *  Get Record_ID
	 *  @return Record_ID
	 */
	protected int getRecord_ID()
	{
		return m_pi.getRecord_ID();
	}   //  getRecord_ID

	/**
	 * Get Record_IDs
	 * 
	 * @return Record_IDs
	 */
	protected List<Integer> getRecord_IDs() 
	{
		return m_pi.getRecord_IDs();
	} // getRecord_IDs

	/**
	 *  Get AD_User_ID
	 *  @return AD_User_ID of Process owner or -1 if not found
	 */
	protected int getAD_User_ID()
	{
		return Env.getAD_User_ID(getCtx());
	}   //  getAD_User_ID

	/**
	 *  Get AD_User_ID
	 *  @return AD_User_ID of Process owner
	 */
	protected int getAD_Client_ID()
	{
		if (m_pi.getAD_Client_ID() == null)
		{
			getAD_User_ID();	//	sets also Client
			if (m_pi.getAD_Client_ID() == null)
				return 0;
		}
		return m_pi.getAD_Client_ID().intValue();
	}	//	getAD_Client_ID

	
	/**************************************************************************
	 * 	Get Parameter
	 *	@return parameter
	 */
	protected ProcessInfoParameter[] getParameter()
	{
		ProcessInfoParameter[] retValue = m_pi.getParameter();
		if (retValue == null)
		{
			ProcessInfoUtil.setParameterFromDB(m_pi);
			retValue = m_pi.getParameter();
		}
		return retValue;
	}	//	getParameter


	/**************************************************************************
	 *  Add Log Entry with table name
	 *  
	 */
	public void addLog (int id, Timestamp date, BigDecimal number, String msg, int tableId ,int recordId)
	{
		if (m_pi != null)
			m_pi.addLog(id, date, number, msg,tableId,recordId);
		
		if (log.isLoggable(Level.INFO)) log.info(id + " - " + date + " - " + number + " - " + msg + " - " + tableId + " - " + recordId);
	}	//	addLog

	/**************************************************************************
	 *  Add Log Entry
	 *  @param date date or null
	 *  @param id record id or 0
	 *  @param number number or null
	 *  @param msg message or null
	 */
	public void addLog (int id, Timestamp date, BigDecimal number, String msg)
	{
		if (m_pi != null)
			m_pi.addLog(id, date, number, msg);
		if (log.isLoggable(Level.INFO)) log.info(id + " - " + date + " - " + number + " - " + msg);
	}	//	addLog

	/**
	 * 	Add Log
	 *	@param msg message
	 */
	public void addLog (String msg)
	{
		if (msg != null)
			addLog (0, null, null, msg);
	}	//	addLog

	private void flushBufferLog () {
		if (listEntryLog == null)
			return;

		for (ProcessInfoLog entryLog : listEntryLog) {
			if (m_pi != null)
				m_pi.addLog(entryLog);
			if (log.isLoggable(Level.INFO)) log.info(entryLog.getP_ID() + " - " + entryLog.getP_Date() + " - " + entryLog.getP_Number() + " - " + entryLog.getP_Msg() + " - " + entryLog.getAD_Table_ID() + " - " + entryLog.getRecord_ID());
		}							
	}

	/**************************************************************************
	 * 	Execute function
	 * 	@param className class
	 * 	@param methodName method
	 * 	@param args arguments
	 * 	@return result
	 */
	public Object doIt (String className, String methodName, Object args[])
	{
		try
		{
			Class<?> clazz = Class.forName(className);
			Object object = clazz.getDeclaredConstructor().newInstance();
			Method[] methods = clazz.getMethods();
			for (int i = 0; i < methods.length; i++)
			{
				if (methods[i].getName().equals(methodName))
					return methods[i].invoke(object, args);
			}
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "doIt", ex);
			throw new RuntimeException(ex);
		}
		return null;
	}	//	doIt

	
	/**************************************************************************
	 *  Lock Process Instance
	 */
	private void lock()
	{
		/*
		if (log.isLoggable(Level.FINE)) log.fine("AD_PInstance_ID=" + m_pi.getAD_PInstance_ID());
		try 
		{
			DB.executeUpdate("UPDATE AD_PInstance SET IsProcessing='Y' WHERE AD_PInstance_ID=" 
				+ m_pi.getAD_PInstance_ID(), null);		//	outside trx
		} catch (Exception e)
		{
			log.severe("lock() - " + e.getLocalizedMessage());
		}
		*/
	}   //  lock

	/**
	 *  Unlock Process Instance.
	 *  Update Process Instance DB and write option return message
	 */
	private void unlock ()
	{
		/*
		boolean noContext = Env.getCtx().isEmpty() && Env.getCtx().getProperty("#AD_Client_ID") == null;
		try 
		{
			//save logging info even if context is lost
			if (noContext)
				Env.getCtx().put("#AD_Client_ID", m_pi.getAD_Client_ID());
			//Quynhnv.x8: Bo_AD_PInstance
			
			MPInstance mpi = new MPInstance (getCtx(), m_pi.getAD_PInstance_ID(), null);
			if (mpi.get_ID() == 0)
			{
				log.log(Level.SEVERE, "Did not find PInstance " + m_pi.getAD_PInstance_ID());
				return;
			}
			mpi.setIsProcessing(false);
			mpi.setResult(!m_pi.isError());
			mpi.setErrorMsg(m_pi.getSummary());
			mpi.saveEx();
			
			if (log.isLoggable(Level.FINE)) log.fine(mpi.toString());
			
			ProcessInfoUtil.saveLogToDB(m_pi);
		} 
		catch (Exception e)
		{
			log.severe("unlock() - " + e.getLocalizedMessage());
		}
		finally
		{
			if (noContext)
				Env.getCtx().remove("#AD_Client_ID");
		}
		*/
	}   //  unlock

	/**
	 * Return the main transaction of the current process.
	 * @return the transaction name
	 */
	public String get_TrxName()
	{
		if (m_trx != null)
			return m_trx.getTrxName();
		return null;
	}	//	get_TrxName

	@Override
	public void setProcessUI(IProcessUI monitor)
	{
		processUI = monitor;
	}
	
	/**
	 * publish status update message
	 * @param message
	 */
	protected void statusUpdate(String message)
	{
		if (processUI != null)
		{
			processUI.statusUpdate(message);
		}
	}
}   //  SvrProcess
