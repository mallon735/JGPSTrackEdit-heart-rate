/**
 * 
 */
package jgpstrackedit.map.util;

import jgpstrackedit.data.Point;

/** A map extract, which is determined by the left upper coordinate and the zoom level
 * @author Hubert
 *
 */
public class MapExtract {
	
	private String name;
	private Point upperLeftBoundary;
	private int zoomLevel;

	public MapExtract() {
		
	}
	
	public MapExtract(String name, int zoomLevel, String lattitude, String longitude) {
		setName(name);
		setZoomLevel(zoomLevel);
		setUpperLeftBoundary(new Point(lattitude,longitude));
	}
	
	public MapExtract(String mapExtractName, int zoomLevel,
			Point upperLeftBoundary) {
		setName(mapExtractName);
		setZoomLevel(zoomLevel);
		setUpperLeftBoundary(upperLeftBoundary);
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the upperLeftBoundary
	 */
	public Point getUpperLeftBoundary() {
		return upperLeftBoundary;
	}
	/**
	 * @param upperLeftBoundary the upperLeftBoundary to set
	 */
	public void setUpperLeftBoundary(Point upperLeftBoundary) {
		this.upperLeftBoundary = upperLeftBoundary;
	}
	/**
	 * @return the zoomLevel
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}
	/**
	 * @param zoomLevel the zoomLevel to set
	 */
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	
	

}
