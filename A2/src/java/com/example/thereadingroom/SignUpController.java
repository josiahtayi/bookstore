package com.example.thereadingroom;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/*
 * TF stands for Text field
 * PF stands for password field
 * LBL stands fo label
 * Btn stands for button
 *
 *
 * */

public class SignUpController {

    @FXML
    private Label CreateLBL;
    @FXML
    private Label signUpMessageLabel;
    @FXML
    private Label setUsername;
    @FXML
    private TextField setUsernameTF;
    @FXML
    private Label setPassword;
    @FXML
    private PasswordField setPasswordPF;
    @FXML
    private Label FnameLbl;
    @FXML
    private TextField FnameTF;
    @FXML
    private Label LnameLbl;
    @FXML
    private TextField LnameTF;
    @FXML
    private Button signUpBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Label confirmPassword;
    @FXML
    private PasswordField confirmPasswordPF;
    @FXML
    private Label passwordCheck;


    public void SignUpBtnOnAction(ActionEvent event) {
        if (setPasswordPF.getText().equals(confirmPasswordPF.getText())) {
            registerUser();
            passwordCheck.setText("Passwords match");
            signUpMessageLabel.setText("User Registration Successful");
        } else {
            passwordCheck.setText("Passwords do not match");
        }
    }

    public void registerUser() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.openLink();

        String username = setUsernameTF.getText();
        String password = setPasswordPF.getText();
        String fname = FnameTF.getText();
        String lname = LnameTF.getText();
        String insertToUsers = "INSERT INTO Users(Username, Password, Fname, Lname) VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = connectDB.prepareStatement(insertToUsers);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fname);
            pstmt.setString(4, lname);
            pstmt.executeUpdate();
            signUpMessageLabel.setText("User Registration Successful");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void exitBtnOnAction(ActionEvent e) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.hide();
    }
}
