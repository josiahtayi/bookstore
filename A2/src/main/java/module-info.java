module com.example.thereadingroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.thereadingroom to javafx.fxml;
    exports com.example.thereadingroom;
}