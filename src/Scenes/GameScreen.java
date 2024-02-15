package Scenes;

import Entities.Game;
import SceneControllers.SceneEnums;
import SceneControllers.SceneTransferData;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

import static Entities.Game.font;


public class GameScreen extends MasterScene{
    //Declaring static variables and objects for use in all methods and in main program
    static byte [][] coordinates = new byte [2][4];
    static byte [][] board = new byte[24][10];
    static byte [][] nextPieceCoordinates = new byte[2][4];
    static byte [][] nextPieceBoard= new byte[2][4];
    static byte type = 0;
    static byte colour = 0;
    static boolean finishGame = false;
    static boolean[] fixedRandom = new boolean [28];

    //This 3 dimensional array contains all the coordinates for the 7 tetrominos
    static byte [][][] tetrominos = {{{0,0,0,1},{5,4,6,5}},{{0,0,0,0},{5,4,6,3}},{{1,1,0,0},{5,4,5,6}},{{0,0,1,1},{5,4,5,6}},{{0,0,0,1},{5,4,6,6}},{{0,1,0,0},{5,4,4,6}},{{0,0,1,1},{4,5,5,4}}};

    /*
			Declaring objects and assigning them, as well as emptying
			previously used objects
			 */
    static byte currentType = 0;
    static byte time2 = 0;
    static byte [][] holdPieceCoordinates = new byte[2][4];
    static byte [][] holdPieceBoard = new byte[2][4];
    static int  xShift = 225;
    static byte holdColour = 0;
    static int  yShift = 99;
    static byte [] delay = new byte[3];
    static byte constantSpeedChange = 32;
    static byte normal = constantSpeedChange;
    static boolean swappedAlready = false;
    static boolean holding = false;
    static byte tempType = 0;
    static byte [][] tempArray = new byte[2][4];
    static byte [][] coordinates3 = new byte [2][4];
    static int score = 0;
    static int totalLinesCleared = 0;
    static int linesClearedCounter = 0;
    static double speedMultiplier = 1.0;
    static boolean firstBlock = true;
    static boolean refresh = true;
    static boolean clearing = false;

    static int animationDelay = 0;
    static Image[] img = new Image[10];

    static MediaPlayer[] mediaPlayer = new MediaPlayer [10];

    static Timeline mainGameTimeline;
    int highScore = 0;
    int time = 0;
    static volatile boolean leftKeyDown = false;
    static volatile boolean rightKeyDown = false;
    static volatile boolean downKeyDown = false;
    static volatile boolean spaceKeyDown = false;
    static volatile boolean zKeyDown = false;
    static volatile boolean xKeyDown = false;
    static volatile boolean cKeyDown = false;
    @Override
    public Scene run(Stage primaryStage, SceneTransferData data) {

        Pane mainGroup = new Pane();
        //Setting up images
        Image BackgroundImage = new Image("SceneAssets/GameScreen/MainScreen.png",gameWidth, gameHeight, false, false);
        ImageView background = 	new ImageView(BackgroundImage);

        BorderPane pane = new BorderPane();
        Canvas canvas = new Canvas(gameWidth,gameHeight);
        pane.getChildren().addAll(background,canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        initStuff();
        startGame(gc);
        mainGroup.getChildren().addAll(pane);
        Scene gameScene = new Scene(mainGroup);
        gameScene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.LEFT){
                leftKeyDown = true;
            }
            if (keyCode == KeyCode.RIGHT){
                rightKeyDown = true;
            }
            if (keyCode == KeyCode.DOWN){
                downKeyDown = true;
            }
            if (keyCode == KeyCode.SPACE){
                spaceKeyDown = true;
            }
            if (keyCode == KeyCode.Z){
                zKeyDown = true;
            }
            if (keyCode == KeyCode.X){
                xKeyDown = true;
            }
            if (keyCode == KeyCode.C){
                cKeyDown = true;
            }
            event.consume();

        });
        gameScene.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.LEFT){
                leftKeyDown = false;
            }
            if (keyCode == KeyCode.RIGHT){
                rightKeyDown = false;
            }
            if (keyCode == KeyCode.DOWN){
                downKeyDown = false;
            }
            if (keyCode == KeyCode.SPACE){
                spaceKeyDown = false;
            }
            if (keyCode == KeyCode.Z){
                zKeyDown = false;
            }
            if (keyCode == KeyCode.X){
                xKeyDown = false;
            }
            if (keyCode == KeyCode.C){
                cKeyDown = false;
            }
            event.consume();

        });



        return gameScene;
    }
    public void initStuff(){
        //Placing images into array "img"
        img[0] = new Image("SceneAssets/GameScreen/Grid.png",30,30,false,false);
        img[1] = new Image("SceneAssets/GameScreen/BlueSquare.png",30,30,false,false);
        img[2] = new Image("SceneAssets/GameScreen/YellowSquare.png",30,30,false,false);
        img[3] = new Image("SceneAssets/GameScreen/RedSquare.png",30,30,false,false);
        img[4] = new Image("SceneAssets/GameScreen/GreenSquare.png",30,30,false,false);
        img[5] = new Image("SceneAssets/GameScreen/LineClearSquare.png",30,30,false,false);
        img[6] = new Image("SceneAssets/GameScreen/GreySquare.png",30,30,false,false);
        img[7] = new Image("SceneAssets/GameScreen/StartSquare.png");
        img[8] = new Image("SceneAssets/GameScreen/MainScreen.png");
        img[9] = new Image("SceneAssets/GameScreen/EndScreen.png");

        //These images being placed into "font" are numbers, since the font courier is not available in console
        Game.font[0] = new Image("SceneAssets/GameScreen/Font/Number0.png");
        Game.font[1] = new Image("SceneAssets/GameScreen/Font/Number1.png");
        Game.font[2] = new Image("SceneAssets/GameScreen/Font/Number2.png");
        Game.font[3] = new Image("SceneAssets/GameScreen/Font/Number3.png");
        Game.font[4] = new Image("SceneAssets/GameScreen/Font/Number4.png");
        Game.font[5] = new Image("SceneAssets/GameScreen/Font/Number5.png");
        Game.font[6] = new Image("SceneAssets/GameScreen/Font/Number6.png");
        Game.font[7] = new Image("SceneAssets/GameScreen/Font/Number7.png");
        Game.font[8] = new Image("SceneAssets/GameScreen/Font/Number8.png");
        Game.font[9] = new Image("SceneAssets/GameScreen/Font/Number9.png");

        //Placing mp3s into array "mediaPlayer"
        mediaPlayer[0] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/BackgroundMusic.mp3")).toExternalForm()));
        mediaPlayer[1] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/LineClear.mp3")).toExternalForm()));
        mediaPlayer[2] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/Spin.mp3")).toExternalForm()));
        mediaPlayer[3] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/Lose.mp3")).toExternalForm()));
        mediaPlayer[4] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/Swap.mp3")).toExternalForm()));
        mediaPlayer[5] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/HardDrop.mp3")).toExternalForm()));
        mediaPlayer[6] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/Ending.mp3")).toExternalForm()));
        mediaPlayer[7] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/Introduction.mp3")).toExternalForm()));
        mediaPlayer[8] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/BackgroundMusicSpeed.mp3")).toExternalForm()));
        mediaPlayer[9] = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("/SceneAssets/Music/Place.mp3")).toExternalForm()));

        //Setting the volume of each mp3
        mediaPlayer[0].setVolume(0.2);
        mediaPlayer[1].setVolume(0.5);
        mediaPlayer[2].setVolume(0.3);
        mediaPlayer[3].setVolume(0.5);
        mediaPlayer[4].setVolume(0.5);
        mediaPlayer[5].setVolume(0.3);
        mediaPlayer[6].setVolume(0.8);
        mediaPlayer[7].setVolume(0.4);
        mediaPlayer[8].setVolume(0.3);
        mediaPlayer[9].setVolume(0.7);

        //Making curtain mp3s repeat when called upon to play
        mediaPlayer[0].setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer[8].setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer[2].setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer[7].setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer[6].setCycleCount(MediaPlayer.INDEFINITE);

           /*
			Declaring objects and assigning them, as well as emptying
			previously used objects
			 */
        coordinates = new byte [2][4];
        board = new byte[24][10];
        nextPieceCoordinates = new byte[2][4];
        nextPieceBoard= new byte[2][4];
        type = 0;
        colour = 0;
        finishGame = false;
        fixedRandom = new boolean [28];

        currentType = 0;
        time2 = 0;
        holdPieceCoordinates = new byte[2][4];
        holdPieceBoard = new byte[2][4];
        xShift = 225;
        holdColour = 0;
        yShift = 99;
        delay = new byte[3];
        constantSpeedChange = 32;
        normal = constantSpeedChange;
        swappedAlready = false;
        holding = false;
        tempType = 0;
        tempArray = new byte[2][4];
        coordinates3 = new byte [2][4];
        score = 0;
        totalLinesCleared = 0;
        linesClearedCounter = 0;
        speedMultiplier = 1.0;
        firstBlock = true;
        refresh = true;
        clearing = false;

        animationDelay = 0;

        highScore = 0;
        time = 0;
        leftKeyDown = false;
        rightKeyDown = false;
        downKeyDown = false;
        spaceKeyDown = false;
        zKeyDown = false;
        xKeyDown = false;
        cKeyDown = false;

    }
    public static void startGame(GraphicsContext gc){

        /*
			This block of code generates the first two tetrominos and assigns variable "currentType" with the
			current type of tetromino piece being used
			 */
        randomTetromino();
        currentType = usingTetromino();
        randomTetromino();
        mainGameTimeline = new Timeline(
                new KeyFrame(Duration.millis(10),
                        event -> game(gc)));
        mainGameTimeline.setCycleCount(Timeline.INDEFINITE);
        mainGameTimeline.play();
    }
    public static void drawBoard(GraphicsContext c){
        c.clearRect(0, 0, gameWidth, gameHeight);
					/*
					Draws the main board (center of screen).
					The first two rows of the board are not seen on purpose.
					*/
        for (byte i = 2; i<board.length; i++) {
            for (byte i2 = 0; i2<board[1].length; i2++ ) {
                c.drawImage(img[blockForDraw(i,i2)-1], i2*30+xShift, i*30+yShift);
            }
        }

        //Draws the next piece to the right of the main board
        for (byte i = 0; i<nextPieceBoard.length; i++) {
            for (byte i2 = 0; i2<nextPieceBoard[1].length; i2++ ) {
                if (nextPieceBoard[i][i2] == 0) {
                }
                else {
                    c.drawImage(img[6], i2*30+587, i*30+221);
                }

            }
        }

        //Draws the held piece to the left of the main board
        for (byte i = 0; i<holdPieceBoard.length; i++) {
            for (byte i3 = 0; i3<holdPieceBoard[1].length; i3++ ) {
                if (holdPieceBoard[i][i3] == 0) {
                }
                else {
                    c.drawImage(img[holdColour-1], i3*30+32, i*30+153);
                }

            }
        }

        /*
         * Draws the total lines the user has cleared on top of the main board with the courier font.
         * Font is generated through image array "font" and uses a formula to draw additional digits to the left of the original digit's position
         *
         */
        for (int i = String.valueOf(totalLinesCleared).length()-1; i>=0; i--) {
            c.drawImage(font[Character.getNumericValue(String.valueOf(totalLinesCleared).charAt(i))], 290-(String.valueOf(totalLinesCleared).length()-1-i)*11+(String.valueOf(totalLinesCleared).length())*11, 70);
        }

        /*
         * Draws the score of the user to the middle left of the main board with the courier font.
         * Font is generated through image array "font" and uses a formula to draw additional digits to the left of the original digit's position
         *
         */
        for (int i = String.valueOf(score).length()-1; i>=0; i--) {
            c.drawImage(font[Character.getNumericValue(String.valueOf(score).charAt(i))], 20-(String.valueOf(score).length()-1-i)*11+(String.valueOf(score).length())*11, 300);
        }
    }
    public static boolean fastAudioCheck(){
        /*
				This loop checks for pieces in the upper region of the board, and plays
				a faster version of the original background music if this condition is true
				 */
        for (byte i = 0; i<10; i++) {
            for (byte i2 = 0; i2<board[1].length;i2++) {
                if (board[i][i2] >1) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean checkMovement(){

        if (rightKeyDown && leftKeyDown){

        }
        /*
		Movement right, delay makes the user have greater control of the tetromino they control
		(the tetromino they control moves slower with the delay)
		*/
        if (rightKeyDown && delay[2] <= 0) {
            delay[2] = 10;

            //This boolean changes when the block should not move

            boolean move = true;

					/*
					This loop checks if any of the blocks making up the tetromino they control
					will be out of bounds or will overlap a block not connected to the tetromino
					if shifted 1 unit to the right
					 */
            for (byte i = 0; i<4; i++) {
                if (coordinates[1][i]+1 == board[1].length) {
                    move = false;
                    break;
                }
                if (board[coordinates[0][i]][coordinates[1][i]+1] > 1) {
                    move = false;
                    break;
                }
            }

            //Performs movement, resets the delay, and sets refresh to true, when the condition is met
            if (move) {
                movement(1,1);
                refresh = true;
            }

        }
				/*
				Movement left, delay makes the user have greater control of the tetromino they control
				(the tetromino they control moves slower with the delay)
				 */
        else if (leftKeyDown && delay[2] <= 0) {
            delay[2] = 10;
            //This boolean changes when the block should not move
            boolean move = true;

					/*
					This loop checks if any of the blocks making up the tetromino they control
					will be out of bounds or will overlap a block not connected to the tetromino
					if shifted 1 unit to the left
					 */
            for (byte i = 0; i<4; i++) {
                if (coordinates[1][i]-1 == -1) {
                    move = false;
                    break;
                }
                if (board[coordinates[0][i]][coordinates[1][i]-1] >1) {
                    move = false;
                    break;
                }
            }
            //performs movement, resets the delay, and sets refresh to true, when the condition is met
            if (move) {
                refresh = true;
                movement(1,-1);
            }

        }
        /*
         * Swaps the current piece with the piece being held, if there is no piece
         * in held, the current piece is stored in held and the next piece becomes the current piece.
         * The piece cannot be swapped twice in a row (if you swap a piece, you now must place your
         * swapped piece before being able to swap again)
         */
        if (zKeyDown) {

            //This if statement is invoked when there is no piece being held
            if (!holding) {
                //Holding being false is a one time occurrence each play through
                holding = true;

                //Playing audio
                mediaPlayer[4].stop();
                mediaPlayer[4].play();

                //Removing current piece from board
                for (byte i = 0; i<4; i++){
                    board[coordinates[0][i]][coordinates[1][i]] = 0;
                }

                //TempType holds the number correlated to the tetromino
                tempType = currentType;

                //Putting the current tetromino into the hold array
                for (byte i = 0; i<4; i++){
                    holdPieceCoordinates[0][i] = (byte) (tetrominos[currentType][0][i]+2);;
                    holdPieceCoordinates[1][i] = tetrominos[currentType][1][i];
                }

                //Storing the current tetromino's colour into the hold variable
                holdColour = colour;

                //Putting the next tetromino into the current tetromino and assigning the tetromino's type to "currentType"
                currentType = usingTetromino();

                //Generating a random tetromino
                randomTetromino();

                //Screen will refresh next loop
                refresh = true;

                //Preventing the user from being able to swap 2 times in a row
                swappedAlready = true;

            }
            else if (!swappedAlready) {
                //playing audio
                mediaPlayer[4].stop();
                mediaPlayer[4].play();
                //Removing current piece from board
                for (byte i = 0; i<4; i++){
                    board[coordinates[0][i]][coordinates[1][i]] = 0;
                }
                //emptying array "holdPieceBoard"
                for (byte i = 0; i<holdPieceBoard.length; i++) {
                    for (byte i2 = 0; i2<holdPieceBoard[0].length; i2++) {
                        holdPieceBoard[i][i2] = 0;
                    }
                }

                //putting the coordinates of the held piece into "tempArray"
                for (byte i = 0; i<4; i++){
                    tempArray[0][i] = holdPieceCoordinates[0][i];
                    tempArray[1][i] = holdPieceCoordinates[1][i];
                }

						/*
						Putting the current type of tetromino into the hold array and putting the
						coordinates of the held object into the coordinates of the current object
						 */
                for (byte i = 0; i<4; i++){
                    holdPieceCoordinates[0][i] = (byte) (tetrominos[currentType][0][i]+2);;
                    holdPieceCoordinates[1][i] = tetrominos[currentType][1][i];
                    coordinates[0][i] = tempArray[0][i];
                    coordinates[1][i] = tempArray[1][i];
                }
                //Swapping the types of the tetrominos and the colours of the tetrominos
                byte tempColour = holdColour;
                byte temptype2 = currentType;
                holdColour = colour;
                colour = tempColour;
                currentType = tempType;
                tempType = temptype2;
						/*
						By invoking movement without putting units for the block to move,
						you can put the coordinates of the current object into the board
						 */
                movement(0,0);

                //Screen will refresh next loop
                refresh = true;

                //Preventing the user from being able to swap 2 times in a row
                swappedAlready = true;
            }
            //Placing the holdPieceCoordinates into the holdPieceBoard
            for (byte i = 0; i<4; i++) {
                holdPieceBoard[holdPieceCoordinates [0][i]-2][holdPieceCoordinates [1][i]-3] = 1;
            }
        }
        //Makes the piece move faster down when down key is pressed
        if (downKeyDown) {
            constantSpeedChange = 1;
            //Rewarding user for using this key
            if (time2%4 == 0) {
                score+=speedMultiplier*1;
            }
                /*
                Creating a delay when key is used to place pieces to prevent accidental movement
                (when placing a block, a new block is created at the top instantly, and reacting to this instant change of position is very difficult)
                 */
            if (place(coordinates)) {
                downKeyDown = false;
            }

        }
        else {
            //Changing the time2 back to normal when the key is not pressed
            constantSpeedChange = normal;
        }
				/*
				Rotating block counterclockwise, delay makes the user have greater control of the tetromino they control
				(the tetromino they control rotates slower with the delay)
				Also, currentType 6 indicates a square tetromino, and square tetrominos do not spin.
				 */
        if (xKeyDown && currentType!=6 && delay[1] <= 0) {
            delay[1] = 30;
					/*
					If a rotation is sucessful, reset the delay, play the audio,
					and assign true to refresh so screen will refresh next loop
					 */
            if (rotation(-1)) {
//                mediaPlayer[2].stop();
//                mediaPlayer[2].play();
                refresh= true;
            }

        }
				/*
				Rotating block clockwise, delay makes the user have greater control of the tetromino they control
				(the tetromino they control rotates slower with the delay)
				Also, currentType 6 indicates a square tetromino, and square tetrominos do not spin.
				 */
        if (cKeyDown && currentType!=6 && delay[1] <= 0) {
            delay[1] = 30;
            /*
					If a rotation is successful, reset the delay, play the audio,
					and assign true to refresh so screen will refresh next loop
					 */
            if (rotation(1) ) {
                refresh= true;
//                mediaPlayer[2].stop();
//                mediaPlayer[2].play();
            }
        }

        boolean down = false;
        //Places the block to the furthermost position at the bottom, delay makes sure that the user doesn't accidentally place multiple tetrominos
        if (spaceKeyDown && delay[0] <= 0) {
            delay[0] = 20;
            //Play audio
            mediaPlayer[5].stop();
            mediaPlayer[5].play();

            //Set refresh to true so screen will refresh next loop
            refresh = true;

            byte counter = 0;

            //Putting current coordinates of tetromino into temporary array
            for (byte i = 0; i<4; i++) {
                coordinates3[0][i] = coordinates [0][i];
                coordinates3[1][i] = coordinates [1][i];
            }

            //Counting how far the piece can be placed downward without overlapping already filled board positions
            while (!place(coordinates3)) {
                counter++;
                for (byte i = 0; i<4; i++) {
                    coordinates3[0][i]++;
                }
            }

            //Rewarding user for using this game mechanic
            score += counter*speedMultiplier;

            //Moving tetromino with counter to furthermost position at bottom
            movement(0,counter);

            //Making sure the piece is instantly locked
            down = true;
            spaceKeyDown = false;
        }
        for (int i = 0; i<3; i++){
            if (delay[i] > 0){
                delay[i] --;
            }
        }

        return down;
    }
    static int animationTimer = 0;
    static byte animationCounter = 0;
    public static boolean checkClears(GraphicsContext c){
        //This loop finds the rows which need to be cleared, and displays an animation across those rows
        boolean stall = false;
        for (byte i2 = 0; i2<board[1].length; i2++) {
            boolean clear = false;
            for (byte i3 = 0; i3<board.length; i3++) {
                //Placing -1 into the rest of the row which needs to be cleared
                if (board[i3][0] == -1 ){
                    board[i3][i2] = -1;
                    clear = true;
                    stall = true;
                }
            }
            //Refreshing the board every 40 milliseconds to create an animated effect
            if (clear) {
                //Playing sound
                mediaPlayer[1].play();
                for (byte i4 = 2; i4<board.length; i4++) {
                    c.drawImage(img[blockForDraw(i4,animationCounter)-1], animationCounter*30+xShift, i4*30+yShift);
                }
            }

        }
        if (animationCounter<board[1].length-1 && animationTimer % 5 == 0){
            animationCounter +=1;
        }
        return stall;
    }
    public static void endingAnimation(GraphicsContext gc){
        AnimationTimer timer = new AnimationTimer() {
            int counter = 0;
            int i = 0;
            @Override
            public void handle(long l) {
                if (counter >= 120) {
                    if (counter % 10 == 5) {
                        gc.setFill(Color.BLUE);
                        gc.fillRect(0, i * 20, gameWidth, 20 * heightAdjust);
                        i++;
                    } else if (counter % 10 == 0) {
                        gc.setFill(Color.CYAN);
                        gc.fillRect(0, i * 20, gameHeight, 20 * heightAdjust);
                        i++;
                    }
                    if (i == gameHeight / (20 * heightAdjust)) {
                        //Stop sound
                        mediaPlayer[3].stop();
                        this.stop();
                        controller.changeScenes(SceneEnums.END_SCREEN, null);
                    }
                }
                counter = counter + 1;
            }
        };
        timer.start();
    }
    public static void game(GraphicsContext c){
        if (finishGame){
            //Change highScore is score is greater
            if (score>Game.highScore) {
                Game.highScore = score;
            }
            //Display board and piece which lead to loss
            for (byte i = 2; i<board.length; i++) {
                for (byte i2 = 0; i2<board[1].length; i2++ ) {
                    if (board[i][i2] == 1) {
                        c.drawImage(img[6], i2*30+xShift, i*30+yShift);
                    }
                    else {
                        c.drawImage(img[blockForDraw(i,i2)-1], i2*30+xShift, i*30+yShift);
                    }
                }
            }
            //Play sound and stop music
            mediaPlayer[8].stop();
            mediaPlayer[3].play();
            mainGameTimeline.stop();
            endingAnimation(c);
            return;
        }

        if (animationDelay > 0){
            checkClears(c);
            animationTimer += 1;
            animationDelay -= 1;
            return;
        }
        if (clearing && animationDelay == 0){
            animationCounter = 0;
            animationTimer = 0;

            clearing = false;
        }

        if (fastAudioCheck()){
            //This if statement prevents these methods from being invoked if the music is already playing
            if (mediaPlayer[8].getStatus() != MediaPlayer.Status.PLAYING) {
                mediaPlayer[0].stop();
                mediaPlayer[8].play();
            }
        }
        else {
            mediaPlayer[8].stop();
            mediaPlayer[0].play();
        }
        drawBoard(c);

        boolean down = checkMovement();

				/*
				This if checks for line clears every few loops (this exact amount is indicated by ConstantSpeedChange).
				This allows for the user to move the piece when it touches the bottom for a few milliseconds, rather than locking the piece from user control instantly.
				The variable down is affected only by the user entering a key press of the space bar, and this allows for the piece to lock instantly when the space bar is pressed.
				 */
        if (time2%constantSpeedChange == 0 || down) {

            //If the tetromino can no longer move down, this if statement is entered
            if (place(coordinates)) {

                //A different sound is played when the piece is placed by soft dropping, or naturally
                if (!down) {
                    mediaPlayer[9].stop();
                    mediaPlayer[9].play();
                }

                //Placing the tetromino, with it's respective colour
                for (byte i = 0; i<4; i++) {
                    board[coordinates[0][i]][coordinates[1][i]] = colour;
                }
                boolean rowFilled = false;
                //Checking for line clears across the entire board with variable "counter"
                for (byte i = 0; i<board.length; i++) {
                    byte counter = 0;
                    for (byte i2 = 0; i2<board[1].length; i2++) {
                        if (board[i][i2] != 0 && board[i][i2] != -1) {
                            counter++;
                        }
                    }
                    //If a row is filled, replace the row's first value with -1
                    if (counter == board[1].length) {
                        for (byte i2 = 0; i2<board[1].length; i2++) {
                            board[i][0] = -1;
                        }
                        rowFilled = true;
                    }
                }
                if (rowFilled){
                    clearing = true;
                    animationDelay = 75;
                    drawBoard(c);
                    return;
                }
                //Clearing the lines which are filled and pushing the lines above downward
                int push = 0;
                linesClearedCounter = 0;
                for (byte i2 = (byte) (board.length-1); i2>=0; i2--) {
                    if (board[i2][0] == -1) {
                        push++;
                        linesClearedCounter++;
                    }
                    else {
                        for (byte i3 = 0; i3<board[1].length; i3++) {
                            board[i2+push][i3] = board[i2][i3];

                        }
                    }
                }
                //Formulas to calculate scores and speed after line clear
                totalLinesCleared += linesClearedCounter;
                Game.linesCleared = totalLinesCleared;
                score += (100)*Math.pow(linesClearedCounter,2)*speedMultiplier;
                int number = totalLinesCleared/10;
                constantSpeedChange= (byte) Math.round(32/Math.pow(1.5,number));
                normal = constantSpeedChange;
                speedMultiplier = Math.pow(1.8, number);

                //Changing the tetromino and generating a new tetromino
                currentType = usingTetromino();
                randomTetromino();

                //Stopping audio
                mediaPlayer[1].stop();

                //Tetrominos can now be swapped again
                swappedAlready = false;

            }
            else {
                //move tetromino down by 1 unit
                movement(0,1);
            }
            refresh = true;

        }
        //the first block will move when program is ran since 0 modulus any number will yield 0, so move first block back up by 1 unit
        if (firstBlock) {
            movement(0,-1);
            firstBlock = false;
        }
        //increase time
        time2++;
//        //increase all delays by 1
//        for (byte i = 0; i<delay.length;i++) {
//            delay[i]++;
//        }


    }
    public static void resetControls(){
        leftKeyDown = false;

        rightKeyDown = false;

        downKeyDown = false;

        spaceKeyDown = false;

        zKeyDown = false;

        xKeyDown = false;

        cKeyDown = false;

    }


    /**
     * This method rotate the tetromino that the user is in control of
     * @param num - direction to rotate, positive equals clockwise, negative counterclockwise
     * @return change - boolean which indicates if rotation was sucessful
     */
    public static boolean rotation (int num) {
        boolean change = true;
        byte [][] coordinates2 = new byte [2][4];

        //Rotates each piece around a center-piece
        for (byte i = 1; i<4; i++) {
            byte tempX = 0;
            byte tempY = 0;

            //Formula for rotation
            if (coordinates [0][i]>coordinates[0][0]) {
                tempX = (byte) (-num*Math.abs(coordinates[0][i]-coordinates [0][0]));
            }
            else {
                tempX = (byte) (num*Math.abs(coordinates[0][i]-coordinates [0][0]));
            }
            if (coordinates [1][i]>coordinates[1][0]) {
                tempY =  (byte) (num*Math.abs(coordinates[1][i]-coordinates [1][0]));
            }
            else {
                tempY = (byte) (-num*Math.abs(coordinates[1][i]-coordinates [1][0]));
            }
            tempY += coordinates[0][0];
            tempX += coordinates[1][0];

            //Array "coordinates2" contains the tetromino's possible new location
            coordinates2[1][i] = tempX;
            coordinates2[0][i] = tempY;

            //if any block in the tetromino is out of bounds or overlaps another block in the array, do not change the location of the tetromino
            if (tempY>=board.length || tempY<0 || tempX>=board[0].length || tempX<0) {
                change = false;
            }
            if (change != false && board[tempY][tempX] > 1) {
                change = false;
            }

        }
        //changes tetromino location
        if (change == true) {
            //clearing old location from board
            for (byte i = 0; i<4; i++) {

                board[coordinates[0][i]][coordinates[1][i]] = 0;

            }
            //putting new coordinates in array "coordinates"
            for (byte i = 1; i<4; i++){
                coordinates[0][i] = coordinates2[0][i];

                coordinates[1][i] = coordinates2[1][i];
            }
            //putting coordinates into board
            for (byte i = 0; i<4; i++) {
                board[coordinates[0][i]][coordinates[1][i]] = 1;
            }
        }
        //returns a boolean indicating if the change was sucessful
        return change;

    }
    /**
     * Generates random tetromino and puts coordinates into array "nextPieceCoordinates"
     * Randomness of tetromino is weighed, if you get a piece once, you have a less likely chance of getting it again
     */
    public static void randomTetromino() {

        nextPieceBoard = new byte [2][4];

		/*
		Formulas for making weighed randomness, however, getting the piece you want is not guaranteed
		Array of boolean values which reset and make getting the same piece over
		and over unlikely, but does not make getting and specific piece guaranteed after
		a certain number of piece generations
		*/
        byte counter = 0;
        for (byte i = 0; i<fixedRandom.length; i++) {
            if (fixedRandom[i] == false){
                counter++;
            }
        }
        if (counter>15) {
            for (byte i = 0; i<fixedRandom.length; i++) {
                fixedRandom[i] = false;
            }
        }
        do {
            type = (byte)(Math.random()*fixedRandom.length);
        }while (fixedRandom[type] == true);
        fixedRandom[type] = true;

        while (type>=7) {
            type = (byte) (type-7);
        }
        colour = (byte)(Math.random()*4+2);

		/*
		Putting coordinates of the next tetromino into nextPieceCoordinates.
		The y coordinates of the piece is purposely shifted 2 units down to allow the user
		to spin the tetromino as soon as it spawns.
		(Having the tetromino right at the top (row 0) prevents the user from spinning as
		spinning may lead to the piece coordinates being out of bounds)
		*/
        for (byte i = 0; i<4; i++) {
            nextPieceCoordinates [0][i] = (byte) (tetrominos[type][0][i]+2);
            nextPieceCoordinates [1][i] = tetrominos[type][1][i];

        }
        //Puting coordinates of nextPieceCoordinates into nextPieceBoard
        for (byte i = 0; i<4; i++) {
            nextPieceBoard[nextPieceCoordinates [0][i]-2][nextPieceCoordinates [1][i]-3] = 1;
        }
    }
    /**
     * This method puts the next piece coordinates into current coordinates
     *
     */
    public static byte usingTetromino() {
        //Putting nextPieceCoordinates into current coordinates
        for (byte i = 0; i<4; i++) {
            coordinates[0][i] = nextPieceCoordinates [0][i];
            coordinates[1][i] = nextPieceCoordinates [1][i];
        }
        boolean finish = false;

        //puts the coordinates into the board and checks if the game is lost
        for (byte i = 0; i<4; i++) {
            //when the blocks overlap the blocks on the board, the game is lost
            if (board[coordinates[0][i]][coordinates[1][i]] != 0) {
                finish = true;
            }
            board[coordinates[0][i]][coordinates[1][i]] = 1;
        }
        //finishGame is true when game is lost
        if (finish == true) {
            finishGame = true;
        }

        return type;
    }
    /**
     * Moves the tetromino
     * @param axis - axis in which the tetromino is moving, 1 is x axis, 0 is y axis
     * @param amount
     */
    public static void movement(int axis, int amount) {
        //removing current coordinates from board
        for (byte i = 0; i<4; i++) {
            board[coordinates[0][i]][coordinates[1][i]] = 0;
        }
        //changing coordinates
        for (byte i = 0; i<4; i++) {
            coordinates[axis][i]+=amount;
        }
        //putting new coordinates on board
        for (byte i = 0; i<4; i++) {
            board[coordinates[0][i]][coordinates[1][i]] = 1;
        }

    }
    /**
     * Checks if a tetromino should be placed
     * @param array of coordinates of the tetromino
     * @return a boolean on whether the tetromino should be placed
     */
    public static boolean place(byte[][] array) {
        boolean place = false;

        //checks if there are blocks under the tetromino or if the tetromino is at the bottom of the board
        for (byte i = 0; i<4; i++) {
            if (array[0][i]+1 == board.length) {
                place = true;
            }
            else if (board[array[0][i]+1][array[1][i]] >1) {
                place = true;
            }

        }
        return place;
    }
    /**
     * This method finds out what kind of block to draw at certain coordinates in the board
     * @param i - row coordinate
     * @param i2 - column coordinate
     * @return what kind of block should be drawn
     */
    public static byte blockForDraw(byte i, byte i2) {
        byte block  = 0;

        //generates number based on number in board
        if (board[i][i2] >1) {
            block = board[i][i2];
        }
        else if (board[i][i2] == 1) {
            block  = colour;
        }
        else if (board[i][i2] == -1) {
            block  = 6;
        }
        else {
            block  = 1;
        }
        return block ;
    }
}
