package pl.rasztabiga.haldeserializer.deserializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * HalParser class representing HAL+JSON parser
 *
 * @author Bartłomiej Rasztabiga
 * @version 1.0
 * @since 1.0
 */
public class HalParser {

    /**
     * Default empty constructor
     */
    public HalParser() {
    }

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


}
