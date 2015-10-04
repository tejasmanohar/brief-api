package utils.websockets;

import actors.messages.WsMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.UserModel;
import play.data.validation.Constraints;

import javax.persistence.Column;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WsUserMessage {
    @Constraints.Required
    public Long conversationId;

    @Column(length = 256, nullable = false)
    public String message;

    public WsMessage toWsMessage(Long id, Set<Long> recipients, Long from, UserModel.Role role) {
        return new WsMessage(id, recipients, from, conversationId, message, role);
    }
}
