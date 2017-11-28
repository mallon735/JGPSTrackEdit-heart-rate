/**
 * 
 */
package jgpstrackedit.trackfile.asc;

import java.io.BufferedReader;
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

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;

import org.xml.sax.SAXException;

/**
 * @author Hubert
 *
 */
public class ASC implements TrackFile {

	@Override
	public Track openTrack(File file) throws FileNotFoundException,
			SAXException, ParserConfigurationException, IOException {
		final Track track = new Track();
		try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line = in.readLine();
			while (line != null) {
				String[] elements = line.split(",");
				if (elements.length < 2) {
					throw new SAXException("Illegal ASC-Format");
				}
				Point point = new Point(elements[0],elements[1]);
				if((elements.length > 2) && (elements[2].trim().length() > 0)) {
					point.setInformation(elements[2].trim());
				}
				track.add(point);
				line = in.readLine();
			}
		}
		
		track.setValid(true);
		
		return track;
	}

	@Override
	public String getOpenReadyMessage() {
		// TODO Auto-generated method stub
		return "ASC Track imported.";
	}

	@Override
	public String getTrackFileExtension() {
		// TODO Auto-generated method stub
		return "asc";
	}

	@Override
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return "ASC Track";
	}

	@Override
	public void saveTrack(Track track, File file) throws FileNotFoundException,
			IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file))));
		new ASCWriter().print(track, out);
		out.close();
		
	}

	@Override
	public String getSaveReadyMessage() {
		// TODO Auto-generated method stub
		return "ASC Track saved.";
	}

}
