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
                    .put("eventType", "movement_allowed")
                    .put("eventMessage", currentPlayer.name)
            )
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
            println("Clicked [row, col]: [$rowIndex, $colIndex]")
            val data = JSONObject()
                .put("rowIndex", rowIndex)
                .put("colIndex", colIndex)
                .toString()
            broadcastMessage(
                JSONObject()
                    .put("eventType", "field_to_be_marked")
                    .put("eventMessage", data)
            )
            if (turnResult == TurnResultSoccer.TURN_ONGOING || turnResult == TurnResultSoccer.TURN_OVER) {
                currentPlayer.makeMove(board, rowIndex, colIndex)
                turnResult = judge.announceTurnResult()
                if (turnResult == TurnResultSoccer.TURN_OVER) {
                    currentPlayer = assignPlayer()
                } else {
                    val resultMessage = getEndgameMessage()
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "server_message")
                            .put("eventMessage", "Game has ended: $resultMessage")
                    )
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "movement_allowed")
                            .put("eventMessage", "none")
                    )
                    return
                }
                broadcastMessage(
                    JSONObject()
                        .put("eventType", "movement_allowed")
                        .put("eventMessage", currentPlayer.name)
                )
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

    private fun formatMessage(condition: Boolean) = "${if (condition) player1.name else player2.name} won"

    override fun onDisconnect(session: WebSocketSession) {
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
    }
}
