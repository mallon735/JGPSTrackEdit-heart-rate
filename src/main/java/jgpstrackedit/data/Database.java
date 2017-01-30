/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.data;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import jgpstrackedit.data.util.TrackTableModel;
import jgpstrackedit.international.International;

/**
 * 
 * @author Hubert
 */
public class Database extends AbstractTableModel implements TrackObserver {

	private ArrayList<Track> tracks = new ArrayList<Track>();
	private Track boundary = new Track();
	private ArrayList<DBObserver> dbObservers = new ArrayList<DBObserver>();
	private TrackTableModel trackTableModel = new TrackTableModel();

	/**
	 * @return the trackTableModel
	 */
	public TrackTableModel getTrackTableModel() {
		return trackTableModel;
	}

	public ArrayList<Track> getTracks() {
		return tracks;
	}

	public Track getTrack(int index) {
		return tracks.get(index);
	}
	
	/**
	 * Returns the index of the given track in the database
	 * @param track the track
	 * @return index of track
	 */
	public int getTrack(Track track) {
		return tracks.indexOf(track);
		
	}

	/**
	 * Returns the track which is nearest to the given point
	 * @param point point nearest to track
	 * @return the nearest track or null if no nearest track could be found
	 */
	public Track getTrack(Point point) {
		Track nearestTrack = null;
		Point nearestPoint;
		double minDistance = 10000.0;
		for (Track track:tracks) {
			if (track.isInBoundary(point)) {
				Point nP = track.nearestPoint(point);
				if (nP != null) {
					if (nearestTrack == null) {
						nearestTrack = track;
						minDistance = nP.distance(point);
					} else {
						double dist = nP.distance(point);
						if (dist < minDistance) {
							nearestTrack = track;
							minDistance = nP.distance(point);
						
						}
					}
				}
			}
		}
		return nearestTrack;		
	}
	
	public void addTrack(Track track) {
		tracks.add(track);
		track.addTrackObserver(this);
		boundary.add(track.getLeftUpperBoundary());
		boundary.add(track.getRightLowerBoundary());
		notifyDBObservers();
		fireTableDataChanged();
	}

	public void removeTrack(Track track) {
		tracks.remove(track);
		notifyDBObservers();
	}

	public void removeTrack(int index) {
		tracks.remove(index);
		notifyDBObservers();

	}

	/**
	 * Returns whether a track has been modified.
	 * @return true if any track has been modified
	 */
	public boolean isModified() {
		for (Track track : tracks) {
			if (track.isModified())
				return true;
		}
		return false;
	}

	/**
	 * Returns the number of stored tracks.
	 * 
	 * @return number of stored tracks
	 */
	public int getTrackNumber() {
		return tracks.size();
	}

	public Point getLeftUpperBoundary() {
		return boundary.getLeftUpperBoundary();
	}

	public Point getRightLowerBoundary() {
		return boundary.getRightLowerBoundary();
	}

	public void addDBObserver(DBObserver observer) {
		dbObservers.add(observer);
	}

	public void removeDBObserver(DBObserver observer) {
		dbObservers.remove(observer);
	}

	protected void notifyDBObservers() {
		for (DBObserver observer : dbObservers) {
			observer.dbModified();
		}
	}

	public void reverseTrack(int index) {
		tracks.get(index).reverse();
	}

	// TableModel Methods

	public int getRowCount() {
		return tracks.size();
	}

	public int getColumnCount() {
		return 4;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0: // Track name
			return tracks.get(rowIndex).getName();
		case 1: // color
			return tracks.get(rowIndex).getColor();
		case 2: // lengtrh
			return tracks.get(rowIndex).getLength();
		case 3: // modified
			return tracks.get(rowIndex).isModified();		
		}
		return null;

	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return International.getText("tracktbl.Track");
		case 1:
			return International.getText("tracktbl.Color");
		case 2:
			return International.getText("tracktbl.Length");
		case 3:
			return International.getText("tracktbl.Modified");
		}
		return "Unknown";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return Color.class;
		case 2:
			return Double.class;
		case 3:
			return Boolean.class;
		}
		return Object.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 0:
			return true;
		case 1:
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		switch (columnIndex) {
		case 0:
			tracks.get(rowIndex).setName((String) aValue);
			break;
		case 1:
			tracks.get(rowIndex).setColor((Color) aValue);
			break;
		}
		notifyDBObservers();
	}

	@Override
	public void trackModified(Track track) {
		// TODO Auto-generated method stub
		notifyDBObservers();
		fireTableDataChanged();
	}

}
