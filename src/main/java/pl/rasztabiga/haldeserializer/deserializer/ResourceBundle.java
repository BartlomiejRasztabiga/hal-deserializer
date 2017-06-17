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

    private List<HalLink> halLinks;
    private List<T> resources;
    private T resource;

    ResourceBundle(JSONObject root, Class targetClass) {
        this.targetClass = targetClass;
        this.rootObject = root;

        //TODO Firstly do some checks

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
            resources.add(parseResource(object, classFields));
        }

        //TODO

        return resources;
    }

    private T retrieveResource(JSONObject root) {
        List<Field> classFields = retrieveClassFieldsList();
        resource = parseResource(root, classFields);

        return resource;
    }

    private List<HalLink> retrieveLinks(JSONObject _links) {
        halLinks = new ArrayList<>();

        halLinks.add(addLink("search", _links));
        halLinks.add(addLink("profile", _links));
        halLinks.add(addLink("self", _links));

        return halLinks;
    }

    private <T> T parseResource(JSONObject json, List<Field> classFields) { //TODO Add proxy class pl.rasztabiga.haldeserializer.deserializer.Resource<T> that holds content and halLinks
        try {
            Object targetClassInstance = targetClass.newInstance();
            for (Field classField : classFields) {
                Object fieldValue = null;
                try {
                    fieldValue = json.get(classField.getName());
                } catch (JSONException e) {
                    //Check if boolean field starts with "is" and remove it (compatible with SpringDataRest)
                    if (classField.getType().equals(Boolean.class)) {
                        String fieldName = classField.getName();
                        if (fieldName.substring(0, 2).contains("is")) {
                            try {
                                fieldName = fieldName.replaceFirst("is", "");
                                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                                fieldValue = json.get(fieldName);
                            } catch (JSONException e1) {
                                continue; //TODO Shitty code
                            }
                        }
                    } else {
                        continue; //IF field is not present in json, skip it
                    }
                }
                classField.setAccessible(true);

                //TODO Shitty code down there XD

                //Check if we should parse int to longs (JSON returns int, but ID is of type long)
                if (fieldValue instanceof Integer) {
                    Long fieldValueNew = (long) (int) fieldValue;
                    classField.set(targetClassInstance, fieldValueNew);
                }
                //Check if we should parse Date from String
                else if (classField.getType().equals(Date.class)) {
                    try {
                        String dateString = (String) fieldValue;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        Date date = simpleDateFormat.parse(dateString);
                        classField.set(targetClassInstance, date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        classField.set(targetClassInstance, null);
                    }
                } else {
                    classField.set(targetClassInstance, fieldValue);
                }

            }

            //TODO I finished here

            return (T) targetClassInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
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
