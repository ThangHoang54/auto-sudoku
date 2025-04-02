module org.example.groupalgo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.groupalgo to javafx.fxml;
    exports org.example.groupalgo;
}