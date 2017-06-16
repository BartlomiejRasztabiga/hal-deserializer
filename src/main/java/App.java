import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getAuthToken());

        HalDeserializer halDeserializer = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/students/")
                .withHeaders(headers)
                .withParams(new HashMap<>())
                .build();

        halDeserializer.toList(Student.class);

    }

    private static String getAuthToken() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, "{\"client_id\":\"5RF7Drx0eY8uh0uZ9e4C4a5L23Lbf83L\",\"client_secret\":\"PsA_YXFvCdmmjNIgKnH2fG0UmIXODmDtWSwQReoo3pFkFVW6FRReJ2vX92TMbwpk\",\"audience\":\"https://api.klasa1a.pl\",\"grant_type\":\"client_credentials\",\"scope\":\"read:exams read:news read:lucky-numbers read:surveys read:survey-options read:exams-photos read:students read:classes read:schools\"}");

        Request request = new Request.Builder()
                .url("https://infinite-future.eu.auth0.com/oauth/token")
                .addHeader("content-type", "application/json")
                .post(body)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        return new JSONObject(response.body().string()).getString("access_token");
    }
}
