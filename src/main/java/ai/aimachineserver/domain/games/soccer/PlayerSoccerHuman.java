package ai.aimachineserver.domain.games.soccer;

import static ai.aimachineserver.domain.games.soccer.NodeLink.*;

class PlayerSoccerHuman extends PlayerSoccer {

    PlayerSoccerHuman(String name) {
        super(name);
    }

    @Override
    void makeMove(BoardSoccer board, int otherNodeRowIndex, int otherNodeColIndex) {
        BoardSoccer.Node currentNode = board.getCurrentNode();
        int rowsDiff = otherNodeRowIndex - currentNode.rowIndex;
        int colsDiff = otherNodeColIndex - currentNode.colIndex;
        if (rowsDiff == -1 && colsDiff == 0)
            board.makeLink(LINK_TOP);
        else if (rowsDiff == -1 && colsDiff == 1)
            board.makeLink(LINK_TOP_RIGHT);
        else if (rowsDiff == 0 && colsDiff == 1)
            board.makeLink(LINK_RIGHT);
        else if (rowsDiff == 1 && colsDiff == 1)
            board.makeLink(LINK_BOTTOM_RIGHT);
        else if (rowsDiff == 1 && colsDiff == 0)
            board.makeLink(LINK_BOTTOM);
        else if (rowsDiff == 1 && colsDiff == -1)
            board.makeLink(LINK_BOTTOM_LEFT);
        else if (rowsDiff == 0 && colsDiff == -1)
            board.makeLink(LINK_LEFT);
        else if (rowsDiff == -1 && colsDiff == -1)
            board.makeLink(LINK_TOP_LEFT);
        else throw new IllegalArgumentException("Illegal movement coords");
    }
}
