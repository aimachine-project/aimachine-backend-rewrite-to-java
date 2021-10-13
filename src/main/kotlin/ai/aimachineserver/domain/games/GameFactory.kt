package ai.aimachineserver.application

import ai.aimachineserver.domain.games.AbstractGame
import ai.aimachineserver.domain.games.soccer.BoardSoccer
import ai.aimachineserver.domain.games.soccer.GameSoccer
import ai.aimachineserver.domain.games.soccer.JudgeSoccer
import ai.aimachineserver.domain.games.tictactoe.Board
import ai.aimachineserver.domain.games.tictactoe.GameTicTacToe
import ai.aimachineserver.domain.games.tictactoe.Judge

interface GameFactory {
    fun createGame(): AbstractGame
}

class GameFactoryClassicTicTacToe : GameFactory {
    override fun createGame() = GameTicTacToe(gameName = "tictactoe")
}

class GameFactoryTicTacToeNFields : GameFactory {
    override fun createGame(): AbstractGame {
        val board = Board(14)
        val judge = Judge(board, 5)
        return GameTicTacToe(board, judge, "tictactoeextended")
    }
}

class GameFactorySoccer : GameFactory {
    override fun createGame(): AbstractGame {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        return GameSoccer(board, judge, "soccer")
    }
}
