package pl.rasztabiga.haldeserializer.entities;

public class Account {

    private Long id;

    private String email;

    private String apiKey;

    public String getEmail() {
        return email;
    }

    public String getApiKey() {
        return apiKey;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
