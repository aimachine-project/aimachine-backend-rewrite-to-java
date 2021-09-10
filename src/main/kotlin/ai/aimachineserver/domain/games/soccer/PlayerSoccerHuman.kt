package ai.aimachineserver.domain.games.soccer

import ai.aimachineserver.domain.games.soccer.NodeLink.*

class PlayerSoccerHuman(name: String) : PlayerSoccer(name) {
    override fun makeMove(board: BoardSoccer, rowIndex: Int, colIndex: Int) {
        val currentNode = board.getCurrentNode()
        val rowsDiff = rowIndex - currentNode.rowIndex
        val colsDiff = colIndex - currentNode.colIndex
        when {
            rowsDiff == -1 && colsDiff == 0 -> {
                board.makeLink(LINK_TOP)
            }
            rowsDiff == -1 && colsDiff == 1 -> {
                board.makeLink(LINK_TOP_RIGHT)
            }
            rowsDiff == 0 && colsDiff == 1 -> {
                board.makeLink(LINK_RIGHT)
            }
            rowsDiff == 1 && colsDiff == 1 -> {
                board.makeLink(LINK_BOTTOM_RIGHT)
            }
            rowsDiff == 1 && colsDiff == 0 -> {
                board.makeLink(LINK_BOTTOM)
            }
            rowsDiff == 1 && colsDiff == -1 -> {
                board.makeLink(LINK_BOTTOM_LEFT)
            }
            rowsDiff == 0 && colsDiff == -1 -> {
                board.makeLink(LINK_LEFT)
            }
            rowsDiff == 1 && colsDiff == -1 -> {
                board.makeLink(LINK_TOP_LEFT)
            }
        }
    }
}
