package actors.messages;

import models.ReadMessage;
import models.UserModel;

import java.util.Set;

public class WsMessage {
    public Long id;
    public Set<Long> recipients;
    public Long from;
    public Long conversationId;
    public String message;
    public UserModel.Role role;

    public WsMessage(Long id, Set<Long> recipients, Long from, Long conversationId, String message, UserModel.Role role) {
        this.id = id;
        this.recipients = recipients;
        this.from = from;
        this.conversationId = conversationId;
        this.message = message;
        this.role = role;
    }

    public WsMessageHelper toWsMessageHelper(ReadMessage.MessageStatus status) {
        return new WsMessageHelper(status, id, from, conversationId, message, role);
    }

    public static class WsMessageHelper {
        public ReadMessage.MessageStatus status;
        public Long id;
        public Long fromId;
        public Long conversationId;
        public String message;
        public UserModel.Role role;

        public WsMessageHelper(ReadMessage.MessageStatus status, Long id, Long fromId, Long conversationId, String message, UserModel.Role role) {
            this.status = status;
            this.id = id;
            this.fromId = fromId;
            this.conversationId = conversationId;
            this.message = message;
            this.role = role;
        }
    }
}
