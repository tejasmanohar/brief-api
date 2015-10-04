package controllers.nonsecured;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import models.SkillModel;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

@Api(value = "/api/v1/skills", description = "Operations about skills")
public class Skills extends Controller {
    @Transactional
    @ApiOperation(value = "Get list of skills", httpMethod = "GET", response = SkillModel.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success")})
    public static Result list() {
        return ok(Json.toJson(SkillModel.find.all()));
    }
}
