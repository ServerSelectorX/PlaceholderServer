package nl.rslot.ssx.placeholderserver;

import com.google.gson.JsonObject;

public class ConnectorServer implements ILastUpdate {

    private final long lastUpdate;
    private final String name;
    private final JsonObject data;

    ConnectorServer(final String name, final JsonObject data) {
        this.lastUpdate = System.currentTimeMillis();
        this.name = name;
        this.data = data;
    }

    @Override
    public long getLastUpdate() {
        return this.lastUpdate;
    }

    public String getName() {
        return this.name;
    }

    public JsonObject getData() {
        return this.data;
    }

}
