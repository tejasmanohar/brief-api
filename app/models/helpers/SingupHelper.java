package models.helpers;

import play.data.validation.Constraints;

public class SingupHelper {
    @Constraints.Required
    @Constraints.Email
    public String emailAddress;

    @Constraints.Required
    public String password;
}
