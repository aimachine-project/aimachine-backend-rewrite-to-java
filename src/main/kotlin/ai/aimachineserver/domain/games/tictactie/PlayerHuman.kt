package ai.aimachineserver.domain.games.tictactie

class PlayerHuman(
    name: String,
    symbol: Symbol
) : Player(name, symbol) {
    override fun makeMove(board: Board, rowIndex: Int, colIndex: Int) {
        board.setFieldValue(rowIndex, colIndex, symbol.token)
    }
}
