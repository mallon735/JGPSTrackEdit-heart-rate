/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jgpstrackedit.map.util.ImageConverter;

/**
 * @author Hubert
 * 
 */
public class TileSaveCommand extends AbstractDiskTileCommand 
{
	private static Logger logger = LoggerFactory.getLogger(TileSaveCommand.class);
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
		String dirPath = getBaseDirectory() + File.separator
				+ getTileNumber().getZoom() + File.separator
				+ getTileNumber().getX();
		File dir = new File(dirPath);
		dir.mkdirs();
		String fileName = dirPath + File.separator
				+ getTileNumber().getY() + ".png";
		BufferedImage bufferedImage = ImageConverter
				.toBufferedImage(getTileImage());
		if (bufferedImage != null) {
			File file = new File(fileName);
			try {
				ImageIO.write(bufferedImage, "png", file);
			} catch (IOException e) {
				logger.error(String.format("Cannot write png file \"%s\"" + file), e);
			}
		} else {
			logger.warn("File not saved due to null-image: "+fileName);
		}

	}

}
