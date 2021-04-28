package eone.install;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

import org.compiere.util.Ini;

/**
 *
 * @author Quynhnv.x8. Mod 19/10/2020
 *
 */
public class ConfigurationConsole {

	ConfigurationData data = new ConfigurationData(null);

	public void doSetup() {
		BufferedReader reader = null;
		PrintWriter writer  = null;
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new PrintWriter(System.out, true);

		Ini.setShowLicenseDialog(false);
		data.load();
		data.initJava();

		try {
			jvmHome(reader, writer);

			adempiereHome(reader, writer);
			keyStorePass(reader, writer);

			appServerHostname(reader, writer);
			appServerWebPort(reader, writer);
			appServerSSLPort(reader, writer);

			dbExists(reader, writer);
			dbType(reader, writer);

			dbHostname(reader, writer);
			dbPort(reader, writer);
			dbName(reader, writer);
			dbUser(reader, writer);
			dbPassword(reader, writer);
			dbSystemPassword(reader, writer);

			writer.println("Save changes (Y/N) [Y]: ");
			String yesNo = reader.readLine();
			if ((yesNo == null || yesNo.trim().length() == 0) || "y".equalsIgnoreCase(yesNo))
			{
				boolean b = data.save();
				if (b)
					writer.println("Changes save successfully.");
				else
					writer.println("Failed to save changes.");
			}
			else
			{
				writer.println("Changes ignore.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	private void dbPort(BufferedReader reader, PrintWriter writer) throws IOException {
		while (true)
		{
			writer.println("Database Server Port ["+data.getDatabasePort()+"]:");
			String input = reader.readLine();
			if (input != null && input.trim().length() > 0)
			{
				try
				{
					int inputPort = Integer.parseInt(input);
					if (inputPort <= 0 || inputPort > 65535)
					{
						writer.println("Invalid input, please enter a valid port number");
						continue;
					}
					data.setDatabasePort(input);
					break;
				}
				catch (NumberFormatException e){
					writer.println("Invalid input, please enter a valid port number");
					continue;
				}
			}
			break;
		}
	}

	private void dbSystemPassword(BufferedReader reader, PrintWriter writer) throws IOException {
		while (true)
		{
			
			String error = data.testDatabase(null);
			if (error != null && error.trim().length() > 0)
			{
				writer.println("Database test fail: " + error);
				dbExists(reader, writer);
				dbType(reader, writer);
				dbHostname(reader, writer);
				dbPort(reader, writer);
				dbName(reader, writer);
				dbUser(reader, writer);
				dbPassword(reader, writer);
				continue;
			}
			break;
		}
	}

	private void dbPassword(BufferedReader reader, PrintWriter writer) throws IOException {
		writer.println("Database Password [" + data.getDatabasePassword()+"]:");
		String dbPassword = reader.readLine();
		if (dbPassword != null && dbPassword.trim().length() > 0)
		{
			data.setDatabasePassword(dbPassword);
		}
	}

	private void dbUser(BufferedReader reader, PrintWriter writer) throws IOException {
		writer.println("Database user ["+data.getDatabaseUser()+"]:");
		String dbUser = reader.readLine();
		if (dbUser != null && dbUser.trim().length() > 0)
		{
			data.setDatabaseUser(dbUser);
		}
	}

	private void dbName(BufferedReader reader, PrintWriter writer) throws IOException {
		writer.println("Database Name["+data.getDatabaseName()+"]:");
		String dbName = reader.readLine();
		if (dbName != null && dbName.trim().length() > 0)
		{
			data.setDatabaseName(dbName);
		}
	}

	private void dbHostname(BufferedReader reader, PrintWriter writer) throws IOException {
		writer.println("Database Server Host Name ["+data.getDatabaseServer()+"]:");
		String hostName = reader.readLine();
		if (hostName != null && hostName.trim().length() > 0)
		{
			data.setDatabaseServer(hostName);
		}
	}

	private void appServerSSLPort(BufferedReader reader, PrintWriter writer) throws IOException {
		while (true)
		{
			writer.println("Application Server SSL Port["+data.getAppsServerSSLPort()+"]:");
			String input = reader.readLine();
			if (input != null && input.trim().length() > 0)
			{
				try
				{
					int inputPort = Integer.parseInt(input);
					if (inputPort <= 0 || inputPort > 65535)
					{
						writer.println("Invalid input, please enter a valid port number");
						continue;
					}
					data.setAppsServerSSLPort(input);
					String error = data.testAppsServer();
					if (error != null && error.trim().length() > 0)
					{
						writer.println("Application server test fail: " + error);
						appServerHostname(reader, writer);
						appServerWebPort(reader, writer);
						continue;
					}
					break;
				}
				catch (NumberFormatException e){
					writer.println("Invalid input, please enter a valid port number");
					continue;
				}
			}
			break;
		}

	}

	private void appServerWebPort(BufferedReader reader, PrintWriter writer) throws IOException {
		while (true)
		{
			writer.println("Application Server Web Port ["+data.getAppsServerWebPort()+"]:");
			String input = reader.readLine();
			if (input != null && input.trim().length() > 0)
			{
				try
				{
					int inputPort = Integer.parseInt(input);
					if (inputPort <= 0 || inputPort > 65535)
					{
						writer.println("Invalid input, please enter a valid port number");
						continue;
					}
					data.setAppsServerWebPort(input);
					break;
				}
				catch (NumberFormatException e){
					writer.println("Invalid input, please enter a valid port number");
					continue;
				}
			}
			break;
		}

	}

	private void appServerHostname(BufferedReader reader, PrintWriter writer) throws IOException {
		writer.println("Application Server Host Name ["+data.getAppsServer()+"]:");
		String hostName = reader.readLine();
		if (hostName != null && hostName.trim().length() > 0)
		{
			data.setAppsServer(hostName);
		}
	}

	private void keyStorePass(BufferedReader reader, PrintWriter writer) throws Exception {
		while (true)
		{
			writer.println("Key Store Password [" + data.getKeyStore() + "]:");
			String password = reader.readLine();
			if (password != null && password.trim().length() > 0)
			{
				data.setKeyStore(password);
			}
			else
			{
				password = data.getKeyStore();
			}

			File adempiereHome = new File(data.getAdempiereHome());
			String fileName = KeyStoreMgt.getKeystoreFileName(adempiereHome.getAbsolutePath());
			KeyStoreMgt storeMgt = new KeyStoreMgt (fileName, password.toCharArray());
			KeyStore keyStore = storeMgt.getKeyStore();
			if (keyStore == null)
			{
				String cn = data.getProperty(ConfigurationData.EONE_CERT_CN);
				if (cn == null)
					cn = System.getProperty("user.name");
				String ou = data.getProperty(ConfigurationData.EONE_CERT_ORG_UNIT);
				if (ou == null)
					ou = "EONE";//iDempiereUser
				String o = data.getProperty(ConfigurationData.EONE_CERT_ORG);
				if (o == null)
					o = System.getProperty("user.name");
				String lt = data.getProperty(ConfigurationData.EONE_CERT_LOCATION);
				if (lt == null)
					lt = "MyTown";
				String st = data.getProperty(ConfigurationData.EONE_CERT_STATE);
				if (st == null) st = "";
				String country = data.getProperty(ConfigurationData.EONE_CERT_COUNTRY);
				if (country == null)
					country = System.getProperty("user.country");

				writer.println("KeyStore Settings.");
				writer.println("(ON) Common Name [" + cn + "]:");
				String input = reader.readLine();
				if (input != null && input.trim().length() > 0)
				{
					cn = input;
					data.updateProperty(ConfigurationData.EONE_CERT_CN, input);
				}

				writer.println("(OU) Organization Unit [" + ou + "]:");
				input = reader.readLine();
				if (input != null && input.trim().length() > 0)
				{
					ou = input;
					data.updateProperty(ConfigurationData.EONE_CERT_ORG_UNIT, ou);
				}

				writer.println("(O) Organization [" + o + "]:");
				input = reader.readLine();
				if (input != null && input.trim().length() > 0)
				{
					o = input;
					data.updateProperty(ConfigurationData.EONE_CERT_ORG, o);
				}

				writer.println("(L) Locale/Town [" + lt + "]:");
				input = reader.readLine();
				if (input != null && input.trim().length() > 0)
				{
					lt = input;
					data.updateProperty(ConfigurationData.EONE_CERT_LOCATION, lt);
				}

				writer.println("(S) State [" + st + "]:");
				input = reader.readLine();
				if (input != null && input.trim().length() > 0)
				{
					st = input;
					data.updateProperty(ConfigurationData.EONE_CERT_STATE, st);
				}

				writer.println("(C) Country (2 Char) [" + country +"]");
				input = reader.readLine();
				if (input != null && input.trim().length() > 0)
				{
					country = input;
					data.updateProperty(ConfigurationData.EONE_CERT_COUNTRY, input);
				}

			}

			String error = data.testAdempiere();
			if (error != null && error.trim().length() > 0)
			{
				writer.println("Eone home and keystore validation error: " + error);
				adempiereHome(reader, writer);
				continue;
			}
			break;
		}
	}

	private void adempiereHome(BufferedReader reader, PrintWriter writer) throws IOException {
		writer.println("iDempiere Home ["+data.getAdempiereHome()+"]:");
		String input = reader.readLine();
		if (input != null && input.trim().length() > 0)
		{
			data.setAdempiereHome(input);
		}
	}

	private void jvmHome(BufferedReader reader, PrintWriter writer) throws IOException {
		while (true)
		{
			writer.println("Java Home ["+data.getJavaHome()+"]:");
			String input = reader.readLine();
			if (input != null && input.trim().length() > 0)
			{
				data.setJavaHome(input);
			}
			String error = data.testJava();
			if (error != null && error.trim().length() > 0)
			{
				writer.println("JVM test fail: " + error);
				continue;
			}
			break;
		}
	}

	private void dbExists(BufferedReader reader, PrintWriter writer) throws IOException {
		String dbExists = data.getDatabaseExists() ? "Y" : "N";
	
		writer.println("DB Already Exists?(Y/N) [" + dbExists + "]: ");
		String yesNo = reader.readLine();
		if (yesNo == null || yesNo.trim().length() == 0)
			yesNo = dbExists;
		if ("n".equalsIgnoreCase(yesNo))
		{
			data.setDatabaseExists("N");
		}
		else
		{
		    data.setDatabaseExists("Y");
		}
		
	}

	private void dbType(BufferedReader reader, PrintWriter writer) throws IOException {
		String dbType = data.getDatabaseType();
		int dbTypeSelected = 0;
		for(int i = 0; i < ConfigurationData.DBTYPE.length; i++)
		{
			if (ConfigurationData.DBTYPE[i].equals(dbType))
			{
				dbTypeSelected = i;
				break;
			}
		}
//		console.writer().println("JVM Type:");
		for(int i = 0; i < ConfigurationData.DBTYPE.length; i++)
		{
			writer.println((i+1)+". "+ConfigurationData.DBTYPE[i]);
		}

		while (true)
		{
			writer.println("Database Type ["+(dbTypeSelected+1)+"]");
			String input = reader.readLine();
			try
			{
				if (input == null || input.trim().length() == 0)
				{
					input = Integer.toString(dbTypeSelected+1);
				}
				int inputIndex = Integer.parseInt(input);
				if (inputIndex <= 0 || inputIndex > ConfigurationData.DBTYPE.length)
				{
					writer.println("Invalid input, please enter numeric value of 1 to " + ConfigurationData.DBTYPE.length);
					continue;
				}
				if (dbTypeSelected+1 != inputIndex)
					data.dbChanged();
				data.initDatabase(ConfigurationData.DBTYPE[inputIndex-1]);
				data.setDatabaseType(ConfigurationData.DBTYPE[inputIndex-1]);
				break;
			}
			catch (NumberFormatException e){
				writer.println("Invalid input, please enter numeric value of 1 to " + ConfigurationData.DBTYPE.length);
			}
		}
	}
}
