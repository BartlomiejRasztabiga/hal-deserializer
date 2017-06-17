package pl.rasztabiga.haldeserializer.deserializer;

import java.net.URL;

public class HalLink {
    private String name;
    private URL href;

    public HalLink(String name, URL href) {
        this.name = name;
        this.href = href;
    }

    public HalLink(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "pl.rasztabiga.haldeserializer.HalLink{" +
                "name='" + name + '\'' +
                ", href=" + href +
                '}';
    }
}
