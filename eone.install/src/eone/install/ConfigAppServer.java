package eone.install;

import java.net.InetAddress;
import java.util.logging.Level;


/**
 *	Apps Server Configuration
 *
 *  @author hengsin
 *  @version $Id: ConfigJBoss.java,v 1.3 2006/07/30 00:57:42 jjanke Exp $
 */
public class ConfigAppServer extends Config
{

	/**
	 * 	ConfigJBoss
	 * 	@param data configuration
	 */
	public ConfigAppServer(ConfigurationData data)
	{
		super (data);
	}	//	ConfigJBoss

	/**
	 * 	Initialize
	 */
	public void init()
	{
		p_data.setAppsServerWebPort("8080");
		p_data.setAppsServerWebPort(true);
		p_data.setAppsServerSSLPort("8443");
		p_data.setAppsServerSSLPort(true);
	}	//	init

	/**
	 * 	Test
	 *	@return error message or null if OK
	 */
	public String test()
	{
		//	AppsServer
		String server = p_data.getAppsServer();
		boolean pass = server != null && server.length() > 0;
		InetAddress appsServer = null;
		String error = "Not correct: AppsServer = " + server;
		try
		{
			if (pass)
				appsServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		if (getPanel() != null)
			signalOK(getPanel().okAppsServer, "ErrorAppsServer",
				pass, true, error);
		if (!pass)
			return error;
		if (log.isLoggable(Level.INFO)) log.info("OK: AppsServer = " + appsServer);
		setProperty(ConfigurationData.EONE_APPS_SERVER, appsServer.getHostName());

		//	Web Port
		int WebPort = p_data.getAppsServerWebPort();
		pass = !p_data.testPort ("http", appsServer.getHostName(), WebPort, "/")
			&& p_data.testServerPort(WebPort);
		error = "Not correct: Web Port = " + WebPort;
		if (getPanel() != null)
			signalOK(getPanel().okWebPort, "ErrorWebPort",
				pass, true, error);
		if (!pass)
			return error;
		if (log.isLoggable(Level.INFO)) log.info("OK: Web Port = " + WebPort);
		setProperty(ConfigurationData.EONE_WEB_PORT, String.valueOf(WebPort));

		//	SSL Port
		int sslPort = p_data.getAppsServerSSLPort();
		pass = !p_data.testPort ("https", appsServer.getHostName(), sslPort, "/")
			&& p_data.testServerPort(sslPort);
		error = "Not correct: SSL Port = " + sslPort;
		if (getPanel() != null)
			signalOK(getPanel().okSSLPort, "ErrorWebPort",
				pass, true, error);
		if (!pass)
			return error;
		if (log.isLoggable(Level.INFO)) log.info("OK: SSL Port = " + sslPort);
		setProperty(ConfigurationData.EONE_SSL_PORT, String.valueOf(sslPort));
		//
		return null;
	}	//	test

}	//	ConfigJBoss
