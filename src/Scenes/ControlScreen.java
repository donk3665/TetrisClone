package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControlScreen extends MasterScene{
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {


        //Setting up images
        Image BackgroundImage = new Image("SceneAssets/ControlScreen/Controls.png", 1536*widthAdjust,864*heightAdjust , false, false);
        ImageView Background = 	new ImageView(BackgroundImage);


        // Play Button
        Image PlayButton = new Image("SceneAssets/StartScreen/PlayButton.png", 442*widthAdjust, 238*heightAdjust, false, false);
        Button playButton = new Button();
        ImageView PlayBut = new ImageView(PlayButton);
        playButton.setGraphic(PlayBut);
        playButton.setStyle("-fx-background-color: transparent;");

        //Button function
        playButton.setOnAction(event -> controller.changeScenes(SceneEnums.GAME_SCREEN, null));

        // Adding Buttons to a Grid Pane
        GridPane grid = new GridPane();
        grid.add(playButton, 0,0,1,1);
        grid.setVgap(10);

        // Setting Location of Grid Pane
        grid.setTranslateX(850*widthAdjust);
        grid.setTranslateY(275*heightAdjust);

        // Creating main group to add into scene and adding back button
        Pane mainGroup = new Pane();
        mainGroup.getChildren().addAll(Background, addTopAnchorPane(SceneEnums.START_SCREEN), grid);


        return new Scene(mainGroup);
    }
}
