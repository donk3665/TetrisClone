package SceneControllers;

import javafx.stage.Stage;

public abstract class ControllerTemplate {
    Stage currentStage;
    public ControllerTemplate(Stage currentStage){
        this.currentStage = currentStage;
    }
    public abstract void changeScenes(SceneEnums scene, SceneTransferData data);
}
