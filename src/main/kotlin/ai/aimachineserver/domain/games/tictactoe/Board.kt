package ai.aimachineserver.domain.games.tictactoe

class Board(val size: Int = DEFAULT_SIZE) {

    companion object {
        const val DEFAULT_SIZE = 3
        const val EMPTY_FIELD_VALUE = 0
    }

    private val allFieldValues: Array<IntArray> = Array(size) { IntArray(size) { EMPTY_FIELD_VALUE } }

    fun getAllFieldValues() = allFieldValues

    fun setFieldValue(rowIndex: Int, colIndex: Int, fieldValue: Int) {
        allFieldValues[rowIndex][colIndex] = fieldValue
    }

    fun getAvailableFieldIndices(): List<BoardCoords> {
        val availableFieldIndices = mutableListOf<Pair<Int, Int>>()
        allFieldValues.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, fieldValue ->
                if (fieldValue == EMPTY_FIELD_VALUE) {
                    availableFieldIndices.add(Pair(rowIndex, colIndex))
                }
            }
        }
        return availableFieldIndices.map { BoardCoords(it.first, it.second) }
    }

    fun isFieldAvailable(rowIndex: Int, colIndex: Int) = allFieldValues[rowIndex][colIndex] == EMPTY_FIELD_VALUE

    fun clearAllFields() = allFieldValues.forEachIndexed { rowIndex, row ->
        row.indices.forEach { colIndex ->
            allFieldValues[rowIndex][colIndex] = EMPTY_FIELD_VALUE
        }
    }
}
