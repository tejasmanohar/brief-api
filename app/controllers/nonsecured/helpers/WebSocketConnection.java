package controllers.nonsecured.helpers;

import actors.WsConectionsActor;
import actors.messages.WsUserConnection;
import com.fasterxml.jackson.databind.JsonNode;
import models.ConversationModel;
import models.MessageModel;
import models.UserModel;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.WebSocket;
import services.IConversationService;
import services.IMessageService;
import services.IUserService;
import services.MailService;
import utils.exceptions.BusinessValidationException;
import utils.websockets.WsAuthorization;
import utils.websockets.WsEventType;
import utils.websockets.WsJsonParser;
import utils.websockets.WsUserMessage;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

import java.util.*;

public class WebSocketConnection extends WebSocket<JsonNode> {
    private IUserService userService;
    private IConversationService conversationService;
    private IMessageService messageService;
    private UserModel user;
    private MailService mailService;


    public WebSocketConnection(IConversationService conversationService, IUserService userService, IMessageService messageService, MailService mailService) {
        this.conversationService = conversationService;
        this.userService = userService;
        this.messageService = messageService;
        this.mailService = mailService;
    }

    @Override
    public void onReady(In<JsonNode> in, Out<JsonNode> out) {

        in.onMessage((json) -> {
            try {
                WsEventType type = WsJsonParser.getEventType(json);

                if (type.equals(WsEventType.AUTHORIZATION)) {
                    final WsAuthorization authorization = WsJsonParser.getJsonBody(json, WsAuthorization.class);

                    JPA.withTransaction(() -> user = userService.getByToken(authorization.authToken));

                    if (user == null) {
                        throw new BusinessValidationException("authToken", "Invalid token");
                    } else {
                        WsConectionsActor.WS_ACTOR.tell(WsUserConnection.wsConnected(user.id, out), null);
                    }
                } else {
                    if (user == null) {
                        throw new BusinessValidationException("authorization", "Not authorized");
                    } else {
                        switch (type) {
                            case MESSAGE:
                                final WsUserMessage wsUserMessage = WsJsonParser.getJsonBody(json, WsUserMessage.class);
                                final List<Long> idBox = new ArrayList<>();
                                final Set<Long> members = new HashSet<>();

                                JPA.withTransaction(() -> {
                                    try {
                                        user = userService.findById(user.id);
                                        ConversationModel conversation = conversationService.get(user, wsUserMessage.conversationId);
                                        conversation.members.forEach(member -> members.add(member.id));
                                        idBox.add(messageService.createConversationMessage(user, conversation, wsUserMessage.message).id);
                                        mailService.sendMailToConversation(conversation, user, wsUserMessage.message);
                                    } catch (BusinessValidationException e) {
                                        out.write(WsJsonParser.getError(e.getJson()));
                                    } catch (Exception e) {
                                        Logger.error("Send message exception : " + e.getMessage());
                                    }
                                });

                                if(!idBox.isEmpty() && idBox.get(0) != null) {
                                    WsConectionsActor.WS_ACTOR.tell(wsUserMessage.toWsMessage(idBox.get(0), members, user.getId(), user.role), null);
                                } else {
                                    Logger.error("Can't send message");
                                }

                                break;
                            default:
                                throw new BusinessValidationException("event", "Unsuported event type");
                        }
                    }
                }
            } catch(BusinessValidationException e){
              out.write(WsJsonParser.getError(e.getJson()));
            }
        });

        in.onClose(() -> {
            if (user != null) {
                WsConectionsActor.WS_ACTOR.tell(WsUserConnection.wsDisconnected(user.id, out), null);
            }
        });
    }
}
