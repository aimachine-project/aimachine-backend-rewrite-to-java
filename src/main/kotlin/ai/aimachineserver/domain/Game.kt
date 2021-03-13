package ai.aimachineserver.domain

import com.corundumstudio.socketio.SocketIOServer
import org.json.JSONObject
import java.util.*

class Game(private val gameId: String, private val server: SocketIOServer) {
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
    val playerIds = mutableListOf<String>()

    fun onPlayerJoinedGame(sid: String) {
        server.getClient(UUID.fromString(sid)).sendEvent("client_id", sid)
        playerIds.add(sid)
        if (player1 == playerStub) {
            player1 = PlayerHuman(sid, Symbol.SYMBOL_O)
            currentPlayer = player1
        } else {
            player2 = PlayerHuman(sid, Symbol.SYMBOL_X)
            server.getRoomOperations(gameId).sendEvent("movement_allowed", currentPlayer.name)
        }
        println("player: $sid joined the game: $gameId")
        server.getRoomOperations(gameId).sendEvent("server_message", "players in game: $playerIds")
    }

    fun onFieldClicked(rowIndex: Int, colIndex: Int) {
        if (board.isFieldAvailable(rowIndex, colIndex)) {
            println("clicked [row, col]: [$rowIndex, $colIndex]")
            val data = JSONObject()
                .put("rowIndex", rowIndex)
                .put("colIndex", colIndex)
                .put("fieldToken", currentPlayer.symbol.token)
                .toString()
            server.getRoomOperations(gameId).sendEvent("field_to_be_marked", data)
            if (turnResult == TurnResult.GAME_ONGOING) {
                turnNumber++
                board = currentPlayer.makeMove(board, rowIndex, colIndex)
                turnResult = judge.announceTurnResult(board, turnNumber)
                if (turnResult == TurnResult.GAME_ONGOING) {
                    changePlayer()
                } else {
                    val resultMessage = getResultMessage()
                    server.getRoomOperations(gameId).sendEvent("server_message", "game ended: $resultMessage")
                    server.getRoomOperations(gameId).sendEvent("movement_allowed", "none")
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
        server.getRoomOperations(gameId).sendEvent("server_message", "movement_allowed: " + currentPlayer.name)
        server.getRoomOperations(gameId).sendEvent("movement_allowed", currentPlayer.name)
    }

    private fun getResultMessage() = if (turnResult == TurnResult.TIE) {
        "Tie"
    } else {
        "${currentPlayer.symbol.identifier} won"
    }

    fun onDisconnect(sid: String) {
        playerIds.remove(sid)
        println("player $sid disconnected from game: $gameId")
        server.getRoomOperations(gameId).sendEvent("server_message", "player: $sid disconnected")
        server.getRoomOperations(gameId).sendEvent("server_message", "players in game: $playerIds")
    }
}
