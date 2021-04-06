package eone.webui.factory;

import org.compiere.util.DisplayType;

import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.webui.editor.IEditorConfiguration;
import eone.webui.editor.WAssignmentEditor;
import eone.webui.editor.WBinaryEditor;
import eone.webui.editor.WButtonEditor;
import eone.webui.editor.WChartEditor;
import eone.webui.editor.WChosenboxListEditor;
import eone.webui.editor.WChosenboxSearchEditor;
import eone.webui.editor.WDashboardContentEditor;
import eone.webui.editor.WDateEditor;
import eone.webui.editor.WDatetimeEditor;
import eone.webui.editor.WEditor;
import eone.webui.editor.WFileDirectoryEditor;
import eone.webui.editor.WFilenameEditor;
import eone.webui.editor.WGridTabMultiSelectionEditor;
import eone.webui.editor.WGridTabSingleSelectionEditor;
import eone.webui.editor.WHtmlEditor;
import eone.webui.editor.WImageEditor;
import eone.webui.editor.WNumberEditor;
import eone.webui.editor.WPAttributeEditor;
import eone.webui.editor.WPasswordEditor;
import eone.webui.editor.WPaymentEditor;
import eone.webui.editor.WRadioGroupEditor;
import eone.webui.editor.WSearchEditor;
import eone.webui.editor.WStringEditor;
import eone.webui.editor.WTableDirEditor;
import eone.webui.editor.WTimeEditor;
import eone.webui.editor.WUnknownEditor;
import eone.webui.editor.WUrlEditor;
import eone.webui.editor.WYesNoEditor;

/**
 *
 * @author hengsin
 *
 */
public class DefaultEditorFactory implements IEditorFactory {

	@Override
	public WEditor getEditor(GridTab gridTab, GridField gridField,
			boolean tableEditor) {
		return getEditor(gridTab, gridField, tableEditor, null);
	}
	
	@Override
	public WEditor getEditor(GridTab gridTab, GridField gridField,
			boolean tableEditor, IEditorConfiguration editorConfiguration) {
		if (gridField == null)
        {
            return null;
        }

        WEditor editor = null;
        int displayType = gridField.getDisplayType();

        /** Not a Field */
        if (gridField.isHeading())
        {
            return null;
        }

        /** String (clear/password) */
        if (displayType == DisplayType.String
            || displayType == DisplayType.PrinterName || displayType == DisplayType.Color
            || (tableEditor && (displayType == DisplayType.Text || displayType == DisplayType.TextLong)))
        {
            if (gridField.isEncryptedField())
            {
                editor = new WPasswordEditor(gridField, tableEditor, editorConfiguration);
            }
            else
            {
            	if (gridField.isHtml())
            		editor = new WHtmlEditor(gridField, tableEditor, editorConfiguration);
            	else
            		editor = new WStringEditor(gridField, tableEditor, editorConfiguration);
            }
            //enable html5 color input type
            if (displayType == DisplayType.Color)
            	((WStringEditor)editor).getComponent().setClientAttribute("type", "color");
        }
        /** File */
        else if (displayType == DisplayType.FileName)
        {
        	editor = new WFilenameEditor(gridField, tableEditor, editorConfiguration);
        }
        /** File Path */
        else if (displayType == DisplayType.FilePath)
        {
        	editor = new WFileDirectoryEditor(gridField, tableEditor, editorConfiguration);
        }
        /** Number */
        else if (DisplayType.isNumeric(displayType))
        {
            editor = new WNumberEditor(gridField, tableEditor, editorConfiguration);
        }

        /** YesNo */
        else if (displayType == DisplayType.YesNo)
        {
            editor = new WYesNoEditor(gridField, tableEditor, editorConfiguration);
            if (tableEditor)
            	((WYesNoEditor)editor).getComponent().setLabel("");
        }

        /** Text */
        else if (displayType == DisplayType.Text || displayType == DisplayType.Memo || displayType == DisplayType.TextLong || displayType == DisplayType.ID)
        {
        	if (gridField.isHtml())
        		editor = new WHtmlEditor(gridField, tableEditor, editorConfiguration);
        	else
        		editor = new WStringEditor(gridField, tableEditor, editorConfiguration);
        }

        /** Date */
        else if (DisplayType.isDate(displayType))
        {
        	if (displayType == DisplayType.Time)
        		editor = new WTimeEditor(gridField, tableEditor, editorConfiguration);
        	else if (displayType == DisplayType.DateTime)
        		editor = new WDatetimeEditor(gridField, tableEditor, editorConfiguration);
        	else
        		editor = new WDateEditor(gridField, tableEditor, editorConfiguration);
        }
        
        /** Chart */
        else if(displayType == DisplayType.Chart)
        {
        	editor = new WChartEditor(gridField, (gridTab == null ? 0 : gridTab.getWindowNo()), tableEditor, editorConfiguration);
        }

        /** Dashboard Content */
        else if(displayType == DisplayType.DashboardContent)
        {
        	editor = new WDashboardContentEditor(gridField, (gridTab == null ? 0 : gridTab.getWindowNo()), tableEditor, editorConfiguration);
        }
        
        /**  Button */
        else if (displayType == DisplayType.Button)
        {
            editor = new WButtonEditor(gridField, tableEditor, editorConfiguration);
        }

        /** Table Direct */
        else if (displayType == DisplayType.TableDir ||
                displayType == DisplayType.Table || displayType == DisplayType.List)
        {
            editor = new WTableDirEditor(gridField, tableEditor, editorConfiguration);
        }
        
        else if (displayType == DisplayType.Payment)
        {
        	editor = new WPaymentEditor(gridField, tableEditor, editorConfiguration);
        }

        else if (displayType == DisplayType.URL)
        {
        	editor = new WUrlEditor(gridField, tableEditor, editorConfiguration);
        }

        else if (displayType == DisplayType.Search)
        {
        	editor = new WSearchEditor(gridField, tableEditor, editorConfiguration);
        }
        /*
        else if (displayType == DisplayType.Location)
        {
            editor = new WLocationEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.Locator)
        {
        	editor = new WLocatorEditor(gridField, tableEditor, editorConfiguration);
        }
        
        else if (displayType == DisplayType.Account)
        {
        	editor = new WAccountEditor(gridField, tableEditor, editorConfiguration);
        }
        */
        else if (displayType == DisplayType.Image)
        {
        	editor = new WImageEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.Binary)
        {
        	editor = new WBinaryEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.PAttribute)
        {
        	editor = new WPAttributeEditor(gridTab, gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.Assignment)
        {
        	editor = new WAssignmentEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.SingleSelectionGrid)
        {
        	editor = new WGridTabSingleSelectionEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.MultipleSelectionGrid)
        {
        	editor = new WGridTabMultiSelectionEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.ChosenMultipleSelectionList || displayType == DisplayType.ChosenMultipleSelectionTable)
        {
        	editor = new WChosenboxListEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.ChosenMultipleSelectionSearch)
        {
        	editor = new WChosenboxSearchEditor(gridField, tableEditor, editorConfiguration);
        }
        else if (displayType == DisplayType.RadiogroupList)
        {
        	editor = new WRadioGroupEditor(gridField, tableEditor, editorConfiguration);
        }
        else
        {
            editor = new WUnknownEditor(gridField, tableEditor, editorConfiguration);
        }

        return editor;
	}

}
