package jgpstrackedit.map.elevation.mapquest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jgpstrackedit.data.Track;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IElevationCorrection;
import jgpstrackedit.map.elevation.IProgressDetector;
import jgpstrackedit.map.elevation.PointWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
	private static final Logger logger = LoggerFactory.getLogger(MapQuestElevationCorrection.class);
	private static final String BASE_URL = "http://open.mapquestapi.com/elevation/v1/profile?key=Fmjtd%7Cluubn96ynu%2C2s%3Do5-907guw&shapeFormat=cmp&latLngCollection=";
	private static final int NUMBER_OF_POINTS_PER_REQUEST = 120;
	private static final int NO_DATA_ERROR = 601;
	private static final int PARTIAL_SUCCESS = 602;
	private static final int NO_HEIGHT_DATA = -32768;

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
			updateElevationThrow(track, progressDetector);
		} catch(ElevationException ee) {
			throw ee;
		} catch (Exception e) {
			throw new ElevationException(e.getMessage());
		}
	}
	
	private void updateElevationThrow(Track track, IProgressDetector progressDetector) throws Exception {
		List<List<PointWrapper>> splittedList = splitUpTrack(track);
		ObjectMapper mapper = new ObjectMapper();
		
		for(List<PointWrapper> list : splittedList) {
			try(InputStream istream = openUrlStream(this.getRequest(list))) {
				if(progressDetector.isCanceled()) {
					return;
				}
				
				ElevationResponse elevationResponse = mapper.readValue(istream, ElevationResponse.class);
				updatePoints(elevationResponse, list, track);
				setProgress(track, progressDetector, list);
			}
		}
	}

	private void setProgress(Track track, IProgressDetector progressDetector, List<PointWrapper> list) {
		if(list.size() > 0) {
			int index = list.get(list.size()-1).getIndex();
			progressDetector.setProgress(Math.round((100F) * ((float)index/(float)track.getNumberPoints())));
		}
	}
	
	InputStream openUrlStream(String request) throws IOException {
		URL url = new URL(request);
		return url.openStream();
	}
	
	/**
	 * Update points use the {@link ElevationResponse}. There are two errors to ignore. Status 601 and status 602.
	 * 
	 * @param elevationResponse mapquest elevation profile response 
	 * @param list list of points
	 * @param track track to correct the elevation
	 * @throws ElevationException elevation exception
	 */
	private void updatePoints(ElevationResponse elevationResponse, List<PointWrapper> list, Track track) throws ElevationException {
		int status = getStatus(elevationResponse);

		if(status == NO_DATA_ERROR) {
			return;
		}

		if(status > 0 && status != PARTIAL_SUCCESS) {
			throw new ElevationException(
					String.format("Mapquest response: [%d] %s", status, getErrorInfo(elevationResponse)));
		}
		
		if(elevationResponse.getElevationProfile() != null && elevationResponse.getElevationProfile().size() == list.size()) {
			int idx = 0;
			for(ElevationProfile elevationProfile : elevationResponse.getElevationProfile()) {
				if(hasHeight(elevationProfile)) {
					list.get(idx).getPoint().setElevation(elevationProfile.getHeight());
				}
				idx += 1;
			}
			track.hasBeenModified();
		} else {
			logger.error("The elevation correction response is null or has a wrong size!");
		}
	}
	
	private boolean hasHeight(ElevationProfile elevationProfile) {
		if(elevationProfile.getHeight() == null) {
			return false;
		}

		return elevationProfile.getHeight() != NO_HEIGHT_DATA;
	}
	
	private int getStatus(ElevationResponse elevationResponse) {
		if(elevationResponse.getInfo() != null && elevationResponse.getInfo().getStatuscode() != null) {
			return elevationResponse.getInfo().getStatuscode();
		}
		return 0;
	}
	
	private String getErrorInfo(ElevationResponse elevationResponse) {
		if(elevationResponse.getInfo() != null 
				&& elevationResponse.getInfo().getMessages() != null 
				&& elevationResponse.getInfo().getMessages().size() > 0) {
			return elevationResponse.getInfo().getMessages()
					.stream()
					.map(Object::toString)
					.collect(Collectors.joining(","));
		}
		return "";
	}

	private List<List<PointWrapper>> splitUpTrack(Track track) {
		List<List<PointWrapper>> splittedList = new LinkedList<>();
		List<PointWrapper> pointList = new ArrayList<>(NUMBER_OF_POINTS_PER_REQUEST + 1);
		
		for (int i = 0; i < track.getNumberPoints(); i++) {
			pointList.add(new PointWrapper(track.getPoint(i), i));
			if((i > 0) && (i % NUMBER_OF_POINTS_PER_REQUEST == 0)) {
				splittedList.add(pointList);
				pointList = new ArrayList<>(NUMBER_OF_POINTS_PER_REQUEST + 1);
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
	private String getRequest(List<PointWrapper> points) {
		final StringBuilder urlBuilder = new StringBuilder(BASE_URL);
		urlBuilder.append(compress(points));
		return urlBuilder.toString();
	}

	/**
	 * Compress points to a string. Use a given precision of 5.
	 * 
	 * See: https://developer.mapquest.com/documentation/common/encode-decode/
	 * compress 
	 */
	String compress(List<PointWrapper> points) {
		return compress(points, 5);
	}

	/**
	 * Compress points to a string. Use a given precision for each point.
	 * 
	 * See: https://developer.mapquest.com/documentation/common/encode-decode/
	 * compress 
	 */
	private String compress(List<PointWrapper> points, int pointPrecision) {
		long oldLat = 0;
		long oldLng = 0;
		int len = points.size();
		int index = 0;
		StringBuilder encoded = new StringBuilder();

		double precision = Math.pow(10, pointPrecision);
		while (index < len) {
			// Round to N decimal places
			long lat = Math.round(points.get(index).getPoint().getLatitude() * precision);
			long lng = Math.round(points.get(index).getPoint().getLongitude() * precision);
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
