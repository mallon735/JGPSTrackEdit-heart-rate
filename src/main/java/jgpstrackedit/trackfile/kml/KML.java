/**
 * 
 */
package jgpstrackedit.trackfile.kml;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Hubert
 * 
 */
public class KML implements TrackFile {
	private static Logger logger = LoggerFactory.getLogger(KML.class);
	
	@Override
	public List<Track> openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		final KmlHandlerImpl handler = new KmlHandlerImpl();
		final KmlParser parser = new KmlParser(handler, null);
		
		try(FileInputStream fis = new FileInputStream(file)) {
			InputSource in = new InputSource(new InputStreamReader(fis,"UTF-8"));
			parser.parse(in);
			
			final Track track = handler.getTrack();
			track.correct();
			return Arrays.asList(track);
		} catch(Exception e) {
			logger.error(String.format("Cannot open track [%s]", file.getAbsolutePath()), e);
			return Collections.emptyList();
		}
	}

	public List<Track> openTrack(URL url) throws 
			SAXException, ParserConfigurationException, IOException {
		final KmlHandlerImpl handler = new KmlHandlerImpl();
		final KmlParser parser = new KmlParser(handler, null);
		parser.parse(url);
		
		final Track track = handler.getTrack();
		track.correct();
		return Arrays.asList(track);
	}

	@Override
	public String getOpenReadyMessage() {
		return "KML Track imported.";
	}

	@Override
	public String getTrackFileExtension() {
		return "kml";
	}

	@Override
	public String getTypeDescription() {
		return "KML Track";
	}

	@Override
	public void saveTrack(Track track, File file) throws FileNotFoundException,
			IOException {
		try(FileOutputStream fos = new FileOutputStream(file)) {
			final PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(fos)));
			new KMLWriter().print(track, out);
			out.close();
		} catch(Exception e) {
			logger.error(String.format("Cannot write track [%s]", file.getAbsolutePath()), e);
		}
	}

	@Override
	public String getSaveReadyMessage() {
		return "KML Track saved.";
	}

}
