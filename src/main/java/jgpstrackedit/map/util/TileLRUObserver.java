/**
 * 
 */
package jgpstrackedit.map.util;


/** Classes which want to be informed when a tile image should be freed from memory
 * should implement the TileLRUObserver interface and register themself to a TileLRUList.
 * @author hlutnik
 *
 */
public interface TileLRUObserver {
	
    /**
     * The given tile image should be cleared from memory
     * @param tileNumber tile to clear
     */
	public void freeTile(TileNumber tileNumber);

}
