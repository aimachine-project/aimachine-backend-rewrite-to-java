package ai.aimachineserver.domain.games.soccer;


abstract class PlayerSoccer {

    private final String name;

    PlayerSoccer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract void makeMove(BoardSoccer board, int otherNodeRowIndex, int otherNodeColIndex);
}
