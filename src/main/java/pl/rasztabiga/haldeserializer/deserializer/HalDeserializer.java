package pl.rasztabiga.haldeserializer.deserializer;

import pl.rasztabiga.haldeserializer.exception.ResourceNotFoundException;

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
 * @version 1.0
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
    //TODO Repeating code
    public <T> T toObject(Class<T> targetClass) {
        String json = "";
        try {
            json = getJsonStringFromUrl();
        } catch (IOException e) {
            System.out.println("JSON is empty!");
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found. Check your URL. " + e.getMessage());
        }
        return parser.parseObjectFromJson(json, targetClass);
    }

    /**
     * Deserialize objects list to given type
     *
     * @param targetClass Class to use to deserialize
     * @param <T>         Resource type
     * @return Deserialized objects list
     */
    public <T> List<T> toList(Class<T> targetClass) {
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
        return parser.parseListFromJson(json, targetClass);
    }

    private String getJsonStringFromUrl() throws IOException, ResourceNotFoundException {
        HttpClient client = new HttpClient(baseUrl, httpHeaders, httpParams);
        return client.getJsonString();
    }

    /**
     * Builder class for HalDeserializer
     *
     * @author Bartłomiej Rasztabiga
     * @version 1.0
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
         * @return Builder instance
         */
        public Builder baseUrl(String url) {
            try {
                instance.baseUrl = new URL(url);
            } catch (MalformedURLException e) {
                System.out.println("Wrong URL!");
            }
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
         * Adds Authentication Header
         *
         * @param header Authentication.Header instance
         * @return Builder instance
         */
        public Builder withAuthentication(Authentication.Header header) {
            instance.httpHeaders.put(Authentication.AUTHORIZATION, header.getToken());
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
