module main.java.com.example.thereadingroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires junit;


    opens theReadingRoom to javafx.fxml;
    exports theReadingRoom;
}