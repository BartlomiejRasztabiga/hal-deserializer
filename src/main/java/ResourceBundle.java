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

public class ResourceBundle<T> {
    private Class targetClass;
    private JSONObject rootObject;

    private List<Link> links;
    private List<T> resources;

    public ResourceBundle(JSONObject root, Class targetClass) {
        this.targetClass = targetClass;
        this.rootObject = root;

        //TODO Firstly do some checks

    }

    public List<T> getResources() throws DeserializationError {
        JSONObject _embedded = null;
        try {
            _embedded = rootObject.getJSONObject("_embedded");
        } catch (JSONException e) {
            //It might mean that resource is not list
            if(e.getMessage().contains("JSONObject[\"_embedded\"] not found")) {
                throw new DeserializationError("Resource cannot be deserialized to list!");
            }
        }

        this.resources = retrieveResources(_embedded);
        this.links = retrieveLinks(rootObject.getJSONObject("_links"));

        return resources;
    }

    private List<Link> retrieveLinks(JSONObject _links) {
        links = new ArrayList<>();

        links.add(addLink("search", _links));
        links.add(addLink("profile", _links));
        links.add(addLink("self", _links));

        return links;
    }

    private List<T> retrieveResources(JSONObject _embedded) {

        String rootObject = _embedded.keySet().toArray()[0].toString(); //maybe
        JSONArray root = _embedded.getJSONArray(rootObject);

        Iterator<Object> iterator = root.iterator();

        List<T> resources = new ArrayList<>();

        List<Field> classFields = retrieveClassFieldsList();

        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            resources.add(parseResource(object, classFields));
        }

        //TODO

        return resources;
    }

    private <T> T parseResource(JSONObject json, List<Field> classFields) { //TODO Add proxy class Resource<T> that holds content and links
        try {
            Object object = targetClass.newInstance();
            for (Field classField : classFields) {
                Object fieldValue;
                try {
                    fieldValue = json.get(classField.getName());
                } catch (JSONException e) {
                    continue; //IF field is not present in json, skip it
                }
                classField.setAccessible(true);

                //Check if we should parse int to longs (JSON returns int, but ID is of type long)
                if(fieldValue instanceof Integer) {
                    Long fieldValueNew = (long) (int) fieldValue;
                    classField.set(object, fieldValueNew);
                } else {
                    classField.set(object, fieldValue);
                }

            }

            //TODO I finished here

            return (T) object;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<Field> retrieveClassFieldsList() {
        Field[] fields = targetClass.getDeclaredFields();
        return Arrays.asList(fields);
    }

    private Link addLink(String name, JSONObject _links) {
        try {
            return new Link(name, new URL(_links.getJSONObject(name).getString("href")));
        } catch (JSONException | MalformedURLException e) {
            return new Link(name);
        }
    }

}
