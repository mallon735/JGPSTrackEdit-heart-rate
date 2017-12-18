/**
 * 
 */
package jgpstrackedit.map.elevation.mapquest;

import java.util.List;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IElevationCorrection;

/**
 * Implementation of the interface {@link IElevationCorrection}.
 * Use the mapquest api to calculate the elevation.
 * 
 * See: https://developer.mapquest.com/documentation/open/elevation-api/elevation-profile/get/
 * 
 * @author gerdba
 * 
 */
public class MapQuestElevationCorrection implements IElevationCorrection 
{

	/**
	 * Updates the elevation of the given track using mapquest elevation api
	 * 
	 * @param track
	 *            track to be updated
	 * @throws ElevationException
	 *             indicates an error
	 */
	@Override
	public void updateElevation(Track track) throws ElevationException {
	}

	/**
	 * Compress points to a string. Use a given precision of 5.
	 * 
	 * See: https://developer.mapquest.com/documentation/common/encode-decode/
	 * compress 
	 */
	String compress(List<Point> points) {
		return compress(points, 5);
	}

	/**
	 * Compress points to a string. Use a given precision for each point.
	 * 
	 * See: https://developer.mapquest.com/documentation/common/encode-decode/
	 * compress 
	 */
	private String compress(List<Point> points, int pointPrecision) {
		long oldLat = 0;
		long oldLng = 0;
		int len = points.size();
		int index = 0;
		StringBuilder encoded = new StringBuilder();

		double precision = Math.pow(10, pointPrecision);
		while (index < len) {
			// Round to N decimal places
			long lat = Math.round(points.get(index).getLatitude() * precision);
			long lng = Math.round(points.get(index).getLongitude() * precision);
			index += 1;

			// Encode the differences between the points
			encoded.append(encodeNumber(lat - oldLat));
			encoded.append(encodeNumber(lng - oldLng));

			oldLat = lat;
			oldLng = lng;
		}
		return encoded.toString();
	}

	private String encodeNumber(long number) {
		long num = number << 1;
		if (num < 0) {
			num = ~(num);
		}
		StringBuilder encoded = new StringBuilder();
		while (num >= 0x20) {
			encoded.append((char) ((0x20 | (num & 0x1f)) + 63));
			num >>= 5;
		}
		encoded.append((char) (num + 63));
		return encoded.toString();
	}
}
