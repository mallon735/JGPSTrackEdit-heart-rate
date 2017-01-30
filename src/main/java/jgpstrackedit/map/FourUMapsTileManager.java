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
		setBaseURL("http://4UMaps.eu");
		setMaxZoom(15);
	}

}
