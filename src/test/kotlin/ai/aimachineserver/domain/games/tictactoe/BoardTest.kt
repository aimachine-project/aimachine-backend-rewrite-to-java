package ai.aimachineserver.domain.games.tictactoe

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BoardTest {

    private val emptyBoardFields = Array(3) { IntArray(3) { Board.EMPTY_FIELD_VALUE } }

    @Test
    fun init_noValuePassed_allFieldsEmpty() {
        assertThat(Board().getAllFieldValues()).isEqualTo(emptyBoardFields)
    }

    @Test
    fun setFieldValue_valueInBoardScope_returnsCorrectValues() {
        val board = Board()
        board.setFieldValue(0, 0, 1)
        board.setFieldValue(1, 2, -1)
        board.setFieldValue(2, 0, -1)
        val expectedFieldValues = arrayOf(intArrayOf(1, 0, 0), intArrayOf(0, 0, -1), intArrayOf(-1, 0, 0))
        assertThat(board.getAllFieldValues()).isEqualTo(expectedFieldValues)
    }

    @Test
    fun isFieldAvailable() {
        val board = Board()
        assertThat(board.isFieldAvailable(1, 2)).isTrue
        board.setFieldValue(1, 2, 1)
        assertThat(board.isFieldAvailable(1, 2)).isFalse
    }

    @Test
    fun getAvailableFieldIndices_someValuesSet_returnsCorrectIndices() {
        val board = Board()
        with(board) {
            setFieldValue(0, 0, 1)
            setFieldValue(0, 1, 1)
            setFieldValue(2, 2, -1)
            setFieldValue(2, 1, 1)
            setFieldValue(1, 1, -1)
        }
        val expected = listOf(Pair(0, 2), Pair(1, 0), Pair(1, 2), Pair(2, 0))
        assertThat(board.getAvailableFieldIndices()).isEqualTo(expected)
    }

    @Test
    fun clearFields_someValuesSet_returnsEmptyBoard() {
        val board = Board()
        with(board) {
            setFieldValue(0, 0, -1)
            setFieldValue(2, 1, 1)
        }
        assertThat(board.getAllFieldValues()[0]).isEqualTo(intArrayOf(-1, 0, 0))
        assertThat(board.getAllFieldValues()[2]).isEqualTo(intArrayOf(0, 1, 0))
        board.clearAllFields()
        assertThat(board.getAllFieldValues()).isEqualTo(emptyBoardFields)
    }

    @Test
    fun initialize_boardSize14_allFieldsInitializedWithEmptyFieldValue() {
        val board = Board(14)
        val expected = Array(14) { IntArray(14) { Board.EMPTY_FIELD_VALUE } }
        assertThat(board.getAllFieldValues()).isEqualTo(expected)
    }
}
