import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class HalParser {

    public <T> T parseObjectFromJson(String json, Class targetClass) {
        return null;
    }

    public <T> List<T> parseListFromJson(String json, Class targetClass) {
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
