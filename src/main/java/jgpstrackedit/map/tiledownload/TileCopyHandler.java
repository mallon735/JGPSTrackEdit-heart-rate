/**
 * 
 */
package jgpstrackedit.map.tiledownload;

import jgpstrackedit.map.tilehandler.AbstractDiskTileHandler;
import jgpstrackedit.map.tilehandler.AbstractTileCommand;
import jgpstrackedit.map.util.TileNumber;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author hlutnik
 *
 */
public class TileCopyHandler extends AbstractDiskTileHandler {
	
	public TileCopyHandler() {
		setCommandQueue(new LinkedBlockingQueue<AbstractTileCommand>());
	}
	
	public void addCopy(String source, String destination, TileNumber tileNumber, CopyErrorObserver observer) {
		addCommand(new TileCopyCommand(source,destination,tileNumber,observer));
	}

}
