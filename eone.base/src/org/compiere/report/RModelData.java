package org.compiere.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import eone.base.model.MRole;

public class RModelData
{

	public RModelData (String TableName)
	{
		m_TableName = TableName;
	}   //  RModelData

	/** The Rows                        */
	public ArrayList<ArrayList<Object>> 	rows = new ArrayList<ArrayList<Object>>();
	/** The temporary Rows              */
	private ArrayList<ArrayList<Object>>	m_rows = new ArrayList<ArrayList<Object>>();

	/** The Row MetaData                */
	public ArrayList<Object>	rowsMeta = new ArrayList<Object>();
	/** The Column Definitions          */
	public ArrayList<RColumn>	cols = new ArrayList<RColumn>();


	/** Table Name                      */
	private String          m_TableName;

	/** Functions (Integer - String)    */
	public HashMap<Integer,String>	functions = new HashMap<Integer,String>();
	/** Groups (Integer)                */
	public ArrayList<Integer>		groups = new ArrayList<Integer>();

	/** Array with row numbers that are groups  */
	private ArrayList<Integer>		m_groupRows = new ArrayList<Integer>();
	private ArrayList<Boolean>  	m_groupRowsIndicator = null;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(RModelData.class);

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		rows.clear();
		m_rows.clear();
		rowsMeta.clear();
		cols.clear();
	}   //  dispose

	/**************************************************************************
	 *  Query
	 *  @param ctx
	 *  @param  whereClause the SQL where clause (w/o the WHERE)
	 *  @param  orderClause
	 */
	public void query (Properties ctx, String whereClause, String orderClause)
	{
		RColumn rc = null;
		//  Create SQL
		StringBuilder sql = new StringBuilder ("SELECT ");
		int size = cols.size();
		for (int i = 0; i < size; i++)
		{
			rc = cols.get(i);
			if (i > 0)
				sql.append(",");
			sql.append(rc.getColSQL());
		}
		sql.append(" FROM ").append(m_TableName).append(" ").append(RModel.TABLE_ALIAS);
		if (whereClause != null && whereClause.length() > 0)
			sql.append(" WHERE ").append(whereClause);
		String finalSQL = MRole.getDefault(ctx, false).addAccessSQL(
			sql.toString(), RModel.TABLE_ALIAS, MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		if (orderClause != null && orderClause.length() > 0)
			finalSQL += " ORDER BY " + orderClause;
		log.fine(finalSQL);

		//  FillData
		int index = 0;      //  rowset index
		m_rows.clear();
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = DB.createStatement();
			if (DB.isPostgreSQL()) {
				finalSQL = finalSQL.replace("NVL", "COALESCE");
				finalSQL = finalSQL.replace("RowNum", "COUNT(*) OVER ( ORDER BY zz.Fact_Acct_ID)");
			}
			rs = stmt.executeQuery(finalSQL);
			while (rs.next())
			{
				ArrayList<Object> row = new ArrayList<Object>(size);
				index = 1;
				//  Columns
				for (int i = 0; i < size; i++)
				{
					rc = cols.get(i);
					//  Get ID
					if (rc.isIDcol())
						row.add(new KeyNamePair (rs.getInt(index++), rs.getString(index++)));
					//  Null check
					else if (rs.getObject(index) == null)
					{
						index++;
						row.add(null);
					}
					else if (rc.getColClass() == String.class)
						row.add(rs.getString(index++));
					else if (rc.getColClass() == BigDecimal.class)
						row.add(rs.getBigDecimal(index++));
					else if (rc.getColClass() == Double.class)
						row.add(Double.valueOf(rs.getDouble(index++)));
					else if (rc.getColClass() == Integer.class)
						row.add(Integer.valueOf(rs.getInt(index++)));
					else if (rc.getColClass() == Timestamp.class)
						row.add(rs.getTimestamp(index++));
					else if (rc.getColClass() == Boolean.class)
						row.add(Boolean.valueOf("Y".equals(rs.getString(index++))));
					else    //  should not happen
					{
						row.add(rs.getString(index++));
					}
				}
				m_rows.add(row);
			}
		}
		catch (SQLException e)
		{
			if (index == 0)
				log.log(Level.SEVERE, finalSQL, e);
			else
				log.log(Level.SEVERE, "Index=" + index + "," + rc, e);
			e.printStackTrace();
		}
		finally
		{
			DB.close(rs, stmt);
			rs = null;
			stmt = null;
		}
		process();
	}   //  query

	/**
	 *  Process Data
	 *  Copy data in m_rows to rows and perform functions
	 */
	private void process()
	{
		if (log.isLoggable(Level.FINE)) log.fine("Start Rows=" + m_rows.size());

		//  Row level Funcions
		//  would come here

		//  Group by Values
		int gSize = groups.size();
		int[] groupBys = new int[gSize];
		Object[] groupBysValue = new Object[gSize];
		Object INITVALUE = new Object();
		for (int i = 0; i < gSize; i++)
		{
			groupBys[i] = groups.get(i).intValue();
			groupBysValue[i] = INITVALUE;
			if (log.isLoggable(Level.FINE)) log.fine("GroupBy level=" + i + " col=" + groupBys[i]);
		}
		//  Add additional row to force group change
		if (gSize > 0)
		{
			ArrayList<Object> newRow = new ArrayList<Object>();
			for (int c = 0; c < cols.size(); c++)
				newRow.add("");
			m_rows.add(newRow);
		}

		//  Function Values - Function - GroupValue
		int fSize = functions.size();
		int[] funcCols = new int[fSize];
		String[] funcFuns = new String[fSize];
		int index = 0;
		Iterator<Integer> it = functions.keySet().iterator();
		while (it.hasNext())
		{
			Object key = it.next();
			funcCols[index] = ((Integer)key).intValue();
			funcFuns[index] = functions.get(key).toString();
			if (log.isLoggable(Level.FINE)) log.fine("Function " + funcFuns[index] + " col=" + funcCols[index]);
			index++;
		}
		BigDecimal[][] funcVals = new BigDecimal [fSize][gSize+1];
		int totalIndex = gSize;  //  place for overall total
		if (log.isLoggable(Level.FINE)) log.fine("FunctionValues = [ " + fSize + " * " + (gSize+1) + " ]");
		for (int f = 0; f < fSize; f++)
			for (int g = 0; g < gSize+1; g++)
				funcVals[f][g] = Env.ZERO;

		rows.clear();
		//  Copy m_rows into rows
		for (int r = 0; r < m_rows.size(); r++)
		{
			ArrayList<Object> row = m_rows.get(r);
			//  do we have a group break
			boolean[] haveBreak = new boolean[groupBys.length];
			for (int level = 0; level < groupBys.length; level ++)
			{
				int idx = groupBys[level];
				if (groupBysValue[level] == INITVALUE)
					haveBreak[level] = false;
				else if (!groupBysValue[level].equals(row.get(idx)))
					haveBreak[level] = true;
				else
					haveBreak[level] = false;
				//  previous level had a break
				if (level > 0 && haveBreak[level-1])
					haveBreak[level] = true;
			}
			//  create group levels - reverse order
			for (int level = groupBys.length-1; level >= 0; level--)
			{
				int idx = groupBys[level];
				if (groupBysValue[level] == INITVALUE)
					groupBysValue[level] = row.get(idx);
				else if (haveBreak[level])
				{
				//	log.fine( "GroupBy Change level=" + level + " col=" + idx + " - " + groupBysValue[level]);
					//  create new row
					ArrayList<Object> newRow = new ArrayList<Object>();
					for (int c = 0; c < cols.size(); c++)
					{
						if (c == idx)   //  the group column
						{
							if (groupBysValue[c] == null || groupBysValue[c].toString().length() == 0)
								newRow.add("=");
							else
								newRow.add(groupBysValue[c]);
						}
						else
						{
							boolean found = false;
							for (int fc = 0; fc < funcCols.length; fc++)
							{
								if (c == funcCols[fc])
								{
								//	newRow.add("fc= " + fc + " gl=" + level + " " + funcFuns[fc]);
									newRow.add(funcVals[fc][level]);
									funcVals[fc][level] = Env.ZERO;
									found = true;
								}
							}
							if (!found)
								newRow.add(null);
						}
					}   //  for all columns
					//
					m_groupRows.add(Integer.valueOf(rows.size())); //  group row indicator
					rows.add(newRow);
					groupBysValue[level] = row.get(idx);
				}
			}   //  for all groups

			//	functions
			for (int fc = 0; fc < funcCols.length; fc++)
			{
				int col = funcCols[fc];
				//  convert value to big decimal
				Object value = row.get(col);
				BigDecimal bd = Env.ZERO;
				if (value == null)
					;
				else if (value instanceof BigDecimal)
					bd = (BigDecimal)value;
				else
				{
					try {
						bd = new BigDecimal(value.toString());
					} catch (Exception e) { }
				}

				for (int level = 0; level < gSize+1; level++)
				{
					if (funcFuns[fc].equals(RModel.FUNCTION_SUM))
						funcVals[fc][level] = funcVals[fc][level].add(bd);
					else if (funcFuns[fc].equals(RModel.FUNCTION_COUNT))
						funcVals[fc][level] = funcVals[fc][level].add(Env.ONE);
				}   //  for all group levels
			}   //  for all functions

			rows.add(row);
		}   //  for all m_rows

		//  total row
		if (functions.size() > 0)
		{
			ArrayList<Object> newRow = new ArrayList<Object>();
			for (int c = 0; c < cols.size(); c++)
			{
				boolean found = false;
				for (int fc = 0; fc < funcCols.length; fc++)
				{
					if (c == funcCols[fc])
					{
						newRow.add(funcVals[fc][totalIndex]);
						found = true;
					}
				}
				if (!found)
					newRow.add(null);
			}   //  for all columns
			//  remove empty row added earlier to force group change
			if (gSize > 0)
				rows.remove(rows.size()-1);
			m_groupRows.add(Integer.valueOf(rows.size())); //  group row indicator
			rows.add(newRow);
		}
		if (log.isLoggable(Level.FINE)) log.fine("End Rows=" + rows.size());
		m_rows.clear();
	}   //  process


	/**************************************************************************
	 *  Is Row a Group Row
	 *  @param row row index
	 *  @return true, if group row
	 */
	public boolean isGroupRow (int row)
	{
		//  build boolean Array
		if (m_groupRowsIndicator == null)
		{
			m_groupRowsIndicator = new ArrayList<Boolean>(rows.size());
			for (int r = 0; r < rows.size(); r++)
				m_groupRowsIndicator.add(Boolean.valueOf(m_groupRows.contains(Integer.valueOf(r))));
		}
		if (row < 0 || row >= m_groupRowsIndicator.size())
			return false;
		return m_groupRowsIndicator.get(row).booleanValue();
	}   // isGroupRow

	/**
	 *  Move Row
	 *  @param from index
	 *  @param to index
	 *  @throws IllegalArgumentException if row index is invalid
	 */
	public void moveRow (int from, int to)
	{
		if (from < 0 || to >= rows.size())
			throw new IllegalArgumentException("Row from invalid");
		if (to < 0 || to >= rows.size())
			throw new IllegalArgumentException("Row to invalid");
		//  Move Data
		ArrayList<Object> temp = rows.get(from);
		rows.remove(from);
		rows.add(to, temp);
		//  Move Description indicator >>> m_groupRows is not in sync after row move !!
		if (m_groupRowsIndicator != null)
		{
			Boolean tempB = m_groupRowsIndicator.get(from);
			m_groupRowsIndicator.remove(from);
			m_groupRowsIndicator.add(to, tempB);
		}
	}   //  moveRow

	
	@SuppressWarnings("deprecation")
	public void queryGroup (Properties ctx, String whereClause, String orderClause)
	{
		RColumn rc = null;
		//  Create SQL
		StringBuilder sql = new StringBuilder ("SELECT ");
		StringBuilder gSQL = new StringBuilder ("SELECT ");
		StringBuilder groupBy = new StringBuilder();
		int size = cols.size();	
		
	   //  for all group levels
		for (int i = 0; i < size; i++)
		{
			rc = cols.get(i);
			if (i > 0) {
				sql.append(",");
				gSQL.append(",");
			}
			if (rc.isVirtual()) {
				sql.append(rc.getColSQL()).append(" as ").append(rc.getColHeader());
				if (rc.getFunction().equals(RModel.FUNCTION_COUNT)) {
					gSQL.append("COUNT(").append(rc.getColHeader()).append(") as ").append(rc.getColHeader());
				}else if (rc.getFunction().equals(RModel.FUNCTION_SUM)) {
					gSQL.append("SUM(").append(rc.getColHeader()).append(") as ").append(rc.getColHeader());
 				}
			} else {
				String group = null;
				if (!rc.getColumnName().equalsIgnoreCase(rc.getColSQL())) {
					sql.append(rc.getColSQL()).append(" as ").append(rc.getColumnName()).append("_Alias");
					gSQL.append(rc.getColumnName()).append(", ").append(rc.getColumnName()).append("_Alias");
					group = rc.getColumnName() + ", " +  rc.getColumnName() + "_Alias";
					
				} else {
					sql.append(rc.getColSQL());
					if (rc.getFunction().equals(RModel.FUNCTION_COUNT)) {
						gSQL.append("COUNT(").append(rc.getColSQL()).append(") as ").append(rc.getColSQL());
					}else if (rc.getFunction().equals(RModel.FUNCTION_SUM)) {
						gSQL.append("SUM(").append(rc.getColSQL()).append(") as ").append(rc.getColSQL());
	 				} else {
	 					gSQL.append(rc.getColSQL());
	 					group = rc.getColSQL();
	 				}
				}
				// GroupBy
				if (rc.isGroup() && group !=null) {
					if (groupBy.length() > 0)
						groupBy.append(", ").append(group);
					else 
						groupBy.append(group);
				}
			}
		}
		//  Add function column
		
		sql.append(" FROM ").append(m_TableName).append(" ").append(RModel.TABLE_ALIAS);
		if (whereClause != null && whereClause.length() > 0)
			sql.append(" WHERE ").append(whereClause);
		String finalSQL = MRole.getDefault(ctx, false).addAccessSQL(
			sql.toString(), RModel.TABLE_ALIAS, MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		log.fine(finalSQL);

		// Group SQL 
		
		gSQL.append(" FROM (").append(finalSQL).append(")B GROUP BY ").append(groupBy.toString());
		//  FillData
		int index = 0;      //  rowset index
		m_rows.clear();
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = DB.createStatement();
			
			rs = stmt.executeQuery(gSQL.toString());
			while (rs.next())
			{
				ArrayList<Object> row = new ArrayList<Object>(size);
				index = 1;
				//  Columns
				for (int i = 0; i < size; i++)
				{
					rc = cols.get(i);
					//  Get ID
					if (rc.isIDcol())
						row.add(new KeyNamePair (rs.getInt(index++), rs.getString(index++)));
					//  Null check
					else if (rs.getObject(index) == null)
					{
						index++;
						row.add(null);
					}
					else if (rc.getColClass() == String.class)
						row.add(rs.getString(index++));
					else if (rc.getColClass() == BigDecimal.class)
						row.add(rs.getBigDecimal(index++));
					else if (rc.getColClass() == Double.class)
						row.add(new Double(rs.getDouble(index++)));
					else if (rc.getColClass() == Integer.class)
						row.add(new Integer(rs.getInt(index++)));
					else if (rc.getColClass() == Timestamp.class)
						row.add(rs.getTimestamp(index++));
					else if (rc.getColClass() == Boolean.class)
						row.add(new Boolean("Y".equals(rs.getString(index++))));
					else    //  should not happen
					{
						row.add(rs.getString(index++));
					}
				}
				m_rows.add(row);
			}
		}
		catch (SQLException e)
		{
			if (index == 0)
				log.log(Level.SEVERE, finalSQL, e);
			else
				log.log(Level.SEVERE, "Index=" + index + "," + rc, e);
			e.printStackTrace();
		}
		finally
		{
			DB.close(rs, stmt);
			rs = null;
			stmt = null;
		}
		process();
		
	}
}   //  RModelData
