package org.example.groupalgo;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ScreenController {

    @FXML
    public Text execution_time;
    @FXML
    public Text loading_text;
    @FXML
    public Text result_text;
    @FXML
    private ChoiceBox<String> cb_mapCase;
    @FXML
    private GridPane sudoku_board;

    private final TextField[][] presentNum = new TextField[9][9];
    private int map_index;
    private int[][] map = SudokuMap.getAllSudokuMaps[0];
    private int[][] givenMap = SudokuMap.getAllSudokuMaps[0];

    @FXML
    public void initialize() {
        initializeGrid();

        cb_mapCase.setOnAction(_ -> {
            result_text.setVisible(false); // hide text
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

                presentNum[row][col] = cell;
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

        if ((map = RMIT_Sudoku_Solver.solveSudoku(board))  != null) {
            System.out.println("Solved Sudoku: Case " + (map_index + 1) + " successfully !!!\n");
            RMIT_Sudoku_Solver.printBoard(map);
            System.out.println();
            result_text.setText("Solved Sudoku: Case " + (map_index + 1) + " successfully");
            result_text.setVisible(true); // show result_text
            initializeGrid();
        } else {
            System.out.println("No solution found");
            result_text.setText("No solution found");
            result_text.setVisible(true); // show result_text
        }
    }

    @FXML
    void reset(){
        result_text.setVisible(false); // hide text
        map = SudokuMap.getAllSudokuMaps[map_index];
        initializeGrid();
    }

    @FXML
    void exit(){
        System.exit(0);
    }
}
