/**
 * 
 */
package jgpstrackedit.map;

import jgpstrackedit.map.util.TileNumber;


/**
 * @author Hubert
 *
 */
public class GoogleMapHybridTileManager extends AbstractGoogleTileManager {

	public GoogleMapHybridTileManager() {
		setMapName("GoogleMapHybrid");
		setBaseURL("http://maps.google.com/maps/api");
		setMaxZoom(19);
	}

	/* (non-Javadoc)
	 * @see jgpstrackedit.map.TileManager#getTileURL(jgpstrackedit.map.TileNumber)
	 */
	@Override
	public String getTileURL(TileNumber tileNumber) {
		// TODO Auto-generated method stub
        return getTileURL(tileNumber,"hybrid");
	}

}
