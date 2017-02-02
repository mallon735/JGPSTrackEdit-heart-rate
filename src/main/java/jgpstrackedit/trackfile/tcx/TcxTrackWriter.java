/**
 * 
 */
package jgpstrackedit.trackfile.tcx;

import java.io.PrintWriter;

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
		out.println("\t<Courses>");
		out.println("\t\t<Course>");
		out.println("\t\t\t<Name>" + track.getName() + "</Name>");
		out.println("\t\t\t<Track>");
				
		for (Point p : track.getPoints()) {
			out.println("\t\t\t\t<Trackpoint>");
			
			if (p.getTime() != null) {
				out.println("\t\t\t\t\t<Time>" + p.getTime() + "</Time>");
			}
			
			out.println("\t\t\t\t\t<Position>");
			out.println("\t\t\t\t\t\t<LatitudeDegrees>" + p.getLatitudeAsString() + "</LatitudeDegrees>"); 
			out.println("\t\t\t\t\t\t<LongitudeDegrees>" + p.getLongitudeAsString() + "</LongitudeDegrees>");
			out.println("\t\t\t\t\t</Position>");

			out.println("\t\t\t\t\t<AltitudeMeters>" + p.getElevationAsString() + "</AltitudeMeters>");

			out.println("\t\t\t\t</Trackpoint>");
		}
		
		out.println("\t\t\t</Track>");
		out.println("\t\t</Course>");
		out.println("\t</Courses>");
		out.println("</TrainingCenterDatabase>");
	}
}
