
package jgpstrackedit.map.elevation.mapquest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
