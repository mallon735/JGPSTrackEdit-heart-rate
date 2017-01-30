/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jgpstrackedit.view;

import jgpstrackedit.data.Point;

/**
 * Classes which wants to be informed about changes of the zoom area
 * should implement this interface and register itself.
 * @author Hubert
 */
public interface ZoomObserver {

	/** 
	 * Sets the new zoom area of the map. If leftUpperBoundary is null, the zoom area remains unchanged. 
	 * Only the view will be repaint.
	 * @param leftUpperBoundary
	 * @param rightLowerBoundary
	 * @param recalculateZoomLevel true recalculates zoom level
	 */
    public void modifiedZoom(Point leftUpperBoundary, Point rightLowerBoundary, boolean recalculateZoomLevel);

}
