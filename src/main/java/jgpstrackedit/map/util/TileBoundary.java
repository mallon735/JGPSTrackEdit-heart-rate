/**
 * 
 */
package jgpstrackedit.map.util;


/**
 * @author Hubert
 * 
 */
public class TileBoundary {
	private double north;
	private double south;
	private double east;
	private double west;

	/**
	 * @return the north
	 */
	public double getNorth() {
		return north;
	}

	/**
	 * @param north
	 *            the north to set
	 */
	public void setNorth(double north) {
		this.north = north;
	}

	/**
	 * @return the south
	 */
	public double getSouth() {
		return south;
	}

	/**
	 * @param south
	 *            the south to set
	 */
	public void setSouth(double south) {
		this.south = south;
	}

	/**
	 * @return the east
	 */
	public double getEast() {
		return east;
	}

	/**
	 * @param east
	 *            the east to set
	 */
	public void setEast(double east) {
		this.east = east;
	}

	/**
	 * @return the west
	 */
	public double getWest() {
		return west;
	}

	/**
	 * @param west
	 *            the west to set
	 */
	public void setWest(double west) {
		this.west = west;
	}

	/**
	 * Returns the longitude of the center of the tile
	 * @return center longitude
	 */
	public double getCenterLongitude() {
		return (west+east)/2.0;
	}
	
	/**
	 * Returns the latitude of the center of the tile
	 * @return center latitude
	 */
	public double getCenterLatitude() {
		return (north+south)/2.0;
	}
	
	public static TileBoundary getTileBoundary(TileNumber tileNumber) {
		TileBoundary bb = new TileBoundary();
		bb.north = tile2lat(tileNumber.getY(), tileNumber.getZoom());
		bb.south = tile2lat(tileNumber.getY() + 1, tileNumber.getZoom());
		bb.west = tile2lon(tileNumber.getX(), tileNumber.getZoom());
		bb.east = tile2lon(tileNumber.getX() + 1, tileNumber.getZoom());
		return bb;
	}

	static double tile2lon(int x, int z) {
		return x / Math.pow(2.0, z) * 360.0 - 180;
	}

	static double tile2lat(int y, int z) {
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WNES ["+west+";"+north+"]["+east+";"+south+"]";
	}

	/**
	 * Calculates the x scale (longitude degree per pixel) of the current zoom
	 * level
	 * 
	 * @param zoomLevel
	 *            zoom level
	 * @return the scale
	 */
	public static double scaleX(int zoomLevel) {
		return (360.0 / (1 << zoomLevel)) / Tile.PIXEL_PER_TILE;
	}

	/**
	 * Calculates the y scale (latitude degree per pixel) of the current zoom
	 * level
	 * 
	 * @param zoomLevel
	 *            zoom level
	 * @return the scale
	 */
	public static double scaleY(TileNumber tileNumber) {
		TileBoundary tb = getTileBoundary(tileNumber);
		int pixel_per_tile = Tile.PIXEL_PER_TILE;
		/*
		switch (tileNumber.getZoom()) {
		case 4:
			pixel_per_tile+=20;
			break;
		case 5:
			pixel_per_tile+=10;
			break;
		case 6:
			pixel_per_tile+=7;
			break;
		case 7:
			pixel_per_tile+=2;
			break;
		case 8:
			pixel_per_tile+=1;
			break;
		}
		*/
		return (tb.getNorth() - tb.getSouth()) / pixel_per_tile;
	}

    /**
     * Calculates the y scale (latitude degree per pixel) of the current zoom
	 * level
	 * @param zoomLevel zoom level
     * @param longitude longitude
     * @param latitude latitude
     * @return
     */
	public static double scaleY(int zoomLevel, double longitude, double latitude) {
		return scaleY(TileNumber.getTileNumber(zoomLevel,longitude,latitude));
		
	}
	
	/*public static double scaleY(int zoomLevel,double longitude, double latitude) {
		return (85.0511*2 / (1 << zoomLevel)) / Tile.PIXEL_PER_TILE;
	}*/

}
