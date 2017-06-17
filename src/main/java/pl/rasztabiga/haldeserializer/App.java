package pl.rasztabiga.haldeserializer;

import pl.rasztabiga.haldeserializer.entities.Account;

import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        Map<String, String> headers = new HashMap<>();

        Map<String, String> oauthParams = new HashMap<>();
        oauthParams.put("client_id", "5RF7Drx0eY8uh0uZ9e4C4a5L23Lbf83LL");
        oauthParams.put("client_secret", "PsA_YXFvCdmmjNIgKnH2fG0UmIXODmDtWSwQReoo3pFkFVW6FRReJ2vX92TMbwpk");
        oauthParams.put("audience", "https://api.klasa1a.pl");
        oauthParams.put("grant_type", "client_credentials");
        oauthParams.put("scope", "read:exams read:news read:lucky-numbers read:surveys read:survey-options read:exams-photos read:students read:classes read:schools");
        Map.Entry<String, String> authenticationHeader = Authentication.getOAuthTokenHeader("https://infinite-future.eu.auth0.com/oauth/token", oauthParams);

        if (authenticationHeader != null) {
            headers.put(authenticationHeader.getKey(), authenticationHeader.getValue());
        }

        HalDeserializer halDeserializer = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/accounts/1")
                .withHeaders(headers)
                .withParams(new HashMap<>())
                .build();

        Account account = halDeserializer.toObject(Account.class);
        System.out.println(account);

    }
}
