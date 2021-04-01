package org.adempiere.webui.editor.grid.selection;

import org.adempiere.webui.ValuePreference;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.editor.IEditorConfiguration;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WEditorPopupMenu;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.window.WFieldRecordInfo;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;

import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.base.model.GridTabVO;
import eone.base.model.GridWindow;

/**
 * 
 * @author hengsin
 *
 */
public class WGridTabSingleSelectionEditor extends WEditor implements ContextMenuListener
{
	private static final String[] LISTENER_EVENTS = {Events.ON_SELECT};

    private Object oldValue;

	private GridTab listViewGridTab = null;
	
	private String currentLinkValue = null;

	private boolean readWrite;

	/**
	 * 
	 * @param gridField
	 */
    public WGridTabSingleSelectionEditor(GridField gridField) {
    	this(gridField, false);
    }

    /**
     * 
     * @param gridField
     * @param tableEditor
     */
    public WGridTabSingleSelectionEditor(GridField gridField, boolean tableEditor) {
    	this(gridField, tableEditor, null);
    }
    
    /**
     * 
     * @param gridField
     * @param tableEditor
     * @param editorConfiguration
     */
    public WGridTabSingleSelectionEditor(GridField gridField, boolean tableEditor, IEditorConfiguration editorConfiguration)
    {
        super(tableEditor ? new Textbox() : new GridTabSelectionListView(false, gridField.getWindowNo()), gridField, tableEditor, editorConfiguration);
        init();
    }

    @Override
    public HtmlBasedComponent getComponent() {
    	return (HtmlBasedComponent) component;
    }

    @Override
	public boolean isReadWrite() {
		return readWrite;
	}

	@Override
	public void setReadWrite(boolean readWrite) {
		if (getComponent() instanceof GridTabSelectionListView) {
    		GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
    		listView.getListbox().setEnabled(readWrite);
    	}
		this.readWrite = readWrite; 
	}

	private void init()
    {
		if (tableEditor)
		{
			((Textbox)getComponent()).setReadonly(true);
		}
		else if (gridField != null && gridField.getGridTab() != null)
		{
			int AD_Tab_ID = gridField.getIncluded_Tab_ID();
			GridWindow gridWindow = gridField.getGridTab().getGridWindow();
			int count = gridWindow.getTabCount();
			GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
			for(int i = 0; i < count; i++)
			{
				GridTab t = gridWindow.getTab(i);
				if (t.getAD_Tab_ID() == AD_Tab_ID)
				{
					GridTabVO vo = t.getVO();
					listViewGridTab = new GridTab(vo, gridWindow);
					String lcn = t.getLinkColumnName();
					if (Util.isEmpty(lcn)) {
						t.setLinkColumnName(null);
						lcn = t.getLinkColumnName();
					}
					listViewGridTab.setLinkColumnName(lcn);
					listView.init(listViewGridTab);
					break;
				}
			}
			
			popupMenu = new WEditorPopupMenu(false, false, isShowPreference());
			Menuitem clear = new Menuitem(Msg.getMsg(Env.getCtx(), "ClearSelection"), null);
			clear.setAttribute("EVENT", "onClearSelection");
			clear.addEventListener(Events.ON_CLICK, popupMenu);
			popupMenu.appendChild(clear);
			
			listView.getListbox().setContext(popupMenu);
		}
    }

	public void onEvent(Event event)
    {
    	if (Events.ON_SELECT.equals(event.getName()))
    	{
    		GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
    		int selected = listView.getListbox().getSelectedIndex();
	        Object newValue = selected >= 0 ? Integer.toString(listViewGridTab.getKeyID(selected)) : null;
	        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
	    	    return;
	    	}
	        if (oldValue == null && newValue == null) {
	        	return;
	        }
	        ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), oldValue, newValue);
	        super.fireValueChange(changeEvent);
	        oldValue = newValue;
    	}
    }

    @Override
    public String getDisplay()
    {
        return oldValue != null ? oldValue.toString() : "";
    }

    @Override
    public Object getValue()
    {
        return oldValue;
    }

    @Override
    public void setValue(Object value)
    {
    	if (value == null && oldValue == null)
    	{
    		return;
    	}
    	else if (value != null && oldValue != null && value.equals(oldValue))
    	{
    		return;
    	}
    	oldValue = value;
    	if (!tableEditor)
    		updateSlectedIndices();
    	else
    		((Textbox)getComponent()).setValue(oldValue != null ? oldValue.toString() : "");
    }

	private void updateSlectedIndices() {
		GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
		listView.clearSelection();
		if (!Util.isEmpty((String)oldValue))
        {
        	int id = Integer.parseInt((String) oldValue);
        	for(int i = 0; i < listViewGridTab.getRowCount(); i++) {
    			if (listViewGridTab.getKeyID(i) == id) {
    				listView.setSelectedIndex(i);
    				return;
    			}
    		}        	
        }
    }

    @Override
    public String[] getEvents()
    {
        return LISTENER_EVENTS;
    }

    public void onMenu(ContextMenuEvent evt)
	{
		if (WEditorPopupMenu.PREFERENCE_EVENT.equals(evt.getContextEvent()))
		{
			if (isShowPreference())
				ValuePreference.start (getComponent(), this.getGridField(), getValue());
			return;
		}
		else if (WEditorPopupMenu.CHANGE_LOG_EVENT.equals(evt.getContextEvent()))
		{
			WFieldRecordInfo.start(gridField);
		}
		else if ("onClearSelection".equals(evt.getContextEvent()))
		{
			ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), oldValue, null);
	        super.fireValueChange(changeEvent);
	        oldValue = null;
		}
	}

	@Override
	public void dynamicDisplay() {
		if (!tableEditor && listViewGridTab != null) {
			String name = listViewGridTab.getLinkColumnName();
			String linkValue = Env.getContext(Env.getCtx(), gridField.getWindowNo(), name);
			if ((currentLinkValue == null && linkValue != null)
				|| (currentLinkValue != null && linkValue == null)
				|| (currentLinkValue != null && linkValue != null && !currentLinkValue.equals(linkValue)))
			{
				GridTabSelectionListView listView = (GridTabSelectionListView) getComponent();
				listView.refresh(listViewGridTab);
				updateSlectedIndices();
			}
		}
	}
}