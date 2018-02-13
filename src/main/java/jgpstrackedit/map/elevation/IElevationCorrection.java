package jgpstrackedit.map.elevation;

import jgpstrackedit.data.Track;

/**
 * Interface for correction the elevation of a track.
 * 
 * @author gerdba
 *
 */
public interface IElevationCorrection {
	/**
	 * Updates the elevation of a given track.
	 * 
	 * @param track
	 *            track to be updated
	 * @throws ElevationException
	 *             indicates an error
	 */
	void updateElevation(Track track, IProgressDetector progressDetector) throws ElevationException;
}
