/**
 * 
 */
package jgpstrackedit.gpsies;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 * @author Hubert
 *
 */
public class GPSiesResult extends AbstractTableModel {
	
	private ArrayList<GPSiesTrackDescription> tracks = new ArrayList<GPSiesTrackDescription>();

	public void add(GPSiesTrackDescription track) {
		tracks.add(track);
	}
	
	public GPSiesTrackDescription get(int index) {
		return tracks.get(index);
	}

	// TableModel methods
	
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 7;
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return columnIndex == 1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		// TODO Auto-generated method stub
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class; //"Description";
		case 2:
			return String.class;
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
		// TODO Auto-generated method stub
		return tracks.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		// TODO Auto-generated method stub
		GPSiesTrackDescription track = tracks.get(row);
		switch (column) {
		case 0:
			return track.getTitle();
		case 1:
			/* old implementation
			if (track.getDescription() != null && track.getDescription().length() > 10)
			    return track.getDescription().substring(0,10)+"...";
			else
			    return "";
			    */
			String desc;
			if (track.getDescription() != null )
			    desc = track.getDescription();
			else
			    desc = "";
            return desc;
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

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
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
