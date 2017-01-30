/**
 * 
 */
package jgpstrackedit.trackfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jgpstrackedit.control.UIController;
import jgpstrackedit.data.Track;

/**
 * Manages opening and saving of trackfiles.
 * 
 * @author Hubert
 * 
 */
public class TrackFileManager {
	
	private static String lastMessage;

	private static LinkedList<TrackFile> trackFiles = new LinkedList<TrackFile>();

	/**
	 * Opens the given trackfile. The determination of the current trackfile
	 * format is done using a try and error principle: In sequence each of the
	 * stored TrackFile object is used to open the trackfile until the opening
	 * was successfull.
	 * 
	 * @param file
	 *            trackfile to be opened
	 * @return track Object containing the track
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Track openTrack(File file) throws TrackFileException {
		Track track = null;
		setLastMessage("");
		forloop: for (TrackFile trackFile : trackFiles) {
			try {
				System.out.println("\nTrackFileManager: trying to import "+trackFile.getTypeDescription());
				track = trackFile.openTrack(file);
				if (track != null && track.isValid())
					break forloop;
				setLastMessage(trackFile.getOpenReadyMessage());
			} catch (FileNotFoundException e) {
				throw new TrackFileException("Trackfile "
						+ file.getAbsolutePath() + " not found!", e);
			} catch (SAXException ex) {
				// DEBUG
				ex.printStackTrace();
			} catch (ParserConfigurationException ex) {
				// DEBUG
				ex.printStackTrace();
			} catch (IOException e) {
				throw new TrackFileException("General file error", e);
			} catch (NullPointerException e) {
				// DEBUG
				e.printStackTrace();
			}

		}
		if (track == null) {
			throw new TrackFileException("Unknown trackfile type");
		} else {
			System.out.println("TrackFileManager: "+track.getTrackFileType()+" imported.");
		}
		return track;

	}
	
	/**
	 * Saves the given track.
	 * 
	 * @param track track to be saved
	 * @param file trackfile for track
	 * @param trackFileType type of trackfile (as is returned by the TrackFile.getTypeDescription() method)
	 * @throws TrackFileException 
	 */
	public static void saveTrack(Track track, File file, String trackFileType) throws TrackFileException {
		for (TrackFile trackFile : trackFiles) {
			if (trackFile.getTypeDescription().equals(trackFileType)) {
				try {
					String fileName = file.getAbsolutePath();
					System.out.println("TrackFileManager.saveTrack: "+file.getAbsolutePath());
					if (!fileName.endsWith(trackFile.getTrackFileExtension())) {
						fileName = fileName + "." + trackFile.getTrackFileExtension();
						file = new File(fileName);
					}
					System.out.println("                            "+file.getAbsolutePath());
					trackFile.saveTrack(track, file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					throw new TrackFileException("Trackfile "+file.getAbsolutePath()+" could not be created",e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new TrackFileException("Error writing to trackfile "+file.getAbsolutePath(),e);
				}
			}
		}
		
	}

	/**
	 * Adds a TrackFile object, capable of opening and saving of a dedicated
	 * trackfile format.
	 * 
	 * @param trackFile
	 *            TrackFile object
	 */
	public static void addTrackFile(TrackFile trackFile) {
		trackFiles.add(trackFile);
	}

	public static String getLastMessage() {
		return lastMessage;
	}

	public static void setLastMessage(String lastMessage) {
		TrackFileManager.lastMessage = lastMessage;
	}
	
	public static List<FileNameExtensionFilter> getFileNameExtensionFilters() {
		LinkedList<FileNameExtensionFilter> filters = new LinkedList<FileNameExtensionFilter>();
		for (TrackFile trackFile : trackFiles) {
		     filters.add(new FileNameExtensionFilter(trackFile.getTypeDescription(), trackFile.getTrackFileExtension()));
		}
		return filters;
	}

}
