package controllers.utils;

import models.UserModel;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import play.mvc.Security;
import services.IUserService;
import services.UserService;
import utils.exceptions.BusinessValidationException;
import utils.secure.SecurityHelper;

public class Secured extends Security.Authenticator {

    IUserService userService;

    public Secured() {
        userService = new UserService();
    }

    @Override
    public String getUsername(Context ctx) {
        String authToken = getAuthToken(ctx);
        if (authToken != null && !authToken.isEmpty()) {
            UserModel user = userService.getByToken(authToken);
            if (user != null) {
                if(ctx.request().uri().equals("/api/v1/secure/credentials") && user.emailAddress == null) {
                    ctx.args.put(SecurityHelper.AUTHORIZED_USER, user);
                    return "twitterUser";
                } else if(user.emailAddress == null) {
                    throw new BusinessValidationException("emailAddress", "Please specify email address");
                } else {
                    ctx.args.put(SecurityHelper.AUTHORIZED_USER, user);
                    return user.emailAddress;
                }

            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return unauthorized("Not authorized");
    }

    private String getAuthToken(Context context){
        Cookie authTokenCookie = context.request().cookie(SecurityHelper.AUTH_TOKEN_COOKIE);
        String[] authTokenHeaderValues = context.request().headers().get(SecurityHelper.AUTH_TOKEN_HEADER);

        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            return authTokenHeaderValues[0];
        } else if(authTokenCookie != null){
            return authTokenCookie.value();
        } else {
            return null;
        }
    }
}
