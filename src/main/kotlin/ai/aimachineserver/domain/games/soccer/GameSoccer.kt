package ai.aimachineserver.domain.games.soccer

import ai.aimachineserver.domain.games.AbstractGame
import org.json.JSONObject
import org.springframework.web.socket.WebSocketSession

class GameSoccer(
    private val board: BoardSoccer = BoardSoccer(),
    private val judge: JudgeSoccer = JudgeSoccer(board)
) : AbstractGame() {
    private companion object {
        val playerStub = PlayerSoccerHuman("")
    }

    private var player1 = playerStub
    private var player2 = playerStub
    private var currentPlayer = player1
    private var turnResult = TurnResultSoccer.TURN_ONGOING
    override val playerSessions = mutableListOf<WebSocketSession>()

    override fun onPlayerJoinedGame(session: WebSocketSession) {
        playerSessions.add(session)
        if (player1 == playerStub) {
            player1 = PlayerSoccerHuman(session.id)
            currentPlayer = player1
        } else {
            player2 = PlayerSoccerHuman(session.id)
            broadcastMessage("eventType" to "movement_allowed", "eventMessage" to currentPlayer.name)
            val data = JSONObject()
                .put("player1", player1.name)
                .put("player2", player2.name)
                .toString()
            broadcastMessage("eventType" to "game_starting", "eventMessage" to data)
        }
        val playersCount = playerSessions.count()
        broadcastMessage(
            "eventType" to "server_message",
            "eventMessage" to "$playersCount players in game"
        )
        val message = if (playersCount == 1) "Waiting for opponent" else "Game has started"
        broadcastMessage("eventType" to "server_message", "eventMessage" to message)
    }

    override fun onFieldClicked(rowIndex: Int, colIndex: Int) {
        if (board.isFieldAvailable(rowIndex, colIndex)) {
            println("${if (currentPlayer == player1) "player1" else "player2"} clicked [row, col]: [$rowIndex, $colIndex]")
            val data = JSONObject()
                .put("rowIndex", rowIndex)
                .put("colIndex", colIndex)
                .toString()
            broadcastMessage("eventType" to "field_to_be_marked", "eventMessage" to data)
            currentPlayer.makeMove(board, rowIndex, colIndex)
            turnResult = judge.announceTurnResult()
            when (turnResult) {
                TurnResultSoccer.TURN_OVER -> {
                    currentPlayer = assignPlayer()
                    broadcastMessage(
                        "eventType" to "movement_allowed",
                        "eventMessage" to currentPlayer.name
                    )
                }
                TurnResultSoccer.TURN_ONGOING -> {
                    broadcastMessage(
                        "eventType" to "movement_allowed",
                        "eventMessage" to currentPlayer.name
                    )
                }
                else -> {
                    val resultMessage = getEndgameMessage()
                    broadcastMessage(
                        "eventType" to "server_message",
                        "eventMessage" to "Game has ended: $resultMessage"
                    )
                    println(resultMessage)
                    broadcastMessage(
                        "eventType" to "movement_allowed",
                        "eventMessage" to "none"
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
                "eventType" to "server_message",
                "eventMessage" to "Player disconnected"
            )
            broadcastMessage(
                "eventType" to "server_message",
                "eventMessage" to "${playerSessions.count()} players in game"
            )
        } catch (e: NullPointerException) {
            println("No client ref. Both clients already disconnected. Cannot broadcast message to missing socket")
        } catch (e: IllegalStateException) {
            println("Illegal state. Both clients already disconnected. Cannot broadcast message to missing socket")
        }
    }
}
