package pl.rasztabiga.haldeserializer.deserializer;

import java.net.URL;

/**
 * HalLink class representing HAL hyperlinks included in JSON output
 *
 * @author Bartłomiej Rasztabiga
 * @version 1.0.0
 * @since 1.0
 */
public class HalLink {
    private String name;
    private URL href;

    HalLink(String name, URL href) {
        this.name = name;
        this.href = href;
    }

    HalLink(String name) {
        this.name = name;
    }

}
