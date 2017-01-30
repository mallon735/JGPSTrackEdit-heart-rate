/**
 * 
 */
package jgpstrackedit.config;

/**
 * @author Hubert
 *
 */
public interface ConfigurationObserver {
	
	/** This method called if the configuration parameters has been changed. 
	 * A class implementing this method should perform all action needed
	 * to reflect the change.
	 */
	public void configurationChanged();

}
