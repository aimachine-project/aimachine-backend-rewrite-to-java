package ai.aimachineserver.application

import ai.aimachineserver.domain.games.Game
import ai.aimachineserver.domain.games.soccer.BoardSoccer
import ai.aimachineserver.domain.games.soccer.GameSoccer
import ai.aimachineserver.domain.games.soccer.JudgeSoccer
import ai.aimachineserver.domain.games.tictactoe.Board
import ai.aimachineserver.domain.games.tictactoe.GameTicTacToe
import ai.aimachineserver.domain.games.tictactoe.Judge

interface GameFactory {
    fun createGame(): Game
}

class GameFactoryClassicTicTacToe : GameFactory {
    override fun createGame() = GameTicTacToe()
}

class GameFactoryTicTacToeNFields : GameFactory {
    override fun createGame(): Game {
        val board = Board(14)
        val judge = Judge(board, 5)
        return GameTicTacToe(board, judge)
    }
}

class GameFactorySoccer : GameFactory {
    override fun createGame(): Game {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        return GameSoccer(board, judge)
    }
}
