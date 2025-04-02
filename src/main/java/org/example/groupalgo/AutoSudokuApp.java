package org.example.groupalgo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AutoSudokuApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AutoSudokuApp.class.getResource("sudokuUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 600);
        stage.setTitle("Sudoku Solver");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}