package ai.aimachineserver.application

import ai.aimachineserver.domain.gamelogic.Game
import org.json.JSONObject
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class WebSocketServerHandler(private val gameFactory: GameFactory) : TextWebSocketHandler() {

    private val games = mutableMapOf<String, Game>()
    private var roomsCounter = 1

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("Client ${session.id} connected")
        session.sendMessage(
            TextMessage(
                JSONObject()
                    .put("eventType", "client_id")
                    .put("eventMessage", session.id).toString()
            )
        )
        val gameId = "game$roomsCounter"
        session.sendMessage(
            TextMessage(
                JSONObject()
                    .put("eventType", "game_id")
                    .put("eventMessage", gameId).toString()
            )
        )
        if (games.containsKey(gameId)) {
            games.getValue(gameId).onPlayerJoinedGame(session)
            println("player ${session.id} joined game $gameId")
            roomsCounter++
        } else {
            val game = gameFactory.createGame()
            game.onPlayerJoinedGame(session)
            games.putIfAbsent(gameId, game)
            println("games: ${games.keys}")
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = JSONObject(message.payload)
        when (json.getString("eventType")) {
            "field_clicked" -> {
                val data = json.getJSONObject("eventMessage")
                val gameId = data.getString("gameId")
                val rowIndex = data.getInt("rowIndex")
                val colIndex = data.getInt("colIndex")
                games.getValue(gameId).onFieldClicked(rowIndex, colIndex)
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("Client ${session.id} disconnected")
        val gameId = games.entries.find { it.value.playerSessions.contains(session) }?.key
        if (gameId != null) {
            val game = games.getValue(gameId)
            game.onDisconnect(session)
            games.remove(gameId)
            val serverMessage = "Game has been disbanded. Restart client to play a new game"
            game.broadcastMessage(
                JSONObject()
                    .put("eventType", "server_message")
                    .put("eventMessage", serverMessage)
            )
            println("Ongoing games: ${games.keys}")
        }
    }
}
