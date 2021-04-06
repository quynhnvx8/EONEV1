
package eone.webui.factory;

import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.webui.editor.IEditorConfiguration;
import eone.webui.editor.WEditor;

public interface IEditorFactory {

	public WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor);
	
	public WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor, IEditorConfiguration editorConfiguration);
}
