package pl.rasztabiga.haldeserializer.deserializer;

import okhttp3.*;
import pl.rasztabiga.haldeserializer.json.JSONException;
import pl.rasztabiga.haldeserializer.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication class to support Authentication headers sent to REST API
 *
 * @author Bartłomiej Rasztabiga
 * @version 1.0
 * @since 1.0
 */
public class Authentication {

    static final String AUTHORIZATION = "Authorization";
    static final String BEARER = "Bearer";

    private Authentication() {
    }

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


    /**
     * Header class representing authorization header
     *
     * @author Bartłomiej Rasztabiga
     * @version 1.0
     * @since 1.0
     */
    public static class Header {
        private Map<String, String> oauthParams = new HashMap<>();
        private String tokenAccessURL;

        private String OAuthToken;

        /**
         * Adds URL to request OAuth token from
         *
         * @param tokenAccessURL Access token URL
         * @return Header instance
         */
        public Header tokenAccessURL(String tokenAccessURL) {
            this.tokenAccessURL = tokenAccessURL;
            return this;
        }

        /**
         * Adds client ID to request
         *
         * @param clientID ClientID
         * @return Header instance
         */
        public Header clientID(String clientID) {
            this.oauthParams.put("client_id", clientID);
            return this;
        }

        /**
         * Adds client secret
         *
         * @param clientSecret Client secret
         * @return Header instance
         */
        public Header clientSecret(String clientSecret) {
            this.oauthParams.put("client_secret", clientSecret);
            return this;
        }

        /**
         * Adds token audience
         *
         * @param audience Token audience
         * @return Header instance
         */
        public Header audience(String audience) {
            this.oauthParams.put("audience", audience);
            return this;
        }

        /**
         * Adds token grant type
         *
         * @param grantType Token grant type
         * @return Header instance
         */
        public Header grantType(String grantType) {
            this.oauthParams.put("grant_type", grantType);
            return this;
        }

        /**
         * Adds token scope
         *
         * @param scope Token scope
         * @return Header instance
         */
        public Header scope(String scope) {
            this.oauthParams.put("scope", scope);
            return this;
        }

        /**
         * Builds Authentication Header with given parameters
         *
         * @return Authentication.Header instance
         */
        public Header build() {
            return this;
        }

        /**
         * Returns OAuth access token used to authenticate REST API queries
         *
         * @return OAuth access token
         */
        public String getToken() {
            return BEARER + " " + getOAuthTokenHeader(tokenAccessURL, oauthParams);
        }
    }
}
