/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.util.LinkedList;
import java.util.concurrent.PriorityBlockingQueue;

import jgpstrackedit.map.util.TileNumber;


/** Abstract superclass for all tile loaders
 * @author hlutnik
 *
 */
public abstract class AbstractTileLoader extends AbstractDiskTileHandler {
	
	public AbstractTileLoader() {
		setCommandQueue(new PriorityBlockingQueue<AbstractTileCommand>());
	}
	
	private LinkedList<TileLoadObserver> tileLoadObservers =
			new LinkedList<TileLoadObserver>();


	/**
	 * @return the tileLoadObservers
	 */
	public LinkedList<TileLoadObserver> getTileLoadObservers() {
		return tileLoadObservers;
	}

	public synchronized void addTileLoadObserver(TileLoadObserver tileLoadObserver) {
		tileLoadObservers.add(tileLoadObserver);
	}
	
	public synchronized void removeTileLoadObserver(TileLoadObserver tileLoadObserver) {
		tileLoadObservers.remove(tileLoadObserver);
	}
    
	protected abstract void loadTile(TileNumber tileNumber, int priority);
	
	public void loadTile(TileNumber tileNumber) {
		loadTile(tileNumber,1);
	}

	public void loadTileInBackground(TileNumber tileNumber) {
		loadTile(tileNumber,0);
	}
	

	
}
