package models.helpers;

import play.data.validation.Constraints;

public class EmailHelper {
    @Constraints.Required
    @Constraints.Email
    public String emailAddress;
}
