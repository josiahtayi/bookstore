package theReadingRoom;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class CheckoutController {
    private String username;
    @FXML
    private TextField CardName;
    @FXML
    private TextField CardNumber;
    @FXML
    private TextField CardExp;
    @FXML
    private TextField CardCVV;
    @FXML
    private Button checkoutBtn;
    @FXML
    private Label checkoutStatus;
    @FXML
    private Label billTotal;
    @FXML
    private Button backButton;
    private ObservableList<ShoppingCart> cartItems;
    private double total;
    private final Connection connection = DBConnection.openLink();

    public void initialize() {
        this.username = SessionManager.getInstance().getUsername();
    }

    public boolean cardVerification() {
        String cardName = CardName.getText();
        String cardNumber = CardNumber.getText();
        String cardExp = CardExp.getText();
        String cardCVV = CardCVV.getText();
        if (cardName.isBlank() || cardNumber.isBlank() || cardExp.isBlank() || cardCVV.isBlank()) {
            checkoutStatus.setText("Please Fill in all Fields");
            return false;
        }
        // check the card number length
        if (cardNumber.length() != 16) {
            checkoutStatus.setText("Please Fill in 16 Digits");
            return false;
        }
        //checK the length of the CVV
        if (cardCVV.length() != 3) {
            checkoutStatus.setText("Please Fill in 3 Digits");
            return false;
        }
        //check if the expiry is a later date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        try {
            YearMonth expDate = YearMonth.parse(cardExp, formatter);
            if (expDate.isBefore(YearMonth.now())) {
                checkoutStatus.setText("Card Expiry must be a later Date");
            }
        } catch (DateTimeParseException e) {
            checkoutStatus.setText("Invalid Format. Use MM/YY format");
            return false;
        }
        checkoutStatus.setText("Valid!!");
        return true;
    }

    //displays the cart total as text at the bottom of the table
    public void setBillTotal(double total) {
        billTotal.setText("Total: AU$" + total);
    }

    public void setCartItems(ObservableList<ShoppingCart> cartItems) {
        this.cartItems = cartItems;
    }

    public void checkoutBtnOnAction(ActionEvent event) {
        String orderNumber = "TRR" + System.currentTimeMillis(); // this generates an order number with the prefix The ReadingRoom abbreviated
        // and the time in milliseconds that the order was placed
        if (cardVerification()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Order Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Order number: " + orderNumber);
            alert.showAndWait();
            //stores the order date in a specific format
            String orderDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            // creates a new orders object
            Orders newOrder = new Orders();
            newOrder.setOrder_id(orderNumber);
            newOrder.setOrder_date(orderDate);
            newOrder.setUsername(username);
            newOrder.setOrder_total(total);
            StringBuilder descriptionBuilder = new StringBuilder();
            for (ShoppingCart item : cartItems) {
                descriptionBuilder.append(item.getTitle()).append(" x ").append(item.getQuantity()).append(" x ");
            }
            if (!descriptionBuilder.isEmpty()) {
                descriptionBuilder.setLength(descriptionBuilder.length() - 2);
            }
            newOrder.setOrder_description(descriptionBuilder.toString());
            // add the order to the database
            insertOrder(newOrder);
            updateBookInventory(cartItems);
            updateUserCartStatus();
        } else {
            checkoutStatus.setText("Card verification failed. Please try again.");
        }
    }

    // this method changes the status column to false, so it will no longer be displayed softer a checkout has completed
    public void updateUserCartStatus() {
        String query = "Update UserCart SET Status = 1 WHERE Username = ?";
        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            psmt.setString(1, username);
            int rowsAffected = psmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status updated");
            } else {
                System.out.println("Status not updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //this just deletes the cart item for vibes
        String deleteRows = "DELETE FROM UserCart WHERE Username = ? AND Status = 1";
        try (PreparedStatement psmt = connection.prepareStatement(deleteRows)) {
            psmt.setString(1, username);
            int rowsAffected = psmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status updated");
            } else {
                System.out.println("Status not updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeLink();
        }
    }
    //add the purchase to an order table in the database
    public void insertOrder(Orders order) {
        String sql = "INSERT INTO Orders (Order_ID, Username, Date, Total, Description) Values (?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getOrder_id());
            ps.setString(2, username);
            ps.setString(3, order.getOrder_date());
            ps.setDouble(4, order.getOrder_total());
            ps.setString(5, order.getOrder_description());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) { //just foe debugging can be removed
                System.out.println("Order inserted successfully");
            } else {
                System.out.println("Order insertion failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    //this changes the stock of books after a checkout is completed
    public void updateBookInventory(ObservableList<ShoppingCart> cartItems) {
        String query = "UPDATE Books SET Stock  = Stock - ? WHERE Title = ?";
        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            for (ShoppingCart item : cartItems) {
                psmt.setInt(1, item.getQuantity());
                psmt.setString(2, item.getTitle());
                psmt.executeUpdate();
            }
            System.out.println("Stock Updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findTotal(double total) {
        this.total = total;
    }

    public void backToCart(ActionEvent actionEvent) {
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            // Create and set up the new stage for order management
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            DBConnection.closeLink(); // close the database connection when the scene is closed
        } catch (IOException e) {
            e.getCause();
        }
    }
}