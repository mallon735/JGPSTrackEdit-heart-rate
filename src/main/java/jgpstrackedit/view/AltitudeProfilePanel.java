package jgpstrackedit.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.data.TrackObserver;
import jgpstrackedit.data.util.TourPlaner;
import jgpstrackedit.util.Parser;

public class AltitudeProfilePanel extends JPanel implements TrackObserver {

	private Track track = null;
	private Point selectedPoint = null;
	private boolean showDayTourMarkers = false;
	private List<Point> markers = null;
	
	private final static int ALT_WIDTH = 35;
	private final static int LENGTH_HEIGHT = 15;
	private final static int BORDER_HEIGHT = 14;

	/**
	 * @return the showDayTourMarkers
	 */
	public boolean isShowDayTourMarkers() {
		return showDayTourMarkers;
	}

	/**
	 * @param showDayTourMarkers
	 *            the showDayTourMarkers to set
	 */
	public void setShowDayTourMarkers(boolean showDayTourMarkers) {
		this.showDayTourMarkers = showDayTourMarkers;
		repaint();
	}

	/**
	 * @param selectedPoint
	 *            the selectedPoint to set
	 */
	public void setSelectedPoint(Point selectedPoint) {
		this.selectedPoint = selectedPoint;
		repaint();
	}

	/**
	 * @return the track
	 */
	public Track getTrack() {
		return track;
	}

	/**
	 * @param track
	 *            the track to set
	 */
	public void setTrack(Track track) {
		if (this.track != null) {
			this.track.removeTrackObserver(this);
		}
		this.track = track;
		if (this.track != null) {
			this.track.addTrackObserver(this);
		}
		repaint();
	}

	/**
	 * Create the panel.
	 */
	public AltitudeProfilePanel() {
		setPreferredSize(new Dimension(200, 100));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		if (this.track != null) {
			Graphics2D g2D = (Graphics2D) g;
			double trackLength = track.getLength();
			double maxElevation = track.getHighestElevation();
			double minElevation = track.getLowestElevation();
			double altitudeDifference = maxElevation - minElevation;
			double scaleUnit = altitudeScaleUnit(altitudeDifference);
			double scaleMinAltitude = calcScaleMinAltitude(minElevation,
					scaleUnit);
			double scaleMaxAltitude = calcScaleMaxAltitude(maxElevation,
					scaleUnit);
			double scaleAltitudeDifference = scaleMaxAltitude
					- scaleMinAltitude;
			double scaleUnitLength = lengthScaleUnit(trackLength);
			int width = getWidth() - ALT_WIDTH;
			int height = getHeight() - BORDER_HEIGHT - LENGTH_HEIGHT;
			g2D.setColor(Color.WHITE);
			g2D.fillRect(0, 0, getWidth(), getHeight());
			g2D.setColor(Color.LIGHT_GRAY);
			for (double alt = scaleMinAltitude; alt < scaleMaxAltitude
					+ scaleUnit; alt += scaleUnit) {
				g2D.drawLine(0, (height - 1 - (int) ((alt - scaleMinAltitude)
						/ scaleAltitudeDifference * height))
						+ BORDER_HEIGHT, getWidth(),
						(height - 1 - (int) ((alt - scaleMinAltitude)
								/ scaleAltitudeDifference * height))
								+ BORDER_HEIGHT);
				g2D.drawString(Parser.formatAltProfile(alt), 2,
						(height - 1 - (int) ((alt - scaleMinAltitude)
								/ scaleAltitudeDifference * height)) + 12);
			}
			for (double length = 0.0; length < trackLength; length += scaleUnitLength) {
				g2D.drawLine((int) (length / trackLength * width) + ALT_WIDTH,
						0, (int) (length / trackLength * width) + ALT_WIDTH,
						getHeight() - LENGTH_HEIGHT);
				g2D.drawString(Parser.formatLengthProfile(length),
						(int) (length / trackLength * width) + ALT_WIDTH - 4,
						getHeight() - LENGTH_HEIGHT + 11);
			}
			g2D.drawString("m/km", 1, getHeight() - LENGTH_HEIGHT + 11);
			g2D.setColor(Color.BLACK);
			g2D.drawLine(ALT_WIDTH, 0, ALT_WIDTH, getHeight() - LENGTH_HEIGHT);
			g2D.drawLine(ALT_WIDTH, getHeight() - LENGTH_HEIGHT, getWidth(),
					getHeight() - LENGTH_HEIGHT);
			g2D.setColor(Color.RED);
			ArrayList<Point> points = track.getPoints();
			Point firstPoint = track.getFirstPoint();
			double distance = 0.0;
			if (isShowDayTourMarkers()) {
				markers = new TourPlaner(track).dayTourMarkers();
			}
			for (int i = 1; i < points.size(); i++) {
				Point secondPoint = points.get(i);
				double delta = firstPoint.distance(secondPoint);
				g2D.drawLine(
						(int) (distance / trackLength * width) + ALT_WIDTH,
						(height
								- 1
								- (int) ((firstPoint.getElevation() - scaleMinAltitude)
										/ scaleAltitudeDifference * height) + BORDER_HEIGHT),
						(int) ((distance + delta) / trackLength * width + ALT_WIDTH),
						(height - 1 - (int) ((secondPoint.getElevation() - scaleMinAltitude)
								/ scaleAltitudeDifference * height))
								+ BORDER_HEIGHT);
				if (isShowDayTourMarkers()) {
					for (Point marker : markers) {
						if (firstPoint.equals(marker)) {
							g2D.setColor(Color.BLUE);
							g2D.drawLine(
									(int) (distance / trackLength * width)
											+ ALT_WIDTH,
									getHeight() - LENGTH_HEIGHT,
									(int) (distance / trackLength * width)
											+ ALT_WIDTH,
									(height
											- 1
											- (int) ((firstPoint.getElevation() - scaleMinAltitude)
													/ scaleAltitudeDifference * height) + BORDER_HEIGHT));
							g2D.setColor(Color.RED);
						}
					}
				}
				if (selectedPoint == firstPoint) {
					g2D.setColor(Color.GREEN);		 
					g2D.drawLine(
							(int) (distance / trackLength * width) + ALT_WIDTH,
							0,
							(int) (distance / trackLength * width) + ALT_WIDTH,
							getHeight());
					g2D.setColor(Color.RED);
				}
				distance += delta;
				firstPoint = secondPoint;
			}
		}

	}

	@Override
	public void trackModified(Track track) {
		if (this.track != null && this.track.equals(track)) {
			repaint();
		}

	}

	public double altitudeScaleUnit(double altitudeDifference) {
		double[] scaleUnits = { 1, 2.5, 5, 10, 25, 50, 100, 250, 500, 1000,
				2500 };
		for (int i = 0; i < scaleUnits.length; i++) {
			if (4 * scaleUnits[i] > altitudeDifference)
				return scaleUnits[i];
		}
		return 1000;
	}

	public double lengthScaleUnit(double length) {
		double[] scaleUnits = { 0.1, 0.2, 0.5, 1, 2.5, 5, 10, 25, 50, 100, 250,
				500, 1000, 2500, 5000, 10000 };
		for (int i = 0; i < scaleUnits.length; i++) {
			if (7 * scaleUnits[i] > length)
				return scaleUnits[i];
		}
		return 5000;
	}

	public double calcScaleMinAltitude(double minAltitude, double scaleUnit) {
		return Math.floor(minAltitude / scaleUnit) * scaleUnit;
	}

	public double calcScaleMaxAltitude(double maxAltitude, double scaleUnit) {
		return Math.ceil(maxAltitude / scaleUnit) * scaleUnit;
	}

	/**
	 * Returns the current screen shoot of this panel
	 * 
	 * @return screen shoot of panel
	 */
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = image.createGraphics();
		paint(graphics2D);
		return image;
	}
}
