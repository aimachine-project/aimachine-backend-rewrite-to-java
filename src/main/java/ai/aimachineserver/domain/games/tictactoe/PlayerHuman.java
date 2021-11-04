package ai.aimachineserver.domain.games.tictactoe;

public class PlayerHuman extends Player {
    PlayerHuman(String name, Symbol symbol) {
        super(name, symbol);
    }

    @Override
    public void makeMove(Board board, int rowIndex, int colIndex) {
        board.setFieldValue(rowIndex, colIndex, symbol.token);
    }
}
