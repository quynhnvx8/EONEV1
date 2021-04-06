package eone.webui.component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.compiere.util.KeyNamePair;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
/**
 * 
 * @author Quynhnv.x8
 * @date   07/10/2020
 */
public class WListItemRenderer2 extends WListItemRenderer {
	private List<ArrayList<Object>> m_avlist = null;
	private HashMap<String, Boolean> m_rowChanse = null;
	/** Position of PostingType Column in Fact_Acct	*/
	private int POSTING_TYPE = -1;
	/** Position of GL_Distribution_ID Column in Fact_Acct	*/
	private int AUTO_POSTED = -1;
	/** Flag for Accounting Viewer	*/
	private boolean isAcctViewer = false;
	
	public WListItemRenderer2() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Contructor for Account Viewer
	 * @param nPostingType
	 * @param nAutoPosted
	 */
	public WListItemRenderer2(int nPostingType, int nAutoPosted) {
		POSTING_TYPE = nPostingType;
		AUTO_POSTED = nAutoPosted;
		isAcctViewer = true;
	}
	
	public WListItemRenderer2(IZoom viewer, boolean zoomable) {
		super(viewer, zoomable);
		// TODO Auto-generated constructor stub
	}

	public WListItemRenderer2(IZoom viewer, boolean zoomable,
			List<? extends String> columnNames) {
		super(viewer, zoomable, columnNames);
		// TODO Auto-generated constructor stub
	}

	public WListItemRenderer2(List<? extends String> columnNames) {
		super(columnNames);
		// TODO Auto-generated constructor stub
	}
	public void setAVList(List<ArrayList<Object>> list) {
		m_avlist = list;
	}
	
	public void setRowChanses(HashMap<String, Boolean> rowChanse) {
		this.m_rowChanse = rowChanse;
	}	
	
	public void setPostingType(int nPostingType) {
		POSTING_TYPE = nPostingType;
	}
	
	public void setAutoPosted(int nAutoPosted) {
		AUTO_POSTED = nAutoPosted;
	}
	
	@Override
	public void render(Listitem item, Object data, int index) throws Exception
	{
		render((ListItem)item, data, index);
	}
	
	
	private void render(ListItem item, Object data, int index)
	{
		Listcell listcell = null;
		int colIndex = 0;
		int rowIndex = item.getIndex();
		WListbox table = null;

		if (item.getListbox() instanceof WListbox)
		{
			table = (WListbox)item.getListbox();
		}

		if (!(data instanceof List))
		{
			throw new IllegalArgumentException("A model element was not a list");
		}

		if (listBox == null || listBox != item.getListbox())
		{
			listBox = item.getListbox();
		}
		if (cellListener == null)
		{
			cellListener = new CellListener();
		}

		List<?> fields = (List<?>)data;
		for (Object field : fields)
		{
			listcell = getCellComponent(table, field, rowIndex, colIndex);
			if (POSTING_TYPE > 0 && fields.get(POSTING_TYPE) != null) {
				String oldStyle = listcell.getStyle();
				if (fields.get(POSTING_TYPE).equals("D")) {
					listcell.setStyle(oldStyle + ";color:red");
				} else if (fields.get(POSTING_TYPE).equals("A")) {
					if (AUTO_POSTED >0 && fields.get(AUTO_POSTED) != null) {
						Object obj = fields.get(AUTO_POSTED);
						if (obj instanceof Integer) {
							Integer value = (Integer)obj;
							if (value.intValue() >= 1000000)
								listcell.setStyle(oldStyle + ";color:blue");
						}
					}
				}
			}
			
			listcell.setParent(item);
			listcell.addEventListener(Events.ON_DOUBLE_CLICK, cellListener);
			listcell.addEventListener(Events.ON_CLICK, cellListener);
			colIndex++;
		}
		return;
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		int col = -1;
		int row = -1;
		Object value = null;

		Component source = event.getTarget();
		
		if (isWithinListCell(source))
		{
			row = getRowPosition(source);
			col = getColumnPosition(source);

			if (source instanceof Checkbox)
			{
				value = Boolean.valueOf(((Checkbox)source).isChecked());
			}
			else if (source instanceof Decimalbox)
			{
				value = ((Decimalbox)source).getValue().intValue();
			}
			else if (source instanceof Datebox)
			{
				if (((Datebox)source).getValue() != null)
					value = new Timestamp(((Datebox)source).getValue().getTime());
			}
			else if (source instanceof Textbox)
			{
				value = ((Textbox)source).getValue();
			}
			if(value != null)
			{
				ArrayList<Object>  rowValues = m_avlist.get(row);
				rowValues.set(col, value);	
				KeyNamePair keyValue = (KeyNamePair)rowValues.get(0);
				m_rowChanse.put(keyValue.getID(), Boolean.TRUE);
			}
		}
		return;
	}
		
	private void doClick (Event event) {
		int col = -1;
		int row = -1;
		Object value = null;

		Component source = event.getTarget();
		if (isWithinListCell(source))
		{
			row = getRowPosition(source);
			col = getColumnPosition(source);
			if (isAcctViewer) {
				Listitem item = listBox.getSelectedItem();
				Listcell cell = (Listcell) item.getChildren().get(col);
				//Clear old cell
				clearSelectedCell();
				//Clear all selected
				clearSelectedCells();
				listBox.setAttribute("total", null);
				String oldStyle = cell.getStyle();
				cell.setStyle(oldStyle + ";background:#C0C0C0");
				listBox.setAttribute("selectedcell", cell);
				listBox.setAttribute("colcell", col);
				listBox.setAttribute("rowcell", row);
				//Set total = 0;
				listBox.setAttribute("total", null);
			} else {
				if (source instanceof Checkbox)
				{
					value = Boolean.valueOf(((Checkbox)source).isChecked());
				}
				else if (source instanceof Decimalbox)
				{
					value = ((Decimalbox)source).getValue().intValue();
				}
				else if (source instanceof Datebox)
				{
					if (((Datebox)source).getValue() != null)
						value = new Timestamp(((Datebox)source).getValue().getTime());
				}
				else if (source instanceof Textbox)
				{
					value = ((Textbox)source).getValue();
				}
				if(value != null)
				{
					ArrayList<Object>  rowValues = m_avlist.get(row);
					rowValues.set(col, value);	
					KeyNamePair keyValue = (KeyNamePair)rowValues.get(0);
					m_rowChanse.put(keyValue.getID(), Boolean.TRUE);
				}
			
			}
			
		}
	}
	
	private void clearSelectedCell()
	{
		Object objCell = listBox.getAttribute("selectedcell");
		if (objCell != null) {
			Listcell oldCell = (Listcell) objCell;
			oldCell.setStyle(oldCell.getStyle().replace(";background:#C0C0C0", ""));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void clearSelectedCells() 
	{
		Object objs = listBox.getAttribute("selectedcells");
		if (objs == null) 
			return ;
		ArrayList<Listcell> cells = (ArrayList<Listcell>)objs;
		for (int i = 0; i < cells.size(); i++) {
			Listcell c = (Listcell)cells.get(i);
			c.setStyle(c.getStyle().replace(";background:#C0C0C0", ""));
		}
		listBox.setAttribute("selectedcells", null);
	}
	
	@SuppressWarnings("unchecked")
	private void doOnCtrSelect (Event event) 
	{
		int col = -1;
		Object value;
		
		Component source = event.getTarget();
		value = listBox.getAttribute("colcell");
		if (value != null) {
			col = ((Integer)value).intValue();
		}
		int newCol = getColumnPosition(source);
		
		Object objs = listBox.getAttribute("selectedcells");
		// Current cell
		Listitem item = listBox.getSelectedItem();
		Listcell cell = (Listcell) item.getChildren().get(newCol);
		String oldStyle = cell.getStyle();
		cell.setStyle(oldStyle + ";background:#C0C0C0");
		
		// Clear old cell
		if (newCol != col) {
			// Reset all selected
			clearSelectedCells();
			clearSelectedCell();
			List<Listcell> cells = new ArrayList<Listcell>();
			// Set a new cell
			cells.add(cell);
			listBox.setAttribute("selectedcells", cells);
			listBox.setAttribute("colcell", newCol);
		} else {
			List<Listcell> cells = null;
			if (objs == null) {
				cells = new ArrayList<Listcell>();
			} else {
				cells = (ArrayList<Listcell>)objs;
			}
			Object objCell = listBox.getAttribute("selectedcell");
			if (objCell != null) {
				Listcell oldCell = (Listcell)objCell;
				cells.add(oldCell);
				if (isNumeric(oldCell.getValue().toString())) {
					listBox.setAttribute("total", new BigDecimal(oldCell.getValue().toString()));
				}
				listBox.setAttribute("selectedcell", null);
			}
			cells.add(cell);
			listBox.setAttribute("selectedcells", cells);
		}
		//Sum
		value = listBox.getAttribute("total");
		BigDecimal total = null;
		if (value != null) {
			total = new BigDecimal(value.toString());
			
		} else {
			total = new BigDecimal("0");
		}
		value = cell.getValue();
		if (isNumeric(value.toString())) {
			total = total.add(new BigDecimal(value.toString()));
		}
		
		listBox.setAttribute("total", total);
	}
	
	@SuppressWarnings("unchecked")
	private void doOnShiftSelect (Event event) 
	{

		int col = -1;
		int row = -1;
		Object value;
		
		Component source = event.getTarget();
		//Get position of column 
		value = listBox.getAttribute("colcell");
		if (value != null) {
			col = ((Integer)value).intValue();
		}
		//Get position of row
		value = listBox.getAttribute("rowcell");
		if (value != null) {
			row = ((Integer)value).intValue();
		}
		int newCol = getColumnPosition(source);
		int newRow = getRowPosition(source);
		
		Object objs = listBox.getAttribute("selectedcells");
		// Current cell
		Listitem item = listBox.getSelectedItem();
		Listcell cell = (Listcell) item.getChildren().get(newCol);
		
		// Clear old cell
		clearSelectedCell();
		if (newCol != col) {
			// Reset all selected
			clearSelectedCells();
			List<Listcell> cells = new ArrayList<Listcell>();
			// Set a new cell
			cells.add(cell);
			listBox.setAttribute("selectedcells", cells);
			listBox.setAttribute("colcell", newCol);
			listBox.setAttribute("total", null);
		} else {
			List<Listcell> cells = null;
			if (objs == null) {
				cells = new ArrayList<Listcell>();
			} else {
				cells = (ArrayList<Listcell>)objs;
				//Reset all selected
				clearSelectedCells();
			}
			// Adds from old cell to new cell in column
			int nfrom = row;
			if (row == -1) {
				nfrom = newRow;
			}
			int nto = newRow;
			if (row > newRow) {
				nfrom = newRow;
				nto = row;
			}
			
			BigDecimal total = new BigDecimal("0");
			for (int r = nfrom; r <= nto; r++) {
				Listitem items = listBox.getItemAtIndex(r);
				Listcell c = (Listcell) items.getChildren().get(newCol);
				if (c != null) {
					String oldStyle = c.getStyle();
					c.setStyle(oldStyle + ";background:#C0C0C0");
					cells.add(c);
					value = c.getValue();
					if (isNumeric(value.toString())) {
						total = total.add(new BigDecimal(value.toString()));
					}
				}
			}
			listBox.setAttribute("selectedcells", cells);
			listBox.setAttribute("total", total);
		}
	
	}
	
	/**
	 * Check if is number
	 * @author hungtq24
	 * @date 13/02/2014
	 * @param s
	 * @return
	 */
	public static boolean isNumeric (String s) {
		return s.matches("[+-]?\\d*(\\.\\d+)?") 				//###.###
    			||s.matches("[+-]?\\d*(\\,\\d+)?")				//###,###
    			||s.matches("[+-]?\\d*(\\.\\d+)(\\,\\d+)?")		//#.###,###
    			||s.matches("[+-]?\\d*(\\,\\d+)(\\.\\d+)?"); 	//#,###.###
	}
	
	/**
	 * Lop xu ly to hop cac phim nong cua nguoi dung
	 * @author hungtq24
	 * @date 26/02/2014
	 *
	 */
	class CellListener implements EventListener<Event> {

		public CellListener() {
		}

		public void onEvent(Event event) throws Exception {
			if (listBox != null && Events.ON_DOUBLE_CLICK.equals(event.getName())) {
				Event evt = new Event(Events.ON_DOUBLE_CLICK, listBox);
				Events.sendEvent(listBox, evt);
			} else if (listBox != null && Events.ON_CLICK.equals(event.getName())) {
				MouseEvent me = (MouseEvent)event;
				/** Keep Ctrl 	*/
				if (((me.getKeys() & MouseEvent.CTRL_KEY) > 0)) {
					doOnCtrSelect(event);
				} 
				/** Keep Shift	*/
				else if ((me.getKeys() & MouseEvent.SHIFT_KEY) > 0) {
					doOnShiftSelect(event);
				} 
				/** Click		*/
				else {
					doClick(event);
				}
				Event evt = new Event(Events.ON_CLICK, listBox);
				Events.sendEvent(listBox, evt);
			}
		}

	}
}
