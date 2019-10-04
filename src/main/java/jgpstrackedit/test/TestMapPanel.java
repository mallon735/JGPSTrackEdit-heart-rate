package jgpstrackedit.test;

import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.util.MapObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class TestMapPanel extends JPanel implements MapObserver, ImageObserver {

	private static final Logger logger = LoggerFactory.getLogger(TestMapPanel.class);
	private TileManager tileManager;
	/*
	 * (non-Javadoc)
	 * 
	 * @see jgpstrackedit.map.MapObserver#mapTilesUpdated()
	 */
	private Image image = null;

	/**
	 * @return the image
	 */
	protected Image getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	protected void setImage(Image image) {
		this.image = image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		//image = tileManager.getTileImage(new TileNumber(15, 17683, 11577));
		logger.info("TestPanel: paintComponent "+image);
		g2D.drawImage(image, 0, 0, this);
		//g2D.drawRect(20,20,100,100);
	}

	/**
	 * Create the panel.
	 */
	public TestMapPanel(TileManager tileManager) {
		this.tileManager = tileManager;
		tileManager.addMapObserver(this);
	}

	@Override
	public void mapTilesUpdated() {
		logger.info("TestPanel: mapTilesUpdated");
		repaint();

	}

	@Override
	public boolean imageUpdate(Image arg0, int infoflags, int arg2, int arg3,
			int arg4, int arg5) {
		boolean loaded = (infoflags & ImageObserver.ALLBITS) > 0;
		if (loaded) {
			logger.info("ImageObserver");
			repaint();
		}

		return !loaded;
	}

}
