package org.example.groupalgo.Algorithm;

import org.example.groupalgo.SudokuMap;

//this is backtracking only
public class PureBacktracking {
    private static final int SIZE = 9;

    // Modified solve method that returns a solved board or null if no solution exists.
    public static int[][] solve(int[][] board) {
        int[] cell = findEmpty(board);
        // If there's no empty cell, the board is complete so return it.
        if (cell == null) {
            return board;
        }
        int row = cell[0];
        int col = cell[1];

        // Try numbers 1 through 9 in the current empty cell.
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num; // Place the candidate number.
                int[][] result = solve(board); // Attempt to solve the updated board.
                if (result != null) { // If the sub-call returns a solution, propagate it.
                    return result;
                }
                board[row][col] = 0; // Backtrack if placing num didn't lead to a solution.
            }
        }
        return null; // Return null if no valid number can solve from here.
    }

    // Find an empty cell; returns null if no empty cell is found.
    private static int[] findEmpty(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Check whether placing 'num' at (row, col) is valid.
    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row and column.
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        // Check 3x3 sub-grid.
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    // Print the board to standard output.
    public static void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // get all stored boards
        int[][][] boards = SudokuMap.getAllSudokuMaps;

        for (int[][] board : boards) {
            int[][] solvedBoard = solve(board);
            if (solvedBoard != null) {
                printBoard(solvedBoard);
                System.out.println();
            } else {
                System.out.println("No solution exists");
            }
        }
    }
}
