/**
 * 
 */
package jgpstrackedit.map.tilehandler;


/**
 * @author hlutnik
 *
 */
public interface TileLoadObserver {
	
	/** This method is called when an image has been loaded successfully.
	 * 
	 * @param tileLoadEvent event holds more information about loaded image
	 */
	public void tileLoaded(TileLoadEvent tileLoadEvent);

}
