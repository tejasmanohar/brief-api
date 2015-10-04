package controllers.secured;

import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import controllers.setup.BriefController;
import controllers.utils.Secured;
import models.SkillModel;
import models.UserModel;
import models.helpers.*;
import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import services.ICandidateService;
import services.IUserService;

import javax.ws.rs.QueryParam;

@Api(value = "/api/v1/candidates", description = "Operations about candidates")
public class Candidate extends BriefController {

    @Inject
    ICandidateService candidateService;

    @Inject
    IUserService userService;

    @Transactional
    @ApiOperation(value = "Get candidate's public info", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = PublicCandidateInfoHelper.class),
            @ApiResponse(code = 400, message = "invalid input data"),
    })
    public Result show(@ApiParam(value = "candidate id") @QueryParam("id") Long id) {
        UserModel user = userService.findCandidateById(id);

        if(user == null) {
            return ok(Json.newObject());
        } else {
            return ok(Json.toJson(user.toCandidatePublicInfo()));
        }
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Get candidate self info", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = SelfCandidateInfoHelper.class),
            @ApiResponse(code = 400, message = "invalid user role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result showSelf() {
        ifCandidate();

        UserModel user = getUser();

        return ok(Json.toJson(user.toSelfCandidateInfo()));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Delete candidate account", httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid user role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result delete() {
        ifCandidate();
        getUser().delete();
        return ok();
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Add candidate skill", httpMethod = "PUT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = SelfCandidateInfoHelper.class),
            @ApiResponse(code = 400, message = "invalid user role or invalid data input"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.AddSkillHelper", paramType = "body")})
    public Result addSkill() {
        ifCandidate();

        Form<AddSkillHelper> addSkillHelper = Form.form(AddSkillHelper.class).bindFromRequest();

        validateData(addSkillHelper);

        return ok(Json.toJson(candidateService.addSkills(getUser(), addSkillHelper.get()).toSelfCandidateInfo()));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Delete candidate's skill", httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = SelfCandidateInfoHelper.class),
            @ApiResponse(code = 400, message = "invalid user role or invalid data input"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.AddSkillHelper", paramType = "body")})
    public Result deleteSkill(@ApiParam(value = "Name of deleted skill") @QueryParam("name") String name) {
        ifCandidate();
        return ok(Json.toJson(candidateService.deleteSkill(getUser(), name).toSelfCandidateInfo()));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Update candidate's info", httpMethod = "PUT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = SelfCandidateInfoHelper.class),
            @ApiResponse(code = 400, message = "invalid user role or invalid data input"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.CandidateInfoHelper", paramType = "body")})
    public Result update() {
        ifCandidate();

        Form<CandidateInfoHelper> infoHelper = Form.form(CandidateInfoHelper.class).bindFromRequest();

        validateData(infoHelper);

        UserModel user =getUser();
        
        return ok(Json.toJson(candidateService.update(user, infoHelper.get()).toSelfCandidateInfo()));
    }
}
