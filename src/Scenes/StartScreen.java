package Scenes;

import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.util.Objects;


import static javafx.application.Application.STYLESHEET_CASPIAN;

public class StartScreen extends MasterScene{
    Object waitingObject = new Object();
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {
        Scene mainMenuScreen;

        //playing the main menu music looped
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/Introduction.mp3")).toExternalForm());

        menuMusic = new MediaPlayer(media);
        menuMusic.setVolume(0.2);
        menuMusic.play();
        menuMusic.setOnEndOfMedia(() -> {
            menuMusic.stop();
            menuMusic.play();
        });
        //setting up the stage to be a maximized, borderless window
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Ball game");
        primaryStage.setMaximized(false);

        //setting up graphics and groups
        Image BackgroundImage = new Image("SceneAssets/StartScreen/StartBackground.png", gameWidth,gameHeight , false, false);
        Pane mainGroup = new Pane();
        Canvas canvas = new Canvas(gameWidth,gameHeight);
        ImageView background = 	new ImageView(BackgroundImage);

        mainGroup.getChildren().addAll(background, canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            int counter = 0;
            @Override
            public void handle(long l) {
                //This allows for a blinking message
                if (counter < 120 ) {
                    //Drawing background image
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }
                else{
                    //Drawing text at bottom
                    gc.setFill(Color.WHITE);
                    gc.setFont(Font.font(STYLESHEET_CASPIAN, 36 * widthAdjust));
                    gc.fillText("Press the space bar to start", 150 * widthAdjust, 800 * heightAdjust);
                }
                //Refresh screen and delay
                counter = (counter + 1) % 240;
            }
        };
        timer.start();

        //setting up scene object and showing scene
        mainMenuScreen = new Scene(mainGroup,gameWidth,gameHeight);
        mainMenuScreen.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.SPACE){
                startingAnimation(gc);
                menuMusic.stop();
                timer.stop();

            }
        });
        primaryStage.setScene(mainMenuScreen);
        primaryStage.show();
        primaryStage.toFront();
        return mainMenuScreen;
    }
    public static void startingAnimation(GraphicsContext gc){
        AnimationTimer timer = new AnimationTimer() {
            int counter = 0;
            int i = 0;
            @Override
            public void handle(long l) {
                if (counter % 10 == 5) {
                    gc.setFill(Color.AQUA);
                    gc.fillRect(0, i * 20, gameWidth, 20 * heightAdjust);
                    i ++;
                }
                else if (counter % 10 == 0){
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, i*20, gameHeight, 20 * heightAdjust);
                    i ++;
                }
                if (i == gameHeight /(20 * heightAdjust)){
                    this.stop();

                    controller.changeScenes(SceneEnums.GAME_SCREEN, null);
                }
                counter = counter + 1;
            }
        };
        timer.start();
    }
}
