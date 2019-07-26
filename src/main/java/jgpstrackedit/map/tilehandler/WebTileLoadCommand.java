/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import jgpstrackedit.map.TileManager;


/**
 * @author Hubert
 *
 */
public class WebTileLoadCommand extends AbstractTileCommand 
{
	private Image image;
	
	@Override
	public void doAction() {
		String urlString = TileManager.getCurrentTileManager().getTileURL(getTileNumber()) ;
		
		try {	
			image = ImageIO.read(new URL(urlString));
			TileLoadEvent event = new TileLoadEvent();
			event.setImageLoaded(image);
			event.setTileNumber(getTileNumber());
			notifyTileLoadObservers(event);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch(Exception exception) {
			System.err.println(String.format("Exception while reading %s! %s", urlString, exception.getMessage()));
			if(exception.getCause() != null && exception.getCause().getMessage() != null) {
				System.err.println(String.format("    %s", exception.getCause().getMessage()));
				if(exception.getCause().getCause() != null && exception.getCause().getCause().getMessage() != null) {
					System.err.println(String.format("    %s", exception.getCause().getCause().getMessage()));
				}
			}
		}
		
	}
}
