package org.example.groupalgo;

// This will be the place to store default map as well as a variable for a customMap input
// Puzzle 2-4 source: https://sandiway.arizona.edu/sudoku/examples.html

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

    // Intermediate: Daily Telegraph January 19th "Diabolical"
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

    // Difficult: Vegard Hanssen puzzle 2155141
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

    // Not fun: Challenge 1 from Sudoku Solver by Logic
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

    // David Filmer's Unsolvable #28
    // https://www.sudokuwiki.org/Print_A_New_Metric_for_Difficult_Sudoku_Puzzles
    // https://www.sudokuwiki.org/sudoku.htm?bd=600008940900006100070040000200610000000000200089002000000060005000000030800001600
    private static final int[][] sudokuMap6 = {
            {6, 0, 0, 0, 0, 8, 9, 4, 0},
            {9, 0, 0, 0, 0, 6, 1, 0, 0},
            {0, 7, 0, 0, 4, 0, 0, 0, 0},
            {2, 0, 0, 6, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 2, 0, 0},
            {0, 8, 9, 0, 0, 2, 0, 0, 0},
            {0, 0, 0, 0, 6, 0, 0, 0, 5},
            {0, 0, 0, 0, 0, 0, 0, 3, 0},
            {8, 0, 0, 0, 0, 1, 6, 0, 0}
    };

    // No solution example
    private static final int[][] sudokuMap7 = {
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
            sudokuMap6,
            sudokuMap7
    };
}
