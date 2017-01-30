/**
 * 
 */
package jgpstrackedit.map;

import jgpstrackedit.map.util.TileNumber;

/** Tile manager for google map satellite, see  http://code.google.com/intl/de/apis/maps/documentation/staticmaps/
 * @author Hubert
 *
 */
public class GoogleMapSatelliteTileManager extends AbstractGoogleTileManager {

	public GoogleMapSatelliteTileManager() {
		setMapName("GoogleMapSatellite");
		setBaseURL("http://maps.google.com/maps/api");
		setMaxZoom(19);
	}

	/* (non-Javadoc)
	 * @see jgpstrackedit.map.TileManager#getTileURL(jgpstrackedit.map.TileNumber)
	 */
	@Override
	public String getTileURL(TileNumber tileNumber) {
		// TODO Auto-generated method stub
        return getTileURL(tileNumber,"satellite");
	}

}
