/**
 * 
 */
package jgpstrackedit.routing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.util.TrackUtil;
import jgpstrackedit.config.Configuration;
import jgpstrackedit.gpsies.GPSiesResultParser;

/**
 * @author Hubert
 *
 */
public class MapQuestRouting {
	
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
        System.out.println(urlString);
		URL url;
		try {
			url = new URL(urlString);
			parser.parse(url);
			points = handler.getPoints();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
