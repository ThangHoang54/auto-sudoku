package org.example.groupalgo.Algorithm;

import org.example.groupalgo.SudokuMap;

public class TimeTesting {
    private static void solveWithNakedSingle() {
        int[][][] boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        long startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (BacktrackingNakedSingle.solveSudoku(map) == null) {
                System.out.println("No solution exists");
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for backtrack with naked single");
    }

    private static void solveWithHiddenSingleAndNakedSingle() {
        int[][][] boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        long startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (BacktrackingHiddenSingleNakedSingle.solveSudoku(map) == null) {
                System.out.println("No solution exists");
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for backtrack with naked single and hidden single");
    }

    private static void solveWithPureBacktracking() {
        int[][][] boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        long startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (PureBacktracking.solve(map) == null) {
                System.out.println("No solution exists");
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for pure backtrack");
    }

    private static void solveWithConstraintSatisfaction() {
        int[][][] boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        long startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (ConstraintSatisfaction.solve(map) == null) {
                System.out.println("No solution exists");
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for constraint satisfaction.");
    }

    private static void solveWithDancingLinksArray() {
        int[][][] boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        long startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (DancingLinksArray.solve(map) == null) {
                System.out.println("No solution exists");
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for DancingLinksArray.");
    }

    private static void solveWithDancingLinksPureLinkedList() {
        int[][][] boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        long startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (DancingLinksPureLinkedList.solve(map) == null) {
                System.out.println("No solution exists");
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for DancingLinksPureLinkedList.");
    }

    public static int[][][] deepCopyBoards(int[][][] original) {
        int[][][] copy = new int[original.length][][];
        for (int i = 0; i < original.length; i++) {
            if (original[i] != null) {
                copy[i] = new int[original[i].length][];
                for (int j = 0; j < original[i].length; j++) {
                    if (original[i][j] != null) {
                        copy[i][j] = new int[original[i][j].length];
                        System.arraycopy(original[i][j], 0, copy[i][j], 0, original[i][j].length);
                    }
                }
            }
        }
        return copy;
    }

    private static void runSudokuSolvers() {
        solveWithNakedSingle();
        solveWithHiddenSingleAndNakedSingle();
        solveWithPureBacktracking();
        solveWithConstraintSatisfaction();
        solveWithDancingLinksArray();
        solveWithDancingLinksPureLinkedList();
    }

    public static void main(String[] args) {
        runSudokuSolvers();
    }
}
