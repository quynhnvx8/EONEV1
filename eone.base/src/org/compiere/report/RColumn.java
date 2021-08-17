
package org.compiere.report;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;

import eone.base.model.MLookupFactory;


public class RColumn
{
	/**
	 * 	Create Report Column
	 *	@param ctx context 
	 *	@param columnName column name
	 *	@param displayType display type
	 */
	public RColumn (Properties ctx, String columnName, int displayType)
	{
		this (ctx, columnName, displayType, null, 0, null);
	}	//	RColumn

	
	/**
	 *  Create Report Column
	 *	@param ctx context 
	 *	@param columnName column name
	 *	@param displayType display type
	 *	@param sql sql (if null then columnName is used). 
	 *	Will be overwritten if TableDir or Search 
	 */
	public RColumn (Properties ctx, String columnName, int displayType, String sql)
	{
		this (ctx, columnName, displayType, sql, 0, null);
	}
	/**
	 *  Create Report Column
	 *	@param ctx context 
	 *	@param columnName column name
	 *	@param displayType display type
	 *	@param sql sql (if null then columnName is used). 
	 *	@param AD_Reference_Value_ID List/Table Reference
	 *	@param refColumnName UserReference column name
	 *	Will be overwritten if TableDir or Search 
	 */
	public RColumn (Properties ctx, String columnName, int displayType, 
		String sql,	int AD_Reference_Value_ID, String refColumnName)
	{
		m_columnName = columnName;
		m_colHeader = Msg.translate(ctx, columnName);
		if (refColumnName != null)
			m_colHeader = Msg.translate(ctx, refColumnName);
		m_displayType = displayType;
		m_colSQL = sql;
		if (m_colSQL == null || m_colSQL.length() == 0)
			m_colSQL = columnName;

		//  Strings
		if (DisplayType.isText(displayType))
			m_colClass = String.class;                  //  default size=30
		//  Amounts
		else if (displayType == DisplayType.Amount)
		{
			m_colClass = BigDecimal.class;
			m_colSize = 70;
		}
		//  Boolean
		else if (displayType == DisplayType.YesNo)
			m_colClass = Boolean.class;
		//  Date
		else if (DisplayType.isDate(displayType))
			m_colClass = Timestamp.class;
		//  Number
		else if (displayType == DisplayType.Quantity 
			|| displayType == DisplayType.Number  
			|| displayType == DisplayType.CostPrice)
		{
			m_colClass = Double.class;
			m_colSize = 70;
		}
		//  Integer
		else if (displayType == DisplayType.Integer)
			m_colClass = Integer.class;
		//  List
		else if (displayType == DisplayType.List)
		{
			Language language = Language.getLanguage(Env.getAD_Language(ctx));
			m_colSQL = "(" + MLookupFactory.getLookup_ListEmbed(
				language, AD_Reference_Value_ID, columnName) + ")";
			m_displaySQL = m_colSQL;
			m_colClass = String.class;
			m_isIDcol = false;
		}
		else if (displayType == DisplayType.ID) {
			m_colClass = Integer.class;
		}
		
		else
		{
			m_colClass = String.class;
			Language language = Language.getLanguage(Env.getAD_Language(ctx));
			if (columnName.equals("Account_Dr_ID") || columnName.equals("Account_Cr_ID"))
			{
				m_displaySQL = "(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "C_ElementValue_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_colSQL += "," + m_displaySQL;
				m_isIDcol = true;
			}
			else if (columnName.startsWith("C_BPartner"))
			{
				m_displaySQL = "(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "C_BPartner_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_colSQL += "," + m_displaySQL;
				m_isIDcol = true;
			}
			else if (columnName.startsWith("M_Warehouse"))
			{
				m_displaySQL = "(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "M_Warehouse_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_colSQL += "," + m_displaySQL;
				m_isIDcol = true;
			}
			else if (columnName.startsWith("C_Contract"))
			{
				m_displaySQL = "(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "C_Contract_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_colSQL += "," + m_displaySQL;
				m_isIDcol = true;
			}
			
			else if (columnName.equals("CreatedBy") || columnName.equals("UpdatedBy"))
			{
				m_displaySQL = "(" + MLookupFactory.getLookup_TableDirEmbed(
					language, "AD_User_ID", RModel.TABLE_ALIAS, columnName) + ")";
				m_colSQL += "," + m_displaySQL;
				m_isIDcol = true;
			}
			else if (displayType == DisplayType.TableDir)
			{
				m_displaySQL = "(" + MLookupFactory.getLookup_TableDirEmbed(
					language, columnName, RModel.TABLE_ALIAS) + ")";
				m_colSQL += "," + m_displaySQL;
				m_isIDcol = true;
			}
		}
	}   //  RColumn

	/**
	 *  Create Info Column (r/o and not color column)
	 *
	 *  @param colHeader Column Header
	 *  @param colSQL    SQL select code for column
	 *  @param colClass  class of column - determines display
	 */
	public RColumn (String colHeader, String colSQL, Class<?> colClass)
	{
		m_colHeader = colHeader;
		m_colSQL = colSQL;
		m_colClass = colClass;
	}   //  RColumn


	/** Column Name                 */
	private String		m_columnName;
	/** Column Header               */
	private String      m_colHeader;
	/** Column SQL                  */
	private String      m_colSQL;
	/** Column Display SQL          */
	private String      m_displaySQL;
	/** Column Display Class        */
	private Class<?>       m_colClass;

	/** Display Type                */
	private int         m_displayType = 0;
	/** Column Size im px           */
	private int         m_colSize = 30;

	private boolean     m_readOnly = true;
	private boolean     m_colorColumn = false;
	private boolean     m_isIDcol = false;


	/**
	 * @return Column Name
	 */
	public String getColumnName() {
		return m_columnName;
	}
	/**
	 *  Column Header
	 */
	public String getColHeader()
	{
		return m_colHeader;
	}
	public void setColHeader(String colHeader)
	{
		m_colHeader = colHeader;
	}

	/**
	 *  Column SQL
	 */
	public String getColSQL()
	{
		return m_colSQL;
	}
	public void setColSQL(String colSQL)
	{
		m_colSQL = colSQL;
	}
	/**
	 *  Column Display SQL
	 */
	public String getDisplaySQL()
	{
		if (m_displaySQL == null)
			return m_columnName;
		return m_displaySQL;
	}
	/**
	 *  This column is an ID Column (i.e. two values - int & String are read)
	 */
	public boolean isIDcol()
	{
		return m_isIDcol;
	}

	/**
	 *  Column Display Class
	 */
	public Class<?> getColClass()
	{
		return m_colClass;
	}
	public void setColClass(Class<?> colClass)
	{
		m_colClass = colClass;
	}

	/**
	 *  Column Size in px
	 */
	public int getColSize()
	{
		return m_colSize;
	}   //  getColumnSize

	/**
	 *  Column Size in px
	 */
	public void setColSize(int colSize)
	{
		m_colSize = colSize;
	}   //  getColumnSize

	/**
	 *  Get DisplayType
	 */
	public int getDisplayType()
	{
		return m_displayType;
	}   //  getDisplayType

	/**
	 *  Column is Readonly
	 */
	public boolean isReadOnly()
	{
		return m_readOnly;
	}
	public void setReadOnly(boolean readOnly)
	{
		m_readOnly = readOnly;
	}

	/**
	 *  This Color Determines the Color of the row
	 */
	public void setColorColumn(boolean colorColumn)
	{
		m_colorColumn = colorColumn;
	}
	public boolean isColorColumn()
	{
		return m_colorColumn;
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("RColumn[");
		sb.append(m_colSQL).append("=").append(m_colHeader)
			.append("]");
		return sb.toString();
	}	//	toString

	private boolean 	m_isVirtual = false;
	private boolean 	m_isGroup	= false;
	private String		m_function	= "";  
	
	public void setVirual()
	{
		m_isVirtual = true;
	}
	
	public boolean isVirtual()
	{
		return m_isVirtual;
	}
	
	public void setGroup()
	{
		m_isGroup = true;
	}
	
	public boolean isGroup() {
		return m_isGroup;
	}
	
	public void setFunction(String functionName) // SUM, COUNT
	{
		m_function = functionName;
	}
	
	public String getFunction()
	{
		return m_function;
	}
}   //  RColumn
