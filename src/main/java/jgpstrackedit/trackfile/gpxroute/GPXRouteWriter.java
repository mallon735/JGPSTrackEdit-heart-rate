/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jgpstrackedit.trackfile.gpxroute;

import java.io.PrintWriter;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

/**
 *
 * @author hlutnik
 */
public class GPXRouteWriter {

    public void print(Track track, PrintWriter out) {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<gpx "+(track.getGpxAttributes()==null?"xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"JGPSTrackEdit\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd":track.getGpxAttributes())+">");
        out.println("  <metadata>");
        out.println("    <name>"+track.getName()+"</name>");
        out.println("    <copyright author=\""+track.getCopyright()+"\" />");
        out.println("    <link href=\""+track.getLink()+"\" >");
        out.println("      <text>"+track.getLinkText()+"</text>");
        out.println("    </link>");
        out.println("    <time>"+(track.getTime()==null?"2012-01-01T00:00:01Z":track.getTime())+"</time>");
        out.println("  </metadata>");
        out.println("  <rte>");
        out.println("    <name>"+track.getName()+"</name>");
        out.println("    <link href=\""+track.getLink()+"\" />");
        for (Point p:track.getPoints()) {
            out.println("    <rtept lat=\""+p.getLatitudeAsString()+"\" "+
                                   "lon=\""+p.getLongitudeAsString()+"\">");
            out.println("      <ele>"+p.getElevationAsString()+"</ele>");
			if (p.getTime() != null)
              out.println("      <time>"+p.getTime()+"</time>");
            out.println("    </rtept>");
        }
        out.println("  </rte>");
        out.println("</gpx>");
    }
}
