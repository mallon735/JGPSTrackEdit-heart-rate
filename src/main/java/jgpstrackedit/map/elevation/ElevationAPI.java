/**
 * 
 */
package jgpstrackedit.map.elevation;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.util.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;

/**
 * Facade of the google elevation api, see
 * http://code.google.com/intl/de/apis/maps/documentation/elevation/
 * 
 * @author Hubert
 * 
 */
public class ElevationAPI 
{
	private static Logger logger = LoggerFactory.getLogger(ElevationAPI.class);
	private StringBuilder elevationURL;
	private boolean firstPoint;
	private HashMap<String,Point> points;

	/**
	 * Updates the elevation of the given track using google elevation api
	 * 
	 * @param track
	 *            track to be updated
	 * @throws ElevationException
	 *             indicates an error using google elevation api, see
	 *             http://code.google.com/intl/de/apis/maps/documentation/elevation/ for
	 *             details.
	 */
	public void updateElevation(Track track) throws ElevationException {
        initElevationRequest();
        int pointCounter = 0;
        for (int i=0; i<track.getNumberPoints();i++) {
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
	 * Initializes the elevation request. Must be called prior to addPoint()
	 * call. For internal use only.
	 * 
	 */
	protected void initElevationRequest() {
		elevationURL = new StringBuilder(
				"http://maps.googleapis.com/maps/api/elevation/xml?locations=");
		firstPoint = true;
		points = new HashMap<String,Point>();
	}

	/**
	 * Adds the given point to the elevation request. initElevationRequest must
	 * be called first. At most 50 times a call to addPoint must be performed.
	 * The next action is a call of method issueElevationRequest(). For internal
	 * use only.
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
		String location = Parser.trim_0(point.getLatitudeAsString())+","+Parser.trim_0(point.getLongitudeAsString());
		elevationURL.append(location);
		points.put(location,point);
	}

	/**
	 * Sends out the elevation request to the web. For internal use only.
	 * @throws ElevationException 
	 *             indicates an error using google elevation api, see
	 *             http://code.google.com/intl/de/apis/maps/documentation/elevation/ for
	 *             details.
	 * 
	 */
	protected void issueElevationRequest() throws ElevationException {
		elevationURL.append("&sensor=false");
		logger.info(elevationURL.toString());
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
			for (ElevationResult elevationResult:elevationResponse.getResults()) {
				logger.info(elevationResult.toString());
				Point point = points.get(elevationResult.getLocation());
				if (point == null) {
					// No match, what went wrong?
					logger.info("ElevationAPI: no match for "+elevationResult.getLocation());
				} else {
				    point.setElevation(elevationResult.getElevation());
				}
			}
		} catch (Exception e) {
			logger.error("Cannot perform elevation request!", e);
		}

	}
}
