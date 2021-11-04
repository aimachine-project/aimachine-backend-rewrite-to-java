package ai.aimachineserver.domain.games.tictactoe;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.stream.IntStream;

@AllArgsConstructor
public class Judge {
    private final Board board;
    private final int sameValuesCountWinningCondition;
    private final int minTurnsCount;
    private final int maxTurnsCount;

    public Judge(Board board, int sameValuesCountWinningCondition) {
        this.board = board;
        this.sameValuesCountWinningCondition = sameValuesCountWinningCondition;

        minTurnsCount = 2 * sameValuesCountWinningCondition - 1;
        maxTurnsCount = board.size * board.size;
    }

    TurnResult announceTurnResult(int turnNumber) {
        int[][] fieldValues = board.getAllFieldValues();
        if (turnNumber < minTurnsCount) return TurnResult.GAME_ONGOING;
        if (isWinningConditionMet(fieldValues)) return TurnResult.WIN;
        if (turnNumber >= maxTurnsCount) return TurnResult.TIE;
        return TurnResult.GAME_ONGOING;
    }

    private boolean isWinningConditionMet(int[][] fieldValues) {
        if (enoughValuesOrthogonally(fieldValues)) return true;
        return enoughValuesDiagonally(fieldValues);
    }

    private boolean enoughValuesOrthogonally(int[][] fieldValues) {
        if (enoughAdjacentValuesInAnyRow(fieldValues)) return true;
        return enoughValuesInAnyColumn(fieldValues);
    }

    private boolean enoughAdjacentValuesInAnyRow(int[][] fieldValues) {
        for (int[] row : fieldValues) {
            for (int rowIndex = 0; rowIndex <= row.length - sameValuesCountWinningCondition; rowIndex++) {
                if (Math.abs(
                        Arrays.stream(row, rowIndex, rowIndex + sameValuesCountWinningCondition).sum()
                ) == sameValuesCountWinningCondition) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean enoughValuesInAnyColumn(int[][] fieldValues) {
        int[][] fieldValuesTransposed = transposeArray(fieldValues);
        return enoughAdjacentValuesInAnyRow(fieldValuesTransposed);
    }

    private int[][] transposeArray(int[][] inputArray) {
        int[][] outputArray = new int[inputArray[0].length][inputArray.length];
        for (int i = 0; i <= inputArray.length - 1; i++) {
            for (int j = 0; j <= inputArray[i].length - 1; j++) {
                outputArray[i][j] = inputArray[j][i];
            }
        }
        return outputArray;
    }

    private boolean enoughValuesDiagonally(int[][] fieldValues) {
        if (enoughValuesOnDiagonals(fieldValues)) return true;
        return enoughValuesOnAntiDiagonals(fieldValues);
    }

    private boolean enoughValuesOnDiagonals(int[][] fieldValues) {
        for (int rowIndex = 0; rowIndex <= (fieldValues.length - sameValuesCountWinningCondition); rowIndex++) {
            for (int colIndex = 0; colIndex <= (fieldValues.length - sameValuesCountWinningCondition); colIndex++) {
                int finalRowIndex = rowIndex;
                int finalColIndex = colIndex;
                if (Math.abs(IntStream.range(0, sameValuesCountWinningCondition).map(it ->
                        fieldValues[finalRowIndex + it][finalColIndex + it]).sum()) == sameValuesCountWinningCondition) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean enoughValuesOnAntiDiagonals(int[][] fieldValues) {
        for (int rowIndex = sameValuesCountWinningCondition - 1; rowIndex <= fieldValues.length - 1; rowIndex++) {
            for (int colIndex = 0; colIndex <= fieldValues.length - sameValuesCountWinningCondition; colIndex++) {
                int finalRowIndex = rowIndex;
                int finalColIndex = colIndex;
                if (Math.abs(IntStream.range(0, sameValuesCountWinningCondition).map(it ->
                        fieldValues[finalRowIndex - it][finalColIndex + it]).sum()
                ) == sameValuesCountWinningCondition) {
                    return true;
                }
            }
        }
        return false;
    }
}
