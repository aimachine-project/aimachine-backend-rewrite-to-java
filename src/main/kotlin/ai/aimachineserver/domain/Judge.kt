package ai.aimachineserver.domain

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
            haveSameValuesOrthogonally(fieldValues) || haveSameValuesDiagonally(fieldValues) -> TurnResult.WIN
            turnNumber >= MAX_TURNS_COUNT -> TurnResult.TIE
            else -> TurnResult.GAME_ONGOING
        }
    }

    private fun haveSameValuesOrthogonally(fieldValues: Array<IntArray>) = when {
        fieldValues.any { abs(it.sum()) == Board.SIZE } -> true
        (fieldValues.first().indices).any { colIndex ->
            abs(fieldValues.sumBy { rowValues -> rowValues[colIndex] }) == Board.SIZE
        } -> true
        else -> false
    }

    private fun haveSameValuesDiagonally(fieldValues: Array<IntArray>) = when {
        abs(
            (fieldValues.indices).sumOf { rowIndex ->
                fieldValues[rowIndex][rowIndex]
            }
        ) == Board.SIZE -> true
        abs(
            (fieldValues.indices).sumOf { rowIndex ->
                fieldValues[fieldValues.lastIndex - rowIndex][rowIndex]
            }
        ) == Board.SIZE -> true
        else -> false
    }
}
