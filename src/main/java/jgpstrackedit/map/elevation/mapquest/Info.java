
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
    "statuscode",
    "copyright",
    "messages"
})
public class Info {

    @JsonProperty("statuscode")
    private Integer statuscode;
    @JsonProperty("copyright")
    private Copyright copyright;
    @JsonProperty("messages")
    private List<Object> messages = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("statuscode")
    public Integer getStatuscode() {
        return statuscode;
    }

    @JsonProperty("statuscode")
    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }

    @JsonProperty("copyright")
    public Copyright getCopyright() {
        return copyright;
    }

    @JsonProperty("copyright")
    public void setCopyright(Copyright copyright) {
        this.copyright = copyright;
    }

    @JsonProperty("messages")
    public List<Object> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<Object> messages) {
        this.messages = messages;
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
