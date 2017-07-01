package pl.rasztabiga.haldeserializer.deserializer;

import pl.rasztabiga.haldeserializer.exception.DeserializationError;
import pl.rasztabiga.haldeserializer.exception.ResourceNotFoundException;
import pl.rasztabiga.haldeserializer.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main library class used to proxy between user and HAL parser
 *
 * @author Bartłomiej Rasztabiga
 * @version 1.0.0
 * @since 1.0
 */
public class HalDeserializer {
    private URL baseUrl;
    private Map<String, String> httpHeaders;
    private Map<String, String> httpParams;

    private HalParser parser;

    private HalDeserializer() {
        this.parser = new HalParser();
        this.httpHeaders = new HashMap<>();
        this.httpParams = new HashMap<>();
    }

    /**
     * Deserialize single object to given type
     *
     * @param targetClass Class to use to deserialize
     * @param <T>         Resource type
     * @return Deserialized object
     */
    public <T> Resource<T> toObject(Class<T> targetClass) {
        String json = "";
        try {
            json = getJsonStringFromUrl();
        } catch (IOException e) {
            System.out.println("JSON is empty!");
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found. Check your URL. " + e.getMessage());
        }
        return deserializeObjectFromJson(json, targetClass);
    }

    /**
     * Deserialize objects list to given type
     *
     * @param targetClass Class to use to deserialize
     * @param <T>         Resource type
     * @return Deserialized objects list
     */
    public <T> List<Resource<T>> toList(Class<T> targetClass) {
        String json;
        try {
            json = getJsonStringFromUrl();
        } catch (IOException e) {
            System.out.println("JSON is empty!");
            return Collections.emptyList();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found. Check your URL. " + e.getMessage());
            return Collections.emptyList();
        }
        return deserializeListFromJson(json, targetClass);
    }

    private <T> Resource<T> deserializeObjectFromJson(String json, Class targetClass) {
        if (json.isEmpty()) {
            return null;
        }
        JSONObject root = new JSONObject(json);
        ResourceBundle<T> resourceBundle = new ResourceBundle<>(root, targetClass);
        try {
            return resourceBundle.getResource();
        } catch (DeserializationError e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private <T> List<Resource<T>> deserializeListFromJson(String json, Class targetClass) {
        if (json.isEmpty()) {
            return Collections.emptyList();
        }
        JSONObject root = new JSONObject(json);
        ResourceBundle<T> resourceBundle = new ResourceBundle<>(root, targetClass);
        try {
            return resourceBundle.getResources();
        } catch (DeserializationError e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    private String getJsonStringFromUrl() throws IOException, ResourceNotFoundException {
        HttpClient client = new HttpClient(baseUrl, httpHeaders, httpParams);
        return client.getJsonString();
    }

    /**
     * Builder class for HalDeserializer
     *
     * @author Bartłomiej Rasztabiga
     * @version 1.0.0
     * @since 1.0
     */
    public static class Builder {
        private HalDeserializer instance;

        /**
         * Default constructor
         */
        public Builder() {
            instance = new HalDeserializer();
        }

        /**
         * Adds base URL of REST API
         *
         * @param url REST APO URL
         * @throws MalformedURLException When given URL is incorrect
         * @return Builder instance
         */
        public Builder baseUrl(String url) throws MalformedURLException {
            instance.baseUrl = new URL(url);
            return this;
        }

        /**
         * Adds HTTP headers
         *
         * @param headers Map representing http headers
         * @return Builder instance
         */
        public Builder withHeaders(Map<String, String> headers) {
            if (instance.httpHeaders.size() == 0) {
                instance.httpHeaders = headers;
            } else {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    instance.httpHeaders.put(entry.getKey(), entry.getValue());
                }
            }

            return this;
        }

        /**
         * Adds accessToken to authentication header
         *
         * @param accessToken OAuth access token
         * @return Builder instance
         */
        public Builder withAuthentication(String accessToken) {
            instance.httpHeaders.put(Authentication.AUTHORIZATION, Authentication.BEARER + " " + accessToken);
            return this;
        }

        /**
         * Adds URL parameters
         *
         * @param params Map representing URL paremeters
         * @return Builder instance
         */
        public Builder withParams(Map<String, String> params) {
            instance.httpParams = params;
            return this;
        }

        /**
         * Builds instance of HalDeserializer
         *
         * @return HalDeserializer instance
         */
        public HalDeserializer build() {
            return instance;
        }

    }
}
