import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;

public class FlappyBird extends Application {
    public static void main(String[] args) {
        launch();
    }

    double count;
    boolean gameStopped;
    boolean inEndScreen;

    Rectangle backRound;
    Rectangle sign;
    StackPane mainStackPane;
    VBox gameVbox;
    Pane gameField;
    VBox menuBarVbox;
    HBox menuBar;
    Label deathCounter;


    StackPane endScreenStackPane;
    VBox endScreenVbox;
    HBox endScreenHbox;
    Rectangle deathScreenBackground;
    Label score;
    Label best;
    Button okBtn;

    Bird bird;
    Ground ground;
    AnimationTimer timer;
    ArrayList<Pipe> pipes;
    ArrayList<Ground> grounds;


    @Override
    public void start(Stage primaryStage) throws Exception {
        bird = new Bird(250);
        pipes = new ArrayList<>();
        grounds = new ArrayList<>();

        count = 0;
        gameStopped = true;
        inEndScreen = false;

        mainStackPane = new StackPane();
        Scene root = new Scene(mainStackPane);

        gameVbox = new VBox();
        gameField = new Pane();
        menuBarVbox = new VBox();
        menuBar = new HBox();
        deathCounter = new Label("0");
        deathCounter.setAlignment(Pos.CENTER);
        deathCounter.setFont(Font.loadFont("file:res/font/8bitFont.TTF",25));
        deathCounter.setTextFill(Color.WHITE);
        deathCounter.getStyleClass().add("outline");
        root.getStylesheets().addAll(getClass().getResource(
                "outline.css"
        ).toExternalForm());

        ground = new Ground();
        ground.x = 0;
        ground.width = 500;
        ground.setX(ground.x);
        ground.setWidth(ground.width);
        grounds.add(ground);
        backRound = new Rectangle(0, 0, 500, 500);
        setImage(backRound,"flappy-bird-background");
        sign = new Rectangle(200, 250, 100, 100);
        setImage(sign, "sign");
        menuBar.getChildren().addAll(deathCounter);
        menuBar.setAlignment(Pos.TOP_CENTER);
        menuBarVbox.getChildren().addAll(menuBar);
        mainStackPane.getChildren().addAll(gameVbox, menuBarVbox);
        VBox.setMargin(menuBar, new Insets(30,0,0,0));
        gameVbox.getChildren().addAll(gameField);
        gameField.getChildren().addAll(backRound, bird, sign, ground);

        endScreenStackPane = new StackPane();
        deathScreenBackground = new Rectangle(150, 150, 300, 150);
        setImage(deathScreenBackground, "scoreScreen");
        endScreenVbox = new VBox();
        score = new Label(String.valueOf((int)count));
        score.setFont(Font.loadFont("file:res/font/8bitFont.TTF",20));
        score.setTextFill(Color.WHITE);
        score.getStyleClass().add("outline");
        root.getStylesheets().addAll(getClass().getResource(
                "outline.css"
        ).toExternalForm());

        best = new Label();
        best.setFont(Font.loadFont("file:res/font/8bitFont.TTF",20));
        best.setTextFill(Color.WHITE);
        best.getStyleClass().add("outline");
        root.getStylesheets().addAll(getClass().getResource(
                "outline.css"
        ).toExternalForm());

        okBtn = new Button("Ok");
        okBtn.setFont(Font.loadFont("file:res/font/8bitFont.TTF",20));
        okBtn.setTextFill(Color.WHITE);
        okBtn.setBackground( new Background(new BackgroundFill(Color.RED,
                new CornerRadii(5,false),
                new Insets(0,0,0,0))));


        endScreenHbox = new HBox();
        endScreenStackPane.getChildren().addAll(deathScreenBackground, endScreenHbox);
        endScreenVbox.getChildren().addAll(score, best, okBtn);
        endScreenVbox.setAlignment(Pos.CENTER_RIGHT);
        endScreenVbox.setSpacing(40);
        endScreenHbox.getChildren().addAll(endScreenVbox);

        HBox.setMargin(endScreenVbox, new Insets(100,0,0,100));
        VBox.setMargin(okBtn, new Insets(0,200,0,0));

        timer = new AnimationTimer() {
            private double nextTime = 0;

            @Override
            public void handle(long now) {

                if (now > nextTime) {
                    long temp = (long) (1000000000);
                    nextTime = now + (temp * 2);
                    generateNewPipes();
                    menuBar.toFront();
                }
                for (Pipe p : pipes) {
                    p.movePipe();
                }
                for (Ground g : grounds) {
                    g.moveGround();
                }

                bird.fly();
                bird.checkCollisions(pipes, grounds, FlappyBird.this);
                bird.checkPoint(pipes, FlappyBird.this);
            }
        };

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getText().contentEquals("w")) {
                    if (!inEndScreen) {
                        if (gameStopped) {
                            timer.start();
                            gameField.getChildren().remove(sign);
                        }
                    }
                    bird.flap(FlappyBird.this);
                    System.out.println("flap");
                }
            }
        });

        okBtn.setOnAction((event -> {
            mainStackPane.getChildren().remove(endScreenStackPane);
            count = 0;
            deathCounter.setText(String.valueOf((int)count));

            inEndScreen = false;
            gameField.getChildren().clear();
            pipes.clear();
            grounds.clear();
            ground.x = 0;
            ground.width = 500;
            ground.setX(ground.x);
            ground.setWidth(ground.width);
            grounds.add(ground);
            gameField.getChildren().addAll(backRound, bird, sign, ground);
        }));


        primaryStage.setWidth(500);
        primaryStage.setScene(root);
        primaryStage.setTitle("Flappy Bird");
        primaryStage.show();
    }

    public void generateNewPipes() {
        double randY = (Math.random() * 280) + 170;
        Pipe p = new Pipe(randY + 23, 500 - randY - 23, false);
        pipes.add(p);
        gameField.getChildren().addAll(p);
        p.setImage("/Users/brian/IdeaProjects/Flappy Bird/res/pipeBody.png");

        Pipe p2 = new Pipe(randY, 23, false);
        pipes.add(p2);
        gameField.getChildren().addAll(p2);
        p2.setImage("/Users/brian/IdeaProjects/Flappy Bird/res/pipeTip.png");
        p2.setRotate(180);
        p2.setNodeOrientation(RIGHT_TO_LEFT);

        Pipe p3 = new Pipe(0, randY - 120 - 23, false);
        pipes.add(p3);
        gameField.getChildren().addAll(p3);
        p3.setImage("/Users/brian/IdeaProjects/Flappy Bird/res/pipeBody.png");

        Pipe p4 = new Pipe(randY - 120 - 23, 23, false);
        pipes.add(p4);
        gameField.getChildren().addAll(p4);
        p4.setImage("/Users/brian/IdeaProjects/Flappy Bird/res/pipeTip.png");

        Ground g = new Ground();
        grounds.add(g);
        gameField.getChildren().addAll(g);
    }

    public void reset() {
        best.setText(String.valueOf((int)setHighScore()));
        score.setText(String.valueOf((int)count));
        mainStackPane.getChildren().addAll(endScreenStackPane);
        inEndScreen = true;
    }

    public double setHighScore(){
        String fileContents = "";
        try {
            File file = new File("res/highScore");
            Scanner scan = new Scanner(file);
            while (scan.hasNext()){
                fileContents += scan.next();
            }
            if(Double.parseDouble(fileContents) < count){
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                fileContents = String.valueOf(count);
                fileWriter.append(fileContents);
                fileWriter.close();
            }
        } catch(IOException e){
            System.out.println(e);
        }
        return Double.parseDouble(fileContents);
    }
    public void playSound(String fileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println(fileName);
            System.out.println("ex = " + ex);
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
    public void setImage(Shape shape, String name){

        try {
            FileInputStream input = new FileInputStream(
                    "/Users/brian/IdeaProjects/Flappy Bird/res/" + name + ".png");
            Image image = new Image(input);
            ImagePattern imagePattern = new ImagePattern(image);
            shape.setFill(imagePattern);
        } catch (Exception e) {
            System.out.println("Image Not Found");
        }
        ;
    }
}
