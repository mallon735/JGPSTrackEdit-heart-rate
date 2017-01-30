/**
 * 
 */
package jgpstrackedit.trackfile.gpxtrack;

import java.io.PrintWriter;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

/**
 * @author Hubert
 * 
 */
public class GPXTrackWriter {

	public void print(Track track, PrintWriter out) {
		// TODO Auto-generated method stub
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		String attr = "";
		if (track.getGpxAttributes() != null)
			attr = track.getGpxAttributes();
		out.println("<gpx creator=\"JGPSTrackEdit\" version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
		out.println("  <metadata>");
		out.println("    <name>" + track.getName() + "</name>");
		out.println("    <copyright author=\"" + track.getCopyright() + "\" />");
		out.println("    <link href=\"" + track.getLink() + "\" >");
		out.println("      <text>" + track.getLinkText() + "</text>");
		out.println("    </link>");
		out.println("    <time>" + (track.getTime()==null?"2012-01-01T00:00:01Z":track.getTime()) + "</time>");
		out.println("  </metadata>");
		out.println("  <trk>");
		out.println("    <name>" + track.getName() + "</name>");
		out.println("    <link href=\"" + track.getLink() + "\" />");
		out.println("    <trkseg>");
		for (Point p : track.getPoints()) {
			out.println("      <trkpt lat=\"" + p.getLatitudeAsString() + "\" "
					+ "lon=\"" + p.getLongitudeAsString() + "\">");
			out.println("        <ele>" + p.getElevationAsString() + "</ele>");
			if (p.getTime() != null)
			  out.println("        <time>" + p.getTime() + "</time>");
			out.println("      </trkpt>");
		}
		out.println("    </trkseg>");
		out.println("  </trk>");
		out.println("</gpx>");

	}

}
