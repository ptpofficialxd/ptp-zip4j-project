package se233.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    public static Stage stage;
    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage stage) throws IOException {
        Launcher.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/assets/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Launcher.stage.setTitle("PTP-Archiver v1");
        Launcher.stage.setResizable(false);
        Launcher.stage.setScene(scene);
        Launcher.stage.show();
    }
}