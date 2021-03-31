package org.compiere.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.RowSet;

import org.adempiere.db.DBBundleActivator;
import org.adempiere.exceptions.DBException;
import org.compiere.dbPort.Convert;
import org.compiere.dbPort.Convert_PostgreSQL;
import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Ini;
import org.compiere.util.Language;
import org.compiere.util.Trx;
import org.compiere.util.Util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 *  @author QuynhNV.X8
 */
public class DB_MSSQL implements AdempiereDatabase
{

    private static final String P_MS_SQL_NATIVE = "MSSQLNative";

	private static final String POOL_PROPERTIES = "pool.properties";

	private static Boolean sysNative = null;
	
	static
	{
		String property = System.getProperty(P_MS_SQL_NATIVE);
		if (!Util.isEmpty(property, true) ) 
		{
			sysNative = "Y".equalsIgnoreCase(property);
		}
	}
	
	public Convert getConvert() {
		return m_convert;
	}

	public DB_MSSQL()
	{
	}

	private com.microsoft.sqlserver.jdbc.SQLServerDriver   s_driver = null;

    public static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public static final int         DEFAULT_PORT = 1433;

	private ComboPooledDataSource m_ds = null;

	private Convert_PostgreSQL         m_convert = new Convert_PostgreSQL();

	private String          m_connection;

	private String			m_dbName = null;

	private String				m_userName = null;

	private String          		m_connectionURL;

	private static final CLogger			log	= CLogger.getCLogger (DB_MSSQL.class);

    private static int              m_maxbusyconnections = 0;

    private static final String NATIVE_MARKER = "NATIVE_"+Database.DB_MSSQL+"_KEYWORK";

    private CCache<String, String> convertCache = new CCache<String, String>(null, "DB_MSSQL_Convert_Cache", 1000, 60, false);

    private Random rand = new Random();

    private static final List<String> reservedKeywords = Arrays.asList("limit","action","old","new");
    
	
	public String getName()
	{
		return Database.DB_MSSQL;
	}   //  getName

	
	public String getDescription()
	{      
		try
		{
			if (s_driver == null)
				getDriver();
		}
		catch (Exception e)
		{
		}
		if (s_driver != null)
			return s_driver.toString();
		return "No Driver";
	}   //  getDescription

	public int getStandardPort()
	{
		return DEFAULT_PORT;
	}   //  getStandardPort

	public java.sql.Driver getDriver() throws SQLException
	{
		if (s_driver == null)
		{
			s_driver = new com.microsoft.sqlserver.jdbc.SQLServerDriver();
			DriverManager.registerDriver (s_driver);
			DriverManager.setLoginTimeout (Database.CONNECTION_TIMEOUT);
		}
		return s_driver;
	}   //  getDriver

	
	public String getConnectionURL (CConnection connection)
	{
		StringBuilder sb = new StringBuilder("jdbc:sqlserver://")
			.append(connection.getHostSecond())
			.append(":").append(connection.getPortSecond()).append(";")
			.append("databaseName=").append(connection.getDbNameSecond()).append(";")
			.append("user=").append(connection.getDbUserSecond()).append(";")
			.append("password=").append(connection.getDbPassSecond()).append(";");

		m_connection = sb.toString();
		m_userName = connection.getDbUserSecond();
		m_dbName = connection.getDbNameSecond();
		return m_connection;
	}   //  getConnectionString

	
	public String getConnectionURL (String dbHost, int dbPort, String dbName,
		String userName)
	{
		m_userName = userName;
		m_dbName = dbName;
		StringBuilder sb = new StringBuilder("jdbc:sqlserver://")
			.append(dbHost)
			.append(":").append(dbPort)
			.append("/").append(dbName);

		String urlParameters = System.getProperty("org.idempiere.postgresql.URLParameters") ;
	    if (!Util.isEmpty(urlParameters)) {
			sb.append("?").append(urlParameters);  
		}

		return sb.toString();
	}	//	getConnectionURL

     
	public String getConnectionURL (String connectionURL, String userName)
	{
		m_userName = userName;
		m_connectionURL = connectionURL;
		return m_connectionURL;
	}	//	getConnectionURL

	
	public String getCatalog()
	{
		if (m_dbName != null)
			return m_dbName;
		return null;
	}	//	getCatalog

	
	public String getSchema()
	{
		if (m_userName != null)
            return m_userName;
        log.severe("User Name not set (yet) - call getConnectionURL first");
        return null;
	}	//	getSchema

	
	public boolean supportsBLOB()
	{
		return true;
	}   //  supportsBLOB

	
	public String toString()
	{
		StringBuilder sb = new StringBuilder("DB_MSSQL[");
        sb.append(m_connectionURL);
        try
        {
            StringBuilder logBuffer = new StringBuilder(50);
            logBuffer.append("# Connections: ").append(m_ds.getNumConnections());
            logBuffer.append(" , # Busy Connections: ").append(m_ds.getNumBusyConnections());
            logBuffer.append(" , # Idle Connections: ").append(m_ds.getNumIdleConnections());
            logBuffer.append(" , # Orphaned Connections: ").append(m_ds.getNumUnclosedOrphanedConnections());
        }
        catch (Exception e)
        {
            sb.append("=").append(e.getLocalizedMessage());
        }
        sb.append("]");
        return sb.toString();
	}   //  toString

	
	public String getStatus()
	{
        if (m_ds == null)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try
        {
            sb.append("# Connections: ").append(m_ds.getNumConnections());
            sb.append(" , # Busy Connections: ").append(m_ds.getNumBusyConnections());
            sb.append(" , # Idle Connections: ").append(m_ds.getNumIdleConnections());
            sb.append(" , # Orphaned Connections: ").append(m_ds.getNumUnclosedOrphanedConnections());
            sb.append(" , # Min Pool Size: ").append(m_ds.getMinPoolSize());
            sb.append(" , # Max Pool Size: ").append(m_ds.getMaxPoolSize());
            sb.append(" , # Max Statements Cache Per Session: ").append(m_ds.getMaxStatementsPerConnection());
            sb.append(" , # Active Transactions: ").append(Trx.getActiveTransactions().length);
        }
        catch (Exception e)
        {}
        return sb.toString();
	}	//	getStatus

	
	public String convertStatement (String oraStatement)
	{
		if (!isNativeMode())
		{
			String cache = convertCache.get(oraStatement);
			if (cache != null) {
				Convert.logMigrationScript(oraStatement, cache);
				if ("true".equals(System.getProperty("org.idempiere.db.postgresql.debug"))) {
					// log.warning("Oracle -> " + oraStatement);
					log.warning("Pgsql  -> " + cache);
				}
				return cache;
			}
		}

		String retValue[] = m_convert.convert(oraStatement);

		if (retValue == null || retValue.length == 0 )
			return  oraStatement;

		if (retValue.length != 1)
		{
			log.log(Level.SEVERE, ("DB_PostgreSQL.convertStatement - Convert Command Number=" + retValue.length
				+ " (" + oraStatement + ") - " + m_convert.getConversionError()));
			throw new IllegalArgumentException
				("DB_PostgreSQL.convertStatement - Convert Command Number=" + retValue.length
					+ " (" + oraStatement + ") - " + m_convert.getConversionError());
		}

		if (!isNativeMode())
			convertCache.put(oraStatement, retValue[0]);

		if (log.isLoggable(Level.FINE))
		{
			if (!oraStatement.equals(retValue[0]) && retValue[0].indexOf("AD_Error") == -1)
			{
				if (log.isLoggable(Level.FINE))log.log(Level.FINE, "PostgreSQL =>" + retValue[0] + "<= <" + oraStatement + ">");
			}
		}
		Convert.logMigrationScript(oraStatement, retValue[0]);
		return retValue[0];
	}   //  convertStatement


	/**
	 *  Get Name of System User
	 *  @return e.g. sa, system
	 */
	public String getSystemUser()
	{
    	String systemUser = System.getProperty("EONE_DB_SYSTEM_USER");
    	if (systemUser == null)
    		systemUser = "postgres";
        return systemUser;
	}	//	getSystemUser

	
	public String TO_DATE (Timestamp time, boolean dayOnly)
	{
		if (time == null)
		{
			if (dayOnly)
				return "current_date()";
			return "current_date()";
		}

		StringBuilder dateString = new StringBuilder("TO_DATE('");
		String myDate = time.toString();
		if (dayOnly)
		{
			dateString.append(myDate.substring(0,10));
			dateString.append("','YYYY-MM-DD')");
		}
		else
		{
			dateString.append(myDate.substring(0, myDate.indexOf('.')));	//	cut off miliseconds
			dateString.append("','YYYY-MM-DD HH24:MI:SS')");
		}
		return dateString.toString();
	}   //  TO_DATE

	
	public String TO_CHAR (String columnName, int displayType, String AD_Language)
	{
		StringBuilder retValue = null;
		if (DisplayType.isDate(displayType)) {
			retValue = new StringBuilder("TO_CHAR(");
	        retValue.append(columnName)
	        	.append(",'")
            	.append(Language.getLanguage(AD_Language).getDBdatePattern())
            	.append("')");	        
		} else {
			retValue = new StringBuilder("CAST (");
			retValue.append(columnName);
			retValue.append(" AS Text)");
		}
		return retValue.toString();
			
	}   //  TO_CHAR

	
	public String TO_NUMBER (BigDecimal number, int displayType)
	{
		if (number == null)
			return "NULL";
		BigDecimal result = number;
		int scale = DisplayType.getDefaultPrecision(displayType);
		if (scale > number.scale())
		{
			try
			{
				result = number.setScale(scale, RoundingMode.HALF_UP);
			}
			catch (Exception e)
			{
			//	log.severe("Number=" + number + ", Scale=" + " - " + e.getMessage());
			}
		}
		return result.toString();
	}	//	TO_NUMBER


	/**
	 * 	Get SQL Commands
	 *	@param cmdType CMD_*
	 *	@return array of commands to be executed
	 */
	public String[] getCommands (int cmdType)
	{
		if (CMD_CREATE_USER == cmdType)
			return new String[]
			{
			"CREATE USER xerp;",
			};
		//
		if (CMD_CREATE_DATABASE == cmdType)
			return new String[]
			{
		    "CREATE DATABASE xerp OWNER xerp;",
			"GRANT ALL PRIVILEGES ON xerp TO xerp;"	,
			"CREATE SCHEMA xerp;",
			"SET search_path TO xerp;"
			};
		//
		if (CMD_DROP_DATABASE == cmdType)
			return new String[]
			{
			"DROP DATABASE xerp;"
			};
		//
		return null;
	}	//	getCommands


	public RowSet getRowSet (java.sql.ResultSet rs) throws SQLException
	{
		throw new UnsupportedOperationException("PostgreSQL does not support RowSets");
	}	//	getRowSet


	public Connection getCachedConnection (CConnection connection,
		boolean autoCommit, int transactionIsolation)
		throws Exception
	{
		Connection conn = null;
        Exception exception = null;
        try
        {
            if (m_ds == null)
                getDataSource(connection);

            //
            try
            {
            	int numConnections = m_ds.getNumBusyConnections();
        		if(numConnections >= m_maxbusyconnections && m_maxbusyconnections > 0)
        		{
        			//system is under heavy load, wait between 20 to 40 seconds
        			int randomNum = rand.nextInt(40 - 20 + 1) + 20;
        			Thread.sleep(randomNum * 1000);//
        		}
        		//conn =  m_ds.getConnection();
        		
        		ConnectionReference connReference = threadLocalConnection.get();
            	try {
        			if (connReference != null && !connReference.connection.isClosed()) {
        				connReference.referenceCount++;
        				return connReference.connection;
        			} 
        		} catch (SQLException e) {}
            	
            	
        		conn = getDriverConnection(m_connectionURL, connection.getDbUserSecond(), connection.getDbPassSecond());
        		
        		connReference = new ConnectionReference(conn);
            	threadLocalConnection.set(connReference);
        		
        		if (conn == null) {
        			//try again after 10 to 30 seconds
        			int randomNum = rand.nextInt(30 - 10 + 1) + 10;
        			Thread.sleep(randomNum * 1000);//
        			conn = m_ds.getConnection();
        		}

                if (conn != null)
                {
                    if (conn.getTransactionIsolation() != transactionIsolation)
                        conn.setTransactionIsolation(transactionIsolation);
                    if (conn.getAutoCommit() != autoCommit)
                        conn.setAutoCommit(autoCommit);
                }
            }
            catch (Exception e)
            {
                exception = e;
                conn = null;
            }

            if (conn == null && exception != null)
            {
            	System.err.println(exception.toString());
            }
        }
        catch (Exception e)
        {
            exception = e;
        }

        try
        {
        	if (conn != null) {
        		boolean trace = "true".equalsIgnoreCase(System.getProperty("org.adempiere.db.traceStatus"));
        		int numConnections = m_ds.getNumBusyConnections();
        		if (numConnections > 1)
        		{
	    			if (trace)
	    			{
	    				log.warning(getStatus());
	    			}
	    			if(numConnections >= m_maxbusyconnections && m_maxbusyconnections > 0)
		            {
	    				if (!trace)
	    					log.warning(getStatus());
		                Runtime.getRuntime().runFinalization();
		            }
        		}
        	} else {
        		System.err.println("Failed to acquire new connection. Status=" + getStatus());
        	}
        }
        catch (Exception ex)
        {
        }
        if (exception != null)
            throw exception;
        return conn;
	}	//	getCachedConnection

	private String getFileName ()
	{
		//
		String base = null;
		if (Ini.isClient())
			base = System.getProperty("user.home");
		else
			base = Ini.getEOneHome();
		
		if (base != null && !base.endsWith(File.separator))
			base += File.separator;
		
		//
		return base + getName() + File.separator + POOL_PROPERTIES;
	}	//	getFileName
		
	/**
	 * 	Create DataSource (Client)
	 *	@param connection connection
	 *	@return data dource
	 */
	public DataSource getDataSource(CConnection connection)
	{
		if (m_ds != null)
			return m_ds;

		InputStream inputStream = null;
		
		//check property file from home
		String propertyFilename = getFileName();
		File propertyFile = null;
		if (!Util.isEmpty(propertyFilename))
		{
			propertyFile = new File(propertyFilename);
			if (propertyFile.exists() && propertyFile.canRead())
			{
				try {
					inputStream = new FileInputStream(propertyFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		//fall back to default config
		URL url = null;
		if (inputStream == null)
		{
			propertyFile = null;
			url = Ini.isClient()
	    			? DBBundleActivator.bundleContext.getBundle().getEntry("META-INF/pool/client.default.properties")
	    			: DBBundleActivator.bundleContext.getBundle().getEntry("META-INF/pool/server.default.properties");
	    	
	    	try {
				inputStream = url.openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
		
		Properties poolProperties = new Properties();
		try {
			poolProperties.load(inputStream);
			inputStream.close();
			inputStream = null;
		} catch (IOException e) {
			throw new DBException(e);
		}

		//auto create property file at home folder from default config
		if (propertyFile == null)			
		{
			String directoryName = propertyFilename.substring(0,  propertyFilename.length() - (POOL_PROPERTIES.length()+1));
			File dir = new File(directoryName);
			if (!dir.exists())
				dir.mkdir();
			propertyFile = new File(propertyFilename);
			try {
				inputStream = url.openStream();
				Files.copy(inputStream, propertyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				inputStream.close();
				inputStream = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		if (inputStream != null)
		{
			try {
				inputStream.close();
			} catch (IOException e) {}
		}
		
		int idleConnectionTestPeriod = getIntProperty(poolProperties, "IdleConnectionTestPeriod", 1200);
		int acquireRetryAttempts = getIntProperty(poolProperties, "AcquireRetryAttempts", 2);
		int maxIdleTimeExcessConnections = getIntProperty(poolProperties, "MaxIdleTimeExcessConnections", 1200);
		int maxIdleTime = getIntProperty(poolProperties, "MaxIdleTime", 1200);
		int unreturnedConnectionTimeout = getIntProperty(poolProperties, "UnreturnedConnectionTimeout", 0);
		boolean testConnectionOnCheckin = getBooleanProperty(poolProperties, "TestConnectionOnCheckin", false);
		boolean testConnectionOnCheckout = getBooleanProperty(poolProperties, "TestConnectionOnCheckout", true);
		String mlogClass = getStringProperty(poolProperties, "com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		
		int checkoutTimeout = getIntProperty(poolProperties, "CheckoutTimeout", 0);

        try
        {
            System.setProperty("com.mchange.v2.log.MLog", mlogClass);
            //System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "ALL");
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDataSourceName("EONE-MSSQL");
            cpds.setDriverClass(DRIVER);
            //loads the jdbc driver
            cpds.setJdbcUrl(getConnectionURL(connection));
            cpds.setUser(connection.getDbUserSecond());
            cpds.setPassword(connection.getPortSecond());
            cpds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
            cpds.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
            cpds.setMaxIdleTime(maxIdleTime);
            cpds.setTestConnectionOnCheckin(testConnectionOnCheckin);
            cpds.setTestConnectionOnCheckout(testConnectionOnCheckout);
            cpds.setAcquireRetryAttempts(acquireRetryAttempts);
            if (checkoutTimeout > 0)
            	cpds.setCheckoutTimeout(checkoutTimeout);

            int maxPoolSize = getIntProperty(poolProperties, "MaxPoolSize", 400);
        	int initialPoolSize = getIntProperty(poolProperties, "InitialPoolSize", 10);
        	int minPoolSize = getIntProperty(poolProperties, "MinPoolSize", 5);
            cpds.setInitialPoolSize(initialPoolSize);
            cpds.setInitialPoolSize(initialPoolSize);
            cpds.setMinPoolSize(minPoolSize);
            cpds.setMaxPoolSize(maxPoolSize);
            m_maxbusyconnections = (int) (maxPoolSize * 0.9);
            
            //statement pooling
            int maxStatementsPerConnection = getIntProperty(poolProperties, "MaxStatementsPerConnection", 0);
            if (maxStatementsPerConnection > 0)
            	cpds.setMaxStatementsPerConnection(maxStatementsPerConnection);

            if (unreturnedConnectionTimeout > 0)
            {
	            //the following sometimes kill active connection!
	            cpds.setUnreturnedConnectionTimeout(1200);
	            cpds.setDebugUnreturnedConnectionStackTraces(true);
            }

            m_ds = cpds;
            m_connectionURL = m_ds.getJdbcUrl();
        }
        catch (Exception ex)
        {
            m_ds = null;
            log.log(Level.SEVERE, "Could not initialise C3P0 Datasource", ex);
        }

		return m_ds;
	}

	/**
	 * 	Create Pooled DataSource (Server)
	 *	@param connection connection
	 *	@return data source
	 */
	public ConnectionPoolDataSource createPoolDataSource(CConnection connection)
	{
		throw new UnsupportedOperationException("Not supported/implemented");
	}

	/**
	 * 	Get Connection from Driver
	 *	@param connection info
	 *	@return connection or null
	 */
	public Connection getDriverConnection (CConnection connection) throws SQLException
	{
		getDriver();
		return DriverManager.getConnection (getConnectionURL (connection),
			connection.getDbUid(), connection.getDbPwd());
	}	//	getDriverConnection

	
	public Connection getDriverConnection (String dbUrl, String dbUid, String dbPwd)
		throws SQLException
	{
		getDriver();
		return DriverManager.getConnection (dbUrl, dbUid, dbPwd);
	}	//	getDriverConnection


	/**
	 * 	Close
	 */
	public void close()
	{

		if (log.isLoggable(Level.CONFIG)) log.config(toString());

		if (m_ds != null)
		{
			try
			{
				m_ds.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		m_ds = null;
	}	//	close


	
	public String getAlternativeSQL(int reExNo, String msg, String sql)
	{
		return null; //do not do re-execution of alternative SQL
	}

     
	public String getConstraintType(Connection conn, String tableName, String IXName)
	{
		if (IXName == null || IXName.length()==0)
			return "0";
		if (IXName.toUpperCase().endsWith("_KEY"))
			return "1"+IXName;
		else
			return "0";
		//jz temp, modify later from user.constraints
	}

   
	public static void dumpLocks(Connection conn)
	{
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select pg_class.relname,pg_locks.* from pg_class,pg_locks where pg_class.relfilenode=pg_locks.relation order by 1";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int cnt = rs.getMetaData().getColumnCount();
			System.out.println();
			while (rs.next())
			{
				for(int i = 0; i < cnt; i++)
				{
					Object value = rs.getObject(i+1);
					if (i > 0)
						System.out.print(", ");
					System.out.print(value != null ? value.toString() : "");
				}
				System.out.println();
			}
			System.out.println();
		} catch (Exception e) {

		} finally {
			DB.close(rs,stmt);
			rs = null;stmt = null;
		}
	}

	public int getNextID(String name) {
		return getNextID(name, null);
	}

	public int getNextID(String name, String trxName) {
		int m_sequence_id = DB.getSQLValueEx(trxName, "SELECT nextval('"+name.toLowerCase()+"')");
		return m_sequence_id;
	}

	public boolean createSequence(String name , int increment , int minvalue , int maxvalue ,int  start, String trxName)
	{
		// Check if Sequence exists
		final int cnt = DB.getSQLValueEx(trxName, "SELECT COUNT(*) FROM pg_class WHERE UPPER(relname)=? AND relkind='S'", name.toUpperCase());
		final int no;
		if (start < minvalue)
			start = minvalue;
		//
		// New Sequence
		if (cnt == 0)
		{
			no = DB.executeUpdate("CREATE SEQUENCE "+name.toUpperCase()
								+ " INCREMENT BY " + increment
								+ " MINVALUE " + minvalue
								+ " MAXVALUE " + maxvalue
								+ " START WITH " + start, trxName);
		}
		//
		// Already existing sequence => ALTER
		else
		{
			no = DB.executeUpdate("ALTER SEQUENCE "+name.toUpperCase()
					+ " INCREMENT BY " + increment
					+ " MINVALUE " + minvalue
					+ " MAXVALUE " + maxvalue
					+ " RESTART WITH " + start, trxName);
		}
		if(no == -1 )
			return false;
		else
			return true;
	}

	
	public boolean isQueryTimeoutSupported() {
		return true;
	}

	
	public String addPagingSQL(String sql, int start, int end) {
		StringBuilder newSql = new StringBuilder(sql);
		if (end > 0) {
			newSql.append(" ")
				.append(markNativeKeyword("LIMIT "))
				.append(( end - start + 1 ));
		}
		newSql.append(" ")
			.append(markNativeKeyword("OFFSET "))
			.append((start - 1));
		return newSql.toString();
	}

	public boolean isPagingSupported() {
		return true;
	}

	private int getIntProperty(Properties properties, String key, int defaultValue)
	{
		int i = defaultValue;
		try
		{
			String s = properties.getProperty(key);
			if (s != null && s.trim().length() > 0)
				i = Integer.parseInt(s);
		}
		catch (Exception e) {}
		return i;
	}

	private boolean getBooleanProperty(Properties properties, String key, boolean defaultValue)
	{
		boolean b = defaultValue;
		try
		{
			String s = properties.getProperty(key);
			if (s != null && s.trim().length() > 0)
				b = Boolean.valueOf(s);
		}
		catch (Exception e) {}
		return b;
	}
	
	private String getStringProperty(Properties properties, String key, String defaultValue)
	{
		String b = defaultValue;
		try
		{
			String s = properties.getProperty(key);
			if (s != null && s.trim().length() > 0)
				b = s.trim();
		}
		catch (Exception e) {}
		return b;
	}

	@Override
	public boolean forUpdate(PO po, int timeout) {
    	//only can lock for update if using trx
    	if (po.get_TrxName() == null)
    		return false;
    	
    	String[] keyColumns = po.get_KeyColumns();
		if (keyColumns != null && keyColumns.length > 0 && !po.is_new()) {
			StringBuilder sqlBuffer = new StringBuilder(" SELECT ");
			sqlBuffer.append(keyColumns[0])
				.append(" FROM ")
				.append(po.get_TableName())
				.append(" WHERE ");
			for(int i = 0; i < keyColumns.length; i++) {
				if (i > 0)
					sqlBuffer.append(" AND ");
				sqlBuffer.append(keyColumns[i]).append("=?");
			}
			sqlBuffer.append(" FOR UPDATE ");

			Object[] parameters = new Object[keyColumns.length];
			for(int i = 0; i < keyColumns.length; i++) {
				Object parameter = po.get_Value(keyColumns[i]);
				if (parameter != null && parameter instanceof Boolean) {
					if ((Boolean) parameter)
						parameter = "Y";
					else
						parameter = "N";
				}
				parameters[i] = parameter;
			}

			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = DB.prepareStatement(sqlBuffer.toString(),
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, po.get_TrxName());
				for(int i = 0; i < keyColumns.length; i++) {
					stmt.setObject(i+1, parameters[i]);
				}
				stmt.setQueryTimeout(timeout > 0 ? timeout : LOCK_TIME_OUT);
				
				rs = stmt.executeQuery();
				if (rs.next()) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				if (log.isLoggable(Level.INFO))log.log(Level.INFO, e.getLocalizedMessage(), e);
				throw new DBException("Could not lock record for " + po.toString() + " caused by " + e.getLocalizedMessage());
			} finally {
				DB.close(rs, stmt);
				rs = null;stmt = null;
			}			
		}
		return false;
	}
	
	@Override
	public String getNameOfUniqueConstraintError(Exception e) {
		String info = e.getMessage();
		int fromIndex = info.indexOf("\"");
		if (fromIndex == -1)
			fromIndex = info.indexOf("\u00ab"); // quote for spanish postgresql message
		if (fromIndex == -1)
			return info;
		int toIndex = info.indexOf("\"", fromIndex + 1);
		if (toIndex == -1)
			toIndex = info.indexOf("\u00bb", fromIndex + 1);
		if (toIndex == -1)
			return info;
		return info.substring(fromIndex + 1, toIndex);
	}

	@Override
	public String subsetClauseForCSV(String columnName, String csv) {
		StringBuilder builder = new StringBuilder();
		builder.append("string_to_array(")
			.append(columnName)
			.append(",',')");
		builder.append(" <@ "); //is contained by
		builder.append("string_to_array('")
			.append(csv)
			.append("',',')");

		return builder.toString();
	}

	@Override
	public String quoteColumnName(String columnName) {
		if (!isNativeMode()) {
			return columnName;
		}
		
		String lowerCase = columnName.toLowerCase();
		if (reservedKeywords.contains(lowerCase)) {
			StringBuilder sql = new StringBuilder("\"");
			sql.append(lowerCase).append("\"");
			return sql.toString();
		} else {
			return columnName;
		}
	}

	@Override
	public String intersectClauseForCSV(String columnName, String csv) {
		StringBuilder builder = new StringBuilder();
		builder.append("string_to_array(")
			.append(columnName)
			.append(",',')");
		builder.append(" && "); //is contained by
		builder.append("string_to_array('")
			.append(csv)
			.append("',',')");

		return builder.toString();
	}
	
	@Override
	public boolean isNativeMode() {
		return isUseNativeDialect();
	}
	
	/**
	 * @return true if it is using native dialect
	 */
	public final static boolean isUseNativeDialect() {
		if (Convert.isLogMigrationScript())
			return false;
		else if (sysNative != null)
			return sysNative;
		else
			return Ini.isPropertyBool(P_MS_SQL_NATIVE);
	}
	
	/**
	 * 
	 * @param keyword
	 * @return if not using native dialect, return native_marker + keyword
	 */
	public final static String markNativeKeyword(String keyword) {
		if (isUseNativeDialect())
			return keyword;
		else
			return NATIVE_MARKER + keyword;
	}
	
	/**
	 * 
	 * @param statement
	 * @return statement after the removal of native keyword marker
	 */
	public final static String removeNativeKeyworkMarker(String statement) {
		return statement.replace(DB_MSSQL.NATIVE_MARKER, "");
	}

	
	@Override
	public String getNumericDataType() {
		return "NUMERIC";
	}

	@Override
	public String getCharacterDataType() {
		return "CHAR";
	}

	@Override
	public String getVarcharDataType() {
		return "VARCHAR";
	}

	@Override
	public String getBlobDataType() {
		return "BYTEA";
	}

	@Override
	public String getClobDataType() {
		return "TEXT";
	}

	@Override
	public String getTimestampDataType() {
		return "TIMESTAMP";
	}

	@Override
	public String getSQLDDL(MColumn column) {				
		StringBuilder sql = new StringBuilder ().append(column.getColumnName())
			.append(" ").append(column.getSQLDataType());

		//	Null
		if (column.isMandatory())
			sql.append(" NOT NULL");
			
		//	Inline Constraint
		if (column.getAD_Reference_ID() == DisplayType.YesNo)
			sql.append(" CHECK (").append(column.getColumnName()).append(" IN ('Y','N'))");

		//	Default
		String defaultValue = column.getDefaultValue();
		if (defaultValue != null 
				&& defaultValue.length() > 0
				&& defaultValue.indexOf('@') == -1		//	no variables
				&& ( ! (DisplayType.isID(column.getAD_Reference_ID()) && defaultValue.equals("-1") ) ) )  // not for ID's with default -1
		{
			if (DisplayType.isText(column.getAD_Reference_ID()) 
					|| column.getAD_Reference_ID() == DisplayType.List
					|| column.getAD_Reference_ID() == DisplayType.YesNo
					// Two special columns: Defined as Table but DB Type is String 
					|| column.getColumnName().equals("EntityType") || column.getColumnName().equals("AD_Language")
					|| (column.getAD_Reference_ID() == DisplayType.Button &&
							!(column.getColumnName().endsWith("_ID"))))
			{
				if (!defaultValue.startsWith("'") && !defaultValue.endsWith("'"))
					defaultValue = DB.TO_STRING(defaultValue);
			}
			if (defaultValue.equalsIgnoreCase("sysdate"))
				defaultValue = "getDate()";
			sql.append(" DEFAULT ").append(defaultValue);
		}
		else
		{
			if (! column.isMandatory())
				sql.append(" DEFAULT NULL ");
			defaultValue = null;
		}
		
		return sql.toString();
	
	}
	
	/**
	 * 	Get SQL Add command
	 *	@param table table
	 *	@return sql
	 */
	@Override
	public String getSQLAdd (MTable table, MColumn column)
	{
		StringBuilder sql = new StringBuilder ("ALTER TABLE ")
			.append(table.getTableName())
			.append(" ADD COLUMN ").append(column.getSQLDDL());
		String constraint = column.getConstraint(table.getTableName());
		if (constraint != null && constraint.length() > 0) {
			sql.append(DB.SQLSTATEMENT_SEPARATOR).append("ALTER TABLE ")
			.append(table.getTableName())
			.append(" ADD ").append(constraint);
		}
		return sql.toString();
	}	//	getSQLAdd
	
	
	public String getSQLModify (MTable table, MColumn column, boolean setNullOption)
	{
		StringBuilder sql = new StringBuilder();
		StringBuilder sqlBase = new StringBuilder ("ALTER TABLE ")
			.append(table.getTableName())
			.append(" MODIFY ").append(column.getColumnName());
		
		//	Default
		StringBuilder sqlDefault = new StringBuilder(sqlBase)
			.append(" ").append(column.getSQLDataType());
		String defaultValue = column.getDefaultValue();
		if (defaultValue != null 
			&& defaultValue.length() > 0
			&& defaultValue.indexOf('@') == -1		//	no variables
			&& ( ! (DisplayType.isID(column.getAD_Reference_ID()) && defaultValue.equals("-1") ) ) )  // not for ID's with default -1
		{
			if (DisplayType.isText(column.getAD_Reference_ID()) 
				|| column.getAD_Reference_ID() == DisplayType.List
				|| column.getAD_Reference_ID() == DisplayType.YesNo
				// Two special columns: Defined as Table but DB Type is String 
				|| column.getColumnName().equals("EntityType") || column.getColumnName().equals("AD_Language")
				|| (column.getAD_Reference_ID() == DisplayType.Button &&
						!(column.getColumnName().endsWith("_ID"))))
			{
				if (!defaultValue.startsWith("'") && !defaultValue.endsWith("'"))
					defaultValue = DB.TO_STRING(defaultValue);
			}
			sqlDefault.append(" DEFAULT ").append(defaultValue);
		}
		else
		{
			if (! column.isMandatory())
				sqlDefault.append(" DEFAULT NULL ");
			defaultValue = null;
		}
		sql.append(sqlDefault);
		
		//	Constraint

		//	Null Values
		if (column.isMandatory() && defaultValue != null && defaultValue.length() > 0)
		{
			StringBuilder sqlSet = new StringBuilder("UPDATE ")
				.append(table.getTableName())
				.append(" SET ").append(column.getColumnName())
				.append("=").append(defaultValue)
				.append(" WHERE ").append(column.getColumnName()).append(" IS NULL");
			sql.append(DB.SQLSTATEMENT_SEPARATOR).append(sqlSet);
		}
		
		//	Null
		if (setNullOption)
		{
			StringBuilder sqlNull = new StringBuilder(sqlBase);
			if (column.isMandatory())
				sqlNull.append(" NOT NULL");
			else
				sqlNull.append(" NULL");
			sql.append(DB.SQLSTATEMENT_SEPARATOR).append(sqlNull);
		}
		//
		return sql.toString();
	}

	@Override
	public boolean isSupported(String sql) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getSystemDatabase(String databaseName) {
		// TODO Auto-generated method stub
		return "";
	}	
	
	private static ThreadLocal<ConnectionReference> threadLocalConnection = new ThreadLocal<ConnectionReference>() {
        protected ConnectionReference initialValue()
        {
        	return null;
        }
    };
    
	
	protected class ConnectionReference {
		protected Connection connection;
		protected int referenceCount;
		protected ConnectionReference(Connection conn) {
			connection = conn;
			referenceCount = 1;
		}		
	}

}   //  DB_MSSQL
