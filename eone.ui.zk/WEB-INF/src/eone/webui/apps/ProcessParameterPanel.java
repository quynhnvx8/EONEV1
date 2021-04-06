
package eone.webui.apps;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.compiere.apps.IProcessParameter;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Space;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.XulElement;

import eone.base.model.GridField;
import eone.base.model.GridFieldVO;
import eone.base.model.MLookup;
import eone.base.model.MProcess;
import eone.base.process.ProcessInfo;
import eone.base.process.ProcessInfoParameter;
import eone.webui.Extensions;
import eone.webui.component.Column;
import eone.webui.component.Columns;
import eone.webui.component.EditorBox;
import eone.webui.component.Grid;
import eone.webui.component.GridFactory;
import eone.webui.component.NumberBox;
import eone.webui.component.Panel;
import eone.webui.component.Row;
import eone.webui.component.Rows;
import eone.webui.component.Urlbox;
import eone.webui.editor.IZoomableEditor;
import eone.webui.editor.WEditor;
import eone.webui.editor.WEditorPopupMenu;
import eone.webui.editor.WSearchEditor;
import eone.webui.editor.WebEditorFactory;
import eone.webui.event.ContextMenuListener;
import eone.webui.event.ValueChangeEvent;
import eone.webui.event.ValueChangeListener;
import eone.webui.session.SessionManager;
import eone.webui.util.ZKUpdateUtil;
import eone.webui.window.FDialog;

/**
 * Quynhnv.x8
 * Mod 25/09/2020
 */
public class ProcessParameterPanel extends Panel implements
		ValueChangeListener, IProcessParameter, EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2388338147222636369L;

	
	public ProcessParameterPanel(int WindowNo, ProcessInfo pi) {
		//
		m_WindowNo = WindowNo;
		m_processInfo = pi;
		m_AD_Window_ID = AEnv.getADWindowID (WindowNo);		
		this.m_InfoWindowID = pi.getAD_InfoWindow_ID();
		//
		initComponent();
		addEventListener("onDynamicDisplay", this);
		addEventListener("onPostEditorValueChange", this);
	} // ProcessParameterPanel
	
	private void initComponent() {
		centerPanel = GridFactory.newGridLayout();
		this.appendChild(centerPanel);

		// setup columns
		Columns columns = new Columns();
		centerPanel.appendChild(columns);
		Column col = new Column();
		ZKUpdateUtil.setWidth(col, "30%");
		columns.appendChild(col);
		col = new Column();
		ZKUpdateUtil.setWidth(col, "70%");
		columns.appendChild(col);
	}

	private int m_WindowNo;
	private ProcessInfo m_processInfo;
	// AD_Window of window below this dialog in case show parameter dialog panel
	private int			m_AD_Window_ID = 0;
	// infoWindowID of infoWindow below this dialog in case call process from infoWindow
	private int 		m_InfoWindowID = 0;
	/** Logger */
	private static final CLogger log = CLogger
			.getCLogger(ProcessParameterPanel.class);

	//
	private ArrayList<WEditor> m_wEditors = new ArrayList<WEditor>();
	private ArrayList<WEditor> m_wEditors2 = new ArrayList<WEditor>(); // for
																		// ranges
	private ArrayList<GridField> m_mFields = new ArrayList<GridField>();
	private ArrayList<Object []> m_Extend = new ArrayList<Object []>();
	private ArrayList<GridField> m_mFields2 = new ArrayList<GridField>();
	private ArrayList<Space> m_separators = new ArrayList<Space>();
	//
	private Grid centerPanel = null;

	/**
	 * Dispose
	 */
	public void dispose() {
		m_wEditors.clear();
		m_wEditors2.clear();
		m_mFields.clear();
		m_mFields2.clear();
		m_Extend.clear();

	} // dispose

	/**
	 * Read Fields to display
	 * 
	 * @return true if loaded OK
	 */
	public boolean init() {
		log.config("");

		
		String ASPFilter = "";
		
		String sql = null;
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Process_Para"))
			sql = "SELECT p.Name, p.Description, p.Help, "
					+ "p.AD_Reference_ID, p.AD_Process_Para_ID, "
					+ "p.FieldLength, p.IsMandatory, p.IsRange, p.ColumnName, "
					+ "p.DefaultValue, p.DefaultValue2, p.VFormat, p.ValueMin, p.ValueMax, "
					+ "p.SeqNo, p.AD_Reference_Value_ID, vr.Code AS ValidationCode, "
					+ "p.ReadOnlyLogic, p.DisplayLogic, p.IsEncrypted, NULL AS FormatPattern, p.MandatoryLogic, p.Placeholder, p.Placeholder2 "
					+ "FROM AD_Process_Para p"
					+ " LEFT OUTER JOIN AD_Val_Rule vr ON (p.AD_Val_Rule_ID=vr.AD_Val_Rule_ID) "
					+ "WHERE p.AD_Process_ID=?" // 1
					+ " AND p.IsActive='Y' " + ASPFilter + " ORDER BY SeqNo";
		else
			sql = "SELECT t.Name, t.Description, t.Help, "
					+ "p.AD_Reference_ID, p.AD_Process_Para_ID, "
					+ "p.FieldLength, p.IsMandatory, p.IsRange, p.ColumnName, "
					+ "p.DefaultValue, p.DefaultValue2, p.VFormat, p.ValueMin, p.ValueMax, "
					+ "p.SeqNo, p.AD_Reference_Value_ID, vr.Code AS ValidationCode, "
					+ "p.ReadOnlyLogic, p.DisplayLogic, p.IsEncrypted, NULL AS FormatPattern,p.MandatoryLogic, t.Placeholder, t.Placeholder2 "
					+ "FROM AD_Process_Para p"
					+ " INNER JOIN AD_Process_Para_Trl t ON (p.AD_Process_Para_ID=t.AD_Process_Para_ID)"
					+ " LEFT OUTER JOIN AD_Val_Rule vr ON (p.AD_Val_Rule_ID=vr.AD_Val_Rule_ID) "
					+ "WHERE p.AD_Process_ID=?" // 1
					+ " AND t.AD_Language='" + Env.getAD_Language(Env.getCtx())
					+ "'" + " AND p.IsActive='Y' " + ASPFilter
					+ " ORDER BY SeqNo";

		// Create Fields
		boolean hasFields = false;
		Rows rows = new Rows();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_processInfo.getAD_Process_ID());
			rs = pstmt.executeQuery();
			ArrayList<GridFieldVO> listVO = new ArrayList<GridFieldVO>();
			while (rs.next()) {
				hasFields = true;

				// Create Field
				GridFieldVO voF = GridFieldVO.createParameter(Env.getCtx(), m_WindowNo, m_processInfo.getAD_Process_ID(), m_AD_Window_ID, m_InfoWindowID,rs);
				listVO.add(voF);
			}
			Collections.sort(listVO, new GridFieldVO.SeqNoComparator());

			for (int i = 0; i < listVO.size(); i++)
			{
				createField(listVO.get(i), rows);
				if (log.isLoggable(Level.INFO)) log.info(listVO.get(i).ColumnName + listVO.get(i).SeqNo);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally{
			DB.close(rs,pstmt);
			rs = null;
			pstmt = null;
		}

		// both vectors the same?
		if (m_mFields.size() != m_mFields2.size()
				|| m_mFields.size() != m_wEditors.size()
				|| m_mFields2.size() != m_wEditors2.size())
			log.log(Level.SEVERE, "View & Model vector size is different");

		// clean up
		if (hasFields) {
			centerPanel.appendChild(rows);
			dynamicDisplay();
		} else
			dispose();
		return hasFields;
	} // initDialog

	private void createField(GridFieldVO voF, Rows rows) {
		GridField mField = new GridField(voF);
		m_mFields.add(mField); // add to Fields
		m_Extend.add(new Object [] {voF.displayType, voF.Header, voF.isRange});
		Row row = new Row();

		// The Editor
		WEditor editor = WebEditorFactory.getEditor(mField, false);
		editor.setProcessParameter(true);
		editor.getComponent().addEventListener(Events.ON_FOCUS, this);
		editor.addValueChangeListener(this);
		editor.dynamicDisplay();
		// MField => VEditor - New Field value to be updated to editor
		mField.addPropertyChangeListener(editor);
		// Set Default
		Object defaultObject = mField.getDefaultForPanel();
		mField.setValue(defaultObject, true);
		if (defaultObject instanceof Timestamp) {
			Env.setContext(Env.getCtx(), m_WindowNo, "DaySel", TimeUtil.getDaySel((Timestamp) defaultObject));
			Env.setContext(Env.getCtx(), m_WindowNo, "MonthSel", TimeUtil.getMonthSel((Timestamp) defaultObject));
			Env.setContext(Env.getCtx(), m_WindowNo, "YearSel", TimeUtil.getYearSel((Timestamp) defaultObject));
		}
		// streach component to fill grid cell
		editor.fillHorizontal();
		// setup editor context menu
		WEditorPopupMenu popupMenu = editor.getPopupMenu();
		if (popupMenu != null)
		{
			popupMenu.addMenuListener((ContextMenuListener)editor);
			popupMenu.setId(mField.getColumnName()+"-popup");
			this.appendChild(popupMenu);
			if (!mField.isFieldOnly())
			{
				Label label = editor.getLabel();
				if (popupMenu.isZoomEnabled() && editor instanceof IZoomableEditor)
				{
					label.addEventListener(Events.ON_CLICK, new ZoomListener((IZoomableEditor) editor));
				}

				popupMenu.addContextElement(label);
				if (editor.getComponent() instanceof XulElement) 
				{
					popupMenu.addContextElement((XulElement) editor.getComponent());
				}
			}        				        				
		}
		//
		m_wEditors.add(editor); // add to Editors

    	Div div = new Div();
        div.setStyle("text-align: right;");
        eone.webui.component.Label label = editor.getLabel();
        div.appendChild(label);
        if (label.getDecorator() != null)
        	div.appendChild(label.getDecorator());
        row.appendChild(div);

		//
		if (voF.isRange) {
			Div box = new Div();
			box.setStyle("display: flex; align-items: center;");
			ZKUpdateUtil.setWidth(box, "100%");
			box.appendChild(editor.getComponent());
			ZKUpdateUtil.setWidth((HtmlBasedComponent) editor.getComponent(), "49%");
			//
			GridFieldVO voF2 = GridFieldVO.createParameter(voF);
			GridField mField2 = new GridField(voF2);
			m_mFields2.add(mField2);
			// The Editor
			WEditor editor2 = WebEditorFactory.getEditor(mField2, false);
			editor2.setProcessParameter(true);
			//override attribute
			editor2.getComponent().setClientAttribute("columnName", mField2.getColumnName()+"_To");
			editor2.getComponent().addEventListener(Events.ON_FOCUS, this);
			// New Field value to be updated to editor
			mField2.addPropertyChangeListener(editor2);
			editor2.dynamicDisplay();
			ZKUpdateUtil.setWidth((HtmlBasedComponent) editor2.getComponent(), "49%");
			setEditorPlaceHolder(editor2, mField2.getPlaceholder2());
			// setup editor context menu
			popupMenu = editor2.getPopupMenu();
			if (popupMenu != null) {
				popupMenu.addMenuListener((ContextMenuListener) editor2);
				this.appendChild(popupMenu);
			}
			// Set Default
			Object defaultObject2 = mField2.getDefaultForPanel();
			mField2.setValue(defaultObject2, true);
			//
			m_wEditors2.add(editor2);
			Space separator = new Space();
			separator.setStyle("margin:0; width: 2%;");
			m_separators.add(separator);
			box.appendChild(separator);
			box.appendChild(editor2.getComponent());
			row.appendChild(box);
		} else {
			row.appendChild(editor.getComponent());
			m_mFields2.add(null);
			m_wEditors2.add(null);
			m_separators.add(null);
		}
		rows.appendChild(row);
	} // createField

	private void setEditorPlaceHolder(WEditor editor, String msg) {
		Component c = editor.getComponent();
		if (c instanceof InputElement) {
			((InputElement) c).setPlaceholder(msg);
		} else {
			for (Component e : c.getChildren()) {
				if (e instanceof InputElement) {
					((InputElement) e).setPlaceholder(msg);
					break;
				}
			}
		}
		
	}

	/**
	 * Validate Parameter values
	 * 
	 * @return true if parameters are valid
	 */
	public boolean validateParameters() {
		log.config("");

		/**
		 * Mandatory fields see - MTable.getMandatory
		 */
		StringBuilder sb = new StringBuilder();
		int size = m_mFields.size();
		for (int i = 0; i < size; i++) {
			GridField field = (GridField) m_mFields.get(i);
			if (field.isMandatory(true)) // check context
			{
				WEditor wEditor = (WEditor) m_wEditors.get(i);
				Object data = wEditor.getValue();
				if (data == null || data.toString().length() == 0) {
					field.setInserting(true); // set editable (i.e. updateable)
												// otherwise deadlock
					field.setError(true);
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(field.getHeader());
					if (m_wEditors2.get(i) != null) // is a range
						sb.append(" (").append(Msg.getMsg(Env.getCtx(), "ProcessParameterRangeFrom")).append(")");
				} else
					field.setError(false);
				// Check for Range
				WEditor wEditor2 = (WEditor) m_wEditors2.get(i);
				if (wEditor2 != null) {
					Object data2 = wEditor2.getValue();
					GridField field2 = (GridField) m_mFields2.get(i);
					if (data2 == null || data2.toString().length() == 0) {
						field2.setInserting(true); // set editable (i.e.
													// updateable) otherwise
													// deadlock
						field2.setError(true);
						if (sb.length() > 0)
							sb.append(", ");
						sb.append(field2.getHeader());
						sb.append(" (").append(Msg.getMsg(Env.getCtx(), "ProcessParameterRangeTo")).append(")");
					} else
						field2.setError(false);
				} // range field
			} // mandatory
		} // field loop

		if (sb.length() != 0) {
			FDialog.error(m_WindowNo, this, "FillMandatory", sb.toString());
			return false;
		}

		if (m_processInfo.getAD_Process_ID() > 0) {
			String className = MProcess.get(Env.getCtx(), m_processInfo.getAD_Process_ID()).getClassname();
			List<IProcessParameterListener> listeners = Extensions.getProcessParameterListeners(className, null);
			for(IProcessParameterListener listener : listeners) {
				String error = listener.validate(this);
				if (!Util.isEmpty(error)) {
					FDialog.error(m_WindowNo, this, error);
					return false;
				}
			}
		}
		
		return true;
	}	//	validateParameters
	
	
	//QUYNHNV.X8: Lay gia tri tu form report.
	public boolean saveParameters() {
		log.config("");

		if (!validateParameters())
			return false;

		ArrayList<ProcessInfoParameter> listPara = new ArrayList<ProcessInfoParameter>();
		
		
		for (int i = 0; i < m_mFields.size(); i++) {
			String paramName = null;
			String str = null, strTo = null;
			Timestamp date = null, dateTo = null;
			BigDecimal num = null, numTo = null;
			String info = null, infoTo = null;
			// Get Values
			WEditor editor = (WEditor) m_wEditors.get(i);
			WEditor editor2 = (WEditor) m_wEditors2.get(i);
			Object result = editor.getValue();
			Object result2 = null;
			if (editor2 != null)
				result2 = editor2.getValue();

			GridField mField = (GridField) m_mFields.get(i);
			paramName = mField.getColumnName();	
			String header = mField.getHeader();
			Object [] keyExtend = m_Extend.get(i);
			
			// Date
			if (result instanceof Timestamp || result2 instanceof Timestamp) {
				if (result instanceof Timestamp)
					date = (Timestamp) result;
				if (editor2 != null && result2 != null && result2 instanceof Timestamp)
					dateTo = (Timestamp) result2;
			}
			// Integer
			else if (result instanceof Integer || result2 instanceof Integer) {
				if (result != null && result instanceof Integer) {
					Integer ii = (Integer) result;
					num = new BigDecimal(ii.intValue());
				}
				if (editor2 != null && result2 != null && result2 instanceof Integer) {
					Integer ii = (Integer) result2;
					numTo = new BigDecimal(ii.intValue());
				}
			}
			// BigDecimal
			else if (result instanceof BigDecimal
					|| result2 instanceof BigDecimal) {
				if (result instanceof BigDecimal)
					num = (BigDecimal) result;
				if (editor2 != null && result2 != null && result2 instanceof BigDecimal)
					numTo = (BigDecimal) result2;
			}
			// Boolean
			else if (result instanceof Boolean) {
				Boolean bb = (Boolean) result;
				String value = bb.booleanValue() ? "Y" : "N";
				str = value;
				// to does not make sense
			}
			// String
			else {
				if (result != null)
					str = result.toString();
				if (editor2 != null && result2 != null)
					strTo = result2.toString();
			}

			// Info
			info = editor.getDisplay();
			if (editor2 != null)
				infoTo = editor2.getDisplay();
			
			ProcessInfoParameter para = new ProcessInfoParameter(paramName, date, dateTo, num, numTo, str, strTo, info, infoTo, keyExtend, header);
			
			if (log.isLoggable(Level.FINE)) log.fine(para.toString());
			
			listPara.add(para);
		} // for every parameter
		m_processInfo.setProcessPara(listPara.toArray(new ProcessInfoParameter[]{}));
		return true;
	} // saveParameters

	
	public void valueChange(ValueChangeEvent evt) {
		if (evt.getSource() instanceof WEditor) {
			GridField changedField = ((WEditor) evt.getSource()).getGridField();
			if (changedField != null) {
				processDependencies (changedField);
				// future processCallout (changedField);
			}
			Events.postEvent("onPostEditorValueChange", this, evt.getSource());
		}
		processNewValue(evt.getNewValue(), evt.getPropertyName());
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_FOCUS)) {
    		for (WEditor editor : m_wEditors)
    		{
    			if (editor.isComponentOfEditor(event.getTarget()))
    			{
        			SessionManager.getAppDesktop().updateHelpTooltip(editor.getGridField());
        			return;
    			}
    		}
    		
    		for (WEditor editor : m_wEditors2)
    		{
    			if (editor != null && editor.getComponent() != null && editor.isComponentOfEditor(event.getTarget()))
    			{
        			SessionManager.getAppDesktop().updateHelpTooltip(editor.getGridField());
        			return;
    			}
    		}
    	}
    	else if (event.getName().equals("onDynamicDisplay")) {
    		dynamicDisplay();
    	}
    	else if (event.getName().equals("onPostEditorValueChange")) {
    		onPostEditorValueChange((WEditor)event.getData());
    	}
	}

	private void onPostEditorValueChange(WEditor editor) {
		if (m_processInfo.getAD_Process_ID() > 0) {
			String className = MProcess.get(Env.getCtx(), m_processInfo.getAD_Process_ID()).getClassname();
			List<IProcessParameterListener> listeners = Extensions.getProcessParameterListeners(className, editor.getColumnName());
			for(IProcessParameterListener listener : listeners) {
				listener.onChange(this, editor.getColumnName(), editor);
			}
		}
	}
	
	/**
	 *  Evaluate Dependencies
	 *  @param changedField changed field
	 */
	private void processDependencies (GridField changedField)
	{
		String columnName = changedField.getColumnName();

		for (GridField field : m_mFields) {
			if (field == null || field == changedField)
				continue;
			verifyChangedField(field, columnName);
		}
		for (GridField field : m_mFields2) {
			if (field == null || field == changedField)
				continue;
			verifyChangedField(field, columnName);
		}
	}   //  processDependencies

	private void verifyChangedField(GridField field, String columnName) {
		ArrayList<String> list = field.getDependentOn();
		if (list.contains(columnName)) {
			if (field.getLookup() instanceof MLookup)
			{
				MLookup mLookup = (MLookup)field.getLookup();
				//  if the lookup is dynamic (i.e. contains this columnName as variable)
				if (mLookup.getValidation().indexOf("@"+columnName+"@") != -1)
				{
					if (log.isLoggable(Level.FINE)) log.fine(columnName + " changed - "
						+ field.getColumnName() + " set to null");
					//  invalidate current selection
					field.setValue(null, true);
				}
			}
		}
	}
	
	private void processNewValue(Object value, String name) {
		if (value == null)
			value = new String("");

		if (value instanceof String)
			Env.setContext(Env.getCtx(), m_WindowNo, name, (String) value);
		else if (value instanceof Integer)
			Env.setContext(Env.getCtx(), m_WindowNo, name,
					((Integer) value).intValue());
		else if (value instanceof Boolean)
			Env.setContext(Env.getCtx(), m_WindowNo, name,
					((Boolean) value).booleanValue());
		else if (value instanceof Timestamp) {
			Env.setContext(Env.getCtx(), m_WindowNo, name, (Timestamp) value);
			Env.setContext(Env.getCtx(), m_WindowNo, "DaySel", TimeUtil.getDaySel((Timestamp) value));
			Env.setContext(Env.getCtx(), m_WindowNo, "MonthSel", TimeUtil.getMonthSel((Timestamp) value));
			Env.setContext(Env.getCtx(), m_WindowNo, "YearSel", TimeUtil.getYearSel((Timestamp) value));
		}
		else
			Env.setContext(Env.getCtx(), m_WindowNo, name, value.toString());

		Events.postEvent("onDynamicDisplay", this, (Object)null);
	}

	private void dynamicDisplay() {
		for (int i = 0; i < m_wEditors.size(); i++) {
			WEditor editor = m_wEditors.get(i);
			GridField mField = editor.getGridField();
			if (mField.isDisplayed(true)) {
				if (!editor.isVisible()) {
					editor.setVisible(true);
					if (mField.getVO().isRange) {
						m_separators.get(i).setVisible(true);
						m_wEditors2.get(i).setVisible(true);
					}
				}
				boolean rw = mField.isEditablePara(true); // r/w - check if
															// field is Editable
				editor.setReadWrite(rw);
				editor.dynamicDisplay();
				if (mField.getVO().isRange) {
					m_wEditors2.get(i).setReadWrite(rw);
					m_wEditors2.get(i).dynamicDisplay();
				}
			} else if (editor.isVisible()) {
				editor.setVisible(false);
				if (mField.getVO().isRange) {
					m_separators.get(i).setVisible(false);
					m_wEditors2.get(i).setVisible(false);
				}
			}
			editor.setMandatory(mField.isMandatory(true));
        	editor.updateStyle();
		}
		if (getParent() != null) {
			getParent().invalidate();
		}
	}

	/**
	 * Restore window context.
	 * 
	 * @author teo_sarca [ 1699826 ]
	 * @see eone.base.model.GridField#restoreValue()
	 */
	protected void restoreContext() {
		for (GridField f : m_mFields) {
			if (f != null)
				f.restoreValue();
		}
		for (GridField f : m_mFields2) {
			if (f != null)
				f.restoreValue();
		}
	}

	/**
	 * @param processInfo
	 */
	public void setProcessInfo(ProcessInfo processInfo) {
		m_processInfo = processInfo;
	}
	
	public boolean focusToFirstEditor() {
		if (m_wEditors.isEmpty())
			return false;
		for(WEditor editor : m_wEditors) {
			if (editor.isVisible()) {
				focusToEditor(editor);
				return true;
			}
		}
		return false;
	}

	private void focusToEditor(WEditor toFocus) {
		Component c = toFocus.getComponent();
		if (c instanceof EditorBox) {
			c = ((EditorBox)c).getTextbox();
		} else if (c instanceof NumberBox) {
			c = ((NumberBox)c).getDecimalbox();
		} else if (c instanceof Urlbox) {
			c = ((Urlbox)c).getTextbox();
		}
		((HtmlBasedComponent)c).focus();		
	}
	
	/**
	 * Get parameter field editor by column name
	 * @param columnName
	 * @return editor
	 */
	public WEditor getEditor(String columnName) {
		for(int i = 0; i < m_mFields.size(); i++) {
			if (m_mFields.get(i).getColumnName().equals(columnName)) {
				return m_wEditors.get(i);
			}
		}
		return null;
	}

	/**
	 * Get parameter field value to editor by column name
	 * @param columnName
	 * @return editor
	 */
	public WEditor getEditorTo(String columnName) {
		for(int i = 0; i < m_mFields.size(); i++) {
			if (m_mFields.get(i).getColumnName().equals(columnName)) {
				return m_wEditors2.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @return true if editor is showing dialog awaiting user action
	 */
	public boolean isWaitingForDialog() {
		for (int i = 0; i < m_mFields.size(); i++) {
			// Get Values
			WEditor editor = (WEditor) m_wEditors.get(i);
			WEditor editor2 = (WEditor) m_wEditors2.get(i);
			if (editor != null && editor instanceof WSearchEditor) {
				if (((WSearchEditor)editor).isShowingDialog())
					return true;
			} else if (editor2 != null && editor2 instanceof WSearchEditor) {
				if (((WSearchEditor)editor2).isShowingDialog())
					return true;
			}
		}
		
		return false;
	}
	
	static class ZoomListener implements EventListener<Event> {

		private IZoomableEditor searchEditor;

		ZoomListener(IZoomableEditor editor) {
			searchEditor = editor;
		}

		public void onEvent(Event event) throws Exception {
			if (Events.ON_CLICK.equals(event.getName())) {
				searchEditor.actionZoom();
			}

		}

	}

} // ProcessParameterPanel
