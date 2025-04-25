package org.example.groupalgo.Algorithm;

import org.example.groupalgo.SudokuMap;

public class TimeTesting {
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
    public static void main(String[] args) {
        int[][][] boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);//get all the board
        long startTime, endTime;
        startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (SudokuBackTrackingWithNakedSingle.solveSudoku(map) != null) { //this one only naked single
//                SudokuBackTrackingWithNakedSingle.printBoard(map);
//                System.out.println();
            } else {
                System.out.println("No solution exists");
            }
        }
        endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for backtrack with naked single");

        boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);//reset the map
        startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (SudokuBackTrackingWithHiddenSingleAndNakedSingle.solveSudoku(map) != null) { //this one only naked single
//                SudokuBackTrackingWithHiddenSingleAndNakedSingle.printBoard(map);
//                System.out.println();
            } else {
                System.out.println("No solution exists");
            }
        }
        endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for backtrack with naked single and hidden single");

        boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        startTime = System.nanoTime();
        for (int[][] map : boards) {
            if (SudokuBacktracking.solve(map) != null) { //this one only naked single
//                SudokuBacktracking.printBoard(map);
//                System.out.println();
            } else {
                System.out.println("No solution exists");
            }
        }
        endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for pure backtrack (this is int[][] return)");

        boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        startTime = System.nanoTime();
        for(int[][] map : boards){
            if(ConstraintSatisfaction.solve(map) != null){
//                ConstraintSatisfaction.printBoard(map);
//                System.out.println();
            } else{
                System.out.println("No solution exists");
            }
        }
        endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for constraint satisfaction.");

        boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        startTime = System.nanoTime();
        for(int[][] map : boards){
            if(DancingLinksArray.solve(map) != null){
//                DancingLinksArray.printBoard(map);
//                System.out.println();
            } else{
                System.out.println("No solution exists");
            }
        }
        endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for DancingLinksArray.");

        boards = deepCopyBoards(SudokuMap.getAllSudokuMaps);
        startTime = System.nanoTime();
        for(int[][] map : boards){
            if(DancingLinksPureLinkedList.solve(map) != null){
//                DancingLinksPureLinkedList.printBoard(map);
//                System.out.println();
            } else{
                System.out.println("No solution exists");
            }
        }
        endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) + " nanosecond for DancingLinksPureLinkedList.");
    }
}
