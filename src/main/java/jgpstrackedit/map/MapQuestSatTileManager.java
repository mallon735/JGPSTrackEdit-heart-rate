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
public class MapQuestSatTileManager extends TileManager {
	public MapQuestSatTileManager() {
		setMapName("MapQuestSat");
		setBaseURL("http://open.mapquestapi.com/staticmap/v4");
		setMaxZoom(16);

	}
	/* (non-Javadoc)
	 * @see jgpstrackedit.map.TileManager#getTileURL(jgpstrackedit.map.TileNumber)
	 * http://open.mapquestapi.com/staticmap/
	 */
	@Override
	public String getTileURL(TileNumber tileNumber) {
		// TODO Auto-generated method stub
        TileBoundary tb = TileBoundary.getTileBoundary(tileNumber);
        double latitude = (tb.getNorth() + tb.getSouth() ) / 2.0;
        double longitude = (tb.getWest() + tb.getEast() ) / 2.0;
        //http://open.mapquestapi.com/staticmap/v4/getmap?size=600,500&type=map&zoom=9&center=39.740112,-104.98485600000001&imagetype=JPEG
		return getBaseURL()+"/getmap?key=Fmjtd%7Cluubn96ynu%2C2s%3Do5-907guw&size=256,256&type=sat&zoom="+
        		tileNumber.getZoom()+"&center="+latitude+","+longitude+"&imagetype=png";
	}
}
