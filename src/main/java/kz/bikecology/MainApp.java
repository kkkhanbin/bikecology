package kz.bikecology;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        Label label = new Label("Hello - some gap");               // текстовая метка
//        Button btn = new Button("Button");           // кнопка
//
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//
//                btn.setText("You've clicked!");
//            }
//        });
//
//        btn.setOnAction(e -> {
//
//            btn.setText("You've clicked!");
//        });

//        Group group = new Group(btn);                // вложенный узел Group
//
//        FlowPane root = new FlowPane(label, group);       // корневой узел
//        Scene scene = new Scene(root, 300, 150, Color.BLUE);        // создание Scene

//        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);                          // установка Scene для Stage

        stage.setTitle("Hello JavaFX");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
