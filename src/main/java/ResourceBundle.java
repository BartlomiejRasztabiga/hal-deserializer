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

    private List<Link> links;
    private List<T> resources;
    private Class targetClass;

    public ResourceBundle(JSONObject root, Class targetClass) {
        this.targetClass = targetClass;
        this.resources = retrieveResources(root.getJSONObject("_embedded"));
        this.links = retrieveLinks(root.getJSONObject("_links"));
    }

    public List<T> getResources() {
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

    private <T> T parseResource(JSONObject json, List<Field> classFields) {
        try {
            Object object = targetClass.newInstance();
            for (Field classField : classFields) {
                Object fieldValue = null;
                try {
                    fieldValue = json.get(classField.getName());
                } catch (JSONException e) {
                    continue; //IF field is not present in json, skip it
                }
                //System.out.println(fieldValue);
                classField.setAccessible(true);
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
