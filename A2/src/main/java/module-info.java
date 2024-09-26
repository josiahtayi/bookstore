module com.example.thereadingroom {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.thereadingroom to javafx.fxml;
    exports com.example.thereadingroom;
}