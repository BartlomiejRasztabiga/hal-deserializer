package pl.rasztabiga.haldeserializer;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Authentication {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";

    private static String getOAuthTokenHeader(String tokenAccessURL, Map<String, String> params) {
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

            return accessToken;
        } catch (IOException | JSONException e) {
            System.out.println("Couldn't get OAuth token");
            return null;
        }
    }

    public static class Header {
        private Map<String, String> oauthParams = new HashMap<>();
        private String tokenAccessURL;

        private String OAuthToken;

        public Header tokenAccessURL(String tokenAccessURL) {
            this.tokenAccessURL = tokenAccessURL;
            return this;
        }

        public Header clientID(String clientID) {
            this.oauthParams.put("client_id", clientID);
            return this;
        }

        public Header clientSecret(String clientSecret) {
            this.oauthParams.put("client_secret", clientSecret);
            return this;
        }

        public Header audience(String audience) {
            this.oauthParams.put("audience", audience);
            return this;
        }

        public Header grantType(String grantType) {
            this.oauthParams.put("grant_type", grantType);
            return this;
        }

        public Header scope(String scope) {
            this.oauthParams.put("scope", scope);
            return this;
        }

        public Header build() {
            return this;
        }

        public String getToken() {
            return BEARER + " " + getOAuthTokenHeader(tokenAccessURL, oauthParams);
        }
    }
}
