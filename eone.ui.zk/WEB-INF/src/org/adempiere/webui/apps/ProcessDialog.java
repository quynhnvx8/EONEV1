package org.adempiere.webui.apps;

import java.text.SimpleDateFormat;
import java.util.logging.Level;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.DocumentLink;
import org.adempiere.webui.component.Mask;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.desktop.IDesktop;
import org.adempiere.webui.panel.IHelpContext;
import org.adempiere.webui.part.WindowContainer;
import org.adempiere.webui.process.WProcessInfo;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Text;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.au.out.AuScript;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

import eone.base.process.ProcessInfo;
import eone.base.process.ProcessInfoLog;

/**
 *	Dialog to Start process or report.
 *	Displays information about the process
 *		and lets the user decide to start it
 *  	and displays results (optionally print them).
 *  Calls ProcessCtl to execute.
 *  @author 	Low Heng Sin
 *  @author     arboleda - globalqss
 *  - Implement ShowHelp option on processes and reports
 */
public class ProcessDialog extends AbstractProcessDialog implements EventListener<Event>, IHelpContext
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6728929130788829223L;

	public static final String ON_INITIAL_FOCUS_EVENT = "onInitialFocus";
	
	private static final String ON_OK_ECHO = "onOkEcho";
	
	/**	Logger			*/
	private static final CLogger log = CLogger.getCLogger(ProcessDialog.class);
	//

	private Table logMessageTable;	
	//private int[]		    m_ids = null;	
	
	private boolean isParameterPage = true;	
	private Mask mask;
	private HtmlBasedComponent resultPanelLayout;
	private HtmlBasedComponent messageResultContent;
	private HtmlBasedComponent infoResultContent;

	/** Window No					*/
	private int                 m_WindowNo = -1;
	private long prevKeyEventTime = 0;
	private KeyEvent prevKeyEvent;


	public ProcessDialog (int AD_Process_ID, boolean isSOTrx)
	{
		log.info("Process=" + AD_Process_ID );
		m_WindowNo = SessionManager.getAppDesktop().registerWindow(this);
		this.setAttribute(IDesktop.WINDOWNO_ATTRIBUTE, m_WindowNo);
		Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", isSOTrx ? "Y" : "N");
		try
		{
			init(Env.getCtx(), m_WindowNo, AD_Process_ID, null, false, false);
			//querySaved();
			addEventListener(WindowContainer.ON_WINDOW_CONTAINER_SELECTION_CHANGED_EVENT, this);
			addEventListener(ON_INITIAL_FOCUS_EVENT, this);
			addEventListener(ON_OK_ECHO, this);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
	}	//	ProcessDialog
	
	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		try {
			SessionManager.getSessionApplication().getKeylistener().addEventListener(Events.ON_CTRL_KEY, this);
		} catch (Exception e) {}
	}

	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		try {
			SessionManager.getSessionApplication().getKeylistener().removeEventListener(Events.ON_CTRL_KEY, this);
		} catch (Exception e) {}
	}

	/**
	 * 	Set Visible 
	 * 	(set focus to OK if visible)
	 * 	@param visible true if visible
	 */
	public boolean setVisible (boolean visible)
	{
		return super.setVisible(visible);
	}	//	setVisible

	/**
	 *	Dispose
	 */
	public void dispose()
	{
		super.dispose();
		SessionManager.getAppDesktop().closeWindow(getWindowNo());
	}//	dispose
	
	public void onEvent(Event event) {
		Component component = event.getTarget(); 
		
		if(component instanceof A && event.getName().equals((Events.ON_CLICK))){
			doOnClick((A)component);
		} else if (bOK.equals(component)) {
			super.onEvent(event);
			
			onOk();
		} else if (event.getName().equals(ON_OK_ECHO)) {
			onOk();
		}else if (bCancel.equals(component)){
			super.onEvent(event);
			cancelProcess();
		} else if (event.getName().equals(WindowContainer.ON_WINDOW_CONTAINER_SELECTION_CHANGED_EVENT)) {
    		;//SessionManager.getAppDesktop().updateHelpContext("Process", getAD_Process_ID());
		} else if (event.getName().equals(ON_INITIAL_FOCUS_EVENT)) {
			if (!isUILocked())
			{
				if (!getParameterPanel().focusToFirstEditor()) 
				{
					bOK.focus();
				}
			}
        } else if (event.getName().equals(Events.ON_CTRL_KEY)) {
        	KeyEvent keyEvent = (KeyEvent) event;
        	if (LayoutUtils.isReallyVisible(this)) {
	        	//filter same key event that is too close
	        	//firefox fire key event twice when grid is visible
	        	long time = System.currentTimeMillis();
	        	if (prevKeyEvent != null && prevKeyEventTime > 0 &&
	        			prevKeyEvent.getKeyCode() == keyEvent.getKeyCode() &&
	    				prevKeyEvent.getTarget() == keyEvent.getTarget() &&
	    				prevKeyEvent.isAltKey() == keyEvent.isAltKey() &&
	    				prevKeyEvent.isCtrlKey() == keyEvent.isCtrlKey() &&
	    				prevKeyEvent.isShiftKey() == keyEvent.isShiftKey()) {
	        		if ((time - prevKeyEventTime) <= 300) {
	        			return;
	        		}
	        	}
	        	this.onCtrlKeyEvent(keyEvent);
        	}
		} else {
			super.onEvent(event);
		}
	}

	private void onOk() {
		if (isParameterPage)
		{
			if (getParameterPanel().isWaitingForDialog())
			{
				Events.echoEvent(ON_OK_ECHO, this, null);
				return;
			}
			startProcess();
		}
		else
			restart();
	}
	
	private void onCtrlKeyEvent(KeyEvent keyEvent) {
		if (keyEvent.isAltKey() && keyEvent.getKeyCode() == 0x58) { // Alt-X
			if (m_WindowNo > 0) {
				prevKeyEventTime = System.currentTimeMillis();
				prevKeyEvent = keyEvent;
				keyEvent.stopPropagation();
				SessionManager.getAppDesktop().closeWindow(m_WindowNo);
			}
		}
	}

	private void doOnClick(A btn) {
		int Record_ID = 0;
		int AD_Table_ID =0;
		try
		{
			Record_ID = Integer.valueOf((String)btn.getAttribute("Record_ID"));            		
			AD_Table_ID= Integer.valueOf((String)btn.getAttribute("AD_Table_ID"));            		
		}
		catch (Exception e) {
		}

		if (Record_ID > 0 && AD_Table_ID > 0) {
			
			AEnv.zoom(AD_Table_ID, Record_ID);
		}		
	}

	@Override
	public void showBusyDialog() {		
		BusyDialog progressWindow = createBusyDialog();
		progressWindow.setStyle("position: absolute;");
		showBusyMask(progressWindow);
		LayoutUtils.openOverlappedWindow(this, progressWindow, "middle_center");
	}

	private Div getMask() {
		if (mask == null) {
			mask = new Mask();
		}
		return mask;
	}
	
	private void showBusyMask(Window window) {
	  if (getParent() != null) {
		getParent().appendChild(getMask());
		StringBuilder script = new StringBuilder("var w=zk.Widget.$('#");
		script.append(getParent().getUuid()).append("');");
		if (window != null) {
			script.append("var d=zk.Widget.$('#").append(window.getUuid()).append("');w.busy=d;");
		} else {
			script.append("w.busy=true;");
		}
		Clients.response(new AuScript(script.toString()));
	  }
	}
		
	private void hideBusyMask() 
	{
		if (mask != null && mask.getParent() != null) {
			mask.detach();
			StringBuilder script = new StringBuilder("var w=zk.Widget.$('#");
			script.append(getParent().getUuid()).append("');w.busy=false;");
			Clients.response(new AuScript(script.toString()));
		}
	}
	
	@Override
	public void hideBusyDialog() 
	{		
		hideBusyMask();
		closeBusyDialog();
	}

	@Override
	public void updateUI() {
		swithToFinishScreen();
	}
	
	protected void swithToFinishScreen() {
		ProcessInfo pi = getProcessInfo();
		
		layoutResultPanel (topParameterLayout);
		
		StringBuilder buildMsg = new StringBuilder(getInitialMessage());
		buildMsg.append("<hr><p><font color=\"").append(pi.isError() ? "#FF0000" : "#0000FF").append("\">** ")
		.append(pi.getSummary())
		.append("</font></p>");
	
		((Html)messageResultContent).setContent (buildMsg.toString());
		
		// Add Log info with zoom on record id
		infoResultContent.getChildren().removeAll(infoResultContent.getChildren());
		appendRecordLogInfo(pi.getLogs(), infoResultContent);
		
		bOK.setLabel(Msg.getMsg(Env.getCtx(), "Parameter"));
		bOK.setDisabled(false);

		bCancel.setLabel(Msg.getMsg(Env.getCtx(), "Close"));
		bCancel.setDisabled(false);

    	if (ThemeManager.isUseFontIconForImage()) {
    		String iconSclass = "z-icon-Reset";
    		bOK.setIconSclass(iconSclass);
    		LayoutUtils.addSclass("font-icon-toolbar-button", bOK);
    	} else {
			bOK.setImage(ThemeManager.getThemeResource("images/Reset16.png"));
			bCancel.setImage(ThemeManager.getThemeResource("images/Cancel16.png"));
		}

		isParameterPage = false;

		//m_ids = pi.getIDs();
		
		//move message div to center to give more space to display potentially very long log info
		replaceComponent (resultPanelLayout, topParameterLayout);
		invalidate();
		Clients.response(new AuEcho(this, "onAfterProcess", null));
	}
	
	private void layoutResultPanel (HtmlBasedComponent topParameterLayout){
		if (resultPanelLayout == null){
			resultPanelLayout = new Vlayout();
			resultPanelLayout.setSclass("result-parameter-layout");
			ZKUpdateUtil.setVflex(resultPanelLayout, "true");
			// reference for update late
			messageResultContent = setHeadMessage(resultPanelLayout, null);
			
			infoResultContent = new Div();
			resultPanelLayout.appendChild(infoResultContent);
		}
	}
	
	protected void replaceComponent(HtmlBasedComponent newComponent, HtmlBasedComponent oldComponent) {
		oldComponent.getParent().insertBefore(newComponent, oldComponent);
		oldComponent.detach();
	}	
	
	private void appendRecordLogInfo(ProcessInfoLog[] m_logs, HtmlBasedComponent infoResultContent) {
		if (m_logs == null)
			return;

		SimpleDateFormat dateFormat = DisplayType
				.getDateFormat(DisplayType.Date);

		logMessageTable = new Table();
		logMessageTable.setId("logrecords");
		logMessageTable.setDynamicProperty("border", "1");
		logMessageTable.setDynamicProperty("cellpadding", "0");
		logMessageTable.setDynamicProperty("cellspacing", "0");
		logMessageTable.setDynamicProperty("width", "100%");

		infoResultContent.appendChild(logMessageTable);

		boolean datePresents = false;
		boolean numberPresents = false;
		boolean msgPresents = false;

		for (ProcessInfoLog log : m_logs) {
			if (log.getP_Date() != null)
				datePresents = true;
			if (log.getP_Number() != null)
				numberPresents = true;
			if (log.getP_Msg() != null)
				msgPresents = true;
		}

		for (int i = 0; i < m_logs.length; i++) {

			Tr tr = new Tr();
			logMessageTable.appendChild(tr);

			ProcessInfoLog log = m_logs[i];

			if (datePresents) {
				Td td = new Td();
				if (log.getP_Date() != null) {
					Label label = new Label(dateFormat.format(log.getP_Date()));
					td.appendChild(label);
					// label.setStyle("padding-right:100px");
				}
				tr.appendChild(td);

			}

			if (numberPresents) {

				Td td = new Td();
				if (log.getP_Number() != null) {
					Label labelPno = new Label("" + log.getP_Number());
					td.appendChild(labelPno);
				}
				tr.appendChild(td);
			}

			if (msgPresents) {
				Td td = new Td();
				if (log.getP_Msg() != null) {
					if (log.getAD_Table_ID() > 0 && log.getRecord_ID() > 0) {
						DocumentLink recordLink = new DocumentLink(log.getP_Msg(), log.getAD_Table_ID(), log.getRecord_ID());																								
						td.appendChild(recordLink);
					} else {
						Text t = new Text();
						t.setEncode(false);
						t.setValue(log.getP_Msg());
						td.appendChild(t);
					}
				}
				tr.appendChild(td);
			}
		}
		//messageDiv.appendChild(logMessageTable);
	}

	private void restart() {
		replaceComponent (topParameterLayout, resultPanelLayout);
		
		isParameterPage = true;

		bOK.setLabel(Msg.getMsg(Env.getCtx(), "Start"));
		
		bCancel.setLabel(Util.cleanAmp(Msg.translate(Env.getCtx(), ConfirmPanel.A_CANCEL)));

    	if (ThemeManager.isUseFontIconForImage()) {
    		String iconSclass = "z-icon-Ok";
    		bOK.setIconSclass(iconSclass);
    		LayoutUtils.addSclass("font-icon-toolbar-button", bOK);
    	} else {
			bOK.setImage(ThemeManager.getThemeResource("images/Ok16.png"));
			bCancel.setImage(ThemeManager.getThemeResource("images/Cancel16.png"));
		}

		//recreate process info
		ProcessInfo m_pi = new WProcessInfo(getName(), getAD_Process_ID());
		m_pi.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
		m_pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
		setProcessInfo(m_pi);
		getParameterPanel().setProcessInfo(m_pi);

		//m_ids = null;
		//if (fSavedName != null)
			//querySaved();
		invalidate();
	}

	
}	//	ProcessDialog
