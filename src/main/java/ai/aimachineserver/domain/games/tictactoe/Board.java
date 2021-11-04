package ai.aimachineserver.domain.games.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    public final static int DEFAULT_SIZE = 3;
    public final static int EMPTY_FIELD_VALUE = 0;
    public final int size;
    private final int[][] allFieldValues;

    public Board(int size) {
        this.size = size;
        allFieldValues = new int[size][size];
        setAllFieldsEmpty();
    }

    public Board() {
        this.size = DEFAULT_SIZE;
        allFieldValues = new int[size][size];
        setAllFieldsEmpty();
    }

    int[][] getAllFieldValues() {
        return allFieldValues;
    }

    void setFieldValue(int rowIndex, int colIndex, int fieldValue) {
        allFieldValues[rowIndex][colIndex] = fieldValue;
    }

    List<BoardCoords> getAvailableFieldIndices() {
        List<BoardCoords> availableFieldIndices = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < allFieldValues.length; rowIndex++) {
            for (int colIndex = 0; colIndex < allFieldValues[rowIndex].length; colIndex++) {
                if (allFieldValues[rowIndex][colIndex] == EMPTY_FIELD_VALUE) {
                    availableFieldIndices.add(new BoardCoords(rowIndex, colIndex));
                }
            }
        }
        return availableFieldIndices;
    }

    boolean isFieldAvailable(int rowIndex, int colIndex) {
        return allFieldValues[rowIndex][colIndex] == EMPTY_FIELD_VALUE;
    }

    void clearAllFields() {
        setAllFieldsEmpty();
    }

    private void setAllFieldsEmpty() {
        for (int[] row : allFieldValues) {
            Arrays.fill(row, EMPTY_FIELD_VALUE);
        }
    }
}
