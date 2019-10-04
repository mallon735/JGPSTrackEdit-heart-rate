/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import jgpstrackedit.map.TileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author Hubert
 *
 */
public class WebTileLoadCommand extends AbstractTileCommand 
{
	private static final String USER_AGENT_VALUE = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0";
	private static Logger logger = LoggerFactory.getLogger(WebTileLoadCommand.class);
	
	@Override
	public void doAction() {
		String urlString = TileManager.getCurrentTileManager().getTileURL(getTileNumber()) ;
		
		try {	
			final URL url = new URL(urlString);
			final HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent", USER_AGENT_VALUE);
			final Image image = ImageIO.read(httpcon.getInputStream());
			TileLoadEvent event = new TileLoadEvent();
			event.setImageLoaded(image);
			event.setTileNumber(getTileNumber());
			notifyTileLoadObservers(event);
		} catch (MalformedURLException e) {
			logger.error(String.format("Incorrect URL! (%s)", urlString), e);
		} catch(Exception exception) {
			logger.error(String.format("Exception while reading %s! %s", urlString, exception.getMessage()));
			if(exception.getCause() != null && exception.getCause().getMessage() != null) {
				logger.error(String.format("    %s", exception.getCause().getMessage()));
				if(exception.getCause().getCause() != null && exception.getCause().getCause().getMessage() != null) {
					logger.error(String.format("    %s", exception.getCause().getCause().getMessage()));
				}
			}
		}
		
	}
}