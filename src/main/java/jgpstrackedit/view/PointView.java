/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.view;

import java.awt.Graphics2D;

import jgpstrackedit.config.Configuration;
import jgpstrackedit.config.view.ViewingConfiguration;
import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.util.Parser;

/**
 *
 * @author Hubert
 */
public class PointView {


	private boolean outView = false;
	
	
    /**
	 * @return the outView
	 */
	public boolean isOutView() {
		return outView;
	}

	/**
	 * @param outView the outView to set
	 */
	public void setOutView(boolean outView) {
		this.outView = outView;
	}

	private Point point;
    /**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	private Track track;

    public PointView(Point point, Track track) {
        this.point = point;
        this.track = track;
    }

    /** Paints a point
     *
     * @param g2D Current graphics object
     */
    public void paint(Graphics2D g2D, boolean isSelected) {
    	int POINTSIZE = Configuration.getIntProperty("POINT_DIAMETER");
        int pointX = Transform.screenX(point.getLongitude());
        int pointY = Transform.screenY(point.getLatitude());
    	g2D.fillOval(pointX - POINTSIZE/2, pointY - POINTSIZE/2, POINTSIZE, POINTSIZE);
    	if (isSelected)
        	g2D.drawOval(pointX - POINTSIZE/2-2, pointY - POINTSIZE/2-2, POINTSIZE+4, POINTSIZE+4);
    	if (ViewingConfiguration.isShowLength()) {
    		g2D.drawString(Parser.formatLength(point.getLength()),pointX+5,pointY-5);
    	}
    	if (ViewingConfiguration.isShowInformation() && point.getInformation() != null) {
    		g2D.drawString(point.getInformation(),pointX+5,pointY+5);
    	}
    		
    }
    
    /** 
     * Paints the marker of the start point of a track.
     * 
     * @param g2D Graphics2D object to be paint on
     */
    public void paintStartMarker(Graphics2D g2D) {
        int pointX = Transform.screenX(point.getLongitude());
        int pointY = Transform.screenY(point.getLatitude());
        g2D.drawLine(pointX, pointY, pointX, pointY-10);
        g2D.drawLine(pointX, pointY-10, pointX+10, pointY-10);
        g2D.drawLine(pointX+6, pointY-14, pointX+10, pointY-10);
        g2D.drawLine(pointX+6, pointY-6, pointX+10, pointY-10);   	
    }
    
    /**
     * Paints the marker of the end point of a track.
     * 
     * @param g2D Graphics2D object to be paint on
     */
    public void paintEndMarker(Graphics2D g2D) {
        int pointX = Transform.screenX(point.getLongitude());
        int pointY = Transform.screenY(point.getLatitude());
        g2D.drawLine(pointX, pointY, pointX, pointY-8);
        g2D.drawRect(pointX, pointY-14, 10, 8);
        g2D.fillRect(pointX, pointY-14, 5, 4);
        g2D.fillRect(pointX+5, pointY-10, 5, 4);
    	
    }

    /** tests if the given screen-point is contained by the point.
     *
     * @param x  screen x-coordinate
     * @param y  screen y-coordinate
     * @return true if the given screen-point is contained by the point
     */
    public boolean contains(int x, int y) {
        int pointX = Transform.screenX(point.getLongitude());
        int pointY = Transform.screenY(point.getLatitude());
    	int POINTSIZE = Configuration.getIntProperty("POINT_DIAMETER");
        return (Math.abs(pointX - x) < POINTSIZE) && (Math.abs(pointY - y) < POINTSIZE);
        /*
        return pointX - POINTSIZE <= x &&
               pointX + POINTSIZE >= x &&
               pointY - POINTSIZE <= y &&
               pointY + POINTSIZE >= y;
               */

    }

    public int getX() {
        return Transform.screenX(point.getLongitude());
    }

    public int getY() {
        return Transform.screenY(point.getLatitude());
    }
}
