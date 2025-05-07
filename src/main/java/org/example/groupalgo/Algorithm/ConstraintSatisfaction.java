package org.example.groupalgo.Algorithm;

import org.example.groupalgo.SudokuMap;
import java.util.*;

/**
 * @author Group05
 */

// This class implements a Sudoku solver using Constraint Satisfaction Problem (CSP) techniques.
public class ConstraintSatisfaction {
    private static final int SIZE = 9;

    /**
     * Initializes the domain (possible values) for each cell in the Sudoku board.
     * <p>
     * For assigned cells (non-zero), the domain contains only the assigned value.
     * For unassigned cells (zero), the domain contains values 1 through 9.
     *
     * @param board The 9x9 Sudoku board with initial values (0 represents unassigned cells).
     * @return A map where keys are cell coordinates in "row-col" format, and values are sets of possible integers.
     */
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

    /**
     * Finds the first unassigned cell (with value 0) in the Sudoku board.
     *
     * @param board The current state of the Sudoku board.
     * @param domains The domain map associated with each cell (not used in the method but included for future enhancements).
     * @return The key of the first unassigned cell in "row-col" format, or {@code null} if all cells are assigned.
     */
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

    /**
     * Retrieves all neighboring cell coordinates (in the same row, column, or 3x3 block)
     * for the given cell key.
     *
     * @param key The key representing the cell in "row-col" format.
     * @return A list of keys (in "row-col" format) that are neighbors of the given cell.
     */
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

    /**
     * Applies constraint propagation to reduce the domains of each cell.
     * <p>
     * If a cell has only one possible value, that value is removed from the domains
     * of all its neighbors. This process repeats until no more changes occur.
     * <p>
     * If at any point a domain becomes empty, indicating a constraint violation,
     * the method returns {@code false}.
     *
     * @param domains A map of cell coordinates to their possible values (domains).
     * @return {@code true} if all constraints are satisfied, {@code false} if any constraint is violated.
     */
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

    /**
     * Creates a deep copy of a map that maps cell keys to sets of possible integer values.
     * <p>
     * Each entry in the returned map contains a new {@link HashSet} instance, ensuring
     * that modifications to the copy do not affect the original map or vice versa.
     *
     * @param original The original map to copy.
     * @return A deep copy of the input map, with independent keys and value sets.
     */
    private static Map<String, Set<Integer>> deepCopy(Map<String, Set<Integer>> original) {
        Map<String, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Solves the given Sudoku board using recursive backtracking with constraint propagation.
     * <p>
     * This method tries assigning values to unassigned cells by selecting values from
     * their domains. If constraints are still satisfied after an assignment,
     * it recurses further. If a dead end is reached, it backtracks and tries another value.
     *
     * @param board   The current state of the Sudoku board.
     * @param domains A map of cell keys to possible values (domains), used to guide search.
     * @return A completed Sudoku board if a solution is found, or {@code null} if no solution exists.
     */
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

    /**
     * Solves a given Sudoku puzzle using constraint propagation and recursive backtracking.
     * <p>
     * This method initializes the domains for each cell, applies constraints to reduce the search space,
     * and then invokes a backtracking algorithm to search for a valid solution.
     *
     * @param board A 9x9 Sudoku board where empty cells are represented by 0.
     * @return A solved 9x9 Sudoku board if a solution exists, or {@code null} if the puzzle is unsolvable.
     */
    public static int[][] solve(int[][] board) {
        Map<String, Set<Integer>> domains = initializeDomains(board);

        if (!applyConstraints(domains)) {
            return null; // Return null if constraints cannot be satisfied
        }

        return backtrack(board, domains);
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
            for (int j = 0; j < SIZE; j++) {
                // Print the number with a vertical divider after 3rd and 6th columns
                System.out.print(((j == 2 || j == 5) ? board[i][j] + " | " : board[i][j] + " "));
            }
            // Print a horizontal divider after the 3rd and 6th rows
            System.out.println(((i == 2 || i == 5) ? "\n" + "-".repeat(22) : ""));
        }
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
}