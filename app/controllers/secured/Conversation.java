package controllers.secured;

import actors.messages.WsMessage;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import controllers.setup.BriefController;
import controllers.utils.Secured;
import models.UserModel;
import models.helpers.*;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import services.IConversationService;
import services.IMessageService;

import javax.ws.rs.QueryParam;

@Api(value = "/api/v1/conversations", description = "Operations about conversations")
public class Conversation extends BriefController {

    @Inject
    IConversationService conversationService;

    @Inject
    IMessageService messageService;

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Get conversation", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = ConversationHelper.class),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result show(@ApiParam(value = "ConversationModel id") @QueryParam("id") Long id) {
        checkRole(UserModel.Role.CANDIDATE, UserModel.Role.EMPLOYER);
        return ok(Json.toJson(conversationService.show(getUser(), id)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Get conversations list", httpMethod = "GET", response = ConversationHelper.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result list() {
        checkRole(UserModel.Role.CANDIDATE, UserModel.Role.EMPLOYER);
        return ok(Json.toJson(conversationService.list(getUser().id)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Get conversation's messages", httpMethod = "GET", response = WsMessage.WsMessageHelper.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result messages(@ApiParam(value = "ConversationModel id") @QueryParam("id") Long id) {
        checkRole(UserModel.Role.CANDIDATE, UserModel.Role.EMPLOYER);
        return ok(Json.toJson(messageService.getConversationMessages(getUser(), id)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Mark message as read", httpMethod = "PUT", response = WsMessage.WsMessageHelper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result read(@ApiParam(value = "MessageModel id") @QueryParam("id") Long id) {
        checkRole(UserModel.Role.CANDIDATE, UserModel.Role.EMPLOYER);
        return ok(Json.toJson(messageService.markMessageAsRead(getUser(), id)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Invite users to conversation", httpMethod = "PUT", response = ConversationHelper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.ConversationInviteHelper", paramType = "body")})
    public Result invite(@ApiParam(value = "ConversationModel id") @QueryParam("id") Long id) {
        ifEmployer();

        Form<ConversationInviteHelper> conversationHelper = Form.form(ConversationInviteHelper.class).bindFromRequest();

        validateData(conversationHelper);

        return ok(Json.toJson(conversationService.invite(getUser(), id, conversationHelper.get())));
    }

}
