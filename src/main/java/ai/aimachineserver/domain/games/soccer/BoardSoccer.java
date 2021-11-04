package ai.aimachineserver.domain.games.soccer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static ai.aimachineserver.domain.games.soccer.NodeLink.*;

public class BoardSoccer {
    static final int BOARD_HEIGHT = 12;
    static final int BOARD_WIDTH = 10;
    static final int GATE_WIDTH = 2;
    static final int middleRowIndex = Math.round(BOARD_HEIGHT / 2f);
    static final int middleColIndex = Math.round(BOARD_WIDTH / 2f);
    static final int gateHalfWidth = Math.round(GATE_WIDTH / 2f);

    private final Node[][] nodes = new Node[BOARD_HEIGHT + 1][BOARD_WIDTH + 1];
    private Node currentNode;

    {
        for (int rowIndex = 0; rowIndex <= BOARD_HEIGHT; rowIndex++) {
            for (int colIndex = 0; colIndex <= BOARD_WIDTH; colIndex++) {
                nodes[rowIndex][colIndex] = new Node(rowIndex, colIndex);
            }
        }

        currentNode = nodes[middleRowIndex][middleColIndex];

        // link gate rear borders
        IntStream.range(0, gateHalfWidth).forEach(it -> {
            nodes[0][middleColIndex - it].makeLink(LINK_LEFT);
            nodes[0][middleColIndex + it].makeLink(LINK_RIGHT);
            nodes[BOARD_HEIGHT][middleColIndex - it].makeLink(LINK_LEFT);
            nodes[BOARD_HEIGHT][middleColIndex + it].makeLink(LINK_RIGHT);
        });
        // link gate skews
        nodes[1][middleColIndex - gateHalfWidth].makeLink(LINK_TOP_LEFT);
        nodes[1][middleColIndex + gateHalfWidth].makeLink(LINK_TOP_RIGHT);
        nodes[BOARD_HEIGHT - 1][middleColIndex - gateHalfWidth].makeLink(LINK_BOTTOM_LEFT);
        nodes[BOARD_HEIGHT - 1][middleColIndex + gateHalfWidth].makeLink(LINK_BOTTOM_RIGHT);
        // link gate side borders
        nodes[0][middleColIndex - 1].makeLink(LINK_BOTTOM);
        nodes[0][middleColIndex + 1].makeLink(LINK_BOTTOM);
        nodes[BOARD_HEIGHT][middleColIndex - 1].makeLink(LINK_TOP);
        nodes[BOARD_HEIGHT][middleColIndex + 1].makeLink(LINK_TOP);
        // link vertical borders
        IntStream.range(1, BOARD_HEIGHT).forEach(it -> {
            Node leftBorderNode = nodes[it][1];
            leftBorderNode.makeLink(LINK_TOP);
            leftBorderNode.makeLink(LINK_TOP_LEFT);
            leftBorderNode.makeLink(LINK_LEFT);
            leftBorderNode.makeLink(LINK_BOTTOM_LEFT);
            leftBorderNode.makeLink(LINK_BOTTOM);

            Node rightBorderNode = nodes[it][BOARD_WIDTH - 1];
            rightBorderNode.makeLink(LINK_TOP);
            rightBorderNode.makeLink(LINK_TOP_RIGHT);
            rightBorderNode.makeLink(LINK_RIGHT);
            rightBorderNode.makeLink(LINK_BOTTOM_RIGHT);
            rightBorderNode.makeLink(LINK_BOTTOM);
        });
        // link horizontal borders beside gates
        IntStream.range(1, middleColIndex - gateHalfWidth).forEach(it -> {
            Node topLeftBorderNode = nodes[1][it];
            topLeftBorderNode.makeLink(LINK_LEFT);
            topLeftBorderNode.makeLink(LINK_TOP_LEFT);
            topLeftBorderNode.makeLink(LINK_TOP);
            topLeftBorderNode.makeLink(LINK_TOP_RIGHT);
            topLeftBorderNode.makeLink(LINK_RIGHT);

            Node topRightBorderNode = nodes[1][BOARD_WIDTH - it];
            topRightBorderNode.makeLink(LINK_RIGHT);
            topRightBorderNode.makeLink(LINK_TOP_RIGHT);
            topRightBorderNode.makeLink(LINK_TOP);
            topRightBorderNode.makeLink(LINK_TOP_LEFT);
            topRightBorderNode.makeLink(LINK_LEFT);

            Node bottomLeftNode = nodes[BOARD_HEIGHT - 1][it];
            bottomLeftNode.makeLink(LINK_LEFT);
            bottomLeftNode.makeLink(LINK_BOTTOM_LEFT);
            bottomLeftNode.makeLink(LINK_BOTTOM);
            bottomLeftNode.makeLink(LINK_BOTTOM_RIGHT);
            bottomLeftNode.makeLink(LINK_RIGHT);

            Node bottomRightNode = nodes[BOARD_HEIGHT - 1][BOARD_WIDTH - it];
            bottomRightNode.makeLink(LINK_RIGHT);
            bottomRightNode.makeLink(LINK_BOTTOM_RIGHT);
            bottomRightNode.makeLink(LINK_BOTTOM);
            bottomRightNode.makeLink(LINK_BOTTOM_LEFT);
            bottomRightNode.makeLink(LINK_LEFT);

            currentNode = nodes[middleRowIndex][middleColIndex];
        });
    }

    Node getCurrentNode() {
        return currentNode;
    }

    boolean isFieldAvailable(int otherNodeRowIndex, int otherNodeColIndex) {
        if (isNotBorderNode(otherNodeRowIndex, otherNodeColIndex)
                && isNodeInNearestNeighbourhood(otherNodeRowIndex, otherNodeColIndex)
                && doesNotHaveLinkAlready(otherNodeRowIndex, otherNodeColIndex)) {
            return nodes[otherNodeRowIndex][otherNodeColIndex].hasAnyFreeLink();
        } else {
            return false;
        }
    }

    private boolean doesNotHaveLinkAlready(int otherNodeRowIndex, int otherNodeColIndex) {
        Node node = currentNode;
        int rowsDiff = otherNodeRowIndex - node.rowIndex;
        int colsDiff = otherNodeColIndex - node.colIndex;
        if (rowsDiff == -1 && colsDiff == 0)
            return node.hasLink(LINK_TOP);
        else if (rowsDiff == -1 && colsDiff == 1)
            return !node.hasLink(LINK_TOP_RIGHT);
        else if (rowsDiff == 0 && colsDiff == 1)
            return !node.hasLink(LINK_RIGHT);
        else if (rowsDiff == 1 && colsDiff == 1)
            return !node.hasLink(LINK_BOTTOM_RIGHT);
        else if (rowsDiff == 1 && colsDiff == 0)
            return !node.hasLink(LINK_BOTTOM);
        else if (rowsDiff == 1 && colsDiff == -1)
            return !node.hasLink(LINK_BOTTOM_LEFT);
        else if (rowsDiff == 0 && colsDiff == -1)
            return !node.hasLink(LINK_LEFT);
        else if (rowsDiff == -1 && colsDiff == -1)
            return !node.hasLink(LINK_TOP_LEFT);
        else
            return true;
    }

    private boolean isNotBorderNode(int otherNodeRowIndex, int otherNodeColIndex) {
        boolean isNotMinMaxCol = otherNodeColIndex != 0 && otherNodeColIndex != BOARD_WIDTH;
        boolean isNotInGate =
                (otherNodeRowIndex != 0 && otherNodeRowIndex != BOARD_HEIGHT) || Math.abs(otherNodeColIndex - middleColIndex) <= gateHalfWidth;
        return isNotMinMaxCol && isNotInGate;
    }

    private boolean isNodeInNearestNeighbourhood(int otherNodeRowIndex, int otherNodeColIndex) {
        int rowsAbsDiff = Math.abs(otherNodeRowIndex - currentNode.rowIndex);
        int colsAbsDiff = Math.abs(otherNodeColIndex - currentNode.colIndex);
        return rowsAbsDiff <= 1 && colsAbsDiff <= 1 && rowsAbsDiff + colsAbsDiff != 0;
    }

    void makeLink(NodeLink link) {
        if (!currentNode.hasLink(link)) {
            currentNode = currentNode.makeLink(link);
        }
    }

    public class Node {
        private final int maxLinksCount = NodeLink.values().length;
        private final Set<NodeLink> links = new HashSet<>();
        int rowIndex;
        int colIndex;
        public Node(int rowIndex, int colIndex) {
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
        }

        boolean hasLink(NodeLink link) {
            return links.contains(link);
        }

        boolean hasAnyFreeLink() {
            return links.size() < maxLinksCount;
        }

        boolean hasMoreThanOneLink() {
            return links.size() > 1;
        }

        boolean hasOnlyOneLink() {
            return links.size() == 1;
        }

        Node makeLink(NodeLink link) {
            try {
                switch (link) {
                    case LINK_TOP: {
                        links.add(LINK_TOP);
                        Node linkedNode = nodes[rowIndex - 1][colIndex];
                        linkedNode.links.add(LINK_BOTTOM);
                        return linkedNode;
                    }
                    case LINK_TOP_RIGHT: {
                        links.add(LINK_TOP_RIGHT);
                        Node linkedNode = nodes[rowIndex - 1][colIndex + 1];
                        linkedNode.links.add(LINK_BOTTOM_LEFT);
                        return linkedNode;
                    }
                    case LINK_RIGHT: {
                        links.add(LINK_RIGHT);
                        Node linkedNode = nodes[rowIndex][colIndex + 1];
                        linkedNode.links.add(LINK_LEFT);
                        return linkedNode;
                    }
                    case LINK_BOTTOM_RIGHT: {
                        links.add(LINK_BOTTOM_RIGHT);
                        Node linkedNode = nodes[rowIndex + 1][colIndex + 1];
                        linkedNode.links.add(LINK_TOP_LEFT);
                        return linkedNode;
                    }
                    case LINK_BOTTOM: {
                        links.add(LINK_BOTTOM);
                        Node linkedNode = nodes[rowIndex + 1][colIndex];
                        linkedNode.links.add(LINK_TOP);
                        return linkedNode;
                    }
                    case LINK_BOTTOM_LEFT: {
                        links.add(LINK_BOTTOM_LEFT);
                        Node linkedNode = nodes[rowIndex + 1][colIndex - 1];
                        linkedNode.links.add(LINK_TOP_RIGHT);
                        return linkedNode;
                    }
                    case LINK_LEFT: {
                        links.add(LINK_LEFT);
                        Node linkedNode = nodes[rowIndex][colIndex - 1];
                        linkedNode.links.add(LINK_RIGHT);
                        return linkedNode;
                    }
                    case LINK_TOP_LEFT: {
                        links.add(LINK_TOP_LEFT);
                        Node linkedNode = nodes[rowIndex - 1][colIndex - 1];
                        linkedNode.links.add(LINK_BOTTOM_RIGHT);
                        return linkedNode;
                    }
                    default:
                        return currentNode;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.printf("Board node index out of bounds.%nCurrent node is [%s, %s]%n", rowIndex, colIndex);
                return currentNode;
            }
        }
    }
}