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

        return retrieveResources(_embedded);
    }

    Resource<T> getResource() throws DeserializationError {
        return retrieveResource(rootObject);
    }

    private List<Resource<T>> retrieveResources(JSONObject _embedded) {
        String rootObject = _embedded.keySet().toArray()[0].toString(); //maybe
        JSONArray root = _embedded.getJSONArray(rootObject);

        Iterator<Object> iterator = root.iterator();

        List<Resource<T>> resources = new ArrayList<>();

        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            resources.add(retrieveResource(object));
        }

        return resources;
    }

    private Resource<T> retrieveResource(JSONObject root) {
        List<Field> classFields = retrieveClassFieldsList();
        List<HalLink> links = parser.retrieveLinks(rootObject.getJSONObject("_links"));
        return new Resource<>(parser.parseResource(root, classFields, targetClass), links);
    }

    private List<Field> retrieveClassFieldsList() {
        Field[] fields = targetClass.getDeclaredFields();
        return Arrays.asList(fields);
    }

}
