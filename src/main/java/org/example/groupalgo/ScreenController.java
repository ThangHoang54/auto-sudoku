package org.example.groupalgo;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.groupalgo.Algorithm.ConstraintSatisfaction;
import org.example.groupalgo.Algorithm.DancingLinksArray;
import org.example.groupalgo.Algorithm.DancingLinksPureLinkedList;

public class ScreenController {
    @FXML
    private Text executionTime;
    @FXML
    private Text resultAnnounce;
    @FXML
    private ChoiceBox<String> cb_mapCase;
    @FXML
    private GridPane sudoku_board;

    private int map_index;
    private int[][] map = SudokuMap.getAllSudokuMaps[0];
    private int[][] givenMap = SudokuMap.getAllSudokuMaps[0];

    @FXML
    public void initialize() {
        initializeGrid();

        cb_mapCase.setOnAction(_ -> {
            resultAnnounce.setText("Click Solve button to get the result");
            executionTime.setText("N/A");
            // Retrieve the selected index from the ComboBox
            map_index = cb_mapCase.getSelectionModel().getSelectedIndex();
            // Set the selected Sudoku map based on the retrieved index
            map = SudokuMap.getAllSudokuMaps[map_index];
            // Assign the same Sudoku map to the 'givenMap' variable
            givenMap = SudokuMap.getAllSudokuMaps[map_index];
            initializeGrid(); // Reinitialize the grid with the selected map to reflect changes
        });
    }

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
                sudoku_board.add(cell, col, row);
            }
        }
        refreshGridLine();
    }

    private void refreshGridLine(){
        // Forcing refresh the gridLine
        sudoku_board.setGridLinesVisible(false);
        sudoku_board.setGridLinesVisible(true);
    }

    @FXML
    public void solveMap() {
        long startTime = System.nanoTime(); // Start Time
        long endTime = 0, duration = 0;

        int[][] board = new int[9][9];
        // Retrieve data from the original board into int[][]
        for (int i = 0; i < 9; i++) {
            System.arraycopy(givenMap[i], 0, board[i], 0, 9);
        }

        if ((map = ConstraintSatisfaction.solve(board))  != null) { // Solved successfully case
            endTime = System.nanoTime(); // End Time
            System.out.println("Solved Sudoku: Case " + (map_index + 1) + " successfully !!!\n");
            ConstraintSatisfaction.printBoard(map);
            System.out.println();
            resultAnnounce.setText("Solved Sudoku: Case " + (map_index + 1) + " successfully !!!");
            initializeGrid();
        } else { // No solution case
            System.out.println("No solution found");
            resultAnnounce.setText("No solution found");
        }

        if(endTime != 0) {
            duration = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        }

        //System.out.println("Start time: " + startTime + ", end time: " + endTime + ", duration: " + duration + " ms");
        System.out.println((map != null) ? "Total executed time take to solve the Sudoku is in: " + duration + " ms" : "");
        executionTime.setText((map != null) ?  duration + " ms\n" : "N/A");
    }

    @FXML
    void reset(){
        resultAnnounce.setText("Click Solve button to get the result");
        executionTime.setText("N/A");
        map = SudokuMap.getAllSudokuMaps[map_index];
        initializeGrid();
    }

    @FXML
    void exit(){
        System.exit(0);
    }
}
