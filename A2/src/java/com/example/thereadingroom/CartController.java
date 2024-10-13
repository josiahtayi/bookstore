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
    private TableView<ShoppingCart> cartTable;
    @FXML
    private TableColumn<ShoppingCart, String> cartTitle;
    @FXML
    private TableColumn<ShoppingCart, Double> cartPrice;
    @FXML
    private TableColumn<ShoppingCart, Integer> cartQty;
    @FXML
    private Button removeBook;
    @FXML
    private Label cartTotal;
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

    public void setCartItems(ObservableList<ShoppingCart> cartItems) {
        this.cartItems = cartItems;
        cartTable.setItems(cartItems);
        setTotal(); // Initialize the total when setting cart items
    }


    public void setTotal() {
        double total = 0.0;
        // Ensure cartItems is not null before accessing
        if (cartItems != null) {
            for (ShoppingCart cart : cartItems) {
                total += cart.getPrice() * cart.getQuantity();
            }
        }

        // Update the cartTotal label with the formatted total amount
        cartTotal.setText(String.format("Total: $%.2f", total));
    }


    public void doRemoveBook(ActionEvent actionEvent) {
        ShoppingCart selectedItem = (ShoppingCart) cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            cartTable.refresh();
            setTotal(); // Update the total after an item is removed
        } else {
            showAlert(Alert.AlertType.ERROR, "No item Selected", "Please select an item to remove");
        }
    }


    public void increaseQuantityBtn(ActionEvent actionEvent) {
        ShoppingCart selectedItem = (ShoppingCart) cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectedItem.setQuantity(selectedItem.getQuantity() + 1);
            cartTable.refresh();
            setTotal(); // Update the total after increasing quantity
        } else {
            showAlert(Alert.AlertType.ERROR, "No item Selected", "Please select an item to increase quantity");
        }
    }

    public void decreaseQuantityBtn(ActionEvent actionEvent) {
        ShoppingCart selectedItem = (ShoppingCart) cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int quantity = selectedItem.getQuantity();
            if (quantity > 1) {
                selectedItem.setQuantity(quantity - 1);
                cartTable.refresh();
                setTotal(); // Update the total after decreasing quantity
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Can't decrease quantity below zero");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "No item Selected", "Please select an item to decrease quantity");
        }
    }


    public void goToCheckout(ActionEvent actionEvent) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Checkout.fxml"));
            Parent root = loader.load();

            CheckoutController checkoutController = loader.getController();

            double total = calculateTotal();
            checkoutController.setBillTotal(total);
            checkoutController.findTotal(total);
            checkoutController.setCartItems(cartItems);

            Stage checkoutStage = new Stage();
            checkoutStage.setScene(new Scene(root, 800, 600));
            checkoutStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double calculateTotal() {
        double total = 0.0;

        if (cartItems != null) {
            for (ShoppingCart cart : cartItems) {
                total += cart.getPrice() * cart.getQuantity();
            }
        }
        return total;

    }


    public void backBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController dashboardController = loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setCartLabel(String username) {
        cartLabel.setText(username + "'s Cart");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
