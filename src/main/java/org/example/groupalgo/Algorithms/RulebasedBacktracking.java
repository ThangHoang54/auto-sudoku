package org.example.groupalgo.Algorithms;

import org.example.groupalgo.SudokuMap;
import org.example.groupalgo.DataStructures.IntArrayQueue;

/**
 * @author Group05
 */

/**
 * Solves Sudoku puzzles using a combination of rule-based inference and backtracking.
 * <p>
 * This solver enhances traditional backtracking by integrating:
 * <ul>
 *     <li><b>Naked Single</b>: Assigns values to cells with only one valid candidate.</li>
 *     <li><b>Hidden Single</b>: Assigns values when a candidate appears in only one possible cell in a row, column, or block.</li>
 * </ul>
 * These techniques help prune the search space and improve efficiency.
 * If no deterministic assignment is possible, the algorithm resorts to backtracking.
 */
public class RulebasedBacktracking {
    private static final int SIZE = 9;

    /**
     * Solves the Sudoku puzzle using recursive backtracking and logic-based techniques.
     * <p>
     * This method tries placing digits 1–9 in each empty cell recursively.
     * It uses:
     * <ul>
     *   <li>Backtracking to explore possibilities</li>
     *   <li>Naked Single technique: Assigns cells that can only have one valid value</li>
     *   <li>Hidden Single technique: Finds unique positions for values in a row/col/box</li>
     * </ul>
     * If a number assignment leads to a valid board, recursion continues.
     * If it leads to a dead-end, it backtracks and undoes all naked/hidden single steps.
     *
     * @param board The 9x9 Sudoku board to solve. 0 represents empty cells.
     * @return A 9x9 board solution, or null if no solution exists.
     */
    public static int[][] solve(int[][] board) {

        int[] emptyCell = findEmptyCell(board);
        if (emptyCell == null) return board;

        int row = emptyCell[0], col = emptyCell[1];
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;

                // Apply the naked single technique
                IntArrayQueue nakedSingleQueue = nakedSingle(board);
                // Also try the hidden single technique.
                IntArrayQueue hiddenSingleQueue = hiddenSingle(board);

                int[][] result = solve(board);
                if (result != null) {
                    return result;
                }

                // Backtracking step: undo assignment
                board[row][col] = 0;

                // Reverse the naked single moves.
                while (!nakedSingleQueue.isEmpty()) {
                    int[] cell = nakedSingleQueue.dequeue();
                    board[cell[0]][cell[1]] = 0;
                }
                // Reverse the hidden single moves.
                while (!hiddenSingleQueue.isEmpty()) {
                    int[] cell = hiddenSingleQueue.dequeue();
                    board[cell[0]][cell[1]] = 0;
                }
            }
        }
        return null;
    }

    /**
     * Checks whether placing a number at the given cell violates any Sudoku rules.
     *
     * @param board The current Sudoku board.
     * @param row   The row index of the cell.
     * @param col   The column index of the cell.
     * @param num   The number to check.
     * @return True if the placement is valid, false otherwise.
     */
    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int boxRow = (row / 3) * 3, boxCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRow + i][boxCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Finds the first empty cell (represented by 0) in the board.
     *
     * @param board The Sudoku board.
     * @return An array [row, col] representing the empty cell, or null if the board is full.
     */
    private static int[] findEmptyCell(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Applies the Naked Single technique to the Sudoku board.
     * <p>
     * A naked single occurs when a cell has only one valid candidate value.
     * This method repeatedly scans all cells to find and assign such values.
     * Every assignment is recorded in a queue so it can be undone during backtracking.
     *
     * @param board The current state of the Sudoku board.
     * @return A queue of coordinates (int[2] arrays) representing the cells modified.
     */
    private static IntArrayQueue nakedSingle(int[][] board) {
        IntArrayQueue queue = new IntArrayQueue(SIZE);
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == 0) {
                        int possibleValue = -1;
                        int count = 0;

                        for (int num = 1; num <= 9; num++) {
                            if (isValid(board, i, j, num)) {
                                possibleValue = num;
                                count++;
                                if (count > 1) break;  // More than one candidate: not a naked single.
                            }
                        }

                        if (count == 1) { // Naked single found.
                            board[i][j] = possibleValue;
                            queue.enqueue(new int[]{i, j});
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);
        return queue;
    }

    /**
     * Applies the Hidden Single technique to the Sudoku board.
     * A hidden single occurs when a number can go in only one possible cell
     * within a row, column, or 3x3 block—even if that cell has other candidates.
     *
     * This method scans each unit (row, column, block) repeatedly until no more
     * hidden singles are found. All modifications are stored in a queue so they
     * can be undone during backtracking.
     *
     * @param board The current state of the Sudoku board.
     * @return A queue of coordinates (int[2] arrays) representing the modified cells.
     */
    private static IntArrayQueue hiddenSingle(int[][] board) {
        IntArrayQueue queue = new IntArrayQueue(SIZE);
        boolean changed;
        do {
            changed = false;
            // Process rows for hidden singles
            for (int row = 0; row < SIZE; row++) {
                for (int num = 1; num <= 9; num++) {
                    // Skip if 'num' already exists in the row.
                    boolean exists = false;
                    for (int col = 0; col < SIZE; col++) {
                        if (board[row][col] == num) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) continue;

                    int possibleCount = 0;
                    int possibleCol = -1;
                    for (int col = 0; col < SIZE; col++) {
                        if (board[row][col] == 0 && isValid(board, row, col, num)) {
                            possibleCount++;
                            possibleCol = col;
                        }
                    }
                    if (possibleCount == 1) {
                        board[row][possibleCol] = num;
                        queue.enqueue(new int[]{row, possibleCol});
                        changed = true;
                    }
                }
            }
            // Process columns for hidden singles
            for (int col = 0; col < SIZE; col++) {
                for (int num = 1; num <= 9; num++) {
                    boolean exists = false;
                    for (int row = 0; row < SIZE; row++) {
                        if (board[row][col] == num) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) continue;

                    int possibleCount = 0;
                    int possibleRow = -1;
                    for (int row = 0; row < SIZE; row++) {
                        if (board[row][col] == 0 && isValid(board, row, col, num)) {
                            possibleCount++;
                            possibleRow = row;
                        }
                    }
                    if (possibleCount == 1) {
                        board[possibleRow][col] = num;
                        queue.enqueue(new int[]{possibleRow, col});
                        changed = true;
                    }
                }
            }
            // Process 3x3 blocks for hidden singles.
            for (int boxRow = 0; boxRow < SIZE; boxRow += 3) {
                for (int boxCol = 0; boxCol < SIZE; boxCol += 3) {
                    for (int num = 1; num <= 9; num++) {
                        boolean exists = false;
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (board[boxRow + i][boxCol + j] == num) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) break;
                        }
                        if (exists) continue;

                        int possibleCount = 0;
                        int possibleRow = -1, possibleCol = -1;
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (board[boxRow + i][boxCol + j] == 0 &&
                                        isValid(board, boxRow + i, boxCol + j, num)) {
                                    possibleCount++;
                                    possibleRow = boxRow + i;
                                    possibleCol = boxCol + j;
                                }
                            }
                        }
                        if (possibleCount == 1) {
                            board[possibleRow][possibleCol] = num;
                            queue.enqueue(new int[]{possibleRow, possibleCol});
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);
        return queue;
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

        for (int i = 0; i < SIZE; i++) {
            // Print a horizontal divider after the 3rd and 6th rows
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < SIZE; j++) {
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
            if (solve(map) != null) {
                printBoard(map);
                System.out.println();
            } else {
                System.out.println("No solution exists");
            }
        }
    }
}
