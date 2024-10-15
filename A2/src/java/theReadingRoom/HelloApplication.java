package theReadingRoom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(getClass().getResource("The Reading Room.png").toExternalForm());
        Parent root = FXMLLoader.load((getClass().getResource("Login.fxml")));
        Scene loginScene = new Scene(root);
        stage.getIcons().add(icon);
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}