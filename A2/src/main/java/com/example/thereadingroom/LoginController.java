package com.example.thereadingroom;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {
    @FXML
    private Button exitBtn;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTF;
    @FXML
    private PasswordField passwordPF;
    @FXML
    private Button signUpBtn;

    public void loginBtnOnAction(ActionEvent e) {
        if (usernameTF.getText().isBlank() == false && passwordPF.getText().isBlank() == false) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter your username and password");
        }
    }

    public void exitBtnOnAction(ActionEvent e) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    public void signUpBtnOnAction(ActionEvent e) {
        signUpForm();
    }

    public void validateLogin() {
        DBConnection connect = new DBConnection();
        Connection connectDB = connect.openLink();

        String VerifyLogin = "SELECT count(1) From Users Where Username = '" + usernameTF.getText() + "' AND Password = '" + passwordPF.getText() + "'";
        ;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(VerifyLogin);

            while (resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    loginMessageLabel.setText("You are logged in.");
                } else {
                    loginMessageLabel.setText("You are not logged in, Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signUpForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            Stage signUpStage = new Stage();
            signUpStage.initStyle(StageStyle.UNDECORATED);
            signUpStage.setScene(new Scene(root, 520, 400));
            signUpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }
}
