/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.awt.Image;
import java.util.concurrent.LinkedBlockingQueue;

import jgpstrackedit.map.util.TileNumber;

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
