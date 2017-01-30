/**
 * 
 */
package jgpstrackedit.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import jgpstrackedit.util.Parser;
import jgpstrackedit.international.International;

/**
 * @author Hubert
 * 
 */
public class Configuration {


	private static Properties properties = null;
	
	private static ArrayList<ConfigurationObserver> observers = new ArrayList<ConfigurationObserver>();
	
	public static void addConfigurationObserver(ConfigurationObserver observer) {
		observers.add(observer);
	}

	public static void removeConfigurationObserver(ConfigurationObserver observer) {
		observers.remove(observer);
	}
	
	protected static void notifyConfigurationObservers() {
		for (ConfigurationObserver observer:observers) {
			observer.configurationChanged();
		}
	}


	protected static void checkInit() {
		if (properties == null) {
			properties = new Properties();
			properties.setProperty("SHOW_MAP_ON_STARTUP","1");
			properties.setProperty("MAX_TILES_IN_MEMORY","250");
			properties.setProperty("SELECTED_LINE_WIDTH","1");
			properties.setProperty("UNSELECTED_LINE_WIDTH","1");
			properties.setProperty("POINT_DIAMETER", "7");
			properties.setProperty("SHOW_DIRECTION_BUTTONS", "0");
			properties.setProperty("SHOW_HELP_ON_STARTUP", "1");
			properties.setProperty("COUNTRY_SPECIFIC_MAP", "1");
			properties.setProperty("MAPEXTRACT", "");
			properties.setProperty("GUILOOKFEEL","System");
			properties.setProperty("ROUTINGTYPE","bicycle");
			properties.setProperty("ROUTINGPOINTDISTANCE","10");
			properties.setProperty("ROUTINGAVOIDLIMITEDACCESS","1");
			properties.setProperty("ROUTINGAVOIDTOLLROAD","1");
			properties.setProperty("AVERAGESPEED","20.0");  // km/h
			properties.setProperty("INCLINETIME100METERS","10.0"); // min
			properties.setProperty("BREAKRATIO","0.5"); //
			properties.setProperty("MAXTOURTIME","8.0"); // h
			properties.setProperty("MAPTYPE","OpenStreetMap"); 
			
			properties.setProperty("LOCALE", Locale.getDefault().toString());
			
			FileInputStream inStream;
			try {
				inStream = new FileInputStream("JGPSTrackEdit.properties");
				properties.load(inStream);
				inStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				properties.list(System.out);
				saveProperties();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			International.setCurrentLocale(
					new Locale(
							properties.getProperty("LOCALE").split("_")[0],
							properties.getProperty("LOCALE").split("_")[1]));
			System.out.println(International.getCurrentLocale());
		}
	}
	
	public static String getProperty(String property) {
		checkInit();
		String s = (String)properties.get(property);
		return s;
	}
	
	public static int getIntProperty(String property) {
		checkInit();
		String value = getProperty(property);
		return Parser.parseInt(value);
	}

	/**
	 * Returns the value of the given property as boolean value. True is stored as "1", false is stored as "0"
	 * @param property the property
	 * @return boolean value of the given property
	 */
	public static boolean getBooleanProperty(String property) {
		checkInit();
		String value = getProperty(property);
		return Parser.parseInt(value) == 1;
	}
	

	public static void setProperty(String property, String value) {
		checkInit();
		properties.setProperty(property, value);
	}
	
	public static void saveProperties() {
		notifyConfigurationObservers();
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream("JGPSTrackEdit.properties");
			properties.store(outStream,"JGPSTrackEdit.properties");
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static double getDoubleProperty(String property) {
		// TODO Auto-generated method stub
		checkInit();
		String value = getProperty(property);
		return Parser.parseDouble(value);
	}

	public static double getHourProperty(String property) {
		// TODO Auto-generated method stub
		checkInit();
		String value = getProperty(property);
		return Parser.parseTime(value);
	}

}
