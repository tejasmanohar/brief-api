package models.helpers;

import play.data.validation.Constraints;

public class LoginHelper {
    @Constraints.Required
    @Constraints.Email
    public String emailAddress;

    @Constraints.Required
    public String password;
}
