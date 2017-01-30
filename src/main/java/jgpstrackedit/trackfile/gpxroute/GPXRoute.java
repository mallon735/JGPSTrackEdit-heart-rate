/**
 * 
 */
package jgpstrackedit.trackfile.gpxroute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.xml.parsers.ParserConfigurationException;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Hubert
 *
 */
public class GPXRoute implements TrackFile {

	@Override
	public Track openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		// TODO Auto-generated method stub
		InputSource in = null;
		GPXRoute_HandlerImpl handler = new GPXRoute_HandlerImpl();
		GPXRoute_Parser parser = new GPXRoute_Parser(handler, null);
		in = new InputSource(new InputStreamReader(new FileInputStream(
				file)));
		parser.parse(in);
		Track track = handler.getTrack();
		track.setTrackFileType(getTypeDescription());
		return track;
	}

	@Override
	public String getOpenReadyMessage() {
		// TODO Auto-generated method stub
		return "Garmin GPX Route imported.";
	}

	@Override
	public String getTrackFileExtension() {
		// TODO Auto-generated method stub
		return "gpx";
	}

	@Override
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return "Garmin GPX Route";
	}

	@Override
	public void saveTrack(Track track, File file)
			throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = new PrintWriter(
				              new BufferedWriter(
				            		  new OutputStreamWriter(
				            				  new FileOutputStream(file))));
		new GPXRouteWriter().print(track, out);
		out.close();
		
	}

	@Override
	public String getSaveReadyMessage() {
		// TODO Auto-generated method stub
		return "Garmin GPX Route saved.";
	}

}
