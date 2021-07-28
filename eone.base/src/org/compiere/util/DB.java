package org.compiere.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.RowSet;

import org.adempiere.exceptions.DBException;
import org.compiere.db.AdempiereDatabase;
import org.compiere.db.CConnection;
import org.compiere.db.Database;
import org.compiere.db.ProxyFactory;

import eone.base.model.MTable;
import eone.base.model.PO;
import eone.base.model.POResultSet;


public final class DB
{
	private static CConnection      s_cc = null;
	
	private static CLogger			log = CLogger.getCLogger (DB.class);

	private static Object			s_ccLock = new Object();

	public static final String SQLSTATEMENT_SEPARATOR = "; ";


	public static void updateMail()
	{
		//	Get Property File
		String envName = Ini.getEOneHome();
		if (envName == null)
			return;
		envName += File.separator + "eoneEnv.properties";
		File envFile = new File(envName);
		if (!envFile.exists())
			return;

		Properties env = new Properties();
		try
		{
			FileInputStream in = new FileInputStream(envFile);
			env.load(in);
			in.close();
		}
		catch (Exception e)
		{
			return;
		}
		String updated = env.getProperty("EONE_MAIL_UPDATED");
		if (updated != null && updated.equals("Y"))
			return;

		//	See org.compiere.install.ConfigurationData
		String server = env.getProperty("EONE_MAIL_SERVER");
		if (server == null || server.length() == 0)
			return;
		String adminEMail = env.getProperty("EONE_ADMIN_EMAIL");
		if (adminEMail == null || adminEMail.length() == 0)
			return;
		String mailUser = env.getProperty("EONE_MAIL_USER");
		if (mailUser == null || mailUser.length() == 0)
			return;
		String mailPassword = env.getProperty("EONE_MAIL_PASSWORD");

		StringBuilder sql = new StringBuilder("UPDATE AD_Client SET")
			.append(" SMTPHost=").append(DB.TO_STRING(server))
			.append(", RequestEMail=").append(DB.TO_STRING(adminEMail))
			.append(", RequestUser=").append(DB.TO_STRING(mailUser))
			.append(", RequestUserPW=").append(DB.TO_STRING(mailPassword))
			.append(", IsSMTPAuthorization='Y' WHERE AD_Client_ID=0");
		int no = DB.executeUpdate(sql.toString(), null);
		if (log.isLoggable(Level.FINE)) log.fine("Client #"+no);
		//
		sql = new StringBuilder("UPDATE AD_User SET ")
			.append(" EMail=").append(DB.TO_STRING(adminEMail))
			.append(", EMailUser=").append(DB.TO_STRING(mailUser))
			.append(", EMailUserPW=").append(DB.TO_STRING(mailPassword))
			.append(" WHERE AD_User_ID IN (0,100)");
		no = DB.executeUpdate(sql.toString(), null);
		if (log.isLoggable(Level.FINE)) log.fine("User #"+no);
		//
		try
		{
			env.setProperty("EONE_MAIL_UPDATED", "Y");
			FileOutputStream out = new FileOutputStream(envFile);
			env.store(out, "");
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
		}

	}	//	updateMail

	
	public synchronized static void setDBTarget (CConnection cc)
	{
		if (cc == null)
			throw new IllegalArgumentException("Connection is NULL");

		if (s_cc != null && s_cc.equals(cc))
			return;

		DB.closeTarget();
		//
		synchronized(s_ccLock)
		{
			s_cc = cc;			
		}

		s_cc.setDataSource();

		if (log.isLoggable(Level.CONFIG)) log.config(s_cc + " - DS=" + s_cc.isDataSource());
	//	Trace.printStack();
	}   //  setDBTarget

	/**
	 * Connect to database and initialise all connections.
	 * @return True if success, false otherwise
	 */
	public static boolean connect() {
		//direct connection
		boolean success =false;
		try
		{
            Connection connRW = getConnectionRW(true);
            if (connRW != null)
            {
                s_cc.readInfo(connRW);
                connRW.close();
            }

            Connection connRO = getConnectionRO();
            if (connRO != null)
            {
                connRO.close();
            }

            Connection connID = getConnectionID();
            if (connID != null)
            {
                connID.close();
            }
            success = ((connRW != null) && (connRO != null) && (connID != null));
		}
        catch (Exception e)
		{
        	System.err.println("Could not connect to DB - " + e.getLocalizedMessage());
        	e.printStackTrace();
            success = false;
		}
		return success;
	}

	/**
	 * @return true, if connected to database
	 */
	public static boolean isConnected()
	{
		return isConnected(true);
	}

	/**
	 *  Is there a connection to the database ?
	 *  @param createNew If true, try to connect it not already connected
	 *  @return true, if connected to database
	 */
	public static boolean isConnected(boolean createNew)
	{
		//bug [1637432]
		if (s_cc == null) return false;

		//direct connection
		boolean success = false;
		CLogErrorBuffer eb = CLogErrorBuffer.get(false);
		if (eb != null && eb.isIssueError())
			eb.setIssueError(false);
		else
			eb = null;	//	don't reset
		try
		{
            Connection conn = getConnectionRW(createNew);   //  try to get a connection
            if (conn != null)
            {
                conn.close();
                success = true;
            }
            else success = false;
		}
		catch (Exception e)
		{
			success = false;
		}
		if (eb != null)
			eb.setIssueError(true);
		return success;
	}   //  isConnected


	public static Connection getConnectionRW (boolean createNew)
	{
        return createConnection(true, false, Connection.TRANSACTION_READ_COMMITTED);
	}   //  getConnectionRW

	
	public static Connection getConnectionID ()
	{
        return createConnection(false, false, Connection.TRANSACTION_READ_COMMITTED);
	}   //  getConnectionID

	/**
	 *	Return read committed, read/only from pool.
	 *  @return Connection (r/o)
	 */
	public static Connection getConnectionRO ()
	{
        return createConnection(true, true, Connection.TRANSACTION_READ_COMMITTED);     //  see below
	}	//	getConnectionRO

	/**
	 *	Return a replica connection if possible, otherwise read committed, read/only from pool.
	 *  @return Connection (r/o)
	 */
	public static Connection getReportingConnectionRO ()
	{
		Connection conn = DBReadReplica.getConnectionRO();
		if (conn == null)
			conn = getConnectionRO();
        return conn;
	}	//	getReportingConnectionRO

	
	public static Connection createConnection (boolean autoCommit, int trxLevel)
	{
		Connection conn = s_cc.getConnection (autoCommit, trxLevel);

		//hengsin: failed to set autocommit can lead to severe lock up of the system
        try {
	        if (conn != null && conn.getAutoCommit() != autoCommit)
	        {
	        	throw new IllegalStateException("Failed to set the requested auto commit mode on connection. [autoCommit=" + autoCommit +"]");
	        }
        } catch (SQLException e) {}

		return conn;
	}	//	createConnection

    
    public static Connection createConnection (boolean autoCommit, boolean readOnly, int trxLevel)
    {
        Connection conn = s_cc.getConnection (autoCommit, trxLevel);

        if (conn == null)
        {
            throw new IllegalStateException("DB.getConnectionRO - @NoDBConnection@");
        }

        try {
	        if (conn.getAutoCommit() != autoCommit)
	        {
	        	throw new IllegalStateException("Failed to set the requested auto commit mode on connection. [autocommit=" + autoCommit +"]");
	        }
        } catch (SQLException e) {}

        return conn;
    }   //  createConnection

	/**
	 *  Get Database Driver.
	 *  Access to database specific functionality.
	 *  @return Adempiere Database Driver
	 */
	public static AdempiereDatabase getDatabase()
	{
		if (s_cc != null)
			return s_cc.getDatabase();
		log.severe("No Database Connection");
		return null;
	}   //  getDatabase

	/**
	 *  Get Database Driver.
	 *  Access to database specific functionality.
	 *  @param URL JDBC connection url
	 *  @return Adempiere Database Driver
	 */
	public static AdempiereDatabase getDatabase(String URL)
	{
		return Database.getDatabaseFromURL(URL);
	}   //  getDatabase

	public static boolean isOracle()
	{
		if (s_cc != null)
			return s_cc.isOracle();
		log.severe("No Database Connection");
		return false;
	}	//	isOracle

    
	public static boolean isPostgreSQL()
	{
		if (s_cc != null)
			return s_cc.isPostgreSQL();
		log.severe("No Database");
		return false;
	}	//	isPostgreSQL

	/**
	 * 	Get Database Info
	 *	@return info
	 */
	public static String getDatabaseInfo()
	{
		if (s_cc != null)
			return s_cc.getDBInfo();
		return "No Database";
	}	//	getDatabaseInfo


	public static boolean isBuildOK (Properties ctx)
	{
		return true;
	}   //  isDatabaseOK


	/**************************************************************************
	 *	Close Target
	 */
	public static void closeTarget()
	{

        boolean closed = false;

        //  CConnection
        if (s_cc != null)
        {
            closed = true;
            s_cc.setDataSource(null);
        }
        s_cc = null;
        if (closed)
            log.fine("closed");
	}	//	closeTarget

	/**************************************************************************
	 *	Prepare Forward Read Only Call
	 *  @param SQL sql
	 *  @return Callable Statement
	 */
	public static CallableStatement prepareCall(String sql)
	{
		return prepareCall(sql, ResultSet.CONCUR_UPDATABLE, null);
	}
	
	
	public static CallableStatement prepareCall(String SQL, int resultSetConcurrency, String trxName)
	{
		if (SQL == null || SQL.length() == 0)
			throw new IllegalArgumentException("Required parameter missing - " + SQL);
		
		return ProxyFactory.newCCallableStatement(ResultSet.TYPE_FORWARD_ONLY, resultSetConcurrency, SQL,
				trxName);
	}	//	prepareCall

	
	

	/**************************************************************************
	 *	Prepare Statement
	 *  @param sql
	 *  @return Prepared Statement
	 *  @deprecated
	 */
	public static CPreparedStatement prepareStatement (String sql)
	{
		int concurrency = ResultSet.CONCUR_READ_ONLY;
		String upper = sql.toUpperCase();
		if (upper.startsWith("UPDATE ") || upper.startsWith("DELETE "))
			concurrency = ResultSet.CONCUR_UPDATABLE;
		return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, concurrency, null);
	}	//	prepareStatement

	
	/**
	 *	Prepare Statement
	 *  @param sql
	 * 	@param trxName transaction
	 *  @return Prepared Statement
	 */
	public static CPreparedStatement prepareStatement (String sql, String trxName)
	{
		int concurrency = ResultSet.CONCUR_READ_ONLY;
		String upper = sql.toUpperCase();
		if (upper.startsWith("UPDATE ") || upper.startsWith("DELETE "))
			concurrency = ResultSet.CONCUR_UPDATABLE;
		return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, concurrency, trxName);
	}	//	prepareStatement

	
		
	public static CPreparedStatement prepareStatement(String sql,
		int resultSetType, int resultSetConcurrency, String trxName)
	{
		if (sql == null || sql.length() == 0)
			throw new IllegalArgumentException("No SQL");
		//
		return ProxyFactory.newCPreparedStatement(resultSetType, resultSetConcurrency, sql, trxName);
	}	//	prepareStatement

	/**
	 *	Create Read Only Statement
	 *  @return Statement
	 */
	public static Statement createStatement()
	{
		return createStatement (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, null);
	}	//	createStatement

	
	public static Statement createStatement(int resultSetType, int resultSetConcurrency, String trxName)
	{
		return ProxyFactory.newCStatement(resultSetType, resultSetConcurrency, trxName);
	}	//	createStatement

	
	public static void setParameters(PreparedStatement stmt, Object[] params)
	throws SQLException
	{
		if (params == null || params.length == 0) {
			return;
		}
		//
		for (int i = 0; i < params.length; i++)
		{
			setParameter(stmt, i+1, params[i]);
		}
	}

	/**
	 * Set parameters for given statement
	 * @param stmt statements
	 * @param params parameters list; if null or empty list, no parameters are set
	 */
	public static void setParameters(PreparedStatement stmt, List<?> params)
	throws SQLException
	{
		if (params == null || params.size() == 0)
		{
			return;
		}
		for (int i = 0; i < params.size(); i++)
		{
			setParameter(stmt, i+1, params.get(i));
		}
	}


	
	public static void setParameter(PreparedStatement pstmt, int index, Object param)
	throws SQLException
	{
		if (param == null)
			pstmt.setObject(index, null);
		else if (param instanceof String)
			pstmt.setString(index, (String)param);
		else if (param instanceof Integer)
			pstmt.setInt(index, ((Integer)param).intValue());
		else if (param instanceof BigDecimal)
			pstmt.setBigDecimal(index, (BigDecimal)param);
		else if (param instanceof Timestamp)
			pstmt.setTimestamp(index, (Timestamp)param);
		else if (param instanceof Boolean)
			pstmt.setString(index, ((Boolean)param).booleanValue() ? "Y" : "N");
		else if (param instanceof byte[])
			pstmt.setBytes(index, (byte[]) param);
		else
			throw new DBException("Unknown parameter type "+index+" - "+param);
	}

	
	public static int executeUpdate (String sql)
	{
		return executeUpdate(sql, null, false, null);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, String trxName)
	{
		return executeUpdate(sql, trxName, 0);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, String trxName, int timeOut)
	{
		return executeUpdate(sql, null, false, trxName, timeOut);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, boolean ignoreError)
	{
		return executeUpdate (sql, null, ignoreError, null);
	}	//	executeUpdate

	public static int executeUpdate (String sql, boolean ignoreError, String trxName)
	{
		return executeUpdate (sql, ignoreError, trxName, 0);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, boolean ignoreError, String trxName, int timeOut)
	{
		return executeUpdate (sql, null, ignoreError, trxName, timeOut);
	}

	
	public static int executeUpdate (String sql, int param, String trxName)
	{
		return executeUpdate (sql, param, trxName, 0);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, int param, String trxName, int timeOut)
	{
		return executeUpdate (sql, new Object[]{Integer.valueOf(param)}, false, trxName, timeOut);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, int param, boolean ignoreError, String trxName)
	{
		return executeUpdate (sql, param, ignoreError, trxName, 0);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, int param, boolean ignoreError, String trxName, int timeOut)
	{
		return executeUpdate (sql, new Object[]{Integer.valueOf(param)}, ignoreError, trxName, timeOut);
	}	//	executeUpdate

	
	public static int executeUpdate (String sql, Object[] params, boolean ignoreError, String trxName)
	{
		return executeUpdate(sql, params, ignoreError, trxName, 0);
	}

	
	public static int executeUpdate (String sql, Object[] params, boolean ignoreError, String trxName, int timeOut)
	{
		if (sql == null || sql.length() == 0)
			throw new IllegalArgumentException("Required parameter missing - " + sql);
		verifyTrx(trxName, sql);
		//
		int no = -1;
		CPreparedStatement cs = ProxyFactory.newCPreparedStatement(ResultSet.TYPE_FORWARD_ONLY,
			ResultSet.CONCUR_UPDATABLE, sql, trxName);	//	converted in call

		try
		{
			setParameters(cs, params);
			//set timeout
			if (timeOut > 0)
			{
				cs.setQueryTimeout(timeOut);
			}
			no = cs.executeUpdate();
			//	No Transaction - Commit
			if (trxName == null)
			{
				cs.commit();	//	Local commit
			}
		}
		catch (Exception e)
		{
			String errorCode = e.toString();
			if (errorCode.contains("duplicate key")) {
				log.saveError("Error", Msg.getMsg(Env.getCtx(), "Double_Code"));
				
			}else {
				e = getSQLException(e);
				if (ignoreError)
					log.log(Level.SEVERE, cs.getSql() + " [" + trxName + "] - " +  e.getMessage());
				else
				{
					log.log(Level.SEVERE, cs.getSql() + " [" + trxName + "]", e);
					String msg = DBException.getDefaultDBExceptionMessage(e);
					log.saveError (msg != null ? msg : "DBExecuteError", e);
				}
			}
			
		//	throw new DBException(e);
		}
		finally
		{
			//  Always close cursor
			close(cs);
			cs = null;
		}
		return no;
	}	//	executeUpdate

	
	public static int executeUpdateEx (String sql, Object[] params, String trxName) throws DBException
	{
		return executeUpdateEx(sql, params, trxName, 0);
	}

	
	public static int executeUpdateEx (String sql, Object[] params, String trxName, int timeOut) throws DBException
	{
		if (sql == null || sql.length() == 0)
			throw new IllegalArgumentException("Required parameter missing - " + sql);
		//
		verifyTrx(trxName, sql);
		int no = -1;
		CPreparedStatement cs = ProxyFactory.newCPreparedStatement(ResultSet.TYPE_FORWARD_ONLY,
			ResultSet.CONCUR_UPDATABLE, sql, trxName);	//	converted in call

		try
		{
			setParameters(cs, params);
			if (timeOut > 0)
			{
				{
					cs.setQueryTimeout(timeOut);
				}
			}
			no = cs.executeUpdate();
			//	No Transaction - Commit
			if (trxName == null)
			{
				cs.commit();	//	Local commit
			}
		}
		catch (Exception e)
		{
			throw new DBException(e);
		}
		finally
		{
			close(cs);
			cs = null;
		}
		return no;
	}

	
	public static int executeUpdateMultiple (String sql, boolean ignoreError, String trxName)
	{
		if (sql == null || sql.length() == 0)
			throw new IllegalArgumentException("Required parameter missing - " + sql);
		int index = sql.indexOf(SQLSTATEMENT_SEPARATOR);
		if (index == -1)
			return executeUpdate(sql, null, ignoreError, trxName);
		int no = 0;
		//
		String statements[] = sql.split(SQLSTATEMENT_SEPARATOR);
		for (int i = 0; i < statements.length; i++)
		{
			if (log.isLoggable(Level.FINE)) log.fine(statements[i]);
			no += executeUpdate(statements[i], null, ignoreError, trxName);
		}

		return no;
	}	//	executeUpdareMultiple

	/**
	 * Execute Update and throw exception.
	 * @see {@link #executeUpdateEx(String, Object[], String)}
	 */
	public static int executeUpdateEx (String sql, String trxName) throws DBException
	{
		return executeUpdateEx(sql, trxName, 0);
	}	//	executeUpdateEx

	/**
	 * Execute Update and throw exception.
	 * @see {@link #executeUpdateEx(String, Object[], String)}
	 */
	public static int executeUpdateEx (String sql, String trxName, int timeOut) throws DBException
	{
		return executeUpdateEx(sql, null, trxName, timeOut);
	}	//	executeUpdateEx

	
	public static boolean commit (boolean throwException, String trxName) throws SQLException,IllegalStateException
	{
        // Not on transaction scope, Connection are thus auto commit
        if (trxName == null)
        {
            return true;
        }

		try
		{
			Trx trx = Trx.get(trxName, false);
			if (trx != null)
				return trx.commit(true);

            if (throwException)
            {
                throw new IllegalStateException("Could not load transation with identifier: " + trxName);
            }
            else
            {
                return false;
            }
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "[" + trxName + "]", e);
			if (throwException)
				throw e;
			return false;
		}
	}	//	commit

	
	public static boolean rollback (boolean throwException, String trxName) throws SQLException
	{
		try
		{
			Connection conn = null;
			Trx trx = trxName == null ? null : Trx.get(trxName, true);
			if (trx != null)
				return trx.rollback(true);
			else
				conn = DB.getConnectionRW(true);
			if (conn != null && !conn.getAutoCommit())
				conn.rollback();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "[" + trxName + "]", e);
			if (throwException)
				throw e;
			return false;
		}
		return true;
	}	//	commit

	
	public static RowSet getRowSet (String sql)
	{
		// Bugfix Gunther Hoppe, 02.09.2005, vpj-cd e-evolution
		CStatementVO info = new CStatementVO (RowSet.TYPE_SCROLL_INSENSITIVE, RowSet.CONCUR_READ_ONLY, DB.getDatabase().convertStatement(sql));
		CPreparedStatement stmt = ProxyFactory.newCPreparedStatement(info);
		RowSet retValue = stmt.getRowSet();
		close(stmt);
		return retValue;
	}	//	getRowSet

    
    public static int getSQLValueEx (String trxName, String sql, Object... params) throws DBException
    {
    	int retValue = -1;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
    		if (rs.next())
    			retValue = rs.getInt(1);
    		else
    			if (log.isLoggable(Level.FINE)) log.fine("No Value " + sql);
    	}
    	catch (SQLException e)
    	{
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	return retValue;
    }

    
    public static int getSQLValueEx (String trxName, String sql, List<Object> params)
    {
		return getSQLValueEx(trxName, sql, params.toArray(new Object[params.size()]));
    }

   
    public static int getSQLValue (String trxName, String sql, Object... params)
    {
    	int retValue = -1;
    	try
    	{
    		retValue = getSQLValueEx(trxName, sql, params);
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, sql, getSQLException(e));
    	}
    	return retValue;
    }

    
    public static int getSQLValue (String trxName, String sql, List<Object> params)
    {
		return getSQLValue(trxName, sql, params.toArray(new Object[params.size()]));
    }

   
    public static String getSQLValueStringEx (String trxName, String sql, Object... params)
    {
    	String retValue = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
    		if (rs.next())
    			retValue = rs.getString(1);
    		else
    			if (log.isLoggable(Level.FINE)) log.fine("No Value " + sql);
    	}
    	catch (SQLException e)
    	{
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	return retValue;
    }

    
    public static String getSQLValueStringEx (String trxName, String sql, List<Object> params)
    {
		return getSQLValueStringEx(trxName, sql, params.toArray(new Object[params.size()]));
    }

    
    public static String getSQLValueString (String trxName, String sql, Object... params)
    {
    	String retValue = null;
    	try
    	{
    		retValue = getSQLValueStringEx(trxName, sql, params);
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, sql, getSQLException(e));
    	}
    	return retValue;
    }

    
    public static String getSQLValueString (String trxName, String sql, List<Object> params)
    {
		return getSQLValueString(trxName, sql, params.toArray(new Object[params.size()]));
    }

    
    public static BigDecimal getSQLValueBDEx (String trxName, String sql, Object... params) throws DBException
    {
    	BigDecimal retValue = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
    		if (rs.next())
    			retValue = rs.getBigDecimal(1);
    		else
    			if (log.isLoggable(Level.FINE)) log.fine("No Value " + sql);
    	}
    	catch (SQLException e)
    	{
    		//log.log(Level.SEVERE, sql, getSQLException(e));
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	return retValue;
    }

   
    public static BigDecimal getSQLValueBDEx (String trxName, String sql, List<Object> params) throws DBException
    {
		return getSQLValueBDEx(trxName, sql, params.toArray(new Object[params.size()]));
    }


    /**
     * Get BigDecimal Value from sql
     * @param trxName trx
     * @param sql sql
     * @param params array of parameters
     * @return first value or null
     */
    public static BigDecimal getSQLValueBD (String trxName, String sql, Object... params)
    {
    	try
    	{
    		return getSQLValueBDEx(trxName, sql, params);
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, sql, getSQLException(e));
    	}
    	return null;
    }


    
    public static BigDecimal getSQLValueBD (String trxName, String sql, List<Object> params)
    {
		return getSQLValueBD(trxName, sql, params.toArray(new Object[params.size()]));
    }

   
    public static Timestamp getSQLValueTSEx (String trxName, String sql, Object... params)
    {
    	Timestamp retValue = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
    		if (rs.next())
    			retValue = rs.getTimestamp(1);
    		else
    			if (log.isLoggable(Level.FINE)) log.fine("No Value " + sql);
    	}
    	catch (SQLException e)
    	{
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	return retValue;
    }


    public static Timestamp getSQLValueTSEx (String trxName, String sql, List<Object> params) throws DBException
    {
		return getSQLValueTSEx(trxName, sql, params.toArray(new Object[params.size()]));
    }

    
    public static Timestamp getSQLValueTS (String trxName, String sql, Object... params)
    {
    	try
    	{
    		return getSQLValueTSEx(trxName, sql, params);
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, sql, getSQLException(e));
    	}
    	return null;
    }

    
    public static Timestamp getSQLValueTS (String trxName, String sql, List<Object> params)
    {
		Object[] arr = new Object[params.size()];
		params.toArray(arr);
		return getSQLValueTS(trxName, sql, arr);
    }

	
	public static KeyNamePair[] getKeyNamePairs(String sql, boolean optional)
	{
		return getKeyNamePairs(sql, optional, (Object[])null);
	}

	
	public static KeyNamePair[] getKeyNamePairs(String sql, boolean optional, Object ... params)
	{
		return getKeyNamePairs(null, sql, optional, params);
	}

	
	public static KeyNamePair[] getKeyNamePairs(String trxName, String sql, boolean optional, Object ... params)
	{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        if (optional)
        {
            list.add (new KeyNamePair(-1, ""));
        }
        try
        {
            pstmt = DB.prepareStatement(sql, trxName);
            setParameters(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, sql, getSQLException(e));
        }
        finally
        {
            close(rs, pstmt);
            rs= null;
            pstmt = null;
        }
        KeyNamePair[] retValue = new KeyNamePair[list.size()];
        list.toArray(retValue);
    //  s_log.fine("getKeyNamePairs #" + retValue.length);
        return retValue;
	}	//	getKeyNamePairs

	
	public static int[] getIDsEx(String trxName, String sql, Object ... params) throws DBException
	{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Integer> list = new ArrayList<Integer>();
        try
        {
            pstmt = DB.prepareStatement(sql, trxName);
            setParameters(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                list.add(rs.getInt(1));
            }
        }
        catch (SQLException e)
        {
    		throw new DBException(e, sql);
        }
        finally
        {
            close(rs, pstmt);
            rs= null;
            pstmt = null;
        }
		//	Convert to array
		int[] retValue = new int[list.size()];
		for (int i = 0; i < retValue.length; i++)
		{
			retValue[i] = list.get(i);
		}
        return retValue;
	}	//	getIDsEx
	
	
	public static Object[] getObjectValuesEx(String trxName, String sql, int size, Object ... params) throws DBException
	{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Object [] myObject = new Object[size];
        try
        {
            pstmt = DB.prepareStatement(sql, trxName);
            setParameters(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
	            for(int i = 0; i < size; i++)
	            {
	                myObject[i] = rs.getObject(i+1);
	            }
            }
        }
        catch (SQLException e)
        {
    		throw new DBException(e, sql);
        }
        finally
        {
            close(rs, pstmt);
            rs= null;
            pstmt = null;
        }
		
        return myObject;
	}
	
	public static boolean isSOTrx (String TableName, String whereClause, int windowNo)
	{
        if (TableName == null || TableName.length() == 0)
        {
            log.severe("No TableName");
            return true;
        }
        if (whereClause == null || whereClause.length() == 0)
        {
            log.severe("No Where Clause");
            return true;
        }
        //
        Boolean isSOTrx = null;
        boolean noIsSOTrxColumn = false;
        if (MTable.get(Env.getCtx(), TableName).getColumn("IsSOTrx") == null) {
        	noIsSOTrxColumn = true;
        } else {
        	String sql = "SELECT IsSOTrx FROM " + TableName
        	+ " WHERE " + whereClause;
        	PreparedStatement pstmt = null;
        	ResultSet rs = null;
        	try
        	{
        		pstmt = DB.prepareStatement (sql, null);
        		rs = pstmt.executeQuery ();
        		if (rs.next ())
        			isSOTrx = Boolean.valueOf("Y".equals(rs.getString(1)));
        	}
        	catch (Exception e)
        	{
        		noIsSOTrxColumn = true;
        	}
        	finally
        	{
        		close(rs, pstmt);
        		rs= null;
        		pstmt = null;
        	}
        }
        if (noIsSOTrxColumn && TableName.endsWith("Line")) {
        	noIsSOTrxColumn = false;
        	String hdr = TableName.substring(0, TableName.indexOf("Line"));
        	if (MTable.get(Env.getCtx(), hdr).getColumn("IsSOTrx") == null) {
        		noIsSOTrxColumn = true;
        	} else {
        		// use IN instead of EXISTS as the subquery should be highly selective
        		String sql = "SELECT IsSOTrx FROM " + hdr
        		+ " h WHERE h." + hdr + "_ID IN (SELECT l." + hdr + "_ID FROM " + TableName
        		+ " l WHERE " + whereClause + ")";
        		PreparedStatement pstmt2 = null;
        		ResultSet rs2 = null;
        		try
        		{
        			pstmt2 = DB.prepareStatement (sql, null);
        			rs2 = pstmt2.executeQuery ();
        			if (rs2.next ())
        				isSOTrx = Boolean.valueOf("Y".equals(rs2.getString(1)));
        		}
        		catch (Exception ee)
        		{
        			noIsSOTrxColumn = true;
        		}
        		finally
        		{
        			close(rs2, pstmt2);
        			rs2= null;
        			pstmt2 = null;
        		}
        	}
        }
        if (noIsSOTrxColumn)
        	if (log.isLoggable(Level.FINEST))log.log(Level.FINEST, TableName + " - No SOTrx");
        if (isSOTrx == null) {
        	if (windowNo >= 0) {
        		// check context
        		isSOTrx = Boolean.valueOf("Y".equals(Env.getContext(Env.getCtx(), windowNo, "IsSOTrx")));
        	} else {
            	isSOTrx = Boolean.TRUE;
        	}
        }
        return isSOTrx.booleanValue();
	}	//	isSOTrx

	public static boolean isSOTrx (String TableName, String whereClause) {
		return isSOTrx (TableName, whereClause, -1);
	}

	
	public static int getNextID (Properties ctx, String TableName, String trxName)
	{
		if (ctx == null)
			throw new IllegalArgumentException("Context missing");
		if (TableName == null || TableName.length() == 0)
			throw new IllegalArgumentException("TableName missing");
		return getNextID(Env.getAD_Client_ID(ctx), TableName, trxName);
	}	//	getNextID

	public static int getNextID (int AD_Client_ID, String TableName, String trxName)
	{
		
		String sequenceName ="";		
		try{
		sequenceName = TableName.toUpperCase() + "_SEQ";
		}catch (Exception e)
		{
			throw new IllegalArgumentException(TableName + " - " + e.getMessage(), e);
		}
		
		String sqlSequence = "";
		if (DB.isOracle()) {
			sqlSequence = "Select " + sequenceName + ".NEXTVAL from dual";
		}else {
			sqlSequence = "Select NextVal('" + sequenceName + "')";
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int retValue = 0;
		try{
			conn = DB.getConnectionID();
			//	Error
			if (conn == null)
				return 0;

			pstmt = conn.prepareStatement(sqlSequence,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				retValue = rs.getInt(1);
			}
		}catch (Exception e)
		{
			try 
			{
				if (conn != null)
					conn.rollback();
			} catch (SQLException e1) { 
				throw new IllegalArgumentException(e1.getMessage(),e1);
			}
		}
		finally
		{
			DB.close(rs, pstmt);
			pstmt = null;
			rs = null;
			if (conn != null)
				{
					try {
						conn.close();
					} catch (SQLException e) {
						throw new IllegalArgumentException(e.getMessage(),e);
					}
						conn = null;
				}		
		}
		return retValue;
	}
	
	
	public static String getDocumentNo(int C_DocType_ID, String trxName, boolean definite, PO po)
	{
		return CalloutUtil.getCalloutValue(Env.getCtx(), po, 0, C_DocType_ID, "DocumentNo");	
	}	//	getDocumentNo

	
	public static String getDocumentNo (Properties ctx, int WindowNo,
		String TableName, boolean onlyDocType, String trxName)
	{
		if (ctx == null || TableName == null || TableName.length() == 0)
			throw new IllegalArgumentException("Required parameter missing");

		int	C_DocType_ID = Env.getContextAsInt(ctx, WindowNo + "|C_DocType_ID");
		
		return getDocumentNo (C_DocType_ID, trxName, false, null);
	}	//	getDocumentNo

	
	public static String TO_DATE (Timestamp time, boolean dayOnly)
	{
		return s_cc.getDatabase().TO_DATE(time, dayOnly);
	}   //  TO_DATE

	/**
	 *  Create SQL TO Date String from Timestamp
	 *  @param day day time
	 *  @return TO_DATE String (day only)
	 */
	public static String TO_DATE (Timestamp day)
	{
		return TO_DATE(day, true);
	}   //  TO_DATE

	
	public static String TO_CHAR (String columnName, int displayType, String AD_Language)
	{
		if (columnName == null || AD_Language == null || columnName.length() == 0)
			throw new IllegalArgumentException("Required parameter missing");
		return s_cc.getDatabase().TO_CHAR(columnName, displayType, AD_Language);
	}   //  TO_CHAR

	
	public static String TO_NUMBER (BigDecimal number, int displayType)
	{
		return s_cc.getDatabase().TO_NUMBER(number, displayType);
	}	//	TO_NUMBER

	public static String TO_STRING (String txt)
	{
		return TO_STRING (txt, 0);
	}   //  TO_STRING

	
	public static String TO_STRING (String txt, int maxLength)
	{
		if (txt == null || txt.length() == 0)
			return "NULL";

		//  Length
		String text = txt;
		if (maxLength != 0 && text.length() > maxLength)
			text = txt.substring(0, maxLength);

		//  copy characters		(we need to look through anyway)
		StringBuilder out = new StringBuilder();
		out.append(QUOTE);		//	'
		for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if (c == QUOTE)
				out.append("''");
			else
				out.append(c);
		}
		out.append(QUOTE);		//	'
		//
		return out.toString();
	}	//	TO_STRING

	/**
	 * convenient method to close result set
	 * @param rs
	 */
	public static void close( ResultSet rs) {
        try {
            if (rs!=null) rs.close();
        } catch (SQLException e) {
            ;
        }
    }

	/**
	 * convenient method to close statement
	 * @param st
	 */
    public static void close( Statement st) {
        try {
            if (st!=null) 
            	st.close();
        } catch (SQLException e) {
            ;
        }
    	if (readReplicaStatements.contains(st)) {
			try {
				DBReadReplica.closeReadReplicaStatement(st);
			} catch (Exception e) {
				;
			} finally {
				readReplicaStatements.remove(st);
			}
    	}
    }

    /**
     * convenient method to close result set and statement
     * @param rs result set
     * @param st statement
     * @see #close(ResultSet)
     * @see #close(Statement)
     */
    public static void close(ResultSet rs, Statement st) {
    	close(rs);
    	close(st);
    	rs = null;
    	st = null;
    }

    /**
     * convenient method to close a {@link POResultSet}
     * @param rs result set
     * @see POResultSet#close()
     */
    public static void close(POResultSet<?> rs) {
    	if (rs != null)
    		rs.close();
    }

	/**
	 * Try to get the SQLException from Exception
	 * @param e Exception
	 * @return SQLException if found or provided exception elsewhere
	 */
    public static Exception getSQLException(Exception e)
    {
    	Throwable e1 = e;
    	while (e1 != null)
    	{
	    	if (e1 instanceof SQLException)
	    		return (SQLException)e1;
	    	e1 = e1.getCause();
    	}
    	return e;
    }

	/** Quote			*/
	private static final char QUOTE = '\'';

	
    public static int getSQLValue (String trxName, String sql)
    {
    	return getSQLValue(trxName, sql, new Object[]{});
    }
    public static int getSQLValue (String trxName, String sql, int int_param1)
    {
    	return getSQLValue(trxName, sql, new Object[]{int_param1});
    }
    public static int getSQLValue (String trxName, String sql, int int_param1, int int_param2)
    {
    	return getSQLValue(trxName, sql, new Object[]{int_param1, int_param2});
    }
    public static int getSQLValue (String trxName, String sql, String str_param1)
    {
    	return getSQLValue(trxName, sql, new Object[]{str_param1});
    }
    public static int getSQLValue (String trxName, String sql, int int_param1, String str_param2)
    {
    	return getSQLValue(trxName, sql, new Object[]{int_param1, str_param2});
    }
    public static String getSQLValueString (String trxName, String sql, int int_param1)
    {
    	return getSQLValueString(trxName, sql, new Object[]{int_param1});
    }
    public static BigDecimal getSQLValueBD (String trxName, String sql, int int_param1)
    {
    	return getSQLValueBD(trxName, sql, new Object[]{int_param1});
    }

	
	public static ValueNamePair[] getValueNamePairs(String sql, boolean optional, List<Object> params)
	{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<ValueNamePair> list = new ArrayList<ValueNamePair>();
        if (optional)
        {
            list.add (ValueNamePair.EMPTY);
        }
        try
        {
            pstmt = DB.prepareStatement(sql, null);
            setParameters(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                list.add(new ValueNamePair(rs.getString(1), rs.getString(2)));
            }
        }
        catch (SQLException e)
        {
            throw new DBException(e, sql);
        }
        finally
        {
            close(rs, pstmt);
            rs = null; pstmt = null;
        }
		return list.toArray(new ValueNamePair[list.size()]);
	}

	
	public static KeyNamePair[] getKeyNamePairs(String sql, boolean optional, List<Object> params)
	{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        if (optional)
        {
            list.add (KeyNamePair.EMPTY);
        }
        try
        {
            pstmt = DB.prepareStatement(sql, null);
            setParameters(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (SQLException e)
        {
            throw new DBException(e, sql);
        }
        finally
        {
            close(rs, pstmt);
            rs = null; pstmt = null;
        }
		return list.toArray(new KeyNamePair[list.size()]);
	}

	
	private static void verifyTrx(String trxName, String sql) {
		if (trxName != null && Trx.get(trxName, false) == null) {
			String msg = "Transaction closed or never opened ("+trxName+") => (maybe timed out)";
			log.severe(msg); // severe
			throw new DBException(msg);
		}
	}

	/**
	 * @param tableName
	 * @return true if table or view with name=tableName exists in db
	 */
	public static boolean isTableOrViewExists(String tableName) {
		Connection conn = getConnectionRO();
		ResultSet rs = null;
		try {
			DatabaseMetaData metadata = conn.getMetaData();
			String tblName;
			if (metadata.storesUpperCaseIdentifiers())
				tblName = tableName.toUpperCase();
			else if (metadata.storesLowerCaseIdentifiers())
				tblName = tableName.toLowerCase();
			else
				tblName = tableName;
			rs = metadata.getTables(null, null, tblName, null);
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			try {
				conn.close();
			} catch (SQLException e) {}
		}
		return false;
	}

  
	public static List<Object> getSQLValueObjectsEx(String trxName, String sql, Object... params) {
		List<Object> retValue = new ArrayList<Object>();
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
    		if (rs.next()) {
    			for (int i=1; i<=rsmd.getColumnCount(); i++) {
    				Object obj = rs.getObject(i);
        			if (rs.wasNull())
        				retValue.add(null);
        			else
        				retValue.add(obj);
    			}
    		} else {
    			retValue = null;
    		}
    	}
    	catch (SQLException e)
    	{
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	return retValue;
	}
	
	
	public static List<List<Object>> getSQLArrayObjectsEx(String trxName, String sql, Object... params) {
		List<List<Object>> rowsArray = new ArrayList<List<Object>>();
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
    		while (rs.next()) {
    			List<Object> retValue = new ArrayList<Object>();
    			for (int i=1; i<=rsmd.getColumnCount(); i++) {
    				Object obj = rs.getObject(i);
        			if (rs.wasNull())
        				retValue.add(null);
        			else
        				retValue.add(obj);
    			}
    			rowsArray.add(retValue);
    		}
    	}
    	catch (SQLException e)
    	{
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	if (rowsArray.size() == 0)
    		return null;
    	return rowsArray;
	}
	
	public static List<Object> getSQLObjectsEx(String trxName, String sql, Object... params) {
		List<Object> rowsArray = new ArrayList<Object>();
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		setParameters(pstmt, params);
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			Object obj = rs.getObject(1);
    			if (rs.wasNull())
    				rowsArray.add(null);
    			else
    				rowsArray.add(obj);
    		}
    	}
    	catch (SQLException e)
    	{
    		throw new DBException(e, sql);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	if (rowsArray.size() == 0)
    		return null;
    	return rowsArray;
	}

	/**	Read Replica Statements List	*/
	private static final List<PreparedStatement> readReplicaStatements = Collections.synchronizedList(new ArrayList<PreparedStatement>());

	
	
	public static String inClauseForCSV(String columnName, String csv) 
	{
		StringBuilder builder = new StringBuilder();
		builder.append(columnName).append(" IN (");
		String[] values = csv.split("[,]");
		for(int i = 0; i < values.length; i++)
		{
			if (i > 0)
				builder.append(",");
			String key = values[i];
			if (columnName.endsWith("_ID")) 
			{
				builder.append(key);
			}
			else
			{
				if (key.startsWith("\"") && key.endsWith("\"")) 
				{
					key = key.substring(1, key.length()-1);
				}
				builder.append(TO_STRING(key));
			}
		}
		builder.append(")");
		return builder.toString();
	}
	
	
	public static String intersectClauseForCSV(String columnName, String csv)
	{
		return getDatabase().intersectClauseForCSV(columnName, csv);
	}
	
	public static Object getSQLValueObject (String trxName, String sql, Object... params)
    {
    	Object retValue = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try
    	{
    		pstmt = prepareStatement(sql, trxName);
    		if (params != null && params.length > 0)
    		{
	    		for (int i = 0; i < params.length; i++) 
	    		{
	    			pstmt.setObject(i+1, params[i]);
	    		}
    		}
    		rs = pstmt.executeQuery();
    		if (rs.next())
    			retValue = rs.getObject(1);
    		else
    			log.info("No Value " + sql);
    	}
    	catch (Exception e)
    	{
    		log.log(Level.SEVERE, sql, e);
    	}
    	finally
    	{
    		close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
    	return retValue;
    }
	
	public static String excuteBatch(String sql, List<List<Object>> paramsList, String trxName) {
		String errorString = null;
		CPreparedStatement pstmt_Update = null;
		int size = paramsList.size();
		List<Object> lst = null;
		int j = 0;
		int i = 0;
		try {
			pstmt_Update = DB.prepareStatement(sql, trxName);

			for (i = 0; i < size; i++) {		
				lst = paramsList.get(i);
				for (j = 0; j < lst.size(); j++) {
					pstmt_Update.setObject(j + 1, lst.get(j));
				}
				pstmt_Update.addBatch();
			}
			pstmt_Update.executeBatch();

		} catch (SQLException e) {
			errorString = e.getMessage();
			e.printStackTrace();
		} finally {
			close(pstmt_Update);
			pstmt_Update = null;
		} 
		
		return errorString;
	}
	
	public static CallableStatement prepareCallExtend(String SQL, String trxName)
	{
		if (SQL == null || SQL.length() == 0)
			throw new IllegalArgumentException("Required parameter missing - " + SQL);
		Connection conn = s_cc.getConnectionExtend(true, 0);
		CallableStatement ps = null;
		try {
			ps = conn.prepareCall(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	public static PreparedStatement prepareStatementExtend(String SQL, String trxName)
	{
		if (SQL == null || SQL.length() == 0)
			throw new IllegalArgumentException("Required parameter missing - " + SQL);
		Connection conn = s_cc.getConnectionExtend(true, 0);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return ps;
	}
}	//	DB
