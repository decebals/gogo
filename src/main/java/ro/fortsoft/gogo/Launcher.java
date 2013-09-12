package ro.fortsoft.gogo;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Set;

import ro.fortsoft.gogo.util.JarUtils;
import ro.fortsoft.gogo.util.ReflectionUtils;

/**
 * @author Decebal Suiu
 */
public class Launcher {

	public static final String LAUNCHER_PROPERTIES = "gogo.properties";

	// property names
	public static final String APP_MAIN_JAR = "app.main.jar";
	public static final String APP_MAIN_CLASS = "app.main.class";
	public static final String APP_CLASS_PATH = "app.class.path";

	public static void main(String[] args) {
		Launcher launcher = new Launcher();

		ExtendedProperties properties;
		try {
			properties = launcher.getProperties();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		properties.list(System.out);

		// create the class loader for the application jar file
		LauncherClassLoader launcherClassLoader = new LauncherClassLoader();
		System.out.println("launcherClassLoader = '" + launcherClassLoader + "'");

		// load app resources (classes and jars)
		String appClassPath = properties.getProperty(APP_CLASS_PATH);
		if (appClassPath != null) {
			String[] entries = appClassPath.split(";");
			if (entries != null) {
				for (String entry : entries) {
					if (entry.endsWith("/*")) {
						launcherClassLoader.loadJars(entry.substring(0, entry.length() -1));
					} else {
						launcherClassLoader.loadClasses(entry);
					}
				}
			}
		}
		launcherClassLoader.list(System.out);
		
		// get the application's main class name
		String mainClassName = properties.getProperty(APP_MAIN_CLASS);
		if (mainClassName == null) {
			String mainJar = properties.getProperty(APP_MAIN_JAR);
			if (mainJar != null) {
				fatal("Property '" + APP_MAIN_JAR + "' cannot be null");
			}
			
			try {
				if (!launcherClassLoader.contains(mainJar)) {
					launcherClassLoader.loadJar(mainJar);
				}
				mainClassName = JarUtils.getMainClassName(mainJar);
			} catch (IOException e) {
				fatal(e);
			}
			
			if (mainClassName == null) {
				fatal(mainJar + " file does not contains a 'Main-Class' manifest attribute");
			}
		}
		System.out.println("mainClassName = '" + mainClassName + "'");

		// export vm.* properties as system properties
		ExtendedProperties vmProperties = properties.getSection("vm");		
		System.getProperties().putAll(vmProperties);
		
		// invoke application's main class
		try {
			ReflectionUtils.invokeMain(mainClassName, args, launcherClassLoader);
		} catch (ClassNotFoundException e) {
			fatal("class not found: '" + mainClassName + "'");
		} catch (NoSuchMethodException e) {
			fatal("class does not define a 'main' method: '" + mainClassName + "'");
		} catch (InvocationTargetException e) {
			fatal(e);
		}
	}

	private ExtendedProperties getProperties() throws IOException {
		// cascading properties
		Properties systemProperties = System.getProperties();
		String propertiesFile = systemProperties.getProperty(LAUNCHER_PROPERTIES, "gogo.properties");
		System.out.println("propertiesFile = " + propertiesFile);

		Properties defaultProperties = new Properties();
		defaultProperties.load(getClass().getResourceAsStream("/gogo.properties"));

		/*
    	// check and throw a nice esxception message
    	File tmp = new File(propertiesFile);
    	if (!(tmp.exists() && tmp.isFile())) {
    		throw new FileNotFoundException("Cannot find file '" + tmp +"'");
    	}
		 */

		ExtendedProperties launcherProperties = new ExtendedProperties(defaultProperties);
		launcherProperties.load(new FileInputStream(propertiesFile));

		// overrides with system properties
		Set<String> names = launcherProperties.stringPropertyNames();
		for (String name : names) {
			if (systemProperties.containsKey(name)) {
				launcherProperties.setProperty(name, systemProperties.getProperty(name));
			}
		} 

		return launcherProperties;
	}

	private static void fatal(String message) {
		System.out.println(message);
		System.exit(1);
	}

	private static void fatal(Exception e) {
		e.printStackTrace(System.out);
		System.exit(1);
	}

}
