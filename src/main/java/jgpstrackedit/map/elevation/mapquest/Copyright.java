
package jgpstrackedit.map.elevation.mapquest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "imageAltText",
        "imageUrl",
        "text"
})

public class Copyright {

    @JsonProperty("imageAltText")
    private String imageAltText;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("text")
    private String text;

    @JsonProperty("imageAltText")
    public String getImageAltText() {
        return imageAltText;
    }

    @JsonProperty("imageAltText")
    public void setImageAltText(String imageAltText) {
        this.imageAltText = imageAltText;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }
}

