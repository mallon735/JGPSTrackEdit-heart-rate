/**
 * 
 */
package jgpstrackedit.data;

/**
 * @author Hubert
 *
 */
public interface TrackObserver {
	
	/**
	 * Method is called when the track has been modified.
	 * 
	 * @param track the modified track
	 */
	public void trackModified(Track track);

}
