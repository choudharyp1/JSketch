import java.awt.*;
import java.io.Serializable;


interface IView {
    public void updateView();
}

enum tool {
    SELECT, ERASE, LINE, CIRCLE, RECTANGLE, FILL
}

class MyShape implements Serializable{
    Shape shape;
    Color lineColor = Color.BLACK;
    Color fillColor = null;
    Integer thickness = 1;

    MyShape(Shape newShape, Color color, Integer newThickness) {
        shape = newShape;
        lineColor = color;
        thickness = newThickness;
    };
}


