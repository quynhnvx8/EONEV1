package eone.base.db;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.logging.Level;

import org.compiere.db.AdempiereDatabase;
import org.compiere.db.Database;
import org.compiere.util.CLogger;

import eone.install.ConfigurationData;
import eone.install.DBConfigStatus;
import eone.install.IDBConfigMonitor;
import eone.install.IDatabaseConfig;


public class ConfigPostgreSQL implements IDatabaseConfig
{

	private final static CLogger log = CLogger.getCLogger(ConfigPostgreSQL.class);

	
	public ConfigPostgreSQL ()
	{
	}	//	ConfigPostgreSQL

	/** Discovered TNS			*/
	private String[] 			p_discovered = null;

	private AdempiereDatabase p_db = Database.getDatabase(Database.DB_POSTGRESQL);

	/**
	 * Init
	 * @param configurationData
	 */
	public void init(ConfigurationData configurationData)
	{
		configurationData.setDatabasePort(String.valueOf(Database.DB_POSTGRESQL_DEFAULT_PORT));
		configurationData.setDatabaseName("eone");
	}	//	init

	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	public String[] discoverDatabases(String selected)
	{
		if (p_discovered != null)
			return p_discovered;
		p_discovered = new String[]{};
		return p_discovered;
	}	//	discoveredDatabases


	/**************************************************************************
	 * 	Test
	 *  @param monitor
	 *  @param data
	 *	@return error message or null if OK
	 */
	public String test(IDBConfigMonitor monitor, ConfigurationData data)
	{
		//	Database Server
		String server = data.getDatabaseServer();
		boolean pass = server != null && server.length() > 0;

		String error = "Not correct: DB Server = " + server;
		InetAddress databaseServer = null;
		try
		{
			if (pass)
				databaseServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		if (monitor != null)
			monitor.update(new DBConfigStatus(DBConfigStatus.DATABASE_SERVER, "ErrorDatabaseServer",
				pass, true, error));
		if (log.isLoggable(Level.INFO)) log.info("OK: Database Server = " + databaseServer);
		data.setProperty(ConfigurationData.EONE_DB_SERVER, databaseServer!=null ? databaseServer.getHostName() : null);
		//store as lower case for better script level backward compatibility
		data.setProperty(ConfigurationData.EONE_DB_TYPE, data.getDatabaseType());
		data.setProperty(ConfigurationData.EONE_DB_PATH, data.getDatabaseType().toLowerCase());

		//	Database Port
		int databasePort = data.getDatabasePort();
		pass = data.testPort (databaseServer, databasePort, true);
		error = "DB Server Port = " + databasePort;
		if (monitor != null)
			monitor.update(new DBConfigStatus(DBConfigStatus.DATABASE_SERVER, "ErrorDatabasePort",
				pass, true, error));
		if (!pass)
			return error;
		if (log.isLoggable(Level.INFO)) log.info("OK: Database Port = " + databasePort);
		data.setProperty(ConfigurationData.EONE_DB_PORT, String.valueOf(databasePort));

		boolean  isDBExists =  data.getDatabaseExists();
		//	JDBC Database Info
		String databaseName = data.getDatabaseName();	//	Service Name
		System.out.println(databaseName);
		
		/*
		String systemPassword = data.getDatabaseSystemPassword();

		//	URL (derived)
		String urlSystem = p_db.getConnectionURL(databaseServer.getHostName(), databasePort,
			p_db.getSystemDatabase(databaseName), p_db.getSystemUser());
		pass = testJDBC(urlSystem, p_db.getSystemUser(), systemPassword);
		error = "Error connecting: " + urlSystem
			+ " - " + p_db.getSystemUser() + "/" + systemPassword;
		if (monitor != null)
			monitor.update(new DBConfigStatus(DBConfigStatus.DATABASE_SYSTEM_PASSWORD, "ErrorJDBC",
				pass, true, error));
		if (!pass) {
			if (isDBExists) {
				log.warning(error);
			} else {
				if ("^TryLocalConnection^".equals(systemPassword)) {
					// Debian installer uses postgres socket domain connection
					log.warning(error);
				} else {
					return error;
				}
			}
		}
		if (log.isLoggable(Level.INFO)) log.info("OK: System Connection = " + urlSystem);
		data.setProperty(ConfigurationData.EONE_DB_SYSTEM, systemPassword);
		*/

		//	Database User Info
		String databaseUser = data.getDatabaseUser();	//	UID
		String databasePassword = data.getDatabasePassword();	//	PWD
		//
		String url= p_db.getConnectionURL(databaseServer.getHostName(), databasePort,
			databaseName, databaseUser);
		//	Ignore result as it might not be imported
		pass = testJDBC(url, databaseUser, databasePassword);
		error = "Database imported? Cannot connect to User: " + databaseUser + "/" + databasePassword;
		if (monitor != null) {
			boolean critical = true;
			if (!isDBExists) {
				critical = false;
			}
			monitor.update(new DBConfigStatus(DBConfigStatus.DATABASE_USER, "ErrorJDBC", pass, critical, error));
		}
		if (pass)
		{
			if (log.isLoggable(Level.INFO)) log.info("OK: Database User = " + databaseUser);
		}
		else
		{
			if (isDBExists) {
			   return error;
			} else {
				log.warning(error);
			}
		}
		data.setProperty(ConfigurationData.EONE_DB_URL, url);
		data.setProperty(ConfigurationData.EONE_DB_NAME, databaseName);
		data.setProperty(ConfigurationData.EONE_DB_USER, databaseUser);
		data.setProperty(ConfigurationData.EONE_DB_PASSWORD, databasePassword);
		data.setProperty(ConfigurationData.EONE_DB_EXISTS, (isDBExists ? "Y" : "N"));		

		return null;
	}	//	test

	/**
	 * 	Test JDBC Connection to Server
	 * 	@param url connection string
	 *  @param uid user id
	 *  @param pwd password
	 * 	@return true if OK
	 */
	private boolean testJDBC (String url, String uid, String pwd)
	{
		try
		{
			@SuppressWarnings("unused")
			Connection conn = p_db.getDriverConnection(url, uid, pwd);
		}
		catch (Exception e)
		{
			log.severe(e.toString());
			return false;
		}
		return true;
	}	//	testJDBC

	@Override
	public String getDatabaseName(String nativeConnectioName) {
		return nativeConnectioName;
	}

	@Override
	public String getName() {
		return Database.DB_POSTGRESQL;
	}

}	//	ConfigPostgreSQL
