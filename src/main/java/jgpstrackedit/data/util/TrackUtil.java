/**
 * 
 */
package jgpstrackedit.data.util;

import java.util.ArrayList;
import jgpstrackedit.data.Point;

/**
 * @author Hubert
 *
 */
public class TrackUtil {
	
/**
 * Removes all double points from the points list. A double points means that two consecutive
 * points have the same coordinates
 * @param points
 */
	public static void removeDoublePoints(ArrayList<Point> points) {
		Point first = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).equals(first)) {
				points.remove(i);
				i--;
			} else {
				first = points.get(i);
			}
		}
		
	}

}
