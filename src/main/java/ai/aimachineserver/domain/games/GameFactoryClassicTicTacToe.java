package ai.aimachineserver.domain.games;

import ai.aimachineserver.domain.games.tictactoe.Board;
import ai.aimachineserver.domain.games.tictactoe.GameTicTacToe;
import ai.aimachineserver.domain.games.tictactoe.Judge;

public class GameFactoryClassicTicTacToe implements GameFactory {
    public AbstractGame createGame() {
        Board board = new Board();
        Judge judge = new Judge(board, 3);
        return new GameTicTacToe(board, judge, "tictactoe");
    }
}
