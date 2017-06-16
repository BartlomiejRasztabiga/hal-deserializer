import java.util.HashMap;

public class App {

    public static void main(String[] args) {
        HalDeserializer halDeserializer = new HalDeserializer.Builder()
                .baseUrl("http://api-v2.eu-central-1.elasticbeanstalk.com/")
                .withHeaders(new HashMap<>())
                .withParams(new HashMap<>())
                .build();


    }
}
