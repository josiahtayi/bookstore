package com.example.thereadingroom;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML
    private Label cartLabel;
    @FXML
    private TableView cartTable;
    @FXML
    private TableColumn cartTitle;
    @FXML
    private TableColumn cartPrice;
    @FXML
    private TableColumn cartQty;
    @FXML
    private Button removeBook;
    @FXML
    private Button updateBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button checkOutBtn;

    private ObservableList<ShoppingCart> cartItems;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    cartTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    cartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));


    }
    public void setCartItems(ObservableList<ShoppingCart> cartItems){
        this.cartItems = cartItems;
        cartTable.setItems(cartItems);
    }

    public void handleRemoveBook(ActionEvent actionEvent) {
    }

    public void handleUpdateQuantity(ActionEvent actionEvent) {
    }

    public void checkOutBtn (ActionEvent actionEvent) {
    }


    public void backBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController dashboardController = loader.getController();


            Stage dashboardStage = new Stage();
            dashboardStage.setScene(new Scene(root, 800, 600)); // Adjust the size as needed
            dashboardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void handleCheckout(ActionEvent actionEvent) {
    }
}
