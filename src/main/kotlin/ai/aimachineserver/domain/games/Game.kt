package ai.aimachineserver.domain.games

import org.json.JSONObject
import org.springframework.web.socket.WebSocketSession

interface Game {
    fun onPlayerJoinedGame(session: WebSocketSession)
    fun onDisconnect(session: WebSocketSession)
    fun broadcastMessage(eventObject: JSONObject)
    fun onFieldClicked(rowIndex: Int, colIndex: Int)

    fun getPlayerSessions(): List<WebSocketSession>
}
