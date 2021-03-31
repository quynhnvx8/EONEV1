package eone.base.process;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.logging.Level;

import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.ValueNamePair;


public class ColumnSync extends SvrProcess
{
	/** The Column				*/
	private int			p_AD_Column_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_Column_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info("C_Column_ID=" + p_AD_Column_ID);
		if (p_AD_Column_ID == 0)
			throw new AdempiereUserError("@No@ @AD_Column_ID@");
		MColumn column = new MColumn (getCtx(), p_AD_Column_ID, get_TrxName());
		if (column.get_ID() == 0)
			throw new AdempiereUserError("@NotFound@ @AD_Column_ID@ " + p_AD_Column_ID);
		
		MTable table = new MTable(getCtx(), column.getAD_Table_ID(), get_TrxName());
		if (table.get_ID() == 0)
			throw new AdempiereUserError("@NotFound@ @AD_Table_ID@ " + column.getAD_Table_ID());
		
		//	Find Column in Database
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DB.getConnectionRO();
			DatabaseMetaData md = conn.getMetaData();
			String catalog = DB.getDatabase().getCatalog();
			String schema = DB.getDatabase().getSchema();
			String tableName = table.getTableName();
			if (md.storesUpperCaseIdentifiers())
			{
				tableName = tableName.toUpperCase();
			}
			else if (md.storesLowerCaseIdentifiers())
			{
				tableName = tableName.toLowerCase();
			}
			int noColumns = 0;
			String sql = null;
			//
			rs = md.getColumns(catalog, schema, tableName, null);
			while (rs.next())
			{
				noColumns++;
				String columnName = rs.getString ("COLUMN_NAME");
				if (!columnName.equalsIgnoreCase(column.getColumnName()))
					continue;
				
				//	update existing column
				boolean notNull = DatabaseMetaData.columnNoNulls == rs.getInt("NULLABLE");
				sql = column.getSQLModify(table, column.isMandatory() != notNull);
				if (DB.isOracle()) {
					int actualType = rs.getInt("DATA_TYPE");
					if (actualType == Types.CLOB) {
						if (sql.contains(" MODIFY " + column.getColumnName() + " CLOB")) {
							sql = sql.replaceFirst(" MODIFY " + column.getColumnName() + " CLOB", " MODIFY " + column.getColumnName());
						}
					} else if (actualType == Types.BLOB) {
						if (sql.contains(" MODIFY " + column.getColumnName() + " BLOB")) {
							sql = sql.replaceFirst(" MODIFY " + column.getColumnName() + " BLOB", " MODIFY " + column.getColumnName());
						}
					}
				}
				break;
			}
			DB.close(rs);
			rs = null;
		
			boolean isNoTable = noColumns == 0;
			//	No Table
			if (isNoTable)
				sql = table.getSQLCreate ();
			//	No existing column
			else if (sql == null)
				sql = column.getSQLAdd(table);
			
			int no = 0;
			if (sql.indexOf(DB.SQLSTATEMENT_SEPARATOR) == -1)
			{
				no = DB.executeUpdate(sql, false, get_TrxName());
				addLog (0, null, new BigDecimal(no), sql);
			}
			else
			{
				String statements[] = sql.split(DB.SQLSTATEMENT_SEPARATOR);
				for (int i = 0; i < statements.length; i++)
				{
					int count = DB.executeUpdate(statements[i], get_TrxName());
					addLog (0, null, new BigDecimal(count), statements[i]);
					no += count;
				}
			}
	
			if (no == -1)
			{
				StringBuilder msg = new StringBuilder("@Error@ ");
				ValueNamePair pp = CLogger.retrieveError();
				if (pp != null)
					msg = new StringBuilder(pp.getName()).append(" - ");
				msg.append(sql);
				throw new AdempiereUserError (msg.toString());
			}
			return sql;
		} finally {
			DB.close(rs);
			rs = null;
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {}
			}
		}
	}	//	doIt
	
}	//	ColumnSync
