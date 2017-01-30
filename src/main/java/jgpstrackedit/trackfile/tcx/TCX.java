/**
 * 
 */
package jgpstrackedit.trackfile.tcx;

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
import jgpstrackedit.trackfile.gpxtrack.GPXTrackWriter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Hubert
 *
 */
public class TCX implements TrackFile{

	@Override
	public Track openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		// TODO Auto-generated method stub
		InputSource in = null;
		TcxHandlerImpl handler = new TcxHandlerImpl();
		TcxParser parser = new TcxParser(handler, null);
		in = new InputSource(new InputStreamReader(new FileInputStream(
				file)));
		parser.parse(in);
		Track track = handler.getTrack();
		track.setTrackFileType(getTypeDescription());
		if (track.getNumberPoints() == 0) {
			track = null;
		}
		if (track != null) {
			track.correct();
		}
		return track;
	}

	@Override
	public String getOpenReadyMessage() {
		// TODO Auto-generated method stub
		return "Garmin TCX imported.";
	}

	@Override
	public String getTrackFileExtension() {
		// TODO Auto-generated method stub
		return "tcx";
	}

	@Override
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return "Garmin TCX";
	}

	@Override
	public void saveTrack(Track track, File file)
			throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = new PrintWriter(
				              new BufferedWriter(
				            		  new OutputStreamWriter(
				            				  new FileOutputStream(file))));
		new GPXTrackWriter().print(track, out);
		out.close();
		
	}

	@Override
	public String getSaveReadyMessage() {
		// TODO Auto-generated method stub
		return "Garmin TCX as GPX Track saved.";
	}

}
