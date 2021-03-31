
package org.compiere.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.adempiere.base.Service;
import org.compiere.interfaces.Server;
import org.compiere.util.CLogger;
import org.compiere.util.Ini;

public class CConnection implements Serializable, Cloneable
{
	private static final long serialVersionUID = -858558852550858165L;

	private volatile static CConnection	s_cc = null;

	private static final CLogger 		log = CLogger.getCLogger (CConnection.class);

	public synchronized static CConnection get ()
	{
		if (s_cc == null)
		{
			String attributes = Ini.getProperty (Ini.P_CONNECTION);
			String attributesSecond = Ini.getProperty (Ini.P_CONNECTION_SECOND);
			s_cc = new CConnection (null);
			s_cc.setAttributes (attributes);
			s_cc.setAttributesSecond(attributesSecond);
			if (log.isLoggable(Level.FINE)) log.fine(s_cc.toString());
		}

		return s_cc;
	}	//	get

	/**
	 *  Get/Set default client/server Connection
	 *  @param apps_host optional apps host for new connections
	 *  @return Connection Descriptor
	 *  @deprecated
	 */
	public synchronized static CConnection get (String apps_host)
	{
		return get();
	} 	//  get


	
	public static CConnection get (String type, String db_host, int db_port, String db_name)
	{
		return get (type, db_host, db_port, db_name, null, null);
	} 	//  get

	public static CConnection get (String type, String db_host, int db_port,
	  String db_name, String db_uid, String db_pwd)
	{
		CConnection cc = new CConnection (db_host);
		cc.setAppsHost (db_host); //  set Apps=DB
		cc.setType (type);
		cc.setDbHost (db_host);
		cc.setDbPort (db_port);
		cc.setDbName (db_name);
		//
		if (db_uid != null)
			cc.setDbUid (db_uid);
		if (db_pwd != null)
			cc.setDbPwd (db_pwd);
		return cc;
	}	//  get



	public CConnection (String host)
	{
		if (host != null)
		{
			m_apps_host = host;
			m_db_host = host;
		}
	} 	//  CConnection

	private String 		m_name = "Standard";
	private String 		m_apps_host = "MyAppsServer";
	private String 		m_type = "";
	private String 		m_db_host = "MyDBServer";
	private int m_db_port = 0;
	private String 		m_db_name = "MyDBName";
	private boolean 	m_bequeath = false;
	private boolean 	m_firewall = false;
	private String 		m_fw_host = "";
	private int 		m_fw_port = 0;
	private String 		m_db_uid = "eone";
	private String 		m_db_pwd = "eone";
	private AdempiereDatabase m_db = null;
	private AdempiereDatabase m_db_ex = null;
	private Exception 	m_dbException = null;
	private boolean 	m_okDB = false;
	private String[] 	m_info = new String[2];

	private String 		m_version = null;

	private DataSource	m_ds = null;
	private String		m_dbInfo = null;
	private int m_webPort;
	private int m_sslPort;
	
	//Bo sung lay thong tin DB second
	private String hostSecond = "";
	private String portSecond = "";
	private String dbNameSecond = "";
	private String dbUserSecond = "";
	private String dbPassSecond = "";
	private String dbTypeSecond = "";

	
	public String getDbTypeSecond() {
		return dbTypeSecond;
	}

	public void setDbTypeSecond(String dbTypeSecond) {
		this.dbTypeSecond = dbTypeSecond;
	}

	public String getHostSecond() {
		return hostSecond;
	}

	public void setHostSecond(String hostSecond) {
		this.hostSecond = hostSecond;
	}

	public String getPortSecond() {
		return portSecond;
	}

	public void setPortSecond(String portSecond) {
		this.portSecond = portSecond;
	}

	public String getDbNameSecond() {
		return dbNameSecond;
	}

	public void setDbNameSecond(String dbNameSecond) {
		this.dbNameSecond = dbNameSecond;
	}

	public String getDbUserSecond() {
		return dbUserSecond;
	}

	public void setDbUserSecond(String dbUserSecond) {
		this.dbUserSecond = dbUserSecond;
	}

	public String getDbPassSecond() {
		return dbPassSecond;
	}

	public void setDbPassSecond(String dbPassSecond) {
		this.dbPassSecond = dbPassSecond;
	}

	public String getName ()
	{
		return m_name;
	}

	/**
	 *  Set Name
	 *  @param name connection name
	 */
	public void setName (String name)
	{
		m_name = name;
	}	//  setName

	/**
	 *  Set Name
	 */
	public void setName ()
	{
		m_name = toString ();
	} 	//  setName


	/*************
	 *  Get Application Host
	 *  @return apps host
	 */
	public String getAppsHost ()
	{
		return m_apps_host;
	}

	/**
	 *  Set Application Host
	 *  @param apps_host apps host
	 */
	public void setAppsHost (String apps_host)
	{
		m_apps_host = apps_host;
		m_name = toString ();
	}

	/**
	 * @return web port
	 */
	public int getWebPort()
	{
		return m_webPort;
	}

	/**
	 * set web port
	 * @param webPort
	 */
	public void setWebPort(int webPort)
	{
		m_webPort = webPort;
	}

	/**
	 * Set Web Port
	 * @param webPortString web port as String
	 */
	public void setWebPort (String webPortString)
	{
		try
		{
			if (webPortString == null || webPortString.length() == 0)
				;
			else
				setWebPort (Integer.parseInt (webPortString));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}

	/**
	 * @return ssl port
	 */
	public int getSSLPort()
	{
		return m_sslPort;
	}

	/**
	 * set ssl port
	 * @param sslPort
	 */
	public void setSSLPort(int sslPort)
	{
		m_sslPort = sslPort;
	}

	/**
	 * Set SSL Port
	 * @param sslPortString web port as String
	 */
	public void setSSLPort (String sslPortString)
	{
		try
		{
			if (sslPortString == null || sslPortString.length() == 0)
				;
			else
				setSSLPort (Integer.parseInt (sslPortString));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}

	/**
	 * 	Get Server
	 * 	@return Server
	 */
	public Server getServer()
	{
		return Service.locator().locate(Server.class).getService();
	}	//	getServer


	/**
	 *  Get Apps Server Version
	 *  @return db host name
	 */
	public String getServerVersion ()
	{
		return m_version;
	}	//	getServerVersion


	/*************
	 *  Get Database Host name
	 *  @return db host name
	 */
	public String getDbHost ()
	{
		return m_db_host;
	}	//	getDbHost

	/**
	 *  Set Database host name
	 *  @param db_host db host
	 */
	public void setDbHost (String db_host)
	{
		m_db_host = db_host;
		m_name = toString ();
		m_okDB = false;
	}	//	setDbHost

	/**
	 *  Get Database Name (Service Name)
	 *  @return db name
	 */
	public String getDbName ()
	{
		return m_db_name;
	}	//	getDbName

	/**
	 *  Set Database Name (Service Name)
	 *  @param db_name db name
	 */
	public void setDbName (String db_name)
	{
		m_db_name = db_name;
		m_name = toString ();
		m_okDB = false;
	}	//	setDbName

	/**
	 * 	Get DB Port
	 * 	@return port
	 */
	public int getDbPort ()
	{
		return m_db_port;
	}	//	getDbPort

	/**
	 * Set DB Port
	 * @param db_port db port
	 */
	public void setDbPort (int db_port)
	{
		m_db_port = db_port;
		m_okDB = false;
	}	//	setDbPort

	/**
	 * Set DB Port
	 * @param db_portString db port as String
	 */
	public void setDbPort (String db_portString)
	{
		try
		{
			if (db_portString == null || db_portString.length() == 0)
				;
			else
				setDbPort (Integer.parseInt (db_portString));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	} 	//  setDbPort

	/**
	 *  Get Database Password
	 *  @return db password
	 */
	public String getDbPwd ()
	{
		return m_db_pwd;
	}	//	getDbPwd

	/**
	 *  Set DB password
	 *  @param db_pwd db user password
	 */
	public void setDbPwd (String db_pwd)
	{
		m_db_pwd = db_pwd;
		m_okDB = false;
	}	//	setDbPwd

	/**
	 *  Get Database User
	 *  @return db user
	 */
	public String getDbUid ()
	{
		return m_db_uid;
	}	//	getDbUid

	/**
	 *  Set Database User
	 *  @param db_uid db user id
	 */
	public void setDbUid (String db_uid)
	{
		m_db_uid = db_uid;
		m_name = toString ();
		m_okDB = false;
	}	//	setDbUid

	/**
	 * 	RMI over HTTP
	 *
	 *  Deprecated, always return false
	 * 	@return true if RMI over HTTP (Wan Connection Profile)
	 *  @deprecated
	 */
	public boolean isRMIoverHTTP ()
	{
		return false;
	}	//	isRMIoverHTTP

	/**
	 *  Should objects be created on Server ?
	 *  @return true if client and VPN/WAN
	 *  @deprecated
	 */
	public boolean isServerObjects()
	{
		return false;
	}   //  isServerObjects

	/**
	 *  Should objects be created on Server ?
	 *  @return true if client and Terminal/VPN/WAN
	 *  @deprecated
	 */
	public boolean isServerProcess()
	{
		return false;
	}   //  isServerProcess

	/**
	 *  Is this a Terminal Server ?
	 *  @return true if client and Terminal
	 *  @deprecated
	 */
	public boolean isTerminalServer()
	{
		return false;
	}   //  isTerminalServer

	/**
	 *  Is DB via Firewall
	 *  @return true if via firewall
	 */
	public boolean isViaFirewall ()
	{
		return m_firewall;
	}

	/**
	 * Method setViaFirewall
	 * @param viaFirewall boolean
	 */
	public void setViaFirewall (boolean viaFirewall)
	{
		m_firewall = viaFirewall;
		m_okDB = false;
	}

	/**
	 * Method setViaFirewall
	 * @param viaFirewallString String
	 */
	public void setViaFirewall (String viaFirewallString)
	{
		try
		{
			setViaFirewall (Boolean.valueOf (viaFirewallString).booleanValue ());
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}

	/**
	 * Method getFwHost
	 * @return String
	 */
	public String getFwHost ()
	{
		return m_fw_host;
	}

	/**
	 * Method setFwHost
	 * @param fw_host String
	 */
	public void setFwHost (String fw_host)
	{
		m_fw_host = fw_host;
		m_okDB = false;
	}

	/**
	 * Get Firewall port
	 * @return firewall port
	 */
	public int getFwPort ()
	{
		return m_fw_port;
	}

	/**
	 * Set Firewall port
	 * @param fw_port firewall port
	 */
	public void setFwPort (int fw_port)
	{
		m_fw_port = fw_port;
		m_okDB = false;
	}

	/**
	 * Set Firewall port
	 * @param fw_portString firewall port as String
	 */
	public void setFwPort (String fw_portString)
	{
		try
		{
			if (fw_portString == null || fw_portString.length() == 0)
				;
			else
				setFwPort (Integer.parseInt (fw_portString));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}

	/**
	 *  Is it a bequeath connection
	 *  @return true if bequeath connection
	 */
	public boolean isBequeath ()
	{
		return m_bequeath;
	}

	/**
	 * Set Bequeath
	 * @param bequeath bequeath connection
	 */
	public void setBequeath (boolean bequeath)
	{
		m_bequeath = bequeath;
		m_okDB = false;
	}

	/**
	 * Set Bequeath
	 * @param bequeathString bequeath connection as String (true/false)
	 */
	public void setBequeath (String bequeathString)
	{
		try
		{
			setBequeath (Boolean.valueOf (bequeathString).booleanValue ());
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}	//	setBequeath

	/**
	 *  Get Database Type
	 *  @return database type
	 */
	public String getType ()
	{
		return m_type;
	}

	/**
	 *  Set Database Type and default settings.
	 *  Checked against installed databases
	 *  @param type database Type, e.g. Database.DB_ORACLE
	 */
	public void setType (String type)
	{
		if (Database.getDatabase(type) != null)
		{
			m_type = type;
			m_okDB = false;
		}
		//  Oracle
		if (isOracle ())
		{
			if (getDbPort () != Database.DB_ORACLE_DEFAULT_PORT)
				setDbPort (Database.DB_ORACLE_DEFAULT_PORT);
			setFwPort (Database.DB_ORACLE_DEFAULT_CM_PORT);
		}
		else
		{
			setBequeath (false);
			setViaFirewall (false);
		}

        // begin vpj-cd e-evolution 09 ene 2006
		//  PostgreSQL
		if (isPostgreSQL ())
		{
			if (getDbPort () != Database.DB_POSTGRESQL_DEFAULT_PORT)
				setDbPort (Database.DB_POSTGRESQL_DEFAULT_PORT);
		}
		//end vpj-cd e-evolution 09 ene 2006
	} 	//  setType

	/**
	 *  Supports BLOB
	 *  @return true if BLOB is supported
	 */
	public boolean supportsBLOB ()
	{
		return m_db.supportsBLOB ();
	} //  supportsBLOB


	/**
	 *  Is Oracle DB
	 *  @return true if Oracle
	 */
	public boolean isOracle ()
	{
		return Database.DB_ORACLE.equals (m_type);
	} 	//  isOracle

	/**
	 *  Is PostgreSQL DB
	 *  @return true if PostgreSQL
	 */
	public boolean isPostgreSQL ()
	{
		return Database.DB_POSTGRESQL.equals (m_type);
	} 	//  isPostgreSQL

	/**
	 *  Is Database Connection OK
	 *  @return true if database connection is OK
	 */
	public boolean isDatabaseOK ()
	{
		return m_okDB;
	} 	//  isDatabaseOK

	/**************************************************************************
	 *  Create DB Connection
	 * @return data source != null
	 */
	public boolean setDataSource()
	{
		if (m_ds == null && Ini.isClient())
		{
			AdempiereDatabase getDB = getDatabase(); 
			if (getDB != null)	//	no db selected
				m_ds = getDB.getDataSource(this);
		}
		return m_ds != null;
	} 	//	setDataSource

	/**
	 * 	Set Data Source
	 *	@param ds data source
	 *	@return data source != null
	 */
	public boolean setDataSource(DataSource ds)
	{
		if (ds == null && m_ds != null)
			getDatabase().close();
		m_ds = ds;
		return m_ds != null;
	} 	//	setDataSource

	/**
	 *  Get Server Connection
	 *  @return DataSource
	 */
	public DataSource getDataSource ()
	{
		return m_ds;
	} 	//	getDataSource

	/**
	 *  Has Server Connection
	 *  @return true if DataSource exists
	 */
	public boolean isDataSource ()
	{
		return m_ds != null;
	} 	//	isDataSource


	/**************************************************************************
	 *  Test Database Connection.
	 *  -- Example --
	 *  Database: PostgreSQL - 7.1.3
	 *  Driver:   PostgreSQL Native Driver - PostgreSQL 7.2 JDBC2
	 *  -- Example --
	 *  Database: Oracle - Oracle8i Enterprise Edition Release 8.1.7.0.0 - Production With the Partitioning option JServer Release 8.1.7.0.0 - Production
	 *  Driver:   Oracle JDBC driver - 9.0.1.1.0
	 *  @param retest
	 *  @return Exception or null
	 */
	public Exception testDatabase(boolean retest)
	{
		if (!retest && m_ds != null && m_okDB)
			return null;

		if (getDatabase() != null)
			getDatabase().close();
		m_ds = null;
		setDataSource();
		//  the actual test
		Connection conn = getConnection (true,
			Connection.TRANSACTION_READ_COMMITTED);
		if (conn != null)
		{
			try
			{
				readInfo(conn);
				conn.close ();
			}
			catch (Exception e)
			{
				log.severe (e.toString());
				return e;
			}
		}
		return m_dbException; //  from opening
	} 	//  testDatabase

	public void readInfo(Connection conn) throws SQLException {
		DatabaseMetaData dbmd = conn.getMetaData ();
		m_info[0] = "Database=" + dbmd.getDatabaseProductName ()
					+ " - " + dbmd.getDatabaseProductVersion ();
		m_info[0] = m_info[0].replace ('\n', ' ');
		m_info[1] = "Driver  =" + dbmd.getDriverName ()
					+ " - " + dbmd.getDriverVersion ();
		if (isDataSource())
			m_info[1] += " - via DataSource";
		m_info[1] = m_info[1].replace ('\n', ' ');
		if (log.isLoggable(Level.CONFIG)) log.config(m_info[0] + " - " + m_info[1]);
	}

	/*************************************************************************
	 *  Short String representation
	 *  @return appsHost{dbHost-dbName-uid}
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder (m_apps_host);
		sb.append ("{").append (m_db_host)
		  .append ("-").append (m_db_name)
		  .append ("-").append (m_db_uid)
		  .append ("}");
		return sb.toString ();
	} 	//  toString

	/**
	 * 	Detail Info
	 *	@return info
	 */
	public String toStringDetail ()
	{
		StringBuilder sb = new StringBuilder (m_apps_host);
		sb.append ("{").append (m_db_host)
		  .append ("-").append (m_db_name)
		  .append ("-").append (m_db_uid)
		  .append ("}");
		//
		Connection conn = getConnection (true,
			Connection.TRANSACTION_READ_COMMITTED);
		if (conn != null)
		{
			try
			{
				DatabaseMetaData dbmd = conn.getMetaData ();
				sb.append("\nDatabase=" + dbmd.getDatabaseProductName ()
							+ " - " + dbmd.getDatabaseProductVersion());
				sb.append("\nDriver  =" + dbmd.getDriverName ()
							+ " - " + dbmd.getDriverVersion ());
				if (isDataSource())
					sb.append(" - via DS");
				conn.close ();
			}
			catch (Exception e)
			{
			}
		}
		conn = null;
		return sb.toString ();
	} 	//  toStringDetail

	/**
	 * 	Get DB Version Info
	 *	@return info
	 */
	public String getDBInfo()
	{
		if (m_dbInfo != null)
			return m_dbInfo;
		StringBuilder sb = new StringBuilder ();
		Connection conn = getConnection (true,
			Connection.TRANSACTION_READ_COMMITTED);
		if (conn != null)
		{
			try
			{
				DatabaseMetaData dbmd = conn.getMetaData ();
				sb.append(dbmd.getDatabaseProductVersion())
					.append(";").append(dbmd.getDriverVersion());
				if (isDataSource())
					sb.append(";DS");
				conn.close ();
				m_dbInfo = sb.toString ();
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
				sb.append(e.getLocalizedMessage());
			}
		}
		conn = null;
		return sb.toString();
	} 	//  toStringDetail


	/**
	 *  String representation.
	 *  Used also for Instanciation
	 *  @return string representation
	 *	@see #setAttributes(String) setAttributes
	 */
	public String toStringLong ()
	{
		StringBuilder sb = new StringBuilder ("CConnection[");
		sb.append ("name=").append (escape(m_name))
		  .append (",AppsHost=").append (escape(m_apps_host))
		  .append (",WebPort=").append (m_webPort)
		  .append (",SSLPort=").append (m_sslPort)
		  .append (",type=").append (escape(m_type))
		  .append (",DBhost=").append (escape(m_db_host))
		  .append (",DBport=").append (m_db_port)
		  .append (",DBname=").append (escape(m_db_name))
		  .append (",BQ=").append (m_bequeath)
		  .append (",FW=").append (m_firewall)
		  .append (",FWhost=").append (escape(m_fw_host))
		  .append (",FWport=").append (m_fw_port)
		  .append (",UID=").append (escape(m_db_uid))
		  .append (",PWD=").append (escape(m_db_pwd))
		  .append("]");
		  ;		//	the format is read by setAttributes
		return sb.toString ();
	}	//  toStringLong
	
	public String toStringLongEx ()
	{
		//[DBhost:103.74.121.159,DBport:1433,DBname:Sale,UID:sa,PWD:Dss123@]
		StringBuilder sb = new StringBuilder ("[");
		sb.append ("DBhost:").append (escape(hostSecond))
		  .append (",DBport:").append (escape(portSecond))
		  .append (",DBname:").append (dbNameSecond)
		  .append (",UID:").append (dbUserSecond)
		  .append (",PWD:").append (escape(dbPassSecond))
		  .append (",TypeEX:").append (escape(dbTypeSecond))
		  .append("]");
		  ;		
		return sb.toString ();
	}

	private String escape(String value) {
		if (value == null)
			return null;

		// use html like escape sequence to escape = and ,
		value = value.replace("=", "&eq;");
		value = value.replace(",", "&comma;");
		return value;
	}

	/**
	 *  Set Attributes from String (pares toStringLong())
	 *  @param attributes attributes
	 */
	private void setAttributes (String attributes)
	{
		try
		{
			attributes = attributes.substring(attributes.indexOf("[")+1, attributes.length() - 1);
			String[] pairs= attributes.split("[,]");
			for(String pair : pairs)
			{
				String[] pairComponents = pair.split("[=]");
				String key = pairComponents[0];
				String value = pairComponents.length == 2 ? unescape(pairComponents[1]) : "";
				if ("name".equalsIgnoreCase(key))
				{
					setName(value);
				}
				else if ("AppsHost".equalsIgnoreCase(key))
				{
					setAppsHost(value);
				}
				else if ("type".equalsIgnoreCase(key))
				{
					setType(value);
				}
				else if ("DBhost".equalsIgnoreCase(key))
				{
					setDbHost(value);
				}
				else if ("DBport".equalsIgnoreCase(key))
				{
					setDbPort(value);
				}
				else if ("DbName".equalsIgnoreCase(key))
				{
					setDbName(value);
				}
				else if ("BQ".equalsIgnoreCase(key))
				{
					setBequeath(value);
				}
				else if ("FW".equalsIgnoreCase(key))
				{
					setViaFirewall(value);
				}
				else if ("FWhost".equalsIgnoreCase(key))
				{
					setFwHost(value);
				}
				else if ("FWport".equalsIgnoreCase(key))
				{
					setFwPort(value);
				}
				else if ("UID".equalsIgnoreCase(key))
				{
					setDbUid(value);
				}
				else if ("PWD".equalsIgnoreCase(key))
				{
					setDbPwd(value);
				}
				else if ("WebPort".equalsIgnoreCase(key))
				{
					setWebPort(value);
				}
				else if ("SSLPort".equalsIgnoreCase(key))
				{
					setSSLPort(value);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, attributes + " - " + e.toString (), e);
		}
	}	//  setAttributes
	
	private void setAttributesSecond (String attributes)
	{
		try
		{
			attributes = attributes.substring(attributes.indexOf("[")+1, attributes.length() - 1);
			String[] pairs= attributes.split("[,]");
			for(String pair : pairs)
			{
				String[] pairComponents = pair.split("[:]");
				String key = pairComponents[0];
				String value = pairComponents.length == 2 ? unescape(pairComponents[1]) : "";
				if ("DBhost".equalsIgnoreCase(key))
				{
					setHostSecond(value);
				}
				else if ("DBport".equalsIgnoreCase(key))
				{
					setPortSecond(value);
				}
				else if ("DbName".equalsIgnoreCase(key))
				{
					setDbNameSecond(value);
				}
				else if ("UID".equalsIgnoreCase(key))
				{
					setDbUserSecond(value);
				}
				else if ("PWD".equalsIgnoreCase(key))
				{
					setDbPassSecond(value);
				}
				else if ("TypeEX".equalsIgnoreCase(key))
				{
					setDbTypeSecond(value);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, attributes + " - " + e.toString (), e);
		}
	}

	private String unescape(String value) {
		value = value.replace("&eq;", "=");
		value = value.replace("&comma;", ",");
		return value;
	}

	/**
	 *  Equals
	 *  @param o object
	 *  @return true if o equals this
	 */
	public boolean equals (Object o)
	{
		if (o instanceof CConnection)
		{
			CConnection cc = (CConnection)o;
			if (cc.getAppsHost().equals (m_apps_host)
			  && cc.getDbHost().equals (m_db_host)
			  && cc.getDbPort() == m_db_port
			  && cc.getDbName().equals(m_db_name)
			  && cc.getType().equals(m_type)
			  && cc.getDbUid().equals(m_db_uid)
			  && cc.getDbPwd().equals(m_db_pwd))
				return true;
		}
		return false;
	}	//  equals

	/**
	 *  Get Info.
	 *  - Database, Driver, Status Info
	 *  @return info
	 */
	public String getInfo ()
	{
		StringBuilder sb = new StringBuilder (m_info[0] != null ? m_info[0] : "");
		sb.append (" - ").append (m_info[1] != null ? m_info[1] : "")
		  .append ("\n").append (getDatabase ().toString ());
		
		sb.append ("\nDatabaseOK=").append (isDatabaseOK ());
		
		return sb.toString ();
	}	//  getInfo


	/*************************************************************************
	 *  Hashcode
	 *  @return hashcode of name
	 */
	public int hashCode ()
	{
		return m_name.hashCode ();
	} 	//  hashCode

	/**
	 *  Get Database
	 *  @return database
	 */
	public AdempiereDatabase getDatabase ()
	{
		//  different driver
		if (m_db != null && !m_db.getName ().equals (m_type))
			m_db = null;

		if (m_db == null)
		{
			try
			{
		         m_db = Database.getDatabase(m_type);
		         if (m_db != null)		//	test class loader ability
		        	 m_db.getDataSource(this);
			}
			catch (NoClassDefFoundError ee)
			{
				System.err.println("Environment Error - Check eone.properties - " + ee);
				if (Ini.isClient())
				{
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog
						(null, "There is a configuration error:\n" + ee
							+ "\nDo you want to reset the saved configuration?",
							"Adempiere Configuration Error",
							JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE))
						Ini.deletePropertyFile();
				}
				System.exit (1);
			}
			catch (Exception e)
			{
				log.severe(e.toString ());
			}
		}
		return m_db;
	} 	//  getDatabase

	
	public AdempiereDatabase getDatabaseExtend ()
	{
		//  different driver
		if (m_db_ex != null && !m_db_ex.getName ().equals (dbTypeSecond))
			m_db_ex = null;

		if (m_db_ex == null)
		{
			try
			{
				m_db_ex = Database.getDatabase(dbTypeSecond);
		         if (m_db_ex != null)		//	test class loader ability
		        	 m_db_ex.getDataSource(this);
			}
			catch (NoClassDefFoundError ee)
			{
				System.err.println("Environment Error - Check eone.properties - " + ee);
				if (Ini.isClient())
				{
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog
						(null, "There is a configuration error:\n" + ee
							+ "\nDo you want to reset the saved configuration?",
							"Adempiere Configuration Error",
							JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE))
						Ini.deletePropertyFile();
				}
				System.exit (1);
			}
			catch (Exception e)
			{
				log.severe(e.toString ());
			}
		}
		return m_db_ex;
	}
	/**
	 *  Get Connection String
	 *  @return connection string
	 */
	public String getConnectionURL ()
	{
		getDatabase (); //  updates m_db
		if (m_db != null)
			return m_db.getConnectionURL (this);
		else
			return "";
	} 	//  getConnectionURL

	
	public Connection getConnection (boolean autoCommit, int transactionIsolation)
	{
		Connection conn = null;
		m_dbException = null;
		m_okDB = false;
		//
		getDatabase (); //  updates m_db
		if (m_db == null)
		{
			m_dbException = new IllegalStateException("No Database Connector");
			return null;
		}
		//

		try
		{
		
			try
			{
				conn = m_db.getCachedConnection(this, autoCommit, transactionIsolation);
			}
			catch (Exception e)
			{
				//ee = e;
			}
			if (conn != null)
			{
				if (conn.getTransactionIsolation() != transactionIsolation)
					conn.setTransactionIsolation (transactionIsolation);
				if (conn.getAutoCommit() != autoCommit)
					conn.setAutoCommit (autoCommit);
				m_okDB = true;
			}
		}
		catch (UnsatisfiedLinkError ule)
		{
			String msg = ule.getLocalizedMessage()
				+ " -> Did you set the LD_LIBRARY_PATH ? - " + getConnectionURL();
			m_dbException = new Exception(msg);
			log.severe(msg);
		}
		catch (SQLException ex)
		{
			m_dbException = ex;
			try
			{
				log.severe(getConnectionURL ()
					+ ", (2) AutoCommit=" + conn.getAutoCommit() + "->" + autoCommit
					+ ", TrxIso=" + getTransactionIsolationInfo(conn.getTransactionIsolation()) + "->" + getTransactionIsolationInfo(transactionIsolation)
					+ " - " + ex.getMessage());
			}
			catch (Exception ee)
			{
				log.severe(getConnectionURL ()
					+ ", (3) AutoCommit=" + autoCommit + ", TrxIso=" + getTransactionIsolationInfo(transactionIsolation)
					+ " - " + ex.getMessage());
			}
		}
		catch (Exception ex)
		{
			m_dbException = ex;
			System.err.println(getConnectionURL() + " - " + ex.getLocalizedMessage());
		}
		return conn;
	}	//  getConnection

	//Dung cho connection den database thu hai
	public Connection getConnectionExtend (boolean autoCommit, int transactionIsolation)
	{
		Connection conn = null;
		m_dbException = null;
		m_okDB = false;
		//
		getDatabaseExtend(); //  updates m_db
		if (m_db_ex == null)
		{
			m_dbException = new IllegalStateException("No Database Connector");
			return null;
		}
		//

		try
		{
		
			try
			{
				conn = m_db_ex.getCachedConnection(this, autoCommit, transactionIsolation);
			}
			catch (Exception e)
			{
				//ee = e;
			}
			if (conn != null)
			{
				if (conn.getTransactionIsolation() != transactionIsolation)
					conn.setTransactionIsolation (transactionIsolation);
				if (conn.getAutoCommit() != autoCommit)
					conn.setAutoCommit (autoCommit);
				m_okDB = true;
			}
		}
		catch (UnsatisfiedLinkError ule)
		{
			String msg = ule.getLocalizedMessage()
				+ " -> Did you set the LD_LIBRARY_PATH ? - " + getConnectionURL();
			m_dbException = new Exception(msg);
			log.severe(msg);
		}
		catch (SQLException ex)
		{
			m_dbException = ex;
			try
			{
				log.severe(getConnectionURL ()
					+ ", (2) AutoCommit=" + conn.getAutoCommit() + "->" + autoCommit
					+ ", TrxIso=" + getTransactionIsolationInfo(conn.getTransactionIsolation()) + "->" + getTransactionIsolationInfo(transactionIsolation)
					+ " - " + ex.getMessage());
			}
			catch (Exception ee)
			{
				log.severe(getConnectionURL ()
					+ ", (3) AutoCommit=" + autoCommit + ", TrxIso=" + getTransactionIsolationInfo(transactionIsolation)
					+ " - " + ex.getMessage());
			}
		}
		catch (Exception ex)
		{
			m_dbException = ex;
			System.err.println(getConnectionURL() + " - " + ex.getLocalizedMessage());
		}
		return conn;
	}

	public Exception getDatabaseException ()
	{
		return m_dbException;
	} 	//  getConnectionException

	
	public InitialContext getInitialContext (boolean useCache)
	{
		return null;
	}	//	getInitialContext

	
	public String convertStatement (String origStatement)
	  throws Exception
	{
		//  make sure we have a good database
		if (m_db != null && !m_db.getName ().equals (m_type))
			getDatabase ();
		if (m_db != null)
			return m_db.convertStatement (origStatement);
		throw new Exception (
		  "CConnection.convertStatement - No Converstion Database");
	}	//  convertStatement

	/**
	 * 	Get Status Info
	 *	@return info
	 */
	public String getStatus()
	{
		StringBuilder sb = new StringBuilder (m_apps_host);
		sb.append ("{").append (m_db_host)
		  .append ("-").append (m_db_name)
		  .append ("-").append (m_db_uid)
		  .append ("}");
		if (m_db != null)
		  sb.append (m_db.getStatus());
		return sb.toString ();
	}	//	getStatus

	/**
	 * 	Get Transaction Isolation Info
	 *	@param transactionIsolation trx iso
	 *	@return clear test
	 */
	public static String getTransactionIsolationInfo(int transactionIsolation)
	{
		if (transactionIsolation == Connection.TRANSACTION_NONE)
			return "NONE";
		if (transactionIsolation == Connection.TRANSACTION_READ_COMMITTED)
			return "READ_COMMITTED";
		if (transactionIsolation == Connection.TRANSACTION_READ_UNCOMMITTED)
			return "READ_UNCOMMITTED";
		if (transactionIsolation == Connection.TRANSACTION_REPEATABLE_READ)
			return "REPEATABLE_READ";
		if (transactionIsolation == Connection.TRANSACTION_READ_COMMITTED)
			return "SERIALIZABLE";
		return "<?" + transactionIsolation + "?>";
	}	//	getTransactionIsolationInfo

	@Override
	public Object clone() throws CloneNotSupportedException {
		CConnection c = (CConnection)super.clone();
		String[] info = new String[2];
		info[0] = m_info[0];
		info[1] = m_info[1];
		c.m_info = info;
		return c;
	}
}	//  CConnection
