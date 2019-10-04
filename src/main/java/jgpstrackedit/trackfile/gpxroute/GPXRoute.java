/**
 * 
 */
package jgpstrackedit.trackfile.gpxroute;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Hubert
 *
 */
public class GPXRoute implements TrackFile 
{
	private static Logger logger = LoggerFactory.getLogger(GPXRoute.class);

	@Override
	public List<Track> openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		GPXRoute_HandlerImpl handler = new GPXRoute_HandlerImpl();
		GPXRoute_Parser parser = new GPXRoute_Parser(handler, null);
		
		try(FileInputStream fis = new FileInputStream(file)) {
			InputSource in = new InputSource(new InputStreamReader(fis,"UTF-8"));
			parser.parse(in);
			return Arrays.asList(handler.getTrack());
		} catch(Exception e) {
			logger.error(String.format("Cannot open track [%s]", file.getAbsolutePath()), e);
			return Collections.emptyList();
		}
		
	}

	@Override
	public String getOpenReadyMessage() {
		return "Garmin GPX Route imported.";
	}

	@Override
	public String getTrackFileExtension() {
		return "gpx";
	}

	@Override
	public String getTypeDescription() {
		return "Garmin GPX Route";
	}

	@Override
	public void saveTrack(Track track, File file)
			throws FileNotFoundException, IOException {
		try(PrintWriter out = new PrintWriter(
				              new BufferedWriter(
				            		  new OutputStreamWriter(
				            				  new FileOutputStream(file))))) {
			new GPXRouteWriter().print(track, out);
			out.close();
		} catch(Exception e) {
			logger.error(String.format("Cannot write track [%s]", file.getAbsolutePath()), e);
		}
	}

	@Override
	public String getSaveReadyMessage() {
		return "Garmin GPX Route saved.";
	}

}
