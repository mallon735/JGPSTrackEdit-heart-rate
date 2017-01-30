/**
 * 
 */
package jgpstrackedit.map;

import jgpstrackedit.map.util.TileNumber;

/** Tile manager for google map, see  http://code.google.com/intl/de/apis/maps/documentation/staticmaps/

 * @author Hubert
 *
 */
public class GoogleMapTileManager extends AbstractGoogleTileManager {

	public GoogleMapTileManager() {
		setMapName("GoogleMap");
		setBaseURL("http://maps.google.com/maps/api");
		setMaxZoom(18);
	}

	/* (non-Javadoc)
	 * @see jgpstrackedit.map.TileManager#getTileURL(jgpstrackedit.map.TileNumber)
	 */
	@Override
	public String getTileURL(TileNumber tileNumber) {
		//http://maps.google.com/maps/api/staticmap?center=46.61099090,14.28494490&zoom=16&size=256x256&maptype=hybrid&sensor=false
        return getTileURL(tileNumber,"roadmap");
	}

}
