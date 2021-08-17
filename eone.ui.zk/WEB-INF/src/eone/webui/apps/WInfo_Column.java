package eone.webui.apps;

public class WInfo_Column
{
	public WInfo_Column (String colHeader, String colSQL, Class<?> colClass)
	{
		this(colHeader, colSQL, colClass, true, false, null);
	}   //  Info_Column

	public WInfo_Column (String colHeader, String colSQL, Class<?> colClass, String IDcolSQL)
	{
		this(colHeader, colSQL, colClass, true, false, IDcolSQL);
	}   //  Info_Column

	public WInfo_Column (String colHeader, String colSQL, Class<?> colClass, 
		boolean readOnly, boolean colorColumn, String IDcolSQL)
	{
		setColHeader(colHeader);
		setColSQL(colSQL);
		setColClass(colClass);
		setReadOnly(readOnly);
		setColorColumn(colorColumn);
		setIDcolSQL(IDcolSQL);
	}   //  Info_Column
	
	
	public WInfo_Column (String colHeader, String colTooltiptext, String colSQL, Class<?> colClass, 
		boolean readOnly, boolean colorColumn, String IDcolSQL)
	{
		setColHeader(colHeader);
		setColTooltiptext(colTooltiptext);
		setColSQL(colSQL);
		setColClass(colClass);
		setReadOnly(readOnly);
		setColorColumn(colorColumn);
		setIDcolSQL(IDcolSQL);
	}   //  Info_Column


	private String      m_colTooltiptext = "";
	private String      m_colHeader;
	private String      m_colSQL;
	private Class<?>       m_colClass;
	private boolean     m_readOnly;
	private boolean     m_colorColumn;
	private String      m_IDcolSQL = "";

	public Class<?> getColClass()
	{
		return m_colClass;
	}
	public String getColTooltiptext()
	{
		return m_colTooltiptext;
	}
	public String getColHeader()
	{
		return m_colHeader;
	}
	public String getColSQL()
	{
		return m_colSQL;
	}
	public boolean isReadOnly()
	{
		return m_readOnly;
	}
	public void setColClass(Class<?> colClass)
	{
		m_colClass = colClass;
	}
	public void setColTooltiptext(String colTooltiptext)
	{
		m_colTooltiptext = colTooltiptext;
		if (colTooltiptext != null)
		{
			int index = colTooltiptext.indexOf('&');
			if (index != -1)
				m_colHeader = colTooltiptext.substring(0, index) + colTooltiptext.substring(index+1); 
		}
	}
	
	public void setColHeader(String colHeader)
	{
		m_colHeader = colHeader;
		if (colHeader != null)
		{
			int index = colHeader.indexOf('&');
			if (index != -1)
				m_colHeader = colHeader.substring(0, index) + colHeader.substring(index+1); 
		}
	}
	
	public void setColSQL(String colSQL)
	{
		m_colSQL = colSQL;
	}
	public void setReadOnly(boolean readOnly)
	{
		m_readOnly = readOnly;
	}
	public void setColorColumn(boolean colorColumn)
	{
		m_colorColumn = colorColumn;
	}
	public boolean isColorColumn()
	{
		return m_colorColumn;
	}
	
	public void setIDcolSQL(String IDcolSQL)
	{
		m_IDcolSQL = IDcolSQL;
		if (m_IDcolSQL == null)
			m_IDcolSQL = "";
	}
	public String getIDcolSQL()
	{
		return m_IDcolSQL;
	}
	public boolean isIDcol()
	{
		return m_IDcolSQL.length() > 0;
	}
}   //  infoColumn
