module org.example.program8 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.program8 to javafx.fxml;
    exports org.example.program8;
}