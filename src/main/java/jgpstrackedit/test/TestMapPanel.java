package jgpstrackedit.test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;
import jgpstrackedit.map.TileManager;
import jgpstrackedit.map.util.MapObserver;
import jgpstrackedit.map.util.TileNumber;

public class TestMapPanel extends JPanel implements MapObserver, ImageObserver {

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
		System.out.println("TestPanel: paintComponent "+image);
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
		// TODO Auto-generated method stub
		System.out.println("TestPanel: mapTilesUpdated");
		/*
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		repaint();

	}

	@Override
	public boolean imageUpdate(Image arg0, int infoflags, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		boolean loaded = (infoflags & ImageObserver.ALLBITS) > 0;
		if (loaded) {
			System.out.println("ImageObserver");
			repaint();
		}

		return !loaded;
	}

}
