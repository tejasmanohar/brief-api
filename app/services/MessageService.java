package services;

import actors.messages.WsMessage;
import com.google.inject.Inject;
import models.*;
import utils.exceptions.BusinessValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageService implements IMessageService {

    @Inject
    IUserService userService;

    @Inject
    IConversationService conversationService;

    @Override
    public MessageModel createConversationMessage(UserModel owner, ConversationModel conversation, String messageText) {
        MessageModel message = new MessageModel(owner, conversation, messageText);
        message.save();

        conversation.members.forEach(member -> {
            ReadMessage readMessage = new ReadMessage(member, message);

            if (member.id.equals(owner.id)) {
                readMessage.status = ReadMessage.MessageStatus.READ;
            }

            readMessage.save();
        });

        return message;
    }

    @Override
    public Long getUnreadMessages(UserModel user, ConversationModel conversation) {
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("count", 0l);
        List<MessageModel> messages = MessageModel.find.where().eq("conversation.id", conversation.id).findList();

        messages.forEach(message -> {
            Long count = 0l;
            ReadMessage readMessage = ReadMessage.find.where().eq("user.id", user.id).eq("message.id", message.id).findUnique();
            if(readMessage.status.equals(ReadMessage.MessageStatus.UNREAD)) {
                count++;
            }
            resultMap.put("count", count);
        });

        return resultMap.get("count");
    }

    @Override
    public ReadMessage.MessageStatus getMessageStatus(Long messageId, Long userId) {

        ReadMessage message = ReadMessage.find.where().eq("user.id", userId).eq("message.id", messageId).findUnique();

        if(message == null) {
            throw new BusinessValidationException("message", "Can't find message");
        }

        return message.status;
    }

    @Override
    public List<WsMessage.WsMessageHelper> getConversationMessages(UserModel user, Long conversationId) {

        conversationService.get(userService.findById(user.id), conversationId);

        List<MessageModel> messages = MessageModel.find.where().eq("conversation.id", conversationId).findList();

        List<WsMessage.WsMessageHelper> wsMessages = new ArrayList<>();

        messages.forEach(message -> {
            ReadMessage readMessage = ReadMessage.find.where().eq("user.id", user.id).eq("message.id", message.id).findUnique();
            WsMessage.WsMessageHelper helper = new WsMessage.WsMessageHelper(readMessage.status, message.id, message.owner.getId(), conversationId, message.message, message.owner.role);
            wsMessages.add(helper);
        });

        return wsMessages;
    }

    @Override
    public WsMessage.WsMessageHelper markMessageAsRead(UserModel user, Long messageId) {

        ReadMessage readMessage = ReadMessage.find.where().eq("user.id", user.id).eq("message.id", messageId).findUnique();

        if(readMessage == null) {
            throw new BusinessValidationException("message", "Can't find message");
        }

        MessageModel message = MessageModel.find.byId(messageId);

        readMessage.status = ReadMessage.MessageStatus.READ;
        readMessage.save();

        return new WsMessage.WsMessageHelper(readMessage.status, message.id, message.owner.getId(), message.conversation.id, message.message, message.owner.role);
    }
}
