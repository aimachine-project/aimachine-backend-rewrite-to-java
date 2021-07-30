package ai.aimachineserver.domain.gamelogic

class Board(private val allFieldValues: Array<IntArray> = Array(SIZE) { IntArray(SIZE) { BLANK_VALUE } }) {

    companion object {
        const val SIZE = 3
        private const val BLANK_VALUE = 0
    }

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
