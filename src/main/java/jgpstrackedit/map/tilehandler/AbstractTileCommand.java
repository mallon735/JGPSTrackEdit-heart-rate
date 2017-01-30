/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.util.Comparator;
import java.util.LinkedList;

import jgpstrackedit.map.util.TileNumber;

/** 
 * @author hlutnik
 *
 */
public abstract class AbstractTileCommand implements Comparator<AbstractTileCommand>,
													 Comparable<AbstractTileCommand>{
	
	private TileNumber tileNumber;
	private int priority = 0;
	private LinkedList<TileLoadObserver> observers =
			new LinkedList<TileLoadObserver>();


	public void addTileLoadObserver(TileLoadObserver tileLoadObserver) {
		observers.add(tileLoadObserver);
	}

	public void addTileLoadObserver(LinkedList<TileLoadObserver> tileLoadObservers) {
		observers.addAll(tileLoadObservers);
	}

	public void removeTileLoadObserver(TileLoadObserver tileLoadObserver) {
		observers.remove(tileLoadObserver);
	}
	
	public void notifyTileLoadObservers(TileLoadEvent tileLoadEvent) {
		for (TileLoadObserver observer:observers) {
			observer.tileLoaded(tileLoadEvent);
		}
				
	}

	
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/** Sets the priority of the command. A value of 0 indicates a
	 * background task, a value of 1 a high priority task
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the tileNumber
	 */
	public TileNumber getTileNumber() {
		return tileNumber;
	}

	/**
	 * @param tileNumber the tileNumber to set
	 */
	public void setTileNumber(TileNumber tileNumber) {
		this.tileNumber = tileNumber;
	}
	
	public abstract void doAction();

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(AbstractTileCommand o1, AbstractTileCommand o2) {
		// TODO Auto-generated method stub
		if (o1.getPriority() < o2.getPriority())
			return -1;
		else if (o1.getPriority() > o2.getPriority())
			return 1;
		else
			return 0;
					
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AbstractTileCommand arg0) {
		// TODO Auto-generated method stub
		return compare(this,arg0);
	}

	
	
}
