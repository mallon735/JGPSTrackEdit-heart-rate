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
public class MapQuestTileManager extends TileManager {

	public MapQuestTileManager() {
		setMapName("MapQuest");
		setBaseURL("http://open.mapquestapi.com/staticmap/v4");
		http://open.mapquestapi.com/staticmap/v4/getmap?size=400,200&zoom=9&center=40.0378,-76.305801
		setMaxZoom(18);

	}
	/* (non-Javadoc)
	 * @see jgpstrackedit.map.TileManager#getTileURL(jgpstrackedit.map.TileNumber)
	 * http://open.mapquestapi.com/staticmap/wizard.html
	 */
	@Override
	public String getTileURL(TileNumber tileNumber) {
		// TODO Auto-generated method stub
        TileBoundary tb = TileBoundary.getTileBoundary(tileNumber);
        double latitude = (tb.getNorth() + tb.getSouth() ) / 2.0;
        double longitude = (tb.getWest() + tb.getEast() ) / 2.0;
        //http://open.mapquestapi.com/staticmap/v4/getmap?size=600,500&type=map&zoom=9&center=39.740112,-104.98485600000001&imagetype=JPEG
		return getBaseURL()+"/getmap?key=Fmjtd%7Cluubn96ynu%2C2s%3Do5-907guw&size=256,256&type=map&zoom="+
        		tileNumber.getZoom()+"&center="+latitude+","+longitude+"&imagetype=png";
	}

}
