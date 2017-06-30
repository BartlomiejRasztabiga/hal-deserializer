package pl.rasztabiga.haldeserializer.deserializer;

import org.junit.Test;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HalDeserializerTest {

    @Test(expected = MalformedURLException.class)
    public void buildInstanceWithMalformedURL_test() throws Exception {
        HalDeserializer deserializer = new HalDeserializer.Builder()
                .baseUrl("somethingCertainlyNotURL")
                .build();
    }

    @Test(expected = MalformedURLException.class)
    public void buildInstanceWithURLWithoutSpecifiedProtocol_test() throws Exception {
        HalDeserializer deserializer = new HalDeserializer.Builder()
                .baseUrl("google.com")
                .build();
    }

    @Test
    public void buildInstanceWithEmptyHeaders_test() throws Exception {
        HalDeserializer deserializer = new HalDeserializer.Builder()
                .withHeaders(new HashMap<String, String>())
                .build();

        Map<String, String> httpHeaders = (Map<String, String>) getHalDeserializerField("httpHeaders", deserializer);

        assertTrue(httpHeaders.isEmpty());
    }

    @Test
    public void buildInstanceWithHeadersAddedOnTopOfAnotherHeaders_test() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Test", "Test");
        headers.put("Test1", "Test1");

        Map<String, String> newHeaders = new HashMap<>();
        newHeaders.put("Test2", "Test2");
        newHeaders.put("Test3", "Test3");

        HalDeserializer deserializer = new HalDeserializer.Builder()
                .withHeaders(headers)
                .withHeaders(newHeaders)
                .build();

        Map<String, String> httpHeaders = (Map<String, String>) getHalDeserializerField("httpHeaders", deserializer);

        assertEquals(4, httpHeaders.size());
    }

    @Test
    public void buildInstanceWithAccessTokenAuthenticationHeader_test() throws Exception {
        String mockedAccessToken = "blablablaWhatever";

        HalDeserializer deserializer = new HalDeserializer.Builder()
                .withAuthentication(mockedAccessToken)
                .build();

        Map<String, String> httpHeaders = (Map<String, String>) getHalDeserializerField("httpHeaders", deserializer);

        assertEquals(Authentication.BEARER + " " + mockedAccessToken, httpHeaders.get(Authentication.AUTHORIZATION));
    }

    @Test
    public void buildInstanceWithEmptyQueryParams_test() throws Exception {
        HalDeserializer deserializer = new HalDeserializer.Builder()
                .withParams(new HashMap<String, String>())
                .build();

        Map<String, String> queryParams = (Map<String, String>) getHalDeserializerField("httpParams", deserializer);

        assertTrue(queryParams.isEmpty());
    }

    @Test
    public void buildInstanceWithNonEmptyQueryParams_test() throws Exception {
        Map<String, String> mockedQueryParams = new HashMap<>();
        mockedQueryParams.put("Test", "Test");
        mockedQueryParams.put("Test1", "Test1");

        HalDeserializer deserializer = new HalDeserializer.Builder()
                .withParams(mockedQueryParams)
                .build();

        Map<String, String> queryParams = (Map<String, String>) getHalDeserializerField("httpParams", deserializer);

        assertEquals(2, queryParams.size());
    }

    @Test
    public void buildInstanceWithAllProperData_test() throws Exception {
        HalDeserializer deserializer = new HalDeserializer.Builder()
                .baseUrl("http://rest-api.com")
                .withAuthentication("dummyAccessToken")
                .build();
    }




    private Object getHalDeserializerField(String name, Object instance) throws Exception {
        Field field = HalDeserializer.class.getDeclaredField(name);
        field.setAccessible(true);

        return field.get(instance);
    }
}