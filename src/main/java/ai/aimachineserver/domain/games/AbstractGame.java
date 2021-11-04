package ai.aimachineserver.domain.games;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractGame implements Game {
    protected final String gameName;
    protected final List<WebSocketSession> playerSessions = new ArrayList<>();

    public AbstractGame(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return this.gameName;
    }

    @Override
    public List<WebSocketSession> getAllPlayerSessions() {
        return playerSessions;
    }

    public void broadcastMessage(KeyValue... keyValues) {
        JSONObject json = new JSONObject();
        Arrays.stream(keyValues).forEach(pair -> json.put(pair.getKey(), pair.getValue()));
        playerSessions.forEach(session -> {
            ConcurrentWebSocketSessionDecorator sessionDecorator = new ConcurrentWebSocketSessionDecorator(session, 2000, 200);
            try {
                sessionDecorator.sendMessage(new TextMessage(json.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
