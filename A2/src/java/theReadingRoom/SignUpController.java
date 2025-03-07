package theReadingRoom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
/*TF stands for Text field
 * PF stands for password field
 * LBL stands fo label
 * Btn stands for button
 */

public class SignUpController {
    private final UserDAO userDAO = new UserDAO();
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
            // Check if the username exists
            if (!userDAO.checkUsernameExists(setUsernameTF.getText())) {
                registerUser(); // Call registerUser if username is available
                passwordCheck.setText("Passwords match");
                signUpMessageLabel.setText("User Registration Successful");
            } else {
                signUpMessageLabel.setText("Username already exists. Please choose a different username.");
            }
        } else {
            passwordCheck.setText("Passwords do not match");
        }
    }

    public void registerUser() {
        String username = setUsernameTF.getText().trim();
        String password = setPasswordPF.getText().trim();
        String fname = FnameTF.getText().trim();
        String lname = LnameTF.getText().trim();
        String confirmPassword = confirmPasswordPF.getText().trim();
        // checks if the username already exists
        if (userDAO.checkUsernameExists(username)) {
            passwordCheck.setText("Username already exists. Please choose a different username.");
            return;
        }
        // Check if any fields are empty
        if (username.isEmpty() || password.isEmpty() || fname.isEmpty() || lname.isEmpty() || confirmPassword.isEmpty()) {
            passwordCheck.setText("");
            signUpMessageLabel.setText("Please fill all the fields");
            return;
        }
        // if all checks pass register the user
        if (userDAO.registerUser(username, password, fname, lname)) {
            signUpMessageLabel.setText("User Registration Successful");
        } else {
            signUpMessageLabel.setText("User Registration Failed");
        }
    }

    public void exitBtnOnAction(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
