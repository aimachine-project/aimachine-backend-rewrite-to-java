package ai.aimachineserver.domain.games.tictactie

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Judge::class, Board::class])
class JudgeTest {

    @Test
    fun announceTurnResult_ninthTurn_noResult_returnsTie() {
        val board = Board(3)
        setFieldValues(board, arrayOf(intArrayOf(1, -1, 1), intArrayOf(1, -1, 1), intArrayOf(-1, 1, -1)))
        val judge = Judge(board)
        assertThat(judge.announceTurnResult(9)).isEqualTo(TurnResult.TIE)
    }

    private fun setFieldValues(board: Board, fieldValues: Array<IntArray>): Board {
        fieldValues.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, fieldValue ->
                board.setFieldValue(rowIndex, colIndex, fieldValue)
            }
        }
        return board
    }

    @Test
    fun announceTurnResult_fourthTurn_noResult_returnsGameOngoing() {
        val board = Board(3)
        setFieldValues(board, arrayOf(intArrayOf(1, 1, 1), intArrayOf(1, 1, 1), intArrayOf(1, 1, 1)))
        val judge = Judge(board)
        assertThat(judge.announceTurnResult(4)).isEqualTo(TurnResult.GAME_ONGOING)
    }

    @Test
    fun announceTurnResult_xBegins_allDiagonalX_returnsWin() {
        val board = Board(3)
        setFieldValues(board, arrayOf(intArrayOf(1, -1, 1), intArrayOf(-1, 1, -1), intArrayOf(0, 0, 1)))
        val judge = Judge(board)
        assertThat(judge.announceTurnResult(6)).isEqualTo(TurnResult.WIN)
    }

    @Test
    fun announceTurnResult_oBegins_allOppositeDiagonalO_returnsWin() {
        val board = Board(3)
        setFieldValues(board, arrayOf(intArrayOf(1, -1, -1), intArrayOf(1, -1, 0), intArrayOf(-1, 0, 1)))
        val judge = Judge(board)
        assertThat(judge.announceTurnResult(5)).isEqualTo(TurnResult.WIN)
    }

    @Test
    fun announceTurnResult_oBegins_allOrthogonalX_returnsWin() {
        val board = Board(3)
        setFieldValues(board, arrayOf(intArrayOf(-1, 1, -1), intArrayOf(1, 1, 1), intArrayOf(0, -1, 1)))
        val judge = Judge(board)
        assertThat(judge.announceTurnResult(9)).isEqualTo(TurnResult.WIN)
    }

    @Test
    fun announceTurnResult_7x7_diagonalConditionMet_returnsWin() {
        val board = Board(7)
        board.setFieldValue(0, 3, 1)
        board.setFieldValue(1, 4, 1)
        board.setFieldValue(2, 5, 1)
        board.setFieldValue(3, 6, 1)
        board.setFieldValue(0, 4, -1)
        board.setFieldValue(0, 5, -1)
        board.setFieldValue(0, 6, -1)
        val judge = Judge(board, 4)
        assertThat(judge.announceTurnResult(7)).isEqualTo(TurnResult.WIN)
    }
}
