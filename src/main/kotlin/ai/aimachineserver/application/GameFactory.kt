package ai.aimachineserver.application

import ai.aimachineserver.domain.gamelogic.Board
import ai.aimachineserver.domain.gamelogic.Game
import ai.aimachineserver.domain.gamelogic.Judge

interface GameFactory {
    fun createGame(): Game
}

class GameFactoryClassicTicTacToe : GameFactory {
    override fun createGame() = Game(Board(), Judge())
}

class GameFactoryTicTacToeNFields : GameFactory {
    override fun createGame() = Game(Board(14), Judge(5, 14))
}
