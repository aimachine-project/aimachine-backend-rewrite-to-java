package ai.aimachineserver.domain.games.soccer;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class JudgeSoccerTest {

    @Test
    void announceTurnResult__noMovements__returnOngoing() {
        BoardSoccer board = new BoardSoccer();
        JudgeSoccer judge = new JudgeSoccer(board);
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_ONGOING);
    }

    @Test
    void announceTurnResult__nodeAlreadyHasLink__returnOngoing() {
        BoardSoccer board = new BoardSoccer();
        JudgeSoccer judge = new JudgeSoccer(board);
        board.makeLink(NodeLink.LINK_LEFT);
        board.makeLink(NodeLink.LINK_TOP_RIGHT);
        board.makeLink(NodeLink.LINK_BOTTOM);
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_ONGOING);
    }

    @Test
    void announceTurnResult__nodeHasNoLink__returnOver() {
        BoardSoccer board = new BoardSoccer();
        JudgeSoccer judge = new JudgeSoccer(board);
        board.makeLink(NodeLink.LINK_LEFT);
        board.makeLink(NodeLink.LINK_TOP);
        board.makeLink(NodeLink.LINK_BOTTOM);
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_OVER);
    }

    @Test
    void announceTurnResult__nodeHasNoLinks__noLinkCreated__returnOver() {
        BoardSoccer board = new BoardSoccer();
        JudgeSoccer judge = new JudgeSoccer(board);
        board.makeLink(NodeLink.LINK_LEFT);
        board.makeLink(NodeLink.LINK_RIGHT);
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_OVER);
    }

    @Test
    void announceTurnResult__movedInBottomLeftCorner__returnLose() {
        BoardSoccer board = new BoardSoccer();
        JudgeSoccer judge = new JudgeSoccer(board);
        IntStream.range(1, BoardSoccer.middleColIndex - 2).forEach(
                i -> board.makeLink(NodeLink.LINK_LEFT)
        );
        IntStream.range(1, BoardSoccer.middleRowIndex - 2).forEach(
                i -> board.makeLink(NodeLink.LINK_BOTTOM)
        );
        board.makeLink(NodeLink.LINK_BOTTOM_LEFT);
        board.makeLink(NodeLink.LINK_BOTTOM_LEFT);
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.LOSE);
    }

    @Test
    void announceTurnResult__movedInTopGate__returnWin() {
        BoardSoccer board = new BoardSoccer();
        JudgeSoccer judge = new JudgeSoccer(board);
        IntStream.range(0, BoardSoccer.middleRowIndex).forEach(
                i -> board.makeLink(NodeLink.LINK_TOP)
        );
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.WIN);
    }
}
