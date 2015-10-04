package controllers.secured;

import com.wordnik.swagger.annotations.*;
import controllers.setup.BriefController;
import controllers.utils.Secured;
import models.UserModel;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.amazone.S3FileLoader;
import utils.exceptions.BusinessValidationException;
import play.db.jpa.Transactional;

@Api(value = "/api/v1/users", description = "Operations about users")
public class User extends BriefController {

    private static String CONTENT_TYPE = "multipart/form-data";
    private static String CONTENT_TYPE_HEADER = "Content-Type";

    @Transactional
    @Security.Authenticated(Secured.class)
    @ApiOperation(value = "Load user's image", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "invalid user role or invalid data input"),
            @ApiResponse(code = 401, message = "login required")
    })
    @ApiImplicitParams({@ApiImplicitParam(value = "upload", required = true, paramType = "upload", dataType = "file")})
    public Result loadImage() {
        String header = request().getHeader(CONTENT_TYPE_HEADER);

        if (header == null || !header.startsWith(CONTENT_TYPE)) {
            throw new BusinessValidationException("content type",
                    "Not valid content type, required header " + CONTENT_TYPE_HEADER + ":" + CONTENT_TYPE + " but :" + header);
        }

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart uploadFilePart = body.getFile("upload");
        if (uploadFilePart == null) {
            throw new BusinessValidationException("file", "Image file should be specified");
        }

        UserModel user = getUser();

        if(user.imageUrl != null) {
            S3FileLoader.delete(user.id + "/" + user.imageFileName);
        }

        String imageFileName = user.id + "/" + uploadFilePart.getFilename();
        String imageUrl = S3FileLoader.load(imageFileName, uploadFilePart.getFile());

        user.imageFileName = uploadFilePart.getFilename();
        user.imageUrl = imageUrl;
        user.save();

        return ok();
    }
}
