
package org.compiere.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.compiere.dbPort.Convert;
import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.model.PO;


public interface AdempiereDatabase
{
	
	static final int LOCK_TIME_OUT = 60;
	
	public String getName();

	public String getDescription();

	public Driver getDriver() throws SQLException;


	public int getStandardPort();

	public String getConnectionURL (CConnection connection);
	
	public String getConnectionURL (String dbHost, int dbPort, String dbName,
		String userName);

	public String getConnectionURL (String connectionURL, String userName);

	public String getCatalog();
	
	public String getSchema();

	public boolean supportsBLOB();

	public String toString();

	
	public String convertStatement (String oraStatement);

	public boolean isSupported(String sql);

	public String getConstraintType(Connection conn, String tableName, String IXName);
	
	public String getAlternativeSQL(int reExNo, String msg, String sql);

	public String getSystemUser();
	
	public String getSystemDatabase(String databaseName);
	
	public String TO_DATE (Timestamp time, boolean dayOnly);

	public String TO_CHAR (String columnName, int displayType, String AD_Language);

	public String TO_NUMBER (BigDecimal number, int displayType);
	
	public int getNextID(String Name, String trxName);
	
	public int getNextID(String Name);
	
	public boolean createSequence(String name , int increment , int minvalue , int maxvalue ,int  start, String trxName);
	
	
	/** Create User commands					*/
	public static final int		CMD_CREATE_USER = 0;
	/** Create Database/Schema Commands			*/
	public static final int		CMD_CREATE_DATABASE = 1;
	/** Drop Database/Schema Commands			*/
	public static final int		CMD_DROP_DATABASE = 2;
	
	public String[] getCommands (int cmdType);

	public Connection getCachedConnection (CConnection connection, 
		boolean autoCommit, int transactionIsolation) throws Exception;

	public Connection getDriverConnection (CConnection connection) throws SQLException;

	public Connection getDriverConnection (String dbUrl, String dbUid, String dbPwd) 
		throws SQLException;

	public DataSource getDataSource(CConnection connection);

	public String getStatus();

	public void close();
	
	public Convert getConvert();

	public boolean isQueryTimeoutSupported();

	public boolean isPagingSupported();

	public String addPagingSQL(String sql, int start, int end);
	
	public boolean forUpdate(PO po, int timeout);
	
	public String getNameOfUniqueConstraintError(Exception e);

	public String subsetClauseForCSV(String columnName, String csv);
	
	public String intersectClauseForCSV(String columnName, String csv);
	
	public default String quoteColumnName(String columnName) {
		return columnName;
	}
	
	public default boolean isNativeMode() {
		return true;
	}
	
	public String getNumericDataType();
	
	public String getCharacterDataType();
	
	public String getVarcharDataType();
	
	public String getBlobDataType();
	
	public String getClobDataType();
	
	public String getTimestampDataType();
	
	public default String getSQLCreate(MTable table)
	{
		StringBuilder sb = new StringBuilder("CREATE TABLE ")
			.append(table.getTableName()).append(" (");
		//
		StringBuilder constraints = new StringBuilder();
		MColumn[] columns = table.getColumns(true);
		boolean columnAdded = false;
		for (int i = 0; i < columns.length; i++)
		{
			MColumn column = columns[i];
			String colSQL = column.getSQLDDL();
			if ( colSQL != null )
			{
				if (columnAdded)
					sb.append(", ");
				else
					columnAdded = true;
				sb.append(column.getSQLDDL());
			}
			else // virtual column
				continue;
			//
			String constraint = column.getConstraint(table.getTableName());
			if (constraint != null && constraint.length() > 0)
				constraints.append(", ").append(constraint);
		}

		sb.append(constraints)
			.append(")");
		return sb.toString();
	}	//	getSQLCreate
	
	public String getSQLDDL(MColumn column);
	
	public String getSQLAdd(MTable table, MColumn column);
	
	public String getSQLModify (MTable table, MColumn column, boolean setNullOption);
		
}   //  AdempiereDatabase

