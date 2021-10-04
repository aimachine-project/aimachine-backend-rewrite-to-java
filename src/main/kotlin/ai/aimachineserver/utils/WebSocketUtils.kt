package ai.aimachineserver.utils

import org.json.JSONObject
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

object WebSocketUtils {
    fun WebSocketSession.sendMessage(vararg keyValues: Pair<String, String>) {
        val json = JSONObject().apply {
            keyValues.forEach { pair ->
                this.put(pair.first, pair.second)
            }
        }
        synchronized(this) {
            this.sendMessage(TextMessage(json.toString()))
        }
    }
}
