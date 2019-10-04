/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.data;

import jgpstrackedit.data.util.ColorUtils;
import jgpstrackedit.data.util.Geometry;
import jgpstrackedit.data.util.TrackUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


/**
 * 
 * @author hlutnik
 */
public class Track 
{
	private static final Logger logger = LoggerFactory.getLogger(Track.class);
	private static ColorUtils colorConverter = new ColorUtils();
	
	private ArrayList<Point> points = new ArrayList<Point>();
	private Point leftUpperBoundary = null;
	private Point rightLowerBoundary = null;

	private String name;
	private String copyright;
	private String link;
	private String linkText;

	private String trackFileType;
	private Path trackFilePath;
	private String trackFileName;

	private boolean valid = false;
	private boolean modified = false;
	
	private Color color;
	private boolean colorSet = false;
	private List<TrackObserver> trackObservers = Collections.synchronizedList(new ArrayList<TrackObserver>());

	
	private transient TreeSet<Integer> compressedTrackIndizes;
	
	/**
	 * Indicates whether the track has been modified by editing.
	 * 
	 * @return true if the track has been modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Sets the modified state of the track.
	 * 
	 * @param modified
	 *            the modified state to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyTrackObservers();
		this.modified = modified;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return the trackFileName
	 */
	public String getTrackFileName() {
		return trackFileName;
	}

	/**
	 * Set the track file name and the path to file.
	 * 
	 * @param trackFileName
	 *            the trackFileName to set
	 */
	public void setTrackFileName(String trackFileName) {
		Path path = null;
		
		this.trackFileName = trackFileName;
		if(trackFileName != null && trackFileName.length() > 0) {
			File parentFile = new File(trackFileName).getParentFile();
			if(parentFile != null) {
				path = parentFile.toPath();
			}
		}
		
		// Don't overwrite a valid path with null!
		if(path != null) {
			this.trackFilePath = path;
		}
	}
	
	/**
	 * Set the file path. Note: Ignore it, if a valid track file name is already given.
	 * @param path
	 */
	public void setTrackFilePath(Path path) {
		if(trackFileName == null || trackFileName.length() == 0) {
			this.trackFilePath = path;
		}
	}
	
	public Path getTrackFilePath() {
		return this.trackFilePath;
	}

	/**
	 * @return the trackFileType
	 */
	public String getTrackFileType() {
		return trackFileType;
	}

	/**
	 * @param trackFileType
	 *            the trackFileType to set
	 */
	public void setTrackFileType(String trackFileType) {
		this.trackFileType = trackFileType;
	}

	/**
	 * Creates a new Track.
	 * 
	 */
	public Track() {
		color = Color.BLACK;
		Date now = new Date();
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
		time = df1.format(now)+"T"+df2.format(now)+"Z";
		//gpxAttributes = "creator=\"JGPSTrackEdit\" version=\"1.1\"";

	}

	public void addTrackObserver(TrackObserver observer) {
		trackObservers.add(observer);
		logger.debug(String.format("#track observers %d", trackObservers.size()));
	}

	public void removeTrackObserver(TrackObserver observer) {
		trackObservers.remove(observer);
	}

	protected void notifyTrackObservers() {
		if(trackObservers.size() > 0) {
			TrackObserver[] observers = trackObservers.toArray(new TrackObserver[trackObservers.size()]);
			for (TrackObserver observer : observers) {
				observer.trackModified(this);
			}
		}
		modified = true;
	}

	/**
	 * This method should be called if the track has been modified, e.g. if the
	 * points has been modified without using a track method.
	 */
	public void hasBeenModified() {
		checkBoundaries();
		notifyTrackObservers();
	}

	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
		colorSet = true;
	}

	public String getExtensionsColor() {
		return colorConverter.getColorNameFromColor(color);
	}

	public void setExtensionsColor(String color) {
		this.color = colorConverter.getColorFromName(color);
		colorSet = true;
	}

	public void assignColor(){
		if( false == colorSet){
			this.color = colorConverter.getNextColor();
		}
	}
	
	public static void resetColors() {
		colorConverter.reset();
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	private String time;
	private String gpxAttributes;

	public String getGpxAttributes() {
		return gpxAttributes;
	}

	public void setGpxAttributes(String gpxAttributes) {
		this.gpxAttributes = gpxAttributes;
	}

	public Point getLeftUpperBoundary() {
		return leftUpperBoundary;
	}

	public void setLeftUpperBoundary(Point leftUpperBoundary) {
		this.leftUpperBoundary = leftUpperBoundary;
	}

	public Point getRightLowerBoundary() {
		return rightLowerBoundary;
	}

	public void setRightLowerBoundary(Point rightLowerBoundary) {
		this.rightLowerBoundary = rightLowerBoundary;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
		notifyTrackObservers();
	}

	public Point getPoint(int index) {
		return points.get(index);
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
		notifyTrackObservers();
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
		notifyTrackObservers();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyTrackObservers();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
		notifyTrackObservers();
	}

	/**
	 * Returns the first point of the track
	 * 
	 * @return first point of the track
	 */
	public Point getFirstPoint() {
		return points.get(0);
	}

	/**
	 * Returns the last point of the track
	 * 
	 * @return last point of the track
	 */
	public Point getLastPoint() {
		return points.get(points.size() - 1);
	}

	/**
	 * Returns the number of points
	 * 
	 * @return number of Points
	 */
	public int getNumberPoints() {
		return points.size();
	}

	/**
	 * Returns the length of the track.
	 * 
	 * @return length of track ion km
	 */
	public double getLength() {
		
		return getLength(false);
	}

	/**
	 * Returns the length of the track.
	 * @param storeLength if true the current length  at each point is
	 * stored 
	 * @return length of track ion km
	 */
	public double getLength(boolean storeLength) {
		double length = 0.0;
		Point first = getFirstPoint();
		if (storeLength)
			first.setLength(0.0);
		for (int i = 1; i < points.size(); i++) {
			length += first.distance(points.get(i));
			if (storeLength)
				points.get(i).setLength(length);
			first = points.get(i);
		}
		return length;
	}

	/**
	 * Returns the point of track which has the distance length form start
	 * point.
	 * 
	 * @param length
	 *            the length [km]
	 * @return the point with distance length from start point
	 */
	public Optional<Point> getPoint(double length) {
		double currentLength = 0.0;
		Point first = getFirstPoint();
		for (int i = 1; i < points.size(); i++) {
			currentLength += first.distance(points.get(i));
			if (currentLength > length)
				return Optional.of(first);
			first = points.get(i);
		}
		return Optional.empty();
	}

	/**
	 * Returns the length of the track till given point.
	 * 
	 * @param point
	 *            point
	 * @return length of sub track
	 */
	public double getLength(Point point) {
		double tourLength = 0.0;
		Point firstPoint = getFirstPoint();
		for (int i = 1; points.get(i) != point; i++) {
			tourLength += firstPoint.distance(points.get(i));
			firstPoint = points.get(i);
		}
		return tourLength;

	}

	/**
	 * Returns the up altitude difference of the track.
	 * 
	 * @return up altitude difference [m]
	 */
	public double getUpAltitudeDifference() {
		double attitude = 0.0;
		Point first = getFirstPoint();
		for (int i = 1; i < points.size(); i++) {
			double delta = first.altitudeDifference(points.get(i));
			if (delta > 0)
				attitude += delta;
			first = points.get(i);
		}
		return attitude;
	}

	/**
	 * Returns the down altitude difference of the track.
	 * 
	 * @return down altitude difference [m]
	 */
	public double getDownAltitudeDifference() {
		double attitude = 0.0;
		Point first = getFirstPoint();
		for (int i = 1; i < points.size(); i++) {
			double delta = first.altitudeDifference(points.get(i));
			if (delta < 0)
				attitude += delta;
			first = points.get(i);
		}
		return attitude;
	}

	/**
	 * Determines the highest jgpstrackedit.map.elevation.
	 * 
	 * @return highest Elevation [m]
	 */
	public double getHighestElevation() {
		double max = getFirstPoint().getElevation();
		for (Point point : points) {
			if (point.getElevation() > max) {
				max = point.getElevation();
			}
		}
		return max;
	}

	/**
	 * Determines the lowest jgpstrackedit.map.elevation.
	 * 
	 * @return lowest Elevation [m]
	 */
	public double getLowestElevation() {
		double min = getFirstPoint().getElevation();
		for (Point point : points) {
			if (point.getElevation() < min) {
				min = point.getElevation();
			}
		}
		return min;
	}

	/**
	 * reverses current track
	 * 
	 */
	public void reverse() {
		Collections.reverse(points);
		notifyTrackObservers();
	}

	/**
	 * Adds the given point to the end of the track.
	 * 
	 * @param point
	 *            to be added
	 */
	public void add(Point point) {
		points.add(point);
		checkBoundaries(point);
		notifyTrackObservers();
	}

	/**
	 * Removes the given point from track.
	 * 
	 * @param point
	 *            point to remove
	 */
	public void remove(Point point) {
		points.remove(point);
		checkBoundaries();
		notifyTrackObservers();
	}

	public void removeOnly(Point point) {
		points.remove(point);
	}
	
	public void notifyObservers() {
		checkBoundaries();
		notifyTrackObservers();
	}
	/**
	 * Removes an interval of points from track, beginning after the start point
	 * and ending before the end point of the given interval.
	 * 
	 * @param start
	 *            start point of interval
	 * @param end
	 *            end point of interval
	 */
	public void remove(Point start, Point end) {
		ArrayList<Point> removePoints = new ArrayList<Point>();
		boolean isInterval = false;
		for (Point point : points) {
			if (point.equals(start) || point.equals(end)) {
				isInterval = !isInterval;
				continue;
			}
			if (isInterval) {
				removePoints.add(point);
			}
		}
		points.removeAll(removePoints);
		checkBoundaries();
		notifyTrackObservers();
	}

	/**
	 * Adds points (track section) to the end of the track
	 * 
	 * @param points
	 *            points to be added
	 */
	public void add(ArrayList<Point> points) {
		this.points.addAll(points);
		checkBoundaries();
		notifyTrackObservers();
	}

	public void insert(int index, ArrayList<Point> points) {
		this.points.addAll(index,points);
		checkBoundaries();
		notifyTrackObservers();
	}
	/**
	 * Merges the given track to the current track.
	 * 
	 * @param track
	 *            the track to merge
	 * @param direct if true, then the last point of current track is connected to the
	 *        first point of the given track, otherwise the tracks are reversed
	 *        as necessary
	 */
	public void add(Track track, boolean direct) {
		if (!direct) {
			double distances[] = new double[4];
			distances[0] = getFirstPoint().distance(track.getFirstPoint());
			distances[1] = getFirstPoint().distance(track.getLastPoint());
			distances[2] = getLastPoint().distance(track.getFirstPoint());
			distances[3] = getLastPoint().distance(track.getLastPoint());
			int minIndex = 0;
			double minDistance = distances[0];
			for (int i = 1; i < 4; i++) {
				if (distances[i] < minDistance) {
					minIndex = i;
					minDistance = distances[i];
				}
			}
			switch (minIndex) {
			case 0: // first track to reverse
				reverse();
				break;
			case 1: // both tracks to reverse
				reverse();
				track.reverse();
				break;
			case 2: // OK
				break;
			case 3: // second track to reverse
				track.reverse();
				break;
			}
		}
		points.addAll(track.getPoints());
		checkBoundaries(track.getLeftUpperBoundary());
		checkBoundaries(track.getRightLowerBoundary());
		notifyTrackObservers();
	}


	/**
	 * Corrects the track. Deletes all points which has a zero coordinates, that
	 * means whose longitude and latitude values are equals to 0.0. The first
	 * point is assumed to be correct.
	 */
	public void correct() {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).isZero()) {
				points.remove(i);
				i--;
			}
		}
		checkBoundaries();

	}

	/**
	 * Eliminates all double points (points which are equal) from the track.
	 * 
	 */
	public void correctDoublePoints() {
		TrackUtil.removeDoublePoints(points);
		Point first = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).equals(first)) {
				points.remove(i);
				i--;
			} else {
				first = points.get(i);
			}
		}
		
	}
	
	/**
	 * Corrects the track. Deletes all points which are more then
	 * epsilon-distance distant from other points. The first point is assumed to
	 * be correct.
	 * 
	 * @param epsilon
	 *            the epsilon distance
	 */
	public void correct(double epsilon) {
		Point reference = getFirstPoint();
		for (int i = 1; i < points.size(); i++) {
			if (reference.distance(points.get(i)) > epsilon) {
				points.remove(i);
				i--;
			}
		}
		checkBoundaries();

	}

	/**
	 * Calculates new boundaries of track for given point.
	 * 
	 * @param point
	 *            point to be checked
	 */
	protected void checkBoundaries(Point point) {
		if(point == null) {
			return;
		}
		if (leftUpperBoundary == null) {
			leftUpperBoundary = point.clone();
			rightLowerBoundary = point.clone();
		}
		if (point.getLatitude() > leftUpperBoundary.getLatitude()) {
			leftUpperBoundary.setLatitude(point.getLatitude());
		}
		if (point.getLongitude() < leftUpperBoundary.getLongitude()) {
			leftUpperBoundary.setLongitude(point.getLongitude());
		}
		if (point.getLatitude() < rightLowerBoundary.getLatitude()) {
			rightLowerBoundary.setLatitude(point.getLatitude());
		}
		if (point.getLongitude() > rightLowerBoundary.getLongitude()) {
			rightLowerBoundary.setLongitude(point.getLongitude());
		}
	}

	/**
	 * Calculates the boundary of track. May be used to recalculate boundaries
	 * after changing of coordinates of points
	 */
	public void checkBoundaries() {

		leftUpperBoundary = null;
		rightLowerBoundary = null;
		for (Point p : points) {
			checkBoundaries(p);
		}
	}

	/**
	 * Splits the current track into two sub-tracks. The splitting point is
	 * member of each of the subtracks.
	 * 
	 * 
	 * @param splitPoint
	 *            Point who splits the track into two new tracks
	 * @param name
	 *            new base name of the splitted track
	 * @param automaticColor select a new color for the second track
	 * @return new track
	 */
	public Track split(Point splitPoint, String name, boolean automaticColor) {
		int splitIndex = points.indexOf(splitPoint);
		assert (splitIndex != -1);
		
		if(name == null) {
			name = "Track";
		}
		
		Track secondTrack = new Track();
		secondTrack.setName(name + "_2");
		secondTrack.setLink(this.getLink());
		secondTrack.setLinkText(this.getLinkText());
		secondTrack.setTime(this.getTime());
		secondTrack.setGpxAttributes(this.getGpxAttributes());
		secondTrack.setCopyright(this.getCopyright());
		secondTrack.add(splitPoint);
		secondTrack.setTrackFilePath(this.getTrackFilePath());
		secondTrack.setTrackFileType(this.getTrackFileType());
		if(automaticColor) {
			secondTrack.assignColor();
		} else {
			secondTrack.setColor(this.getColor());
		}
		
		for (int i = splitIndex + 1; i < points.size(); i++) {
			secondTrack.add(points.get(i));
		}
		for (int i = points.size() - 1; i > splitIndex; i--) {
			points.remove(i);
		}
		
		this.setName(name + "_1");
		this.setTrackFileName(null);
		this.checkBoundaries();
		secondTrack.checkBoundaries();
		notifyTrackObservers();
		return secondTrack;

	}

	/**
	 * Splits the current track into two sub-tracks. The splitting point is
	 * member of each of the subtracks.
	 * 
	 * @param splitPoint
	 *            Point who splits the track into two new tracks
	 * @return new track
	 */
	public Track split(Point splitPoint, boolean automaticColor) {
		return split(splitPoint, this.getName(), automaticColor);
	}

	/**
	 * Inserts two new points on each side of the selected point. The position
	 * of each newly inserted point is determined by linear interpolation of the
	 * selected point coordinates and the corresponding adjacent point
	 * coordinates.
	 * 
	 * @param selectedPoint
	 *            selected point
	 */
	public void insertAdjacentPoints(Point selectedPoint) {
		int selectedIndex = points.indexOf(selectedPoint);
		if (selectedIndex < points.size() - 1)
			insertAdjacentPoint(selectedIndex + 1, selectedPoint,
					points.get(selectedIndex + 1));
		if (selectedIndex > 0)
			insertAdjacentPoint(selectedIndex, selectedPoint,
					points.get(selectedIndex - 1));
		notifyTrackObservers();

	}

	/**
	 * Inserts a new point on the given index. The position of the newly
	 * inserted point is determined by linear interpolation of the selected
	 * point coordinates and the corresponding adjacent point coordinates.
	 * 
	 * @param insertionIndex
	 *            index of insertion
	 * @param selectedPoint
	 *            first point
	 * @param adjacentPoint
	 *            second point
	 */
	protected void insertAdjacentPoint(int insertionIndex, Point selectedPoint,
			Point adjacentPoint) {
		double longitude = (selectedPoint.getLongitude() + adjacentPoint
				.getLongitude()) / 2.0;
		double latitude = (selectedPoint.getLatitude() + adjacentPoint
				.getLatitude()) / 2.0;
		double elevation = (selectedPoint.getElevation() + adjacentPoint
				.getElevation()) / 2.0;
		Point point = new Point(longitude, latitude, elevation);
		points.add(insertionIndex, point);
	}

	/**
	 * Sets the position of the given point.
	 * 
	 * @param point
	 *            the point
	 * @param mapLongitude
	 *            longitude
	 * @param mapLatitude
	 *            latitude
	 */
	public void setPointPosition(Point point, double mapLongitude,
			double mapLatitude) {
		if (point != null) {
			point.setLongitude(mapLongitude);
			point.setLatitude(mapLatitude);
			notifyTrackObservers();
		}

	}

	/**
	 * Returns the index of the given point.
	 * 
	 * @param point
	 *            the point
	 * @return index or -1 if point not found
	 */
	public int indexOf(Point point) {
		return points.indexOf(point);
	}

	/**
	 * Adds the given point to the end of track.
	 * 
	 * @param mapLongitude
	 *            longitude of point
	 * @param mapLatitude
	 *            latitude of point
	 */
	public void add(double mapLongitude, double mapLatitude) {
		Point point = new Point(mapLongitude, mapLatitude);
		add(point);
	}

	/**
	 * Compresses the track. Following algorithm is used:<br>
	 * The first and the third point are used to determine a line<br>
	 * for the middle point the distance of the point to the line is calculates<br>
	 * if the distance is less than maxDeviation, then the point is deleted from
	 * the track and the second point is the next point of the track<br>
	 * else the middle point and the point after the second point are used to
	 * determine a new line. These steps are repeated until the end of track is
	 * reached.
	 * 
	 * @param maxDeviation
	 *            maximum deviation of a point from current track line
	 *            (determined by the two previous points) in meter
	 */
	public void compress(double maxDeviation) {
		if (getNumberPoints() > 3) {
			Point first = points.get(0);
			Point second = points.get(2);
			for (int i = 1; i + 2 < points.size(); i++) {
				if (Geometry.distanceLineToPoint(first, second, points.get(i)) <= maxDeviation) {
					points.remove(i);
					second = points.get(i + 1);
					i--;
				} else {
					first = points.get(i);
					second = points.get(i + 2);
				}
			}
			notifyTrackObservers();

		}
	}

    /**
     * Used by compressDouglasPeucker() to compress a track. 
     * Compresses the track between given points. This method is called recursively
     * @param firstPointIndex 
     * @param lastPointIndex
     * @param maxDeviation 
	 *            maximum deviation of a point  in meter
     */
	protected void compressDP(int firstPointIndex, int lastPointIndex, double maxDeviation) {
		Point first = points.get(firstPointIndex);
		Point last = points.get(lastPointIndex);
		double maxDistance = 0.0;
		double actualDistance = 0.0;
		int distanceIndex = firstPointIndex;
		for (int i=firstPointIndex+1;i<lastPointIndex;i++) {
			actualDistance = Geometry.distanceLineToPoint(first, last, points.get(i));
			if (actualDistance > maxDistance) {
				maxDistance = actualDistance;
				distanceIndex = i;
			}
		}
		if (maxDistance > maxDeviation) {
			compressDP(firstPointIndex,distanceIndex,maxDeviation);
			compressDP(distanceIndex,lastPointIndex,maxDeviation);
		} else {
			compressedTrackIndizes.add(firstPointIndex);			
			compressedTrackIndizes.add(lastPointIndex);			
		}
		
	}
	
    /**
     * Compresses the track using the Douglas Peucker algorithm (see e.g. http://www.cs.ubc.ca/cgi-bin/tr/1992/TR-92-07.pdf)
     * @param maxDeviation 
	 *            maximum deviation of a point  in meter
     */
	public void compressDouglasPeucker(double maxDeviation) {
		compressedTrackIndizes = new TreeSet<Integer>(); 
		compressDP(0,points.size()-1,maxDeviation);
		ArrayList<Point> pointsCompressed = new ArrayList<Point>();
		for (int i:compressedTrackIndizes) {
			pointsCompressed.add(points.get(i));
		}
		points = pointsCompressed;
		compressedTrackIndizes = null;
		notifyTrackObservers();
    }
    
	/**
	 * Compresses the track. Following algorithm is used:<br>
	 * Every removeInterval-th point is removed from the track.
	 * 
	 * @param removeInterval
	 *            remove interval, must be greater than 1.
	 */
	public void compress(int removeInterval) {
		if (removeInterval > 1) {
			for (int i = removeInterval - 1; i < points.size() - 1; i = i
					+ removeInterval - 1) {
				points.remove(i);
			}
			notifyTrackObservers();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Track clone() {
		Track cloneTrack = new Track();
		for (Point p : points) {
			cloneTrack.add(p.clone());
		}
		cloneTrack.setColor(getColor());
		cloneTrack.setCopyright(getCopyright());
		cloneTrack.setGpxAttributes(getGpxAttributes());
		cloneTrack.setLink(getLink());
		cloneTrack.setLinkText(getLinkText());
		cloneTrack.setName(getName());
		cloneTrack.setTime(getTime());
		cloneTrack.setTrackFilePath(getTrackFilePath());
		cloneTrack.setTrackFileType(getTrackFileType());
		return cloneTrack;
	}

	/**
	 * Compresses the track. Following algorithm is used:<br>
	 * The distance between the first two points is calculated. If the distance
	 * is less than interdistance, then the second point is removed and the
	 * algorithm is repeated.
	 * 
	 * @param interdistance
	 *            the maximum distance between two consecutive points
	 * @param dummy
	 *            not used
	 */
	public void compress(double interdistance, int dummy) {
		for (int i = 0; i + 2 < points.size() - 1; i++) {
			if (points.get(i).distance(points.get(i + 1)) <= (interdistance / 1000.0)) {
				points.remove(i + 1);
				i--;
			}
		}
		notifyTrackObservers();

	}

	/**
	 * Returns the point before the given point. If the point is the starting
	 * point of the track, then the point is returned.
	 * 
	 * @param point
	 *            the point
	 * @return the point before the given point
	 */
	public Point previousPoint(Point point) {
		int pointIndex = points.indexOf(point);
		if (pointIndex > 0)
			return points.get(pointIndex - 1);
		else
			return point;
	}

	/**
	 * Returns the point after the given point. If the point is the end point of
	 * the track, then the point is returned.
	 * 
	 * @param point
	 *            the point
	 * @return the point after the given point
	 */
	public Point nextPoint(Point point) {
		int pointIndex = points.indexOf(point);
		if (pointIndex < points.size() - 1)
			return points.get(pointIndex + 1);
		else
			return point;
	}

	/**
	 * Performs a smoothing operation on elevations using weighted moving average algorithm.
	 */
	public void smoothElevation() {
		if (points.size() > 5) {
			for (int i=2; i< points.size()-2;i++) {
				points.get(i).setElevation(weigthedMovingAverage(i));
			}
		}
		
	}

	/**
	 * Calculates the weighted average elevation of given point
	 * @param pointIndex
	 * @return weighted average elevation of given point
	 */
	private double weigthedMovingAverage(int pointIndex) {
		return (points.get(pointIndex-2).getElevation() + points.get(pointIndex-1).getElevation() 
				 + points.get(pointIndex).getElevation() + points.get(pointIndex+1).getElevation() + points.get(pointIndex+2).getElevation())/5.0;
		//return (points.get(pointIndex-2).getElevation() + points.get(pointIndex-1).getElevation()*2 
		//		 + points.get(pointIndex).getElevation()*3 + points.get(pointIndex+1).getElevation()*2 + points.get(pointIndex+2).getElevation())/9.0;
	}
}
