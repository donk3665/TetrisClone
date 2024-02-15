package main;

import SceneControllers.SceneController;
import SceneControllers.SceneEnums;
import Scenes.MasterScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Entry point to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) {
        SceneController mainController = new SceneController(stage);
        stage.setMinWidth(MasterScene.gameWidth);
        stage.setMinHeight(MasterScene.gameHeight);
        stage.setMaxWidth(MasterScene.gameWidth);
        stage.setMaxHeight(MasterScene.gameHeight);
        mainController.changeScenes(SceneEnums.START_SCREEN, null);
    }
    public void stop() {
        System.exit(0);
    }
}
