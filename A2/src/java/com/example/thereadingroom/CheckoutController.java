package com.example.thereadingroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class CheckoutController {

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
    private ObservableList<ShoppingCart> cartItems;
    private double total;
    private String username;


    public void initialize() {


    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setBillTotal(double total) {
        billTotal.setText(String.format("Total: $%.2f", total));
    }

    public void setCartItems(ObservableList<ShoppingCart> cartItems) {
        this.cartItems = cartItems;
    }

    public void checkoutBtnOnAction(ActionEvent event) {
        String orderNumber = "TRR" + System.currentTimeMillis();

        if (cardVerification()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Order Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Order number: " + orderNumber);
            alert.showAndWait();

            Orders newOrder = new Orders();
            newOrder.setOrder_id(orderNumber);
            newOrder.setOrder_date(new Date());
            newOrder.setCustomer_name(username);
            newOrder.setOrder_total(total);

            StringBuilder descriptionBuilder = new StringBuilder();
            for (ShoppingCart item : cartItems) { // Now you can use cartItems
                descriptionBuilder.append(item.getTitle())
                        .append(" x ")
                        .append(item.getQuantity());
            }
            if (descriptionBuilder.length() > 0) {
                descriptionBuilder.setLength(descriptionBuilder.length() - 2);
            }
            newOrder.setOrder_description(descriptionBuilder.toString());
            // add the order to the database
            insertOrder(newOrder);
        } else {
            checkoutStatus.setText("Card verification failed. Please try again.");
        }
    }

    public void insertOrder(Orders order) {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();

        String sql = "INSERT INTO Orders (Order_ID, Username, Date, Total, Description) Values (?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getOrder_id());
            ps.setString(2, order.getCustomer_name());
            ps.setDate(3, order.getOrder_date());
            ps.setDouble(4, order.getOrder_total());
            ps.setString(5, order.getOrder_description());

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }


    public double findTotal(double total) {
        this.total = total;
        return total;
    }


}

