/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import jgpstrackedit.map.util.TileNumber;

/**
 * @author Hubert
 *
 */
public class WebTileLoader extends AbstractTileLoader {
	
	
	public WebTileLoader() {
		super();
	}
	
    @Override
	protected void loadTile(TileNumber tileNumber, int priority) {
		WebTileLoadCommand command = new WebTileLoadCommand();
		command.setTileNumber(tileNumber);
		command.addTileLoadObserver(getTileLoadObservers());
		command.setPriority(priority);
		addCommand(command);
	}


}
