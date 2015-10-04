package services;

import actors.messages.WsMessage;
import models.ConversationModel;
import models.MessageModel;
import models.ReadMessage;
import models.UserModel;

import java.util.List;

public interface IMessageService {
    MessageModel createConversationMessage(UserModel owner, ConversationModel conversation, String message);
    ReadMessage.MessageStatus getMessageStatus(Long messageId, Long userId);
    List<WsMessage.WsMessageHelper> getConversationMessages(UserModel user, Long conversationId);
    WsMessage.WsMessageHelper markMessageAsRead(UserModel user, Long messageId);
    Long getUnreadMessages(UserModel user, ConversationModel conversation);
}
