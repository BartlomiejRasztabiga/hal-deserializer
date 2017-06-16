import java.net.URL;

public class Link {
    private String name;
    private URL href;

    public Link(String name, URL href) {
        this.name = name;
        this.href = href;
    }

    public Link(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Link{" +
                "name='" + name + '\'' +
                ", href=" + href +
                '}';
    }
}
