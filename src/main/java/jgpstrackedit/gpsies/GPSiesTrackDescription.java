/**
 * 
 */
package jgpstrackedit.gpsies;

import jgpstrackedit.util.Parser;

/**
 * @author Hubert
 *
 */
public class GPSiesTrackDescription {
	
	private String title;
	private String downloadlink;
	private String description;
	private double trackLength;
	private double minAltitude;
	private double maxAltitude;
	private double totalAscent;
	private double totalDescent;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the downloadlink
	 */
	public String getDownloadlink() {
		return downloadlink;
	}
	/**
	 * @param downloadlink the downloadlink to set
	 */
	public void setDownloadlink(String downloadlink) {
		this.downloadlink = downloadlink;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the trackLength
	 */
	public double getTrackLength() {
		return trackLength;
	}
	
	public String getTrackLengthAsString() {
		return Parser.formatLength(trackLength);
	}
	/**
	 * @param trackLength the trackLength to set
	 */
	public void setTrackLength(double trackLength) {
		this.trackLength = trackLength;
	}
	/**
	 * @return the minAltitude
	 */
	public double getMinAltitude() {
		return minAltitude;
	}
	/**
	 * @param minAltitude the minAltitude to set
	 */
	public void setMinAltitude(double minAltitude) {
		this.minAltitude = minAltitude;
	}
	/**
	 * @return the maxAltitude
	 */
	public double getMaxAltitude() {
		return maxAltitude;
	}
	/**
	 * @param maxAltitude the maxAltitude to set
	 */
	public void setMaxAltitude(double maxAltitude) {
		this.maxAltitude = maxAltitude;
	}
	/**
	 * @return the totalAscent
	 */
	public double getTotalAscent() {
		return totalAscent;
	}
	/**
	 * @param totalAscent the totalAscent to set
	 */
	public void setTotalAscent(double totalAscent) {
		this.totalAscent = totalAscent;
	}
	/**
	 * @return the totalDescent
	 */
	public double getTotalDescent() {
		return totalDescent;
	}
	/**
	 * @param totalDescent the totalDescent to set
	 */
	public void setTotalDescent(double totalDescent) {
		this.totalDescent = totalDescent;
	}
	
	

}
