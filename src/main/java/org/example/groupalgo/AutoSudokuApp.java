package org.example.groupalgo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author Group05
 */

public class AutoSudokuApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML layout file for the Sudoku UI.
        FXMLLoader fxmlLoader = new FXMLLoader(AutoSudokuApp.class.getResource("sudokuUI.fxml"));
        // Create a new scene with the loaded layout, with width 950 and height 600.
        Scene scene = new Scene(fxmlLoader.load(), 950, 600);
        stage.setTitle("Sudoku Solver");    // Set the title of the window (stage).
        stage.setResizable(false);          // Prevent the window from being resizable to keep the layout fixed.
        stage.setScene(scene);              // Set the scene on the stage and display the window.
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // Launch the JavaFX application, calling the start() method.
    }
}