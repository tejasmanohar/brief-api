package utils.websockets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import play.data.validation.Constraints;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WsAuthorization {
    @Constraints.Required
    public String authToken;
}
