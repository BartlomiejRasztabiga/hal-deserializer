package pl.rasztabiga.haldeserializer.deserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.rasztabiga.haldeserializer.exception.DeserializationError;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ResourceBundle<T> {
    private Class targetClass;
    private JSONObject rootObject;

    private HalParser parser;

    private List<HalLink> halLinks;
    private List<Resource<T>> resources;
    private Resource<T> resource;

    ResourceBundle(JSONObject root, Class targetClass) {
        this.targetClass = targetClass;
        this.rootObject = root;
        this.parser = new HalParser();
    }

    List<Resource<T>> getResources() throws DeserializationError { //TODO Maybe add Resources class wrapper
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
        this.halLinks = parser.retrieveLinks(rootObject.getJSONObject("_links"));

        return resources;
    }

    Resource<T> getResource() throws DeserializationError {
        this.resource = retrieveResource(rootObject);
        this.halLinks = parser.retrieveLinks(rootObject.getJSONObject("_links"));

        return resource;
    }

    private List<Resource<T>> retrieveResources(JSONObject _embedded) {
        String rootObject = _embedded.keySet().toArray()[0].toString(); //maybe
        JSONArray root = _embedded.getJSONArray(rootObject);

        Iterator<Object> iterator = root.iterator();

        List<Resource<T>> resources = new ArrayList<>();

        List<Field> classFields = retrieveClassFieldsList();

        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            resources.add(parser.parseResource(object, classFields, targetClass));
        }

        return resources;
    }

    private Resource<T> retrieveResource(JSONObject root) {
        List<Field> classFields = retrieveClassFieldsList();
        resource = parser.parseResource(root, classFields, targetClass);

        return resource;
    }

    private List<Field> retrieveClassFieldsList() {
        Field[] fields = targetClass.getDeclaredFields();
        return Arrays.asList(fields);
    }


}
