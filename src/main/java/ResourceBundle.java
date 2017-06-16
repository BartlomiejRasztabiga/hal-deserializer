import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceBundle {

    private List<Link> links;
    private List<Resource> resources;
    private Class targetClass;

    public ResourceBundle(JSONObject root, Class targetClass) {
        this.targetClass = targetClass;
        this.resources = retrieveResources(root.getJSONObject("_embedded"));
        this.links = retrieveLinks(root.getJSONObject("_links"));
    }

    private List<Link> retrieveLinks(JSONObject _links) {
        links = new ArrayList<>();

        links.add(addLink("search", _links));
        links.add(addLink("profile", _links));
        links.add(addLink("self", _links));

        return links;
    }

    private List<Resource> retrieveResources(JSONObject _embedded) {

        String rootObject = _embedded.keySet().toArray()[0].toString(); //maybe
        JSONArray root = _embedded.getJSONArray(rootObject);

        Iterator<Object> iterator = root.iterator();

        List<Resource> resources = new ArrayList<>();
        List<String> classFields = retrieveClassFieldsList();

        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            resources.add(parseResource(object, classFields));
        }

        System.out.println(classFields);
        System.out.println(resources);
        //TODO

        return resources;
    }

    private Resource parseResource(JSONObject json, List<String> classFields) {
        //TODO
        return null;
    }

    private List<String> retrieveClassFieldsList() {
        Field[] fields = targetClass.getDeclaredFields();
        return Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    private Link addLink(String name, JSONObject _links) {
        try {
            return new Link(name, new URL(_links.getJSONObject(name).getString("href")));
        } catch (JSONException | MalformedURLException e) {
            return new Link(name);
        }
    }

}
