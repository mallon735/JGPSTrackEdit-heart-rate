/**
 * 
 */
package jgpstrackedit.map.util;

/**
 * @author Hubert
 *
 */
public enum TileState {
	
	unknown,				// init state
	onDisk,             // tile image is on disk but not yet loaded
	loadingFromWeb,		// tile image is currently loaded from web
	loadingFromDisk,	// tile image is currently loaded from disk
	available			// tile image is available (is loaded)

}
