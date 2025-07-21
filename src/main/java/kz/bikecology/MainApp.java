package kz.bikecology;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main.fxml")));
        Scene scene = new Scene(root);

        // Configuration
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApp.class.getResourceAsStream("/images/logo.png"))));
        stage.setTitle("БиК Эколоджи");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
