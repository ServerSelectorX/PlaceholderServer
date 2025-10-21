package nl.rslot.ssx.placeholderserver;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

public class Network {

    private final long lastUpdate;
    private final Map<String, ConnectorServer> connectorServers = new HashMap<>();
    private final Map<String, LobbyServer> lobbyServers = new HashMap<>();

    Network() {
        this.lastUpdate = System.currentTimeMillis();
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void updateConnectorData(final String serverName, final JsonObject data) {
        final ConnectorServer server = new ConnectorServer(serverName, data);
        connectorServers.put(serverName, server);
    }

    public Collection<ConnectorServer> getServers() {
        return connectorServers.values();
    }

    public LobbyServer getLobbyServer(final String name) {
        return lobbyServers.computeIfAbsent(name, (name2) -> new LobbyServer());
    }

    public List<String> getLobbyPlayers() {
        return this.lobbyServers.values().stream().flatMap(server -> Arrays.stream(server.getOnlinePlayers())).toList();
    }

}
