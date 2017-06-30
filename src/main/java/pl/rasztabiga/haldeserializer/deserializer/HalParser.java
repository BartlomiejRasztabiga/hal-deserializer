package pl.rasztabiga.haldeserializer.deserializer;

import pl.rasztabiga.haldeserializer.json.JSONArray;
import pl.rasztabiga.haldeserializer.json.JSONException;
import pl.rasztabiga.haldeserializer.json.JSONObject;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * HalParser class representing HAL+JSON parser
 *
 * @author Bartłomiej Rasztabiga
 * @version 1.0.0
 * @since 1.0
 */
public class HalParser {

    /**
     * Default empty constructor
     */
    public HalParser() {
    }

    // FIXME: 17.06.2017 
    @SuppressWarnings("unchecked")
    <T> T parseResource(JSONObject json, List<Field> classFields, Class targetClass) { //TODO Add proxy class Resource<T> that holds content and halLinks
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
                if (fieldValue instanceof Integer && classField.getType().equals(Long.class)) {
                    Long fieldValueNew = (long) (int) fieldValue;
                    classField.set(targetClassInstance, fieldValueNew);
                }
                //Check if we should convert JSONArray to List
                else if (fieldValue instanceof JSONArray) { //TODO Make it more generic
                    JSONArray array = (JSONArray) fieldValue;
                    List<Object> list = array.toList();
                    classField.set(targetClassInstance, list);
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

            return (T) targetClassInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    List<HalLink> retrieveLinks(JSONObject _links) {
        List<HalLink> halLinks = new ArrayList<>();

        halLinks.add(addLink("search", _links));
        halLinks.add(addLink("profile", _links));
        halLinks.add(addLink("self", _links));

        return halLinks;
    }

    private HalLink addLink(String name, JSONObject _links) {
        try {
            return new HalLink(name, new URL(_links.getJSONObject(name).getString("href")));
        } catch (JSONException | MalformedURLException e) {
            return new HalLink(name);
        }
    }

}
