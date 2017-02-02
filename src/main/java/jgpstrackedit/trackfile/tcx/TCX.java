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
import jgpstrackedit.trackfile.XmlParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TCX Track file support.
 *  
 * @author Hubert
 *
 */
public class TCX implements TrackFile
{
	@Override
	public Track openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		final XmlParser parser = new XmlParser(new TcxTagHandler());
		final InputSource in = new InputSource(new InputStreamReader(new FileInputStream(
				file)));
		parser.parse(in);
		
		Track track = parser.getTrack();
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
		return "Garmin TCX imported.";
	}

	@Override
	public String getTrackFileExtension() {
		return "tcx";
	}

	@Override
	public String getTypeDescription() {
		return "Garmin TCX";
	}

	@Override
	public void saveTrack(Track track, File file)
			throws FileNotFoundException, IOException {
		PrintWriter out = new PrintWriter(
				              new BufferedWriter(
				            		  new OutputStreamWriter(
				            				  new FileOutputStream(file))));
		new TcxTrackWriter().print(track, out);
		out.close();
	}

	@Override
	public String getSaveReadyMessage() {
		return "Garmin TCX saved.";
	}
}
