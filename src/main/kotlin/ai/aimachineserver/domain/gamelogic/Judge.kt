package ai.aimachineserver.domain.gamelogic

import kotlin.math.absoluteValue

class Judge(
    private val sameValuesCountWinningCondition: Int = 3,
    boardSize: Int = 3
) {
    private val minTurnsCount = 2 * sameValuesCountWinningCondition - 1
    private val maxTurnsCount = boardSize * boardSize

    fun announceTurnResult(board: Board, turnNumber: Int): TurnResult {
        val fieldValues = board.getAllFieldValues()
        return when {
            turnNumber < minTurnsCount -> TurnResult.GAME_ONGOING
            isWinningConditionMet(fieldValues) -> TurnResult.WIN
            turnNumber >= maxTurnsCount -> TurnResult.TIE
            else -> TurnResult.GAME_ONGOING
        }
    }

    private fun isWinningConditionMet(fieldValues: Array<IntArray>) = when {
        enoughValuesOrthogonally(fieldValues) -> true
        enoughValuesDiagonally(fieldValues) -> true
        else -> false
    }

    private fun enoughValuesOrthogonally(fieldValues: Array<IntArray>) = when {
        enoughAdjacentValuesInAnyRow(fieldValues) -> true
        enoughValuesInAnyColumn(fieldValues) -> true
        else -> false
    }

    private fun enoughAdjacentValuesInAnyRow(fieldValues: Array<IntArray>) = fieldValues.any { colValues ->
        (0 until sameValuesCountWinningCondition).sumOf { colValues[it] }.absoluteValue == sameValuesCountWinningCondition
    }

    private fun enoughValuesInAnyColumn(fieldValues: Array<IntArray>): Boolean {
        val fieldValuesTransposed = transposeArray(fieldValues)
        return enoughAdjacentValuesInAnyRow(fieldValuesTransposed)
    }

    private fun transposeArray(inputArray: Array<IntArray>): Array<IntArray> {
        val outputArray = Array(inputArray.first().size) { IntArray(inputArray.size) }
        for (i in 0..inputArray.lastIndex) {
            for (j in 0..inputArray.first().lastIndex) {
                outputArray[i][j] = inputArray[j][i]
            }
        }
        return outputArray
    }

    private fun enoughValuesDiagonally(fieldValues: Array<IntArray>) = when {
        enoughValuesOnDiagonals(fieldValues) -> true
        enoughValuesOnAntiDiagonals(fieldValues) -> true
        else -> false
    }

    private fun enoughValuesOnDiagonals(fieldValues: Array<IntArray>): Boolean {
        for (i in 0..(fieldValues.size - sameValuesCountWinningCondition)) {
            for (j in 0..(fieldValues.size - sameValuesCountWinningCondition)) {
                if ((0 until sameValuesCountWinningCondition).sumOf {
                        fieldValues[i + it][j + it]
                    }.absoluteValue == sameValuesCountWinningCondition) {
                    return true
                }
            }
        }
        return false
    }

    private fun enoughValuesOnAntiDiagonals(fieldValues: Array<IntArray>): Boolean {
        for (i in (sameValuesCountWinningCondition - 1)..(fieldValues.lastIndex)) {
            for (j in (sameValuesCountWinningCondition - 1)..(fieldValues.lastIndex)) {
                if ((0 until sameValuesCountWinningCondition).sumOf {
                        fieldValues[sameValuesCountWinningCondition - 1 - it][j - (sameValuesCountWinningCondition - 1) + it]
                    }.absoluteValue == sameValuesCountWinningCondition) {
                    return true
                }
            }
        }
        return false
    }
}
