package pl.rasztabiga.haldeserializer.deserializer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.rasztabiga.haldeserializer.json.JSONException;
import pl.rasztabiga.haldeserializer.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResourceBundleTest {

    private Class noFieldsClass = NoFieldsClass.class;
    private Class twoFieldsClass = TwoFieldsClass.class;

    @Mock
    private HalParser mockParser;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void retrieveClassFieldsListWithNoFieldsClassGiven_test() throws Exception {
        ResourceBundle resourceBundle = new ResourceBundle(new JSONObject(), noFieldsClass);

        Method retrieveClassFieldsListMethod = ResourceBundle.class.getDeclaredMethod("retrieveClassFieldsList");
        retrieveClassFieldsListMethod.setAccessible(true);

        List<Field> fieldsList = (List<Field>) retrieveClassFieldsListMethod.invoke(resourceBundle);
        assertTrue(fieldsList.isEmpty());
    }

    @Test
    public void retrieveClassFieldsListWithTwoFieldsClassGiven_test() throws Exception {
        ResourceBundle resourceBundle = new ResourceBundle(new JSONObject(), twoFieldsClass);

        Method retrieveClassFieldsListMethod = ResourceBundle.class.getDeclaredMethod("retrieveClassFieldsList");
        retrieveClassFieldsListMethod.setAccessible(true);

        List<Field> fieldsList = (List<Field>) retrieveClassFieldsListMethod.invoke(resourceBundle);

        assertEquals(2, fieldsList.size());
        assertEquals("firstField", fieldsList.get(0).getName());
        assertEquals("secondField", fieldsList.get(1).getName());
    }

    // FIXME: 01.07.2017  Integration test
    @Test(expected = JSONException.class)
    public void retrieveResourceFromPlainJsonWithoutLinks_throwsJSONException_test() throws Exception { //TODO Support plain json in future
        String twoFieldsObjectInJson = "{\n" +
                "\t\"firstField\": \"something\",\n" +
                "\t\"secondField\": \"something else\"\n" +
                "}";
        JSONObject jsonObject = new JSONObject(twoFieldsObjectInJson);

        ResourceBundle resourceBundle = new ResourceBundle(jsonObject, twoFieldsClass);

        Field targetClassField = ResourceBundle.class.getDeclaredField("targetClass");
        targetClassField.setAccessible(true);
        targetClassField.set(resourceBundle, twoFieldsClass);

        Method retrieveResourceMethod = ResourceBundle.class.getDeclaredMethod("retrieveResource", JSONObject.class);
        retrieveResourceMethod.setAccessible(true);

        try {
            retrieveResourceMethod.invoke(resourceBundle, jsonObject);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof JSONException) {
                throw new JSONException(e.getCause());
            }
        }
    }

    // FIXME: 01.07.2017  Integration test
    @Test
    public void retrieveResourceFromHalJsonWithLinks_test() throws Exception {
        String twoFieldsObjectInJson = "{\n" +
                "\t\"firstField\": \"something\",\n" +
                "\t\"secondField\": \"something else\",\n" +
                "\t\"_links\": {\n" +
                "\t\t\n" +
                "\t}\n" +
                "}";
        JSONObject jsonObject = new JSONObject(twoFieldsObjectInJson);

        ResourceBundle resourceBundle = new ResourceBundle(jsonObject, twoFieldsClass);

        Method retrieveResourceMethod = ResourceBundle.class.getDeclaredMethod("retrieveResource", JSONObject.class);
        retrieveResourceMethod.setAccessible(true);

        Resource resource = (Resource) retrieveResourceMethod.invoke(resourceBundle, jsonObject);
        TwoFieldsClass object = (TwoFieldsClass) resource.getContent();

        assertEquals("something", object.getFirstField());
        assertEquals("something else", object.getSecondField());
    }

    @Test
    public void retrieveResourceFromHalJsonWithLinks_unitTest() throws Exception {
        String twoFieldsObjectInJson = "{\n" +
                "\t\"firstField\": \"something\",\n" +
                "\t\"secondField\": \"something else\",\n" +
                "\t\"_links\": {\n" +
                "\t\t\n" +
                "\t}\n" +
                "}";
        JSONObject jsonObject = new JSONObject(twoFieldsObjectInJson);

        ResourceBundle resourceBundle = new ResourceBundle(jsonObject, twoFieldsClass);

        Field parserField = ResourceBundle.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        parserField.set(resourceBundle, mockParser);

        when(mockParser.retrieveLinks(any(JSONObject.class))).thenReturn(new ArrayList<HalLink>()); //Return empty hal links list, not needed
        when(mockParser.parseResource(any(JSONObject.class), anyList(), any(Class.class))).thenReturn(new TwoFieldsClass("something", "something else"));

        Method retrieveResourceMethod = ResourceBundle.class.getDeclaredMethod("retrieveResource", JSONObject.class);
        retrieveResourceMethod.setAccessible(true);

        Resource resource = (Resource) retrieveResourceMethod.invoke(resourceBundle, jsonObject);
        TwoFieldsClass object = (TwoFieldsClass) resource.getContent();

        assertEquals("something", object.getFirstField());
        assertEquals("something else", object.getSecondField());
    }

    @Ignore
    @Test
    public void retrieveResourcesFromHalJsonWithEmbeddedAndLinks_test() throws Exception {
        //TODO RetrieveResource
        fail("Not yet implemented");
    }




    private static class NoFieldsClass {
    }

    private static class TwoFieldsClass {
        private String firstField;
        private String secondField;

        public TwoFieldsClass() {
        }

        public TwoFieldsClass(String firstField, String secondField) {
            this.firstField = firstField;
            this.secondField = secondField;
        }

        public String getFirstField() {
            return firstField;
        }

        public String getSecondField() {
            return secondField;
        }

        @Override
        public String toString() {
            return "TwoFieldsClass{" +
                    "firstField='" + firstField + '\'' +
                    ", secondField='" + secondField + '\'' +
                    '}';
        }
    }
}