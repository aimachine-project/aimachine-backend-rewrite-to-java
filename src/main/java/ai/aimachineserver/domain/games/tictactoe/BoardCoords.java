package ai.aimachineserver.domain.games.tictactoe;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardCoords {
    public int rowIndex;
    public int colIndex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardCoords that = (BoardCoords) o;

        if (rowIndex != that.rowIndex) return false;
        return colIndex == that.colIndex;
    }

    @Override
    public int hashCode() {
        int result = rowIndex;
        result = 31 * result + colIndex;
        return result;
    }
}
