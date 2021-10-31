package ai.aimachineserver.utils

import org.json.JSONObject
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

object GeneralUtils {
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

    fun parseQueryParams(queryString: String): Map<String, String> {
        val split = queryString.split("=", "&")
        val odds = split.withIndex().filter { it.index % 2 != 0 }.map { it.value }
        val evens = split.withIndex().filter { it.index % 2 == 0 }.map { it.value }
        return evens.zip(odds).toMap()
    }
}
