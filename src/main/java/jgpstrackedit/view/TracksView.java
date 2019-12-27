/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.view;

import jgpstrackedit.data.DBObserver;
import jgpstrackedit.data.Database;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class TracksView - Manage the views of the tracks. 
 * 
 * @author Hubert
 */
public class TracksView implements DBObserver 
{
	private final Database db;
	private Point leftUpperBoundary = null;
	private Point rightLowerBoundary = null;
	private final List<TrackView> tracksView;
	private final List<ZoomObserver> zoomObservers;

	public TracksView(Database db) {
		this.db = db;
		this.tracksView = new LinkedList<>();
		this.zoomObservers = new ArrayList<>();
		
		this.db.addDBObserver(this);
	}

	/**
	 * Get a list of track views.
	 * 
	 * Potential problem: the track can be unsubscribed from a view. 
	 * What happens when using the published list and access the view without a track?
	 * 
	 * @return list of views
	 */
	public List<TrackView> getTracksView() {
		return new LinkedList<>(tracksView);
	}

	public Point getLeftUpperBoundary() {
		return leftUpperBoundary;
	}

	public void setLeftUpperBoundary(Point leftUpperBoundary) {
		this.leftUpperBoundary = leftUpperBoundary;
	}

	public Point getRightLowerBoundary() {
		return rightLowerBoundary;
	}

	public void setRightLowerBoundary(Point rightLowerBoundary) {
		this.rightLowerBoundary = rightLowerBoundary;
	}

	protected void createTrackViews() {
		cleanUpTrackViews();
		for (Track track : db.getTracks()) {
			TrackView trackView = new TrackView(track);
			tracksView.add(trackView);
		}
	}
	
	private void cleanUpTrackViews() {
		tracksView.forEach(view -> {
			view.getTrack().removeTrackObserver(view);
		});
		tracksView.clear();
	}

	public void setView(Point leftUpperBoundary, Point rightLowerBoundary) {
		Point selectedPoint = null;
		TrackView selectedTrackView = getSelectedTrackView();
		if (selectedTrackView != null) {
			selectedPoint = selectedTrackView.getSelectedPoint();
		}
		Track selectedTrack = null;
		if (selectedTrackView != null) {
			selectedTrack = getSelectedTrackView().getTrack();
		}
		createTrackViews();
		for (TrackView trackView : tracksView) {
			trackView.setView(leftUpperBoundary, rightLowerBoundary);
		}
		if (selectedTrack != null) {
			setSelectedTrack(selectedTrack);
			if (selectedPoint != null)
				setSelectedPoint(selectedPoint);
		}
	}

	public void dbModified() {
		// notifyZoomObservers(null,null);
		notifyZoomObservers(db.getLeftUpperBoundary(),
				db.getRightLowerBoundary());
	}

	public void addZoomObserver(ZoomObserver observer) {
		zoomObservers.add(observer);
	}

	public void removeZoomObserver(ZoomObserver observer) {
		zoomObservers.remove(observer);
	}

	protected void notifyZoomObservers(Point leftUpperBoundary,
			Point rightLowerBoundary) {
		for (ZoomObserver observer : zoomObservers) {
			observer.modifiedZoom(null, null, false);
			// observer.modifiedZoom(leftUpperBoundary, rightLowerBoundary);
		}
	}

	public void setSelectedTrack(Track track) {
		for (TrackView trackView : tracksView) {
			trackView.setSelected(track);
		}
	}

	/**
	 * Returns the selected TrackView
	 * 
	 * @return selected TrackView
	 */
	public TrackView getSelectedTrackView() {
		for (TrackView trackView : tracksView) {
			if (trackView.isSelected())
				return trackView;
		}
		return null;

	}

	public void setSelectedPoint(Point point) {
		for (TrackView trackView : tracksView) {
			if (trackView.isSelected())
				trackView.setSelectedPoint(point);
		}

	}
}
