package jgpstrackedit.map.tiledownload;

import jgpstrackedit.map.util.TileNumber;

public interface CopyErrorObserver {
	
	public void errorOccured(TileNumber tileNumber);

}
