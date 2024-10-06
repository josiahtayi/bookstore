package com.example.thereadingroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;

public class ShoppingCartController {

    @FXML
    private Label cartLabel;
    @FXML
    private TableView cartTable;
    @FXML
    private Button removeBook;
    @FXML
    private Button updateBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button checkOutBtn;


    public void handleRemoveBook(ActionEvent actionEvent) {
    }

    public void handleUpdateQuantity(ActionEvent actionEvent) {
    }

    public void handleCheckout(ActionEvent actionEvent) {
    }


    public void backBtnOnAction(ActionEvent actionEvent) {

        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }
}
