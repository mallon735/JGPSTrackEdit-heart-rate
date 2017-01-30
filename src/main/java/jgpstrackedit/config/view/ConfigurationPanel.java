/**
 * 
 */
package jgpstrackedit.config.view;

/** Each panel of the Configuration dialogs tab panel must implement this interface.
 * @author Hubert
 *
 */
public interface ConfigurationPanel {
	
    /**
     * returns the name of the tab of the tabpanel
     * @return the name of the tab
     */
	public String getTabName();

    /**
     * Initialize the configuration data. this method is called before the ConfigurationDialog is opened.
     */
	public void initialize();
	
	/**
	 * Save the configuration data. This method is called if the user hits the OK-button of the
	 * ConfigurationDialog.
	 */
	public void save();
	
	/**
	 * Cancel any changes of the configuration data. This method is called if the user Hits the Cancel-button of the
	 * ConfigurationDialog.
	 */
	public void cancel();

}
