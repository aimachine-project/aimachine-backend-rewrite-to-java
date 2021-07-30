package ai.aimachineserver.domain.gamelogic

import kotlin.math.abs

class Judge {
    private companion object {
        const val MIN_TURNS_COUNT = 5
        const val MAX_TURNS_COUNT = 9
    }

    fun announceTurnResult(board: Board, turnNumber: Int): TurnResult {
        val fieldValues = board.getAllFieldValues()
        return when {
            turnNumber < MIN_TURNS_COUNT -> TurnResult.GAME_ONGOING
            isWinningConditionMet(fieldValues) -> TurnResult.WIN
            turnNumber >= MAX_TURNS_COUNT -> TurnResult.TIE
            else -> TurnResult.GAME_ONGOING
        }
    }

    private fun isWinningConditionMet(fieldValues: Array<IntArray>) = when {
        areSameValuesOrthogonally(fieldValues) -> true
        areSameValuesDiagonally(fieldValues) -> true
        else -> false
    }

    private fun areSameValuesOrthogonally(fieldValues: Array<IntArray>) = when {
        areSameValuesInAnyRow(fieldValues) -> true
        areSameValuesInAnyColumn(fieldValues) -> true
        else -> false
    }

    private fun areSameValuesInAnyRow(fieldValues: Array<IntArray>) = fieldValues.any { abs(it.sum()) == Board.SIZE }

    private fun areSameValuesInAnyColumn(fieldValues: Array<IntArray>) =
        (fieldValues.first().indices).any { colIndex ->
            abs(fieldValues.sumOf { rowValues -> rowValues[colIndex] }) == Board.SIZE
        }

    private fun areSameValuesDiagonally(fieldValues: Array<IntArray>) = when {
        areSameValuesMainDiagonal(fieldValues) -> true
        areSameValuesAntiDiagonal(fieldValues) -> true
        else -> false
    }

    private fun areSameValuesMainDiagonal(fieldValues: Array<IntArray>) =
        abs((fieldValues.indices).sumOf { rowIndex -> fieldValues[rowIndex][rowIndex] }) == Board.SIZE

    private fun areSameValuesAntiDiagonal(fieldValues: Array<IntArray>) =
        abs(
            (fieldValues.indices).sumOf { rowIndex ->
                fieldValues[fieldValues.lastIndex - rowIndex][rowIndex]
            }
        ) == Board.SIZE
}
