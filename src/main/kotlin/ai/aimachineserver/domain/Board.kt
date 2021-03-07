package ai.aimachineserver.domain

class Board {
    private companion object {
        const val BOARD_SIZE = 3
        const val BLANK_VALUE = 0
    }

    private val allFieldValues: Array<IntArray> = Array(BOARD_SIZE) { IntArray(BOARD_SIZE) { BLANK_VALUE } }

    fun getAllFieldValues() = allFieldValues

    fun setFieldValue(rowIndex: Int, colIndex: Int, fieldValue: Int) {
        allFieldValues[rowIndex][colIndex] = fieldValue
    }

    fun getAvailableFieldIndices(): List<Pair<Int, Int>> {
        val availableFieldIndices = mutableListOf<Pair<Int, Int>>()
        allFieldValues.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, fieldValue ->
                if (fieldValue == BLANK_VALUE) {
                    availableFieldIndices.add(Pair(rowIndex, colIndex))
                }
            }
        }
        return availableFieldIndices
    }

    fun isFieldAvailable(rowIndex: Int, colIndex: Int) = allFieldValues[rowIndex][colIndex] == BLANK_VALUE

    fun clearAllFields() = allFieldValues.forEachIndexed { rowIndex, row ->
        row.indices.forEach { colIndex ->
            allFieldValues[rowIndex][colIndex] = BLANK_VALUE
        }
    }
}
