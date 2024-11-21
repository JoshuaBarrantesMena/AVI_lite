package com.josh.avi_lite;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage myStage;

    @Override
    public void start(Stage stage) throws IOException {
        myStage = stage;
        scene = new Scene(loadFXML("Login"), 360, 360);
        myStage.setScene(scene);
        myStage.show();
    }

    public static void setRoot(String fxml, int x, int y) throws IOException {
        scene = new Scene(loadFXML(fxml), x, y);
        myStage.setScene(scene);
        myStage.centerOnScreen();
        myStage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}