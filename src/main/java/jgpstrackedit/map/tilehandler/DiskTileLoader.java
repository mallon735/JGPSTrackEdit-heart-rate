/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import jgpstrackedit.map.util.TileNumber;

/**
 * @author Hubert
 *
 */
public class DiskTileLoader extends AbstractTileLoader {

	
	public DiskTileLoader() {
		super();
	}
	
    @Override
	protected void loadTile(TileNumber tileNumber, int priority) {
		DiskTileLoadCommand command = new DiskTileLoadCommand();
		command.setBaseDirectory(getBaseDirectory());
		command.setTileNumber(tileNumber);
		command.addTileLoadObserver(getTileLoadObservers());
		command.setPriority(priority);
		addCommand(command);
	}
	


}
