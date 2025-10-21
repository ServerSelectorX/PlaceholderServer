package nl.rslot.ssx.placeholderserver;

public class LobbyServer implements ILastUpdate {

    private final long lastUpdate;
    private final String[] onlinePlayers;

    LobbyServer(final String[] onlinePlayers) {
        this.lastUpdate = System.currentTimeMillis();
        this.onlinePlayers = onlinePlayers;
    }

    @Override
    public long getLastUpdate() {
        return this.lastUpdate;
    }

    public String[] getOnlinePlayers() {
        return this.onlinePlayers;
    }

}
