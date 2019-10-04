/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;


/**
 * @author Hubert
 * 
 */
public class DiskTileLoadCommand extends AbstractDiskTileCommand implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(DiskTileLoadCommand.class);
	private TileLoadEvent event;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jgpstrackedit.map.AbstractTileCommand#doAction()
	 */
	@Override
	public void doAction() {
		new Thread(this).start();

	}

	protected void doActionIntern() {
		String fileName = getBaseDirectory() + File.separator + getTileNumber().getZoom()
				+ File.separator + getTileNumber().getX() + File.separator + getTileNumber().getY()
				+ ".png";
		// logger.info("File loading: " + fileName);
		Image image = Toolkit.getDefaultToolkit().createImage(fileName);
		MediaTracker tracker = new MediaTracker(new Label("Dummy"));
		tracker.addImage(image, 1);
		try {
			tracker.waitForID(1);
		} catch (InterruptedException e) {
			logger.warn("Error while waiting for media tracker!",e);
		}
		event = new TileLoadEvent();
		event.setImageLoaded(image);
		event.setTileNumber(getTileNumber());
		notifyTileLoadObservers(event);	
	}
	
	@Override
	public void run() {
		doActionIntern();
	}
}
