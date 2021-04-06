package eone.webui.apps;

import java.util.logging.Level;

import org.compiere.apps.AbstractProcessCtl;
import org.compiere.apps.IProcessParameter;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.IProcessUI;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import eone.base.process.ProcessInfo;
import eone.webui.ISupportMask;
import eone.webui.LayoutUtils;
import eone.webui.component.Mask;
import eone.webui.component.Window;
import eone.webui.event.DialogEvents;
import eone.webui.session.SessionManager;


public class WProcessCtl extends AbstractProcessCtl {
	
	/**	Logger			*/
	private static final CLogger log = CLogger.getCLogger(WProcessCtl.class);
	
	public static void process (int WindowNo, ProcessInfo pi, Trx trx)
	{
		process(WindowNo, pi, trx, null);
	}
	

	public static void process (int WindowNo, ProcessInfo pi, Trx trx, EventListener<Event> listener)
	{
		if (log.isLoggable(Level.FINE)) log.fine("WindowNo=" + WindowNo + " - " + pi);
		
		ProcessModalDialog para = new ProcessModalDialog(listener, WindowNo, pi, false);
		if (para.isValid())
		{
			//para.setWidth("500px");
			para.setVisible(true);

			Object window = SessionManager.getAppDesktop().findWindow(WindowNo);
			if (window != null && window instanceof Component && window instanceof ISupportMask){
				final ISupportMask parent = LayoutUtils.showWindowWithMask(para, (Component)window, LayoutUtils.OVERLAP_PARENT);
				para.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						parent.hideMask();
					}
				});
			}else if (window != null && window instanceof Component){
				final Mask mask = LayoutUtils.showWindowWithMask(para, (Component)window, null);
				para.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						mask.hideMask();
					}
				});
			}else{
				para.setPosition("center");
				para.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
				AEnv.showWindow(para);
			}
			
		}
	}	//	execute
	
	
	public static void process(IProcessUI aProcessUI, int WindowNo, IProcessParameter parameter, ProcessInfo pi, Trx trx)
	{
	  if (log.isLoggable(Level.FINE)) log.fine("WindowNo=" + WindowNo + " - " + pi);
	  
		if (parameter != null) {
			if (!parameter.saveParameters())
			{
				pi.setSummary (Msg.getMsg(Env.getCtx(), "ProcessCancelled"));
				pi.setError (true);
				return;
			}
		}
		
		WProcessCtl worker = new WProcessCtl(aProcessUI, WindowNo, pi, trx);
		worker.run();
	}

	/**
	 * @param parent
	 * @param WindowNo
	 * @param pi
	 * @param trx
	 */
	public WProcessCtl(IProcessUI aProcessUI, int WindowNo, ProcessInfo pi,	Trx trx) {
		super(aProcessUI, WindowNo, pi, trx);
	}
	/*
	@Override
	protected void updateProgressWindowTimerEstimate(int estSeconds) {
	}

	@Override
	protected void updateProgressWindowTitle(String title) {
	}

	*/
}
