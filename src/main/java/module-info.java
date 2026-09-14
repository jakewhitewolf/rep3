module com.example.rep3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.rep3 to javafx.fxml;
    exports com.example.rep3;
}