package ai.aimachineserver.domain.gamelogic

import kotlin.math.abs

class Judge private constructor(
    private val minTurnsCount: Int,
    private val maxTurnsCount: Int,
    private val sameValuesCountWinningCondition: Int
) {
    companion object {
        fun makeJudgeForClassicGame() = Judge(5, 9, 3)
        fun makeJudgeFor14Fields() = Judge(9, 196, 5)
    }

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

    private fun enoughAdjacentValuesInAnyRow(fieldValues: Array<IntArray>) =
        fieldValues.any { row ->
            row.mapIndexed { colIndex, _ ->
                if (colIndex < row.size - sameValuesCountWinningCondition) {
                    abs(
                        row.copyOfRange(colIndex, colIndex + sameValuesCountWinningCondition - 1).sum()
                    ) == sameValuesCountWinningCondition
                } else {
                    false
                }
            }.any { it }
        }

    private fun enoughValuesInAnyColumn(fieldValues: Array<IntArray>) =
        (fieldValues.first().indices).any { colIndex ->
            abs(fieldValues.sumOf { rowValues -> rowValues[colIndex] }) == Board.DEFAULT_SIZE
        }

    private fun enoughValuesDiagonally(fieldValues: Array<IntArray>) = when {
        enoughValuesMainDiagonals(fieldValues) -> true
        enoughValuesAntiDiagonals(fieldValues) -> true
        else -> false
    }

    private fun enoughValuesMainDiagonals(fieldValues: Array<IntArray>) =
        abs((fieldValues.indices).sumOf { rowIndex -> fieldValues[rowIndex][rowIndex] }) == Board.DEFAULT_SIZE

    private fun enoughValuesAntiDiagonals(fieldValues: Array<IntArray>) =
        abs(
            (fieldValues.indices).sumOf { rowIndex ->
                fieldValues[fieldValues.lastIndex - rowIndex][rowIndex]
            }
        ) == Board.DEFAULT_SIZE
}
