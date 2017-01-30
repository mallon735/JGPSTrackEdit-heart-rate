/**
 * 
 */
package jgpstrackedit.map.util;

/** All objects which wants to be informed about updated map tiles should register
 * themselves as MapObserver.
 * @author Hubert
 *
 */
public interface MapObserver {

	/** Indicates that at least one tile of the map has been updated.
	 * 
	 */
	public void mapTilesUpdated();

}
