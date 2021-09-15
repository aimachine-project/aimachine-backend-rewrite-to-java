package ai.aimachineserver.domain.games.soccer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class JudgeSoccerTest {

    @Test
    fun announceTurnResult__noMovements__returnOngoing() {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_ONGOING)
    }

    @Test
    fun announceTurnResult__nodeAlreadyHasLink__returnOngoing() {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        board.makeLink(NodeLink.LINK_LEFT)
        board.makeLink(NodeLink.LINK_TOP_RIGHT)
        board.makeLink(NodeLink.LINK_BOTTOM)
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_ONGOING)
    }

    @Test
    fun announceTurnResult__nodeHasNoLink__returnOver() {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        board.makeLink(NodeLink.LINK_LEFT)
        board.makeLink(NodeLink.LINK_TOP)
        board.makeLink(NodeLink.LINK_BOTTOM)
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_OVER)
    }

    @Test
    fun announceTurnResult__nodeHasNoLinks__noLinkCreated__returnOver() {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        board.makeLink(NodeLink.LINK_LEFT)
        board.makeLink(NodeLink.LINK_RIGHT)
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.TURN_OVER)
    }

    @Test
    fun announceTurnResult__movedInBottomLeftCorner__returnLose() {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        (1 until BoardSoccer.middleColIndex - 2).forEach { _ ->
            board.makeLink(NodeLink.LINK_LEFT)
        }
        (1 until BoardSoccer.middleRowIndex - 2).forEach { _ ->
            board.makeLink(NodeLink.LINK_BOTTOM)
        }
        board.makeLink(NodeLink.LINK_BOTTOM_LEFT)
        board.makeLink(NodeLink.LINK_BOTTOM_LEFT)
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.LOSE)
    }

    @Test
    fun announceTurnResult__movedInTopGate__returnWin() {
        val board = BoardSoccer()
        val judge = JudgeSoccer(board)
        (0 until BoardSoccer.middleRowIndex).forEach { _ ->
            board.makeLink(NodeLink.LINK_TOP)
        }
        assertThat(judge.announceTurnResult()).isEqualTo(TurnResultSoccer.WIN)
    }
}