/**
 * 
 */
package jgpstrackedit.data.util;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

import java.util.ArrayList;
import java.util.List;

/** Utility for planing a tour. The time (duration) of a tour is calculated using following formula:<br>
 * duration = (length / averageSpeed + altitudeDifference/100 * inclineTime100Meters/60.0) * (1+breakRatio)
 * @author Hubert
 *
 */
public class TourPlaner {
	
	private static double averageSpeed = 20.0;  // km/h
	private static double inclineTime100Meters = 10.0; // min
	private static double breakRatio = 0.5; //
	private static double maxTourTime = 8.0; // h

	private double length; // km
	private double altitudeDifference; //m
	
	private Track track;

 
    public static void initConfig()    {
		setAverageSpeed(Configuration.getDoubleProperty("AVERAGESPEED"));
		setInclineTime100Meters(Configuration.getDoubleProperty("INCLINETIME100METERS")); // min
		setBreakRatio(Configuration.getDoubleProperty("BREAKRATIO")); //
		setMaxTourTime(Configuration.getHourProperty("MAXTOURTIME")); // h
    }

	public TourPlaner(Track track) {
		setTrack(track);
	}
	
	/**
	 * @return the track
	 */
	public Track getTrack() {
		return track;
	}
	/** Sets the track. Almost all calculating methods are based on this stored track.
	 * @param track the track to set
	 */
	public void setTrack(Track track) {
		this.track = track;
		length = track.getLength();
		altitudeDifference = track.getUpAltitudeDifference();
	}
	/** Average speed [km/h]. Default value: 20 km/h
	 * @return the averageSpeed
	 */
	public static double getAverageSpeed() {
		return averageSpeed;
	}
	/** Average speed in km/h. Default value: 20 km/h
	 * @param averageSpeed the averageSpeed to set
	 */
	public static void setAverageSpeed(double averageSpeedP) {
		averageSpeed = averageSpeedP;
	}
	/** Additional time to climb up 100 meter [min]. default value: 10 min
	 * @return the inclineTime100Meters
	 */
	public static double getInclineTime100Meters() {
		return inclineTime100Meters;
	}
	/** Additional time to climb up 100 meter [min]. default value: 10 min
	 * @param inclineTime100Meters the inclineTime100Meters to set
	 */
	public static void setInclineTime100Meters(double inclineTime100MetersP) {
		inclineTime100Meters = inclineTime100MetersP;
	}
	/** Ratio of break time to driving time. Default value: 0.5. 
	 * That means, for one hour driving the break is half an hour.
	 * @return the breakRatio
	 */
	public static double getBreakRatio() {
		return breakRatio;
	}
	/** Ratio of break time to driving time. Default value: 0.5. 
	 * That means, for one hour driving the break is half an hour.
	 * @param breakRatio the breakRatio to set
	 */
	public static void setBreakRatio(double breakRatioP) {
		breakRatio = breakRatioP;
	}

	/**
	 * @return the maxTourTime
	 */
	public static double getMaxTourTime() {
		return maxTourTime;
	}
	/**
	 * @param maxTourTime the maxTourTime to set
	 */
	public static void setMaxTourTime(double maxTourTime) {
		TourPlaner.maxTourTime = maxTourTime;
	}
	/**
	 * Calculates the driving time of stored track
	 * @return the driving time [h] in decimal notation.
	 */
	public double drivingTime() {
		return length / averageSpeed + altitudeDifference/100 * inclineTime100Meters/60.0;
		
	}
	
	/**
	 * Calculates the driving time 
	 * @param tourLength length of tour
	 * @param altDiff altitude difference (up)
	 * @return the driving time [h] in decimal notation.
	 * 
	 */
	public double drivingTime(double tourLength, double altDiff) {
		return tourLength / averageSpeed + altDiff/100 * inclineTime100Meters/60.0;
		
	}
	
	/**
	 * Calculates the overall tour time of stored track
	 * @return the tour time [h] in decimal notation.
	 */
	public double tourTime () {
		return drivingTime() * (1+breakRatio);
	}

    /**
     * Calculates the overall tour time
     * @param tourLength length of tour
     * @param altDiff altDiff altitude difference (up)
     * @return the tour time [h] in decimal notation
     */
	public double tourTime (double tourLength, double altDiff) {
		return drivingTime(tourLength, altDiff) * (1+breakRatio);
	}

	/**
	 * Returns markers for day tours of a multiple day tour of stored track.
	 * 
	 * @return day tour markers
	 */
	public List<Point> dayTourMarkers() {
		List<Point> markers = new ArrayList<Point>();
		double tourLength = 0.0;
		double tourIncline = 0.0;
		Point firstPoint = getTrack().getFirstPoint();
		for (int i=1; i<getTrack().getNumberPoints();i++) {
			Point secondPoint = getTrack().getPoint(i);
			tourLength += firstPoint.distance(secondPoint);
			double altDiff = firstPoint.altitudeDifference(secondPoint);
			if (altDiff > 0)
			  tourIncline += altDiff;
			if (tourTime(tourLength,tourIncline) > getMaxTourTime()) {
				markers.add(firstPoint.clone());
				tourLength = 0.0;
				tourIncline = 0.0;
			}
			firstPoint = secondPoint;
		}
		return markers;
	}

}
