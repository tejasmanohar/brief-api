package controllers.nonsecured;

import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import controllers.setup.BriefController;
import controllers.utils.Secured;
import models.UserModel;
import models.helpers.*;
import play.mvc.Result;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Security;
import services.IAuthenticationService;
import utils.secure.SecurityHelper;

@Api(value = "/api/v1/secure", description = "Operations about authentification")
public class Authentification extends BriefController {

    @Inject
    IAuthenticationService authentificationService;

    @Transactional
    @ApiOperation(value = "Sing up as candidate", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success candidate singup", response = AuthHelper.class),
            @ApiResponse(code = 400, message = "invalid input data"),
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.SingupHelper", paramType = "body")})
    public Result singupCandidate() throws Exception {
        Form<SingupHelper> singupHelper = Form.form(SingupHelper.class).bindFromRequest();

        validateData(singupHelper);

        UserModel user = authentificationService.singUp(null, UserModel.Role.CANDIDATE, singupHelper.get());
        response().setCookie(SecurityHelper.AUTH_TOKEN_COOKIE, user.authToken);

        return ok(Json.toJson(user.authResponse()));
    }

    @Transactional
    @ApiOperation(value = "Sing up as company", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success company singup", response = AuthHelper.class),
            @ApiResponse(code = 400, message = "invalid input data"),
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.SingupHelper", paramType = "body")})
    public Result singupCompany() throws Exception {
        Form<SingupHelper> singupHelper = Form.form(SingupHelper.class).bindFromRequest();

        validateData(singupHelper);

        UserModel user = authentificationService.singUp(null, UserModel.Role.COMPANY, singupHelper.get());
        response().setCookie(SecurityHelper.AUTH_TOKEN_COOKIE, user.authToken);

        return ok(Json.toJson(user.authResponse()));
    }

    @Transactional
    @ApiOperation(value = "Sing in", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success singin", response = AuthHelper.class),
            @ApiResponse(code = 400, message = "invalid input data"),
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.LoginHelper", paramType = "body")})
    public Result singin() throws Exception {
        Form<LoginHelper> loginHelper = Form.form(LoginHelper.class).bindFromRequest();

        validateData(loginHelper);

        UserModel user = authentificationService.singIn(loginHelper.get());
        response().setCookie(SecurityHelper.AUTH_TOKEN_COOKIE, user.authToken);

        return ok(Json.toJson(user.authResponse()));
    }

    @Transactional
    @ApiOperation(value = "Sing in with faceboock", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success singin", response = AuthHelper.class),
            @ApiResponse(code = 400, message = "invalid input data"),
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.FacebookLoginHelper", paramType = "body")})
    public Result facebookSingIn() {
        Form<FacebookLoginHelper> facebookLoginHelper = Form.form(FacebookLoginHelper.class).bindFromRequest();

        validateData(facebookLoginHelper);

        UserModel user = authentificationService.facebookSingIn(facebookLoginHelper.get());
        response().setCookie(SecurityHelper.AUTH_TOKEN_COOKIE, user.authToken);

        return ok(Json.toJson(user.authResponse()));
    }

    @Transactional
    @ApiOperation(value = "Sing in with twitter", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success singin", response = AuthHelper.class),
            @ApiResponse(code = 400, message = "invalid input data"),
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.TwitterLoginHelper", paramType = "body")})
    public Result twitterSingIn() {
        Form<TwitterLoginHelper> twitterLoginHelper = Form.form(TwitterLoginHelper.class).bindFromRequest();

        validateData(twitterLoginHelper);

        UserModel user = authentificationService.twitterSingIn(twitterLoginHelper.get());
        response().setCookie(SecurityHelper.AUTH_TOKEN_COOKIE, user.authToken);

        return ok(Json.toJson(user.authResponse()));
    }


    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Log out", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success log out"),
            @ApiResponse(code = 400, message = "invalid input data"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result logout() {
        response().discardCookie(SecurityHelper.AUTH_TOKEN_COOKIE);
        return ok();
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Set up user credentials", httpMethod = "PUT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = AuthHelper.class),
            @ApiResponse(code = 400, message = "invalid input data"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.SingupHelper", paramType = "body")})
    public Result setUpCredentials() {
        Form<SingupHelper> singupHelper = Form.form(SingupHelper.class).bindFromRequest();

        validateData(singupHelper);

        return ok(Json.toJson(
                authentificationService.setUpCredentials(
                        getUser(),
                        singupHelper.get()
                ).authResponse()
        ));
    }
}
