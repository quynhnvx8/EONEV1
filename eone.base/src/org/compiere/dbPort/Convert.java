package org.compiere.dbPort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.compiere.db.Database;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Ini;


public abstract class Convert
{

	/** RegEx: insensitive and dot to include line end characters   */
	public static final int         REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;

	/** Statement used                  */
	protected Statement               m_stmt = null;

	/** Last Conversion Error           */
	protected String                  m_conversionError = null;
	/** Last Execution Error            */
	protected Exception               m_exception = null;
	/** Verbose Messages                */
	protected boolean                 m_verbose = true;

	/**	Logger	*/
	private static final CLogger	log	= CLogger.getCLogger (Convert.class);
	
    private static FileOutputStream tempFileOr = null;
    private static Writer writerOr;
    private static FileOutputStream tempFilePg = null;
    private static Writer writerPg;

    /**
	 *  Set Verbose
	 *  @param verbose
	 */
	public void setVerbose (boolean verbose)
	{
		m_verbose = verbose;
	}   //  setVerbose

	/**************************************************************************
	 *  Execute SQL Statement (stops at first error).
	 *  If an error occured hadError() returns true.
	 *  You can get details via getConversionError() or getException()
	 *  @param sqlStatements
	 *  @param conn connection
	 *  @return true if success
	 *  @throws IllegalStateException if no connection
	 */
	public boolean execute (String sqlStatements, Connection conn)
	{
		if (conn == null)
			throw new IllegalStateException ("Require connection");
		//
		String[] sql = convert (sqlStatements);
		m_exception = null;
		if (m_conversionError != null || sql == null)
			return false;

		boolean ok = true;
		int i = 0;
		String statement = null;
		try
		{
			if (m_stmt == null)
				m_stmt = conn.createStatement();
			//
			for (i = 0; ok && i < sql.length; i++)
			{
				statement = sql[i];
				if (statement.length() == 0)
				{
					if (m_verbose)
						if (log.isLoggable(Level.FINER)) log.finer("Skipping empty (" + i + ")");
				}
				else
				{
					if (m_verbose) {
						if (log.isLoggable(Level.INFO)) log.info("Executing (" + i + ") <<" + statement + ">>");
					} else {
						if (log.isLoggable(Level.INFO)) log.info("Executing " + i);
					}
					try
					{
						m_stmt.clearWarnings();
						int no = m_stmt.executeUpdate(statement);
						SQLWarning warn = m_stmt.getWarnings();
						if (warn != null)
						{
							if (m_verbose) {
								if (log.isLoggable(Level.INFO)) log.info("- " + warn);
							} else {
								if (log.isLoggable(Level.INFO)) {
									log.info("Executing (" + i + ") <<" + statement + ">>");
									log.info("- " + warn);
								}
							}
						}
						if (m_verbose)
							if (log.isLoggable(Level.FINE)) log.fine("- ok " + no);
					}
					catch (SQLException ex)
					{
						//  Ignore Drop Errors
						if (!statement.startsWith("DROP "))
						{
							ok = false;
							m_exception = ex;
						}
						if (!m_verbose)
							if (log.isLoggable(Level.INFO)) log.info("Executing (" + i + ") <<" + statement + ">>");
						if (log.isLoggable(Level.INFO)) log.info("Error executing " + i + "/" + sql.length + " = " + ex);
					}
				}
			}   //  for all statements
		}
		catch (SQLException e)
		{
			m_exception = e;
			if (!m_verbose)
				if (log.isLoggable(Level.INFO)) log.info("Executing (" + i + ") <<" + statement + ">>");
			if (log.isLoggable(Level.INFO)) log.info("Error executing " + i + "/" + sql.length + " = " + e);
			return false;
		}
		return ok;
	}   //  execute

	/**
	 *  Return last execution exception
	 *  @return execution exception
	 */
	public Exception getException()
	{
		return m_exception;
	}   //  getException

	/**
	 *  Returns true if a conversion or execution error had occured.
	 *  Get more details via getConversionError() or getException()
	 *  @return true if error had occured
	 */
	public boolean hasError()
	{
		return (m_exception != null) | (m_conversionError != null);
	}   //  hasError

	/**
	 *  Convert SQL Statement (stops at first error).
	 *  Statements are delimited by /
	 *  If an error occured hadError() returns true.
	 *  You can get details via getConversionError()
	 *  @param sqlStatements
	 *  @return converted statement as a string
	 */
	public String convertAll (String sqlStatements)
	{
		String[] sql = convert (sqlStatements);
		StringBuilder sb = new StringBuilder (sqlStatements.length() + 10);
		for (int i = 0; i < sql.length; i++)
		{
			//  line.separator
			sb.append(sql[i]).append("\n/\n");
			if (m_verbose)
				if (log.isLoggable(Level.INFO)) log.info("Statement " + i + ": " + sql[i]);
		}
		return sb.toString();
	}   //  convertAll

	/**
	 *  Convert SQL Statement (stops at first error).
	 *  If an error occured hadError() returns true.
	 *  You can get details via getConversionError()
	 *  @param sqlStatements
	 *  @return Array of converted Statements
	 */
	public String[] convert (String sqlStatements)
	{
		m_conversionError = null;
		if (sqlStatements == null || sqlStatements.length() == 0)
		{
			m_conversionError = "SQL_Statement is null or has zero length";
			log.info(m_conversionError);
			return null;
		}
		//
		return convertIt (sqlStatements);
	}   //  convert

	/**
	 *  Return last conversion error or null.
	 *  @return lst conversion error
	 */
	public String getConversionError()
	{
		return m_conversionError;
	}   //  getConversionError

	
	/**************************************************************************
	 *  Conversion routine (stops at first error).
	 *  <pre>
	 *  - convertStatement
	 *      - convertWithConvertMap
	 *      - convertComplexStatement
	 *      - decode, sequence, exception
	 *  </pre>
	 *  @param sqlStatements
	 *  @return array of converted statements
	 */
	protected String[] convertIt (String sqlStatements)
	{
		ArrayList<String> result = new ArrayList<String> ();
		result.addAll(convertStatement(sqlStatements));     //  may return more than one target statement
		
		//  convert to array
		String[] sql = new String[result.size()];
		result.toArray(sql);
		return sql;
	}   //  convertIt

	/**
	 * Clean up Statement. Remove trailing spaces, carrige return and tab 
	 * 
	 * @param statement
	 * @return sql statement
	 */
	protected String cleanUpStatement(String statement) {
		String clean = statement.trim();

		// Convert cr/lf/tab to single space
		Matcher m = Pattern.compile("\\s+").matcher(clean);
		clean = m.replaceAll(" ");

		clean = clean.trim();
		return clean;
	} // removeComments
	
	/**
	 * Utility method to replace quoted string with a predefined marker
	 * @param retValue
	 * @param retVars
	 * @return string
	 */
	protected String replaceQuotedStrings(String inputValue, Vector<String>retVars) {
		// save every value  
		// Carlos Ruiz - globalqss - better matching regexp
		retVars.clear();
		
		// First we need to replace double quotes to not be matched by regexp - Teo Sarca BF [3137355 ]
		final String quoteMarker = "<--QUOTE"+System.currentTimeMillis()+"-->";
		inputValue = inputValue.replace("''", quoteMarker);
		
		Pattern p = Pattern.compile("'[[^']*]*'");
		Matcher m = p.matcher(inputValue);
		int i = 0;
		StringBuilder retValue = new StringBuilder(inputValue.length());
		while (m.find()) {
			String var = inputValue.substring(m.start(), m.end()).replace(quoteMarker, "''"); // Put back quotes, if any
			retVars.addElement(var);
			m.appendReplacement(retValue, "<--" + i + "-->");
			i++;
		}
		m.appendTail(retValue);
		return retValue.toString()
			.replace(quoteMarker, "''") // Put back quotes, if any
		;
	}

	/**
	 * Utility method to recover quoted string store in retVars
	 * @param retValue
	 * @param retVars
	 * @return string
	 */
	protected String recoverQuotedStrings(String retValue, Vector<String>retVars) {
		for (int i = 0; i < retVars.size(); i++) {
			//hengsin, special character in replacement can cause exception
			String replacement = (String) retVars.get(i);
			replacement = escapeQuotedString(replacement);
			retValue = retValue.replace("<--" + i + "-->", replacement);
		}
		return retValue;
	}
	
	/**
	 * hook for database specific escape of quoted string ( if needed )
	 * @param in
	 * @return string
	 */
	protected String escapeQuotedString(String in)
	{
		return in;
	}
	
	/**
	 * Convert simple SQL Statement. Based on ConvertMap
	 * 
	 * @param sqlStatement
	 * @return converted Statement
	 */
	private String applyConvertMap(String sqlStatement) {
		// Error Checks
		if (sqlStatement.toUpperCase().indexOf("EXCEPTION WHEN") != -1) {
			String error = "Exception clause needs to be converted: "
					+ sqlStatement;
			log.info(error);
			m_conversionError = error;
			return sqlStatement;
		}

		// Carlos Ruiz - globalqss
		// Standard Statement -- change the keys in ConvertMap
		
		String retValue = sqlStatement;

		Pattern p;
		Matcher m;

		// for each iteration in the conversion map
		Map<String,String> convertMap = getConvertMap();
		if (convertMap != null) {
			Iterator<?> iter = convertMap.keySet().iterator();
			while (iter.hasNext()) {
	
			    // replace the key on convertmap (i.e.: number by numeric)   
				String regex = (String) iter.next();
				String replacement = (String) convertMap.get(regex);
				try {
					p = Pattern.compile(regex, REGEX_FLAGS);
					m = p.matcher(retValue);
					retValue = m.replaceAll(replacement);
	
				} catch (Exception e) {
					String error = "Error expression: " + regex + " - " + e;
					log.info(error);
					m_conversionError = error;
				}
			}
		}
		return retValue;
	} // convertSimpleStatement
	
	/**
	 * do convert map base conversion
	 * @param sqlStatement
	 * @return string
	 */
	protected String convertWithConvertMap(String sqlStatement) {
		try 
		{
			sqlStatement = applyConvertMap(cleanUpStatement(sqlStatement));
		}
		catch (RuntimeException e) {
			log.warning(e.getLocalizedMessage());
			m_exception = e;
		}
		
		return sqlStatement;
	}
	
	/**
	 * Get convert map for use in sql convertion
	 * @return map
	 */
	protected Map<String,String> getConvertMap() {
		return null;
	}
	
	/**
	 *  Convert single Statements.
	 *  - remove comments
	 *      - process FUNCTION/TRIGGER/PROCEDURE
	 *      - process Statement
	 *  @param sqlStatement
	 *  @return converted statement
	 */
	protected abstract ArrayList<String> convertStatement (String sqlStatement);

	/**
	 * True if the database support native oracle dialect, false otherwise.
	 * @return boolean
	 */
	public abstract boolean isOracle();

	public synchronized static void logMigrationScript(String oraStatement, String pgStatement) {
		// Check AdempiereSys
		// check property Log migration script
		boolean logMigrationScript = isLogMigrationScript();
		if (logMigrationScript) {
			if (dontLog(oraStatement))
				return;
			// Log oracle and postgres migration scripts in temp directory
			// migration_script_oracle.sql and migration_script_postgresql.sql
			try {
				if (tempFileOr == null) {
		            File fileNameOr = File.createTempFile("migration_script_", "_oracle.sql");
		            tempFileOr = new FileOutputStream(fileNameOr, true);
		            writerOr = new BufferedWriter(new OutputStreamWriter(tempFileOr, "UTF8"));
		            writerOr.append("SET SQLBLANKLINES ON\nSET DEFINE OFF\n\n");
				}
				writeLogMigrationScript(writerOr, oraStatement);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (pgStatement == null) {
					// if oracle call convert for postgres before logging
					Convert convert = Database.getDatabase(Database.DB_POSTGRESQL).getConvert();
					String[] r = convert.convert(oraStatement);
					pgStatement = r[0];
				}
				if (tempFilePg == null) {
		            File fileNamePg = File.createTempFile("migration_script_", "_postgresql.sql");
		            tempFilePg = new FileOutputStream(fileNamePg, true);
		            writerPg = new BufferedWriter(new OutputStreamWriter(tempFilePg, "UTF8"));
				}
				writeLogMigrationScript(writerPg, pgStatement);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return true if it is in log migration script mode
	 */
	public static boolean isLogMigrationScript() {
		boolean logMigrationScript = false;
		if (Ini.isClient()) {
			logMigrationScript = Ini.isPropertyBool(Ini.P_LOGMIGRATIONSCRIPT);
		} else {
			String sysProperty = Env.getCtx().getProperty("LogMigrationScript", "N");
			logMigrationScript = "y".equalsIgnoreCase(sysProperty) || "true".equalsIgnoreCase(sysProperty);
		}
		return logMigrationScript;
	}


	private static String [] dontLogTables = new String[] {
			"AD_ALERTPROCESSORLOG",
			"AD_CHANGELOG",
			"AD_FORM_ACCESS",
			"AD_INFOWINDOW_ACCESS",
			"AD_LDAPPROCESSORLOG",
			"AD_PACKAGE_IMP",
			"AD_PACKAGE_IMP_BACKUP",
			"AD_PACKAGE_IMP_DETAIL",
			"AD_PACKAGE_IMP_INST",
			"AD_PACKAGE_IMP_PROC",
			"AD_PREFERENCE",
			"AD_PROCESS_ACCESS",
			"AD_RECENTITEM",
			"AD_REPLICATION_LOG",
			"AD_SCHEDULERLOG",
			"AD_SESSION",
			"AD_WINDOW_ACCESS",
			"AD_USERPREFERENCE",
			"CM_WEBACCESSLOG",
			"C_ACCTPROCESSORLOG"
		};
	
	private static boolean dontLog(String statement) {
		// Do not log *Access records - teo_Sarca BF [ 2782095 ]

		String uppStmt = statement.toUpperCase().trim();
		// don't log selects
		if (uppStmt.startsWith("SELECT "))
			return true;
		// don't log update to statistic process
		if (uppStmt.startsWith("UPDATE AD_PROCESS SET STATISTIC_"))
			return true;
		if (uppStmt.startsWith("UPDATE C_ACCTPROCESSOR SET DATENEXTRUN"))
			return true;
		
		// Don't log DELETE FROM Some_Table WHERE AD_Table_ID=? AND Record_ID=?
		if (uppStmt.startsWith("DELETE FROM ") && uppStmt.endsWith(" WHERE AD_TABLE_ID=? AND RECORD_ID=?"))
			return true;
		// Don't log trl related statements - those will be created/maintained using synchronize terminology
		if (uppStmt.matches("UPDATE .*_TRL SET .*"))
			return true;
		if (uppStmt.matches("INSERT INTO .*_TRL .*"))
			return true;
		// Don't log tree custom table statements (not present in core)
		if (uppStmt.startsWith("INSERT INTO AD_TREENODE ") && uppStmt.contains(" AND T.TREETYPE='TL' AND T.AD_TABLE_ID="))
			return true;
		for (int i = 0; i < dontLogTables.length; i++) {
			if (uppStmt.startsWith("INSERT INTO " + dontLogTables[i] + " "))
				return true;
			if (uppStmt.startsWith("DELETE FROM " + dontLogTables[i] + " "))
				return true;
			if (uppStmt.startsWith("DELETE " + dontLogTables[i] + " "))
				return true;
			if (uppStmt.startsWith("UPDATE " + dontLogTables[i] + " "))
				return true;
			if (uppStmt.startsWith("INSERT INTO " + dontLogTables[i] + "("))
				return true;
		}
		
		return false;
	}

	private static String m_oldprm_COMMENT = "";

	private static void writeLogMigrationScript(Writer w, String statement) throws IOException
	{
		String prm_COMMENT;
		prm_COMMENT = Env.getContext(Env.getCtx(), "MigrationScriptComment");
		if (prm_COMMENT != null && ! m_oldprm_COMMENT.equals(prm_COMMENT)) {
			// log sysconfig comment
			w.append("-- ");
			w.append(prm_COMMENT);
			w.append("\n");
			if (w == writerPg)
				m_oldprm_COMMENT = prm_COMMENT;
		}
		// log time and date
		SimpleDateFormat format = DisplayType.getDateFormat(DisplayType.DateTime);
		String dateTimeText = format.format(new Timestamp(System.currentTimeMillis()));
		w.append("-- ");
		w.append(dateTimeText);
		w.append("\n");
		// log statement
		w.append(statement);
		// close statement
		w.append("\n;\n\n");
		// flush stream - teo_sarca BF [ 1894474 ]
		w.flush();
	}

}   //  Convert