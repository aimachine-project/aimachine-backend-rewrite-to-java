package ai.aimachineserver.domain.games

import org.json.JSONObject
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

abstract class AbstractGame : Game {
    protected abstract val playerSessions: List<WebSocketSession>

    abstract val gameName: String

    override fun getAllPlayerSessions() = playerSessions

    override fun broadcastMessage(vararg keyValues: Pair<String, String>) {
        val json = JSONObject().apply {
            keyValues.forEach { pair ->
                this.put(pair.first, pair.second)
            }
        }
        playerSessions.forEach { session ->
            synchronized(session) {
                session.sendMessage(TextMessage(json.toString()))
            }
        }
    }
}
