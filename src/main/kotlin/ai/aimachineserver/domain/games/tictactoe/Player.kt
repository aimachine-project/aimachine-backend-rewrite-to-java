package ai.aimachineserver.domain.games.tictactoe

abstract class Player(
    val name: String,
    val symbol: Symbol
) {
    abstract fun makeMove(board: Board, rowIndex: Int, colIndex: Int)
}
