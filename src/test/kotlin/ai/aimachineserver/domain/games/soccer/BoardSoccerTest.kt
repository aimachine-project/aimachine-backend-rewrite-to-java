package ai.aimachineserver.domain.games.soccer

import ai.aimachineserver.domain.games.soccer.BoardSoccer.Companion.BOARD_HEIGHT
import ai.aimachineserver.domain.games.soccer.BoardSoccer.Companion.BOARD_WIDTH
import ai.aimachineserver.domain.games.soccer.BoardSoccer.Companion.GATE_WIDTH
import ai.aimachineserver.domain.games.soccer.BoardSoccer.Companion.middleColIndex
import ai.aimachineserver.domain.games.soccer.BoardSoccer.Companion.middleRowIndex
import ai.aimachineserver.domain.games.soccer.NodeLink.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BoardSoccerTest {

    @Test
    fun getCurrentNode__afterInitialization__currentNodeIsInTheMiddle() {
        val board = BoardSoccer()
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(middleRowIndex to middleColIndex)
    }

    @Test
    fun makeLink__moveInBounds__currentNodeHasCorrectIndices() {
        val board = BoardSoccer()
        board.makeLink(LINK_TOP)
        board.makeLink(LINK_RIGHT)
        board.makeLink(LINK_RIGHT)
        val expectedRowIndex = middleRowIndex - 1
        val expectedColIndex = middleColIndex + 2
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__moveInBoundsOtherDirection__currentNodeHasCorrectIndices() {
        val board = BoardSoccer()
        board.makeLink(LINK_LEFT)
        board.makeLink(LINK_TOP_RIGHT)
        board.makeLink(LINK_TOP_LEFT)
        board.makeLink(LINK_LEFT)
        board.makeLink(LINK_LEFT)
        board.makeLink(LINK_BOTTOM_RIGHT)
        board.makeLink(LINK_BOTTOM)
        board.makeLink(LINK_BOTTOM)
        board.makeLink(LINK_BOTTOM_LEFT)
        val expectedRowIndex = 8
        val expectedColIndex = 2
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsRight__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (0..(2 * BOARD_WIDTH)).forEach { _ ->
            board.makeLink(LINK_RIGHT)
        }
        val expectedRowIndex = middleRowIndex
        val expectedColIndex = BOARD_WIDTH - 1
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsLeft__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (0..(2 * BOARD_WIDTH)).forEach { _ ->
            board.makeLink(LINK_LEFT)
        }
        val expectedRowIndex = middleRowIndex
        val expectedColIndex = 1
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsTop__colInsideGate__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (0..(2 * BOARD_HEIGHT)).forEach { _ ->
            board.makeLink(LINK_TOP)
        }
        val expectedRowIndex = 0
        val expectedColIndex = middleColIndex
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsDown__colInsideGate__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (0..(2 * BOARD_HEIGHT)).forEach { _ ->
            board.makeLink(LINK_BOTTOM)
        }
        val expectedRowIndex = BOARD_HEIGHT
        val expectedColIndex = middleColIndex
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsTop__colOutsideGate__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (0..GATE_WIDTH).forEach { _ ->
            board.makeLink(LINK_RIGHT)
        }
        (0..(2 * BOARD_HEIGHT)).forEach { _ ->
            board.makeLink(LINK_TOP)
        }
        val expectedRowIndex = 1
        val expectedColIndex = middleColIndex + GATE_WIDTH + 1
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsDown__colOutsideGate__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (0..GATE_WIDTH).forEach { _ ->
            board.makeLink(LINK_LEFT)
        }
        (0..(2 * BOARD_HEIGHT)).forEach { _ ->
            board.makeLink(LINK_BOTTOM)
        }
        val expectedRowIndex = BOARD_HEIGHT - 1
        val expectedColIndex = middleColIndex - GATE_WIDTH - 1
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideRightBorderAndThenDown__currentNodeStaysInPlace() {
        val board = BoardSoccer()
        (0..(2 * BOARD_WIDTH)).forEach { _ ->
            board.makeLink(LINK_RIGHT)
        }
        board.makeLink(LINK_BOTTOM)
        val expectedRowIndex = middleRowIndex
        val expectedColIndex = BOARD_WIDTH - 1
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsBottomRight__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (1 until middleRowIndex - 1).forEach { _ ->
            board.makeLink(LINK_BOTTOM)
        }
        (1 until middleColIndex - 1).forEach { _ ->
            board.makeLink(LINK_RIGHT)
        }
        board.makeLink(LINK_BOTTOM_RIGHT)
        board.makeLink(LINK_BOTTOM_RIGHT)
        val expectedRowIndex = BOARD_HEIGHT - 1
        val expectedColIndex = BOARD_WIDTH - 1
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun makeLink__tryMoveOutsideBoundsBottomSkew__currentNodeStaysInBounds() {
        val board = BoardSoccer()
        (1 until middleRowIndex - 1).forEach { _ ->
            board.makeLink(LINK_BOTTOM)
        }
        (1 until BOARD_WIDTH - middleColIndex - 3).forEach { _ ->
            board.makeLink(LINK_RIGHT)
        }
        board.makeLink(LINK_BOTTOM_RIGHT)
        board.makeLink(LINK_BOTTOM_LEFT)
        val expectedRowIndex = BOARD_HEIGHT - 1
        val expectedColIndex = BOARD_WIDTH - 3
        val result = board.getCurrentNode().let { it.rowIndex to it.colIndex }
        assertThat(result).isEqualTo(expectedRowIndex to expectedColIndex)
    }

    @Test
    fun isFieldAvailable__topLeftBorder__moveTopRight__returnFalse() {
        val board = BoardSoccer()
        (0 until middleColIndex - 1).forEach { _ ->
            board.makeLink(LINK_LEFT)
        }
        (0 until middleRowIndex - 1).forEach { _ ->
            board.makeLink(LINK_TOP)
        }
        val result = board.isFieldAvailable(0, 0)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isFieldAvailable__bottomRightBorder__moveBottomRight__returnFalse() {
        val board = BoardSoccer()
        (0 until middleColIndex - 1).forEach { _ ->
            board.makeLink(LINK_RIGHT)
        }
        (0 until middleRowIndex - 1).forEach { _ ->
            board.makeLink(LINK_BOTTOM)
        }
        val result = board.isFieldAvailable(BOARD_HEIGHT, BOARD_WIDTH)
        assertThat(result).isEqualTo(false)
    }
}
