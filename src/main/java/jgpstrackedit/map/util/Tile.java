/**
 * 
 */
package jgpstrackedit.map.util;

import java.awt.*;


/** The Tile class represents a tile of the map. Usually a tile consist of an image with quadratic 
 * dimensions 256px.
 * @author Hubert
 *
 */
public class Tile {

	public static final int PIXEL_PER_TILE = 256;

	private TileNumber tileNumber;
	private Image tileImage = null;
	private TileState tileState = TileState.unknown;
	private TileBoundary tileBoundary;
	
	/**
	 * @param tileBoundary the tileBoundary to set
	 */
	protected void setTileBoundary(TileBoundary tileBoundary) {
		this.tileBoundary = tileBoundary;
	}
	/**
	 * @return the tileBoundary
	 */
	public TileBoundary getTileBoundary() {
		return tileBoundary;
	}
	
	/**
	 * Creates a Tile. A map consist of a number of tiles, depending on the zoom level. 
	 * @param tileNumber
	 * @see http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
	 */
	public Tile(TileNumber tileNumber) {
		setTileNumber(tileNumber);
		setTileBoundary(TileBoundary.getTileBoundary(tileNumber));
	}
	/**
	 * @return the tileNumber
	 */
	public TileNumber getTileNumber() {
		return tileNumber;
	}
	/**
	 * @param tileNumber the tileNumber to set
	 */
	protected void setTileNumber(TileNumber tileNumber) {
		this.tileNumber = tileNumber;
	}
	/**
	 * @return the tileImage
	 */
	public synchronized Image getTileImage() {
		return tileImage;
	}
	/**
	 * @param tileImage the tileImage to set
	 */
	public synchronized void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}
	/**
	 * @return the tileState
	 */
	public synchronized TileState getTileState() {
		return tileState;
	}
	/**
	 * @param tileState the tileState to set
	 */
	public synchronized void setTileState(TileState tileState) {
		this.tileState = tileState;
	}



	
}
