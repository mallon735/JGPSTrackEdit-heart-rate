
package jgpstrackedit.map.elevation.mapquest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "elevationProfile",
    "info"
})
public class ElevationResponse {

    @JsonProperty("elevationProfile")
    private List<ElevationProfile> elevationProfile = null;
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

    @JsonProperty("info")
    public Info getInfo() {
        return info;
    }

    @JsonProperty("info")
    public void setInfo(Info info) {
        this.info = info;
    }
}
