package com.example.thereadingroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection DBLink;

    public Connection openLink() {
        String databaseName = "";
        String databaseUser = "";
        String databasePassword = "";
        String databaseURL = "jdbc:sqlite:./src/TheDatabase.db";


        try {
            // add class.forname
            DBLink = DriverManager.getConnection(databaseURL);
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


