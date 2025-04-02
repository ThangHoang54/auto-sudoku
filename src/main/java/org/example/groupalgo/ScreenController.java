package org.example.groupalgo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ScreenController {

    @FXML
    private Button Map1;

    @FXML
    private Button Map2;

    @FXML
    private Button Map3;

    @FXML
    private Button custom;

    @FXML
    private Button solve;

    @FXML
    private GridPane sudoku;

    private TextField[][] presentNum = new TextField[9][9];

    int[][] map = SudokuMap.allSudokuMaps[0];

    @FXML
    public void initialize() {
        initializeGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(80, 20);
                cell.setStyle("-fx-font-size: 16; -fx-alignment: center;");

                if (map[row][col] != 0) {
                    cell.setText(String.valueOf(map[row][col]));
                    cell.setEditable(false);
                    cell.setStyle("-fx-background-color: lightgray; -fx-font-size: 16; -fx-alignment: center;");
                }

                // Apply thick border every 3 rows
                if ((row + 1) % 3 == 0) {
                    cell.setStyle(cell.getStyle() + "-fx-border-width: 1px 1px 10px 1px;");
                }

                // Apply thick border every 3 columns
                if ((col + 1) % 3 == 0) {
                    cell.setStyle(cell.getStyle() + "-fx-border-width: 1px 10px 1px 1px;");
                }

                // Apply thick borders for both row and column at 3x3 intersections
                if ((row + 1) % 3 == 0 && (col + 1) % 3 == 0) {
                    cell.setStyle(cell.getStyle() + "-fx-border-width: 1px 10px 10px 1px;");
                }

                presentNum[row][col] = cell;
                sudoku.add(cell, col, row);
            }
        }
            refreshGridLine();

    }

    public void refreshGridLine(){
        // Forcing refresh the gridLine
        sudoku.setGridLinesVisible(false);
        sudoku.setGridLinesVisible(true);
    }

    @FXML
    public void solveMap() {
        int[][] board = new int[9][9];

        // Retrieve data from TextFields into int[][]
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = presentNum[row][col].getText().trim();
                if (!text.isEmpty() && text.matches("\\d")) {
                    board[row][col] = Integer.parseInt(text);
                } else {
                    board[row][col] = 0; // Empty cell
                }

            }
        }

        map = RMIT_Sudoku_Solver.solveSudoku(board);
        initializeGrid();
    }

    public void mapOne(){

    }
    public void mapTwo(){

    }
    public void mapThree(){

    }
    public void customize(){

    }
}
