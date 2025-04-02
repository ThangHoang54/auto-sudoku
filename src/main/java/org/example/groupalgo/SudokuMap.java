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
            {0, 0, 0, 5, 0, 7, 0, 0, 0},
            {0, 4, 0, 2, 6, 3, 0, 0, 0},
            {1, 0, 7, 4, 0, 0, 0, 0, 0},
            {3, 6, 0, 0, 0, 0, 0, 4, 5},
            {0, 0, 2, 0, 5, 0, 7, 0, 0},
            {7, 9, 0, 0, 0, 0, 0, 6, 2},
            {0, 0, 0, 0, 0, 9, 4, 0, 1},
            {0, 0, 0, 1, 3, 4, 0, 9, 0},
            {0, 0, 0, 6, 0, 5, 0, 0, 0}
    };

    // Easy
    private static final int[][] sudokuMap3 = {
            {0, 0, 3, 0, 2, 0, 6, 0, 0},
            {9, 0, 0, 3, 0, 5, 0, 0, 1},
            {0, 0, 1, 8, 0, 6, 4, 0, 0},
            {0, 0, 8, 1, 0, 2, 9, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 6, 7, 0, 8, 2, 0, 0},
            {0, 0, 2, 6, 0, 9, 5, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9},
            {0, 0, 5, 0, 1, 0, 3, 0, 0}
    };

    //only use in the solver (terminal version) in for loop
    public static int[][][] getAllSudokuMaps = {
            sudokuMap1,
            sudokuMap2,
            sudokuMap3
    };
}
