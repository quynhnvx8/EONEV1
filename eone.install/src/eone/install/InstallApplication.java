package eone.install;

import java.io.File;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 *
 * @author Admin
 * copy file jettyhome and hazelcast
 *
 */
public class InstallApplication implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		Setup.main(new String[]{});
		Thread.sleep(10000);//10000
		while (Setup.instance.isDisplayable()) {
			Thread.sleep(2000);//2000
		}
		String path = System.getProperty("user.dir") + "/eone.install/build.xml";
		File file = new File(path);
		if (file.exists()) {
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setMessageOutputLevel(Project.MSG_VERBOSE);
			runner.addBuildLogger(DefaultLogger.class.getName());
			runner.run();
			runner.stop();
		}
		return EXIT_OK;
	}

	public void stop() {
	}

}
