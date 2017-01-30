/**
 * 
 */
package jgpstrackedit.map.util;

import jgpstrackedit.config.SystemConfig;

/**
 * @author hlutnik
 * 
 */
public class TileNumber implements Comparable {

	private int zoom;
	private int x;
	private int y;

	/**
	 * creates a new tile number.
	 * 
	 * @param zoom
	 *            zoom level
	 * @param x
	 *            x-coordinate of tile number
	 * @param y
	 *            y-cordinate of tile number
	 */
	public TileNumber(int zoom, int x, int y) {
		setZoom(zoom);
		setX(x);
		setY(y);
	}

	/**
	 * @return the zoom
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * @param zoom
	 *            the zoom to set
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "/" + zoom + "/" + x + "/" + y;
	}

	public String toFileName() {
		return SystemConfig.dirSeparator() + zoom + SystemConfig.dirSeparator() + x + SystemConfig.dirSeparator() + y;
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		TileNumber t = (TileNumber) obj;
		return (getY() == t.getY()) && (getX() == t.getX())
				&& (getZoom() == t.getZoom());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return (int) ((getZoom() * 1000 + getY() + getX()) % 30011);
	}

    /**
     * Returns tile number of given point. See http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
     * for more information.
     * @param zoom zoom level 
     * @param lon longitude of point
     * @param lat latitude of point
     * @return
     */
	public static TileNumber getTileNumber(final int zoom, final double lon, final double lat) {
		int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		int ytile = (int) Math
				.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
						/ Math.cos(Math.toRadians(lat)))
						/ Math.PI)
						/ 2 * (1 << zoom));
		return new TileNumber(zoom, xtile, ytile);
	}

	
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return toString().compareTo(arg0.toString());
	}

}
