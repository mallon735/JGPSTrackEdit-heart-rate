/**
 * 
 */
package jgpstrackedit.map;

import jgpstrackedit.map.util.TileNumber;

/**
 * @author Hubert
 *
 */
public class AbstractOSMTileManager extends TileManager {

	@Override
	/**
	 * Returns the URL to load the given tile from web. See http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
	 */
	public String getTileURL(TileNumber tileNumber) {
		// TODO Auto-generated method stub
		return getBaseURL()+tileNumber.toString()+".png";
	}

}
