package com.example.thereadingroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    private TextField checkoutBtn;
    @FXML
    private Label checkoutStatus;


    public void initialize() {

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
        if (cardNumber.length() != 16){
            checkoutStatus.setText("Please Fill in 16 Digits");
            return false;
        }

        //checK the length of the CVV
        if (cardCVV.length() != 3){
            checkoutStatus.setText("Please Fill in 3 Digits");
            return false;
        }

        //check if the expiry is a later date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        try {
            YearMonth expDate = YearMonth.parse(cardExp, formatter);
            if (expDate.isBefore(YearMonth.now())){
                checkoutStatus.setText("Card Expiry must be a later Date");
            }
        } catch(DateTimeParseException e) {
            checkoutStatus.setText("Invalid Format. Use MM/YY format");
            return false;
        }

        checkoutStatus.setText("Valid!!");
        return true;

    }

    public void checkoutBtnOnAction(ActionEvent event) {
        if (cardVerification()){
            String orderNumber = "TRR" + System.currentTimeMillis();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Order Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Order number: " + orderNumber);
            alert.showAndWait();
        }

    }


}

