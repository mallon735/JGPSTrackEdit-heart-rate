/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import jgpstrackedit.map.util.TileNumber;

import java.awt.*;

/**
 * @author hlutnik
 *
 */
public class TileLoadEvent {
	
	private Image imageLoaded;
	private TileNumber tileNumber;
	/**
	 * @return the imageLoaded
	 */
	public Image getImageLoaded() {
		return imageLoaded;
	}
	/**
	 * @param imageLoaded the imageLoaded to set
	 */
	public void setImageLoaded(Image imageLoaded) {
		this.imageLoaded = imageLoaded;
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
	public void setTileNumber(TileNumber tileNumber) {
		this.tileNumber = tileNumber;
	}

	
}
