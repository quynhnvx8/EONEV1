package org.compiere.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.adempiere.base.equinox.EquinoxExtensionLocator;
import org.adempiere.install.IDBConfigMonitor;
import org.adempiere.install.IDatabaseConfig;
import org.compiere.Adempiere;
import org.compiere.db.CConnection;
import org.compiere.db.Database;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Ini;



public class ConfigurationData
{
	/**
	 * 	Constructor
	 * 	@param panel UI panel
	 */
	public ConfigurationData (ConfigurationPanel panel)
	{
		super ();
		p_panel = panel;
		initDatabaseConfig();
	}	//	ConfigurationData

	private void initDatabaseConfig()
	{
		List<IDatabaseConfig> configList = EquinoxExtensionLocator.instance().list(IDatabaseConfig.class).getExtensions();
		m_databaseConfig = new IDatabaseConfig[configList.size()];
		DBTYPE = new String[m_databaseConfig.length];
		for(int i = 0; i < configList.size(); i++)
		{
			m_databaseConfig[i] = configList.get(i);
			DBTYPE[i] = m_databaseConfig[i].getName();
		}
	}

	/** UI Panel				*/
	protected ConfigurationPanel	p_panel = null;
	/** Environment Properties	*/
	protected Properties		p_properties = new Properties();
	/**	Adempiere Home			*/
	private File				m_eoneHome;


	/**	Static Logger	*/
	protected static final CLogger	log	= CLogger.getCLogger (ConfigurationData.class);


	/** Properties File	name			*/
	public static final String	EONE_ENV_FILE		= "eoneEnv.properties";

	/** EONE Home					*/
	public static final String	EONE_HOME 			= "EONE_HOME";
	/** 				*/
	public static final String	JAVA_HOME 				= "JAVA_HOME";
	/** 				*/
	public static final String	EONE_JAVA_OPTIONS 	= "EONE_JAVA_OPTIONS";
	/** Default Keysore Password		*/
	public static final String	KEYSTORE_PASSWORD		= "myPassword";

	/** 				*/
	public static final String	EONE_APPS_TYPE		= "EONE_APPS_TYPE";
	/** 				*/
	public static final String	EONE_APPS_SERVER 	= "EONE_APPS_SERVER";
	/** 				*/
	public static final String	EONE_APPS_DEPLOY	= "EONE_APPS_DEPLOY";
	/** 				*/
	public static final String	EONE_JNP_PORT 		= "EONE_JNP_PORT";
	/** 				*/
	public static final String	EONE_WEB_PORT 		= "EONE_WEB_PORT";
	/** 				*/
	public static final String	EONE_SSL_PORT 		= "EONE_SSL_PORT";
	/** 				*/
	public static final String	EONE_WEB_ALIAS 	= "EONE_WEB_ALIAS";

	/** 				*/
	public static final String	EONE_KEYSTORE 		= "EONE_KEYSTORE";
	/** 				*/
	public static final String	EONE_KEYSTOREPASS	= "EONE_KEYSTOREPASS";
	/** 				*/
	public static final String	EONE_KEYSTORECODEALIAS	= "EONE_KEYSTORECODEALIAS";
	/** 				*/
	public static final String	EONE_KEYSTOREWEBALIAS	= "EONE_KEYSTOREWEBALIAS";

	public static final String	EONE_CERT_CN	= "EONE_CERT_CN";

	public static final String	EONE_CERT_ORG	= "EONE_CERT_ORG";

	public static final String	EONE_CERT_ORG_UNIT	= "EONE_CERT_ORG_UNIT";

	public static final String	EONE_CERT_LOCATION	= "EONE_CERT_LOCATION";

	public static final String	EONE_CERT_STATE	= "EONE_CERT_STATE";

	public static final String	EONE_CERT_COUNTRY	= "EONE_CERT_COUNTRY";

	public static final String	EONE_DB_URL 		= "EONE_DB_URL";
	/** DB Path			*/
	public static final String	EONE_DB_PATH		= "EONE_DB_PATH";
	/** 				*/
	/** DB Type			*/
	public static final String	EONE_DB_TYPE		= "EONE_DB_TYPE";
	/** 				*/
	public static final String	EONE_DB_SERVER 	= "EONE_DB_SERVER";
	/** 				*/
	public static final String	EONE_DB_PORT 		= "EONE_DB_PORT";
	/** 				*/
	public static final String	EONE_DB_NAME 		= "EONE_DB_NAME";
	/** 				*/
	public static final String	EONE_DB_USER 		= "EONE_DB_USER";
	/** 				*/
	public static final String	EONE_DB_PASSWORD 	= "EONE_DB_PASSWORD";
	/** 				*/
	public static final String	EONE_DB_SYSTEM 	= "EONE_DB_SYSTEM";	
	/** 				*/
	public static final String	EONE_DB_EXISTS 	= "EONE_DB_EXISTS";
	/** 				*/
	public static final String	EONE_MAIL_SERVER 	= "EONE_MAIL_SERVER";
	/** 				*/
	public static final String	EONE_MAIL_USER 	= "EONE_MAIL_USER";
	/** 				*/
	public static final String	EONE_MAIL_PASSWORD = "EONE_MAIL_PASSWORD";
	/** 				*/
	public static final String	EONE_ADMIN_EMAIL	= "EONE_ADMIN_EMAIL";
	/** 				*/
	public static final String	EONE_MAIL_UPDATED	= "EONE_MAIL_UPDATED";

	/** 				*/
	public static final String	EONE_FTP_SERVER	= "EONE_FTP_SERVER";
	/** 				*/
	public static final String	EONE_FTP_USER		= "EONE_FTP_USER";
	/** 				*/
	public static final String	EONE_FTP_PASSWORD	= "EONE_FTP_PASSWORD";
	/** 				*/
	public static final String	EONE_FTP_PREFIX	= "EONE_FTP_PREFIX";

	/** 				*/
	public static final String	EONE_WEBSTORES		= "EONE_WEBSTORES";


	public void updateProperty(String property, String value) {
		if (value == null) value = "";
		String currentValue = (String)p_properties.get(property);
		if (currentValue == null)
			p_properties.put(property, value);
		else if (!currentValue.equals(value))
			p_properties.put(property, value);
	}

	public String getProperty(String property)
	{
		return p_properties.getProperty(property);
	}

	/**
	 * 	Load Configuration Data
	 * 	@return true if loaded
	 */
	public boolean load()
	{
		String eoneHome = System.getProperty(EONE_HOME);
		if (eoneHome == null || eoneHome.length() == 0)
			eoneHome = System.getProperty("user.dir");

		boolean envLoaded = false;
		String fileName = eoneHome + File.separator + EONE_ENV_FILE;
		File env = new File (fileName);
		if (env.exists())
		{
			try
			{
				FileInputStream fis = new FileInputStream(env);
				p_properties.load(fis);
				fis.close();
			}
			catch (Exception e)
			{
				log.warning(e.toString());
			}
			if (log.isLoggable(Level.INFO)) log.info(env.toString());
			if (p_properties.size() > 5)
				envLoaded = true;

			Properties loaded = new Properties();
			loaded.putAll(p_properties);
			//
			initJava();
			if (loaded.containsKey(JAVA_HOME))
				setJavaHome((String)loaded.get(JAVA_HOME));
			//
			setAdempiereHome((String)p_properties.get(EONE_HOME));
			String s = (String)p_properties.get(EONE_KEYSTOREPASS);
			if (s == null || s.length() == 0)
			{
				s = KEYSTORE_PASSWORD;
				p_properties.put(EONE_KEYSTOREPASS, s);
			}
			setKeyStore(s);
			//
			if (loaded.containsKey(EONE_APPS_SERVER))
				setAppsServer((String)loaded.get(EONE_APPS_SERVER));
			if (loaded.containsKey(EONE_WEB_PORT))
				setAppsServerWebPort((String)loaded.get(EONE_WEB_PORT));
			if (loaded.containsKey(EONE_SSL_PORT))
				setAppsServerSSLPort((String)loaded.get(EONE_SSL_PORT));
			//
			int dbTypeIndex = setDatabaseType((String)p_properties.get(EONE_DB_TYPE));
			initDatabase((String)p_properties.get(EONE_DB_NAME), dbTypeIndex);	//	fills Database Options
			if (loaded.containsKey(EONE_DB_SERVER))
				setDatabaseServer((String)loaded.get(EONE_DB_SERVER));
			if (loaded.containsKey(EONE_DB_PORT))
				setDatabasePort((String)loaded.get(EONE_DB_PORT));
			if (loaded.containsKey(EONE_DB_NAME))
				setDatabaseName((String)loaded.get(EONE_DB_NAME));
			if (loaded.containsKey(EONE_DB_USER))
				setDatabaseUser((String)loaded.get(EONE_DB_USER));
			if (loaded.containsKey(EONE_DB_PASSWORD))
				setDatabasePassword((String)loaded.get(EONE_DB_PASSWORD));
			if (loaded.containsKey(EONE_DB_EXISTS))
				setDatabaseExists((String)loaded.get(EONE_DB_EXISTS));
			
			//Set default Extend
			if (loaded.containsKey(EONE_DB_SERVER_EX))
				setDBHostEx((String)loaded.get(EONE_DB_SERVER_EX));
			if (loaded.containsKey(EONE_DB_NAME_EX))
				setDBNameEx((String)loaded.get(EONE_DB_NAME_EX));
			if (loaded.containsKey(EONE_DB_PORT_EX))
				setDBPortEx((String)loaded.get(EONE_DB_PORT_EX));
			if (loaded.containsKey(EONE_DB_USER_EX))
				setDBUserEx((String)loaded.get(EONE_DB_USER_EX));
			if (loaded.containsKey(EONE_DB_PASSWORD_EX))
				setDBPassEx((String)loaded.get(EONE_DB_PASSWORD_EX));
			if (loaded.containsKey(EONE_DB_TYPE_EX))
				setDBTypeEx((String)loaded.get(EONE_DB_TYPE_EX));
		}
		else
		{
			setDatabaseType(Database.DB_POSTGRESQL);
		}

		InetAddress localhost = null;
		@SuppressWarnings("unused")
		String hostName = "unknown";
		try
		{
			localhost = InetAddress.getLocalHost();
			hostName = localhost.getHostName();
		}
		catch (Exception e)
		{
			log.severe("Cannot get local host name");
		}

		//	No environment file found - defaults
	//	envLoaded = false;
		if (!envLoaded)
		{
			log.info("Defaults");
			initJava();
			//
			setAdempiereHome(eoneHome);
			setKeyStore(KEYSTORE_PASSWORD);
			//	AppsServer
			initAppsServer();
			setAppsServer("0.0.0.0");
			//	Database Server
			initDatabase(Database.DB_POSTGRESQL);
			setDatabaseName("xerp");
			setDatabaseServer("localhost");
			setDatabaseUser("xerp");
			setDatabasePassword("1");
			//	Mail Server
		}	//	!envLoaded

		//	Default FTP stuff
		if (!p_properties.containsKey(EONE_FTP_SERVER))
		{
			p_properties.setProperty(EONE_FTP_SERVER, "localhost");
			p_properties.setProperty(EONE_FTP_USER, "anonymous");
			p_properties.setProperty(EONE_FTP_PASSWORD, "user@host.com");
			p_properties.setProperty(EONE_FTP_PREFIX, "my");
		}
		//	Default Java Options
		if (!p_properties.containsKey(EONE_JAVA_OPTIONS))
			p_properties.setProperty(EONE_JAVA_OPTIONS, "-Xms2048M -Xmx4096M");
		//	Web Alias
		if (!p_properties.containsKey(EONE_WEB_ALIAS) && localhost != null)
			p_properties.setProperty(EONE_WEB_ALIAS, localhost.getCanonicalHostName());

		//	(String)p_properties.get(EONE_DB_URL)	//	derived

		//	Keystore Alias
		if (!p_properties.containsKey(EONE_KEYSTORECODEALIAS))
			p_properties.setProperty(EONE_KEYSTORECODEALIAS, "eone");
		if (!p_properties.containsKey(EONE_KEYSTOREWEBALIAS))
			p_properties.setProperty(EONE_KEYSTOREWEBALIAS, "eone");

		return true;
	}	//	load


	public String resolveDatabaseName(String connectionName) {
		int index = p_panel.fDatabaseType.getSelectedIndex();
		if (index < 0 || index >= DBTYPE.length)
			log.warning("DatabaseType Index invalid: " + index);
		else if (m_databaseConfig[index] == null)
			log.warning("DatabaseType Config missing: " + DBTYPE[index]);
		else
			return m_databaseConfig[index].getDatabaseName(connectionName);
		return connectionName;
	}

	/**************************************************************************
	 * 	test
	 *	@return true if test ok
	 */
	public boolean test(IDBConfigMonitor monitor)
	{
		String error = testJava();
		if (error != null)
		{
			log.severe(error);
			return false;
		}

		error = testAdempiere();
		if (error != null)
		{
			log.warning(error);
			return false;
		}

		if (p_panel != null)
			p_panel.setStatusBar(p_panel.lAppsServer.getText());
		error = testAppsServer();
		if (error != null)
		{
			log.warning(error);
			return false;
		}

		if (p_panel != null)
			p_panel.setStatusBar(p_panel.lDatabaseServer.getText());
		error = testDatabase(monitor);
		if (error != null)
		{
			log.warning(error);
			return false;
		}

		

		return true;
	}	//	test


	/**
	 * 	Test Adempiere and set AdempiereHome
	 *	@return error message or null if OK
	 */
	public String testAdempiere()
	{
		//	Adempiere Home
		m_eoneHome = new File (getAdempiereHome());
		boolean pass =m_eoneHome.exists();
		String error = "Not found: EONEHome = " + m_eoneHome;
		if (p_panel != null)
			p_panel.signalOK(p_panel.okAdempiereHome, "ErrorEoneHome",
					pass, true, error);
		if (!pass)
			return error;
		if (log.isLoggable(Level.INFO)) log.info("OK: EoneHome = " + m_eoneHome);
		p_properties.setProperty(EONE_HOME, m_eoneHome.getAbsolutePath());
		System.setProperty(EONE_HOME, m_eoneHome.getAbsolutePath());

		//	KeyStore
		String fileName = KeyStoreMgt.getKeystoreFileName(m_eoneHome.getAbsolutePath());
		p_properties.setProperty(EONE_KEYSTORE, fileName);

		//	KeyStore Password
		String pw = p_panel != null
			? new String(p_panel.fKeyStore.getPassword())
			: (String)p_properties.get(EONE_KEYSTOREPASS);
		pass = pw != null && pw.length() > 0;
		error = "Invalid Key Store Password = " + pw;
		if (p_panel != null)
			p_panel.signalOK(p_panel.okKeyStore, "KeyStorePassword",
					pass, true, error);
		if (!pass)
			return error;
		p_properties.setProperty(EONE_KEYSTOREPASS, pw);
		KeyStoreMgt ks = p_panel != null
			? new KeyStoreMgt (fileName, p_panel.fKeyStore.getPassword())
			: new KeyStoreMgt (fileName, pw.toCharArray());
		ks.setCommonName((String)p_properties.getProperty(EONE_CERT_CN));
		ks.setOrganization((String)p_properties.getProperty(EONE_CERT_ORG));
		ks.setOrganizationUnit((String)p_properties.getProperty(EONE_CERT_ORG_UNIT));
		ks.setLocation((String)p_properties.getProperty(EONE_CERT_LOCATION));
		ks.setState((String)p_properties.getProperty(EONE_CERT_STATE));
		ks.setCountry((String)p_properties.getProperty(EONE_CERT_COUNTRY));
		error = p_panel != null
			? ks.verify((JFrame)SwingUtilities.getWindowAncestor(p_panel), p_properties.getProperty(EONE_KEYSTOREWEBALIAS))
			: ks.verify(null, p_properties.getProperty(EONE_KEYSTOREWEBALIAS));
		pass = error == null;
		if (p_panel != null)
			p_panel.signalOK(p_panel.okKeyStore, "KeyStorePassword",
					pass, true, error);
		if (!pass)
			return error;
		if (log.isLoggable(Level.INFO)) log.info("OK: KeyStore = " + fileName);
		return null;
	}	//	testAdempiere


	/**************************************************************************
	 * 	Test Apps Server Port (client perspective)
	 *  @param protocol protocol (http, ..)
	 *  @param server server name
	 *  @param port port
	 *  @param file file name
	 *  @return true if able to connect
	 */
	public boolean testPort (String protocol, String server, int port, String file)
	{
		System.out.println("testPort[" + protocol + "," + server + ", " + port + ", " + file +"]");
		URL url = null;
		try
		{
			url = new URL (protocol, server, port, file);
		}
		catch (MalformedURLException ex)
		{
			log.severe("No URL for Protocol=" + protocol
				+ ", Server=" + server
				+ ": " + ex.getMessage());
			return false;
		}
		try
		{
			URLConnection c = url.openConnection();
			Object o = c.getContent();
			if (o == null)
				log.warning("In use=" + url);	//	error
			else
				log.warning("In Use=" + url);	//	error
		}
		catch (Exception ex)
		{
			if (log.isLoggable(Level.FINE)) log.fine("Not used=" + url);	//	ok
			return false;
		}
		return true;
	}	//	testPort

	/**
	 * 	Test Server Port
	 *  @param port port
	 *  @return true if able to create
	 */
	protected boolean testServerPort (int port)
	{
		System.out.println("testServerPort: " + port);
		try
		{
			ServerSocket ss = new ServerSocket (port);
			if (log.isLoggable(Level.FINE)) log.fine(ss.getInetAddress() + ":" + ss.getLocalPort() + " - created");
			ss.close();
		}
		catch (Exception ex)
		{
			log.warning("Port " + port + ": " + ex.getMessage());
			return false;
		}
		return true;
	}	//	testPort


	/**
	 * 	Test Port
	 *  @param host host
	 *  @param port port
	 *  @param shouldBeUsed true if it should be used
	 *  @return true if some server answered on port
	 */
	public boolean testPort (InetAddress host, int port, boolean shouldBeUsed)
	{
		System.out.println("testPort[" + host.getHostAddress() + ", " + port + "]");
		Socket pingSocket = null;
		try
		{
			pingSocket = new Socket(host, port);
		}
		catch (Exception e)
		{
			if (shouldBeUsed)
				log.warning("Open Socket " + host + ":" + port + " - " + e.getMessage());
			else
				if (log.isLoggable(Level.FINE)) log.fine(host + ":" + port + " - " + e.getMessage());
			return false;
		}
		if (!shouldBeUsed)
			log.warning("Open Socket " + host + ":" + port + " - " + pingSocket);

		if (log.isLoggable(Level.FINE)) log.fine(host + ":" + port + " - " + pingSocket);
		//	success
		try
		{
			pingSocket.close();
		}
		catch (IOException e)
		{
			log.warning("close socket=" + e.toString());
		}
		return true;
	}	//	testPort


	/**************************************************************************
	 * 	Save Settings
	 *	@return true if saved
	 */
	public boolean save()
	{
		//	Add
		p_properties.setProperty("EONE_MAIN_VERSION", Adempiere.MAIN_VERSION);
		p_properties.setProperty("EONE_DATE_VERSION", Adempiere.DATE_VERSION);
		p_properties.setProperty("EONE_DB_VERSION", Adempiere.DB_VERSION);

		if (log.isLoggable(Level.FINEST)) log.finest(p_properties.toString());

		//	Before we save, load Ini
		Ini.setClient(false);
		String fileName = m_eoneHome.getAbsolutePath() + File.separator + Ini.EONE_PROPERTY_FILE;
		Ini.loadProperties(fileName);

		//	Save Environment
		fileName = m_eoneHome.getAbsolutePath() + File.separator + EONE_ENV_FILE;
		try
		{
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			p_properties.store(fos, EONE_ENV_FILE);
			fos.flush();
			fos.close();
		}
		catch (Exception e)
		{
			log.severe("Cannot save Properties to " + fileName + " - " + e.toString());
			if (p_panel != null)
				JOptionPane.showConfirmDialog(p_panel,
					ConfigurationPanel.res.getString("ErrorSave"),
					ConfigurationPanel.res.getString("EoneServerSetup"),
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			else
				System.err.println(ConfigurationPanel.res.getString("ErrorSave"));
			return false;
		}
		catch (Throwable t)
		{
			log.severe("Cannot save Properties to " + fileName + " - " + t.toString());
			if (p_panel != null)
				JOptionPane.showConfirmDialog(p_panel,
					ConfigurationPanel.res.getString("ErrorSave"),
					ConfigurationPanel.res.getString("EoneServerSetup"),
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			else
				System.err.println(ConfigurationPanel.res.getString("ErrorSave"));
			return false;
		}
		log.info(fileName);
		return saveIni();
	}	//	save

	/**
	 * 	Synchronize and save Connection Info in Ini
	 * 	@return true
	 */
	private boolean saveIni()
	{
		Ini.setEOneHome(m_eoneHome.getAbsolutePath());

		//	Create Connection
		String ccType = Database.DB_ORACLE;
		if (getDatabaseType() != null && !getDatabaseType().equals(Database.DB_ORACLE))
			ccType = getDatabaseType();
		CConnection cc = null;
		try
		{
			cc = CConnection.get (ccType,
				getDatabaseServer(), getDatabasePort(), getDatabaseName(),
				getDatabaseUser(), getDatabasePassword());
			cc.setAppsHost(getAppsServer());
			cc.setWebPort(getAppsServerWebPort());
			cc.setSSLPort(getAppsServerSSLPort());
		}
		
		catch(Exception e)
		{
			log.log(Level.SEVERE, "connection", e);
			return false;
		}
		Ini.setProperty(Ini.P_CONNECTION, cc.toStringLong());
		if (getDBHostEx() != null) {
			cc.setHostSecond(getDBHostEx());
			cc.setDbNameSecond(getDBNameEx());
			cc.setPortSecond(""+ getDBPortEx());
			cc.setDbUserSecond(getDBUserEx());
			cc.setDbPassSecond(getDBPassEx());
			cc.setDbTypeSecond(getDBTypeEx());
			Ini.setProperty(Ini.P_CONNECTION_SECOND, cc.toStringLongEx());
		}
		
		Ini.setProperty(Ini.P_TRACELEVEL, "SEVERE");
		//org.eclipse.jetty.osgi.annotations.TraceLevel=SEVERE
		//org.eclipse.jetty.annotations.TraceLevel=SEVERE
		Ini.setProperty("org.eclipse.jetty.osgi.annotations.TraceLevel", "SEVERE");
		Ini.setProperty("org.eclipse.jetty.annotations.TraceLevel", "SEVERE");
		Ini.saveProperties(false);
		return true;
	}	//	saveIni


	/**
	 * 	Get Properties
	 *	@return properties
	 */
	Properties getProperties ()
	{
		return p_properties;
	}	//	getProperties

	/**
	 * 	Get Adempiere Home
	 *	@return adempiere home
	 */
	public String getAdempiereHome()
	{
		return p_panel != null
			? p_panel.fAdempiereHome.getText()
			: (String)p_properties.get(EONE_HOME);
	}	//	getAdempiereHome

	/**
	 * 	Set Adempiere Home
	 *	@param adempiereHome
	 */
	public void setAdempiereHome (String adempiereHome)
	{
		if (p_panel != null)
			p_panel.fAdempiereHome.setText(adempiereHome);
		else
			updateProperty(EONE_HOME, adempiereHome);
	}	//	setAdempiereHome

	/**
	 * 	Get Key Store
	 *	@return password
	 */
	public String getKeyStore ()
	{
		if (p_panel != null)
		{
			char[] pw = p_panel.fKeyStore.getPassword();
			if (pw != null)
				return new String(pw);
		}
		else
		{
			String pw = getProperty(EONE_KEYSTOREPASS);
			if (pw != null)
				return pw;
		}
		return "";
	}	//	getKeyStore

	/**
	 * 	Set Key Store Password
	 *	@param password
	 */
	public void setKeyStore (String password)
	{
		if (p_panel != null)
			p_panel.fKeyStore.setText(password);
		else
			updateProperty(EONE_KEYSTOREPASS, password);
	}	//	setKeyStore


	/**************************************************************************
	 * 	Java Settings
	 *************************************************************************/

	/** SUN VM (default)	*/
	private static String	JAVATYPE_SUN = "sun";
	/** Apple VM			*/
	private static String	JAVATYPE_MAC = "mac";
	/** Open JDK			*/
	private static String	JAVATYPE_OPENJDK = "OpenJDK";
	/** Java VM Types		*/
	public static String[]	JAVATYPE = new String[]
		{JAVATYPE_SUN, JAVATYPE_OPENJDK, JAVATYPE_MAC};

	/** Virtual machine Configurations	*/
	private Config m_javaConfig = new ConfigVM(this);
	private ConfigAppServer m_appsConfig = new ConfigAppServer(this);

	/**
	 * 	Init Database
	 */
	public void initJava()
	{
		m_javaConfig.init();
	}	//	initDatabase

	/**
	 * 	Test Java
	 *	@return error message or null of OK
	 */
	public String testJava()
	{
		return m_javaConfig.test();
	}	//	testJava

	/**
	 * @return Returns the javaHome.
	 */
	public String getJavaHome ()
	{
		if (p_panel != null)
			return p_panel.fJavaHome.getText();
		else
			return (String)p_properties.get(JAVA_HOME);
	}
	/**
	 * @param javaHome The javaHome to set.
	 */
	public void setJavaHome (String javaHome)
	{
		if (p_panel != null)
			p_panel.fJavaHome.setText(javaHome);
		else
			updateProperty(JAVA_HOME, javaHome);
	}

	/**
	 * 	Init Apps Server
	 */
	public void initAppsServer()
	{
		m_appsConfig.init();
	}	//	initAppsServer

	/**
	 * 	Test Apps Server
	 *	@return error message or null of OK
	 */
	public String testAppsServer()
	{
		return m_appsConfig.test();
	}	//	testAppsServer

	/**
	 * @return Returns the appsServer.
	 */
	public String getAppsServer ()
	{
		return p_panel != null
			? p_panel.fAppsServer.getText()
			: (String)p_properties.get(EONE_APPS_SERVER);
	}
	/**
	 * @param appsServer The appsServer to set.
	 */
	public void setAppsServer (String appsServer)
	{
		if (p_panel != null)
			p_panel.fAppsServer.setText(appsServer);
		else
			updateProperty(EONE_APPS_SERVER, appsServer);
	}

	/**
	 * @return Returns the appsServerSSLPort.
	 */
	public int getAppsServerSSLPort ()
	{
		String port = p_panel != null
			? p_panel.fSSLPort.getText()
			: (String)p_properties.get(EONE_SSL_PORT);
		try
		{
			return Integer.parseInt(port);
		}
		catch (Exception e)
		{
			setAppsServerSSLPort("0");
		}
		return 0;
	}
	/**
	 * @param appsServerSSLPort The appsServerSSLPort to set.
	 */
	public void setAppsServerSSLPort (String appsServerSSLPort)
	{
		if (p_panel != null)
			p_panel.fSSLPort.setText(appsServerSSLPort);
		else
			updateProperty(EONE_SSL_PORT, appsServerSSLPort);
	}
	/**
	 * @param enable if tre enable SSL entry
	 */
	public void setAppsServerSSLPort (boolean enable)
	{
		if (p_panel != null)
			p_panel.fSSLPort.setEnabled(enable);
	}
	/**
	 * @return Returns the appsServerWebPort.
	 */
	public int getAppsServerWebPort ()
	{
		String port = p_panel != null
			? p_panel.fWebPort.getText()
			: (String)p_properties.get(EONE_WEB_PORT);
		try
		{
			return Integer.parseInt(port);
		}
		catch (Exception e)
		{
			setAppsServerWebPort("0");
		}
		return 0;
	}
	/**
	 * @param appsServerWebPort The appsServerWebPort to set.
	 */
	public void setAppsServerWebPort (String appsServerWebPort)
	{
		if (p_panel != null)
			p_panel.fWebPort.setText(appsServerWebPort);
		else
			updateProperty(EONE_WEB_PORT, appsServerWebPort);
	}
	/**
	 * @param enable if tre enable Web entry
	 */
	public void setAppsServerWebPort (boolean enable)
	{
		if (p_panel != null)
			p_panel.fWebPort.setEnabled(enable);
	}


	/**************************************************************************
	 * 	Database Settings
	 *************************************************************************/

	/** Database Types		*/
	public static String[]	DBTYPE = null;
	    //end e-evolution vpj-cd 02/07/2005 PostgreSQL

	/** Database Configs	*/
	private IDatabaseConfig[] m_databaseConfig = null;

	/**
	 * 	Init Database
	 * 	@param selected DB
	 */
	public void initDatabase(String selected)
	{
		int index = (p_panel != null ? p_panel.fDatabaseType.getSelectedIndex() : -1);
		if (index < 0)
		{
			for(int i = 0; i < DBTYPE.length; i++)
			{
				if (DBTYPE[i].equals(selected))
				{
					index = i;
					break;
				}
			}
			if (index < 0)
				index = 0;
		}
		initDatabase(selected, index);
	}	//	initDatabase

	private void initDatabase(String selected, int index)
	{
		if (index < 0 || index >= DBTYPE.length)
			log.warning("DatabaseType Index invalid: " + index);
		else if (m_databaseConfig[index] == null)
		{
			log.warning("DatabaseType Config missing: " + DBTYPE[index]);
			if (p_panel != null)
				p_panel.fDatabaseType.setSelectedIndex(0);
		}
		else
		{
			if (   ! p_properties.containsKey(EONE_DB_NAME)
				&& ! p_properties.containsKey(EONE_DB_PORT)) {
				m_databaseConfig[index].init(this);
			}

			if (p_panel != null)
			{
				String[] databases = m_databaseConfig[index].discoverDatabases(selected);
				//DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<Object>(databases);
				//p_panel.fDatabaseDiscovered.setModel(model);
				//p_panel.fDatabaseDiscovered.setEnabled(databases.length != 0);
				if (databases.length > 0)
					p_panel.fDatabaseName.setText(databases[0]);
			}
		}
	}

	/**
	 * 	Test Database
	 * @param monitor
	 *	@return error message or null of OK
	 */
	public String testDatabase(IDBConfigMonitor monitor)
	{
		int index = p_panel != null
			? p_panel.fDatabaseType.getSelectedIndex()
			: setDatabaseType((String)p_properties.get(EONE_DB_TYPE));
		if (index < 0 || index >= DBTYPE.length)
			return "DatabaseType Index invalid: " + index;
		else if (m_databaseConfig[index] == null)
			return "DatabaseType Config class missing: " + index;
		return m_databaseConfig[index].test(monitor, this);
	}	//	testDatabase


	/**
	 * 	Set Database Type
	 *	@param databaseType The databaseType to set.
	 */
	public int setDatabaseType (String databaseType)
	{
		int index = -1;
		for (int i = 0; i < DBTYPE.length; i++)
		{
			if (DBTYPE[i].equals(databaseType))
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			index = 0;
			log.warning("Invalid DatabaseType=" + databaseType);
		}
		if (p_panel != null)
			p_panel.fDatabaseType.setSelectedIndex(index);
		else
			updateProperty(EONE_DB_TYPE, databaseType);

		return index;
	}	//	setDatabaseType

	/**
	 * @return Returns the databaseType.
	 */
	public String getDatabaseType ()
	{
		return p_panel != null
			? (String)p_panel.fDatabaseType.getSelectedItem()
			: (String)p_properties.get(EONE_DB_TYPE);
	}
	
	
	/**
	 * @return Returns the databaseName.
	 */
	public String getDatabaseName ()
	{
		return p_panel != null
			? p_panel.fDatabaseName.getText()
			: (String)p_properties.get(EONE_DB_NAME);
	}
	/**
	 * @param databaseName The databaseName to set.
	 */
	public void setDatabaseName (String databaseName)
	{
		if (p_panel != null)
			p_panel.fDatabaseName.setText(databaseName);
		else
			updateProperty(EONE_DB_NAME, databaseName);
	}

	/**
	 * @return Returns the database User Password.
	 */
	public String getDatabasePassword ()
	{
		if (p_panel != null)
		{
			char[] pw = p_panel.fDatabasePassword.getPassword();
			if (pw != null)
				return new String(pw);
			return "";
		}
		else
		{
			String pw = (String)p_properties.get(EONE_DB_PASSWORD);
			return (pw != null ? pw : "");
		}
	}
	/**
	 * @param databasePassword The databasePassword to set.
	 */
	public void setDatabasePassword (String databasePassword)
	{
		if (p_panel != null)
			p_panel.fDatabasePassword.setText(databasePassword);
		else
			updateProperty(EONE_DB_PASSWORD, databasePassword);
	}

	/**
	 * @return Returns the databasePort.
	 */
	public int getDatabasePort ()
	{
		String port = p_panel != null
			? p_panel.fDatabasePort.getText()
			: (String)p_properties.get(EONE_DB_PORT);
		try
		{
			return Integer.parseInt(port);
		}
		catch (Exception e)
		{
			setDatabasePort("0");
		}
		return 0;
	}	//	getDatabasePort
	/**
	 * @param databasePort The databasePort to set.
	 */
	public void setDatabasePort (String databasePort)
	{
		if (p_panel != null)
			p_panel.fDatabasePort.setText(databasePort);
		else
			updateProperty(EONE_DB_PORT, databasePort);
	}
	/**
	 * @return Returns the databaseServer.
	 */
	public String getDatabaseServer ()
	{
		return p_panel != null
			? p_panel.fDatabaseServer.getText()
			: (String)p_properties.get(EONE_DB_SERVER);
	}
	/**
	 * @param databaseServer The databaseServer to set.
	 */
	public void setDatabaseServer (String databaseServer)
	{
		if (p_panel != null)
			p_panel.fDatabaseServer.setText(databaseServer);
		else
			updateProperty(EONE_DB_SERVER, databaseServer);
	}
	
	public void setDatabaseExists(String dbExists)
	{
	    
		if (p_panel != null)
			p_panel.okdbExists.setSelected("Y".equalsIgnoreCase(dbExists));
		else
			updateProperty(EONE_DB_EXISTS, dbExists);
	}
	
	/**
	 * @param 
	 */
	public boolean getDatabaseExists()
	{
		Object dbExists = p_properties.get(EONE_DB_EXISTS);
		if (dbExists == null)
			dbExists = "N";
		else
			dbExists = dbExists.toString();
		return p_panel != null
				? p_panel.okdbExists.isSelected()
				: "Y".equalsIgnoreCase( (String) dbExists);
	}
	
	/**
	 * @return Returns the databaseUser.
	 */
	public String getDatabaseUser ()
	{
		return p_panel != null
			? p_panel.fDatabaseUser.getText()
			: (String)p_properties.get(EONE_DB_USER);
	}
	/**
	 * @param databaseUser The databaseUser to set.
	 */
	public void setDatabaseUser (String databaseUser)
	{
		if (p_panel != null)
			p_panel.fDatabaseUser.setText(databaseUser);
		else
			updateProperty(EONE_DB_USER, databaseUser);
	}

	
	public String getWebStores(Connection con)
	{
		String sql = "SELECT WebContext FROM W_Store WHERE IsActive='Y'";
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder result = new StringBuilder();
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next ())
			{
				if (result.length() > 0)
					result.append(",");
				result.append(rs.getString(1));
			}
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
		finally
		{
			DB.close(rs, stmt);
			rs = null;
			stmt = null;
		}
		return result.toString();
	}	//	getWebStores

	/**
	 * 	Set Configuration Property
	 *	@param key key
	 *	@param value value
	 */
	public void setProperty(String key, String value)
	{
		p_properties.setProperty(key, value);
	}	//	setProperty

	public void dbChanged() {
		p_properties.remove(EONE_DB_NAME);
		p_properties.remove(EONE_DB_PORT);
	}
	
	//Add DB Second
	/** DB Type			*/
	public static final String	EONE_DB_TYPE_EX		= "EONE_DB_TYPE_EX";
	/** 				*/
	public static final String	EONE_DB_SERVER_EX 	= "EONE_DB_SERVER_EX";
	/** 				*/
	public static final String	EONE_DB_PORT_EX 		= "EONE_DB_PORT_EX";
	/** 				*/
	public static final String	EONE_DB_NAME_EX 		= "EONE_DB_NAME_EX";
	/** 				*/
	public static final String	EONE_DB_USER_EX 		= "EONE_DB_USER_EX";
	/** 				*/
	public static final String	EONE_DB_PASSWORD_EX 	= "EONE_DB_PASSWORD_EX";
	

	public void setDBHostEx (String databaseServer)
	{
		if (p_panel != null)
			p_panel.fDBHostEx.setText(databaseServer);
		else
			updateProperty(EONE_DB_SERVER_EX, databaseServer);
	}
	public String getDBHostEx ()
	{
		return p_panel != null
			? p_panel.fDBHostEx.getText()
			: (String)p_properties.get(EONE_DB_SERVER_EX);
	}
	public int setDBTypeEx (String databaseType)
	{
		int index = -1;
		for (int i = 0; i < DBTYPE.length; i++)
		{
			if (DBTYPE[i].equals(databaseType))
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			index = 0;
			log.warning("Invalid DatabaseType=" + databaseType);
		}
		if (p_panel != null)
			p_panel.fTypeEx.setSelectedIndex(index);
		else
			updateProperty(EONE_DB_TYPE_EX, databaseType);

		return index;
	}
	public String getDBTypeEx ()
	{
		return p_panel != null
			? (String)p_panel.fTypeEx.getSelectedItem()
			: (String)p_properties.get(EONE_DB_TYPE_EX);
	}
	public void setDBNameEx (String databaseName)
	{
		if (p_panel != null)
			p_panel.fDBNameEx.setText(databaseName);
		else
			updateProperty(EONE_DB_NAME_EX, databaseName);
	}
	public String getDBNameEx ()
	{
		return p_panel != null
			? p_panel.fDBNameEx.getText()
			: (String)p_properties.get(EONE_DB_NAME_EX);
	}
	public void setDBUserEx (String user)
	{
		if (p_panel != null)
			p_panel.fDBUserEx.setText(user);
		else
			updateProperty(EONE_DB_USER_EX, user);
	}
	public String getDBUserEx ()
	{
		return p_panel != null
			? p_panel.fDBUserEx.getText()
			: (String)p_properties.get(EONE_DB_USER_EX);
	}
	public void setDBPassEx (String user)
	{
		if (p_panel != null)
			p_panel.fDBPassEx.setText(user);
		else
			updateProperty(EONE_DB_PASSWORD_EX, user);
	}
	public String getDBPassEx ()
	{
		if (p_panel != null)
		{
			char[] pw = p_panel.fDBPassEx.getPassword();
			if (pw != null)
				return new String(pw);
			return "";
		}
		else
		{
			String pw = (String)p_properties.get(EONE_DB_PASSWORD_EX);
			return (pw != null ? pw : "");
		}
	}
	public void setDBPortEx (String user)
	{
		if (p_panel != null)
			p_panel.fDBPortEx.setText(user);
		else
			updateProperty(EONE_DB_PORT_EX, user);
	}
	public int getDBPortEx ()
	{
		String port = p_panel != null
			? p_panel.fDBPortEx.getText()
			: (String)p_properties.get(EONE_DB_PORT_EX);
		try
		{
			return Integer.parseInt(port);
		}
		catch (Exception e)
		{
			setDatabasePort("0");
		}
		return 0;
	}
}	//	ConfigurationData
