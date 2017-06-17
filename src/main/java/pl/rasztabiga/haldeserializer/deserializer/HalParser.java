package pl.rasztabiga.haldeserializer.deserializer;

import org.json.JSONObject;
import pl.rasztabiga.haldeserializer.exception.DeserializationError;

import java.util.Collections;
import java.util.List;

/**
 * HalParser class representing HAL+JSON parser
 *
 * @author Bartłomiej Rasztabiga
 * @version 1.0
 * @since 1.0
 */
public class HalParser {

    /**
     * Default empty constructor
     */
    public HalParser() {
    }

    //TODO Move this class methods to HalDeserializer and use it only for parsing

    <T> T parseObjectFromJson(String json, Class targetClass) {
        if (json.isEmpty()) {
            return null;
        }
        JSONObject root = new JSONObject(json);
        ResourceBundle<T> resourceBundle = new ResourceBundle<>(root, targetClass);
        try {
            return resourceBundle.getResource();
        } catch (DeserializationError e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    <T> List<T> parseListFromJson(String json, Class targetClass) {
        if (json.isEmpty()) {
            return Collections.emptyList();
        }
        JSONObject root = new JSONObject(json);
        ResourceBundle<T> resourceBundle = new ResourceBundle<>(root, targetClass);
        try {
            return resourceBundle.getResources();
        } catch (DeserializationError e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }


}
