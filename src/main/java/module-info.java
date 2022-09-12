module com.example.rinscript1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rinscript1 to javafx.fxml;
    exports com.example.rinscript1;
}