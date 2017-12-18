package jgpstrackedit.map.elevation;

import jgpstrackedit.map.elevation.google.GoogleElevationCorrection;
import jgpstrackedit.map.elevation.mapquest.MapQuestElevationCorrection;

/**
 * Factory for creating an implementation of the {@link IElevationCorrection}
 * interface.
 * 
 * @author gerdba
 * 
 */
public class ElevationCorrectionFactory {
	private static final String ELEVATION_CORRECTION_API_GOOGLE = "google";
	private static final String ELEVATION_CORRECTION_API_MAPQUEST = "mapquest";
	
	public static IElevationCorrection create(String name) {
		if (name.equals(ELEVATION_CORRECTION_API_GOOGLE)) {
			return new GoogleElevationCorrection();
		} else if (name.equals(ELEVATION_CORRECTION_API_MAPQUEST)) {
			return new MapQuestElevationCorrection();
		}

		throw new NullPointerException(String.format("Cannot create elevation correction for name \"%s\"!", name));
	}
}
