/**
 * 
 */
package jgpstrackedit.trackfile.gpxtrack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;

/**
 * @author Hubert
 *
 */
public class GPXTrack implements TrackFile
{
	private static Logger logger = LoggerFactory.getLogger(GPXTrack.class);

	@Override
	public List<Track> openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		GPXTrackHandlerImpl handler = new GPXTrackHandlerImpl();
		GPXTrackParser parser = new GPXTrackParser(handler, null);
		
		try(FileInputStream fis = new FileInputStream(file)) {
			InputSource in = new InputSource(new InputStreamReader(fis, "UTF-8"));
			parser.parse(in);
			return Arrays.asList(handler.getTrack());
		} catch(Exception e) {
			logger.error(String.format("Cannot open track [%s]", file.getAbsolutePath()), e);
			return Collections.emptyList();
		}
	}

	@Override
	public String getOpenReadyMessage() {
		return "Garmin GPX Track imported.";
	}

	@Override
	public String getTrackFileExtension() {
		return "gpx";
	}

	@Override
	public String getTypeDescription() {
		return "Garmin GPX Track";
	}

	@Override
	public void saveTrack(Track track, File file)
			throws FileNotFoundException, IOException {
		try(PrintWriter out = new PrintWriter(
				              new BufferedWriter(
				            		  new OutputStreamWriter(
				            				  new FileOutputStream(file))))) {
			new GPXTrackWriter().print(track, out);
			out.close();
		} catch(Exception e) {
			logger.error(String.format("Cannot write track [%s]", file.getAbsolutePath()), e);
		}
		
	}

	@Override
	public String getSaveReadyMessage() {
		return "Garmin GPX Track saved.";
	}

}
