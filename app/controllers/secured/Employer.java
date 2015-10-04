package controllers.secured;

import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import models.UserModel;
import models.helpers.EmployerInfoHelper;
import models.helpers.PublicCandidateInfoHelper;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.IUserService;

import javax.ws.rs.QueryParam;

@Api(value = "/api/v1/employers", description = "Operations about employers")
public class Employer extends Controller {

    @Inject
    IUserService userService;

    @Transactional
    @ApiOperation(value = "Get candidate", httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success", response = EmployerInfoHelper.class)})
    public Result show(@ApiParam(value = "employer id") @QueryParam("id") Long id) {
        UserModel user = userService.findEmployerById(id);

        if(user == null) {
            return ok(Json.newObject());
        } else {
            return ok(Json.toJson(user.toEmployerInfoHelper()));
        }
    }

}
