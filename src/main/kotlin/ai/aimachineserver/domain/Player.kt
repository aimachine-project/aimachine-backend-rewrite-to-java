package ai.aimachineserver.domain

abstract class Player(
    val name: String,
    val symbol: Symbol
) {
    abstract fun makeMove(board: Board, rowIndex: Int, colIndex: Int): Board
}
