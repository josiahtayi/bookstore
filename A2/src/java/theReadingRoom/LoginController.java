package theReadingRoom;


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

import java.io.IOException;
import java.sql.*;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private UserDAO userDAO = new UserDAO();
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

    public void loginBtnOnAction(ActionEvent e) throws IOException {
        if (!usernameTF.getText().isBlank() && !passwordPF.getText().isBlank()) {
            if (validateLogin()) {
                //sets the current user in the Session manager
                SessionManager.getInstance().setUsername(usernameTF.getText());
                // if the validation is successfull the dashboard is opened
                openDashBoard();
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
        stage.close();
    }

    public void signUpBtnOnAction(ActionEvent e) throws IOException {
        signUpForm();

    }

    public boolean validateLogin() {
        return userDAO.validateLogin(usernameTF.getText(), passwordPF.getText());
    }

    public void signUpForm() throws IOException {
            root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            stage = (Stage) signUpBtn.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }

    public void openDashBoard() {
        try {
            stage = (Stage) loginBtn.getScene().getWindow();
            FXMLLoader loader;

            boolean isAdmin = "admin".equalsIgnoreCase(usernameTF.getText());

            if (isAdmin) {
                loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
                root = loader.load();

            } else {
                loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                root = loader.load();

            }
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



