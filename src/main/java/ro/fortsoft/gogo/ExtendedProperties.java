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
package ro.fortsoft.gogo;

import java.util.Properties;
import java.util.Set;

/**
 * @author Decebal Suiu
 */
public class ExtendedProperties extends Properties {

	private static final long serialVersionUID = 1L;

	public ExtendedProperties() {
		super();
	}

	public ExtendedProperties(Properties defaults) {
		super(defaults);
	}

	/**
	 * Retrieves the property's value. Also if the value contains variables (${propertyName}) 
	 * then the variables are resolved. 
	 */
	@Override
    public String getProperty(String key) {
        String value = super.getProperty(key);
        if (value != null) {
        	value = evaluate(value);
        }
        
        return value;
    }
    
    @Override
    public String getProperty(String key, String defaultValue) {
        String value = super.getProperty(key);
        if (value != null) {
        	value = evaluate(value);
        }
        
        if (value == null) {
        	value = defaultValue;
        }
        
        return value;
    }
    
    /**
     * Retrieves all properties for a section (all properties that starts with "section." prefix).
     * Property names retrieved for a section don't contain the section name.
     * For example:
     * <code>
     *     # app section
     *     app.class.path=lib/*
     *     app.main.class=com.acme.Main
     *     
     *     # vm section
     * 		vm.http.proxyHost=127.0.0.1
     * 		vm.http.proxyPort=3128
     * 
     * 		getSection("app"); // =>
     *     class.path=lib/*
     *     main.class=com.acme.Main
     * 
     * 		getSection("vm"); // =>
     * 		http.proxyHost=127.0.0.1
     * 		http.proxyPort=3128
     * </code>
     */
    public ExtendedProperties getSection(String section) {
    	ExtendedProperties sectionProperties = new ExtendedProperties();
    	
    	String prefix = section + '.'; 
    	Set<String> names = stringPropertyNames();
    	for (String name : names) {
    		if (name.startsWith(prefix)) {
    			sectionProperties.setProperty(name.substring(prefix.length()), getProperty(name));
    		}
    	}
    	
    	return sectionProperties;
    }
    
    private String evaluate(String value) {
        if ((value == null) || (value.length() < 4)) {
            return value;
        }
        
        StringBuilder result = new StringBuilder(value.length());
        result.append(value);
        int start = result.indexOf("${");
        int end = result.indexOf("}", start + 2);
        while ((start >= 0) && (end > start)) {
            String paramName = result.substring(start + 2, end);
            String paramValue = getProperty(paramName);
            if (paramValue != null) {
                result.replace(start, end + 1, paramValue);
                start += paramValue.length();
            } else {
                start = end + 1;
                // TODO throws an exception ?
                System.out.println("Cannot found parameter '" + paramName + "'");
            }
            start = result.indexOf("${", start);
            end = result.indexOf("}", start + 2);
        }
        
        return result.toString();
    }
    
}
