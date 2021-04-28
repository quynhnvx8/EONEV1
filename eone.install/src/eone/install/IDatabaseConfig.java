package eone.install;

public interface IDatabaseConfig {


	public String getDatabaseName(String nativeConnectioName);


	public void init(ConfigurationData configurationData);


	public String[] discoverDatabases(String selected);


	public String test(IDBConfigMonitor monitor, ConfigurationData data);


	public String getName();
}
