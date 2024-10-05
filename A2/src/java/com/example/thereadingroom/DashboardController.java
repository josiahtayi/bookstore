package com.example.thereadingroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.sqlite.core.DB;

import java.sql.*;


public class DashboardController {

    @FXML
    public Label welcomeLabel;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> topBooksListView;
    @FXML
    private ListView<String> searchResultsListView;


    public void top5Books() {


        DBConnection connectDB = new DBConnection();
        Connection connection = connectDB.openLink();

        String top5query = "SELECT * FROM Books ORDER BY sales DESC LIMIT 5";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(top5query);

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("bookTitle");
                String author = resultSet.getString("author");
                String sales = resultSet.getString("sales");
                String price = resultSet.getString("price");

                
            }


        } catch (Exception e){
            e.printStackTrace();
            e.getCause();

        }



    }





    }

