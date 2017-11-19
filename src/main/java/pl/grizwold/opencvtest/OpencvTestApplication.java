package pl.grizwold.opencvtest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.net.URL;

public class OpencvTestApplication extends Application {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("/OpencvTestApplication.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        BorderPane rootElement = loader.load();

        Scene scene = new Scene(rootElement, 800, 600);

        primaryStage.setTitle("Camera capture");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
