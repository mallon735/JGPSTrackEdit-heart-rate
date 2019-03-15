/**
 * 
 */
package jgpstrackedit.map;

/**
 * @author Hubert
 *
 */
public class FourUMapsTileManager extends AbstractOSMTileManager{

	public FourUMapsTileManager() {
		setMapName("4UMap");
		// old URL: setBaseURL("http://4UMaps.eu");
		setBaseURL("https://tileserver.4umaps.com");
		
		setMaxZoom(15);
	}

}
