package org.example.groupalgo.Algorithm;

import org.example.groupalgo.SudokuMap;

import java.util.LinkedList;
import java.util.Queue;

//this is backtracking with naked single and hidden single
public class SudokuBackTrackingWithHiddenSingleAndNakedSingle {
    private static final int SIZE = 9;

    // The main recursive solver.
    public static int[][] solveSudoku(int[][] board) {

        int[] emptyCell = findEmptyCell(board);
        if (emptyCell == null) return board;

        int row = emptyCell[0], col = emptyCell[1];
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;

                // Apply the naked single technique
                Queue<int[]> nakedSingleQueue = nakedSingle(board);
                // Also try the hidden single technique.
                Queue<int[]> hiddenSingleQueue = hiddenSingle(board);

                int[][] result = solveSudoku(board);
                if (result != null) {
                    return result;
                }

                // Backtracking step: undo assignment.
                board[row][col] = 0;
                // Reverse the naked single moves.
                while (!nakedSingleQueue.isEmpty()) {
                    int[] cell = nakedSingleQueue.poll();
                    board[cell[0]][cell[1]] = 0;
                }
                // Reverse the hidden single moves.
                while (!hiddenSingleQueue.isEmpty()) {
                    int[] cell = hiddenSingleQueue.poll();
                    board[cell[0]][cell[1]] = 0;
                }
            }
        }
        return null;
    }

    // Check if placing 'num' at (row, col) is valid.
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

    // Find the next empty cell; returns null if no empty cell remains.
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

    // Naked Single
    // This method goes over each cell and if there is exactly one candidate (naked single),
    // it assigns the value and adds the cell to a queue for backtracking.
    private static Queue<int[]> nakedSingle(int[][] board) {
        Queue<int[]> queue = new LinkedList<>();
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
                            queue.add(new int[]{i, j});
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);
        return queue;
    }

    // Hidden Single Technique
    // examines each row, column, and 3x3 block separately
    // It looks for a candidate number that has only one possible cell in that unit
    // When found, that candidate is placed, and the cell's coordinates are added to the queue
    private static Queue<int[]> hiddenSingle(int[][] board) {
        Queue<int[]> queue = new LinkedList<>();
        boolean changed;
        do {
            changed = false;
            // Process rows for hidden singles.
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
                        queue.add(new int[]{row, possibleCol});
                        changed = true;
                    }
                }
            }
            // Process columns for hidden singles.
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
                        queue.add(new int[]{possibleRow, col});
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
                            queue.add(new int[]{possibleRow, possibleCol});
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);
        return queue;
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
            if (solveSudoku(map) != null) {
                printBoard(map);
                System.out.println();
            } else {
                System.out.println("No solution exists");
            }
        }
    }
}
