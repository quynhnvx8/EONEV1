package eone.webui.adwindow;

import org.compiere.util.Callback;

public interface WindowValidator {
	public void onWindowEvent(WindowValidatorEvent event, Callback<Boolean> callback);
}
