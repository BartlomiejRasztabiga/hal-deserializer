import org.json.JSONObject;

import java.util.List;

public class HalParser {

    public <T> T parseObjectFromJson(String json, Class targetClass) {
        return null;
    }

    public <T> List<T> parseListFromJson(String json, Class targetClass) {
        JSONObject root = new JSONObject(json);
        ResourceBundle<T> resourceBundle = new ResourceBundle<>(root, targetClass);
        return resourceBundle.getResources();
    }


}
