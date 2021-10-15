package ai.aimachineserver.domain.games

import org.springframework.web.socket.WebSocketSession

interface Game {
    fun onPlayerJoinedGame(session: WebSocketSession)
    fun onDisconnect(session: WebSocketSession)
    fun onFieldClicked(rowIndex: Int, colIndex: Int)
    fun getAllPlayerSessions(): List<WebSocketSession>
    fun broadcastMessage(vararg keyValues: Pair<String, String>)
}
