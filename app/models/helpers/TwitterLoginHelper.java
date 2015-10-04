package models.helpers;

import play.data.validation.Constraints;

public class TwitterLoginHelper {
    @Constraints.Required
    public String accessToken;

    @Constraints.Required
    public String accessSecret;
}
