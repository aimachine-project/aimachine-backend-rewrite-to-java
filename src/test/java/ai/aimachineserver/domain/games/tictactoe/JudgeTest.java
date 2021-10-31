package ai.aimachineserver.domain.games.tictactoe;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JudgeTest {

    @Test
    void announceTurnResult_ninthTurn_noResult_returnsTie() {
        Board board = new Board(3);
        setFieldValues(board, new int[][]{{1, -1, 1}, {1, -1, 1}, {-1, 1, -1}});
        Judge judge = new Judge(board, 3);
        assertThat(judge.announceTurnResult(9)).isEqualTo(TurnResult.TIE);
    }

    private void setFieldValues(Board board, @NotNull int[][] fieldValues) {
        for (int rowIndex = 0; rowIndex < fieldValues.length; rowIndex++) {
            for (int colIndex = 0; colIndex < fieldValues[rowIndex].length; colIndex++) {
                board.setFieldValue(rowIndex, colIndex, fieldValues[rowIndex][colIndex]);
            }
        }
    }

    @Test
    void announceTurnResult_fourthTurn_noResult_returnsGameOngoing() {
        Board board = new Board(3);
        setFieldValues(board, new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        Judge judge = new Judge(board, 3);
        assertThat(judge.announceTurnResult(4)).isEqualTo(TurnResult.GAME_ONGOING);
    }

    @Test
    void announceTurnResult_xBegins_allDiagonalX_returnsWin() {
        Board board = new Board(3);
        setFieldValues(board, new int[][]{{1, -1, 1}, {-1, 1, -1}, {0, 0, 1}});
        Judge judge = new Judge(board, 3);
        assertThat(judge.announceTurnResult(6)).isEqualTo(TurnResult.WIN);
    }

    @Test
    void announceTurnResult_oBegins_allOppositeDiagonalO_returnsWin() {
        Board board = new Board(3);
        setFieldValues(board, new int[][]{{1, -1, -1}, {1, -1, 0}, {-1, 0, 1}});
        Judge judge = new Judge(board, 3);
        assertThat(judge.announceTurnResult(5)).isEqualTo(TurnResult.WIN);
    }

    @Test
    void announceTurnResult_oBegins_allOrthogonalX_returnsWin() {
        Board board = new Board(3);
        setFieldValues(board, new int[][]{{-1, 1, -1}, {1, 1, 1}, {0, -1, 1}});
        Judge judge = new Judge(board, 3);
        assertThat(judge.announceTurnResult(9)).isEqualTo(TurnResult.WIN);
    }

    @Test
    void announceTurnResult_7x7_diagonalConditionMet_returnsWin() {
        Board board = new Board(7);
        board.setFieldValue(0, 3, 1);
        board.setFieldValue(1, 4, 1);
        board.setFieldValue(2, 5, 1);
        board.setFieldValue(3, 6, 1);
        board.setFieldValue(0, 4, -1);
        board.setFieldValue(0, 5, -1);
        board.setFieldValue(0, 6, -1);
        Judge judge = new Judge(board, 4);
        assertThat(judge.announceTurnResult(7)).isEqualTo(TurnResult.WIN);
    }

    @Test
    void announceTurnResult_7x7_rowConditionMet_returnsWin() {
        Board board = new Board(7);
        board.setFieldValue(6, 2, 1);
        board.setFieldValue(6, 3, -1);
        board.setFieldValue(6, 4, -1);
        board.setFieldValue(6, 5, -1);
        board.setFieldValue(6, 6, -1);
        board.setFieldValue(0, 5, 1);
        board.setFieldValue(0, 6, 1);
        Judge judge = new Judge(board, 4);
        assertThat(judge.announceTurnResult(7)).isEqualTo(TurnResult.WIN);
    }

    @Test
    void announceTurnResult_14x14_diagonalsConditionMet_returnsWin() {
        Board board = new Board(14);
        board.setFieldValue(6, 2, -1);
        board.setFieldValue(7, 3, -1);
        board.setFieldValue(8, 4, -1);
        board.setFieldValue(9, 5, -1);
        board.setFieldValue(10, 6, -1);
        board.setFieldValue(2, 5, 1);
        board.setFieldValue(3, 6, -1);
        board.setFieldValue(0, 5, 1);
        board.setFieldValue(0, 6, 1);
        Judge judge = new Judge(board, 5);
        assertThat(judge.announceTurnResult(9)).isEqualTo(TurnResult.WIN);
    }

    @Test
    void announceTurnResult_14x14_antiDiagonalsConditionMet_returnsWin() {
        Board board = new Board(14);
        board.setFieldValue(6, 6, 1);
        board.setFieldValue(7, 5, 1);
        board.setFieldValue(8, 4, 1);
        board.setFieldValue(9, 3, 1);
        board.setFieldValue(10, 2, 1);
        board.setFieldValue(2, 5, -1);
        board.setFieldValue(3, 6, 1);
        board.setFieldValue(0, 5, -1);
        board.setFieldValue(0, 6, -1);
        Judge judge = new Judge(board, 5);
        assertThat(judge.announceTurnResult(9)).isEqualTo(TurnResult.WIN);
    }
}
