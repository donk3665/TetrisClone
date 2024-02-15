package SceneControllers;

import Scenes.ControlScreen;
import Scenes.EndScreen;
import Scenes.GameScreen;
import Scenes.StartScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneFactory {
    Stage stage;
    Scene startScreen;
    Scene controlScreen;
    Scene gameScreen;
    Scene endScreen;

    public SceneFactory(Stage stage) {
        this.stage = stage;
        startScreen = new StartScreen().run(stage,null);
        controlScreen = new ControlScreen().run(stage, null);
    }
    public Scene getScene(SceneEnums sceneType, SceneTransferData data){
        switch (sceneType){
            case START_SCREEN -> {
                return startScreen;
            }
            case CONTROL_SCREEN -> {
                return controlScreen;
            }
            case GAME_SCREEN -> {
                return new GameScreen().run(stage,null);
            }
            case END_SCREEN -> {
                return new EndScreen().run(stage, null);
            }
        }
        return null;
    }

}
