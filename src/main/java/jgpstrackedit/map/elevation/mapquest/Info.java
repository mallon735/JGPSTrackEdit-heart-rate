
package jgpstrackedit.map.elevation.mapquest;

import java.util.List;

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
}
