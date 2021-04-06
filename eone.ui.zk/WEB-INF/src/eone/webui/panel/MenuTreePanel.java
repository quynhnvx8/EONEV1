

package eone.webui.panel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Toolbarbutton;

import eone.webui.component.ToolBarButton;
import eone.webui.util.TreeUtils;


public class MenuTreePanel extends AbstractMenuPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -911113870835089567L;
	private static final String ON_EXPAND_MENU_EVENT = "onExpandMenu";
	
	private ToolBarButton expandToggle;
	private MenuTreeFilterPanel filterPanel;
	private Toolbarbutton filterBtn;
    
    public MenuTreePanel(Component parent)
    {
    	super(parent);
    }

    protected void init() 
    {
		super.init();

       
     	
     	EventQueues.lookup(MenuTreeFilterPanel.MENU_TREE_FILTER_CHECKED_QUEUE, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				if (event.getName() == Events.ON_CHECK)
				{
					Checkbox chk = (Checkbox) event.getData();
					if (chk != null)
					{
						if ("flatView".equals(chk.getId()))
							MenuTreeFilterPanel.toggleFlatView(getMenuTree(), chk);
						else
							MenuTreeFilterPanel.toggle(getMenuTree(), chk);
						getMenuTree().invalidate();
					}
				}
			}
		});
     }
    
    protected void initComponents()
    {
    	super.initComponents();
    	
        Panelchildren pc = new Panelchildren();
        this.appendChild(pc);
        pc.appendChild(getMenuTree());
       
    }
    
    public void onEvent(Event event)
    {
    	super.onEvent(event);
    	
        String eventName = event.getName();
        // Elaine 2009/02/27 - expand tree
        if (eventName.equals(Events.ON_CHECK) && event.getTarget() == expandToggle)
        {
        	Clients.showBusy(null);
        	Events.echoEvent(ON_EXPAND_MENU_EVENT, this, null);        	
        }
        else if (eventName.equals(ON_EXPAND_MENU_EVENT)) 
        {
        	expandOnCheck();
        	Clients.clearBusy();
        }
        //
        else if (event.getName().equals(Events.ON_CLICK) && event.getTarget() == filterBtn)
        	filterPanel.open(filterBtn, "before_start");
    }
	
	/**
	* expand all node
	*/
	public void expandAll()
	{
		if (!expandToggle.isChecked())
			expandToggle.setChecked(true);
	
		TreeUtils.expandAll(getMenuTree());
	}
	
	/**
	 * collapse all node
	 */
	public void collapseAll()
	{
		if (expandToggle.isChecked())
			expandToggle.setChecked(false);
	
		TreeUtils.collapseAll(getMenuTree());
	}
	
	/**
	 *  On check event for the expand checkbox
	 */
	private void expandOnCheck()
	{
		if (expandToggle.isChecked())
			expandAll();
		else
			collapseAll();
	}
	//
}
