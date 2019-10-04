/**
 * see http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 */
package jgpstrackedit.map;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.map.tiledownload.TileCopyCommand;
import jgpstrackedit.map.tiledownload.TileDownload;
import jgpstrackedit.map.tilehandler.*;
import jgpstrackedit.map.util.*;
import jgpstrackedit.util.ProgressHandler;
import jgpstrackedit.view.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Manages all tiles of a map
 * 
 * @author Hubert
 * 
 */
public abstract class TileManager implements Runnable, TileLoadObserver,
		ImageObserver, TileLRUObserver 
{
	private static Logger logger = LoggerFactory.getLogger(TileManager.class);
	
	// Constants
	private int maxZoom = 18;

	private static TileManager currentTileManager = null;

	private boolean showTiles = false;

	private String mapName;
	private String baseURL;
	private boolean ready = false; // true indicates that tile cache structure
									// has been initialized

	private DiskTileLoader diskTileLoader;
	private WebTileLoader webTileLoader;
	private TileSaver tileSaver;

	private HashMap<TileNumber, Tile> tiles;
	private TileLRUList tileLRU;
	private LinkedList<MapObserver> mapObservers = new LinkedList<MapObserver>();

	private Image loadingImage;

	private TileDownload tileDownload = null;
	
	private int[] scales = new int[] {25000000,10000000,5000000,2500000,1000000,
			                          500000,250000,100000,50000,
			                          25000,10000,5000,2500,
			                          1000,500,250,100,
			                          50,25,10,5};

	/**
	 * @return the tileDownload
	 */
	public TileDownload getTileDownload() {
		return tileDownload;
	}

	/**
	 * @param tileDownload
	 *            the tileDownload to set
	 */
	public void setTileDownload(TileDownload tileDownload) {
		this.tileDownload = tileDownload;
	}

	/**
	 * @return the showTiles
	 */
	public boolean isShowTiles() {
		return showTiles;
	}

	/**
	 * @param showTiles
	 *            the showTiles to set
	 */
	public void setShowTiles(boolean showTiles) {
		this.showTiles = showTiles;
	}

	/**
	 * @return the maxZoom
	 */
	public int getMaxZoom() {
		return maxZoom;
	}

	/**
	 * @param maxzoom the maxZoom to set
	 */
	protected void setMaxZoom(int maxzoom) {
		maxZoom = maxzoom;
	}

	public void addMapObserver(MapObserver observer) {
		mapObservers.add(observer);
	}

	public void removeMapObserver(MapObserver observer) {
		mapObservers.remove(observer);
	}

	protected void notifyMapObservers() {
		for (MapObserver observer : mapObservers) {
			observer.mapTilesUpdated();
		}
	}

	/**
	 * @return the mapName
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * @param mapName
	 *            the mapName to set
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * @param baseURL
	 *            the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public static TileManager getCurrentTileManager() {
		return currentTileManager;
	}

	public static void setCurrentTileManager(TileManager tileManager) {
		currentTileManager = tileManager;
	}

	public void addWebQueueObserver(QueueObserver observer) {
		webTileLoader.addQueueObserver(observer);
	}

	public void removeWebQueueObserver(QueueObserver observer) {
		webTileLoader.removeQueueObserver(observer);
	}

	public void addTileSaveQueueObserver(QueueObserver observer) {
		tileSaver.addQueueObserver(observer);
	}

	public void removeTileSaveQueueObserver(QueueObserver observer) {
		tileSaver.removeQueueObserver(observer);
	}

	/**
	 * Opens a map.
	 */
	public void open() {
		tiles = new HashMap<TileNumber, Tile>();
		tileLRU = new TileLRUList(
				Configuration.getIntProperty("MAX_TILES_IN_MEMORY"));
		tileLRU.addTileLRUObserver(this);
		diskTileLoader = new DiskTileLoader();
		diskTileLoader.setBaseDirectory("map" + File.separator
				+ mapName);
		diskTileLoader.addTileLoadObserver(this);
		diskTileLoader.start();
		webTileLoader = new WebTileLoader();
		webTileLoader.addTileLoadObserver(this);
		webTileLoader.start();
		tileSaver = new TileSaver();
		tileSaver.setBaseDirectory("map" + File.separator
				+ mapName);
		tileSaver.start();
		loadingImage = createLoadingImage();
		new Thread(this).start();

	}

	/**
	 * Closes the map.
	 * 
	 */
	public void close() {
		tileLRU.removeTileLRUObserver(this);
		diskTileLoader.removeTileLoadObserver(this);
		diskTileLoader.stop();
		webTileLoader.removeTileLoadObserver(this);
		webTileLoader.stop();
		tileSaver.stop();

	}

	/**
	 * Paints all tiles of the map.
	 * 
	 * @param g2D
	 *            Graphics2D object to paint on
	 * @param upperLeftX
	 *            upper left corner (longitude) of viewing area (east)
	 * @param upperLeftY
	 *            upper left corner (latitude) of viewing area (north)
	 * @param lowerRightX
	 *            lower right corner (longitude) of viewing area (west)
	 * @param lowerRightY
	 *            lower right corner (latitude) of viewing area (south)
	 * @param screenWidth
	 *            width of viewing area (pixel)
	 * @param screenHeight
	 *            height of viewing area (pixel)
	 */
	public void paintMap(Graphics2D g2D, double upperLeftX, double upperLeftY,
			double lowerRightX, double lowerRightY, int screenWidth,
			int screenHeight) {
		int zoomLevel = Transform.getZoomLevel();
		/*
		 * int zoomLevel = calculateZoomLevel(upperLeftX, upperLeftY,
		 * lowerRightX, lowerRightY);
		 */
		TileNumber upperLeftTileNumber = TileNumber.getTileNumber(zoomLevel,
				upperLeftX, upperLeftY);
		TileBoundary upperLeftTileBoundary = TileBoundary
				.getTileBoundary(upperLeftTileNumber);
		int tilesX = calculateNumberHorizontalTiles(zoomLevel, upperLeftX,
				upperLeftTileBoundary.getWest(), screenWidth);
		int tilesY = calculateNumberVerticalTiles(zoomLevel, upperLeftY,
				upperLeftTileBoundary.getWest(),
				upperLeftTileBoundary.getNorth(), screenHeight);
		int xOffset = pixelXOffset(zoomLevel, upperLeftX,
				upperLeftTileBoundary.getWest());
		int yOffset = pixelYOffset(zoomLevel, upperLeftY,
				upperLeftTileBoundary.getWest(),
				upperLeftTileBoundary.getNorth());
		/*
		 * logger.info("paintMap ready=" + ready + " tilesX=" + tilesX +
		 * " tilesY=" + tilesY + " xOff=" + xOffset + " yOff=" + yOffset);
		 */
		for (int x = 0; x < tilesX; x++)
			for (int y = 0; y < tilesY; y++) {
				TileNumber loadingTileNumber = new TileNumber(zoomLevel,
						upperLeftTileNumber.getX() + x,
						upperLeftTileNumber.getY() + y);
				Image loadImage = getTileImage(loadingTileNumber);
				TileBoundary tb = TileBoundary
						.getTileBoundary(loadingTileNumber);
				if (zoomLevel > 11) {
					g2D.drawImage(loadImage, Transform.screenX(tb.getWest()),
							Transform.screenY(tb.getNorth()), null);
				} else {
					g2D.drawImage(loadImage, x * Tile.PIXEL_PER_TILE - xOffset,
							y * Tile.PIXEL_PER_TILE - yOffset, null);
				}
				if (isShowTiles()) {
					Color saveColor = g2D.getColor();
					g2D.setColor(Color.BLUE);
					// logger.info("TileBoundary: "+tb);
					drawTileBorder(g2D, tb);
					g2D.drawString(loadingTileNumber.toString(),
							Transform.screenX(tb.getWest()) + 2,
							Transform.screenY(tb.getNorth()) + 12);
					g2D.setColor(saveColor);
					//
				}
			}
		if (getTileDownload() != null) {
			for (TileNumber tileNumber : getTileDownload().getDownloadTiles()) {
				TileBoundary tb = TileBoundary.getTileBoundary(tileNumber);
				Color saveColor = g2D.getColor();
				g2D.setColor(Color.RED);
				drawTileBorder(g2D, tb);
				g2D.setColor(saveColor);
			}
		}

		// Debug
		/*
		 * g2D.drawLine(Transform.screenX(upperLeftTileBoundary.getWest()),
		 * Transform.screenY(upperLeftTileBoundary.getNorth()),
		 * Transform.screenX(upperLeftTileBoundary.getEast()),
		 * Transform.screenY(upperLeftTileBoundary.getSouth()) );
		 * g2D.drawLine(Transform.screenX(upperLeftTileBoundary.getWest()),
		 * Transform.screenY(upperLeftTileBoundary.getSouth()),
		 * Transform.screenX(upperLeftTileBoundary.getEast()),
		 * Transform.screenY(upperLeftTileBoundary.getSouth()) );
		 * g2D.setColor(Color.RED);
		 * g2D.drawLine(Transform.screenX(Transform.getUpperLeftBoundary
		 * ().getLongitude()),
		 * Transform.screenY(Transform.getUpperLeftBoundary().getLattitude()),
		 * Transform.screenX(Transform.getLowerRightBoundary().getLongitude()),
		 * Transform.screenY(Transform.getLowerRightBoundary().getLattitude()));
		 */
		//
	}

	protected void drawTileBorder(Graphics2D g2D, TileBoundary tb) {
		// TODO Auto-generated method stub
		g2D.drawLine(Transform.screenX(tb.getWest()),
				Transform.screenY(tb.getNorth()),
				Transform.screenX(tb.getEast()),
				Transform.screenY(tb.getNorth()));
		g2D.drawLine(Transform.screenX(tb.getEast()),
				Transform.screenY(tb.getNorth()),
				Transform.screenX(tb.getEast()),
				Transform.screenY(tb.getSouth()));
		g2D.drawLine(Transform.screenX(tb.getEast()),
				Transform.screenY(tb.getSouth()),
				Transform.screenX(tb.getWest()),
				Transform.screenY(tb.getSouth()));
		g2D.drawLine(Transform.screenX(tb.getWest()),
				Transform.screenY(tb.getSouth()),
				Transform.screenX(tb.getWest()),
				Transform.screenY(tb.getNorth()));

	}

	/**
	 * Paints the scale
	 * @param g2D
	 * @param x
	 * @param y
	 */
	public void paintScale(Graphics2D g2D, int x, int y) {
		int zoom = Transform.getZoomLevel();
		int scale = scales[Transform.getZoomLevel()];
		//# paint scale
		Color color = g2D.getColor();
		g2D.setColor(Color.BLACK);
		double resolution = 156543.034  * Math.cos(Transform.getLowerRightBoundary().getLatitude()*Math.PI/180.0) / (Math.pow(2, zoom));
		int lengthPixel = (int)(scale / resolution);
		g2D.fillRect(x, y, lengthPixel, 2);
		g2D.fillRect(x, y-10, 2, 10);
		g2D.fillRect(x+lengthPixel-2, y-10, 2, 10);
		if (scale >= 1000) {
			g2D.drawString(""+(scale/1000)+"km", x+10, y-2);
		} else {
			g2D.drawString(""+scale+"m", x+10, y-2);
			
		}
		g2D.setColor(color);
	}
	
	/**
	 * Calculates the zoom level which fits best to the given viewing area.
	 * 
	 * @param upperLeftX
	 *            upper left corner (longitude) of viewing area (east)
	 * @param upperLeftY
	 *            upper left corner (latitude) of viewing area (north)
	 * @param lowerRightX
	 *            lower right corner (longitude) of viewing area (west)
	 * @param lowerRightY
	 *            lower right corner (latitude) of viewing area (south)
	 * @return zoom level (range: 0 (whole world) to 18 (max. detail))
	 */
	public static int calculateZoomLevel(double upperLeftX, double upperLeftY,
			double lowerRightX, double lowerRightY) {
		double viewingWidth = Math.abs(lowerRightX - upperLeftX);
		double worldWidth = 360.0;
		int zoomLevel = getCurrentTileManager().getMaxZoom();
		for (int i = 0; i <= getCurrentTileManager().getMaxZoom() + 1; i++) {
			if (viewingWidth > (worldWidth / (1 << i))) {
				zoomLevel = i; // Debug: -1
				break;
			}
		}
		return zoomLevel;

	}

	/**
	 * Increments the given zoom level
	 * 
	 * @param zoomLevel
	 *            current zoom level
	 * @return incremented zoom level
	 */
	public static int incZoomLevel(int zoomLevel) {
		if (zoomLevel < getCurrentTileManager().getMaxZoom()) {
			return zoomLevel + 1;
		} else
			return zoomLevel;
	}

	/**
	 * Decrements the given zoom level
	 * 
	 * @param zoomLevel
	 *            current zoom level
	 * @return decremented zoom level
	 */
	public static int decZoomLevel(int zoomLevel) {
		if (zoomLevel > 0) {
			return zoomLevel - 1;
		} else
			return zoomLevel;
	}

	protected int pixelXOffset(int zoomLevel, double x, double tileX) {
		double hiddenLength = Math.abs(x - tileX);
		return (int) (hiddenLength / TileBoundary.scaleX(zoomLevel));

	}

	protected int pixelYOffset(int zoomLevel, double y, double tileX,
			double tileY) {
		double hiddenLength = tileY - y;
		return (int) (hiddenLength / TileBoundary.scaleY(zoomLevel, tileX,
				tileY));

	}

	/**
	 * Calculates the number of tiles horizontally
	 * 
	 * @param zoomLevel
	 * @param upperLeftX
	 * @param tileUpperLeftX
	 * @param screenWidth
	 * @return number of tiles horizontally
	 */
	protected int calculateNumberHorizontalTiles(int zoomLevel,
			double upperLeftX, double tileUpperLeftX, int screenWidth) {
		double minPixel = pixelXOffset(zoomLevel, upperLeftX, tileUpperLeftX)
				+ screenWidth;
		int numberTiles = (int) (Math.floor(minPixel / Tile.PIXEL_PER_TILE)) + 1;
		return numberTiles;
	}

	/**
	 * Calculates the number of tiles vertically
	 * 
	 * @param zoomLevel
	 * @param upperLeftY
	 * @param tileUpperLeftY
	 * @param screenHeight
	 * @return number of tiles horizontally
	 */
	protected int calculateNumberVerticalTiles(int zoomLevel,
			double upperLeftY, double tileUpperLeftX, double tileUpperLeftY,
			int screenHeight) {
		double minPixel = pixelYOffset(zoomLevel, upperLeftY, tileUpperLeftX,
				tileUpperLeftY) + screenHeight;
		int numberTiles = (int) (Math.floor(minPixel / Tile.PIXEL_PER_TILE)) + 1;
		return numberTiles;
	}

	/**
	 * Returns the image of the given tile. This methods performs the loading of
	 * the tile image, depended on the actual storage position of the tile
	 * image. First, if the image already has been loaded to memory, the image
	 * is returned. If the image is on disk, then it is loaded to memory. If the
	 * image is not stored on disk, then it is loaded from the web.
	 * 
	 * @param tileNumber
	 * @return tile image if present, else default tile image
	 */
	protected Image getTileImage(TileNumber tileNumber) {
		Image img = loadingImage;
		if (!ready) {
			/*
			 * logger.info("getTileImage: not ready, return DefaultImage "
			 * + tileNumber.toString());
			 */
			return img;
		}
		Tile tile = tiles.get(tileNumber);
		if (tile == null) {
			tile = new Tile(tileNumber);
			tiles.put(tileNumber, tile);
		}
		tileLRU.touch(tileNumber);
		switch (tile.getTileState()) {
		case unknown:
			tile.setTileState(TileState.loadingFromWeb);
			webTileLoader.loadTile(tileNumber);
			/*
			 * logger.info("getTileImage: unknown, return DefaultImage "
			 * + tileNumber.toString());
			 */
			break;
		case onDisk:
			tile.setTileState(TileState.loadingFromDisk);
			diskTileLoader.loadTile(tileNumber);
			/*
			 * logger.info("getTileImage: onDisk, return DefaultImage " +
			 * tileNumber.toString());
			 */
			break;
		case loadingFromWeb:
			/*
			 * logger.info("getTileImage: loadingFromWeb, return DefaultImage " +
			 * tileNumber.toString());
			 */
			break;
		case loadingFromDisk:
			/*
			 * logger.info("getTileImage: loadingFromDisk, return DefaultImage " +
			 * tileNumber.toString());
			 */
			break;
		case available:
			/*
			 * logger.info("getTileImage: available, return TileImage " +
			 * tileNumber.toString());
			 */
			if (tile.getTileImage() != null) {
			    img = tile.getTileImage();
			} else {
				tile.setTileState(TileState.unknown);
			}
			break;
		}
		return img;

	}

	/**
	 * Downloads tile from web, if the tile is not already on disk.
	 * 
	 * @param tileNumber
	 * @return true if the tile image has been downloaded successfully
	 */
	protected boolean downloadTileFromWeb(TileNumber tileNumber) {
		boolean result = false;
		Tile tile = tiles.get(tileNumber);
		if (tile == null) {
			tile = new Tile(tileNumber);
			tiles.put(tileNumber, tile);
		}
		tileLRU.touch(tileNumber);
		switch (tile.getTileState()) {
		case unknown:
			String urlString = getTileURL(tileNumber);
			URL url;
			try {
				//logger.info("TileManager: URL loading: "+urlString);
				url = new URL(urlString);
				Image image = Toolkit.getDefaultToolkit().createImage(url);
				String dirPath = getBaseDir() + File.separator
						+ tileNumber.getZoom() + File.separator
						+ tileNumber.getX();
				File dir = new File(dirPath);
				dir.mkdirs();
				String fileName = dirPath + File.separator
						+ tileNumber.getY() + ".png";
				BufferedImage bufferedImage = ImageConverter
						.toBufferedImage(image);
				//logger.info("TileManager: File saving: "+fileName);
				if (bufferedImage != null) {
					File file = new File(fileName);
					try {
						ImageIO.write(bufferedImage, "png", file);
						tile.setTileState(TileState.onDisk);
						result = true;
					} catch (IOException e) {
						logger.error("Exception while writing image!", e);
					}
				} else {
					logger.info("TileManager: File not saved due to null-image: " + fileName);

				}
			} catch (MalformedURLException e) {

				logger.error("TileManager: URL=" + urlString, e);
			}
			break;
		case onDisk:
			result = true;
			break;
		case loadingFromWeb:
			break;
		case loadingFromDisk:
			result = true;
			break;
		case available:
			result = true;
			break;
		}
		return result;

	}

	/**
	 * Downloads the given tile images and copies them to the given target
	 * directory, optionally appending the given extension to the image file
	 * names. If the tile image is not present in the disk tile cache, then the
	 * image is loaded from web. This method is intended to download a large number of tile images
	 * for further offline use, although the tile cache on disk is honored.
	 * 
	 * @param downloadTiles list of tiles to download
	 * @param targetDir target directory to store tile images
	 * @param extension optional appended extension for image file names
	 * @param progressHandler progress handler which is informed of download progress, may be null
	 * @return a list of tiles whose image could not have been downloaded
	 */
	public ArrayList<TileNumber> downloadTiles(
			TreeSet<TileNumber> downloadTiles, String targetDir,
			String extension, ProgressHandler progressHandler) {

		ArrayList<TileNumber> errorTiles = new ArrayList<TileNumber>();
		int downloadCount = 0;
		for (TileNumber tile : downloadTiles) {
			if (progressHandler != null) {
				progressHandler.stepDone((int)((downloadCount*100.0/downloadTiles.size())));
				Thread.yield();
			}
			if (downloadTileFromWeb(tile)) {
				try {
					File file = new File(targetDir + File.separator
							+ tile.getZoom() + File.separator
							+ tile.getX());
					file.mkdirs();
					String sourceFile = getBaseDir() + tile.toFileName() + ".png";
					String destFile = targetDir + tile.toFileName() + ".png"
							+ extension;
					TileCopyCommand.copy(sourceFile, destFile);
					//logger.info("TileManager: File copied: " + destFile);
				} catch (IOException e) {
					errorTiles.add(tile);
					logger.error("Exception while downloading tiles!", e);
				}
			} else {
				errorTiles.add(tile);
			}
			Thread.yield();
			downloadCount++;
		}
		return errorTiles;
	}


	/**
	 * Initializes the tile cache on disk. Checks and creates if necessary the
	 * directory structure on disk.
	 */
	public void initializeTileCacheStructure() {
		tiles.clear();
		for (int i = 0; i <= getMaxZoom(); i++) {
			File file = new File(getBaseDir() + File.separator + i);
			file.mkdirs();
			initializeZoomLevel(file, i);
		}

	}

	public String getBaseDir() {
		return "map" + File.separator + getMapName();
	}

	protected void initializeZoomLevel(File directory, int zoomLevel) {

		File[] xTilesDir = directory.listFiles();
		for (int i = 0; i < xTilesDir.length; i++) {
			initializeXLevel(xTilesDir[i], zoomLevel,
					Integer.parseInt(xTilesDir[i].getName()));
		}
	}

	protected void initializeXLevel(File xLevelDir, int zoomLevel, int x) {
		File[] yTiles = xLevelDir.listFiles();
		for (int i = 0; i < yTiles.length; i++) {
			String yTileImageFileName = yTiles[i].getName();
			if (yTileImageFileName.endsWith(".png")) {
				int y = Integer.parseInt(yTileImageFileName.substring(0,
						yTileImageFileName.length() - 4));
				TileNumber tileNumber = new TileNumber(zoomLevel, x, y);
				Tile tile = new Tile(tileNumber);
				tile.setTileState(TileState.onDisk);
				tiles.put(tileNumber, tile);
			}
		}

	}

	@Override
	public void run() {
		initializeTileCacheStructure();
		ready = true;
		notifyMapObservers();
	}

	public static Image createLoadingImage() {
		BufferedImage img = new BufferedImage(256, 256,
				BufferedImage.TYPE_INT_RGB);
		img.createGraphics();
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(new Color(255, 255, 220));
		g.fillRect(0, 0, 256, 256);
		g.setColor(new Color(240, 240, 30));
		g.drawString("Loading...          Loading...", 1, 60);
		g.drawString("If not loaded properly, choose View.Refresh Map", 1, 100);
		return img;
	}

	@Override
	public void tileLoaded(TileLoadEvent tileLoadEvent) {
		Tile tile = tiles.get(tileLoadEvent.getTileNumber());
		if (tile != null) {
			tile.setTileImage(tileLoadEvent.getImageLoaded());
			switch (tile.getTileState()) {
			case unknown:
				break;
			case onDisk:
				break;
			case loadingFromWeb:
				tile.setTileState(TileState.available);
				/*
				 * logger.info("tileLoaded from Web " +
				 * tileLoadEvent.getTileNumber().toString());
				 */
				notifyMapObservers();
				tileSaver.saveImage(tileLoadEvent.getImageLoaded(),
						tileLoadEvent.getTileNumber());
				break;
			case loadingFromDisk:
				tile.setTileState(TileState.available);
				/*
				 * logger.info("tileLoaded from Disk " +
				 * tileLoadEvent.getTileNumber().toString());
				 */
				notifyMapObservers();
				break;
			case available:
				notifyMapObservers();
				break;
			}
		}

	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		boolean loaded = (infoflags & ImageObserver.ALLBITS) > 0;
		boolean error = (infoflags & ImageObserver.ERROR) > 0;
		if (loaded || error) {
			notifyMapObservers();
		}
		return !loaded;
	}

	public abstract String getTileURL(TileNumber tileNumber);

	public void reloadTile(double mapLongitude, double mapLatitude) {
		TileNumber reloadTileNumber = TileNumber.getTileNumber(
				Transform.getZoomLevel(), mapLongitude, mapLatitude);
		Tile reloadTile = tiles.get(reloadTileNumber);
		if (reloadTile != null) {
			reloadTile.setTileState(TileState.unknown);
		}
	}

	@Override
	public void freeTile(TileNumber tileNumber) {
		Tile tile = tiles.get(tileNumber);
		tile.setTileImage(null);
		switch (tile.getTileState()) {
		case unknown:
			tile.setTileImage(null);
			break;
		case onDisk:
			break;
		case loadingFromWeb:
			tile.setTileState(TileState.unknown);
			break;
		case loadingFromDisk:
			tile.setTileState(TileState.onDisk);
			break;
		case available:
			tile.setTileState(TileState.onDisk);
			break;
		}
	}

}
