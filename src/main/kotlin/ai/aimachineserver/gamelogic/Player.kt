package ai.aimachineserver.gamelogic

abstract class Player(
    val name: String,
    val symbol: Symbol
) {
    abstract fun makeMove(board: Board, rowIndex: Int, colIndex: Int): Board
}
