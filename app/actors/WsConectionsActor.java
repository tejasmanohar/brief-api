package actors;

import actors.messages.WsMessage;
import actors.messages.WsUserConnection;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;
import play.mvc.WebSocket;
import services.IMessageService;
import utils.websockets.WsEventType;
import utils.websockets.WsJsonParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WsConectionsActor extends UntypedActor {

    @Inject
    IMessageService messageService;

    public static ActorRef WS_ACTOR;

    private final Map<Long, Set<WebSocket.Out<JsonNode>>> usersConnections;

    public WsConectionsActor() {
        usersConnections = new HashMap<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof WsUserConnection) {
            WsUserConnection wsConnection = (WsUserConnection) message;

            switch (wsConnection.type) {
                case CONNECTED:
                    connectUser(wsConnection.userId, wsConnection.connection);
                    break;
                case DISCONNECTED:
                    disconnectUser(wsConnection.userId, wsConnection.connection);
                    break;
            }
        } else if(message instanceof WsMessage) {
            sendMessage((WsMessage) message);

        } else {
            unhandled(message);
        }
    }

    public Map<Long, Set<WebSocket.Out<JsonNode>>> connectUser(Long userId, WebSocket.Out<JsonNode> connection) {
        if (usersConnections.containsKey(userId)) {
            usersConnections.get(userId).add(connection);
        } else {
            Set<WebSocket.Out<JsonNode>> userChannelsSet = new HashSet<>();
            userChannelsSet.add(connection);
            usersConnections.put(userId, userChannelsSet);
        }

        sendSuccess(connection);

        return usersConnections;
    }

    private void sendSuccess(WebSocket.Out<JsonNode> connection) {
        try {
            connection.write(WsJsonParser.getSuccess());
        } catch (Exception e) {
            Logger.error("Connection closed by client");
        }
    }

    public Map<Long, Set<WebSocket.Out<JsonNode>>> disconnectUser(Long userId, WebSocket.Out<JsonNode> connection) {
        if (usersConnections.containsKey(userId)) {
            usersConnections.get(userId).remove(connection);
        }

        return usersConnections;
    }

    public void sendMessage(WsMessage message) {
        message.recipients.forEach(id -> {
            ObjectNode event = Json.newObject();
            event.put("eventType", WsEventType.MESSAGE.toString());
            JPA.withTransaction(() -> {
                event.putPOJO("body", Json.toJson(message.toWsMessageHelper(messageService.getMessageStatus(message.id, id))));
            });

            if (usersConnections.containsKey(id)) {
                usersConnections.get(id).forEach(connection -> {
                    try {
                        connection.write(event);
                    } catch (Exception e) {
                        disconnectUser(id, connection);
                    }
                });
            }
        });
    }
}
