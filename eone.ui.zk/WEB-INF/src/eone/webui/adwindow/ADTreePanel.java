
package eone.webui.adwindow;


import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Tree;

import eone.webui.EONEWebUI;
import eone.webui.component.SimpleTreeModel;
import eone.webui.component.ToolBarButton;
import eone.webui.panel.TreeSearchPanel;
import eone.webui.util.TreeUtils;
import eone.webui.util.ZKUpdateUtil;

/**
 * 
 * @author hengsin
 *
 */
public class ADTreePanel extends Panel implements EventListener<Event>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2718257463734592729L;
	private static final String ON_EXPAND_MENU_EVENT = "onExpandMenu";
	private TreeSearchPanel pnlSearch;
    private Tree tree;
    
    private ToolBarButton expandToggle; // Elaine 2009/02/27 - expand tree
	private int m_windowno = -1;
	private int m_tabno = -1;
	private int AD_Tree_ID = -1;
    	
    public ADTreePanel()
    {
        init();        
    }
    
    public ADTreePanel(int windowno, int tabno)
    {
    	m_windowno = windowno;
    	m_tabno = tabno;
        init();        
    }
    
    /**
     * @param AD_Tree_ID
     * @param windowNo
     */
    public boolean initTree(int AD_Tree_ID, int windowNo) 
    {
    	return initTree(AD_Tree_ID, windowNo, null, 0);
    }

    public boolean initTree(int AD_Tree_ID, int windowNo, String linkColName, int linkID) 
    {
    	if (this.AD_Tree_ID != AD_Tree_ID)
    	{
	    	this.AD_Tree_ID = AD_Tree_ID;
	    	SimpleTreeModel.initADTree(tree, AD_Tree_ID, windowNo, linkColName, linkID);
	    	pnlSearch.initialise();
	    	return true;
    	}
    	return false;
    }
    
    private void init()
    {
    	setWidgetAttribute(EONEWebUI.WIDGET_INSTANCE_NAME, "treepanel");
    	ZKUpdateUtil.setWidth(this, "100%");
    	ZKUpdateUtil.setHeight(this, "100%");
    	
        tree = new Tree();
        tree.setMultiple(false);
        ZKUpdateUtil.setWidth(tree, "100%");
        ZKUpdateUtil.setVflex(tree, true);
        tree.setPageSize(-1); // Due to bug in the new paging functionality
        
        tree.setStyle("border: none");
        pnlSearch = new TreeSearchPanel(tree, Events.ON_SELECT, m_windowno, m_tabno);
        
        Toolbar toolbar = new Toolbar();
        toolbar.setMold("panel");
        toolbar.appendChild(pnlSearch);
        this.appendChild(toolbar);
        
        Panelchildren pc = new Panelchildren();
        this.appendChild(pc);
        pc.appendChild(tree);  
        
    }
    
    /**
     * @param event
     * @see EventListener#onEvent(Event)
     */
    public void onEvent(Event event)
    {
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
    }
    
    /**
     * @return tree
     */
	public Tree getTree() 
	{
		return tree;
	}
	
	/**
	* expand all node
	*/
	public void expandAll()
	{
		if (!expandToggle.isChecked())
			expandToggle.setChecked(true);
	
		TreeUtils.expandAll(tree);
	}
	
	/**
	 * collapse all node
	 */
	public void collapseAll()
	{
		if (expandToggle.isChecked())
			expandToggle.setChecked(false);
	
		TreeUtils.collapseAll(tree);
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

	public void prepareForRefresh() {
		this.AD_Tree_ID = -1;
	}

}
