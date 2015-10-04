package actors.messages;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.WebSocket;

public class WsUserConnection {
    public Long userId;
    public WebSocket.Out<JsonNode> connection;
    public Type type;

    public enum Type {
        CONNECTED,
        DISCONNECTED
    }

    private WsUserConnection(Long userId, WebSocket.Out<JsonNode> connection, Type type) {
        this.userId = userId;
        this.connection = connection;
        this.type = type;
    }

    public static WsUserConnection wsConnected(Long userId, WebSocket.Out<JsonNode> connection) {
        return new WsUserConnection(userId, connection, Type.CONNECTED);
    }

    public static WsUserConnection wsDisconnected(Long userId, WebSocket.Out<JsonNode> connection) {
        return new WsUserConnection(userId, connection, Type.DISCONNECTED);
    }
}
