/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.view;

import jgpstrackedit.data.Point;
import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.util.TileBoundary;

/**
 * Transforms between coordinates of the current map (within boundary of the
 * current zoom level) and the coordinates of the drawing area.
 * 
 * @author Hubert
 */
public class Transform {

	private static double upperLeftX;
	private static double upperLeftY;
	private static double mapWidth;
	private static double mapHeight;
	private static int screenWidth;
	private static int screenHeight;
	private static double scaleX;
	private static double scaleY;
	private static int zoomLevel = 0;

	/**
	 * Sets the transform parameter. If a map is displayed, then the parameter
	 * lowerRightBoundary is interpreted as a hint. Depending of the scale of
	 * the current zoom level the lower right boundary point of the viewing area
	 * may be expanded, that means shifted to right and lower.
	 * 
	 * @param upperLeftBoundary
	 *            upper left corner of the map boundary
	 * @param lowerRightBoundary
	 *            lower right corner of the map boundary
	 * @param width
	 *            screen width of the drawing area
	 * @param height
	 *            screen height of the drawing area
	 * @param recalculateZoomLevel
	 *            true indicates that the zoom level should be recalculated
	 */
	public static void setTransform(Point upperLeftBoundary,
			Point lowerRightBoundary, int width, int height,
			boolean recalculateZoomLevel) {
		screenWidth = width;
		screenHeight = height;
		upperLeftX = upperLeftBoundary.getLongitude();
		upperLeftY = upperLeftBoundary.getLatitude();
		double lowerRightX = lowerRightBoundary.getLongitude();
		double lowerRightY = lowerRightBoundary.getLatitude();
		if (TileManager.getCurrentTileManager() != null) {
			// a map is displayed
			if (recalculateZoomLevel) {
				zoomLevel = TileManager.calculateZoomLevel(upperLeftX,
						upperLeftY, lowerRightX, lowerRightY);
			}
			scaleX = TileBoundary.scaleX(zoomLevel);
			scaleY = TileBoundary.scaleY(zoomLevel, upperLeftX, upperLeftY);
			mapWidth = scaleX * width;
			mapHeight = scaleY * height;
		} else {
			mapWidth = lowerRightX - upperLeftX;
			mapHeight = upperLeftY - lowerRightY;

			double resolutionX = mapWidth / screenWidth;
			double resolutionY = mapHeight / screenHeight;
			if (resolutionX > resolutionY) {
				mapHeight = resolutionX * screenHeight;
			} else {
				mapWidth = resolutionY * screenWidth;
			}
			scaleX = mapWidth / screenWidth;
			scaleY = mapHeight / screenHeight;
		}
		/*
		logger.info("SetTransform UpperLeft:  " + upperLeftBoundary);
		logger.info("             LowerRight: " + lowerRightBoundary);
		logger.info("             Screen width=" + width + " height="
				+ height);
		logger.info("        mapWidth=" + mapWidth + " mapHeight="
				+ mapHeight);
		logger.info("        zoomLevel=" + zoomLevel);
		*/

	}

	/**
	 * Recalculates the transform parameter (Zooms the map, the given point becomes the center of the current map view). 
	 * The zoom level remains unchanged.
	 * @param centerPoint new center of map view
	 */
	public static void zoomCenter(Point centerPoint) {
		upperLeftX = centerPoint.getLongitude() - mapWidth/2.0;
		upperLeftY = centerPoint.getLatitude() + mapHeight/2.0;
		
	}
	
    /**
     * Decrements the current zoom level. Must be called before setTransform if zooming should be performed.
     * Work around solution, to be refactored?
     */
	public static void zoomOut() {
		if (TileManager.getCurrentTileManager() != null) {
			zoomLevel = TileManager.decZoomLevel(zoomLevel);
			scaleX = TileBoundary.scaleX(zoomLevel);
			scaleY = TileBoundary.scaleY(zoomLevel, upperLeftX, upperLeftY);
			mapWidth = scaleX * screenWidth;
			mapHeight = scaleY * screenHeight;

			//upperLeftX = upperLeftBoundary.getLongitude();
			//upperLeftY = upperLeftBoundary.getLatitude();
		}
	}

    /**
     * Increments the current zoom level. Must be called before setTransform if zooming should be performed.
     * Work around solution, to be refactored?
     */
	public static void zoomIn() {
		if (TileManager.getCurrentTileManager() != null) {
			zoomLevel = TileManager.incZoomLevel(zoomLevel);
			scaleX = TileBoundary.scaleX(zoomLevel);
			scaleY = TileBoundary.scaleY(zoomLevel, upperLeftX, upperLeftY);
			mapWidth = scaleX * screenWidth;
			mapHeight = scaleY * screenHeight;

			//upperLeftX = upperLeftBoundary.getLongitude();
			//upperLeftY = upperLeftBoundary.getLatitude();
		}
	}

	/**
	 * Transforms a longitude coordinate to the x-xoordinate of the drawing area
	 * 
	 * @param longitude
	 *            the longitude
	 * @return corresponding screen x-coordinate
	 */
	public static int screenX(double longitude) {
		// return (int) ((longitude - upperLeftX) * screenWidth / mapWidth);
		return (int) ((longitude - upperLeftX) / scaleX);
	}

	/**
	 * Transforms a lattitude coordinate to the y-xoordinate of the drawing area
	 * 
	 * @param lattitude
	 *            the lattitude
	 * @return corresponding screen y-coordinate
	 */
	public static int screenY(double lattitude) {
		// return (int) ((upperLeftY - lattitude) * screenHeight / mapHeight);
		return (int) ((upperLeftY - lattitude) / scaleY);
	}

	/**
	 * Transforms a screen x-coordinate to the corresponding longitude of the
	 * map.
	 * 
	 * @param screenX
	 *            x-Coordinate of the screen
	 * @return longitude
	 */
	public static double mapLongitude(int screenX) {
		return screenX * mapWidth / screenWidth + upperLeftX;
	}

	/**
	 * Transforms a screen y-coordinate to the corresponding latitude of the
	 * map.
	 * 
	 * @param screenY
	 *            y-Coordinate of the screen
	 * @return latitude
	 */
	public static double mapLatitude(int screenY) {
		return upperLeftY - (screenY * mapHeight / screenHeight);
	}

	/**
	 * Transforms screen coordinates to the corresponding point of the map
	 * @param screenX x-coordinate of screen
	 * @param screenY y-coordinate of screen
	 * @return A point containing the corresponding screen coordinates (latitude, longiude) of the map
	 */
	public static Point mapPoint(int screenX, int screenY) {
		return new Point(mapLongitude(screenX),mapLatitude(screenY));
	}

	/**
	 * Sets the new screen dimension. This method should be called after the
	 * screen dimensions are changed (by the user). Internally the view
	 * dimensions are changed. The new lower right point of the boundary
	 * reflects the new screenWidth and screenHeight.
	 * 
	 * @param width
	 *            new screenWidth
	 * @param height
	 *            new screenHeight
	 */
	public static void setNewScreenDimension(int width, int height) {
		mapWidth = mapWidth * width / screenWidth;
		mapHeight = mapHeight * height / screenHeight;
		screenWidth = width;
		screenHeight = height;
		double resolutionX = mapWidth / screenWidth;
		double resolutionY = mapHeight / screenHeight;
		if (resolutionX > resolutionY) {
			mapHeight = resolutionX * screenHeight;
		} else {
			mapWidth = resolutionY * screenWidth;
		}
	}

	/**
	 * Sets the screen dimension. This method should be called before any other methods of the Transform class
	 * to initialize the transform.
	 * @param width
	 * @param height
	 */
	public static void setScreenDimension(int width, int height) {
		screenWidth = width;
		screenHeight = height;	
	}
	
	/**
	 * Returns left upper Point of viewing boundary
	 * 
	 * @return left upper point
	 */
	public static Point getUpperLeftBoundary() {
		return new Point(upperLeftX, upperLeftY);
	}

	/**
	 * Returns left upper Point of viewing boundary
	 * 
	 * @return left upper point
	 */
	public static Point getLowerRightBoundary() {
		return new Point(upperLeftX + mapWidth, upperLeftY - mapHeight);
	}

	/**
	 * @return the screenWidth
	 */
	public static int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * @return the screenHeight
	 */
	public static int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Returns the current zoom level.
	 * 
	 * @return the zoomLevel
	 */
	public static int getZoomLevel() {
		return zoomLevel;
	}

    /**
     * Set the transform to the given zoom level and upper left boundary point.
     * @param newZoomLevel the new zoom level
     * @param upperLeftBoundary upper left boundary point
    */
	public static void zoom(int newZoomLevel, Point upperLeftBoundary) {
		// TODO Auto-generated method stub
		upperLeftX = upperLeftBoundary.getLongitude();
		upperLeftY = upperLeftBoundary.getLatitude();
		zoomLevel = newZoomLevel;
		scaleX = TileBoundary.scaleX(zoomLevel);
		scaleY = TileBoundary.scaleY(zoomLevel, upperLeftX, upperLeftY);
		mapWidth = scaleX * screenWidth;
		mapHeight = scaleY * screenHeight;
		
	}

}
