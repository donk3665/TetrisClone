package Scenes;

import SceneControllers.ControllerTemplate;
import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;


public abstract class MasterScene {
    static ControllerTemplate controller;
    //capturing screen size of monitor to resize application for any screen size
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static double widthAdjust = Math.min(screenSize.getWidth()/1920.0, screenSize.getHeight()/1080.0);
    static double heightAdjust = Math.min(screenSize.getWidth()/1920.0, screenSize.getHeight()/1080.0);
    public static final double gameHeight = 900 * heightAdjust;
    public static final double  gameWidth = 750 * widthAdjust;
    static MediaPlayer menuMusic;

    /**
     * Function to create the scene object.
     */
    public abstract Scene run(Stage primaryStage, SceneTransferData data);
    /**
     * Setting the controller of the scenes
     */
    public static void setController(ControllerTemplate controller1){
        controller = controller1;
    }

    /**
     * This method creates a back button which goes to the previous scene and displays the logo in the middle of the screen
     */
    public AnchorPane addTopAnchorPane(SceneEnums type) {

        //Creates Anchor Pane
        AnchorPane anchorpane = new AnchorPane();

        //Gets the Back Button image from package, sets into ImageView, creates button and sets graphic for button.
        Image BackButton = new Image("SceneAssets/Misc/BackButton.png", 317*widthAdjust, 187*heightAdjust, false, false);
        ImageView backBut = new ImageView(BackButton);
        Button backButton = new Button();
        backButton.setGraphic(backBut);
        backButton.setStyle("-fx-background-color: transparent;");

        anchorpane.getChildren().addAll(backButton);

        // Anchoring the location

        AnchorPane.setTopAnchor(backButton, 0d);
        AnchorPane.setLeftAnchor(backButton, 0d);

        backButton.setOnAction(event-> controller.changeScenes(type, null));

        return anchorpane;
    }
    /**
     * Function to initialize new buttons
     */
    public Button createStandardButton(Image image){
        Button tempButton = new Button();
        ImageView buttonImage = new ImageView(image);
        tempButton.setGraphic(buttonImage);
        tempButton.setStyle("-fx-background-color: transparent;");
        return tempButton;
    }

}
