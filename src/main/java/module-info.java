module com.example.rep3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rep3 to javafx.fxml;
    exports com.example.rep3;
}