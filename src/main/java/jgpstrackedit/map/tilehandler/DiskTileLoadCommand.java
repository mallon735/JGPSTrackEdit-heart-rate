/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;

import jgpstrackedit.config.SystemConfig;


/**
 * @author Hubert
 * 
 */
public class DiskTileLoadCommand extends AbstractDiskTileCommand 
                                 implements Runnable {


	private TileLoadEvent event;
	/*
	 * (non-Javadoc)
	 * 
	 * @see jgpstrackedit.map.AbstractTileCommand#doAction()
	 */
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		new Thread(this).start();

	}

	protected void doActionIntern() {
		String fileName = getBaseDirectory() + SystemConfig.dirSeparator() + getTileNumber().getZoom()
				+ SystemConfig.dirSeparator() + getTileNumber().getX() + SystemConfig.dirSeparator() + getTileNumber().getY()
				+ ".png";
		//System.out.println("File loading: "+fileName);
		Image image = Toolkit.getDefaultToolkit().createImage(fileName);
		MediaTracker tracker = new MediaTracker(new Label("Dummy"));
		tracker.addImage(image, 1);
		try {
			tracker.waitForID(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		event = new TileLoadEvent();
		event.setImageLoaded(image);
		event.setTileNumber(getTileNumber());
		notifyTileLoadObservers(event);
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		doActionIntern();

	}

	/* Old Version
	public void doAction() {
		// TODO Auto-generated method stub
		String fileName = getBaseDirectory() + SystemConfig.dirSeparator() + getTileNumber().getZoom()
				+ SystemConfig.dirSeparator() + getTileNumber().getX() + SystemConfig.dirSeparator() + getTileNumber().getY()
				+ ".png";
		//System.out.println("File loading: "+fileName);
		Image image = Toolkit.getDefaultToolkit().createImage(fileName);
		event = new TileLoadEvent();
		event.setImageLoaded(image);
		event.setTileNumber(getTileNumber());
		notifyTileLoadObservers(event);
		new Thread(this).start();

	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyTileLoadObservers(event);
	}
	
	*/



}
