package nl.rslot.ssx.placeholderserver;

public class LobbyServer {

    private String[] onlinePlayers = new String[0];

    LobbyServer() {

    }

    public String[] getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(final String[] newPlayers) {
        this.onlinePlayers = newPlayers;
    }


}
