package internseason.scheduler.gui;

import internseason.scheduler.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXVisualisation extends Application {



    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            loader.setController(new MainScreen(Main.config));
            root = loader.load();


        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Scheduler");
        Scene currentScene = new Scene(root, 1280, 800);
        //currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add("internseason/scheduler/gui/stylesheets/SkyBlue.css");
//        currentScene.getStylesheets().add("/main/resources/internseason/scheduler/gui/stylesheets/AppleGreen.css");
        primaryStage.setScene(currentScene);
        primaryStage.show();
    }
}


