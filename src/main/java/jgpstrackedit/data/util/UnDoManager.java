/**
 * 
 */
package jgpstrackedit.data.util;

import java.util.ArrayList;
import java.util.Stack;
import jgpstrackedit.data.Track;
import jgpstrackedit.data.Point;

/** Undo manager for inserting points to track. Undo levels may be added using the add()-methods.
 * Undoing is performed by the unDo()-method.
 * @author Hubert
 *
 */
public class UnDoManager {
	
	private Stack<UnDoLevel> undoStack = new Stack<UnDoLevel>();
		
	public UnDoManager() {
	}

	/**
	 * Adds a single point as an undo level
	 * @param point the point to add
	 */
	public void add(Track track, Point point, boolean action ) {
		UnDoLevel udl = new UnDoLevel(track, action);
		udl.add(point);
		undoStack.push(udl);		
	}
	
	public void add(Track track, Point point, boolean action, int startindex ) {
		UnDoLevel udl = new UnDoLevel(track, action);
		udl.setStartindex(startindex);
		udl.add(point);
		undoStack.push(udl);		
	}

	/**
	 * Adds the given points as an undo level
	 * @param points ArrayList<Point> containing the points
	 */
	public void add(Track track, ArrayList<Point> points,boolean action) {
		UnDoLevel udl = new UnDoLevel(track, action);
		udl.add(points);
		undoStack.push(udl);				
	}
	
	public void add(Track track, ArrayList<Point> points,boolean action,int startindex) {
		UnDoLevel udl = new UnDoLevel(track, action);
		udl.setStartindex(startindex);
		udl.add(points);
		undoStack.push(udl);				
	}

	/**
	 * Adds the given undo level.
	 * @param undoLevel undo level to add
	 */
	public void add(Track track, UnDoLevel undoLevel) {
		undoStack.push(undoLevel);
	}
	
	/** Undo one undo level.
	 * 
	 */
	public void unDo() {
		if (!undoStack.empty()) {
			UnDoLevel undoLevel = undoStack.pop();
			ArrayList<Point> points = undoLevel.getPoints();
			if( undoLevel.getAction()) {
			for (int i=points.size()-1;i>=0;i--) {
					undoLevel.getTrack().remove(points.get(i));
				}
			}else {
			   undoLevel.getTrack().insert(undoLevel.getStartindex(), points);
			}
		}
		
	}
	
	/** All undo levels are committed. The undo level stack is cleared.
	 * 
	 */
	public void commit() {
		undoStack.clear();
	}

}
