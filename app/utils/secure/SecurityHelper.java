package utils.secure;

import models.UserModel;
import play.mvc.Http;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityHelper {
    // headers names
    public final static String AUTH_TOKEN_HEADER = "X-Auth-Token";

    // cookies names
    public static final String AUTH_TOKEN_COOKIE = "authToken";

    // context names
    public static final String AUTHORIZED_USER = "authorizedUser";

    public static UserModel getUser(Http.Context context) {
        return (UserModel)context.args.get(AUTHORIZED_USER);
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
