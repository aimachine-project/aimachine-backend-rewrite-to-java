package ai.aimachineserver.domain.games.soccer

import kotlin.math.abs
import kotlin.math.roundToInt

class BoardSoccer {

    private companion object {
        const val BOARD_HEIGHT = 13
        const val BOARD_WIDTH = 9
    }

    val nodes: Array<Array<Node>> = Array(BOARD_HEIGHT) { rowIndex ->
        Array(BOARD_WIDTH) { colIndex -> Node(rowIndex, colIndex) }
    }

    private var currentNode: Node

    init {
        // link left side
        (0 until nodes.lastIndex).forEach { rowIndex ->
            currentNode = nodes[rowIndex][0]
            currentNode.makeLink(rowIndex + 1, 0)
        }

        // link right side
        (0 until nodes.lastIndex).forEach { rowIndex ->
            currentNode = nodes[rowIndex][BOARD_WIDTH - 1]
            currentNode.makeLink(rowIndex + 1, BOARD_WIDTH)
        }

        // link top
        (0 until BOARD_WIDTH - 1).forEach { colIndex ->
            currentNode = nodes[0][colIndex]
            currentNode.makeLink(0, colIndex + 1)
        }

        // link bottom
        (0 until BOARD_WIDTH - 1).forEach { colIndex ->
            currentNode = nodes[nodes.lastIndex][colIndex]
            currentNode.makeLink(nodes.lastIndex, colIndex + 1)
        }

        // link disabled areas top left
        (0 until 3).forEach {
            nodes[0][it].makeLink(1, 1 + it)
            nodes[1][it].makeLink(0, 1 + it)
            nodes[1][it].makeLink(1, 1 + it)
            nodes[0][1 + it].makeLink(1, 1 + it)
        }

        // link disabled areas top right
        (0 until 3).forEach {
            nodes[0][BOARD_WIDTH - 1 - it].makeLink(1, BOARD_WIDTH - 2 - it)
            nodes[1][BOARD_WIDTH - 1 - it].makeLink(0, BOARD_WIDTH - 2 - it)
            nodes[1][BOARD_WIDTH - 1 - it].makeLink(1, BOARD_WIDTH - 2 - it)
            nodes[0][BOARD_WIDTH - 2 - it].makeLink(1, BOARD_WIDTH - 2 - it)
        }

        // link disabled areas bottom left
        (0 until 3).forEach {
            nodes[BOARD_HEIGHT - 2][it].makeLink(BOARD_HEIGHT - 1, 1 + it)
            nodes[BOARD_HEIGHT - 1][it].makeLink(BOARD_HEIGHT - 2, 1 + it)
            nodes[BOARD_HEIGHT - 2][it].makeLink(BOARD_HEIGHT - 2, 1 + it)
            nodes[BOARD_HEIGHT - 2][1 + it].makeLink(BOARD_HEIGHT - 1, 1 + it)
        }

        // link disabled areas bottom right
        (0 until 3).forEach {
            nodes[BOARD_HEIGHT - 2][BOARD_WIDTH - 1 - it].makeLink(BOARD_HEIGHT - 1, BOARD_WIDTH - 2 - it)
            nodes[BOARD_HEIGHT - 1][BOARD_WIDTH - 1 - it].makeLink(BOARD_HEIGHT - 2, BOARD_WIDTH - 2 - it)
            nodes[BOARD_HEIGHT - 2][BOARD_WIDTH - 1 - it].makeLink(BOARD_HEIGHT - 2, BOARD_WIDTH - 2 - it)
            nodes[BOARD_HEIGHT - 2][BOARD_WIDTH - 2 - it].makeLink(BOARD_HEIGHT - 1, BOARD_WIDTH - 2 - it)
        }

        currentNode = nodes[BOARD_HEIGHT.div(2.0).roundToInt()][BOARD_WIDTH.div(2.0).roundToInt()]
    }

    fun getCurrentNode() = currentNode

    inner class Node(
        private val rowIndex: Int,
        private val colIndex: Int
    ) {
        private var hasLinkTop = false
        private var hasLinkTopRight = false
        private var hasLinkRight = false
        private var hasLinkDownRight = false
        private var hasLinkDown = false
        private var hasLinkDownLeft = false
        private var hasLinkLeft = false
        private var hasLinkTopLeft = false

        fun makeLink(otherNodeRowIndex: Int, otherNodeColIndex: Int) {
            val rowsDiff = otherNodeRowIndex - rowIndex
            val colsDiff = otherNodeColIndex - colIndex
            if (abs(rowsDiff) <= 1 && abs(colsDiff) <= 1) {
                val otherNode = nodes[otherNodeRowIndex][otherNodeColIndex]
                when {
                    rowsDiff == 1 && colsDiff == 0 -> {
                        if (!hasLinkTop) {
                            hasLinkTop = true
                            otherNode.hasLinkDown = true
                        }
                    }
                    rowsDiff == 1 && colsDiff == 1 -> {
                        if (!hasLinkTopRight) {
                            hasLinkTopRight = true
                            otherNode.hasLinkDownLeft = true
                        }
                    }
                    rowsDiff == 0 && colsDiff == 1 -> {
                        if (!hasLinkRight) {
                            hasLinkRight = true
                            otherNode.hasLinkLeft = true
                        }
                    }
                    rowsDiff == -1 && colsDiff == 1 -> {
                        if (!hasLinkDownRight) {
                            hasLinkDownRight = true
                            otherNode.hasLinkTopLeft = true
                        }
                    }
                    rowsDiff == -1 && colsDiff == 0 -> {
                        if (!hasLinkDown) {
                            hasLinkDown = true
                            otherNode.hasLinkTop = true
                        }
                    }
                    rowsDiff == -1 && colsDiff == -1 -> {
                        if (!hasLinkDownLeft) {
                            hasLinkDownLeft = true
                            otherNode.hasLinkTopRight = true
                        }
                    }
                    rowsDiff == 0 && colsDiff == -1 -> {
                        if (!hasLinkLeft) {
                            hasLinkLeft = true
                            otherNode.hasLinkRight = true
                        }
                    }
                    rowsDiff == 1 && colsDiff == -1 -> {
                        if (!hasLinkTopLeft) {
                            hasLinkTopLeft = true
                            otherNode.hasLinkDownRight = true
                        }
                    }
                    else -> return
                }
                currentNode = otherNode
            }
        }
    }
}
