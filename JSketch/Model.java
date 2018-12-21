import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class Model{
    //Attributes that are needed to be stored:
    private tool currentTool;
    private Color currentColor;
    private Integer currentThickness;
    private MyShape currentShape;
    private MyShape selectedShape;

    private ArrayList<MyShape> listOfShapes = new ArrayList<MyShape>();
    private ArrayList<IView> views = new ArrayList<IView>();

    private double offsetX;
    private double offsetY;


    //Initialize the private variables.
    Model(){
        currentTool = tool.SELECT;
        currentColor = Color.BLACK;
        currentThickness = 0;
        currentShape = null;
        selectedShape = null;
    }

    //Notify all observers (views) to update their views based on the IView interface's updateView function.
    void notifyObservers(){
        for (IView view : this.views){
            view.updateView();
        }
    }

    void addView(IView view){
        views.add(view);
        view.updateView();
    }

    //Setters

    public void setCurrentTool(tool newTool){
        currentTool = newTool;
        notifyObservers();

    }

    public void setCurrentColor(Color newColor){
        currentColor = newColor;
        notifyObservers();
    }

    public void setCurrentThickness(Integer newThickness){
        currentThickness = newThickness;
        notifyObservers();

    }

    public void setSelectedShapeThickness(Integer newThickness){
        if (selectedShape != null){
            selectedShape.thickness = newThickness;
        }
        notifyObservers();
    }

    public void setSelectedShapeColor(Color newColor){
        if (selectedShape != null){
            selectedShape.lineColor = newColor;
        }
        notifyObservers();
    }

    public void setCurrentShape(MyShape newCurrentShape){
        currentShape = newCurrentShape;
        notifyObservers();
    }

    public void setNewDrawing(){
        listOfShapes = new ArrayList<MyShape>();
        notifyObservers();
    }

    public void loadSavedDrawing(ArrayList<MyShape> loadedList){
        listOfShapes = loadedList;
        notifyObservers();
    }

    public void addNewShape(int x1, int y1, int x2, int y2){
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int width = Math.abs(x1 - x2);
        int length = Math.abs(y1 - y2);

        if (currentTool == tool.LINE){
            currentShape = new MyShape(new Line2D.Float(x1, y1, x2, y2), currentColor, currentThickness);
        }
        else if (currentTool == tool.RECTANGLE){
            currentShape = new MyShape(new Rectangle2D.Float(x, y, width, length), currentColor, currentThickness);
        }
        else if (currentTool == tool.CIRCLE){
            currentShape = new MyShape(new Ellipse2D.Float(x, y, width, length), currentColor, currentThickness);
        }
        notifyObservers();
    }

    public void addShape(){
        if (currentShape != null){
            listOfShapes.add(currentShape);
        }
        currentShape = null;
        notifyObservers();
    }

    public void clearSelectedShape(){
        selectedShape = null;
        notifyObservers();
    }

    //Getters

    public tool getCurrentTool(){
        return currentTool;
    }

    public Color getCurrentColor(){
        return currentColor;
    }

    public Integer getCurrentThickness(){
        return currentThickness;
    }

    public MyShape getCurrentShape() {
        return currentShape;
    }

    public MyShape getSelectedShape(){
        return selectedShape;
    }

    public ArrayList<MyShape> getListOfShapes(){
        return listOfShapes;
    }

    public void setSelectedShape(int x, int y){
        for (int i = listOfShapes.size() - 1; i >= 0; i--){
            if (listOfShapes.get(i).shape.contains(x, y) ||
                    listOfShapes.get(i).shape.intersects(x-1,y-1,2,2)){
                clearSelectedShape();
                selectedShape = listOfShapes.get(i);
                currentColor = selectedShape.lineColor;
                currentThickness = selectedShape.thickness;
                break;
            }
        }

        if (selectedShape != null){
            offsetX = selectedShape.shape.getBounds().getX() - x;
            offsetY = selectedShape.shape.getBounds().getY() - y;
        }
        notifyObservers();
    }

    public void moveSelectedShape(int x, int y){
        if (selectedShape != null){
            double oldX = selectedShape.shape.getBounds().getX();
            double oldY = selectedShape.shape.getBounds().getY();

            AffineTransform transform = new AffineTransform();
            transform.translate(x - oldX + offsetX, y - oldY + offsetY);
            selectedShape.shape = transform.createTransformedShape(selectedShape.shape);
        }
        notifyObservers();
    }

    public void eraseSelectedShape(int x, int y){
        for (int i = listOfShapes.size() - 1; i >= 0; i--) {
            if (listOfShapes.get(i).shape.contains(x, y) ||
                    listOfShapes.get(i).shape.intersects(x - 1, y - 1, 2, 2)) {
                listOfShapes.remove(i);
                break;
            }
        }
        notifyObservers();
    }

    public void fillSelectedShape(int x, int y){
        for (int i = listOfShapes.size() - 1; i >= 0; i--) {
            if (listOfShapes.get(i).shape.contains(x, y) ||
                    listOfShapes.get(i).shape.intersects(x - 1, y - 1, 2, 2)) {
                listOfShapes.get(i).fillColor = currentColor;
                break;
            }
        }
        notifyObservers();
    }
}