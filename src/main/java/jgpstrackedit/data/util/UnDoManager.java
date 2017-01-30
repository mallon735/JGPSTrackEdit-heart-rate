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
	private Track track;
	
	public UnDoManager(Track track) {
		this.track = track;
		
	}

	/**
	 * Adds a single point as an undo level
	 * @param point the point to add
	 */
	public void add(Point point) {
		UnDoLevel udl = new UnDoLevel();
		udl.add(point);
		undoStack.push(udl);		
	}

	/**
	 * Adds the given points as an undo level
	 * @param points ArrayList<Point> containing the points
	 */
	public void add(ArrayList<Point> points) {
		UnDoLevel udl = new UnDoLevel();
		udl.add(points);
		undoStack.push(udl);				
	}

	/**
	 * Adds the given undo level.
	 * @param undoLevel undo level to add
	 */
	public void add(UnDoLevel undoLevel) {
		undoStack.push(undoLevel);
	}
	
	/** Undo one undo level.
	 * 
	 */
	public void unDo() {
		if (!undoStack.empty()) {
			ArrayList<Point> points = undoStack.pop().getPoints();
			for (int i=points.size()-1;i>=0;i--) {
				track.remove(points.get(i));
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
