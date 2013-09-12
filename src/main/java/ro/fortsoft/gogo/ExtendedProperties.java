package ro.fortsoft.gogo;

import java.util.Properties;

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
            }
            start = result.indexOf("${", start);
            end = result.indexOf("}", start + 2);
        }
        
        return result.toString();
    }
    
}
