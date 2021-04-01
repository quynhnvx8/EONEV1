
package org.adempiere.webui.apps;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Combobox;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.factory.ButtonFactory;
import org.adempiere.webui.info.InfoWindow;
import org.adempiere.webui.process.WProcessInfo;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.FDialog;
import org.adempiere.webui.window.MultiFileDownloadDialog;
import org.adempiere.webui.window.SimplePDFViewer;
import org.compiere.Adempiere;
import org.compiere.util.CLogger;
import org.compiere.util.Callback;
import org.compiere.util.ContextRunnable;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.IProcessUI;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Space;
import org.zkoss.zul.Vlayout;

import eone.base.model.MLookup;
import eone.base.model.MProcess;
import eone.base.model.MSysConfig;
import eone.base.process.ProcessInfo;

public abstract class AbstractProcessDialog extends Window implements IProcessUI, EventListener<Event>
{
	/**
	 * Quynhnv.x8 
	 * Mod: 25/09/2020
	 */
	private static final long serialVersionUID = -9220870163215609274L;

	private static final String ON_COMPLETE = "onComplete";
	private static final String ON_STATUS_UPDATE = "onStatusUpdate";
	
	private static final CLogger log = CLogger.getCLogger(AbstractProcessDialog.class);

	protected int m_WindowNo;
	private Properties m_ctx;
	private int m_AD_Process_ID;
	private ProcessInfo m_pi = null;
	private boolean m_disposeOnComplete;

	private ProcessParameterPanel parameterPanel = null;
	private Checkbox runAsJobField = null;

	private BusyDialog progressWindow;	
	
	private String		    m_Name = null;
	private String initialMessage;
	
	private boolean m_valid = true;
	private boolean m_cancel = false;
		
	private Future<?> future;
	private List<File> downloadFiles;
	private boolean m_locked = false;
	private String	m_AD_Process_UU = "";
		
	protected AbstractProcessDialog()
	{
		super();		
	}
	
	
	protected boolean init(Properties ctx, int WindowNo, int AD_Process_ID, ProcessInfo pi, boolean autoStart, boolean isDisposeOnComplete)
	{
		m_ctx = ctx;
		m_WindowNo = WindowNo;
		m_AD_Process_ID = AD_Process_ID;
		setProcessInfo(pi);
		m_disposeOnComplete = isDisposeOnComplete;
		
		log.config("");
		//
		boolean trl = !Env.isBaseLanguage(m_ctx, "AD_Process");
		String sql = "SELECT Name, Description, AD_Process_UU "
				+ "FROM AD_Process "
				+ "WHERE AD_Process_ID=?";
		if (trl)
			sql = "SELECT t.Name, t.Description, AD_Process_UU "
				+ "FROM AD_Process p, AD_Process_Trl t "
				+ "WHERE p.AD_Process_ID=t.AD_Process_ID"
				+ " AND p.AD_Process_ID=? AND t.AD_Language=?";

		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Process_ID);
			if (trl)
				pstmt.setString(2, Env.getAD_Language(m_ctx));
			rs = pstmt.executeQuery();
			StringBuilder buildMsg = new StringBuilder();
			if (rs.next())
			{
				m_Name = rs.getString(1);
				//
				buildMsg.append("<b>");
				String s = rs.getString(2);		//	Description
				if (rs.wasNull())
					buildMsg.append(Msg.getMsg(m_ctx, "StartProcess?"));
				else
					buildMsg.append(s);
				buildMsg.append("</b>");

				m_AD_Process_UU = rs.getString(3);
			}
			
			initialMessage = buildMsg.toString();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return false;
		}
		finally
		{
			DB.close(rs, pstmt);
		}

		if (m_Name == null)
			return false;
		//
		this.setTitle(m_Name);

		//	Move from APanel.actionButton
		if (m_pi == null)
			m_pi = new WProcessInfo(m_Name, AD_Process_ID);
		m_pi.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
		m_pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
		m_pi.setTitle(m_Name);
		m_pi.setAD_Process_UU(m_AD_Process_UU);
		
		parameterPanel = new ProcessParameterPanel(m_WindowNo, m_pi);		
		if ( !parameterPanel.init() ) {
			
			if (autoStart)
			{
				layout();
				bOK.setDisabled(true);
				bCancel.setDisabled(true);
				autoStart();
				return true;
			}
		}

		
		layout();
		
		return true;
	}
	
	protected HtmlBasedComponent topParameterLayout;
	protected HtmlBasedComponent bottomParameterLayout;
	protected HtmlBasedComponent mainParameterLayout;
	protected WTableDirEditor fPrintFormat;
	//private WEditor fLanguageType;
	protected Listbox freportType;
	//private Checkbox chbIsSummary;
	protected Button bOK;
	protected Button bCancel;
	protected Combobox fSavedName=new Combobox();
	
	protected void layout(){
		overalLayout();
		topLayout(topParameterLayout);
		bottomLayout(bottomParameterLayout);
		
	}
	
	protected void overalLayout(){
		mainParameterLayout = new Div();
		mainParameterLayout.setSclass("main-parameter-layout"); 
		this.appendChild(mainParameterLayout);
		// header and input component
		topParameterLayout = new Vlayout();
		topParameterLayout.setSclass("top-parameter-layout"); 
		mainParameterLayout.appendChild(topParameterLayout);
		ZKUpdateUtil.setVflex(topParameterLayout, "true");
		// button and advanced control
		bottomParameterLayout = new Vlayout();
		bottomParameterLayout.setSclass("bottom-parameter-layout"); 
		mainParameterLayout.appendChild(bottomParameterLayout);		
	}
	
	protected void topLayout(HtmlBasedComponent topParameterLayout) {
		// message
		setHeadMessage (topParameterLayout, initialMessage);
		
		// input component
		HtmlBasedComponent inputParameterLayout = new Div();
		inputParameterLayout.setSclass("input-paramenter-layout"); 
		topParameterLayout.appendChild(inputParameterLayout);
		
		// input parameter content
		inputParameterLayout(inputParameterLayout);
	}
	
	protected HtmlBasedComponent setHeadMessage (HtmlBasedComponent parent, String contentMsg){
		// message
		HtmlBasedComponent messageParameterLayout = new Vlayout();
		parent.appendChild(messageParameterLayout);
		messageParameterLayout.setSclass("message-parameter-layout");
		
		// header content
		HtmlBasedComponent messageDiv = new Div();
		Html content = new Html();
		if (contentMsg != null){
			content.setContent(contentMsg);
		}
		messageDiv.appendChild(content);
		messageDiv.setSclass("message-paramenter");
		messageParameterLayout.appendChild(messageDiv);
		
		return content;
	}
	
	protected void inputParameterLayout (HtmlBasedComponent parent) {
		parent.appendChild(parameterPanel);
		
		if (MSysConfig.getBooleanValue(MSysConfig.BACKGROUND_JOB_ALLOWED, false))
		{
			Grid grid = GridFactory.newGridLayout();
			parent.appendChild(grid);

			Columns columns = new Columns();
			grid.appendChild(columns);
			Column col = new Column();
			ZKUpdateUtil.setWidth(col, "30%");
			columns.appendChild(col);
			col = new Column();
			ZKUpdateUtil.setWidth(col, "70%");
			columns.appendChild(col);
			
			Rows rows = new Rows();
			grid.appendChild(rows);
			
			Row row = new Row();
			rows.appendChild(row);
			
			row.appendChild(new Space());
						
			row = new Row();
			rows.appendChild(row);
			
			
			
		}
	}
	
	protected void bottomLayout(HtmlBasedComponent bottomParameterLayout) {
		//reportOptionLayout(bottomParameterLayout);
		
		HtmlBasedComponent bottomContainer = new Div ();
		bottomContainer.setSclass("bottom-container");
		bottomParameterLayout.appendChild(bottomContainer);
		
		buttonLayout (bottomContainer);
	}
	
	protected boolean isReport () {
		MProcess pr = new MProcess(m_ctx, m_AD_Process_ID, null);
		return pr.isReport() && pr.getJasperReport() == null;
	}
	
	protected boolean isJasperReport () {
		MProcess pr = new MProcess(m_ctx, m_AD_Process_ID, null);
		return pr.isReport() && pr.getJasperReport() != null;
	}
	
	protected void buttonLayout (HtmlBasedComponent bottomParameterLayout) {
		HtmlBasedComponent confParaPanel =new Div();
		confParaPanel.setSclass("button-container");
		bottomParameterLayout.appendChild(confParaPanel);
		
		// Invert - Unify  OK/Cancel IDEMPIERE-77
		bOK = ButtonFactory.createNamedButton(ConfirmPanel.A_OK, true, true);
		bOK.setId("Ok");
		bOK.addEventListener(Events.ON_CLICK, this);
		confParaPanel.appendChild(bOK);
		confParaPanel.appendChild(new Space());
		
		bCancel = ButtonFactory.createNamedButton(ConfirmPanel.A_CANCEL, true, true);
		bCancel.setId("Cancel");
		bCancel.addEventListener(Events.ON_CLICK, this);
		confParaPanel.appendChild(bCancel);
		
	}
	
	
	
	protected void autoStart()
	{
		startProcess0();
	}
	
	public void onEvent(Event event) 
	{
		Component component = event.getTarget();
		if (component == runAsJobField && event.getName().equals(Events.ON_CHECK))
		{
			mainParameterLayout.invalidate();

		}
		else if (event.getName().equals(ON_COMPLETE))
			onComplete();
		else if (event.getName().equals(ON_STATUS_UPDATE))
			onStatusUpdate(event);
		
	}
	
	
	public  Comboitem getComboItem( String value) {
		Comboitem item = null;
		for (int i = 0; i < fSavedName.getItems().size(); i++) {
			if (fSavedName.getItems().get(i) != null) {
				item = (Comboitem)fSavedName.getItems().get(i);
				if (value.equals(item.getLabel().toString())) {
					break;
				}
			}
		}
		return item;
	}
		
	
	
	protected void startProcess()
	{
		if (!parameterPanel.validateParameters())
			return;
		
		startProcess0();
	}
	
	protected void cancelProcess() 
	{
		m_cancel = true;
		this.dispose();
	}
	
	protected BusyDialog createBusyDialog() 
	{
		progressWindow = new BusyDialog();
		this.appendChild(progressWindow);
		return progressWindow;
	}
	
	protected void closeBusyDialog() 
	{
		if (progressWindow != null) {
			progressWindow.dispose();
			progressWindow = null;
		}
	}
	
	@Override
	public void dispose()
	{
		m_valid = false;
	}	//	dispose
	
	private void startProcess0()
	{		
		if (!isBackgroundJob())
			getProcessInfo().setPrintPreview(true);

		lockUI(getProcessInfo());
		
		downloadFiles = new ArrayList<File>();

		//use echo, otherwise lock ui wouldn't work
		Clients.response(new AuEcho(this, isBackgroundJob() ? "runBackgroundJob" : "runProcess", this));
	}
	
	public void runProcess() 
	{
		Events.sendEvent(DialogEvents.ON_BEFORE_RUN_PROCESS, this, null);
		future = Adempiere.getThreadPoolExecutor().submit(new DesktopRunnable(new ProcessDialogRunnable(null), getDesktop()));
	}

	
	
	private void onComplete()
	{
		ProcessInfo m_pi = getProcessInfo();
		
		if (future != null) {
			try {
				future.get();
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				if (!m_pi.isError()) {
					m_pi.setSummary(e.getLocalizedMessage(), true);
				}
			}
		}
		future = null;
		unlockUI(m_pi);
		if (downloadFiles.size() > 0) {
			MultiFileDownloadDialog downloadDialog = new MultiFileDownloadDialog(downloadFiles.toArray(new File[0]));
			downloadDialog.setPage(getPage());
			downloadDialog.setTitle(m_pi.getTitle());
			Events.postEvent(downloadDialog, new Event(MultiFileDownloadDialog.ON_SHOW));
		}
		
		if (m_disposeOnComplete)
			dispose();
	}
	
	private void onStatusUpdate(Event event) 
	{
		String message = (String) event.getData();
		if (progressWindow != null)
			progressWindow.statusUpdate(message);
	}

	@Override
	public void lockUI(ProcessInfo pi) {
		if (m_locked || Executions.getCurrent() == null) 
			return;
		m_locked = true;
		showBusyDialog();
	}
	
	public abstract void showBusyDialog();

	@Override
	public void unlockUI(ProcessInfo pi) {
		if (!m_locked) 
			return;
		m_locked = false;
		
		if (Executions.getCurrent() == null) 
		{
			if (getDesktop() != null) 
			{
				Executions.schedule(getDesktop(), new EventListener<Event>() 
				{
					@Override
					public void onEvent(Event event) throws Exception {
						doUnlockUI();
					}
				}, new Event("onUnLockUI"));
			}
		} else {
			doUnlockUI();
		}
	}
	
	private void doUnlockUI()
	{
		hideBusyDialog();
		updateUI();		
	}
	
	public abstract void hideBusyDialog();
	
	public abstract void updateUI();

	@Override
	public boolean isUILocked() {
		return m_locked;
	}

	@Override
	public void statusUpdate(String message) {
		Desktop desktop = getDesktop();
		if (desktop != null && desktop.isAlive())
			Executions.schedule(desktop, this, new Event(ON_STATUS_UPDATE, this, message));
	}

	@Override
	public void ask(final String message, final Callback<Boolean> callback) {
		Executions.schedule(getDesktop(), new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				FDialog.ask(getWindowNo(), null, message, callback);
			}
		}, new Event("onAsk"));
	}

	@Override
	public void download(File file) {
		downloadFiles.add(file);
	}

	/**
	 * 
	 * @return ProcessInfo
	 */
	public ProcessInfo getProcessInfo() {
		return m_pi;
	}
	
	public void setProcessInfo(ProcessInfo pi) {
		m_pi = pi;
	}

	/**
	 * is dialog still valid
	 * @return boolean
	 */
	public boolean isValid()
	{
		return m_valid;
	}
	
	/**
	 * @return true if user have press the cancel button to close the dialog
	 */
	public boolean isCancel()
	{
		return m_cancel;
	}
	
	public Properties getCtx()
	{
		return m_ctx;
	}

	public int getWindowNo()
	{
		return m_WindowNo;
	}
	
	public int getAD_Process_ID()
	{
		return m_AD_Process_ID;
	}
		
	public ProcessParameterPanel getParameterPanel()
	{
		return parameterPanel;
	}
	
	public String getName()
	{
		return m_Name;
	}

	

	public String getInitialMessage()
	{
		return initialMessage;
	}
	
	public boolean isBackgroundJob()
	{
		return runAsJobField != null && runAsJobField.isChecked();
	}
	
	
	public List<File> getDownloadFiles()
	{
		return downloadFiles;
	}
	
	private class ProcessDialogRunnable extends ContextRunnable
	{
		private Trx m_trx;
		
		private ProcessDialogRunnable(Trx trx) 
		{
			super();			
			m_trx = trx;
		}
		
		protected void doRun() 
		{
			ProcessInfo m_pi = getProcessInfo();
			try {
				if (log.isLoggable(Level.INFO))
					log.log(Level.INFO, "Process Info=" + m_pi + " AD_Client_ID="+ Env.getAD_Client_ID(Env.getCtx()));
				WProcessCtl.process(AbstractProcessDialog.this, getWindowNo(), getParameterPanel(), m_pi, m_trx);
			} catch (Exception ex) {
				m_pi.setError(true);
				m_pi.setSummary(ex.getLocalizedMessage());
				log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			} finally {
				Executions.schedule(getDesktop(), AbstractProcessDialog.this, new Event(ON_COMPLETE, AbstractProcessDialog.this, null));
			}		
		}
	}
	

	@Override
	public void askForInput(final String message, final Callback<String> callback) {
		Executions.schedule(getDesktop(), new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				FDialog.askForInput(m_WindowNo, null, message, callback);
			}
		}, new Event("onAskForInput"));
	}

	@Override
	public void askForInput(final String message, MLookup lookup, int editorType, final Callback<Object> callback) {
		FDialog.askForInput(message, lookup, editorType, callback, getDesktop(), m_WindowNo);
	}

	@Override
	public void showReports(List<File> pdfList) {

		if (pdfList == null || pdfList.isEmpty())
			return;

		AEnv.executeAsyncDesktopTask(new Runnable() {
			@Override
			public void run() {
				if (pdfList.size() > 1) {
					try {
						File outFile = File.createTempFile(m_Name, ".pdf");
						AEnv.mergePdf(pdfList, outFile);
						Window win = new SimplePDFViewer(m_Name, new FileInputStream(outFile));
						win.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
						SessionManager.getAppDesktop().showWindow(win, "center");
					} catch (Exception e) {
						log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				} else if (pdfList.size() > 0) {
					try {
						Window win = new SimplePDFViewer(m_Name, new FileInputStream(pdfList.get(0)));
						win.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
						SessionManager.getAppDesktop().showWindow(win, "center");
					} catch (Exception e) {
						log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
		});
	}

	@Override
	public void showInfoWindow(int WindowNo, String tableName, String keyColumn, String queryValue,
			boolean multipleSelection, String whereClause, Integer AD_InfoWindow_ID, boolean lookup) {

		if (AD_InfoWindow_ID <= 0)
			return;

		AEnv.executeAsyncDesktopTask(new Runnable() {
			@Override
			public void run() {
				try {
					Window win = new InfoWindow(WindowNo, tableName, keyColumn, queryValue, multipleSelection,
							whereClause, AD_InfoWindow_ID, lookup);

					SessionManager.getAppDesktop().showWindow(win, "center");
				} catch (Exception e) {
					log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}

			}
		});
	}
}
