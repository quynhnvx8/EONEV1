
package eone.webui.component;

import java.util.List;

import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.util.KeyNamePair;
import org.zkoss.zul.Listcell;

import eone.base.model.GridField;
import eone.base.model.MInfoColumn;
import eone.webui.editor.WEditor;
import eone.webui.editor.WebEditorFactory;
import eone.webui.event.ValueChangeEvent;
import eone.webui.event.ValueChangeListener;
import eone.webui.info.InfoWindow;

public class WInfoWindowListItemRenderer extends WListItemRenderer
{
	private MInfoColumn[]	gridDisplayedInfoColumns = null;
	private ColumnInfo[]	gridDisplayedColumnInfos = null;
	private InfoWindow infoWindow = null;

	public WInfoWindowListItemRenderer(InfoWindow infoWindow)
	{
		this.infoWindow = infoWindow;
	}

	public WInfoWindowListItemRenderer(InfoWindow infoWindow, List<? extends String> columnNames)
	{
		super(columnNames);
		this.infoWindow = infoWindow;
	}
	
	public void setGridDisplaydInfoColumns(MInfoColumn[] infoColumns, ColumnInfo[] columnInfos)
	{
		this.gridDisplayedInfoColumns = infoColumns;
		this.gridDisplayedColumnInfos = columnInfos;
	}		
		
	@Override
	protected Listcell getCellComponent(WListbox table, Object field,
			final int rowIndex, final int columnIndex)
	{
		if(gridDisplayedInfoColumns == null || gridDisplayedColumnInfos == null)
		{
			return super.getCellComponent(table, field, rowIndex, columnIndex);
		}
				
		Listcell listcell = null;
		ListModelTable model = table.getModel();
		Object obj = model.get(rowIndex);
		
		MInfoColumn infoColumn = gridDisplayedInfoColumns[columnIndex];
		if (infoColumn != null)
		{
		
			final GridField gridField = gridDisplayedColumnInfos[columnIndex].getGridField();
			final WEditor editor = WebEditorFactory.getEditor(gridField, false);

			if(model.isSelected(obj)) // First index may be null
		{
			if(infoColumn.isReadOnly() == false 
					&& columnIndex > 0)
			{
				ListCell listCell = new ListCell();

				
				// Set editor value
				
				Object value = table.getValueAt(rowIndex, columnIndex);
				
				if(value instanceof IDColumn)
				{
					IDColumn idc = (IDColumn)value;
					value = idc.getRecord_ID();
				}
				else if(value instanceof KeyNamePair)
				{
					KeyNamePair knp = (KeyNamePair)value;
					value = knp.getKey();
				}
				
				editor.setValue(value);
				
				editor.addValueChangeListener(new ValueChangeListener()
				{					
					@Override
					public void valueChange(ValueChangeEvent evt)
					{
						infoWindow.onCellEditCallback(evt, rowIndex, columnIndex, editor, gridField);
					}
				});
				
				listCell.appendChild(editor.getComponent());
				listcell = listCell;
			}
		}
		
		if(listcell == null)
			listcell = super.getCellComponent(table, field, rowIndex, columnIndex);
		
		}

		if(listcell == null)
			listcell = super.getCellComponent(table, field, rowIndex, columnIndex);

		return listcell;
	}

}
