package ai.aimachineserver.domain.games.soccer;

import ai.aimachineserver.domain.games.tictactoe.BoardCoords;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static ai.aimachineserver.domain.games.soccer.BoardSoccer.*;
import static ai.aimachineserver.domain.games.soccer.NodeLink.*;
import static org.assertj.core.api.Assertions.assertThat;

class BoardSoccerTest {

    @Test
    void getCurrentNode__afterInitialization__currentNodeIsInTheMiddle() {
        BoardSoccer board = new BoardSoccer();
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(new BoardCoords(
                BoardSoccer.middleRowIndex,
                BoardSoccer.middleColIndex));
    }

    @Test
    void makeLink__moveInBounds__currentNodeHasCorrectIndices() {
        BoardSoccer board = new BoardSoccer();
        board.makeLink(LINK_TOP);
        board.makeLink(LINK_RIGHT);
        board.makeLink(LINK_RIGHT);
        int expectedRowIndex = BoardSoccer.middleRowIndex - 1;
        int expectedColIndex = BoardSoccer.middleColIndex + 2;
        BoardCoords expected = new BoardCoords(expectedRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__moveInBoundsOtherDirection__currentNodeHasCorrectIndices() {
        BoardSoccer board = new BoardSoccer();
        board.makeLink(LINK_LEFT);
        board.makeLink(LINK_TOP_RIGHT);
        board.makeLink(LINK_TOP_LEFT);
        board.makeLink(LINK_LEFT);
        board.makeLink(LINK_LEFT);
        board.makeLink(LINK_BOTTOM_RIGHT);
        board.makeLink(LINK_BOTTOM);
        board.makeLink(LINK_BOTTOM);
        board.makeLink(LINK_BOTTOM_LEFT);
        int expectedRowIndex = 8;
        int expectedColIndex = 2;
        BoardCoords expected = new BoardCoords(expectedRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsRight__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.rangeClosed(0, 2 * BoardSoccer.BOARD_WIDTH).forEach(
                i -> board.makeLink(LINK_RIGHT)
        );
        int expectedColIndex = BoardSoccer.BOARD_WIDTH - 1;
        BoardCoords expected = new BoardCoords(BoardSoccer.middleRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsLeft__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.rangeClosed(0, 2 * BoardSoccer.BOARD_WIDTH).forEach(
                i -> board.makeLink(LINK_LEFT)
        );
        int expectedColIndex = 1;
        BoardCoords expected = new BoardCoords(BoardSoccer.middleRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsTop__colInsideGate__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.rangeClosed(0, 2 * BoardSoccer.BOARD_HEIGHT).forEach(
                i -> board.makeLink(LINK_TOP)
        );
        int expectedRowIndex = 0;
        BoardCoords expected = new BoardCoords(expectedRowIndex, BoardSoccer.middleColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsDown__colInsideGate__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.rangeClosed(0, BoardSoccer.BOARD_HEIGHT).forEach(
                i -> board.makeLink(LINK_BOTTOM)
        );
        BoardCoords expected = new BoardCoords(BoardSoccer.BOARD_HEIGHT, BoardSoccer.middleColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsTop__colOutsideGate__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.rangeClosed(0, BoardSoccer.GATE_WIDTH).forEach(
                i -> board.makeLink(LINK_RIGHT)
        );
        IntStream.rangeClosed(0, 2 * BoardSoccer.BOARD_HEIGHT).forEach(
                i -> board.makeLink(LINK_TOP)
        );
        int expectedRowIndex = 1;
        int expectedColIndex = BoardSoccer.middleColIndex + GATE_WIDTH + 1;
        BoardCoords expected = new BoardCoords(expectedRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsDown__colOutsideGate__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.rangeClosed(0, GATE_WIDTH).forEach(
                i -> board.makeLink(LINK_LEFT)
        );
        IntStream.rangeClosed(0, 2 * BOARD_HEIGHT).forEach(
                i -> board.makeLink(LINK_BOTTOM)
        );
        int expectedRowIndex = BOARD_HEIGHT - 1;
        int expectedColIndex = BoardSoccer.middleColIndex - GATE_WIDTH - 1;
        BoardCoords expected = new BoardCoords(expectedRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideRightBorderAndThenDown__currentNodeStaysInPlace() {
        BoardSoccer board = new BoardSoccer();
        IntStream.rangeClosed(0, 2 * BOARD_WIDTH).forEach(
                i -> board.makeLink(LINK_RIGHT)
        );
        board.makeLink(LINK_BOTTOM);
        int expectedColIndex = BOARD_WIDTH - 1;
        BoardCoords expected = new BoardCoords(BoardSoccer.middleRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsBottomRight__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.range(1, BoardSoccer.middleRowIndex - 1).forEach(
                i -> board.makeLink(LINK_BOTTOM)
        );
        IntStream.range(1, BoardSoccer.middleColIndex - 1).forEach(
                i -> board.makeLink(LINK_RIGHT)
        );
        board.makeLink(LINK_BOTTOM_RIGHT);
        board.makeLink(LINK_BOTTOM_RIGHT);
        int expectedRowIndex = BOARD_HEIGHT - 1;
        int expectedColIndex = BOARD_WIDTH - 1;
        BoardCoords expected = new BoardCoords(expectedRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void makeLink__tryMoveOutsideBoundsBottomSkew__currentNodeStaysInBounds() {
        BoardSoccer board = new BoardSoccer();
        IntStream.range(1, middleRowIndex - 1).forEach(
                i -> board.makeLink(LINK_BOTTOM)
        );
        IntStream.range(1, middleColIndex - 3).forEach(
                i -> board.makeLink(LINK_RIGHT)
        );
        board.makeLink(LINK_BOTTOM_RIGHT);
        board.makeLink(LINK_BOTTOM_LEFT);
        int expectedRowIndex = BOARD_HEIGHT - 1;
        int expectedColIndex = BOARD_WIDTH - 3;
        BoardCoords expected = new BoardCoords(expectedRowIndex, expectedColIndex);
        BoardSoccer.Node node = board.getCurrentNode();
        BoardCoords result = new BoardCoords(node.rowIndex, node.colIndex);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void isFieldAvailable__topLeftBorder__moveTopRight__returnFalse() {
        BoardSoccer board = new BoardSoccer();
        IntStream.range(0, middleColIndex - 1).forEach(
                i -> board.makeLink(LINK_LEFT)
        );
        IntStream.range(0, middleRowIndex - 1).forEach(
                i -> board.makeLink(LINK_TOP)
        );
        boolean result = board.isFieldAvailable(0, 0);
        assertThat(result).isEqualTo(false);
    }

    @Test
    void isFieldAvailable__bottomRightBorder__moveBottomRight__returnFalse() {
        BoardSoccer board = new BoardSoccer();
        IntStream.range(0, middleColIndex - 1).forEach(
                i -> board.makeLink(LINK_RIGHT)
        );
        IntStream.range(0, middleRowIndex - 1).forEach(
                i -> board.makeLink(LINK_BOTTOM)
        );
        boolean result = board.isFieldAvailable(BOARD_HEIGHT, BOARD_WIDTH);
        assertThat(result).isEqualTo(false);
    }
}
