package org.adempiere.webui.adwindow.validator;

import org.compiere.util.Callback;

public interface WindowValidator {
	public void onWindowEvent(WindowValidatorEvent event, Callback<Boolean> callback);
}
