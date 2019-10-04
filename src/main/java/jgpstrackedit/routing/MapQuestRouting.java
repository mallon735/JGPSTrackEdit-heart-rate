/**
 * 
 */
package jgpstrackedit.routing;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.util.TrackUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;

/**
 * @author Hubert
 *
 */
public class MapQuestRouting {

	private static final Logger logger = LoggerFactory.getLogger(MapQuestRouting.class);

	protected ArrayList<Point> loadRouteFromMapQuest(Point fromPoint, Point toPoint) {
		ArrayList<Point> points = null;
		MapQuestRoutingHandlerImpl handler = new MapQuestRoutingHandlerImpl();
		MapQuestRoutingParser parser = new MapQuestRoutingParser(handler, null);
		/* Old version without configuration
		String urlString = "http://open.mapquestapi.com/directions/v0/route?outFormat=xml&routeType=bicycle&timeType=1&enhancedNarrative=false&shapeFormat=raw&generalize=10&unit=k"+
	               "&from="+fromPoint.getLatitudeAsString()+","+fromPoint.getLongitudeAsString()+
	               "&to="+toPoint.getLatitudeAsString()+","+toPoint.getLongitudeAsString();
	               */
		String urlString = "http://open.mapquestapi.com/directions/v2/route?key=Fmjtd%7Cluubn96ynu%2C2s%3Do5-907guw&outFormat=xml&routeType="
	                   +Configuration.getProperty("ROUTINGTYPE")
	                   +"&timeType=1&enhancedNarrative=false&shapeFormat=raw&generalize="
	                   +Configuration.getProperty("ROUTINGPOINTDISTANCE")
	                   +"&unit=k"
	                   +(Configuration.getBooleanProperty("ROUTINGAVOIDLIMITEDACCESS")?"&avoids=Limited Access":"")
	                   +(Configuration.getBooleanProperty("ROUTINGAVOIDTOLLROAD")?"&avoids=Toll road":"")
		               +"&from="+fromPoint.getLatitudeAsString()+","+fromPoint.getLongitudeAsString()
		               +"&to="+toPoint.getLatitudeAsString()+","+toPoint.getLongitudeAsString();
        logger.info(urlString);
		URL url;
		try {
			url = new URL(urlString);
			parser.parse(url);
			points = handler.getPoints();
		} catch (Exception e) {
			logger.error("Error loading route!", e);
		}
		return points;

	}

	public ArrayList<Point> loadRoute(Point fromPoint, Point toPoint) {
		ArrayList<Point> points = loadRouteFromMapQuest(fromPoint,toPoint);
		if (points != null) {
			points.remove(0);  // delete first point since it is in near approximation of fromPoint
			TrackUtil.removeDoublePoints(points);
		}
		return points;
	}

}
