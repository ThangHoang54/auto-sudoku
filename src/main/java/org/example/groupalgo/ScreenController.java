package org.example.groupalgo;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.groupalgo.Algorithms.ConstraintSatisfaction;
import org.example.groupalgo.Algorithms.DancingLinks;
import org.example.groupalgo.Algorithms.RulebasedBacktracking;

/**
 * @author Group05
 */

public class ScreenController {
    @FXML
    private Text executionTime;
    @FXML
    private Text resultAnnounce;
    @FXML
    private ChoiceBox<String> cbMapCase;
    @FXML
    private GridPane sudokuBoard;

    private int mapIndex;
    private int[][] map = SudokuMap.getAllSudokuMaps[0];
    private int[][] givenMap = SudokuMap.getAllSudokuMaps[0];

    /**
     * Initializes the Sudoku UI components when the application starts.
     * <p>
     * This method sets up the initial Sudoku grid and configures the ComboBox (`cbMapCase`)
     * for selecting predefined Sudoku puzzles. When a new puzzle is selected, the grid is
     * reset and updated accordingly. It also resets the result announcement and execution
     * time display fields.
     */
    @FXML
    public void initialize() {
        initializeGrid();
        cbMapCase.setOnAction(_ -> {
            resultAnnounce.setText("Click Solve button to get the result");
            executionTime.setText("N/A");
            // Retrieve the selected index from the ComboBox
            mapIndex = cbMapCase.getSelectionModel().getSelectedIndex();
            // Set the selected Sudoku map based on the retrieved index
            map = SudokuMap.getAllSudokuMaps[mapIndex];
            // Assign the same Sudoku map to the 'givenMap' variable
            givenMap = SudokuMap.getAllSudokuMaps[mapIndex];
            initializeGrid(); // Reinitialize the grid with the selected map to reflect changes
        });
    }

    /**
     * Initializes the Sudoku board grid UI with the current state of the puzzle.
     * <p>
     * It creates 9x9 `TextField` cells and configures their appearance and interactivity.
     * Each cell reflects the corresponding value from the `map` array and displays the number
     * if it is a non-zero value. Bold borders are added to visually distinguish 3x3 subgrids.
     * <p>
     * Cells from the initial puzzle (`givenMap`) are styled differently to distinguish
     * between given numbers and those added later.
     */
    private void initializeGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(80, 80); // Adjust cell size for visibility
                cell.setEditable(false);//make cell not able to receive input
                cell.setStyle("-fx-font-size: 16; -fx-alignment: center; -fx-background-color: #F7F2E6; -fx-font-weight: bold;");

                // Compute border widths
                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int right = (col == 8) ? 3 : 1; // Thick right border on last column
                int bottom = (row == 8) ? 3 : 1; // Thick bottom border on last row

                // Apply computed border widths
                cell.setBorder(new Border(new BorderStroke(
                        Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                        new BorderWidths(top, right, bottom, left)
                )));

                if (map[row][col] != 0) {
                    if(givenMap[row][col] == 0) {
                        cell.setStyle(cell.getStyle() + "-fx-text-fill: #A39172;");
                    }
                    cell.setText(String.valueOf(map[row][col]));
                    cell.setEditable(false);
                }
                sudokuBoard.add(cell, col, row);
            }
        }
        refreshGridLine();
    }

    /**
     * Forces the Sudoku grid to refresh its lines.
     * <p>
     * This is a workaround for JavaFX's GridPane not redrawing lines automatically after updates.
     * The method briefly disables and then re-enables the grid lines visibility.
     */
    private void refreshGridLine(){
        // Forcing refresh the gridLine
        sudokuBoard.setGridLinesVisible(false);
        sudokuBoard.setGridLinesVisible(true);
    }

    /**
     * Solves the currently selected Sudoku puzzle using the `RMIT_Sudoku_Solver`.
     * <p>
     * The method clones the original `givenMap` to avoid altering the input,
     * tracks memory usage and execution time, and attempts to solve the puzzle.
     * If successful, it updates the `map` and re-initializes the grid with the solution.
     * If no solution is found, an appropriate message is displayed.
     * <p>
     * Execution time is measured and displayed in milliseconds. Memory usage is logged
     * to the console before and after solving.
     */
    @FXML
    public void solveMap() {
        int[][] board = new int[9][9];
        // Retrieve data from the original board into int[][]
        for (int i = 0; i < 9; i++) {
            System.arraycopy(givenMap[i], 0, board[i], 0, 9);
        }

        // Keep tracking memory usage before solving the Sudoku
        printMemoryUsage("Before solving Sudoku");

        long startTime = System.nanoTime(); // Start Time
        long endTime = 0, duration = 0;

        if ((map = RMIT_Sudoku_Solver.solve(board)) != null) { // Solved successful case
            endTime = System.nanoTime(); // End Time
            System.out.println("Solved Sudoku: Case " + (mapIndex + 1) + " successfully\n");
            RMIT_Sudoku_Solver.printBoard(map);
            System.out.println();
            resultAnnounce.setText("Solved Sudoku: Case " + (mapIndex + 1) + " successfully");
            initializeGrid();
        } else { // No solution case
            System.out.println("No solution found in sudoku case " + mapIndex);
            resultAnnounce.setText("No solution found");
        }

        //Keep tracking of the memory used after solving the Sudoku
        printMemoryUsage("After solving Sudoku");

        if(endTime != 0) {
            duration = (endTime - startTime) / 1000000; // milliseconds
        }

        //System.out.println("Start time: " + startTime + ", end time: " + endTime + ", duration: " + duration + " ms");
        System.out.println((map != null) ? "Total executed time take to solve the Sudoku is in: " + duration + " ms" : "");
        System.out.println("=".repeat(50));
        executionTime.setText((map != null) ?  duration + " ms\n" : "N/A");
    }

    /**
     * Prints the current memory usage of the Java Virtual Machine to the console.
     * <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html">...</a>
     *
     * @param label A label to identify when the memory usage was recorded (e.g., "Before solving Sudoku").
     */
    public static void printMemoryUsage(String label) {
        Runtime runtime = Runtime.getRuntime();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(label + " - Memory Used: " + memoryUsed / (1024 * 1024) + " MB");
    }

    /**
     * Resets the Sudoku UI to its original state.
     * <p>
     * Restores the puzzle to its default configuration (based on the selected map index),
     * clears the result announcement and execution time display, and re-initializes the grid.
     */
    @FXML
    void reset(){
        resultAnnounce.setText("Click Solve button to get the result");
        executionTime.setText("N/A");
        map = SudokuMap.getAllSudokuMaps[mapIndex];
        initializeGrid();
    }

    /**
     * Exits the application.
     * <p>
     * Invokes a system exit with status code 0, which forcibly shuts down the JavaFX program.
     */
    @FXML
    void exit(){
        System.exit(0);
    }
}
