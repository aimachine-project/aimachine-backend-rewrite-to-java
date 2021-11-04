package ai.aimachineserver.domain.games;

import ai.aimachineserver.domain.games.tictactoe.Board;
import ai.aimachineserver.domain.games.tictactoe.GameTicTacToe;
import ai.aimachineserver.domain.games.tictactoe.Judge;

public class GameFactoryTicTacToeNFields implements GameFactory {
    public AbstractGame createGame() {
        Board board = new Board(14);
        Judge judge = new Judge(board, 5);
        return new GameTicTacToe(board, judge, "tictactoeextended");
    }
}
