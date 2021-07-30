package ai.aimachineserver.domain.gamelogic

class PlayerHuman(
    name: String,
    symbol: Symbol
) : Player(name, symbol) {
    override fun makeMove(board: Board, rowIndex: Int, colIndex: Int): Board {
        board.setFieldValue(rowIndex, colIndex, symbol.token)
        return board
    }
}
