/**
 * 
 */
package jgpstrackedit.map.tiledownload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import jgpstrackedit.config.SystemConfig;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.map.tilehandler.QueueObserver;
import jgpstrackedit.map.util.TileBoundary;
import jgpstrackedit.map.util.TileNumber;

/**
 * @author hlutnik
 * 
 */
public class TileDownload {

	private TreeSet<TileNumber> downloadTiles = new TreeSet<TileNumber>();
	private TreeSet<TileNumber> extensionDownloadTiles = new TreeSet<TileNumber>();

	private int zoomLevel = 10;
	private TileCopyHandler tileCopyHandler;

	public TileDownload() {
		tileCopyHandler = new TileCopyHandler();
		tileCopyHandler.start();
	}

	public void addCopyQueueObserver(QueueObserver observer) {
		tileCopyHandler.addQueueObserver(observer);
	}
	
	public void removeCopyQueueObserver(QueueObserver observer) {
		tileCopyHandler.removeQueueObserver(observer);
	}
	

	/**
	 * @return the zoomLevel
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * @param zoomLevel
	 *            the zoomLevel to set
	 */
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	/**
	 * @return the downloadTiles
	 */
	public TreeSet<TileNumber> getDownloadTiles() {
		return downloadTiles;
	}

	public TreeSet<TileNumber> getExtensionDownloadTiles() {
		return extensionDownloadTiles;
	}

	/** Returns all download tiles (tiles from current zoom level and extended zoom levels).
	 * @return all downloadTiles
	 */
	public TreeSet<TileNumber> getAllDownloadTiles() {
		TreeSet<TileNumber> all = new TreeSet<TileNumber>();
		all.addAll(downloadTiles);
		all.addAll(extensionDownloadTiles);
		return all;
	}
	
	/**
	 * Adds the given tile to the list of download tiles
	 * 
	 * @param tileNumber
	 *            to be added
	 */
	public void addTile(TileNumber tileNumber) {
		downloadTiles.add(tileNumber);
	}

	/**
	 * Adds the given tile to the extension list of download tiles
	 * 
	 * @param zoomLevel
	 *            desired zoom level
	 * @param longitude
	 *            longitude within the tile
	 * @param latitude
	 *            latitude within the tile
	 */
	public void addExtensionTile(int zoomLevel, double longitude, double latitude) {
		extensionDownloadTiles.add(TileNumber.getTileNumber(zoomLevel, longitude, latitude));
	}

	/**
	 * Removes the given tile from the list of download tiles
	 * 
	 * @param tileNumber
	 */
	public void remove(TileNumber tileNumber) {
		downloadTiles.remove(tileNumber);
	}

	/**
	 * Adds all tiles of the given track to the download tiles
	 * 
	 * @param zoomlevel
	 *            the zoomlevel whose tiles should be added
	 * @param track
	 *            the track whoese tiles should be added
	 */
	public void addTiles(int zoomlevel, Track track) {

		for (Point point : track.getPoints()) {
			downloadTiles.add(TileNumber.getTileNumber(zoomlevel,
					point.getLongitude(), point.getLatitude()));
		}

	}

	/**
	 * Adds all tiles of the given track to the download tiles
	 * 
	 * @param track
	 *            the track whose tiles should be added
	 */
	public void addTiles(Track track) {
		addTiles(getZoomLevel(), track);
	}

	/**
	 * Adds all tiles of the given tracks to the download tiles
	 * 
	 * @param tracks
	 *            list of tracks whose tiles should be added
	 */

	public void addTiles(ArrayList<Track> tracks) {
		for (Track track : tracks) {
			addTiles(track);
		}
	}

	/**
	 * Adds all tiles within the given boundary in the set zoom level.
	 * @param upperLeftBoundary coordinates of upper left point of the boundary
	 * @param lowerRightBoundary coordinates of lower right point of the boundary
	 */
	public void addTiles(Point upperLeftBoundary, Point lowerRightBoundary) {
		// TODO Auto-generated method stub
		TileNumber upperLeftTile = TileNumber.getTileNumber(getZoomLevel(), upperLeftBoundary.getLongitude(), upperLeftBoundary.getLatitude());
		TileNumber lowerRightTile = TileNumber.getTileNumber(getZoomLevel(), lowerRightBoundary.getLongitude(), lowerRightBoundary.getLatitude());
		for (int x=upperLeftTile.getX();x<=lowerRightTile.getX();x++)
			for (int y=upperLeftTile.getY();y<=lowerRightTile.getY();y++) {
				addTile(new TileNumber(getZoomLevel(),x,y));
			}
	}

	/**
	 * Adds tiles of the given (lower) zoom levels to the extension list, based on the already defined
	 * download tiles. Call this method if the tiles of lower zoom levels should
	 * be down loaded also.
	 * 
	 * @param zoomLevels
	 */
	public void addExtensionTiles(List list) {
		for (TileNumber tile : downloadTiles) {
			TileBoundary tb = TileBoundary.getTileBoundary(tile);
			for (Object o:list) {
                addExtensionTile(((Integer)o).intValue(),tb.getCenterLongitude(),tb.getCenterLatitude());
			}
		}
	}

	/**
	 * If the given tile number is in the list of download tiles, the tile is
	 * removed. Otherwise, the tile is added to the download tiles.
	 * 
	 * @param tileNumber
	 *            tile number to toggle
	 */
	public void toggleTile(TileNumber tileNumber) {
		if (downloadTiles.contains(tileNumber)) {
			downloadTiles.remove(tileNumber);
		} else {
			addTile(tileNumber);
		}
	}

	/**
	 * Adds a border, width one tile, to the list of download tiles. Be
	 * carefully using this method, several calls to this method increases the
	 * number of tiles to be downloaded significantly!
	 */
	public void appendBorderTiles() {
		TreeSet<TileNumber> borderTiles = new TreeSet<TileNumber>();
		for (TileNumber tile : downloadTiles) {
			int zoom = tile.getZoom();
			int x = tile.getX();
			int y = tile.getY();
			borderTiles.add(new TileNumber(zoom, x - 1, y - 1));
			borderTiles.add(new TileNumber(zoom, x, y - 1));
			borderTiles.add(new TileNumber(zoom, x + 1, y - 1));
			borderTiles.add(new TileNumber(zoom, x - 1, y));
			borderTiles.add(new TileNumber(zoom, x + 1, y));
			borderTiles.add(new TileNumber(zoom, x - 1, y + 1));
			borderTiles.add(new TileNumber(zoom, x, y + 1));
			borderTiles.add(new TileNumber(zoom, x + 1, y + 1));
		}
		downloadTiles.addAll(borderTiles);
	}

	/**
	 * Saves the list of download tile to the given file.
	 * 
	 * @param fileName
	 *            name of the file
	 * @throws FileNotFoundException
	 *             if there is an error writing to the file
	 */
	public void saveToFile(String fileName) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName))));
		out.println(getZoomLevel());
		for (TileNumber tile : downloadTiles) {
			out.println(tile.toString());
		}
		out.close();
	}

	/**
	 * Loads the list of download tiles from the given file. The file must have
	 * been saved prior by calling the saveToFile() method.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void loadFromFile(String fileName) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName)));
		String zoomLevel = in.readLine();
		if (zoomLevel != null)
			setZoomLevel(Integer.parseInt(zoomLevel));
		String tileNumber = in.readLine();
		while (tileNumber != null) {
			String[] parts = tileNumber.split("/");
			downloadTiles.add(new TileNumber(Integer.parseInt(parts[1]),
					Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
			tileNumber = in.readLine();
		}
		in.close();
	}


	/**
	 * Physically copies all tile images(from the map tiles cache on disk) to
	 * the given tile Directory. Creates all necessary directories. Important:
	 * All required tile images must already exist in the map tiles cache on
	 * disk!
	 * 
	 * @param mapDir
	 *            base directory of disk tile cache
	 * @param tileDir
	 *            destination directory for downloaded tiles
	 * @param extraExtension
	 *            appends the given extension to tile image names
	 */
	/*
	public void saveDownloadedTiles(String mapDir, String tileDir,
			String extraExtension) {
		/-*
		 * File file = new File("map" + SystemConfig.dirSeparator() +
		 * getMapName() + SystemConfig.dirSeparator() + i); file.mkdirs();
		 *-/
		File file = new File(tileDir);
		file.mkdirs();
		TreeSet<TileNumber> allTiles = new TreeSet<TileNumber>(downloadTiles);
		allTiles.addAll(extensionDownloadTiles);
		for (TileNumber tile : allTiles) {
			file = new File(tileDir + SystemConfig.dirSeparator()
					+ tile.getZoom() + SystemConfig.dirSeparator()
					+ tile.getX());
			file.mkdirs();
			String sourceFile = mapDir + tile.toFileName() + ".png";
			String destFile = tileDir + tile.toFileName() + ".png"
					+ extraExtension;
			tileCopyHandler.addCopy(sourceFile, destFile,tile,this);
		}
	}
	*/


}
