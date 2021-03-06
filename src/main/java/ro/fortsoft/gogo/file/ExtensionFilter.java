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
package ro.fortsoft.gogo.file;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

/**
 * @author Decebal Suiu
 */
public class ExtensionFilter extends FileFilter implements FilenameFilter {

    private String extension;

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }

    /**
     * Accepts any file ending in extension. The case of the filename is ignored.
     */
    @Override
    public boolean accept(File file) {
    	if (file.isDirectory()) {
    		return true;
    	}
    	
        // perform a case insensitive check.
        return file.getName().toUpperCase().endsWith(extension.toUpperCase());
    }

    @Override
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }

	@Override
	public String getDescription() {
		return extension + " filter";
	}

}
