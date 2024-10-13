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

import java.sql.*;

public class LoginController {
    @FXML
    private Button exitBtn;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Button loginBtn;
    @FXML
    TextField usernameTF;
    @FXML
    private PasswordField passwordPF;
    @FXML
    private Button signUpBtn;

    public void loginBtnOnAction(ActionEvent e) {
        if (!usernameTF.getText().isBlank() && !passwordPF.getText().isBlank()) {
            if (validateLogin()) {
                openDashBoard();
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                stage.hide();
            } else {
                loginMessageLabel.setText("Invalid username or password. Please try again.");
                passwordPF.clear();
                usernameTF.clear();
            }
        } else {
            loginMessageLabel.setText("Please enter your username and password");
        }
    }

    public void exitBtnOnAction(ActionEvent e) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.hide();
    }

    public void signUpBtnOnAction(ActionEvent e) {
        signUpForm();

    }

    public boolean validateLogin() {
        DBConnection connect = new DBConnection();
        Connection connectDB = connect.openLink();

        // Use a prepared statement with placeholders to prevent SQL injection
        String verifyLogin = "SELECT count(1) FROM Users WHERE Username = ? AND Password = ?";
        boolean loginResult = false;

        try {
            // Prepare the SQL statement
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);

            // Set the parameters for the placeholders
            preparedStatement.setString(1, usernameTF.getText());
            preparedStatement.setString(2, passwordPF.getText());

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the user exists
            if (resultSet.next() && resultSet.getInt(1) == 1) {
                loginMessageLabel.setText("You are logged in.");
                loginResult = true;
            } else {
                loginMessageLabel.setText("Invalid username or password.");
            }
            // Close resources
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connect.closeLink();  // Always close the connection
        }
        return loginResult;
    }

    public void signUpForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            Stage signUpStage = new Stage();
            signUpStage.initStyle(StageStyle.UNDECORATED);
            signUpStage.setScene(new Scene(root, 600, 600));
            signUpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void openDashBoard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.setWelcomeLabel(usernameTF.getText());
            dashboardController.setUsername(usernameTF.getText());


            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("Checkout.fxml"));
            Parent root1 = loader1.load();
            CheckoutController checkoutController = loader1.getController();
            checkoutController.setUsername(usernameTF.getText());

            Stage dashboardStage = new Stage();
            dashboardStage.initStyle(StageStyle.UNDECORATED);
            dashboardStage.setScene(new Scene(root, 600, 600));
            dashboardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }
}
