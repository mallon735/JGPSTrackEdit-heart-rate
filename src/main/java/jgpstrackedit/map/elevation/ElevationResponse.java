/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.map.elevation;

import java.util.ArrayList;

/**
 *
 * @author hlutnik
 */
public class ElevationResponse {
    private ArrayList<ElevationResult> results = new ArrayList<ElevationResult>();
    private String state;
    
    /**
	 * @return the results
	 */
	public ArrayList<ElevationResult> getResults() {
		return results;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	public void addElevationResult(ElevationResult elevationResult) {
    	results.add(elevationResult);
    }
    
}
