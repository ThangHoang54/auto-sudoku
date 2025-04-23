package org.example.groupalgo.Algorithm;

import org.example.groupalgo.SudokuMap;

import java.util.*;

//The algorithm solves Sudoku by approaching constraint propagation and backtracking.
public class ConstraintSatisfaction {
    private static final int SIZE = 9;

    //Set up initial domains for each cell.
    private static Map<String, Set<Integer>> initializeDomains(int[][] board) {
        Map<String, Set<Integer>> domains = new HashMap<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String key = row + "-" + col;
                if (board[row][col] != 0) {
                    domains.put(key, new HashSet<>(Collections.singleton(board[row][col])));
                } else {
                    Set<Integer> domain = new HashSet<>();
                    for (int i = 1; i <= 9; i++) {
                        domain.add(i);
                    }
                    domains.put(key, domain);
                }
            }
        }
        return domains;
    }

    //Find the first unassigned cell in the board. Returns null if all cells are assigned.
    private static String findUnassigned(int[][] board, Map<String, Set<Integer>> domains) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    return row + "-" + col;
                }
            }
        }
        return null;
    }

    // Get all neighbors of a cell.
    private static List<String> getNeighbors(String key) {
        String[] parts = key.split("-");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        List<String> neighbors = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            if (i != col) {
                neighbors.add(row + "-" + i); // Row neighbors
            }
            if (i != row) {
                neighbors.add(i + "-" + col); // Column neighbors
            }
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (i != row || j != col) {
                    neighbors.add(i + "-" + j); // Block neighbors
                }
            }
        }

        return neighbors;
    }

    // Apply constraints to the domains. Returns true if all constraints are satisfied.
    // Returns false if a constraint is violated.
    // If a constraint is violated, the domains are restored to the state before the constraint was applied.
    // This method is recursive.
    // The method returns true if all constraints are satisfied.
    // The method returns false if a constraint is violated.
    // The method restores the domains to the state before the constraint was applied.
    // The method returns null if a constraint cannot be satisfied.
    private static boolean applyConstraints(Map<String, Set<Integer>> domains) {
        boolean changed;
        do {
            changed = false;
            for (String key : domains.keySet()) {
                Set<Integer> domain = domains.get(key);
                if (domain.size() == 1) {
                    int value = domain.iterator().next();
                    for (String neighbor : getNeighbors(key)) {
                        if (domains.get(neighbor).remove(value)) {
                            changed = true;
                        }
                        if (domains.get(neighbor).isEmpty()) {
                            return false; // Constraint violated
                        }
                    }
                }
            }
        } while (changed);
        return true;
    }

    // Make a deep copy of a map.
    // The copy is a new map with the same keys and values.
    // The values in the copy are independent of the original map.
    // The original map and the copy are different objects.
    // The original map and the copy are not modified.
    // The original map and the copy are not affected by changes to the original map.
    // The original map and the copy are not affected by changes to the copy.
    // The original map and the copy are independent of each other.
    private static Map<String, Set<Integer>> deepCopy(Map<String, Set<Integer>> original) {
        Map<String, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    // Backtrack to find a solution.
    // The method returns the solution if one exists.
    // The method returns null if no solution exists.
    // The method is recursive.
    // The method backtracks to find a solution.
    // The method returns the solution if one exists.
    // The method returns null if no solution exists.
    // The method restores the domains to the state before the constraint was applied.
    private static int[][] backtrack(int[][] board, Map<String, Set<Integer>> domains) {
        String unassigned = findUnassigned(board, domains);
        if (unassigned == null) {
            return board; // All cells are assigned
        }

        String[] parts = unassigned.split("-");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        for (int value : domains.get(unassigned)) {
            board[row][col] = value;

            Map<String, Set<Integer>> snapshot = deepCopy(domains);
            snapshot.get(unassigned).clear();
            snapshot.get(unassigned).add(value);

            if (applyConstraints(snapshot)) {
                int[][] result = backtrack(board, snapshot);
                if (result != null) {
                    return result;
                }
            }

            board[row][col] = 0; // Backtrack
        }

        return null; // No solution
    }

    // The main method solves a Sudoku puzzle.
    public static int[][] solve(int[][] board) {
        Map<String, Set<Integer>> domains = initializeDomains(board);

        if (!applyConstraints(domains)) {
            return null; // Return null if constraints cannot be satisfied
        }

        return backtrack(board, domains);
    }


    public static void main(String[] args) {
        // Assuming SudokuMap.getAllSudokuMaps returns an array of Sudoku puzzles.
        int[][][] boards = SudokuMap.getAllSudokuMaps;

        for (int[][] board : boards) {
            System.out.println("Original Board:");
            printBoard(board);
            System.out.println();

            int[][] solvedBoard = solve(board);
            if (solvedBoard != null) {
                System.out.println("Final Solution:");
                printBoard(solvedBoard);
                System.out.println();
            } else {
                System.out.println("No solution exists");
                System.out.println();
            }
        }
    }

    // Print the board to standard output.
    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}