
package org.adempiere.webui.factory;

import org.adempiere.webui.editor.IEditorConfiguration;
import org.adempiere.webui.editor.WEditor;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public interface IEditorFactory {

	public WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor);
	
	public WEditor getEditor(GridTab gridTab, GridField gridField, boolean tableEditor, IEditorConfiguration editorConfiguration);
}
