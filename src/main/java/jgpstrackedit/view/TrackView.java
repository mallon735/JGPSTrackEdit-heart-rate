/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.view;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.data.TrackObserver;

/**
 * 
 * @author Hubert
 */
public class TrackView implements TrackObserver {

	private LinkedList<PointView> points = new LinkedList<PointView>();
	private Track track;
	private boolean selected = false;
	private Point selectedPoint;

	public TrackView(Track track) {
		this.track = track;
		this.track.addTrackObserver(this);
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(Track selectedTrack) {
		selected = selectedTrack == track;
	}

	public List<PointView> getPoints() {
		return points;
	}

	public void setPoints(LinkedList<PointView> points) {
		this.points = points;
	}

	public void setView(Point leftUpperBoundary, Point rightLowerBoundary) {
		points.clear();
		Point previous = null;
		boolean view = false;
		for (Point point : track.getPoints()) {
			if (point.isWithin(leftUpperBoundary, rightLowerBoundary)) {
				if (!view && previous != null) {
					PointView outViewPoint = new PointView(previous, track);
					outViewPoint.setOutView(true);
					points.add(outViewPoint);
					view = true;
				}
				PointView pointView = new PointView(point, track);
				points.add(pointView);
			} else {
				if (view) {
					PointView outViewPoint = new PointView(point, track);
					outViewPoint.setOutView(true);
					points.add(outViewPoint);
					view = false;
				}
			}
			previous = point;
		}
	}

	public String getName() {
		return track.getName();
	}

	public Color getColor() {
		return track.getColor();
	}

	public Point getSelectedPoint() {
		return selectedPoint;
	}

	public int getSelectedPointIndex() {
		return getTrack().indexOf(getSelectedPoint());
	}
	public void setSelectedPoint(Point selectedPoint) {
		this.selectedPoint = selectedPoint;
	}

	/**
	 * Deletes selected point from selected track.
	 */
	public void deleteSelectedPoint() {
		track.remove(selectedPoint);
	}

	/**
	 * If the given x,y-Screen-Coordinate matches to a stored PointView this
	 * PointView is returned, else null.
	 * 
	 * @param x
	 *            X-Coordinate (screen)
	 * @param y
	 *            Y-Coordinate (screen)
	 * @return PointView if Coordinate matches, else null
	 */
	public PointView getPointAt(int x, int y) {
		for (PointView pointView : points) {
			if (pointView.contains(x, y))
				return pointView;
		}
		return null;

	}

	/**
	 * If the given x,y-Screen-Coordinate matches to a stored PointView the
	 * Index of the PointView is returned, else -1.
	 * 
	 * @param x
	 *            X-Coordinate (screen)
	 * @param y
	 *            Y-Coordinate (screen)
	 * @return Index if Coordinate matches, else -1
	 */
	public int getPointIndexAt(int x, int y) {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).contains(x, y))
				return i;
		}
		return -1;

	}

	@Override
	public void trackModified(Track track) {
		setView(Transform.getUpperLeftBoundary(),
				Transform.getLowerRightBoundary());

	}

	/**
	 * @return the track
	 */
	public Track getTrack() {
		return track;
	}

	/**
	 * If the first point (starting point of track) is within trackview, the
	 * corresponding PointView object is returned, else null is returned.
	 * 
	 * @return PointView of starting point or null
	 */
	public PointView getFirstPoint() {
		if (points.size() > 0
				&& points.getFirst().getPoint() == track.getFirstPoint())
			return points.getFirst();
		else
			return null;

	}

	/**
	 * If the last point (ending point of track) is within trackview, the
	 * corresponding PointView object is returned, else null is returned.
	 * 
	 * @return PointView of ending point or null
	 */
	public PointView getLastPoint() {
		if (points.size() > 0
				&& points.getLast().getPoint() == track.getLastPoint())
			return points.getLast();
		else
			return null;

	}

    /**
     * Inserts adjacent points.
     */
	public void insertAdjacentPoints() {
		// TODO Auto-generated method stub
		track.insertAdjacentPoints(selectedPoint);
		
	}

	public void setSelectedPointPosition(double mapLongitude,double mapLatitude) {
		// TODO Auto-generated method stub
		track.setPointPosition(selectedPoint,mapLongitude,mapLatitude);
	}

}
