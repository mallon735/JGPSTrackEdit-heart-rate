/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.data;

import jgpstrackedit.data.util.Geometry;
import jgpstrackedit.util.Parser;


/**
 * Single point of a track.
 * 
 * @author hlutnik
 */
public class Point {
	private double longitude = 0D;
    private double latitude = 0D;
    private double elevation = 0D;
    private String time = null;
    private String information = null;
    private transient double length;

    public Point() {
    }

    public Point(String longitude, String latitude, String elevation, String time, String information) {
        setLongitude(longitude);
        setLatitude(latitude);
        setElevation(elevation);
        setTime(time);
        setInformation(information);
    }

    public Point(String longitude, String latitude, String elevation, String time) {
        setLongitude(longitude);
        setLatitude(latitude);
        setElevation(elevation);
        setTime(time);
    }

    public Point(String longitude, String latitude, String elevation) {
        setLongitude(longitude);
        setLatitude(latitude);
        setElevation(elevation);
    }

    public Point(String longitude, String latitude) {
        setLongitude(longitude);
        setLatitude(latitude);
    }

    public Point(double longitude, double latitude) {
        setLongitude(longitude);
        setLatitude(latitude);
    }

    public Point(double longitude, double latitude, double elevation) {
        setLongitude(longitude);
        setLatitude(latitude);
        setElevation(elevation);
    }

    public double getElevation() {
        return elevation;
    }

    public String getElevationAsString() {
        return Parser.formatElevation(elevation);
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = Double.parseDouble(elevation);
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLatitudeAsString() {
        return Parser.formatLatitude(latitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.parseDouble(latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLongitudeAsString() {
        return Parser.formatLongitude(longitude);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}

	
	/**
	 * @return the information
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

	@Override
    public Point clone()  {
        return new Point(getLongitudeAsString(),
                              getLatitudeAsString(),
                              getElevationAsString(),
                              getTime(),
                              getInformation());
    }

    @Override
    public boolean equals(Object obj) {
        Point p1 = (Point)this;
        Point p2 = (Point)obj;
        return p1.getLongitude() == p2.getLongitude() &&
               p1.getLatitude() == p2.getLatitude();
    }


    /** tests if the point is within the given boundary.
     *
     * @param leftUpperBoundary
     * @param rightLowerBoundary
     * @return true if the point is within boundary
     */
    public boolean isWithin(Point leftUpperBoundary, Point rightLowerBoundary) {

        return leftUpperBoundary.getLongitude() <= longitude &&
               rightLowerBoundary.getLongitude() >= longitude &&
               leftUpperBoundary.getLatitude() >= latitude &&
               rightLowerBoundary.getLatitude() <= latitude;

    }

    /** Calculates the approximate distance (without proper consideration of the evaluation) to the given point
     * @param point point to which the distance is calculated
     * @return distance to the given point in km
     */
    public double distance(Point point) {
    	return Geometry.distance(this,point);
    }

    /** Calculates the altitude difference between current point and the given point. If point is higher, then the result is > 0
     * 
     * @param point point
     * @return altitude difference
     */
    public double altitudeDifference(Point point) {
    	return point.getElevation() - this.getElevation();
    	
    }
	
    /**
     * Custom toString method.
     */
	@Override
	public String toString() {
		return "["+getLongitudeAsString()+";"+getLatitudeAsString()+"]";
	}

	/** 
	 * Indicates wether the longitude and latitude coordinates are equals to 0.0
	 *  
	 * @return true if coordinates are the longitude and latitude coordinates are equals to 0.0 
	 */
	public boolean isZero() {
		return Parser.equals0(longitude) && Parser.equals0(latitude);
		
	}
    
}
