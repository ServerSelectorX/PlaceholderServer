package nl.rslot.ssx.placeholderserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;

public class Util {

    public static String[] jsonToStringArray(final JsonArray json) {
        final String[] players = new String[json.size()];
        for (int i = 0; i < json.size(); i++) {
            players[i] = json.get(i).getAsString();
        }
        return players;
    }

    public static JsonElement readJsonRequest(HttpExchange http) throws IOException {
        try (InputStream is = http.getRequestBody()) {
            String requestBody = new String(is.readAllBytes());
            return JsonParser.parseString(requestBody);
        }
    }

    public static void sendJsonResponse(HttpExchange http, final JsonElement json) throws IOException {
        byte[] responseBytes = json.toString().getBytes();
        http.getResponseHeaders().set("Content-Type", "application/json");
        http.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = http.getResponseBody()) {
            os.write(responseBytes);
        }
        http.close();
    }

    public static void sendHtmlResponse(HttpExchange http, final String html) throws IOException {
        byte[] data = html.getBytes();
        http.getResponseHeaders().set("Content-Type", "text/html");
        http.sendResponseHeaders(200, data.length);
        try (OutputStream out = http.getResponseBody()) {
            out.write(data);
        }
        http.close();
    }

    public static <K, V extends ILastUpdate> void prune(Map<K, V> map, long timeoutMs) {
        Set<K> toRemove = new HashSet<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue().getLastUpdate() > timeoutMs) {
                toRemove.add(entry.getKey());
            }
        }

        for (K key : toRemove) {
            map.remove(key);
        }
    }

}
