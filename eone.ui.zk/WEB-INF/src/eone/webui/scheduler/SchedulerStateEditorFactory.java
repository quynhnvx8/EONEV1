
package eone.webui.scheduler;

import org.osgi.service.component.annotations.Component;

import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.webui.editor.IEditorConfiguration;
import eone.webui.editor.WEditor;
import eone.webui.factory.IEditorFactory;

/**
 * @author hengsin
 *
 */
@Component(name = "eone.webui.scheduler.SchedulerStateEditorFactory", service = {IEditorFactory.class}, 
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
