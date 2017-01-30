/**
 * 
 */
package jgpstrackedit.map.util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

/**
 * @author Hubert
 * 
 */
public class ImageConverter {

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null),
					image.getHeight(null), transparency);
			if (bimage == null) {
				// Create a buffered image using the default color model
				int type = BufferedImage.TYPE_INT_RGB;
				bimage = new BufferedImage(image.getWidth(null),
						image.getHeight(null), type);
			}
		} catch (HeadlessException e) {
		} catch (IllegalArgumentException e) {// No screen
			System.out
					.println("Converter(line 45): IllegalArgumentException cought");
		}

		// Copy image to buffered image
		if (bimage != null) {
			Graphics g = bimage.createGraphics();

			// Paint the image onto the buffered image
			g.drawImage(image, 0, 0, null);
			g.dispose();
		}

		return bimage;
	}

	public static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			return ((BufferedImage) image).getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		return pg.getColorModel().hasAlpha();
	}
}
