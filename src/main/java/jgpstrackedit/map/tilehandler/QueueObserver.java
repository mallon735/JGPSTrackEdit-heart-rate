/**
 * 
 */
package jgpstrackedit.map.tilehandler;

/**
 * @author Hubert
 *
 */
public interface QueueObserver {
	
	/**
	 * Informs the observer that the queue length has been changed.
	 * @param length
	 */
	public void lengthChanged(int length);

}
