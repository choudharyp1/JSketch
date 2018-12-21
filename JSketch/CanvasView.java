import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


class CanvasView extends JPanel implements IView {
    //A copy of the model
    private Model model;
    private int x_s;
    private int y_s;
    private double width = 1200;
    private double height = 900;

    private JPanel canvasFullSize = new JPanel(){
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            paintShapes(g2, this);
        }
    };

    private	JPanel canvasFitToScreen = new JPanel(){
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            paintShapes(g2, this);
        }
    };

    CanvasView(Model tmpmodel){
        //assign model
        this.model = tmpmodel;
        //set Layout and border of the canvas.
        this.setLayout(new CardLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //Canvas Full Size
        canvasFullSize.setBackground(Color.WHITE);
        canvasFullSize.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel canvasContainer = new JPanel(new BorderLayout());
        canvasContainer.setPreferredSize(new Dimension((int)width,(int)height));
        canvasContainer.setMaximumSize(new Dimension((int)width,(int)height));
        canvasContainer.setMinimumSize(new Dimension((int)width,(int)height));
        canvasContainer.add(canvasFullSize, BorderLayout.CENTER);

        JScrollPane scroller = new JScrollPane(canvasContainer);
        scroller.setSize(new Dimension(640,480));
        this.add(scroller, "Full Size");


        //Canvas Fit to Screen.
        canvasFitToScreen.setBackground(Color.WHITE);
        canvasFitToScreen.setPreferredSize(new Dimension((int)width,(int)height));

        JPanel fitToWindow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fitToWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                double newW = fitToWindow.getWidth() / width;
                double newH = fitToWindow.getHeight() / width;
                double scale =  Math.min(newW, newH);
                canvasFitToScreen.setPreferredSize(new Dimension((int)(width*scale), (int)(width*scale)));
                fitToWindow.revalidate();
            }
        });

        fitToWindow.setBackground(Color.LIGHT_GRAY);
        fitToWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        fitToWindow.add(canvasFitToScreen);
        this.add(fitToWindow, "Fit to Window");

        //Mouse listener for full size canvas
        canvasFullSize.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x_s = e.getX();
                y_s = e.getY();

                if(model.getCurrentTool() == tool.CIRCLE ||
                        model.getCurrentTool() == tool.RECTANGLE ||
                        model.getCurrentTool() == tool.LINE) {
                    model.addNewShape(0,0,0,0);
                }
                else if (model.getCurrentTool() == tool.SELECT){
                    model.setSelectedShape(e.getX(),e.getY());
                }
                else if (model.getCurrentTool() == tool.ERASE){
                    model.eraseSelectedShape(e.getX(),e.getY());
                }
                else if (model.getCurrentTool() == tool.FILL){
                    model.fillSelectedShape(e.getX(),e.getY());
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(model.getCurrentTool() == tool.CIRCLE ||
                        model.getCurrentTool() == tool.RECTANGLE ||
                        model.getCurrentTool() == tool.LINE) {
                    model.addShape();
                }
            }
        });
        //Mouse motion listener for full size canvas
        canvasFullSize.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if(model.getCurrentTool() == tool.CIRCLE ||
                        model.getCurrentTool() == tool.RECTANGLE ||
                        model.getCurrentTool() == tool.LINE) {
                    model.addNewShape(x_s, y_s, x, y);
                }
                else if (model.getCurrentTool() == tool.SELECT){
                    model.moveSelectedShape(x, y);
                }
            }
        });

        // Mouse listener for fit to screen canvas.
        canvasFitToScreen.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                double scaleWidth = canvasFitToScreen.getWidth() / width;
                double scaleHeight = canvasFitToScreen.getHeight() / height;

                x_s = (int) (e.getX()/scaleWidth);
                y_s = (int) (e.getY()/scaleHeight);
                int x = (int) (e.getX()/scaleWidth);
                int y =(int) (e.getY()/scaleHeight);

                if(model.getCurrentTool() == tool.CIRCLE || model.getCurrentTool() == tool.RECTANGLE || model.getCurrentTool() == tool.LINE) {
                    model.addNewShape(0,0,0,0);
                }
                else if (model.getCurrentTool() == tool.SELECT){
                    model.setSelectedShape(x,y);
                }
                else if (model.getCurrentTool() == tool.ERASE){
                    model.eraseSelectedShape(x,y);
                }
                else if (model.getCurrentTool() == tool.FILL){
                    model.fillSelectedShape(x,y);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(model.getCurrentTool() == tool.CIRCLE ||
                        model.getCurrentTool() == tool.RECTANGLE ||
                        model.getCurrentTool() == tool.LINE) {
                    model.addShape();
                }
            }
        });
        //Mouse motion listener for fit to screen.
        canvasFitToScreen.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                double scaleWidth = canvasFitToScreen.getWidth() / width;
                double scaleHeight = canvasFitToScreen.getHeight() / height;
                int x = (int) (e.getX()/scaleWidth);
                int y =(int) (e.getY()/scaleHeight);
                if(model.getCurrentTool() == tool.CIRCLE ||
                        model.getCurrentTool() == tool.RECTANGLE ||
                        model.getCurrentTool() == tool.LINE) {
                    model.addNewShape(x_s, y_s, x, y);
                }
                else if (model.getCurrentTool() == tool.SELECT){
                    model.moveSelectedShape(x, y);
                }
            }
        });
    }
    //Painting all the shapes into the canvas
    private void paintShapes(Graphics2D g2, JPanel canvas){
        double scaleW = canvas.getWidth() / width;
        double scaleH = canvas.getHeight() / height;
        double scale = Math.min(scaleW, scaleH);
        g2.scale(scale, scale);

        ArrayList <MyShape> listOfShapes = model.getListOfShapes();
        for (MyShape shape : listOfShapes){
            if (shape.fillColor != null){
                g2.setColor(shape.fillColor);
                g2.fill(shape.shape);
            }

            int curThickness = (shape.thickness + 1) * 2;
            g2.setStroke(new BasicStroke(curThickness));
            if(shape == model.getSelectedShape()) {
                float dash1[] = {10.0f};
                g2.setStroke(new BasicStroke(curThickness,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f));
            }

            g2.setColor(shape.lineColor);
            g2.draw(shape.shape);
            g2.setStroke(new BasicStroke(3));
        }
        if (model.getCurrentShape() != null){
            g2.setStroke(new BasicStroke((model.getCurrentThickness()+1)*2));
            g2.setColor(model.getCurrentColor());
            g2.draw(model.getCurrentShape().shape);
        }
    }

    public void updateView(){
        canvasFullSize.repaint();
        canvasFitToScreen.repaint();
    }

}