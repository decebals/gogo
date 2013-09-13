/*
 * Copyright 2013 Decebal Suiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
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
