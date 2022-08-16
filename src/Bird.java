import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Bird extends Rectangle {
    double y;
    double uV;
    final double width = 40;
    final double height = 28;
    final double x = 150;
    final double gravity = .25;
    ImagePattern imagePattern;
    Ellipse elipse;

    public Bird(double y){
        setX(x);
        setHeight(height);
        setWidth(width);
        this.y = y;
        setY(y);
        try {
            FileInputStream input = new FileInputStream("/Users/brian/IdeaProjects/Flappy Bird/res/flappyBird.png");
            Image image = new Image(input);
            imagePattern = new ImagePattern(image);
            setFill(imagePattern);
        } catch (Exception e){

        }

        elipse = new Ellipse(x + (width/2),y + (height/2),(width/2)-3,(height/2)-3);
        elipse.setFill(Color.BLACK);

    }
    public void flap(FlappyBird main){
        uV = -6;
        main.playSound("res/whoosh.wav");
    }
    public void fly(){
        y += uV;
        uV += gravity;
        setY(y);
        setRotate(uV*6);
        elipse.setCenterY(y+ (height/2));
        elipse.setRotate(uV*6);
    }
//    public void checkCollisions(ArrayList<Pipe> pipes, ArrayList<Ground> grounds, FlappyBird main) {
//        double top = y;
//        double bot = y + height;
//        double left = x;
//        double right = x + width;
//        Circle circle = new Circle();
//        if(top < -1){
//            uV = 2;
//            y = 1;
//            setY(y);
//        }
//
//
//        for (Pipe pipe : pipes) {
//            double wTop = pipe.y;
//            double wBot = pipe.y + pipe.height;
//            double wLeft = pipe.x;
//            double wRight = pipe.x + pipe.width;
//
//            double xOverlap = Math.min(right, wRight) - Math.max(left, wLeft);
//            double yOverlap = Math.min(bot, wBot) - Math.max(top, wTop);
//
//            if (xOverlap > 0 && yOverlap > 0) {
//                main.playSound("res/death.wav");
//                main.timer.stop();
//                setY(250);
//                y = 250;
//                uV = 0;
//                setRotate(0);
//                main.reset();
//            }
//        }
//        for (Ground ground : grounds) {
//            double wTop = ground.y;
//            double wBot = ground.y + ground.height;
//            double wLeft = ground.x;
//            double wRight = ground.x + ground.width;
//
//            double xOverlap = Math.min(right, wRight) - Math.max(left, wLeft);
//            double yOverlap = Math.min(bot, wBot) - Math.max(top, wTop);
//
//            if (xOverlap > 0 && yOverlap > 0) {
//                main.playSound("res/death.wav");
//                main.timer.stop();
//                setY(250);
//                y = 250;
//                uV = 0;
//                setRotate(0);
//                main.reset();
//            }
//        }
//    }
    public void checkCollisions(ArrayList<Pipe> pipes, ArrayList<Ground> grounds, FlappyBird main) {
        if(y + (height/2) - elipse.getRadiusY() < -1){
            uV = 2;
            y = 1;
            setY(y);
        }
//        if(!main.gameField.getChildren().contains(elipse)){
//            main.gameField.getChildren().add(elipse);
//        }

        for (Pipe pipe : pipes) {

            if(elipse.intersects(pipe.x, pipe.y,pipe.width, pipe.height)){
                main.playSound("res/death.wav");
                main.timer.stop();
                try{
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e){
                    
                }
                setY(250);
                y = 250;
                uV = 0;
                setRotate(0);
                main.reset();
            }
        }
        for (Ground ground : grounds) {

            if(elipse.intersects(ground.x, ground.y,ground.width, ground.height)){
                main.playSound("res/death.wav");
                main.timer.stop();
                try{
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e){

                }
                setY(250);
                y = 250;
                uV = 0;
                setRotate(0);
                main.reset();
            }
        }
    }
    public void checkPoint(ArrayList<Pipe> pipes, FlappyBird main){
        for(Pipe pipe: pipes){
            if(!pipe.isCounted){
                if(pipe.x < x){
                    pipe.isCounted = true;
                    main.count += .25;
                    main.deathCounter.setText(String.valueOf((int)main.count));
                    main.playSound("res/point.wav");
                }
            }
        }
    }

}
