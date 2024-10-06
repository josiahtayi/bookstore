package com.example.thereadingroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class DashboardController {

    @FXML
    public Label welcomeLabel;
    @FXML
    public ListView searchResultsListView;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<String> top5table;
    @FXML
    private Button searchBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button shoppingCartBtn;


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

