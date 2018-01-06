/**
 * 
 */
package jgpstrackedit.trackfile.gpxtrack;

import java.io.PrintWriter;
import java.util.Optional;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

/**
 * Serialize a track as a garmin gpx track file.
 * 
 * @author Hubert
 * 
 */
public class GPXTrackWriter {
	
	public void print(Track track, PrintWriter out) {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<gpx creator=\"JGPSTrackEdit\" version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
		
		writeMetaData(track, out);
		
		out.println("  <trk>");
		out.println("    <name>" + track.getName() + "</name>");
		writeLink(track, out);
		
		out.println("    <trkseg>");
		for (Point p : track.getPoints()) {
			out.println("      <trkpt lat=\"" + p.getLatitudeAsString() + "\" "
					+ "lon=\"" + p.getLongitudeAsString() + "\">");
			out.println("        <ele>" + p.getElevationAsString() + "</ele>");
			
			if (p.getTime() != null) {
			  out.println("        <time>" + p.getTime() + "</time>");
			}
			
			out.println("      </trkpt>");
		}
		out.println("    </trkseg>");
		
		out.println("  </trk>");
		out.println("</gpx>");

	}

	private void writeLink(Track track, PrintWriter out) {
		if(track.getLink() != null && track.getLink().trim().length() > 0) {
			out.println("    <link href=\"" + track.getLink().trim() + "\" />");
		}
	}

	private void writeMetaData(Track track, PrintWriter out) {
		if(track.getLink() != null || track.getCopyright() != null) {
			out.println("  <metadata>");
			out.println("    <name>" + track.getName() + "</name>");
			
			if(track.getCopyright() != null) {
				out.println("    <copyright author=\"" + track.getCopyright() + "\" />");
			}
			
			if(track.getLink() != null) {
				out.println("    <link href=\"" + track.getLink() + "\" >");
				out.println("      <text>" 
						+ Optional.ofNullable(track.getLinkText()).orElse(track.getLink()) 
						+ "</text>");
				out.println("    </link>");
			}
			
			out.println("    <time>" + (track.getTime()==null?"2012-01-01T00:00:01Z":track.getTime()) + "</time>");
			out.println("  </metadata>");
		}
	}
}
