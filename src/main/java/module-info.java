module com.example.rep3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.rep3 to javafx.fxml;
    opens com.example.rep3.model to javafx.base;
    exports com.example.rep3;
}