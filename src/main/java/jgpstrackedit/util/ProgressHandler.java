/**
 * 
 */
package jgpstrackedit.util;

/**
 * @author Hubert
 *
 */
public interface ProgressHandler {
	
	/**
	 * This method is called to indicate a progress. The method must return true to continue the progress.
	 * @param percentage the actual percentage of completion
	 * @return return false if the progress should be cancelled
	 */
	public boolean stepDone(int percentage);

}
