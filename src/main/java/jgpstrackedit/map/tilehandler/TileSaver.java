/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import jgpstrackedit.map.util.TileNumber;

import java.awt.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Hubert
 *
 */
public class TileSaver extends AbstractDiskTileHandler {
	
	public TileSaver() {
		setCommandQueue(new LinkedBlockingQueue<AbstractTileCommand>());
	}
	
	public void saveImage(Image image, TileNumber tileNumber) {
		TileSaveCommand command = new TileSaveCommand();
		command.setTileImage(image);
		command.setTileNumber(tileNumber);
		command.setBaseDirectory(getBaseDirectory());
		addCommand(command);
		
	}

}
