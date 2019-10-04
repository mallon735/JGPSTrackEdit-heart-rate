/**
 * 
 */
package jgpstrackedit.trackfile.tcx;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;
import jgpstrackedit.trackfile.XmlParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

/**
 * TCX Track file support.
 *  
 * @author Hubert
 *
 */
public class TCX implements TrackFile
{
	@Override
	public List<Track> openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		final XmlParser parser = new XmlParser(new TcxTagHandler());
		final InputSource in = new InputSource(new InputStreamReader(new FileInputStream(
				file)));
		parser.parse(in);
		
		final List<Track> tracks = parser.getTrack();
		tracks.stream().forEach(track -> track.correct());
		
		return tracks;
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
