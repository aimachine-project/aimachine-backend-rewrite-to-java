package ai.aimachineserver.domain.games.soccer

abstract class PlayerSoccer(val name: String) {
    abstract fun makeMove(board: BoardSoccer, otherNodeRowIndex: Int, otherNodeColIndex: Int)
}
