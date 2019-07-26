/**
 * 
 */
package jgpstrackedit.trackfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.kml.KML;

/**
 * Manages opening and saving of trackfiles.
 * 
 * @author Hubert
 * 
 */
public class TrackFileManager 
{
	private static Logger logger = LoggerFactory.getLogger(TrackFileManager.class);

	private static boolean automaticColors = Configuration.getProperty("AUTOMATIC_COLORS").equals("1");
	private static String lastMessage = null;
	private static LinkedList<TrackFile> trackFiles = new LinkedList<TrackFile>();
	
	/**
	 * Opens a KML Track from url.
	 * 
	 * @param url url to a kml track resource
	 * @return Track
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws TrackFileException 
	 */
	public static List<Track> openKmlTrack(URL url) throws SAXException, ParserConfigurationException, IOException, TrackFileException {
		setLastMessage(null);
		final KML kmlImporter = new KML();
		final List<Track> tracks = kmlImporter.openTrack(url);
		
		for(Track track : tracks) {
			String fileName = "Imported KML File.kml";
			if(track.getName() != null) {
				fileName = track.getName() + ".kml";
			}
			File file = new File(fileName);
			updateFileAndType(track, kmlImporter, file.getAbsolutePath());
			postProcessTrack(track, file);
		}
		
		return tracks;
	}

	/**
	 * Opens the given trackfile. The determination of the current trackfile
	 * format is done using a try and error principle: In sequence each of the
	 * stored TrackFile object is used to open the trackfile until the opening
	 * was successfull.
	 * 
	 * @param file
	 *            trackfile to be opened
	 * @return track Object containing the track
	 * @throws TrackFileException
	 */
	public static List<Track> openTrack(File file) throws TrackFileException {
		List<Track> tracks = null;
		setLastMessage(null);
		
		forloop: for (TrackFile trackFile : trackFiles) {
			try {
				logger.info("TrackFileManager: trying to import "+trackFile.getTypeDescription());
				tracks = trackFile.openTrack(file);
				if (containsValidTracks(tracks)) {
					updateFileAndType(tracks, trackFile, file.getAbsolutePath());
					break forloop;
				}
				setLastMessage(trackFile.getOpenReadyMessage());
			} catch (FileNotFoundException e) {
				throw new TrackFileException("Trackfile "
						+ file.getAbsolutePath() + " not found!", e);
			} catch (SAXException ex) {
				logger.warn(String.format("Cannot open track %s", file.toString()), ex);
			} catch (ParserConfigurationException ex) {
				logger.warn(String.format("Cannot open track %s", file.toString()), ex);
			} catch (IOException e) {
				throw new TrackFileException("General file error", e);
			} catch (NullPointerException e) {
				logger.warn(String.format("Cannot open track %s", file.toString()), e);
			}

		}
		
		for(Track track : tracks) {
			postProcessTrack(track, file);
		}
		
		return tracks;
	}
	
	private static boolean containsValidTracks(List<Track> tracks) {
		boolean valid = false;
		if(tracks.size() > 0) {
			valid = tracks.stream()
						.allMatch(track -> track.isValid() == true);
		}
		return valid;
	}

	private static Track postProcessTrack(Track track, File file) throws TrackFileException {
		if (track == null) {
			throw new TrackFileException("Unknown trackfile type");
		} 
		
		if (track.getPoints() == null) {
			throw new TrackFileException("Track import failed! No points!");
		}
		
		if (track.getPoints().size() < 2) {
			throw new TrackFileException(String.format("Track import failed! Current number of points: %d!", track.getPoints().size()));
		}
		
		logger.info(String.format("TrackFileManager: \"%s\" (%s) imported!", file.toString(), track.getTrackFileType()));
		
		if(track.getName() == null) {
			track.setName(file.getName());
		}
		
		if(automaticColors) {
			track.assignColor();
		}
		
		track.setModified(false);
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
		final TrackFile trackFile = getTrackFileObject(track, trackFileType);
		
		if(trackFile == null) {
			throw new TrackFileException("Can't save track! No file type specified!");
		}
		
		try {
			String fileName = file.getAbsolutePath();
			logger.info("TrackFileManager.saveTrack: " + file.getAbsolutePath());
			if (!fileName.endsWith(trackFile.getTrackFileExtension())) {
				fileName = fileName + "." + trackFile.getTrackFileExtension();
				file = new File(fileName);
			}
			
			updateFileAndType(track, trackFile, file.getAbsolutePath());
			logger.info("TrackFileManager: file name and extension updated! " + file.getAbsolutePath());
			trackFile.saveTrack(track, file);
		} catch (FileNotFoundException e) {
			throw new TrackFileException("Trackfile "+file.getAbsolutePath()+" could not be created",e);
		} catch (IOException e) {
			throw new TrackFileException("Error writing to trackfile "+file.getAbsolutePath(),e);
		}
	}

	/**
	 * Update the underlying track file name and the track file type.
	 *  
	 * @param tracks All parsed tracks.
	 * @param trackFile The track file object.
	 * @param fileName The given file name.
	 */
	private static void updateFileAndType(List<Track> tracks, TrackFile trackFile, String fileName) {
		tracks.stream().forEach(track -> {
			updateFileAndType(track, trackFile, fileName);
		});
	}
	
	/**
	 * Update the underlying track file name and the track file type.
	 *  
	 * @param track The Track object.
	 * @param trackFile The track file object.
	 * @param fileName The given file name.
	 */
	private static void updateFileAndType(Track track, TrackFile trackFile, String fileName) {
		track.setTrackFileName(fileName);
		track.setTrackFileType(trackFile.getTypeDescription());
	}
	
	/**
	 * Select a track file object for a given track object and a requested file type.
	 * 
	 * @param track The track to be saved.
	 * @param trackFileType The given file type.
	 * @return TrackFile object
	 */
	private static TrackFile getTrackFileObject(Track track, String trackFileType) {
		TrackFile targetTackFile = getTrackFileObject(trackFileType);
		
		if(targetTackFile == null && track.getTrackFileType() != null) {
			targetTackFile = getTrackFileObject(track.getTrackFileType());
		}
		
		return targetTackFile;
	}
	
	/**
	 * Select a track file object for a requested file type.
	 * 
	 * @param track The track to be saved.
	 * @param trackFileType The given file type.
	 * @return TrackFile object
	 */
	private static TrackFile getTrackFileObject(String trackFileType) {
		TrackFile targetTackFile = null;
		for (TrackFile trackFile : trackFiles) {
			if (trackFile.getTypeDescription().equals(trackFileType)) {
				targetTackFile = trackFile;
				break;
			}
		}
		
		return targetTackFile;
	}

	/**
	 * Adds a TrackFile object, capable of opening and saving of a dedicated
	 * track file format.
	 * 
	 * @param trackFile
	 *            TrackFile object
	 */
	public static void addTrackFile(TrackFile trackFile) {
		trackFiles.add(trackFile);
	}

	public static String getLastMessage() {
		return Optional.ofNullable(lastMessage).orElse("");
	}

	private static void setLastMessage(String lastMessage) {
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
