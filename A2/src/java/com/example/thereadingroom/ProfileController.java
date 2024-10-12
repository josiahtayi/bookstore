package com.example.thereadingroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProfileController {

    @FXML
    private Label heading;
    @FXML
    private Label usernameLbl;
    @FXML
    private Label profileFName;
    @FXML
    private Label profileLName;
    @FXML
    private Label profilePassword;
    @FXML
    private TextField profileFNameTF;
    @FXML
    private TextField profileLNameTF;
    @FXML
    private TextField profilePasswordTF;
    @FXML
    private Button profileSave;
    @FXML
    private Button profileCancel;
    @FXML
    private Button back;


    public void setUsernameLbl(String usernameLbl) {
        // this should come from the login controller
        this.usernameLbl.setText(usernameLbl);

    }


    public void saveChanges(ActionEvent event) {
        DBConnection dbcon = new DBConnection();
        Connection connection = dbcon.openLink();


        String newFName = profileFNameTF.getText();
        String newLName = profileLNameTF.getText();
        String newPassword = profilePasswordTF.getText();
        String usernameLblText = usernameLbl.getText();

        String updateQuery = "UPDATE Users SET Fname = ?, Lname = ?, Password = ? WHERE Username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newFName);
            preparedStatement.setString(2, newLName);
            preparedStatement.setString(3, newPassword);
            preparedStatement.setString(4, usernameLblText);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("Profile updated successfully");
            } else {
                System.out.println("Username not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating profile: " + e.getMessage());
        } finally {
            dbcon.closeLink();
        }


    }

    public void cancel(ActionEvent event) {
        profileFNameTF.clear();
        profileLNameTF.clear();
        profilePasswordTF.clear();
    }

    public void backToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent dashboard = loader.load();

            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(dashboard));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
