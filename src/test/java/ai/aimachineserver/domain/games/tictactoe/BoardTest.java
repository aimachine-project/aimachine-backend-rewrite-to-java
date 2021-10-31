package ai.aimachineserver.domain.games.tictactoe;

import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    int[][] emptyBoardFields = new int[3][3];

    {
        for (int[] row : emptyBoardFields) {
            Arrays.fill(row, Board.EMPTY_FIELD_VALUE);
        }
    }

    @Test
    void init_noValuePassed_allFieldsEmpty() {
        assertThat(new Board().getAllFieldValues()).isEqualTo(emptyBoardFields);
    }

    @Test
    void setFieldValue_valueInBoardScope_returnsCorrectValues() {
        Board board = new Board();
        board.setFieldValue(0, 0, 1);
        board.setFieldValue(1, 2, -1);
        board.setFieldValue(2, 0, -1);
        int[][] expectedFieldValues = {{1, 0, 0}, {0, 0, -1}, {-1, 0, 0}};
        assertThat(board.getAllFieldValues()).isEqualTo(expectedFieldValues);
    }

    @Test
    void isFieldAvailable() {
        Board board = new Board();
        assertThat(board.isFieldAvailable(1, 2)).isTrue();
        board.setFieldValue(1, 2, 1);
        assertThat(board.isFieldAvailable(1, 2)).isFalse();
    }

    @Test
    void getAvailableFieldIndices_someValuesSet_returnsCorrectIndices() {
        Board board = new Board();
        board.setFieldValue(0, 0, 1);
        board.setFieldValue(0, 1, 1);
        board.setFieldValue(2, 2, -1);
        board.setFieldValue(2, 1, 1);
        board.setFieldValue(1, 1, -1);
        List<AbstractMap.SimpleEntry<Integer, Integer>> expected = Arrays.asList(
                new AbstractMap.SimpleEntry<>(0, 2),
                new AbstractMap.SimpleEntry<>(1, 0),
                new AbstractMap.SimpleEntry<>(1, 2),
                new AbstractMap.SimpleEntry<>(2, 0));
        assertThat(board.getAvailableFieldIndices()).isEqualTo(expected);
    }

    @Test
    void clearFields_someValuesSet_returnsEmptyBoard() {
        Board board = new Board();
        board.setFieldValue(0, 0, -1);
        board.setFieldValue(2, 1, 1);
        assertThat(board.getAllFieldValues()[0]).isEqualTo(new int[]{-1, 0, 0});
        assertThat(board.getAllFieldValues()[2]).isEqualTo(new int[]{0, 1, 0});
        board.clearAllFields();
        assertThat(board.getAllFieldValues()).isEqualTo(emptyBoardFields);
    }

    @Test
    void initialize_boardSize14_allFieldsInitializedWithEmptyFieldValue() {
        Board board = new Board(14);
        int[][] expected = new int[14][14];
        for (int[] row : expected) {
            Arrays.fill(row, Board.EMPTY_FIELD_VALUE);
        }
        assertThat(board.getAllFieldValues()).isEqualTo(expected);
    }
}
