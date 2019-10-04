/**
 * 
 */
package jgpstrackedit.trackfile.asc;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.TrackFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hubert
 *
 */
public class ASC implements TrackFile 
{
	private static Logger logger = LoggerFactory.getLogger(ASC.class);

	@Override
	public List<Track> openTrack(File file) throws FileNotFoundException,
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
		} catch(Exception e) {
			logger.error(String.format("Cannot open track [%s]", file.getAbsolutePath()), e);
		}
		
		track.setValid(true);
		return Arrays.asList(track);
	}

	@Override
	public String getOpenReadyMessage() {
		return "ASC Track imported.";
	}

	@Override
	public String getTrackFileExtension() {
		return "asc";
	}

	@Override
	public String getTypeDescription() {
		return "ASC Track";
	}

	@Override
	public void saveTrack(Track track, File file) throws FileNotFoundException,
			IOException {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file))))) {
			new ASCWriter().print(track, out);
			out.close();
		} catch(Exception e) {
			logger.error(String.format("Cannot write track [%s]", file.getAbsolutePath()), e);
		}
	}

	@Override
	public String getSaveReadyMessage() {
		return "ASC Track saved.";
	}

}
