package controllers.secured;

import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import controllers.setup.BriefController;
import controllers.utils.Secured;
import models.EmployerModel;
import models.JobModel;
import models.UserModel;
import models.helpers.CompanyInfoHelper;
import models.helpers.EmailHelper;
import models.helpers.EmployerInfoHelper;
import models.helpers.SingupHelper;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import services.IAuthenticationService;
import services.IUserService;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/api/v1/company", description = "Operations about companies")
public class Company extends BriefController {

    @Inject
    IAuthenticationService authentificationService;

    @Inject
    IUserService userService;

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Invite employer", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = UserModel.class),
            @ApiResponse(code = 400, message = "invalid user role or invalid data input"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.EmailHelper", paramType = "body")})
    public Result inviteEmployer() {
        ifCompany();

        Form<EmailHelper> emailHelper = Form.form(EmailHelper.class).bindFromRequest();

        validateData(emailHelper);

        SingupHelper singup = new SingupHelper();
        singup.emailAddress = emailHelper.get().emailAddress;

        return ok(Json.toJson(authentificationService.singUp(getUser(), UserModel.Role.EMPLOYER, singup)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Delete company", httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = UserModel.class),
            @ApiResponse(code = 400, message = "invalid user role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result delete() {
        ifCompany();
        getUser().delete();
        return ok();
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Get company employees list", httpMethod = "GET", response = EmployerInfoHelper.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid user role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result employees() {
        ifCompany();

        UserModel user = getUser();

        List<EmployerInfoHelper> employyes = new ArrayList<>();

        user.company.employees.forEach(employer -> employyes.add(employer.user.toEmployerInfoHelper()));

        return ok(Json.toJson(employyes));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Get company jobs list", httpMethod = "GET", response = JobModel.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid user role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result jobs() {
        ifCompany();

        UserModel user = getUser();

        return ok(Json.toJson(user.company.jobs));
    }

    @Transactional
    @ApiOperation(value = "Get company", httpMethod = "GET", response = CompanyInfoHelper.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
    })
    public Result show(@ApiParam(value = "company id") @QueryParam("id") Long id) {
        UserModel user = userService.findCompanyById(id);

        if(user == null) {
            return ok(Json.newObject());
        } else {
            return ok(Json.toJson(user.toCompanyInfoHelper()));
        }
    }
}
