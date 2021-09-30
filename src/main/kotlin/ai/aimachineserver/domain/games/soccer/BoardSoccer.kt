package ai.aimachineserver.domain.games.soccer

import ai.aimachineserver.domain.games.soccer.NodeLink.*
import kotlin.math.abs
import kotlin.math.roundToInt

class BoardSoccer {
    companion object {
        const val BOARD_HEIGHT = 12
        const val BOARD_WIDTH = 10
        const val GATE_WIDTH = 2
        val middleRowIndex = (BOARD_HEIGHT / 2.0).roundToInt()
        val middleColIndex = (BOARD_WIDTH / 2.0).roundToInt()
        val gateHalfWidth = (GATE_WIDTH / 2.0).roundToInt()
    }

    private val nodes: Array<Array<Node>> = Array(BOARD_HEIGHT + 1) { rowIndex ->
        Array(BOARD_WIDTH + 1) { colIndex -> Node(rowIndex, colIndex) }
    }

    private var currentNode = nodes[middleRowIndex][middleColIndex]

    init {
        // link gate rear borders
        (0 until gateHalfWidth).forEach {
            nodes[0][middleColIndex - it].makeLink(LINK_LEFT)
            nodes[0][middleColIndex + it].makeLink(LINK_RIGHT)
            nodes[BOARD_HEIGHT][middleColIndex - it].makeLink(LINK_LEFT)
            nodes[BOARD_HEIGHT][middleColIndex + it].makeLink(LINK_RIGHT)
        }
        // link gate skews
        nodes[1][middleColIndex - gateHalfWidth].makeLink(LINK_TOP_LEFT)
        nodes[1][middleColIndex + gateHalfWidth].makeLink(LINK_TOP_RIGHT)
        nodes[BOARD_HEIGHT - 1][middleColIndex - gateHalfWidth].makeLink(LINK_TOP_LEFT)
        nodes[BOARD_HEIGHT - 1][middleColIndex + gateHalfWidth].makeLink(LINK_TOP_RIGHT)
        // link gate side borders
        nodes[0][middleColIndex - 1].makeLink(LINK_BOTTOM)
        nodes[0][middleColIndex + 1].makeLink(LINK_BOTTOM)
        nodes[BOARD_HEIGHT][middleColIndex - 1].makeLink(LINK_TOP)
        nodes[BOARD_HEIGHT][middleColIndex + 1].makeLink(LINK_TOP)
        // link vertical borders
        (1 until BOARD_HEIGHT).forEach {
            val leftBorderNode = nodes[it][1]
            leftBorderNode.makeLink(LINK_TOP)
            leftBorderNode.makeLink(LINK_TOP_LEFT)
            leftBorderNode.makeLink(LINK_LEFT)
            leftBorderNode.makeLink(LINK_BOTTOM_LEFT)
            leftBorderNode.makeLink(LINK_BOTTOM)

            val rightBorderNode = nodes[it][BOARD_WIDTH - 1]
            rightBorderNode.makeLink(LINK_TOP)
            rightBorderNode.makeLink(LINK_TOP_RIGHT)
            rightBorderNode.makeLink(LINK_RIGHT)
            rightBorderNode.makeLink(LINK_BOTTOM_RIGHT)
            rightBorderNode.makeLink(LINK_BOTTOM)
        }
        // link horizontal borders beside gates
        (1 until (middleColIndex - gateHalfWidth)).forEach {
            val topLeftBorderNode = nodes[1][it]
            topLeftBorderNode.makeLink(LINK_LEFT)
            topLeftBorderNode.makeLink(LINK_TOP_LEFT)
            topLeftBorderNode.makeLink(LINK_TOP)
            topLeftBorderNode.makeLink(LINK_TOP_RIGHT)
            topLeftBorderNode.makeLink(LINK_RIGHT)

            val topRightBorderNode = nodes[1][BOARD_WIDTH - it]
            topRightBorderNode.makeLink(LINK_RIGHT)
            topRightBorderNode.makeLink(LINK_TOP_RIGHT)
            topRightBorderNode.makeLink(LINK_TOP)
            topRightBorderNode.makeLink(LINK_TOP_LEFT)
            topRightBorderNode.makeLink(LINK_LEFT)

            val bottomLeftNode = nodes[BOARD_HEIGHT - 1][it]
            bottomLeftNode.makeLink(LINK_LEFT)
            bottomLeftNode.makeLink(LINK_BOTTOM_LEFT)
            bottomLeftNode.makeLink(LINK_BOTTOM)
            bottomLeftNode.makeLink(LINK_BOTTOM_RIGHT)
            bottomLeftNode.makeLink(LINK_RIGHT)

            val bottomRightNode = nodes[BOARD_HEIGHT - 1][BOARD_WIDTH - it]
            bottomRightNode.makeLink(LINK_RIGHT)
            bottomRightNode.makeLink(LINK_BOTTOM_RIGHT)
            bottomRightNode.makeLink(LINK_BOTTOM)
            bottomRightNode.makeLink(LINK_BOTTOM_LEFT)
            bottomRightNode.makeLink(LINK_LEFT)
        }

        currentNode = nodes[middleRowIndex][middleColIndex]
    }

    fun getCurrentNode() = currentNode

    fun isFieldAvailable(otherNodeRowIndex: Int, otherNodeColIndex: Int) =
        if (
            isNotBorderNode(otherNodeRowIndex, otherNodeColIndex) &&
            isNodeInNearestNeighbourhood(otherNodeRowIndex, otherNodeColIndex) &&
                doesNotHaveLinkAlready(otherNodeRowIndex, otherNodeColIndex)
        ) {
            nodes[otherNodeRowIndex][otherNodeColIndex].hasAnyFreeLink()
        } else {
            false
        }

    private fun doesNotHaveLinkAlready(otherNodeRowIndex: Int, otherNodeColIndex: Int): Boolean {
        val node = currentNode
        val rowsDiff = otherNodeRowIndex - node.rowIndex
        val colsDiff = otherNodeColIndex - node.colIndex
        return !when {
            rowsDiff == -1 && colsDiff == 0 -> {
                node.hasLink(LINK_TOP)
            }
            rowsDiff == -1 && colsDiff == 1 -> {
                node.hasLink(LINK_TOP_RIGHT)
            }
            rowsDiff == 0 && colsDiff == 1 -> {
                node.hasLink(LINK_RIGHT)
            }
            rowsDiff == 1 && colsDiff == 1 -> {
                node.hasLink(LINK_BOTTOM_RIGHT)
            }
            rowsDiff == 1 && colsDiff == 0 -> {
                node.hasLink(LINK_BOTTOM)
            }
            rowsDiff == 1 && colsDiff == -1 -> {
                node.hasLink(LINK_BOTTOM_LEFT)
            }
            rowsDiff == 0 && colsDiff == -1 -> {
                node.hasLink(LINK_LEFT)
            }
            rowsDiff == -1 && colsDiff == -1 -> {
                node.hasLink(LINK_TOP_LEFT)
            }
            else -> false
        }
    }

    private fun isNotBorderNode(otherNodeRowIndex: Int, otherNodeColIndex: Int): Boolean {
        val isNotMinMaxCol = otherNodeColIndex != 0 && otherNodeColIndex != BOARD_WIDTH
        val isNotInGate =
            (otherNodeRowIndex != 0 && otherNodeRowIndex != BOARD_HEIGHT) || abs(otherNodeColIndex - middleColIndex) <= gateHalfWidth
        return isNotMinMaxCol && isNotInGate
    }

    private fun isNodeInNearestNeighbourhood(otherNodeRowIndex: Int, otherNodeColIndex: Int): Boolean {
        val rowsAbsDiff = abs(otherNodeRowIndex - currentNode.rowIndex)
        val colsAbsDiff = abs(otherNodeColIndex - currentNode.colIndex)
        return rowsAbsDiff <= 1 && colsAbsDiff <= 1 && rowsAbsDiff + colsAbsDiff != 0
    }

    fun makeLink(link: NodeLink) {
        if (!currentNode.hasLink(link)) {
            currentNode = currentNode.makeLink(link)
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

        fun makeLink(link: NodeLink): Node = try {
            when (link) {
                LINK_TOP -> {
                    links.add(LINK_TOP)
                    val linkedNode = nodes[rowIndex - 1][colIndex]
                    linkedNode.apply { links.add(LINK_BOTTOM) }
                }
                LINK_TOP_RIGHT -> {
                    links.add(LINK_TOP_RIGHT)
                    val linkedNode = nodes[rowIndex - 1][colIndex + 1]
                    linkedNode.apply { links.add(LINK_BOTTOM_LEFT) }
                }
                LINK_RIGHT -> {
                    links.add(LINK_RIGHT)
                    val linkedNode = nodes[rowIndex][colIndex + 1]
                    linkedNode.apply { links.add(LINK_LEFT) }
                }
                LINK_BOTTOM_RIGHT -> {
                    links.add(LINK_BOTTOM_RIGHT)
                    val linkedNode = nodes[rowIndex + 1][colIndex + 1]
                    linkedNode.apply { links.add(LINK_TOP_LEFT) }
                }
                LINK_BOTTOM -> {
                    links.add(LINK_BOTTOM)
                    val linkedNode = nodes[rowIndex + 1][colIndex]
                    linkedNode.apply { links.add(LINK_TOP) }
                }
                LINK_BOTTOM_LEFT -> {
                    links.add(LINK_BOTTOM_LEFT)
                    val linkedNode = nodes[rowIndex + 1][colIndex - 1]
                    linkedNode.apply { links.add(LINK_TOP_RIGHT) }
                }
                LINK_LEFT -> {
                    links.add(LINK_LEFT)
                    val linkedNode = nodes[rowIndex][colIndex - 1]
                    linkedNode.apply { links.add(LINK_RIGHT) }
                }
                LINK_TOP_LEFT -> {
                    links.add(LINK_TOP_LEFT)
                    val linkedNode = nodes[rowIndex - 1][colIndex - 1]
                    linkedNode.apply { links.add(LINK_BOTTOM_RIGHT) }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            print("Board node index out of bounds. Current node is [$rowIndex, $colIndex]")
            currentNode
        }
    }
}
