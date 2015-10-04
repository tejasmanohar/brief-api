package models.helpers;

import play.data.validation.Constraints;

public class FacebookLoginHelper {
    @Constraints.Required
    public String accessToken;
}
