package pl.rasztabiga.haldeserializer.deserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.rasztabiga.haldeserializer.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResourceBundleTest {

    private Class noFieldsClass = NoFieldsClass.class;
    private Class twoFieldsClass = TwoFieldsClass.class;

    @Mock
    private HalParser mockParser;

    private TwoFieldsClass twoFieldsObject = new TwoFieldsClass("First", "Second");

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

    @Test
    public void retrieveResourceWithTwoFieldsClassAndPlainJson_test() throws Exception {
        ResourceBundle resourceBundle = new ResourceBundle(new JSONObject(), twoFieldsClass);

        //TODO

    }

    private static class NoFieldsClass { }

    private static class TwoFieldsClass {
        private String firstField;
        private String secondField;

        public TwoFieldsClass(String firstField, String secondField) {
            this.firstField = firstField;
            this.secondField = secondField;
        }
    }
}