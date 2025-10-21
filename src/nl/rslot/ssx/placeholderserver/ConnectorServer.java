package nl.rslot.ssx.placeholderserver;

import com.google.gson.JsonObject;

public class ConnectorServer {

    private final String name;
    private final JsonObject data;

    ConnectorServer(final String name, final JsonObject data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public JsonObject getData() {
        return data;
    }

}
