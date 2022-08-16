import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;

public class Pipe extends Rectangle {
    double x = 500;
    double y;
    double hv = -2;
    double height;
    double width = 50;
    boolean isCounted;
    public Pipe(double y, double height, boolean isCounted){
        setX(x);
        setY(y);
        this.y = y;
        setWidth(width);
        setHeight(height);
        this.height = height;
        this.isCounted = isCounted;
    }
    public void movePipe(){
        x += hv;
        setX(x);
    }
    public void setImage(String filePath){
        try {
            FileInputStream input = new FileInputStream(filePath);
            Image image = new Image(input);
            ImagePattern imagePattern = new ImagePattern(image);
            setFill(imagePattern);
        } catch (Exception e){

        }
    }
}
