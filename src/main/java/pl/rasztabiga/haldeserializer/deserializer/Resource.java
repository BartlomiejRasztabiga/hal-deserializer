package pl.rasztabiga.haldeserializer.deserializer;

import java.util.List;

public class Resource<T> {
    private T content;
    private List<HalLink> links;

    Resource(T content, List<HalLink> links) {
        this.content = content;
        this.links = links;
    }

    public T getContent() {
        return content;
    }

    public List<HalLink> getLinks() {
        return links;
    }
}
