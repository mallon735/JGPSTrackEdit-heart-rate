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
public class UnDoLevel {
	
	private ArrayList<Point> points = new ArrayList<Point>();
	
	public void add(Point point) {
		points.add(point);
	}

	public void add(ArrayList<Point> points) {
		this.points.addAll(points);
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}

}
