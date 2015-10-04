package utils.amazone;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import plugins.S3Plugin;
import utils.exceptions.BusinessValidationException;
import java.io.File;


public class S3FileLoader {

    public static String load(String fileName, File file) {
        if (S3Plugin.amazonS3 == null) {
            throw new BusinessValidationException("image", "Could not load");
        } else {
            PutObjectRequest putObjectRequest = new PutObjectRequest(S3Plugin.s3Bucket, fileName, file);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
        }

        return "https://s3.amazonaws.com/" + S3Plugin.s3Bucket + "/" + fileName;
    }


    public static void delete(String fileName) {
        if (S3Plugin.amazonS3 == null) {
            throw new BusinessValidationException("image", "Could not delete previous image");
        } else {
            S3Plugin.amazonS3.deleteObject(S3Plugin.s3Bucket, fileName);
        }
    }
}
