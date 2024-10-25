package theReadingRoom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProfileController {
    private String username;
    private final UserDAO userDAO = new UserDAO();
    @FXML
    private Label heading;
    @FXML
    private Label usernameLbl;
    @FXML
    private Label warningLbl;
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

    public void initialize() {
        this.username = SessionManager.getInstance().getUsername();
        usernameLbl.setText(username + "'s Profile");
    }

    public void saveChanges(ActionEvent event) {
        String newFName = profileFNameTF.getText().trim();
        String newLName = profileLNameTF.getText().trim();
        String newPassword = profilePasswordTF.getText().trim();
        if (newFName.isEmpty() || newLName.isEmpty() || newPassword.isEmpty()) {
            warningLbl.setText("Error: Please fill out all fields");
            profileFNameTF.clear();
            profileLNameTF.clear();
            profilePasswordTF.clear();
            return;
        }

        // Use UserDAO to update user profile
        boolean isUpdated = userDAO.updateUserProfile(username, newFName, newLName, newPassword);
        if (isUpdated) {
            System.out.println("Profile updated successfully");
        } else {
            System.out.println("Error updating profile");
        }
    }

    public void cancel(ActionEvent event) {
        profileFNameTF.clear();
        profileLNameTF.clear();
        profilePasswordTF.clear();
    }

    public void backToDashboard(ActionEvent event) {
        try {
            Stage stage = (Stage) back.getScene().getWindow();
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            // Create and set up the new stage for the dashboard
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}