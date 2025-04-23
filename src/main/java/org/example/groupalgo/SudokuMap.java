package org.example.groupalgo;

//this will be place to store default map as well as a variable for a customMap input

public class SudokuMap {

    // Easy
    private static final int[][] sudokuMap1 = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    // Medium
    private static final int[][] sudokuMap2 = {
            {0, 2, 0, 6, 0, 8, 0, 0, 0},
            {5, 8, 0, 0, 0, 9, 7, 0, 0},
            {0, 0, 0, 0, 4, 0, 0, 0, 0},
            {3, 7, 0, 0, 0, 0, 5, 0, 0},
            {6, 0, 0, 0, 0, 0, 0, 0, 4},
            {0, 0, 8, 0, 0, 0, 0, 1, 3},
            {0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 9, 8, 0, 0, 0, 3, 6},
            {0, 0, 0, 3, 0, 6, 0, 9, 0}
    };

    // Difficult
    private static final int[][] sudokuMap3 = {
            {0, 0, 0, 6, 0, 0, 4, 0, 0},
            {7, 0, 0, 0, 0, 3, 6, 0, 0},
            {0, 0, 0, 0, 9, 1, 0, 8, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 5, 0, 1, 8, 0, 0, 0, 3},
            {0, 0, 0, 3, 0, 6, 0, 4, 5},
            {0, 4, 0, 2, 0, 0, 0, 6, 0},
            {9, 0, 3, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 1, 0, 0}
    };

    //Not fun
    private static final int[][] sudokuMap4 = {
            {0, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 6, 0, 0, 0, 0, 3},
            {0, 7, 4, 0, 8, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 0, 2},
            {0, 8, 0, 0, 4, 0, 0, 1, 0},
            {6, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 7, 8, 0},
            {5, 0, 0, 0, 0, 9, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 4, 0}
    };

    // Theoretical 17 clue
    // https://www.youtube.com/watch?v=jU_W53M5aMQ
    // https://cracking-the-cryptic.web.app/sudoku/PMhgbbQRRb
    private static final int[][] sudokuMap5 = {
            {0, 2, 9, 0, 0, 0, 4, 0, 0},
            {0, 0, 0, 5, 0, 0, 1, 0, 0},
            {0, 4, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 4, 2, 0, 0, 0},
            {6, 0, 0, 0, 0, 0, 0, 7, 0},
            {5, 0, 0, 0, 0, 0, 0, 0, 0},
            {7, 0, 0, 3, 0, 0, 0, 0, 5},
            {0, 1, 0, 0, 9, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 6, 0}
    };

    //handle error due for map with no solution
    private static final int[][] sudokuMap6 = {
            {0, 2, 9, 0, 0, 0, 4, 0, 0},
            {0, 0, 0, 5, 0, 0, 1, 0, 0},
            {0, 4, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 4, 2, 0, 0, 5},
            {6, 0, 0, 0, 0, 0, 0, 7, 0},
            {5, 0, 0, 0, 0, 0, 0, 0, 0},
            {7, 0, 0, 3, 0, 0, 0, 0, 5},
            {0, 1, 0, 0, 9, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 6, 0}
    };

    //only use in the solver (terminal version) in for loop
    public static int[][][] getAllSudokuMaps = {
            sudokuMap1,
            sudokuMap2,
            sudokuMap3,
            sudokuMap4,
            sudokuMap5,
            sudokuMap6
    };
}
