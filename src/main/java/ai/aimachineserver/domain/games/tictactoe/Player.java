package ai.aimachineserver.domain.games.tictactoe;

public abstract class Player {
    public final String name;
    public final Symbol symbol;

    protected Player(String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    abstract void makeMove(Board board, int rowIndex, int colIndex);
}
