module org.example.autosudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.autosudoku to javafx.fxml;
    exports org.example.autosudoku;
}