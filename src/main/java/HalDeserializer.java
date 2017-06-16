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

    public <T> T toObject() {

    }

    public <T> List<T> toList() {

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
