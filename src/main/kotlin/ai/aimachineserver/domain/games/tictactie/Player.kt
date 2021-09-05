package ai.aimachineserver.domain.games.tictactie

abstract class Player(
    val name: String,
    val symbol: Symbol
) {
    abstract fun makeMove(board: Board, rowIndex: Int, colIndex: Int)
}
