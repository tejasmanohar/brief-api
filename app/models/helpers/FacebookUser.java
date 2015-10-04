package models.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.validation.Constraints;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUser {
    @Constraints.Required
    @JsonProperty("email")
    public String emailAddress;

    @Constraints.Required
    @JsonProperty("last_name")
    public String lastName;

    @Constraints.Required
    @JsonProperty("first_name")
    public String firstName;

    public SingupHelper toSingup() {
        SingupHelper helper = new SingupHelper();
        helper.emailAddress = emailAddress;
        return helper;
    }

}
