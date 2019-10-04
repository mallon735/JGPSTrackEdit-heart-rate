package jgpstrackedit.view;

import jgpstrackedit.international.International;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DlgHelp extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public DlgHelp() {
		setTitle("JGPSTrackEdit - "+International.getText("dlghelp.Help"));
		setBounds(100, 100, 616, 480);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				JTextArea txtrJgpstrackeditIsA = new JTextArea();
				txtrJgpstrackeditIsA.setEditable(false);
				txtrJgpstrackeditIsA.setText("Hint: An online user manual may be found at https://sourceforge.net/p/jgpstrackedit/wiki/Home/.\r\nUse the menu Help.User Manual to open the online user manual in your webbrowser.\r\n\r\nJGPSTrackEdit is a tool for editing (gps) tracks and planing (multiple days) tours.\r\n\r\nAn abritary number of tracks may be opened or created, two tracks may be merged, a track may be splitted, reversed or compressed.\r\nPoints may be inserted or appended to a track or may be moved or deleted from a track.\r\n\r\nA map may be  used optionaly: View.Maps\r\nMaps of several providers are available, default map is OpenStreetMap. Be carefully when using google maps, read google license und usage conditions first. \r\nMaps consists of tiles, which are loaded when needed. \r\nTiles are permanently cached in the local file system (disk) in the maps subdirectory to minimize map traffic. \r\nThe maps directory is usually located in the same directory as the JGPSTrackEdit.jar file.\r\nNote: A connection to the internet is required to load not already cached map tiles from the web! \r\nBe carefully using google maps. Read license conditions first!\r\n\r\nThere is a configuration dialog (File.Configuration) where several parameters may be configured and stored.\r\n\r\nImportant: Read googles licence conditions before using functions which use a google api.\r\n\r\nCommon tasks:\r\n\r\nOpen a track: Choose \"File.Open Track\", then find the desired track file. Currently Garmin gpx track, route, tcx and kml are supported. The current file type is determined automatically.\r\nYou may load tracks from gpsies.com, choose \"File.Open GPSies.com Track\".\r\n\r\nSelect a track: Click on the desired track in the track window. All operations are done on the selected track.\r\n\r\nSelect a point: Click on a point of the selected track. Most point functions are done on the selected point.\r\n\r\nCreate a new track: Click the mouse on the start point of the map. Than choose \"Track.New\". If you are ready, end appending of points with Point.Append mode.\r\n\r\nPan: drag the mouse in the desired direction or use direction buttons.\r\n\r\nZooming: either by the \"View.Zoom in\", \"View.Zoom out\" menu items or by steering the mouse wheel.\r\nPlaning a multiple day tour: First merge all tracks to a single track. Than update the parameter in the tourplaner dilog (File.Configuration, tab tourplaner). \r\nTo display the day limit markers, choose \"View.Show Day Tour markers\". \r\nNow you can split the overall track to daily tracks.\r\n\r\nAppend points to a track: Choose \"Point.Append mode\". New points are appended to the end of the track. \r\nIf you want to add points to the beginning of the tour, reverse (\"Track.Reverse\") the track first.\r\nYou may use  \"Points.Append routing mode\" to automatically route to the new point.\r\nImportant: The elevation of newly inserted point ist set to 0. After finishing, choose \"Track.Update elevation\" to recalculate the elevations of the whole track. These restriction is forced by google's condition of using the google map elevation api.\r\n\r\nDeleting points: First select a point, then choose \"Point.Delete\". \r\nHint: you may use the popup menu clicking on the right mouse button. To delete more points, use deleting mode (\"Point.Delete Mode\").\r\n\r\nStore a track: Choose \"File.Save as\". Currently only gpx track, route and kml are supported.\r\n\r\nKnown issues:\r\n- Map tiles are currently not checked for updates. Use an explorer of the local file system to delete map tiles in the maps directory if they are out of date. The maps directory is usually located in the same directory as the JGPSTrackEdit.jar file. \r\nIt is save to delete any subdirectories or image files. JGPSTrackEdit must be stopped prior to any changes.\r\n");
				txtrJgpstrackeditIsA.setCaretPosition(0);
				scrollPane.setViewportView(txtrJgpstrackeditIsA);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(International.getText("OK"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				okButton.setActionCommand(International.getText("OK"));
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
