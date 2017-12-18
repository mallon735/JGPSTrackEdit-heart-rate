/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.map.elevation.google;

import java.util.Optional;

/**
 * Class ElevationResult.
 * 
 * @author hlutnik
 */
public class ElevationResult {
    private String location;
    private String elevation;

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	@Override
	public String toString() {
		return Optional.ofNullable(location).orElse("") + ":" + Optional.ofNullable(elevation).orElse("");
	}
    
}
