package ro.fortsoft.gogo;

import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ro.fortsoft.gogo.file.DirectoryFilter;
import ro.fortsoft.gogo.file.JarFilter;

/**
 * @author Decebal Suiu
 */
public class LauncherClassLoader extends URLClassLoader {

    public LauncherClassLoader() {
        super(new URL[0]);
    }

    /*
    @Override
	public void addURL(URL url) {
		super.addURL(url);
	}
	*/

    public boolean loadClasses(String classesDirectory) {
		return loadClasses(new File(classesDirectory));
	}

	public boolean loadClasses(File classesDirectory) {
		try {
			URL url = classesDirectory.toURI().toURL();
//			System.out.println("adding '" + url + "' to the class loader path");
			addURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean loadJars(String jarsDirectory) {
	    return loadJars(new File(jarsDirectory));
	}

	public boolean loadJars(File jarsDirectory) {
		// make jarsDirectory absolute
		File directory = jarsDirectory.getAbsoluteFile();

		List<String> jars = new ArrayList<String>();
		getJars(jars, directory);

		for (String jar : jars) {
			File jarFile = new File(directory, jar);

			try {
				URL url = jarFile.toURI().toURL();
//				System.out.println("adding '" + url + "' to the class loader path");
				addURL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public boolean loadJar(String jarFile) {
	    return loadJar(new File(jarFile));
	}

	public boolean loadJar(File jarFile) {
		// make jarFile absolute
		File file = jarFile.getAbsoluteFile();

		try {
			URL url = file.toURI().toURL();
//			System.out.println("adding '" + url + "' to the class loader path");
			addURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * List all URLs in alphabetical order.
	 * @param out
	 */
    public void list(PrintStream out) {
    	out.println("-- listing class loader urls --");
        URL[] urls = getURLs();
        List<String> tmp = new ArrayList<String>();
        for (URL url : urls) {
        	tmp.add(url.toString());
        }
        
        Collections.sort(tmp, String.CASE_INSENSITIVE_ORDER);
        
        for (String url : tmp) {
        	out.println(url);
        }
    }
    
    public boolean contains(String jarFile) throws MalformedURLException {
    	URL url = new File(jarFile).toURI().toURL();
    	
        URL[] urls = getURLs();
        for (URL entry : urls) {
        	if (url.equals(entry)) {
        		return true;
        	}
        }
    	
        return false;
    }
    
	protected void getJars(List<String> jars, File file) {
		JarFilter jarFilter = new JarFilter();
		DirectoryFilter directoryFilter = new DirectoryFilter();

		if (file.exists() && file.isDirectory() && file.isAbsolute()) {
			String[] jarList = file.list(jarFilter);
			for (int i = 0; (jarList != null) && (i < jarList.length); ++i) {
				jars.add(jarList[i]);
			}

			String[] directories = file.list(directoryFilter);
			for (int i = 0; (directories != null) && (i < directories.length); ++i) {
				getJars(jars, new File(file, directories[i]));
			}
		}
	}

}