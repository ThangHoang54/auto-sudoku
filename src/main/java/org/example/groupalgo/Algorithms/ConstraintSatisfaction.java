package org.example.groupalgo.Algorithms;

import org.example.groupalgo.DataStructures.DomainMap;
import org.example.groupalgo.DataStructures.IntSet;
import org.example.groupalgo.SudokuMap;

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
    private static DomainMap initializeDomains(int[][] board) {
        DomainMap domains = new DomainMap();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String key = row + "-" + col;
                if (board[row][col] != 0) {
                    domains.put(key, new IntSet(board[row][col]));
                } else {
                    domains.put(key, new IntSet());
                }
            }
        }
        return domains;
    }

    /**
     * Finds the first unassigned cell (with value 0) in the Sudoku board.
     *
     * @param board The current state of the Sudoku board.
     * @return The key of the first unassigned cell in "row-col" format, or {@code null} if all cells are assigned.
     */
    private static String findUnassigned(int[][] board) {
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
    private static String[] getNeighbors(String key) {
        // Parse row and column from the key
        int dashIndex = key.indexOf('-');
        int row = parseInt(key.substring(0, dashIndex));
        int col = parseInt(key.substring(dashIndex + 1));

        String[] neighbors = new String[20]; // Preallocate space for neighbors (max 20)
        int count = 0;

        // Add neighbors from the same row and same column
        for (int i = 0; i < SIZE; i++) {
            if (i != col) neighbors[count++] = row + "-" + i; // Row neighbor
            if (i != row) neighbors[count++] = i + "-" + col; // Column neighbor
        }

        // Calculate top-left coordinates of the 3x3 block
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        // Add neighbors from the same 3x3 block, excluding the cell itself
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (i != row || j != col) {
                    String neighbor = i + "-" + j;
                    // Avoid duplicates by checking against existing entries
                    if (!contains(neighbors, count, neighbor)) {
                        neighbors[count++] = neighbor;
                    }
                }
            }
        }

        // Copy relevant entries into a new trimmed array
        String[] result = new String[count];
        System.arraycopy(neighbors, 0, result, 0, count);
        return result;
    }

    /**
     * Checks whether the given target exists in the array up to a specified length.
     */
    private static boolean contains(String[] arr, int len, String target) {
        for (int i = 0; i < len; i++) {
            if (equals(arr[i], target)) return true;
        }
        return false;
    }

    /**
     * Compares two strings for character-by-character equality.
     */
    private static boolean equals(String a, String b) {
        if (a.length() != b.length()) return false;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) return false;
        }
        return true;
    }

    /**
     * Parses a non-negative integer from a numeric string.
     */
    private static int parseInt(String s) {
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            num = num * 10 + (s.charAt(i) - '0');
        }
        return num;
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
    private static boolean applyConstraints(DomainMap domains) {
        boolean changed;
        do {
            changed = false;
            // Retrieve the keys (cell identifiers) of all domains in the map
            String[] keys = domains.keySet();
            // Iterate over all the keys (cells) to check for cells with a single value
            for (String key : keys) {
                IntSet domain = domains.get(key);
                // If the domain contains only one value (naked single)
                if (domain.getSize() == 1) {
                    int val = domain.getOnlyValue();        // Get the single value from the domain
                    String[] neighbors = getNeighbors(key); // Get all the neighbors of the current cell
                    // Iterate over each neighbor of the current cell
                    for (String neighbor : neighbors) {
                        IntSet neighborDomain = domains.get(neighbor);
                        // If the neighbor has a domain and the value is in its domain, remove it
                        if (neighborDomain != null && neighborDomain.remove(val)) {
                            changed = true; // Mark that a change was made
                            // If the neighbor's domain becomes empty, a conflict occurs
                            if (neighborDomain.getSize() == 0) {
                                return false; // Return false if a conflict is found
                            }
                        }
                    }
                }
            }
        } while (changed); // Repeat until no changes occur in a full pass

        return true; // Return true if constraints were satisfied without any conflicts
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
    private static int[][] backtrack(int[][] board, DomainMap domains) {
        String unassigned = findUnassigned(board);
        if (unassigned == null) {
            return board;
        }

        int dashIndex = unassigned.indexOf('-');
        int row = parseInt(unassigned.substring(0, dashIndex));
        int col = parseInt(unassigned.substring(dashIndex + 1));

        IntSet domain = domains.get(unassigned);
        int[] values = domain.getValues();

        for (int i = 0; i < domain.getSize(); i++) {
            int val = values[i];
            board[row][col] = val;

            DomainMap snapshot = domains.copy();
            IntSet newDomain = new IntSet(val);
            snapshot.put(unassigned, newDomain);

            if (applyConstraints(snapshot)) {
                int[][] result = backtrack(board, snapshot);
                if (result != null) {
                    return result;
                }
            }

            board[row][col] = 0; // backtrack
        }
        return null;
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
        DomainMap domains = initializeDomains(board);

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

        for (int i = 0; i < 9; i++) {
            // Print a horizontal divider after the 3rd and 6th rows
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < 9; j++) {
                // Print the number with a vertical divider after 3rd and 6th columns
                if (j % 3 == 0 && j != 0) System.out.print("| ");
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
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