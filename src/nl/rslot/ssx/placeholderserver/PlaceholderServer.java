package nl.rslot.ssx.placeholderserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class PlaceholderServer {

    private static final Logger LOG = Logger.getLogger("PlaceholderServer");
    private static final Map<String, Network> NETWORKS = new HashMap<>();

    public static void main(String[] args) throws IOException {
        LOG.info("starting web server");
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        server.createContext("/connector", PlaceholderServer::handleConnector);
        server.createContext("/lobby", PlaceholderServer::handleLobby);
        server.start();
        Logger.getGlobal().setLevel(Level.ALL);
    }

    private static Network getNetwork(String networkId) {
        return NETWORKS.computeIfAbsent(networkId, (_networkId) -> new Network());
    }

    /**
     * Connector sends a list of placeholders to us
     * We send back a list of players
     * @param ex
     * @throws IOException
     */
    private static void handleConnector(HttpExchange http) throws IOException {
        try {
            JsonObject json = Util.readJsonRequest(http).getAsJsonObject();

            String networkId = json.get("network").getAsString();
            Network network = getNetwork(networkId);

            String serverName = json.get("server").getAsString();
            JsonObject data = json.get("data").getAsJsonObject();

            LOG.info("network " + networkId + " connector " + serverName);

            network.updateConnectorData(serverName, data);

            JsonArray responsePlayers = new JsonArray();
            for (String player : network.getLobbyPlayers()) {
                responsePlayers.add(player);
            }

            JsonObject responseJson = new JsonObject();
            responseJson.add("players", responsePlayers);

            Util.sendJsonResponse(http, responseJson);
        } catch (Exception e) {
            e.printStackTrace();
            http.sendResponseHeaders(500, 0);
            http.close();
        }
    }

    /**
     * Lobby sends a list of players to us
     * We send back a list of servers with their placeholders
     * @param ex
     * @throws IOException
     */
    private static void handleLobby(HttpExchange http) throws IOException {
        try {
            JsonObject json = Util.readJsonRequest(http).getAsJsonObject();

            String networkId = json.get("network").getAsString();
            Network network = getNetwork(networkId);

            String serverName = json.get("server").getAsString();
            LobbyServer lobby = network.getLobbyServer(serverName);

            LOG.info("network " + networkId + " lobby " + serverName);

            String[] players = Util.jsonToStringArray(json.get("players").getAsJsonArray());
            lobby.setOnlinePlayers(players);

            JsonObject responseJsonServers = new JsonObject();
            for (ConnectorServer server : network.getServers()) {
                responseJsonServers.add(server.getName(), server.getData());
            }

            JsonObject responseJson = new JsonObject();
            responseJson.add("servers", responseJsonServers);

            Util.sendJsonResponse(http, responseJson);
        } catch (Exception e) {
            e.printStackTrace();
            http.sendResponseHeaders(500, 0);
            http.close();
        }
    }

}
