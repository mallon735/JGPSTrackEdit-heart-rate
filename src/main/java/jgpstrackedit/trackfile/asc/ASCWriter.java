/**
 * 
 */
package jgpstrackedit.trackfile.asc;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

import java.io.PrintWriter;
import java.util.Optional;

/**
 * ASCII-Track writer
 * 
 * @author Hubert
 *
 */
public class ASCWriter {

	public void print(Track track, PrintWriter out) {
		for (Point p : track.getPoints()) {
			out.print(p.getLongitudeAsString() + "," + p.getLatitudeAsString());
			
			Optional<String> optInformation = getPointInformation(p);
			optInformation.ifPresent(info -> out.print("," + info));
			
			out.println();
		}
	}
	
	/**
	 * Get the information of the given point or an empty optional if no information 
	 * is available.
	 * 
	 * TODO: Filter information. If the information contains a comma sign,
	 * the asc reader will have problems!
	 */
	private Optional<String> getPointInformation(Point p) {
		return Optional.ofNullable(p.getInformation());
	}
}
