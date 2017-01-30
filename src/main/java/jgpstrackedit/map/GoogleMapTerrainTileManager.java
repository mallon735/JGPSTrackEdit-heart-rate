/**
 * 
 */
package jgpstrackedit.map;

import jgpstrackedit.map.util.TileNumber;

/**
 * @author Hubert
 *
 */
public class GoogleMapTerrainTileManager extends AbstractGoogleTileManager {

	public GoogleMapTerrainTileManager() {
		setMapName("GoogleMapTerrain");
		setBaseURL("http://maps.google.com/maps/api");
		setMaxZoom(17);
	}

	/* (non-Javadoc)
	 * @see jgpstrackedit.map.TileManager#getTileURL(jgpstrackedit.map.TileNumber)
	 */
	@Override
	public String getTileURL(TileNumber tileNumber) {
		// TODO Auto-generated method stub
        return getTileURL(tileNumber,"terrain");
	}

}
