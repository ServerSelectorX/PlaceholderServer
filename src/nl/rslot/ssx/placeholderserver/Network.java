package nl.rslot.ssx.placeholderserver;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

public class Network implements ILastUpdate {

    private long lastUpdate;
    private final Map<String, ConnectorServer> connectorServers = new HashMap<>();
    private final Map<String, LobbyServer> lobbyServers = new HashMap<>();

    Network() {
        this.lastUpdate = System.currentTimeMillis();
    }

    @Override
    public long getLastUpdate() {
        return this.lastUpdate;
    }

    public void updateConnectorData(final String serverName, final JsonObject data) {
        final ConnectorServer server = new ConnectorServer(serverName, data);
        this.connectorServers.put(serverName, server);
        this.lastUpdate = System.currentTimeMillis();
    }

    public void updateLobbyData(final String serverName, final String[] onlinePlayers) {
        final LobbyServer server = new LobbyServer(onlinePlayers);
        this.lobbyServers.put(serverName, server);
        this.lastUpdate = System.currentTimeMillis();
    }

    public void pruneServers() {
        Util.prune(this.connectorServers, 10_000);
        Util.prune(this.lobbyServers, 10_000);
    }

    public Collection<ConnectorServer> getConnectorServers() {
        return this.connectorServers.values();
    }

    public List<String> getLobbyPlayers() {
        return this.lobbyServers.values().stream().flatMap(server -> Arrays.stream(server.getOnlinePlayers())).toList();
    }

}
