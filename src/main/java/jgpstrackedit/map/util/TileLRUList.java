/**
 * 
 */
package jgpstrackedit.map.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/** LRU list of tile numbers. TileLRUObservers may register themself by
 * the addTileLRUObserver() method to be informed of tiles whose tile image should be removed from memory.
 * @author hlutnik
 *
 */
public class TileLRUList extends LinkedHashMap<TileNumber,Object> {

	private int numberElements;
	private ArrayList<TileLRUObserver> observers =
			new ArrayList<TileLRUObserver>();

	/**
	 * Registers a TileLRUObserver.
	 * @param observer the TileLRUObserver
	 */
	public void addTileLRUObserver(TileLRUObserver observer) {
		observers.add(observer);
	}

	/**
	 * Removes the given TileLRUObservers
	 * @param observer the TileLRUObserver
	 */
	public void removeTileLRUObserver(TileLRUObserver observer) {
		observers.remove(observer);
	}

	/** All registered TileLRUObservers are informed that the given
	 * tile image should be freed from memory.
	 * @param tileNumber
	 */
	protected void notifyTileLRUObservers(TileNumber tileNumber) {
		for (TileLRUObserver observer:observers) {
			observer.freeTile(tileNumber);
		}
	}
	/**
	 * @return the numberElements
	 */
	public int getNumberElements() {
		return numberElements;
	}

	/**
	 * @param numberElements the numberElements to set
	 */
	public void setNumberElements(int numberElements) {
		this.numberElements = numberElements;
	}

	/**
	 * Constructor to construct a LRU list for tiles.
	 * @param numberElements the maximum number of tiles which
	 * the LRU list should hold.
	 */
	public TileLRUList(int numberElements) {
		// TODO Auto-generated constructor stub
		super(numberElements*2,(float)0.75,true);
		setNumberElements(numberElements);
	}

	/* (non-Javadoc)
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 */
	@Override
	protected boolean removeEldestEntry(
			java.util.Map.Entry<TileNumber, Object> eldest) {
		if (size() > getNumberElements()) {
			notifyTileLRUObservers(eldest.getKey());
			//System.out.println("LRU("+size()+"): free "+eldest.getKey());
			return true;
		} else {
		  return false;
		}
	}

	/**
	 * Indicates an usage of the given tile number, which is put to the begin of the LRU list.
	 * If the LRU list contains the maximum numberElements tiles,
	 * then the least recently used tile is freed (the tile image is removed from memory).
	 *
	 * @param tileNumber the tile number which has been used recently
	 */
	public void touch(TileNumber tileNumber) {
		put(tileNumber,null);
		//System.out.println("LRU("+size()+"): touch "+tileNumber);
	}

}
