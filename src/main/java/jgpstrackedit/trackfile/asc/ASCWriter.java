/**
 * 
 */
package jgpstrackedit.trackfile.asc;

import java.io.PrintWriter;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

/**
 * @author Hubert
 *
 */
public class ASCWriter {

	public void print(Track track, PrintWriter out) {
		// TODO Auto-generated method stub
		for (Point p : track.getPoints()) {
			out.print(p.getLongitudeAsString()+ ","+ p.getLatitudeAsString() 
					  + "," + p.getInformation());
		}
		
	}

}
