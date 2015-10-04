package controllers.nonsecured;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import controllers.nonsecured.helpers.WebSocketConnection;
import controllers.setup.BriefController;
import services.IConversationService;
import services.IMessageService;
import services.IUserService;
import services.MailService;

@Api(value = "/api/v1/conversations/websocket", description = "Operations about websockets")
public class WebSocket extends BriefController {

    @Inject
    IUserService userService;

    @Inject
    IConversationService conversationService;

    @Inject
    IMessageService messageService;

    @Inject
    MailService mailService;

    @ApiOperation(value = "Conversations websockets entry point", httpMethod = "POST")
    public play.mvc.WebSocket<JsonNode> connection() {
        return new WebSocketConnection(conversationService, userService, messageService, mailService);
    }

}
