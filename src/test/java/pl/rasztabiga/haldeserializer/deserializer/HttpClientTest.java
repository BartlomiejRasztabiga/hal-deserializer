package pl.rasztabiga.haldeserializer.deserializer;

import org.junit.Test;
import pl.rasztabiga.haldeserializer.exception.ResourceNotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpClientTest {

    @Test
    public void addTwoParamsToURL_test() throws Exception {
        String mockBaseUrl = "https://httpbin.org/get";

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("a", "123");
        queryParams.put("b", "xyz");

        HttpClient httpClient = new HttpClient(
                new URL(mockBaseUrl),
                new HashMap<String, String>(),
                queryParams
        );

        Method addParamsToURLMethod = HttpClient.class.getDeclaredMethod("addParamsToURL");
        addParamsToURLMethod.setAccessible(true);

        addParamsToURLMethod.invoke(httpClient);

        Field baseUrlField = HttpClient.class.getDeclaredField("baseUrl");
        baseUrlField.setAccessible(true);

        String baseURL = baseUrlField.get(httpClient).toString();

        assertEquals(mockBaseUrl + "?a=123&b=xyz", baseURL);
    }

    @Test
    public void addEmptyParamsToURL_ShouldBeEqualsToBaseURL_test() throws Exception {
        String mockBaseUrl = "https://httpbin.org/get";

        HttpClient httpClient = new HttpClient(
                new URL(mockBaseUrl),
                new HashMap<String, String>(),
                new HashMap<String, String>()
        );

        Method addParamsToURLMethod = HttpClient.class.getDeclaredMethod("addParamsToURL");
        addParamsToURLMethod.setAccessible(true);

        addParamsToURLMethod.invoke(httpClient);

        Field baseUrlField = HttpClient.class.getDeclaredField("baseUrl");
        baseUrlField.setAccessible(true);

        String baseURL = baseUrlField.get(httpClient).toString();

        assertEquals(mockBaseUrl, baseURL);
    }

    //Integration test
    @Test
    public void getJsonStringFromRestApi_test() throws Exception {
        String mockBaseUrl = "https://httpbin.org/get";

        HttpClient httpClient = new HttpClient(
                new URL(mockBaseUrl),
                new HashMap<String, String>(),
                new HashMap<String, String>()
        );

        Method addParamsToURLMethod = HttpClient.class.getDeclaredMethod("getJsonString");
        addParamsToURLMethod.setAccessible(true);

        String jsonString = (String) addParamsToURLMethod.invoke(httpClient);

        assertFalse(jsonString.isEmpty());

        // To know that it certainly is JSON
        assertTrue(jsonString.startsWith("{"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getJsonStringFrom404NotFoundUrl_throwsResourceNotFoundException_test() throws Exception {
        String mockBaseUrl = "https://httpbin.org/status/404";

        HttpClient httpClient = new HttpClient(
                new URL(mockBaseUrl),
                new HashMap<String, String>(),
                new HashMap<String, String>()
        );

        Method addParamsToURLMethod = HttpClient.class.getDeclaredMethod("getJsonString");
        addParamsToURLMethod.setAccessible(true);

        try {
            addParamsToURLMethod.invoke(httpClient);
        }  catch (InvocationTargetException e) {
            if (e.getCause() instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getCause());
            }
        }
    }
}