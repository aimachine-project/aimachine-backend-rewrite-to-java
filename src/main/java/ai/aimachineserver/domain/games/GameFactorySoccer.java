package ai.aimachineserver.domain.games;

import ai.aimachineserver.domain.games.soccer.BoardSoccer;
import ai.aimachineserver.domain.games.soccer.GameSoccer;
import ai.aimachineserver.domain.games.soccer.JudgeSoccer;

public class GameFactorySoccer implements GameFactory {
    public AbstractGame createGame() {
        BoardSoccer board = new BoardSoccer();
        JudgeSoccer judge = new JudgeSoccer(board);
        return new GameSoccer(board, judge, "soccer");
    }
}
