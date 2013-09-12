package ro.fortsoft.gogo.util;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Decebal Suiu
 */
public class JarUtils {

	/**
     * Returns the name of the jar file, or null if no "Main-Class"
     * manifest attributes was defined.
     */
    public static String getMainClassName(String filename) throws IOException {
    	JarFile jarfile = new JarFile(filename);
        Manifest manifest = jarfile.getManifest();

        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes == null) {
        	return null;
        }
        
        return mainAttributes.getValue(Attributes.Name.MAIN_CLASS);
    }

}
