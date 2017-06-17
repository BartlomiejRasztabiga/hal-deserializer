package pl.rasztabiga.haldeserializer;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HalDeserializer {
    private URL baseUrl;
    private Map<String, String> httpHeaders;
    private Map<String, String> params;

    private HalParser parser;

    private HalDeserializer() {
        this.parser = new HalParser();
    }

    //TODO Repeating code
    public <T> T toObject(Class<T> targetClass) { //TODO Finish this
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

    public <T> List<T> toList(Class<T> targetClass) { //TODO Make targetClass more generic
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
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl) //TODO Add params
                .headers(Headers.of(httpHeaders))
                .build();

        Response response = client.newCall(request).execute();

        //TODO Move checking response code to other class
        if (response.code() == 404 || response.code() == 500 || response.code() == 403 || response.code() == 401) {
            throw new ResourceNotFoundException("Http code: " + response.code());
        }
        return response.body().string();
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
            instance.httpHeaders = headers;
            return this;
        }

        public Builder withParams(Map<String, String> params) {
            instance.params = params;
            return this;
        }

        public HalDeserializer build() {
            return instance;
        }

    }
}
