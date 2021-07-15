package org.compiere.print;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.NamePair;

/**
 *	Quynhnv.x8. Add 06/10/2020
 *
 */
public class PrintDataItem implements Serializable
{
	private static final long serialVersionUID = -2121125127301364735L;

	public PrintDataItem (String columnName, Serializable value, int displayType, String format, String zoomLogic, 
			int alignment, String setupFormular,
			boolean isGroup, boolean isCountG, String tableLink,
			int rotation, String fieldSumGroup, int rowSpan, int colSpan, boolean breakPage)
	{
		if (columnName == null)
			throw new IllegalArgumentException("PrintDataElement - Name cannot be null");
		m_columnName = columnName;
		m_value = value;
		m_displayType = displayType;
		m_formatPattern = format;
		m_ZoomLogic = zoomLogic;
		m_Alignment = alignment;
		m_setupFormular = setupFormular;
		setGroup(isGroup);
		setCountG(isCountG);
		setTableLink(tableLink);
		setRotation(rotation);
		setFieldSumGroup(fieldSumGroup);
		setRowSpan(rowSpan);
		setColSpan(colSpan);
		setBreakPage(breakPage);
		
	}	

	

	private String 		m_columnName;
	private Serializable 	m_value;
	private int 		m_displayType;
	private String		m_formatPattern;
	private String m_ZoomLogic;
	private int m_Alignment;
	private String m_setupFormular = "";
	private boolean isGroup = false;
	private boolean isCountG = false;
	private String tableLink = "";
	private int rotation = 0;
	private String fieldSumGroup = "";
	private int rowSpan = 0;
	private int colSpan = 0;
	private boolean isBreakPage = false;
	
	public boolean isBreakPage() {
		return isBreakPage;
	}

	public void setBreakPage(boolean isBreakPage) {
		this.isBreakPage = isBreakPage;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public String getFieldSumGroup() {
		return fieldSumGroup;
	}

	public void setFieldSumGroup(String fieldSumGroup) {
		this.fieldSumGroup = fieldSumGroup;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public boolean isGroup() {
		return isGroup;
	}


	public String getTableLink() {
		return tableLink;
	}

	public void setTableLink(String tableLink) {
		this.tableLink = tableLink;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}


	public boolean isCountG() {
		return isCountG;
	}

	public void setCountG(boolean isCountG) {
		this.isCountG = isCountG;
	}

	public String getSetupFormular() {
		return m_setupFormular;
	}

	public void setSetupFormular(String setupFormular) {
		this.m_setupFormular = setupFormular;
	}

	public int getAlignment() {
		return m_Alignment;
	}

	public void setAlignment(int m_Alignment) {
		this.m_Alignment = m_Alignment;
	}

	public String getZoomLogic() {
		return m_ZoomLogic;
	}

	public void setZoomLogic(String m_ZoomLogic) {
		this.m_ZoomLogic = m_ZoomLogic;
	}

	public String getColumnName()
	{
		return m_columnName;
	}	//	getName
	
	
	public Object getValue()
	{
		return m_value;
	}	//	getValue

	public void setValue(Serializable value) {
		m_value = value;
	}
	
	public String getValueDisplay (Language language)
	{
		if (m_value == null)
			return "";
		String retValue = m_value.toString();
		if (DisplayType.ID == m_displayType && m_value instanceof KeyNamePair)
			return ((KeyNamePair)m_value).getID();
		else if (m_displayType == 0 || m_value instanceof String || m_value instanceof NamePair)
			;
		else if (language != null)	//	Optional formatting of Numbers and Dates
		{
			if (DisplayType.isNumeric(m_displayType)) {
				retValue = DisplayType.getNumberFormat(m_displayType, language, m_formatPattern).format(m_value);
				if ("0".equals(retValue))
					retValue = null;
			} else if (DisplayType.isDate(m_displayType)) {
				retValue = DisplayType.getDateFormat(m_displayType, language, m_formatPattern).format(m_value);
			} else if (m_value instanceof Boolean) {
				if (m_value.toString().equals("true")) {
					retValue = Msg.getMsg(Env.getCtx(), "Yes");
				} else if (m_value.toString().equals("false")) {
					retValue = Msg.getMsg(Env.getCtx(), "No");
				}
			}
		}
		return retValue;
	}	//	getValueDisplay


	public String getValueAsString()
	{
		if (m_value == null)
			return "";
		String retValue = m_value.toString();
		if (m_value instanceof NamePair)
			retValue = ((NamePair)m_value).getID();
		return retValue;
	}	//	getValueDisplay

	

	public String getValueKey()
	{
		if (m_value == null)
			return "";
		if (m_value instanceof NamePair)
			return ((NamePair)m_value).getID();
		return "";
	}	//	getValueKey


	public boolean isNull()
	{
		return m_value == null;
	}	//	isNull


	public int getDisplayType()
	{
		return m_displayType;
	}	//	getDisplayType


	public boolean isNumeric()
	{
		if (m_displayType == 0)
			return m_value instanceof BigDecimal;
		return DisplayType.isNumeric(m_displayType);
	}	//	isNumeric


	public boolean isZoomLogic() {
		if (m_ZoomLogic != null && !m_ZoomLogic.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean isDate()
	{
		if (m_displayType == 0)
			return m_value instanceof Timestamp;
		return DisplayType.isDate(m_displayType);
	}	//	isDate

	public String getFormatPattern() {
		return m_formatPattern;
	}

	public void setFormatPattern(String pattern) {
		m_formatPattern = pattern;
	}

}	
