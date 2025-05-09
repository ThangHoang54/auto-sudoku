package org.example.groupalgo.Algorithms;

import org.example.groupalgo.DataStructures.MyArrayList;
import org.example.groupalgo.SudokuMap;

/**
 * @author Group05
 */

public class DancingLinks {
    // Array of ColumnNode objects representing the 324 constraints in the DLX matrix
    private static final ColumnNode[] columns = new ColumnNode[324]; // 81 (cells) + 81 (rows) + 81 (columns) + 81 (boxes)
    private static ColumnNode header; // Header node for the column headers in the DLX matrix
    // List of DataNode objects representing the solution found by DLX
    private static MyArrayList<DataNode> answer; // Stores the solution once found during the search

    /**
     * Represents a node in the Dancing Links (DLX) structure.
     *
     * Each DataNode is part of a 4-way doubly linked list: left, right, up, and down.
     * It also holds a reference to its corresponding column header ({@link ColumnNode}),
     * which allows tracking of which constraint this node is part of.
     */
    private static class DataNode {
        DataNode L, R, U, D; // Pointers to neighboring nodes (Left, Right, Up, Down)
        ColumnNode C;        // The column this node belongs to (used for constraint tracking)

        /**
         * Constructs a standalone DataNode that links to itself in all directions.
         */
        public DataNode() {
            L = R = U = D = this;
        }

        /**
         * Constructs a DataNode belonging to a specific column.
         *
         * @param c The column node this data node belongs to.
         */
        public DataNode(ColumnNode c) {
            this();
            C = c;
        }

        /**
         * Links the given node directly below this node in the vertical list.
         *
         * @param node The node to link downward.
         */
        public void linkDown(DataNode node) {
            node.D = D;
            node.D.U = node;
            node.U = this;
            D = node;
        }

        /**
         * Links the given node directly to the right of this node in the horizontal list.
         *
         * @param node The node to link to the right.
         */
        public void linkRight(DataNode node) {
            node.R = R;
            node.R.L = node;
            node.L = this;
            R = node;
        }

        /**
         * Unlinks this node from its horizontal neighbors (left-right).
         */
        public void unlinkLR() {
            L.R = R;
            R.L = L;
        }

        /**
         * Relinks this node to its horizontal neighbors (left-right).
         */
        public void relinkLR() {
            L.R = this;
            R.L = this;
        }

        /**
         * Unlinks this node from its vertical neighbors (up-down).
         */
        public void unlinkUD() {
            U.D = D;
            D.U = U;
        }

        /**
         * Relinks this node to its vertical neighbors (up-down).
         */
        public void relinkUD() {
            U.D = this;
            D.U = this;
        }
    }

    /**
     * Represents a column header node in the Dancing Links (DLX) matrix.
     *
     * A {@code ColumnNode} extends {@link DataNode} and tracks additional metadata:
     * - {@code size}: the number of 1s (data nodes) in this column.
     * - {@code name}: a unique identifier for the constraint this column represents (0–323 in Sudoku).
     *
     * The {@code cover()} and {@code uncover()} methods are the core of Algorithm X,
     * allowing dynamic addition/removal of constraints during recursive search.
     */
    private static class ColumnNode extends DataNode {
        int size;  // Number of data nodes in this column (i.e., active choices)
        int name;  // Unique identifier for the column (constraint index)

        /**
         * Constructs a new column node with the given constraint name.
         *
         * @param n The unique ID of this constraint (0–323 for Sudoku).
         */
        public ColumnNode(int n) {
            super();
            size = 0;
            name = n;
            C = this; // Each column node is its own column reference
        }

        /**
         * Covers this column from the DLX matrix.
         *
         * This removes the column and all its associated rows from the matrix,
         * effectively removing all conflicting candidates for this constraint.
         */
        public void cover() {
            unlinkLR(); // Remove this column from the header list
            // For each row (candidate) in this column
            for (DataNode i = D; i != this; i = i.D) {
                // Remove all other nodes in this row (i.e., other constraints satisfied by this candidate)
                for (DataNode j = i.R; j != i; j = j.R) {
                    j.unlinkUD();    // Remove node vertically
                    j.C.size--;      // Decrease count in that column
                }
            }
        }

        /**
         * Uncovers this column in the DLX matrix.
         *
         * This reinserts all rows and nodes that were removed during {@code cover()},
         * restoring the matrix to its previous state.
         */
        public void uncover() {
            // Reinsert all rows in reverse order
            for (DataNode i = U; i != this; i = i.U) {
                // Reinsert all nodes in this row (restore vertical links)
                for (DataNode j = i.L; j != i; j = j.L) {
                    j.C.size++;     // Increase count in the column
                    j.relinkUD();   // Restore vertical link
                }
            }
            relinkLR(); // Restore this column to the header list
        }
    }


    /**
     * Solves a given Sudoku puzzle using the Dancing Links (DLX) algorithm.
     *
     * @param puzzle A 9x9 integer matrix representing the Sudoku puzzle.
     *               Empty cells should be represented with 0.
     *               Numbers must be between 0 and 9 (inclusive).
     * @return A 9x9 integer matrix representing the solved Sudoku puzzle,
     *         or null if the input is invalid or the puzzle has no solution.
     */
    public static int[][] solve(int[][] puzzle) {
        // Validate input dimensions
        if (puzzle == null || puzzle.length != 9 || puzzle[0].length != 9) {
            return null;
        }

        // Initialize the DLX (Dancing Links) structure
        initDLX();

        // Process known values and add constraints to the DLX structure
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int num = puzzle[row][col];
                // Validate each cell value
                if (num < 0 || num > 9)
                    return null;
                // If the cell has a number, add the known constraint
                if (num != 0)
                    addKnownConstraint(row, col, num - 1); // subtract 1 to convert to 0-based index
            }
        }
        // Add all possible constraints for unknown (empty) cells
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[row][col] == 0)
                    addUnknownConstraints(row, col);
            }
        }
        // Attempt to solve the puzzle using DLX search
        answer = new MyArrayList<>();
        if (!search(0)) {
            // No solution found
            return null;
        }

        // Convert the DLX solution back into a 9x9 Sudoku grid
        return convertSolutionToGrid();
    }


    /**
     * Initializes the Dancing Links (DLX) data structure.
     *
     * This method sets up the header node and links 324 column nodes representing
     * all possible Sudoku constraints. These constraints include:
     * - 81 cell constraints (each cell must be filled),
     * - 81 row constraints (each number must appear once per row),
     * - 81 column constraints (each number must appear once per column),
     * - 81 box constraints (each number must appear once per 3x3 box).
     */
    private static void initDLX() {
        // Create the root (header) node of the DLX matrix
        header = new ColumnNode(-1); // -1 indicates the header node

        // Link 324 columns representing all constraints in Sudoku
        // Constraint index mapping:
        // 0–80:    cell constraints (row * 9 + col)
        // 81–161:  row constraints (row * 9 + num)
        // 162–242: column constraints (col * 9 + num)
        // 243–323: box constraints ((boxRow * 3 + boxCol) * 9 + num)
        ColumnNode prev = header;
        for (int i = 0; i < 324; i++) {
            columns[i] = new ColumnNode(i);   // Create column node for each constraint
            prev.linkRight(columns[i]);       // Link it to the previous node in the row
            prev = columns[i];                // Update the previous pointer
        }
    }

    /**
     * Adds a known (pre-filled) Sudoku value as a row in the DLX structure.
     *
     * This encodes the constraints imposed by placing a specific number in a cell,
     * including cell, row, column, and box constraints. The corresponding row is
     * added directly to the exact cover matrix.
     *
     * @param row The row index of the cell (0-8).
     * @param col The column index of the cell (0-8).
     * @param num The number to place in the cell (0-8), where 0 = '1', 1 = '2', ..., 8 = '9'.
     */
    private static void addKnownConstraint(int row, int col, int num) {
        // Compute the box index (0-8) from the row and column
        int box = (row / 3) * 3 + (col / 3);

        // Calculate indices for the 4 constraints this value satisfies
        int cellConstraint = row * 9 + col;               // Cell must be filled
        int rowConstraint = 81 + row * 9 + num;           // Number must appear in this row
        int colConstraint = 162 + col * 9 + num;          // Number must appear in this column
        int boxConstraint = 243 + box * 9 + num;          // Number must appear in this 3x3 box

        // Retrieve the relevant column headers for each constraint
        ColumnNode[] cols = {
                columns[cellConstraint],
                columns[rowConstraint],
                columns[colConstraint],
                columns[boxConstraint]
        };

        // Create the first node in the row for this constraint
        DataNode rowStart = new DataNode(cols[0]);
        cols[0].size++;                       // Increment column size (number of nodes)
        cols[0].U.linkDown(rowStart);        // Link vertically in the column

        // Add remaining three constraint nodes and link horizontally
        DataNode curr = rowStart;
        for (int i = 1; i < 4; i++) {
            DataNode newNode = new DataNode(cols[i]);
            curr.linkRight(newNode);         // Link horizontally in the row
            cols[i].size++;                  // Increment column size
            cols[i].U.linkDown(newNode);     // Link vertically in the column
            curr = newNode;
        }
    }

    /**
     * Adds all possible number constraints for an unknown (empty) cell in the Sudoku grid.
     *
     * This method assumes the cell is empty and considers all 9 digits (1–9) as potential
     * candidates. For each possible digit, it encodes the 4 constraints (cell, row, column,
     * and box) into the DLX structure using {@code addKnownConstraint()}.
     *
     * @param row The row index of the empty cell (0–8).
     * @param col The column index of the empty cell (0–8).
     */
    private static void addUnknownConstraints(int row, int col) {
        // Try placing each number (0-8, representing 1-9) in the empty cell
        for (int num = 0; num < 9; num++) {
            addKnownConstraint(row, col, num);
        }
    }

    /**
     * Recursively searches for a solution using Algorithm X with Dancing Links (DLX).
     *
     * This method attempts to cover all constraints by choosing a column (constraint)
     * with the fewest options (minimum branching), and recursively tries possible rows
     * (candidates) that satisfy that constraint. If all constraints are covered, a solution
     * has been found.
     *
     * @param k The current depth of the search (used for recursion depth tracking).
     * @return {@code true} if a complete solution is found, {@code false} otherwise.
     */
    private static boolean search(int k) {
        // If there are no remaining columns, all constraints are satisfied
        if (header.R == header)
            return true; // Found a valid solution

        // Select the column with the fewest remaining options (heuristic to optimize search)
        ColumnNode col = selectColumn();
        col.cover(); // Temporarily remove the column from the matrix

        // Iterate through each row (possible candidate) in this column
        for (DataNode r = col.D; r != col; r = r.D) {
            answer.add(r); // Choose this row as part of the solution

            // Cover all other columns linked to this row (satisfy other constraints)
            for (DataNode j = r.R; j != r; j = j.R) {
                j.C.cover();
            }
            // Recurse to continue solving with the reduced matrix
            if (search(k + 1)) {
                return true; // If a solution is found, propagate success
            }
            // Backtrack: remove last choice and uncover columns
            answer.removeLast();
            for (DataNode j = r.L; j != r; j = j.L) {
                j.C.uncover();
            }
        }
        // Uncover this column (restore to previous state) before returning
        col.uncover();
        return false; // No solution found at this path
    }

    /**
     * Selects the column (constraint) with the fewest remaining options (minimum size).
     *
     * This heuristic improves the efficiency of the DLX algorithm by reducing the branching
     * factor—choosing the column with the least number of 1s (i.e., fewest rows that can satisfy it).
     * If a column has size 0, it means there's no possible way to satisfy that constraint, so
     * we break early for efficiency.
     *
     * @return The {@link ColumnNode} with the smallest size (least number of remaining options).
     */
    private static ColumnNode selectColumn() {
        ColumnNode selected = null;
        int minSize = Integer.MAX_VALUE;

        // Iterate through all columns in the header row (linked list)
        for (ColumnNode col = (ColumnNode) header.R; col != header; col = (ColumnNode) col.R) {
            // Keep track of the column with the smallest number of 1s
            if (col.size < minSize) {
                minSize = col.size;
                selected = col;

                // If a column has 0 options, it's unsatisfiable — no need to continue
                if (minSize == 0) break;
            }
        }

        return selected;
    }

    /**
     * Converts the list of selected DLX rows (stored in {@code answer}) into a 9x9 Sudoku grid.
     *
     * Each selected row in the DLX solution corresponds to a placement of a specific digit
     * in a specific cell. This method decodes that information by identifying the cell and
     * row constraints encoded in the DLX matrix.
     *
     * @return A fully filled 9x9 Sudoku grid representing the solution.
     */
    private static int[][] convertSolutionToGrid() {
        int[][] solution = new int[9][9];

        for (int i = 0; i < answer.getSize(); i++) {
            DataNode rowNode = answer.get(i);

            // Collect all four nodes in the current DLX row
            MyArrayList<DataNode> nodesInRow = new MyArrayList<>();
            DataNode currentNode = rowNode;
            do {
                nodesInRow.add(currentNode);
                currentNode = currentNode.R;
            } while (currentNode != rowNode);

            // Find the cellNode (C.name < 81)
            DataNode cellNode = null;
            for (int j = 0; j < nodesInRow.getSize(); j++) {
                DataNode node = nodesInRow.get(j);
                if (node.C.name < 81) {
                    cellNode = node;
                    break;
                }
            }

            if (cellNode == null) {
                return null; // Should never happen
            }

            int cellConstraint = cellNode.C.name;
            int row = cellConstraint / 9;
            int col = cellConstraint % 9;

            // Find the rowConstraintNode (C.name between 81 and 161)
            DataNode rowConstraintNode = null;
            for (int j = 0; j < nodesInRow.getSize(); j++) {
                DataNode node = nodesInRow.get(j);
                if (node.C.name >= 81 && node.C.name < 162) {
                    rowConstraintNode = node;
                    break;
                }
            }

            if (rowConstraintNode == null) {
                return null; // Should never happen
            }

            int num = (rowConstraintNode.C.name - 81) % 9 + 1;

            solution[row][col] = num;
        }

        return solution;
    }

    /**
     * Utility method to print a 9x9 Sudoku board in a human-readable format.
     *
     * The method includes visual separators to distinguish the 3x3 boxes,
     * enhancing the readability of the output. If the board is {@code null},
     * the method returns without printing.
     *
     * @param board A 9x9 Sudoku board to print; each element should be in the range 1–9.
     */
    public static void printBoard(int[][] board) {
        if (board == null) {
            return; // Do nothing if board is null
        }

        for (int i = 0; i < 9; i++) {
            // Print a horizontal divider after the 3rd and 6th rows
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < 9; j++) {
                // Print the number with a vertical divider after 3rd and 6th columns
                if (j % 3 == 0 && j != 0) System.out.print("| ");
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