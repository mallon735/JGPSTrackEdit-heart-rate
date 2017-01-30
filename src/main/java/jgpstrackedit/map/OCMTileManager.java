/**
 * 
 */
package jgpstrackedit.map;

/**
 * @author Hubert
 *
 */
public class OCMTileManager extends AbstractOSMTileManager {

	public OCMTileManager() {
		setMapName("OpenCycleMap");
		setBaseURL("http://tile.opencyclemap.org/cycle");
		setMaxZoom(16);
	}

}
