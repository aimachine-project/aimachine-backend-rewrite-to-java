package ai.aimachineserver.domain.games.soccer

class JudgeSoccer(private val board: BoardSoccer) {

    private val winningNodes = listOf(
        0 to BoardSoccer.middleColIndex - 1,
        0 to BoardSoccer.middleColIndex,
        0 to BoardSoccer.middleColIndex + 1,
        BoardSoccer.BOARD_HEIGHT to BoardSoccer.middleColIndex - 1,
        BoardSoccer.BOARD_HEIGHT to BoardSoccer.middleColIndex,
        BoardSoccer.BOARD_HEIGHT to BoardSoccer.middleColIndex + 1,
    )

    fun announceTurnResult(): TurnResultSoccer {
        val currentNode = board.getCurrentNode()
        return when {
            winningNodes.contains(currentNode.rowIndex to currentNode.colIndex) -> TurnResultSoccer.WIN
            currentNode.hasAnyFreeLink().not() -> TurnResultSoccer.LOSE
            currentNode.hasMoreThanOneLink() -> TurnResultSoccer.TURN_ONGOING
            currentNode.hasOnlyOneLink() -> TurnResultSoccer.TURN_OVER
            else -> TurnResultSoccer.TURN_ONGOING
        }
    }
}
