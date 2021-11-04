package ai.aimachineserver.domain.games;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface Game {
    void onPlayerJoinedGame(WebSocketSession session);

    void onDisconnect(WebSocketSession session);

    void onFieldClicked(int rowIndex, int colIndex);

    void broadcastMessage(KeyValue... keyValues);

    List<WebSocketSession> getAllPlayerSessions();
}
