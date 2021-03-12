package ai.aimachineserver.domain

import org.json.JSONObject

class Game(private val gameId: String) {
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
    private val playerIds = mutableListOf<String>()

    fun onPlayerJoinedGame(sid: String) {
        // flask_socketio.emit("client_id", sid)
        playerIds.add(sid)
        if (player1 == playerStub) {
            player1 = PlayerHuman(sid, Symbol.SYMBOL_O)
            currentPlayer = player1
        } else {
            player2 = PlayerHuman(sid, Symbol.SYMBOL_X)
            // flask_socketio.emit("movement_allowed", self._current_player.name, room=self._game_id)
        }
        println("player: $sid joined the game: $gameId")
        // flask_socketio.emit("server_message", "players in game: {}".format(self._player_ids), room=self._game_id)
    }

    fun onFieldClicked(rowIndex: Int, colIndex: Int) {
        if (board.isFieldAvailable(rowIndex, colIndex)) {
            println("clicked [row, col]: [$rowIndex, $colIndex]")
            val dataToSend = JSONObject()
                .put("rowIndex", rowIndex)
                .put("colIndex", colIndex)
                .put("fieldToken", currentPlayer.symbol.token)
            // flask_socketio.emit("field_to_be_marked", data_to_send, room=self._game_id)
            if (turnResult == TurnResult.GAME_ONGOING) {
                turnNumber++
                board = currentPlayer.makeMove(board, rowIndex, colIndex)
                turnResult = judge.announceTurnResult(board, turnNumber)
                if (turnResult == TurnResult.GAME_ONGOING) {
                    changePlayer()
                } else {
                    val resultMessage = getResultMessage()
                    // flask_socketio.emit("server_message", "game ended: {} ".format(result_message), room=self._game_id)
                    // flask_socketio.emit("movement_allowed", "none", room=self._game_id)
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
        // flask_socketio.emit("movement_allowed", self._current_player.name, room=self._game_id)
    }

    private fun getResultMessage() = if (turnResult == TurnResult.TIE) {
        "Tie"
    } else {
        "${currentPlayer.symbol.identifier} won"
    }

    fun onDisconnect(sid: String) {
        playerIds.remove(sid)
        println("player $sid disconnected from game: $gameId")
        // flask_socketio.emit("server_message", "player: {} disconnected".format(sid), room=self._game_id)
        // flask_socketio.emit("server_message", "players in game: {}".format(self._player_ids), room=self._game_id)
    }
}
