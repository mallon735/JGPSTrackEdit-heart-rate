/**
 * 
 */
package jgpstrackedit.map;

/**
 * @author Hubert
 *
 */
public class HikeBikeTileManager extends AbstractOSMTileManager {

	public HikeBikeTileManager() {
		setMapName("HikeBikeMap");
		// old URL: setBaseURL("http://toolserver.org/tiles/hikebike"); 
		setBaseURL("http://a.tiles.wmflabs.org/hikebike"); 
		setMaxZoom(18);
	}
	
	

}
