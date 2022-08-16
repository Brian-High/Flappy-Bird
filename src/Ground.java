import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;

public class Ground extends Rectangle {

    double x;
    double y;
    double width;
    final double height = 50;
    double hV = -2;
    public Ground() {
        x = 500;
        y = 470;
        width = 360;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        try {
            FileInputStream input = new FileInputStream(
                    "/Users/brian/IdeaProjects/Flappy Bird/res/flappy-bird-ground.png");
            Image image = new Image(input);
            ImagePattern imagePattern = new ImagePattern(image);
            setFill(imagePattern);
        } catch (Exception e){
        }
    }
    public void moveGround(){
        x += hV;
        setX(x);
    }
}
