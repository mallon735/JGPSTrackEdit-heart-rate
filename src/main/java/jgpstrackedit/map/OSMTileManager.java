/**
 * 
 */
package jgpstrackedit.map;

/** Tile manager for OSM (Open Street Map) maps, see http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 * @author Hubert
 *
 */
public class OSMTileManager extends AbstractOSMTileManager {

	public OSMTileManager() {
		setMapName("OpenStreetMap");
		setBaseURL("http://tile.openstreetmap.org");
		setMaxZoom(18);
	}


}
