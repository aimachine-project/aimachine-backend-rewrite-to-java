package ai.aimachineserver.domain.games.soccer;

import ai.aimachineserver.domain.games.tictactoe.BoardCoords;

import java.util.Arrays;
import java.util.List;

public class JudgeSoccer {
    private final BoardSoccer board;

    private final List<BoardCoords> winningNodesCoords = Arrays.asList(
            new BoardCoords(0, BoardSoccer.middleColIndex - 1),
            new BoardCoords(0, BoardSoccer.middleColIndex),
            new BoardCoords(0, BoardSoccer.middleColIndex + 1),
            new BoardCoords(BoardSoccer.BOARD_HEIGHT, BoardSoccer.middleColIndex - 1),
            new BoardCoords(BoardSoccer.BOARD_HEIGHT, BoardSoccer.middleColIndex),
            new BoardCoords(BoardSoccer.BOARD_HEIGHT, BoardSoccer.middleColIndex + 1)
    );

    public JudgeSoccer(BoardSoccer board) {
        this.board = board;
    }

    TurnResultSoccer announceTurnResult() {
        BoardSoccer.Node currentNode = board.getCurrentNode();
        if (winningNodesCoords.contains(new BoardCoords(currentNode.rowIndex, currentNode.colIndex)))
            return TurnResultSoccer.WIN;
        if (!currentNode.hasAnyFreeLink()) return TurnResultSoccer.LOSE;
        if (currentNode.hasMoreThanOneLink()) return TurnResultSoccer.TURN_ONGOING;
        if (currentNode.hasOnlyOneLink()) return TurnResultSoccer.TURN_OVER;
        return TurnResultSoccer.TURN_ONGOING;
    }
}
