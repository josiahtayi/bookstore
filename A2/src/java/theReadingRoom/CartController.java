package theReadingRoom;

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
import java.util.function.Function;

public class CartController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String username;
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
    private Function<ShoppingCart, Boolean> stockCheckCallback;  // Stock check callback

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cartTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        this.username = SessionManager.getInstance().getUsername();
        cartLabel.setText(username + "'s Cart");
    }

    public void setCartItems(ObservableList<ShoppingCart> cartItems) {
        this.cartItems = cartItems;
        cartTable.setItems(cartItems);
        setTotal(); // Initialize the total when setting cart items
    }

    public void setTotal() {
        double total = 0.0;
        if (cartItems != null) {
            for (ShoppingCart cart : cartItems) {
                total += cart.getPrice() * cart.getQuantity();
            }
        }
        cartTotal.setText(String.format("Total: $%.2f", total));
    }

    public void doRemoveBook(ActionEvent actionEvent) {
        ShoppingCart selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            cartTable.refresh();
            setTotal();
        } else {
            showAlert(Alert.AlertType.ERROR, "No item Selected", "Please select an item to remove");
        }
    }

    public void increaseQuantityBtn(ActionEvent actionEvent) {
        ShoppingCart selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectedItem.setQuantity(selectedItem.getQuantity() + 1);
            if (stockCheckCallback != null && !stockCheckCallback.apply(selectedItem)) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                        "There is not enough stock for " + selectedItem.getTitle() + ".");
                selectedItem.setQuantity(selectedItem.getQuantity() - 1); // Revert the change
                return;
            }
            cartTable.refresh();
            setTotal();
        } else {
            showAlert(Alert.AlertType.ERROR, "No item Selected", "Please select an item to increase quantity");
        }
    }

    public void decreaseQuantityBtn(ActionEvent actionEvent) {
        ShoppingCart selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int quantity = selectedItem.getQuantity();
            if (quantity > 1) {
                selectedItem.setQuantity(quantity - 1);
                cartTable.refresh();
                setTotal();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Can't decrease quantity below zero");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "No item Selected", "Please select an item to decrease quantity");
        }
    }

    public void goToCheckout(ActionEvent actionEvent) {
        // Ensure stock is available for all items in the cart before proceeding to checkout
        for (ShoppingCart item : cartItems) {
            if (stockCheckCallback != null && !stockCheckCallback.apply(item)) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                        "There is not enough stock for " + item.getTitle() + ". Please update the quantity.");
                return;
            }
        }

        try {
            stage = (Stage) checkOutBtn.getScene().getWindow();
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("Checkout.fxml"));
            root = loader.load();

            CheckoutController checkoutController = loader.getController();
            double total = calculateTotal();
            checkoutController.setBillTotal(total);
            checkoutController.findTotal(total);
            checkoutController.setCartItems(cartItems);

            // Create and set up the new stage for checkout
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

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

    public void backToDashboard(ActionEvent event) {
        try {
            stage = (Stage) backBtn.getScene().getWindow();
            FXMLLoader loader;

            loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            root = loader.load();

            // Create and set up the new stage for order management
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCartLabel(String username) {
        cartLabel.setText(username + "'s Cart");
    }

    public void setStockCheckCallback(Function<ShoppingCart, Boolean> stockCheckCallback) {
        this.stockCheckCallback = stockCheckCallback;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
