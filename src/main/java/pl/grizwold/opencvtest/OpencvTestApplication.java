package pl.grizwold.opencvtest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.opencv.core.Core;
import pl.grizwold.opencvtest.controllers.MainSceneController;

import java.net.URL;

@Log
public class OpencvTestApplication extends Application {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(OpencvTestApplication.class.getName());

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        log.info("app started");
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("/OpencvTestApplication.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        BorderPane rootElement = loader.load();

        Scene scene = new Scene(rootElement, 880, 700);

        primaryStage.setTitle("Camera capture");
        primaryStage.setScene(scene);
        primaryStage.show();

        MainSceneController controller = loader.getController();
        primaryStage.setOnCloseRequest((event) -> controller.close());
    }
}
