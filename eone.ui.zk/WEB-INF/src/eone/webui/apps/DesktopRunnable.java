/**
 * 
 */
package eone.webui.apps;

import java.lang.ref.WeakReference;

import org.zkoss.zk.ui.Desktop;

/**
 * If your background task need access to desktop, wrap your runnable with this, i.e new DesktopRunnable(yourRunnable, desktop).
 * You can then use AEnv.getDesktop() in your runnable to get access to desktop.
 * @author hengsin
 *
 */
public class DesktopRunnable implements Runnable {

	private Runnable runnable;
	private WeakReference<Desktop> desktopWeakRef;
	
	private static ThreadLocal<WeakReference<Desktop>> threadLocalDesktop = new ThreadLocal<WeakReference<Desktop>>() {
        protected WeakReference<Desktop> initialValue()
        {
        	return null;
        }
    };
	
	public DesktopRunnable(Runnable runnable, Desktop desktop) {
		this.runnable = runnable;
		this.desktopWeakRef = new WeakReference<Desktop>(desktop);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			threadLocalDesktop.set(desktopWeakRef);
			runnable.run();
		} finally {
			threadLocalDesktop.remove();
		}
	}

	/* package */static WeakReference<Desktop> getThreadLocalDesktop() {
		return threadLocalDesktop.get();
	}
}
