package controllers.nonsecured;

import com.wordnik.swagger.annotations.*;
import models.CountryModel;
import models.StateModel;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

@Api(value = "/api/v1/location", description = "Operations about locations")
public class Location extends Controller {

    @Transactional
    @ApiOperation(value = "Get list of countries", httpMethod = "GET", response = CountryModel.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success")})
    public static Result countryList() {
        return ok(Json.toJson(CountryModel.all()));
    }

    @Transactional
    @ApiOperation(value = "Get list of states", httpMethod = "GET", response = StateModel.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success")})
    public static Result stateslist() {
        return ok(Json.toJson(StateModel.all()));
    }

}
