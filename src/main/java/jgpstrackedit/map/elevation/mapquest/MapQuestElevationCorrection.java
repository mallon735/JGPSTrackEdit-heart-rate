/**
 * 
 */
package jgpstrackedit.map.elevation.mapquest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IElevationCorrection;
import jgpstrackedit.map.elevation.IProgressDetector;

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
	private static final String BASE_URL = "http://open.mapquestapi.com/elevation/v1/profile?key=Fmjtd%7Cluubn96ynu%2C2s%3Do5-907guw&shapeFormat=cmp&outShapeFormat=none&latLngCollection=";
	private static final int NUMBER_OF_POINTS_PER_REQUEST = 240;

	/**
	 * Updates the elevation of the given track using mapquest elevation api.
	 * 
	 * @param track
	 *            track to be updated
	 * @throws ElevationException
	 *             indicates an error
	 */
	@Override
	public void updateElevation(Track track, IProgressDetector progressDetector) throws ElevationException {
		try {
			updateElevationThrow(track);
		} catch(ElevationException ee) {
			throw ee;
		} catch (Exception e) {
			throw new ElevationException(e.getMessage());
		}
	}
	
	void updateElevationThrow(Track track) throws Exception {
		List<List<Point>> splittedList = splitUpTrack(track);
		ObjectMapper mapper = new ObjectMapper();
		
		for(List<Point> list : splittedList) {
			try(InputStream istream = openUrlStream(this.getRequest(list))) {
				ElevationResponse elevationResponse = mapper.readValue(istream, ElevationResponse.class);
				updatePoints(elevationResponse, list, track);
			}
		}
	}
	
	InputStream openUrlStream(String request) throws IOException {
		URL url = new URL(request);
		return url.openStream();
	}
	
	void updatePoints(ElevationResponse elevationResponse, List<Point> list, Track track) throws ElevationException {
		if(elevationResponse.getInfo() != null && elevationResponse.getInfo().getStatuscode() != null && elevationResponse.getInfo().getStatuscode() > 0) {
			throw new ElevationException(String.format("HTTP response is %d", elevationResponse.getInfo().getStatuscode())); 
		}
		
		if(elevationResponse.getElevationProfile() != null && elevationResponse.getElevationProfile().size() == list.size()) {
			int idx = 0;
			for(ElevationProfile elevationProfile : elevationResponse.getElevationProfile()) {
				list.get(idx).setElevation(Optional.ofNullable(elevationProfile.getHeight()).orElse(0));
				idx += 1;
			}
			track.hasBeenModified();
		} else {
			System.err.println("The elevation correction response is null or has a wrong size!");
		}
	}

	List<List<Point>> splitUpTrack(Track track) {
		
		List<List<Point>> splittedList = new LinkedList<List<Point>>();
		List<Point> pointList = new ArrayList<Point>(NUMBER_OF_POINTS_PER_REQUEST + 1);
		
		for (int i = 0; i < track.getNumberPoints(); i++) {
			pointList.add(track.getPoint(i));
			if((i > 0) && (i % NUMBER_OF_POINTS_PER_REQUEST == 0)) {
				splittedList.add(pointList);
				pointList = new ArrayList<Point>(NUMBER_OF_POINTS_PER_REQUEST + 1);
			}
		}
		
		splittedList.add(pointList);
		return splittedList;
	}
	
	/**
	 * Get the request url.
	 *  
	 * @param points List of points
	 * @return Request URL
	 */
	String getRequest(List<Point> points) {
		StringBuilder urlBuilder = new StringBuilder(BASE_URL);
		urlBuilder.append(compress(points));
		return urlBuilder.toString();
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
