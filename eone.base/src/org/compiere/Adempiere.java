package org.compiere;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;

import org.adempiere.base.Core;
import org.compiere.db.CConnection;
import org.compiere.model.MClient;
import org.compiere.model.MSysConfig;
import org.compiere.model.MSystem;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ServerStateChangeEvent;
import org.compiere.model.ServerStateChangeListener;
import org.compiere.util.CLogFile;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Login;
import org.compiere.util.SecureEngine;
import org.compiere.util.SecureInterface;
import org.compiere.util.Trx;
import org.compiere.util.Util;


public final class Adempiere
{
	static public final String	ID				= "EOne java 11, maven 3.6.3, ERP - CRM";
	static public String	MAIN_VERSION	= "Version 1.0";
	static public String	DATE_VERSION	= "2019-11-22";
	static public String	DB_VERSION		= "2019-11-22";

	static public final String	NAME 			= "EONE";
	static public final String	URL				= "www.eone.com";
	static private final String	s_File16x16		= "images/iD16.gif";
	static private final String	s_file32x32		= "images/iD32.gif";
	static private final String	s_file100x30	= "images/iD10030.png";
	static private final String	s_file48x15		= "images/EONE.png";
	static private final String	s_file48x15HR	= "images/EONEHR.png";
	static private String		s_supportEmail	= "";

	/** Subtitle                */
	static public final String	SUB_TITLE		= "Smart ERP, CRM ";
	static public final String	ADEMPIERE_R		= "EONE";
	static public final String	COPYRIGHT		= "\u00A9 2020 EONE";

	static private String		s_ImplementationVersion = null;
	static private String		s_ImplementationVendor = null;

	static private Image 		s_image16;
	static private Image 		s_image48x15;
	static private Image 		s_imageLogo;
	static private ImageIcon 	s_imageIcon32;
	static private ImageIcon 	s_imageIconLogo;

	static private final String ONLINE_HELP_URL = "http://www.eone.com";

	/**	Logging								*/
	private static CLogger		log = null;
	
	/** Thread pool **/
	private static ScheduledThreadPoolExecutor threadPoolExecutor = null;
	
	 /** A list of event listeners for this component.	*/
    private static EventListenerList m_listenerList = new EventListenerList();

	static {
		ClassLoader loader = Adempiere.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream("org/adempiere/version.properties");
		if (inputStream != null)
		{
			Properties properties = new Properties();
			try {
				properties.load(inputStream);
				if (properties.containsKey("MAIN_VERSION"))
					MAIN_VERSION = properties.getProperty("MAIN_VERSION");
				if (properties.containsKey("DATE_VERSION"))
					DATE_VERSION = properties.getProperty("DATE_VERSION");
				if (properties.containsKey("DB_VERSION"))
					DB_VERSION = properties.getProperty("DB_VERSION");
				if (properties.containsKey("IMPLEMENTATION_VERSION"))
					s_ImplementationVersion = properties.getProperty("IMPLEMENTATION_VERSION");
				if (properties.containsKey("IMPLEMENTATION_VENDOR"))
					s_ImplementationVendor = properties.getProperty("IMPLEMENTATION_VENDOR");
			} catch (IOException e) {
			}
		}
	}

	/**
	 *  Get Product Name
	 *  @return Application Name
	 */
	public static String getName()
	{
		return NAME;
	}   //  getName

	/**
	 *  Get Product Version
	 *  @return Application Version
	 */
	public static String getVersion()
	{
		String version = "Copyright 2020 - Developer Eone";
		return version;
	}   //  getVersion

	public static boolean isVersionShown(){ 
		return MSysConfig.getBooleanValue(MSysConfig.APPLICATION_MAIN_VERSION_SHOWN, true);
	}

	public static boolean isDBVersionShown(){
		boolean defaultVal = MSystem.get(Env.getCtx()).getSystemStatus().equalsIgnoreCase("P") ? false : true;
		return MSysConfig.getBooleanValue(MSysConfig.APPLICATION_DATABASE_VERSION_SHOWN, defaultVal);
	}

	public static boolean isVendorShown(){
		return MSysConfig.getBooleanValue(MSysConfig.APPLICATION_IMPLEMENTATION_VENDOR_SHOWN, true);
	}

	public static boolean isJVMShown(){
		boolean defaultVal = MSystem.get(Env.getCtx()).getSystemStatus().equalsIgnoreCase("P") ? false : true;
		return MSysConfig.getBooleanValue(MSysConfig.APPLICATION_JVM_VERSION_SHOWN, defaultVal);
	}

	public static boolean isOSShown(){
		boolean defaultVal = MSystem.get(Env.getCtx()).getSystemStatus().equalsIgnoreCase("P") ? false : true;
		return MSysConfig.getBooleanValue(MSysConfig.APPLICATION_OS_INFO_SHOWN, defaultVal);
	}

	public static boolean isHostShown() 
	{
		boolean defaultVal = MSystem.get(Env.getCtx()).getSystemStatus().equalsIgnoreCase("P") ? false : true;
		return MSysConfig.getBooleanValue(MSysConfig.APPLICATION_HOST_SHOWN, defaultVal);
	}

	public static String getDatabaseVersion() 
	{
//		return DB.getSQLValueString(null, "select lastmigrationscriptapplied from ad_system");
		return MSysConfig.getValue(MSysConfig.APPLICATION_DATABASE_VERSION,
				DB.getSQLValueString(null, "select lastmigrationscriptapplied from ad_system"));
	}
	
	/**
	 *	Short Summary (Windows)
	 *  @return summary
	 */
	public static String getSum()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(NAME).append(" ").append(MAIN_VERSION).append(SUB_TITLE);
		return sb.toString();
	}	//	getSum

	
	public static String getSummary()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(NAME).append(" ")
			.append(MAIN_VERSION).append("_").append(DATE_VERSION)
			.append(" -").append(SUB_TITLE)
			.append("- ").append(COPYRIGHT)
			.append("; Implementation: ").append(getImplementationVersion())
			.append(" - ").append(getImplementationVendor());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Set Package Info
	 */
	@SuppressWarnings("deprecation")
	private static void setPackageInfo()
	{
		if (s_ImplementationVendor != null)
			return;

		Package adempierePackage = Package.getPackage("org.compiere");
		s_ImplementationVendor = adempierePackage.getImplementationVendor();
		s_ImplementationVersion = adempierePackage.getImplementationVersion();
		if (s_ImplementationVendor == null)
		{
			s_ImplementationVendor = "Supported by EONE";
			s_ImplementationVersion = "EONE";
		}
	}	//	setPackageInfo

	/**
	 * 	Get Jar Implementation Version
	 * 	@return Implementation-Version
	 */
	public static String getImplementationVersion()
	{
		if (s_ImplementationVersion == null)
			setPackageInfo();
		return s_ImplementationVersion;
	}	//	getImplementationVersion

	/**
	 * 	Get Jar Implementation Vendor
	 * 	@return Implementation-Vendor
	 */
	public static String getImplementationVendor()
	{
		if(DB.isConnected()){
			String vendor = MSysConfig.getValue(MSysConfig.APPLICATION_IMPLEMENTATION_VENDOR, null);
			if(vendor != null)
				return vendor;
		}
		if (s_ImplementationVendor == null)
			setPackageInfo();
		return s_ImplementationVendor;
	}	//	getImplementationVendor

	/**
	 *  Get Checksum
	 *  @return checksum
	 */
	public static int getCheckSum()
	{
		return getSum().hashCode();
	}   //  getCheckSum

	/**
	 *	Summary in ASCII
	 *  @return Summary in ASCII
	 */
	public static String getSummaryAscii()
	{
		String retValue = getSummary();
		//  Registered Trademark
		retValue = Util.replace(retValue, "\u00AE", "(r)");
		//  Trademark
		retValue = Util.replace(retValue, "\u2122", "(tm)");
		//  Copyright
		retValue = Util.replace(retValue, "\u00A9", "(c)");
		//  Cr
		retValue = Util.replace(retValue, Env.NL, " ");
		retValue = Util.replace(retValue, "\n", " ");
		return retValue;
	}	//	getSummaryAscii

	/**
	 * 	Get Java VM Info
	 *	@return VM info
	 */
	public static String getJavaInfo()
	{
		return System.getProperty("java.vm.name")
			+ " " + System.getProperty("java.vm.version");
	}	//	getJavaInfo

	/**
	 * 	Get Operating System Info
	 *	@return OS info
	 */
	public static String getOSInfo()
	{
		return System.getProperty("os.name") + " "
			+ System.getProperty("os.version") + " "
			+ System.getProperty("sun.os.patch.level");
	}	//	getJavaInfo

	/**
	 *  Get full URL
	 *  @return URL
	 */
	public static String getURL()
	{
		return "http://" + URL;
	}   //  getURL

	/**
	 * @return URL
	 */
	public static String getOnlineHelpURL()
	{
		return ONLINE_HELP_URL;
	}

	/**
	 *  Get Sub Title
	 *  @return Subtitle
	 */
	public static String getSubtitle()
	{
		return SUB_TITLE;
	}   //  getSubitle

	/**
	 *  Get 16x16 Image.
	 *	@return Image Icon
	 */
	public static Image getImage16()
	{
		if (s_image16 == null)
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			URL url = Core.getResourceFinder().getResource(s_File16x16);
			if (url == null)
				return null;
			s_image16 = tk.getImage(url);
		}
		return s_image16;
	}   //  getImage16

	/**
	 *  Get 28*15 Logo Image.
	 *  @param hr high resolution
	 *  @return Image Icon
	 */
	public static Image getImageLogoSmall(boolean hr)
	{
		if (s_image48x15 == null)
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			URL url = null;
			if (hr)
				url = Core.getResourceFinder().getResource(s_file48x15HR);
			else
				url = Core.getResourceFinder().getResource(s_file48x15);
			if (url == null)
				return null;
			s_image48x15 = tk.getImage(url);
		}
		return s_image48x15;
	}   //  getImageLogoSmall

	/**
	 *  Get Logo Image.
	 *  @return Image Logo
	 */
	public static Image getImageLogo()
	{
		if (s_imageLogo == null)
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			URL url = Core.getResourceFinder().getResource(s_file100x30);
			if (url == null)
				return null;
			s_imageLogo = tk.getImage(url);
		}
		return s_imageLogo;
	}   //  getImageLogo

	/**
	 *  Get 32x32 ImageIcon.
	 *	@return Image Icon
	 */
	public static ImageIcon getImageIcon32()
	{
		if (s_imageIcon32 == null)
		{
			URL url = Core.getResourceFinder().getResource(s_file32x32);
			if (url == null)
				return null;
			s_imageIcon32 = new ImageIcon(url);
		}
		return s_imageIcon32;
	}   //  getImageIcon32

	/**
	 *  Get 100x30 ImageIcon.
	 *	@return Image Icon
	 */
	public static ImageIcon getImageIconLogo()
	{
		if (s_imageIconLogo == null)
		{
			URL url = Core.getResourceFinder().getResource(s_file100x30);
			if (url == null)
				return null;
			s_imageIconLogo = new ImageIcon(url);
		}
		return s_imageIconLogo;
	}   //  getImageIconLogo

	/**
	 *  Get default (Home) directory
	 *  @return Home directory
	 */
	public static String getAdempiereHome()
	{
		//  Try Environment
		String retValue = Ini.getEOneHome();
		if (retValue == null)
			retValue = File.separator + "idempiere";
		return retValue;
	}   //  getHome

	/**
	 *  Get Support Email
	 *  @return Support mail address
	 */
	public static String getSupportEMail()
	{
		return s_supportEmail;
	}   //  getSupportEMail

	/**
	 *  Set Support Email
	 *  @param email Support mail address
	 */
	public static void setSupportEMail(String email)
	{
		s_supportEmail = email;
	}   //  setSupportEMail

	public static synchronized boolean isStarted()
	{
		return (log != null);
	}

	/*************************************************************************
	 *  Startup Client/Server.
	 *  - Print greeting,
	 *  - Check Java version and
	 *  - load ini parameters
	 *  If it is a client, load/set PLAF and exit if error.
	 *  If Client, you need to call startupEnvironment explicitly!
	 * 	For testing call method startupEnvironment
	 *	@param isClient true for client
	 *  @return successful startup
	 */
	public static synchronized boolean startup (boolean isClient)
	{
		//	Already started
		if (log != null)
			return true;

		//	Check Version
		if (isClient && !Login.isJavaOK(isClient))
			System.exit(1);

		Ini.setClient (isClient);		//	init logging in Ini
		
		if (! isClient)  
			CLogMgt.initialize(isClient);
		log = CLogger.getCLogger(Adempiere.class);
		if (log.isLoggable(Level.INFO)) log.info(getSummaryAscii());
		Ini.loadProperties (false);

		//	Set up Log
		CLogMgt.setLevel(Ini.getProperty(Ini.P_TRACELEVEL));
		if (isClient && Ini.isPropertyBool(Ini.P_TRACEFILE))
			CLogMgt.addHandler(new CLogFile(Ini.findEOneHome(), true, isClient));

		//setup specific log level
		Properties properties = Ini.getProperties();
		for(Object key : properties.keySet())
		{
			if (key instanceof String)
			{
				String s = (String)key;
				if (s.endsWith("."+Ini.P_TRACELEVEL))
				{
					String level = properties.getProperty(s);
					s = s.substring(0, s.length() - ("."+Ini.P_TRACELEVEL).length());
					CLogMgt.setLevel(s, level);
				}
			}
		}
		
		//	Set UI
		if (isClient)
		{
			if (CLogMgt.isLevelAll())
				log.log(Level.FINEST, System.getProperties().toString());
		}

		//  Set Default Database Connection from Ini
		DB.setDBTarget(CConnection.get());

		createThreadPool();
		
		fireServerStateChanged(new ServerStateChangeEvent(new Object(), ServerStateChangeEvent.SERVER_START));
		
		if (isClient)		//	don't test connection
			return false;	//	need to call

		return startupEnvironment(isClient);
	}   //  startup

	private static void createThreadPool() {
		int max = Runtime.getRuntime().availableProcessors() * 20;
		int defaultMax = max;
		Properties properties = Ini.getProperties();
		String maxSize = properties.getProperty("MaxThreadPoolSize");
		if (maxSize != null) {
			try {
				max = Integer.parseInt(maxSize);
			} catch (Exception e) {}
		}
		if (max <= 0) {
			max = defaultMax;
		}
		
		// start thread pool
		threadPoolExecutor = new ScheduledThreadPoolExecutor(max);		
		
		Trx.startTrxMonitor();
	}

	/**
	 * 	Startup Adempiere Environment.
	 * 	Automatically called for Server connections
	 * 	For testing call this method.
	 *	@param isClient true if client connection
	 *  @return successful startup
	 */
	public static boolean startupEnvironment (boolean isClient)
	{
		startup(isClient);		//	returns if already initiated
		if (!DB.isConnected())
		{
			log.severe ("No Database");
			return false;
		}
		
		//	Check Build
		if (!DB.isBuildOK(Env.getCtx()))
		{
			if (isClient)
				System.exit(1);
			log = null;
			return false;
		}
		
		MSystem system = MSystem.get(Env.getCtx());	//	Initializes Base Context too
		if (system == null)
			return false;
		
		//	Initialize main cached Singletons
		ModelValidationEngine.get();
		try
		{
			String className = system.getEncryptionKey();
			if (className == null || className.length() == 0)
			{
				className = System.getProperty(SecureInterface.ADEMPIERE_SECURE);
				if (className != null && className.length() > 0
					&& !className.equals(SecureInterface.ADEMPIERE_SECURE_DEFAULT))
				{
					SecureEngine.init(className);	//	test it
					system.setEncryptionKey(className);
					system.saveEx();
				}
			}
			SecureEngine.init(className);

			//
			if (isClient)
				MClient.get(Env.getCtx(),0);			//	Login Client loaded later
			else
				MClient.getAll(Env.getCtx());
		}
		catch (Exception e)
		{
			log.warning("Environment problems: " + e.toString());
		}

		//	Start Workflow Document Manager (in other package) for PO
		/*Quynhnv.x8: Bo workflow roi
		String className = null;
		try
		{
			className = "org.compiere.wf.DocWorkflowManager";
			Class.forName(className);
			//	Initialize Archive Engine
			className = "org.compiere.print.ArchiveEngine";
			Class.forName(className);
		}
		catch (Exception e)
		{
			log.warning("Not started: " + className + " - " + e.getMessage());
		}
		*/
		if (!isClient)
			DB.updateMail();
				
		return true;
	}	//	startupEnvironment

	public static URL getResource(String name) {
		return Core.getResourceFinder().getResource(name);
	}
	
	public static synchronized void stop() {
		if (threadPoolExecutor != null) {
			threadPoolExecutor.shutdown();
		}
		log = null;
	}
	
	public static ScheduledThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}
	
	/**
	 *  @param l listener
	 */
	public static synchronized void removeServerStateChangeListener(ServerStateChangeListener l)
	{
		m_listenerList.remove(ServerStateChangeListener.class, l);
	}
	/**
	 *  @param l listener
	 */
	public static synchronized void addServerStateChangeListener(ServerStateChangeListener l)
	{
		m_listenerList.add(ServerStateChangeListener.class, l);
	}
	
	private static synchronized void fireServerStateChanged(ServerStateChangeEvent e)
	{
		ServerStateChangeListener[] listeners = m_listenerList.getListeners(ServerStateChangeListener.class);
		for (int i = 0; i < listeners.length; i++)
        	listeners[i].stateChange(e);
	}
}	