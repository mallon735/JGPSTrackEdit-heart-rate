/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import jgpstrackedit.map.TileManager;


/**
 * @author Hubert
 *
 */
public class WebTileLoadCommand extends AbstractTileCommand {

	private Image image;
	
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		String urlString = TileManager.getCurrentTileManager().getTileURL(getTileNumber()) ;
		URL url;
		try {
			//System.out.println("URL loading: "+urlString);
			url = new URL(urlString);
			image = Toolkit.getDefaultToolkit().createImage(url);
			TileLoadEvent event = new TileLoadEvent();
			event.setImageLoaded(image);
			event.setTileNumber(getTileNumber());
			notifyTileLoadObservers(event);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("URL="+urlString);
			e.printStackTrace();
		}
		
	}


	
}
