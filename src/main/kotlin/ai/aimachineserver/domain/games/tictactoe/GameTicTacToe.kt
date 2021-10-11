package ai.aimachineserver.domain.games.tictactoe

import ai.aimachineserver.domain.games.Game
import org.json.JSONObject
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class GameTicTacToe(
    private val board: Board = Board(),
    private val judge: Judge = Judge(board)
) : Game {
    private companion object {
        val playerStub = PlayerHuman("", Symbol.SYMBOL_X)
    }

    private var player1 = playerStub
    private var player2 = playerStub
    private var currentPlayer = player1
    private var turnResult = TurnResult.GAME_ONGOING
    private var turnNumber = 0
    private val playerSessions = mutableListOf<WebSocketSession>()

    override fun getPlayerSessions() = playerSessions

    override fun onPlayerJoinedGame(session: WebSocketSession) {
        playerSessions.add(session)
        if (player1 == playerStub) {
            player1 = PlayerHuman(session.id, Symbol.SYMBOL_O)
            currentPlayer = player1
        } else {
            player2 = PlayerHuman(session.id, Symbol.SYMBOL_X)
            broadcastMessage(
                JSONObject()
                    .put("eventType", "current_player")
                    .put("eventMessage", currentPlayer.name)
            )
            broadcastMessage(
                JSONObject()
                    .put("eventType", "game_started")
                    .put("eventMessage", "game starting")
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
                .put("fieldToken", currentPlayer.symbol.token)
                .toString()
            broadcastMessage(
                JSONObject()
                    .put("eventType", "new_move_to_mark")
                    .put("eventMessage", data)
            )
            if (turnResult == TurnResult.GAME_ONGOING) {
                turnNumber++
                currentPlayer.makeMove(board, rowIndex, colIndex)
                turnResult = judge.announceTurnResult(turnNumber)
                if (turnResult == TurnResult.GAME_ONGOING) {
                    changePlayer()
                } else {
                    val resultMessage = getResultMessage()
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "game_ended")
                            .put("eventMessage", resultMessage)
                    )
                    broadcastMessage(
                        JSONObject()
                            .put("eventType", "current_player")
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
                .put("eventType", "current_player")
                .put("eventMessage", currentPlayer.name)
        )
    }

    private fun getResultMessage() = if (turnResult == TurnResult.TIE) {
        "Tie"
    } else {
        "${currentPlayer.symbol.identifier} won"
    }

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
