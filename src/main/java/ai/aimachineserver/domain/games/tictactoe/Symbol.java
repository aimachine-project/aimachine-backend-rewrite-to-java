package ai.aimachineserver.domain.games.tictactoe;

public enum Symbol {
    SYMBOL_X("X", 1),
    SYMBOL_O("O", -1);

    public String identifier;
    public int token;

    Symbol(String identifier, int token) {
        this.identifier = identifier;
        this.token = token;
    }
}
