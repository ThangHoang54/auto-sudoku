package org.example.groupalgo;

//NOT FINAL
public class RMIT_Sudoku_Solver {
    private static final int SIZE = 9;

    public static int[][] solve(int[][] puzzle) {
        constraintPropagation(puzzle);
        int[] emptyCell = findEmptyCell(puzzle);

        if (emptyCell == null) return puzzle;

        int row = emptyCell[0], col = emptyCell[1];
        for (int num = 1; num <= 9; num++) {
            if (isValid(puzzle, row, col, num)) {
                puzzle[row][col] = num;
                int[][] result = solve(puzzle);
                if (result != null) {
                    return result;
                }
                puzzle[row][col] = 0; // Backtracking step
            }
        }
        return null;
    }

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

    private static void constraintPropagation(int[][] board) {
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
                                if (count > 1) break;
                            }
                        }

                        if (count == 1) {
                            board[i][j] = possibleValue;
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);
    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][][] board = SudokuMap.getAllSudokuMaps;

        for(int[][] map : board) {
            if (solve(map) != null) {
                printBoard(map);
                System.out.println();
            } else {
                System.out.println("No solution exists");
            }
        }

    }


}
