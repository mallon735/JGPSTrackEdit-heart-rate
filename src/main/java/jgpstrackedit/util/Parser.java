/**
 * 
 */
package jgpstrackedit.util;

import java.util.Formatter;
import java.util.Locale;

/**
 * Methods for convenient parsing of strings.
 * 
 * @author Hubert
 * 
 */
public class Parser {

	/**
	 * Parses a string containing an integer
	 * 
	 * @param s
	 *            the string to parse
	 * @return the integer value
	 */
	public static int parseInt(String s) {
		if (s == null)
			return 0;
		if (s.length() == 0)
			return 0;
		else {
			return Integer.parseInt(s.trim());
		}
	}

	/**
	 * Parses a string containing an integer
	 * 
	 * @param s
	 *            the string to parse
	 * @return the integer value
	 */
	public static double parseDouble(String s) {
		if (s == null)
			return 0.0;
		if (s.length() == 0)
			return 0.0;
		else {
			String sn = s.replace(',', '.');
			return Double.parseDouble(sn.trim());
		}

	}

    /**
     * Parses a string containing a time value in format hh:mm:ss. Returns the decimal hour value.
     * @param s 
     * @return the decimal hour value
     */
	public static double parseTime(String s) {
		if (s.length() == 0)
			return 0.0;
		else {
			String[] mttA = s.split(":");
			double time = Double.parseDouble(mttA[0].trim());
			if (mttA.length == 2) {
				time += Double.parseDouble(mttA[1].trim()) / 60;
			}
			if (mttA.length == 3) {
				time += Double.parseDouble(mttA[1].trim()) / 3600;
			}
			return time;
		}

	}

	/** Formats an hour value (decimal notation) in a string hh:mm
	 * 
	 * @param hoursDecimal
	 * @return the hh:mm string
	 */
	public static String formatTimeHHMM(double hoursDecimal) {
		int hour = (int)Math.floor(hoursDecimal);
		int min = (int)Math.floor((hoursDecimal-hour)*60);
		return ""+hour+":"+(min<10?"0":"")+min;
		
	}

	/**
	 * Trims any trailing '0'-characters from a double value in string representation.
	 * @param s string to trim
	 * @return string without trailing 0-characters
	 */
	public static String trim_0(String s) {
		int dPoint = s.indexOf('.');
		if (s.length()-dPoint > 7)
			s = s.substring(0,dPoint+8);
		while (s.endsWith("0"))
			s = s.substring(0, s.length()-1);
		return s;
	}

	/** Formats the given longitude double value into "%10.6f", Locale.US.
	 * 
	 * @param longitude the longitude to format
	 * @return a proper string representation of the given longitude
	 */
	public static String formatLongitude(double longitude) {
		try(Formatter format = new Formatter(Locale.US)) {
			return (format.format("%10.6f", longitude).toString()).trim();
		}
	}
	
	/** Formats the given latitude double value into "%9.6f", Locale.US.
	 * 
	 * @param latitude the latitude to format
	 * @return a proper string representation of the given latitude
	 */
	public static String formatLatitude(double latitude) {
		try(Formatter format = new Formatter(Locale.US)) {
			return (format.format("%9.6f", latitude).toString()).trim();
		}
	}

	/** Formats the given elevation double value into "%9.5f", Locale.US.
	 * 
	 * @param elevation the elevation to format
	 * @return a proper string representation of the given elevation
	 */
	public static String formatElevation(double elevation) {
		try(Formatter format = new Formatter(Locale.US);) {
			return (format.format("%9.5f", elevation).toString()).trim();
		}
	}

	/**
	 * Formats the given length double value into "%8.3f", Locale.US.
	 * @param length the length to format
	 * @return a proper string representation of the given length
	 */
	public static String formatLength(double length) {
		try(Formatter format = new Formatter(Locale.US)) {
			return (format.format("%8.3f", length).toString()).trim();
		}
	}
	
	public static String formatAltProfile(double length) {
		try(Formatter format = new Formatter(Locale.US)) {
			return (format.format("%4.0f", length).toString()).trim();
		}
	}
	
	public static String formatLengthProfile(double length) {
		try(Formatter format = new Formatter(Locale.US)) {
			return (format.format("%5.1f", length).toString()).trim();
		}
	}

	/** Returns true if the given value equals 0.0
	 * 
	 * @param value the value test
	 * @return true if the value equals 0.0
	 */
	public static boolean equals0 (double value) {
		return Math.abs(value) < 0.0000001;
	}

}
