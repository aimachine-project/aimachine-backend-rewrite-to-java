package ai.aimachineserver.domain.gamelogic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JudgeTest {

    @Test
    fun announceTurnResult_ninthTurn_noResult_returnsTie() {
        val board = Board(arrayOf(intArrayOf(1, -1, 1), intArrayOf(1, -1, 1), intArrayOf(-1, 1, -1)))
        val judge = Judge()
        assertThat(judge.announceTurnResult(board, 9)).isEqualTo(TurnResult.TIE)
    }

    @Test
    fun announceTurnResult_fourthTurn_noResult_returnsGameOngoing() {
        val board = Board(arrayOf(intArrayOf(1, 1, 1), intArrayOf(1, 1, 1), intArrayOf(1, 1, 1)))
        val judge = Judge()
        assertThat(judge.announceTurnResult(board, 4)).isEqualTo(TurnResult.GAME_ONGOING)
    }

    @Test
    fun announceTurnResult_xBegins_allDiagonalX_returnsWin() {
        val board = Board(arrayOf(intArrayOf(1, -1, 1), intArrayOf(-1, 1, -1), intArrayOf(0, 0, 1)))
        val judge = Judge()
        assertThat(judge.announceTurnResult(board, 6)).isEqualTo(TurnResult.WIN)
    }

    @Test
    fun announceTurnResult_oBegins_allOppositeDiagonalO_returnsWin() {
        val board = Board(arrayOf(intArrayOf(1, -1, -1), intArrayOf(1, -1, 0), intArrayOf(-1, 0, 1)))
        val judge = Judge()
        assertThat(judge.announceTurnResult(board, 5)).isEqualTo(TurnResult.WIN)
    }

    @Test
    fun announceTurnResult_oBegins_allOrthogonalX_returnsWin() {
        val board = Board(arrayOf(intArrayOf(-1, 1, -1), intArrayOf(1, 1, 1), intArrayOf(0, -1, 1)))
        val judge = Judge()
        assertThat(judge.announceTurnResult(board, 9)).isEqualTo(TurnResult.WIN)
    }
}
