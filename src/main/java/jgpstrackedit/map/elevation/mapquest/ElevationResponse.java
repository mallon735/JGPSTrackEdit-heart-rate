
package jgpstrackedit.map.elevation.mapquest;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "elevationProfile",
        "shapePoints",
        "info"
})

public class ElevationResponse {

    @JsonProperty("elevationProfile")
    private List<ElevationProfile> elevationProfile = null;
    @JsonProperty("shapePoints")
    private List<Double> shapePoints = null;
    @JsonProperty("info")
    private Info info;

    @JsonProperty("elevationProfile")
    public List<ElevationProfile> getElevationProfile() {
        return elevationProfile;
    }

    @JsonProperty("elevationProfile")
    public void setElevationProfile(List<ElevationProfile> elevationProfile) {
        this.elevationProfile = elevationProfile;
    }

    @JsonProperty("shapePoints")
    public List<Double> getShapePoints() {
        return shapePoints;
    }

    @JsonProperty("shapePoints")
    public void setShapePoints(List<Double> shapePoints) {
        this.shapePoints = shapePoints;
    }

    @JsonProperty("info")
    public Info getInfo() {
        return info;
    }

    @JsonProperty("info")
    public void setInfo(Info info) {
        this.info = info;
    }

}
