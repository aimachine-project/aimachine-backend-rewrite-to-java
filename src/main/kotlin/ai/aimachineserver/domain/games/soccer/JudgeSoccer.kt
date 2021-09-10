package ai.aimachineserver.domain.games.soccer

class JudgeSoccer(private val board: BoardSoccer) {

    private val winningNodes = listOf(
        0 to BoardSoccer.middleColIndex - 1,
        0 to BoardSoccer.middleColIndex,
        0 to BoardSoccer.middleColIndex + 1,
        BoardSoccer.LAST_ROW_INDEX to BoardSoccer.middleColIndex - 1,
        BoardSoccer.LAST_ROW_INDEX to BoardSoccer.middleColIndex,
        BoardSoccer.LAST_ROW_INDEX to BoardSoccer.middleColIndex + 1,
    )

    fun announceTurnResult(): TurnResultSoccer {
        val currentNode = board.getCurrentNode()
        return when {
            currentNode.hasAnyFreeLink().not() -> TurnResultSoccer.LOSE
            currentNode.hasMoreThanOneLink() -> TurnResultSoccer.TURN_ONGOING
            currentNode.hasOnlyOneLink() -> TurnResultSoccer.TURN_OVER
            winningNodes.contains(currentNode.rowIndex to currentNode.colIndex) -> TurnResultSoccer.WIN
            else -> TurnResultSoccer.TURN_ONGOING
        }
    }
}
