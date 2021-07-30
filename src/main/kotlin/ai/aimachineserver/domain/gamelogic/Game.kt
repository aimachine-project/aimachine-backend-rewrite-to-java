package ai.aimachineserver.domain.gamelogic

import org.json.JSONObject
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class Game {
    private companion object {
        val playerStub = PlayerHuman("", Symbol.SYMBOL_X)
    }

    private var board = Board()
    private val judge = Judge()
    private var player1 = playerStub
    private var player2 = playerStub
    private var currentPlayer = player1
    private var turnResult = TurnResult.GAME_ONGOING
    private var turnNumber = 0
    val playerSessions = mutableListOf<WebSocketSession>()

    fun onPlayerJoinedGame(session: WebSocketSession) {
        playerSessions.add(session)
        if (player1 == playerStub) {
            player1 = PlayerHuman(session.id, Symbol.SYMBOL_O)
            currentPlayer = player1
        } else {
            player2 = PlayerHuman(session.id, Symbol.SYMBOL_X)
            broadcastMessage(
                JSONObject()
                    .put("eventType", "movement_allowed")
                    .put("eventMessage", currentPlayer.name)
            )
        }
        broadcastMessage(
            JSONObject()
                .put("eventType", "server_message")
                .put("eventMessage", "players in game: ${playerSessions.map { it.id }}")
        )
    }

    fun broadcastMessage(eventObject: JSONObject) {
        playerSessions.forEach {
            it.sendMessage(TextMessage(eventObject.toString()))
        }
    }

    fun onFieldClicked(rowIndex: Int, colIndex: Int) {
        if (board.isFieldAvailable(rowIndex, colIndex)) {
            println("clicked [row, col]: [$rowIndex, $colIndex]")
            val data = JSONObject()
                .put("rowIndex", rowIndex)
                .put("colIndex", colIndex)
                .put("fieldToken", currentPlayer.symbol.token)
                .toString()
            broadcastMessage(
                JSONObject()
                    .put("eventType", "field_to_be_marked")
                    .put("eventMessage", data)
            )
            if (turnResult == TurnResult.GAME_ONGOING) {
                turnNumber++
                board = currentPlayer.makeMove(board, rowIndex, colIndex)
                turnResult = judge.announceTurnResult(board, turnNumber)
                if (turnResult == TurnResult.GAME_ONGOING) {
                    changePlayer()
                } else {
                    val resultMessage = getResultMessage()
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "server_message")
                            .put("eventMessage", "game ended: $resultMessage")
                    )
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "movement_allowed")
                            .put("eventMessage", "none")
                    )
                }
            }
        }
    }

    private fun changePlayer() {
        currentPlayer = if (currentPlayer == player1) {
            player2
        } else {
            player1
        }
        broadcastMessage(
            JSONObject()
                .put("eventType", "server_message")
                .put("eventMessage", "movement_allowed: " + currentPlayer.name)
        )
        broadcastMessage(
            JSONObject()
                .put("eventType", "movement_allowed")
                .put("eventMessage", currentPlayer.name)
        )
    }

    private fun getResultMessage() = if (turnResult == TurnResult.TIE) {
        "Tie"
    } else {
        "${currentPlayer.symbol.identifier} won"
    }

    fun onDisconnect(session: WebSocketSession) {
        playerSessions.remove(session)
        broadcastMessage(
            JSONObject()
                .put("eventType", "server_message")
                .put("eventMessage", "player: ${session.id} disconnected")
        )
        broadcastMessage(
            JSONObject()
                .put("eventType", "server_message")
                .put("eventMessage", "players in game: ${playerSessions.map { it.id }}")
        )
    }
}
