/**
 * 
 */
package jgpstrackedit.data.util;

import jgpstrackedit.data.Point;

/** Class for geometric calculations.
 * @author Hubert
 *
 */
public class Geometry {

	private static final double EARTH_RADIUS_KM = 6372.795;

	/** Calculates the distance of the given point to the given line, represented by
	 * points g1 and g2. 
	 * @param g1 first point of line
	 * @param g2 second point of line
	 * @param point point 
	 * @return distance of point to the line in meter
	 */
	public static double distanceLineToPoint(Point g1, Point g2, Point point) {
		double k = (g2.getLatitude()-g1.getLatitude()) / (g2.getLongitude()-g1.getLongitude());
		double d = g2.getLatitude() - k*g2.getLongitude();
		double kn = -1.0 / k;
		double dn = point.getLatitude() - kn * point.getLongitude();
		double sx = (dn - d) / (k - kn);
		double sy = k * sx + d;
//		double distance = Math.sqrt((point.getLongitude()-sx)*(point.getLongitude()-sx)+(point.getLatitude()-sy)*(point.getLatitude()-sy));
//		return distance;
		return distance(new Point(sx,sy,point.getElevation()),point)*1000;
	}

	/**
	 * Calculate the number of radians between two points (for distance calculation)
	 * @param inPoint1 first point
	 * @param inPoint2 second point
	 * @return angular distance between points in radians
	 */
	public static double calculateRadiansBetween(Point inPoint1, Point inPoint2)
	{
		if (inPoint1 == null || inPoint2 == null)
			return 0.0;
		final double TO_RADIANS = Math.PI / 180.0;
		// Get lat and long from points
		double lat1 = inPoint1.getLatitude() * TO_RADIANS;
		double lat2 = inPoint2.getLatitude() * TO_RADIANS;
		double lon1 = inPoint1.getLongitude() * TO_RADIANS;
		double lon2 = inPoint2.getLongitude() * TO_RADIANS;
		// Formula given by Wikipedia:Great-circle_distance as follows:
		// angle = 2 arcsin( sqrt( (sin ((lat2-lat1)/2))^^2 + cos(lat1)cos(lat2)(sin((lon2-lon1)/2))^^2))
		double firstSine = Math.sin((lat2-lat1) / 2.0);
		double secondSine = Math.sin((lon2-lon1) / 2.0);
		double term2 = Math.cos(lat1) * Math.cos(lat2) * secondSine * secondSine;
		double answer = 2 * Math.asin(Math.sqrt(firstSine*firstSine + term2));
		// phew
		return answer;
	}

    /** Calculates the approximate distance (without proper consideration of the evalation) between the points
     * @param firstpoint point to which the distance is calculated
     * @param secondpoint
     * @return distance to the given point in km
     */
    public static double distance(Point firstPoint, Point secondPoint) {
    	double radDist = calculateRadiansBetween(firstPoint,secondPoint);
    	double dist = radDist * (EARTH_RADIUS_KM + ( (firstPoint.getElevation()+secondPoint.getElevation()) /2000.0));
        return dist;
    }

    /**
     * Converts a gps degree-minute-decimalpoint-second value to a decimal degree value.
     * @param degree gps degree-minute-decimalpoint-second value
     * @return decimal degree value
     */
    public static double gpsDegreeToDecDegree(double degree) {
    	int gradMinute = (int)degree;
    	int grad = gradMinute / 100;
    	int minute = gradMinute % 100;
    	double second = (degree - gradMinute) * 100;
    	System.out.println("g="+grad+"m="+minute+" s="+second);
    	return grad + (second/60.0 + minute)/60.0;
    }

    /** 
     * Converts a gps degree-minute-decimalpoint-second value to a decimal degree value.
     * @param degree gps degree-minute-decimalpoint-second value
     * @return decimal degree value
     */
    public static double gpsDegreeToDecDegree(String degree) {
    	return gpsDegreeToDecDegree(Double.parseDouble(degree));
    }

  
}
