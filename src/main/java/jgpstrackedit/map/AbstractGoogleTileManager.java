/**
 * 
 */
package jgpstrackedit.map;

import jgpstrackedit.map.util.TileBoundary;
import jgpstrackedit.map.util.TileNumber;

/**
 * @author Hubert
 *
 */
public abstract class AbstractGoogleTileManager extends TileManager {

	public String getTileURL(TileNumber tileNumber,String mapType) {
		//http://maps.google.com/maps/api/staticmap?center=46.61099090,14.28494490&zoom=16&size=256x256&maptype=hybrid&sensor=false
        TileBoundary tb = TileBoundary.getTileBoundary(tileNumber);
        double latitude = (tb.getNorth() + tb.getSouth() ) / 2.0;
        double longitude = (tb.getWest() + tb.getEast() ) / 2.0;
		return getBaseURL()+"/staticmap?center="+latitude+","+longitude+"&zoom="+
        		tileNumber.getZoom()+"&size=256x256&maptype="+mapType+"&sensor=false";
	}

}
