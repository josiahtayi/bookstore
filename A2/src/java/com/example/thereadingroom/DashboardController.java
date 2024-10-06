package com.example.thereadingroom;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DashboardController {

    @FXML
    public Label welcomeLabel;
    @FXML
    public ListView searchResultsListView;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> top5table;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> salesColumn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button shoppingCartBtn;
    @FXML
    private Button addToCartBtn;

    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));

        loadTop5Books();
    }

    public void loadTop5Books() {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();
        ObservableList<Book> top5Books = FXCollections.observableArrayList();

        String query = "SELECT Title, Author, Sales FROM Books ORDER BY sales DESC LIMIT 5";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                int sales = rs.getInt("Sales");

                top5Books.add(new Book(title, author,0,0,sales));
            }
            top5table.setItems(top5Books);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbcon.closeLink();
        }


















    }





    public void setWelcomeLabel(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }


    public void logOutBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) logOutBtn.getScene().getWindow();
        stage.close();
        showLogin();

    }

    public void showLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage signUpStage = new Stage();
            signUpStage.initStyle(StageStyle.UNDECORATED);
            signUpStage.setScene(new Scene(root, 520, 400));
            signUpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }


    public void shoppingCartBtnOnAction(ActionEvent actionEvent) {
        openShoppingCart();
    }

    public void openShoppingCart() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ShoppingCart.fxml"));
            Stage shoppingCart = new Stage();
            shoppingCart.initStyle(StageStyle.UNDECORATED);
            shoppingCart.setScene(new Scene(root, 520, 400));
            shoppingCart.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }


}

