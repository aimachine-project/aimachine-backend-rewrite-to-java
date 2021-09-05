package ai.aimachineserver.application

import ai.aimachineserver.domain.games.tictactie.Board
import ai.aimachineserver.domain.games.tictactie.GameTicTacToe
import ai.aimachineserver.domain.games.tictactie.Judge

interface GameFactory {
    fun createGame(): GameTicTacToe
}

class GameFactoryClassicTicTacToe : GameFactory {
    override fun createGame() = GameTicTacToe()
}

class GameFactoryTicTacToeNFields : GameFactory {
    override fun createGame(): GameTicTacToe {
        val board = Board(14)
        val judge = Judge(board, 5)
        return GameTicTacToe(board, judge)
    }
}

class GameFactorySoccer : GameFactory {
    override fun createGame(): GameTicTacToe {
        val board = Board(14)
        val judge = Judge(board, 5)
        return GameTicTacToe(board, judge)
    }
}
