/******************************************************************************
 * Copyright (C) 2008 Low Heng Sin                                            *
 * Copyright (C) 2008 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package eone.webui.desktop;

import java.util.List;

import org.compiere.util.Callback;
import org.compiere.util.Env;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanels;

import eone.base.model.MInfoWindow;
import eone.base.model.MQuery;
import eone.base.model.MTask;
import eone.webui.EONEIdGenerator;
import eone.webui.adwindow.ADWindow;
import eone.webui.apps.ProcessDialog;
import eone.webui.component.DesktopTabpanel;
import eone.webui.component.Tabbox;
import eone.webui.component.Tabpanel;
import eone.webui.component.Window;
import eone.webui.component.Tab.DecorateInfo;
import eone.webui.factory.InfoManager;
import eone.webui.panel.ADForm;
import eone.webui.panel.IHelpContext;
import eone.webui.panel.InfoPanel;
import eone.webui.part.WindowContainer;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.FDialog;
import eone.webui.window.WTask;

/**
 * A Tabbed MDI implementation
 * @author hengsin
 *
 */
public abstract class TabbedDesktop extends AbstractDesktop {

	protected WindowContainer windowContainer;

	public TabbedDesktop() {
		super();
		windowContainer = new WindowContainer();
	}

	/**
     *
     * @param processId
     * @param soTrx
     * @return ProcessDialog
     */
	public ProcessDialog openProcessDialog(int processId, boolean soTrx) {
		ProcessDialog pd = new ProcessDialog (processId, soTrx);
		if (pd.isValid()) {
			DesktopTabpanel tabPanel = new DesktopTabpanel();
			pd.setParent(tabPanel);
			String title = pd.getTitle();
			pd.setTitle(null);
			preOpenNewTab();
			windowContainer.addWindow(tabPanel, title, true, null);
			Events.postEvent(ProcessDialog.ON_INITIAL_FOCUS_EVENT, pd, null);
		}
		return pd;
	}

    /**
     *
     * @param formId
     * @return ADWindow
     */
	public ADForm openForm(int formId) {
		ADForm form = ADForm.openForm(formId);

		if (Window.Mode.EMBEDDED == form.getWindowMode()) {
			DesktopTabpanel tabPanel = new DesktopTabpanel();
			form.setParent(tabPanel);
			//do not show window title when open as tab
			form.setTitle(null);
			preOpenNewTab();
			windowContainer.addWindow(tabPanel, form.getFormName(), true, null);
			form.focus();
		} else {
			form.setAttribute(Window.MODE_KEY, form.getWindowMode());
			showWindow(form);
		}

		return form;
	}

	/**
	 *
	 * @param infoId
	 */
	@Override
	public void openInfo(int infoId) {
		InfoPanel infoPanel = InfoManager.create(infoId);
		
		if (infoPanel != null) {
			DesktopTabpanel tabPanel = new DesktopTabpanel();
			infoPanel.setParent(tabPanel);
			String title = infoPanel.getTitle();
			infoPanel.setTitle(null);
			preOpenNewTab();
			windowContainer.addWindow(tabPanel, title, true, DecorateInfo.get(MInfoWindow.get(infoId, null)));
			infoPanel.focus();
		} else {
			FDialog.error(0, "NotValid");
		}
	}
	
	
	public void openWindow(int windowId, Callback<ADWindow> callback) {
		openWindow(windowId, null, callback);
	}

	/**
	 *
	 * @param windowId
     * @param query
	 * @return ADWindow
	 */
	public void openWindow(int windowId, MQuery query, Callback<ADWindow> callback) {
		final ADWindow adWindow = new ADWindow(Env.getCtx(), windowId, query);

		final DesktopTabpanel tabPanel = new DesktopTabpanel();		
		String id = EONEIdGenerator.escapeId(adWindow.getTitle());
		tabPanel.setId(id+"_"+adWindow.getADWindowContent().getWindowNo());
		final Tab tab = windowContainer.addWindow(tabPanel, adWindow.getTitle(), true, DecorateInfo.get(adWindow));
		
		tab.setClosable(false);	
		
		if (adWindow.createPart(tabPanel) != null ) {
			tab.setClosable(true);
			if (callback != null) {
				callback.onCallback(adWindow);
			}
		} else {
			tab.onClose();
		}
		
		/* TODO fixme 06/05/2021. Thu bo di xem khi mo form no con quay nua khong
		final OpenWindowRunnable runnable = new OpenWindowRunnable(adWindow, tab, tabPanel, callback);
		preOpenNewTab();
		runnable.run();
		*/
	}

	/**
     *
     * @param taskId
     */
	public void openTask(int taskId) {
		MTask task = new MTask(Env.getCtx(), taskId, null);
		new WTask(task.getName(), task);
	}

	/**
	 * @param url
	 */
	public void showURL(String url, boolean closeable)
    {
    	showURL(url, url, closeable);
    }

	/**
	 *
	 * @param url
	 * @param title
	 * @param closeable
	 */
    public void showURL(String url, String title, boolean closeable)
    {
    	Iframe iframe = new Iframe(url);
    	addWin(iframe, title, closeable);
    }

    /**
     * @param content
     * @param title
     * @param closeable
     */
    @Override
    public void showHTMLContent(String content, String title, boolean closeable)
    {
    	Iframe iframe = new Iframe();

    	AMedia media = new AMedia(title, "html", "text/html", content.getBytes());
    	iframe.setContent(media);

    	addWin(iframe, title, closeable);
    }

    /**
     *
     * @param fr
     * @param title
     * @param closeable
     */
    private void addWin(Iframe fr, String title, boolean closeable)
    {
    	ZKUpdateUtil.setWidth(fr, "100%");
        ZKUpdateUtil.setHeight(fr, "100%");
        fr.setStyle("padding: 0; margin: 0; border: none; position: absolute");
        Window window = new Window();
        ZKUpdateUtil.setWidth(window, "100%");
        ZKUpdateUtil.setHeight(window, "100%");
        window.setStyle("padding: 0; margin: 0; border: none");
        window.appendChild(fr);
        window.setStyle("position: absolute");

        Tabpanel tabPanel = new Tabpanel();
    	window.setParent(tabPanel);
    	preOpenNewTab();
    	windowContainer.addWindow(tabPanel, title, closeable, null);
    }

    /**
     * @param AD_Window_ID
     * @param query
     */
    public void showZoomWindow(int AD_Window_ID, MQuery query)
    {
    	final ADWindow wnd = new ADWindow(Env.getCtx(), AD_Window_ID, query);

    	final DesktopTabpanel tabPanel = new DesktopTabpanel();		
		final Tab tab = windowContainer.insertAfter(windowContainer.getSelectedTab(), tabPanel, wnd.getTitle(), true, true, DecorateInfo.get(wnd));
		tab.setClosable(false);		
		final OpenWindowRunnable runnable = new OpenWindowRunnable(wnd, tab, tabPanel, null);
		preOpenNewTab();
		runnable.run();
	}

    /**
     * @param AD_Window_ID
     * @param query
     * @deprecated
     */
    public void showWindow(int AD_Window_ID, MQuery query)
    {
    	openWindow(AD_Window_ID, query, null);
	}

	/**
	 *
	 * @param window
	 */
	protected void showEmbedded(Window window)
   	{
		Tabpanel tabPanel = new Tabpanel();
    	window.setParent(tabPanel);
    	Object defer = window.getAttribute(WindowContainer.DEFER_SET_SELECTED_TAB);
    	if ( defer != null)
    		tabPanel.setAttribute(WindowContainer.DEFER_SET_SELECTED_TAB, defer);
    	String title = window.getTitle();
    	window.setTitle(null);
    	preOpenNewTab();
    	if (Window.INSERT_NEXT.equals(window.getAttribute(Window.INSERT_POSITION_KEY)))
    		windowContainer.insertAfter(windowContainer.getSelectedTab(), tabPanel, title, true, true, null);
    	else
    		windowContainer.addWindow(tabPanel, title, true, null);
    	if (window instanceof IHelpContext)
			Events.sendEvent(new Event(WindowContainer.ON_WINDOW_CONTAINER_SELECTION_CHANGED_EVENT, window));
   	}

	/**
	 * Close active tab
	 * @return boolean
	 */
	public boolean closeActiveWindow()
	{
		if (windowContainer.getSelectedTab() != null)
		{
			Tabpanel panel = (Tabpanel) windowContainer.getSelectedTab().getLinkedPanel();
			Component component = panel.getFirstChild();
			Object att = component.getAttribute(WINDOWNO_ATTRIBUTE);

			if ( windowContainer.closeActiveWindow() )
			{
				if (att != null && (att instanceof Integer))
				{
					unregisterWindow((Integer) att);
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}

	/**
	 * @return Component
	 */
	public Component getActiveWindow()
	{
		return windowContainer.getSelectedTab().getLinkedPanel().getFirstChild();
	}

	/**
	 *
	 * @param windowNo
	 * @return boolean
	 */
	public boolean closeWindow(int windowNo)
	{
		Tabbox tabbox = windowContainer.getComponent();
		Tabpanels panels = tabbox.getTabpanels();
		List<?> childrens = panels.getChildren();
		for (Object child : childrens)
		{
			Tabpanel panel = (Tabpanel) child;
			Component component = panel.getFirstChild();
			Object att = component != null ? component.getAttribute(WINDOWNO_ATTRIBUTE) : null;
			if (att != null && (att instanceof Integer))
			{
				if (windowNo == (Integer)att)
				{
					Tab tab = panel.getLinkedTab();
					panel.getLinkedTab().onClose();
					if (tab.getParent() == null)
					{
						unregisterWindow(windowNo);
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * invoke before a new tab is added to the desktop
	 */
	protected void preOpenNewTab()
	{
	}
	
	class OpenWindowRunnable implements Runnable {

		private final ADWindow adWindow;
		private final Tab tab;
		private final DesktopTabpanel tabPanel;
		private Callback<ADWindow> callback;

		protected OpenWindowRunnable(ADWindow adWindow, Tab tab, DesktopTabpanel tabPanel, Callback<ADWindow> callback) {
			this.adWindow = adWindow;
			this.tab = tab;
			this.tabPanel = tabPanel;
			this.callback = callback;
		}
		
		@Override
		public void run() {			
			if (adWindow.createPart(tabPanel) != null ) {
				tab.setClosable(true);
				if (callback != null) {
					callback.onCallback(adWindow);
				}
			} else {
				tab.onClose();
			}
		}		
	}

	public void setTabTitle(String title, int windowNo) {
		windowContainer.setTabTitle(title, windowNo);		
	}

}
