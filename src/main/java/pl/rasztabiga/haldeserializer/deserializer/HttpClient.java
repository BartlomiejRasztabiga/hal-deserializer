package pl.rasztabiga.haldeserializer.deserializer;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.rasztabiga.haldeserializer.exception.ResourceNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * HttpClient class representing OkHttpClient wrapper used to make http calls to REST API
 */
public class HttpClient {

    private URL baseUrl;
    private Map<String, String> httpHeaders;
    private Map<String, String> httpParams;

    HttpClient(URL baseUrl, Map<String, String> httpHeaders, Map<String, String> httpParams) {
        this.baseUrl = baseUrl;
        this.httpHeaders = httpHeaders;
        this.httpParams = httpParams;
    }

    String getJsonString() throws IOException, ResourceNotFoundException {
        OkHttpClient client = new OkHttpClient();

        addParamsToURL();

        Request request = new Request.Builder()
                .url(baseUrl)
                .headers(Headers.of(httpHeaders))
                .build();

        Response response = client.newCall(request).execute();

        if (response.code() == 404 || response.code() == 500 || response.code() == 403 || response.code() == 401) {
            throw new ResourceNotFoundException("Http code: " + response.code());
        }
        return response.body().string();
    }

    private void addParamsToURL() throws MalformedURLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?");
        for (Map.Entry<String, String> entry : httpParams.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String paramsURL = stringBuilder.toString();
        String lastChar = paramsURL.substring(paramsURL.length() - 1);
        if (lastChar.equals("&")) paramsURL = paramsURL.substring(0, paramsURL.length() - 1); //IF & is last char, remove it
        if (lastChar.equals("?")) paramsURL = paramsURL.substring(0, paramsURL.length() - 1); // IF ? is last char, remove it

        this.baseUrl = new URL(baseUrl.toString() + paramsURL);
    }
}
