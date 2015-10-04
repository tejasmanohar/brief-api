package utils.websockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.data.validation.Validation;
import play.libs.Json;
import utils.exceptions.BusinessValidationException;

import javax.validation.ConstraintViolation;
import java.util.Iterator;
import java.util.Set;

public class WsJsonParser {

    public final static String EVENT_TYPE = "eventType";
    public final static String BODY = "body";

    public static WsEventType getEventType(JsonNode json){
        JsonNode eventType = json.get(EVENT_TYPE);

        if(eventType == null) {
            throw new BusinessValidationException("eventType", "Event type should be specified");
        }

        try{
            return WsEventType.valueOf(eventType.asText().toUpperCase());
        } catch (Exception e) {
            throw new BusinessValidationException("eventType", "Invalid event type");
        }
    }

    public static <T> T getJsonBody(JsonNode json, Class<T> clazz) {
        JsonNode body = json.get(BODY);

        if(body == null) {
            throw new BusinessValidationException("eventType", "Event type should be specified");
        }

        T t;

        try {
            t = Json.fromJson(body, clazz);
        } catch (Exception e) {
            throw new BusinessValidationException("json", "Can't parse json");
        }


        Set<ConstraintViolation<T>> constraintViolations = Validation.getValidator().validate(t);

        Logger.info("constraintViolations : " + constraintViolations.toString());

        if (constraintViolations.size() > 0) {
            throw new BusinessValidationException("body", contraintViolationToJson(constraintViolations).toString());
        }


        return t;
    }

    public static JsonNode getSuccess() {
        ObjectNode successJson = Json.newObject();
        successJson.put(EVENT_TYPE, WsEventType.SUCCESS.toString());
        return successJson;
    }

    public static JsonNode getError(JsonNode error) {
        ObjectNode successJson = Json.newObject();
        successJson.put(EVENT_TYPE, WsEventType.ERROR.toString());
        successJson.putPOJO(BODY, error);
        return successJson;
    }

    private static ObjectNode contraintViolationToJson(Set<?> violations) {
        ObjectNode result = Json.newObject();
        ObjectNode node = result.putObject("validationErrors");
        violations.forEach(error -> {
            ConstraintViolation<?> violation = (ConstraintViolation<?>) error;
            node.put(violation.getPropertyPath().toString(), violation.getMessage());
        });
        return result;
    }
}
