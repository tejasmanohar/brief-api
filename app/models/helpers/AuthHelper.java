package models.helpers;


public class AuthHelper {
    public String authToken;
    public String emailAddress;

    public AuthHelper(String authToken, String emailAddress) {
        this.authToken = authToken;
        this.emailAddress = emailAddress;
    }
}
