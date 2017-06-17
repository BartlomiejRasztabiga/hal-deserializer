package entities;

public class Account {

    private Long id;

    private String email;

    private String apiKey;

    private Student student;

    public String getEmail() {
        return email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Student getStudent() {
        return student;
    }

}
