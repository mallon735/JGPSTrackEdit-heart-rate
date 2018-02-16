package jgpstrackedit.map.elevation;

import jgpstrackedit.data.Point;

/**
 * Wrapper around a point. Save the position index in track for calculation the progress bar. 
 *  
 * @author gerdba
 *
 */
public class PointWrapper {
	private final Point point;
	private final int index;

	public PointWrapper(Point point, int index) {
		this.point = point;
		this.index = index;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public int getIndex() {
		return this.index;
	}
}
