import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Resource {

    private List<Link> links;
    private JSONObject _embedded;

    public Resource(JSONObject root) {
        this._embedded = root.getJSONObject("_embedded");
        this.links = retrieveLinks(root.getJSONObject("_links"));
    }

    private List<Link> retrieveLinks(JSONObject _links) {
        links = new ArrayList<>();

        links.add(addLink("search", _links));
        links.add(addLink("profile", _links));
        links.add(addLink("self", _links));

        return links;
    }

    public <T> T parseList() {
        System.out.println(_embedded);

        return null;
    }

    private Link addLink(String name, JSONObject _links) {
        try {
            return new Link(name, new URL(_links.getJSONObject(name).getString("href")));
        } catch (JSONException | MalformedURLException e) {
            return new Link(name);
        }
    }

}
