package ai.aimachineserver.domain.games.soccer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt

class BoardSoccerTest {

    @Test
    fun getCurrentNode__afterInitialization__currentNodeIsInTheMiddle() {
        val board = BoardSoccer()
        val expectedRowIndex = (BoardSoccer.BOARD_HEIGHT / 2.0).roundToInt()
        val expectedColIndex = (BoardSoccer.BOARD_WIDTH / 2.0).roundToInt()
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun isFieldAvailable() {
    }

    @Test
    fun makeLink() {
    }
}