package utils.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class BusinessValidationException extends RuntimeException {
    private int httpResponseCode;
    public String message;
    public String violationField;

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public BusinessValidationException(String violationField, String message, int httpResponseCode) {
        super(message + ":" + httpResponseCode);
        this.message = message;
        this.violationField = violationField;
        this.httpResponseCode = httpResponseCode;
    }

    public BusinessValidationException(String violationField, String message) {
        super(message + ":" + 400);
        this.message = message;
        this.violationField = violationField;
        this.httpResponseCode = 400;
    }

    public JsonNode getJson() {
        ObjectNode result = Json.newObject();
        ObjectNode node = result.putObject("validationErrors");
        node.put(this.violationField, this.message);
        return result;
    }
}