package jgpstrackedit.trackfile.tcx;

import java.io.PrintWriter;
import java.util.Optional;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

/**
 * Writer for Garmin TrainingCenterDatabase (TCX).
 * 
 * @author gerdba
 * 
 */
public class TcxTrackWriter {

	public void print(Track track, PrintWriter out) {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
		out.println("<TrainingCenterDatabase xmlns=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd\">");
		out.println("  <Courses>");
		out.println("    <Course>");
		out.println("      <Name>" + getTrackName(track) + "</Name>");
		out.println("      <Track>");
				
		for (Point p : track.getPoints()) {
			out.println("        <Trackpoint>");
			
			if (p.getTime() != null) {
				out.println("          <Time>" + p.getTime() + "</Time>");
			}
			
			out.println("          <Position>");
			out.println("            <LatitudeDegrees>" + p.getLatitudeAsString() + "</LatitudeDegrees>"); 
			out.println("            <LongitudeDegrees>" + p.getLongitudeAsString() + "</LongitudeDegrees>");
			out.println("          </Position>");

			out.println("          <AltitudeMeters>" + p.getElevationAsString() + "</AltitudeMeters>");

			out.println("        </Trackpoint>");
		}
		
		out.println("      </Track>");
		out.println("    </Course>");
		out.println("  </Courses>");
		out.println("</TrainingCenterDatabase>");
	}
	
	private String getTrackName(final Track track) {
		String trackName = Optional.of(track.getName()).orElse("");
		return trackName.length() > 15 ? trackName.substring(0, 15) : trackName;
	}
}
