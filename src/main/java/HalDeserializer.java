import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
    public <T> T toObject() {
        String json;
        try {
            json = getJsonStringFromUrl();
        } catch (Exception e) {
            System.out.println("JSON is empty!");
            return null;
        }
        System.out.println(json);
        return parser.parseObjectFromJson(json);
    }

    public <T> List<T> toList() {
        String json;
        try {
            json = getJsonStringFromUrl();
        } catch (Exception e) {
            System.out.println("JSON is empty!");
            return null;
        }
        return parser.parseListFromJson(json);
    }

    private String getJsonStringFromUrl() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl) //TODO Add params
                .headers(Headers.of(httpHeaders))
                .build();

        Response response = client.newCall(request).execute();
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
