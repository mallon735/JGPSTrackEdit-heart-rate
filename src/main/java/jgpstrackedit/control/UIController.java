/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.control;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.config.Constants;
import jgpstrackedit.data.Database;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.data.util.UnDoManager;
import jgpstrackedit.gpsies.GPSiesComDialog;
import jgpstrackedit.gpsies.GPSiesSaveDlg;
import jgpstrackedit.map.*;
import jgpstrackedit.map.elevation.ElevationCorrectionFactory;
import jgpstrackedit.map.elevation.IElevationCorrection;
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
import jgpstrackedit.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Hubert
 */
public class UIController implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(UIController.class);
	private static final String ELEVATION_CORRECTION_API_GOOGLE = "google";
	private static final String ELEVATION_CORRECTION_API_MAPQUEST = "mapquest";
	private static boolean automaticColors = Configuration.getProperty("AUTOMATIC_COLORS").equals("1");
	
	private Database db;
	private JGPSTrackEdit form;
	private JFileChooser fileSaveChooser;
	private JFileChooser fileOpenChooser;
	private File dirFile = null;

	private boolean googleMapEnabled = false;
	private boolean googleElevationAPIEnabled = false;

	public UIController(Database db, JGPSTrackEdit form) {
		this.db = db;
		this.form = form;
		TrackFileManager.addTrackFile(new GPXRoute());
		TrackFileManager.addTrackFile(new GPXTrack());
		TrackFileManager.addTrackFile(new KML());
		TrackFileManager.addTrackFile(new TCX());
		TrackFileManager.addTrackFile(new ASC());
		fileOpenChooser = new JFileChooser(System.getProperty("user.home"));
		fileSaveChooser = new JFileChooser(System.getProperty("user.home"));
		for (FileNameExtensionFilter filter : TrackFileManager
				.getFileNameExtensionFilters()) {
			fileSaveChooser.addChoosableFileFilter(filter);
		}
	}

	/**
	 * Opens the given track file
	 * 
	 * @param file
	 *            track file to open
	 */
	public void openTrack(File file) {
		form.setStateMessage("Reading file " + file.getAbsolutePath());

		try {
			final List<Track> tracks = TrackFileManager.openTrack(file);
			tracks.stream().forEach(track -> db.addTrack(track));
			
			int lastTrackIndex = db.getTrackNumber() - 1;
			final Track track = db.getTrack(lastTrackIndex);
			
			db.getTrackTableModel().setSelectedTrack(track);
			form.getTracksTable().addRowSelectionInterval(lastTrackIndex, lastTrackIndex);
			form.setSelectedTrack(track);
			form.setStateMessage(TrackFileManager.getLastMessage());
			zoomSelectedTrack();
			form.repaint();
		} catch (TrackFileException e) {
			logger.error("Exception while open track!", e);
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

		try {
			final List<Track> tracks = TrackFileManager.openKmlTrack(new URL(urlString));
			tracks.stream().forEach(track -> db.addTrack(track));
			
			int lastTrackIndex = db.getTrackNumber() - 1;
			final Track track = db.getTrack(lastTrackIndex);
			
			db.getTrackTableModel().setSelectedTrack(track);
			form.getTracksTable().addRowSelectionInterval(db.getTrackNumber() - 1, db.getTrackNumber() - 1);
			form.setSelectedTrack(track);
			form.setStateMessage("GPSies track " + track.getName() + " loaded.");
			zoomSelectedTrack();
			form.repaint();
		} catch (Exception e) {
			logger.error("Exception while open track!", e);
		}
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
			db.removeTrack(selectedRows[i]);
		}
		form.setStateMessage("");
			
		if(db.getTrackNumber() > 0) {
			int lastTrackIndex = db.getTrackNumber() - 1;
			final Track track = db.getTrack(lastTrackIndex);
			
			db.getTrackTableModel().setSelectedTrack(track);
			form.getTracksTable().addRowSelectionInterval(lastTrackIndex, lastTrackIndex);
			form.setSelectedTrack(track);
			form.repaint();
		} else {
			db.getTrackTableModel().setSelectedTrack(null);
			form.getTracksTable().clearSelection();
			form.setSelectedTrack(null);
			Track.resetColors();
			form.repaint();
		}
	}

	public void deleteAll() {
		int count = db.getTracks().size();
		for (int i = 0; i < count; i++) {
			db.removeTrack(0);
		}
		
		db.getTrackTableModel().setSelectedTrack(null);
		form.getTracksTable().clearSelection();
		form.setSelectedTrack(null);
		Track.resetColors();
		form.repaint();
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
				logger.error("Exception while save selected track!", e);
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
				logger.error("Exception while save track!", e);
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
			
			this.preselectFileFilterInFileSaveChooser(saveTrack);
			if(saveTrack.getTrackFilePath() != null) {
				fileSaveChooser.setCurrentDirectory(saveTrack.getTrackFilePath().toFile());
			}
			
			int returnVal = fileSaveChooser.showSaveDialog(form);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					TrackFileManager.saveTrack(saveTrack, fileSaveChooser
							.getSelectedFile(), fileSaveChooser.getFileFilter()
							.getDescription());
					saveTrack.setModified(false);
				} catch (TrackFileException e) {
					logger.error("Exception while save track as file!", e);
					JOptionPane.showMessageDialog(form,
							"Error while saving track: " + e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	/**
	 * Preselect the file chooser dialog with the file type of the track.
	 * 
	 * @param track The track to be saved
	 */
	private void preselectFileFilterInFileSaveChooser(final Track track) {
		if(track.getTrackFileType() == null) {
			return;
		}
		
		for(FileFilter filter: fileSaveChooser.getChoosableFileFilters()) {
			if(filter.getDescription().equals(track.getTrackFileType())) {
				fileSaveChooser.setFileFilter(filter);
				break;
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
		// logger.info("UIController: move: deltaX="+deltaX+" deltaY="+deltaY);
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
			secondTrack = firstTrack.split(
					firstTrack.getPoint(numberPoints),
					trackName + c,
					automaticColors);
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
			if(form.getSelectedPoint() != null 
				&& form.getSelectedPoint() != track.getFirstPoint() 
				&& form.getSelectedPoint() != track.getLastPoint()) 
			{
				secondTrack = track.split(form.getSelectedPoint(), trackName, automaticColors);
				db.addTrack(secondTrack);
			}
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
		while (firstTrack.getLength() > splitLength) {
			Optional<Point> splitPoint = firstTrack.getPoint(splitLength);
			if(splitPoint.isPresent()) {
				Track secondTrack = firstTrack.split(
						splitPoint.get(),
						trackName + c,
						automaticColors);
				c = c + "a";
				db.addTrack(secondTrack);
				firstTrack = secondTrack;
			}
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
			form.getAppendUnDo().add(selectedTrack,points,true);
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
		UnDoManager undo = form.getAppendUnDo();
		if( null != undo ){
			undo.unDo();
			form.repaint();
		}
	}

	public void tileManagerNone() {
		TileManager tileManager = TileManager.getCurrentTileManager();
		if (tileManager != null) {
			tileManager.close();
			TileManager.setCurrentTileManager(null);
		}

	}

	public void tileManagerOSM_Mapnik() {
		logger.debug("Command: OpenStreetMap");
		initTileManager(new OSMTileManager());

	}

	public void tileManagerOCM() {
		// TODO Auto-generated method stub
		// logger.debug("Command: OpenCycleMap");
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
			// logger.debug("Command: GoogleMap");
			initTileManager(new GoogleMapTileManager());
		}

	}

	public void tileManagerGoogleMapSatellite() {
		// TODO Auto-generated method stub
		if (isGoogleMapEnabled()) {
			// logger.debug("Command: GoogleMapSatellite");
			initTileManager(new GoogleMapSatelliteTileManager());
		}

	}

	public void tileManagerGoogleMapHybrid() {
		// TODO Auto-generated method stub
		if (isGoogleMapEnabled()) {
			// logger.debug("Command: GoogleMapHybride");
			initTileManager(new GoogleMapHybridTileManager());
		}

	}

	public void tileManagerGoogleMapTerrain() {
		// TODO Auto-generated method stub
		if (isGoogleMapEnabled()) {
			// logger.debug("Command: GoogleMapTerrain");
			initTileManager(new GoogleMapTerrainTileManager());
		}

	}

	public void tileManagerHikeBikeMap() {
		// TODO Auto-generated method stub
		// logger.debug("Command: HikeBikeMap");
		initTileManager(new HikeBikeTileManager());

	}

	public void tileManager4UMap() {
		// TODO Auto-generated method stub
		initTileManager(new FourUMapsTileManager());
		
	}
	
	public void tileManagerThunderForest() {
		initTileManager(new ThunderForestCycleMapTileManager(Configuration.getProperty("MAP_API_KEY_THUNDER_FOREST")));
	}

	public void tileManagerMapQuest() {
		// TODO Auto-generated method stub
		// logger.debug("Command: MapQuest");
		initTileManager(new MapQuestTileManager());

	}

	public void tileManagerMapQuestSat() {
		initTileManager(new MapQuestSatTileManager());

	}

	public void tileManagerMapQuestHybride() {
		initTileManager(new MapQuestHybrideTileManager());
	}
	
	public void updateTimeStamps() {
		final Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		if(selectedTrack == null) return;
	}
	
	public void updateElevation() {
		updateElevation(ELEVATION_CORRECTION_API_MAPQUEST);
	}

	private void updateElevation(String elevationCorrectionName) {
		final IElevationCorrection elevationCorrection = ElevationCorrectionFactory
				.create(elevationCorrectionName);

		if (elevationCorrectionName.equals(ELEVATION_CORRECTION_API_GOOGLE)) {
			if (!isGoogleElevationAPIEnabled()) {
				return;
			}
		}

		final List<Track> selectedTracks = new ArrayList<Track>();
		int[] selectedRows = form.getTracksTable().getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			selectedTracks.add(db.getTrack(selectedRows[i]));
		}
		
		// Run the elevation correction for all selected tracks.
		new ElevationCorrectionAction().elevationCorrectionPerformed(elevationCorrection, selectedTracks, this.form);
	}

	public Track newTrack() {
		final Track track = new Track();
		
		final String trackName = this.getNewTrackName();
		track.setName(trackName);
		
		Path path = this.getTrackPath();
		track.setTrackFilePath(path);
		track.setTrackFileType("Garmin GPX Track");
		
		if( Configuration.getProperty("AUTOMATIC_COLORS").equals("1")){
			track.assignColor();
		}
		return track;
	}
	
	private String getNewTrackName() {
		String trackName = JOptionPane.showInputDialog("Name of new track:");
		if (trackName == null || trackName.trim().length() == 0) {
			trackName = "Track-" + Long.toString((long)(Math.random() * 1000D));
		}
		return trackName;
	}
	
	private Path getTrackPath() {
		Path path = null;
		for(Track track : this.db.getTracks()) {
			if(track.getTrackFilePath() != null) {
				path = track.getTrackFilePath();
			}
		}
		
		if(path == null) {
			path = new File(System.getProperty("user.home")).toPath();
		}
		
		return path;
	}

	public void correctSelectedTrack() {
		final Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		if(selectedTrack == null) return;
		
		final String epsilon = JOptionPane
				.showInputDialog("Correction epsilon perimeter in km:");
		if (epsilon == null) return;
		
		selectedTrack.correct(Parser.parseDouble(epsilon));
		form.setStateMessage("Track " + selectedTrack.getName() + " corrected");
		zoomSelectedTrack();

	}

	public void openGPSies() {
		try {
			GPSiesComDialog dialog = new GPSiesComDialog(this);
			dialog.setModal(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			logger.error("Exception while open GPSies track!", e);
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
		logger.debug("UIControlleR: shortCut (" + start + "," + end + ")");
		if (start != null && end != null) {
			db.getTrackTableModel().getSelectedTrack().remove(start, end);
		}

	}

	@Override
	public void run() {
		openDirectoryTrackFiles();

	}

	public void correctZeroPoints() {
		final Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		if(selectedTrack == null) return;
		
		selectedTrack.correct();
		selectedTrack.correctDoublePoints();
		form.setStateMessage("Track " + selectedTrack.getName() + " corrected");
		zoomSelectedTrack();

	}

	public void compress() {
		Track selectedTrack = db.getTrackTableModel().getSelectedTrack();
		if(selectedTrack == null) return;
		
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
			form.getTracksView().getSelectedTrackView()
					.setSelectedPoint(selectedPoint);
			form.getAltitudeProfilePanel().setSelectedPoint(selectedPoint);
			int selectedPointIndex = selectedTrack.indexOf(selectedPoint);
			form.getPointsTable().setRowSelectionInterval(selectedPointIndex,
					selectedPointIndex);
			//
			if (zoom)
				zoomSelectedPoint();
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
		form.getTracksView().getSelectedTrackView()
					.setSelectedPoint(selectedPoint);
			form.getAltitudeProfilePanel().setSelectedPoint(selectedPoint);
			int selectedPointIndex = selectedTrack.indexOf(selectedPoint);
        form.getPointsTable().setRowSelectionInterval(selectedPointIndex,
        			selectedPointIndex);
//
        if (zoom)
        	zoomSelectedPoint();
		}
		
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
		JFileChooser imageSaveChooser = new JFileChooser();
		imageSaveChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image", "png"));
		int returnVal = imageSaveChooser.showSaveDialog(form);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
	        try {
	        	String fileName = imageSaveChooser.getSelectedFile().getAbsolutePath();
	        	if (!fileName.endsWith(".png")) {
	        		fileName = fileName + ".png";
	        	}
				ImageIO.write(form.getTracksPanel().getImage(),"png", new File(fileName));
			} catch (IOException e) {
				logger.error("Exception in save map view as image!", e);
			}

		}

	}

	public void saveAltitudeProfileasImage() {
		JFileChooser imageSaveChooser = new JFileChooser();
		imageSaveChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image", "png"));
		int returnVal = imageSaveChooser.showSaveDialog(form);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
	        try {
	        	String fileName = imageSaveChooser.getSelectedFile().getAbsolutePath();
	        	if (!fileName.endsWith(".png")) {
	        		fileName = fileName + ".png";
	        	}
				ImageIO.write(form.getAltitudeProfilePanel().getImage(),"png", new File(fileName));
			} catch (IOException e) {
				logger.error("Exception in save altitude profile as image!", e);
			}

		}
	}

	public void smoothTrackElevation() {
		int[] selectedRows = form.getTracksTable().getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			db.smoothTrackElevation(selectedRows[i]); 
			form.setStateMessage("Track "
					+ db.getTracks().get(selectedRows[i]).getName()
					+ " smoothed");
		}
	}

}
