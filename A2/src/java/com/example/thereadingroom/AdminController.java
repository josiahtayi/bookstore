package com.example.thereadingroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<Book> inventoryTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Integer> salesColumn;
    @FXML
    private Button increaseBtn;
    @FXML
    private Button decreaseBtn;
    @FXML
    private Button logoutBtn;



    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));

        insertBooks();
    }

    public void insertBooks(){
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();
        ObservableList<Book> bookInventory = FXCollections.observableArrayList();

        String query = "SELECT * FROM Books";

        try {
            PreparedStatement psmt = connection.prepareStatement(query);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int stock = rs.getInt("stock");
                int price = rs.getInt("price");
                int sales = rs.getInt("sales");

                bookInventory.add(new Book(title, author, stock, price, sales));
            }
            inventoryTable.setItems(bookInventory);


        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void setWelcomeLabel(String admin) {
        welcomeLabel.setText("Hello, " + admin);
    }

    public void doIncreaseStock(ActionEvent actionEvent) {
    }

    public void doDecreaseStock(ActionEvent actionEvent) {
    }

    public void logout(ActionEvent actionEvent) {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        stage.close();
        showLogin();
    }

    public void showLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root, 520, 400));
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
