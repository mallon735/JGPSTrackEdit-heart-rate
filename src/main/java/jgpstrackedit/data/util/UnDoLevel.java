/**
 * 
 */
package jgpstrackedit.data.util;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

import java.util.ArrayList;
/**
 * @author Hubert
 *
 */
public class UnDoLevel {
	
	private ArrayList<Point> points = new ArrayList<Point>();
	private Track track;
	private boolean actionAdd;
	private int startindex;
	
	public UnDoLevel(Track track,boolean action) {
		this.track = track;	
		actionAdd = action;
	}
	
	public void setStartindex(int index){
		startindex = index;
	}
	
	public void add(Point point) {
		points.add(point);
	}

	public void add(ArrayList<Point> points) {
		this.points.addAll(points);
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}

	public Track getTrack(){
		return track;
	}
	public boolean getAction(){
		return actionAdd;
	}
	
	public int getStartindex() {
		return startindex;
	}
}
