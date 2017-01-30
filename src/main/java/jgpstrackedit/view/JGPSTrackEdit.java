/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JGPSTrackEdit.java
 *
 * Created on 08.06.2010, 20:32:46
 */
package jgpstrackedit.view;

/*
 * This software is copyright Hubert Lutnik 2012 and made available through the GNU GPL version 3,
 * see also http://www.gnu.org/copyleft/gpl.html.
 * Usage only for non commercial purposes.
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.config.ConfigurationObserver;
import jgpstrackedit.config.view.ConfigurationDialog;
import jgpstrackedit.config.view.ViewingConfiguration;
import jgpstrackedit.control.UIController;
import jgpstrackedit.data.Database;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.data.util.TourPlaner;
import jgpstrackedit.data.util.UnDoManager;
import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.tiledownload.DlgProcessingTileDownload;
import jgpstrackedit.map.tiledownload.DlgStartTiledownloadMode;
import jgpstrackedit.map.tiledownload.DlgStopTiledownloadMode;
import jgpstrackedit.map.tiledownload.TileDownload;
import jgpstrackedit.map.util.MapExtract;
import jgpstrackedit.map.util.MapExtractManager;
import jgpstrackedit.map.util.TileNumber;
import jgpstrackedit.view.util.ColorEditor;
import jgpstrackedit.view.util.ColorRenderer;
import jgpstrackedit.international.International;
import jgpstrackedit.util.Parser;

/* TODO:
 * AltitudeProfil, Sync selected Point to Map
 - Internationalisierung (Deutsch)

 */

/**
 * JGPSTrackEdit is a tool for editing (gps) tracks and planing (multiple days)
 * tours. This class represent the main class, providing the main method.<br>
 * This software is copyright Hubert Lutnik 2012,2013,2014 and made available through the
 * GNU GPL version 3, see also http://www.gnu.org/copyleft/gpl.html. Usage only
 * for non commercial purposes.
 * 
 * @author Hubert
 */
public class JGPSTrackEdit extends javax.swing.JFrame implements
		ListSelectionListener, MouseListener, MouseMotionListener,
		MouseWheelListener, ConfigurationObserver, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TracksPanel tracksPanel;
	private TrackPanel trackPanel;
	private TrackDataPanel trackDataPanel;
	private AltitudeProfilePanel altitudeProfilePanel;
	private TracksView tracksView;
	private UIController uiController;

	private Database db;

	private int selectedRowTracksTable = -1;
	private boolean pointDeleteMode = false;
	private boolean moveSelectedPoint = false;
	private boolean moveSelectedPointMode = false;
	private boolean appendMode = false;
	private boolean appendRoutingMode = false;
	private boolean showCoordinatesMode = false;
	private boolean shortCut = false;
	private boolean distanceMeasurement = false;
	private Point distanceMeasurementFirstPoint = null;
	private Point distanceMeasurementSecondPoint = null;
	private Point shortCutStartPoint = null;
	private int currentScreenX = 0;
	private int currentScreenY = 0;
	private int lastScreenX = 0;
	private int lastScreenY = 0;
	private int lastDraggedX = -1;
	private int lastDraggedY = -1;
	private boolean draggingActive = false;

	private TileDownload tileDownload = null;
	public static final int MODE_INACTIVE = 0;
	public static final int MODE_WAIT_FIRST_POINT = 1;
	public static final int MODE_WAIT_SECOND_POINT = 2;
	private int tileSelectionMode = MODE_INACTIVE;
	private Point tileSelectFirstPoint = null;

	private ButtonGroup mapRadioButtons = new ButtonGroup();
	private JGPSTrackEdit own;
	private JPopupMenu popupTracksView;
	private UnDoManager appendUnDo = null;
	private JCheckBoxMenuItem chckbxmntmScale;
	private boolean showScale = true;

	/**
	 * Sets the variant of JGPSTrackEdit
	 */
	private void setJGPSTrackEditVariant() {
		// comment and uncomment the desired variant of JGPSTrackEdit

		// TODO: This code must be optimized, generic list of tilemanagers
		String mapType = Configuration.getProperty("MAPTYPE");

		if (mapType.equals("OpenStreetMap")) {
			uiController.tileManagerOSM_Mapnik();
			rdbtnmntmOpenstreetmapmapnik.setSelected(true);
		} else if (mapType.equals("OpenCycleMap")) {
			uiController.tileManagerOCM();
			this.rdbtnmntmOpencyclemap.setSelected(true);
		} else if (mapType.equals("MapQuest")) {
			uiController.tileManagerMapQuest();
			this.rdbtnmntmMapquest.setSelected(true);
		} else if (mapType.equals("MapQuestSat")) {
			uiController.tileManagerMapQuestSat();
			this.rdbtnmntmMapquestsatellite.setSelected(true);
		} else if (mapType.equals("MapQuestHybride")) {
			uiController.tileManagerMapQuestHybride();
			this.rdbtnmntmMapquesthybride.setSelected(true);
		} else if (mapType.equals("HikeBikeMap")) {
			uiController.tileManagerHikeBikeMap();
			this.rdbtnmntmHikebikemap.setSelected(true);
		} else if (mapType.equals("GoogleMap")) {
			uiController.tileManagerGoogleMap();
			this.rdbtnmntmGooglemap.setSelected(true);
		} else if (mapType.equals("GoogleMapTerrain")) {
			uiController.tileManagerGoogleMapTerrain();
			this.rdbtnmntmGooglemapterrain.setSelected(true);
		} else if (mapType.equals("GoogleMapSatellite")) {
			uiController.tileManagerGoogleMapSatellite();
			this.rdbtnmntmGooglemapsatellite.setSelected(true);
		} else if (mapType.equals("GoogleMapHybrid")) {
			uiController.tileManagerGoogleMapHybrid();
			this.rdbtnmntmGooglemaphybrid.setSelected(true);
		} else if (mapType.equals("4UMap")) {
			uiController.tileManager4UMap();
			this.rdbtnmntmumap.setSelected(true);
		} else {
			rdbtnmntmOpenstreetmapmapnik.setSelected(true);
			uiController.tileManagerOSM_Mapnik();
		}

		// Variant: Starts with u4map
		// rdbtnmntmumap.setSelected(true);
		// uiController.tileManager4UMap();

	}

	/** Creates new form JGPSTrackEdit */
	public JGPSTrackEdit() {
		own = this;
		Configuration.addConfigurationObserver(this);
		MapExtractManager.load();
		International.initialize(Configuration.getProperty("LOCALE"));
		try {
			if (Configuration.getProperty("GUILOOKFEEL").equals("System")) {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} else {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TourPlaner.initConfig();
		initComponents();
		initGPSViews();
		// MapExtractManager.load();
		configurationChanged();
		tracksPanel.setFocusable(true);
		tracksPanel.addKeyListener(this);
	}

	/**
	 * @return the appendUnDo
	 */
	public UnDoManager getAppendUnDo() {
		return appendUnDo;
	}

	public void initGPSViews() {
		db = new Database();
		tracksView = new TracksView(db);
		jTableTracks.setModel(db);
		jTableTracks.setDefaultRenderer(Color.class, new ColorRenderer(true));
		jTableTracks.setDefaultEditor(Color.class, new ColorEditor());
		jTableTracks.setDefaultEditor(String.class, new DefaultCellEditor(
				new JTextField()));
		jTableTracks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableTracks.getSelectionModel().addListSelectionListener(this);
		jTablePoints.setModel(db.getTrackTableModel());
		jTablePoints.setDefaultEditor(String.class, new DefaultCellEditor(
				new JTextField()));
		jTablePoints.getSelectionModel().addListSelectionListener(this);
		uiController = UIController.newUIController(db, this);
		tracksPanel = new TracksPanel(tracksView);
		tracksPanel.setPreferredSize(new Dimension(600, 450));
		tracksPanel.addMouseListener(this);
		tracksPanel.addMouseMotionListener(this);
		tracksPanel.addMouseWheelListener(this);
		jPanelMap.add(tracksPanel, java.awt.BorderLayout.CENTER);
		trackPanel = new TrackPanel();
		trackDataPanel = new TrackDataPanel();
		altitudeProfilePanel = new AltitudeProfilePanel();
		trackPanel.add(trackDataPanel, java.awt.BorderLayout.NORTH);
		trackPanel.add(altitudeProfilePanel, java.awt.BorderLayout.CENTER);
		jSplitPaneTrackDetail.setLeftComponent(trackPanel);
		pack();

		Transform.setScreenDimension(getTracksPanel().getWidth(),
				getTracksPanel().getHeight());
		getTracksPanel()
				.zoom(new MapExtract("XY", 9, "13.336978", "47.038977"));
		if (Configuration.getBooleanProperty("SHOW_MAP_ON_STARTUP")) {
			Transform.setScreenDimension(getTracksPanel().getWidth(),
					getTracksPanel().getHeight());
			if (Configuration.getBooleanProperty("COUNTRY_SPECIFIC_MAP")) {
				if (MapExtractManager.contains(Configuration.getProperty(
						"LOCALE").substring(3))) {
					getTracksPanel().zoom(
							MapExtractManager.get(Configuration.getProperty(
									"LOCALE").substring(3)));
				}
			} else if (Configuration.getBooleanProperty("LAST_MAP_EXTRACT")) {
				getTracksPanel()
						.zoom(MapExtractManager.get("LAST_MAP_EXTRACT"));
			} else {
				if (MapExtractManager.contains(Configuration
						.getProperty("MAPEXTRACT"))) {
					getTracksPanel().zoom(
							MapExtractManager.get(Configuration
									.getProperty("MAPEXTRACT")));
				}

			}
			setJGPSTrackEditVariant();
		}

		popupTracksView.add(mntmDeleteP);
		popupTracksView.add(chckbxmntmDeleteModeP);
		popupTracksView.add(mntmShortCutP);
		popupTracksView.addSeparator();
		popupTracksView.add(mntmMoveSelectedPointP);
		popupTracksView.add(chckbxmntmMoveselectedpointmodeP);
		popupTracksView.add(mntmEditPointP);
		popupTracksView.addSeparator();
		popupTracksView.add(chckbxmntmAppendModeP);
		popupTracksView.add(chckbxmntmAppendRoutingModeP);
		popupTracksView.add(mntmUndoAppendP);
		popupTracksView.addSeparator();
		popupTracksView.add(mntmInsertAdjacentPointsP);
		popupTracksView.addSeparator();
		mnuItemReloadTile = new JMenuItem(
				International.getText("menu.kontext.Reload_tile"));
		mnuItemReloadTile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TileManager.getCurrentTileManager().reloadTile(
						Transform.mapLongitude(currentScreenX),
						Transform.mapLatitude(currentScreenY));
				repaint();
			}
		});
		popupTracksView.add(mnuItemReloadTile);

	}

	protected void handleAppendModeChange() {
		if (!appendMode && chckbxmntmAppendMode.isSelected()) {
			if (appendUnDo == null)
				appendUnDo = new UnDoManager(db.getTrackTableModel()
						.getSelectedTrack());
			chckbxmntmAppendRoutingMode.setSelected(false);
			chckbxmntmAppendRoutingModeP.setSelected(false);
			handleAppendRoutingModeChange();
			tracksPanel.addBondPoint(getTracksView().getSelectedTrackView()
					.getTrack().getLastPoint());
			tracksPanel.setShowBonds(true);
			tracksPanel.setCursorText(International.getText("append_point"),
					Color.BLUE);
		}
		if (appendMode && !chckbxmntmAppendMode.isSelected()) {
			tracksPanel.clearBondPoints();
			tracksPanel.setShowBonds(false);
			tracksPanel.setCursorText("", Color.BLUE);
			repaint();
		}
		appendMode = chckbxmntmAppendMode.isSelected();
		if (!appendMode && !appendRoutingMode && appendUnDo != null) {
			appendUnDo = null;
		}
	}

	protected void handleAppendRoutingModeChange() {
		if (!appendRoutingMode && chckbxmntmAppendRoutingMode.isSelected()) {
			chckbxmntmAppendMode.setSelected(false);
			chckbxmntmAppendModeP.setSelected(false);
			handleAppendModeChange();
			tracksPanel.setCursorText(International.getText("append_route"),
					Color.BLUE);
			tracksPanel.setCursor(Cursor
					.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			if (appendUnDo == null)
				appendUnDo = new UnDoManager(db.getTrackTableModel()
						.getSelectedTrack());
		}
		if (appendRoutingMode && !chckbxmntmAppendRoutingMode.isSelected()) {
			tracksPanel.setCursorText("", Color.BLUE);
		}
		appendRoutingMode = chckbxmntmAppendRoutingMode.isSelected();
		if (!appendMode && !appendRoutingMode && appendUnDo != null) {
			appendUnDo = null;
		}
	}

	protected void handleNewTrack() {
		Track track = uiController.newTrack();
		if (track != null) {
			track.setLeftUpperBoundary(Transform.getUpperLeftBoundary());
			track.setRightLowerBoundary(Transform.getLowerRightBoundary());
			track.add(Transform.mapLongitude(lastScreenX),
					Transform.mapLatitude(lastScreenY));
			db.addTrack(track);
			db.getTrackTableModel().setSelectedTrack(track);
			getTracksTable().addRowSelectionInterval(db.getTrackNumber() - 1,
					db.getTrackNumber() - 1);
			setSelectedTrack(track);
			setStateMessage("New track " + track.getName() + " created.");
			chckbxmntmAppendMode.setSelected(true);
			handleAppendModeChange();
			repaint();
		}

	}

	protected void handleConfiguration() {
		ConfigurationDialog conf = new ConfigurationDialog(own);
		conf.initialize();
		conf.setVisible(true);

	}

	public void handleHelp() {
		DlgHelp dialog = new DlgHelp();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	public void handleDistanceMeasurementChange() {
		distanceMeasurement = chckbxmntmDistanceMeasurement.isSelected();
		if (distanceMeasurement) {
			distanceMeasurementFirstPoint = new Point(
					Transform.mapLongitude(lastScreenX),
					Transform.mapLatitude(lastScreenY), 0);
			tracksPanel.addBondPoint(distanceMeasurementFirstPoint);
			tracksPanel.setShowBonds(true);
			tracksPanel.setCursorText("0.000km", Color.BLUE);
		} else {
			tracksPanel.clearBondPoints();
			tracksPanel.setShowBonds(false);
			tracksPanel.setCursorText("", Color.BLACK);
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanelstatusBar = new javax.swing.JPanel();
		jTextFieldStateMessage = new javax.swing.JTextField();
		jSplitPaneTrack = new javax.swing.JSplitPane();
		jSplitPaneTrackDetail = new javax.swing.JSplitPane();
		jSplitPaneMap = new javax.swing.JSplitPane(
				javax.swing.JSplitPane.VERTICAL_SPLIT);
		jScrollPaneTracksTable = new javax.swing.JScrollPane();
		jScrollPaneTracksTable.setPreferredSize(new Dimension(200, 140));
		jScrollPanePointsTable = new javax.swing.JScrollPane();
		jScrollPanePointsTable.setPreferredSize(new Dimension(150, 140));
		jTableTracks = new javax.swing.JTable();
		jTablePoints = new javax.swing.JTable();
		jPanelMap = new javax.swing.JPanel();
		jButtonNorth = new javax.swing.JButton();
		jButtonSouth = new javax.swing.JButton();
		jButtonWest = new javax.swing.JButton();
		jButtonEast = new javax.swing.JButton();
		popupTracksView = new JPopupMenu();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openMenuItem = new javax.swing.JMenuItem();
		saveMenuItem = new javax.swing.JMenuItem();
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.save();
			}
		});
		saveAsMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		trackMenu = new javax.swing.JMenu();
		jMenuItemReverse = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		contentsMenuItem = new javax.swing.JMenuItem();
		contentsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleHelp();
			}
		});
		aboutMenuItem = new javax.swing.JMenuItem();
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleAbout();
			}
		});

		// setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				// Perhaps ask user if they want to save any unsaved files
				// first.
				exitMenuItemActionPerformed(null);
				/*
				 * if (db.isModified()) {# if
				 * (JOptionPane.showConfirmDialog(own,
				 * International.getText("exit_anyway"),
				 * International.getText("Unsaved_Tracks"),
				 * JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;
				 * } MapExtractManager.add("LAST_MAP_EXTRACT",
				 * Transform.getZoomLevel(), Transform.getUpperLeftBoundary());
				 * MapExtractManager.save(); System.exit(0);
				 */
			}
		});
		setTitle("JGPSTrackEdit");

		// jPanelToolbar.setLayout(new java.awt.FlowLayout(
		// java.awt.FlowLayout.LEFT, 0, 0));
		JToolBar toolBar = new JToolBar();
		toolBar.setBorder(new LineBorder(new Color(0, 0, 0)));

		getContentPane().add(toolBar, java.awt.BorderLayout.NORTH);

		toolBar.setFloatable(false);
		jButtonOpenTrack = new javax.swing.JButton();
		jButtonOpenTrack.setBorder(null);
		toolBar.add(jButtonOpenTrack);
		jButtonOpenTrack.setPreferredSize(new Dimension(20, 20));
		jButtonOpenTrack.setMaximumSize(new Dimension(20, 20));
		jButtonOpenTrack.setMinimumSize(new Dimension(20, 20));
		jButtonOpenTrack.setContentAreaFilled(false);
		jButtonOpenTrack.setToolTipText(International.getText("Open_Track"));
		jButtonOpenTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/folder.png")));

		JButton btnOpenGpsiesTrack = new JButton("");
		btnOpenGpsiesTrack.setBorder(null);
		btnOpenGpsiesTrack.setMaximumSize(new Dimension(20, 20));
		btnOpenGpsiesTrack.setMinimumSize(new Dimension(20, 20));
		btnOpenGpsiesTrack.setContentAreaFilled(false);
		btnOpenGpsiesTrack.setPreferredSize(new Dimension(20, 20));
		btnOpenGpsiesTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/folder_find.png")));
		btnOpenGpsiesTrack.setToolTipText(International
				.getText("Open_GPSies_Track"));
		btnOpenGpsiesTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.openGPSies();
			}
		});
		toolBar.add(btnOpenGpsiesTrack);

		JButton btnNewButton = new JButton("");
		btnNewButton.setBorder(null);
		btnNewButton.setMinimumSize(new Dimension(20, 20));
		btnNewButton.setMaximumSize(new Dimension(20, 20));
		btnNewButton.setPreferredSize(new Dimension(20, 20));
		btnNewButton.setToolTipText(International.getText("Save_As"));
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/page_save.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAsMenuItemActionPerformed(e);
			}
		});

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.openDirectory();
			}
		});
		btnNewButton_1.setBorder(null);
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setMaximumSize(new Dimension(20, 20));
		btnNewButton_1.setMinimumSize(new Dimension(20, 20));
		btnNewButton_1.setPreferredSize(new Dimension(20, 20));
		btnNewButton_1.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/folder_table.png")));
		btnNewButton_1.setToolTipText(International.getText("sel_dir"));
		toolBar.add(btnNewButton_1);
		toolBar.addSeparator();
		jButtonSave = new javax.swing.JButton();
		jButtonSave.setBorder(null);
		jButtonSave.setMaximumSize(new Dimension(20, 20));
		jButtonSave.setMinimumSize(new Dimension(20, 20));
		jButtonSave.setToolTipText(International.getText("Save_Track"));
		jButtonSave.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/disk.png")));
		jButtonSave.setPreferredSize(new Dimension(20, 20));
		jButtonSave.setContentAreaFilled(false);
		jButtonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.save();
			}
		});
		toolBar.add(jButtonSave);
		toolBar.add(btnNewButton);

		btnConfiguration = new JButton("");
		btnConfiguration.setBorder(null);
		btnConfiguration.setMinimumSize(new Dimension(20, 20));
		btnConfiguration.setMaximumSize(new Dimension(20, 20));
		btnConfiguration.setContentAreaFilled(false);
		btnConfiguration.setPreferredSize(new Dimension(20, 20));
		btnConfiguration.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/wrench.png")));
		btnConfiguration.setToolTipText("Configuration");
		btnConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleConfiguration();
			}
		});

		toolBar.addSeparator();
		JButton btnZoomIn = new JButton("");
		btnZoomIn.setBorder(null);
		btnZoomIn.setContentAreaFilled(false);
		btnZoomIn.setMaximumSize(new Dimension(20, 20));
		btnZoomIn.setMinimumSize(new Dimension(20, 20));
		btnZoomIn.setPreferredSize(new Dimension(20, 20));
		btnZoomIn.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/zoom_in.png")));
		btnZoomIn.setToolTipText(International.getText("Zoom_In"));
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				uiController.zoomIn();
			}
		});

		btnNewTrack = new JButton("");
		btnNewTrack.setBorder(null);
		btnNewTrack.setContentAreaFilled(false);
		btnNewTrack.setMaximumSize(new Dimension(20, 20));
		btnNewTrack.setMinimumSize(new Dimension(20, 20));
		btnNewTrack.setPreferredSize(new Dimension(20, 20));
		btnNewTrack.setToolTipText(International.getText("New_Track"));
		btnNewTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/map_add.png")));
		btnNewTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleNewTrack();
			}
		});
		toolBar.add(btnNewTrack);

		btnReverseTrack = new JButton("");
		btnReverseTrack.setBorder(null);
		btnReverseTrack.setContentAreaFilled(false);
		btnReverseTrack.setMaximumSize(new Dimension(20, 20));
		btnReverseTrack.setMinimumSize(new Dimension(20, 20));
		btnReverseTrack.setPreferredSize(new Dimension(20, 20));
		btnReverseTrack.setToolTipText(International.getText("Reverse_Track"));
		btnReverseTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_refresh.png")));
		btnReverseTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.reverseTrack();
			}
		});
		toolBar.add(btnReverseTrack);

		btnSplitTrack = new JButton("");
		btnSplitTrack.setMaximumSize(new Dimension(20, 20));
		btnSplitTrack.setMinimumSize(new Dimension(20, 20));
		btnSplitTrack.setPreferredSize(new Dimension(20, 20));
		btnSplitTrack.setToolTipText(International.getText("Split_Track"));
		btnSplitTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_divide.png")));
		btnSplitTrack.setContentAreaFilled(false);
		btnSplitTrack.setBorder(null);
		btnSplitTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSplit();
			}
		});
		toolBar.add(btnSplitTrack);

		btnMergeTrack = new JButton("");
		btnMergeTrack.setBorder(null);
		btnMergeTrack.setContentAreaFilled(false);
		btnMergeTrack.setMaximumSize(new Dimension(20, 20));
		btnMergeTrack.setMinimumSize(new Dimension(20, 20));
		btnMergeTrack.setPreferredSize(new Dimension(20, 20));
		btnMergeTrack.setToolTipText(International.getText("Merge_Track"));
		btnMergeTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_join.png")));
		btnMergeTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleMerge();
			}
		});
		toolBar.add(btnMergeTrack);

		btnCompressTrack = new JButton("");
		btnCompressTrack.setMaximumSize(new Dimension(20, 20));
		btnCompressTrack.setMinimumSize(new Dimension(20, 20));
		btnCompressTrack.setPreferredSize(new Dimension(20, 20));
		btnCompressTrack
				.setToolTipText(International.getText("Compress_Track"));
		btnCompressTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_in.png")));
		btnCompressTrack.setContentAreaFilled(false);
		btnCompressTrack.setBorder(null);
		btnCompressTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.compress();
			}
		});
		toolBar.add(btnCompressTrack);

		btnUpdateElevations = new JButton("");
		btnUpdateElevations.setMaximumSize(new Dimension(20, 20));
		btnUpdateElevations.setMinimumSize(new Dimension(20, 20));
		btnUpdateElevations.setPreferredSize(new Dimension(20, 20));
		btnUpdateElevations.setToolTipText(International
				.getText("Update_Elevations"));
		btnUpdateElevations.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/transmit_edit.png")));
		btnUpdateElevations.setContentAreaFilled(false);
		btnUpdateElevations.setBorder(null);
		btnUpdateElevations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.updateElevation();
			}
		});
		toolBar.add(btnUpdateElevations);
		toolBar.addSeparator();

		btnMoveSelectedPoint = new JButton("");
		btnMoveSelectedPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleMoveSelectedPoint();
			}
		});
		btnMoveSelectedPoint.setBorder(null);
		btnMoveSelectedPoint.setContentAreaFilled(false);
		btnMoveSelectedPoint.setMaximumSize(new Dimension(20, 20));
		btnMoveSelectedPoint.setMinimumSize(new Dimension(20, 20));
		btnMoveSelectedPoint.setPreferredSize(new Dimension(20, 20));
		btnMoveSelectedPoint.setToolTipText(International
				.getText("Move_selected_point"));
		btnMoveSelectedPoint.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/anchor.png")));
		toolBar.add(btnMoveSelectedPoint);

		btnAppendMode = new JButton("");
		btnAppendMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxmntmAppendMode.setSelected(!chckbxmntmAppendMode
						.isSelected());
				chckbxmntmAppendModeP.setSelected(chckbxmntmAppendMode
						.isSelected());
				handleAppendModeChange();
			}
		});
		btnAppendMode.setBorder(null);
		btnAppendMode.setContentAreaFilled(false);
		btnAppendMode.setMaximumSize(new Dimension(20, 20));
		btnAppendMode.setMinimumSize(new Dimension(20, 20));
		btnAppendMode.setPreferredSize(new Dimension(20, 20));
		btnAppendMode.setToolTipText(International.getText("Append_mode"));
		btnAppendMode.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/map_edit.png")));
		toolBar.add(btnAppendMode);

		btnAppendRoutingMode = new JButton("");
		btnAppendRoutingMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxmntmAppendRoutingMode
						.setSelected(!chckbxmntmAppendRoutingMode.isSelected());
				chckbxmntmAppendRoutingModeP
						.setSelected(chckbxmntmAppendRoutingMode.isSelected());
				handleAppendRoutingModeChange();

			}
		});
		btnAppendRoutingMode.setBorder(null);
		btnAppendRoutingMode.setContentAreaFilled(false);
		btnAppendRoutingMode.setMaximumSize(new Dimension(20, 20));
		btnAppendRoutingMode.setMinimumSize(new Dimension(20, 20));
		btnAppendRoutingMode.setPreferredSize(new Dimension(20, 20));
		btnAppendRoutingMode.setToolTipText(International
				.getText("Append_routing_mode"));
		btnAppendRoutingMode.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/map_go.png")));
		toolBar.add(btnAppendRoutingMode);

		btnUndoAppends = new JButton("");
		btnUndoAppends.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.undoAppend();
			}
		});
		btnUndoAppends.setBorder(null);
		btnUndoAppends.setContentAreaFilled(false);
		btnUndoAppends.setMaximumSize(new Dimension(20, 20));
		btnUndoAppends.setMinimumSize(new Dimension(20, 20));
		btnUndoAppends.setPreferredSize(new Dimension(20, 20));
		btnUndoAppends.setToolTipText(International.getText("Undo_appends"));
		btnUndoAppends.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_undo.png")));
		toolBar.add(btnUndoAppends);

		btnInsertAdjacentPoints = new JButton("");
		btnInsertAdjacentPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.insertAdjacentPoints();
			}
		});
		btnInsertAdjacentPoints.setMaximumSize(new Dimension(20, 20));
		btnInsertAdjacentPoints.setMinimumSize(new Dimension(20, 20));
		btnInsertAdjacentPoints.setPreferredSize(new Dimension(20, 20));
		btnInsertAdjacentPoints.setToolTipText(International
				.getText("Insert_adjacent_points"));
		btnInsertAdjacentPoints.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/vector_add.png")));
		btnInsertAdjacentPoints.setContentAreaFilled(false);
		btnInsertAdjacentPoints.setBorder(null);
		toolBar.add(btnInsertAdjacentPoints);

		btnDeletePoint = new JButton("");
		btnDeletePoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.deleteSelectedPoint();
			}
		});
		btnDeletePoint.setBorder(null);
		btnDeletePoint.setContentAreaFilled(false);
		btnDeletePoint.setMaximumSize(new Dimension(20, 20));
		btnDeletePoint.setMinimumSize(new Dimension(20, 20));
		btnDeletePoint.setPreferredSize(new Dimension(20, 20));
		btnDeletePoint.setToolTipText(International.getText("Delete_Point"));
		btnDeletePoint.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/delete.png")));
		toolBar.add(btnDeletePoint);

		btnDeleteMode = new JButton("");
		btnDeleteMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxmntmDeleteMode.setSelected(!chckbxmntmDeleteMode
						.isSelected());
				handleDeleteMode();
			}
		});
		btnDeleteMode.setMaximumSize(new Dimension(20, 20));
		btnDeleteMode.setMinimumSize(new Dimension(20, 20));
		btnDeleteMode.setPreferredSize(new Dimension(20, 20));
		btnDeleteMode.setToolTipText(International.getText("Delete_mode"));
		btnDeleteMode.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/vector_delete.png")));
		btnDeleteMode.setContentAreaFilled(false);
		btnDeleteMode.setBorder(null);
		toolBar.add(btnDeleteMode);

		btnShortCut = new JButton("");
		btnShortCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleShortCut();
			}
		});
		btnShortCut.setMaximumSize(new Dimension(16, 16));
		btnShortCut.setMinimumSize(new Dimension(16, 16));
		btnShortCut.setPreferredSize(new Dimension(16, 16));
		btnShortCut.setToolTipText(International.getText("Short_cut"));
		btnShortCut.setContentAreaFilled(false);
		btnShortCut.setBorder(null);
		btnShortCut.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/cut_red.png")));
		toolBar.add(btnShortCut);
		toolBar.addSeparator();
		toolBar.add(btnZoomIn);

		JButton btnZoomOut = new JButton("");
		btnZoomOut.setBorder(null);
		btnZoomOut.setMaximumSize(new Dimension(20, 20));
		btnZoomOut.setMinimumSize(new Dimension(20, 20));
		btnZoomOut.setPreferredSize(new Dimension(20, 20));
		btnZoomOut.setToolTipText(International.getText("Zoom_Out"));
		btnZoomOut.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/zoom_out.png")));
		btnZoomOut.setContentAreaFilled(false);
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.zoomOut();
			}
		});
		toolBar.add(btnZoomOut);

		JButton btnPanNorth = new JButton("");
		btnPanNorth.setBorder(null);
		btnPanNorth.setContentAreaFilled(false);
		btnPanNorth.setPreferredSize(new Dimension(20, 20));
		btnPanNorth.setMinimumSize(new Dimension(20, 20));
		btnPanNorth.setMaximumSize(new Dimension(20, 20));
		btnPanNorth.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_up.png")));
		btnPanNorth.setToolTipText(International.getText("Pan_North"));
		btnPanNorth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButtonNorthActionPerformed(evt);
			}
		});

		JButton btnZoomTrack = new JButton("");
		btnZoomTrack.setContentAreaFilled(false);
		btnZoomTrack.setBorder(null);
		btnZoomTrack.setPreferredSize(new Dimension(20, 20));
		btnZoomTrack.setMaximumSize(new Dimension(20, 20));
		btnZoomTrack.setMinimumSize(new Dimension(20, 20));
		btnZoomTrack.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/map_magnify.png")));
		btnZoomTrack.setToolTipText(International.getText("Zoom_Track"));
		btnZoomTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.zoomSelectedTrack();
			}
		});
		toolBar.add(btnZoomTrack);

		btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.zoomSelectedPoint();
			}
		});
		btnNewButton_2.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/bullet_red.png")));
		btnNewButton_2.setMaximumSize(new Dimension(20, 20));
		btnNewButton_2.setMinimumSize(new Dimension(20, 20));
		btnNewButton_2.setPreferredSize(new Dimension(20, 20));
		btnNewButton_2.setSelectedIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/bullet_red.png")));
		btnNewButton_2.setToolTipText(International
				.getText("zoom_selected_point"));
		btnNewButton_2.setContentAreaFilled(false);
		btnNewButton_2.setBorder(null);
		toolBar.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.zoomMapExtract();
			}
		});
		btnNewButton_3.setBorder(null);
		btnNewButton_3.setContentAreaFilled(false);
		btnNewButton_3.setPreferredSize(new Dimension(20, 20));
		btnNewButton_3.setMaximumSize(new Dimension(20, 20));
		btnNewButton_3.setMinimumSize(new Dimension(20, 20));
		btnNewButton_3.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/photo.png")));
		btnNewButton_3
				.setToolTipText(International.getText("Zoom_map_extract"));
		toolBar.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.saveMapExtract();
			}
		});
		btnNewButton_4.setToolTipText(International
				.getText("Save_current_map_extract"));
		btnNewButton_4.setPreferredSize(new Dimension(20, 20));
		btnNewButton_4.setMinimumSize(new Dimension(20, 20));
		btnNewButton_4.setMaximumSize(new Dimension(20, 20));
		btnNewButton_4.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/photo_add.png")));
		btnNewButton_4.setContentAreaFilled(false);
		btnNewButton_4.setBorder(null);
		toolBar.add(btnNewButton_4);
		toolBar.add(btnPanNorth);

		JButton btnPanEast = new JButton("");
		btnPanEast.setContentAreaFilled(false);
		btnPanEast.setBorder(null);
		btnPanEast.setMaximumSize(new Dimension(20, 20));
		btnPanEast.setMinimumSize(new Dimension(20, 20));
		btnPanEast.setPreferredSize(new Dimension(20, 20));
		btnPanEast.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_right.png")));
		btnPanEast.setToolTipText(International.getText("Pan_East"));
		btnPanEast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonEastActionPerformed(e);
			}
		});
		toolBar.add(btnPanEast);

		JButton btnPanSouth = new JButton("");
		btnPanSouth.setContentAreaFilled(false);
		btnPanSouth.setBorder(null);
		btnPanSouth.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_down.png")));
		btnPanSouth.setMaximumSize(new Dimension(20, 20));
		btnPanSouth.setMinimumSize(new Dimension(20, 20));
		btnPanSouth.setPreferredSize(new Dimension(20, 20));
		btnPanSouth.setToolTipText(International.getText("Pan_South"));
		btnPanSouth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSouthActionPerformed(e);
			}
		});
		toolBar.add(btnPanSouth);

		JButton btnPanWest = new JButton("");
		btnPanWest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonWestActionPerformed(e);
			}
		});
		btnPanWest.setBorder(null);
		btnPanWest.setMaximumSize(new Dimension(20, 20));
		btnPanWest.setMinimumSize(new Dimension(20, 20));
		btnPanWest.setPreferredSize(new Dimension(20, 20));
		btnPanWest.setToolTipText(International.getText("Pan_West"));
		btnPanWest.setContentAreaFilled(false);
		btnPanWest.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/arrow_left.png")));
		toolBar.add(btnPanWest);
		toolBar.addSeparator();
		toolBar.add(btnConfiguration);

		JButton btnHelp = new JButton("");
		btnHelp.setBorder(null);
		btnHelp.setMinimumSize(new Dimension(20, 20));
		btnHelp.setMaximumSize(new Dimension(20, 20));
		btnHelp.setContentAreaFilled(false);
		btnHelp.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/help.png")));
		btnHelp.setPreferredSize(new Dimension(20, 20));
		btnHelp.setToolTipText(International.getText("Help"));
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleHelp();
			}
		});
		toolBar.addSeparator();
		toolBar.add(btnHelp);

		JButton btnOnlineUserManual = new JButton("");
		btnOnlineUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.userManual();
			}
		});
		btnOnlineUserManual.setMaximumSize(new Dimension(20, 20));
		btnOnlineUserManual.setMinimumSize(new Dimension(20, 20));
		btnOnlineUserManual.setPreferredSize(new Dimension(20, 20));
		btnOnlineUserManual.setToolTipText(International
				.getText("Online_User_Manual"));
		btnOnlineUserManual.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/house.png")));
		btnOnlineUserManual.setContentAreaFilled(false);
		btnOnlineUserManual.setBorder(null);
		toolBar.add(btnOnlineUserManual);

		JButton btnUpdateJgpstrackeditDownload = new JButton("");
		btnUpdateJgpstrackeditDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.updatePage();
			}
		});
		btnUpdateJgpstrackeditDownload.setMaximumSize(new Dimension(20, 20));
		btnUpdateJgpstrackeditDownload.setMinimumSize(new Dimension(20, 20));
		btnUpdateJgpstrackeditDownload.setPreferredSize(new Dimension(20, 20));
		btnUpdateJgpstrackeditDownload.setToolTipText(International
				.getText("Update_JGPSTrackEdit_Download_Page"));
		btnUpdateJgpstrackeditDownload.setContentAreaFilled(false);
		btnUpdateJgpstrackeditDownload.setBorder(null);
		btnUpdateJgpstrackeditDownload
				.setIcon(new ImageIcon(JGPSTrackEdit.class
						.getResource("/jgpstrackedit/view/icon/house_link.png")));
		toolBar.add(btnUpdateJgpstrackeditDownload);

		JButton btnAbout = new JButton("");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAbout();
			}
		});
		btnAbout.setBorder(null);
		btnAbout.setContentAreaFilled(false);
		btnAbout.setMaximumSize(new Dimension(20, 20));
		btnAbout.setMinimumSize(new Dimension(20, 20));
		btnAbout.setPreferredSize(new Dimension(20, 20));
		btnAbout.setToolTipText(International.getText("About"));
		btnAbout.setIcon(new ImageIcon(JGPSTrackEdit.class
				.getResource("/jgpstrackedit/view/icon/information.png")));
		toolBar.add(btnAbout);
		jButtonOpenTrack.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonOpenTrackActionPerformed(evt);
			}
		});

		jPanelstatusBar.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.LEFT, 0, 5));

		jTextFieldStateMessage.setColumns(60);
		jTextFieldStateMessage.setEditable(false);
		jPanelstatusBar.add(jTextFieldStateMessage);

		getContentPane().add(jPanelstatusBar, java.awt.BorderLayout.SOUTH);

		jTableTracks.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, null } }, new String[] { "Title 1",
						"Title 2", "Title 3", "Title 4" }));
		jTablePoints.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, null } }, new String[] { "Title 1",
						"Title 2", "Title 3", "Title 4" }));

		jScrollPaneTracksTable.setViewportView(jTableTracks);
		jScrollPanePointsTable.setViewportView(jTablePoints);

		jSplitPaneTrack.setLeftComponent(jScrollPaneTracksTable);

		jPanelMap.setLayout(new java.awt.BorderLayout());

		jButtonNorth.setText(International.getText("North"));
		jButtonNorth.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonNorthActionPerformed(evt);
			}
		});
		jPanelMap.add(jButtonNorth, java.awt.BorderLayout.NORTH);

		jButtonSouth.setText(International.getText("South"));
		jButtonSouth.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonSouthActionPerformed(evt);
			}
		});
		jPanelMap.add(jButtonSouth, java.awt.BorderLayout.SOUTH);

		jButtonWest.setText(International.getText("West"));
		jButtonWest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonWestActionPerformed(evt);
			}
		});
		jPanelMap.add(jButtonWest, java.awt.BorderLayout.WEST);

		jButtonEast.setText(International.getText("East"));
		jButtonEast.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonEastActionPerformed(evt);
			}
		});
		jPanelMap.add(jButtonEast, java.awt.BorderLayout.EAST);

		jSplitPaneTrack.setRightComponent(jSplitPaneTrackDetail);
		jSplitPaneTrackDetail.setRightComponent(jScrollPanePointsTable);

		jSplitPaneMap.setTopComponent(jSplitPaneTrack);
		jSplitPaneMap.setBottomComponent(jPanelMap);

		getContentPane().add(jSplitPaneMap, java.awt.BorderLayout.CENTER);

		fileMenu.setText(International.getText("menu.File"));
		fileMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fileMenuActionPerformed(evt);
			}
		});

		openMenuItem.setText(International.getText("Open_Track") + "...");
		openMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(openMenuItem);

		mntmOpenGpsiescomTrack = new JMenuItem(
				International.getText("menu.File.Open_GPSies.com") + "...");
		mntmOpenGpsiescomTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.openGPSies();
			}
		});
		fileMenu.add(mntmOpenGpsiescomTrack);

		JMenuItem mntmOpenDirectory = new JMenuItem(
				International.getText("menu.File.Open_Directory") + "...");
		mntmOpenDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.openDirectory();
			}
		});
		fileMenu.add(mntmOpenDirectory);
		fileMenu.addSeparator();
		saveMenuItem.setText(International.getText("menu.File.Save"));
		fileMenu.add(saveMenuItem);

		saveAsMenuItem.setText(International.getText("menu.File.Save_As")
				+ "...");
		saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAsMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveAsMenuItem);

		exitMenuItem.setText(International.getText("menu.File.Exit"));
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});

		mntmConfiguration = new JMenuItem(
				International.getText("menu.File.Configuration") + "...");
		mntmConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleConfiguration();
			}
		});

		mntmDelete_1 = new JMenuItem(International.getText("menu.File.Close"));
		mntmDelete_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.delete();
			}
		});

		mntmSaveToGpsiescom = new JMenuItem(International.getText("menu.File.Save_GPSies.com") + "...");
		mntmSaveToGpsiescom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.saveGPSies();
			}
		});
		fileMenu.add(mntmSaveToGpsiescom);
		fileMenu.add(mntmDelete_1);
		fileMenu.addSeparator();
		
		mntmSaveMapView = new JMenuItem(International.getText("menu.File.Save_Map_View_as_Image") + "...");
		mntmSaveMapView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mntmSaveMapViewActionPerformed(arg0);
			}
		});
		fileMenu.add(mntmSaveMapView);
		
		mntmSaveAltitudeProfile = new JMenuItem(International.getText("menu.File.Save_Altitude_Profile_as_Image") + "...");
		mntmSaveAltitudeProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mntmSaveAltitudeProfileActionPerformed(arg0);
			}
		});
		fileMenu.add(mntmSaveAltitudeProfile);
		fileMenu.addSeparator();
		fileMenu.add(mntmConfiguration);
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		trackMenu.setText(International.getText("menu.Track"));

		jMenuItemReverse.setText(International.getText("menu.Track.Reverse"));
		jMenuItemReverse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemReverseActionPerformed(evt);
			}
		});

		mntmNew = new JMenuItem(International.getText("menu.Track.New"));
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleNewTrack();
			}
		});
		trackMenu.add(mntmNew);
		trackMenu.addSeparator();
		trackMenu.add(jMenuItemReverse);

		menuBar.add(trackMenu);

		mntmSplit = new JMenuItem(International.getText("menu.Track.Split")
				+ "...");
		mntmSplit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSplit();

			}
		});
		trackMenu.add(mntmSplit);

		mntmMerge = new JMenuItem(International.getText("menu.Track.Merge")
				+ "...");
		mntmMerge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleMerge();

			}
		});
		trackMenu.add(mntmMerge);
		trackMenu.addSeparator();

		JMenuItem mntmUpdateElevation = new JMenuItem(
				International.getText("menu.Track.Update_Elevation"));
		mntmUpdateElevation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.updateElevation();
			}
		});

		mntmCompress = new JMenuItem(
				International.getText("menu.Track.Compress") + "...");
		mntmCompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.compress();
			}
		});
		trackMenu.add(mntmCompress);
		trackMenu.add(mntmUpdateElevation);

		JMenuItem mntmCorrectPoints = new JMenuItem(
				International.getText("menu.Track.Correct_points"));
		mntmCorrectPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.correctSelectedTrack();
			}
		});
		trackMenu.add(mntmCorrectPoints);

		mntmRemoveInvalidPoints = new JMenuItem(
				International.getText("menu.Track.Remove_invalid_points"));
		mntmRemoveInvalidPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.correctZeroPoints();
			}
		});
		trackMenu.add(mntmRemoveInvalidPoints);

		mnPoints = new JMenu(International.getText("menu.Point"));
		menuBar.add(mnPoints);

		mntmDelete = new JMenuItem(International.getText("menu.Point.Delete"));
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.deleteSelectedPoint();
			}
		});

		mntmDeleteP = new JMenuItem(International.getText("menu.Point.Delete"));
		mntmDeleteP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.deleteSelectedPoint();
			}
		});

		mntmInsertAdjacentPoints = new JMenuItem(
				International.getText("menu.Point.Insert_adjacent_points"));
		mntmInsertAdjacentPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.insertAdjacentPoints();
			}
		});
		mntmInsertAdjacentPointsP = new JMenuItem(
				International.getText("menu.Point.Insert_adjacent_points"));
		mntmInsertAdjacentPointsP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.insertAdjacentPoints();
			}
		});

		mntmMoveSelectedPoint = new JMenuItem(
				International.getText("menu.Point.Move_selected_point"));
		mntmMoveSelectedPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleMoveSelectedPoint();
			}
		});
		mntmMoveSelectedPointP = new JMenuItem(
				International.getText("menu.Point.Move_selected_point"));
		mntmMoveSelectedPointP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleMoveSelectedPoint();
			}
		});
		mnPoints.add(mntmMoveSelectedPoint);

		chckbxmntmMoveselectedpointmode = new JCheckBoxMenuItem(
				International.getText("menu.Point.Move_selected_point_mode"));
		chckbxmntmMoveselectedpointmode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleMoveSelectedPointMode(chckbxmntmMoveselectedpointmode
						.isSelected());
			}
		});
		mnPoints.add(chckbxmntmMoveselectedpointmode);
		mnPoints.addSeparator();

		chckbxmntmMoveselectedpointmodeP = new JCheckBoxMenuItem(
				International.getText("menu.Point.Move_selected_point_mode"));
		chckbxmntmMoveselectedpointmodeP
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleMoveSelectedPointMode(chckbxmntmMoveselectedpointmodeP
								.isSelected());
					}
				});

		chckbxmntmAppendMode = new JCheckBoxMenuItem(
				International.getText("menu.Point.Append_mode"));
		chckbxmntmAppendMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxmntmAppendModeP.setSelected(chckbxmntmAppendMode
						.isSelected());
				handleAppendModeChange();
			}
		});
		chckbxmntmAppendModeP = new JCheckBoxMenuItem(
				International.getText("menu.Point.Append_mode"));
		chckbxmntmAppendModeP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxmntmAppendMode.setSelected(chckbxmntmAppendModeP
						.isSelected());
				handleAppendModeChange();
			}
		});
		mnPoints.add(chckbxmntmAppendMode);

		chckbxmntmAppendRoutingMode = new JCheckBoxMenuItem(
				International.getText("menu.Point.Append_routing_mode"));
		chckbxmntmAppendRoutingMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxmntmAppendRoutingModeP
						.setSelected(chckbxmntmAppendRoutingMode.isSelected());
				handleAppendRoutingModeChange();
			}
		});
		chckbxmntmAppendRoutingModeP = new JCheckBoxMenuItem(
				International.getText("menu.Point.Append_routing_mode"));
		chckbxmntmAppendRoutingModeP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxmntmAppendRoutingMode
						.setSelected(chckbxmntmAppendRoutingModeP.isSelected());
				handleAppendRoutingModeChange();
			}
		});
		mnPoints.add(chckbxmntmAppendRoutingMode);

		mntmUndoAppend = new JMenuItem(
				International.getText("menu.Point.Undo_append"));
		mntmUndoAppend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.undoAppend();
				repaint();
			}
		});
		mntmUndoAppendP = new JMenuItem(
				International.getText("menu.Point.Undo_append"));
		mntmUndoAppendP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.undoAppend();
				repaint();
			}
		});
		mnPoints.add(mntmUndoAppend);
		mnPoints.add(mntmInsertAdjacentPoints);
		mnPoints.addSeparator();
		mnPoints.add(mntmDelete);

		chckbxmntmDeleteMode = new JCheckBoxMenuItem(
				International.getText("menu.Point.Delete_mode"));
		chckbxmntmDeleteMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleDeleteMode();
			}
		});
		chckbxmntmDeleteModeP = new JCheckBoxMenuItem(
				International.getText("menu.Point.Delete_mode"));
		chckbxmntmDeleteModeP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pointDeleteMode = chckbxmntmDeleteModeP.getState();
				tracksPanel.setPointDeleteMode(chckbxmntmDeleteModeP.getState());
				repaint();
			}
		});
		mnPoints.add(chckbxmntmDeleteMode);

		mntmShortCut = new JMenuItem(
				International.getText("menu.Point.Short_Cut"));
		mntmShortCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleShortCut();
			}
		});
		mnPoints.add(mntmShortCut);

		mntmEditPointP = new JMenuItem("Edit Point");
		mntmEditPointP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmEditPointActionPerformed(e);
			}
		});

		mntmEditPoint = new JMenuItem("Edit Point");
		mntmEditPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmEditPointActionPerformed(e);
			}
		});
		mnPoints.addSeparator();
		mnPoints.add(mntmEditPoint);

		mntmShortCutP = new JMenuItem(
				International.getText("menu.Point.Short_Cut"));
		mntmShortCutP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleShortCut();
			}
		});

		mnView = new JMenu(International.getText("menu.View"));
		menuBar.add(mnView);

		mntmZoomIn = new JMenuItem(International.getText("menu.View.Zoom_In"));
		mntmZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.zoomIn();
			}
		});
		mnView.add(mntmZoomIn);

		mntmZoomOut = new JMenuItem(International.getText("menu.View.Zoom_out"));
		mntmZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.zoomOut();
			}
		});
		mnView.add(mntmZoomOut);

		mntmZoomSelectedTrack = new JMenuItem(
				International.getText("menu.View.Zoom_selected_track"));
		mntmZoomSelectedTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.zoomSelectedTrack();
			}
		});
		mnView.add(mntmZoomSelectedTrack);

		JMenuItem mntmZoomSelectedPoint = new JMenuItem(
				International.getText("menu.View.Zoom_selected_point"));
		mntmZoomSelectedPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.zoomSelectedPoint();
			}
		});
		mnView.add(mntmZoomSelectedPoint);

		JMenuItem mntmZoomMapExtract = new JMenuItem(
				International.getText("menu.View.Zoom_map_extract") + "...");
		mntmZoomMapExtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.zoomMapExtract();
			}
		});
		mnView.add(mntmZoomMapExtract);

		JMenuItem mntmSaveCurrentMap = new JMenuItem(
				International.getText("menu.View.Save_current_map_extract")
						+ "...");
		mntmSaveCurrentMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.saveMapExtract();
			}
		});
		mnView.add(mntmSaveCurrentMap);
		mnView.addSeparator();

		mnMaps = new JMenu(International.getText("menu.View.Maps"));
		mnView.add(mnMaps);

		rdbtnmntmNone = new JRadioButtonMenuItem(
				International.getText("menu.View.Maps.None"));
		rdbtnmntmNone.setSelected(true);
		mapRadioButtons.add(rdbtnmntmNone);
		rdbtnmntmNone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// No Map should be used
				uiController.tileManagerNone();
				repaint();

			}
		});
		mnMaps.add(rdbtnmntmNone);

		rdbtnmntmOpenstreetmapmapnik = new JRadioButtonMenuItem(
				"OpenStreetMap (Mapnik)");
		mapRadioButtons.add(rdbtnmntmOpenstreetmapmapnik);
		rdbtnmntmOpenstreetmapmapnik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// OSM (Mapnik) should be used
				uiController.tileManagerOSM_Mapnik();
			}
		});
		mnMaps.add(rdbtnmntmOpenstreetmapmapnik);

		rdbtnmntmOpencyclemap = new JRadioButtonMenuItem("OpenCycleMap");
		mapRadioButtons.add(rdbtnmntmOpencyclemap);
		rdbtnmntmOpencyclemap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.tileManagerOCM();
			}
		});
		mnMaps.add(rdbtnmntmOpencyclemap);

		JRadioButtonMenuItem rdbtnmntmGooglemap = new JRadioButtonMenuItem(
				"GoogleMap");
		mapRadioButtons.add(rdbtnmntmGooglemap);
		rdbtnmntmGooglemap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManagerGoogleMap();
			}
		});

		rdbtnmntmHikebikemap = new JRadioButtonMenuItem("HikeBikeMap");
		mapRadioButtons.add(rdbtnmntmHikebikemap);
		rdbtnmntmHikebikemap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManagerHikeBikeMap();
			}
		});
		mnMaps.add(rdbtnmntmHikebikemap);

		rdbtnmntmumap = new JRadioButtonMenuItem("4UMap");
		mapRadioButtons.add(rdbtnmntmumap);
		rdbtnmntmumap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManager4UMap();
			}
		});
		mnMaps.add(rdbtnmntmumap);
		mnMaps.add(new JSeparator());

		rdbtnmntmMapquest = new JRadioButtonMenuItem("MapQuest");
		rdbtnmntmMapquest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.tileManagerMapQuest();
			}
		});
		mnMaps.add(rdbtnmntmMapquest);

		mapRadioButtons.add(rdbtnmntmMapquest);

		rdbtnmntmMapquestsatellite = new JRadioButtonMenuItem(
				"MapQuest (Satellite)");
		rdbtnmntmMapquestsatellite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManagerMapQuestSat();
			}
		});
		mnMaps.add(rdbtnmntmMapquestsatellite);
		mapRadioButtons.add(rdbtnmntmMapquestsatellite);

		rdbtnmntmMapquesthybride = new JRadioButtonMenuItem(
				"MapQuest (Hybride)");
		rdbtnmntmMapquesthybride.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManagerMapQuestHybride();
			}
		});
		mnMaps.add(rdbtnmntmMapquesthybride);
		mapRadioButtons.add(rdbtnmntmMapquesthybride);

		mnMaps.addSeparator();
		mnMaps.add(rdbtnmntmGooglemap);

		rdbtnmntmGooglemapsatellite = new JRadioButtonMenuItem(
				"GoogleMap (Satellite)");
		mapRadioButtons.add(rdbtnmntmGooglemapsatellite);
		rdbtnmntmGooglemapsatellite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManagerGoogleMapSatellite();
			}
		});
		mnMaps.add(rdbtnmntmGooglemapsatellite);

		rdbtnmntmGooglemaphybrid = new JRadioButtonMenuItem(
				"GoogleMap (Hybrid)");
		mapRadioButtons.add(rdbtnmntmGooglemaphybrid);
		rdbtnmntmGooglemaphybrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManagerGoogleMapHybrid();
			}
		});
		mnMaps.add(rdbtnmntmGooglemaphybrid);

		rdbtnmntmGooglemapterrain = new JRadioButtonMenuItem(
				"GoogleMap (Terrain)");
		mapRadioButtons.add(rdbtnmntmGooglemapterrain);
		rdbtnmntmGooglemapterrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.tileManagerGoogleMapTerrain();
			}
		});
		mnMaps.add(rdbtnmntmGooglemapterrain);
		mnView.addSeparator();

		chckbxmntmShowDayTour = new JCheckBoxMenuItem(
				International.getText("menu.View.Show_day_tour_markers"));
		chckbxmntmShowDayTour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tracksPanel.setShowDayTourMarkers(chckbxmntmShowDayTour
						.isSelected());
				altitudeProfilePanel.setShowDayTourMarkers(chckbxmntmShowDayTour
						.isSelected());
				repaint();
			}
		});
		mnView.add(chckbxmntmShowDayTour);

		mntmRefreshMap = new JMenuItem(
				International.getText("menu.View.Refresh_Map"));
		mntmRefreshMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});

		chckbxmntmShowCoordinates = new JCheckBoxMenuItem(
				International.getText("menu.View.Show_coordinates"));
		chckbxmntmShowCoordinates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tracksPanel.setShowCoordinates(chckbxmntmShowCoordinates
						.isSelected());
				showCoordinatesMode = chckbxmntmShowCoordinates.isSelected();
				repaint();
			}
		});
		mnView.add(chckbxmntmShowCoordinates);

		chckbxmntmDistanceMeasurement = new JCheckBoxMenuItem(
				International.getText("menu.View.Distance_Measurement"));
		chckbxmntmDistanceMeasurement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleDistanceMeasurementChange();
			}
		});

		chckbxmntmPointInformation = new JCheckBoxMenuItem(
				International.getText("menu.View.Point_Information"));
		chckbxmntmPointInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxmntmPointInformationActionPerformed(arg0);
			}
		});
		mnView.add(chckbxmntmPointInformation);
		mnView.add(chckbxmntmDistanceMeasurement);

		chckbxmntmShowTrackLength = new JCheckBoxMenuItem(
				International.getText("menu.View.Show_Track_Lengths"));
		chckbxmntmShowTrackLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxmntmShowTrackLengthActionPerformed(arg0);
			}
		});
		mnView.add(chckbxmntmShowTrackLength);

		chckbxmntmScale = new JCheckBoxMenuItem(
				International.getText("menu.View.Show_Scale"));
		chckbxmntmScale.setSelected(true);
		chckbxmntmScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chckbxmntmScaleActionPerformed(arg0);
			}
		});
		mnView.add(chckbxmntmScale);

		mnView.addSeparator();
		mnView.add(mntmRefreshMap);

		chckbxmntmNewCheckItem = new JCheckBoxMenuItem(
				International.getText("menu.View.Show_Tiles"));
		chckbxmntmNewCheckItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TileManager.getCurrentTileManager().setShowTiles(
						chckbxmntmNewCheckItem.isSelected());
				repaint();
			}
		});

		chckbxmntmAutoRefresh = new JCheckBoxMenuItem(
				International.getText("menu.View.Auto_refresh"));
		chckbxmntmAutoRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tracksPanel.setAutoRefresh(chckbxmntmAutoRefresh.isSelected());
			}
		});
		chckbxmntmAutoRefresh.setSelected(true);
		mnView.add(chckbxmntmAutoRefresh);
		mnView.add(chckbxmntmNewCheckItem);

		helpMenu.setText(International.getText("menu.Help"));

		contentsMenuItem.setText(International.getText("menu.Help.Short_Help"));
		helpMenu.add(contentsMenuItem);

		JMenuItem mntmUserManual = new JMenuItem(
				International.getText("menu.Help.User_Manual"));
		mntmUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uiController.userManual();
			}
		});
		helpMenu.add(mntmUserManual);

		JMenuItem mntmUpdatePage = new JMenuItem(
				International.getText("menu.Help.Update_Page"));
		mntmUpdatePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.updatePage();
			}
		});

		mnTiledownload = new JMenu(
				International.getText("menu.TileDown.Tile_Download"));
		menuBar.add(mnTiledownload);

		mntmStartTileDownload = new JMenuItem(
				International.getText("menu.TileDown.Start_Tile_Download_Mode"));
		mntmStartTileDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mntmStartTileDownloadActionPerformed(arg0);
			}
		});
		mnTiledownload.add(mntmStartTileDownload);
		mnTiledownload.addSeparator();

		mntmAddBorderTiles = new JMenuItem(
				International.getText("menu.TileDown.Add_Border_Tiles"));
		mntmAddBorderTiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmAddBorderTilesActionPerformed(e);
			}
		});
		mnTiledownload.add(mntmAddBorderTiles);

		mntmSaveCurrentWork = new JMenuItem(
				International.getText("menu.TileDown.Save_Current_Work"));
		mntmSaveCurrentWork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmSaveCurrentWorkActionPerformed(e);
			}
		});

		mntmAddCurentMap = new JMenuItem(
				International.getText("menu.TileDown.Add_Current_Map_Extract"));
		mntmAddCurentMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mntmAddCurentMapActionPerformed(arg0);
			}
		});

		mntmAddArea = new JMenuItem(
				International.getText("menu.TileDown.Add_Area"));
		mntmAddArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mntmAddAreaActionPerformed(arg0);
			}
		});
		mnTiledownload.add(mntmAddArea);
		mnTiledownload.add(mntmAddCurentMap);

		mnTiledownload.addSeparator();

		mnTiledownload.add(mntmSaveCurrentWork);

		mntmStopAndSave = new JMenuItem(
				International.getText("menu.TileDown.Save_Download_Exit_Mode"));
		mntmStopAndSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mntmStopAndSaveActionPerformed(arg0);
			}
		});
		mnTiledownload.add(mntmStopAndSave);
		helpMenu.add(mntmUpdatePage);

		aboutMenuItem.setText(International.getText("menu.Help.About"));
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	protected void chckbxmntmScaleActionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		showScale = chckbxmntmScale.isSelected();
		tracksPanel.setShowScale(showScale);
		repaint();
	}

	protected void handleMoveSelectedPointMode(boolean selected) {
		// TODO Auto-generated method stub
		chckbxmntmMoveselectedpointmode.setSelected(selected);
		chckbxmntmMoveselectedpointmodeP.setSelected(selected);
		moveSelectedPointMode = selected;
		if (selected)
			handleMoveSelectedPoint();
	}

	public JTable getPointsTable() {
		return jTablePoints;
	}

	protected void handleAbout() {
		DlgAbout dialog = new DlgAbout();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

	}

	protected void handleDeleteMode() {
		pointDeleteMode = chckbxmntmDeleteMode.getState();
		tracksPanel.setPointDeleteMode(chckbxmntmDeleteMode.getState());
		repaint();

	}

	protected void handleMerge() {
		try {
			if(db.getTracks().size() < 2) {
				return;
			}
			
			if(jTableTracks.getSelectedRow() == -1) {
				return;
			}
			
			DlgMerge dialog = new DlgMerge(db.getTrack(
					jTableTracks.getSelectedRow()).getName(), db.getTracks());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
			dialog.setVisible(true);
			uiController.merge(dialog.getMergeOption(),
					db.getTrack(jTableTracks.getSelectedRow()),
					dialog.getMergeTrack(), dialog.getTrackName());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	protected void handleSplit() {
		try {
			DlgSplit dialog = new DlgSplit(db.getTrack(
					jTableTracks.getSelectedRow()).getName());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
			dialog.setVisible(true);
			uiController.split(dialog.getSplitOption(),
					db.getTrack(jTableTracks.getSelectedRow()),
					dialog.getTrackName(), dialog.getNumberTracks(),
					dialog.getNumberPoints(), dialog.getSplitLength());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	protected void handleShortCut() {
		shortCut = true;
		shortCutStartPoint = getTracksView().getSelectedTrackView()
				.getSelectedPoint();
		tracksPanel.addBondPoint(shortCutStartPoint);
		tracksPanel.setShowBonds(true);
		tracksPanel.setCursorText(International.getText("ct.short_cut"),
				Color.RED);
		// tracksPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	protected void handleMoveSelectedPoint() {
		moveSelectedPoint = true;
		tracksPanel.setShowBonds(true);
		TrackView selectedTrackView = getTracksView().getSelectedTrackView();
		int selectedIndex = selectedTrackView.getSelectedPointIndex();
		if (selectedIndex > 1) {
			tracksPanel.addBondPoint(selectedTrackView.getTrack().getPoint(
					selectedIndex - 1));
		}
		if (selectedIndex < selectedTrackView.getTrack().getNumberPoints() - 1) {
			tracksPanel.addBondPoint(selectedTrackView.getTrack().getPoint(
					selectedIndex + 1));
		}
		tracksPanel
				.setCursorText(International.getText("ct_move"), Color.GREEN);

	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
		if (db.isModified()) {
			if (JOptionPane.showConfirmDialog(this,
					International.getText("exit_anyway"),
					International.getText("Unsaved_Tracks"),
					JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
				return;
		}
		MapExtractManager.add("LAST_MAP_EXTRACT", Transform.getZoomLevel(),
				Transform.getUpperLeftBoundary());
		MapExtractManager.save();
		Configuration.setProperty("MAPTYPE", TileManager
				.getCurrentTileManager().getMapName());
		Configuration.saveProperties();
		System.exit(0);
	}// GEN-LAST:event_exitMenuItemActionPerformed

	private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_fileMenuActionPerformed
		// TODO add your handling code here:

	}// GEN-LAST:event_fileMenuActionPerformed

	private void jButtonOpenTrackActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOpenTrackActionPerformed
		// TODO add your handling code here:
		uiController.openTrack();
	}// GEN-LAST:event_jButtonOpenTrackActionPerformed

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openMenuItemActionPerformed
		// TODO add your handling code here:
		uiController.openTrack();
	}// GEN-LAST:event_openMenuItemActionPerformed

	private void jMenuItemReverseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemReverseActionPerformed
		// TODO add your handling code here:
		uiController.reverseTrack();
	}// GEN-LAST:event_jMenuItemReverseActionPerformed

	private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAsMenuItemActionPerformed
		// TODO add your handling code here:
		uiController.saveAs();
	}// GEN-LAST:event_saveAsMenuItemActionPerformed

	private void jButtonNorthActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNorthActionPerformed
		// TODO add your handling code here:
		uiController.moveNorth();
	}// GEN-LAST:event_jButtonNorthActionPerformed

	private void jButtonSouthActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSouthActionPerformed
		// TODO add your handling code here:
		uiController.moveSouth();
	}// GEN-LAST:event_jButtonSouthActionPerformed

	private void jButtonWestActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonWestActionPerformed
		// TODO add your handling code here:
		uiController.moveWest();
	}// GEN-LAST:event_jButtonWestActionPerformed

	private void jButtonEastActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonEastActionPerformed
		// TODO add your handling code here:
		uiController.moveEast();
	}// GEN-LAST:event_jButtonEastActionPerformed

	public void setStateMessage(String msg) {
		jTextFieldStateMessage.setText(msg);
	}

	public javax.swing.JTable getTracksTable() {
		return jTableTracks;
	}

	public TracksView getTracksView() {
		return tracksView;
	}

	public TracksPanel getTracksPanel() {
		return tracksPanel;
	}

	public Point getSelectedPoint() {
		return tracksView.getSelectedTrackView().getSelectedPoint();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				JGPSTrackEdit w = new JGPSTrackEdit();
				w.setVisible(true);
				if (Configuration.getBooleanProperty("SHOW_HELP_ON_STARTUP")) {
					w.handleHelp();
				}
				w.handleUpdateRequest();
			}
		});
	}

	protected void handleUpdateRequest() {
		// Warning: month are counted from 0! (February == 1)
		GregorianCalendar updateDay = new GregorianCalendar(2016, 6, 27);
		GregorianCalendar today = new GregorianCalendar();
		if (!Configuration.getBooleanProperty("NOUPDATEREQUEST")
				&& updateDay.get(Calendar.YEAR) <= today.get(Calendar.YEAR)
				&& updateDay.get(Calendar.MONTH) <= today.get(Calendar.MONTH)
				&& updateDay.get(Calendar.DAY_OF_MONTH) <= today
						.get(Calendar.DAY_OF_MONTH)) {
			if (JOptionPane.showConfirmDialog(this,
					International.getText("Update_Request_Text"),
					International.getText("Update_request"),
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				uiController.updatePage();
			} else {
				Configuration.setProperty("NOUPDATEREQUEST", "1");
				Configuration.saveProperties();
			}
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JMenuItem contentsMenuItem;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JButton jButtonSave;
	private javax.swing.JButton jButtonEast;
	private javax.swing.JButton jButtonNorth;
	private javax.swing.JButton jButtonOpenTrack;
	private javax.swing.JButton jButtonSouth;
	private javax.swing.JButton jButtonWest;
	private javax.swing.JMenuItem jMenuItemReverse;
	private javax.swing.JPanel jPanelstatusBar;
	private javax.swing.JPanel jPanelMap;
	private javax.swing.JScrollPane jScrollPaneTracksTable;
	private javax.swing.JScrollPane jScrollPanePointsTable;
	private javax.swing.JSplitPane jSplitPaneTrack;
	private javax.swing.JSplitPane jSplitPaneTrackDetail;
	private javax.swing.JSplitPane jSplitPaneMap;
	private javax.swing.JTable jTableTracks;
	private javax.swing.JTable jTablePoints;
	private javax.swing.JTextField jTextFieldStateMessage;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JMenuItem saveMenuItem;
	private javax.swing.JMenu trackMenu;
	private JMenuItem mntmConfiguration;
	private JMenu mnPoints;
	private JMenuItem mntmDelete;
	private JMenuItem mntmDeleteP;
	private JCheckBoxMenuItem chckbxmntmDeleteMode;
	private JCheckBoxMenuItem chckbxmntmDeleteModeP;
	private JMenu mnView;
	private JMenuItem mntmZoomIn;
	private JMenuItem mntmZoomOut;
	private JMenuItem mntmSplit;
	private JMenuItem mntmMerge;
	private JMenuItem mntmDelete_1;
	private JMenu mnMaps;
	private JRadioButtonMenuItem rdbtnmntmNone;
	private JRadioButtonMenuItem rdbtnmntmOpenstreetmapmapnik;
	private JMenuItem mntmZoomSelectedTrack;
	private JMenuItem mntmRefreshMap;
	private JCheckBoxMenuItem chckbxmntmShowDayTour;
	private JMenuItem mntmMoveSelectedPoint;
	private JMenuItem mntmMoveSelectedPointP;
	private JCheckBoxMenuItem chckbxmntmAppendMode;
	private JCheckBoxMenuItem chckbxmntmAppendModeP;
	private JRadioButtonMenuItem rdbtnmntmOpencyclemap;
	private JCheckBoxMenuItem chckbxmntmShowCoordinates;
	private JMenuItem mntmInsertAdjacentPoints;
	private JMenuItem mntmInsertAdjacentPointsP;
	private JMenuItem mntmNew;
	private JButton btnConfiguration;
	private JCheckBoxMenuItem chckbxmntmNewCheckItem;
	private JMenuItem mnuItemReloadTile;
	private JMenuItem mntmOpenGpsiescomTrack;
	private JRadioButtonMenuItem rdbtnmntmHikebikemap;
	private JRadioButtonMenuItem rdbtnmntmMapquest;
	private JCheckBoxMenuItem chckbxmntmAppendRoutingMode;
	private JCheckBoxMenuItem chckbxmntmAppendRoutingModeP;
	private JCheckBoxMenuItem chckbxmntmAutoRefresh;
	private JMenuItem mntmUndoAppend;
	private JMenuItem mntmUndoAppendP;
	private JRadioButtonMenuItem rdbtnmntmMapquestsatellite;
	private JRadioButtonMenuItem rdbtnmntmMapquesthybride;
	private JMenuItem mntmShortCut;
	private JMenuItem mntmShortCutP;
	private JMenuItem mntmRemoveInvalidPoints;
	private JMenuItem mntmCompress;
	private JButton btnNewTrack;
	private JButton btnReverseTrack;
	private JButton btnSplitTrack;
	private JButton btnMergeTrack;
	private JButton btnCompressTrack;
	private JButton btnUpdateElevations;
	private JButton btnMoveSelectedPoint;
	private JButton btnAppendMode;
	private JButton btnAppendRoutingMode;
	private JButton btnUndoAppends;
	private JButton btnInsertAdjacentPoints;
	private JButton btnDeletePoint;
	private JButton btnDeleteMode;
	private JButton btnShortCut;
	private JButton btnNewButton_2;
	private JCheckBoxMenuItem chckbxmntmMoveselectedpointmode;
	private JCheckBoxMenuItem chckbxmntmMoveselectedpointmodeP;
	private JCheckBoxMenuItem chckbxmntmDistanceMeasurement;
	private JRadioButtonMenuItem rdbtnmntmumap;
	private JRadioButtonMenuItem rdbtnmntmGooglemapsatellite;
	private JRadioButtonMenuItem rdbtnmntmGooglemaphybrid;
	private JRadioButtonMenuItem rdbtnmntmGooglemapterrain;
	private JRadioButtonMenuItem rdbtnmntmGooglemap;
	private JMenu mnTiledownload;
	private JMenuItem mntmStartTileDownload;
	private JMenuItem mntmAddBorderTiles;
	private JMenuItem mntmStopAndSave;
	private JMenuItem mntmSaveCurrentWork;
	private JMenuItem mntmAddCurentMap;
	private JMenuItem mntmAddArea;
	private JCheckBoxMenuItem chckbxmntmShowTrackLength;
	private JCheckBoxMenuItem chckbxmntmPointInformation;
	private JMenuItem mntmEditPoint;
	private JMenuItem mntmEditPointP;
	private JMenuItem mntmSaveToGpsiescom;
	private JMenuItem mntmSaveMapView;
	private JMenuItem mntmSaveAltitudeProfile;

	// End of variables declaration//GEN-END:variables

	/**
	 * Sets the selected track in some components. Not for general use!
	 * 
	 * @param track
	 */
	public void setSelectedTrack(Track selectedTrack) {
		tracksView.setSelectedTrack(selectedTrack);
		trackDataPanel.setTrack(selectedTrack);
		altitudeProfilePanel.setTrack(selectedTrack);

	}

	/**
	 * @return the altitudeProfilePanel
	 */
	public AltitudeProfilePanel getAltitudeProfilePanel() {
		return altitudeProfilePanel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jTableTracks.getSelectionModel()) {
			int selectedRow = jTableTracks.getSelectedRow();
			if (selectedRow != -1) {
				db.getTrackTableModel().setSelectedTrack(
						db.getTrack(selectedRow));
				Track selectedTrack = db.getTrack(selectedRow);
				setSelectedTrack(selectedTrack);
				selectedRowTracksTable = selectedRow;
				tracksPanel.repaint();
			} else if (selectedRowTracksTable != -1) {
				jTableTracks.addRowSelectionInterval(selectedRowTracksTable,
						selectedRowTracksTable);
			}
		}
		if (e.getSource() == jTablePoints.getSelectionModel()) {
			int selectedRow = jTablePoints.getSelectedRow();
			if (selectedRow != -1) {
				tracksView.setSelectedPoint(db.getTrackTableModel()
						.getSelectedTrack().getPoint(selectedRow));
				altitudeProfilePanel.setSelectedPoint(db.getTrackTableModel()
						.getSelectedTrack().getPoint(selectedRow));
				tracksPanel.repaint();
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean moveSelectedPointModeStarted = false;
		tracksPanel.requestFocusInWindow();
		int screenX = e.getX();
		int screenY = e.getY();
		currentScreenX = screenX;
		currentScreenY = screenY;
		tracksPanel.setMousePosition(screenX, screenY);
		int selectedPointIndex = -1;
		if (e.getClickCount() == 1) {
		if (tracksView.getSelectedTrackView() != null) {
			PointView selectedPoint = tracksView.getSelectedTrackView()
					.getPointAt(screenX, screenY);
			if (selectedPoint != null) {
				selectedPointIndex = tracksView.getSelectedTrackView()
						.getTrack().indexOf(selectedPoint.getPoint());
				altitudeProfilePanel.setSelectedPoint(selectedPoint.getPoint());
			} else {
				altitudeProfilePanel.setSelectedPoint(null);
			}
		}
		/*
		 * int selectedPointIndex = tracksView.getSelectedTrackView()
		 * .getPointIndexAt(screenX, screenY);
		 */
		if (selectedPointIndex != -1) {
			// Set Row Selection Intervall of jTablePoints....
			jTablePoints.setRowSelectionInterval(selectedPointIndex,
					selectedPointIndex);
			if (moveSelectedPointMode) {
				handleMoveSelectedPoint();
				moveSelectedPointModeStarted = true;
			}
		}
		if (pointDeleteMode) {
			uiController.deleteSelectedPoint();
		}
		if (moveSelectedPoint && !moveSelectedPointModeStarted) {
			moveSelectedPoint = false;
			tracksPanel.setShowBonds(false);
			tracksPanel.clearBondPoints();
			tracksPanel.setCursorText("", Color.BLACK);
			uiController.setSelectedPointPosition(screenX, screenY);
			repaint();
		}
		if (appendMode) {
			uiController.appendPoint(screenX, screenY);
			tracksPanel.clearBondPoints();
			tracksPanel.addBondPoint(getTracksView().getSelectedTrackView()
					.getTrack().getLastPoint());
			repaint();
		}
		if (appendRoutingMode) {
			uiController.appendRoutingPoint(screenX, screenY);
		}
		if (shortCut) {
			tracksPanel.setShowBonds(false);
			tracksPanel.clearBondPoints();
			tracksPanel.setCursorText("", Color.RED);
			uiController.shortCut(shortCutStartPoint, tracksView
					.getSelectedTrackView().getSelectedPoint());
			tracksPanel.setCursor(Cursor.getDefaultCursor());
			shortCut = false;

		}
		if (tileDownload != null) {
			double longitude = Transform.mapLongitude(screenX);
			double latitude = Transform.mapLatitude(screenY);
			if (tileSelectionMode == MODE_WAIT_FIRST_POINT) {
				tileSelectionMode = MODE_WAIT_SECOND_POINT;
				tracksPanel.setCursorText("Define lower right", Color.RED);
				System.out
						.println("tileSelectionMode = MODE_WAIT_SECOND_POINT");
				tileSelectFirstPoint = new Point(longitude, latitude);
				tracksPanel.setRectanglePoint(tileSelectFirstPoint);
			} else if (tileSelectionMode == MODE_WAIT_SECOND_POINT) {
				tileSelectionMode = MODE_INACTIVE;
				tracksPanel.setCursorText("", Color.RED);
				tracksPanel.setRectanglePoint(null);
				tileDownload.addTiles(tileSelectFirstPoint, new Point(
						longitude, latitude));
			} else {
				TileNumber clickedTileNumber = TileNumber.getTileNumber(
						tileDownload.getZoomLevel(), longitude, latitude);
				tileDownload.toggleTile(clickedTileNumber);
			}
			repaint();
		}
		if (distanceMeasurement) {
			tracksPanel.clearBondPoints();
			distanceMeasurementFirstPoint = new Point(
					Transform.mapLongitude(screenX),
					Transform.mapLatitude(screenY), 0);
			tracksPanel.addBondPoint(distanceMeasurementFirstPoint);
			tracksPanel.setCursorText("0.000km", Color.BLUE);
			repaint();
		}
		} else if (e.getClickCount() == 2) {
			// Double Click, select track
			Track selTrack = db.getTrack(Transform.mapPoint(screenX, screenY));
			if (selTrack != null) {
				setSelectedTrack(selTrack);
				int index = db.getTrack(selTrack);
				jTableTracks.setRowSelectionInterval(index, index);
			}
		}

		lastScreenX = screenX;
		lastScreenY = screenY;
		lastDraggedX = -1;
		lastDraggedY = -1;

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		tracksPanel.requestFocusInWindow();

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void maybeShowPopup(MouseEvent e) {
		int screenX = e.getX();
		int screenY = e.getY();
		currentScreenX = screenX;
		currentScreenY = screenY;
		if (e.isPopupTrigger()) {
			popupTracksView.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		int screenX = e.getX();
		int screenY = e.getY();
		currentScreenX = screenX;
		currentScreenY = screenY;
		maybeShowPopup(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		int screenX = e.getX();
		int screenY = e.getY();
		currentScreenX = screenX;
		currentScreenY = screenY;
		lastDraggedX = -1;
		lastDraggedY = -1;
		if (draggingActive) {
			tracksPanel.setCursor(Cursor.getDefaultCursor());
			draggingActive = false;
		}
		maybeShowPopup(e);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		int screenX = e.getX();
		int screenY = e.getY();
		/*
		 * System.out.println("JGPSTRackEdit: mouseDragged screenX=" + screenX +
		 * " screenY=" + screenY + " lastDraggedX=" + lastDraggedX +
		 * " lastDraggedY=" + lastDraggedY);
		 */
		if (lastDraggedX != -1) {
			uiController
					.move((screenX - lastDraggedX)
							/ (double) tracksPanel.getWidth(),
							(lastDraggedY - screenY)
									/ (double) tracksPanel.getHeight());
		}
		lastDraggedX = screenX;
		lastDraggedY = screenY;
		tracksPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		draggingActive = true;
		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		int screenX = e.getX();
		int screenY = e.getY();
		tracksPanel.setMousePosition(screenX, screenY);
		if (distanceMeasurement) {
			distanceMeasurementSecondPoint = new Point(
					Transform.mapLongitude(screenX),
					Transform.mapLatitude(screenY), 0);
			double distance = distanceMeasurementFirstPoint
					.distance(distanceMeasurementSecondPoint);
			tracksPanel.setCursorText(Parser.formatLength(distance) + "km",
					Color.BLUE);

		}

		if (moveSelectedPoint || appendMode || appendRoutingMode
				|| showCoordinatesMode || pointDeleteMode || shortCut
				|| distanceMeasurement || tileSelectionMode > 0) {
			repaint();
		}

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		/*
		 * System.out.println("JGPSTRackEdit: mouseWheelMoved=" +
		 * e.getWheelRotation());
		 */
		if (e.getWheelRotation() < 0) {
			uiController.zoomIn();
		} else {
			uiController.zoomOut();
		}

	}

	@Override
	public void configurationChanged() {
		// TODO Auto-generated method stub
		if (jButtonNorth != null) {
			jButtonNorth.setVisible(Configuration
					.getBooleanProperty("SHOW_DIRECTION_BUTTONS"));
			jButtonSouth.setVisible(Configuration
					.getBooleanProperty("SHOW_DIRECTION_BUTTONS"));
			jButtonEast.setVisible(Configuration
					.getBooleanProperty("SHOW_DIRECTION_BUTTONS"));
			jButtonWest.setVisible(Configuration
					.getBooleanProperty("SHOW_DIRECTION_BUTTONS"));
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		// System.out.println("Key Pressed: " + key.getKeyCode());
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (pointDeleteMode) {
				chckbxmntmDeleteModeP.setState(false);
				chckbxmntmDeleteMode.setState(false);
				pointDeleteMode = false;
				tileSelectionMode = MODE_INACTIVE;
				tracksPanel.setPointDeleteMode(false);
				tracksPanel.setCursor(Cursor.getDefaultCursor());
				repaint();
			}
			if (moveSelectedPoint) {
				moveSelectedPoint = false;
				tracksPanel.setShowBonds(false);
				tracksPanel.clearBondPoints();
				tracksPanel.setCursorText("", Color.BLACK);
				repaint();
			}
			handleMoveSelectedPointMode(false);
			if (shortCut) {
				tracksPanel.setShowBonds(false);
				tracksPanel.clearBondPoints();
				tracksPanel.setCursorText("", Color.RED);
				shortCut = false;
				tracksPanel.setCursor(Cursor.getDefaultCursor());
				repaint();
			}
			if (appendMode) {
				chckbxmntmAppendMode.setSelected(false);
				chckbxmntmAppendModeP.setSelected(false);
				handleAppendModeChange();
				repaint();
			}
			if (appendRoutingMode) {
				chckbxmntmAppendRoutingMode.setSelected(false);
				chckbxmntmAppendRoutingModeP.setSelected(false);
				handleAppendRoutingModeChange();
				tracksPanel.setCursor(Cursor.getDefaultCursor());
				repaint();
			}
			if (distanceMeasurement) {
				chckbxmntmDistanceMeasurement.setSelected(false);
				handleDistanceMeasurementChange();
				repaint();
			}
		}
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			uiController.selectPreviousPoint(true);
		}
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			uiController.selectNextPoint(true);
		}
		if (key.getKeyCode() == KeyEvent.VK_UP) {
			uiController.selectPreviousPoint(false);
		}
		if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			uiController.selectNextPoint(false);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	protected void mntmStartTileDownloadActionPerformed(ActionEvent arg0) {
		DlgStartTiledownloadMode dialog = new DlgStartTiledownloadMode(this,
				Transform.getZoomLevel(), TileManager.getCurrentTileManager()
						.getMaxZoom());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		tileDownload = new TileDownload();
		TileManager.getCurrentTileManager().setTileDownload(tileDownload);
		tileDownload.setZoomLevel(dialog.getDownloadZoom());
		tracksPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		switch (dialog.getResult()) {
		case DlgStartTiledownloadMode.TRACKS:
			tileDownload.addTiles(db.getTracks());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			tracksPanel.setCursor(Cursor
					.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

			break;
		case DlgStartTiledownloadMode.FILE:
			try {
				tileDownload.loadFromFile(dialog.getFilePath());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
				tracksPanel.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error opening file" + " "
						+ dialog.getFilePath(), "File Open Error",
						JOptionPane.ERROR_MESSAGE);
				tileDownload = null;
				TileManager.getCurrentTileManager().setTileDownload(
						tileDownload);
				tracksPanel.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			break;
		case DlgStartTiledownloadMode.EMPTY:
			tracksPanel.setCursor(Cursor
					.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

			break;
		case DlgStartTiledownloadMode.RESULT_CANCEL:
			tileDownload = null;
			TileManager.getCurrentTileManager().setTileDownload(tileDownload);
			tracksPanel.setCursor(Cursor
					.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			break;
		}
		repaint();
	}

	protected void mntmAddBorderTilesActionPerformed(ActionEvent e) {
		if (tileDownload != null) {
			tileDownload.appendBorderTiles();
			repaint();
		}
	}

	protected void mntmSaveCurrentWorkActionPerformed(ActionEvent e) {
		if (tileDownload != null) {
			JFileChooser fileSaveChooser = new JFileChooser();
			int returnVal = fileSaveChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					tileDownload.saveToFile(fileSaveChooser.getSelectedFile()
							.getPath());

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this,
							"Error writing to file"
									+ " "
									+ fileSaveChooser.getSelectedFile()
											.getPath(), "File Writing Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

	}

	protected void mntmStopAndSaveActionPerformed(ActionEvent arg0) {
		if (tileDownload != null) {
			JFileChooser fileSaveChooser = new JFileChooser();
			int returnVal = fileSaveChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					tileDownload.saveToFile(fileSaveChooser.getSelectedFile()
							.getPath());
					final DlgStopTiledownloadMode dlg = new DlgStopTiledownloadMode(
							this, tileDownload.getZoomLevel(), TileManager
									.getCurrentTileManager().getMapName());
					dlg.setVisible(true);
					if (dlg.isResult()) {
						final DlgProcessingTileDownload proc = new DlgProcessingTileDownload();
						proc.setVisible(true);

						/*
						 * try { Thread.sleep(250); } catch
						 * (InterruptedException e) { }
						 */
						Thread t = new Thread(new Runnable() {
							public void run() {
								tileDownload.addExtensionTiles(dlg
										.getAdditionalZoomLevels());
								proc.startDownload(
										tileDownload.getAllDownloadTiles(),
										dlg.getTargetDir(), dlg.getExtension());
								// Now exit tiledownload mode
								tileDownload = null;
								TileManager.getCurrentTileManager()
										.setTileDownload(tileDownload);
								tracksPanel.setCursor(Cursor
										.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							}
						});
						t.start();

					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this,
							"Error writing to file"
									+ " "
									+ fileSaveChooser.getSelectedFile()
											.getPath(), "File Writing Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	protected void mntmAddCurentMapActionPerformed(ActionEvent arg0) {
		tileDownload.addTiles(Transform.getUpperLeftBoundary(),
				Transform.getLowerRightBoundary());
	}

	@SuppressWarnings("deprecation")
	protected void mntmAddAreaActionPerformed(ActionEvent arg0) {
		tileSelectionMode = MODE_WAIT_FIRST_POINT;
		System.out.println("tileSelectionMode = MODE_WAIT_FIRST_POINT");
		tracksPanel.setCursorText("Define upper left corner", Color.RED);
		repaint();
	}

	protected void chckbxmntmShowTrackLengthActionPerformed(ActionEvent arg0) {
		ViewingConfiguration.setShowLength(chckbxmntmShowTrackLength
				.isSelected());
		if (getTracksTable().getSelectedRowCount() == 1) {
			Track currentTrack = db.getTracks().get(
					getTracksTable().getSelectedRow());
			currentTrack.getLength(true);
		}
		repaint();

	}

	protected void chckbxmntmPointInformationActionPerformed(ActionEvent arg0) {
		ViewingConfiguration.setShowInformation(chckbxmntmPointInformation
				.isSelected());
		repaint();
	}

	protected void mntmEditPointActionPerformed(ActionEvent e) {
		Point selectedPoint = getTracksView().getSelectedTrackView()
				.getSelectedPoint();
		if (selectedPoint != null) {
			int selectedPointIndex = getTracksView().getSelectedTrackView()
					.getSelectedPointIndex();
			DlgPointEdit dialog = new DlgPointEdit(selectedPointIndex,
					selectedPoint);
			dialog.setModal(true);
			dialog.setVisible(true);
			repaint();
		}

	}
	
	protected void mntmSaveMapViewActionPerformed(ActionEvent arg0) {
		uiController.saveMapViewAsImage();
	}
	
	protected void mntmSaveAltitudeProfileActionPerformed(ActionEvent arg0) {
		uiController.saveAltitudeProfileasImage();
	}
}
