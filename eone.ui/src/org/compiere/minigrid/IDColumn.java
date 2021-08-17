package org.compiere.minigrid;


public class IDColumn
{
	
	public IDColumn (int record_ID)
	{
		this(Integer.valueOf(record_ID));
	}   //  IDColumn

	
	public IDColumn(Integer record_ID)
	{
		super();
		setRecord_ID(record_ID);
		setSelected(true);
	}   //  IDColumn

	/** Is the row selected         */
	private boolean     m_selected = false;
	/** The Record_ID               */
	private Integer     m_record_ID;


	
	public void setSelected(boolean selected)
	{
		m_selected = selected;
	}
	
	public boolean isSelected()
	{
		return m_selected;
	}

	
	public void setRecord_ID(Integer record_ID)
	{
		m_record_ID = record_ID;
	}
	
	public Integer getRecord_ID()
	{
		return m_record_ID;
	}

	
	public String toString()
	{
		return "IDColumn - ID=" + m_record_ID + ", Selected=" + m_selected;
	}   //  toString

}   //  IDColumn
