package controllers.setup;

import models.UserModel;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import utils.exceptions.BusinessValidationException;
import utils.secure.SecurityHelper;

public class BriefController extends Controller {

    protected static Boolean ifCandidate() {
        return checkRole(UserModel.Role.CANDIDATE);
    }

    protected static Boolean ifCompany() {
        return checkRole(UserModel.Role.COMPANY);
    }

    protected static Boolean ifEmployer() {
        return checkRole(UserModel.Role.EMPLOYER);
    }

    protected static Boolean checkRole(UserModel.Role... roles) {
        UserModel user = getUser();
        Boolean isValid = Boolean.FALSE;

        for(UserModel.Role role : roles ) {
            if(user.role.equals(role)) {
                isValid = true;
                break;
            }
        }

        if(!isValid) {
            throw new BusinessValidationException("role", "Invalid user role");
        }

        return Boolean.TRUE;
    }

    protected static UserModel getUser() {
        return SecurityHelper.getUser(Http.Context.current());
    }

    protected static <T> void validateData(Form<T> form) {
        if (form.hasErrors()) {
            throw new BusinessValidationException("invalidData", form.errorsAsJson().toString());
        }
    }

}
