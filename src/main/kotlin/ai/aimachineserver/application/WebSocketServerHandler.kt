package ai.aimachineserver.application

import ai.aimachineserver.application.commands.CallAiCommand
import ai.aimachineserver.domain.games.Game
import ai.aimachineserver.utils.GeneralUtils.parseQueryParams
import ai.aimachineserver.utils.GeneralUtils.sendMessage
import org.json.JSONObject
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class WebSocketServerHandler(
    private val gameFactory: GameFactory,
    private val aiCallService: AiCallService
) : TextWebSocketHandler() {

    private val games = mutableMapOf<String, Game>()
    private val aiGames = mutableMapOf<String, Game>()
    private var gamesCounter = 1
    private var gamesVsAiCounter = 1

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("Client ${session.id} connected")
        session.sendMessage("eventType" to "client_id", "eventMessage" to session.id)
        val uri = session.uri
        if (uri != null) {
            val uriParams = parseQueryParams(uri.query)
            when (uriParams["gameType"]) {
                "HumanVsHuman" -> handleHumanVsHumanGameRequest(session)
                "HumanVsAi" -> handleHumanVsAiGameRequest(session)
                "AiVsHuman" -> {
                    val gameId = uriParams["gameId"]
                    if (gameId != null) {
                        handleAiVsHumanGameRequest(session, gameId)
                    } else {
                        println("No gameId found in URI")
                    }
                }
                else -> println("No gameType found in URI")
            }
        }
    }

    private fun handleHumanVsHumanGameRequest(session: WebSocketSession) {
        val gameId = "game$gamesCounter"
        session.sendMessage("eventType" to "game_id", "eventMessage" to gameId)
        if (games.containsKey(gameId)) {
            games.getValue(gameId).onPlayerJoinedGame(session)
            println("player ${session.id} joined game $gameId")
            gamesCounter++
        } else {
            val game = gameFactory.createGame()
            game.onPlayerJoinedGame(session)
            games[gameId] = game
            println("games: ${games.keys}")
        }
    }

    private fun handleHumanVsAiGameRequest(session: WebSocketSession) {
        val gameId = "game${gamesVsAiCounter++}"
        session.sendMessage("eventType" to "game_id", "eventMessage" to gameId)
        val game = gameFactory.createGame()
        game.onPlayerJoinedGame(session)
        aiGames[gameId] = game
        println("games: ${aiGames.keys}")
        println("calling AI to join the game...")
        aiCallService.callAi(CallAiCommand(game.gameName, "AiVsHuman", gameId))
    }

    private fun handleAiVsHumanGameRequest(session: WebSocketSession, gameId: String) {
        session.sendMessage("eventType" to "game_id", "eventMessage" to gameId)
        if (aiGames.containsKey(gameId)) {
            aiGames.getValue(gameId).onPlayerJoinedGame(session)
            println("player ${session.id} joined game $gameId")
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

                val uri = session.uri
                if (uri != null) {
                    val uriParams = parseQueryParams(uri.query)
                    when (uriParams["gameType"]) {
                        "HumanVsHuman" -> games.getValue(gameId).onFieldClicked(rowIndex, colIndex)
                        "HumanVsAi" -> aiGames.getValue(gameId).onFieldClicked(rowIndex, colIndex)
                        "AiVsHuman" -> aiGames.getValue(gameId).onFieldClicked(rowIndex, colIndex)
                        else -> println("No gameType found in URI")
                    }
                }
            }
            else -> println("client event type not handled")
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("Client ${session.id} disconnected")
        val uri = session.uri
        if (uri != null) {
            val uriParams = parseQueryParams(uri.query)
            when (uriParams["gameType"]) {
                "HumanVsHuman" -> disbandGame(session, games)
                "HumanVsAi" -> disbandGame(session, aiGames)
                "AiVsHuman" -> disbandGame(session, aiGames)
                else -> println("No gameType found in URI")
            }
        }
    }

    private fun disbandGame(session: WebSocketSession, games: MutableMap<String, Game>) {
        val gameId = games.entries.find { it.value.getAllPlayerSessions().contains(session) }?.key
        if (gameId != null) {
            val game = games.getValue(gameId)
            game.onDisconnect(session)
            games.remove(gameId)
            val serverMessage = "Game has been disbanded. Restart client to play a new game"
            game.broadcastMessage("eventType" to "server_message", "eventMessage" to serverMessage)
            println("Ongoing games: ${games.keys}")
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("An error has occurred at websocket session: ${session.id}")
        println("Exception message: ${exception.message}")
    }
}
