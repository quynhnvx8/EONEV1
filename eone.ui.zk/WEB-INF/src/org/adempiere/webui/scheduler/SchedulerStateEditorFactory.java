
package org.adempiere.webui.scheduler;

import org.adempiere.webui.editor.IEditorConfiguration;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.factory.IEditorFactory;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.osgi.service.component.annotations.Component;

/**
 * @author hengsin
 *
 */
@Component(name = "org.adempiere.webui.scheduler.SchedulerStateEditorFactory", service = {IEditorFactory.class}, 
		   property = {"service.ranking:Integer=1"})
public class SchedulerStateEditorFactory implements IEditorFactory {

	private final static int SCHEDULER_STATE_AD_REFERENCE_ID = 200173;
	
	/**
	 * default constructor 
	 */
	public SchedulerStateEditorFactory() {
	}

	@Override
	public WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor) {
		return getEditor(gridTab, gridField, tableEditor, null);
	}

	@Override
	public WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor,
			IEditorConfiguration editorConfiguration) {
		if (gridField != null && gridField.getDisplayType() == SCHEDULER_STATE_AD_REFERENCE_ID) {
			return new SchedulerStateEditor(gridField, tableEditor, editorConfiguration);
		}
		return null;
	}

}
