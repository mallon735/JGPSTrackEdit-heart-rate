/**
 * 
 */
package jgpstrackedit.map.elevation.google;

import java.net.URL;
import java.util.ArrayList;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IElevationCorrection;
import jgpstrackedit.map.elevation.IProgressDetector;
import jgpstrackedit.util.Parser;

/**
 * Facade of the google elevation api, see
 * http://code.google.com/intl/de/apis/maps/documentation/elevation/
 * 
 * @author Hubert
 * 
 */
public class GoogleElevationCorrection implements IElevationCorrection {
	private StringBuilder elevationURL;
	private boolean firstPoint;
	private ArrayList<Point> points;

	/**
	 * Updates the elevation of the given track using google elevation api
	 * 
	 * @param track
	 *            track to be updated
	 * @throws ElevationException
	 *             indicates an error using google elevation api, see
	 *             http://code.google.com/intl/de/apis/maps/documentation/elevation/
	 *             for details.
	 */
	@Override
	public void updateElevation(Track track, IProgressDetector progressDetector) throws ElevationException {
		initElevationRequest();
		int pointCounter = 0;
		for (int i = 0; i < track.getNumberPoints(); i++) {
			addPoint(track.getPoint(i));
			pointCounter++;
			if (pointCounter == 50) {
				pointCounter = 0;
				issueElevationRequest();
				initElevationRequest();
			}
		}
		if (pointCounter != 0) {
			issueElevationRequest();
		}
		track.hasBeenModified();
	}

	/**
	 * Initializes the elevation request. Must be called prior to addPoint() call.
	 * For internal use only.
	 * 
	 */
	protected void initElevationRequest() {
		elevationURL = new StringBuilder("http://maps.googleapis.com/maps/api/elevation/xml?locations=");
		firstPoint = true;
		points = new ArrayList<Point>(50);
	}

	/**
	 * Adds the given point to the elevation request. initElevationRequest must be
	 * called first. At most 50 times a call to addPoint must be performed. The next
	 * action is a call of method issueElevationRequest(). For internal use only.
	 * 
	 * @param point
	 *            the point to be added
	 */
	protected void addPoint(Point point) {
		if (firstPoint) {
			firstPoint = false;
		} else {
			elevationURL.append('|');
		}
		String location = Parser.trim_0(point.getLatitudeAsString()) + ","
				+ Parser.trim_0(point.getLongitudeAsString());
		elevationURL.append(location);
		points.add(point);
	}

	/**
	 * Sends out the elevation request to the web. For internal use only.
	 * 
	 * @throws ElevationException
	 *             indicates an error using google elevation api, see
	 *             http://code.google.com/intl/de/apis/maps/documentation/elevation/
	 *             for details.
	 * 
	 */
	protected void issueElevationRequest() throws ElevationException {
		elevationURL.append("&sensor=false");
		System.out.println(elevationURL.toString());
		URL url;
		try {
			url = new URL(elevationURL.toString());
			ElevationHandlerImpl handler = new ElevationHandlerImpl();
			ElevationParser parser = new ElevationParser(handler, null);
			parser.parse(url);
			ElevationResponse elevationResponse = handler.getElevationResponse();
			if (!elevationResponse.getState().equals("OK")) {
				throw new ElevationException(elevationResponse.getState());
			}

			if (elevationResponse.getResults().size() != points.size()) {
				throw new ElevationException("Different number of input and output points! Cannot correct elevation!");
			}

			for (int i = 0; i < elevationResponse.getResults().size(); i++) {
				ElevationResult elevationResult = elevationResponse.getResults().get(i);
				Point point = points.get(i);
				System.out.println(elevationResult);
				point.setElevation(elevationResult.getElevation());
			}
		} catch (ElevationException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
