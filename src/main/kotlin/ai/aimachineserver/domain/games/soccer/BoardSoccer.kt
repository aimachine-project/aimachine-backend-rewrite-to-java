package ai.aimachineserver.domain.games.soccer

import ai.aimachineserver.domain.games.soccer.NodeLink.*
import kotlin.math.abs
import kotlin.math.roundToInt

class BoardSoccer {
    companion object {
        const val BOARD_HEIGHT = 13
        const val BOARD_WIDTH = 9
        private const val GATE_WIDTH = 2
        const val LAST_ROW_INDEX = BOARD_HEIGHT - 1
        const val LAST_COL_INDEX = BOARD_WIDTH - 1
        val middleColIndex = (BOARD_WIDTH / 2.0).roundToInt()
        val gateHalfWidth = (GATE_WIDTH / 2.0).roundToInt()
    }

    private val nodes: Array<Array<Node>> = Array(BOARD_HEIGHT) { rowIndex ->
        Array(BOARD_WIDTH) { colIndex -> Node(rowIndex, colIndex) }
    }

    private var currentNode: Node

    init {
        // link gate rear borders
        (0 until gateHalfWidth).forEach {
            nodes[0][middleColIndex - it].makeLink(LINK_LEFT)
            nodes[0][middleColIndex + it].makeLink(LINK_RIGHT)
            nodes[0][middleColIndex - it].makeLink(LINK_LEFT)
            nodes[0][middleColIndex + it].makeLink(LINK_RIGHT)
        }
        // link gate skews
        nodes[1][middleColIndex - gateHalfWidth].makeLink(LINK_TOP_LEFT)
        nodes[1][middleColIndex + gateHalfWidth].makeLink(LINK_TOP_RIGHT)
        nodes[LAST_ROW_INDEX - 1][middleColIndex - gateHalfWidth].makeLink(LINK_TOP_LEFT)
        nodes[LAST_ROW_INDEX - 1][middleColIndex + gateHalfWidth].makeLink(LINK_TOP_RIGHT)
        // link gate side borders
        nodes[0][middleColIndex - 1].makeLink(LINK_BOTTOM)
        nodes[0][middleColIndex + 1].makeLink(LINK_BOTTOM)
        nodes[LAST_ROW_INDEX][middleColIndex - 1].makeLink(LINK_TOP)
        nodes[LAST_ROW_INDEX][middleColIndex + 1].makeLink(LINK_TOP)
        // link side borders
        (1 until LAST_ROW_INDEX).forEach {
            val leftNode = nodes[it][0]
            leftNode.makeLink(LINK_TOP)
            leftNode.makeLink(LINK_TOP_LEFT)
            leftNode.makeLink(LINK_LEFT)
            leftNode.makeLink(LINK_BOTTOM_LEFT)
            leftNode.makeLink(LINK_BOTTOM)

            val rightNode = nodes[it][LAST_COL_INDEX]
            rightNode.makeLink(LINK_TOP)
            rightNode.makeLink(LINK_TOP_RIGHT)
            rightNode.makeLink(LINK_RIGHT)
            rightNode.makeLink(LINK_BOTTOM_RIGHT)
            rightNode.makeLink(LINK_BOTTOM)
        }
        // link top borders
        (1 until middleColIndex - 2).forEach {
            val topLeftNode = nodes[1][it]
            topLeftNode.makeLink(LINK_LEFT)
            topLeftNode.makeLink(LINK_TOP_LEFT)
            topLeftNode.makeLink(LINK_TOP)
            topLeftNode.makeLink(LINK_TOP_RIGHT)
            topLeftNode.makeLink(LINK_RIGHT)

            val topRightNode = nodes[1][LAST_COL_INDEX - it]
            topRightNode.makeLink(LINK_RIGHT)
            topRightNode.makeLink(LINK_TOP_RIGHT)
            topRightNode.makeLink(LINK_TOP)
            topRightNode.makeLink(LINK_TOP_LEFT)
            topRightNode.makeLink(LINK_LEFT)

            val bottomLeftNode = nodes[LAST_ROW_INDEX - 1][it]
            bottomLeftNode.makeLink(LINK_LEFT)
            bottomLeftNode.makeLink(LINK_BOTTOM_LEFT)
            bottomLeftNode.makeLink(LINK_BOTTOM)
            bottomLeftNode.makeLink(LINK_BOTTOM_RIGHT)
            bottomLeftNode.makeLink(LINK_RIGHT)

            val bottomRightNode = nodes[LAST_ROW_INDEX - 1][LAST_COL_INDEX - it]
            bottomRightNode.makeLink(LINK_RIGHT)
            bottomRightNode.makeLink(LINK_BOTTOM_RIGHT)
            bottomRightNode.makeLink(LINK_BOTTOM)
            bottomRightNode.makeLink(LINK_BOTTOM_LEFT)
            bottomRightNode.makeLink(LINK_LEFT)
        }

        currentNode = nodes[(BOARD_HEIGHT / 2.0).roundToInt()][middleColIndex]
    }

    fun getCurrentNode() = currentNode

    fun isFieldAvailable(otherNodeRowIndex: Int, otherNodeColIndex: Int) =
        if (isNodeInNearestNeighbourHood(otherNodeRowIndex, otherNodeColIndex)) {
            nodes[otherNodeRowIndex][otherNodeColIndex].hasAnyFreeLink()
        } else {
            false
        }

    private fun isNodeInNearestNeighbourHood(otherNodeRowIndex: Int, otherNodeColIndex: Int): Boolean {
        val rowsAbsDiff = abs(otherNodeRowIndex - currentNode.rowIndex)
        val colsAbsDiff = abs(otherNodeColIndex - currentNode.colIndex)
        return rowsAbsDiff <= 1 && colsAbsDiff <= 1 && rowsAbsDiff + colsAbsDiff != 0
    }

    fun makeLink(link: NodeLink) {
        if (!currentNode.hasLink(link)) {
            currentNode.makeLink(link)
        }
    }

    inner class Node(
        val rowIndex: Int,
        val colIndex: Int
    ) {
        private val maxLinksCount = NodeLink.values().count()
        private val links = mutableSetOf<NodeLink>()

        fun hasLink(link: NodeLink) = links.contains(link)
        fun hasAnyFreeLink() = links.count() < maxLinksCount
        fun hasMoreThanOneLink() = links.count() > 1
        fun hasOnlyOneLink() = links.count() == 1

        fun makeLink(link: NodeLink) {
            when (link) {
                LINK_TOP -> {
                    links.add(LINK_TOP)
                    currentNode = nodes[currentNode.rowIndex - 1][currentNode.colIndex]
                    currentNode.links.add(LINK_BOTTOM)
                }
                LINK_TOP_RIGHT -> {
                    links.add(LINK_TOP_RIGHT)
                    currentNode = nodes[currentNode.rowIndex - 1][currentNode.colIndex + 1]
                    currentNode.links.add(LINK_BOTTOM_LEFT)
                }
                LINK_RIGHT -> {
                    links.add(LINK_RIGHT)
                    currentNode = nodes[currentNode.rowIndex][currentNode.colIndex + 1]
                    currentNode.links.add(LINK_LEFT)
                }
                LINK_BOTTOM_RIGHT -> {
                    links.add(LINK_BOTTOM_RIGHT)
                    currentNode = nodes[currentNode.rowIndex + 1][currentNode.colIndex + 1]
                    currentNode.links.add(LINK_TOP_LEFT)
                }
                LINK_BOTTOM -> {
                    links.add(LINK_BOTTOM)
                    currentNode = nodes[currentNode.rowIndex + 1][currentNode.colIndex]
                    currentNode.links.add(LINK_TOP)
                }
                LINK_BOTTOM_LEFT -> {
                    links.add(LINK_BOTTOM_LEFT)
                    currentNode = nodes[currentNode.rowIndex + 1][currentNode.colIndex - 1]
                    currentNode.links.add(LINK_TOP_RIGHT)
                }
                LINK_LEFT -> {
                    links.add(LINK_LEFT)
                    currentNode = nodes[currentNode.rowIndex][currentNode.colIndex - 1]
                    currentNode.links.add(LINK_RIGHT)
                }
                LINK_TOP_LEFT -> {
                    links.add(LINK_TOP_LEFT)
                    currentNode = nodes[currentNode.rowIndex - 1][currentNode.colIndex - 1]
                    currentNode.links.add(LINK_BOTTOM_RIGHT)
                }
            }
        }
    }
}
