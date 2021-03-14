package ai.aimachineserver

import ai.aimachineserver.domain.Game
import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import com.google.gson.Gson
import org.json.JSONObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.io.Serializable

@SpringBootApplication
class AimachineServerApplication {

    @Bean
    fun socketIOServer(): SocketIOServer? {
        val config = Configuration()
        config.hostname = "0.0.0.0"
        config.port = 443
        val server = SocketIOServer(config)

        val games = mutableMapOf<String, Game>()
        var roomsCounter = 1

        server.addConnectListener { client ->
            println("Client ${client.sessionId} connected")
            val gameId = "game$roomsCounter"
            client.sendEvent("game_id", gameId)
            client.joinRoom(gameId)
            if (games.containsKey(gameId)) {
                games.getValue(gameId).onPlayerJoinedGame(client.sessionId.toString())
                roomsCounter++
            } else {
                val game = Game(gameId, server)
                game.onPlayerJoinedGame(client.sessionId.toString())
                games.putIfAbsent(gameId, game)
                println("games: ${games.keys}")
            }
        }

        server.addEventListener("field_clicked", Serializable::class.java) { _, data, _ ->
            val json = JSONObject(Gson().toJson(data))
            val gameId = json.getString("gameId")
            val rowIndex = json.getInt("rowIndex")
            val colIndex = json.getInt("colIndex")
            games.getValue(gameId).onFieldClicked(rowIndex, colIndex)
        }

        server.addDisconnectListener { client ->
            val gameId = games.entries.find { it.value.playerIds.contains(client.sessionId.toString()) }?.key
            if (gameId != null) {
                val serverMessage = "Game: $gameId has been disbanded. Restart client to play a new game"
                server.getRoomOperations(gameId).sendEvent("server_message", serverMessage)
                games.getValue(gameId).onDisconnect(client.sessionId.toString())
                server.getRoomOperations(gameId).disconnect()
                println("Games: ${games.keys}")
                games.remove(gameId)
                println("Games: ${games.keys}")
            }
        }

        return server
    }
}

fun main(args: Array<String>) {
    runApplication<AimachineServerApplication>(*args)
}
