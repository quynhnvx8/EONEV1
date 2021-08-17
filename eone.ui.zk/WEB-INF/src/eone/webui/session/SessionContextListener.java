
package eone.webui.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.compiere.util.Env;
import org.compiere.util.ServerContext;
import org.compiere.util.ServerContextURLHandler;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadInit;
import org.zkoss.zk.ui.sys.DesktopCache;
import org.zkoss.zk.ui.sys.DesktopCtrl;
import org.zkoss.zk.ui.sys.ServerPush;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.DesktopInit;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;

import eone.webui.EONEWebUI;

/**
 *
 * @author Admin
 */
public class SessionContextListener implements ExecutionInit,
        ExecutionCleanup, EventThreadInit, DesktopCleanup, DesktopInit
{
	public static final String SERVLET_SESSION_ID = "servlet.sessionId";
    public static final String SESSION_CTX = "WebUISessionContext";

    public static void setupExecutionContextFromSession(Execution exec) {
    	Session session = exec.getDesktop().getSession();
		Properties ctx = null;
		//catch potential Session already invalidated exception
		boolean sessionInvalidated = false;
		try {
			ctx = (Properties)session.getAttribute(SESSION_CTX);
		} catch (IllegalStateException e) {
			sessionInvalidated = true;
		}
		HttpSession httpSession = sessionInvalidated ? null : (HttpSession)session.getNativeSession();
		//create empty context if there's no valid native session
		if (httpSession == null)
		{
			ctx = new Properties();
			ctx.put(ServerContextURLHandler.SERVER_CONTEXT_URL_HANDLER, new ServerContextURLHandler() {
				public void showURL(String url) {
					SessionManager.getAppDesktop().showURL(url, true);
				}
			});
			ServerContext.setCurrentInstance(ctx);
			return;
		}
		
		if (ctx != null)
		{
			//verify ctx
			String cacheId = ctx.getProperty(SERVLET_SESSION_ID);
			if (cacheId == null || !cacheId.equals(httpSession.getId()) )
			{
				ctx = null;
				session.removeAttribute(SESSION_CTX);
			}
		}
		if (ctx == null)
		{
			ctx = new Properties();
			ctx.put(ServerContextURLHandler.SERVER_CONTEXT_URL_HANDLER, new ServerContextURLHandler() {
				public void showURL(String url) {
					SessionManager.getAppDesktop().showURL(url, true);
				}
			});
			ctx.setProperty(SERVLET_SESSION_ID, httpSession.getId());
		    session.setAttribute(SESSION_CTX, ctx);
		}
		ServerContext.setCurrentInstance(ctx);
	}

    /**
     * @param exec
     * @param parent
     *
     * @see ExecutionInit#init(Execution, Execution)
     */
    @Override
    public void init(Execution exec, Execution parent)
    {
    	//in servlet thread
    	if (parent == null)
    	{
    		ServerPush serverPush = ((DesktopCtrl)exec.getDesktop()).getServerPush();
    		if (serverPush == null || !serverPush.isActive())
    		{
		    	setupExecutionContextFromSession(Executions.getCurrent());
		    	//set locale
		        Locales.setThreadLocal(Env.getLanguage(ServerContext.getCurrentInstance()).getLocale());
	    	}
	    }
    }

    /**
     * @param exec
     * @param parent
     * @param errs
     * @see ExecutionCleanup#cleanup(Execution, Execution, List)
     */
    @Override
    public void cleanup(Execution exec, Execution parent, List<Throwable> errs)
    {
    	//in servlet thread
        if (parent == null)
        {
        	ServerPush serverPush = ((DesktopCtrl)exec.getDesktop()).getServerPush();
    		if (serverPush == null || !serverPush.isActive())
    		{
	            ServerContext.dispose();
	        }
	    }
    }

    /**
     * get from servlet thread's ThreadLocal
     * @param comp
     * @param evt
     * @see EventThreadInit#prepare(Component, Event)
     */
    @Override
    public void prepare(Component comp, Event evt)
    {
    	//in servlet thread
    	//check is thread local context have been setup
    	if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
    		setupExecutionContextFromSession(Executions.getCurrent());	
    	}
    	
    	//set locale
        Locales.setThreadLocal(Env.getLanguage(ServerContext.getCurrentInstance()).getLocale());
    }

    /**
     * @param comp
     * @param evt
     * @see EventThreadInit#init(Component, Event)
     */
    @Override
    public boolean init(Component comp, Event evt)
    {
		return true;
    }

	public static boolean isContextValid() {
		Execution exec = Executions.getCurrent();
		Properties ctx = ServerContext.getCurrentInstance();
		if (ctx == null)
			return false;
		
		if (exec == null || exec.getDesktop() == null)
			return false;
		
		Session session = exec.getDesktop().getSession();
		HttpSession httpSession = (HttpSession)session.getNativeSession();
		//verify ctx
		String cacheId = ctx.getProperty(SERVLET_SESSION_ID);
		if (cacheId == null || httpSession == null || !cacheId.equals(httpSession.getId()) )
		{
			return false;
		}
		
		Properties sessionCtx = null;
		//catch invalidated session exception
		try {
			sessionCtx = (Properties) session.getAttribute(SESSION_CTX);
		} catch (IllegalStateException e) {
			return false;
		}
		if (sessionCtx != null) 
		{
			if (Env.getAD_Client_ID(sessionCtx) != Env.getAD_Client_ID(ctx))
			{
				return false;
			}
			if (Env.getAD_User_ID(sessionCtx) != Env.getAD_User_ID(ctx))
			{
				return false;
			}			
			if (Env.getAD_Role_ID(sessionCtx) != Env.getAD_Role_ID(ctx))
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void cleanup(Desktop desktop) throws Exception {
		if(Executions.getCurrent()==null) {
			if (!ServerContext.getCurrentInstance().isEmpty()) {
				ServerContext.dispose();
			}
			return;
		}
		
		Object sessionInvalidated = desktop.getAttribute(EONEWebUI.DESKTOP_SESSION_INVALIDATED_ATTR);
		if (sessionInvalidated != null) {
			return;
		}
		
		if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
			setupExecutionContextFromSession(Executions.getCurrent());
			if (Env.getCtx().getProperty(SERVLET_SESSION_ID) == null)
				return;
    	}
		String AD_Session_ID = Env.getContext(Env.getCtx(), "#AD_Session_ID");
		if (AD_Session_ID != null) {
			String key = getSessionDesktopListKey(AD_Session_ID);
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) Env.getCtx().get(key);
			if (list != null) {
				list.remove(desktop.getId());
				if (!isEmpty(list, desktop.getSession())) {
					return;
				} else {
					Env.getCtx().remove(key);
				}
			}
			SessionManager.logoutSessionAfterBrowserDestroyed();
		}
	}

	private boolean isEmpty(List<String> list, Session session) {
		if (list.isEmpty())
			return true;
		
		if (session == null)
			return false;
		
		DesktopCache desktopCache = ((SessionCtrl)session).getDesktopCache();
		if (desktopCache == null)
			return false;
		
		int count = 0;
		for(String dtid : list) {
			Desktop desktop = desktopCache.getDesktopIfAny(dtid);
			if (desktop == null) continue;
			if (desktop.getFirstPage() == null) continue;
			Collection<Component> roots = desktop.getFirstPage().getRoots();
			if (roots == null || roots.isEmpty()) continue;
			boolean found = false;
			for (Component root : roots) {
				if (root instanceof EONEWebUI) {
					found = true;
					break;
				}
			}
			if (!found) continue;
			count++;
		}
		
		return count == 0;
	}

	@Override
	public void init(Desktop desktop, Object request) throws Exception {
		if(Executions.getCurrent()==null)
			return;
		
		desktop.addListener(new ValidateReadonlyComponent());
		
		if (ServerContext.getCurrentInstance().isEmpty() || !isContextValid())
    	{
			setupExecutionContextFromSession(Executions.getCurrent());
    	}
		String AD_Session_ID = Env.getContext(Env.getCtx(), "#AD_Session_ID");
		addDesktopId(AD_Session_ID, desktop.getId());
	}
	
	public static synchronized void addDesktopId(String AD_Session_ID, String dtid)
	{
		String key = getSessionDesktopListKey(AD_Session_ID);
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) Env.getCtx().get(key);
		if (list == null) {
			list = new ArrayList<String>();
			Env.getCtx().put(key, list);
		}
		if (!list.contains(dtid))
		{
			list.add(dtid);
		}
	}
	
	/**
	 * @param AD_Session_ID
	 * @return desktop list key
	 */
	public static String getSessionDesktopListKey(String AD_Session_ID)
	{
		String key = "ad_session."+AD_Session_ID+".desktop";
		return key;
	}
}
