package ai.aimachineserver.domain.games.soccer

import ai.aimachineserver.domain.games.Game
import org.json.JSONObject
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class GameSoccer(
    private val board: BoardSoccer = BoardSoccer(),
    private val judge: JudgeSoccer = JudgeSoccer(board)
) : Game {
    private companion object {
        val playerStub = PlayerSoccerHuman("")
    }

    private var player1 = playerStub
    private var player2 = playerStub
    private var currentPlayer = player1
    private var turnResult = TurnResultSoccer.TURN_ONGOING
    private val playerSessions = mutableListOf<WebSocketSession>()

    override fun getPlayerSessions() = playerSessions

    override fun onPlayerJoinedGame(session: WebSocketSession) {
        playerSessions.add(session)
        if (player1 == playerStub) {
            player1 = PlayerSoccerHuman(session.id)
            currentPlayer = player1
        } else {
            player2 = PlayerSoccerHuman(session.id)
            broadcastMessage(
                JSONObject()
                    .put("eventType", "current_player")
                    .put("eventMessage", currentPlayer.name)
            )
            val data = JSONObject()
                .put("player1", player1.name)
                .put("player2", player2.name)
                .toString()
            broadcastMessage(
                JSONObject()
                    .put("eventType", "players_in_game")
                    .put("eventMessage", data)
            )
            broadcastMessage(
                JSONObject()
                    .put("eventType", "game_started")
                    .put("eventMessage", "game starting")
        }
        val playersCount = playerSessions.count()
        broadcastMessage(
            JSONObject()
                .put("eventType", "server_message")
                .put("eventMessage", "$playersCount players in game")
        )
        val message = if (playersCount == 1) "Waiting for opponent" else "Game has started"
        broadcastMessage(
            JSONObject()
                .put("eventType", "server_message")
                .put("eventMessage", message)
        )
    }

    override fun broadcastMessage(eventObject: JSONObject) {
        playerSessions.forEach {
            it.sendMessage(TextMessage(eventObject.toString()))
        }
    }

    override fun onFieldClicked(rowIndex: Int, colIndex: Int) {
        if (board.isFieldAvailable(rowIndex, colIndex)) {
            println("${if (currentPlayer == player1) "player1" else "player2"} clicked [row, col]: [$rowIndex, $colIndex]")
            val data = JSONObject()
                .put("rowIndex", rowIndex)
                .put("colIndex", colIndex)
                .toString()
            broadcastMessage(
                JSONObject()
                    .put("eventType", "new_move_to_mark")
                    .put("eventMessage", data)
            )
            currentPlayer.makeMove(board, rowIndex, colIndex)
            turnResult = judge.announceTurnResult()
            when (turnResult) {
                TurnResultSoccer.TURN_OVER -> {
                    currentPlayer = assignPlayer()
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "current_player")
                            .put("eventMessage", currentPlayer.name)
                    )
                }
                TurnResultSoccer.TURN_ONGOING -> {
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "current_player")
                            .put("eventMessage", currentPlayer.name)
                    )
                }
                else -> {
                    val resultMessage = getEndgameMessage()
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "game_ended")
                            .put("eventMessage", resultMessage)
                    )
                    println(resultMessage)
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "current_player")
                            .put("eventMessage", "none")
                    )
                }
            }
        }
    }

    private fun assignPlayer() = if (currentPlayer == player1) {
        player2
    } else {
        player1
    }

    private fun getEndgameMessage() = when (turnResult) {
        TurnResultSoccer.WIN -> formatMessage(currentPlayer == player1)
        TurnResultSoccer.LOSE -> formatMessage(currentPlayer != player1)
        else -> "Unexpected state, everyone is a winner"
    }

    private fun formatMessage(condition: Boolean) =
        "${(if (condition) "player 1 (${player1.name})" else "player 2 (${player2.name})")} won"

    override fun onDisconnect(session: WebSocketSession) {
        try {
            playerSessions.remove(session)
            broadcastMessage(
                JSONObject()
                    .put("eventType", "server_message")
                    .put("eventMessage", "Player disconnected")
            )
            broadcastMessage(
                JSONObject()
                    .put("eventType", "server_message")
                    .put("eventMessage", "${playerSessions.count()} players in game")
            )
        } catch (e: NullPointerException) {
            println("No client ref. Both clients already disconnected. Cannot broadcast message to missing socket")
        } catch (e: IllegalStateException) {
            println("Illegal state. Both clients already disconnected. Cannot broadcast message to missing socket")
        }
    }
}
