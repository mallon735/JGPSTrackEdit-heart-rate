package jgpstrackedit.map;

import jgpstrackedit.map.util.TileNumber;

/**
 * Use Thunderforest cycle map
 * https://tile.thunderforest.com/cycle/{z}/{x}/{y}.png?apikey=<insert-your-apikey-here>
 * 
 * @author gerdba
 *
 */
public class ThunderForestCycleMapTileManager extends AbstractOSMTileManager{
	private String apiKey = null;

	public ThunderForestCycleMapTileManager(String apiKey) {
		super();
		this.apiKey = apiKey != null ? apiKey.trim() : null;
		
		setMapName("ThunderforestCycleMap");
		setBaseURL("https://tile.thunderforest.com/cycle");
		setMaxZoom(16);
	}
	
	@Override
	public String getTileURL(TileNumber tileNumber) {
		String url = super.getTileURL(tileNumber);
		if(apiKey != null & apiKey.length() > 0) {
			url += ("?apikey=" + apiKey);
		}
		return url;
	}
}
