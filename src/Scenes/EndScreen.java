package Scenes;

import Entities.Game;
import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.application.Application.STYLESHEET_CASPIAN;

public class EndScreen extends MasterScene{
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {
        Scene endScreen;

        //playing the main menu music looped
//        Media media = new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Main_Menu.mp3")).toExternalForm());
//
//        menuMusic = new MediaPlayer(media);
//        menuMusic.setVolume(0.05);
//        menuMusic.play();
//        menuMusic.setOnEndOfMedia(() -> {
//            menuMusic.stop();
//            menuMusic.play();
//        });

        //setting up graphics and groups
        Image BackgroundImage = new Image("SceneAssets/EndScreen/EndScreen.png", gameWidth,gameHeight , false, false);
        Pane mainGroup = new Pane();
        Canvas canvas = new Canvas(gameWidth,gameHeight);
        ImageView background = 	new ImageView(BackgroundImage);

        mainGroup.getChildren().addAll(background, canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(STYLESHEET_CASPIAN, 36 * widthAdjust));


        AnimationTimer timer = new AnimationTimer() {
            int counter = 0;
            @Override
            public void handle(long l) {
                //Drawing background image
                /*
                 * Draws the total lines the user has cleared on top of the main board with the courier font.
                 * Font is generated through image array "font" and uses a formula to draw additional digits to the left of the original digit's position
                 *
                 */
                for (int i = String.valueOf(Game.linesCleared).length()-1; i>=0; i--) {
                    gc.drawImage(Game.font[Character.getNumericValue(String.valueOf(Game.linesCleared).charAt(i))], 224-(String.valueOf(Game.linesCleared).length()-1-i)*11+(String.valueOf(Game.linesCleared).length())*11, 104);
                }
                /*
                 * Draws the score of the user to the middle left of the main board with the courier font.
                 * Font is generated through image array "font" and uses a formula to draw additional digits to the left of the original digit's position
                 *
                 */
                for (int i = String.valueOf(Game.highScore).length()-1; i>=0; i--) {
                    gc.drawImage(Game.font[Character.getNumericValue(String.valueOf(Game.highScore).charAt(i))], 224-(String.valueOf(Game.highScore).length()-1-i)*11+(String.valueOf(Game.highScore).length())*11, 101);
                }

                //This allows for a blinking message
                if (counter < 120 ) {
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }
                else{
                    //Drawing text at bottom
                    gc.fillText("Press the space bar to play again", 125 * widthAdjust, 700 * heightAdjust);
                    gc.fillText("Press the escape key to exit", 125 * widthAdjust, 800 * heightAdjust);
                }
                //Refresh screen and delay
                counter = (counter + 1) % 240;
            }
        };
        timer.start();

        //setting up scene object and showing scene
        endScreen = new Scene(mainGroup,gameWidth,gameHeight);
        endScreen.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.SPACE){
                startingAnimation(gc);
                timer.stop();
            }
            else if (key.getCode() == KeyCode.ESCAPE){
                Platform.exit();
            }
        });
        primaryStage.setScene(endScreen);
        primaryStage.show();
        primaryStage.toFront();
        return endScreen;
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
