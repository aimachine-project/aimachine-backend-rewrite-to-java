package ai.aimachineserver.domain.games.tictactoe

import ai.aimachineserver.domain.games.AbstractGame
import org.json.JSONObject
import org.springframework.web.socket.WebSocketSession

class GameTicTacToe(
    private val board: Board = Board(),
    private val judge: Judge = Judge(board),
    override val gameName: String
) : AbstractGame() {
    private companion object {
        val playerStub = PlayerHuman("", Symbol.SYMBOL_X)
    }

    private var player1 = playerStub
    private var player2 = playerStub
    private var currentPlayer = player1
    private var turnResult = TurnResult.GAME_ONGOING
    private var turnNumber = 0
    override val playerSessions = mutableListOf<WebSocketSession>()

    override fun getAllPlayerSessions() = playerSessions

    override fun onPlayerJoinedGame(session: WebSocketSession) {
        playerSessions.add(session)
        if (player1 == playerStub) {
            player1 = PlayerHuman(session.id, Symbol.SYMBOL_O)
            currentPlayer = player1
        } else {
            player2 = PlayerHuman(session.id, Symbol.SYMBOL_X)
            broadcastMessage("eventType" to "movement_allowed", "eventMessage" to currentPlayer.name)
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
            println("Clicked [row, col]: [$rowIndex, $colIndex]")
            val data = JSONObject()
                .put("rowIndex", rowIndex)
                .put("colIndex", colIndex)
                .put("fieldToken", currentPlayer.symbol.token)
                .toString()
            broadcastMessage("eventType" to "field_to_be_marked", "eventMessage" to data)
            if (turnResult == TurnResult.GAME_ONGOING) {
                turnNumber++
                currentPlayer.makeMove(board, rowIndex, colIndex)
                turnResult = judge.announceTurnResult(turnNumber)
                if (turnResult == TurnResult.GAME_ONGOING) {
                    changePlayer()
                } else {
                    val resultMessage = getResultMessage()
                    broadcastMessage(
                        "eventType" to "server_message",
                        "eventMessage" to "Game has ended: $resultMessage"
                    )
                    broadcastMessage("eventType" to "movement_allowed", "eventMessage" to "none")
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
        broadcastMessage("eventType" to "movement_allowed", "eventMessage" to currentPlayer.name)
    }

    private fun getResultMessage() = if (turnResult == TurnResult.TIE) {
        "Tie"
    } else {
        "${currentPlayer.symbol.identifier} won"
    }

    override fun onDisconnect(session: WebSocketSession) {
        playerSessions.remove(session)
        broadcastMessage("eventType" to "server_message", "eventMessage" to "Player disconnected")
        broadcastMessage(
            "eventType" to "server_message",
            "eventMessage" to "${playerSessions.count()} players in game"
        )
    }
}
