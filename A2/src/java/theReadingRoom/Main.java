package theReadingRoom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("The Reading Room.png")).toExternalForm()); //creates a new image object
        // and sets the image as the icon
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("Login.fxml"))));
        Scene loginScene = new Scene(root);
        stage.getIcons().add(icon);// changes the icon of the application.
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}