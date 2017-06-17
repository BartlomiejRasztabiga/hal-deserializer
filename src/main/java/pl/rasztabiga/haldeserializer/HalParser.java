package pl.rasztabiga.haldeserializer;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class HalParser {

    public HalParser() {

    }

    public <T> T parseObjectFromJson(String json, Class targetClass) {
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

    public <T> List<T> parseListFromJson(String json, Class targetClass) {
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
