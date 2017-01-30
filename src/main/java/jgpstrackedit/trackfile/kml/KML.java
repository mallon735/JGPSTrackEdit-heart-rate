/**
 * 
 */
package jgpstrackedit.trackfile.kml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;
import jgpstrackedit.trackfile.gpxtrack.GPXTrackWriter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Hubert
 * 
 */
public class KML implements TrackFile {

	@Override
	public Track openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		// TODO Auto-generated method stub
		InputSource in = null;
		KmlHandlerImpl handler = new KmlHandlerImpl();
		KmlParser parser = new KmlParser(handler, null);
		in = new InputSource(new InputStreamReader(new FileInputStream(file)));
		parser.parse(in);
		Track track = handler.getTrack();
		track.setTrackFileType(getTypeDescription());
		return track;
	}

	public Track openTrack(URL url) throws 
			SAXException, ParserConfigurationException, IOException {
		// TODO Auto-generated method stub
		KmlHandlerImpl handler = new KmlHandlerImpl();
		KmlParser parser = new KmlParser(handler, null);
		parser.parse(url);
		Track track = handler.getTrack();
		track.setTrackFileType(getTypeDescription());
		return track;
	}

	@Override
	public String getOpenReadyMessage() {
		// TODO Auto-generated method stub
		return "KML Track imported.";
	}

	@Override
	public String getTrackFileExtension() {
		// TODO Auto-generated method stub
		return "kml";
	}

	@Override
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return "KML Track";
	}

	@Override
	public void saveTrack(Track track, File file) throws FileNotFoundException,
			IOException {
		// TODO Auto-generated method stub
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file))));
		new KMLWriter().print(track, out);
		out.close();

	}

	@Override
	public String getSaveReadyMessage() {
		// TODO Auto-generated method stub
		return "KML Track saved.";
	}

}
