/**
 * 
 */
package jgpstrackedit.gpsies;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * @author Hubert
 *
 */
public class GPSiesResult extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private ArrayList<GPSiesTrackDescription> tracks = new ArrayList<GPSiesTrackDescription>();

	public void add(GPSiesTrackDescription track) {
		tracks.add(track);
	}
	
	public GPSiesTrackDescription get(int index) {
		return tracks.get(index);
	}

	//
	// TableModel methods
	//
		
	@Override
	public int getColumnCount() {
		return 7;
	}

	/**
	 * Is a cell editable?
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1;
	}

	/**
	 * Column type information
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class; // Name, Title
		case 1:
			return String.class; // Description
		case 2:
			return String.class; // Track length as string
		case 3:
			return Double.class;
		case 4:
			return Double.class;
		case 5:
			return Double.class;
		case 6:
			return Double.class;
		}
		return String.class;
	}

	@Override
	public int getRowCount() {
		return tracks.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		GPSiesTrackDescription track = tracks.get(row);
		switch (column) {
		case 0:
			return track.getTitle();
		case 1:
            return track.getDescription();
		case 2:
			return track.getTrackLengthAsString();
		case 3:
			return track.getMinAltitude();
		case 4:
			return track.getMaxAltitude();
		case 5:
			return track.getTotalAscent();
		case 6:
			return track.getTotalDescent();
		}
		return "unknown";
	}

	/**
	 * Column names of the result table
	 * TODO: I18n of the column names
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Name";
		case 1:
			return "Description";
		case 2:
			return "Length [km]";
		case 3:
			return "Lowest [m]";
		case 4:
			return "Highest [m]";
		case 5:
			return "Ascent [m]";
		case 6:
			return "Descent [m]";
		}
		return "unknown";
	}
}
