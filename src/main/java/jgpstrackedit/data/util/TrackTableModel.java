/**
 * 
 */
package jgpstrackedit.data.util;

import javax.swing.table.AbstractTableModel;

import jgpstrackedit.data.Track;
import jgpstrackedit.data.TrackObserver;
import jgpstrackedit.international.International;

/**
 * Table model for points view, which shows a table of all track points.
 * @author Hubert
 * 
 */
public class TrackTableModel extends AbstractTableModel 
                             implements TrackObserver 
{
	private Track currentTrack = null;

	/**
	 * @return the currentTrack
	 */
	public Track getSelectedTrack() {
		return currentTrack;
	}

	/**
	 * @param currentTrack
	 *            the currentTrack to set
	 */
	public void setSelectedTrack(Track currentTrack) {
		if (this.currentTrack != null) {
			this.currentTrack.removeTrackObserver(this);
		}
		
		this.currentTrack = currentTrack;
		if (this.currentTrack != null) {
			this.currentTrack.addTrackObserver(this);
		}
		
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		if (currentTrack != null)
			return currentTrack.getNumberPoints();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if(this.currentTrack == null) {
			return null;
		}
		
		switch (column) {
		case 0:
			return row;
		case 1:
			return currentTrack.getPoint(row).getLatitudeAsString();
		case 2:
			return currentTrack.getPoint(row).getLongitudeAsString();
		case 3:
			return currentTrack.getPoint(row).getElevationAsString();
		case 4:
			return currentTrack.getPoint(row).getTime();
		default:
			return null;
		}
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass
	 */
	@Override
	public Class<?> getColumnClass(int arg0) {
		switch (arg0) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		default:
			return Object.class;
		}
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int arg0) {
		switch (arg0) {
		case 0:
			return International.getText("pointtbl.Index");
		case 1:
			return International.getText("pointtbl.Latitude");
		case 2:
			return International.getText("pointtbl.Longitude");
		case 3:
			return International.getText("pointtbl.Elevation");
		case 4:
			return International.getText("pointtbl.Timestamp");
		default:
			return "Unknown";
		}
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return false;
		case 1:
			return true;
		case 2:
			return true;
		case 3:
			return true;
		case 4:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(this.currentTrack == null) {
			return;
		}
		
		switch (columnIndex) {
		case 0:
			break;
		case 1:
			currentTrack.getPoint(rowIndex).setLatitude((String)aValue);
			break;
		case 2:
			currentTrack.getPoint(rowIndex).setLongitude((String)aValue);
			break;
		case 3:
			currentTrack.getPoint(rowIndex).setElevation((String)aValue);
			break;
		case 4:
			currentTrack.getPoint(rowIndex).setTime((String)aValue);
			break;
		default:
			break;
		}
	}

	@Override
	public void trackModified(Track track) {
		this.fireTableDataChanged();
	}
}
