
package org.adempiere.webui.dashboard;

import java.util.Calendar;

import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Center;
import org.zkoss.zul.South;
import org.zkoss.zul.Timebox;

/**
 * 
 * @author Elaine
 *
 */
public class RequestWindow extends Window implements EventListener<Event> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7757368164776005797L;

	
	/** Read Only				*/
	private boolean m_readOnly = false;
	
	private WTableDirEditor requestTypeField, dueTypeField, priorityField, 
		confidentialField, salesRepField, entryConfidentialField;
	private Textbox txtSummary;
	private Datebox dbxStartPlan, dbxCompletePlan;
	private Timebox tbxStartTime, tbxEndTime;
	private ConfirmPanel confirmPanel;
	
	private Calendar calBegin,calEnd;
	
	public RequestWindow(CalendarsEvent ce, Window parent) {
		
		super();
		

		setTitle(Msg.getMsg(Env.getCtx(),"Event"));
		setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
		if (!ThemeManager.isUseCSSForWindowSize()) {
			ZKUpdateUtil.setWindowWidthX(this, 400);
			ZKUpdateUtil.setWindowHeightX(this, 550);
		} else {
			addCallback(AFTER_PAGE_ATTACHED, t -> {
				ZKUpdateUtil.setCSSHeight(this);
				ZKUpdateUtil.setCSSWidth(this);
			});
		}
		this.setSclass("popup-dialog request-dialog");
		this.setBorder("normal");
		this.setShadow(true);
		this.setClosable(true);
		
		
		
		txtSummary = new Textbox();
		txtSummary.setRows(3);
		ZKUpdateUtil.setWidth(txtSummary, "95%");
		
		dbxStartPlan = new Datebox();
		dbxCompletePlan = new Datebox();
		
		tbxStartTime = new Timebox();
		tbxEndTime = new Timebox();
		
		confirmPanel = new ConfirmPanel(true);
		confirmPanel.addActionListener(this);
		
		
		Grid grid = GridFactory.newGridLayout();
		
		Columns columns = new Columns();
		grid.appendChild(columns);
		
		Column column = new Column();
		columns.appendChild(column);
		
		column = new Column();
		columns.appendChild(column);
		ZKUpdateUtil.setWidth(column, "250px");
		
		Rows rows = new Rows();
		grid.appendChild(rows);
		
		
		Borderlayout borderlayout = new Borderlayout();
		this.appendChild(borderlayout);
		ZKUpdateUtil.setHflex(borderlayout, "1");
		ZKUpdateUtil.setVflex(borderlayout, "1");
		
		Center centerPane = new Center();
		centerPane.setSclass("dialog-content");
		centerPane.setAutoscroll(true);
		borderlayout.appendChild(centerPane);
		
		centerPane.appendChild(grid);
		ZKUpdateUtil.setVflex(grid, "min");
		ZKUpdateUtil.setHflex(grid, "1");
		ZKUpdateUtil.setVflex(centerPane, "min");

		South southPane = new South();
		southPane.setSclass("dialog-footer");
		borderlayout.appendChild(southPane);
		southPane.appendChild(confirmPanel);
		
		dbxStartPlan.setValue(ce.getBeginDate());
		dbxCompletePlan.setValue(ce.getEndDate());
		tbxStartTime.setValue(ce.getBeginDate());
		tbxEndTime.setValue(ce.getEndDate());
	}
	
	public void onEvent(Event e) throws Exception {
		if (m_readOnly)
			this.detach();
		else if (e.getTarget() == confirmPanel.getButton(ConfirmPanel.A_OK)) {
			// Check Mandatory fields
			String fillMandatory = Msg.translate(Env.getCtx(), "FillMandatory");
			if (dueTypeField.getValue() == null || dueTypeField.getValue().equals(""))
				throw new WrongValueException(dueTypeField.getComponent(), fillMandatory);
			if (requestTypeField.getValue() == null || requestTypeField.getValue().equals(0))
				throw new WrongValueException(requestTypeField.getComponent(), fillMandatory);
			if (priorityField.getValue() == null || priorityField.getValue().equals(""))
				throw new WrongValueException(priorityField.getComponent(), fillMandatory);
			if (txtSummary.getText() == null || txtSummary.getText().equals(""))
				throw new WrongValueException(txtSummary, fillMandatory);
			if (confidentialField.getValue() == null || confidentialField.getValue().equals(""))
				throw new WrongValueException(confidentialField.getComponent(), fillMandatory);
			if (salesRepField.getValue() == null || salesRepField.getValue().equals(0))
				throw new WrongValueException(salesRepField.getComponent(), fillMandatory);
			if (entryConfidentialField.getValue() == null || entryConfidentialField.getValue().equals(""))
				throw new WrongValueException(entryConfidentialField.getComponent(), fillMandatory);
			if (dbxStartPlan.getValue().compareTo(dbxCompletePlan.getValue()) > 0) 
				throw new WrongValueException(dbxCompletePlan, Msg.translate(Env.getCtx(), "DateCompletePlan"));	
			if (checkTime()) 
				throw new WrongValueException(tbxStartTime, Msg.translate(Env.getCtx(), "CheckTime"));	
					
			
			this.detach();
		}
		else if (e.getTarget() == confirmPanel.getButton(ConfirmPanel.A_CANCEL))
			this.detach();
	}
	
	//Check, Start time is not  >=  End time, when Start Plan == Complete Plan
	private boolean checkTime()
	{
		calBegin = Calendar.getInstance();
		calBegin.setTime(dbxStartPlan.getValue());
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(tbxStartTime.getValue().getTime());
		calBegin.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY));
		calBegin.set(Calendar.MINUTE, cal1.get(Calendar.MINUTE));
		calBegin.set(Calendar.SECOND, 0);
		calBegin.set(Calendar.MILLISECOND, 0);
		
		calEnd = Calendar.getInstance();
		calEnd.setTime(dbxCompletePlan.getValue());
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(tbxEndTime.getValue().getTime());
		calEnd.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
		calEnd.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);

		if (calBegin.compareTo(calEnd) >= 0) {
			return true;
		} else {
			return false;
		}	
	}	
}
