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
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
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

    private ObservableList<ShoppingCart> cartItems = FXCollections.observableArrayList();
    private Function<ShoppingCart, Boolean> stockCheckCallback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns
        cartTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Load the username and set it on the cart label
        this.username = SessionManager.getInstance().getUsername();
        cartLabel.setText(username + "'s Cart");

        // Load cart items from the database
        loadCartItemsFromDatabase();
    }


    public void loadCartItemsFromDatabase() {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();

        if (connection == null) {
            System.out.println("Database connection failed.");
            return;
        }
        ObservableList<ShoppingCart> loadedCartItems = FXCollections.observableArrayList();
        String query = "SELECT Title, Price, Quantity FROM UserCart WHERE Username = ? AND Status = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, this.username);
            pstmt.setBoolean(2, false); // Load only items with Status = FALSE
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                double price = resultSet.getDouble("Price");
                int quantity = resultSet.getInt("Quantity");
                ShoppingCart cartItem = new ShoppingCart(title, quantity, price);
                loadedCartItems.add(cartItem);
            }

            System.out.println("Loaded items: " + loadedCartItems.size());
            setCartItems(loadedCartItems);
            cartTable.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load cart items from database.");
        } finally {
            dbcon.closeLink();
        }
    }


    public void setCartItems(ObservableList<ShoppingCart> cartItems) {
        this.cartItems = cartItems;
        cartTable.setItems(cartItems);
        setTotal();
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
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();

        ShoppingCart selectedItem = (ShoppingCart) cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            String query = "DELETE FROM UserCart WHERE Username = ? AND Title = ?";
            try(PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, selectedItem.getTitle());
                pstmt.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("Database Error");
            }
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
            updateItemInDB(selectedItem);


            cartTable.refresh();
            setTotal();
        } else {
            showAlert(Alert.AlertType.ERROR, "No item Selected", "Please select an item to increase quantity");
        }
    }

    public void decreaseQuantityBtn(ActionEvent actionEvent) {
        ShoppingCart selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.getQuantity() > 1) {
            selectedItem.setQuantity(selectedItem.getQuantity() - 1);

            updateItemInDB(selectedItem);

            cartTable.refresh();
            setTotal();
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Can't decrease quantity below zero");
        }
    }

    public void updateItemInDB(ShoppingCart cartItem){
       DBConnection dbcon = new DBConnection();
       Connection connection = dbcon.openLink();

       String query = "UPDATE UserCart SET Quantity = ? WHERE Username = ? AND Title = ?";
       try (PreparedStatement psmt = connection.prepareStatement(query)){
           psmt.setInt(1, cartItem.getQuantity());
           psmt.setString(2, this.username);
           psmt.setString(3, cartItem.getTitle());
           psmt.executeUpdate();
       } catch (SQLException e){
           e.printStackTrace();
       }



    }

    public void goToCheckout(ActionEvent actionEvent) {
        // Ensure stock is available for all items in the cart before proceeding to checkout
        for (ShoppingCart item : cartItems) {
            if (stockCheckCallback != null && !stockCheckCallback.apply(item)) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                        "Sorry " + item.getTitle() + " is out of stock");
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
