package controllers.secured;

import com.google.inject.Inject;
import com.wordnik.swagger.annotations.*;
import controllers.setup.BriefController;
import controllers.utils.Secured;
import models.JobApplyModel;
import models.JobModel;
import models.UserModel;
import models.helpers.JobHelper;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import services.IJobService;

import javax.ws.rs.QueryParam;

@Api(value = "/api/v1/jobs", description = "Operations about jobs")
public class Job extends BriefController {

    @Inject
    IJobService jobService;

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Create job", httpMethod = "POST", response = JobModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "Helper", required = true, dataType = "models.helpers.JobHelper", paramType = "body")})
    public Result create() {
        checkRole(UserModel.Role.COMPANY, UserModel.Role.EMPLOYER);
        Form<JobHelper> jobHelperForm = Form.form(JobHelper.class).bindFromRequest();

        validateData(jobHelperForm);

        return ok(Json.toJson(jobService.createJob(getUser(), jobHelperForm.get())));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Find job", httpMethod = "GET", response = JobModel.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result find(@ApiParam(value = "CountryModel code", required = false) @QueryParam("countryCode") String countryCode,
                       @ApiParam(value = "StateModel code", required = false) @QueryParam("stateCode") String stateCode,
                       @ApiParam(value = "City name", required = false) @QueryParam("cityName") String cityName,
                       @ApiParam(value = "ExperienceModel", required = false) @QueryParam("experience") Integer experience,
                       @ApiParam(value = "Salary", required = false) @QueryParam("salary") Integer salary,
                       @ApiParam(value = "Skills should be splited by coma like(java,css,html)" , required = false) @QueryParam("skills") String skills) {
        checkRole(UserModel.Role.CANDIDATE, UserModel.Role.COMPANY, UserModel.Role.EMPLOYER);
        return ok(Json.toJson(
                jobService.findJobs(
                        getUser(),
                        countryCode, stateCode,
                        cityName, experience,
                        salary, skills)
                )
        );
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Get job applicants", httpMethod = "GET", response = JobModel.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result getJobApplicants(@ApiParam(value = "JobModel id", required = false) @QueryParam("id") Long id) {
        checkRole(UserModel.Role.COMPANY, UserModel.Role.EMPLOYER);
        return ok(Json.toJson(jobService.getJobApplicants(getUser(), id)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Apply to job", httpMethod = "PUT", response = JobModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result applyToJob(@ApiParam(value = "JobModel id", required = false) @QueryParam("id") Long id) {
        ifCandidate();
        return ok(Json.toJson(jobService.applyToJob(getUser(), id)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Dismiss job", httpMethod = "PUT", response = JobModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result dismissJob(@ApiParam(value = "JobModel id", required = false) @QueryParam("id") Long id) {
        ifCandidate();
        return ok(Json.toJson(jobService.dismissJob(getUser(), id)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Reject job application", httpMethod = "PUT", response = JobModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result reject(@ApiParam(value = "Application id", required = false) @QueryParam("id") Long appId) {
        ifEmployer();
        return ok(Json.toJson(jobService.changeApplicationStatus(getUser(), appId, JobApplyModel.Status.REJECTED)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Request job interview", httpMethod = "PUT", response = JobModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result interview(@ApiParam(value = "Application id", required = false) @QueryParam("id") Long appId) {
        ifEmployer();
        return ok(Json.toJson(jobService.changeApplicationStatus(getUser(), appId, JobApplyModel.Status.INTERVIEWED)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Hire candidate to open position", httpMethod = "PUT", response = JobModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid role"),
            @ApiResponse(code = 401, message = "login required")
    })
    public Result hire(@ApiParam(value = "Application id", required = false) @QueryParam("id") Long appId) {
        ifEmployer();
        return ok(Json.toJson(jobService.changeApplicationStatus(getUser(), appId, JobApplyModel.Status.HIRED)));
    }

}
