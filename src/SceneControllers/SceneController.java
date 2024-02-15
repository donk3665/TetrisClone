package SceneControllers;

import Scenes.MasterScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController extends ControllerTemplate{

    SceneFactory factory;

    public SceneController(Stage currentStage) {
        super(currentStage);
        factory = new SceneFactory(currentStage);
        MasterScene.setController(this);
    }

    public void changeScenes(SceneEnums sceneType, SceneTransferData data){
        Scene newScene = factory.getScene(sceneType, data);
        currentStage.setScene(newScene);
        currentStage.show();
    }

}
