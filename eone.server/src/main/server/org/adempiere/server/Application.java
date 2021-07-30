package org.adempiere.server;

import java.io.File;
import java.util.Properties;

import org.compiere.Adempiere;
import org.compiere.util.Ini;
import org.compiere.util.ServerContext;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;


public class Application implements IApplication {

	
	@Override
	public Object start(IApplicationContext context) throws Exception {
        Properties serverContext = new Properties();
        ServerContext.setCurrentInstance(serverContext);

        String propertyFile = Ini.getFileName(false);
        File file = new File(propertyFile);
        if (!file.exists()) {
        	throw new IllegalStateException("eone.properties file missing. Path="+file.getAbsolutePath());
        }
        if (!Adempiere.isStarted())
        {
	        boolean started = Adempiere.startup(false);
	        if(!started)
	        {
	            throw new Exception("Could not start EONE");
	        }
        }

		return IApplication.EXIT_OK;
	}

	
	@Override
	public void stop() {
	}

}
