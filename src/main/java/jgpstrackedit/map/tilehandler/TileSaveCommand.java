/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jgpstrackedit.config.SystemConfig;
import jgpstrackedit.map.util.ImageConverter;

/**
 * @author Hubert
 * 
 */
public class TileSaveCommand extends AbstractDiskTileCommand {

	private Image tileImage;

	/**
	 * @return the tileImage
	 */
	public Image getTileImage() {
		return tileImage;
	}

	/**
	 * @param tileImage
	 *            the tileImage to set
	 */
	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jgpstrackedit.map.AbstractTileCommand#doAction()
	 */
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		String dirPath = getBaseDirectory() + SystemConfig.dirSeparator()
				+ getTileNumber().getZoom() + SystemConfig.dirSeparator()
				+ getTileNumber().getX();
		File dir = new File(dirPath);
		dir.mkdirs();
		String fileName = dirPath + SystemConfig.dirSeparator()
				+ getTileNumber().getY() + ".png";
		BufferedImage bufferedImage = ImageConverter
				.toBufferedImage(getTileImage());
		// System.out.println("File saving: "+fileName);
		if (bufferedImage != null) {
			File file = new File(fileName);
			try {
				ImageIO.write(bufferedImage, "png", file);
				// System.out.println("File saved: "+fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("File not saved due to null-image: "+fileName);
			
		}

	}

}
