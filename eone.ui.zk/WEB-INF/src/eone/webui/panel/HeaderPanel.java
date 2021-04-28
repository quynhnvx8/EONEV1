package eone.webui.panel;

import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Image;
import org.zkoss.zul.Popup;

import eone.webui.ClientInfo;
import eone.webui.LayoutUtils;
import eone.webui.apps.GlobalSearch;
import eone.webui.apps.MenuSearchController;
import eone.webui.component.Panel;
import eone.webui.session.SessionManager;
import eone.webui.theme.ITheme;
import eone.webui.theme.ThemeManager;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.AboutWindow;

/**
 *
 * @author  Admin
 */

public class HeaderPanel extends Panel implements EventListener<Event>
{
	private static final long serialVersionUID = -2351317624519209484L;

	protected Image image;
	protected Image btnMenu;
	protected Popup popMenu;
	private static final String IMAGES_THREELINE_MENU_PNG = "images/threelines.png";

	private MenuTreePanel menuTreePanel;

    public HeaderPanel()
    {
        super();
        addEventListener(Events.ON_CREATE, this);              
    }

    protected void onCreate()
    {
    	image = (Image) getFellow("logo");
    	image.setSrc(ITheme.LOGO_SMALL);
    	image.addEventListener(Events.ON_CLICK, this);
    	image.setStyle("cursor: pointer;");
    	
    	if( ClientInfo.isMobile())
    	{
    		createPopupMenu();
    		createSearchPanel(menuTreePanel);
    		
	    	btnMenu = (Image) getFellow("menuButton");
	    	btnMenu.setSrc(ThemeManager.getThemeResource(IMAGES_THREELINE_MENU_PNG));
	    	btnMenu.setTooltiptext(Util.cleanAmp(Msg.getMsg(Env.getCtx(),"Menu")));
	    	btnMenu.addEventListener(Events.ON_CLICK, this);
    		LayoutUtils.addSclass("mobile", this);
    		ClientInfo.onClientInfo(this, this::onClientInfo);
    	}
    	SessionManager.getSessionApplication().getKeylistener().addEventListener(Events.ON_CTRL_KEY, this);
    }

	protected void createPopupMenu() {
		popMenu = new Popup();
    	popMenu.setId("menuTreePopup");
		menuTreePanel = new MenuTreePanel(popMenu);
    	popMenu.setSclass("desktop-menu-popup");
    	ZKUpdateUtil.setHeight(popMenu, "90%");
    	ZKUpdateUtil.setWindowWidthX(popMenu, 600);
    	popMenu.setPage(this.getPage());
    	popMenu.addEventListener(Events.ON_OPEN, (OpenEvent evt) -> popMenuOpenEvent(evt));
	}

	private void popMenuOpenEvent(OpenEvent evt) {
		popMenu.setAttribute(popMenu.getUuid(), System.currentTimeMillis());
	}

	public void createSearchPanel(MenuTreePanel menuTreePanel) {
		GlobalSearch globalSearch = new GlobalSearch(new MenuSearchController(menuTreePanel.getMenuTree()));
		Component stub = getFellow("menuLookup");
    	stub.getParent().insertBefore(globalSearch, stub);
    	stub.detach();
    	globalSearch.setId("menuLookupL");
	}
	
	public void onEvent(Event event) throws Exception {
		if (Events.ON_CLICK.equals(event.getName())) {
			if(event.getTarget() == image)
			{
				AboutWindow w = new AboutWindow();
				w.setPage(this.getPage());
				w.doHighlighted();
			}
			else if(event.getTarget() == btnMenu )
			{
				Long ts = (Long) popMenu.removeAttribute(popMenu.getUuid());
				if (ts != null) {
					if ((System.currentTimeMillis()-ts.longValue()) < 500)
						return;
				}
				popMenu.open(btnMenu, "after_start");
			}
		} else if (Events.ON_CREATE.equals(event.getName())) {
			onCreate();
		}else if (event instanceof KeyEvent)
		{
			//alt+m for the menu
			KeyEvent ke = (KeyEvent) event;
			if (ke.getKeyCode() == 77)
			{
				popMenu.open(btnMenu, "after_start");
				popMenu.setFocus(true);
			}else if (ke.getKeyCode() == 27 && popMenu != null) {//Bo Sung do loi ESCAPE
				popMenu.close();
			} 
		}
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.AbstractComponent#onPageAttached(org.zkoss.zk.ui.Page, org.zkoss.zk.ui.Page)
	 */
	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		if (newpage != null && popMenu != null)
			popMenu.setPage(newpage);
	}

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.AbstractComponent#onPageDetached(org.zkoss.zk.ui.Page)
	 */
	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		if (popMenu != null)
			popMenu.setPage(null);
	}
	
	public Image getLogo() {
		return image;
	}
	
	public void closeSearchPopup() {
		Component c = getFellow("menuLookup");
		if (c != null && c instanceof GlobalSearch)
			((GlobalSearch)c).closePopup();
	}
	
	protected void onClientInfo() {
		ZKUpdateUtil.setWindowWidthX(popMenu, 600);
		Component c = getFellow("menuLookup");
		if (c != null && c instanceof GlobalSearch)
			((GlobalSearch)c).onClientInfo();
	}
}
