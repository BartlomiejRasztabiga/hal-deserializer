package pl.rasztabiga.haldeserializer.deserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.rasztabiga.haldeserializer.exception.DeserializationError;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResourceBundle<T> {
    private Class targetClass;
    private JSONObject rootObject;

    private HalParser parser;

    private List<HalLink> halLinks;
    private List<T> resources;
    private T resource;

    ResourceBundle(JSONObject root, Class targetClass) {
        this.targetClass = targetClass;
        this.rootObject = root;
        this.parser = new HalParser();
    }

    List<T> getResources() throws DeserializationError {
        JSONObject _embedded = null;
        try {
            _embedded = rootObject.getJSONObject("_embedded");
        } catch (JSONException e) {
            //It might mean that resource is not list
            if (e.getMessage().contains("JSONObject[\"_embedded\"] not found")) {
                throw new DeserializationError("Resource cannot be deserialized to list!");
            }
        }

        this.resources = retrieveResources(_embedded);
        this.halLinks = retrieveLinks(rootObject.getJSONObject("_links"));

        return resources;
    }

    T getResource() throws DeserializationError {
        this.resource = retrieveResource(rootObject);
        this.halLinks = retrieveLinks(rootObject.getJSONObject("_links"));

        return resource;
    }

    private List<T> retrieveResources(JSONObject _embedded) {

        String rootObject = _embedded.keySet().toArray()[0].toString(); //maybe
        JSONArray root = _embedded.getJSONArray(rootObject);

        Iterator<Object> iterator = root.iterator();

        List<T> resources = new ArrayList<>();

        List<Field> classFields = retrieveClassFieldsList();

        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            resources.add(parser.parseResource(object, classFields, targetClass));
        }

        //TODO

        return resources;
    }

    private T retrieveResource(JSONObject root) {
        List<Field> classFields = retrieveClassFieldsList();
        resource = parser.parseResource(root, classFields, targetClass);

        return resource;
    }

    private List<HalLink> retrieveLinks(JSONObject _links) {
        halLinks = new ArrayList<>();

        halLinks.add(addLink("search", _links));
        halLinks.add(addLink("profile", _links));
        halLinks.add(addLink("self", _links));

        return halLinks;
    }
    
    private List<Field> retrieveClassFieldsList() {
        Field[] fields = targetClass.getDeclaredFields();
        return Arrays.asList(fields);
    }

    private HalLink addLink(String name, JSONObject _links) {
        try {
            return new HalLink(name, new URL(_links.getJSONObject(name).getString("href")));
        } catch (JSONException | MalformedURLException e) {
            return new HalLink(name);
        }
    }

}
