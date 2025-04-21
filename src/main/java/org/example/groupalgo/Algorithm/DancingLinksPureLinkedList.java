package org.example.groupalgo.Algorithm;

import org.example.groupalgo.SudokuMap;

import java.util.ArrayList;
import java.util.List;

public class DancingLinksPureLinkedList {
    private static final int SIZE = 9;

    private static class DataNode {
        DataNode L, R, U, D;
        ColumnNode C;

        public DataNode() {
            L = R = U = D = this;
        }

        public DataNode(ColumnNode c) {
            this();
            C = c;
        }

        public void linkDown(DataNode node) {
            node.D = D;
            node.D.U = node;
            node.U = this;
            D = node;
        }

        public void linkRight(DataNode node) {
            node.R = R;
            node.R.L = node;
            node.L = this;
            R = node;
        }

        public void unlinkLR() {
            L.R = R;
            R.L = L;
        }

        public void relinkLR() {
            L.R = this;
            R.L = this;
        }

        public void unlinkUD() {
            U.D = D;
            D.U = U;
        }

        public void relinkUD() {
            U.D = this;
            D.U = this;
        }
    }

    private static class ColumnNode extends DataNode {
        int size;
        int name;

        public ColumnNode(int n) {
            super();
            size = 0;
            name = n;
            C = this;
        }

        public void cover() {
            unlinkLR();
            for (DataNode i = D; i != this; i = i.D) {
                for (DataNode j = i.R; j != i; j = j.R) {
                    j.unlinkUD();
                    j.C.size--;
                }
            }
        }

        public void uncover() {
            for (DataNode i = U; i != this; i = i.U) {
                for (DataNode j = i.L; j != i; j = j.L) {
                    j.C.size++;
                    j.relinkUD();
                }
            }
            relinkLR();
        }
    }

    private static ColumnNode header;
    private static List<DataNode> answer;

    public static int[][] solve(int[][] puzzle) {
        // Validate input
        if (puzzle == null || puzzle.length != 9 || puzzle[0].length != 9) {
            return null;
        }

        // Initialize DLX structure
        initDLX();

        // Add constraints for the given puzzle
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int num = puzzle[row][col];
                if (num < 0 || num > 9) {
                    return null;
                }
                if (num != 0) {
                    addKnownConstraint(row, col, num - 1);
                }
            }
        }

        // Add all possible constraints for empty cells
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[row][col] == 0) {
                    addUnknownConstraints(row, col);
                }
            }
        }

        // Solve using DLX
        answer = new ArrayList<>();
        if (!search(0)) {
            return null;
        }

        // Convert solution back to sudoku grid
        return convertSolutionToGrid();
    }

    private static void initDLX() {
        // Create header node
        header = new ColumnNode(-1);

        // Create column nodes for all 324 constraints
        ColumnNode prev = header;
        for (int i = 0; i < 324; i++) {
            ColumnNode newNode = new ColumnNode(i);
            prev.linkRight(newNode);
            prev = newNode;
        }
    }

    private static ColumnNode findColumn(int name) {
        ColumnNode current = (ColumnNode) header.R;
        while (current != header) {
            if (current.name == name) return current;
            current = (ColumnNode) current.R;
        }
        return null;
    }

    private static void addKnownConstraint(int row, int col, int num) {
        int box = (row / 3) * 3 + (col / 3);
        ColumnNode cellCol = findColumn(row * 9 + col);
        ColumnNode rowCol = findColumn(81 + row * 9 + num);
        ColumnNode colCol = findColumn(162 + col * 9 + num);
        ColumnNode boxCol = findColumn(243 + box * 9 + num);

        if (cellCol == null || rowCol == null || colCol == null || boxCol == null) {
            return;
        }

        DataNode rowStart = new DataNode(cellCol);
        cellCol.size++;
        cellCol.U.linkDown(rowStart);

        DataNode curr = rowStart;
        for (ColumnNode colNode : new ColumnNode[]{rowCol, colCol, boxCol}) {
            DataNode newNode = new DataNode(colNode);
            curr.linkRight(newNode);
            colNode.size++;
            colNode.U.linkDown(newNode);
            curr = newNode;
        }
    }

    private static void addUnknownConstraints(int row, int col) {
        // For empty cells, add all 9 possible numbers
        for (int num = 0; num < 9; num++) {
            addKnownConstraint(row, col, num);
        }
    }

    private static boolean search(int k) {
        if (header.R == header) {
            return true; // Solution found
        }

        // Choose column with minimum size
        ColumnNode col = selectColumn();
        col.cover();

        for (DataNode r = col.D; r != col; r = r.D) {
            answer.add(r);

            for (DataNode j = r.R; j != r; j = j.R) {
                j.C.cover();
            }

            if (search(k + 1)) {
                return true;
            }

            // Backtrack
            answer.removeLast();

            for (DataNode j = r.L; j != r; j = j.L) {
                j.C.uncover();
            }
        }

        col.uncover();
        return false;
    }

    private static ColumnNode selectColumn() {
        ColumnNode selected = null;
        int minSize = Integer.MAX_VALUE;

        for (ColumnNode col = (ColumnNode) header.R; col != header; col = (ColumnNode) col.R) {
            if (col.size < minSize) {
                minSize = col.size;
                selected = col;
                if (minSize == 0) break; // Can't get smaller than this
            }
        }

        return selected;
    }

    private static int[][] convertSolutionToGrid() {
        int[][] solution = new int[9][9];

        for (DataNode rowNode : answer) {
            // Find all nodes in this row
            List<DataNode> nodesInRow = new ArrayList<>();
            DataNode currentNode = rowNode;
            do {
                nodesInRow.add(currentNode);
                currentNode = currentNode.R;
            } while (currentNode != rowNode);

            // Find the cell constraint (0-80)
            DataNode cellNode = nodesInRow.stream()
                    .filter(node -> node.C.name < 81)
                    .findFirst()
                    .orElseThrow();

            int cellConstraint = cellNode.C.name;
            int row = cellConstraint / 9;
            int col = cellConstraint % 9;

            // Find the row constraint (81-161) to get the number
            DataNode rowConstraintNode = nodesInRow.stream()
                    .filter(node -> node.C.name >= 81 && node.C.name < 162)
                    .findFirst()
                    .orElseThrow();

            int num = (rowConstraintNode.C.name - 81) % 9 + 1;
            solution[row][col] = num;
        }

        return solution;
    }

    // Utility method to print the board.
    public static void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][][] boards = SudokuMap.getAllSudokuMaps;

        for (int[][] map : boards) {
            // Create a copy of the original puzzle
            int[][] puzzleCopy = new int[9][9];
            for (int i = 0; i < 9; i++) {
                System.arraycopy(map[i], 0, puzzleCopy[i], 0, 9);
            }

            // Solve the copy and get the solution
            int[][] solution = solve(puzzleCopy);

            // Print both original and solution
            System.out.println("Original puzzle:");
            printBoard(map);
            System.out.println("\nSolution:");

            if (solution == null) {
                System.out.println("No solution exists for this puzzle.");
            } else {
                printBoard(solution);
            }
            System.out.println("\n----------------------\n");
        }
    }
}