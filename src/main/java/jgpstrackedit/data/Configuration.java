/**
 * 
 */
package jgpstrackedit.data;

import java.util.Properties;

/**
 * @author Hubert
 * 
 */
public class Configuration {

	private static Configuration configuration = null;

	private Properties properties;

	public Configuration(String fileName) {
		properties = new Properties(defaultProperties());
	}

	protected Properties defaultProperties() {
		Properties defaultProp = new Properties();
		defaultProp.setProperty("STROKE_WIDTH", "1");
		defaultProp.setProperty("POINT_DIAMETER", "7");
		return defaultProp;
	}

	public static Configuration getConfiguration() {
		if (configuration == null) {
			configuration = new Configuration("JGPSTrackEdit.properties");
			// TO ADD-REd in Properties
		}
		return configuration;

	}
	
	public String getProperty(String property) {
		return (String)properties.get(property);
	}
	
	public int getIntProperty(String property) {
		String value = getProperty(property);
		return Integer.parseInt(getProperty(value));
	}
	
	
}
