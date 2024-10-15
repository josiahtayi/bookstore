package theReadingRoom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection DBLink;

    public static Connection openLink() {
        String databaseURL = "jdbc:sqlite:TheDatabase.db";


        try {
            // add class.forname
            DBLink = DriverManager.getConnection(databaseURL);
            System.out.println("Connected to the database successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DBLink;
    }

    public static void closeLink() {
        if (DBLink != null) {
            try {
                DBLink.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


