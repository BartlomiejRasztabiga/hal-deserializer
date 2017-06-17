package pl.rasztabiga.haldeserializer;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class Authentication {

    public static Map.Entry<String, String> getOAuthTokenHeader(String tokenAccessURL, Map<String, String> params) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String jsonBody = new JSONObject(params).toString();

            RequestBody body = RequestBody.create(JSON, jsonBody);

            Request request = new Request.Builder()
                    .url(tokenAccessURL)
                    .addHeader("content-type", "application/json")
                    .post(body)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String accessToken = new JSONObject(response.body().string()).getString("access_token");

            return new AbstractMap.SimpleEntry<>("Authorization", "Bearer " + accessToken);
        } catch (IOException e) {
            System.out.println("Couldn't get OAuth token");
            return null;
        }
    }
}
