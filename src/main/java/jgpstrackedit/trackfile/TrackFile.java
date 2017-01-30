/**
 * 
 */
package jgpstrackedit.trackfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jgpstrackedit.data.Track;

/**
 * A class which is able to open a trackfile must implement this interface. The interface consists of methods
 * useful for opening and saving of trackfiles.
 * 
 * @author Hubert
 *
 */
public interface TrackFile {
	
	/**
	 * Opens the given trackfile.
	 * 
	 * @param file trackfile to be opened
	 * @return track Object containing the track
	 * @throws FileNotFoundException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public Track openTrack(File file) throws 	FileNotFoundException,
													SAXException,
													ParserConfigurationException,
													IOException;
	
    /**
     * Returns the message which should be shown in the state line after successfully opening a 
     * trackfile
     * @return the message
     */
	public String getOpenReadyMessage();
    
    /** Returns the file extension (type) of the trackfile. Example: "gpx"
     * 
     * @return file extension of this track file
     */
	public String getTrackFileExtension();
	
    /**
     * Returns a description of the file format. Example: "Garmin GPX Track"
     * @return description
     */
	public String getTypeDescription();

	/**
	 * Saves the given Track.
	 * 
	 * @param track track to be saved
	 * @param file trackfile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveTrack(Track track, File file) throws FileNotFoundException,IOException;
	
    /**
     * Returns the message which should be shown in the state line after successfully saving a 
     * trackfile
     * @return the message
     */
	public String getSaveReadyMessage();


}
