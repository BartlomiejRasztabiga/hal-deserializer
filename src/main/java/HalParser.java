import org.json.JSONObject;

import java.util.List;

public class HalParser {

    public <T> T parseObjectFromJson(String json) {
        return null;
    }

    public <T> List<T> parseListFromJson(String json) {
        JSONObject root = new JSONObject(json);
        Resource resource = new Resource(root);
        return resource.parseList();
    }


}
