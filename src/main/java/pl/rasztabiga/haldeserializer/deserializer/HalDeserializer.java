package pl.rasztabiga.haldeserializer.deserializer;

import pl.rasztabiga.haldeserializer.exception.ResourceNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static class Builder {
        private HalDeserializer instance;

        public Builder() {
            instance = new HalDeserializer();
        }

        public Builder baseUrl(String url) {
            try {
                instance.baseUrl = new URL(url);
            } catch (MalformedURLException e) {
                System.out.println("Wrong URL!");
            }
            return this;
        }

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

        public Builder withAuthentication(Authentication.Header header) {
            instance.httpHeaders.put(Authentication.AUTHORIZATION, header.getToken());
            return this;
        }

        public Builder withParams(Map<String, String> params) {
            instance.httpParams = params;
            return this;
        }

        public HalDeserializer build() {
            return instance;
        }

    }
}
