/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.control;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import jgpstrackedit.config.Constants;
import jgpstrackedit.data.Database;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.gpsies.GPSiesComDialog;
import jgpstrackedit.gpsies.GPSiesSaveDlg;
import jgpstrackedit.map.FourUMapsTileManager;
import jgpstrackedit.map.GoogleMapHybridTileManager;
import jgpstrackedit.map.GoogleMapSatelliteTileManager;
import jgpstrackedit.map.GoogleMapTerrainTileManager;
import jgpstrackedit.map.GoogleMapTileManager;
import jgpstrackedit.map.HikeBikeTileManager;
import jgpstrackedit.map.MapQuestHybrideTileManager;
import jgpstrackedit.map.MapQuestSatTileManager;
import jgpstrackedit.map.MapQuestTileManager;
import jgpstrackedit.map.OCMTileManager;
import jgpstrackedit.map.OSMTileManager;
import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.elevation.ElevationAPI;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.util.MapExtract;
import jgpstrackedit.map.util.MapExtractManager;
import jgpstrackedit.routing.MapQuestRouting;
import jgpstrackedit.trackfile.TrackFileException;
import jgpstrackedit.trackfile.TrackFileManager;
import jgpstrackedit.trackfile.asc.ASC;
import jgpstrackedit.trackfile.gpxroute.GPXRoute;
import jgpstrackedit.trackfile.gpxtrack.GPXTrack;
import jgpstrackedit.trackfile.kml.KML;
import jgpstrackedit.trackfile.tcx.TCX;
import jgpstrackedit.util.Browser;
import jgpstrackedit.util.DirectoryFilter;
import jgpstrackedit.util.Parser;
import jgpstrackedit.view.DlgCompressOptions;
import jgpstrackedit.view.DlgSelectMapExtract;
import jgpstrackedit.view.JGPSTrackEdit;
import jgpstrackedit.view.Transform;

import org.xml.sax.SAXException;

/**
 * 
 * @author Hubert
 */
public class UIController implements Runnable {

	private Database db;
	private JGPSTrackEdit form;
	private JFileChooser fileSaveChooser;
	private JFileChooser fileOpenChooser;
	private static JFileChooser imageSaveChooser = null;
	private File dirFile = null;

	private boolean googleMapEnabled = false;
	private boolean googleElevationAPIEnabled = false;

	private static UIController instance = null;
	
	private UIController(Database db, JGPSTrackEdit form) {
		this.db = db;
		this.form = form;
		TrackFileManager.addTrackFile(new GPXRoute());
		TrackFileManager.addTrackFile(new GPXTrack());
		TrackFileManager.addTrackFile(new KML());
		TrackFileManager.addTrackFile(new TCX());
		TrackFileManager.addTrackFile(new ASC());
		fileOpenChooser = new JFileChooser();
		fileSaveChooser = new JFileChooser();
		for (FileNameExtensionFilter filter : TrackFileManager
				.getFileNameExtensionFilters()) {
			fileSaveChooser.addChoosableFileFilter(filter);
		}
	}

	public static UIController newUIController(Database db, JGPSTrackEdit form) {
		if (instance == null) {
			instance = new UIController(db,form);
		}
		return instance;
	}
	
	public static UIController getUIController() {
		return instance;
	}
	
	public static JFileChooser getJFileChooser() {
		if (imageSaveChooser == null) {
		    imageSaveChooser = new JFileChooser();
		    imageSaveChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image", "png"));
		}
		return imageSaveChooser;
	}
	/**
	 * Opens the given track file
	 * 
	 * @param file
	 *            track file to open
	 */
	protected void openTrack(File file) {
		form.setStateMessage("Reading file " + file.getAbsolutePath());
		Track track;
		try {
			track = TrackFileManager.openTrack(file);
			track.setTrackFileName(file.getAbsolutePath());
			track.setModified(false);
			db.addTrack(track);
			db.getTrackTableModel().setSelectedTrack(track);
			// form.getTracksView().setSelectedTrack(track);
			form.getTracksTable().addRowSelectionInterval(
					db.getTrackNumber() - 1, db.getTrackNumber() - 1);
			form.setSelectedTrack(track);
			form.setStateMessage(TrackFileManager.getLastMessage());
			//
			zoomSelectedTrack();
			// form.getTracksPanel().zoom(track.getLeftUpperBoundary(),track.getRightLowerBoundary());
			form.repaint();
		} catch (TrackFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * UIController functionality to open a track, the user is asked to choose a
	 * track file
	 * 
	 */
	public void openTrack() {
		int returnVal = fileOpenChooser.showOpenDialog(form);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileOpenChooser.getSelectedFile();
			openTrack(file);
		}
	}

	/**
	 * UIController functionality to open all tracks of a directory, the user is
	 * asked to choose a directory
	 * 
	 */
	public void openDirectory() {
		JFileChooser dirOpenChooser = new JFileChooser();
		dirOpenChooser.addChoosableFileFilter(new DirectoryFilter());
		dirOpenChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = dirOpenChooser.showOpenDialog(form);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dirFile = dirOpenChooser.getSelectedFile();
			new Thread(this).start();
		}
	}

	protected void openDirectoryTrackFiles() {
		// TODO Auto-generated method stub
		File[] trackFiles = dirFile.listFiles();
		for (int i = 0; i < trackFiles.length; i++) {
			if (trackFiles[i].isFile())
				openTrack(trackFiles[i]);
		}

	}

	public void openTrack(String urlString) {

		form.setStateMessage("Loading GPSies track... ");
		Track track = null;
		URL url;
		try {
			url = new URL(urlString);
			KML kml = new KML();
			track = kml.openTrack(url);
			track.setModified(false);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.addTrack(track);
		db.getTrackTableModel().setSelectedTrack(track);
		form.getTracksTable().addRowSelectionInterval(db.getTrackNumber() - 1,
				db.getTrackNumber() - 1);
		form.setSelectedTrack(track);
		form.setStateMessage("GPSies track " + track.getName() + " loaded.");
		zoomSelectedTrack();
		form.repaint();

	}

	public void reverseTrack() {
		int[] selectedRows = form.getTracksTable().getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			db.reverseTrack(selectedRows[i]);
			form.setStateMessage("Track "
					+ db.getTracks().get(selectedRows[i]).getName()
					+ " reversed");
		}
	}

	public void delete() {
		int[] selectedRows = form.getTracksTable().getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			String name = db.getTracks().get(selectedRows[i]).getName();
			db.removeTrack(selectedRows[i]);
			form.setStateMessage("Track " + name + " deleted.");
		}
	}

	public void saveSelected() {
		if (form.getTracksTable().getSelectedRowCount() == 1) {
			Track saveTrack = db.getTracks().get(
					form.getTracksTable().getSelectedRow());
			File file = new File(saveTrack.getTrackFileName());
			try {
				TrackFileManager.saveTrack(saveTrack, file,
						saveTrack.getTrackFileType());
				saveTrack.setModified(false);
			} catch (TrackFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(form,
						"Error while saving track: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void save() {
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		if (selectedTrack.getTrackFileName() == null
				|| selectedTrack.getTrackFileName().equals("")) {
			saveAs();
		} else {
			File file = new File(selectedTrack.getTrackFileName());
			try {
				TrackFileManager.saveTrack(selectedTrack, file,
						selectedTrack.getTrackFileType());
				selectedTrack.setModified(false);
			} catch (TrackFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						form,
						"Error while saving track "
								+ selectedTrack.getTrackFileName() + ": "
								+ e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void saveAs() {
		if (form.getTracksTable().getSelectedRowCount() == 1) {
			Track saveTrack = db.getTracks().get(
					form.getTracksTable().getSelectedRow());
			int returnVal = fileSaveChooser.showSaveDialog(form);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					TrackFileManager.saveTrack(saveTrack, fileSaveChooser
							.getSelectedFile(), fileSaveChooser.getFileFilter()
							.getDescription());
					saveTrack.setModified(false);
				} catch (TrackFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(form,
							"Error while saving track: " + e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void moveNorth() {
		form.getTracksPanel().moveNorth();
	}

	public void moveSouth() {
		form.getTracksPanel().moveSouth();
	}

	public void moveWest() {
		form.getTracksPanel().moveWest();
	}

	public void moveEast() {
		form.getTracksPanel().moveEast();
	}

	/**
	 * Moves the current view in the given direction
	 * 
	 * @param deltaX
	 *            relative amount to move in x direction (longitude)
	 * @param deltaY
	 *            relative amount to move in y direction (latitudee)
	 */
	public void move(double deltaX, double deltaY) {
		// System.out.println("UIController: move: deltaX="+deltaX+" deltaY="+deltaY);
		form.getTracksPanel().move(deltaX, deltaY);
	}

	public void zoomIn() {
		form.getTracksPanel().zoomIn();
	}

	public void zoomOut() {
		form.getTracksPanel().zoomOut();
	}

	public void zoomSelectedTrack() {
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		form.getTracksPanel().zoom(selectedTrack.getLeftUpperBoundary(),
				selectedTrack.getRightLowerBoundary());

	}

	public void deleteSelectedPoint() {
		form.getTracksView().getSelectedTrackView().deleteSelectedPoint();
	}

	public void insertAdjacentPoints() {
		form.getTracksView().getSelectedTrackView().insertAdjacentPoints();

	}

	protected void splitNrPoints(Track track, String trackName, int numberPoints) {
		int numberTrackPoints = track.getNumberPoints();
		String c = "a";
		Track firstTrack = track;
		Track secondTrack;
		for (int nr = 0; nr < numberTrackPoints - numberPoints - 1; nr = nr
				+ numberPoints) {
			secondTrack = firstTrack.split(firstTrack.getPoint(numberPoints),
					trackName + c);
			c = c + "a";
			db.addTrack(secondTrack);
			firstTrack = secondTrack;
		}

	}

	public void split(int splitOption, Track track, String trackName,
			int numberTracks, int numberPoints, double splitLength) {
		Track secondTrack;
		switch (splitOption) {
		case Constants.SPLIT_NONE:
			break;
		case Constants.SPLIT_SELECTED_POINT:
			secondTrack = track.split(form.getSelectedPoint(), trackName);
			db.addTrack(secondTrack);
			break;
		case Constants.SPLIT_NUMBER_TRACKS:
			splitNrPoints(track, trackName, track.getNumberPoints()
					/ numberTracks);
			break;
		case Constants.SPLIT_NUMBER_POINTS:
			splitNrPoints(track, trackName, numberPoints);
			break;
		case Constants.SPLIT_LENGTH:
			splitLength(track, trackName, splitLength);
			break;
		}

	}

	protected void splitLength(Track track, String trackName, double splitLength) {
		String c = "a";
		Track firstTrack = track;
		Track secondTrack;
		while (firstTrack.getLength() > splitLength) {
			secondTrack = firstTrack.split(firstTrack.getPoint(splitLength),
					trackName + c);
			c = c + "a";
			db.addTrack(secondTrack);
			firstTrack = secondTrack;
		}

	}

	public void merge(int mergeOption, Track track, Track mergeTrack,
			String trackName) {
		switch (mergeOption) {
		case Constants.MERGE_NONE:
			break;
		case Constants.MERGE_TRACK:
			track.add(mergeTrack,false);
			track.setName(trackName);
			db.removeTrack(mergeTrack);
			break;
		case Constants.MERGE_DIRECT:
			track.add(mergeTrack,true);
			track.setName(trackName);
			db.removeTrack(mergeTrack);
			break;
		}

	}

	public void setSelectedPointPosition(int screenX, int screenY) {
		// TODO Auto-generated method stub
		form.getTracksView()
				.getSelectedTrackView()
				.setSelectedPointPosition(Transform.mapLongitude(screenX),
						Transform.mapLatitude(screenY));

	}

	public void appendPoint(int screenX, int screenY) {
		// TODO Auto-generated method stub
		form.getTracksView()
				.getSelectedTrackView()
				.getTrack()
				.add(Transform.mapLongitude(screenX),
						Transform.mapLatitude(screenY));

	}

	public void appendRoutingPoint(int screenX, int screenY) {
		// TODO Auto-generated method stub
		form.getTracksPanel().setCursor(
				Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		MapQuestRouting mapQuestRouting = new MapQuestRouting();
		ArrayList<Point> points = mapQuestRouting.loadRoute(selectedTrack
				.getLastPoint(), new Point(Transform.mapLongitude(screenX),
				Transform.mapLatitude(screenY)));
		form.getTracksPanel().setCursor(
				Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		if (points != null) {
			form.getAppendUnDo().add(points);
			selectedTrack.add(points);
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"The route couldn't be obtained from the MapQuest server. Check your internet connection or the MapQuest server is down.",
							"Internet Access Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void undoAppend() {
		// TODO Auto-generated method stub
		form.getAppendUnDo().unDo();
	}

	public void tileManagerNone() {
		TileManager tileManager = TileManager.getCurrentTileManager();
		if (tileManager != null) {
			tileManager.close();
			TileManager.setCurrentTileManager(null);
		}

	}

	public void tileManagerOSM_Mapnik() {
		System.out.println("Command: OpenStreetMap");
		initTileManager(new OSMTileManager());

	}

	public void tileManagerOCM() {
		// TODO Auto-generated method stub
		// System.out.println("Command: OpenCycleMap");
		initTileManager(new OCMTileManager());

	}

	protected void initTileManager(TileManager tileManager) {
		TileManager currentTileManager = TileManager.getCurrentTileManager();
		if (currentTileManager != null) {
			boolean showTiles = currentTileManager.isShowTiles(); 
			currentTileManager.close();
			tileManager.addMapObserver(form.getTracksPanel());
			tileManager.open();
			TileManager.setCurrentTileManager(tileManager);
			tileManager.setShowTiles(showTiles);
		} else {
			tileManager.addMapObserver(form.getTracksPanel());
			tileManager.open();
			TileManager.setCurrentTileManager(tileManager);
			/*
			 * Transform.setTransform(Transform.getUpperLeftBoundary(),
			 * Transform.getLowerRightBoundary(), Transform.getScreenWidth(),
			 * Transform.getScreenHeight(), true);
			 */
			form.getTracksView().setView(Transform.getUpperLeftBoundary(),
					Transform.getLowerRightBoundary());
		}
		form.getTracksPanel().repaint();

	}

	public void tileManagerGoogleMap() {
		// TODO Auto-generated method stub
		if (isGoogleMapEnabled()) {
			// System.out.println("Command: GoogleMap");
			initTileManager(new GoogleMapTileManager());
		}

	}

	public void tileManagerGoogleMapSatellite() {
		// TODO Auto-generated method stub
		if (isGoogleMapEnabled()) {
			// System.out.println("Command: GoogleMapSatellite");
			initTileManager(new GoogleMapSatelliteTileManager());
		}

	}

	public void tileManagerGoogleMapHybrid() {
		// TODO Auto-generated method stub
		if (isGoogleMapEnabled()) {
			// System.out.println("Command: GoogleMapHybride");
			initTileManager(new GoogleMapHybridTileManager());
		}

	}

	public void tileManagerGoogleMapTerrain() {
		// TODO Auto-generated method stub
		if (isGoogleMapEnabled()) {
			// System.out.println("Command: GoogleMapTerrain");
			initTileManager(new GoogleMapTerrainTileManager());
		}

	}

	public void tileManagerHikeBikeMap() {
		// TODO Auto-generated method stub
		// System.out.println("Command: HikeBikeMap");
		initTileManager(new HikeBikeTileManager());

	}

	public void tileManager4UMap() {
		// TODO Auto-generated method stub
		initTileManager(new FourUMapsTileManager());
		
	}

	public void tileManagerMapQuest() {
		// TODO Auto-generated method stub
		// System.out.println("Command: MapQuest");
		initTileManager(new MapQuestTileManager());

	}

	public void tileManagerMapQuestSat() {
		// TODO Auto-generated method stub
		initTileManager(new MapQuestSatTileManager());

	}

	public void tileManagerMapQuestHybride() {
		// TODO Auto-generated method stub
		initTileManager(new MapQuestHybrideTileManager());

	}

	public void updateElevation() {
		// TODO Auto-generated method stub
		if (isGoogleElevationAPIEnabled()) {
			int[] selectedRows = form.getTracksTable().getSelectedRows();
			ElevationAPI elevationAPI = new ElevationAPI();
			for (int i = 0; i < selectedRows.length; i++) {
				try {
					elevationAPI.updateElevation(db.getTrack(selectedRows[i]));
				} catch (ElevationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (e.getMessage().equals("OVER_QUERY_LIMIT")) {
						JOptionPane.showMessageDialog(form, "The Google-API query limit was reached. Try another day to update elevations!",
								"Google-API-Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				form.setStateMessage("Elevation of track "
						+ db.getTracks().get(i).getName() + " updated");
			}
		}

	}

	public Track newTrack() {
		// TODO Auto-generated method stub
		String trackName = JOptionPane.showInputDialog("Name of new track:");
		if (trackName == null)
			return null;
		Track track = new Track();
		track.setName(trackName);
		track.setTrackFileName(trackName);
		track.setTrackFileType("Garmin GPX Track");
		return track;

	}

	public void correctSelectedTrack() {
		// TODO Auto-generated method stub
		String epsilon = JOptionPane
				.showInputDialog("Correction epsilon perimeter in km:");
		if (epsilon == null)
			return;
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		selectedTrack.correct(Parser.parseDouble(epsilon));
		form.setStateMessage("Track " + selectedTrack.getName() + " corrected");
		zoomSelectedTrack();

	}

	public void openGPSies() {
		// TODO Auto-generated method stub
		try {
			GPSiesComDialog dialog = new GPSiesComDialog(this);
			dialog.setModal(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected boolean isGoogleMapEnabled() {
		String GOOGLEMAPQUESTION = "You are trying to use google maps. "
				+ "Google has established usage conditions.\n It is your sole responsibility to confirm "
				+ "wether using google map is allowed by google's license policy.\n"
				+ "See https://developers.google.com/maps/documentation/staticmaps/\n"
				+ "Click on the OK-button only if you have the right to use google maps!";
		if (!googleMapEnabled) {
			googleMapEnabled = JOptionPane.showConfirmDialog(form,
					GOOGLEMAPQUESTION, "Warning", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
		}
		return googleMapEnabled;
	}

	protected boolean isGoogleElevationAPIEnabled() {
		String GOOGLEAPIQUESTION = "The update elevations function is based on Google Elevation API. "
				+ "Google has established usage conditions.\n It is your sole responsibility to confirm "
				+ "wether using Google Elevation API is allowed by google's license policy.\n"
				+ "See https://developers.google.com/maps/documentation/elevation/\n"
				+ "Click on the OK-button only if you have the right to use Google Elevation API!";
		if (!googleElevationAPIEnabled) {
			googleElevationAPIEnabled = JOptionPane.showConfirmDialog(form,
					GOOGLEAPIQUESTION, "Warning", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
		}
		return googleElevationAPIEnabled;
	}

	public void userManual() {
		// TODO Auto-generated method stub
		Browser.openURL("https://sourceforge.net/p/jgpstrackedit/wiki/Home/");

	}

	public void updatePage() {
		Browser.openURL("http://sourceforge.net/projects/jgpstrackedit/files/binaries/");

	}

	public void shortCut(Point start, Point end) {
		System.out
				.println("UIControlleR: shortCut (" + start + "," + end + ")");
		if (start != null && end != null) {
			db.getTrackTableModel().getSelectedTrack().remove(start, end);
		}

	}

	@Override
	public void run() {
		openDirectoryTrackFiles();

	}

	public void correctZeroPoints() {
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		selectedTrack.correct();
		selectedTrack.correctDoublePoints();
		form.setStateMessage("Track " + selectedTrack.getName() + " corrected");
		zoomSelectedTrack();

	}

	public void compress() {
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		DlgCompressOptions dialog = new DlgCompressOptions(
				selectedTrack.getName());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
		dialog.setVisible(true);
		if (dialog.copyToNewTrack() && dialog.getOption() != DlgCompressOptions.CompressionOption.Cancel) {
			selectedTrack = selectedTrack.clone();
			selectedTrack.setName(dialog.getTrackName());
			selectedTrack.setColor(Color.RED);
			db.addTrack(selectedTrack);
			db.getTrackTableModel().setSelectedTrack(selectedTrack);
			form.getTracksTable().addRowSelectionInterval(
					db.getTrackNumber() - 1, db.getTrackNumber() - 1);
			form.setSelectedTrack(selectedTrack);
		}
		switch (dialog.getOption()) {
		case Cancel:
			break;
		case RemoveInterval:
			selectedTrack.compress(dialog.getRemoveInterval());
			form.setStateMessage("Track " + selectedTrack.getName()
					+ " compressed.");
			break;
		case MaxDistance:
			selectedTrack.compress(dialog.getMaxDistance());
			form.setStateMessage("Track " + selectedTrack.getName()
					+ " compressed.");
			break;
		case Interdistance:
			selectedTrack.compress(dialog.getInterdistance(), 0);
			form.setStateMessage("Track " + selectedTrack.getName()
					+ " compressed.");
			break;
		case DouglasPeucker:
			selectedTrack.compressDouglasPeucker(dialog.getDouglasPeuckerDistance());
			form.setStateMessage("Track " + selectedTrack.getName()
					+ " compressed.");
			break;
		}

	}

	public void zoomSelectedPoint() {
		Point zoomPoint = form.getTracksView().getSelectedTrackView()
				.getSelectedPoint();
		if (zoomPoint != null) {
			form.getTracksPanel().zoom(zoomPoint);
		}

	}

	public void selectPreviousPoint(boolean zoom) {
		Point selectedPoint = form.getTracksView().getSelectedTrackView()
				.getSelectedPoint();
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		if (selectedTrack != null && selectedPoint != null) {
			selectedPoint = selectedTrack.previousPoint(selectedPoint);
			// TODO: Refactor code, use Observer Design Pattern
			selectPoint(selectedTrack,selectedPoint,zoom);
		}

	}

	public void selectNextPoint(boolean zoom) {
		Point selectedPoint = form.getTracksView().getSelectedTrackView()
				.getSelectedPoint();
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		if (selectedTrack != null && selectedPoint != null) {
			selectedPoint = selectedTrack.nextPoint(selectedPoint);
			// TODO: Refactor code, use Observer Design Pattern when selecting a
			// point
			// to inform all interested instances
			selectPoint(selectedTrack,selectedPoint,zoom);
		}

	}

	public void selectPoint(Track track, Point point, boolean zoom) {
		form.getTracksView().getSelectedTrackView()
		.setSelectedPoint(point);
        form.getAltitudeProfilePanel().setSelectedPoint(point);
        int selectedPointIndex = track.indexOf(point);
        form.getPointsTable().setRowSelectionInterval(selectedPointIndex,
        			selectedPointIndex);
//
        if (zoom)
        	zoomSelectedPoint();
		
	}
	
	public void saveMapExtract() {
		// TODO Auto-generated method stub
		String mapExtractName = JOptionPane
				.showInputDialog("Name of new map extract:");
		if (mapExtractName == null)
			return;
		MapExtractManager.add(mapExtractName, Transform.getZoomLevel(),
				Transform.getUpperLeftBoundary());

	}

	public void zoomMapExtract() {
		// TODO Auto-generated method stub
		DlgSelectMapExtract dlg = new DlgSelectMapExtract(
				MapExtractManager.mapExtractNames());
		dlg.setVisible(true);
		String mapExtractName = dlg.getSelectedMapExtractName();
		if (mapExtractName != null) {
			MapExtract mapExtract = MapExtractManager.get(dlg
					.getSelectedMapExtractName());
			if (mapExtract != null) {
				form.getTracksPanel().zoom(mapExtract);
			}
		}

	}

	public void saveGPSies() {
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		GPSiesSaveDlg dialog = new GPSiesSaveDlg(selectedTrack);
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
				
	}

	public void saveMapViewAsImage() {
		// TODO Auto-generated method stub
		JFileChooser chooser = getJFileChooser();
		int returnVal = chooser.showSaveDialog(form);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
	        try {
	        	String fileName = chooser.getSelectedFile().getAbsolutePath();
	        	if (!fileName.endsWith(".png")) {
	        		fileName = fileName + ".png";
	        	}
				ImageIO.write(form.getTracksPanel().getImage(),"png", new File(fileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void saveAltitudeProfileasImage() {
		// TODO Auto-generated method stub
		JFileChooser chooser = getJFileChooser();
		int returnVal = chooser.showSaveDialog(form);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
	        try {
	        	String fileName = chooser.getSelectedFile().getAbsolutePath();
	        	if (!fileName.endsWith(".png")) {
	        		fileName = fileName + ".png";
	        	}
				ImageIO.write(form.getAltitudeProfilePanel().getImage(),"png", new File(fileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}


}
